package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class Day11 {

    public static void main(String[] args) {
        String filePath = "input.txt";
        
        try( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            
            long countPaths = 0;
            
            
            Map<String, List<String>> map = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                map.put(parts[0].substring(0, parts[0].length() - 1),
                        Arrays.asList(parts).subList(1, parts.length));
            }
                
            countPaths = pathFinder("you", "out", new HashMap<>(), map);
            
            System.out.println("Paths from you to out: " + countPaths);

            long countPaths2 = 1; 
            
            countPaths2 *= pathFinder("svr", "fft", new HashMap<>(), map);
            countPaths2 *= pathFinder("fft", "dac", new HashMap<>(), map);
            countPaths2 *= pathFinder("dac", "out", new HashMap<>(), map);

            System.out.println("Paths from visiting dac and fft: " + countPaths2);
        
        } catch (IOException e) {
            System.out.println("File not found.");
        }        
    }    

    public static long pathFinder(String start, String end, Map<String, Long> cache, Map<String, List<String>> map) {
        long paths = 0;
        
        if (start.equals(end)) {
            return 1;
        } else {
            if (map.get(start) == null) {
                return 0;
            }
            for (String child : map.get(start)) {
                paths += cache.containsKey(child) ? cache.get(child) : pathFinder(child, end, cache, map);
            }
        }
        
        cache.put(start, paths);
        
        return paths;
    }
}
