package aoc2025;

import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.Optimisation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class Day10 {

    public static void main(String[] args) {
        
        String filePath = "input.txt";
        
        //Kind of Brute Force it
        int fewestButtonPresses = getFewestButtonPresses(filePath);        
        System.out.println("Fewest Button Presses: " + fewestButtonPresses);
        
        //Had to google a lot and add an External Library 'ojalgo' to solve Part Two
        int joltageLevelCounters = getJoltageLevelCounters(filePath);        
        System.out.println("Fewest Button Presses when considering Joltage Level: " + joltageLevelCounters);
        
    }
    
    //------------------PART 1 -------------------------------------
    
    public static int getFewestButtonPresses(String filePath) {
        int total = 0;
        
        try( BufferedReader br = new BufferedReader(new FileReader(filePath))){
                    
            String input = br.lines().reduce("", (a, b) -> a + "\n" + b).trim();

            List<Machine> machines = new ArrayList<>();
            Pattern lightsPattern = Pattern.compile("\\[([.#]+)\\]");
            Pattern buttonPattern = Pattern.compile("\\(([^)]*)\\)");

            for (String line : input.split("\\R")) {
                Matcher m = lightsPattern.matcher(line);
                if (!m.find()) continue;
                String lights = m.group(1);
                int n = lights.length();
                int desiredState = 0;
                for (int i = 0; i < n; i++) {
                    if (lights.charAt(i) == '#') {
                        desiredState |= (1 << i);
                    }
                }

                List<Integer> buttonMasks = new ArrayList<>();
                Matcher bm = buttonPattern.matcher(line);
                while (bm.find()) {
                    String[] inds = bm.group(1).split(",");
                    int buttonMask = 0;
                    for (String x : inds) {
                        int idx = Integer.parseInt(x.trim());
                        buttonMask |= (1 << idx);
                    }
                    buttonMasks.add(buttonMask);
                }
                machines.add(new Machine(n, desiredState, buttonMasks));
            }

            for (Machine machine : machines) {
                total += breadthFirstSearch(machine.n, machine.desiredState, machine.buttonMasks);
            }
            
        } catch (IOException e) {
            System.out.println("File not found.");
        }     
        
        return total;
    }
    
    public static class Machine {
        int n;
        int desiredState;
        List<Integer> buttonMasks;

        Machine(int n, int desiredState, List<Integer> buttonMasks) {
            this.n = n;
            this.desiredState = desiredState;
            this.buttonMasks = buttonMasks;
        }
    }
        
    //used google to find the logic to brute force it
    public static int breadthFirstSearch(int n, int desiredState, List<Integer> buttonMasks) {
        int start = 0;
        if (desiredState == start) return 0;

        int maxState = 1 << n;
        int[] dist = new int[maxState];
        Arrays.fill(dist, -1);

        Queue<Integer> q = new ArrayDeque<>();
        q.add(start);
        dist[start] = 0;

        while (!q.isEmpty()) {
            int s = q.poll();
            int d = dist[s];
            for (int m : buttonMasks) {
                int nextState = s ^ m;
                if (dist[nextState] == -1) {
                    int nd = d + 1;
                    dist[nextState] = nd;
                    if (nextState == desiredState) {
                        return nd;
                    }
                    q.add(nextState);
                }
            }
        }
        return -1; 
    }
    
    //------------------PART 2 -------------------------------------
    
    public static int getJoltageLevelCounters(String filePath) {
        int total = 0;
        
        try( BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String input = br.lines().reduce("", (a, b) -> a + "\n" + b).trim();
    
            List<Machine2> machines = new ArrayList<>();
            Pattern buttonPattern = Pattern.compile("\\(([^)]*)\\)");
            Pattern joltsPattern = Pattern.compile("\\{([^}]*)\\}");
    
            for (String line : input.split("\\R")) {
                Matcher bm = buttonPattern.matcher(line);
                List<List<Integer>> buttons = new ArrayList<>();
                while (bm.find()) {
                    String[] inds = bm.group(1).split(",");
                    List<Integer> button = new ArrayList<>();
                    for (String x : inds) {
                        button.add(Integer.parseInt(x.trim()));
                    }
                    buttons.add(button);
                }
                Matcher jm = joltsPattern.matcher(line);
                if (!jm.find()) continue;
                String[] joltsStr = jm.group(1).split(",");
                int[] jolts = Arrays.stream(joltsStr).mapToInt(Integer::parseInt).toArray();
    
                machines.add(new Machine2(buttons, jolts));
            }
    
            for (Machine2 machine : machines) {
                total += ilp(machine.buttons, machine.jolts);
            }
    
        } catch (IOException e) {
            System.out.println("File not found.");
        }       
        
        return total;
    }
    
    public static class Machine2 {
        List<List<Integer>> buttons;
        int[] jolts;
        Machine2(List<List<Integer>> buttons, int[] jolts) {
            this.buttons = buttons;
            this.jolts = jolts;
        }
    }
    
    static int ilp(List<List<Integer>> buttons, int[] jolts) {
        int n = jolts.length;
        int m = buttons.size();

        ExpressionsBasedModel model = new ExpressionsBasedModel();

        // Variables: x_j >= 0, integer
        Variable[] x = new Variable[m];
        for (int j = 0; j < m; j++) {
            x[j] = model.addVariable("x_" + j);
            x[j].lower(0);
            x[j].integer(true);
        }

        // Constraints: A * x = jolts
        for (int i = 0; i < n; i++) {
            Expression expr = model.addExpression("eq_" + i);
            for (int j = 0; j < m; j++) {
                if (buttons.get(j).contains(i)) {
                    expr.set(x[j], 1);
                }
            }
            expr.level(jolts[i]); // equality constraint
        }

        // Objective: minimize sum(x_j)
        Expression obj = model.addExpression("obj");
        for (int j = 0; j < m; j++) {
            obj.set(x[j], 1);
        }
        obj.weight(1);

        Optimisation.Result result = model.minimise();
        if (result.getState().isFeasible()) {
            return (int) Math.round(result.getValue());
        } else {
            throw new RuntimeException("No feasible solution found");
        }
    }
}
