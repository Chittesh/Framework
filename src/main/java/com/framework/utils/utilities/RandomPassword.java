package com.framework.utils.utilities;

import java.util.Random;

public class RandomPassword {

	private Random rnd = new Random();
	
    public String getSaltString() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~`!@#$%^&*()_+=-[]{}|\\:;\"'<,>.?/";
        
        StringBuilder salt = new StringBuilder();
        
        while (salt.length() < 15) { 
            int index = (int) (rnd.nextInt() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }

        return salt.toString();
    }
}