package aoc2024;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    
    public static void main(String[] args) throws Exception {
        String filePath = "input.txt";        
        
        List<String> numberInputs = readFile(filePath);
        var part1 = new KeyPad(2);
        long complexity = numberInputs.stream().mapToLong(part1::complexity).sum();
        System.out.println("Complexity at 2: "+ complexity);
        
        //had to change previous code because 25 was taking to long with the old code, old code in Class Day21_old
        var part2 = new KeyPad(25);
        complexity = numberInputs.stream().mapToLong(part2::complexity).sum();
        System.out.println("Complexity at 25: "+complexity);
        
        
    }    
    
    public static List<String> readFile(String filePath){
        List<String> file = new ArrayList<String>();
        try( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            file.addAll(br.lines().toList());
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return file;
    }

}

enum Key {
    ACTIVATE('A', "<0,^3,<^,v>"),
    ZERO('0', "^2,>A"),
    ONE('1', "^4,>2"),
    TWO('2', "<1,^5,>3,v0"),
    THREE('3', "<2,^6,vA"),
    FOUR('4', "^7,>5,v1"),
    FIVE('5', "<4,^8,>6,v2"),
    SIX('6', "<5,^9,v3"),
    SEVEN('7', ">8,v4"),
    EIGHT('8', "<7,>9,v5"),
    NINE('9', "<8,v6"),
    UP('^', ">A,vv"),
    DOWN('v', "<<,^^,>>"),
    LEFT('<', ">v"),
    RIGHT('>', "^A,<v");

    static {
        Arrays.stream(values()).forEach(Key::init);
    }

    private final char symbol;
    private final String instructions;
    private final Collection<Neighbour> neighbours = new HashSet<>();

    Key(char symbol, String instructions) {
        this.symbol = symbol;
        this.instructions = instructions;
    }

    static Key from(char symbol) {
        return Arrays.stream(values()).filter(v -> v.symbol == symbol).findFirst().orElseThrow();
    }

    private void init() {
        neighbours.addAll(Arrays.stream(instructions.split(",")).map(Neighbour::new).toList());
    }

    char symbol() {
        return symbol;
    }

    Collection<Neighbour> neighbours() {
        return neighbours;
    }
}

class KeyPad {
    private static final Map<String, String> INSTRUCTION_CACHE = new HashMap<>();
    private final int repeats;

    KeyPad(int repeats) {
        this.repeats = repeats;
    }

    String instructions(String sequence) {
        var cached = INSTRUCTION_CACHE.get(sequence);
        if (cached != null) {
            return cached;
        }
        Key state;
        var sb = new StringBuilder();
        Key previousState = Key.ACTIVATE;
        for (char c : sequence.toCharArray()) {
            state = Key.from(c);
            sb.append(path(previousState, state));
            previousState = state;
        }
        String instructions = sb.toString();
        INSTRUCTION_CACHE.put(sequence, instructions);
        return instructions;
    }

    Map<String, Long> sequenceMap(String instructions) {
        if (instructions.equals("A")) {
            return Map.of("A", 1L);
        }
        Map<String, Long> map = new HashMap<>();
        for (String part : instructions.split("A")) {
            map.compute(part + "A", (k, v) -> v == null ? 1L : v + 1L);
        }
        return map;
    }

    Map<String, Long> iterate(Map<String, Long> sequenceMap) {
        Map<String, Long> map = new HashMap<>();
        sequenceMap.forEach((sequence, a) ->
                sequenceMap(instructions(sequence)).forEach((instruction, b) ->
                        map.compute(instruction, (unused, c) -> c == null ? a * b : c + a * b)));
        return map;
    }

    Map<String, Long> repeatInstructions(String sequence) {
        var instructions = instructions(sequence);
        Map<String, Long> sequenceMap = sequenceMap(instructions);
        for (int i = 0; i < repeats; i++) {
            sequenceMap = iterate(sequenceMap);
        }
        return sequenceMap;
    }

    long complexity(String sequence) {
        return length(sequence) * value(sequence);
    }

    private long value(String sequence) {
        return Long.parseLong(sequence.replaceAll("A", ""));
    }

    private long length(String sequence) {
        return repeatInstructions(sequence).entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().length()).sum();
    }

    String path(Key from, Key to) {
        var queue = new PriorityQueue<NextDirection>();
        Set<Turn> visited = new HashSet<>();
        queue.add(new NextDirection(from, 0, null, null));
        NextDirection bestPath = null;
        while (!queue.isEmpty()) {
            var current = queue.remove();
            if (current.key() == to) {
                bestPath = current;
                break;
            }
            for (Neighbour neighbour : current.key().neighbours()) {
                var turn = new Turn(current.key(), current.direction(), neighbour.direction(), neighbour.key());
                if (!visited.contains(turn)) {
                    visited.add(turn);
                    queue.add(new NextDirection(neighbour.key(), current.distance() + turn.cost(), neighbour.direction(), current));
                }
            }

        }
        return Objects.requireNonNull(bestPath).path();
    }
}

record Neighbour(Key direction, Key key) {
    Neighbour(String s) {
        this(Key.from(s.charAt(0)), Key.from(s.charAt(1)));
    }
}

record Turn(Key key, Key fromDirection, Key toDirection, Key toKey) {
    int cost() {
        if (fromDirection == null) {
            return switch (toDirection) {
                case LEFT -> 50;
                case UP, DOWN -> 100;
                case RIGHT -> 200;
                default -> throw new IllegalStateException();
            };
        }
        return fromDirection == toDirection ? 1 : 1000;
    }
}

record NextDirection(Key key, int distance, Key direction, NextDirection previous) implements Comparable<NextDirection> {

    @Override
    public int compareTo(NextDirection o) {
        return distance != o.distance ? Integer.compare(distance, o.distance)
                : key != o.key ? key.compareTo(o.key)
                : direction == null ? (o.direction == null ? 0 : -1)
                : o.direction == null ? 1 : direction.compareTo(o.direction);
    }

    String path() {
        List<Key> l = new ArrayList<>();
        l.add(Key.ACTIVATE);
        var nextDirection = this;
        while (nextDirection.direction != null) {
            l.add(nextDirection.direction);
            nextDirection = nextDirection.previous;
        }
        Collections.reverse(l);
        return l.stream().map(k -> String.valueOf(k.symbol())).collect(Collectors.joining());
    }
}

//Previous code used for Part 1 but took to long for Part 2
class Day21_Old{
    private static final String[] GRID = {
        "789", "456", "123", "#0A"
    };
    
    private static final String[] GRID2 = {
        "#^A", "<v>"
    };

    // Cache to store shortest paths (using a Map in Java)
    private static final Map<String, Long> cache = new HashMap<>();

    public static void runOld(String file) throws IOException {
        //String file = "input.txt"; 
        StringBuilder inputString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inputString.append(line).append("\n");
            }
        }

        String[] lines = inputString.toString().split("\n");
        
        long part1 = 0; 
        int time = 2;
        
        // Process each line of the input
        for (String line : lines) {
            line = "A" + line;  
            long shortest = 0;

            for (int i = 0; i < line.length() - 1; i++) {
                shortest += shortestPath(GRID, line.charAt(i), line.charAt(i + 1), time);
            }
            part1 += Integer.parseInt(line.substring(1, line.length() - 1)) * shortest;
        }

        System.out.println("Number of complexities: " + part1);
    }

    //find shortest path
    public static long shortestPath(String[] grid, char start, char end, int human) {
        String cacheKey = start + "-" + end + "-" + human;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        //find positions of start and end on the grid
        int sx = -1, sy = -1, ex = -1, ey = -1;
        for (int x = 0; x < grid.length; x++) {
            if (grid[x].indexOf(start) != -1) {
                sx = x;
                sy = grid[x].indexOf(start);
            }
            if (grid[x].indexOf(end) != -1) {
                ex = x;
                ey = grid[x].indexOf(end);
            }
        }

        //calculate movement
        String vert = (sx > ex) ? "^".repeat(sx - ex) : "v".repeat(ex - sx);
        String hor = (sy > ey) ? "<".repeat(sy - ey) : ">".repeat(ey - sy);

        if (human == 0) {
            return Math.abs(sx - ex) + Math.abs(sy - ey) + 1;
        }

        //try two movement orders and take minimum cost
        String a = "A" + vert + hor + "A";
        String b = "A" + hor + vert + "A";

        long acost = 0;
        long bcost = 0;

        //calculate the cost for both directions
        for (int i = 0; i < a.length() - 1; i++) {
            acost += shortestPath(GRID2, a.charAt(i), a.charAt(i + 1), human - 1);
        }
        for (int i = 0; i < b.length() - 1; i++) {
            bcost += shortestPath(GRID2, b.charAt(i), b.charAt(i + 1), human - 1);
        }

        if (hor.equals("<<") && human != 0) {
            return acost;
        }

        //cache result
        long result = Math.min(acost, bcost);
        cache.put(cacheKey, result);
        return result;
    }
}