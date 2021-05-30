package controller.observer;

import java.util.HashMap;
import java.util.ArrayList;

public interface ObservadoAPI {
    void registraObservador(ObservadorAPI o);   // registra um observador
    void removeObservador(ObservadorAPI o);     // remove um observador
    void notificarTodos(NotificacaoAPI n);      // seta a notificacao, e notifica todos
    NotificacaoAPI getNotificacao();            // metodo de acessoa a notificacao

    String[] getCartasDealer();                 // notificacao: DealerCartas
    int getValorDealer();                       // notificacao: DealerCartas

    String[] getCartasJogador(int idJogador, int mao);     // notificacao: JogadorCartas
    int getValorJogador(int idJogador, int mao);           // notificacao: JogadorCartas

    HashMap<Integer, Integer> getFichasJogador(int idJogador);      // notificacao: JogadorAposta
    ArrayList<Integer> getApostaJogador(int idJogador);
}
