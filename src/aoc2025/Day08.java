package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day08 {

    public static void main(String[] args) {

        String filePath = "input.txt";
        ArrayList<JunctionBox> boxes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            long largestSize = 0;
            long result = 0;
            String lineIn;

            while ((lineIn = br.readLine()) != null) {
                String[] parts = lineIn.split(",");
                long x = Long.parseLong(parts[0].trim());
                long y = Long.parseLong(parts[1].trim());
                long z = Long.parseLong(parts[2].trim());

                JunctionBox newBox = new JunctionBox(x, y, z);
                boxes.add(newBox);
            }

            int connections = 0;
            boolean[] connected = new boolean[boxes.size()];

            ArrayList<Circuit> circuits = new ArrayList<>();
            JunctionBox box1 = null;
            JunctionBox box2 = null;

            int idBox1 = -1;
            int idBox2 = -1;

            double minDistance = 0;
            boolean allConnected = false;

            while (!allConnected) {
                double shortest = Double.MAX_VALUE;

                for (int i = 0; i < boxes.size() - 1; i++) {
                    for (int j = i + 1; j < boxes.size(); j++) {
                        double distance = boxes.get(i).getDistance(boxes.get(j));

                        if (distance < shortest && distance > minDistance) {
                            shortest = distance;
                            box1 = boxes.get(i);
                            box2 = boxes.get(j);
                            idBox1 = i;
                            idBox2 = j;
                        }
                    }
                }

                if (connected[idBox1] == false && connected[idBox2] == false) {                    
                    Circuit newCircuit = new Circuit();
                    newCircuit.addMember(box1);
                    newCircuit.addMember(box2);
                    
                    minDistance = shortest;
                    circuits.add(newCircuit);
                    connections++;
                    
                    connected[idBox1] = true;
                    connected[idBox2] = true;
                    
                } else if (connected[idBox1] == true && connected[idBox2] == false) {
                    int circuitIndex = 0;

                    for (int i = 0; i < circuits.size(); i++) {
                        if (circuits.get(i).hasMember(box1)) {
                            circuitIndex = i;
                        }
                    }

                    circuits.get(circuitIndex).addMember(box2);
                    connections++;
                    
                    connected[idBox2] = true;
                    minDistance = shortest;
                    
                } else if (connected[idBox1] == false && connected[idBox2] == true) {
                    int circuitIndex = 0;

                    for (int i = 0; i < circuits.size(); i++) {
                        if (circuits.get(i).hasMember(box2)) {
                            circuitIndex = i;
                        }
                    }

                    circuits.get(circuitIndex).addMember(box1);
                    connections++;
                    
                    connected[idBox1] = true;
                    minDistance = shortest;
                    
                } else if (connected[idBox1] == true && connected[idBox2] == true) {
                    int circuitIndex1 = 0;
                    int circuitIndex2 = 0;

                    for (int i = 0; i < circuits.size(); i++) {
                        if (circuits.get(i).hasMember(box1)) {
                            circuitIndex1 = i;
                        }
                        if (circuits.get(i).hasMember(box2)) {
                            circuitIndex2 = i;
                        }
                    }

                    if (circuitIndex1 != circuitIndex2) {
                        for (int i = 0; i < circuits.get(circuitIndex2).getMembers(); i++) {
                            circuits.get(circuitIndex1).addMember(circuits.get(circuitIndex2).members.get(i));
                        }

                        circuits.remove(circuitIndex2);
                        connections++;
                        minDistance = shortest;
                    } else if (circuitIndex1 == circuitIndex2) {
                        minDistance = shortest;
                        connections++;
                    }
                }

                if (circuits.size() == 1) {
                    allConnected = true;

                    for (int i = 0; i < connected.length; i++) {
                        if (!connected[i]) {
                            allConnected = false;
                            break;
                        }
                    }

                    if (allConnected) {
                        result = box1.getX() * box2.getX();
                        System.out.println("Mulitply X of last two boxes: " + result);
                        break;
                    }
                }

                box1 = null;
                box2 = null;

                if (connections == 1000) {
                    Circuit[] toSort = new Circuit[circuits.size()];

                    for (int i = 0; i < circuits.size(); i++) {
                        toSort[i] = circuits.get(i);
                    }

                    for (int i = 1; i < toSort.length; i++) {
                        Circuit temp = toSort[i];
                        int index = i;

                        while (index > 0 && temp.compareTo(toSort[index - 1]) > 0) {
                            toSort[index] = toSort[index - 1];
                            index--;
                        }

                        toSort[index] = temp;
                    }

                    largestSize = toSort[0].getMembers() * toSort[1].getMembers() * toSort[2].getMembers();
                    System.out.println("Size of the three largest circuits: " + largestSize);
                }
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public static class JunctionBox {
        private long x;
        private long y;
        private long z;

        public JunctionBox(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        public long getZ() {
            return z;
        }

        public double getDistance(JunctionBox other) {
            return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getY() - other.getY(), 2)
                    + Math.pow(this.getZ() - other.getZ(), 2));
        }
    }

    public static class Circuit {
        public ArrayList<JunctionBox> members;

        public Circuit() {
            this.members = new ArrayList<JunctionBox>();
        }

        public long getMembers() {
            return members.size();
        }

        public void addMember(JunctionBox other) {
            members.add(other);
        }

        public boolean hasMember(JunctionBox input) {
            if (members.contains(input))
                return true;
            return false;
        }

        public int compareTo(Circuit other) {
            if (this.getMembers() > other.getMembers()) {
                return 1;
            }
            if (this.getMembers() == other.getMembers()) {
                return 0;
            }
            return -1;
        }
    }
}
