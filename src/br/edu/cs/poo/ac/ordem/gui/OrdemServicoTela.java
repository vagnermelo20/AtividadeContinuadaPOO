package br.edu.cs.poo.ac.ordem.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import br.edu.cs.poo.ac.excecoes.ExcecaoNegocio;
import br.edu.cs.poo.ac.ordem.entidades.OrdemServico;
import br.edu.cs.poo.ac.ordem.entidades.StatusOrdem;
import br.edu.cs.poo.ac.ordem.entidades.Notebook;
import br.edu.cs.poo.ac.ordem.entidades.FechamentoOrdemServico;
import br.edu.cs.poo.ac.ordem.mediators.DadosOrdemServico;
import br.edu.cs.poo.ac.ordem.mediators.OrdemServicoMediator;
import br.edu.cs.poo.ac.ordem.mediators.ResultadoMediator;

public class OrdemServicoTela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JComboBox<String> cmbListaIds; 
	private JButton btnBuscar;
	private JButton btnLimparBusca;

	private JTextField txtCpfCnpj;
	private JButton btnCadastrarCliente;
	
	private JTextField txtVendedor;
	
	private JComboBox<String> cmbTipoEquipamento;
	private JTextField txtSerialEquipamento; 
	private JButton btnCadastrarEquipamento;
	
	private JComboBox<String> cmbPrecoBase;
	
	private JTextField txtStatus;
	private JTextField txtValor;
	private JTextField txtPrazo;
	private JTextField txtDataAbertura;
	private JPanel panelDados; 

	private JPanel panelFechamento;
	private JTextArea txtRelatorio;
	private JCheckBox chkPago;

	private JButton btnNovaOS;
	private JButton btnSalvarInclusao;
	private JButton btnFecharOS;
	private JButton btnCancelarOS;
	private JButton btnVoltar;

	private OrdemServicoMediator mediator;
	
	private List<String> historicoIdsCriados = new ArrayList<>();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				OrdemServicoTela frame = new OrdemServicoTela();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public OrdemServicoTela() {
		setTitle("Gestão de Ordens de Serviço");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mediator = OrdemServicoMediator.getInstancia();

		initialize();
		configureListeners();
		setEstadoInicial();
	}

	private void initialize() {
		JPanel panelBusca = new JPanel();
		panelBusca.setBorder(new TitledBorder(null, "Buscar OS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBusca.setBounds(10, 11, 614, 70);
		contentPane.add(panelBusca);
		panelBusca.setLayout(null);

		JLabel lblNum = new JLabel("Número da Ordem:");
		lblNum.setBounds(10, 30, 120, 14);
		panelBusca.add(lblNum);

		cmbListaIds = new JComboBox<>();
		cmbListaIds.setEditable(true);
		cmbListaIds.setBounds(130, 27, 270, 22);
		panelBusca.add(cmbListaIds);

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(410, 26, 90, 23);
		panelBusca.add(btnBuscar);

		btnLimparBusca = new JButton("Limpar");
		btnLimparBusca.setBounds(510, 26, 90, 23);
		panelBusca.add(btnLimparBusca);

		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Dados da Ordem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDados.setBounds(10, 92, 614, 210);
		contentPane.add(panelDados);
		panelDados.setLayout(null);

		JLabel lblCpf = new JLabel("CPF/CNPJ Cliente:");
		lblCpf.setBounds(10, 25, 110, 14);
		panelDados.add(lblCpf);

		txtCpfCnpj = new JTextField();
		txtCpfCnpj.setBounds(130, 22, 120, 20);
		panelDados.add(txtCpfCnpj);
		
		btnCadastrarCliente = new JButton("+");
		btnCadastrarCliente.setToolTipText("Cadastrar Novo Cliente");
		btnCadastrarCliente.setBounds(255, 21, 45, 22);
		panelDados.add(btnCadastrarCliente);

		JLabel lblVend = new JLabel("Vendedor:");
		lblVend.setBounds(310, 25, 60, 14);
		panelDados.add(lblVend);

		txtVendedor = new JTextField();
		txtVendedor.setBounds(380, 22, 210, 20);
		panelDados.add(txtVendedor);

		JLabel lblTipo = new JLabel("Equipamento:");
		lblTipo.setBounds(10, 60, 100, 14);
		panelDados.add(lblTipo);

		cmbTipoEquipamento = new JComboBox<>();
		cmbTipoEquipamento.addItem("Notebook (NO)");
		cmbTipoEquipamento.addItem("Desktop (DE)");
		cmbTipoEquipamento.setBounds(130, 56, 120, 22);
		panelDados.add(cmbTipoEquipamento);

		JLabel lblSerial = new JLabel("Serial:");
		lblSerial.setBounds(260, 60, 50, 14);
		panelDados.add(lblSerial);

		txtSerialEquipamento = new JTextField();
		txtSerialEquipamento.setBounds(310, 57, 90, 20);
		panelDados.add(txtSerialEquipamento);
		
		btnCadastrarEquipamento = new JButton("+");
		btnCadastrarEquipamento.setToolTipText("Cadastrar Novo Equipamento");
		btnCadastrarEquipamento.setBounds(405, 56, 45, 22);
		panelDados.add(btnCadastrarEquipamento);
		
		JLabel lblData = new JLabel("Abertura:");
		lblData.setBounds(460, 60, 60, 14);
		panelDados.add(lblData);
		
		txtDataAbertura = new JTextField();
		txtDataAbertura.setEditable(false);
		txtDataAbertura.setBounds(520, 57, 85, 20);
		panelDados.add(txtDataAbertura);

		JLabel lblServico = new JLabel("Tipo Serviço:");
		lblServico.setBounds(10, 95, 100, 14);
		panelDados.add(lblServico);

		cmbPrecoBase = new JComboBox<>();
		cmbPrecoBase.addItem("1 - Manutenção Normal");
		cmbPrecoBase.addItem("2 - Manutenção Emergencial");
		cmbPrecoBase.addItem("3 - Revisão");
		cmbPrecoBase.addItem("4 - Limpeza");
		cmbPrecoBase.setBounds(130, 91, 200, 22);
		panelDados.add(cmbPrecoBase);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 140, 60, 14);
		panelDados.add(lblStatus);

		txtStatus = new JTextField();
		txtStatus.setEditable(false);
		txtStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtStatus.setBounds(70, 137, 120, 20);
		panelDados.add(txtStatus);

		JLabel lblValor = new JLabel("Valor (R$):");
		lblValor.setBounds(210, 140, 70, 14);
		panelDados.add(lblValor);

		txtValor = new JTextField();
		txtValor.setEditable(false);
		txtValor.setBounds(280, 137, 80, 20);
		panelDados.add(txtValor);

		JLabel lblPrazo = new JLabel("Prazo (dias):");
		lblPrazo.setBounds(380, 140, 80, 14);
		panelDados.add(lblPrazo);

		txtPrazo = new JTextField();
		txtPrazo.setEditable(false);
		txtPrazo.setBounds(460, 137, 50, 20);
		panelDados.add(txtPrazo);

		panelFechamento = new JPanel();
		panelFechamento.setBorder(new TitledBorder(null, "Dados de Fechamento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFechamento.setBounds(10, 310, 614, 130);
		contentPane.add(panelFechamento);
		panelFechamento.setLayout(null);

		JLabel lblRel = new JLabel("Relatório Final:");
		lblRel.setBounds(10, 25, 100, 14);
		panelFechamento.add(lblRel);

		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(10, 45, 594, 75);
		panelFechamento.add(scroll);

		txtRelatorio = new JTextArea();
		scroll.setViewportView(txtRelatorio);

		chkPago = new JCheckBox("Pagamento Realizado?");
		chkPago.setBounds(120, 21, 160, 23);
		panelFechamento.add(chkPago);

		int yBtn = 460;
		btnNovaOS = new JButton("Nova O.S.");
		btnNovaOS.setBounds(10, yBtn, 100, 30);
		contentPane.add(btnNovaOS);

		btnSalvarInclusao = new JButton("Incluir");
		btnSalvarInclusao.setBounds(120, yBtn, 100, 30);
		contentPane.add(btnSalvarInclusao);

		btnFecharOS = new JButton("Fechar O.S.");
		btnFecharOS.setBackground(new Color(152, 251, 152));
		btnFecharOS.setBounds(240, yBtn, 110, 30);
		contentPane.add(btnFecharOS);

		btnCancelarOS = new JButton("Cancelar O.S.");
		btnCancelarOS.setBackground(new Color(255, 160, 122));
		btnCancelarOS.setBounds(360, yBtn, 110, 30);
		contentPane.add(btnCancelarOS);
		
		btnVoltar = new JButton("Voltar / Reset");
		btnVoltar.setBounds(500, yBtn, 110, 30);
		contentPane.add(btnVoltar);
	}

	private void configureListeners() {
		btnBuscar.addActionListener(e -> buscarOS());
		btnLimparBusca.addActionListener(e -> {
			cmbListaIds.setSelectedItem("");
		});
		
		btnNovaOS.addActionListener(e -> setEstadoInclusao());
		btnSalvarInclusao.addActionListener(e -> incluirOS());
		btnCancelarOS.addActionListener(e -> cancelarOS());
		btnFecharOS.addActionListener(e -> fecharOS());
		btnVoltar.addActionListener(e -> setEstadoInicial());
		
		btnCadastrarCliente.addActionListener(e -> abrirTelaCliente());
		btnCadastrarEquipamento.addActionListener(e -> abrirTelaEquipamento());
	}

	private void abrirTelaCliente() {
		ClienteTela telaCliente = new ClienteTela();
		telaCliente.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		telaCliente.setVisible(true);
	}
	
	private void abrirTelaEquipamento() {
		EquipamentoTela telaEquip = new EquipamentoTela();
		telaEquip.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		telaEquip.setVisible(true);
	}

	private void buscarOS() {
		Object selectedItem = cmbListaIds.getSelectedItem();
		String numero = (selectedItem != null) ? selectedItem.toString().trim() : "";
		
		if (numero.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Digite ou selecione o número da ordem.");
			return;
		}

		OrdemServico os = mediator.buscar(numero);
		if (os == null) {
			JOptionPane.showMessageDialog(this, "Ordem de Serviço não encontrada!", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		preencherCampos(os);
		setEstadoVisualizacao(os);
	}

	private void incluirOS() {
		LocalDateTime dataHoraEstimada = LocalDateTime.now(); 
		
		try {
			String cpf = txtCpfCnpj.getText();
			String vendedor = txtVendedor.getText();
			String serial = txtSerialEquipamento.getText();
			String prefixo = cmbTipoEquipamento.getSelectedIndex() == 0 ? "NO" : "DE";
			String idEquipamento = prefixo + serial; 
			int codigoPreco = cmbPrecoBase.getSelectedIndex() + 1;

			DadosOrdemServico dados = new DadosOrdemServico(cpf, codigoPreco, idEquipamento, vendedor);

			ResultadoMediator res = mediator.incluir(dados);

			if (res.isOperacaoRealizada()) {
				// 1. Tenta gerar o ID exato
				String idGerado = gerarIdEstimado(idEquipamento, cpf, dataHoraEstimada);
				
				// 2. TENTATIVA DE VALIDAÇÃO: Verifica se essa ordem existe mesmo
				OrdemServico confirmacao = mediator.buscar(idGerado);
				
				// Se não achou (provavelmente mudou o minuto), tenta com +1 minuto
				if (confirmacao == null) {
					String idAlternativo = gerarIdEstimado(idEquipamento, cpf, dataHoraEstimada.plusMinutes(1));
					confirmacao = mediator.buscar(idAlternativo);
					if (confirmacao != null) {
						idGerado = idAlternativo; // Achamos o certo!
					}
				}
				
				adicionarIdAoHistorico(idGerado);
				
				JTextArea msg = new JTextArea("Ordem incluída com sucesso!\n\nID: " + idGerado);
				msg.setEditable(false);
				msg.setBackground(new Color(240,240,240));
				JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				
				setEstadoInicial();
				cmbListaIds.setSelectedItem(idGerado);
				
			} else {
				exibirErros(res);
			}
		} catch (ExcecaoNegocio e) {
			exibirErros(e.getRes());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro técnico: " + e.getMessage());
		}
	}
	
	private String gerarIdEstimado(String idEquip, String cpfCnpj, LocalDateTime dataHora) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		String dataHoraFormatada = dataHora.format(formatter);
		String parteCpfCnpj = cpfCnpj.replaceAll("\\D", "");
		
		String sufixoCpf;
		if(parteCpfCnpj.length() > 11) {
			sufixoCpf = parteCpfCnpj;
		} else {
			sufixoCpf = "000" + parteCpfCnpj;
		}
		
		return idEquip + dataHoraFormatada + sufixoCpf;
	}
	
	private void adicionarIdAoHistorico(String id) {
		if (!historicoIdsCriados.contains(id)) {
			historicoIdsCriados.add(id);
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(historicoIdsCriados.toArray(new String[0]));
			cmbListaIds.setModel(model);
		}
	}

	private void cancelarOS() {
		Object selectedItem = cmbListaIds.getSelectedItem();
		String numero = (selectedItem != null) ? selectedItem.toString() : "";
		
		String motivo = JOptionPane.showInputDialog(this, "Qual o motivo do cancelamento?");
		
		if (motivo != null && !motivo.trim().isEmpty()) {
			try {
				ResultadoMediator res = mediator.cancelar(numero, motivo, LocalDateTime.now());
				if (res.isOperacaoRealizada()) {
					JOptionPane.showMessageDialog(this, "Ordem cancelada com sucesso!");
					setEstadoInicial();
				} else {
					exibirErros(res);
				}
			} catch (ExcecaoNegocio e) {
				exibirErros(e.getRes());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Erro técnico: " + e.getMessage());
			}
		}
	}

	private void fecharOS() {
		Object selectedItem = cmbListaIds.getSelectedItem();
		String numero = (selectedItem != null) ? selectedItem.toString() : "";
		
		String relatorio = txtRelatorio.getText();
		boolean pago = chkPago.isSelected();

		if (relatorio.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "O relatório é obrigatório.");
			return;
		}

		try {
			FechamentoOrdemServico fecho = new FechamentoOrdemServico(numero, LocalDate.now(), pago, relatorio);
			ResultadoMediator res = mediator.fechar(fecho);

			if (res.isOperacaoRealizada()) {
				JOptionPane.showMessageDialog(this, "Ordem fechada com sucesso!");
				setEstadoInicial();
			} else {
				exibirErros(res);
			}
		} catch (ExcecaoNegocio e) {
			exibirErros(e.getRes());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro técnico: " + e.getMessage());
		}
	}

	private void setEstadoInicial() {
		limparCampos();
		cmbListaIds.setEnabled(true);
		btnBuscar.setEnabled(true);
		btnLimparBusca.setEnabled(true);
		habilitarEdicaoDados(false);
		panelFechamento.setVisible(false);
		btnNovaOS.setEnabled(true);
		btnSalvarInclusao.setEnabled(false);
		btnFecharOS.setEnabled(false);
		btnCancelarOS.setEnabled(false);
		btnVoltar.setEnabled(false);
	}

	private void setEstadoInclusao() {
		limparCampos();
		cmbListaIds.setSelectedItem("");
		cmbListaIds.setEnabled(false);
		btnBuscar.setEnabled(false);
		habilitarEdicaoDados(true);
		panelFechamento.setVisible(false);
		btnNovaOS.setEnabled(false);
		btnSalvarInclusao.setEnabled(true);
		btnVoltar.setEnabled(true);
	}

	private void setEstadoVisualizacao(OrdemServico os) {
		cmbListaIds.setEnabled(true);
		btnBuscar.setEnabled(true);
		habilitarEdicaoDados(false);
		panelFechamento.setVisible(true);
		
		boolean isAberta = (os.getStatus() == StatusOrdem.ABERTA);
		txtRelatorio.setEditable(isAberta);
		chkPago.setEnabled(isAberta);
		
		if (os.getStatus() == StatusOrdem.FECHADA && os.getDadosFechamento() != null) {
			txtRelatorio.setText(os.getDadosFechamento().getRelatorioFinal());
			chkPago.setSelected(os.getDadosFechamento().isPago());
			txtRelatorio.setEditable(false);
			chkPago.setEnabled(false);
		}

		btnNovaOS.setEnabled(false);
		btnSalvarInclusao.setEnabled(false);
		btnFecharOS.setEnabled(isAberta);
		btnCancelarOS.setEnabled(isAberta);
		btnVoltar.setEnabled(true);
	}

	private void preencherCampos(OrdemServico os) {
		txtCpfCnpj.setText(os.getCliente().getCpfCnpj());
		txtVendedor.setText(os.getVendedor());
		
		if (os.getEquipamento() instanceof Notebook) cmbTipoEquipamento.setSelectedIndex(0);
		else cmbTipoEquipamento.setSelectedIndex(1);
		
		String idFull = os.getEquipamento().getIdTipo();
		if(idFull.length() > 2) txtSerialEquipamento.setText(idFull.substring(2));
		else txtSerialEquipamento.setText(idFull);
		
		cmbPrecoBase.setSelectedIndex(os.getPrecoBase().getCodigo() - 1);
		txtStatus.setText(os.getStatus().getDescricao());
		txtValor.setText(String.format("%.2f", os.getValor()));
		txtPrazo.setText(String.valueOf(os.getPrazoEmDias()));
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		txtDataAbertura.setText(os.getDataHoraAbertura().format(dtf));
		
		if(os.getStatus() == StatusOrdem.ABERTA) txtStatus.setForeground(new Color(0,100,0));
		else if(os.getStatus() == StatusOrdem.CANCELADA) txtStatus.setForeground(Color.RED);
		else txtStatus.setForeground(Color.BLUE);
	}

	private void habilitarEdicaoDados(boolean enable) {
		txtCpfCnpj.setEditable(enable);
		btnCadastrarCliente.setEnabled(enable);
		txtVendedor.setEditable(enable);
		cmbTipoEquipamento.setEnabled(enable);
		txtSerialEquipamento.setEditable(enable);
		btnCadastrarEquipamento.setEnabled(enable);
		cmbPrecoBase.setEnabled(enable);
	}

	private void limparCampos() {
		txtCpfCnpj.setText("");
		txtVendedor.setText("");
		txtSerialEquipamento.setText("");
		txtStatus.setText("");
		txtValor.setText("");
		txtPrazo.setText("");
		txtDataAbertura.setText("");
		txtRelatorio.setText("");
		chkPago.setSelected(false);
		txtStatus.setForeground(Color.BLACK);
	}

	private void exibirErros(ResultadoMediator resultado) {
		String erros = String.join("\n", resultado.getMensagensErro().listar());
		JOptionPane.showMessageDialog(this, "Erros:\n" + erros, "Atenção", JOptionPane.ERROR_MESSAGE);
	}
}