package aoc2024;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day3 {
    public static void main(String[] args) {
        //Path input file
        String filePath = "input2.txt";
        
        //get all matches Part1
        int totalamount = readListsFromFile(filePath);
        System.out.println("Total Amount Part 1: " + totalamount);
        
        //get all matches Part2
        int totalamount2 = readListsFromFile2(filePath);
        System.out.println("Total Amount Part 2: " + totalamount2);
        
    }
    
    //Methods:    
    public static int readListsFromFile(String filePath) {
        List<String> totalHits = new ArrayList<>();   
        int totalamount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String regex = "mul\\((\\d+),(\\d+)\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;
            int x, y;
            
            //read string from line
            while ((line = br.readLine()) != null) {                
                matcher = pattern.matcher(line);                
                while (matcher.find()) {
                    // Append the matched substring to list
                    totalHits.add(matcher.group());
                }
            }

            for (String match : totalHits) {
                String[] temp = match.replace("mul(", "").replace(")", "").split(",");
                x = Integer.parseInt(temp[0]);  // First number 
                y = Integer.parseInt(temp[1]);  // Second number
                
                totalamount += (x*y);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalamount;
    }
    
    public static int readListsFromFile2(String filePath) {
        List<String> totalHits = new ArrayList<>();   
        int totalamount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String regex = "mul\\((\\d+),(\\d+)\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher;
            int x, y;
            
            StringBuilder content = new StringBuilder();
                        
            //performance went bad handling more than one line while cutting out the dont and do 
            //so now add all lines into a single string 
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            //remove dont to do sections
            String processedString = removeDontToDoSections(content.toString());
            //System.out.println(processedContent);

            matcher = pattern.matcher(processedString);     
                while (matcher.find()) {
                    // Append the matched substring to list
                    totalHits.add(matcher.group());
                }                

            for (String match : totalHits) {
                String[] temp = match.replace("mul(", "").replace(")", "").split(",");
                x = Integer.parseInt(temp[0]);  // First number 
                y = Integer.parseInt(temp[1]);  // Second number
                
                totalamount += (x*y);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalamount;
    }
    
    public static String removeDontToDoSections(String content) {
        while (content.contains("don't()")) {
            int dontIndex = content.indexOf("don't()");
            int doIndex = content.indexOf("do()", dontIndex);
            
            if (doIndex != -1) {
                //remove string from dont to do
                content = content.substring(0, dontIndex) + content.substring(doIndex + 4);                
            } else {
                //no ending do remove everything from "don't()" onwards
                content = content.substring(0, dontIndex);
            }
        }
        return content;
    }    
}
