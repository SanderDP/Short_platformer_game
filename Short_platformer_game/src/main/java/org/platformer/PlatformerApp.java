package org.platformer;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import kotlin.sequences.Sequence;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.T;

public class PlatformerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        //settings.setWidth(50 * 16);
        //settings.setHeight(38 * 16);

        //set mode to developer and make developer tools accessible when playing by pressing "1"
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
        settings.setDeveloperMenuEnabled(true);
    }

    private Entity player;

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Stomp") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).stomp();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).shoot();
            }
        }, KeyCode.Z);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        setLevelFromMap("tmx/platformer.tmx");

        player = spawn("player", 30, 30);

        getGameScene().setBackgroundRepeat("Background/Blue.png");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-100, 0, 250 * 16, getAppHeight() );
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 750);

        onCollision(EntityType.PLAYER, EntityType.POWERUPBOX, (player, powerupbox) -> {
            ArrayList<KeyCode> sequence = new ArrayList<>();
            Random rand = new Random();
            List<KeyCode> inputList = Arrays.asList(UP, DOWN, RIGHT, LEFT);

            int numberOfElements = 8;

            for (int i = 0; i < numberOfElements; i++) {
                int randomIndex = rand.nextInt(inputList.size());
                sequence.add(inputList.get(randomIndex));
            }
            getMiniGameService().startTriggerSequence(sequence, result -> {
                PowerupType pt;
                String message;
                if (result.isSuccess()) {
                    pt = PowerupType.SHOOT;
                    message = "Press \"z\" to shoot!";
                } else {
                    pt = PowerupType.STOMP;
                    message = "Press down to slam down with force!";
                }
                player.getComponent(PlayerComponent.class).addPowerup(pt);
                getNotificationService().pushNotification(message);
            });
            powerupbox.removeFromWorld();
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.GOAL, (player, goal) -> {
            getDialogService().showMessageBox("Game complete", getGameController()::exit);
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (player.getY() > getAppHeight()) {
            onPlayerDied();
        }
    }

    private void onPlayerDied() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(30, 550));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}