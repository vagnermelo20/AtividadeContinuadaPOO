package br.edu.cs.poo.ac.ordem.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

// Imports das suas classes de negócio
import br.edu.cs.poo.ac.ordem.entidades.Cliente;
import br.edu.cs.poo.ac.ordem.entidades.Contato;
import br.edu.cs.poo.ac.ordem.mediators.ClienteMediator;
import br.edu.cs.poo.ac.ordem.mediators.ResultadoMediator;

public class ClienteTela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// --- Componentes da Tela ---
	private JFormattedTextField txtCpfCnpj;
	private JTextField txtNome;
	private JTextField txtEmail;
	private JFormattedTextField txtCelular;
	private JCheckBox chkWhatsapp;
	private JFormattedTextField txtDataCadastro;
	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnBuscar;
	private JButton btnLimpar;
	private JButton btnCancelar;
	private JPanel panelAreaDados;
	
	// --- Atributo do Mediator ---
	private ClienteMediator clienteMediator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteTela frame = new ClienteTela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClienteTela() {
		setTitle("Cadastro de Clientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Inicializa a camada de negócio
		clienteMediator = ClienteMediator.getInstancia();
		
		// Cria e posiciona os componentes visuais
		initialize();
		
		// Configura os listeners (ações dos botões)
		configureListeners();
		
		// Configura o estado inicial da tela
		setEstadoInicial();
	}
	
	private void initialize() {
		JPanel panelAreaAcesso = new JPanel();
		panelAreaAcesso.setBorder(new TitledBorder(null, "\u00C1rea de acesso", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAreaAcesso.setBounds(10, 11, 514, 65);
		contentPane.add(panelAreaAcesso);
		panelAreaAcesso.setLayout(null);
		
		JLabel lblCpfCnpj = new JLabel("CPF ou CNPJ:");
		lblCpfCnpj.setBounds(10, 28, 86, 14);
		panelAreaAcesso.add(lblCpfCnpj);
		
		txtCpfCnpj = new JFormattedTextField();
		txtCpfCnpj.setBounds(95, 25, 170, 20);
		panelAreaAcesso.add(txtCpfCnpj);
		
		panelAreaDados = new JPanel();
		panelAreaDados.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u00C1rea de dados", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelAreaDados.setBounds(10, 87, 514, 230);
		contentPane.add(panelAreaDados);
		panelAreaDados.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 29, 46, 14);
		panelAreaDados.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.setBounds(55, 26, 449, 20);
		panelAreaDados.add(txtNome);
		txtNome.setColumns(10);
		
		JPanel panelContato = new JPanel();
		panelContato.setBorder(new TitledBorder(null, "Contato", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelContato.setBounds(10, 65, 494, 100);
		panelAreaDados.add(panelContato);
		panelContato.setLayout(null);
		
		JLabel lblEmail = new JLabel("E-mail:");
		lblEmail.setBounds(10, 30, 46, 14);
		panelContato.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(66, 27, 418, 20);
		panelContato.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblCelular = new JLabel("Celular:");
		lblCelular.setBounds(10, 61, 46, 14);
		panelContato.add(lblCelular);
		
		txtCelular = new JFormattedTextField();
		txtCelular.setBounds(66, 58, 150, 20);
		panelContato.add(txtCelular);
		
		chkWhatsapp = new JCheckBox("É WhatsApp?");
		chkWhatsapp.setBounds(222, 57, 120, 23);
		panelContato.add(chkWhatsapp);
		
		JLabel lblDataCadastro = new JLabel("Data do Cadastro:");
		lblDataCadastro.setBounds(10, 188, 120, 14);
		panelAreaDados.add(lblDataCadastro);
		
		txtDataCadastro = new JFormattedTextField();
		txtDataCadastro.setBounds(125, 185, 100, 20);
		panelAreaDados.add(txtDataCadastro);
		
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
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(275, 33, 89, 23);
		panelAreaAcesso.add(btnBuscar);
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.setBounds(374, 33, 89, 23);
		panelAreaAcesso.add(btnLimpar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(406, 340, 118, 23);
		contentPane.add(btnCancelar);
		
		// Aplica as máscaras
		aplicarMascaras();
	}
	
	private void configureListeners() {
		btnNovo.addActionListener(e -> novoCliente());
		btnSalvar.addActionListener(e -> incluirCliente());
		btnAlterar.addActionListener(e -> alterarCliente());
		btnExcluir.addActionListener(e -> excluirCliente());
		btnBuscar.addActionListener(e -> buscarCliente());
		btnLimpar.addActionListener(e -> limparCamposAreaAcesso());
		btnCancelar.addActionListener(e -> setEstadoInicial());
	}
	
	// --- MÉTODOS DE CONTROLE DE ESTADO ---

	private void setEstadoInicial() {
		txtCpfCnpj.setEnabled(true);
		txtCpfCnpj.setText("");
		
		limparCamposAreaDados();
		habilitarCamposAreaDados(false);
		
		btnNovo.setEnabled(true);
		btnBuscar.setEnabled(true);
		btnLimpar.setEnabled(true);
		
		btnSalvar.setEnabled(false);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnCancelar.setEnabled(false);
	}
	
	private void setEstadoNovoCliente() {
		txtCpfCnpj.setEnabled(false);
		
		limparCamposAreaDados();
		habilitarCamposAreaDados(true);
		txtDataCadastro.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		
		btnNovo.setEnabled(false);
		btnBuscar.setEnabled(false);
		btnLimpar.setEnabled(false);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		
		btnSalvar.setEnabled(true);
		btnCancelar.setEnabled(true);
	}
	
	private void setEstadoClienteEncontrado() {
		txtCpfCnpj.setEnabled(false);
		habilitarCamposAreaDados(true);
		
		btnNovo.setEnabled(false);
		btnBuscar.setEnabled(false);
		btnLimpar.setEnabled(false);
		btnSalvar.setEnabled(false);
		
		btnAlterar.setEnabled(true);
		btnExcluir.setEnabled(true);
		btnCancelar.setEnabled(true);
	}

	// --- MÉTODOS DE AÇÃO DOS BOTÕES ---

	private void novoCliente() {
		String cpfCnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
		if (cpfCnpj.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Para criar um novo cliente, primeiro digite o CPF/CNPJ.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (clienteMediator.buscar(cpfCnpj) != null) {
			JOptionPane.showMessageDialog(this, "Cliente já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			setEstadoNovoCliente();
		}
	}
	
	private void incluirCliente() {
		Cliente cliente = montarClienteFromTela();
		if (cliente == null) return;
		
		ResultadoMediator resultado = clienteMediator.incluir(cliente);
		
		if (resultado.isOperacaoRealizada()) {
			JOptionPane.showMessageDialog(this, "Inclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			setEstadoInicial();
		} else {
			exibirErros(resultado);
		}
	}
	
	private void alterarCliente() {
		Cliente cliente = montarClienteFromTela();
		if (cliente == null) return;
		
		ResultadoMediator resultado = clienteMediator.alterar(cliente);
		
		if (resultado.isOperacaoRealizada()) {
			JOptionPane.showMessageDialog(this, "Alteração realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			setEstadoInicial();
		} else {
			exibirErros(resultado);
		}
	}
	
	private void excluirCliente() {
		String cpfCnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
		
		int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este cliente?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
		
		if (confirm == JOptionPane.YES_OPTION) {
			ResultadoMediator resultado = clienteMediator.excluir(cpfCnpj);
			if (resultado.isOperacaoRealizada()) {
				JOptionPane.showMessageDialog(this, "Exclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				setEstadoInicial();
			} else {
				exibirErros(resultado);
			}
		}
	}
	
	private void buscarCliente() {
		String cpfCnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
		
		Cliente cliente = clienteMediator.buscar(cpfCnpj);
		
		if (cliente != null) {
			preencherTelaFromCliente(cliente);
			setEstadoClienteEncontrado();
		} else {
			JOptionPane.showMessageDialog(this, "CPF/CNPJ inexistente", "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	// --- MÉTODOS AUXILIARES ---
	
	private void limparCamposAreaDados() {
		txtNome.setText("");
		txtEmail.setText("");
		txtCelular.setText("");
		chkWhatsapp.setSelected(false);
		txtDataCadastro.setText("");
	}
	
	private void limparCamposAreaAcesso() {
		txtCpfCnpj.setText("");
	}
	
	private void habilitarCamposAreaDados(boolean habilitar) {
		panelAreaDados.setEnabled(habilitar);
		txtNome.setEnabled(habilitar);
		txtEmail.setEnabled(habilitar);
		txtCelular.setEnabled(habilitar);
		chkWhatsapp.setEnabled(habilitar);
		txtDataCadastro.setEnabled(habilitar);
	}
	
	/**
	 * Pega os dados da tela, cria um objeto Cliente e o retorna.
	 * O método foi corrigido para tratar o campo de celular vazio com máscara.
	 */
	private Cliente montarClienteFromTela() {
		String cpfCnpj = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
		String nome = txtNome.getText();
		String email = txtEmail.getText();
		
		// --- CORREÇÃO APLICADA AQUI ---
		String celularComMascara = txtCelular.getText();
		// Remove todos os caracteres que não são números
		String celularApenasNumeros = celularComMascara.replaceAll("[^0-9]", "");
		
		// Se não sobrou nenhum número, o campo está vazio.
		// Senão, usa o valor com a máscara para a validação.
		String celularParaMediator = celularApenasNumeros.isEmpty() ? "" : celularComMascara;
		
		boolean isZap = chkWhatsapp.isSelected();
		
		LocalDate dataCadastro;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			dataCadastro = LocalDate.parse(txtDataCadastro.getText(), formatter);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "A data de cadastro está em um formato inválido! Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		// Usa a variável corrigida
		Contato contato = new Contato(email, celularParaMediator, isZap);
		return new Cliente(cpfCnpj, nome, contato, dataCadastro);
	}
	
	private void preencherTelaFromCliente(Cliente cliente) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		txtNome.setText(cliente.getNome());
		txtDataCadastro.setText(cliente.getDataCadastro().format(formatter));
		
		Contato contato = cliente.getContato();
		if (contato != null) {
			txtEmail.setText(contato.getEmail());
			txtCelular.setText(contato.getCelular());
			chkWhatsapp.setSelected(contato.isEhZap());
		}
	}
	
	private void exibirErros(ResultadoMediator resultado) {
		String erros = String.join("\n", resultado.getMensagensErro().listar());
		JOptionPane.showMessageDialog(this, "Erros de validação:\n" + erros, "Erro na Operação", JOptionPane.ERROR_MESSAGE);
	}
	
	private void aplicarMascaras() {
		try {
			MaskFormatter mascaraData = new MaskFormatter("##/##/####");
			mascaraData.install(txtDataCadastro);
			
			MaskFormatter mascaraCelular = new MaskFormatter("(##)#####-####");
			// Define que o campo pode ficar em branco
			mascaraCelular.setPlaceholderCharacter(' '); 
			mascaraCelular.install(txtCelular);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Lógica da máscara dinâmica CPF/CNPJ
		txtCpfCnpj.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String texto = txtCpfCnpj.getText().replaceAll("[^0-9]", "");
				try {
					MaskFormatter mascara;
					if (texto.length() <= 11) {
						mascara = new MaskFormatter("###.###.###-##");
					} else {
						mascara = new MaskFormatter("##.###.###/####-##");
					}
					JFormattedTextField campoFormatado = (JFormattedTextField) e.getSource();
					campoFormatado.setFormatterFactory(null);
					mascara.install(campoFormatado);
					campoFormatado.setText(texto);
				} catch (ParseException ex) {
					// Erro na formatação
				}
			}
		});
	}
}