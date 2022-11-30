package org.platformer;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
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
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        setLevelFromMap("tmx/platformer.tmx");

        player = spawn("player", 50, 50);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-100, 0, 250 * 16, getAppHeight() );
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}