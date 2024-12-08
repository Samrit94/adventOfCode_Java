package aoc2024;
import java.io.*;
import java.util.*;

public class Day8 {
    
    public static void main(String[] args) {
        String filename = "input.txt";
        
        int part1Result = readFileForPart1(filename);
        System.out.println("Number of Unique Locations Part1: "+part1Result);
        
        int part2Result = readFileForPart2(filename);
        System.out.println("Number of Unique Locations Part2: "+part2Result);
    }
    
//--------------------Part 1 -------------------------    
    public static int readFileForPart1(String filename) {
        List<String> map = new ArrayList<>();
        Map<Character, List<Point>> antennaTypeToLocations = new HashMap<>();
        Set<Point> antinodes = new HashSet<>();
        
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line;
            //read file into a String List and Map Type including Class Point
            while ((line = file.readLine()) != null) {
                map.add(line);
                for (int idx = 0; idx < line.length(); idx++) {
                    char c = line.charAt(idx);
                    if (c != '.') {
                        antennaTypeToLocations
                            .computeIfAbsent(c, k -> new ArrayList<>())
                            .add(new Point(map.size() - 1, idx));
                    }
                }
            }
            
            //figuring out unique locations according to instructions through loops
            for (Map.Entry<Character, List<Point>> entry : antennaTypeToLocations.entrySet()) {
                List<Point> locations = entry.getValue();
                for (int i = 0; i < locations.size(); i++) {
                    for (int j = i + 1; j < locations.size(); j++) {
                        Point p1 = locations.get(i);
                        Point p2 = locations.get(j);
                        Point delta = new Point(p1.row - p2.row, p1.col - p2.col);
                        Point an1 = new Point(p1.row + delta.row, p1.col + delta.col);
                        Point an2 = new Point(p2.row - delta.row, p2.col - delta.col);

                        if (inMap(an1, map)) antinodes.add(new Point(p1.row + delta.row, p1.col + delta.col));
                        if (inMap(an2, map)) antinodes.add(new Point(p2.row - delta.row, p2.col - delta.col));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return antinodes.size();
    }

    static boolean inMap(Point p, List<String> map) {
        return p.row >= 0 && p.row < map.size() && p.col >= 0 && p.col < map.get(0).length();
    }


//--------------------Part 2 -------------------------        
    public static int readFileForPart2(String filename) {
        List<String> map = new ArrayList<>();
        Map<Character, List<Point>> antennaTypeToLocations = new HashMap<>();
        Set<Point> antinodes = new HashSet<>();
        
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line;
            //read file into a String List and Map Type including Class Point
            while ((line = file.readLine()) != null) {
                map.add(line);
                for (int idx = 0; idx < line.length(); idx++) {
                    char c = line.charAt(idx);
                    if (c != '.') {
                        antennaTypeToLocations
                            .computeIfAbsent(c, k -> new ArrayList<>())
                            .add(new Point(map.size() - 1, idx));
                    }
                }
            }

          //figuring out unique locations according to new instructions
            //gotta be honest, googled this until I understood what they wanted, still don't understand them fully...
            for (Map.Entry<Character, List<Point>> entry : antennaTypeToLocations.entrySet()) {
                List<Point> locations = entry.getValue();
                for (int i = 0; i < locations.size(); i++) {
                    for (int j = i + 1; j < locations.size(); j++) {
                        Point p1 = locations.get(i);
                        Point p2 = locations.get(j);
                        Point delta = new Point(p1.row - p2.row, p1.col - p2.col);
                        int hcf = findHCF(Math.abs(delta.row), Math.abs(delta.col));
                        delta.row /= hcf;
                        delta.col /= hcf;
                        
                        boolean added = true;
                        int n = 0;
                        while (added) {
                            added = false;
                            Point an1 = new Point(p1.row + n * delta.row, p1.col + n * delta.col);
                            if (inMap(an1, map)) {
                                antinodes.add(an1);
                                added = true;
                            }
                            Point an2 = new Point(p2.row - n * delta.row, p2.col - n * delta.col);
                            if (inMap(an2, map)) {
                                antinodes.add(an2);
                                added = true;
                            }
                            n++;
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return antinodes.size();
    }

    static int findHCF(int n1, int n2) {
        if (n1 == 0) return n2;
        if (n2 == 0) return n1;
        if (n1 == n2) return n1;
        if (n1 > n2) return findHCF(n1 - n2, n2);
        else return findHCF(n1, n2 - n1);
    }

//---------------------helping Classes-------------------------       
    //Point for Antenna location
    static class Point {
        int row;
        int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }
    
    //Hasher to get hash values from location
    static class Hasher {
        public int hashCode(Point p) {
            return Objects.hash(p.row, p.col);
        }
    }
    
    
}
