package br.edu.cs.poo.ac.ordem.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
public class OrdemServico implements Serializable {

	private Cliente cliente;
	private PrecoBase precoBase;
	private Notebook notebook;
	private Desktop desktop;
	private LocalDateTime dataHoraAbertura;
	private int prazoEmDias;
	private double valor;
	
	public LocalDate getDataEstimadaEntrega() {
		
		LocalDate data = dataHoraAbertura.toLocalDate();
		data = data.plusDays(prazoEmDias);
		return data;
	}
	
	public String getNumero() {
		String Equipamento = "";
		if(notebook.getIdTipo() != null) {
			Equipamento = notebook.getIdTipo();
		}
		else if (desktop.getIdTipo() != null){
			Equipamento = desktop.getIdTipo();
		}
		
		LocalDateTime data = dataHoraAbertura;
		
		if (cliente.getCpfCnpj() )
	}
}


• Método público String getNumeroi(): retorna a concatenação do tipo de equipamento (retorno do
método getIdTipo do notebook ou do desktop, quem não for nulo) ano, mês, dia, hora e minuto com o
cpf ou cnpj do cliente, se o cpf ou cnpj do cliente for um cnpj; ou retorna a concatenação do mês,
ano, dia, hora e minuto com “000” e com o cpf ou cnpj do cliente, se o cpf ou cnpj do cliente for um
cnpj.