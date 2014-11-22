package de.slackspace.openkeepass.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.slackspace.openkeepass.exception.KeepassDatabaseUnreadable;

public class Aes {

	private static final String KEY_ALGORITHM = "AES";
	
	public static byte[] decrypt(byte[] key, byte[] ivRaw, byte[] encryptedData) {
		
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			Key aesKey = new SecretKeySpec(key, KEY_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(ivRaw);
			c.init(Cipher.DECRYPT_MODE, aesKey, iv);
			return c.doFinal(encryptedData);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw createCryptoException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw createCryptoException(e);
		} catch (IllegalBlockSizeException e) {
			throw createCryptoException(e);
		} catch (BadPaddingException e) {
			throw createCryptoException(e);
		}
	}

	public static byte[] transformKey(byte[] key, byte[] data, long rounds) {
		try {
			Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
			Key aesKey = new SecretKeySpec(key, KEY_ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, aesKey);
			
			for (long i = 0; i < rounds; ++i) {
				c.update(data, 0, 16, data, 0);
				c.update(data, 16, 16, data, 16);
			}
			
			return data;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("The key has the wrong size. Have you installed Java Cryptography Extension (JCE)?", e);
		} catch (ShortBufferException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static KeepassDatabaseUnreadable createCryptoException(Throwable e) {
		return new KeepassDatabaseUnreadable("Could not decrypt keepass file. Master key wrong?", e);
	}
}
