package aoc2024;
import java.io.*;
import java.util.*;

public class Day24 {

    private static Map<String, Integer> wires = new HashMap<>();
    private static Map<String, String[]> gates1 = new HashMap<>();
    private static Map<String, String> gates2 = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        //read input from the file
        String filename = "input.txt";
        String fileLine = "";
        List<String> tempWires = new ArrayList<String>();
        List<String> tempConnections = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            boolean breakLine = false;
            while ((fileLine = br.readLine()) != null ) {
                if(fileLine.isEmpty()) {
                    breakLine = true;
                    continue;
                }
                if (breakLine) {
                    tempConnections.add(fileLine);
                }
                else {
                    tempWires.add(fileLine);
                }
            }
            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] inputWires = tempWires.toArray(new String[0]);
        String[] connection = tempConnections.toArray(new String[0]);
        
        //initializing wires
        for (String line : inputWires) {
            String wire = line.substring(0, 3);
            int value = Integer.parseInt(line.substring(line.length() - 1));
            wires.put(wire, value);
        }

        //parsing gates and connections
        for (String line : connection) {
            String[] tokens = line.split(" ");
            String wire1 = tokens[0];
            String operator = tokens[1];
            String wire2 = tokens[2];
            String operatorWire = tokens[4];

            wires.putIfAbsent(wire1, null);
            wires.putIfAbsent(wire2, null);
            wires.putIfAbsent(operatorWire, null);
            
            gates1.put(operatorWire, new String[]{wire1, operator, wire2});
            gates2.put(wire1 + "," + operator + "," + wire2, operatorWire);
        }

        
        //Part 1
        StringBuilder bits = new StringBuilder();
        for (int i = 45; i >= 0; i--) {
            bits.append(evaluate("z" + String.format("%02d", i)));
        }
        long decimal = Long.parseLong(bits.toString(), 2);
        System.out.println("Binary Number to Decimal: " + decimal);
        

        //Part 2
        StringBuilder wireList = new StringBuilder();
        List<String> wires2 = new ArrayList<>();
        String resultString = null;

        for (int i = 0; i < 45; i++) {
            String key = String.format("%02d", i);

            if (i == 0) {
                resultString = buildString("x00", "AND", "y00");
                if (resultString == null) throw new AssertionError();
                continue;
            }

            //AND
            String value1 = buildString("x" + key, "AND", "y" + key);
            String value2 = buildString("x" + key, "XOR", "y" + key);
            String resultAND = buildString(resultString, "AND", value2);

            if (resultAND == null) {
                String temp = value1;
                value1 = value2;
                value2 = temp;
                wires2.add(value2);
                wires2.add(value1);
                resultAND = buildString(resultString, "AND", value2);
            }

            //XOR
            String resultXOR = buildString(resultString, "XOR", value2);

            if (value2 != null && value2.startsWith("z")) {
                String temp = value2;
                value2 = resultXOR;
                resultXOR = temp;
                wires2.add(value2);
                wires2.add(resultXOR);
            }

            if (value1 != null && value1.startsWith("z")) {
                String temp = value1;
                value1 = resultXOR;
                resultXOR = temp;
                wires2.add(value1);
                wires2.add(resultXOR);
            }

            if (resultAND != null && resultAND.startsWith("z")) {
                String temp = resultAND;
                resultAND = resultXOR;
                resultXOR = temp;
                wires2.add(resultAND);
                wires2.add(resultXOR);
            }

            //final OR operation
            String resultOR = buildString(resultAND, "OR", value1);

            if (resultOR != null && resultXOR != null && resultOR.startsWith("z") && !resultOR.equals("z45")) {
                String temp = resultOR;
                resultOR = resultXOR;
                resultXOR = temp;
                wires2.add(resultOR);
                wires2.add(resultXOR);
            }

            resultString = resultOR;
        }

        //sorting wires
        Collections.sort(wires2);
        for (String wire : wires2) {
            wireList.append(wire).append(",");
        }
        if (wireList.length() > 0) wireList.deleteCharAt(wireList.length() - 1);

        System.out.println("Wires involved: " + wireList);
        
    }

    private static int evaluate(String wire) {
        if (wires.get(wire) == null) {
            String[] gate = gates1.get(wire);
            int w1 = evaluate(gate[0]);
            int w2 = evaluate(gate[2]);
            String op = gate[1];
            int result = 0;
            if ("AND".equals(op)) result = w1 & w2;
            else if ("OR".equals(op)) result = w1 | w2;
            else if ("XOR".equals(op)) result = w1 ^ w2;
            assert result == 0 || result == 1;
            wires.put(wire, result);
        }
        return wires.get(wire);
    }

    private static String buildString(String a, String operator, String b) {
        String key = a + "," + operator + "," + b;
        if (gates2.containsKey(key)) return gates2.get(key);
        key = b + "," + operator + "," + a;
        if (gates2.containsKey(key)) return gates2.get(key);
        return null;
    }
}
