package org.platformer;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.minigames.circuitbreaker.CircuitBreakerView;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public void hit(Entity powerupbox, Entity player) {
        texture.playAnimationChannel(animHit);
        // remove
        texture.setOnCycleFinished(() -> {
            //generate a random sequence with arrow keys
            ArrayList<KeyCode> sequence = new ArrayList<>();
            Random rand = new Random();
            List<KeyCode> inputList = Arrays.asList(KeyCode.UP, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.LEFT);

            int numberOfElements = 8;

            for (int i = 0; i < numberOfElements; i++) {
                int randomIndex = rand.nextInt(inputList.size());
                sequence.add(inputList.get(randomIndex));
            }
            getMiniGameService().startTriggerSequence(sequence, (result) -> {
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
            powerupbox.removeFromWorld(); // remove the powerbox from the world
        });
    }
}
