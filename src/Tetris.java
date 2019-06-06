import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class Tetris extends PApplet {
    public static double unitsPerSecond = 1;
    UI ui = new UI(this, new PVector(1, 2));
    Tetromino upComingPiece, activePiece;
    boolean gameOver = false;
    double currentLockTimeMS = 1000;
    final double defaultLockTimeMS = 1000;
    public static int highScore = 0;
    public static boolean epilepsy = false;
    private long now;

    public static void main(String[] args) {
        PApplet.main("Tetris");
    }

    public void settings() {
        frameRate = 60;
        size(600, 600);
    }

    public void setup() {
        load();
        activePiece = newPiece(false);
        upComingPiece = newPiece(true);
        strokeWeight(.1f);

        surface.setTitle("Tetris");
        surface.setIcon(loadImage("Icon.PNG"));
    }

    public void draw() {
        background(100);
        scale(20);

        if (activePiece.isColliding(ui.matrix)) {
            now = System.nanoTime();
        }


        ui.draw(gameOver);
        if (!gameOver) {
            upComingPiece.render(255);
            activePiece.render(255);
            ui.computeGhost(activePiece);
        }

        if (frameCount >= frameRate / unitsPerSecond) {
            frameCount = 0;
            pullPiece();
        }

        if (currentLockTimeMS < 0) {
            ui.merge(activePiece);
            changePieces();
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
                changePieces();
                break;
            case 'r':
                gameOver = false;
                activePiece = newPiece(false);
                upComingPiece = newPiece(true);
                ui = new UI(this, new PVector(1, 2));
            case '`':
                epilepsy = !epilepsy;
                break;
        }
    }

    private void pullPiece() {
        activePiece.move(Action.DOWN, ui);
        if (activePiece.isColliding(ui.matrix)) {
            activePiece.position.y--;
                ui.merge(activePiece);
                changePieces();
        }
    }

    private Tetromino newPiece(boolean isUpcoming) {
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
        if (piece.isColliding(ui.matrix)) {
            gameOver = true;
            if (ui.score > highScore) {
                PrintWriter writer = createWriter("Save.txt");
                writer.println(ui.score);
                writer.flush();
                writer.close();
            }

        }
        if (isUpcoming) {
            piece.position = new PVector(15, 14);
        }
        return piece;
    }

    private void changePieces() {
        try {
            activePiece = upComingPiece.getClass().getConstructor(PApplet.class, PVector.class).newInstance(this, new PVector(ui.position.x + ui.BOARD_WIDTH / 2 - 1, ui.position.y + 1));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        upComingPiece = newPiece(true);
    }

    private void load() {
        File saveFile = new File("Save.txt");
        String line;
        if (saveFile.exists()) {
            BufferedReader reader = createReader("Save.txt");
            try {
                line = reader.readLine();
                highScore = Integer.parseInt(line);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
