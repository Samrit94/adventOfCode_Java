package aoc2024;

import java.io.*;

public class Day15 {
    static final int BUF_SIZE = 200;
    static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    static final int BOX = 0, WALL = 1, EMPTY = 2;
    
    public static void main(String[] args) throws IOException {
        String filePath = "input.txt";
        
        //Part 1
        int total = sumOfAllBoxes(filePath); 
        System.out.println("Sum of all Boxes GPS Part 1: "+total);
        
        //Part 2
        int total2 = sumOfAllBoxesPart2(filePath);
        System.out.println("Sum of all Boxes GPS Part 2: "+total2);    
        
    }
 
//---------------------------- Part 1 ----------------------------
    public static int sumOfAllBoxes(String filePath) {
        int total = 0;          
        final int map = 0, movements = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            char[][] warehouse = new char[BUF_SIZE][BUF_SIZE];
            int[] moveArr = new int[70000];
            int moveArrLen = 0;
            int parsePart = map;
            int warehouseSize = 0;
            Cord robotPos = null;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    parsePart = movements;
                    continue;
                }

                if (parsePart == map) {
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '@') {
                            robotPos = new Cord(warehouseSize, i); 
                        }
                        warehouse[warehouseSize][i] = line.charAt(i); 
                    }
                    warehouseSize++;
                } else {
                    for (int i = 0; i < line.length(); ++i) {
                        switch (line.charAt(i)) {
                            case '^':
                                moveArr[moveArrLen] = UP;
                                moveArrLen++;
                                break;
                            case 'v':
                                moveArr[moveArrLen] = DOWN;
                                moveArrLen++;
                                break;
                            case '<':
                                moveArr[moveArrLen] = LEFT;
                                moveArrLen++;
                                break;
                            case '>':
                                moveArr[moveArrLen] = RIGHT;
                                moveArrLen++;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            //check move
            for (int i = 0; i < moveArrLen; ++i) {
                checkMove(warehouse, robotPos, moveArr[i]);
            }

            //calculate total
            for (int i = 0; i < warehouseSize; ++i) {
                for (int j = 0; j < warehouseSize; ++j) {
                    if (warehouse[i][j] == 'O') {
                        total += (100 * i) + j;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return total;
    }
    
    static void checkMove(char[][] warehouse, Cord robotPos, int dir) {
        int rowDif = 0;
        int colDif = 0;

        switch (dir) {
            case UP: 
                rowDif = -1; 
                break;
            case DOWN: 
                rowDif = 1; 
                break;
            case LEFT: 
                colDif = -1; 
                break;
            case RIGHT: 
                colDif = 1; 
                break;
        }


        Cord curPos = new Cord(robotPos.row, robotPos.col);
        while (warehouse[curPos.row][curPos.col] != '.') {
            if (warehouse[curPos.row][curPos.col] == '#') {
                return; // Hit a wall, stop
            }
            curPos.row += rowDif;
            curPos.col += colDif;
        }

        warehouse[robotPos.row][robotPos.col] = '.';
        warehouse[curPos.row][curPos.col] = 'O';
        robotPos.row += rowDif;
        robotPos.col += colDif;
        warehouse[robotPos.row][robotPos.col] = '@';
    }

//---------------------------- Part 2 ----------------------------
    public static int sumOfAllBoxesPart2(String filePath) {
        
        int cordSum = 0;
        final int map = 0, movements = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            char[][] warehouse = new char[BUF_SIZE][BUF_SIZE];
            int[] moveArr = new int[70000];
            int moveArrLen = 0;
            int parsePart = map;
            int warehouseRowCount = 0;
            int warehouseColCount = 0;
            Cord robotPos = null;
    
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    parsePart = movements;
                    continue;
                }
    
                if (parsePart == map) {
                    warehouseColCount = 0;
    
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '@') {
                            robotPos = new Cord(warehouseRowCount, warehouseColCount);
                        }
    
                        switch (line.charAt(i)) {
                            case '.':
                                warehouse[warehouseRowCount][warehouseColCount] = '.';
                                warehouse[warehouseRowCount][warehouseColCount + 1] = '.';
                                warehouseColCount += 2;
                                break;
                            case '#':
                                warehouse[warehouseRowCount][warehouseColCount] = '#';
                                warehouse[warehouseRowCount][warehouseColCount + 1] = '#';
                                warehouseColCount += 2;
                                break;
                            case '@':
                                warehouse[warehouseRowCount][warehouseColCount] = '@';
                                warehouse[warehouseRowCount][warehouseColCount + 1] = '.';
                                warehouseColCount += 2;
                                break;
                            case 'O':
                                warehouse[warehouseRowCount][warehouseColCount] = '[';
                                warehouse[warehouseRowCount][warehouseColCount + 1] = ']';
                                warehouseColCount += 2;
                                break;
                        }
                    }
    
                    ++warehouseRowCount;
                } else {
                    for (int i = 0; i < line.length(); ++i) {
                        switch (line.charAt(i)) {
                            case '^':
                                moveArr[moveArrLen] = UP;
                                ++moveArrLen;
                                break;
                            case 'v':
                                moveArr[moveArrLen] = DOWN;
                                ++moveArrLen;
                                break;
                            case '<':
                                moveArr[moveArrLen] = LEFT;
                                ++moveArrLen;
                                break;
                            case '>':
                                moveArr[moveArrLen] = RIGHT;
                                ++moveArrLen;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
    
            for (int i = 0; i < moveArrLen; ++i) {
                checkMove2(warehouse, robotPos, moveArr[i]);
            }
    
            for (int i = 0; i < warehouseRowCount; ++i) {
                for (int j = 0; j < warehouseColCount; ++j) {
                    if (warehouse[i][j] == '[') {
                        cordSum += (100 * i) + j;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return cordSum;
    }

    static void moveBox(char[][] warehouse, Cord boxPos, int dir) {
        if (warehouse[boxPos.row][boxPos.col] != '[' && warehouse[boxPos.row][boxPos.col] != ']') {
            return;
        }

        switch (dir) {
            case UP:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col + 1] = '.';
                    warehouse[boxPos.row - 1][boxPos.col] = '[';
                    warehouse[boxPos.row - 1][boxPos.col + 1] = ']';
                } else {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col - 1] = '.';
                    warehouse[boxPos.row - 1][boxPos.col - 1] = '[';
                    warehouse[boxPos.row - 1][boxPos.col] = ']';
                }
                break;
            case DOWN:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col + 1] = '.';
                    warehouse[boxPos.row + 1][boxPos.col] = '[';
                    warehouse[boxPos.row + 1][boxPos.col + 1] = ']';
                } else {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col - 1] = '.';
                    warehouse[boxPos.row + 1][boxPos.col - 1] = '[';
                    warehouse[boxPos.row + 1][boxPos.col] = ']';
                }
                break;
            case LEFT:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    warehouse[boxPos.row][boxPos.col] = ']';
                    warehouse[boxPos.row][boxPos.col + 1] = '.';
                    warehouse[boxPos.row][boxPos.col - 1] = '[';
                } else {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col - 1] = ']';
                    warehouse[boxPos.row][boxPos.col - 2] = '[';
                }
                break;
            case RIGHT:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    warehouse[boxPos.row][boxPos.col] = '.';
                    warehouse[boxPos.row][boxPos.col + 1] = '[';
                    warehouse[boxPos.row][boxPos.col + 2] = ']';
                } else {
                    warehouse[boxPos.row][boxPos.col] = '[';
                    warehouse[boxPos.row][boxPos.col - 1] = '.';
                    warehouse[boxPos.row][boxPos.col + 1] = ']';
                }
                break;
        }
    }

    static int canMove(char[][] warehouse, Cord boxPos, int dir, Cord[] childBoxPos) {
        Cord[] checkCords = new Cord[2];

        switch (dir) {
            case UP:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    checkCords[0] = new Cord(boxPos.row - 1, boxPos.col);
                    checkCords[1] = new Cord(boxPos.row - 1, boxPos.col + 1);
                } else {
                    checkCords[0] = new Cord(boxPos.row - 1, boxPos.col);
                    checkCords[1] = new Cord(boxPos.row - 1, boxPos.col - 1);
                }
                break;
            case DOWN:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    checkCords[0] = new Cord(boxPos.row + 1, boxPos.col);
                    checkCords[1] = new Cord(boxPos.row + 1, boxPos.col + 1);
                } else {
                    checkCords[0] = new Cord(boxPos.row + 1, boxPos.col);
                    checkCords[1] = new Cord(boxPos.row + 1, boxPos.col - 1);
                }
                break;
            case LEFT:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    checkCords[0] = new Cord(boxPos.row, boxPos.col - 1);
                    checkCords[1] = new Cord(-1, -1);
                } else {
                    checkCords[0] = new Cord(boxPos.row, boxPos.col - 2);
                    checkCords[1] = new Cord(-1, -1);
                }
                break;
            case RIGHT:
                if (warehouse[boxPos.row][boxPos.col] == '[') {
                    checkCords[0] = new Cord(boxPos.row, boxPos.col + 2);
                    checkCords[1] = new Cord(-1, -1);
                } else {
                    checkCords[0] = new Cord(boxPos.row, boxPos.col + 1);
                    checkCords[1] = new Cord(-1, -1);
                }
                break;
        }

        int childBoxCount = 0;
        int retVal = EMPTY;

        for (int i = 0; i < 2 && checkCords[i].row != -1; ++i) {
            switch (warehouse[checkCords[i].row][checkCords[i].col]) {
                case '#':
                    return WALL;
                case '[':
                    if (i != 0 && checkCords[0].col > checkCords[i].col) {
                        break;
                    }
                    childBoxPos[childBoxCount] = checkCords[i];
                    ++childBoxCount;
                    childBoxPos[childBoxCount] = new Cord(-1, -1); // Sentinel value
                    retVal = BOX;
                    break;
                case ']':
                    if (i != 0 && checkCords[0].col < checkCords[i].col) {
                        break;
                    }
                    childBoxPos[childBoxCount] = checkCords[i];
                    ++childBoxCount;
                    childBoxPos[childBoxCount] = new Cord(-1, -1); // Sentinel value
                    retVal = BOX;
                    break;
                case '.':
                default:
            }
        }

        return retVal;
    }

    
    static void checkMove2(char[][] warehouse, Cord robotPos, int dir) {
        int rowDif = 0;
        int colDif = 0;

        switch (dir) {
            case UP: 
                rowDif = -1;
                break;
            case DOWN:
                rowDif = 1;
                break;
            case LEFT:
                colDif = -1;
                break;
            case RIGHT:
                colDif = 1;
                break;
        }

        Cord[] boxQue = new Cord[1000];
        int getPos = 0;
        int putPos = 0;

        Cord curPos = new Cord(robotPos.row + rowDif, robotPos.col + colDif);

        if (warehouse[curPos.row][curPos.col] == '#') {
            return;
        }

        if (warehouse[curPos.row][curPos.col] == '[' || warehouse[curPos.row][curPos.col] == ']') {
            boxQue[putPos] = curPos;
            ++putPos;
        }

        while (putPos != getPos) {
            Cord[] childBoxPos = new Cord[3];

            switch (canMove(warehouse, boxQue[getPos], dir, childBoxPos)) {
                case WALL:
                    return;
                case EMPTY:
                    break;
                case BOX:
                    for (int i = 0; childBoxPos[i].row != -1; ++i) {
                        boxQue[putPos] = childBoxPos[i];
                        ++putPos;
                    }
                    break;
            }

            ++getPos;
        }

        for (int i = getPos - 1; i >= 0; --i) {
            moveBox(warehouse, boxQue[i], dir);
        }

        warehouse[robotPos.row][robotPos.col] = '.';

        robotPos.row += rowDif;
        robotPos.col += colDif;

        warehouse[robotPos.row][robotPos.col] = '@';
    } 
    
//---------------------------- helping Class ----------------------------
    static class Cord {
        int row;
        int col;

        Cord(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    
}
