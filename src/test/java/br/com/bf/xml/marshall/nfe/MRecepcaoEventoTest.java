package br.com.bf.xml.marshall.nfe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.bf.wrapper.NFAmbiente;
import br.com.bf.wrapper.ResNFeManifestDestWrapper;
import br.com.bf.wrapper.TEventoManifestDestWrapper;

public class MRecepcaoEventoTest {

	@Test
	public void marshall() {

		Map<String, Object> args = new HashMap<>();
		args.put(MRecepcaoEvento.VERSAO, "1.00");
		args.put(MRecepcaoEvento.ID_LOTE, "1");
		args.put(MRecepcaoEvento.CNPJ, "15107846000100");
		args.put(MRecepcaoEvento.TP_AMB, NFAmbiente.HOMOLOGACAO.getCodigo());
		args.put(MRecepcaoEvento.C_ORGAO, "91");
		
		ResNFeManifestDestWrapper nfe1 = new ResNFeManifestDestWrapper("31170422065841000110550020000004286000005514", TEventoManifestDestWrapper.ConfirmacaoDaOperacao, null);
		ResNFeManifestDestWrapper nfe2 = new ResNFeManifestDestWrapper("31170422065841000110550020000004226000005456", TEventoManifestDestWrapper.ConfirmacaoDaOperacao, null);
		
		List<ResNFeManifestDestWrapper> listNFeManifestDest = new ArrayList<>();
		listNFeManifestDest.add(nfe1);
		listNFeManifestDest.add(nfe2);
		
		args.put(MRecepcaoEvento.LISTA_CH_NFE, listNFeManifestDest);
		
		try {
			MRecepcaoEvento re = new MRecepcaoEvento();
			System.out.println(re.marshall(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
