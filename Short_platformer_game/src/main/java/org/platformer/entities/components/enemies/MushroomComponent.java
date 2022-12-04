package org.platformer.entities.components.enemies;

import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MushroomComponent extends EnemyComponent{

    private boolean noGroundFlag;

    public MushroomComponent() {
        setAnimIdle(new AnimationChannel(image("Enemies/Mushroom/Idle (32x32).png"), 14, 32, 32, Duration.seconds(14/10), 0, 13));
        setAnimRun(new AnimationChannel(image("Enemies/Mushroom/Run (32x32).png"), 16, 32, 32, Duration.seconds(16/10), 0, 15));
        setAnimHit(new AnimationChannel(image("Enemies/Mushroom/Hit.png"), 5, 32, 32, Duration.seconds(1), 0, 4));

        setTexture(new AnimatedTexture(getAnimIdle()));
        getTexture().loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.getViewComponent().addChild(getTexture());

        setMovespeed(25);

        noGroundFlag = false;
        setPlatformInWayFlag(false);

        getPhysics().setOnPhysicsInitialized( () -> {
            moving();
            getPhysics().onGroundProperty().addListener((obs, old, thereIsGround) -> {
                if (!thereIsGround) {
                    noGroundFlag = true; // this flag is necessary as we cannot update scaleX of entity at same time as the detection and notification of collision since physicsWorld is locked
                }
            });
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (isPlatformInWayFlag() || noGroundFlag) { //  check if onGroundPropertyListener or whether the platformsensor (EnemyPlatformSensorCollisionHandler) has fired an event
            entity.setScaleX(entity.getScaleX() * -1); //set scaleX to it's opposite => flips whole entity along it's x-axis
            setPlatformInWayFlag(false);
            noGroundFlag = false;
        }

        if (getPhysics().isMovingX()) {
            if (getTexture().getAnimationChannel() != getAnimRun()) {
                getTexture().loopAnimationChannel(getAnimRun());
            }
        }
        getPhysics().setVelocityX(getMovespeed() * entity.getScaleX());
    }

    @Override
    public void moving() {
        getPhysics().setVelocityX(getMovespeed() * entity.getScaleX());
    }
}
