package br.edu.cs.poo.ac.ordem.mediators;

import br.edu.cs.poo.ac.ordem.daos.ClienteDAO;
import br.edu.cs.poo.ac.ordem.entidades.Cliente;
import br.edu.cs.poo.ac.ordem.entidades.Contato;
import br.edu.cs.poo.ac.utils.ErroValidacaoCPFCNPJ;
import br.edu.cs.poo.ac.utils.ListaString;
import br.edu.cs.poo.ac.utils.ResultadoValidacaoCPFCNPJ;
import br.edu.cs.poo.ac.utils.ValidadorCPFCNPJ;
import static br.edu.cs.poo.ac.utils.StringUtils.estaVazia;
import static br.edu.cs.poo.ac.utils.StringUtils.telefoneValido;
import java.time.LocalDate;
import static br.edu.cs.poo.ac.utils.StringUtils.emailValido;

public class ClienteMediator {

    private static final String CLIENTE_NAO_INFORMADO = "Cliente não informado";
    private static final String CPF_CNPJ_NAO_INFORMADO = "CPF/CNPJ não informado";
    private static final String NOME_NAO_INFORMADO = "Nome não informado";
    private static final String CONTATO_NAO_INFORMADO = "Contato não informado";
    private static final String DATA_DO_CADASTRO_NAO_INFORMADA = "Data do cadastro não informada";
    private static final String NAO_E_CPF_NEM_CNJP = "Não é CPF nem CNJP";
    private static final String CPF_OU_CNPJ_COM_DIGITO_VERIFICADOR_INVALIDO = "CPF ou CNPJ com dígito verificador inválido";
    private static final String CPF_CNPJ_JA_EXISTENTE = "CPF/CNPJ já existente";
    private static final String CPF_CNPJ_INEXISTENTE = "CPF/CNPJ inexistente";
    private static final String NOME_MUITO_LONGO = "Nome tem mais de 50 caracteres";
    private static final String DATA_CADASTRO_FUTURA = "Data do cadastro não pode ser posterior à data atual";
    private static final String CELULAR_E_EMAIL_NAO_INFORMADOS = "Celular e e-mail não foram informados";
    private static final String CELULAR_NAO_INFORMADO_E_INDICADOR_DE_ZAP_ATIVO = "Celular não informado e indicador de zap ativo";
    private static final String CELULAR_FORMATO_INVALIDO = "Celular está em um formato inválido";
    private static final String EMAIL_FORMATO_INVALIDO = "E-mail está em um formato inválido";
    private static ClienteMediator instancia;

    private ClienteDAO clienteDAO;

    private ClienteMediator() {
        this.clienteDAO = new ClienteDAO();
    }

    public static ClienteMediator getInstancia() {
        if (instancia == null) {
            instancia = new ClienteMediator();
        }
        return instancia;
    }

    public ResultadoMediator incluir(Cliente cliente) {
        ResultadoMediator resultadoValidacao = this.validar(cliente);
        if (!resultadoValidacao.isValidado()) {
            return resultadoValidacao;
        }
        if (clienteDAO.buscar(cliente.getCpfCnpj()) != null) {
            ListaString erros = new ListaString();
            erros.adicionar(CPF_CNPJ_JA_EXISTENTE);
            return new ResultadoMediator(true, false, erros);
        }
        boolean operacaoRealizada = clienteDAO.incluir(cliente);
        return new ResultadoMediator(true, operacaoRealizada, new ListaString());
    }

    public ResultadoMediator alterar(Cliente cliente) {
        ResultadoMediator resultadoValidacao = this.validar(cliente);
        if (!resultadoValidacao.isValidado()) {
            return resultadoValidacao;
        }
        if (clienteDAO.buscar(cliente.getCpfCnpj()) == null) {
            ListaString erros = new ListaString();
            erros.adicionar(CPF_CNPJ_INEXISTENTE);
            return new ResultadoMediator(true, false, erros);
        }
        boolean operacaoRealizada = clienteDAO.alterar(cliente);
        return new ResultadoMediator(true, operacaoRealizada, new ListaString());
    }

    public ResultadoMediator excluir(String cpfCnpj) {
        if (estaVazia(cpfCnpj)) {
            ListaString erros = new ListaString();
            erros.adicionar(CPF_CNPJ_NAO_INFORMADO);
            return new ResultadoMediator(false, false, erros);
        }
        if (clienteDAO.buscar(cpfCnpj) == null) {
            ListaString erros = new ListaString();
            erros.adicionar(CPF_CNPJ_INEXISTENTE);
            return new ResultadoMediator(true, false, erros);
        }
        boolean operacaoRealizada = clienteDAO.excluir(cpfCnpj);
        return new ResultadoMediator(true, operacaoRealizada, new ListaString());
    }

    public Cliente buscar(String cpfCnpj) {
        if (estaVazia(cpfCnpj)) {
            return null;
        }
        return clienteDAO.buscar(cpfCnpj);
    }

    public ResultadoMediator validar(Cliente cliente) {
        ListaString erros = new ListaString();

        if (cliente == null) {
            erros.adicionar(CLIENTE_NAO_INFORMADO);
            return new ResultadoMediator(false, false, erros);
        }

        if (estaVazia(cliente.getCpfCnpj())) {
            erros.adicionar(CPF_CNPJ_NAO_INFORMADO);
        } else {
            ResultadoValidacaoCPFCNPJ resultadoValidacao = ValidadorCPFCNPJ.validarCPFCNPJ(cliente.getCpfCnpj());
            if (resultadoValidacao.getErroValidacao() != null) {
                ErroValidacaoCPFCNPJ erro = resultadoValidacao.getErroValidacao();
                if (erro == ErroValidacaoCPFCNPJ.CPF_CNPJ_NAO_E_CPF_NEM_CNPJ) {
                    erros.adicionar(NAO_E_CPF_NEM_CNJP);
                } else if (erro == ErroValidacaoCPFCNPJ.CPF_CNPJ_COM_DV_INVALIDO) {
                    erros.adicionar(CPF_OU_CNPJ_COM_DIGITO_VERIFICADOR_INVALIDO);
                }
            }
        }

        if (estaVazia(cliente.getNome())) {
            erros.adicionar(NOME_NAO_INFORMADO);
        } else if (cliente.getNome().length() > 50) {
            erros.adicionar(NOME_MUITO_LONGO);
        }

        Contato contato = cliente.getContato();
        if (contato == null) {
            erros.adicionar(CONTATO_NAO_INFORMADO);
            if (cliente.getDataCadastro() == null) {
                erros.adicionar(DATA_DO_CADASTRO_NAO_INFORMADA);
            } else if (cliente.getDataCadastro().isAfter(LocalDate.now())) {
                erros.adicionar(DATA_CADASTRO_FUTURA);
            }
        } else {
            if (cliente.getDataCadastro() == null) {
                erros.adicionar(DATA_DO_CADASTRO_NAO_INFORMADA);
            } else if (cliente.getDataCadastro().isAfter(LocalDate.now())) {
                erros.adicionar(DATA_CADASTRO_FUTURA);
            }

            // Agora, validações específicas do contato existente
            if (estaVazia(contato.getEmail()) && estaVazia(contato.getCelular())) {
                erros.adicionar(CELULAR_E_EMAIL_NAO_INFORMADOS);
            }
            if (estaVazia(contato.getCelular()) && contato.isEhZap()) {
                erros.adicionar(CELULAR_NAO_INFORMADO_E_INDICADOR_DE_ZAP_ATIVO);
            }
            if (!estaVazia(contato.getEmail()) && !emailValido(contato.getEmail())) {
                erros.adicionar(EMAIL_FORMATO_INVALIDO);
            }
            if (!estaVazia(contato.getCelular()) && !telefoneValido(contato.getCelular())) {
                erros.adicionar(CELULAR_FORMATO_INVALIDO);
            }
        }

        if (erros.tamanho() > 0) {
            return new ResultadoMediator(false, false, erros);
        } else {
            return new ResultadoMediator(true, false, erros);
        }
    }
}