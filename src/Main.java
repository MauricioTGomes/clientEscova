import Jogo.Carta;
import Jogo.Jogada;
import Jogo.Jogador;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Boolean cartaValida = false;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 6789);
        ObjectOutputStream enviaParaServidor = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream recebeDoServidor = new ObjectInputStream(socket.getInputStream());

        Scanner scanner = new Scanner(System.in);
        String idsCartasMesa;
        cartaValida = false;

        System.out.print("Digite seu nome: ");
        Jogador jogador = new Jogador(scanner.nextLine());

        enviaParaServidor.reset();
        enviaParaServidor.writeObject(jogador);

        while (recebeDoServidor.readObject().equals("OK")) {
            Integer opcaoJogada = 0;
            ArrayList<Carta> mesa = (ArrayList<Carta>) recebeDoServidor.readObject();
            System.out.println("Cartas da mesa:\n" + mesa);

            jogador = (Jogador) recebeDoServidor.readObject();
            ArrayList<Carta> mao = jogador.getMao();

            System.out.println("Cartas da sua mao:\n" + mao);
            boolean realizouJogada = false;

            while (!realizouJogada) {
                do {
                    System.out.println(jogador.getNome() + " agora e sua vez:\n1 - Efetuar uma jogada\n2 - Descartar uma carta");
                    opcaoJogada = Integer.parseInt(scanner.nextLine());
                }while (opcaoJogada != 1 && opcaoJogada != 2);

                Carta cartaMao = null;
                ArrayList<Carta> cartasMesa = new ArrayList<Carta>();

                if (opcaoJogada == 1) {
                    while (!cartaValida) {
                        cartaMao = leCarta(mao);
                        cartasMesa = new ArrayList<Carta>();
                        cartaValida = false;
                        System.out.print("Digite os IDs das cartas da mesa que juntas com a sua somam 15.\nUtilizando espaco para separar:");
                        idsCartasMesa = scanner.nextLine();

                        for (String idCartaMesa : idsCartasMesa.split(" ")) {
                            int id = Integer.parseInt(idCartaMesa);
                            for (Carta carta : mesa) {
                                if (id == carta.getId()) {
                                    cartaValida = true;
                                    cartasMesa.add(carta);
                                    break;
                                }
                            }
                        }
                    }

                    Jogada jogada = new Jogada(cartaMao, cartasMesa, mao, mesa);
                    if (jogada.verificaSoma()) {
                        System.out.println("As cartas foram pegas. Aguarde a jogada de " + jogador.getAdversario().getNome() + ".");
                        enviaParaServidor.reset();
                        enviaParaServidor.writeObject(jogada);
                        realizouJogada = true;
                    } else {
                        System.out.println("Jogo.Jogada invalida, pois nao somou 15!\nTente novamente.");
                    }
                } else {
                    cartaMao = leCarta(mao);
                    Jogada jogada = new Jogada(cartaMao, cartasMesa, mao, mesa);
                    jogada.descartaCarta();
                    System.out.println("Carta descartada. Aguarde a jogada de " + jogador.getAdversario().getNome() + ".");
                    enviaParaServidor.reset();
                    enviaParaServidor.writeObject(jogada);
                    realizouJogada = true;
                }
            }
        }

        Jogador vencedor = null;
        if (jogador.venceu())
            vencedor = jogador;
        else {
            vencedor = jogador.getAdversario();
        }

        System.out.println(
                "Jogo terminou!\nJogo.Jogador " + vencedor.getNome() + " foi o vencedor!\n" +
                "Pontuacao final:\n" +
                vencedor.getNome() + ": " + vencedor.getPontos() + " pontos." +
                vencedor.getAdversario().getNome() + ": " + vencedor.getAdversario().getPontos() + " pontos."
        );

        scanner.close();
        socket.close();
    }

    public static Carta leCarta(ArrayList<Carta> mao) {
        Integer idCartaMao;
        Scanner scanner = new Scanner(System.in);
        Carta cartaRetornar = null;

        while (cartaRetornar == null) {
            System.out.print("Digite o ID da carta na sua mao: ");
            idCartaMao = Integer.parseInt(scanner.nextLine());
            for (Carta carta : mao) {
                if (idCartaMao == carta.getId()) {
                    cartaRetornar = carta;
                    cartaValida = true;
                    break;
                }
            }
        }

        return cartaRetornar;
    }
}
