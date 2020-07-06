package Jogo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private ArrayList<Carta> mao = new ArrayList<Carta>();
    private ArrayList<Carta> pilha = new ArrayList<Carta>();
    private int pontos = 0;
    private int escovas = 0;
    private int ouros = 0;
    private int qtdSete = 0;
    private boolean seteOuro = false;
    private boolean venceu = false;
    private Jogador adversario = null;

    public Jogador(String nome) { this.nome = nome; }

    public String getNome() {
        return nome;
    }

    public Jogador getAdversario() {
        return adversario;
    }

    public ArrayList<Carta> getMao() {
        return mao;
    }

    public ArrayList<Carta> getPilha() {
        return pilha;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        setPontos(getPontos() + calculaPontos());
        return pontos;
    }

    public int getEscovas() {
        return escovas;
    }

    public int getOuros() {
        return ouros;
    }

    public boolean venceu() {
        return this.venceu;
    }

    public int calculaPontos() {
        int pontos = getEscovas();

        if (this.getPilha().size() > adversario.getPilha().size()) {
            pontos += 1;
        }

        if (this.getOuros() > adversario.getOuros()) {
            pontos += 1;
        }

        if (seteOuro) {
            pontos += 1;
        }

        if (this.qtdSete == 4) {
            pontos += 3;
        }

        qtdSete = ouros = escovas = 0;
        seteOuro = false;
        return pontos;
    }
}