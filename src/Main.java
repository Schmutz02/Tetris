import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;

public class Main extends PApplet {
    int unitsPerSecond = 1;
    UI ui = new UI(this, new PVector(1, 2));
    Tetromino activePiece;
    public static double lockTimer = 2;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        frameRate = 60;
        size(600, 600);
    }

    public void setup() {
        strokeWeight(.1f);
        activePiece = newPiece();
    }

    public void draw() {
        background(100);
        scale(20);

        ui.draw();
        activePiece.render();

        if (frameCount >= frameRate / unitsPerSecond) {
            frameCount = 0;
            pullPiece();
        }

        double now = System.nanoTime();
        double deltaTime = (double)(now - this.frameRateLastNanos) / 1.0E9D;

        if (activePiece.isColliding(ui.matrix))
            lockTimer -= deltaTime;

        if (lockTimer < 0) {
            ui.merge(activePiece);
            activePiece = newPiece();
            lockTimer = 2;
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKey()) {
            case 'q':
                activePiece.rotate(Rotation.LEFT);
                break;
            case 'e':
                activePiece.rotate(Rotation.RIGHT);
                break;
            case 'f':
                activePiece.rotate(Rotation.FLIP);
                break;
            case 'a':
                activePiece.move(Action.LEFT, ui);
                break;
            case 'd':
                activePiece.move(Action.RIGHT, ui);
                break;
            case 's':
                activePiece.move(Action.DOWN, ui);
                frameCount = 0;
                break;
        }
    }

    private void pullPiece() {
        activePiece.move(Action.DOWN, ui);
    }

    private Tetromino newPiece() {
        Tetromino piece = null;
        switch ((int)(Math.random() * 7)) {
            case 0:
                piece = new LPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
            case 1:
                piece = new IPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 2, ui.position.y));
                break;
            case 2:
                piece = new OPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
            case 3:
                piece = new TPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
            case 4:
                piece = new JPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
            case 5:
                piece = new SPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
            case 6:
                piece = new ZPiece(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
                break;
        }
        return piece;
    }
}