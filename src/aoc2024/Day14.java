package aoc2024;

import java.io.*;
import java.util.*;


public class Day14 {

    public static void main(String[] args) {        
        String filePath = "input.txt";
        List<String> robots = readFile(filePath);
        int width = 101;
        int height = 103;
        
        //Part 1
        int seconds = 100;        
        long safety = readSaftyFactor(robots, width, height, seconds);
        System.out.println("Safty factor: "+safety);
        
        //Part 2
        long time = firstEasterEgg(robots, width, height);
        System.out.println("Lowes elapse time for Easter Egg: "+time);
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

//---------------------------- Part 1 ----------------------------
    public static long readSaftyFactor(List<String> robots, int width, int height, int numSeconds) {
        int NUM_ROBOTS = robots.size()-1;       
        
        long q1 = 0, q2 = 0, q3 = 0, q4 = 0;

        for (int i = 0; i < NUM_ROBOTS; i++) {
            String line = robots.get(i);
            if (line == null) {
                break;
            }
            String[] parts = line.split(" ");
            String[] position = parts[0].split("=")[1].split(",");
            String[] velocity = parts[1].split("=")[1].split(",");

            int px = Integer.parseInt(position[0]);
            int py = Integer.parseInt(position[1]);
            int vx = Integer.parseInt(velocity[0]);
            int vy = Integer.parseInt(velocity[1]);

            //modulo logic for positions after 100 seconds
            px = mod(px + vx * numSeconds, width);
            py = mod(py + vy * numSeconds, height);

            //quadrant
            if (px < width / 2 && py < height / 2) {
                q1++;
            }
            if (px > width / 2 && py < height / 2) {
                q2++;
            }
            if (px < width / 2 && py > height / 2) {
                q3++;
            }
            if (px > width / 2 && py > height / 2) {
                q4++;
            }
        }

        long safety = q1 * q2 * q3 * q4;
        
        return safety;
    }
    
    
    public static int mod(int a, int b) {
        int result = a % b;
        if (result < 0) {
            result += b;
        }
        return result;
    }
    
//---------------------------- Part 2 ----------------------------
    public static long firstEasterEgg(List<String> robots, int width, int height) {
        List<Bot> bots = new ArrayList<>();
        long time  = 0;        
        for (int i = 0; i < robots.size(); i++) {
            String line = robots.get(i);
            String[] parts = line.split(" ");
            String[] position = parts[0].split("=")[1].split(",");
            String[] velocity = parts[1].split("=")[1].split(",");

            int x = Integer.parseInt(position[0]);
            int y = Integer.parseInt(position[1]);
            int dx = Integer.parseInt(velocity[0]);
            int dy = Integer.parseInt(velocity[1]);
            bots.add(new Bot(x, y, dx, dy));
            
        } 

        for (int i = 0; i < 100000; i++) {
            char[][] grid = new char[height][width];
            for (int j = 0; j < height; j++) {
                Arrays.fill(grid[j], '.');
            }

            boolean distinct = true;

            for (Bot bot : bots) {
                bot.x += bot.dx;
                bot.y += bot.dy;

                if (bot.x < 0) {
                    bot.x += width;
                } else if (bot.x >= width) {
                    bot.x -= width;
                }

                if (bot.y < 0) {
                    bot.y += height;
                } else if (bot.y >= height) {
                    bot.y -= height;
                }

                if (grid[bot.y][bot.x] == '.') {
                    grid[bot.y][bot.x] = '#';
                } else {
                    distinct = false;
                }
            }

            if (distinct) {
                time = (i + 1);
                break; //get the first distinct number
            }
        }
        
        return time;
    }
    //additional Class for part2
    static class Bot {
        int x;
        int y;
        int dx;
        int dy;

        Bot(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }
    }
}
