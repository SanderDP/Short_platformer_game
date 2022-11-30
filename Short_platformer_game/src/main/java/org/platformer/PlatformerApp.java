package org.platformer;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;

public class PlatformerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(15 * 70);
        settings.setHeight(10 * 70);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().setLevel(FXGL.setLevelFromMap("tmx/platformer.tmx"));

    }

    public static void main(String[] args) {
        launch(args);
    }
}