package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Temp {  
	//Test Class to test out codeparts
    public static void main(String[] args) {
    	String filePath = "test.txt";
        char [][] twoDimesionArray =  readFile(filePath);

    }
	
	
    public static char[][] readFile(String filename) {
    	ArrayList array = new ArrayList();
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
