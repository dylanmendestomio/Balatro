package Balatro;

import java.util.*;

public class Balatro {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Definindo os ranks por classe
        String[] realeza = { "K", "Q", "J" };
        String[] numerais = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        List<String> ranks = new ArrayList<>();
        ranks.addAll(Arrays.asList(realeza));
        ranks.addAll(Arrays.asList(numerais)); // Total: 13 ranks

        // Naipes atualizados para letras
        String[] suits = { "O", "E", "C", "P" };

        // Gerando o baralho completo (lista de strings como "AO")
        List<String> deck = new ArrayList<>();
        for (String rank : ranks) {
            for (String suit : suits) {
                deck.add(rank + suit);
            }
        }

        // Embaralhando o baralho uma vez
        Collections.shuffle(deck);

        // Contadores e mão final
        int jogadas = 0;
        int descartes = 0;
        List<String> maoFinal = new ArrayList<>();
        int rodada = 1;

        System.out.println("Bem-vindo ao Balatro! Você tem até 3 jogadas e 3 descartes.");
        System.out.println("Em cada rodada, selecione cartas e decida jogá-las (adicionar à mão) ou descartá-las.\n");

        // Loop principal das rodadas
        while (jogadas < 3 && descartes < 3 && deck.size() >= 8) {
            System.out.println("--- Rodada " + rodada + " ---");

            // Tirar 8 cartas do baralho restante
            List<String> allCards = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                allCards.add(deck.remove(0)); // Remove do início (já embaralhado)
            }

            // Exibindo as 8 cartas geradas
            System.out.println("8 Cartas Aleatórias Geradas (do baralho restante):");
            for (int i = 0; i < allCards.size(); i++) {
                String card = allCards.get(i);
                String rank = card.substring(0, card.length() - 1); // Remove o naipe
                String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
                System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
            }

            // Seleção de 1 a 5 cartas pelo usuário
            List<String> userSelectedCards = selectCards(allCards, realeza, scanner);

            // Exibindo as cartas selecionadas temporariamente
            System.out.println("\nCartas Selecionadas nesta Rodada (" + userSelectedCards.size() + " carta(s)):");
            for (int i = 0; i < userSelectedCards.size(); i++) {
                String card = userSelectedCards.get(i);
                String rank = card.substring(0, card.length() - 1);
                String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
                System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
            }

            // Escolha: Jogar ou Descartar
            boolean acaoValida = false;
            char escolha = ' ';
            while (!acaoValida) {
                System.out.print("\nDeseja JOGAR (j) essas cartas ou DESCARTÁ-LAS (d)? Digite 'j' ou 'd': ");
                String inputAcao = scanner.nextLine().trim().toLowerCase();
                if (inputAcao.length() >= 1) {
                    escolha = inputAcao.charAt(0);
                    if (escolha == 'j' || escolha == 'd') {
                        acaoValida = true;
                    }
                }
                if (!acaoValida) {
                    System.out.println("Entrada inválida. Digite 'j' para jogar ou 'd' para descartar.");
                }
            }

            if (escolha == 'j') {
                // Jogar: Adicionar à mão final
                maoFinal.addAll(userSelectedCards);
                jogadas++;
                System.out.println("Cartas jogadas! Adicionadas à mão final. Jogadas usadas: " + jogadas + "/3");
            } else {
                // Descartar: Ignorar
                descartes++;
                System.out.println("Cartas descartadas. Tentativa perdida. Descartes usados: " + descartes + "/3");
            }

            // As 8 cartas da rodada são removidas do baralho (já feito ao tirar)
            rodada++;
            if (jogadas < 3 && descartes < 3 && deck.size() >= 8) {
                System.out.println("\nPróxima rodada...\n");
            }
        }

        // Final do jogo
        System.out.println("\n--- Fim do Jogo ---");
        System.out.println("Estatísticas:");
        System.out.println("- Jogadas usadas: " + jogadas + "/3");
        System.out.println("- Descartes usados: " + descartes + "/3");
        System.out.println("- Cartas restantes no baralho: " + deck.size());

        if (maoFinal.isEmpty()) {
            System.out.println("Você não jogou nenhuma carta. Mão final vazia!");
        } else {
            System.out.println("\nMão Final (" + maoFinal.size() + " carta(s) jogadas):");
            for (int i = 0; i < maoFinal.size(); i++) {
                String card = maoFinal.get(i);
                String rank = card.substring(0, card.length() - 1);
                String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
                System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
            }
        }

        scanner.close();
    }

    /**
     * Método para selecionar 1 a 5 cartas das 8 geradas.
     * Pede input do usuário e valida.
     */
    private static List<String> selectCards(List<String> allCards, String[] realeza, Scanner scanner) {
        List<String> userSelections = new ArrayList<>();
        boolean validInput = false;

        while (!validInput) {
            System.out.print(
                    "\nDigite entre 1 e 5 números (1-8) separados por espaço para selecionar as cartas (ex: 1 ou 1 3 5): ");
            String inputLine = scanner.nextLine().trim();

            // Parsear a entrada
            String[] inputs = inputLine.split("\\s+"); // Split por espaços
            Set<Integer> selectedIndices = new HashSet<>(); // Para checar unicidade

            int numInputs = inputs.length;
            if (numInputs < 1 || numInputs > 5) {
                System.out.println("Erro: Você deve selecionar no mínimo 1 e no máximo 5 cartas. Tente novamente.");
                continue;
            }

            try {
                for (String input : inputs) {
                    int index = Integer.parseInt(input) - 1; // Converter para índice 0-based
                    if (index < 0 || index >= allCards.size()) {
                        throw new NumberFormatException("Número fora do intervalo 1-8.");
                    }
                    if (!selectedIndices.add(index)) {
                        throw new NumberFormatException("Números duplicados não são permitidos.");
                    }
                    userSelections.add(allCards.get(index));
                }
                validInput = true;
                System.out.println("Seleção válida! Você escolheu " + numInputs + " carta(s).");
            } catch (NumberFormatException e) {
                System.out
                        .println("Erro: Digite apenas números inteiros válidos e únicos entre 1 e 8. Tente novamente.");
                userSelections.clear(); // Limpar seleção inválida
            }
        }

        return userSelections;
    }
}
