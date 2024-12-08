package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day06 {
    static int SIZE;
    static final int up = 0;
    static final int right = 1;
    static final int down = 2;
    static final int left = 3;

    
    public static void main(String[] args) {
        // Path to input file
        String filePath = "input.txt";
        char[][] map = new char[SIZE][SIZE];
        char [][] guardMapPart1 = null;
        int[] currentCoordinates = null;
        
        //Part1
        map = readFile(filePath);
        currentCoordinates = getGuardCordinate(map, '^');
        //System.out.println("Character '" + guardDirection + "' found at: (" + currentCoordinates[0] + ", " + currentCoordinates[1] + ")");      
        guardMapPart1 = createMarkedMap(map, currentCoordinates);        
        int markedSpots = getCountMarkedSpots(guardMapPart1);
        System.out.println("Count or Marked spots: "+ markedSpots);
        
        //Part 2
        //refresh map haven't figured out why it gets overwritten in Part 1...
        map = readFile(filePath); 
        SIZE = map.length;
        int[][] dirMap = new int[SIZE][SIZE];
        currentCoordinates = getGuardCordinate(map, '^');
        int result = countLoops(currentCoordinates[0], currentCoordinates[1], up, 0, map, dirMap);
        System.out.println("Count possibilities for Loop: "+ result);

    }
    
    static int countLoops(int guardRow, int guardCol, int direction, int depth, char[][] map, int[][] dirMap) {
        int count = 0;
        int[][] obstacleMap = new int[SIZE][SIZE];
        while (true) {
            // If searching for a loop and we've been to this position before facing that direction, we have reached a loop
            if (depth == 1 && map[guardRow][guardCol] == 'X' && ((1 << direction) & dirMap[guardRow][guardCol]) != 0) {
                return 1;
            }

            map[guardRow][guardCol] = 'X';
            dirMap[guardRow][guardCol] |= 1 << direction;

            int nextRow = guardRow;
            int nextCol = guardCol;

            //adjust row and column according to direction
            if (direction == up) {
                nextRow--;
            } else if (direction == right) {
                nextCol++;
            } else if (direction == down) {
                nextRow++;
            } else if (direction == left) {
                nextCol--;
            }
            
            //do this while guard is in Map
            if (nextRow >= 0 && nextRow < SIZE && nextCol >= 0 && nextCol < SIZE) {
                if (map[nextRow][nextCol] == '#') {
                    direction++;
                    direction %= 4;
                } else if (depth == 0 && map[nextRow][nextCol] != 'X' && obstacleMap[nextRow][nextCol] == 0) {
                    // Check if an obstacle might lead to a loop
                    char[][] newMap = new char[SIZE][SIZE];
                    int[][] newDirMap = new int[SIZE][SIZE];
                    for (int i = 0; i < SIZE; i++) {
                        System.arraycopy(map[i], 0, newMap[i], 0, SIZE);
                        System.arraycopy(dirMap[i], 0, newDirMap[i], 0, SIZE);
                    }
                    newMap[nextRow][nextCol] = '#';
                    int obstacle = countLoops(guardRow, guardCol, (direction + 1) % 4, 1, newMap, newDirMap);
                    if (obstacle == 1) {
                        obstacleMap[nextRow][nextCol] = 1;
                        count++;
                    }
                    guardRow = nextRow;
                    guardCol = nextCol;
                } else {
                    guardRow = nextRow;
                    guardCol = nextCol;
                }
            } else {
                //guard left the map
                break;
            }
        }

        if (depth == 1) {
            return 0;
        }
        return count;
    }

    
    public static int getCountMarkedSpots(char[][] guardMap) {
        // Loop through rows
           int count = 0;
           for (int row = 0; row < guardMap.length; row++) {
               // Loop through columns
               for (int col = 0; col < guardMap[row].length; col++) {
                   if (guardMap[row][col] == 'X') {
                       count++;
                   }                
               }
           }

           return count;
       }
    
    public static char[][] createMarkedMap(char[][] guardMap, int[] startingCoordinates) {
        char[][] newMap = guardMap.clone();
        int[] currentCoordinates = startingCoordinates;
        int[] nextMove = new int[2];
        char guardDirection = '^';
        boolean inMap = true;
        int maxRow = guardMap.length;
        int maxCol = guardMap[0].length;
        do {
            nextMove = getNextPosition( guardDirection, currentCoordinates);
            //System.out.println("Character '" + guardDirection + "' found at: (" + nextMove[0] + ", " + nextMove[1] + ")"); 
            if (nextMove[0] < 0 || nextMove[0] == maxRow || nextMove[1]<0 || nextMove[1] == maxCol) {
                //System.out.println("Next Coordinates out of map, guard left");  
                newMap[currentCoordinates[0]][currentCoordinates[1]] = 'X';               
                inMap = false;
            }
            else {
                if (guardMap[nextMove[0]][nextMove[1]] != '#') {   
                    //guard moves to the next spot, no obstructions 
                    newMap[currentCoordinates[0]][currentCoordinates[1]] = 'X';
                    currentCoordinates[0] = nextMove[0];
                    currentCoordinates[1] = nextMove[1];                    
                }
                else {
                    //obstruction change direction
                    char newGuard = changeGuardDirection(guardDirection);
                    guardDirection = newGuard;
                }
            }         
            
        }while(inMap);
        
        return newMap;
    }    
    
    public static char changeGuardDirection(char guard) {
        char newDirection = 0;
        //change direction always 90 Degree to the right
        if (guard == '^') { 
            newDirection = '>';                   
        }
        else if (guard == '>') { 
            newDirection = 'v'; 
        }
        else if (guard == 'v') { 
            newDirection = '<';  
        }
        else if (guard == '<') { 
            newDirection = '^'; 
        }   
        
        return newDirection;
        
    }
    
    public static int[] getNextPosition(char guard, int[] position) {
        int[] coordinates = new int[2];        
        if (guard == '^') { //move up
            coordinates[0] = position[0]-1;
            coordinates[1] =  position[1];                    
        }
        else if (guard == '>') { //move right
            coordinates[0] = position[0];
            coordinates[1] =  position[1]+1;   
        }
        else if (guard == 'v') { //move down
            coordinates[0] = position[0]+1;
            coordinates[1] =  position[1];  
        }
        else if (guard == '<') { //move left
            coordinates[0] = position[0];
            coordinates[1] =  position[1]-1;
        }        
        return coordinates;
    };
    
    public static int[] getGuardCordinate(char[][] guardMap,char guard) {
     // Loop through rows
        int[] coordinates = new int[2];
        boolean found = false;
        for (int row = 0; row < guardMap.length; row++) {
            // Loop through columns
            for (int col = 0; col < guardMap[row].length; col++) {
                if (guardMap[row][col] == guard) {
                    coordinates[0] = row;
                    coordinates[1] = col;
                    found = true;
                    break;
                }                
            }
            if (found) break;
        }

        return coordinates;
    }
    
    public static char[][] readFile(String filename) {
        ArrayList<char[]> array = new ArrayList<char[]>();
        char [][] twoDimesionArray = null;
        try
        {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String data;
                //Read from file
                while ((data = reader.readLine()) != null)
                {
                    //Convert data to char array and add into array
                    array.add(data.toCharArray());
                }
                reader.close();

                //Creating a 2D char array base on the array size
                twoDimesionArray = new char [array.size()][];

                //Convert array from ArrayList to 2D array
                for (int i = 0; i < array.size(); i++)
                {
                    twoDimesionArray[i] = (char [])array.get(i);
                }
                
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }        
        return twoDimesionArray;
    }
}