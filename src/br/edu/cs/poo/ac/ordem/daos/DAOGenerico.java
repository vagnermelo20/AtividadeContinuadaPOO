package br.edu.cs.poo.ac.ordem.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.utils.Registro;

public abstract class DAOGenerico {

	protected CadastroObjetos cadastroObjetos;

	// Método abstrato para obter a classe na filha
	public abstract Class<?> getClasseEntidade();

	// Construtor sem parâmetros que usa o método abstrato
	public DAOGenerico() {
		this.cadastroObjetos = new CadastroObjetos(getClasseEntidade());
	}

	// Método de busca auxiliar usado nas validações
	public Registro buscar(String id) {
		return (Registro) cadastroObjetos.buscar(id);
	}

	public boolean incluir(Registro registro) {
		// Lógica trazida do exemplo:
		// Se buscar retornar algo diferente de null, já existe. Não inclui.
		if (buscar(registro.getId()) == null) {
			cadastroObjetos.incluir(registro, registro.getId());
			return true;
		} else {
			return false;
		}
	}

	public boolean alterar(Registro registro) {
		// Lógica trazida do exemplo:
		// Se buscar retornar null, não existe. Não altera.
		if (buscar(registro.getId()) != null) {
			cadastroObjetos.alterar(registro, registro.getId());
			return true;
		} else {
			return false;
		}
	}

	public boolean excluir(String id) {
		// Lógica trazida do exemplo:
		// Se buscar retornar null, não existe. Não exclui.
		if (buscar(id) != null) {
			cadastroObjetos.excluir(id);
			return true;
		} else {
			return false;
		}
	}

	public Registro[] buscarTodos() {
		Object[] objs = cadastroObjetos.buscarTodos();
		
		if (objs != null && objs.length > 0) {
			Registro[] registros = new Registro[objs.length];
			for (int i = 0; i < objs.length; i++) {
				registros[i] = (Registro) objs[i];
			}
			return registros;
		} else {
			return new Registro[0];
		}
	}
}