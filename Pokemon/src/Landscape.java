import java.io.*;
import java.util.*;

public class Landscape {
    private char[][] map;

    public Landscape(String filename) throws FileNotFoundException {
        loadMap(filename);
    }

    private void loadMap(String filename) throws FileNotFoundException {
        List<char[]> rows = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            rows.add(scanner.nextLine().toCharArray());
        }
        map = rows.toArray(new char[rows.size()][]);
        scanner.close();
    }

    public int getRows() {
        return map.length;
    }

    public int getCols() {
        return map[0].length;
    }

    public char getTile(int row, int col) {
        return map[row][col];
    }

    public void setTile(int row, int col, char tile) {
        map[row][col] = tile;
    }

    public void displayMap() {
        for (char[] row : map) {
            for (char tile : row) {
                System.out.print(tile);
            }
            System.out.println();
        }
    }
}
