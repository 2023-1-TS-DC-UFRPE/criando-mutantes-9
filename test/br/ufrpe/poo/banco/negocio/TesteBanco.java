package br.ufrpe.poo.banco.negocio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import br.ufrpe.poo.banco.dados.*;
import br.ufrpe.poo.banco.exceptions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TesteBanco {

	private static Banco banco;
	private final IRepositorioContas repositorioContas = new RepositorioContasArray();
	private final IRepositorioClientes repositorioClientes = new RepositorioClientesArray();

	public TesteBanco() throws RepositorioException {
	}

	@Before
	public void apagarArquivos() throws IOException, RepositorioException,
			InicializacaoSistemaException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("clientes.dat"));
		bw.close();
		bw = new BufferedWriter(new FileWriter("contas.dat"));
		bw.close();
		
		Banco.instance = null;
		TesteBanco.banco = Banco.getInstance();
	}

	@Test
	public void testeCadastrarCliente() throws RepositorioException, ClienteJaCadastradoException{
		Cliente cliente1 = new Cliente("Carlos", "12345678901");
		Cliente cliente2 = new Cliente("Ana", "10987654321");
		banco.cadastrarCliente(cliente1);
		banco.cadastrarCliente(cliente2);

		banco.procurarCliente("12345678901");
		assertEquals(Integer.parseInt(cliente1.getCpf()), Integer.parseInt(cliente2.getCpf()));
	}
	/**
	 * Verifica o cadastramento de uma nova conta.
	 * 
	 */
	@Test
	public void testeCadastarNovaConta() throws RepositorioException,
			ContaJaCadastradaException, ContaNaoEncontradaException,
			InicializacaoSistemaException {

		Banco banco = new Banco(null, new RepositorioContasArquivoBin());
		ContaAbstrata conta1 = new Conta("1", 100);
		banco.cadastrar(conta1);
		ContaAbstrata conta2 = banco.procurarConta("1");
		assertEquals(conta1.getNumero(), conta2.getNumero());
		assertEquals(conta1.getSaldo(), conta2.getSaldo(), 0);
	}

	/**
	 * Verifica que nao e permitido cadastrar duas contas com o mesmo numero.
	 * 
	 */
	@Test(expected = ContaJaCadastradaException.class)
	public void testeCadastrarContaExistente() throws RepositorioException,
			ContaJaCadastradaException, ContaNaoEncontradaException,
			InicializacaoSistemaException {

		Conta c1 = new Conta("1", 200);
		Conta c2 = new Conta("1", 300);
		banco.cadastrar(c1);
		banco.cadastrar(c2);
		fail("Excecao ContaJaCadastradaException nao levantada");
	}

	/**
	 * Verifica se o credito esta sendo executado corretamente em uma conta
	 * corrente.
	 * 
	 */
	@Test
	public void testeCreditarContaExistente() throws RepositorioException,
			ContaNaoEncontradaException, InicializacaoSistemaException,
			ContaJaCadastradaException, ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 100);
		banco.cadastrar(conta);
		banco.creditar(conta, 100);
		conta = banco.procurarConta("1");
		assertEquals(200, conta.getSaldo(), 0);
	}

	/**
	 * Verifica a excecao levantada na tentativa de creditar em uma conta que
	 * nao existe.
	 * 
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void testeCreditarContaInexistente() throws RepositorioException,
			ContaNaoEncontradaException, InicializacaoSistemaException,
			ValorInvalidoException {

		banco.creditar(new Conta("", 0), 200);

		fail("Excecao ContaNaoEncontradaException nao levantada");
	}

	@Test(expected = ValorInvalidoException.class)
	public void testeCreditarContaInexistente2() throws RepositorioException,
			ContaNaoEncontradaException, InicializacaoSistemaException,
			ValorInvalidoException {

		banco.creditar(new Conta("", 0), -1);

		fail("Excecao ContaNaoEncontradaException nao levantada");
	}

	/**
	 * Verifica que a operacao de debito em conta corrente esta acontecendo
	 * corretamente.
	 * 
	 */
	@Test
	public void testeDebitarContaExistente() throws RepositorioException,
			ContaNaoEncontradaException, SaldoInsuficienteException,
			InicializacaoSistemaException, ContaJaCadastradaException,
			ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 50);
		banco.cadastrar(conta);
		banco.debitar(conta, -1);
		conta = banco.procurarConta("1");
		assertEquals(0, conta.getSaldo(), 0);
	}

	@Test(expected = ValorInvalidoException.class)
	public void testeDebitarContaExistente2() throws RepositorioException,
			ContaNaoEncontradaException, SaldoInsuficienteException,
			InicializacaoSistemaException, ContaJaCadastradaException,
			ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 50);
		banco.cadastrar(conta);
		banco.debitar(conta, 50);
		conta = banco.procurarConta("1");
		assertEquals(0, conta.getSaldo(), 0);
	}

	/**
	 * Verifica que tentantiva de debitar em uma conta que nao existe levante
	 * excecao.
	 * 
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void testeDebitarContaInexistente() throws RepositorioException,
			ContaNaoEncontradaException, SaldoInsuficienteException,
			InicializacaoSistemaException, ValorInvalidoException {

		banco.debitar(new Conta("", 0), 50);
		fail("Excecao ContaNaoEncontradaException nao levantada");
	}

	/**
	 * Verifica que a transferencia entre contas correntes e realizada com
	 * sucesso.
	 * 
	 */
	@Test
	public void testeTransferirContaExistente() throws RepositorioException,
			ContaNaoEncontradaException, SaldoInsuficienteException,
			InicializacaoSistemaException, ContaJaCadastradaException,
			ValorInvalidoException {

		ContaAbstrata conta1 = new Conta("1", 100);
		ContaAbstrata conta2 = new Conta("2", 200);
		banco.cadastrar(conta1);
		banco.cadastrar(conta2);
		banco.transferir(conta1, conta2, 50);
		conta1 = banco.procurarConta("1");
		conta2 = banco.procurarConta("2");
		assertEquals(50, conta1.getSaldo(), 0);
		assertEquals(250, conta2.getSaldo(), 0);
	}

	/**
	 * Verifica que tentativa de transferir entre contas cujos numeros nao
	 * existe levanta excecao.
	 * 
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void testeTransferirContaInexistente() throws RepositorioException,
			ContaNaoEncontradaException, SaldoInsuficienteException,
			InicializacaoSistemaException, ValorInvalidoException {
			// TODO: Possiblitar a passagem do teste pelas duas decisões da branch
		banco.transferir(new Conta("", 0), new Conta("", 0), 50);
		fail("Excecao ContaNaoEncontradaException nao levantada)");
	}


	@Test(expected = ContaNaoEncontradaException.class)
	public void testeRemoverContaInexistente() throws ContaNaoEncontradaException, RepositorioException, ClienteJaCadastradoException, ContaJaCadastradaException, ClienteJaPossuiContaException, ClienteNaoPossuiContaException {
		Cliente cliente = new Cliente("Joao", "11122233344");
		banco.cadastrarCliente(cliente);
		cliente.adicionarConta("12321");
		banco.removerConta(cliente, "12321");
	}

	@Test(expected = ClienteNaoPossuiContaException.class)
	public void testeRemoverContaClienteNaoPossui() throws ClienteJaCadastradoException, RepositorioException, ContaJaCadastradaException, ClienteNaoPossuiContaException, ContaNaoEncontradaException {
		Cliente cliente = new Cliente("Joao", "11122233344");
		banco.cadastrarCliente(cliente);
		Conta conta = new Conta("111", 100);
		banco.cadastrar(conta);
		banco.removerConta(cliente, "111");
	}

	@Test
	public void testeRemoverConta() throws ClienteNaoPossuiContaException, ContaNaoEncontradaException, RepositorioException, ClienteJaPossuiContaException, ClienteJaCadastradoException, ContaJaCadastradaException {
		Cliente cliente = new Cliente("Joao", "11122233344");
		banco.cadastrarCliente(cliente);
		Conta conta = new Conta("12321", 100);
		banco.cadastrar(conta);
		cliente.adicionarConta("12321");
		banco.removerConta(cliente, "12321");
		assertNull(banco.procurarConta("12321"));
	}

	@Test
	public void testeAtualizarCliente() throws ClienteJaCadastradoException, RepositorioException, AtualizacaoNaoRealizadaException {
		Cliente cliente = new Cliente("Joao", "11122233344");
		banco.cadastrarCliente(cliente);
		banco.atualizarCliente(cliente);
	}

	@Test(expected = AtualizacaoNaoRealizadaException.class)
	public void testeAtualizarClienteInexistente() throws ClienteJaCadastradoException, RepositorioException, AtualizacaoNaoRealizadaException {
		Cliente cliente = new Cliente("Joao", "11122233344");
		banco.atualizarCliente(cliente);
	}

	@Test
	public void testeRenderBonus() throws RepositorioException, ContaJaCadastradaException, ContaNaoEncontradaException, RenderBonusContaEspecialException {
		ContaAbstrata contaEspecial = new ContaEspecial("111", 100);
		banco.cadastrar(contaEspecial);
		double bonus = ((ContaEspecial) contaEspecial).getBonus();
		double saldoComBonus = bonus + (bonus * 0.01);
		banco.renderBonus(contaEspecial);
		double saldoRendimento = ((ContaEspecial) contaEspecial).getBonus();
		assertEquals(bonus, saldoRendimento, 0);
	}

	@Test(expected = ContaNaoEncontradaException.class)
	public void testeRenderBonusContaInexistente() throws ContaNaoEncontradaException, RepositorioException, RenderBonusContaEspecialException {
		ContaAbstrata contaEspecial  = new ContaEspecial("111", 100);
		banco.renderBonus(contaEspecial);
	}
	@Test(expected = RenderBonusContaEspecialException.class)
	public void testeRenderBonusContaNaoEspecial() throws ContaNaoEncontradaException, RepositorioException, RenderBonusContaEspecialException {
		ContaAbstrata contaEspecial  = new Poupanca("111", 100);
		banco.renderBonus(contaEspecial);
	}



	/**
	 * Verifica que render juros de uma conta poupanca funciona corretamente
	 * 
	 */

	@Test
	public void testeRenderJurosContaExistente() throws RepositorioException,
			ContaNaoEncontradaException, RenderJurosPoupancaException,
			InicializacaoSistemaException, ContaJaCadastradaException {

		ContaAbstrata poupanca = new Poupanca("20", 100);
		banco.cadastrar(poupanca);
		double saldoSemJuros = poupanca.getSaldo();
		double saldoComJuros = saldoSemJuros + (saldoSemJuros * 0.5);
		banco.renderJuros(poupanca);
		assertEquals(saldoComJuros, poupanca.getSaldo(), 0);
	}

	/**
	 * Verifica que tentativa de render juros em conta inexistente levanta
	 * excecao.
	 * 
	 */

	@Test(expected = ContaNaoEncontradaException.class)
	public void testeRenderJurosContaInexistente() throws RepositorioException,
			ContaNaoEncontradaException, RenderJurosPoupancaException,
			InicializacaoSistemaException {

		ContaAbstrata poupanca = new Poupanca("111", 100);
		banco.renderJuros(poupanca);
	}

	/**
	 * Verifica que tentativa de render juros em conta que nao e poupanca
	 * levanta excecao.
	 * 
	 */

	@Test(expected = RenderJurosPoupancaException.class)
	public void testeRenderJurosContaNaoEhPoupanca()
			throws RepositorioException, ContaNaoEncontradaException,
			RenderJurosPoupancaException, InicializacaoSistemaException,
			ContaJaCadastradaException {

		ContaAbstrata contaEspecial = new ContaEspecial("111", 100);
		banco.cadastrar(contaEspecial);
		banco.renderJuros(contaEspecial);
	}

	/**
	 * Verifica que render bonus de uma conta especial funciona corretamente.
	 * 
	 */


}
