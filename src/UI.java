import processing.core.PApplet;
import processing.core.PVector;

public class UI {
    public final int BOARD_WIDTH = 12;
    public final int BOARD_HEIGHT = 22;
    public PVector position;
    private PApplet parent;
    public Object[][] matrix = new Object[BOARD_HEIGHT][BOARD_WIDTH];

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
                if (matrix[i][j] == null)
                    continue;

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
                parent.rect(j + position.x, i + position.y, 1, 1);
            }
        }
    }

    public void merge(Tetromino piece) {
        for (int i = 0; i < piece.matrix.length; i++) {
            for (int j = 0; j < piece.matrix.length; j++) {
                if (piece.matrix[i][j] != null) {
                    matrix[i + (int)piece.position.y - 2][j + (int)piece.position.x - 1] = piece.matrix[i][j];
                }
            }
        }
    }
}
