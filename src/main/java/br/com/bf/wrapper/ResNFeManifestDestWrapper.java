package br.com.bf.wrapper;

public class ResNFeManifestDestWrapper {

	private String chNFe;
	private TEventoManifestDestWrapper tipoEvento;
	private String xJust;

	public ResNFeManifestDestWrapper(String chNFe, TEventoManifestDestWrapper tipoEvento, String xJust) {
		super();
		this.chNFe = chNFe;
		this.tipoEvento = tipoEvento;
	}

	public String getChNFe() {
		return chNFe;
	}

	public void setChNFe(String chNFe) {
		this.chNFe = chNFe;
	}

	public TEventoManifestDestWrapper getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TEventoManifestDestWrapper tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getxJust() {
		return xJust;
	}

	public void setxJust(String xJust) {
		this.xJust = xJust;
	}

}
