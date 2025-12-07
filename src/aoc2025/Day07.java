package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day07 {
    public static void main(String[] args) {
        String filePath = "C:\\JavaProjects\\Event\\AoC2025\\Day07\\input.txt";
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            List<String> grid = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                grid.add(line);
            }
    
            int countSplits = 0;
            int row = 1;
            Set<Integer> beams = new TreeSet<>();
            beams.add(grid.getFirst().indexOf('S'));
            while (row < grid.size()) {
                Set<Integer> nextLineBeams = new TreeSet<>();
                for (int beam : beams) {
                    if (grid.get(row).charAt(beam) == '^') {
                        nextLineBeams.add(beam - 1);
                        nextLineBeams.add(beam + 1);
                        countSplits++;
                    } else {
                        nextLineBeams.add(beam);
                    }
                }
                beams = nextLineBeams;
                row++;
            }
            System.out.println("Number of splits: "+countSplits);    
            
            Long timelines = calculateTimelines(grid, new HashMap<Splitter, Long>(), 1, grid.getFirst().indexOf('S'));
            System.out.println("Number of timelines: "+timelines);
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    //google and found how to best calculate this
    public static long calculateTimelines(List<String> grid, Map<Splitter, Long> cache, int row, int col) {
        if (row == grid.size()) {
            return 1;
        }
        if (grid.get(row).charAt(col) == '^') {
            Splitter split = new Splitter(row, col);
            Long cached = cache.get(split);
            if (cached == null) {
                cached = calculateTimelines(grid, cache, row + 1, col - 1) +
                         calculateTimelines(grid, cache, row + 1, col + 1);
                cache.put(split, cached);
            }
            return cached;
        } else {
            return calculateTimelines(grid, cache, row + 1, col);
        }
    }

    record Splitter(int row, int col) {}
}