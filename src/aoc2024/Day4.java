package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {    
    
    public static void main(String[] args) {
        // Path to input file
    
        int totalCount = 0;
        int countHorizontal, countVertical, countDiagonal;
        String filePath = "input.txt";
        char [][] twoDimesionArray = null;
        twoDimesionArray = readFile(filePath);
        
        // Part 1        
        countHorizontal = searchHorizontal(twoDimesionArray);
        System.out.println("Count Horizontal:"+countHorizontal );
        
        countVertical = searchVertical(twoDimesionArray);        
        System.out.println("Count Vertical:"+countVertical );
        
        countDiagonal = searchDiagonal(twoDimesionArray);
        System.out.println("Count Diagonal:"+countDiagonal );    
        
        totalCount = countHorizontal+countVertical+countDiagonal;
        System.out.println("Count Total:"+totalCount );
        
        
        // Part 2
        int countXMax =  countXMasPatterns(twoDimesionArray);
        System.out.println("Count X-MAS: " + countXMax);
        
    }
    
    //Read file into Array
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
    
    //Part 1 functions
    public static int searchPerLine(String line) {
        int count = 0;

            String searchString = "XMAS";
            String searchBackwards = "SAMX";
            Pattern pattern = Pattern.compile(searchString);
            Pattern pattern2 = Pattern.compile(searchBackwards);
            Matcher matcher;
            
            matcher = pattern.matcher(line);                
            while (matcher.find()) {
                   count++;
            }
                
            matcher = pattern2.matcher(line);                
            while (matcher.find()) {
                  count++;
            }
            
        return count;        
    }
    
    public static int searchHorizontal(char [][] searchBase) {
        int count = 0;
        String line = "";
        for (int y = 0; y < searchBase.length; y++)
        {
            line = "";
            char [] temp = searchBase[y];
            for (int x = 0; x < temp.length; x ++ )
            {
                line = line + temp[x];
            }
            //System.out.println(line);
            count += searchPerLine(line);
        }        
        return count;        
    }
    
    public static int searchVertical(char [][] searchBase) {
        int count = 0;
        String line = "";
        char [] temp = searchBase[0];        
        for (int y = 0; y < temp.length; y++)
        {
            line = "";
            for (int x = 0; x < searchBase.length; x++ )
            {
                line = line + searchBase[x][y];
            } 
            //System.out.println(line);
            count += searchPerLine(line);
        }        
        return count;        
    }    
    
    public static int searchDiagonal(char [][] searchBase) {
        int count = 0;
        int rowLength = searchBase.length;
        int colLength = searchBase[0].length;
        int keyWordLength = "XMAS".length();
        
        for(int row = 0; row < rowLength; row++){
            for(int col = 0; col < colLength; col++) { 
                //System.out.println("row:"+row+" column:"+column);
                //System.out.println("max row:"+rowLength+" max column:"+columnLength);
                // Diagonal (top-left to bottom-right)
                if (row <= rowLength - keyWordLength && col <= colLength - keyWordLength && searchDownRight(searchBase, row, col)) {
                    count++;
                }
                // Diagonal (bottom-left to top-right)
                if (row >= keyWordLength - 1 && col <= colLength - keyWordLength && searchUpRight(searchBase, row, col)) {
                    count++;
                }
                // Diagonal (top-right to bottom-left)
                if (row <= rowLength - keyWordLength && col >= keyWordLength - 1 && searchDownLeft(searchBase, row, col)) {
                    count++;
                }
                // Diagonal (bottom-right to top-left)
                if (row >= keyWordLength - 1 && col >= keyWordLength - 1 && searchUpLeft(searchBase, row, col)) {
                    count++;
                }
            }
        }
        return count;        
    }
    
    public static boolean searchUpLeft(char[][] searchBase, int row, int col) {
        boolean found = false;             
        if (searchBase[row][col] == 'X') {            
            if (searchBase[row-1][col-1] =='M') {
                if (searchBase[row-2][col-2]=='A') {
                    if (searchBase[row-3][col-3] =='S') {
                        found = true;
                    }
                }
            }
        }            
        return found;
    }
    
    public static boolean searchUpRight(char[][] searchBase, int row, int col) {
        boolean found = false;    
        if (searchBase[row][col]=='X') {
            if (searchBase[row-1][col+1]=='M') {
                if (searchBase[row-2][col+2]=='A') {
                    if (searchBase[row-3][col+3]=='S') {
                        found = true;
                    }
                }
            }
        }        
        return found;
    }
    
    public static boolean searchDownLeft(char[][] searchBase, int row, int col) {
        boolean found = false;        
        if (searchBase[row][col]=='X') {
            if (searchBase[row+1][col-1]=='M') {
                if (searchBase[row+2][col-2]=='A') {
                    if (searchBase[row+3][col-3]=='S') {
                        found = true;
                    }
                }
            }
        }        
        return found;
    }
    
    public static boolean searchDownRight(char[][] searchBase, int row, int col ) {
        boolean found = false;
        if (searchBase[row][col]=='X') {
            if (searchBase[row+1][col+1]=='M') {
                if (searchBase[row+2][col+2]=='A') {
                    if (searchBase[row+3][col+3]=='S') {
                        found = true;
                    }
                }
            }
        }        
        return found;
    }
    
    
    //Part 2 Method
    public static boolean checkPattern(char[][] searchBase, int i, int j) {
        // Check the main pattern "M.S, .A., M.S"
        if (searchBase[i-1][j-1] == 'M' && searchBase[i-1][j+1] == 'S' && 
            searchBase[i+1][j-1] == 'M' && searchBase[i+1][j+1] == 'S') {
            return true;
        }
        // Check for the reverse pattern
        if (searchBase[i-1][j-1] == 'S' && searchBase[i-1][j+1] == 'M' && 
            searchBase[i+1][j-1] == 'S' && searchBase[i+1][j+1] == 'M') {
            return true;
        }
        if (searchBase[i-1][j-1] == 'S' && searchBase[i-1][j+1] == 'S' && 
            searchBase[i+1][j-1] == 'M' && searchBase[i+1][j+1] == 'M') {
            return true;
        }
        if (searchBase[i-1][j-1] == 'M' && searchBase[i-1][j+1] == 'M' && 
            searchBase[i+1][j-1] == 'S' && searchBase[i+1][j+1] == 'S') {
            return true;
        }
        return false;
    }
    
    public static int countXMasPatterns(char[][] searchBase) {
        int count = 0;
        int rows = searchBase.length;
        int cols = searchBase[0].length;

        // Check each potential center for the X-MAS pattern
        for (int i = 1; i < rows - 1; i++) {  //row index of the center
            for (int j = 1; j < cols - 1; j++) {  //column index of the center
                if (searchBase[i][j] == 'A') {
                    // Check surrounding four corners
                    if (checkPattern(searchBase, i, j)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
