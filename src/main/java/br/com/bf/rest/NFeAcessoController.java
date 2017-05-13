package br.com.bf.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bf.entity.Empresa;
import br.com.bf.entity.MOSTmpAcesso;
import br.com.bf.entity.MosTmpEvento;
import br.com.bf.entity.TipoEvento;
import br.com.bf.entity.model.UF;
import br.com.bf.mock.Mock;
import br.com.bf.repository.EmpresaRepository;
import br.com.bf.repository.MOSTmpAcessoRepository;
import br.com.bf.repository.MOSTmpEventoRepository;
import br.com.bf.wrapper.NFAmbiente;
import br.com.bf.wrapper.ResNFeManifestDestWrapper;
import br.com.bf.wrapper.TEventoManifestDestWrapper;
import br.com.bf.xml.marshall.nfe.MDistDFe;
import br.com.bf.xml.marshall.nfe.MRecepcaoEvento;

@Controller
@RequestMapping("/acesso/nfe")
public class NFeAcessoController extends GenericController {

	@Autowired
	private MOSTmpAcessoRepository tmpAcessoRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private MOSTmpEventoRepository mosTmpEventoRepository;

	@Autowired
	private MDistDFe mDistDFe;

	@Autowired
	private MRecepcaoEvento mRecepcaoEvento;

	private NFAmbiente ambiente = NFAmbiente.HOMOLOGACAO;

	@PostMapping("/consulta/dfe")
	public ResponseEntity<String> acessoDistDFe() throws JAXBException {
		return acesso(Mock.cnpjLogado, TipoEvento.NFE_DIST_DFE, getXmlDistDFe());
	}

	@PostMapping("/manifesto-dest/{chavesNFe}")
	public ResponseEntity<String> acessoManifestoDestinatario(@PathVariable("chavesNFe") List<String> chavesNFe) throws Exception {
		return acesso(Mock.cnpjLogado, TipoEvento.NFE_MANIFESTO_DESTINATARIO, getXmlManifestoDest(chavesNFe));
	}

	/**
	 * Permite o acesso á diferentes eventos, mas terá de ser revisto ao implementar o login com Spring Security
	 * 
	 * @param cnpj
	 * @param tipoEvento
	 * @param xml
	 * @return Resposta HTTP
	 */
	private ResponseEntity<String> acesso(String cnpj, TipoEvento tipoEvento, String xml) {

		ResponseEntity<String> response;

		// busca a empresa por cnpj
		Empresa empresaLogada = empresaRepository.buscarPorCNPJ(cnpj);

		// se a empresa existe, então crie uma chave de acesso para ela usar o MOS
		if (empresaLogada != null) {

			// antes de gerar o CA é necessário verificar se não é uma tentativa de invasão
			if (!validarGeraCA()) {
				response = new ResponseEntity<String>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			} else {

				// gera uma nova chave de acesso
				String newCA = criarCA();

				MOSTmpAcesso tmpAcesso = new MOSTmpAcesso();
				tmpAcesso.setCa(newCA);
				tmpAcesso.setEmpresa(empresaLogada);
				tmpAcesso.setData(Calendar.getInstance().getTime());
				tmpAcesso.setAmbiente(Mock.AMBIENTE);

				// salva o acesso para essa empresa
				tmpAcessoRepository.save(tmpAcesso);

				// cria xml de requisição para a consulta de distribuição DFe
				MosTmpEvento mosTmpEvento = new MosTmpEvento();
				mosTmpEvento.setMosAcesso(tmpAcesso);
				mosTmpEvento.setCnpj(cnpj);
				mosTmpEvento.setEvento(tipoEvento);
				mosTmpEvento.setXmlRequisicao(xml);
				mosTmpEvento.setData(Calendar.getInstance().getTime());

				// salva evento de consulta dfe
				mosTmpEventoRepository.save(mosTmpEvento);

				response = new ResponseEntity<String>(newCA, HttpStatus.OK);
			}

		} else {
			response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}

		return response;

	}

	/**
	 * Cria XML para o evento da NFe - distruição DFe
	 * 
	 * @return XML para requisição
	 * @throws JAXBException
	 */
	private String getXmlDistDFe() throws JAXBException {

		Map<String, Object> args = new HashMap<>();
		args.put(MDistDFe.VERSAO, "1.00");
		args.put(MDistDFe.TP_AMB, ambiente.getCodigo());
		args.put(MDistDFe.C_UF_AUTOR, Mock.ufLogado);
		args.put(MDistDFe.CNPJ, Mock.cnpjLogado);
		args.put(MDistDFe.CPF, null);
		args.put(MDistDFe.ULT_NSU, "0");

		return mDistDFe.marshall(args);
	}

	/**
	 * Cria o XML para o evento da NFe - RecepcaoEvento (Manifesto do Destinatário)
	 * 
	 * @return XML para requisição
	 * @throws Exception
	 */
	private String getXmlManifestoDest(List<String> chavesNFe) throws Exception {

		Map<String, Object> args = new HashMap<>();
		args.put(MRecepcaoEvento.VERSAO, "1.00");
		args.put(MRecepcaoEvento.ID_LOTE, "1");
		args.put(MRecepcaoEvento.CNPJ, Mock.cnpjLogado);
		args.put(MRecepcaoEvento.TP_AMB, ambiente.getCodigo());

		// Sempre será utilizando 91 - Ambiente Nacional
		// Porque sempre é consultado a distribuição DFe do ambiente nacional e as NFe são manifestadas de acordo com os resultados dessa
		// consulta
		// Caso no futuro mude, será utilizado o código do estado do usuário, caso não exista utilizar ambiente nacional
		args.put(MRecepcaoEvento.C_ORGAO, UF.AN.CODIGO);

		List<ResNFeManifestDestWrapper> listaChavesNFeManifestar = new ArrayList<>();

		chavesNFe.forEach(chNFe -> {
			listaChavesNFeManifestar.add(new ResNFeManifestDestWrapper(chNFe, TEventoManifestDestWrapper.ConfirmacaoDaOperacao, null));
		});

		// MOCK da lista de NFe para testes, deverá ser removido após os testes
		args.put(MRecepcaoEvento.LISTA_CH_NFE, listaChavesNFeManifestar);

		return mRecepcaoEvento.marshall(args);

	}

}
