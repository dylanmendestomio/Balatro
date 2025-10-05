package Balatro;

import java.util.*;

public class Balatro{
    // Enum para naipes
    public enum Naipe {
        O, E, C, P
    }

    // Classe para representar uma carta
    public static class Carta {
        private String valor; // "A", "2"-"10", "J", "Q", "K"
        private Naipe naipe;  // O, E, C, P

        public Carta(String valor, Naipe naipe) {
            this.valor = valor;
            this.naipe = naipe;
        }

        public String getValor() { return valor; }
        public Naipe getNaipe() { return naipe; }

        // Valor numérico para fichas e ordenação (para poker: A=14 alta ou 1 baixa)
        public int getValorFichas() {
            if (valor.equals("A")) return 11;
            if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) return 10;
            return Integer.parseInt(valor);
        }

        public int getValorNumerico() { // Para sequências: J=11, Q=12, K=13, A=14
            if (valor.equals("A")) return 14;
            if (valor.equals("J")) return 11;
            if (valor.equals("Q")) return 12;
            if (valor.equals("K")) return 13;
            return Integer.parseInt(valor);
        }

        @Override
        public String toString() {
            return valor + naipe.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Carta carta = (Carta) obj;
            return valor.equals(carta.valor) && naipe == carta.naipe;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valor, naipe);
        }
    }

    // Classe para representar uma mão de pôquer e sua pontuação
    public static class MaoPoker {
        public String nome; // ex.: "Par", "Straight Flush"
        public int fichasFixas;
        public int multi;

        public MaoPoker(String nome, int fichasFixas, int multi) {
            this.nome = nome;
            this.fichasFixas = fichasFixas;
            this.multi = multi;
        }
    }

    // Avaliador de mãos de pôquer
    public static MaoPoker avaliarMao(List<Carta> cartas) {
        int numCartas = cartas.size();
        if (numCartas < 1) return new MaoPoker("Inválida", 0, 1);

        // Ordenar por valor numérico para análise
        cartas.sort(Comparator.comparingInt(Carta::getValorNumerico));

        // Contar frequências de valores e naipes
        Map<Integer, Integer> freqValores = new HashMap<>();
        Map<Naipe, Integer> freqNaipes = new HashMap<>();
        for (Carta c : cartas) {
            int val = c.getValorNumerico();
            freqValores.put(val, freqValores.getOrDefault(val, 0) + 1);
            freqNaipes.put(c.getNaipe(), freqNaipes.getOrDefault(c.getNaipe(), 0) + 1);
        }

        List<Integer> valores = new ArrayList<>(freqValores.keySet());
        Collections.sort(valores);

        boolean isFlush = freqNaipes.values().stream().anyMatch(count -> count == numCartas);
        boolean isStraight = isStraight(valores, numCartas);

        // Para <5 cartas, adaptações
        if (numCartas == 5) {
            if (isFlush && isStraight) return new MaoPoker("Straight Flush", 100, 8);
            if (temQuadra(freqValores)) return new MaoPoker("Quadra", 60, 7);
            if (temFullHouse(freqValores)) return new MaoPoker("Full House", 40, 4);
            if (isFlush) return new MaoPoker("Flush", 35, 4);
            if (isStraight) return new MaoPoker("Sequência", 30, 4);
            if (temTrinca(freqValores)) return new MaoPoker("Trinca", 30, 3);
            if (temDoisPares(freqValores)) return new MaoPoker("Dois Pares", 20, 2);
            if (temPar(freqValores)) return new MaoPoker("Par", 10, 2);
            return new MaoPoker("Carta Alta", 5, 1);
        } else if (numCartas == 4) {
            if (temQuadra(freqValores)) return new MaoPoker("Quadra", 60, 7);
            if (temTrinca(freqValores)) return new MaoPoker("Trinca", 30, 3);
            if (temDoisPares(freqValores)) return new MaoPoker("Dois Pares", 20, 2);
            if (temPar(freqValores)) return new MaoPoker("Par", 10, 2);
            return new MaoPoker("Carta Alta", 5, 1);
        } else if (numCartas == 3) {
            if (temTrinca(freqValores)) return new MaoPoker("Trinca", 30, 3);
            if (temPar(freqValores)) return new MaoPoker("Par", 10, 2);
            return new MaoPoker("Carta Alta", 5, 1);
        } else if (numCartas == 2) {
            if (temPar(freqValores)) return new MaoPoker("Par", 10, 2);
            return new MaoPoker("Carta Alta", 5, 1);
        } else { // 1 carta
            return new MaoPoker("Carta Alta", 5, 1);
        }
    }

    // Verifica se é straight (sequência)
    private static boolean isStraight(List<Integer> valores, int numCartas) {
        if (valores.size() == numCartas) { // Todos valores únicos
            for (int i = 1; i < numCartas; i++) {
                if (valores.get(i) != valores.get(i-1) + 1) return false;
            }
            return true;
        }
        // Para A baixa: checar se A=1 e sequência baixa (ex.: A,2,3,4,5)
        if (numCartas == 5 && valores.contains(14) && valores.contains(2) && valores.contains(3) && valores.contains(4) && valores.contains(5)) {
            return true;
        }
        return false;
    }

    private static boolean temQuadra(Map<Integer, Integer> freq) { return freq.values().stream().anyMatch(c -> c == 4); }
    private static boolean temFullHouse(Map<Integer, Integer> freq) { return freq.values().stream().anyMatch(c -> c == 3) && freq.values().stream().anyMatch(c -> c == 2); }
    private static boolean temTrinca(Map<Integer, Integer> freq) { return freq.values().stream().anyMatch(c -> c == 3); }
    private static boolean temDoisPares(Map<Integer, Integer> freq) {
        long pares = freq.values().stream().filter(c -> c == 2).count();
        return pares == 2;
    }
    private static boolean temPar(Map<Integer, Integer> freq) { return freq.values().stream().anyMatch(c -> c == 2); }

    // Calcula soma de fichas de uma lista de cartas
    public static int somaFichas(List<Carta> cartas) {
        return cartas.stream().mapToInt(Carta::getValorFichas).sum();
    }

    // Cria o baralho completo para validação
    public static List<Carta> criarBaralho() {
        List<Carta> baralho = new ArrayList<>();
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        Naipe[] naipes = Naipe.values();
        for (String val : valores) {
            for (Naipe n : naipes) {
                baralho.add(new Carta(val, n));
            }
        }
        return baralho;
    }

    // Valida se uma string é uma carta válida
    public static Carta parseCarta(String input, List<Carta> baralho) {
        input = input.trim().toUpperCase();
        if (input.length() < 2) return null;
        String val = input.substring(0, input.length() - 1);
        String naipeStr = input.substring(input.length() - 1);
        Naipe naipe = null;
        switch (naipeStr) {
            case "O": naipe = Naipe.O; break;
            case "E": naipe = Naipe.E; break;
            case "C": naipe = Naipe.C; break;
            case "P": naipe = Naipe.P; break;
            default: return null;
        }
        if (!Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K").contains(val)) return null;
        Carta c = new Carta(val, naipe);
        if (baralho.contains(c)) return c;
        return null;
    }

    // Main: Fluxo principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Carta> baralhoCompleto = criarBaralho();

        System.out.println("=== Simulador de Pôquer com Fichas ===");
        System.out.println("Informe sua mão de 8 cartas (formato: 2O, AE, 5C, etc., separadas por vírgula):");

        // Input da mão
        List<Carta> mao = new ArrayList<>();
        boolean validMao = false;
        while (!validMao) {
            String input = scanner.nextLine();
            String[] partes = input.split(",");
            if (partes.length != 8) {
                System.out.println("Erro: Deve informar exatamente 8 cartas. Tente novamente.");
                continue;
            }
            mao.clear();
            Set<Carta> unicas = new HashSet<>();
            boolean todasValidas = true;
            for (String p : partes) {
                Carta c = parseCarta(p, baralhoCompleto);
                if (c == null || !unicas.add(c)) {
                    todasValidas = false;
                    break;
                }
                mao.add(c);
            }
            if (todasValidas) {
                validMao = true;
            } else {
                System.out.println("Erro: Cartas inválidas ou duplicadas. Tente novamente.");
            }
        }

        // Mostrar mão numerada
        System.out.println("\nSua mão de 8 cartas:");
        int somaTotalMao = somaFichas(mao);
        for (int i = 0; i < mao.size(); i++) {
            Carta c = mao.get(i);
            System.out.println((i+1) + ". " + c + " (" + c.getValorFichas() + " fichas)");
        }
        System.out.println("Soma total da mão: " + somaTotalMao + " fichas\n");

        // Input da jogada (1-5 cartas)
        List<Carta> jogada = new ArrayList<>();
        System.out.println("Informe as 1-5 cartas que quer jogar (índices 1-8 separados por vírgula, OU nomes como 2O,5C):");
        boolean validJogada = false;
        while (!validJogada) {
            String input = scanner.nextLine();
            jogada.clear();
            String[] partes = input.split(",");
            if (partes.length < 1 || partes.length > 5) {
                System.out.println("Erro: Escolha 1 a 5 cartas. Tente novamente.");
                continue;
            }
            boolean todasValidas = true;
            for (String p : partes) {
                Carta c = null;
                // Tentar como índice
                try {
                    int idx = Integer.parseInt(p.trim()) - 1;
                    if (idx >= 0 && idx < 8) {
                        c = mao.get(idx);
                    }
                } catch (NumberFormatException e) {
                    // Tentar como nome de carta
                    c = parseCarta(p, baralhoCompleto);
                    if (c != null && mao.contains(c)) {
                        // OK
                    } else {
                        c = null;
                    }
                }
                if (c == null || !jogada.add(c)) { // Evita duplicatas na jogada
                    todasValidas = false;
                    break;
                }
            }
            if (todasValidas && new HashSet<>(jogada).size() == jogada.size()) {
                validJogada = true;
            } else {
                System.out.println("Erro: Cartas inválidas, fora da mão ou duplicadas. Tente novamente.");
            }
        }

        // Avaliar e pontuar
        int somaFichasJogada = somaFichas(jogada);
        MaoPoker maoPoker = avaliarMao(jogada);
        int pontuacao = (somaFichasJogada + maoPoker.fichasFixas) * maoPoker.multi;

        System.out.println("\n=== Resultado da Jogada ===");
        System.out.println("Cartas jogadas: " + jogada);
        System.out.println("Soma de fichas das cartas: " + somaFichasJogada);
        System.out.println("Mão de pôquer: " + maoPoker.nome + " (" + maoPoker.fichasFixas + " fichas fixas × " + maoPoker.multi + " multi)");
        System.out.println("Pontuação total: " + pontuacao + " fichas!");

        scanner.close();
    }
}
