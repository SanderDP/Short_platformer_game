package org.platformer.entities.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class StartComponent extends Component {

    private AnimatedTexture texture;

    public StartComponent() {

        texture = new AnimatedTexture(new AnimationChannel(image("Items/Checkpoints/Start/Start (Moving) (64x64).png"), 17, 64, 64, Duration.seconds(1), 0, 16));
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
