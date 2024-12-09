package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Day09 {
    
    public static void main(String[] args) {
        String filePath = "input.txt";
        char[] input = readFile(filePath);
        DecimalFormat df = new DecimalFormat("####.##");
        List<Block> blocks = new ArrayList<>();
                
        //Part 1
        blocks = readLine(input);
        double checkSum = calculateCheckSum(blocks);
        String stringCheckSum = df.format(checkSum);
        System.out.println("CheckSum: "+ stringCheckSum);
        
        //Part 2
        List<Block> wholeFiles = new ArrayList<>();
        wholeFiles = readLinePart2(input);
        double checkSum2 = calculateCheckSumPart2(wholeFiles);
        String stringCheckSum2 = df.format(checkSum2);
        System.out.println("CheckSum Part2: "+ stringCheckSum2);

    }


    public static char[] readFile(String filePath) {
        BufferedReader br;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(filePath));
            line = br.readLine();  
            br.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        char[] input = line.toCharArray();
        
        return input;
    }
        
//----------------------Part 1 ----------------------------   
    public static double calculateCheckSum (List<Block> blocks) {
        double checkSum = 0;
        double pos = 0;
        
        for (Block b : blocks) {
            for (int i = 0; i < b.len; i++) {
                checkSum += pos * b.id;
                pos++;
            }
            for (int n : b.space) {
                if (n < 0) { break; }
                checkSum += pos * n;
                pos++;
            }
        }        
        return checkSum;        
    }
    
    public static List<Block> readLine(char[] input) {
        List<Block> blocks = new ArrayList<>();
        int start, end;
        
        for (int i = 0; i < input.length; i++) {
            if (i % 2 == 0) {
                blocks.add(new Block(i / 2, input[i] - '0', 0));
            } else {
                blocks.get(blocks.size() - 1).free = input[i] - '0';
            }
        }
        
        start = 0;
        end = blocks.size() - 1;
        while (start < end) {
            Block f = blocks.get(start), b = blocks.get(end);
            while (f.free > 0) {
                //switch to next back source
                if (b.len <= 0) {
                    blocks.remove(b);
                    b = blocks.get(--end);
                    if (start == end){ 
                        break; 
                    }
                }
                //fill up space
                f.space.add(b.id);
                f.free--;
                b.len--;
            }
            start++;
        }
        return blocks; 
    }    
    
//----------------------Part 2 ----------------------------  
    public static double calculateCheckSumPart2(List<Block> blocks) {
        double checkSum = 0;
        int pos = 0;
        for (Block b : blocks) {
            for (int i = 0; i < b.len; i++) {
                checkSum += pos * b.id;
                pos++;
            }
            for (int n : b.space) {
                if (n >= 0) {
                    checkSum += pos * n;
                }
                pos++;
            }
            //skip over left over free space
            pos += b.free;
        }
        return checkSum;
    }
    
    public static List<Block> readLinePart2(char[] input) {
        List<Block> wholeFile = new ArrayList<>();
        int start, end;
        
        for (int i = 0; i < input.length; i++) {
            if (i % 2 == 0) {
                wholeFile.add(new Block(i / 2, input[i] - '0', 0));
            } else {
                wholeFile.get(wholeFile.size() - 1).free = input[i] - '0';
            }
        }
    
        start = 0;
        end = wholeFile.size() - 1;
        while (end > 0) {
            start = 0;
            while (start < end) {
                //look for enough space, insert
                if (wholeFile.get(start).free >= wholeFile.get(end).len) {
                    for (int i = 0; i < wholeFile.get(end).len; i++) {
                        wholeFile.get(start).space.add(wholeFile.get(end).id);
                    }
                    wholeFile.get(start).free -= wholeFile.get(end).len;
                    // Essentially skipping in checksum, but the position is still there
                    wholeFile.get(end).id = 0;
                    break;
                }
                start++;
            }
            end--;
        }
        return wholeFile; 
    }
        
//--------additinal Class--------------
//represents data blocks including free space
    static class Block {
        int id, len, free;
        List<Integer> space;

        public Block(int id, int len, int free) {
            this.id = id;
            this.len = len;
            this.free = free;
            space = new ArrayList<>();
        }

        public String toString() {
            String s = id + "x" + len + "+" + space;
            return s;
        }
    }
}