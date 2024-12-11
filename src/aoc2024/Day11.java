package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;

public class Day11 {

    public static void main(String[] args) {
        
        String filePath = "input.txt";
        List<String> stones = new ArrayList<String>();
        List<String> changedStones = new ArrayList<String>();
        stones = readFile(filePath);
        
        //Part 1
        changedStones = applyRules(stones,25) ;
        System.out.println("Count of Stones after 25 blinks: "+changedStones.size());
        
        //Part 2
        //created new method using HashMaps since applyRule gets problems with more than 35 blinks...
        try {
            long stoneCount = calcApplyRules(filePath,75);
            System.out.println("Count of Stones after 75 blinks: "+stoneCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    public static long calcApplyRules(String filePath, int repeat) throws Exception {
        
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        long[] input = Arrays.stream(line.split(" "))
                        .mapToLong(Long::parseLong)
                        .toArray();
        br.close();
        
        Map<Long, Long> stones = new HashMap<>(), next = new HashMap<>();
        Map<Long, Long[]> cache = new HashMap<>();

        for (long l : input) {
            stones.put(l, stones.getOrDefault(stones, 0l) + 1);
        }

        long count = 0;
        for (int blink = 1; blink <= repeat; blink++) {
            next.clear();

            for (long i : stones.keySet()) {
                if (i == 0) {               
                    //apply first rule
                    next.put(1l, next.getOrDefault(1l, 0l) + stones.get(i));
                } 
                else if (cache.containsKey(i)) {
                    //check if calculated result of this stone was already
                    for (Long l : cache.get(i)) {
                        next.put(l, next.getOrDefault(l, 0l) + stones.get(i));
                    }
                } 
                else {
                    int digits = (int) (Math.log10(i) + 1);
                    if (digits % 2 == 0) {
                        //apply second rule
                        long front = (long) (i / Math.pow(10, digits / 2));
                        long back = (long) (i % Math.pow(10, digits / 2));
                        next.put(front, next.getOrDefault(front, 0l) + stones.get(i));
                        next.put(back, next.getOrDefault(back, 0l) + stones.get(i));
                        cache.put(i, new Long[]{front, back});
                    } 
                    else {
                        //apply third rule
                        long num = 2024 * i;
                        next.put(num, next.getOrDefault(num, 0l) + stones.get(i));
                        cache.put(i, new Long[]{num});
                    }
                }
            }
            stones = Map.copyOf(next);
        }

        for (long num : stones.keySet()) {
            count += stones.get(num);
        }

        return count;
    }
    
    
    public static List<String> applyRules(List<String> input, int repeat){
        List<String> tempList = new ArrayList<String>();
        DecimalFormat df = new DecimalFormat("####.##");
        tempList = input;        
        int i = 0;
        do {
            List<String> workingList = new ArrayList<String>();
            //System.out.println(tempList.toString());
            for (int stone = 0; stone < tempList.size(); stone++) {
                String sStone = tempList.get(stone);
                if( sStone.equals("0")) {
                    workingList.add("1");
                }
                else if(sStone.length()%2==0) {
                    double stone1 = Double.valueOf(sStone.substring(0, sStone.length()/2));
                    double stone2 = Double.valueOf(sStone.substring((sStone.length()/2)));
                    workingList.add(df.format(stone1));
                    workingList.add(df.format(stone2));
                }
                else {
                    double calc = Double.valueOf(sStone)*2024;
                    String sCalc = df.format(calc);
                    workingList.add(String.valueOf(sCalc));
                }
                
            }
            i++;
            tempList = workingList;
        }while (i < repeat);
        
        
        return tempList;
        
    }
    
    public static List<String> readFile(String filepath){
        BufferedReader reader;
        List<String> listStones = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line;
            //Read from file
            while ((line = reader.readLine()) != null)
            {
                String[] stones = line.split(" ");
                for(int i = 0; i < stones.length; i++) {
                    listStones.add(stones[i]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listStones;
    }
    

}