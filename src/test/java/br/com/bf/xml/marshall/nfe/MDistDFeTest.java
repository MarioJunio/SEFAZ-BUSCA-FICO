package br.com.bf.xml.marshall.nfe;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import br.com.bf.mock.Mock;
import br.com.bf.wrapper.NFAmbiente;

public class MDistDFeTest {

	@Test
	public void marshall() {

		Map<String, Object> args = new HashMap<>();
		args.put(MDistDFe.VERSAO, "1.00");
		args.put(MDistDFe.TP_AMB, NFAmbiente.HOMOLOGACAO.getCodigo());
		args.put(MDistDFe.C_UF_AUTOR, "31");
		args.put(MDistDFe.CNPJ, Mock.cnpjLogado);
		args.put(MDistDFe.CPF, null);
		args.put(MDistDFe.ULT_NSU, "0");
		
		try {
			MDistDFe marshallDDFe = new MDistDFe();
			String xml = marshallDDFe.marshall(args);
			
			System.out.println(xml);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
