package br.edu.cs.poo.ac.ordem.telas;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.MaskFormatter;

// Imports de DAOs (Necessários para popular os Combos)
import br.edu.cs.poo.ac.ordem.daos.ClienteDAO;
import br.edu.cs.poo.ac.ordem.daos.DesktopDAO;
import br.edu.cs.poo.ac.ordem.daos.NotebookDAO;
import br.edu.cs.poo.ac.ordem.daos.OrdemServicoDAO;

// Imports de Entidades
import br.edu.cs.poo.ac.ordem.entidades.Cliente;
import br.edu.cs.poo.ac.ordem.entidades.Desktop;
import br.edu.cs.poo.ac.ordem.entidades.FechamentoOrdemServico;
import br.edu.cs.poo.ac.ordem.entidades.Notebook;
import br.edu.cs.poo.ac.ordem.entidades.OrdemServico;
import br.edu.cs.poo.ac.ordem.entidades.PrecoBase;
import br.edu.cs.poo.ac.ordem.entidades.StatusOrdem;

// Imports do Mediator e Exceções
import br.edu.cs.poo.ac.excecoes.ExcecaoNegocio;
import br.edu.cs.poo.ac.ordem.mediators.DadosOrdemServico;
import br.edu.cs.poo.ac.ordem.mediators.OrdemServicoMediator;
import br.edu.cs.poo.ac.ordem.mediators.ResultadoMediator;

public class OrdemServicoTela extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // --- Componentes de Busca ---
    private JComboBox<OrdemItem> cmbNumeroOrdem; // Agora é ComboBox
    private JButton btnBuscar;
    private JButton btnLimparBusca;
    private JButton btnAtualizarGeral; // Botão para recarregar tudo

    // --- Componentes de Inclusão ---
    private JComboBox<ClienteItem> cmbCliente; // Agora é ComboBox
    private JButton btnAddCliente;
    
    private JComboBox<EquipamentoItem> cmbEquipamento; // Agora é ComboBox
    private JButton btnAddEquipamento;
    
    private JComboBox<String> cmbPrecoBase; 
    private JTextField txtVendedor;
    private JButton btnIncluir;

    // --- Componentes de Gestão (Cancelar/Fechar) ---
    private JTextField txtMotivoCancelamento;
    private JButton btnCancelar;
    
    private JTextArea txtRelatorio;
    private JFormattedTextField txtDataFechamento;
    private JButton btnFechar;

    // --- Informações Visuais ---
    private JLabel lblStatusInfo;
    private JPanel panelGestao;
    private JPanel panelInclusao;

    // Mediator
    private OrdemServicoMediator mediator;
    private PrecoBase[] listaPrecos;

    // DAOs para popular os combos (Acesso direto para leitura)
    private ClienteDAO clienteDAO = new ClienteDAO();
    private NotebookDAO notebookDAO = new NotebookDAO();
    private DesktopDAO desktopDAO = new DesktopDAO();
    private OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

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
        setTitle("Gerenciamento de Ordem de Serviço");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 720, 650); // Aumentei um pouco a largura
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        mediator = OrdemServicoMediator.getInstancia();
        
        initialize();
        configureListeners();
        
        // Carrega os dados iniciais dos Dropdowns
        atualizarTodasListas();
        
        setEstadoInicial();
    }

    private void initialize() {
        // --- PAINEL BUSCA ---
        JPanel panelBusca = new JPanel();
        panelBusca.setBorder(new TitledBorder(null, "Busca", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelBusca.setBounds(10, 10, 684, 70);
        contentPane.add(panelBusca);
        panelBusca.setLayout(null);

        JLabel lblNum = new JLabel("Selecionar OS:");
        lblNum.setBounds(10, 25, 100, 14);
        panelBusca.add(lblNum);

        // Dropdown de Ordens
        cmbNumeroOrdem = new JComboBox<>();
        cmbNumeroOrdem.setEditable(false); // Apenas seleção
        cmbNumeroOrdem.setBounds(110, 22, 280, 22);
        panelBusca.add(cmbNumeroOrdem);

        btnBuscar = new JButton("Carregar");
        btnBuscar.setBounds(400, 21, 90, 23);
        panelBusca.add(btnBuscar);

        btnLimparBusca = new JButton("Limpar");
        btnLimparBusca.setBounds(500, 21, 80, 23);
        panelBusca.add(btnLimparBusca);
        
        btnAtualizarGeral = new JButton("↻");
        btnAtualizarGeral.setToolTipText("Atualizar todas as listas");
        btnAtualizarGeral.setBounds(590, 21, 50, 23);
        panelBusca.add(btnAtualizarGeral);

        // --- PAINEL INCLUSÃO ---
        panelInclusao = new JPanel();
        panelInclusao.setBorder(new TitledBorder(new EtchedBorder(), "Nova Ordem de Serviço", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelInclusao.setBounds(10, 90, 684, 160);
        contentPane.add(panelInclusao);
        panelInclusao.setLayout(null);

        // -- CLIENTE --
        JLabel lblCpf = new JLabel("Cliente:");
        lblCpf.setBounds(10, 30, 120, 14);
        panelInclusao.add(lblCpf);
        
        // Dropdown de Clientes
        cmbCliente = new JComboBox<>();
        cmbCliente.setBounds(70, 27, 240, 22);
        panelInclusao.add(cmbCliente);
        
        // Botão + Cliente
        btnAddCliente = new JButton("+");
        btnAddCliente.setToolTipText("Cadastrar Novo Cliente");
        btnAddCliente.setBounds(315, 26, 45, 23);
        panelInclusao.add(btnAddCliente);

        // -- EQUIPAMENTO --
        JLabel lblEquip = new JLabel("Equipamento:");
        lblEquip.setBounds(370, 30, 100, 14);
        panelInclusao.add(lblEquip);
        
        // Dropdown de Equipamentos
        cmbEquipamento = new JComboBox<>();
        cmbEquipamento.setBounds(460, 27, 160, 22);
        panelInclusao.add(cmbEquipamento);
        
        // Botão + Equipamento
        btnAddEquipamento = new JButton("+");
        btnAddEquipamento.setToolTipText("Cadastrar Novo Equipamento");
        btnAddEquipamento.setBounds(625, 26, 45, 23);
        panelInclusao.add(btnAddEquipamento);

        // -- SERVIÇO E VENDEDOR --
        JLabel lblPreco = new JLabel("Serviço:");
        lblPreco.setBounds(10, 65, 120, 14);
        panelInclusao.add(lblPreco);
        
        cmbPrecoBase = new JComboBox<>();
        listaPrecos = PrecoBase.values();
        for (PrecoBase pb : listaPrecos) {
            cmbPrecoBase.addItem(pb.getDescricao());
        }
        cmbPrecoBase.setBounds(70, 62, 180, 22);
        panelInclusao.add(cmbPrecoBase);

        JLabel lblVend = new JLabel("Vendedor:");
        lblVend.setBounds(270, 65, 80, 14);
        panelInclusao.add(lblVend);
        txtVendedor = new JTextField();
        txtVendedor.setBounds(340, 62, 150, 20);
        panelInclusao.add(txtVendedor);

        btnIncluir = new JButton("Incluir Ordem");
        btnIncluir.setBounds(70, 100, 150, 30);
        panelInclusao.add(btnIncluir);

        // --- INFO STATUS ---
        lblStatusInfo = new JLabel("");
        lblStatusInfo.setBounds(20, 260, 660, 20);
        lblStatusInfo.setForeground(Color.BLUE);
        contentPane.add(lblStatusInfo);

        // --- PAINEL GESTÃO ---
        panelGestao = new JPanel();
        panelGestao.setBorder(new TitledBorder(new EtchedBorder(), "Gestão da Ordem (Cancelar / Fechar)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelGestao.setBounds(10, 290, 684, 250);
        contentPane.add(panelGestao);
        panelGestao.setLayout(null);

        // Cancelamento
        JLabel lblMotivo = new JLabel("Motivo Canc.:");
        lblMotivo.setBounds(10, 30, 100, 14);
        panelGestao.add(lblMotivo);
        txtMotivoCancelamento = new JTextField();
        txtMotivoCancelamento.setBounds(110, 27, 350, 20);
        panelGestao.add(txtMotivoCancelamento);
        
        btnCancelar = new JButton("Cancelar OS");
        btnCancelar.setForeground(Color.RED);
        btnCancelar.setBounds(480, 26, 120, 23);
        panelGestao.add(btnCancelar);

        // Separador
        JPanel separator = new JPanel();
        separator.setBorder(new EtchedBorder());
        separator.setBounds(10, 65, 660, 2);
        panelGestao.add(separator);

        // Fechamento
        JLabel lblRelatorio = new JLabel("Relatório Final:");
        lblRelatorio.setBounds(10, 85, 100, 14);
        panelGestao.add(lblRelatorio);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(110, 85, 490, 80);
        panelGestao.add(scrollPane);
        txtRelatorio = new JTextArea();
        scrollPane.setViewportView(txtRelatorio);

        JLabel lblDataFec = new JLabel("Data Fechamento:");
        lblDataFec.setBounds(10, 185, 120, 14);
        panelGestao.add(lblDataFec);
        
        txtDataFechamento = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            mask.install(txtDataFechamento);
        } catch (Exception e) {}
        txtDataFechamento.setBounds(130, 182, 100, 20);
        panelGestao.add(txtDataFechamento);

        btnFechar = new JButton("Fechar OS");
        btnFechar.setForeground(new Color(0, 128, 0));
        btnFechar.setBounds(250, 181, 120, 23);
        panelGestao.add(btnFechar);
    }

    private void configureListeners() {
        btnBuscar.addActionListener(e -> buscarOrdem());
        btnLimparBusca.addActionListener(e -> setEstadoInicial());
        btnIncluir.addActionListener(e -> incluirOrdem());
        btnCancelar.addActionListener(e -> cancelarOrdem());
        btnFechar.addActionListener(e -> fecharOrdem());
        btnAtualizarGeral.addActionListener(e -> atualizarTodasListas());
        
        btnAddCliente.addActionListener(e -> abrirTelaCliente());
        btnAddEquipamento.addActionListener(e -> abrirTelaEquipamento());
    }

    // --- MÉTODOS DE POPULAÇÃO DE COMBOS ---

    private void atualizarTodasListas() {
        carregarClientes();
        carregarEquipamentos();
        carregarOrdens();
    }

    private void carregarClientes() {
        Cliente[] clientes = clienteDAO.buscarTodos();
        Vector<ClienteItem> items = new Vector<>();
        // Adiciona opção vazia/selecione
        items.add(new ClienteItem(null, "Selecione..."));
        
        if (clientes != null) {
            for (Cliente c : clientes) {
                items.add(new ClienteItem(c, c.getNome() + " (" + formatarCpfCnpj(c.getCpfCnpj()) + ")"));
            }
        }
        cmbCliente.setModel(new DefaultComboBoxModel<>(items));
    }

    private void carregarEquipamentos() {
        Vector<EquipamentoItem> items = new Vector<>();
        items.add(new EquipamentoItem(null, null, "Selecione..."));

        Notebook[] notes = notebookDAO.buscarTodos();
        if (notes != null) {
            for (Notebook n : notes) {
                items.add(new EquipamentoItem(n.getIdTipo(), n.getSerial(), "[Note] " + n.getDescricao() + " (" + n.getSerial() + ")"));
            }
        }

        Desktop[] desks = desktopDAO.buscarTodos();
        if (desks != null) {
            for (Desktop d : desks) {
                items.add(new EquipamentoItem(d.getIdTipo(), d.getSerial(), "[Desk] " + d.getDescricao() + " (" + d.getSerial() + ")"));
            }
        }
        cmbEquipamento.setModel(new DefaultComboBoxModel<>(items));
    }

    private void carregarOrdens() {
        OrdemServico[] ordens = ordemServicoDAO.buscarTodos();
        Vector<OrdemItem> items = new Vector<>();
        items.add(new OrdemItem(null, "Selecione uma OS..."));

        if (ordens != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
            for (OrdemServico os : ordens) {
                String label = String.format("OS: %s | %s | %s", 
                        os.getNumero(), 
                        os.getCliente().getNome(), 
                        os.getDataHoraAbertura().format(fmt));
                items.add(new OrdemItem(os.getNumero(), label));
            }
        }
        cmbNumeroOrdem.setModel(new DefaultComboBoxModel<>(items));
    }
    
    // --- WRAPPERS PARA COMBOBOX ---
    
    // Wrapper para Cliente
    private class ClienteItem {
        private Cliente cliente;
        private String label;
        public ClienteItem(Cliente c, String l) { this.cliente = c; this.label = l; }
        public String toString() { return label; }
        public String getCpfCnpj() { return cliente == null ? null : cliente.getCpfCnpj(); }
    }

    // Wrapper para Equipamento
    private class EquipamentoItem {
        private String idTipo;
        private String serial;
        private String label;
        public EquipamentoItem(String t, String s, String l) { this.idTipo = t; this.serial = s; this.label = l; }
        public String toString() { return label; }
        public String getIdCompleto() { return (idTipo == null || serial == null) ? null : idTipo + serial; }
    }

    // Wrapper para Ordem
    private class OrdemItem {
        private String numero;
        private String label;
        public OrdemItem(String n, String l) { this.numero = n; this.label = l; }
        public String toString() { return label; }
        public String getNumero() { return numero; }
    }

    // --- MÉTODOS DE ABERTURA DE TELA ---
    
    private void abrirTelaCliente() {
        try {
            ClienteTela tela = new ClienteTela();
            tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Adiciona listener para recarregar lista quando fechar
            tela.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    carregarClientes();
                }
            });
            
            tela.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void abrirTelaEquipamento() {
        try {
            EquipamentoTela tela = new EquipamentoTela();
            tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Adiciona listener para recarregar lista quando fechar
            tela.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    carregarEquipamentos();
                }
            });
            
            tela.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    // --- LÓGICA DE NEGÓCIO DA TELA ---

    private void setEstadoInicial() {
        if (cmbNumeroOrdem.getItemCount() > 0) cmbNumeroOrdem.setSelectedIndex(0);
        cmbNumeroOrdem.setEnabled(true);
        btnBuscar.setEnabled(true);

        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        if (cmbEquipamento.getItemCount() > 0) cmbEquipamento.setSelectedIndex(0);
        
        txtVendedor.setText("");
        cmbPrecoBase.setSelectedIndex(0);

        txtMotivoCancelamento.setText("");
        txtRelatorio.setText("");
        txtDataFechamento.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        lblStatusInfo.setText("Selecione uma OS para gerenciar ou preencha os dados abaixo para incluir.");

        habilitarInclusao(true);
        habilitarGestao(false);
    }

    private void habilitarInclusao(boolean hab) {
        panelInclusao.setEnabled(hab);
        cmbCliente.setEnabled(hab);
        btnAddCliente.setEnabled(hab);
        cmbEquipamento.setEnabled(hab);
        btnAddEquipamento.setEnabled(hab);
        txtVendedor.setEnabled(hab);
        cmbPrecoBase.setEnabled(hab);
        btnIncluir.setEnabled(hab);
    }

    private void habilitarGestao(boolean hab) {
        panelGestao.setEnabled(hab);
        txtMotivoCancelamento.setEnabled(hab);
        btnCancelar.setEnabled(hab);
        txtRelatorio.setEnabled(hab);
        txtDataFechamento.setEnabled(hab);
        btnFechar.setEnabled(hab);
    }

    private void incluirOrdem() {
        try {
            ClienteItem cliItem = (ClienteItem) cmbCliente.getSelectedItem();
            EquipamentoItem equipItem = (EquipamentoItem) cmbEquipamento.getSelectedItem();

            if (cliItem == null || cliItem.getCpfCnpj() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um Cliente.");
                return;
            }
            if (equipItem == null || equipItem.getIdCompleto() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um Equipamento.");
                return;
            }

            String cpf = cliItem.getCpfCnpj();
            String idEquip = equipItem.getIdCompleto();
            String vend = txtVendedor.getText().trim();
            int codPreco = listaPrecos[cmbPrecoBase.getSelectedIndex()].getCodigo();

            DadosOrdemServico dados = new DadosOrdemServico(cpf, codPreco, idEquip, vend);
            LocalDateTime momentoInclusao = LocalDateTime.now();

            ResultadoMediator resultado = mediator.incluir(dados);

            if (resultado.isOperacaoRealizada()) {
                // Tenta recuperar o ID gerado
                String idGerado = tentarRecuperarIdGerado(idEquip, cpf, momentoInclusao);
                
                String msg = "Ordem de Serviço incluída com sucesso!";
                if (idGerado != null) {
                    msg += "\nNúmero da OS: " + idGerado;
                }
                
                JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Atualiza a lista de ordens e reseta a tela
                carregarOrdens();
                setEstadoInicial();
                
                // Se encontrou o ID, já seleciona ele na lista
                if (idGerado != null) {
                    selecionarOrdemNoCombo(idGerado);
                    buscarOrdem(); // Carrega os dados na tela
                }
            } else {
                exibirErros(resultado);
            }

        } catch (ExcecaoNegocio e) {
            exibirErros(e.getRes());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage());
        }
    }
    
    private void selecionarOrdemNoCombo(String numero) {
        for (int i = 0; i < cmbNumeroOrdem.getItemCount(); i++) {
            OrdemItem item = cmbNumeroOrdem.getItemAt(i);
            if (numero.equals(item.getNumero())) {
                cmbNumeroOrdem.setSelectedIndex(i);
                return;
            }
        }
    }

    // Método auxiliar para formatar CPF/CNPJ na exibição
    private String formatarCpfCnpj(String in) {
        if (in == null) return "";
        if (in.length() == 11) return in.substring(0,3) + "." + in.substring(3,6) + "." + in.substring(6,9) + "-" + in.substring(9);
        if (in.length() == 14) return in.substring(0,2) + "." + in.substring(2,5) + "." + in.substring(5,8) + "/" + in.substring(8,12) + "-" + in.substring(12);
        return in;
    }

    // Lógica para descobrir o ID que foi gerado
    private String tentarRecuperarIdGerado(String idEquipamento, String cpfCnpj, LocalDateTime referencia) {
        if (idEquipamento == null || idEquipamento.length() < 2) return null;
        
        String tipoId = idEquipamento.substring(0, 2);
        String parteCpfCnpj = cpfCnpj.replaceAll("\\D", "");
        String sufixoCpf;
        if(parteCpfCnpj.length() > 11) {
            sufixoCpf = parteCpfCnpj;
        } else {
            sufixoCpf = "000" + parteCpfCnpj;
        }

        LocalDateTime[] tentativas = { 
            referencia, 
            referencia.plusMinutes(1), 
            referencia.minusMinutes(1) 
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        for (LocalDateTime data : tentativas) {
            String dataFormatada = data.format(formatter);
            String candidatoId = tipoId + dataFormatada + sufixoCpf;
            
            OrdemServico os = mediator.buscar(candidatoId);
            if (os != null) {
                return candidatoId;
            }
        }
        return null;
    }

    private void buscarOrdem() {
        OrdemItem itemSelecionado = (OrdemItem) cmbNumeroOrdem.getSelectedItem();
        
        if (itemSelecionado == null || itemSelecionado.getNumero() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma ordem na lista.");
            return;
        }
        
        String num = itemSelecionado.getNumero();
        OrdemServico os = mediator.buscar(num);
        
        if (os == null) {
            JOptionPane.showMessageDialog(this, "Ordem não encontrada na base de dados (talvez tenha sido excluída externamente).");
            carregarOrdens(); // Atualiza a lista para remover o fantasma
            return;
        }

        // Seleciona Cliente e Equipamento nos combos correspondentes (Apenas visualização)
        selecionarClienteNoCombo(os.getCliente().getCpfCnpj());
        selecionarEquipamentoNoCombo(os.getEquipamento().getId());
        
        txtVendedor.setText(os.getVendedor());
        
        for(int i=0; i<listaPrecos.length; i++) {
            if(listaPrecos[i] == os.getPrecoBase()) {
                cmbPrecoBase.setSelectedIndex(i);
                break;
            }
        }

        String info = String.format("STATUS: %s  |  Valor: R$ %.2f  |  Prazo: %d dias  |  Aberta em: %s", 
                os.getStatus(), 
                os.getValor(), 
                os.getPrazoEmDias(),
                os.getDataHoraAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        
        lblStatusInfo.setText(info);

        habilitarInclusao(false); 
        
        if (os.getStatus() == StatusOrdem.ABERTA) {
            habilitarGestao(true);
        } else {
            habilitarGestao(false);
            JOptionPane.showMessageDialog(this, "Ordem " + os.getStatus() + ". Edição bloqueada.");
        }
    }
    
    private void selecionarClienteNoCombo(String cpfCnpj) {
        for (int i=0; i<cmbCliente.getItemCount(); i++) {
            ClienteItem item = cmbCliente.getItemAt(i);
            if (item.getCpfCnpj() != null && item.getCpfCnpj().equals(cpfCnpj)) {
                cmbCliente.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void selecionarEquipamentoNoCombo(String idEquip) {
        for (int i=0; i<cmbEquipamento.getItemCount(); i++) {
            EquipamentoItem item = cmbEquipamento.getItemAt(i);
            if (item.getIdCompleto() != null && item.getIdCompleto().equals(idEquip)) {
                cmbEquipamento.setSelectedIndex(i);
                break;
            }
        }
    }

    private void cancelarOrdem() {
        try {
            OrdemItem item = (OrdemItem) cmbNumeroOrdem.getSelectedItem();
            if (item == null || item.getNumero() == null) return;
            
            String num = item.getNumero();
            String motivo = txtMotivoCancelamento.getText();
            
            ResultadoMediator res = mediator.cancelar(num, motivo, LocalDateTime.now());
            
            if (res.isOperacaoRealizada()) {
                JOptionPane.showMessageDialog(this, "Ordem cancelada com sucesso!");
                carregarOrdens(); // Atualiza lista
                selecionarOrdemNoCombo(num); // Mantém selecionado
                buscarOrdem(); // Atualiza tela
            } else {
                exibirErros(res);
            }
        } catch (ExcecaoNegocio e) {
            exibirErros(e.getRes());
        }
    }

    private void fecharOrdem() {
        try {
            OrdemItem item = (OrdemItem) cmbNumeroOrdem.getSelectedItem();
            if (item == null || item.getNumero() == null) return;
            
            String num = item.getNumero();
            String relatorio = txtRelatorio.getText();
            String dataStr = txtDataFechamento.getText();
            
            LocalDate dataFechamento;
            try {
                dataFechamento = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Data inválida.");
                return;
            }

            FechamentoOrdemServico fechamento = new FechamentoOrdemServico(num, dataFechamento, true, relatorio);
            
            ResultadoMediator res = mediator.fechar(fechamento);
            
            if (res.isOperacaoRealizada()) {
                JOptionPane.showMessageDialog(this, "Ordem fechada com sucesso!");
                carregarOrdens();
                selecionarOrdemNoCombo(num);
                buscarOrdem();
            } else {
                exibirErros(res);
            }
        } catch (ExcecaoNegocio e) {
            exibirErros(e.getRes());
        }
    }

    private void exibirErros(ResultadoMediator res) {
        if (res.getMensagensErro() != null && res.getMensagensErro().tamanho() > 0) {
            String erros = String.join("\n", res.getMensagensErro().listar());
            JOptionPane.showMessageDialog(this, "Erros:\n" + erros, "Atenção", JOptionPane.ERROR_MESSAGE);
        }
    }
}