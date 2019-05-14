import processing.core.PApplet;
import processing.core.PVector;

public abstract class Tetromino {
    Object[][] matrix;
    PVector position;
    PApplet parent;

    public Tetromino(PApplet parent, PVector position) {
        this.position = position;
        this.parent = parent;
    }

    public void rotate(Rotation direction) {
        switch (direction) {
            case RIGHT:
                matrix = rotateRight(matrix);
                break;
            case LEFT:
                matrix = rotateLeft(matrix);
                break;
            case FLIP:
                matrix = rotateRight(matrix);
                matrix = rotateRight(matrix);
                break;
        }
    }

    public void move(Action action, UI ui) {
        switch (action) {
            case RIGHT:
                position.x++;
                if (isColliding(ui.matrix))
                    position.x--;
                break;
            case LEFT:
                position.x--;
                if (isColliding(ui.matrix))
                    position.x++;
                break;
            case DOWN:
                position.y++;
                if (isColliding(ui.matrix))
                    position.y--;
                break;
        }
    }

    public abstract void render();

    private Object[][] rotateRight(Object[][] matrix) {

        int totalRowsOfRotatedMatrix = matrix[0].length;
        int totalColsOfRotatedMatrix = matrix.length;

        Object[][] rotatedMatrix = new Object[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                rotatedMatrix[j][ (totalColsOfRotatedMatrix-1)- i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }

    private Object[][] rotateLeft(Object[][] matrix) {

        int totalRowsOfRotatedMatrix = matrix[0].length;
        int totalColsOfRotatedMatrix = matrix.length;

        Object[][] rotatedMatrix = new Object[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                rotatedMatrix[(totalRowsOfRotatedMatrix-1)-j][i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }

    public boolean isColliding(Object[][] other) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != null && other[i + (int)position.y - 2][j + (int)position.x - 1] != null)
                    return true;
            }
        }
        return false;
    }
}

class LPiece extends Tetromino {
    public LPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {Object.LPIECE, null, null},
                        {Object.LPIECE, Object.LPIECE, Object.LPIECE},
                        {null, null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.LPIECE) {
                    parent.fill(0, 0, 255);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class IPiece extends Tetromino {
    public IPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {null, null, null, null},
                        {Object.IPIECE, Object.IPIECE, Object.IPIECE, Object.IPIECE},
                        {null, null, null, null},
                        {null, null, null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.IPIECE) {
                    parent.fill(0, 255, 255);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class OPiece extends Tetromino {
    public OPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {Object.OPIECE, Object.OPIECE},
                        {Object.OPIECE, Object.OPIECE}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.OPIECE) {
                    parent.fill(255, 255, 0);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class TPiece extends Tetromino {
    public TPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {null, Object.TPIECE, null},
                        {Object.TPIECE, Object.TPIECE, Object.TPIECE},
                        {null ,null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.TPIECE) {
                    parent.fill(255, 0, 255);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class JPiece extends Tetromino {
    public JPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {null, null, Object.JPIECE},
                        {Object.JPIECE, Object.JPIECE, Object.JPIECE},
                        {null ,null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.JPIECE) {
                    parent.fill(255, 165, 0);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class SPiece extends Tetromino {
    public SPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {null, Object.SPIECE, Object.SPIECE},
                        {Object.SPIECE, Object.SPIECE, null},
                        {null ,null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.SPIECE) {
                    parent.fill(0, 255, 0);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}

class ZPiece extends Tetromino {
    public ZPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {Object.ZPIECE, Object.ZPIECE, null},
                        {null, Object.ZPIECE, Object.ZPIECE},
                        {null ,null, null}
                };
    }

    public void render() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.ZPIECE) {
                    parent.fill(255, 0, 0);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}