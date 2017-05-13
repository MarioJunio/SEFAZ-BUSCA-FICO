package br.com.bf.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.bf.entity.MosTmpEvento;
import br.com.bf.entity.TipoEvento;
import br.com.bf.repository.MOSTmpEventoRepository;
import br.com.bf.utils.GzipUtils;
import br.com.bf.view.holder.ConsDistDFeHolder;
import br.com.bf.view.holder.ConsDistDFeHolder.SituacaoNFe;
import br.com.bf.view.holder.ConsDistDFeHolder.TipoNF;
import br.com.bf.view.holder.RetNFeManifestoDest;
import br.com.bf.xml.model.distdfe.ResNFe;
import br.com.bf.xml.model.distdfe.RetDistDFeInt;
import br.com.bf.xml.model.distdfe.RetDistDFeInt.LoteDistDFeInt.DocZip;
import br.com.bf.xml.model.distdfe.TNfeProc;
import br.com.bf.xml.model.recepcaoevento.TRetEnvEvento;
import br.com.bf.xml.unmarshaller.nfe.UMDistDFe;
import br.com.bf.xml.unmarshaller.nfe.UMRecepcaoEvento;

@Controller
@RequestMapping("/nfe")
public class NFeController {

	private final int TENTATIVAS = 120;
	private final long DELAY = 500l;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

	@Autowired
	private MOSTmpEventoRepository tmpEventoRepository;

	@Autowired
	private UMDistDFe unmarshallDistDFe;

	@Autowired
	private UMRecepcaoEvento unmarshallRecepEvent;

	@GetMapping()
	public ModelAndView paginaConsultaDFe() {
		return new ModelAndView("index");
	}

	/**
	 * verifica se o retorno do MOS foi postado
	 * 
	 * @param ca
	 * @return
	 */
	@GetMapping("/checar/dfe/{ca}")
	public ResponseEntity<Boolean> checarConsultaDFe(@PathVariable("ca") String ca) {

		Boolean response = false;

		if (!StringUtils.isEmpty(ca)) {
			response = waitEventosRetornados(ca);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Controlador criado apenas para teste, deve ser removido ao entrar em produção
	 * 
	 * @param ca
	 * @return
	 */
	@GetMapping("/consulta/dfe/homo/{ca}")
	public ModelAndView consultarDFeHomo(@PathVariable("ca") String ca) {

		ModelAndView mv = new ModelAndView("consulta-nfe-dfe");

		List<ConsDistDFeHolder> listConsDistDFeHolder = new ArrayList<>();

		ConsDistDFeHolder holder1 = new ConsDistDFeHolder();
		holder1.setResumo(true);
		holder1.setChNFe("dasdada43243dsada");
		holder1.setNomeEmitente("Jose Silva Antonio");
		holder1.setValor(new BigDecimal("533.89"));
		holder1.setTipoNf(TipoNF.SAIDA);
		holder1.setSituacaoNFe(SituacaoNFe.AUTORIZADO);
		holder1.setDataEmissao(new Date());
		holder1.setDataRecebimento(new Date());

		ConsDistDFeHolder holder2 = new ConsDistDFeHolder();
		holder2.setResumo(false);
		holder2.setChNFe("432432432432432vvfdfsdfdsfds");
		holder2.setNomeEmitente("Gabriela Pereira Angolana");
		holder2.setValor(new BigDecimal("178.15"));
		holder2.setTipoNf(TipoNF.ENTRADA);
		holder2.setSituacaoNFe(SituacaoNFe.DENEGADO);
		holder2.setDataEmissao(new Date());
		holder2.setDataRecebimento(new Date());

		listConsDistDFeHolder.add(holder1);
		listConsDistDFeHolder.add(holder2);

		mv.addObject("listaNFe", listConsDistDFeHolder);

		return mv;
	}

	/**
	 * Redireciona para a página de consulta distribuição DFe
	 * 
	 * @param ca
	 * @return
	 */
	@GetMapping("/consulta/dfe/{ca}")
	public ModelAndView consultarDFe(@PathVariable("ca") String ca) {

		ModelAndView mv = new ModelAndView("consulta-nfe-dfe");
		Map<String, ConsDistDFeHolder> mapDistDFe = new HashMap<>();
		// List<ConsDistDFeHolder> listConsDistDFeHolder = new ArrayList<>();

		// busca os eventos retornados
		List<MosTmpEvento> eventosRetornados = getEventosRetornados(ca);

		System.out.println("Eventos Retornados: " + eventosRetornados.size());

		// TODO: processar os eventos retornados para exibir ao usuário
		eventosRetornados.forEach(mosTmpEvento -> {

			try {
				RetDistDFeInt retDistDFeInt = unmarshallDistDFe.parseDistDFe(mosTmpEvento.getXmlRetorno());

				if (retDistDFeInt.getCStat().equals("138")) {

					// System.out.println(String.format("Status: %s\nMotivo: %s", retDistDFeInt.getCStat(), retDistDFeInt.getXMotivo()));
					// System.out.println(String.format("Ult, Max NSU %s, %s", retDistDFeInt.getUltNSU(), retDistDFeInt.getMaxNSU()));

					for (DocZip lote : retDistDFeInt.getLoteDistDFeInt().getDocZip()) {

						String xml = GzipUtils.decompress(lote.getValue());
						// System.out.println(String.format("NSU %s, Schema %s, Value %s", lote.getNSU(), lote.getSchema(), xml));

						ConsDistDFeHolder holder = new ConsDistDFeHolder();

						if (lote.getSchema().equalsIgnoreCase("resNFe_v1.00.xsd")) {

							ResNFe resNFe = unmarshallDistDFe.parseResNFe(xml);
							holder.setResumo(true);
							holder.setChNFe(resNFe.getChNFe());
							holder.setNomeEmitente(resNFe.getXNome());
							holder.setDataEmissao(getFormatedDate(resNFe.getDhEmi()));
							holder.setTipoNf(resNFe.getTpNF().equals(String.valueOf(TipoNF.ENTRADA.ordinal())) ? TipoNF.ENTRADA : TipoNF.SAIDA);
							holder.setValor(new BigDecimal(resNFe.getVNF()));
							holder.setDataRecebimento(getFormatedDate(resNFe.getDhRecbto()));
							holder.setSituacaoNFe(resNFe.getCSitNFe().equals("1") ? SituacaoNFe.AUTORIZADO : SituacaoNFe.DENEGADO);

						} else if (lote.getSchema().equalsIgnoreCase("procNFe_v3.10.xsd")) {

							TNfeProc parseProcNFe = unmarshallDistDFe.parseProcNFe(xml);

							holder.setResumo(false);
							holder.setChNFe(parseProcNFe.getProtNFe().getInfProt().getChNFe());
							holder.setNomeEmitente(parseProcNFe.getNFe().getInfNFe().getEmit().getXNome());
							holder.setDataEmissao(getFormatedDate(parseProcNFe.getNFe().getInfNFe().getIde().getDhEmi()));
							holder.setTipoNf(parseProcNFe.getNFe().getInfNFe().getIde().getTpNF().equals(String.valueOf(TipoNF.ENTRADA.ordinal()))
									? TipoNF.ENTRADA : TipoNF.SAIDA);
							holder.setValor(new BigDecimal(parseProcNFe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()));
							holder.setDataRecebimento(getFormatedDate(parseProcNFe.getProtNFe().getInfProt().getDhRecbto()));

							if (parseProcNFe.getProtNFe().getInfProt().getCStat().equals("100"))
								holder.setSituacaoNFe(SituacaoNFe.AUTORIZADO);
							else if (parseProcNFe.getProtNFe().getInfProt().getCStat().equals("110"))
								holder.setSituacaoNFe(SituacaoNFe.DENEGADO);

						}

						// adiciona holder da NFe
						Optional<ConsDistDFeHolder> optDistDFeHolder = Optional.ofNullable(mapDistDFe.get(holder.getChNFe()));

						// se encontrar a NFe mapeada para sua chave, então é necessário verificar é um XML resumo ou completo da NFe
						// caso contrário adicione o XML no mapa
						if (optDistDFeHolder.isPresent()) {

							// NFe encontrada
							ConsDistDFeHolder distDFe = optDistDFeHolder.get();

							// se a NFe ja incluida for um resumo, e a NFe a ser adicionado não for resumo, então deve substituir o elemento
							// caso contrario não adicionar pois já foi adicionada a NFe completa
							if (distDFe.isResumo() && !holder.isResumo()) {
								mapDistDFe.put(holder.getChNFe(), holder);
							}

						} else {
							mapDistDFe.put(holder.getChNFe(), holder);
						}

					}

				} else {
					System.out.println("Não foi localizado nenhum documento.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		});

		mv.addObject("listaNFe", mapDistDFe.values());

		return mv;
	}

	/**
	 * Processa os eventos de retorno da manifestação do destinatário
	 * 
	 * @param ca
	 * @return
	 */
	@GetMapping("/manifesto-dest/{ca}")
	public ResponseEntity<List<RetNFeManifestoDest>> manifestoDestinatario(@PathVariable("ca") String ca) {

		ResponseEntity<List<RetNFeManifestoDest>> response = null;
		List<RetNFeManifestoDest> listRetNFeMd = new ArrayList<>();

		for (MosTmpEvento tmpEvento : getEventosRetornados(ca)) {

			// Evento - Manifesto Destinatário
			if (tmpEvento.getEvento() == TipoEvento.NFE_MANIFESTO_DESTINATARIO) {

				try {

					TRetEnvEvento retEnvEvento = unmarshallRecepEvent.parseRecepcaoEvento(tmpEvento.getXmlRetorno());

					if (!retEnvEvento.getCStat().equals("128")) {
						response = new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
					} else {

						retEnvEvento.getRetEvento().forEach(ret -> {

							RetNFeManifestoDest retNFe = new RetNFeManifestoDest();
							retNFe.setChNFe(ret.getInfEvento().getChNFe());

							if (ret.getInfEvento().getCStat().equals("135") || ret.getInfEvento().getCStat().equals("136")) {
								retNFe.setStatus(ret.getInfEvento().getXMotivo().replace("Evento", "Manifesto"));
								retNFe.setTipo(1);
							} else {
								retNFe.setStatus(ret.getInfEvento().getXMotivo());
								retNFe.setTipo(2);
							}

							listRetNFeMd.add(retNFe);

						});
						
						response = new ResponseEntity<>(listRetNFeMd, HttpStatus.OK);
					}

				} catch (JAXBException e) {
					e.printStackTrace();
				}

			}

		}

		return response;
	}

	private Date getFormatedDate(String date) throws ParseException {
		return StringUtils.isEmpty(date) ? null : dateFormat.parse(date);
	}

	private List<MosTmpEvento> getEventosRetornados(String ca) {

		List<MosTmpEvento> eventosRetornados = null;

		eventosRetornados = tmpEventoRepository.getEventosRetornadosByCA(ca);
		int tentativas = 1;

		// se encontrou os eventos foram encontrados, ou seja atingiu o número de tentativas então termine
		// o tempo gasto de espero é o número de tentativas x tempo de espera(delay)
		while (eventosRetornados.isEmpty() && tentativas < TENTATIVAS) {

			if (tentativas > 1)
				eventosRetornados = tmpEventoRepository.getEventosRetornadosByCA(ca);

			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			tentativas++;
		}

		return eventosRetornados;
	}

	private boolean waitEventosRetornados(String ca) {

		Long quantidadeEventosRetornados = tmpEventoRepository.countEventosRetornadosPorCA(ca);
		int tentativas = 1;

		while (quantidadeEventosRetornados == 0 && tentativas < TENTATIVAS) {

			try {
				Thread.sleep(DELAY);
				quantidadeEventosRetornados = tmpEventoRepository.countEventosRetornadosPorCA(ca);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			tentativas++;
		}

		return quantidadeEventosRetornados > 0;

	}

}
