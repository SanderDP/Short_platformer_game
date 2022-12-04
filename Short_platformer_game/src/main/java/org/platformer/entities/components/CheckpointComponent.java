package org.platformer.entities.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class CheckpointComponent extends Component {

    private AnimatedTexture texture;

    private AnimationChannel animCheck, animIdleCheck;

    private boolean checked;

    public CheckpointComponent() {
        animCheck = new AnimationChannel(image("Items/Checkpoints/Checkpoint/Checkpoint (Flag Out) (64x64).png"), 26, 64, 64, Duration.seconds(1), 0, 25);
        animIdleCheck = new AnimationChannel(image("Items/Checkpoints/Checkpoint/Checkpoint (Flag Idle) (64x64).png"), 10, 64, 64, Duration.seconds(1), 0, 9);

        texture = new AnimatedTexture(new AnimationChannel(image("Items/Checkpoints/Checkpoint/Checkpoint (No Flag).png"), 1, 64, 64, Duration.seconds(1), 0, 0));
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);

        checked = false;
    }

    @Override
    public void onUpdate(double tpf) {
        if (isChecked() && texture.getAnimationChannel() != animCheck && texture.getAnimationChannel() != animIdleCheck) {
            texture.playAnimationChannel(animCheck);
            texture.setOnCycleFinished(() -> {
                texture.loopAnimationChannel(animIdleCheck);
            });
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
