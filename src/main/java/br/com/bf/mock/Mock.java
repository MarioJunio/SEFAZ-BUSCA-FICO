package br.com.bf.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.bf.wrapper.NFAmbiente;
import br.com.bf.wrapper.ResNFeManifestDestWrapper;
import br.com.bf.wrapper.TEventoManifestDestWrapper;

public class Mock {

	// cnpj criado para simular o cliente que está logado, em ambiente real, deve ser implementando o spring security
	public static String cnpjLogado = "15107846000100";
	public static String ufLogado = "31";
	public static final NFAmbiente AMBIENTE = NFAmbiente.HOMOLOGACAO;

	public static String getXMLConsultaDFe() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><distDFeInt versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><tpAmb>1</tpAmb><cUFAutor>31</cUFAutor><CNPJ>15107846000100</CNPJ><distNSU><ultNSU>000000000000000</ultNSU></distNSU></distDFeInt>";
	}
	
	public static List<ResNFeManifestDestWrapper> getListaNFeManifestDest() {
		
		ResNFeManifestDestWrapper nfe1 = new ResNFeManifestDestWrapper("31170422065841000110550020000004286000005514", TEventoManifestDestWrapper.ConfirmacaoDaOperacao, null);
		ResNFeManifestDestWrapper nfe2 = new ResNFeManifestDestWrapper("31170422065841000110550020000004226000005456", TEventoManifestDestWrapper.ConfirmacaoDaOperacao, null);
		
		List<ResNFeManifestDestWrapper> listNFeManifestDest = new ArrayList<>();
		listNFeManifestDest.add(nfe1);
		listNFeManifestDest.add(nfe2);
		
		return listNFeManifestDest;
	}
}
