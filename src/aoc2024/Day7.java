package aoc2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Day7 {
    public static void main(String[] args) {
        // Path to input file
        String filePath = "input.txt";
        List<String> equations = new ArrayList<String>();        
        equations = readFile(filePath);
        DecimalFormat df = new DecimalFormat("####.##");
        
        //Part1
        double part1Result = readEquations(equations);
        
        String formattedNumber = df.format(part1Result);
        System.out.println("Total for Part1: "+ formattedNumber);
        
        //Part2        
        double part2Result = readEquationsPart2(equations);
        
        String formattedNumber2 = df.format(part2Result);
        System.out.println("Total for Part2: "+ formattedNumber2);
    }
    
    
    public static List<String> readFile(String filename) {
        List<String> output = new ArrayList<String>();
        try
        {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;
                //Read from file
                while ((line = reader.readLine()) != null)
                {
                    output.add(line);
                    
                }
                reader.close();
                
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }        
        return output;
    }
    

  //----------------PART 1 -------------------
    public static double readEquations(List<String> equations) {
        double total = 0;
        
        for (int line = 0; line < equations.size(); line++) {
            String equation = equations.get(line);
            double lineResult = Double.parseDouble( equation.substring(0, equation.indexOf(":")));
            String numbers = equation.substring(equation.indexOf(":")+1).trim();
            
            numbers = numbers.replace(" ", "+");
            
            List<Integer> plusPositions = new ArrayList<>();
            for (int i = 0; i < numbers.length(); i++) {
                if (numbers.charAt(i) == '+') {
                    plusPositions.add(i);
                }
            }
            
            //generate all possibilites with *
            List<String> variants = new ArrayList<>();
            generateVariants(numbers, plusPositions, 0, variants);
            
            for (String variant : variants) {
                double calc = calculateLine(variant);
                if (calc == lineResult) {
                    total += lineResult;
                    break;
                }
            }         
            
        }
        return total;
    }
    
    private static void generateVariants(String expression, List<Integer> plusPositions, int index, List<String> variants) {
        if (index == plusPositions.size()) {
            variants.add(expression); // Add the current variant to the result list
            return;
        }        
        //replace the '+' at the current position with '*'
        char[] exprArray = expression.toCharArray();
        exprArray[plusPositions.get(index)] = '*';
        String newExpression = new String(exprArray);
        
        //recur to generate the next variants
        generateVariants(newExpression, plusPositions, index + 1, variants);

        //optionally, revert the change 
        exprArray[plusPositions.get(index)] = '+';
        newExpression = new String(exprArray);
        
        //recur again with the '+' replaced back
        generateVariants(newExpression, plusPositions, index + 1, variants);        
    }
        
    public static double calculateLine(String line) {
        //split the input based on numbers and operators (+, *)
        String[] tokens = line.split("(?<=\\d)(?=[+*])|(?<=[+*])(?=\\d)");

        //start with the first number
        double result = Double.parseDouble(tokens[0]);

        //evaluate from left to right
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double nextNumber = Double.parseDouble(tokens[i + 1]);

            //apply the operator
            if (operator.equals("+")) {
                result += nextNumber;
            } else if (operator.equals("*")) {
                result *= nextNumber;
            }
        }
        return result;
    }
    
//----------------PART 2 -------------------
    public static double readEquationsPart2(List<String> equations) {
        double total = 0;
        
        for (int line = 0; line < equations.size(); line++) {
            String equation = equations.get(line);
            double lineResult = Double.parseDouble( equation.substring(0, equation.indexOf(":")));
            String numbers = equation.substring(equation.indexOf(":")+1).trim();
            
            numbers = numbers.replace(" ", "+");
            
            List<Integer> plusPositions = new ArrayList<>();
            for (int i = 0; i < numbers.length(); i++) {
                if (numbers.charAt(i) == '+') {
                    plusPositions.add(i);
                }
            }
            
            //generate all possibilites 
            List<String> variants = new ArrayList<>();
            generateVariantsPart2(numbers, 0, variants);
            
            for (String variant : variants) {
                //System.out.println(variant);
                double calc = calculateLine2(variant);
                //System.out.println(calc);
                if (calc == lineResult) {
                    total += lineResult;
                    break;
                }
            }         
            
        }
        return total;
    }
    
    
    public static double calculateLine2(String line) {
        //split the input based on numbers and operators (+, *, -)
        String[] tokens = line.split("(?<=\\d)(?=[+*-])|(?<=[+*-])(?=\\d)");
        DecimalFormat df = new DecimalFormat("####.##");

        //start with the first number
        double result = Double.parseDouble(tokens[0]);

        //evaluate from left to right
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double nextNumber = Double.parseDouble(tokens[i + 1]);

            //apply the operator
            if (operator.equals("+")) {
                result += nextNumber;
            } else if (operator.equals("*")) {
                result *= nextNumber;
            } else if (operator.equals("-")) {
                String tempNumb = tokens[i+1];
                String formattedNumber2 = df.format(result);
                result = Double.parseDouble(formattedNumber2 + tempNumb);
            }
        }
        return result;
    }
    
    private static void generateVariantsPart2(String expression, int index, List<String> variations) {
        //Adjusted this method to run with 3 different kind of symbols
        //also using - in replacement of || for easier string handling
        
        int plusIndex = expression.indexOf('+', index);
        
        //base: if no more '+' is found, add the current expression to the variations list
        if (plusIndex == -1) {
            variations.add(expression);
            return;
        }
        
        //generate two new expressions: one with '*' and one with '-'
        String expressionWithStar = expression.substring(0, plusIndex) + '*' + expression.substring(plusIndex + 1);
        String expressionWithDash = expression.substring(0, plusIndex) + '-' + expression.substring(plusIndex + 1);
        String expressionWithPlus = expression.substring(0, plusIndex) + '+' + expression.substring(plusIndex + 1);

        //recursively call for both new expressions with modified operators
        generateVariantsPart2(expressionWithStar, plusIndex + 1, variations);
        generateVariantsPart2(expressionWithDash, plusIndex + 1, variations);
        generateVariantsPart2(expressionWithPlus, plusIndex + 1, variations);
    }
        
}