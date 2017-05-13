package br.com.bf.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.springframework.util.StringUtils;

public class CAUtils {

	public static String getCA(int seed) {
		String ca = null;

		try {
			ca = String.valueOf(System.currentTimeMillis() + seed);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			ca = (new HexBinaryAdapter()).marshal(md5.digest(ca.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return ca;
	}

	public static int getRandomNumberBetween(int a, int b) {
		return new Random().nextInt(b - a) + a;
	}

	public static boolean isCAValid(String ca) {
		return !StringUtils.isEmpty(ca) && ca.length() == 32;
	}

}
