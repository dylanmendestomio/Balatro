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

        // Naipes
        String[] suits = { "O", "E", "C", "P" };

        // Gerando o baralho completo (lista de strings como "A♦")
        List<String> deck = new ArrayList<>();
        for (String rank : ranks) {
            for (String suit : suits) {
                deck.add(rank + suit);
            }
        }

        // Embaralhando o baralho e selecionando as 8 primeiras cartas
        Collections.shuffle(deck);
        List<String> selectedCards = deck.subList(0, 8);

        // Exibindo as 8 cartas geradas
        System.out.println("8 Cartas Aleatórias Geradas:");
        for (int i = 0; i < selectedCards.size(); i++) {
            String card = selectedCards.get(i);
            String rank = card.substring(0, card.length() - 1); // Remove o naipe
            String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
            System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
        }

        // Seleção de 5 cartas pelo usuário
        List<String> userSelectedCards = selectFiveCards(selectedCards, realeza, scanner);

        // Exibindo as 5 cartas selecionadas
        System.out.println("\n5 Cartas Selecionadas:");
        for (int i = 0; i < userSelectedCards.size(); i++) {
            String card = userSelectedCards.get(i);
            String rank = card.substring(0, card.length() - 1); // Remove o naipe
            String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
            System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
        }

        scanner.close();
    }

    /**
     * Método para selecionar 5 cartas das 8 geradas.
     * Pede input do usuário e valida.
     */
    private static List<String> selectFiveCards(List<String> allCards, String[] realeza, Scanner scanner) {
        List<String> userSelections = new ArrayList<>();
        boolean validInput = false;

        while (!validInput) {
            System.out.print("\nDigite 5 números (1-8) separados por espaço para selecionar as cartas: ");
            String inputLine = scanner.nextLine().trim();

            // Parsear a entrada
            String[] inputs = inputLine.split("\\s+"); // Split por espaços
            Set<Integer> selectedIndices = new HashSet<>(); // Para checar unicidade

            if (inputs.length != 5) {
                System.out.println("Erro: Digite exatamente 5 números.");
                continue;
            }

            try {
                for (String input : inputs) {
                    int index = Integer.parseInt(input) - 1; // Converter para índice 0-based
                    if (index < 0 || index >= allCards.size()) {
                        throw new NumberFormatException("Índice fora do range.");
                    }
                    if (!selectedIndices.add(index)) {
                        throw new NumberFormatException("Números duplicados.");
                    }
                    userSelections.add(allCards.get(index));
                }
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite números válidos e únicos entre 1 e 8 (ex: 1 3 5 7 2).");
                userSelections.clear(); // Limpar seleção inválida
            }
        }

        return userSelections;
    }
}
