package br.com.bf.view.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ConsDistDFeHolder {

	public enum TipoNF {
		ENTRADA("Entrada"), SAIDA("Saída");

		private String descricao;

		private TipoNF(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	public enum SituacaoNFe {
		AUTORIZADO("Autorizado"), DENEGADO("Denegado"), CANCELADO("Cancelado");

		private String descricao;

		private SituacaoNFe(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	private String chNFe;
	private String nomeEmitente;
	private Date dataEmissao;
	private TipoNF tipoNf;
	private BigDecimal valor;
	private Date dataRecebimento;
	private SituacaoNFe situacaoNFe;
	private boolean resumo;

	public String getChNFe() {
		return chNFe;
	}

	public void setChNFe(String chNFe) {
		this.chNFe = chNFe;
	}

	public String getNomeEmitente() {
		return nomeEmitente;
	}

	public void setNomeEmitente(String nomeEmitente) {
		this.nomeEmitente = nomeEmitente;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public TipoNF getTipoNf() {
		return tipoNf;
	}

	public void setTipoNf(TipoNF tipoNf) {
		this.tipoNf = tipoNf;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public SituacaoNFe getSituacaoNFe() {
		return situacaoNFe;
	}

	public void setSituacaoNFe(SituacaoNFe situacaoNFe) {
		this.situacaoNFe = situacaoNFe;
	}

	public boolean isResumo() {
		return resumo;
	}

	public void setResumo(boolean resumo) {
		this.resumo = resumo;
	}

	@Override
	public String toString() {
		return "ConsDistDFeHolder [chNFe=" + chNFe + ", nomeEmitente=" + nomeEmitente + ", dataEmissao=" + dataEmissao + ", tipoNf=" + tipoNf + ", valor="
				+ valor + ", dataRecebimento=" + dataRecebimento + ", situacaoNFe=" + situacaoNFe + ", resumo=" + resumo + "]";
	}

}
