package org.platformer;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

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

        //groundsensor detects if playercharacter is on the ground
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(5, 30), BoundingShape.box(23, 5)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox(new Point2D(5,5), BoundingShape.box(23, 25)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        Circle view = new Circle(data.<Integer>get("width") / 2, Color.GOLD);
        view.setCenterX(8);
        view.setCenterY(-8);
        return FXGL.entityBuilder(data)
                .type(EntityType.COIN)
                .bbox(new HitBox(new Point2D(0, -16), BoundingShape.circle(data.<Integer>get("width") / 2)))
                .view(view)
                .with(new CollidableComponent(true))
                .build();
    }
}
