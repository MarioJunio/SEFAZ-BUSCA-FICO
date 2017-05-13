package br.com.bf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.bf.entity.Empresa;
import br.com.bf.entity.MOSConfiguracoes;
import br.com.bf.entity.MOSTmpAcesso;
import br.com.bf.wrapper.MosConfiguracaoWrapper;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MOSConfiguracoesTest {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private MOSConfiguracoesRepository mosConfigRepository;

	@Autowired
	private MOSTmpAcessoRepository mosTmpAcessoRepository;

	public void salvarConfiguracoesTest(String ca) {

		String cnpj = "05941339000106";

		Empresa empresa = new Empresa();
		empresa.setCnpj(cnpj);
		empresa.setNomeFantasia("Rossana Aviamentos");
		empresa.setRazaoSocial("Nadir Marques Martins - ME");
		empresa.setFone("3438422804");
		empresa.setSenha("teste");

		empresa = empresaRepository.save(empresa);

		MOSConfiguracoes mosConfig = new MOSConfiguracoes();
		mosConfig.setEmpresa(empresa);
		mosConfig.setCertificado("Mario Junio Marques Martins - ME:32432423423342");
		mosConfig.setSetup(true);

		mosConfigRepository.save(mosConfig);

		MOSTmpAcesso mosTmpAcesso = new MOSTmpAcesso();
		mosTmpAcesso.setCa(ca);
		mosTmpAcesso.setEmpresa(empresa);

		mosTmpAcesso = mosTmpAcessoRepository.save(mosTmpAcesso);
	}

	@Test()
	public void buscarConfiguracoesTest() {

		String ca = "abc456";

		salvarConfiguracoesTest(ca);
		
		MosConfiguracaoWrapper mosConfig = mosTmpAcessoRepository.buscarConfiguracao(ca);

		if (mosConfig != null)
			System.out.printf("\n\nCNPJ: %s\nCertificado: %s\nSetup: %s\n\n", mosConfig.getCnpj(), mosConfig.getCertificado(),
					mosConfig.isSetup() ? "sim" : "nao");
		else
			System.out.println("----------- Nao foi encontrado a configuração para o token: " + ca);
	}

}
