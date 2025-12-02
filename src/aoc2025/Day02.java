package aoc2025;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;


public class Day02 {

    public static void main(String[] args) {
        
        String filePath = "input.txt" ;
        String[] input;
        
        try (BufferedReader rdr = new BufferedReader(new FileReader(filePath))) {
            ArrayList<String> fileLines = new ArrayList<>();
            String line;

            while((line = rdr.readLine()) != null) {
                fileLines.add(line);
            }

            input = fileLines.toArray(new String[0]);
            
            //Count when copy found two times
            String regexPart1 = "^([1-9]\\d*)\\1$";
            CountInvalids(input, regexPart1);
            
            //Count when copy found more than two times -> RegEx 1+
            String regexPart2 = "^([1-9]\\d*)\\1+$";
            CountInvalids(input, regexPart2);
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }
        
    }
    
    public static void CountInvalids(String[] input, String pattern) {
        String[] idLine = input[0].split(",");
        long sum = 0L;
        for(String id : idLine) {
            long start = Long.parseLong(id.split("-")[0]);
            
            long end = Long.parseLong(id.split("-")[1]);
            
            for(long i = start; i <= end; i++) {
                if(String.valueOf(i).matches(pattern)) {
                    sum += i;
                }
            }
        }
        
        System.out.println("Count of invalid IDs: " + sum);
    }
    
}
