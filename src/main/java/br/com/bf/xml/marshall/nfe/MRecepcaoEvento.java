package br.com.bf.xml.marshall.nfe;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import br.com.bf.utils.Dates;
import br.com.bf.wrapper.ResNFeManifestDestWrapper;
import br.com.bf.wrapper.TEventoManifestDestWrapper;
import br.com.bf.xml.model.recepcaoevento.TEnvEvento;
import br.com.bf.xml.model.recepcaoevento.TEvento;
import br.com.bf.xml.model.recepcaoevento.TEvento.InfEvento;
import br.com.bf.xml.model.recepcaoevento.TEvento.InfEvento.DetEvento;

@Service
public class MRecepcaoEvento extends Marshall {

	public static final String VERSAO = "versao";
	public static final String ID_LOTE = "idLote";
	public static final String C_ORGAO = "cOrgao";
	public static final String TP_AMB = "tpAmb";
	public static final String CNPJ = "cnpj";
	public static final String LISTA_CH_NFE = "chNFe";

	@Override
	public String marshall(Map<String, ?> arguments) throws Exception {

		// pega os argumentos para gerar o XML
		String versao = (String) arguments.get(VERSAO);
		String idLote = (String) arguments.get(ID_LOTE);
		String cOrgao = (String) arguments.get(C_ORGAO);
		String tpAmb = (String) arguments.get(TP_AMB);
		String cnpj = (String) arguments.get(CNPJ);

		@SuppressWarnings("unchecked")
		List<ResNFeManifestDestWrapper> listaChNFe = (ArrayList<ResNFeManifestDestWrapper>) arguments.get(LISTA_CH_NFE);

		TEnvEvento envEvento = new TEnvEvento();
		envEvento.setVersao(versao);
		envEvento.setIdLote(idLote);

		for (ResNFeManifestDestWrapper chNFe : listaChNFe) {

			DetEvento detEvento = new DetEvento();
			detEvento.setVersao(versao);

			// verifica qual o tipo de evento
			if (chNFe.getTipoEvento() == TEventoManifestDestWrapper.ConfirmacaoDaOperacao)
				detEvento.setDescEvento(TEventoManifestDestWrapper.ConfirmacaoDaOperacao.getDescricao());
			else if (chNFe.getTipoEvento() == TEventoManifestDestWrapper.CienciaDaOperacao)
				detEvento.setDescEvento(TEventoManifestDestWrapper.CienciaDaOperacao.getDescricao());
			else if (chNFe.getTipoEvento() == TEventoManifestDestWrapper.DesconhecimentoDaOperacao)
				detEvento.setDescEvento(TEventoManifestDestWrapper.DesconhecimentoDaOperacao.getDescricao());
			else if (chNFe.getTipoEvento() == TEventoManifestDestWrapper.OperacaoNaoRealizada)
				detEvento.setDescEvento(TEventoManifestDestWrapper.OperacaoNaoRealizada.getDescricao());
			else
				throw new Exception("Operação: " + chNFe.getTipoEvento().getDescricao() + " não é valida");

			// verifica se a justificativa foi informada em evento: Operacao nao Realizada
			if (chNFe.getTipoEvento() == TEventoManifestDestWrapper.OperacaoNaoRealizada) {

				if (!StringUtils.isEmpty(chNFe.getxJust()))
					detEvento.setXJust(chNFe.getxJust());
				else
					throw new Exception("Para operação não realizada, deve ser informado a justificativa");
			}

			InfEvento infEvento = new InfEvento();

			// '91' para ambiente nacional
			infEvento.setCOrgao(cOrgao);
			infEvento.setTpAmb(tpAmb);
			infEvento.setCNPJ(cnpj);
			infEvento.setChNFe(chNFe.getChNFe());
			infEvento.setDhEvento(Dates.formatDateUTC(new Date()));
			infEvento.setTpEvento(chNFe.getTipoEvento().getCodigo());
			infEvento.setNSeqEvento("1");
			infEvento.setVerEvento(versao);
			infEvento.setId("ID" + infEvento.getTpEvento() + infEvento.getChNFe() + "0" + infEvento.getNSeqEvento());
			infEvento.setDetEvento(detEvento);

			TEvento evento = new TEvento();
			evento.setVersao(versao);
			evento.setInfEvento(infEvento);

			envEvento.getEvento().add(evento);
		}

		JAXBContext jaxbContext = JAXBContext.newInstance("br.com.bf.xml.model.recepcaoevento");
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(envEvento, sw);

		return prepare(sw.toString());
	}

}
