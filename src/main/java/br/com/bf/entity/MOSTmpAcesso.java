package br.com.bf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.bf.wrapper.NFAmbiente;

@Entity
@Table(name = "mos_tmp_acesso")
public class MOSTmpAcesso implements Serializable {

	private static final long serialVersionUID = -7706792280009983573L;

	private String ca;
	private Empresa empresa;
	private Date data;
	private NFAmbiente ambiente;

	@Id
	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	@ManyToOne
	@JoinColumn(name = "empresa_id")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tp_ambiente")
	public NFAmbiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(NFAmbiente ambiente) {
		this.ambiente = ambiente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ca == null) ? 0 : ca.hashCode());
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
		MOSTmpAcesso other = (MOSTmpAcesso) obj;
		if (ca == null) {
			if (other.ca != null)
				return false;
		} else if (!ca.equals(other.ca))
			return false;
		return true;
	}

}
