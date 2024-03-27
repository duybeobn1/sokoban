package modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Level {

    public static int[][] loadLevel(String filename) {
        try {
            // Read map dimensions
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            int cols = sc.nextInt();
            int rows = sc.nextInt();

            int targetRow = sc.nextInt() ;
            int targetCol = sc.nextInt() ;

            int [][] matrix = new int[rows][cols];
            for(int i=0; i<rows; i++) {
                for(int j=0; j<cols; j++) {
                    matrix[i][j] = sc.nextInt();
                }
            }

            matrix[targetRow][targetCol] = 5;
    
            return matrix;
    
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printLevel(int[][] levelMap) {
        for (int[] row : levelMap) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

}