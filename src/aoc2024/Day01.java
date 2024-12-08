package aoc2024;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Day01 {

    public static void main(String[] args) {
        // Path to the input file
        String filePath = "puzzle_input.txt";

        List<int[]> locationLists = readListsFromFile(filePath);

        if (locationLists.size() != 2) {
            System.out.println("Error: The file should contain exactly two columns of numbers.");
            return;
        }

        int[] leftList = locationLists.get(0);
        int[] rightList = locationLists.get(1);

        //calculate distance
        int totalDistance = calculateTotalDistance(leftList, rightList);
        System.out.println("Total distance: " + totalDistance);
        
        //calculate similarity score
        int similarityScore = calculateSimilarityScore(leftList, rightList);
        System.out.println("Total similarity score: " + similarityScore);
    }

    
    //Begin Methods so no other class is needed:
    //read lists from file
    public static List<int[]> readListsFromFile(String filePath) {
        List<int[]> lists = new ArrayList<>();
        int[] leftList = new int[0];
        int[] rightList = new int[0];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Integer> leftNumbers = new ArrayList<>();
            List<Integer> rightNumbers = new ArrayList<>();
            
            //read numbers from line
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split("\\s+");
                leftNumbers.add(Integer.parseInt(numbers[0]));  // First number 
                rightNumbers.add(Integer.parseInt(numbers[1])); // Second number
            }

            //convert lists to arrays
            leftList = leftNumbers.stream().mapToInt(i -> i).toArray();
            rightList = rightNumbers.stream().mapToInt(i -> i).toArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        lists.add(leftList);
        lists.add(rightList);
        return lists;
    }

    //calculate the distance 
    public static int calculateTotalDistance(int[] leftList, int[] rightList) {
        //sort
        Arrays.sort(leftList);
        Arrays.sort(rightList);

        int totalDistance = 0;

        //calculate distance for each pair
        for (int i = 0; i < leftList.length; i++) {
            totalDistance += Math.abs(leftList[i] - rightList[i]);
        }

        return totalDistance;
    }
    
    //calculate the total similarity
    public static int calculateSimilarityScore(int[] leftList, int[] rightList) {
        //convert right list to a map  (googled that no idea if that faster)
        Map<Integer, Integer> rightFrequencyMap = new HashMap<>();
        for (int number : rightList) {
            rightFrequencyMap.put(number, rightFrequencyMap.getOrDefault(number, 0) + 1);
        }

        int similarityScore = 0;

        for (int number : leftList) {
            int frequencyInRightList = rightFrequencyMap.getOrDefault(number, 0);
            similarityScore += number * frequencyInRightList;
        }
        return similarityScore;
    }
}
