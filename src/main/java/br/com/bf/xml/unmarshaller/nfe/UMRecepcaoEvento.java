package br.com.bf.xml.unmarshaller.nfe;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;

import br.com.bf.xml.model.recepcaoevento.TRetEnvEvento;

@Service
public class UMRecepcaoEvento {

	public TRetEnvEvento parseRecepcaoEvento(String xmlRetorno) throws JAXBException {

		xmlRetorno = xmlRetorno.replaceAll(" xmlns=\"http://www.portalfiscal.inf.br/nfe\"", "");

		// cria contexto do JAXB, define o pacote onde está o factory do bean que representa o xml de resposta
		JAXBContext context = JAXBContext.newInstance("br.com.bf.xml.model.recepcaoevento");
		Unmarshaller unmarshaller = context.createUnmarshaller();

		TRetEnvEvento retEnvEvento = (TRetEnvEvento) unmarshaller.unmarshal(new StringReader(xmlRetorno));

		// retorna o bean de resposta
		return retEnvEvento;
	}

}
