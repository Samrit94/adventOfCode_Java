package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day05 {

    public static void main(String[] args) {
        
        String filePath = "input.txt" ;
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Range> rules = new ArrayList<>();
            //read first part of Input for the rules
            while (!(line = br.readLine()).isEmpty()) {
                String[] split = line.split("-");
                String start = split[0];
                String end = split[1];
                rules.add(new Range(Long.parseLong(start), Long.parseLong(end)));
            }

            //check if the next lines of the input is in the rules
            long resultPart1 = 0;
            while ((line = br.readLine()) != null) {
                long candidate = Long.parseLong(line);
                if (rules.stream().anyMatch(r -> r.contains(candidate))) {
                    resultPart1++;
                }
            }
            System.out.println("Number of fresh ingredients: "+resultPart1);

            //remove duplicates 
            rules.sort(Comparator.comparingLong(r -> r.startValue));
            for (int idx = 0; idx < rules.size() - 1; ) {
                Range range1 = rules.get(idx);
                Range range2 = rules.get(idx + 1);
                if (range1.contains(range2.startValue)) {
                    range1.endValue = Math.max(range1.endValue, range2.endValue);
                    range1.startValue = Math.min(range1.startValue, range2.startValue);
                    rules.remove(idx + 1);
                } else {
                    idx++;
                }
            }
            
            //use AtomicLong for easier Handling
            AtomicLong resultPart2 = new AtomicLong();
            rules.forEach(rule -> resultPart2.addAndGet(rule.endValue - rule.startValue));            
            System.out.println("Number of considered fresh ingredients: "+resultPart2.get());
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }
        
        
    }

    public static class Range {
        long startValue; 
        long endValue;    

        public Range(long start, long end) {
            this.startValue = start;
            this.endValue = end + 1;
        }

        public boolean contains(long value) {
            return value >= startValue && value < endValue;
        }
    }
}
