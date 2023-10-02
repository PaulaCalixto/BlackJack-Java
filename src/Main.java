import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        List<String> baralho = criarBaralho();
        List<String> maoJogador = new ArrayList<>();
        List<String> maoDealer = new ArrayList<>();
        embaralhar(baralho, random);

        // Distribuir o baralho
        for (int i = 0; i < 2; i++) {
            maoJogador.add(retirarCarta(baralho, random));
            maoDealer.add(retirarCarta(baralho, random));
        }

        System.out.println("Bem-vindo ao Blackjack!");
        exibirMaoJogador(maoJogador);
        exibirMaoDealer(maoDealer, false);

        // Implementação do jogo
        boolean jogadorPerdeu = false;
        boolean jogadorQuerContinuar = true;
        while (jogadorQuerContinuar && !jogadorPerdeu) {
            System.out.println("Deseja pedir mais uma carta? (S para sim, qualquer outra tecla para não)");
            char resposta = scanner.next().charAt(0);

            if (resposta == 'S' || resposta == 's') {
                String novaCarta = retirarCarta(baralho, random);
                maoJogador.add(novaCarta);
                System.out.println("Você recebeu uma carta: " + novaCarta);
                exibirMaoJogador(maoJogador);

                if (calcularPontuacao(maoJogador) > 21) {
                    System.out.println("Você estourou!");
                    jogadorPerdeu = true;
                }
            } else {
                jogadorQuerContinuar = false;
            }
        }
        while (calcularPontuacao(maoDealer) < 17) {
            maoDealer.add(retirarCarta(baralho, random));
        }

        exibirMaoDealer(maoDealer, true);

        determinarVencedor(maoJogador, maoDealer);
        scanner.close();
    }
    private static List<String> criarBaralho() {
        List<String> baralho = new ArrayList<>();
        String[] naipes = {"Paus", "Ouros", "Copas", "Espadas"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Rei", "Rainha", "Valete", "Ás"};

        for (String naipe : naipes) {
            for (String valor : valores) {
                baralho.add(valor + " de " + naipe);
            }
        }
        return baralho;
    }
    private static void embaralhar(List<String> baralho, Random random) {
        for (int i = baralho.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(baralho, i, j);

        }
    }
    private static String retirarCarta(List<String> baralho, Random random) {
        int indiceCarta = random.nextInt(baralho.size());
        return baralho.remove(indiceCarta);
    }
    private static int calcularPontuacao(List<String> mao) {
        int pontuacao = 0;
        int ases = 0;

        for (String carta : mao) {
            String valorCarta = carta.split(" ")[0]; // Obtém o valor da carta
            if (valorCarta.equals("Ás")) {
                ases++;
                pontuacao += 11; // Ás vale inicialmente 11
            } else if (valorCarta.equals("Rei") || valorCarta.equals("Rainha") || valorCarta.equals("Valete")) {
                pontuacao += 10; // Cartas de figuras valem 10
            } else {
                pontuacao += Integer.parseInt(valorCarta);
            }
        }
        while (pontuacao > 21 && ases > 0) {
            pontuacao -= 10;
            ases--;
        }
        return pontuacao;
    }

    private static void exibirMaoJogador(List<String> mao) {
        System.out.println("Sua mão:");
        for (String carta : mao) {
            System.out.println(carta);
        }
    }

    private static void exibirMaoDealer(List<String> mao, boolean revelarTodas) {
        System.out.println("Mão do dealer:");
        for (int i = 0; i < mao.size(); i++) {
            if (i == 0 && !revelarTodas) {
                System.out.println("Carta oculta");
            } else {
                System.out.println(mao.get(i));
            }
        }
    }
    private static void determinarVencedor(List<String> maoJogador, List<String> maoDealer) {
        int pontuacaoJogador = calcularPontuacao(maoJogador);
        int pontuacaoDealer = calcularPontuacao(maoDealer);
        System.out.println("Pontuação do jogador: " + pontuacaoJogador);
        System.out.println("Pontuação do dealer: " + pontuacaoDealer);

        if (pontuacaoJogador > 21 || (pontuacaoDealer <= 21 && pontuacaoDealer >= pontuacaoJogador)) {
            System.out.println("O dealer venceu!");
        } else {
            System.out.println("Você venceu!");
        }
    }
}
