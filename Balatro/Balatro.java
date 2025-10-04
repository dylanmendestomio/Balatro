package Balatro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Balatro {
    public static void main(String[] args) {
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
        // Exibindo as cartas geradas
        System.out.println("8 Cartas Aleatórias Geradas:");
        for (int i = 0; i < selectedCards.size(); i++) {
            String card = selectedCards.get(i);
            String rank = card.substring(0, card.length() - 1); // Remove o naipe
            String classe = (Arrays.asList(realeza).contains(rank)) ? "Realeza" : "Numeral";
            System.out.printf("%d. %s (%s)%n", (i + 1), card, classe);
        }
    }
}
