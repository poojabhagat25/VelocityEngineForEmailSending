package com.example.rest.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class TokenGenerator
{

	private static Logger logger = Logger.getLogger(TokenGenerator.class);

	/**
	 * This will generate token
	 * 
	 * @param email
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateToken(String email)
	{
		StringBuffer authKey = new StringBuffer();
		try
		{
			byte[] bytesOfMessage = (String.valueOf(System.currentTimeMillis()) + email).getBytes();

			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] thedigest = md.digest(bytesOfMessage);

			for (byte b : thedigest)
			{
				authKey.append(Integer.toHexString((int) (b & 0xff)));
			}
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		return authKey.toString();
	}
}
