package Jogo;

import java.io.Serializable;

public class Carta implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String valor;
    private String naipe;
    private int pontos;

    public Carta(int id, String valor, String naipe, int pontos) {
        this.id = id;
        this.valor = valor;
        this.naipe = naipe;
        this.pontos = pontos;
    }

    public int getId() {
        return id;
    }

    public String getValor() {
        return valor;
    }

    public String getNaipe() {
        return naipe;
    }

    public int getPontos() {
        return pontos;
    }

    public String toString() {
        return "\n" + getId() + ": " + getValor() + " de " + getNaipe();
    }
}