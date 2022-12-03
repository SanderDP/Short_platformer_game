package org.platformer.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.image;

public class GoalComponent extends Component {

    private Texture texture;

    public GoalComponent() {
        texture = new Texture(image("Items/Checkpoints/End/End (Idle).png"));
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
