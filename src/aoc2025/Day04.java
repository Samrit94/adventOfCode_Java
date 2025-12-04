package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day04 {

    public static void main(String[] args) {
        
        String filePath = "input.txt" ;
        
       try( BufferedReader br = new BufferedReader(new FileReader(filePath))){

        String line;
        List<List<Character>> rows = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            List<Character> row = new ArrayList<>(line.chars().mapToObj(i -> (char) i).toList());
            rows.add(row);
        }

        
        List<Position> accessableRoll = findLessThanFour(rows);
        int result = accessableRoll.size();        
        System.out.println("Number of Rolls accessed: "+result);
        
        
        while (!accessableRoll.isEmpty()) {
            accessableRoll.forEach(pos -> rows.get(pos.row).set(pos.col, 'x'));
            accessableRoll = findLessThanFour(rows);
            result += accessableRoll.size();
        }        
        System.out.println("Total Number of Rolls removed: "+result);
        
        
       } catch (IOException e) {
           System.out.println("File not found.");
       }
       
    }

    public static List<Position> findLessThanFour(List<List<Character>> grid) {
        List<Position> result = new ArrayList<>();
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.getFirst().size(); c++) {
                int n = checkSpacesAround(grid, r, c);
                if (n < 4) {
                    result.add(new Position(r, c));
                }
            }
        }
        return result;
    }

    public static int checkSpacesAround(List<List<Character>> grid, int row, int col) {
        int ret = 0;
        if (grid.get(row).get(col) != '@') {
            return 9;
        }
        for (Direction direction : Direction.values()) {
            int rr = row + direction.r;
            int cc = col + direction.c;
            if (rr >= 0 && rr < grid.size() && cc >= 0 && cc < grid.getFirst().size()) {
                if (grid.get(rr).get(cc) == '@') {
                    ret++;
                }
            }
        }
        return ret;
    }

    private enum Direction {
        NW(-1, -1),
        N(-1, 0),
        NE(-1, 1),
        E(0, 1),
        SE(1, 1),
        S(1, 0),
        SW(1, -1),
        W(0, -1);

        public final int r;
        public final int c;

        Direction(int row, int col) {
            this.r = row;
            this.c = col;
        }
    }

    private record Position(int row, int col){}
}

