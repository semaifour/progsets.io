package io.progsets.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.progsets.common.Appproperties;


@Component
public class Cryptor {
	
	static Logger LOG = LoggerFactory.getLogger(Cryptor.class.getName());
	
	byte[] CRYPT_KEY = new byte[] { 0x011, (byte) 0xff, 0x33, (byte) 0xee, (byte) 0x55, (byte) 0xdd, (byte) 0x77, (byte) 0xbb };
	
	private static Cipher ecipher;

	private static Cipher dcipher;

	private static SecretKey syskey;
	
	@Autowired
	Appproperties approperties;

	public Cryptor()  {
	}
	
	@PostConstruct
	public void init() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		String k = approperties.props().getProperty("progsets.cryptkey");
		if (k != null) {
			try {
				CRYPT_KEY = k.getBytes("UTF-8");
			} catch (Exception e) {
				LOG.warn("Failed to use cryptkey [%s]. Continuing with default key. Try keys with 8 chars.", k, e);
			}
		}
		syskey = new SecretKeySpec(CRYPT_KEY, "DES");
		ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	
		ecipher.init(Cipher.ENCRYPT_MODE, syskey);
		dcipher.init(Cipher.DECRYPT_MODE, syskey);
		
	}
	

	
	public String encrypt(String data) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return Hex.encodeHexString(ecipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
	}
	
	public String decrypt(String encoded) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, DecoderException {
		return new String(dcipher.doFinal(Hex.decodeHex(encoded.toCharArray())), StandardCharsets.UTF_8);
	}
	
	public String iencrypt(String encoded) {
		try {
			return new String(dcipher.doFinal(Hex.decodeHex(encoded.toCharArray())), StandardCharsets.UTF_8);
		} catch (Exception e) {}
		return encoded;
	}
}
