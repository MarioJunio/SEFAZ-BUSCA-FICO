package br.com.bf.rest;

import br.com.bf.utils.CAUtils;

public abstract class GenericController {

	protected String criarCA() {
		return CAUtils.getCA(CAUtils.getRandomNumberBetween(1, 1000));
	}

	protected boolean validarGeraCA() {
		// TODO: Validacao: Verificar se a empresa já possui mais de 2 chaves de acesso temporárias, se sim então não será possível gerar
		// uma nova, pois pode ser uma tentativa de invasão
		return true;
	}
}
