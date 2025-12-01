package aoc2025;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Day01 {
    static boolean[] right = new boolean[5000];
    static int[] clicks = new int[5000];    

    public static void main(String[] args) {
        String filePath = "input.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int position = 0;
            String lineIn;
            while ((lineIn = reader.readLine()) != null) {
                if (lineIn.startsWith("R")) {
                    right[position] = true;
                } else {
                    right[position] = false;
                }
                clicks[position] = Integer.parseInt(lineIn.substring(1));
                position++;
            }
            
            FindDoorPassword(position);
            CountNumberOfClicks(position);

            
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }
    
    public static void FindDoorPassword(int position) {
        int pass = 0;
        int point = 50;
        
        for (int i = 0; i < position; i++) { 
            if (right[i]) {
                point += clicks[i];
                while (point >= 100) {
                    point -= 100;
                }
            } else {
                if (point == 0) {
                    point -= clicks[i];
                    while (point < 0) {
                        point += 100;
                    }                   
                } else {
                    point -= clicks[i];
                    while (point < 0) {
                        point += 100;
                    }
                }
            }
            if (point == 0) {
                pass++;
            }
        }
        System.out.println("Password is: "+pass);        
    }
    
    //Duplicated FindDoorPassword and just counted the Clicks accordingly
    public static void CountNumberOfClicks(int position) {
        int count = 0;
        int point = 50;
        
        for (int i = 0; i < position; i++) { 
            if (right[i]) {
                point += clicks[i];
                while (point >= 100) {
                    point -= 100;
                    count++;
                }
            } else {
                if (point == 0) {
                    point -= clicks[i];
                    while (point < 0) {
                        point += 100;
                        count++;
                    }
                    
                    if (point == 0) {
                        count++;
                    }
                    
                    count--;
                    
                } else {
                    point -= clicks[i];
                    while (point < 0) {
                        point += 100;
                        count++;
                    }
                    
                    if (point == 0) {
                        count++;
                    }
                }
            }
        }
        System.out.println("Password by Nunber of Clicks: "+count);        
    }
}

