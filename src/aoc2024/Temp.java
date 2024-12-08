package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Temp {  
	//Test Class to test out codeparts
    public static void main(String[] args) {
    	String filename = "test.txt";
    	 List<String> map = new ArrayList<>();
         Map<Character, List<Point>> antennaTypeToLocations = new HashMap<>();
         
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
             
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
	
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
    
    static class Hasher {
        public int hashCode(Point p) {
            return Objects.hash(p.row, p.col);
        }
    }
    
}
