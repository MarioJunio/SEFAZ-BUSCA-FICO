package br.com.bf.xml.marshall.nfe;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.bf.xml.model.distdfe.DistDFeInt;
import br.com.bf.xml.model.distdfe.DistDFeInt.DistNSU;

@Service
public class MDistDFe extends Marshall {

	public static final String VERSAO = "versao";
	public static final String TP_AMB = "tpAmb";
	public static final String C_UF_AUTOR = "cUFAutor";
	public static final String CNPJ = "cnpj";
	public static final String CPF = "cpf";
	public static final String ULT_NSU = "ultNSU";

	@Override
	public String marshall(Map<String, ?> arguments) throws JAXBException {
		// TODO: Implementar aqui, pois está usando o xml do MOCK

		// pega os argumentos para gerar o XML
		String versao = (String) arguments.get(VERSAO);
		String tpAmb = (String) arguments.get(TP_AMB);
		String cUFAutor = (String) arguments.get(C_UF_AUTOR);
		String cnpj = (String) arguments.get(CNPJ);
		String cpf = (String) arguments.get(CPF);
		String ultNSU = (String) arguments.get(ULT_NSU);

		DistDFeInt distDFeInt = new DistDFeInt();
		distDFeInt.setVersao(versao);
		distDFeInt.setTpAmb(tpAmb);
		distDFeInt.setCUFAutor(cUFAutor);
		distDFeInt.setCNPJ(cnpj);
		distDFeInt.setCPF(cpf);

		DistNSU distNSU = new DistNSU();
		distNSU.setUltNSU(formatUltNSU(ultNSU));

		distDFeInt.setDistNSU(distNSU);

		JAXBContext jaxbContext = JAXBContext.newInstance("br.com.bf.xml.model.distdfe");
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(distDFeInt, sw);

		return prepare(sw.toString());
	}

	private String formatUltNSU(String ultNSU) {

		final int LENGTH_NSU = 15;
		final String AUTO_COMPL_NSU = "0";

		if (!StringUtils.isEmpty(ultNSU) && ultNSU.length() <= LENGTH_NSU) {

			int leftChars = LENGTH_NSU - ultNSU.length();

			for (int i = 0; i < leftChars; i++) {
				ultNSU = AUTO_COMPL_NSU + ultNSU;
			}

			return ultNSU;

		} else
			throw new IllegalStateException("Último NSU recebido pelo ator está em branco, ou excede " + LENGTH_NSU + " caracteres");

	}

}
