package br.ufrpe.poo.banco.negocio;
import br.ufrpe.poo.banco.exceptions.SaldoInsuficienteException;

import static org.junit.Assert.*;

import org.junit.Test;

public class TesteConta {

	
	@Test
	public void getSaldoTest() {
		double saldo = 200;
		Conta c1 = new Conta("1", saldo);
		assertEquals(saldo, c1.getSaldo(), 0);
	}
	
	@Test
	public void getNumeroTest() {
		String numero = "1";
		Conta c1 = new Conta(numero, 0);
		assertEquals(numero, c1.getNumero());
	}
	
	@Test
	public void setSaldoTest() {
		double saldo = 200;
		Conta c1 = new Conta("1", 0);
		c1.setSaldo(saldo);
		assertEquals(saldo, c1.getSaldo(), 0);
	}
	
	@Test
	public void setNumeroTest() {
		String numero = "1";
		Conta c1 = new Conta(null, 0);
		c1.setNumero(numero);
		assertEquals(numero, c1.getNumero());
	}
	
	@Test
	public void criarContaSaldoNegativoTest() {
		double saldo = -200;
		Conta c1 = new Conta("1", saldo);
		assertEquals(0, c1.getSaldo(), 0);
	}

	@Test
	public void creditarTest() {
		double saldoInicial = 100;
		double credito = 250;
		Conta c1 = new Conta("1", saldoInicial);
		c1.creditar(credito);
		assertEquals(350, c1.getSaldo(), 0);
	}

	@Test
	public void debitarTest() {
		double saldoInicial = 100;
		double debito = 25;
		Conta c1 = new Conta("1", saldoInicial);
		try {
			c1.debitar(debito);
		} catch (SaldoInsuficienteException e) {
			fail();
		}
		assertEquals(75, c1.getSaldo(), 0);
	}

	@Test(expected = SaldoInsuficienteException.class)
	public void debitarSaldoInsuficienteTest() 
		throws SaldoInsuficienteException {
		double saldoInicial = 25;
		double debito = 100;
		Conta c1 = new Conta("1", saldoInicial);
		c1.debitar(debito);
	}

	@Test
	public void equalsContaIsNotContaTest() {
		Conta c1 = new Conta("1", 0);
		Conta c2 = null;
		assertFalse(c1.equals(c2));
	}
}
