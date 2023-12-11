package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import br.ufrpe.poo.banco.exceptions.ClienteJaPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoPossuiContaException;

/**
 * Classe de teste responsável por testar as condições dos métodos
 * adicionarConta e removerConta da classe Cliente.
 * 
 * @author @SteffanoP
 * 
 */
public class TesteCliente {

	/**
	 * Testa se os parâmetros de Cliente retornam de acordo
	 */
	@Test
	public void getNomeTest() {
		String nome = "NomeDoCliente";
		Cliente c1 = new Cliente(nome, "123");
		assertEquals(nome, c1.getNome());
	}

	@Test
	public void getCpfTest() {
		String cpf = "123.456.789-01";
		Cliente c1 = new Cliente("nome", cpf);
		assertEquals(cpf, c1.getCpf());
	}

	/**
	 * Testa se os parâmetros setados retornam de acordo
	 */
	@Test
	public void setNomeTest() {
		String nome = "NomeDoCliente";
		Cliente c1 = new Cliente(null, "123");
		c1.setNome(nome);
		assertEquals(nome, c1.getNome());
	}

	@Test
	public void setCpfTest() {
		String cpf = "123.456.789-01";
		Cliente c1 = new Cliente("nome", null);
		c1.setCpf(cpf);
		assertEquals(cpf, c1.getCpf());
	}

	/*
	 * Testa se método toString está parametrizando corretamente
	 */
	@Test
	public void toStringTest() {
		String nome = "NomeDoCliente";
		String cpf = "123.456.789-01";
		String toString = "Nome: NomeDoCliente\nCPF: 123.456.789-01\nContas: [1, 2]";
		Cliente c1 = new Cliente(nome, cpf);
		try {
			c1.adicionarConta("1");
			c1.adicionarConta("2");
		} catch (ClienteJaPossuiContaException e) {
			fail();
		}
		assertEquals(toString, c1.toString());
	}

	/**
	 * Testa a inserção de uma nova conta vinculada ao cliente
	 */
	@Test
	public void adicionarContaTest() {
		Cliente c1 = new Cliente("nome", "123");
		try {
			c1.adicionarConta("1");
		} catch (ClienteJaPossuiContaException e) {
			fail();
		}
		assertEquals(c1.procurarConta("1"), 0);
	}

	/*
	 * Testa a consulta de todas as contas na lista de contas
	 */
	@Test
	public void getContasTest() {
		Cliente c1 = new Cliente("c1", "123");
		String numeroConta1 = "1";
		String numeroConta2 = "2";
		String numeroConta3 = "3";
		try {
			c1.adicionarConta(numeroConta1);
			c1.adicionarConta(numeroConta2);
			c1.adicionarConta(numeroConta3);
		} catch (ClienteJaPossuiContaException e) {
			fail();
		}
		ArrayList<String> contasList = new ArrayList<>();
		contasList = c1.getContas();
		assertEquals(numeroConta1, contasList.get(0));
		assertEquals(numeroConta2, contasList.get(1));
		assertEquals(numeroConta3, contasList.get(2));
	}

	/*
	 * Testa a consulta de uma conta, por número de conta
	 * de um cliente
	 */
	@Test
	public void consultarNumeroContaTest() {
		Cliente c1 = new Cliente("NomeDoCliente", "123.456.789-01");
		String numeroConta = "1";
		try {
			c1.adicionarConta(numeroConta);
		} catch (ClienteJaPossuiContaException e) {
			fail();
		}
		assertEquals(numeroConta, c1.consultarNumeroConta(0));
	}

	/*
	 * Testa a remoção de todos os números de contas de um Cliente
	 */
	@Test
	public void removerTodasAsContasTest() {
		Cliente c1 = new Cliente("c1", "123");
		String numeroConta1 = "1";
		String numeroConta2 = "2";
		String numeroConta3 = "3";
		try {
			c1.adicionarConta(numeroConta1);
			c1.adicionarConta(numeroConta2);
			c1.adicionarConta(numeroConta3);
		} catch (ClienteJaPossuiContaException e) {
			fail();
		}
		ArrayList<String> contasList = c1.getContas();
		c1.removerTodasAsContas();
		assertNotEquals(contasList, c1.getContas());
		assertNull(c1.getContas());
	}

	/**
	 * Testa a condição da tentativa de adicionar uma conta j� existente � lista
	 * de contas do cliente
	 * 
	 * @throws ClienteJaPossuiContaException
	 */
	@Test(expected = ClienteJaPossuiContaException.class)
	public void adicionarContaJaExistenteTest()
			throws ClienteJaPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.adicionarConta("1"); // adiciona a conta a 1� vez
		c1.adicionarConta("1"); // tentativa de adicionar a mesma conta
								// novamente
	}

	/**
	 * Teste a remo��o de uma conta da lista de contas do cliente
	 */
	@Test
	public void removerContaClienteTest() {
		Cliente c1 = new Cliente("nome", "123");
		try {
			c1.adicionarConta("1"); // adiciona conta com n�mero 1
			c1.removerConta("1"); // remove a conta de n�mero 1
		} catch (Exception e) {
			fail("Exceção inesperada lancada!");
		}

		assertEquals(c1.procurarConta("1"), -1);
	}

	/*
	 * Testa método Equals para nomes de cliente iguais
	 */
	@Test
	public void equalsNomeTest() {
		String nomeCliente1 = "NomeDoCliente1";
		Cliente c1 = new Cliente(nomeCliente1, "123");
		Cliente c2 = new Cliente(nomeCliente1, "456");
		assertFalse(c1.equals(c2));
	}

	/*
	 * Testa método Equals para CPFs de cliente iguais
	 */
	@Test
	public void equalsCPFTest() {
		String cpfCliente1 = "123.456.789-01";
		Cliente c1 = new Cliente("nome1", cpfCliente1);
		Cliente c2 = new Cliente("nome2", cpfCliente1);
		assertTrue(c1.equals(c2));
	}

	/*
	 * Testa método Equals para quando não há instância de cliente
	 */
	@Test
	public void equalsClienteIsNotClienteTest() {
		Cliente c1 = new Cliente("nome", "123");
		Cliente c2 = null;
		assertFalse(c1.equals(c2));
	}

	/**
	 * Testa a remoção de uma determinada conta que não está vinculada ao
	 * cliente
	 * 
	 * @throws ClienteNaoPossuiContaException
	 */
	@Test(expected = ClienteNaoPossuiContaException.class)
	public void removerContaClienteSemContaTest()
			throws ClienteNaoPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.removerConta("1"); // tenta remover a conta de um cliente sem contas
	}

}
