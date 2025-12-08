package br.edu.cs.poo.ac.ordem.entidades;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // IMPORTANTE: Adicione este import
import br.edu.cs.poo.ac.utils.Registro;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrdemServico implements Registro {
	
	private static final long serialVersionUID = 1L; 
	private Cliente cliente;
	private PrecoBase precoBase;
	private Equipamento equipamento;
	private LocalDateTime dataHoraAbertura;
	private int prazoEmDias;
	private double valor;
	private FechamentoOrdemServico dadosFechamento;
	private LocalDateTime dataHoraCancelamento;
	private String motivoCancelamento;
	private StatusOrdem status;
	private String vendedor;
	
	public OrdemServico(Cliente cliente, PrecoBase precoBase, Equipamento equipamento, LocalDateTime dataHoraAbertura, int prazoEmDias, double valor) {
		this.cliente = cliente;
		this.precoBase = precoBase;
		this.equipamento = equipamento;
		this.dataHoraAbertura = dataHoraAbertura;
		this.prazoEmDias = prazoEmDias;
		this.valor = valor;
	}

	public LocalDate getDataEstimadaEntrega() {
		return dataHoraAbertura.toLocalDate().plusDays(prazoEmDias);
	}
	
	public String getNumero() {
		String id = equipamento.getIdTipo();
		

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		String dataHoraFormatada = dataHoraAbertura.format(formatter);
		
		String parteCpfCnpj = cliente.getCpfCnpj().replaceAll("\\D", "");
		
		if(parteCpfCnpj.length() > 11) {
			return id + dataHoraFormatada + parteCpfCnpj;
		} else {
			return id + dataHoraFormatada + "000" + parteCpfCnpj;
		}
	}
	
	public String getId() {
		return getNumero();
	}
}