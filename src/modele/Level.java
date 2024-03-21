package modele;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Level{

    public static int[][] loadLevel(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Read map dimensions
            String[] dimensions = br.readLine().split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            // Create level map
            int[][] levelMap = new int[rows][cols];

            // Read player position
            String[] playerPos = br.readLine().split(" ");
            int playerRow = Integer.parseInt(playerPos[0]);
            int playerCol = Integer.parseInt(playerPos[1]);

            // Read level map data
            for (int row = 0; row < rows; row++) {
                String[] tokens = br.readLine().split(" ");
                for (int col = 0; col < cols; col++) {
                    levelMap[row][col] = Integer.parseInt(tokens[col]);
                }
            }

            // Set player position
            levelMap[playerRow][playerCol] = 4;

            return levelMap;
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
