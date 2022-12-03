package org.platformer.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGL.random;

public class FruitComponent extends Component {

    private AnimatedTexture texture;

    public FruitComponent() {
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Apple.png");
        fruits.add("Bananas.png");
        fruits.add("Cherries.png");
        fruits.add("Kiwi.png");
        fruits.add("Melon.png");
        fruits.add("Orange.png");
        fruits.add("Pineapple.png");
        fruits.add("Strawberry.png");

        AnimationChannel animIdle = new AnimationChannel(image("Items/Fruits/" + fruits.get(random(0, 7))), 17, 32, 32, Duration.seconds(.5), 0, 16);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
