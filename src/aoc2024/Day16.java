package aoc2024;

import java.io.*;
import java.util.*;

public class Day16 {

    // Directions: N=0, E=1, S=2, W=3
    static final int[][] DIRECTIONS = {
        {-1, 0}, {0, 1}, {1, 0}, {0, -1}
    };

    public static void main(String[] args) throws IOException {
        String filePath = "input.txt"; 
        List<List<Character>> grid = readFile(filePath);
        
        int rows = grid.size();
        int cols = grid.get(0).size();
        int[] start = new int[2], end = new int[2];

        //parse grid to find start S and end E
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid.get(i).get(j) == 'S') {
                    start[0] = i;
                    start[1] = j;
                } else if (grid.get(i).get(j) == 'E') {
                    end[0] = i;
                    end[1] = j;
                }
            }
        }

        Map<String, Integer> visited = goThroughMap(grid, start, end, rows, cols);
        //get minimum cost
        int minCost = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            String endState = end[0] + "," + end[1] + "," + d;
            minCost = Math.min(minCost, visited.getOrDefault(endState, Integer.MAX_VALUE));
        }
        System.out.println("Lowest Score: " + minCost);
        
        Set<String> shortestPathTiles = shortestPath(grid, visited, end, rows, cols);
        System.out.println("Number of tiles: " + shortestPathTiles.size());
    }

    public static List<List<Character>> readFile(String filePath) {        
        List<List<Character>> grid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                List<Character> row = new ArrayList<>();
                for (char ch : line.toCharArray()) {
                    row.add(ch);
                }
                grid.add(row);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }


    public static Map<String, Integer> goThroughMap(List<List<Character>> grid, int[] start, int[] end, int rows, int cols) {
        // Directions: N=0, E=1, S=2, W=3
        int[] startState = {start[0], start[1], 1};  //start facing going East (index 1)

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0])); //min-heap based on cost
        pq.offer(new int[]{0, startState[0], startState[1], startState[2]});  // cost, x, y, direction

        Map<String, Integer> visited = new HashMap<>();
        visited.put(start[0] + "," + start[1] + "," + startState[2], 0);

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int cost = current[0], x = current[1], y = current[2], d = current[3];

            //skip if already processed a better cost
            if (visited.getOrDefault(x + "," + y + "," + d, Integer.MAX_VALUE) < cost) {
                continue;
            }

            //move forward
            int[] direction = DIRECTIONS[d];
            int nx = x + direction[0], ny = y + direction[1];
            if (0 <= nx && nx < rows && 0 <= ny && ny < cols && grid.get(nx).get(ny) != '#') {
                int newCost = cost + 1;
                if (newCost < visited.getOrDefault(nx + "," + ny + "," + d, Integer.MAX_VALUE)) {
                    visited.put(nx + "," + ny + "," + d, newCost);
                    pq.offer(new int[]{newCost, nx, ny, d});
                }
            }

            //turn left or right
            for (int nd : new int[]{(d - 1 + 4) % 4, (d + 1) % 4}) {
                int newCost = cost + 1000;
                if (newCost < visited.getOrDefault(x + "," + y + "," + nd, Integer.MAX_VALUE)) {
                    visited.put(x + "," + y + "," + nd, newCost);
                    pq.offer(new int[]{newCost, x, y, nd});
                }
            }
        }

        return visited;
    }

    public static Set<String> shortestPath(List<List<Character>> grid, Map<String, Integer> visited, int[] end, int rows, int cols) {
        int minEndCost = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            String endState = end[0] + "," + end[1] + "," + d;
            minEndCost = Math.min(minEndCost, visited.getOrDefault(endState, Integer.MAX_VALUE));
        }

        Set<String> onShortestPath = new HashSet<>();
        Queue<int[]> q = new LinkedList<>();
        for (int d = 0; d < 4; d++) {
            String endState = end[0] + "," + end[1] + "," + d;
            if (visited.containsKey(endState) && visited.get(endState) == minEndCost) {
                onShortestPath.add(endState);
                q.offer(new int[]{end[0], end[1], d});
            }
        }

        while (!q.isEmpty()) {
            int[] state = q.poll();
            int cx = state[0], cy = state[1], cd = state[2];
            int currentCost = visited.getOrDefault(cx + "," + cy + "," + cd, Integer.MAX_VALUE);

            //backward for forward moves
            int[] direction = DIRECTIONS[cd];
            int px = cx - direction[0], py = cy - direction[1];
            if (0 <= px && px < rows && 0 <= py && py < cols && grid.get(px).get(py) != '#') {
                int prevCost = currentCost - 1;
                if (prevCost >= 0 && visited.getOrDefault(px + "," + py + "," + cd, Integer.MAX_VALUE) == prevCost) {
                    String prevState = px + "," + py + "," + cd;
                    if (!onShortestPath.contains(prevState)) {
                        onShortestPath.add(prevState);
                        q.offer(new int[]{px, py, cd});
                    }
                }
            }

            //backward for turns
            int turnCost = currentCost - 1000;
            if (turnCost >= 0) {
                for (int pd : new int[]{(cd - 1 + 4) % 4, (cd + 1) % 4}) {
                    String prevState = cx + "," + cy + "," + pd;
                    if (visited.getOrDefault(prevState, Integer.MAX_VALUE) == turnCost) {
                        if (!onShortestPath.contains(prevState)) {
                            onShortestPath.add(prevState);
                            q.offer(new int[]{cx, cy, pd});
                        }
                    }
                }
            }
        }

        Set<String> shortestPathTiles = new HashSet<>();
        for (String state : onShortestPath) {
            String[] parts = state.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            shortestPathTiles.add(x + "," + y);
        }

        return shortestPathTiles;
    }
}
