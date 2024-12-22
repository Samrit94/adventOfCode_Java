package aoc2024;

import java.io.*;

public class Day17 {
    static int[] program = new int[20];
    static int programSize = 0;

    public static void main(String[] args) {
        String filename = "input.txt"; // default input file

        long a=0, b=0, c=0;
        try {
            String line ="";
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                if (line.contains("A")){
                    a = Long.parseLong(line.split(": ")[1].trim());
                }
                if (line.contains("B")){
                    b = Long.parseLong(line.split(": ")[1].trim());
                }
                if (line.contains("C")){
                    c = Long.parseLong(line.split(": ")[1].trim());
                }
                if(line.contains("Program")) {
                    String[] programValues = line.split(": ")[1].split(",");
                    for (String value : programValues) {
                        program[programSize++] = Integer.parseInt(value.trim());
                    }
                }
            }
            
            //Part 1
            String output = evaluate(a, b, c);
            System.out.println("Output: "+output);
            
            //Part 2
            a = findDuplicate(0, 1);
            System.out.println("Lowest value for Duplicat: " + a);
            
            
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    static String evaluate(long a, long b, long c) {
        String output = "";
        for (int ip = 0; ip < programSize; ip += 2) {
            assert ip >= 0;
            int opc = program[ip];
            int opr = program[ip + 1];
            long combr = opr;

            if (opr == 4) {
                combr = a;
            }
            if (opr == 5) {
                combr = b;
            }
            if (opr == 6) {
                combr = c;
            }
            if (opc == 0) {
                a /= 1 << combr;
            }
            if (opc == 1) {
                b ^= opr;
            }
            if (opc == 2) {
                b = combr % 8;
            }
            if (opc == 3) {
                if (a != 0) {
                    ip = opr - 2;
                }
            }
            if (opc == 4) {
                b ^= c;
            }
            if (opc == 5) {
                output = output+ "," +combr % 8 ;
            }
            if (opc == 6) {
                b = a / (1 << combr);
            }
            if (opc == 7) {
                c = a / (1 << combr);
            }
        }
        return output.replaceFirst(",", "");
    }
    
    // Finds a value for A that is a copy, returns A if found, -1 otherwise
    static long findDuplicate(long a, int n) {
        if (n > programSize) {
            return a;
        }
        for (int i = 0; i < 8; i++) {
            long result;
            if (isCopy(a * 8 + i, n)) {
                result = findDuplicate(a * 8 + i, n + 1);
                if (result != -1) {
                    return result;
                }
            }
        }
        return -1;
    }

    static boolean isCopy(long a, int numToCheck) {
        long b = 0;
        long c = 0;
        int numOutput = 0;

        for (int ip = 0; ip < programSize; ip += 2) {
            assert ip >= 0;
            int opc = program[ip];
            int opr = program[ip + 1];
            long combr = opr;

            if (opr == 4) {
                combr = a;
            } else if (opr == 5) {
                combr = b;
            } else if (opr == 6) {
                combr = c;
            }
            
            if (opc == 0) {
                a /= 1 << combr;
            }
            if (opc == 1) {
                b ^= opr;
            }
            if (opc == 2) {
                b = (combr % 8);
            }
            if (opc == 3) {
                if (a != 0) {
                    ip = opr - 2;
                }
            }
            if (opc == 4) {
                b ^= c;
            }
            if (opc == 5) {
                int out = (int) (combr % 8);
                if (program[programSize - numToCheck + numOutput] != out) {
                    return false;
                }
                numOutput++;
                if (numOutput == numToCheck) {
                    return true;
                }
            }
            if (opc == 6) {
                b = a / (1 << combr);
            }
            if (opc == 7) {
                c = a / (1 << combr);
            }
        }
        return false;
    }
}
