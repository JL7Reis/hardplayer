package com.softplayer.util;

import java.util.regex.Pattern;

public class Validator {
	
	public static boolean isValidEmail(String email) {
		if (email == null || email.isEmpty()) {
			return false;
		}
		
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; 
        Pattern pat = Pattern.compile(emailRegex); 
        return email != null ? pat.matcher(email).matches() : false; 
    } 
	
	public static boolean isValidCpf(String cpf) {
		if (cpf == null || cpf.isEmpty() || !(cpf.length() == 11)) {
			return false;
		}
		
		String firstChar = cpf.substring(0, 1);
		String removeRepeat = cpf.replaceAll(firstChar, "");

		if(removeRepeat.length() == 0) {
			return false;
		}
		
	    char dig10, dig11;
	    int sm, i, r, num, peso;
	      
	    sm = 0;
	    peso = 10;
	    
	    for (i=0; i<9; i++) {                    
	        num = (int)(cpf.charAt(i) - 48); 
	        sm = sm + (num * peso);
	        peso = peso - 1;
	    }
	      
	    r = 11 - (sm % 11);
	    
	    if ((r == 10) || (r == 11)) {
	        dig10 = '0';
	    } else { 
	    	dig10 = (char)(r + 48);
	        sm = 0;
	        peso = 11;
	        for (i=0; i<10; i++) {
		        num = (int)(cpf.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso - 1;
	    	}
	    }
	      
	    r = 11 - (sm % 11);
	    
	    if ((r == 10) || (r == 11)) {
			dig11 = '0';
	    } else { 
	    	dig11 = (char)(r + 48);
	    }
	    
	    if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
	         return true;
	    } else {
			return false;
		}
	}
}