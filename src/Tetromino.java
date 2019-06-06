import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class Tetromino {
    Object[][] matrix;
    PVector position;
    PApplet parent;
    int rotation = 0;

    public Tetromino(PApplet parent, PVector position) {
        this.position = position;
        this.parent = parent;
    }

    public void rotate(Rotation direction, UI ui) {
        switch (direction) {
            case RIGHT:
                matrix = rotateRight(matrix);
                if (isColliding(ui.matrix))
                    matrix = rotateLeft(matrix);
                break;
            case LEFT:
                matrix = rotateLeft(matrix);
                if (isColliding(ui.matrix))
                    matrix = rotateRight(matrix);
                break;
            case FLIP:
                matrix = rotateRight(matrix);
                matrix = rotateRight(matrix);
                if (isColliding(ui.matrix)) {
                    matrix = rotateRight(matrix);
                    matrix = rotateRight(matrix);
                }
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
                break;
        }
    }

    public abstract void render(int alpha);

    private Object[][] rotateRight(Object[][] matrix) {

        int totalRowsOfRotatedMatrix = matrix[0].length;
        int totalColsOfRotatedMatrix = matrix.length;

        Object[][] rotatedMatrix = new Object[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                rotatedMatrix[j][ (totalColsOfRotatedMatrix-1)- i] = matrix[i][j];
            }
        }
        rotation++;
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
        rotation--;
        return rotatedMatrix;
    }

    public boolean isColliding(Object[][] other) {
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix.length; j++) {
//                if (matrix[i][j] != null && other[i + (int)position.y - 2][j + (int)position.x - 1] != null)
//                    return true;
//            }
//        }
//        return false;

        ArrayList<PVector> points = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i + 1 >= matrix.length && matrix[i][j] != null) {
                    points.add(new PVector(j, i + 1));
                    continue;
                }
                if (i + 1 < matrix.length && matrix[i][j] != null && matrix[i + 1][j] == null) {
                    points.add(new PVector(j, i + 1));
                }
            }
        }

        for (PVector point : points) {
            if (other[(int)point.y + (int)position.y - 3][(int)point.x + (int)position.x - 1] != null)
                return true;
        }
        return false;
    }
}

class LPiece extends Tetromino {
    public LPiece(PApplet parent, PVector position) {
        super(parent, position);
        this.matrix = new Object[][]
                {
                        {null, null, Object.LPIECE},
                        {Object.LPIECE, Object.LPIECE, Object.LPIECE},
                        {null, null, null}
                };
    }

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.LPIECE) {
                    parent.fill(0, 0, 255, alpha);
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

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.IPIECE) {
                    parent.fill(0, 255, 255, alpha);
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

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.OPIECE) {
                    parent.fill(255, 255, 0, alpha);
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

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.TPIECE) {
                    parent.fill(255, 0, 255, alpha);
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
                        {Object.JPIECE, null, null},
                        {Object.JPIECE, Object.JPIECE, Object.JPIECE},
                        {null ,null, null}
                };
    }

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.JPIECE) {
                    parent.fill(255, 165, 0, alpha);
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

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.SPIECE) {
                    parent.fill(0, 255, 0, alpha);
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

    public void render(int alpha) {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == Object.ZPIECE) {
                    parent.fill(255, 0, 0, alpha);
                    parent.rect(position.x + j, position.y + i, 1, 1);
                }
            }
        }
    }
}
