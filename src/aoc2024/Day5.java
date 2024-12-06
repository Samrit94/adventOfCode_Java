package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Day5 {
    public static void main(String[] args) {
        List<String> ruleList = new ArrayList<String>();
        List<String> pageList = new ArrayList<String>();
        List<String> validList = new ArrayList<String>();
        List<String> inValidList = new ArrayList<String>();
        
        // Path to input file
        String filePath = "input.txt";        
        readFile(filePath, ruleList, pageList);
        
        //Part1
        getSortLists( ruleList, pageList,validList, inValidList);        
        int resultPart1 = sumMiddleValue(validList);
        System.out.println("Sum of Valid Numebrs in the Middle: "+resultPart1);        
        
        //Part2
        Map<Integer, Rule> rules = new HashMap<>();
        for (int i = 0; i< ruleList.size(); i++) {           
            //read through the rules 
            int[] lineNumbers = Arrays.stream(ruleList.get(i).split("\\|")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();
            if (!rules.containsKey(lineNumbers[0])) { rules.put(lineNumbers[0], new Rule(lineNumbers[0])); }
            if (!rules.containsKey(lineNumbers[1])) { rules.put(lineNumbers[1], new Rule(lineNumbers[1])); }
            
            rules.get(lineNumbers[0]).second.add(lineNumbers[1]);
            rules.get(lineNumbers[1]).first.add(lineNumbers[0]);            
        }
        
        int sumPartTwo = 0; 
       
        for (int i = 0; i< inValidList.size(); i++) {
            String temp = inValidList.get(i);      
            int[] line = Arrays.stream(temp.split(",")).mapToInt(str -> Integer.parseInt(str.trim())).toArray();
            Rule[] sort = Arrays.stream(line).mapToObj(p -> rules.get(p)).toArray(Rule[]::new);
            Arrays.sort(sort);
            sumPartTwo += sort[sort.length / 2].rule;
        }
        System.out.println("Sum of middle page numbers after sorting: " + sumPartTwo); 
    }
    

    static class Rule implements Comparable<Rule> {
        int rule;
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        
        public Rule(int rule) {
            this.rule = rule;
        }

        @Override
        public int compareTo(Rule o) {
            if (this.first.contains(o.rule)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    
    public static int getMiddleValue(String[] line) {
        int size = line.length;        
        int indexAtHalf = size /2;
        return Integer.parseInt(line[indexAtHalf]);
    }
    
    public static int sumMiddleValue(List<String> valid) {
        //count Middle values
        int validSize = valid.size();
        int sum=0;
        for (int row = 0; row < validSize; row++) {
            
            String[] line = valid.get(row).split(",");
            float lineSize = line.length;
            //System.out.println("Size:"+lineSize);
            int indexAtHalf = (int) Math.ceil(lineSize / 2);
            //System.out.println("Num:"+line[indexAtHalf]);
            sum +=  Integer.parseInt(line[indexAtHalf-1]);
        }
        
        return sum;
    }
    
    public static void getSortLists(List<String> rules, List<String> pages, List<String> valid, List<String> inValid) {
        int pageSize = pages.size();
        boolean isValid = true;
        
        for (int i = 0; i < pageSize; i++ ){
            String[] line = pages.get(i).split(",");
            int indexMax = line.length;            
            for (int j = indexMax-1; j >= 0; j--) {
                String checkValue = "|"+line[j];
                //System.out.println("Checkvalue:"+checkValue);
                for (int rule = 0; rule < rules.size(); rule++) {
                    String checkAgainst = rules.get(rule);
                    //System.out.println("checkAgainst:"+checkAgainst);
                    if (checkAgainst.contains(checkValue)) {
                        //System.out.println("checkAgainst:"+checkAgainst);
                        for (int k = j; k < indexMax ; k++) {
                            if (checkAgainst.contains(line[k]+"|")){
                                isValid = false;
                                break;
                            }
                        }                        
                    }    
                }
            }
            
            if (isValid) {
                valid.add(pages.get(i));
            }
            else {
                inValid.add(pages.get(i));
            }        
            isValid = true;
        }                   
    }
    
    
    public static void readFile(String filename, List<String> rules, List<String> pages) {
        try
        {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;
                //Read from file
                while ((line = reader.readLine()) != null)
                {
                    if (line.contains("|")){
                        rules.add(line);
                    }
                    else if(line.contains(",")) {
                        pages.add(line);
                    }
                }
                reader.close();
                
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }        
    }



}

