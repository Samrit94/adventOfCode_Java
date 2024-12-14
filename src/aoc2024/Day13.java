package aoc2024;

import java.io.*;
import java.util.*;

public class Day13 {
    
    public static void main(String[] args) {
        // Input file name
        String filePath = "input.txt";
        
        int part1 = getTotalNumberOfTokens(filePath);
        System.out.println("Total number of tokens spent: "+part1);
        
        
        long part2 = getTotalNumberOfTokens2(filePath);
        System.out.println("Total number of tokens spent Part 2: "+part2);
    }
    
//---------------------------- Part 1 ----------------------------
    public static int getTotalNumberOfTokens(String filePath) {
        List<int[]> buttonA = new ArrayList<>();
        List<int[]> buttonB = new ArrayList<>();
        List<int[]> prize = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; 
                
                if (line.contains("Button A")) {
                    line = line.replace("Button A: X+", "").replace(", Y+", " ");
                    String[] parts = line.split(" ");
                    buttonA.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
                } else if (line.contains("Button B")) {
                    line = line.replace("Button B: X+", "").replace(", Y+", " ");
                    String[] parts = line.split(" ");
                    buttonB.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
                } else if (line.contains("Prize")) {
                    line = line.replace("Prize: X=", "").replace(", Y=", " ");
                    String[] parts = line.split(" ");
                    prize.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        int cost = calculateCost(buttonA, buttonB, prize);
        return cost;
    }
    
    public static int calculateCost(List<int[]> buttonA, List<int[]> buttonB ,  List<int[]> prize) {
        int totalCost = 0;
        for (int i = 0; i < buttonA.size(); i++) {
            int[] buttonAInfo = buttonA.get(i);
            int[] buttonBInfo = buttonB.get(i);
            int[] prizeInfo = prize.get(i);

            int cost = getCheapestCost(
                buttonAInfo[0], buttonAInfo[1], 
                buttonBInfo[0], buttonBInfo[1], 
                prizeInfo[0], prizeInfo[1]
            );

            if (cost >= 0) {
                totalCost += cost;
            }
        }
        return totalCost;
    }
    
    public static int getCheapestCost(int ax, int ay, int bx, int by, int px, int py) {
        int cheapestCost = -1;
        for (int na = 0; na <= 100; na++) {
            int nb = (px - na * ax) / bx;
            if (nb < 0 || nb > 100) {
                continue;
            }
            if ((na * ax) + (nb * bx) == px && (na * ay) + (nb * by) == py) {
                int cost = 3 * na + nb;
                if (cheapestCost == -1 || cheapestCost > cost) {
                    cheapestCost = cost;
                }
            }
        }
        return cheapestCost;
    }
    
  //---------------------------- Part 2 ----------------------------
    
    public static long getTotalNumberOfTokens2(String filePath) {
        List<long[]> buttonA = new ArrayList<>();
        List<long[]> buttonB = new ArrayList<>();
        List<long[]> prize = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; 

                if (line.contains("Button A")) {
                    line = line.replace("Button A: X+", "").replace(", Y+", " ");
                    String[] parts = line.split(" ");
                    buttonA.add(new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])});
                } else if (line.contains("Button B")) {
                    line = line.replace("Button B: X+", "").replace(", Y+", " ");
                    String[] parts = line.split(" ");
                    buttonB.add(new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])});
                } else if (line.contains("Prize")) {
                    line = line.replace("Prize: X=", "").replace(", Y=", " ");
                    String[] parts = line.split(" ");
                    prize.add(new long[]{Long.parseLong(parts[0]) + 10000000000000L, 
                                         Long.parseLong(parts[1]) + 10000000000000L});
                }
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        } 

        long cost = calculateCostLong(buttonA, buttonB, prize);
        return cost;
    }
    
    public static long calculateCostLong(List<long[]> buttonA, List<long[]> buttonB ,  List<long[]> prize) {
        long totalCost = 0;
        for (int i = 0; i < buttonA.size(); i++) {
            long[] buttonAInfo = buttonA.get(i);
            long[] buttonBInfo = buttonB.get(i);
            long[] prizeInfo = prize.get(i);

            long cost = getCheapestCost2(
                buttonAInfo[0], buttonAInfo[1], 
                buttonBInfo[0], buttonBInfo[1], 
                prizeInfo[0], prizeInfo[1]
            );

            if (cost >= 0) {
                totalCost += cost;
            }
        }
        
        return totalCost;
    }
    
    public static long getCheapestCost2(long ax, long ay, long bx, long by, long px, long py) {
        long nb = (ay * px - ax * py) / (ay * bx - ax * by);
        long na = (px - nb * bx) / ax;
        if ((na * ax) + (nb * bx) == px && (na * ay) + (nb * by) == py) {
            return 3 * na + nb;
        }
        return -1;
    }
    

}
