package org.platformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.image;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animJump, animDoubleJump, animFall;
    private int jumps = 2;
    private ArrayList<PowerupType> powerups = new ArrayList<PowerupType>();

    public PlayerComponent() {
        animIdle = new AnimationChannel(image("Virtual Guy/Idle (32x32).png"), 11, 32, 32, Duration.seconds(1), 0, 10);
        animWalk = new AnimationChannel(image("Virtual Guy/Run (32x32).png"), 12, 32, 32, Duration.seconds(1), 0, 11);
        animJump = new AnimationChannel(image("Virtual Guy/Jump (32x32).png"), 1, 32, 32, Duration.seconds(1), 0, 0);
        animDoubleJump = new AnimationChannel(image("Virtual Guy/Double Jump (32x32).png"), 6, 32, 32, Duration.seconds(0.50), 0, 5);
        animFall= new AnimationChannel(image("Virtual Guy/Fall (32x32).png"), 1, 32, 32, Duration.seconds(1), 0, 0);

        texture = new AnimatedTexture(animIdle);
        texture.loop();

        powerups.add(PowerupType.NONE);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (physics.isOnGround()) {
            if (physics.isMovingX()) {
                if (texture.getAnimationChannel() != animWalk) {
                    texture.loopAnimationChannel(animWalk);
                }
            } else if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                }
            }
        else if (physics.getVelocityY() > 0) {
            texture.loopAnimationChannel(animFall);
        } else if (physics.getVelocityY() <= 0 && texture.getAnimationChannel() != animDoubleJump){
            texture.loopAnimationChannel(animJump);
        }
    }

    public void left() {
        //setscaleX(-1) mirrors character sprite compared to given png
        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void stop() {
        physics.setVelocityX(0);
    }

    public void jump() {
        if (jumps == 0)
            return;

        physics.setVelocityY(-250);

        jumps--;

        if (jumps == 0) {
            texture.playAnimationChannel(animDoubleJump);
        } else
            texture.loopAnimationChannel(animJump);
    }

    public void stomp() {
        if (!powerups.contains(PowerupType.STOMP))
            return;

        physics.setVelocityY(1000);
    }

    public void shoot() {
        if (!powerups.contains(PowerupType.SHOOT))
            return;

        FXGL.spawn("bullet", entity.getCenter());
    }

    public void addPowerup(PowerupType powerupType) {
        if (powerups.contains(PowerupType.NONE)) {
            powerups.remove(PowerupType.NONE);
        }
        if (!powerups.contains(powerupType)) {
            powerups.add(powerupType);
        }
    }
}
