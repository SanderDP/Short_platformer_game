package org.platformer.minigames.circuitbreaker;

import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.minigames.MiniGameView;
import com.almasb.fxgl.minigames.circuitbreaker.CircuitBreakerMiniGame;
import com.almasb.fxgl.minigames.circuitbreaker.MazeCell;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomControlCircuitBreakerView extends MiniGameView<CircuitBreakerMiniGame> {

    private static int WIDTH = 700;
    private static int HEIGHT = 500;
    private static CircuitBreakerMiniGame miniGame;

    private Canvas canvas;
    private GraphicsContext g;

    private Point2D oldPosition;

    private HashMap<Point2D, Point2D> lineData = new HashMap<>();

    public CustomControlCircuitBreakerView() {
        super(miniGame = new CircuitBreakerMiniGame(10, 10, 15.0, 135.0, Duration.seconds(2.0)));
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.g = canvas.getGraphicsContext2D();
        this.oldPosition = miniGame.getStartPoint();
        getChildren().add(canvas);
        System.out.println("something");
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D charPosition = miniGame.getPlayerPosition();

        g.clearRect(0,0,WIDTH, HEIGHT);

        g.setFill(Color.DARKGREEN);
        g.setGlobalAlpha(0.9);
        g.fillRect(0,0,WIDTH, HEIGHT);

        g.setStroke(Color.LIGHTGREEN);
        g.setGlobalAlpha(1.0);
        g.setLineWidth(6.5);
        g.strokeRect(0,0,WIDTH, HEIGHT);

        g.setFill(Color.BLACK);
        g.fillOval(charPosition.getX(), charPosition.getY(), miniGame.getPlayerSize(), miniGame.getPlayerSize());

        lineData.put(oldPosition.add(miniGame.getPlayerSize() / 2, miniGame.getPlayerSize() / 2), charPosition.add(miniGame.getPlayerSize() / 2, miniGame.getPlayerSize() / 2));

        g.setStroke(Color.BLACK);
        g.setLineWidth(2.5);
        lineData.forEach( (p0, p1) -> {
            g.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
        });

        g.setLineWidth(3.5);
        g.setStroke(Color.YELLOWGREEN);

        int ratioX = WIDTH / miniGame.getMaze().getWidth();
        int ratioY = HEIGHT / miniGame.getMaze().getHeight();

        for (int y = 0; y < miniGame.getMaze().getHeight(); y++) {
            for (int x = 0; x < miniGame.getMaze().getWidth(); x++) {
                MazeCell cell = miniGame.getMaze().getMazeCell(x, y);

                if (cell.getHasLeftWall$fxgl_gameplay()) {
                    g.strokeLine(x*ratioX, y*ratioY, x*ratioX, (y+1)*ratioY);
                }

                if (cell.getHasTopWall$fxgl_gameplay()) {
                    g.strokeLine(x*ratioX, y*ratioY, (x+1) * ratioX, y*ratioY);
                }
            }
        }

        g.setFill(Color.YELLOW);
        g.fillOval(miniGame.getStartPoint().getX(), miniGame.getStartPoint().getY(), miniGame.getPlayerSize(), miniGame.getPlayerSize());
        g.fillOval(miniGame.getEndPoint().getX(), miniGame.getEndPoint().getY(), miniGame.getPlayerSize(), miniGame.getPlayerSize());

        oldPosition = charPosition;
    }

    @Override
    public void onInitInput(@NotNull Input input) {
        input.addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                getMiniGame().up();
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                getMiniGame().down();
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                getMiniGame().right();
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                getMiniGame().left();
            }
        }, KeyCode.LEFT);
    }
}
