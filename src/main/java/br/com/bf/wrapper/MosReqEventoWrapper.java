package br.com.bf.wrapper;

import br.com.bf.entity.TipoEvento;

public class MosReqEventoWrapper {

	private Long id;
	private TipoEvento evento;
	private String xml;

	public MosReqEventoWrapper(Long id, String xml, TipoEvento eventoXml) {
		super();
		this.id = id;
		this.xml = xml;
		this.evento = eventoXml;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public TipoEvento getEvento() {
		return evento;
	}

	public void setEvento(TipoEvento evento) {
		this.evento = evento;
	}

}
