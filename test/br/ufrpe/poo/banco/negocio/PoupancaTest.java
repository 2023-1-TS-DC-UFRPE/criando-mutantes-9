package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PoupancaTest {

	@Test
	public final void testeJuros() {
		Poupanca x = new Poupanca("0001",100);
		x.renderJuros(0.05);
		System.out.println(x.getSaldo());
		assertEquals("Juros 0.05(5%), Saldo Inicial 100: Saldo final 105 ", 105.0, x.getSaldo(),0);
	}
	
	
	
}
