import processing.core.PApplet;
import processing.core.PVector;

import java.lang.reflect.InvocationTargetException;

public class UI {
    public final int BOARD_WIDTH = 12;
    public final int BOARD_HEIGHT = 22;
    public PVector position;
    private PApplet parent;
    public Object[][] matrix = new Object[BOARD_HEIGHT][BOARD_WIDTH];
    private int score = 0;
    private int level = 1;
    private int lines = 0;

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
                    parent.strokeWeight(.1f);
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

        parent.textSize(2);
        parent.text("Score: " + score, position.x + BOARD_WIDTH + 1, position.y + 2);

        parent.text("Lines: " + lines, position.x + BOARD_WIDTH + 1, position.y + 6);

        parent.text("Level: " + level, position.x + BOARD_WIDTH + 1, position.y + 10);
    }

    public void merge(Tetromino piece) {
        for (int i = 0; i < piece.matrix.length; i++) {
            for (int j = 0; j < piece.matrix.length; j++) {
                if (piece.matrix[i][j] != null) {
                    matrix[i + (int)piece.position.y - 2][j + (int)piece.position.x - 1] = piece.matrix[i][j];
                }
            }
        }
        checkToClearLines();
    }

    public void computeGhost(Tetromino piece) {
        Tetromino ghost = null;
        try {
            ghost = piece.getClass().getConstructor(PApplet.class, PVector.class).newInstance(parent, new PVector(piece.position.x, piece.position.y));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        int r = piece.rotation % 4;
        r = r < 0 ? r+4 : r;

        switch (r) {
            case 1:
                ghost.rotate(Rotation.RIGHT, this);
                break;
            case 2:
                ghost.rotate(Rotation.FLIP, this);
                break;
            case 3:
                ghost.rotate(Rotation.LEFT, this);
                break;
        }

        while (!ghost.isColliding(matrix)) {
            ghost.move(Action.DOWN, this);
        }
        ghost.position.y--;

        ghost.render(100);
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

    private void checkToClearLines() {
        int clearedRows = 0;

        for (int i = 1; i < matrix.length - 1; i++) {
            int colsWithStuff = 0;
            for (Object o : matrix[i]) {
                if (o != null)
                    colsWithStuff++;
            }

            if (colsWithStuff == BOARD_WIDTH) {
                clearLine(i);
                clearedRows++;
                lines++;
                if (lines % 10 == 0) {
                    level++;
                    Main.unitsPerSecond += 0.5;
                }
            }
        }

        if (doesNotContainPieces()) {
            clearedRows++;
        }

        updateScore(clearedRows);
    }

    private void clearLine(int line) {
        for (int j = 0; j < BOARD_WIDTH; j++) {
            if (matrix[line][j] != Object.WALL)
                matrix[line][j] = null;
        }

        for (int y = line; y >= 2; y--) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                matrix[y][x] = matrix[y - 1][x];
            }
        }
    }
}
