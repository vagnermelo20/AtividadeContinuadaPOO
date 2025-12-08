package br.edu.cs.poo.ac.utils;

public class StringUtils {
	
	
public static boolean estaVazia(String str) {
	
	if (str == null){
		return true;
	}
	
	if(str.trim().isEmpty()) {
		
		return true;
	}
	
	return false;
}
public static boolean tamanhoExcedido(String str, int tamanho) {
	if(str == null && tamanho < 1) {
		
		return false;
	}
	else if(str == null && tamanho >= 1) {
		return true;
	}
	
	if (tamanho < 0) {
		
		return false;
	}

	if(str.length() <= tamanho){
		
		return false;
	}
	return true;
		
		
}



public static boolean emailValido(String email) {
	int temArroba = 0;
	int temPonto = 0;
	if(email == null || email.trim().isEmpty()) {
		
		return false;
	}
	for (int i = 0; i<email.length(); i++) {
		
		if(email.charAt(i) == ' ') {
			return false;
		}
		if (email.charAt(i) == '@') {
			
			temArroba ++;
		}
		
		if (email.charAt(i) == '.') {
			
			temPonto ++;
		}
		
	}
	if (temPonto >= 1 && temArroba == 1) {
		
		return true;
	}
	else {
		return false;
	}
	
}
public static boolean telefoneValido(String tel) {
	
	if (tel == null) {
		
		return false;
	}
	
	if (tel.length() != 12 && tel.length() != 13 && tel.length() != 14) {
		
		return false;
	}
			
	if (tel.charAt(0)!= '(' || tel.charAt(3) != ')') {
			
			return false;
		}

	return true;

	
	
}

public static boolean tamanhoMenor(String str, int tamanho) {
	if (tamanho <= 0) {
		return false;
	}
	if (str == null) {
		return true;
	}
	return str.length() < tamanho;
}

}