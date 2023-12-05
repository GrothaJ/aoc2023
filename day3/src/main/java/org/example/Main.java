package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Main {

    private static final Path PATH = Paths.get("day3", "src", "main", "resources", "input.txt");
    private static final Set<Character> DIGITS = Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');

    public static void main(String[] args) throws IOException {
        String fileContent = Files.readString(PATH);
        List<String> lines = Files.readAllLines(PATH);
        char[][] matrix = createMatrix(lines);
        int matrixWidth = getLineLength(lines);
        int matrixHeight = lines.size();

        // Solution 1
        int sum = 0;
        Set<Character> symbols = getSymbols(fileContent);
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                char c = matrix[i][j];
                if (DIGITS.contains(c) && (j == 0 || isFirstDigit(matrix, i, j))) {
                    int number = formNumber(matrix, i, j, matrixWidth);
                    boolean isAdjacentToSymbol = checkIfIsAdjacentToSymbol(symbols, matrix, i, j, matrixHeight, matrixWidth, number);
                    if (isAdjacentToSymbol) {
                        sum += number;
                    }
                }
            }
        }
        System.out.println(sum);

        // Solution 2
        int solution = 0;
        List<String> gears = findGears(matrix, matrixWidth, matrixHeight);
        for (String gear : gears) {
            String[] arr = gear.split(" ");
            int gearI = Integer.parseInt(arr[0]);
            int gearJ = Integer.parseInt(arr[1]);
            List<Integer> numbers = getNumbersAroundGear(matrix, gearI, gearJ);
            if (numbers.size() == 2) {
                solution += numbers.stream()
                    .reduce((num1, num2) -> num1 * num2).orElse(0);
            }
        }
        System.out.println(solution);
    }

    private static Set<Character> getSymbols(String fileContent) {
        Set<Character> symbols = new HashSet<>();
        for (int i = 0; i < fileContent.length(); i++) {
            char c = fileContent.charAt(i);
            if (DIGITS.contains(c)) {
                continue;
            }
            if (c == '.') {
                continue;
            }
            if (c == '\n') {
                continue;
            }
            symbols.add(fileContent.charAt(i));
        }
        return symbols;
    }

    private static char[][] createMatrix(List<String> lines) {
        char[][] matrix = new char[getLineLength(lines)][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = line.charAt(j);
            }
        }
        return matrix;
    }

    private static int getLineLength(List<String> lines) {
        return lines.stream()
            .findFirst()
            .map(String::length)
            .orElse(0);
    }

    private static boolean isFirstDigit(char[][] matrix, int i, int j) {
        if (j == 0) {
            return true;
        }
        return !DIGITS.contains(matrix[i][j - 1]);
    }

    private static int formNumber(char[][] matrix, int i, int j, int matrixWidth) {
        String number = Character.toString(matrix[i][j]);
        return Integer.parseInt(formNumber(matrix, i, j, matrixWidth, number));
    }

    private static String formNumber(char[][] matrix, int i, int j, int matrixWidth, String currentNumber) {
        if (j + 1 == matrixWidth) {
            return currentNumber;
        }
        if (!DIGITS.contains(matrix[i][j + 1])) {
            return currentNumber;
        }
        currentNumber = currentNumber + matrix[i][j + 1];
        return formNumber(matrix, i, j + 1, matrixWidth, currentNumber);
    }

    private static boolean checkIfIsAdjacentToSymbol(Set<Character> symbols, char[][] matrix, int i, int j, int matrixHeight, int matrixWidth, int number) {
        int numberLength = String.valueOf(number).length();
        if (i != 0 && j != 0) {
            if (symbols.contains(matrix[i - 1][j - 1]) || symbols.contains(matrix[i][j - 1])) {
                return true;
            }
        }
        if (i + 1 < matrixHeight && j != 0) {
            if (symbols.contains(matrix[i + 1][j - 1])) {
                return true;
            }
        }
        if (i != 0 && j + numberLength < matrixWidth) {
            if (symbols.contains(matrix[i - 1][j + numberLength])) {
                return true;
            }
            if (symbols.contains(matrix[i][j + numberLength])) {
                return true;
            }
        }
        if (i + 1 < matrixHeight && j + numberLength < matrixWidth) {
            if (symbols.contains(matrix[i + 1][j + numberLength])) {
                return true;
            }
        }
        if (i != 0) {
            for (int iterator = 0; iterator < numberLength; iterator++) {
                if (symbols.contains(matrix[i - 1][j + iterator])) {
                    return true;
                }
            }
        }
        if (i + 1 < matrixHeight) {
            for (int iterator = 0; iterator < numberLength; iterator++) {
                if (symbols.contains(matrix[i + 1][j + iterator])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> findGears(char[][] matrix, int matrixWidth, int matrixHeight) {
        List<String> gears = new LinkedList<>();
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                char c = matrix[i][j];
                if (c == '*') {
                    gears.add(i + " " + j);
                }
            }
        }
        return gears;
    }

    private static List<Integer> getNumbersAroundGear(char[][] matrix, int gearI, int gearJ) {
        Set<Integer> numbers = new HashSet<>();
        if (DIGITS.contains(matrix[gearI][gearJ - 1])) {
            int number = formNumber(matrix, gearI, gearJ - 1);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI][gearJ + 1])) {
            int number = formNumber(matrix, gearI, gearJ + 1);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI - 1][gearJ - 1])) {
            int number = formNumber(matrix, gearI - 1, gearJ - 1);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI - 1][gearJ])) {
            int number = formNumber(matrix, gearI - 1, gearJ);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI - 1][gearJ + 1])) {
            int number = formNumber(matrix, gearI - 1, gearJ + 1);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI + 1][gearJ - 1])) {
            int number = formNumber(matrix, gearI + 1, gearJ - 1);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI + 1][gearJ])) {
            int number = formNumber(matrix, gearI + 1, gearJ);
            numbers.add(number);
        }
        if (DIGITS.contains(matrix[gearI + 1][gearJ + 1])) {
            int number = formNumber(matrix, gearI + 1, gearJ + 1);
            numbers.add(number);
        }
        return new ArrayList<>(numbers);
    }

    private static int formNumber(char[][] matrix, int i, int j) {
        StringBuilder number = new StringBuilder(String.valueOf(matrix[i][j]));
        for (int iterator = 1; iterator <= 2; iterator++) {
            if (!DIGITS.contains(matrix[i][j + iterator])) {
                break;
            } else {
                number.append(matrix[i][j + iterator]);
            }
        }
        for (int iterator = 1; iterator <= 2; iterator++) {
            if (!DIGITS.contains(matrix[i][j - iterator])) {
                break;
            } else {
                number.insert(0, matrix[i][j - iterator]);
            }
        }
        return Integer.parseInt(number.toString());
    }
}
