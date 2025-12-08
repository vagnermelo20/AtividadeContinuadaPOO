package br.edu.cs.poo.ac.excecoes;

public class ExcecaoObjetoJaExistente extends Exception {
	private static final long serialVersionUID = 120;

	public ExcecaoObjetoJaExistente(String erroMsg) {
		super(erroMsg);
	}
}