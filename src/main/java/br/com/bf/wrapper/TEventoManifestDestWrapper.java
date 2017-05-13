package br.com.bf.wrapper;

public enum TEventoManifestDestWrapper {

	ConfirmacaoDaOperacao("210200", "Confirmacao da Operacao"), 
	CienciaDaOperacao("210210", "Ciencia da Operacao"),
	DesconhecimentoDaOperacao("210220", "Desconhecimento da Operacao"),
	OperacaoNaoRealizada("210240", "Operacao nao Realizada");

	private String codigo;
	private String descricao;

	TEventoManifestDestWrapper(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
