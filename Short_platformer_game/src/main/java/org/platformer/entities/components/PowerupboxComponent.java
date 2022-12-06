package org.platformer.entities.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import org.platformer.entities.PowerupType;
import org.platformer.entities.components.player.PlayerComponent;
import org.platformer.minigames.circuitbreaker.CustomControlCircuitBreakerView;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getNotificationService;

public class PowerupboxComponent extends Component {

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animHit, animBreak;

    public PowerupboxComponent() {
        animIdle = new AnimationChannel(image("Items/Boxes/Box1/Idle.png"), 1, 28, 28,Duration.seconds(1), 0, 0);
        animHit = new AnimationChannel(image("Items/Boxes/Box1/Hit (28x24).png"), 3, 28, 24, Duration.seconds(0.5), 0, 2);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    public void hit(Entity player) {
        texture.playAnimationChannel(animHit);
        play("items/powerupbox/powerupboxhit.wav");
        texture.setOnCycleFinished(() -> {
            getMiniGameService().startMiniGame(new CustomControlCircuitBreakerView(), (result) -> {
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
            entity.removeFromWorld(); // remove the powerbox from the world
        });
    }
}
