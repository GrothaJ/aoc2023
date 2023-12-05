package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Main {

    private static final Path PATH = Paths.get("day5", "src", "main", "resources", "input.txt");
    private static final String FIRST_MAPPING_HEADER = "seed-to-soil map:";
    private static final Set<String> MAPPING_HEADERS = Set.of(
        "soil-to-fertilizer map:",
        "fertilizer-to-water map:",
        "water-to-light map:",
        "light-to-temperature map:",
        "temperature-to-humidity map:",
        "humidity-to-location map:"
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(PATH);
        List<String> seeds = getSeeds(lines.stream().findFirst().orElseThrow(RuntimeException::new));
        List<List<String>> mappingList = getMappings(lines);
        List<Long> results = new ArrayList<>();
        for (String seed : seeds) {
            long result = Long.parseLong(seed);
            result = findResult(result, mappingList);
            results.add(result);
        }
        Long finalResult = results.stream().sorted().findFirst().orElse(0L);
        System.out.println(finalResult);
    }

    private static List<String> getSeeds(String firstLine) {
        return new ArrayList<>(List.of(
            firstLine.substring(7)
                .split(" ")
        ));
    }

    private static List<List<String>> getMappings(List<String> linesWithoutSeeds) {
        List<List<String>> mappings = new ArrayList<>();
        List<String> currentMapping = new LinkedList<>();
        for (String line : linesWithoutSeeds) {
            line = line.trim();
            if (line.startsWith("seeds:")) {
                continue;
            }
            if (MAPPING_HEADERS.contains(line)) {
                mappings.add(currentMapping);
                currentMapping = new LinkedList<>();
            } else if (!line.isBlank() && !line.equals(FIRST_MAPPING_HEADER)) {
                currentMapping.add(line);
            }
        }
        mappings.add(currentMapping);
        return mappings;
    }
    private static long findResult(long result, List<List<String>> mappingList) {
        for (List<String> mappings : mappingList) {
            long difference = 0L;
            boolean seedIsInAnyRange = false;
            for (String mapping : mappings) {
                String[] array = mapping.split(" ");
                long targetIndex = Long.parseLong(array[0]);
                long sourceIndex = Long.parseLong(array[1]);
                long offset = Long.parseLong(array[2]);
                boolean seedIsInRange = checkIfSeedIsInRange(result, sourceIndex, offset);
                if (seedIsInRange) {
                    seedIsInAnyRange = true;
                    difference = sourceIndex - targetIndex;
                }
            }
            if (seedIsInAnyRange) {
                result = result - difference;
            }
        }
        return result;
    }
    private static boolean checkIfSeedIsInRange(long seed, long sourceIndex, long range) {
        if (seed < sourceIndex) {
            return false;
        }
        return sourceIndex + range > seed;
    }
}
