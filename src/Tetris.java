import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.*;

public class Tetris extends PApplet {
    public static double unitsPerSecond = 1;
    UI ui = new UI(this, new PVector(1, 2));
    Tetromino activePiece;
    double currentLockTimeMS = 1000;
    final double defaultLockTimeMS = 1000;

    public static void main(String[] args) {
        PApplet.main("Tetris");
    }

    public void settings() {
        frameRate = 60;
//        Image icon = new Image
//        frame.setIconImage();
        size(600, 600);
    }

    public void setup() {
        activePiece = newPiece();
        strokeWeight(.1f);
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

        if (currentLockTimeMS < 0) {
            ui.merge(activePiece);
            activePiece = newPiece();
            currentLockTimeMS = defaultLockTimeMS;
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKey()) {
            case 'q':
                activePiece.rotate(Rotation.LEFT, ui);
                break;
            case 'e':
                activePiece.rotate(Rotation.RIGHT, ui);
                break;
            case 'f':
                activePiece.rotate(Rotation.FLIP, ui);
                break;
            case 'a':
                activePiece.move(Action.LEFT, ui);
                break;
            case 'd':
                activePiece.move(Action.RIGHT, ui);
                break;
            case 's':
                pullPiece();
                frameCount = 0;
                break;
            case 'w':
                while (!activePiece.isColliding(ui.matrix)) {
                    activePiece.move(Action.DOWN, ui);
                }
                activePiece.position.y--;
                ui.merge(activePiece);
                activePiece = newPiece();
                break;
        }
    }

    private void pullPiece() {
        activePiece.move(Action.DOWN, ui);
        if (activePiece.isColliding(ui.matrix)) {
            activePiece.position.y--;
            ui.merge(activePiece);
            activePiece = newPiece();
        }
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
