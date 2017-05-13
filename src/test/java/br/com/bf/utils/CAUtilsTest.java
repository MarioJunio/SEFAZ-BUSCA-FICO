package br.com.bf.utils;

import org.junit.Test;

public class CAUtilsTest {

	@Test
	public void gerarCA() {

		for (int i = 0; i < 1000; i++) {
			
			try {
				System.out.println(CAUtils.getCA(CAUtils.getRandomNumberBetween(1, 1000)));
				Thread.sleep(0l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void gerarAleatorio() {
		
		for (int i = 0; i < 100; i++) {
			System.out.println(CAUtils.getRandomNumberBetween(10, 100));
		}
		
	}
}
