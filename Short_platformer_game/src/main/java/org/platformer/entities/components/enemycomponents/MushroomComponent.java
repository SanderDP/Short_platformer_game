package org.platformer.entities.components.enemycomponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Preload;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.text;

public class MushroomComponent extends EnemyComponent{

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animRun, animHit;

    private int movespeed;

    private int scaleX = -1; // due to the used sprites facing left, scaleX and entity.getScaleX() must always be opposite values
                             // if the sprites where to face right, they should always have the same value

    public MushroomComponent() {
        animIdle = new AnimationChannel(image("Enemies/Mushroom/Idle (32x32).png"), 14, 32, 32, Duration.seconds(3), 0, 13);
        animRun = new AnimationChannel(image("Enemies/Mushroom/Run (32x32).png"), 16, 32, 32, Duration.seconds(3), 0, 15);
        animHit = new AnimationChannel(image("Enemies/Mushroom/Hit.png"), 5, 32, 32, Duration.seconds(1), 0, 4);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.getViewComponent().addChild(texture);

        movespeed = 25;

        physics.setOnPhysicsInitialized(this::moving);

        physics.onGroundProperty().addListener((obs, old, thereIsGround) -> {
            if (!thereIsGround) {
                scaleX *= -1; // entity.getScaleX() geeft hier een error => global variable scaleX gebruiken
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (scaleX == entity.getScaleX()) { // make sure getScaleX() is opposite to scaleX
            entity.setScaleX(scaleX * -1);
        }
        if (physics.isMovingX()) {
            if (texture.getAnimationChannel() != animRun) {
                texture.loopAnimationChannel(animRun);
            }
        }
        physics.setVelocityX(movespeed * scaleX);
    }

    @Override
    public void stop() {
        physics.setVelocityX(0);
    }

    @Override
    public void moving() {
        physics.setVelocityX(movespeed * scaleX);
    }

    @Override
    public void hit() {
        texture.playAnimationChannel(animHit);

        texture.setOnCycleFinished(() -> {
            entity.removeFromWorld();
        });
    }
}
