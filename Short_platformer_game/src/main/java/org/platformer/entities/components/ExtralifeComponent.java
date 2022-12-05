package org.platformer.entities.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.image;

public class ExtralifeComponent extends Component {

    private Texture texture;

    public ExtralifeComponent() {
        texture = new Texture(image("Items/Other/Heart (16x16).png"));
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
