package br.com.bf.rest;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bf.entity.Empresa;
import br.com.bf.entity.MOSConfiguracoes;
import br.com.bf.entity.MOSTmpAcesso;
import br.com.bf.mock.Mock;
import br.com.bf.repository.EmpresaRepository;
import br.com.bf.repository.MOSConfiguracoesRepository;
import br.com.bf.repository.MOSTmpAcessoRepository;
import br.com.bf.repository.MOSTmpEventoRepository;
import br.com.bf.utils.CAUtils;
import br.com.bf.wrapper.MosConfiguracaoWrapper;
import br.com.bf.wrapper.MosReqEventoWrapper;
import br.com.bf.wrapper.MosRetEventosWrapper;
import br.com.bf.wrapper.NFAmbiente;
import br.com.bf.wrapper.TmpRetEventoWrapper;

@RestController
@RequestMapping("/mos")
public class MOSController extends GenericController {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private MOSTmpAcessoRepository mosTmpAcessoRepository;

	@Autowired
	private MOSConfiguracoesRepository mosConfigRepository;

	@Autowired
	private MOSTmpEventoRepository mosTmpEventoRepository;

	@PostMapping("/configurar")
	private ResponseEntity<String> configurar() {

		ResponseEntity<String> response;
		String cnpjLogado = Mock.cnpjLogado;

		Empresa empresa = empresaRepository.buscarPorCNPJ(cnpjLogado);

		if (empresa != null) {

			// TODO: verificar se pode criar o CA

			String ca = criarCA();

			MOSTmpAcesso mosTmpAcesso = new MOSTmpAcesso();
			mosTmpAcesso.setCa(ca);
			mosTmpAcesso.setEmpresa(empresa);
			mosTmpAcesso.setData(Calendar.getInstance().getTime());
			mosTmpAcesso.setAmbiente(NFAmbiente.HOMOLOGACAO);

			mosTmpAcessoRepository.save(mosTmpAcesso);

			response = new ResponseEntity<>(ca, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}

		return response;
	}

	@GetMapping("/configuracoes/{ca}")
	public ResponseEntity<MosConfiguracaoWrapper> getConfiguracoes(@PathVariable("ca") String ca) {

		ResponseEntity<MosConfiguracaoWrapper> response;

		if (!CAUtils.isCAValid(ca)) {
			response = new ResponseEntity<MosConfiguracaoWrapper>(HttpStatus.NOT_ACCEPTABLE);
		} else {

			MosConfiguracaoWrapper config = this.mosTmpAcessoRepository.buscarConfiguracao(ca);

			if (config == null) {
				response = new ResponseEntity<MosConfiguracaoWrapper>(HttpStatus.NOT_FOUND);
			} else {
				response = new ResponseEntity<MosConfiguracaoWrapper>(this.mosTmpAcessoRepository.buscarConfiguracao(ca), HttpStatus.OK);
			}

		}

		return response;
	}

	@PostMapping("/salvar")
	public ResponseEntity<Void> salvarConfiguracoes(@RequestParam("id") Long id, @RequestParam("ca") String ca, @RequestParam("cnpj") String cnpj,
			@RequestParam("certificado") String certificado) {

		ResponseEntity<Void> response;

		if (!CAUtils.isCAValid(ca)) {
			response = new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
		} else {

			// busca empresa por CA e CNPJ
			Empresa empresa = mosTmpAcessoRepository.buscarPorCA(ca, cnpj);

			// verifica se a empresa associada a essa chave de acesso, não foi encontrada, se não foi então o status da resposta deve ser
			// FORBIDDEN
			if (empresa == null) {
				response = new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
			} else {

				MOSConfiguracoes configuracoes = new MOSConfiguracoes();

				configuracoes.setId(id);
				configuracoes.setEmpresa(empresa);
				configuracoes.setCertificado(certificado);
				configuracoes.setSetup(!StringUtils.isEmpty(certificado));

				mosConfigRepository.save(configuracoes);

				response = new ResponseEntity<Void>(HttpStatus.OK);

			}
		}

		return response;
	}

	@GetMapping("/eventos/{ca}/{cnpj}")
	public ResponseEntity<List<MosReqEventoWrapper>> getEventosRequisicao(@PathVariable("ca") String ca, @PathVariable("cnpj") String cnpj) {

		ResponseEntity<List<MosReqEventoWrapper>> response;

		// verifica se a CA recebida é valida
		if (!CAUtils.isCAValid(ca)) {
			response = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} else {
			response = new ResponseEntity<>(mosTmpEventoRepository.getEventosRequisicao(ca, cnpj), HttpStatus.OK);
		}

		return response;
	}

	@PostMapping(value = "/eventos/", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<String> postEventosRetorno(@RequestBody MosRetEventosWrapper retornoEventos) {

		ResponseEntity<String> response;

		// verifica se o parâmetro da requisição retorno, é valido
		if (retornoEventos != null) {

			if (!CAUtils.isCAValid(retornoEventos.getCa())) {
				response = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			} else {

				String ca = retornoEventos.getCa();
				String cnpj = retornoEventos.getCnpj();

				// verifica se o CA é valido para o CNPJ
				if (mosTmpAcessoRepository.buscarPorCAeCNPJ(ca, cnpj)) {

					Collection<TmpRetEventoWrapper> eventos = retornoEventos.getEventos();

					// atualiza todos os eventos retornados
					eventos.forEach(evento -> {
						mosTmpEventoRepository.atualizarEvento(ca, cnpj, evento.getId(), evento.getRetXml());
					});

					response = new ResponseEntity<>(HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}

			}

		} else {
			response = new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}

		return response;
	}
}
