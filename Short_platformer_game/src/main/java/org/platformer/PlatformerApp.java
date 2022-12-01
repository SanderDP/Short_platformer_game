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

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(50 * 16);
        settings.setHeight(38 * 16);

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

        player = spawn("player", 30, 550);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-100, 0, 250 * 16, getAppHeight() );
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 750);

        onCollision(EntityType.PLAYER, EntityType.POWERUPBOX, (player, powerupbox) -> {
            player.getComponent(PlayerComponent.class).addPowerup(PowerupType.SHOOT);
            //Todo: add minigame for powerup
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