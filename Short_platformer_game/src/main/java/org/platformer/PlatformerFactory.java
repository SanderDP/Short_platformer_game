package org.platformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PlatformerFactory implements EntityFactory {

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COIN)
                .viewWithBBox(new Circle(data.<Integer>get("width") / 2, Color.GOLD))
                .with(new PhysicsComponent())
                .build();
    }
}
