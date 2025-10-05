package Balatro;

import java.util.*;

public class Balatro {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Definindo os ranks por classe
        String[] realeza = {"K", "Q", "J"};
        String[] numerais = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        List<String> ranks = new ArrayList<>();
        ranks.addAll(Arrays.asList(realeza));
        ranks.addAll(Arrays.asList(numerais));  // Total: 13 ranks

        // Naipes atualizados para letras
        String[] suits = {"O", "E", "C", "P"};

        // Gerando o baralho completo (lista de strings como "AO")
        List<String> deck = new ArrayList<>();
        for (String rank : ranks) {
            for (String suit : suits) {
                deck.add(rank + suit);
            }
        }

        // Embaralhando o baralho e selecionando as 8 primeiras cartas
        Collections.shuffle(deck);
        List<String> allCards = deck.subList(0, 8);

        // Exibindo as 8 cartas geradas
        System.out.println("8 Cartas Aleatórias Geradas:");
        for (int i = 0; i < allCards.size(); i++) {
            String card = allCards.get(i);
            String rank = card.substring(0, card.length() - 1);  // Remove o naipe
            String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
            System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
        }

        // Seleção de 1 a 5 cartas pelo usuário
        List<String> userSelectedCards = selectCards(allCards, realeza, scanner);
        
        // Exibindo as cartas selecionadas
        System.out.println("\nCartas Selecionadas (" + userSelectedCards.size() + " carta(s)):");
        for (int i = 0; i < userSelectedCards.size(); i++) {
            String card = userSelectedCards.get(i);
            String rank = card.substring(0, card.length() - 1);  // Remove o naipe
            String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
            System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
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
            System.out.print("\nDigite entre 1 e 5 números (1-8) separados por espaço para selecionar as cartas (ex: 1 ou 1 3 5): ");
            String inputLine = scanner.nextLine().trim();

            // Parsear a entrada
            String[] inputs = inputLine.split("\\s+");  // Split por espaços
            Set<Integer> selectedIndices = new HashSet<>();  // Para checar unicidade

            int numInputs = inputs.length;
            if (numInputs < 1 || numInputs > 5) {
                System.out.println("Erro: Você deve selecionar no mínimo 1 e no máximo 5 cartas. Tente novamente.");
                continue;
            }

            try {
                for (String input : inputs) {
                    int index = Integer.parseInt(input) - 1;  // Converter para índice 0-based
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
                System.out.println("Erro: Digite apenas números inteiros válidos e únicos entre 1 e 8. Tente novamente.");
                userSelections.clear();  // Limpar seleção inválida
            }
        }

        return userSelections;
    }
}
