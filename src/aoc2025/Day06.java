package aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day06 {

    public static void main(String[] args) {
        String filePath = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            long grandTotal = 0;
            ArrayList<Long> row1 = new ArrayList<>();
            ArrayList<Long> row2 = new ArrayList<>();
            ArrayList<Long> row3 = new ArrayList<>();
            ArrayList<Long> row4 = new ArrayList<>();
            String[][] grid = new String[5][5000];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j] = " ";
                }
            }
            ArrayList<String> operator = new ArrayList<>();
            int row = 1;
            int gridRow = 0;
            String line;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    grid[gridRow][i] = line.substring(i, i + 1);
                }
                String currentNumber = "";
                for (int i = 0; i < line.length(); i++) {
                    if (line.substring(i, i + 1).equals(" ")) {
                        if (currentNumber.length() > 0) {
                            if (row == 1) {
                                row1.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 2) {
                                row2.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 3) {
                                row3.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 4) {
                                row4.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 5) {
                                operator.add(currentNumber);
                                currentNumber = "";
                            }
                        }
                    } else if (i == line.length() - 1) {
                        currentNumber += line.substring(i, i + 1);
                        if (currentNumber.length() > 0) {
                            if (row == 1) {
                                row1.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 2) {
                                row2.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 3) {
                                row3.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 4) {
                                row4.add(Long.parseLong(currentNumber));
                                currentNumber = "";
                            }
                            if (row == 5) {
                                operator.add(currentNumber);
                                currentNumber = "";
                            }
                        }
                    } else {
                        currentNumber += line.substring(i, i + 1);
                    }
                }
                row++;
                gridRow++;
            }
            for (int i = 0; i < row1.size(); i++) {
                if (operator.get(i).equals("*")) {
                    grandTotal += row1.get(i) * row2.get(i) * row3.get(i) * row4.get(i);
                } else {
                    grandTotal += row1.get(i) + row2.get(i) + row3.get(i) + row4.get(i);
                }
            }
            System.out.println("Grand Total:" + grandTotal);
            
            
            long grandTotalColum = 0;

            ArrayList<Long> rowTotal = new ArrayList<>();
            for (int i = grid[0].length - 1; i >= 0; i--) {
                String number = "";
                if (i < grid[0].length - 1 && !(grid[grid.length - 1][i + 1].equals(" "))) {
                    if (grid[grid.length - 1][i + 1].equals("+")) {
                        long rowSum = 0;
                        for (long val : rowTotal) rowSum += val;
                        grandTotalColum += rowSum;
                        rowTotal = new ArrayList<>();
                    } else {
                        long rowProduct = 1;
                        for (long val : rowTotal) rowProduct *= val;
                        grandTotalColum += rowProduct;
                        rowTotal = new ArrayList<>();
                    }
                } else {
                    for (int j = 0; j < grid.length - 1; j++) {
                        if (!grid[j][i].equals(" ")) {
                            number += grid[j][i];
                        }
                    }
                    if (!number.isEmpty()) {
                        rowTotal.add(Long.parseLong(number));
                    }
                }
            }

            if (grid[grid.length - 1][0].equals("+")) {
                long rowSum = 0;
                for (long val : rowTotal) rowSum += val;
                grandTotalColum += rowSum;
            } else {
                long rowProduct = 1;
                for (long val : rowTotal) rowProduct *= val;
                grandTotalColum += rowProduct;
            }
            System.out.println("Grand Total Column:"+grandTotalColum);

        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }
}
