package aoc2025;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day12 {

    public static void main(String[] args) {
        String filePath = "input.txt";
        
        try (BufferedReader fileIn = new BufferedReader(new FileReader(filePath))){;
            int[] dimension1 = new int[1000];
            int[] dimension2 = new int[1000];
            int[] sums = new int[1000];
            int row = 0;
            int countRegions = 0;

            String lineIn;
            while ((lineIn = fileIn.readLine()) != null) {
                if (lineIn.length() > 5 && lineIn.substring(2, 3).equals("x")) {
                    dimension1[row] = Integer.parseInt(lineIn.substring(0, 2));
                    dimension2[row] = Integer.parseInt(lineIn.substring(3, 5));
                    int[] rowCounts = new int[6];
                    
                    for (int i = 0; i < 6; i++) {
                        rowCounts[i] = Integer.parseInt(lineIn.substring(7 + 3 * i, 9 + 3 * i));
                    }
                    sums[row] = 6 * rowCounts[0] + 
                                7 * rowCounts[1] + 
                                7 * rowCounts[2] +
                                7 * rowCounts[3] + 
                                5 * rowCounts[4] + 
                                7 * rowCounts[5];
                    row++;
                }
            }

            int[] fits = new int[1000];
            for (int i = 0; i < dimension1.length; i++) {
                fits[i] = (dimension1[i] * dimension2[i]) - sums[i];
                if (fits[i] >= 0) {
                    countRegions++;
                }
            }

            System.out.println("Number of regions that fit: " + countRegions);

        } catch (IOException e) {
            System.out.println("File not found.");
        }
        
    }
}

