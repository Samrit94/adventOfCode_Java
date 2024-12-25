package aoc2024;

import java.io.*;
import java.util.*;

public class Day25 {

    @SuppressWarnings("unchecked")
    static Set<Integer>[][] locks = new Set[5][6];
    static List<int[]> keys = new ArrayList<>();
    
    public static void main(String[] args) {
        String filePath = "input.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            
            readFileToKeysAndLocks(reader);
            
            long combinations = getCombos();
            System.out.println("Number of combinations: " + combinations);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //find intersection of two Sets
    public static <T> Set<T> checkOverlap(Set<T> set1, Set<T> set2) {
        Set<T> intersection = new HashSet<>();
        Set<T> smaller = set1.size() < set2.size() ? set1 : set2;
        Set<T> larger = set1.size() < set2.size() ? set2 : set1;

        for (T element : smaller) {
            if (larger.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    //get keys and locks from file
    public static void readFileToKeysAndLocks(BufferedReader reader) throws Exception {
        int id = 0;
        int row = 0;
        int[] combo = new int[5];
        String line;
        boolean isKey = true;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                locks[i][j] = new HashSet<>();
            }
        }

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;

            if (row == 0) {
                isKey = !line.equals("#####");
            } else if (row == 6) {
                if (isKey) {
                    keys.add(combo.clone());
                } else {
                    for (int i = 0; i < combo.length; ++i) {
                        locks[i][combo[i]].add(id);
                    }
                    ++id;
                }

                row = -1;
                combo = new int[5];
            } else {
                for (int i = 0; i < line.length(); ++i) {
                    combo[i] += line.charAt(i) == '#' ? 1 : 0;
                }
            }

            ++row;
        }
    }

    //calculate number of combinations
    public static long getCombos() {
        long combos = 0;

        for (int[] key : keys) {
            Set<Integer> keyLocks = new HashSet<>();
            for (int c = 0; c < key.length; ++c) {
                int n = key[c];
                Set<Integer> comboLocks = new HashSet<>();
                while (5 - n >= 0) {
                    comboLocks.addAll(locks[c][5 - n]);
                    ++n;
                }

                if (c == 0) {
                    keyLocks = comboLocks;
                } else {
                    keyLocks = checkOverlap(keyLocks, comboLocks);
                }
            }
            combos += keyLocks.size();
        }

        return combos;
    }


}
