package com.springboot.primefaces;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TokenGenerator {

	private static final String BASE_VALUE = "ANTI-CSRF";

	public static String generateToken() {
		String token = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(generateSalt());
			byte[] bytes = md.digest(BASE_VALUE.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			token = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return token;
	}

	private static byte[] generateSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

}
