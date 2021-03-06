/*
  Daniel Guimaraes - 1910462
  Mariana Barreto - 1820673
 */
package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

class Ficha {
    public int valor;
    public String cor;      // nao eh utilizado, pode ser retirado posteriormente

    public Ficha(int valor) throws IllegalArgumentException {
        if (valor == 1) { this.cor = "roxo"; }
        else if (valor == 5)  { this.cor = "branco"; }
        else if (valor == 10) { this.cor = "vermelho"; }
        else if (valor == 20) { this.cor = "verde"; }
        else if (valor == 50) { this.cor = "azul"; }
        else if (valor == 100){ this.cor = "preto"; }
        else { throw new IllegalArgumentException("Valor nao existe"); }
        this.valor = valor;
    }

    /**
     * Calcula o valor total de uma lista de fichas
     * @param lista lista de fichas
     * @return o dinheiro total associado
     */
    public static int calculaValor(List<Ficha> lista) {
        int soma = 0;
        for (Ficha ficha : lista) {
            soma += ficha.valor;
        }
        return soma;
    }

    /**
     * Calcula uma combina��o de fichas que mais aproxima ao dinheiro desejado
     * @param dinheiro Dinheiro a ser tranformado em fichas
     * @param listaPossiveis Lista de fichas disponiveis
     * @return Lista de fichas relativas ao dinheiro
     * @throws Exception Error - Se n�o houver melhor escolha
     */
    public static List<Ficha> calculaFicha(int dinheiro, List<Ficha> listaPossiveis) throws Exception{
        // Fazendo uma copia
        int i;
        List<Ficha> lista = new ArrayList<>();
        for (i = 0; i < listaPossiveis.size(); i ++) {
            lista.add(listaPossiveis.get(i));
        }

        // fazendo um sort da lista
        lista.sort(new SortByValorReverse());

        // algoritmo para achar a melhor combinacao
        // greedy -> maior para o menor.
        // se nao conseguir, tentar com o segundo maior, e assim por diante
        int diff, soma, j;
        int menorDiferenca = 100000;
        List<Ficha> melhorEscolha = null;
        List<Ficha> escolhas;

        for (i = 0; i < lista.size(); i ++) {
            escolhas = new ArrayList<>();
            soma = 0;
            for (j=i; j < lista.size(); j ++) {
                if (soma < dinheiro) {
                    soma += lista.get(j).valor;
                    escolhas.add(lista.get(j));
                }
            }
            diff = soma - dinheiro;
            if (diff >= 0 && diff < menorDiferenca) {
                melhorEscolha = escolhas;
                menorDiferenca = diff;
            } else if ((diff == menorDiferenca) && (escolhas.size() < melhorEscolha.size())) {
                melhorEscolha = escolhas;
                menorDiferenca = diff;
            }
        }

        if (melhorEscolha == null) {throw new Exception("Fichas insuficientes para o dinheiro");}
        return melhorEscolha;
    }
    
    
    /**
     * Calcula uma combinação de fichas a partir de um dinheiro.
     *
     * O algoritmo favorece a maior quantidade de fichas para as maiores fichas.
     * Ele aloca a maior quantidade de fichas da maior ficha, depois para a segunda maior ficha,
     * e repete até nao for possivel
     *
     * @param dinheiro Dinheiro a ser tranformado em fichas
     * @return Lista de fichas relativas ao dinheiro
     */
    public static List<Ficha> calculaFicha(int dinheiro){
    	int i, qtd_fichas100, qtd_fichas50, qtd_fichas20, qtd_fichas10, qtd_fichas5, qtd_fichas1;
    	// Fazendo uma copia
        List<Ficha> lista = new ArrayList<>();
        
        //Descubrindo a quantidade de cada ficha daquele valor
        qtd_fichas100 = dinheiro / 100;
        qtd_fichas50 = (dinheiro % 100) / 50;
        qtd_fichas20 = ((dinheiro % 100) % 50) / 20;
        qtd_fichas10 = (((dinheiro % 100) % 50) % 20) / 10;
        qtd_fichas5 = ((((dinheiro % 100) % 50) % 20) % 10) / 5;
        qtd_fichas1 = ((((dinheiro % 100) % 50) % 20) % 10) % 5;
        
        //Adicionando n fichas daquele valor para a nova lista
        for (i = 0; i < qtd_fichas100; i++) {
        	lista.add(new Ficha(100));
        }
        for (i = 0; i < qtd_fichas50; i++) {
        	lista.add(new Ficha(50));
        }
        for (i = 0; i < qtd_fichas20; i++) {
        	lista.add(new Ficha(20));
        }
        for (i = 0; i < qtd_fichas10; i++) {
        	lista.add(new Ficha(10));
        }
        for (i = 0; i < qtd_fichas5; i++) {
        	lista.add(new Ficha(5));
        }
        for (i = 0; i < qtd_fichas1; i++) {
        	lista.add(new Ficha(1));
        }
    	
    	return lista;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) { return false; }
        Ficha f = (Ficha) o;
        return (this.valor == f.valor);
    }

    /**
     * Converte uma lista de fichas em um array, para salvar
     * @param lista Lista de Fichas
     * @return Uma string contendo as informacoes das fichas
     */
    public static String toString(ArrayList<Ficha> lista) {
        // cria uma lista temporaria de strings, para cada valor das fichas
        String[] s = new String[lista.size()];
        for (int i = 0; i < lista.size(); i ++) {
            s[i] = String.valueOf(lista.get(i).valor);      // salva o valor da ficha
        }

        // juntando cada valor em uma so string
        return String.join("-", s);
    }

    /**
     * Converte uma string contendo as informacoes de uma lista de fichas em uma lista de fichas
     * @param stringListaFichas String na forma "valor-valor-valor...valor"
     * @return ArrayList contendo os objetos das fichas
     */
    public static ArrayList<Ficha> fromString(String stringListaFichas) {
        String[] listaString = stringListaFichas.split("-");
        ArrayList<Ficha> ret = new ArrayList<>();
        for (String ficha: listaString) {
            ret.add(new Ficha(Integer.parseInt(ficha)));
        }
        return ret;
    }
}

// para a ordenacao da lista
class SortByValorReverse implements Comparator<Ficha> {
    public int compare(Ficha a, Ficha b) {
        return b.valor - a.valor;
    }
}
