package aoc2024;

import java.io.*;
import java.util.*;

public class Day19 {

    private static Colors head;
    private static HashMap<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        head = new Colors(null);
        String filePath = "input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String[] towels = br.readLine().split(", ");
            for (String towel : towels) {
                head.countValidTowels(towel);
            }
    
            List<String> patterns = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                patterns.add(s);
            }
    
            long p1 = 0;
            long p2 = 0;
            for (String pattern : patterns) {
                long numPoss = head.checkMatch(pattern);
                if (numPoss > 0) {
                    p1++;
                }
                p2 += numPoss;
            }
            System.out.println("Valid Towel pattern: "+p1);
            System.out.println("Possible Compinations: "+p2);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class Colors {
        private Colors[] out = new Colors[5];
        private final Character c;
        private boolean possible;

        public Colors(Character c) {
            this.c = c;
        }

        public void countValidTowels(String towel) {
            if (towel.isEmpty()) {
                possible = true;
            } else {
                char first = towel.charAt(0);
                int index = convert(first);
                if (out[index] == null) {
                    out[index] = new Colors(first);
                }
                out[index].countValidTowels(towel.substring(1));
            }
        }

        //  0 white (w), 1 blue (u), 2 black (b), 3 red (r), or 4 green (g)
        private int convert(char c) {
            return switch (c) {
                case 'w' -> 0;
                case 'u' -> 1;
                case 'b' -> 2;
                case 'r' -> 3;
                case 'g' -> 4;
                default -> throw new IllegalArgumentException();
            };
        }

        public long checkMatch(String pattern) {
            if (c == null) {
                if (cache.containsKey(pattern)) {
                    return cache.get(pattern);
                }
            }
            long result = 0;
            if (pattern.isEmpty()) {
                result = possible ? 1 : 0;
            } else {
                if (possible) {
                    result += head.checkMatch(pattern);
                }
                int index = convert(pattern.charAt(0));
                if (out[index] != null) {
                    result += out[index].checkMatch(pattern.substring(1));
                }
            }
            if (c == null) {
                cache.put(pattern, result);
            }
            return result;
        }
    }
}