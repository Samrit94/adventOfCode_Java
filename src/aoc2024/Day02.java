package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day02 {

    public static void main(String[] args) {
        // Input file path (change this to your file's path)
        String filePath = "reports.txt";
        int safeCount = 0;
        int safeCount2 = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] levelsStr = line.split(" ");
                int[] levels = new int[levelsStr.length];
                for (int i = 0; i < levelsStr.length; i++) {
                    levels[i] = Integer.parseInt(levelsStr[i]);
                }

                if (isSafe(levels)) {
                    safeCount++;
                    safeCount2++;
                }
                else {
                	if (canBeMadeSafeByRemovingOne((levels))) {
                		safeCount2++;
                	}
                }
            }
            System.out.println("Number of safe reports: " + safeCount);
            System.out.println("Number of safe reports Part 2: " + safeCount2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  //check all increasing or all decreasing
    public static boolean isSafe(int[] levels) {
        
        boolean isIncreasing = true;
        boolean isDecreasing = true;
        
        for (int i = 1; i < levels.length; i++) {
            int diff = Math.abs(levels[i] - levels[i - 1]);
            if (diff < 1 || diff > 3) {
                return false; //difference not between 1 and 3
            }
            if (levels[i] < levels[i - 1]) {
                isIncreasing = false; 
            }
            if (levels[i] > levels[i - 1]) {
                isDecreasing = false; 
            }
        }
        
        return isIncreasing || isDecreasing;
    }
    
    //Part 2
    public static boolean canBeMadeSafeByRemovingOne(int[] levels) {
        // removing each level one by one 
        for (int i = 0; i < levels.length; i++) {
            // Create a new array excluding one level according to index i
            int[] newLevels = new int[levels.length - 1];
            int index = 0;
            for (int j = 0; j < levels.length; j++) {
                if (j != i) {
                    newLevels[index++] = levels[j];
                }
            }
            //check new array
            if (isSafe(newLevels)) {
                return true; 
            }
        }
        return false;
    }
}
