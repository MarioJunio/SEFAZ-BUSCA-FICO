package br.com.bf.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {

	public static String compress(String str) throws Exception {

		if (str == null || str.length() == 0) {
			return str;
		}

		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(obj);
		
		// compacta os bytes
		gzip.write(str.getBytes("UTF-8"));
		
		gzip.close();
		obj.close();
		
		return obj.toString("UTF-8");
	}

	public static String decompress(byte[] conteudo) throws Exception {

		if (conteudo == null || conteudo.length == 0) {
			return "";
		}

		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(conteudo));
		BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		String outStr = "";
		String line;

		while ((line = bf.readLine()) != null) {
			outStr += line;
		}

		bf.close();
		gis.close();

		return outStr;
	}

}
