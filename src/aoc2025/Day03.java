package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

    public static void main(String[] args) {
        
        String filePath = "input.txt" ;
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            List<List<Integer>> banks = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                List<Integer> bank = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    bank.add(c - '0');
                }
                banks.add(bank);
            }

            System.out.println(TotalVoltage(banks, 2));
            System.out.println(TotalVoltage(banks, 12));
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }


    }

    //Added Parameter for character Length because of Part two
    public static long TotalVoltage(List<List<Integer>> banks, int length) {
        long ret = 0;
        for (List<Integer> bank : banks) {
            List<Integer> result = new ArrayList<>();
            boolean success = FindLargestNumber(bank, result, length, 0);
            if (!success) {
                System.out.println("No Numbers found.");
            }
            long mult = 1;
            for (int i = result.size() - 1; i >= 0; i--) {
                ret += result.get(i) * mult;
                mult *= 10;
            }
        }
        return ret;
    }

    public static boolean FindLargestNumber(List<Integer> bank, List<Integer> result, int length, int startingPoint) {
        if (length == 0) {
            return true;
        }
        //Count down from 9
        for (int lookFor = 9; lookFor >= 0; lookFor--) {
            
            int lastPosition = bank.size() - length + 1;
            
            for (int position = startingPoint; position < lastPosition; position++) {
                if (bank.get(position) == lookFor) {
                    result.add(lookFor);
                    if (FindLargestNumber(bank, result, length - 1, position + 1)) {
                        return true;
                    } else {
                        result.removeLast();
                    }
                }
            }
        }
        return false;
    }
}
