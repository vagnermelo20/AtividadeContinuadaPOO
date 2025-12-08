package br.edu.cs.poo.ac.excecoes;

import br.edu.cs.poo.ac.ordem.mediators.ResultadoMediator;

public class ExcecaoNegocio extends Exception {
	private static final long serialVersionUID = 212;
	private ResultadoMediator res;

	public ExcecaoNegocio(ResultadoMediator res) {
		super(res.getMensagensErro().toString());
		this.res = res;
	}

	public ResultadoMediator getRes() {
		return res;
	}
}