package br.com.bf.wrapper;

public enum NFAmbiente {

    PRODUCAO("1", "Produção"),
    HOMOLOGACAO("2", "Homologação");

    private final String codigo;
    private final String descricao;

    NFAmbiente(final String codigo, final String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public static NFAmbiente valueOfCodigo(final String codigo) {
    	
        for (NFAmbiente ambiente : NFAmbiente.values()) {
        	
            if (ambiente.getCodigo().equalsIgnoreCase(codigo)) {
                return ambiente;
            }
        }
        
        return null;
    }

    @Override
    public String toString() {
        return codigo + " - " + descricao;
    }
}
