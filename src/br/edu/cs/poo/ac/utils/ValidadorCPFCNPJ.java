package br.edu.cs.poo.ac.utils;
import static br.edu.cs.poo.ac.utils.StringUtils.estaVazia;
public class ValidadorCPFCNPJ {
	public static ResultadoValidacaoCPFCNPJ validarCPFCNPJ(String cpfCnpj) {
	    if (estaVazia(cpfCnpj)) {
	        return new ResultadoValidacaoCPFCNPJ(false, false, ErroValidacaoCPFCNPJ.CPF_CNPJ_NAO_E_CPF_NEM_CNPJ);
	    }

	    
	    String apenasNumeros = cpfCnpj.replaceAll("[^0-9]", "");

	    
	    if (isCPF(apenasNumeros)) {
	        return new ResultadoValidacaoCPFCNPJ(true, false, null);
	    }

	    if (isCNPJ(apenasNumeros)) {
	        return new ResultadoValidacaoCPFCNPJ(false, true, null);
	    }
	    
	    if (apenasNumeros.length() == 11) {
	        return new ResultadoValidacaoCPFCNPJ(true, false, ErroValidacaoCPFCNPJ.CPF_CNPJ_COM_DV_INVALIDO);
	    }
	    
	    if (apenasNumeros.length() == 14) {
	        return new ResultadoValidacaoCPFCNPJ(false, true, ErroValidacaoCPFCNPJ.CPF_CNPJ_COM_DV_INVALIDO);
	    }
	    
	    return new ResultadoValidacaoCPFCNPJ(false, false, ErroValidacaoCPFCNPJ.CPF_CNPJ_NAO_E_CPF_NEM_CNPJ);
	}


public static boolean isCPF(String valor) {
	if(estaVazia(valor)) {
		return false;
	}
	
	if(valor.length() != 11) {
		
		return false;
	}
	
	if(isDigitoVerificadorValidoCPF(valor) == false) {
		return false;
	}
	
	return true;
}
public static boolean isCNPJ(String valor) {
	
	if(estaVazia(valor)) {
		return false;
	}
	
	if(valor.length() != 14) {
		
		return false;
	}
	
	if(isDigitoVerificadorValidoCNPJ(valor) == false) {
		return false;
	}
	
	return true;
}
	
public static ErroValidacaoCPFCNPJ validarCPF(String cpf) {
	if(estaVazia(cpf) == true || cpf.length()!= 11 ) {
		
		return ErroValidacaoCPFCNPJ.CPF_CNPJ_NAO_E_CPF_NEM_CNPJ;
	}
	if(isDigitoVerificadorValidoCPF(cpf) == false) {
		
		return ErroValidacaoCPFCNPJ.CPF_CNPJ_COM_DV_INVALIDO;
	}
	return null;
}
public static ErroValidacaoCPFCNPJ validarCNPJ(String cnpj) {
	
	if(estaVazia(cnpj) == true || cnpj.length()!= 14 ) {
		
		return ErroValidacaoCPFCNPJ.CPF_CNPJ_NAO_E_CPF_NEM_CNPJ;
	}
	if(isDigitoVerificadorValidoCPF(cnpj) == false) {
		
		return ErroValidacaoCPFCNPJ.CPF_CNPJ_COM_DV_INVALIDO;
	}
	return null;
	
	
}


private static boolean isDigitoVerificadorValidoCPF(String cpf) {
	int soma1 = 0;
	int soma2 = 0;
	int mult1 = 10;
	int mult2 = 11;
	for (int i = 0; i<9; i++) {
		
		int valor = Character.getNumericValue(cpf.charAt(i));
		soma1 = soma1 + (valor*mult1);
		mult1--;
	}
	int num1 = soma1%11;
	num1 = 11 - num1;
	if(num1 == 11 || num1 == 10) {
		
		num1 = 0;
	}
	if(num1 != Character.getNumericValue(cpf.charAt(9))) {
		
		return false;
	}
	
	for (int i = 0; i<10; i++) {
		
		int valor = Character.getNumericValue(cpf.charAt(i));
		soma2 = soma2 + (valor*mult2);
		mult2--;
	}
	int num2 = soma2 %11;
	num2 = 11 - num2;
	
	if(num2 == 11 || num2 == 10) {
		
		num2 = 0;
	}
	
	if(num2 != Character.getNumericValue(cpf.charAt(10))) {
		return false;
	}
	
	return true;
}
private static boolean isDigitoVerificadorValidoCNPJ(String cnpj) {
	
	int[] Peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	int[] Peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	int soma1 = 0;
	int soma2 = 0;
	for(int i = 0; i<12; i++) {
		int valor = Character.getNumericValue(cnpj.charAt(i));
		soma1 = soma1 + (valor*Peso1[i]);
	}
	
	int num1  = soma1%11;
	num1 = 11 - num1;

	if(num1 == 11 || num1 == 10) {
		
		num1 = 0;
	}
	if(num1 != Character.getNumericValue(cnpj.charAt(12))) {
		
		return false;
	}
	
	for(int i = 0; i<13; i++) {
		int valor = Character.getNumericValue(cnpj.charAt(i));
		soma2 = soma2 + (valor*Peso2[i]);
	}
	
	int num2  = soma2%11;
	num2= 11 - num2;
	
	if(num2 == 11 || num2 == 10) {
		
		num2 = 0;
	}
	if(num2 != Character.getNumericValue(cnpj.charAt(13))) {
		
		return false;
	}
	return true;
	
}
}