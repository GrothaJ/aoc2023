package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

public class Main {

    private static final Path PATH = Paths.get("day2", "src", "main", "resources", "input.txt");
    private static final Map<String, Integer> MAX_NUMBER_PER_COLOUR = Map.of(
        "red", 12,
        "green", 13,
        "blue", 14
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(PATH);

        // Solution 1
        int sum = 0;
        for (String line : lines) {
            boolean isPossible = true;
            int id = getId(line);
            String[] games = getGames(line);
            for (String game : games) {
                String[] draws = game.split(",");
                for (String draw : draws) {
                    String[] exactDraw = draw.trim().split(" ");
                    int numberOfBalls = Integer.parseInt(exactDraw[0]);
                    String colourOfBalls = exactDraw[1];
                    if (numberOfBalls > MAX_NUMBER_PER_COLOUR.get(colourOfBalls)) {
                        isPossible = false;
                    }
                }
            }
            if (isPossible) {
                sum += id;
            }
        }
        System.out.println(sum);

        // Solution 2
        int solution = 0;
        for (String line : lines) {
            Map<String, Integer> fewestBalls = new HashMap<>(Map.of(
                "red", 0,
                "green", 0,
                "blue", 0
            ));
            String[] games = getGames(line);
            for (String game : games) {
                String[] draws = game.split(",");
                for (String draw : draws) {
                    String[] exactDraw = draw.trim().split(" ");
                    int numberOfBalls = Integer.parseInt(exactDraw[0]);
                    String colourOfBalls = exactDraw[1];
                    if (numberOfBalls > fewestBalls.get(colourOfBalls)) {
                        fewestBalls.put(colourOfBalls, numberOfBalls);
                    }
                }
            }
            solution += fewestBalls.get("red") * fewestBalls.get("green") * fewestBalls.get("blue");
        }
        System.out.println(solution);
    }

    private static int getId(String line) {
        int indexOfSemicolon = line.indexOf(':');
        String id = line.substring(5, indexOfSemicolon);
        return Integer.parseInt(id);
    }

    private static String[] getGames(String line) {
        int indexOfSemicolon = line.indexOf(':');
        String lineWithoutId = line.substring(indexOfSemicolon + 1);
        return lineWithoutId.split(";");
    }
}
