package br.edu.cs.poo.ac.ordem.daos;

import java.io.Serializable;

import br.edu.cs.poo.ac.ordem.entidades.FechamentoOrdemServico;

//O identificador único, por objeto, de FechamentoOrdemServico é o número.   
public class FechamentoOrdemServicoDAO extends DAOGenerico{

	public FechamentoOrdemServicoDAO(){
		
		super(FechamentoOrdemServico.class);
	}
	
	
	public FechamentoOrdemServico buscar (String numero) {
		
		return (FechamentoOrdemServico) cadastroObjetos.buscar(numero);
	}
	
	public boolean incluir(FechamentoOrdemServico fechamentoOrdemServico) {
		if (buscar(fechamentoOrdemServico.getNumeroOrdemServico()) == null) {
			cadastroObjetos.incluir(fechamentoOrdemServico, fechamentoOrdemServico.getNumeroOrdemServico());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean alterar(FechamentoOrdemServico fechamentoOrdemServico) {
		if (buscar(fechamentoOrdemServico.getNumeroOrdemServico()) != null) {
			cadastroObjetos.alterar(fechamentoOrdemServico, fechamentoOrdemServico.getNumeroOrdemServico());
			return true;
		} else {
			return false;
		}
	}
		
	public boolean excluir(String numero) {
		if (buscar(numero) != null) {
			cadastroObjetos.excluir(numero);
			return true;
		} else {
			return false;
		}
	}

	public FechamentoOrdemServico[] buscarTodos() {
		Serializable[] ret = cadastroObjetos.buscarTodos();
		FechamentoOrdemServico[] retorno;
		if (ret != null && ret.length > 0) {
			retorno = new FechamentoOrdemServico[ret.length];
			for (int i = 0; i < ret.length; i++) {
				retorno[i] = (FechamentoOrdemServico) ret[i];
			}
		} else {
			retorno = new FechamentoOrdemServico[0];
		}
		return retorno;
	}
}	