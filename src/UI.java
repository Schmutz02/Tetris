import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;

public class UI {
    public final int BOARD_WIDTH = 12;
    public final int BOARD_HEIGHT = 22;
    public PVector position;
    private PApplet parent;
    public Object[][] matrix = new Object[BOARD_HEIGHT][BOARD_WIDTH];
    private int score = 0;
    private int level = 1;

    public UI(PApplet parent, PVector position) {
        this.parent = parent;
        this.position = position;
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 12; j++) {
                boolean y = i == 0 || i == 21;
                boolean x = j == 0 || j == 11;
                if (y || x)
                    matrix[i][j] = Object.WALL;
            }
        }
    }

    public void draw() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (matrix[i][j] == null) {
                    parent.strokeWeight(.05f);
                    parent.noFill();
                } else {
                    parent.strokeWeight(.2f);
                    switch (matrix[i][j]) {
                        case WALL:
                            parent.fill(255);
                            break;
                        case LPIECE:
                            parent.fill(0, 0, 255);
                            break;
                        case IPIECE:
                            parent.fill(0, 255, 255);
                            break;
                        case OPIECE:
                            parent.fill(255, 255, 0);
                            break;
                        case TPIECE:
                            parent.fill(255, 0, 255);
                            break;
                        case JPIECE:
                            parent.fill(255, 165, 0);
                            break;
                        case SPIECE:
                            parent.fill(0, 255, 0);
                            break;
                        case ZPIECE:
                            parent.fill(255, 0, 0);
                            break;
                    }
                }
                parent.rect(j + position.x, i + position.y, 1, 1);
            }
        }

        parent.textSize(3);
        parent.text(score, position.x + BOARD_WIDTH + 2, position.y + 2);
    }

    public void merge(Tetromino piece) {
        int clearedRows = -2;
        for (int i = 0; i < piece.matrix.length; i++) {
            for (int j = 0; j < piece.matrix.length; j++) {
                if (piece.matrix[i][j] != null) {
                    matrix[i + (int)piece.position.y - 2][j + (int)piece.position.x - 1] = piece.matrix[i][j];
                }
            }
        }

        for (Object[] arr : matrix) {
            int colsWithStuff = 0;
            for (Object o : arr) {
                if (o != null)
                    colsWithStuff++;
            }

            if (colsWithStuff == BOARD_WIDTH) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    if (arr[j] != Object.WALL)
                        arr[j] = null;
                }
                clearedRows++;
            }
        }

        if (doesNotContainPieces()) {
            clearedRows++;
        }

        updateScore(clearedRows);
    }

    private void updateScore(int rowsCleared) {
        switch (rowsCleared) {
            case 1:
                score += 40 * (level + 1);
                break;
            case 2:
                score += 100 * (level + 1);
                break;
            case 3:
                score += 300 * (level + 1);
                break;
            case 4:
                score += 1200 * (level + 1);
                break;
            case 5:
                score += 1200 * (level + 1) * 10;
                break;
        }
    }

    private boolean doesNotContainPieces() {
        for (Object[] arr : matrix) {
            for (Object o : arr) {
                if (o != Object.WALL || o != null)
                    return false;
            }
        }
        return true;
    }
}
