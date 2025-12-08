package br.edu.cs.poo.ac.excecoes;

public class ExcecaoObjetoNaoExistente extends Exception {
	private static final long serialVersionUID = 74;

	public ExcecaoObjetoNaoExistente(String erroMsg) {
		super(erroMsg);
	}
}