package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Day10 {
    record Coords(int x, int y) {}
    public static void main(String[] args){
        String filePath = "input.txt";
        char[][] map;
        
        map = readFile(filePath);
        
        //Part 1
        int sumPart1 = calTotalSumOfTrails(map);
        System.out.println("Total Sum of Trailheads: "+sumPart1 );
        
        //Part 2
        int sumPart2 = calSumOfAllRatings(map);;
        System.out.println("Total Sum of Ratings: "+sumPart2 );
    }
    
    private static char[][] readFile(String filePath){
        char[][] map = null ;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            map = reader.lines().map(String::toCharArray).toArray(char[][]::new);
        }catch (Exception e) {
            e.printStackTrace();
        }        
        return map;        
    }
    
    private static boolean stillInMap(char[][] map, Coords position) {
        boolean inMap = false;
            if (position.y >= 0 
                    && position.y < map.length 
                    && position.x >= 0 
                    && position.x < map[position.y].length) {
                inMap = true;
            }
                
        return inMap;
    }
    
    
//-------------------------------------- Part 1 --------------------------------------
    private static int calTotalSumOfTrails(char[][] map) {
        int totalScore = 0;
        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map[row].length; col++) {
                if(map[row][col] == '0') {
                    totalScore += getTrailScore(map, new Coords(col, row), '0', new HashSet<>());
                }
            }
        }
        return totalScore;
    }

    private static int getTrailScore(char[][] map, Coords currPos, char curr, Set<Coords> visited) {
        visited.add(currPos);
        if(curr == '9') {
            return 1;
        }

        int score = 0;
        //check direction v
        Coords nextPosition = new Coords(currPos.x + 1, currPos.y);
        if(stillInMap(map, nextPosition)
                && !visited.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            score += getTrailScore(map, nextPosition, (char) (curr + 1), visited);
        }
        //check direction >
        nextPosition = new Coords(currPos.x, currPos.y + 1);
        if(stillInMap(map, nextPosition)
                && !visited.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            score += getTrailScore(map, nextPosition, (char) (curr + 1), visited);
        }
        //check direction ^
        nextPosition = new Coords(currPos.x - 1, currPos.y);
        if(stillInMap(map, nextPosition)
                && !visited.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            score += getTrailScore(map, nextPosition, (char) (curr + 1), visited);
        }
        //check direction <
        nextPosition = new Coords(currPos.x, currPos.y - 1);
        if(stillInMap(map, nextPosition)
                && !visited.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            score += getTrailScore(map, nextPosition, (char) (curr + 1), visited);
        }
        return score;
    }

//-------------------------------------- Part 2 --------------------------------------
    private static int calSumOfAllRatings(char[][] grid) {
        int totalRating = 0;
        for(int row = 0; row < grid.length; row++) {
            for(int col = 0; col < grid[row].length; col++) {
                if(grid[row][col] == '0') {
                    totalRating += rateTrail(grid, new Coords(col, row), '0', new HashSet<>());
                }
            }
        }
        return totalRating;
    }

    private static int rateTrail(char[][] map, Coords currPos, char curr, HashSet<Coords> currPath) {
        if(curr == '9') {
            return 1;
        }

        int rating = 0;
        //check direction v
        Coords nextPosition = new Coords(currPos.x + 1, currPos.y);
        if(stillInMap(map, nextPosition)
                && !currPath.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            currPath.add(nextPosition);
            rating += rateTrail(map, nextPosition, (char) (curr + 1), currPath);
            currPath.remove(nextPosition);
        }
        //check direction >
        nextPosition = new Coords(currPos.x, currPos.y + 1);
        if(stillInMap(map, nextPosition)
                && !currPath.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            currPath.add(nextPosition);
            rating += rateTrail(map, nextPosition, (char) (curr + 1), currPath);
            currPath.remove(nextPosition);
        }

        nextPosition = new Coords(currPos.x - 1, currPos.y);
        if(stillInMap(map, nextPosition)
                && !currPath.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            currPath.add(nextPosition);
            rating += rateTrail(map, nextPosition, (char) (curr + 1), currPath);
            currPath.remove(nextPosition);
        }

        nextPosition = new Coords(currPos.x, currPos.y - 1);
        if(stillInMap(map, nextPosition)
                && !currPath.contains(nextPosition)
                && map[nextPosition.y][nextPosition.x] == curr + 1){
            currPath.add(nextPosition);
            rating += rateTrail(map, nextPosition, (char) (curr + 1), currPath);
            currPath.remove(nextPosition);
        }
        return rating;
    }

}