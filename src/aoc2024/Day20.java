package aoc2024;

import java.io.*;
import java.util.*;

public class Day20 {

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        // Read the input
        String filePath = "input.txt";
        Object[] map = readFile(filePath);
        
        List<List<Integer>> grid = (List<List<Integer>>) map[0];
        int[] start = (int[]) map[1];
        int[] end = (int[]) map[2];

        //make copy of map
        List<List<Integer>> grid2 = new ArrayList<>();
        for (List<Integer> row : grid) {
            grid2.add(new ArrayList<>(row));
        }

        //get base by filling grid
        int base = fillGrid(grid, start, end);
        
        //fill grid with end point
        fillGrid(grid2, end, new int[]{-1, -1});

        System.out.println("Number of cheats Part 1: " + search(grid, grid2, base, 2));
        System.out.println("Number of cheats Part 2: " + search(grid, grid2, base, 20));
    }
    
    public static Object[] readFile(String filePath) {
        List<List<Integer>> grid = new ArrayList<>();
        int[] start = new int[2];
        int[] end = new int[2];
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                List<Integer> cur = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '#') {
                        cur.add(-1);
                    } else if (c == 'S') {
                        start[0] = grid.size();
                        start[1] = i;
                        cur.add(0);
                    } else if (c == 'E') {
                        end[0] = grid.size();
                        end[1] = i;
                        cur.add(0);
                    } else {
                        cur.add(0);
                    }
                }
                grid.add(cur);
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        
        return new Object[]{grid, start, end};
    }

    public static int fillGrid(List<List<Integer>> grid, int[] start, int[] end) {
        Set<String> visited = new HashSet<>();
        Queue<int[]> que = new LinkedList<>();
        que.add(new int[]{start[0], start[1], 0});
        int result = 0;

        while (!que.isEmpty()) {
            int[] curr = que.poll();
            int row = curr[0], col = curr[1], t = curr[2];

            if (row == end[0] && col == end[1]) {
                result = t;
            }
            visited.add(row + "," + col);
            grid.get(row).set(col, t);

            for (int i = 0; i < 4; i++) {
                int nextRow = row + dr[i];
                int nextCol = col + dc[i];
                if (grid.get(nextRow).get(nextCol) == 0 && !visited.contains(nextRow + "," + nextCol)) {
                    que.add(new int[]{nextRow, nextCol, t + 1});
                }
            }
        }
        return result;
    }

    public static int search(List<List<Integer>> grid, List<List<Integer>> grid2, int base, int cheats) {
        int result = 0;
        for (int row = 1; row < grid.size() - 1; row++) {
            for (int col = 1; col < grid.get(0).size() - 1; col++) {
                if (grid.get(row).get(col) != -1) {
                    for (int cheatRow = Math.max(1, row - cheats); cheatRow <= Math.min(grid.size() - 2, row + cheats); cheatRow++) {
                        for (int cheatCol = Math.max(1, col - cheats + Math.abs(row - cheatRow)); 
                             cheatCol <= Math.min(grid.get(0).size() - 2, col + cheats - Math.abs(row - cheatRow)); cheatCol++) {
                            if (grid.get(cheatRow).get(cheatCol) != -1 &&
                                base - (grid.get(row).get(col) + Math.abs(cheatRow - row) + Math.abs(cheatCol - col) + grid2.get(cheatRow).get(cheatCol)) >= 100) {
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

}
