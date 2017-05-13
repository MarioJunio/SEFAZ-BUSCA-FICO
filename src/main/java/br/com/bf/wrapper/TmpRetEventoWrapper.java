package br.com.bf.wrapper;

import br.com.bf.entity.TipoEvento;

public class TmpRetEventoWrapper {

	private Long id;
	private TipoEvento tipoEvento;
	private String retXml;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getRetXml() {
		return retXml;
	}

	public void setRetXml(String retXml) {
		this.retXml = retXml;
	}

}
