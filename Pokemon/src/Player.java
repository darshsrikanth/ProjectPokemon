public class Player {
    private int row;
    private int col;
    private String direction;

    public Player(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
        this.direction = "down";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getDirection() {
        return direction;
    }

    public void move(char direction, Landscape landscape) {
        this.direction = switch (direction) {
            case 'W' -> "up";
            case 'S' -> "down";
            case 'A' -> "left";
            case 'D' -> "right";
            default -> this.direction;
        };

        int newRow = row;
        int newCol = col;

        switch (direction) {
            case 'W' -> newRow--;
            case 'S' -> newRow++;
            case 'A' -> newCol--;
            case 'D' -> newCol++;
        }

        if (newRow >= 0 && newRow < landscape.getRows() && newCol >= 0 && newCol < landscape.getCols() &&
            landscape.getTile(newRow, newCol) != '#') {
            row = newRow;
            col = newCol;
        }
    }

    public void setPosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }
}
