package org.platformer;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.image;

public class PowerupboxComponent extends Component {

    private Texture texture;

    private AnimationChannel animHit, animBreak;

    public PowerupboxComponent() {
        texture = new Texture(image("Items/Boxes/Box1/Idle.png"));
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
