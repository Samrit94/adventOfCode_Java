package aoc2024;

import java.io.*;
import java.util.*;

public class Day18 {

    private static final int W = 71;
    private static final int H = 71;
    private static final int N = 1024;

    public static void main(String[] args) throws IOException {
        String filePath = "input.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            List<Pos> corrupt = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                String[] line = s.split(",");
                corrupt.add(new Pos(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
            }

            HashSet<Pos> path = new HashSet<>();
            for (int i = 0; i < N; i++) {
                path.add(corrupt.get(i));
            }
            System.out.println("Shortest Path: "+steps(path));

            for (int i = N; i<corrupt.size(); i++) {
                path.add(corrupt.get(i));
                int res = steps(path);
                if (res == -1) {
                    System.out.println("Block exit at: "+corrupt.get(i).col + "," + corrupt.get(i).row);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int steps(HashSet<Pos> fallen) {
        Pos start = new Pos(0, 0);
        Pos end = new Pos(H - 1, W - 1);

        PriorityQueue<State> queue = new PriorityQueue<>();
        HashSet<Pos> visited = new HashSet<>();
        visited.add(start);
        queue.add(new State(start, 0));
        while (!queue.isEmpty()) {
            State curr = queue.poll();
            if (curr.pos.equals(end)) {
                return curr.steps;
            }
            for (Direction dir : Direction.values()) {
                Pos stepped = new Pos(curr.pos.col + dir.c, curr.pos.row + dir.r);
                if (stepped.col < 0 || stepped.col >= H || stepped.row < 0 || stepped.row >= W) {
                    continue;
                }
                if (!fallen.contains(stepped) && !visited.contains(stepped)) {
                    visited.add(stepped);
                    queue.add(new State(stepped, curr.steps + 1));
                }
            }
        }
        return -1;
    }

    record State(Pos pos, int steps) implements Comparable<State> {
        @Override
        public int compareTo(State o) {
            return steps - o.steps;
        }
    }

    record Pos(long col, long row) {
    }

    enum Direction {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1);

        final int r;
        final int c;

        Direction(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}