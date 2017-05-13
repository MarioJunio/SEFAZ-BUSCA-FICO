package br.com.bf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mos_tmp_evento")
public class MosTmpEvento implements Serializable {

	private static final long serialVersionUID = -6036003340377703220L;

	private Long id;
	private MOSTmpAcesso mosAcesso;
	private String cnpj;
	private TipoEvento evento;
	private String xmlRequisicao;
	private String xmlRetorno;
	private Date data;
	private boolean concluido;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "mos_acesso")
	public MOSTmpAcesso getMosAcesso() {
		return mosAcesso;
	}

	public void setMosAcesso(MOSTmpAcesso mosAcesso) {
		this.mosAcesso = mosAcesso;
	}

	@Column(length = 15)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_evento")
	public TipoEvento getEvento() {
		return evento;
	}

	public void setEvento(TipoEvento evento) {
		this.evento = evento;
	}

	@Lob
	@Column(name = "xml_requisicao")
	public String getXmlRequisicao() {
		return xmlRequisicao;
	}

	public void setXmlRequisicao(String xmlRequisicao) {
		this.xmlRequisicao = xmlRequisicao;
	}

	@Lob
	@Column(name = "xml_retorno")
	public String getXmlRetorno() {
		return xmlRetorno;
	}

	public void setXmlRetorno(String xmlRetorno) {
		this.xmlRetorno = xmlRetorno;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column()
	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	
	
	@Override
	public String toString() {
		return "MosTmpEvento [id=" + id + ", mosAcesso=" + mosAcesso.getCa() + ", cnpj=" + cnpj + ", evento=" + evento + ", xmlRequisicao=" + xmlRequisicao
				+ ", xmlRetorno=" + xmlRetorno + ", data=" + data + ", concluido=" + concluido + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MosTmpEvento other = (MosTmpEvento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
