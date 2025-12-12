package br.edu.cs.poo.ac.ordem.telas;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import br.edu.cs.poo.ac.ordem.entidades.Desktop;
import br.edu.cs.poo.ac.ordem.entidades.Notebook;
import br.edu.cs.poo.ac.ordem.mediators.EquipamentoMediator;
import br.edu.cs.poo.ac.ordem.mediators.ResultadoMediator;

public class EquipamentoTela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// --- Componentes da Tela ---
	private JComboBox<String> cmbTipoEquipamento;
	private JTextField txtSerial;
	private JTextField txtDescricao;
	private JFormattedTextField txtValorEstimado;
	
	// Radio Buttons (conforme especificação)
	private JRadioButton radioEhNovoSim;
	private JRadioButton radioEhNovoNao;
	private final ButtonGroup groupEhNovo = new ButtonGroup();

	private JRadioButton radioDadosSensiveisSim;
	private JRadioButton radioDadosSensiveisNao;
	private final ButtonGroup groupDadosSensiveis = new ButtonGroup();
	private JPanel panelDadosSensiveis; // Painel para agrupar os botões

	private JRadioButton radioEhServidorSim;
	private JRadioButton radioEhServidorNao;
	private final ButtonGroup groupEhServidor = new ButtonGroup();
	private JPanel panelEhServidor; // Painel para agrupar os botões
	
	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnBuscar;
	private JButton btnLimpar;
	private JButton btnCancelar;
	private JPanel panelAreaDados;

	// --- Atributo do Mediator ---
	private EquipamentoMediator equipamentoMediator;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				EquipamentoTela frame = new EquipamentoTela();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public EquipamentoTela() {
		setTitle("Cadastro de Equipamentos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		equipamentoMediator = EquipamentoMediator.getInstancia();
		initialize();
		configureListeners();
		setEstadoInicial();
	}

	private void initialize() {
		JPanel panelAreaAcesso = new JPanel();
		panelAreaAcesso.setBorder(new TitledBorder(null, "Área de acesso", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAreaAcesso.setBounds(10, 11, 514, 100);
		contentPane.add(panelAreaAcesso);
		panelAreaAcesso.setLayout(null);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(10, 28, 46, 14);
		panelAreaAcesso.add(lblTipo);
		
		cmbTipoEquipamento = new JComboBox<>();
		cmbTipoEquipamento.setBounds(50, 24, 150, 22);
		cmbTipoEquipamento.addItem("Notebook");
		cmbTipoEquipamento.addItem("Desktop");
		panelAreaAcesso.add(cmbTipoEquipamento);
		
		JLabel lblSerial = new JLabel("Serial:");
		lblSerial.setBounds(10, 65, 46, 14);
		panelAreaAcesso.add(lblSerial);
		
		txtSerial = new JTextField();
		txtSerial.setBounds(50, 62, 150, 20);
		panelAreaAcesso.add(txtSerial);
		txtSerial.setColumns(10);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(250, 43, 89, 23);
		panelAreaAcesso.add(btnBuscar);
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.setBounds(349, 43, 89, 23);
		panelAreaAcesso.add(btnLimpar);

		panelAreaDados = new JPanel();
		panelAreaDados.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Área de dados", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelAreaDados.setBounds(10, 122, 514, 195);
		contentPane.add(panelAreaDados);
		panelAreaDados.setLayout(null);
		
		JLabel lblDescricao = new JLabel("Descrição:");
		lblDescricao.setBounds(10, 29, 70, 14);
		panelAreaDados.add(lblDescricao);
		
		txtDescricao = new JTextField();
		txtDescricao.setBounds(90, 26, 414, 20);
		panelAreaDados.add(txtDescricao);
		txtDescricao.setColumns(10);
		
		JLabel lblEhNovo = new JLabel("É Novo:");
		lblEhNovo.setBounds(10, 64, 70, 14);
		panelAreaDados.add(lblEhNovo);
		
		radioEhNovoSim = new JRadioButton("SIM");
		groupEhNovo.add(radioEhNovoSim);
		radioEhNovoSim.setBounds(140, 60, 60, 23);
		panelAreaDados.add(radioEhNovoSim);

		radioEhNovoNao = new JRadioButton("NÃO");
		groupEhNovo.add(radioEhNovoNao);
		radioEhNovoNao.setBounds(80, 60, 60, 23);
		panelAreaDados.add(radioEhNovoNao);
		
		JLabel lblValor = new JLabel("Valor Estimado:");
		lblValor.setBounds(10, 94, 100, 14);
		panelAreaDados.add(lblValor);
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setMaximumIntegerDigits(10);
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Double.class);
		formatter.setAllowsInvalid(false);
		txtValorEstimado = new JFormattedTextField(formatter);
		txtValorEstimado.setBounds(120, 91, 150, 20);
		panelAreaDados.add(txtValorEstimado);
		
		// Painel para "Carrega dados sensíveis" (NOTEBOOK)
		panelDadosSensiveis = new JPanel();
		panelDadosSensiveis.setBounds(10, 130, 300, 30);
		panelDadosSensiveis.setLayout(null);
		panelAreaDados.add(panelDadosSensiveis);
		JLabel lblDadosSensiveis = new JLabel("Carrega dados sensíveis:");
		lblDadosSensiveis.setBounds(0, 5, 150, 14);
		panelDadosSensiveis.add(lblDadosSensiveis);
		radioDadosSensiveisSim = new JRadioButton("SIM");
		radioDadosSensiveisSim.setBounds(210, 1, 60, 23);
		groupDadosSensiveis.add(radioDadosSensiveisSim);
		panelDadosSensiveis.add(radioDadosSensiveisSim);
		radioDadosSensiveisNao = new JRadioButton("NÃO");
		radioDadosSensiveisNao.setBounds(150, 1, 60, 23);
		groupDadosSensiveis.add(radioDadosSensiveisNao);
		panelDadosSensiveis.add(radioDadosSensiveisNao);

		// Painel para "É Servidor" (DESKTOP)
		panelEhServidor = new JPanel();
		panelEhServidor.setBounds(10, 130, 300, 30);
		panelEhServidor.setLayout(null);
		panelAreaDados.add(panelEhServidor);
		JLabel lblEhServidor = new JLabel("É Servidor:");
		lblEhServidor.setBounds(0, 5, 150, 14);
		panelEhServidor.add(lblEhServidor);
		radioEhServidorSim = new JRadioButton("SIM");
		radioEhServidorSim.setBounds(140, 1, 60, 23);
		groupEhServidor.add(radioEhServidorSim);
		panelEhServidor.add(radioEhServidorSim);
		radioEhServidorNao = new JRadioButton("NÃO");
		radioEhServidorNao.setBounds(80, 1, 60, 23);
		groupEhServidor.add(radioEhServidorNao);
		panelEhServidor.add(radioEhServidorNao);

		btnNovo = new JButton("Novo");
		btnNovo.setBounds(10, 340, 89, 23);
		contentPane.add(btnNovo);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(109, 340, 89, 23);
		contentPane.add(btnSalvar);
		
		btnAlterar = new JButton("Alterar");
		btnAlterar.setBounds(208, 340, 89, 23);
		contentPane.add(btnAlterar);
		
		btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(307, 340, 89, 23);
		contentPane.add(btnExcluir);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(406, 340, 118, 23);
		contentPane.add(btnCancelar);
	}

	private void configureListeners() {
		cmbTipoEquipamento.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				atualizarCamposEspecificos();
			}
		});
		
		btnNovo.addActionListener(e -> novoEquipamento());
		btnSalvar.addActionListener(e -> incluirEquipamento());
		btnAlterar.addActionListener(e -> alterarEquipamento());
		btnExcluir.addActionListener(e -> excluirEquipamento());
		btnBuscar.addActionListener(e -> buscarEquipamento());
		btnLimpar.addActionListener(e -> limparCamposAreaAcesso());
		btnCancelar.addActionListener(e -> setEstadoInicial());
	}
	
	private void setEstadoInicial() {
		limparCamposAreaAcesso();
		limparCamposAreaDados();
		
		cmbTipoEquipamento.setEnabled(true);
		txtSerial.setEnabled(true);
		
		habilitarCamposAreaDados(false);
		
		btnNovo.setEnabled(true);
		btnBuscar.setEnabled(true);
		btnLimpar.setEnabled(true);
		
		btnSalvar.setEnabled(false);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnCancelar.setEnabled(false);
		
		atualizarCamposEspecificos();
	}

	private void setEstadoNovoEquipamento() {
		cmbTipoEquipamento.setEnabled(false);
		txtSerial.setEnabled(false);
		
		limparCamposAreaDados();
		habilitarCamposAreaDados(true);
		
		btnNovo.setEnabled(false);
		btnBuscar.setEnabled(false);
		btnLimpar.setEnabled(false);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		
		btnSalvar.setEnabled(true);
		btnCancelar.setEnabled(true);
	}
	
	private void setEstadoEquipamentoEncontrado() {
		cmbTipoEquipamento.setEnabled(false);
		txtSerial.setEnabled(false);
		
		habilitarCamposAreaDados(true);
		
		btnNovo.setEnabled(false);
		btnBuscar.setEnabled(false);
		btnLimpar.setEnabled(false);
		btnSalvar.setEnabled(false);
		
		btnAlterar.setEnabled(true);
		btnExcluir.setEnabled(true);
		btnCancelar.setEnabled(true);
	}

	private void novoEquipamento() {
		String serial = txtSerial.getText();
		if (serial.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Para criar um novo equipamento, primeiro digite o Serial.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String idUnico = getIdUnicoFromTela();
		if (equipamentoMediator.buscarDesktop(idUnico) != null || equipamentoMediator.buscarNotebook(idUnico) != null) {
			JOptionPane.showMessageDialog(this, "Equipamento já cadastrado com este serial!", "Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			setEstadoNovoEquipamento();
		}
	}
	
	private void incluirEquipamento() {
		ResultadoMediator resultado;
		if (isNotebookSelecionado()) {
			Notebook note = montarNotebookFromTela();
			resultado = equipamentoMediator.incluirNotebook(note);
		} else {
			Desktop desk = montarDesktopFromTela();
			resultado = equipamentoMediator.incluirDesktop(desk);
		}
		
		if (resultado.isOperacaoRealizada()) {
			JOptionPane.showMessageDialog(this, "Inclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			setEstadoInicial();
		} else {
			exibirErros(resultado);
		}
	}

	private void alterarEquipamento() {
		ResultadoMediator resultado;
		if (isNotebookSelecionado()) {
			Notebook note = montarNotebookFromTela();
			resultado = equipamentoMediator.alterarNotebook(note);
		} else {
			Desktop desk = montarDesktopFromTela();
			resultado = equipamentoMediator.alterarDesktop(desk);
		}
		
		if (resultado.isOperacaoRealizada()) {
			JOptionPane.showMessageDialog(this, "Alteração realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			setEstadoInicial();
		} else {
			exibirErros(resultado);
		}
	}
	
	private void excluirEquipamento() {
		String idUnico = getIdUnicoFromTela();
		int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este equipamento?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
		
		if (confirm == JOptionPane.YES_OPTION) {
			ResultadoMediator resultado;
			if (isNotebookSelecionado()) {
				resultado = equipamentoMediator.excluirNotebook(idUnico);
			} else {
				resultado = equipamentoMediator.excluirDesktop(idUnico);
			}
			
			if (resultado.isOperacaoRealizada()) {
				JOptionPane.showMessageDialog(this, "Exclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Exclusão não pôde ser realizada.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
			setEstadoInicial();
		}
	}
	
	private void buscarEquipamento() {
		String idUnico = getIdUnicoFromTela();
		Object equipamento = null;
		
		if (isNotebookSelecionado()) {
			equipamento = equipamentoMediator.buscarNotebook(idUnico);
		} else {
			equipamento = equipamentoMediator.buscarDesktop(idUnico);
		}
		
		if (equipamento != null) {
			preencherTelaFromEquipamento(equipamento);
			setEstadoEquipamentoEncontrado();
		} else {
			JOptionPane.showMessageDialog(this, "Equipamento não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void limparCamposAreaDados() {
		txtDescricao.setText("");
		radioEhNovoNao.setSelected(true);
		txtValorEstimado.setValue(0.0);
		radioDadosSensiveisNao.setSelected(true);
		radioEhServidorNao.setSelected(true);
	}
	
	private void limparCamposAreaAcesso() {
		txtSerial.setText("");
		cmbTipoEquipamento.setSelectedIndex(0);
	}
	
	private void habilitarCamposAreaDados(boolean habilitar) {
		panelAreaDados.setEnabled(habilitar);
		txtDescricao.setEnabled(habilitar);
		radioEhNovoSim.setEnabled(habilitar);
		radioEhNovoNao.setEnabled(habilitar);
		txtValorEstimado.setEnabled(habilitar);
		
		panelDadosSensiveis.setEnabled(habilitar);
		radioDadosSensiveisSim.setEnabled(habilitar);
		radioDadosSensiveisNao.setEnabled(habilitar);
		
		panelEhServidor.setEnabled(habilitar);
		radioEhServidorSim.setEnabled(habilitar);
		radioEhServidorNao.setEnabled(habilitar);
	}
	
	private Notebook montarNotebookFromTela() {
		String serial = txtSerial.getText();
		String descricao = txtDescricao.getText();
		boolean ehNovo = radioEhNovoSim.isSelected();
		double valor = (double) (txtValorEstimado.getValue() != null ? txtValorEstimado.getValue() : 0.0);
		boolean dadosSensiveis = radioDadosSensiveisSim.isSelected();
		
		return new Notebook(serial, descricao, ehNovo, valor, dadosSensiveis);
	}
	
	private Desktop montarDesktopFromTela() {
		String serial = txtSerial.getText();
		String descricao = txtDescricao.getText();
		boolean ehNovo = radioEhNovoSim.isSelected();
		double valor = (double) (txtValorEstimado.getValue() != null ? txtValorEstimado.getValue() : 0.0);
		boolean ehServidor = radioEhServidorSim.isSelected();
		
		return new Desktop(serial, descricao, ehNovo, valor, ehServidor);
	}

	private void preencherTelaFromEquipamento(Object equipamento) {
		if (equipamento instanceof Notebook) {
			Notebook note = (Notebook) equipamento;
			txtDescricao.setText(note.getDescricao());
			if(note.isEhNovo()) radioEhNovoSim.setSelected(true); else radioEhNovoNao.setSelected(true);
			txtValorEstimado.setValue(note.getValorEstimado());
			if(note.isCarregaDadosSensiveis()) radioDadosSensiveisSim.setSelected(true); else radioDadosSensiveisNao.setSelected(true);
		} else if (equipamento instanceof Desktop) {
			Desktop desk = (Desktop) equipamento;
			txtDescricao.setText(desk.getDescricao());
			if(desk.isEhNovo()) radioEhNovoSim.setSelected(true); else radioEhNovoNao.setSelected(true);
			txtValorEstimado.setValue(desk.getValorEstimado());
			if(desk.isEhServidor()) radioEhServidorSim.setSelected(true); else radioEhServidorNao.setSelected(true);
		}
	}
	
	/**
	 * MÉTODO CORRIGIDO: Exibe os erros em um JOptionPane padrão.
	 */
	private void exibirErros(ResultadoMediator resultado) {
		// Converte a lista de erros em uma única string com quebras de linha
		String erros = String.join("\n", resultado.getMensagensErro().listar());
		// Exibe a string em uma caixa de diálogo de erro
		JOptionPane.showMessageDialog(this, "Erros de validação:\n" + erros, "Erro na Operação", JOptionPane.ERROR_MESSAGE);
	}
	
	private void atualizarCamposEspecificos() {
		if (isNotebookSelecionado()) {
			panelDadosSensiveis.setVisible(true);
			panelEhServidor.setVisible(false);
		} else {
			panelDadosSensiveis.setVisible(false);
			panelEhServidor.setVisible(true);
		}
	}
	
	private boolean isNotebookSelecionado() {
		return "Notebook".equals(cmbTipoEquipamento.getSelectedItem());
	}
	
	private String getIdUnicoFromTela() {
		String tipoId = isNotebookSelecionado() ? "NO" : "DE";
		return tipoId + txtSerial.getText();
	}
}