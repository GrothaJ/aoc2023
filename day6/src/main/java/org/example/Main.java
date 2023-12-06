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

    private static final Path PATH = Paths.get("day6", "src", "main", "resources", "input.txt");

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(PATH);

        // Solution 1
        List<String> timeList = getTimeList(lines);
        List<String> distanceList = getDistanceList(lines);
        Map<Long, Long> timeToDistanceMap = getAssignTimeToDistance(timeList, distanceList);
        List<Long> totalNumberOfWinningWays = getNumberOfWinningWays(timeToDistanceMap);
        long result = totalNumberOfWinningWays.stream()
            .reduce((num1, num2) -> num1 * num2)
            .orElse(0L);
        System.out.println(result);

        // Solution 2
        long time = getTimeOrDistance(lines,0, 5);
        long distance = getTimeOrDistance(lines, 1, 9);
        List<Long> winnings = getNumberOfWinningWays(Map.of(time, distance));
        long result2 = winnings.stream()
            .reduce((num1, num2) -> num1 * num2)
            .orElse(0L);
        System.out.println(result2);
    }

    private static Map<Long, Long> getAssignTimeToDistance(List<String> timeList, List<String> distanceList) {
        Map<Long, Long> timeToDistanceMap = new HashMap<>();
        for (int i = 0; i < timeList.size(); i++) {
            long time = Long.parseLong(timeList.get(i));
            long distance = Long.parseLong(distanceList.get(i));
            timeToDistanceMap.put(time, distance);
        }
        return timeToDistanceMap;
    }

    private static List<String> getDistanceList(List<String> lines) {
        String distanceLine = lines.get(1);
        return Arrays.stream(distanceLine.substring(9).split(" "))
            .map(String::trim)
            .filter(it -> !it.isBlank())
            .toList();
    }

    private static List<String> getTimeList(List<String> lines) {
        String timeLine = lines.get(0);
        return Arrays.stream(timeLine.substring(5).trim().split(" "))
            .map(String::trim)
            .filter(it -> !it.isBlank())
            .toList();
    }

    private static List<Long> getNumberOfWinningWays(Map<Long, Long> timeToDistanceMap) {
        return timeToDistanceMap.entrySet().stream()
            .map(it -> calculateOneRace(it.getKey(), it.getValue()))
            .toList();
    }

    private static long calculateOneRace(long time, long distance) {
        int counter = 0;
        for (long timeSpentPressingButton = 1; timeSpentPressingButton < time - 1; timeSpentPressingButton++) {
            long distanceTravelled = calculateDistanceTravelled(timeSpentPressingButton, time);
            if (distanceTravelled > distance) {
                counter++;
            }
        }
        return counter;
    }

    private static long calculateDistanceTravelled(long timeSpentPressingButton, long totalTime) {
        long timeSpentTravelling = totalTime - timeSpentPressingButton;
        return timeSpentTravelling * timeSpentPressingButton;
    }

    private static long getTimeOrDistance(List<String> lines, int listIndex, int index) {
        String timeLine = lines.get(listIndex);
        timeLine = timeLine.substring(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeLine.length(); i++) {
            char c = timeLine.charAt(i);
            if (c != ' ') {
                sb.append(c);
            }
        }
        return Long.parseLong(sb.toString());
    }
}
