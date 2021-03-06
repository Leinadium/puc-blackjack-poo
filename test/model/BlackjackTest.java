/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import static org.junit.Assert.*;

import org.junit.Test;

import model.cartas.Carta;
import model.cartas.Cor;
import model.cartas.Naipe;
import model.cartas.Nome;

public class BlackjackTest {

	/*
	Nem todas os metodos foram testados, pois alteram alguma propriedade
	durante uma partida, que ficaria complexo de testar, e poderia cobrir poucos
	casos, logo necessitaria muitos testes diferentes.
	 */

	private void adicionaCartaMao(Mao m,  Cor cor, Nome nome, Naipe naipe) {
		Carta c = new Carta(cor, nome, naipe);
		m.ganharCarta(c);
	}
	
	private void criaBlackjack(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
	}
	
	private void criaSplit(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.VALETE, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
	}
	
	private void criaMaoComumMenosPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.TRES, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.TRES, Naipe.PAUS);
	}
	
	private void criaMaoComumMaisPontos(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.AS, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
	}
	
	private void criaMaoQuebrada(Mao m) {
		adicionaCartaMao(m, Cor.VERMELHO, Nome.VALETE, Naipe.OUROS);
		adicionaCartaMao(m, Cor.PRETO, Nome.VALETE, Naipe.PAUS);
		adicionaCartaMao(m, Cor.PRETO, Nome.QUATRO, Naipe.PAUS);
	}

	private Blackjack apiGenerica(int n) {
		Blackjack r = Blackjack.getAPI();
		r.iniciarBlackjack(n);
		return r;
	}

	@Test
	public final void testSingleton() {
		Blackjack api = Blackjack.getAPI();
		Blackjack api2 = Blackjack.getAPI();
		assertEquals("As APIs sao diferentes", api, api2);
	}

	@Test
	public final void testIniciarAPI() {
		Blackjack api = apiGenerica(3);
		assertEquals("A api nao foi iniciada corretamente", 3, api.qtdJogadores);
	}

	@Test
	public final void testPassarVez() {
		Blackjack api = apiGenerica(2);
		api.passaVez();
		assertEquals("A vez nao foi passada corretamente", 1, api.getVez());
	}

	@Test
	public final void testJogadoresFinalizados() {
		Blackjack api = apiGenerica(2);
		api.passaVez();
		api.passaVez();
		assertTrue("Os jogadores nao foram finalizados", api.getJogadoresFinalizados());
	}

	@Test
	public final void testResetVez() {
		Blackjack api = apiGenerica(2);
		api.passaVez();
		api.resetVez();
		assertEquals("A vez nao foi reiniciada", 0, api.getVez());
	}

	@Test
	public final void testDefineVez() {
		Blackjack api = apiGenerica(2);
		api.passaVez();
		api.defineVez(0);
		assertEquals("A vez nao foi definida corretamente", 0, api.getVez());
	}

	@Test
	public final void testDefineAposta() {
		Blackjack api = apiGenerica(1);
		api.defineAposta(0, 0, 30);
		assertEquals("A aposta nao foi definida corretamente", 30, api.getValorApostaJogador(api.getVez(), 0));
	}

	@Test
	public final void testAumentaAposta() {
		Blackjack api = apiGenerica(1);
		api.aumentaAposta(20);
		assertEquals("A aposta nao foi aumentada corretamente",
				20, api.getValorApostaJogador(api.getVez(), 0));
	}

	@Test
	public final void testJogadorGanhandoComBlackjack() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaBlackjack(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test 
	public final void testJogadorGanhandoComBlackjackVerificaPagamento() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		int expected_aposta = actual_jogador.mao.aposta;
		Dealer actual_dealer = actual.dealer;
		actual_jogador.fazAposta(50);
		criaBlackjack(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		// actual.verificaGanhador(actual_jogador, actual_dealer);
		actual.distribuiDinheiroJogadores();
		assertNotEquals("O pagamento nao foi feito como esperado", expected_aposta, actual_jogador.mao.aposta);
	}
	
	@Test
	public final void testJogadorGanhandoNormal() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMaisPontos(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testJogadorGanhandoDealerQuebrando() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMaisPontos(actual_jogador.mao);
		criaMaoQuebrada(actual_dealer.mao);
		assertEquals("O jogador nao foi vencedor da partida", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testDealerGanhandoDoisQuebrando() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoQuebrada(actual_jogador.mao);
		criaMaoQuebrada(actual_dealer.mao);
		assertEquals("O dealer nao foi vencedor da partida", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testDealerGanhandoDoisBlackjacks() {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaBlackjack(actual_jogador.mao);
		criaBlackjack(actual_dealer.mao);
		assertEquals("O dealer nao foi vencedor da partida", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testPush() {
		Blackjack actual =Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMenosPontos(actual_jogador.mao);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O empate nao aconteceu", Resultado.PUSH, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testJogadorGanhandoComMaosSplit() throws Exception {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaSplit(actual_jogador.mao);
		actual_jogador.fazerSplit(actual.baralho);
		criaMaoComumMenosPontos(actual_dealer.mao);
		assertEquals("O jogador ganhou com a mao com split", Resultado.JOGADOR, actual.verificaGanhador(actual_jogador, actual_dealer));
	}
	
	@Test
	public final void testDealerGanhandoComMaosSplit() throws Exception {
		Blackjack actual = Blackjack.getAPI();
		actual.iniciarBlackjack(1);
		Jogador actual_jogador = actual.jogadores.get(0);
		Dealer actual_dealer = actual.dealer;
		criaMaoComumMenosPontos(actual_jogador.mao);
		actual_jogador.fazerSplit(actual.baralho);
		criaMaoComumMaisPontos(actual_dealer.mao);
		assertEquals("O jogador ganhou com a mao com split", Resultado.DEALER, actual.verificaGanhador(actual_jogador, actual_dealer));
	}

}
