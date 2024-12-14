package aoc2024;

import java.io.*;
import java.util.*;

public class Day12 {
    
    static char[][] map;
    static boolean[][] done;
    static int height = 0, width, area, perimeter, fences;
    static long part1 = 0, part2 = 0;
    
    public static void main(String[] args){
        String filePath = "input.txt";
        List<String> lines = readFile(filePath);

        //set static variables
        height = lines.size();
        width = lines.get(0).length();
        map = new char[height][width];
        done = new boolean[height][width];

        //fill map with input data
        for (int i = 0; i < height; i++) {
            map[i] = lines.get(i).toCharArray();
        }

        //calculate part1 and part2
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!done[y][x]) {
                    area = perimeter = fences = 0;
                    process(y, x, map[y][x]);
                    part1 += area * perimeter;
                    part2 += area * fences;
                }
            }
        }

        System.out.println("Total price of Fencing Part 1: "+part1);
        System.out.println("Total Price of Fencing Part 2: "+part2);
    }

    public static List<String> readFile(String filepath){
        BufferedReader reader;
        List<String> lines = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line;
            //Read from file
            while ((line = reader.readLine()) != null)
            {
                 lines.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return lines;
    }

    //function to get get map value
    public static int nb(int y, int x) {
        if (y < 0 || y >= height || x < 0 || x >= width) return -1;
        return map[y][x];
    }

    //recursive function to process each point
    public static void process(int y, int x, char v) {
        if (done[y][x]) return; //if already done, return
        done[y][x] = true; //mark as done
        area++;

        //count fences based on surrounding cells
        if (nb(y - 1, x) != v && nb(y, x - 1) != v) fences++;
        if (nb(y - 1, x) != v && nb(y, x + 1) != v) fences++;
        if (nb(y + 1, x) != v && nb(y, x + 1) != v) fences++;
        if (nb(y + 1, x) != v && nb(y, x - 1) != v) fences++;
        if (nb(y, x + 1) == v && nb(y + 1, x) == v && nb(y + 1, x + 1) != v) fences++;
        if (nb(y - 1, x) == v && nb(y, x + 1) == v && nb(y - 1, x + 1) != v) fences++;
        if (nb(y, x - 1) == v && nb(y + 1, x) == v && nb(y + 1, x - 1) != v) fences++;
        if (nb(y, x - 1) == v && nb(y - 1, x) == v && nb(y - 1, x - 1) != v) fences++;

        //check all directions and recursively process connection points
        if (nb(y - 1, x) != v) perimeter++; else process(y - 1, x, v);
        if (nb(y + 1, x) != v) perimeter++; else process(y + 1, x, v);
        if (nb(y, x - 1) != v) perimeter++; else process(y, x - 1, v);
        if (nb(y, x + 1) != v) perimeter++; else process(y, x + 1, v);
    }

    
}
