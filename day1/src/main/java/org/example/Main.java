package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final List<Character> NUMERIC_VALUES = List.of('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
    private static final List<String> SPELLED_DIGITS = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    private static final Map<String, Character> SPELLED_DIGITS_TO_NUMERIC_VALUES = Map.of(
        "one", '1',
        "two", '2',
        "three", '3',
        "four", '4',
        "five", '5',
        "six", '6',
        "seven", '7',
        "eight", '8',
        "nine", '9'
    );

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("day1", "src", "main", "resources", "input.txt");
        List<String> lines = Files.readAllLines(path);

        // Solution 1
        List<String> valuesSolution1 = new ArrayList<>();
        for (String line : lines) {
            char firstDigit = findFirstDigit(line);
            char lastDigit = findLastDigit(line);
            String number = String.copyValueOf(new char[]{firstDigit, lastDigit});
            valuesSolution1.add(number);
        }
        int sum = sumValues(valuesSolution1);
        System.out.println(sum);

        // Solution 2
        List<String> valuesSolution2 = new ArrayList<>();
        for (String line : lines) {
            char firstDigit = findFirstDigit2(line);
            char lastDigit = findLastDigit2(line);
            String number = String.copyValueOf(new char[]{firstDigit, lastDigit});
            valuesSolution2.add(number);
        }
        int sum2 = sumValues(valuesSolution2);
        System.out.println(sum2);
    }

    private static char findFirstDigit(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (NUMERIC_VALUES.contains(line.charAt(i))) {
                return line.charAt(i);
            }
        }
        throw new RuntimeException("No digit found!");
    }

    private static char findLastDigit(String line) {
        for (int i = line.length() - 1; i >= 0; i--) {
            if (NUMERIC_VALUES.contains(line.charAt(i))) {
                return line.charAt(i);
            }
        }
        throw new RuntimeException("No digit found!");
    }

    private static char findFirstDigit2(String line) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (NUMERIC_VALUES.contains(c)) return c;
            String fragment = line.substring(i);
            for (String spelledDigit : SPELLED_DIGITS) {
                if (fragment.startsWith(spelledDigit)) return SPELLED_DIGITS_TO_NUMERIC_VALUES.get(spelledDigit);
            }
        }
        throw new RuntimeException("No digit found!");
    }

    private static char findLastDigit2(String line) {
        int counter = 0;
        for (int i = line.length() - 1; i >= 0; i--) {
            char c = line.charAt(i);
            if (NUMERIC_VALUES.contains(c)) return c;
            String reversedLine = new StringBuilder(line)
                .reverse()
                .toString();
            String fragment = reversedLine.substring(counter);
            for (String spelledDigit : SPELLED_DIGITS) {
                String reversedDigit = new StringBuilder(spelledDigit)
                    .reverse()
                    .toString();
                if (fragment.startsWith(reversedDigit)) return SPELLED_DIGITS_TO_NUMERIC_VALUES.get(spelledDigit);
            }
            counter ++;
        }
        throw new RuntimeException("No digit found!");
    }

    private static int sumValues(List<String> values) {
        return values.stream()
            .map(Integer::parseInt)
            .reduce(Integer::sum)
            .orElse(0);
    }
}
