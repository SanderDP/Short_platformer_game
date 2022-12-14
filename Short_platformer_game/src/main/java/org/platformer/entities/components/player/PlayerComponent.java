package org.platformer.entities.components.player;

import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.asset.AssetLoaderService;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.platformer.entities.PowerupType;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animJump, animDoubleJump, animFall, animDied, animSpawn, animHit;
    private int jumps;
    private int amountOfJumps = 2;
    private boolean invincible;
    private ArrayList<PowerupType> powerups = new ArrayList<PowerupType>();

    public PlayerComponent() {
        animIdle = new AnimationChannel(image("Virtual Guy/Idle (32x32).png"), 11, 32, 32, Duration.seconds(11/10), 0, 10);
        animWalk = new AnimationChannel(image("Virtual Guy/Run (32x32).png"), 12, 32, 32, Duration.seconds(12/10), 0, 11);
        animJump = new AnimationChannel(image("Virtual Guy/Jump (32x32).png"), 1, 32, 32, Duration.seconds(1/10), 0, 0);
        animDoubleJump = new AnimationChannel(image("Virtual Guy/Double Jump (32x32).png"), 6, 32, 32, Duration.seconds(0.50), 0, 5);
        animFall = new AnimationChannel(image("Virtual Guy/Fall (32x32).png"), 1, 32, 32, Duration.seconds(1/10), 0, 0);
        animDied = new AnimationChannel(image("Virtual Guy/Disappearing (32x32).png"), 7, 32, 32, Duration.seconds(1), 0, 6);
        animSpawn = new AnimationChannel(image("Virtual Guy/Appearing (32x32).png"), 7, 32, 32, Duration.seconds(1), 0, 6);
        animHit = new AnimationChannel(image("Virtual Guy/Hit (32x32).png"), 7, 32, 32, Duration.seconds(1), 0, 6);

        texture = new AnimatedTexture(animIdle);
        texture.loop();

        powerups.add(PowerupType.NONE);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.getViewComponent().addChild(texture);

        invincible = false;

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                jumps = amountOfJumps;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (texture.getAnimationChannel() != animSpawn && texture.getAnimationChannel() != animDied && texture.getAnimationChannel() != animHit) {
            if (physics.isOnGround()) {
                if (physics.isMovingX()) {
                    if (texture.getAnimationChannel() != animWalk) {
                        texture.loopAnimationChannel(animWalk);
                    }
                } else if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                }
            } else if (physics.getVelocityY() > 0) {
                texture.loopAnimationChannel(animFall);
            } else if (physics.getVelocityY() <= 0 && texture.getAnimationChannel() != animDoubleJump) {
                texture.loopAnimationChannel(animJump);
            }
        }
    }

    public void left() {
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
        play("player/jump.wav");

        jumps--;

        if (jumps == 0) {
            texture.playAnimationChannel(animDoubleJump);
        } else
            texture.loopAnimationChannel(animJump);
    }

    public void jumpNoCheck() {
        physics.setVelocityY(-250);

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
        play("player/shoot.wav");
        FXGL.spawn("bullet", entity.getCenter());
    }

    public void addPowerup(PowerupType powerupType) {
        powerups.remove(PowerupType.NONE);
        if (!powerups.contains(powerupType)) {
            powerups.add(powerupType);
        }
    }

    public void died() {
        getInput().clearAll();
        texture.playAnimationChannel(animDied);
        play("player/died.wav");

        texture.setOnCycleFinished(() -> {
            getDialogService().showMessageBox("Game Over", getGameController()::exit);
        });
    }

    public void hit() {
        if (isInvincible()) {
            return;
        }
        texture.playAnimationChannel(animHit);
        play("player/hit.wav");

        setInvincibleFor(1);

        texture.setOnCycleFinished(() -> {
            texture.playAnimationChannel(animIdle);
        });

        powerups.clear();
        powerups.add(PowerupType.NONE);
    }

    public void spawn() {
        texture.playAnimationChannel(animSpawn);
        play("player/died.wav");

        texture.setOnCycleFinished(() -> {
            texture.loopAnimationChannel(animIdle);
        });
    }

    public void setInvincibleFor(double seconds) {
        invincible = true;
        getGameTimer().runOnceAfter(() -> invincible = false, Duration.seconds(seconds));
    }

    public boolean isInvincible() {
        return invincible;
    }
}
