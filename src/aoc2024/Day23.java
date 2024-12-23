package aoc2024;

import java.io.*;
import java.util.*;

public class Day23 {
    
    public static void main(String[] args) throws IOException {
        String filePath = "input.txt";
        Map<String, Set<String>> cumputers = readFile(filePath);
        
        //Part 1
        int numberOfT = findConnectedComputers(cumputers);
        System.out.println("Number of connected with T: " + numberOfT);

        //Part 2
        String password = findPasswordForLargestParty(cumputers);
        System.out.println("Password for the LAN Party: " + password);
    }
    
    public static Map<String, Set<String>> readFile(String filename) {
        Map<String, Set<String>> computers = new HashMap<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null ) {
                String[] parts = line.strip().split("-");
                String u = parts[0];
                String v = parts[1];
              
                computers.putIfAbsent(u, new HashSet<>());
                computers.putIfAbsent(v, new HashSet<>());
                computers.get(u).add(v);
                computers.get(v).add(u);
            }
            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return computers;
        
    }
    
    //find the connected computers
    public static int findConnectedComputers(Map<String, Set<String>> cumputers){        
        Set<String> result = new HashSet<>();
        for (String one : cumputers.keySet()) {
            for (String two : cumputers.get(one)) {
                for (String three : cumputers.get(two)) {
                    if (cumputers.get(three).contains(one)) {
                        //if any of the three starts with 't'
                        if (one.startsWith("t") || two.startsWith("t") || three.startsWith("t")) {
                            List<String> sorted = new ArrayList<>(Arrays.asList(one, two, three));
                            Collections.sort(sorted);
                            result.add(String.join(",", sorted));
                        }
                    }
                }
            }
        }
        return result.size();
    }

    //find the password for largest party using Bron-Kerbosch algorithm 
    public static String findPasswordForLargestParty(Map<String, Set<String>> computers) {
        Set<String> maxComponent = new HashSet<>();
        
        //recursive algorithm
        for (Set<String> component : bronKerbosch(new HashSet<>(), new HashSet<>(computers.keySet()), new HashSet<>(), computers)) {
            if (component.size() > maxComponent.size()) {
                maxComponent = component;
            }
        }
        List<String> sorted = new ArrayList<>(maxComponent);
        Collections.sort(sorted);
        return String.join(",", sorted);
    }
    
    //Bron-Kerbosch algorithm (googled best algorithm for this)
    public static Set<Set<String>> bronKerbosch(Set<String> r, Set<String> p, Set<String> x, Map<String, Set<String>> graph) {
        Set<Set<String>> cliques = new HashSet<>();
        
        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(new HashSet<>(r));
        } else {
            Set<String> pCopy = new HashSet<>(p);
            for (String v : pCopy) {
                Set<String> newR = new HashSet<>(r);
                newR.add(v);
                Set<String> newP = new HashSet<>(p);
                newP.retainAll(graph.get(v));
                Set<String> newX = new HashSet<>(x);
                newX.retainAll(graph.get(v));

                cliques.addAll(bronKerbosch(newR, newP, newX, graph));

                p.remove(v);
                x.add(v);
            }
        }
        return cliques;
    }
}
