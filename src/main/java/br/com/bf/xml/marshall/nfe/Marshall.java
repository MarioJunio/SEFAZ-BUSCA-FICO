package br.com.bf.xml.marshall.nfe;

import java.util.Map;

public abstract class Marshall {

	public String prepare(String xml) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(xml.replaceAll(":ns2|ns2:|xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\"", ""));

		return sb.toString();
	}
	
	public abstract String marshall(Map<String, ?> arguments) throws Exception;
}
