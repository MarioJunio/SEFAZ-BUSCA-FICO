package br.com.bf.xml.unmarshaller.nfe;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;

import br.com.bf.xml.model.distdfe.ResNFe;
import br.com.bf.xml.model.distdfe.RetDistDFeInt;
import br.com.bf.xml.model.distdfe.TNfeProc;

@Service
public class UMDistDFe {

	public RetDistDFeInt parseDistDFe(String xml) throws JAXBException {

		xml = xml.replace("xmlns=\"http://www.portalfiscal.inf.br/nfe\"", "");

		// cria contexto do JAXB, define o pacote onde está o factory do bean que representa o xml de resposta
		JAXBContext context = JAXBContext.newInstance(RetDistDFeInt.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// realizar o unmarshaller, ou seja, transforma xml em um bean
		RetDistDFeInt retDistDFeInt = (RetDistDFeInt) unmarshaller.unmarshal(new StringReader(xml));

		// retorna o bean de resposta
		return retDistDFeInt;
	}
	
	public TNfeProc parseProcNFe(String xml) throws JAXBException {

		// cria contexto do JAXB, define o pacote onde está o factory do bean que representa o xml de resposta
		JAXBContext context = JAXBContext.newInstance("br.com.bf.xml.model.distdfe");
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// realizar o unmarshaller, ou seja, transforma xml em um bean
		@SuppressWarnings("unchecked")
		JAXBElement<TNfeProc> jaxbNFeProc = (JAXBElement<TNfeProc>) unmarshaller.unmarshal(new StringReader(xml));

		// retorna o bean de resposta
		return jaxbNFeProc.getValue();
	}
	
	public ResNFe parseResNFe(String xml) throws JAXBException {

		// cria contexto do JAXB, define o pacote onde está o factory do bean que representa o xml de resposta
		JAXBContext context = JAXBContext.newInstance("br.com.bf.xml.model.distdfe");
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// realizar o unmarshaller, ou seja, transforma xml em um bean
		ResNFe resNFe = (ResNFe) unmarshaller.unmarshal(new StringReader(xml));

		// retorna o bean de resposta
		return resNFe;
	}
	
}
