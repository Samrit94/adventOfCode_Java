package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day22 {

    public static void main(String[] args) {
        String filePath = "input.txt";
        
        List<String> input = new ArrayList<String>();
        
        input = readFile(filePath);
        
        HashMap<Long, Long> memory = new HashMap<>();
        HashMap<Record, Integer> changes = new HashMap<>();

        long sum = 0;
        for (String s : input) {

            long n = Integer.parseInt(s);
            HashSet<Record> changesSeen = new HashSet<>();
            LinkedList<Integer> change = new LinkedList<>();

            for (int i = 0; i < 2000; i++) {
                long original = n;

                if (memory.containsKey(n)) {
                    n = memory.get(n);
                }
                else {
                    n = mix(n, n * 64);
                    n = prune(n);
                    n = mix(n, n / 32);
                    n = prune(n);
                    n = mix(n, n * 2048);
                    n = prune(n);
                    memory.put(original, n);
                }

                int price = (int) n % 10;
                change.add((int) (price - original % 10));
                if (change.size() > 4) {
                    change.removeFirst();
                    Record r = Record.convert(change);
                    if (!changesSeen.contains(r)) {
                        if (!changes.containsKey(r)) {
                            changes.put(r, 0);
                        }
                        changes.put(r, changes.get(r) + price);
                        changesSeen.add(r);
                    }
                }
            }

            sum += n;
        }
        System.out.println("Sum of the 2000th number: "+sum);

        long max = 0;
        for (int n : changes.values()) {
            if (n > max) {
                max = n;
            }
        }
        System.out.println("Most bananas to get: "+max);
    }

    private static List<String> readFile(String filePath) {
        List<String> file = new ArrayList<String>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                file.add(line);
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static long mix(long n1, long n2) {
        return n1 ^ n2;
    }

    public static long prune(long n) {
        return n % 16777216;
    }
    
    private record Record(int n1, int n2, int n3, int n4) {
        
        public static Record convert(LinkedList<Integer> l) {
            return new Record(l.get(0), l.get(1), l.get(2), l.get(3));
        }
    
    }
}
