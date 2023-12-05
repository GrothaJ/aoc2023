package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Path PATH = Paths.get("day4", "src", "main", "resources", "input.txt");

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(PATH);

        // Solution 1
        int sum = 0;
        for (String line : lines) {
            line = line.substring(line.indexOf(":")).trim();
            line = line.replaceAll("  ", " ");
            String[] array = line.split("\\|");
            List<String> winningNumbers = Arrays.asList(array[0].split(" "));
            List<String> myNumbers = Arrays.asList(array[1].split(" "));
            int numberOfMatches = getNumberOfMatches(winningNumbers, myNumbers);
            if (numberOfMatches == 0) {
                continue;
            }
            sum += (int) Math.pow(2.0, numberOfMatches - 1);
        }
        System.out.println(sum);

        // Solution 2
        int numberOfLines = getNumberOfLines(lines);
        Map<Integer, Integer> cards = fillInCards(numberOfLines);
        simulateGame(lines, cards);
        int finalResult = countCards(cards);
        System.out.println(finalResult);

    }

    private static int getNumberOfMatches(List<String> winningNumbers, List<String> myNumbers) {
        int numberOfMatches = 0;
        for (String number : myNumbers) {
            if (winningNumbers.contains(number)) {
                numberOfMatches++;
            }
        }
        return numberOfMatches;
    }

    private static int getNumberOfLines(List<String> lines) {
        int numberOfLines = 0;
        for (String line : lines) {
            if (!line.isBlank()) {
                numberOfLines++;
            }
        }
        return numberOfLines;
    }

    private static Map<Integer, Integer> fillInCards(int numberOfLines) {
        Map<Integer, Integer> cards = new HashMap<>();
        for (int i = 1; i <= numberOfLines; i++) {
            cards.put(i, 1);
        }
        return cards;
    }

    private static void simulateGame(List<String> lines, Map<Integer, Integer> cards) {
        for (String line : lines) {
            int cardNumber = getCardNumber(line);
            int numberOfDraws = cards.get(cardNumber);
            line = line.substring(line.indexOf(":")).trim();
            line = line.replaceAll("  ", " ");
            String[] array = line.split("\\|");
            List<String> winningNumbers = Arrays.asList(array[0].split(" "));
            List<String> myNumbers = Arrays.asList(array[1].split(" "));
            int numberOfWinningNumbers = getNumberOfMatches(winningNumbers, myNumbers);
            for (int i = cardNumber + 1; i <= cardNumber + numberOfWinningNumbers; i++) {
                int currentNumberOfCards = cards.get(i);
                cards.put(i, currentNumberOfCards + numberOfDraws);
            }
        }
    }

    private static int getCardNumber(String line) {
        line = line.replaceAll("   ", " ").replaceAll("  ", " ");
        String cardNumber = line.substring(line.indexOf(" ") + 1, line.indexOf(":"));
        return Integer.parseInt(cardNumber);
    }

    private static int countCards(Map<Integer, Integer> cards) {
        return cards.values().stream()
            .reduce(Integer::sum)
            .orElse(0);
    }
}
