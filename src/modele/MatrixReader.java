package modele;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixReader {

    public static void main(String[] args) {
        int[][] matrix = readMatrixFromFile("res/levels/2.txt");
        printMatrix(matrix);
        int[] positionAndValue = findPositionAndValue(matrix, 5); // Find position of value 5
        if (positionAndValue != null) {
            updateMatrix(matrix, positionAndValue[0], positionAndValue[1], 6); // Update it to 6
            System.out.println("Matrix after update:");
            printMatrix(matrix);
        } else {
            System.out.println("Value not found in the matrix.");
        }
    }

    public static int[][] readMatrixFromFile(String filename) {
        int rows = 0;
        int cols = 0;
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] values = line.trim().split("\\s+");
                cols = Math.max(cols, values.length);
                rows++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int[][] matrix = new int[rows][cols];
        try {
            Scanner scanner = new Scanner(new File(filename));
            for (int i = 0; i < rows; i++) {
                String line = scanner.nextLine();
                String[] values = line.trim().split("\\s+");
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = Integer.parseInt(values[j]);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    public static void updateMatrix(int[][] matrix, int row, int col, int newValue) {
        matrix[row][col] = newValue;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public static int[] findPositionAndValue(int[][] matrix, int targetValue) {
        int[] result = new int[2];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == targetValue) {
                    result[0] = i; // Row index
                    result[1] = j; // Column index
                    return result;
                }
            }
        }
        return null; // Value not found
    }
}
