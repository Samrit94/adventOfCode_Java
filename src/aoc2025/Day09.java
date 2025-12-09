package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 {

    public static void main(String[] args) {
        String filePath = "input.txt";
        
        try( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            
            long largestArea = 0;
            String line;
            List<Coordinate> coords = new ArrayList<>();
            List<Coord> coordsPart2 = new ArrayList<>();
            
            while ((line = br.readLine()) != null) {
                String[] coordinate = line.split(",");
                coords.add(new Coordinate(Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1])));
                //For Part2
                coordsPart2.add(Coord.fromStr(line));
            }
            
            for (int i = 0; i < coords.size(); i++) {
                for (int k = i + 1; k < coords.size(); k++) {
                    Coordinate first = coords.get(i);
                    Coordinate second = coords.get(k);
                    long size = (long) (Math.abs(first.x - second.x) + 1) * (Math.abs(first.y - second.y) + 1);
                    if (size > largestArea) {
                        largestArea = size;
                    }
                }
            }
            
            System.out.println("Largest Area: " + largestArea);        
            
            //Part 2 had to google a different way to solve it...            
            List<Rectangle> rectangles = new ArrayList<>();
            List<Rectangle> lines = new ArrayList<>();

           //build all rectangles from every pair of points
            for (int i = 0; i < coordsPart2.size() - 1; i++) {
                for (int j = i + 1; j < coordsPart2.size(); j++) {
                    rectangles.add(Rectangle.fromCoords(coordsPart2.get(i), coordsPart2.get(j)));
                }
            }

            //build rectangles from consecutive points 
            for (int i = 0; i < coordsPart2.size(); i++) {
                int nextIndex = (i + 1) % coordsPart2.size(); // wrap around
                lines.add(Rectangle.fromCoords(coordsPart2.get(i), coordsPart2.get(nextIndex)));
            }

            //find the largest rectangle area that does not overlap
            long maxArea = rectangles.stream()
                              .filter(r -> lines.stream().noneMatch(r::isOverlap))
                              .mapToLong(Rectangle::area)
                              .max()
                              .orElseThrow();

            System.out.println("Largest Area with only red and green tiles: " + maxArea);            
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }     

        
    }
    
    public static class Coordinate {
        public long x;
        public long y;

        public Coordinate(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public void set(long x, long y) {
            this.x = x;
            this.y = y;
        }

    }
    
    //Needed for Part 2
    public record Coord(long x, long y) {
        static Coord fromStr(String str) {
            var parts = str.split(",");
            return new Coord(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }
    }
    
    public record Rectangle(Coord min, Coord max) {
        boolean isOverlap(Rectangle other) {
            return min.x < other.max.x && max.x > other.min.x && min.y < other.max.y && max.y > other.min.y;
        }
    
        long area() {
            return (max.x - min.x + 1) * (max.y - min.y + 1);
        }
    
        static Rectangle fromCoords(Coord a, Coord b) {
            return new Rectangle(new Coord(Math.min(a.x, b.x), Math.min(a.y, b.y)), new Coord(Math.max(a.x, b.x), Math.max(a.y, b.y)));
        }
    }
    
}
