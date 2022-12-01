package org.platformer;

import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
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
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class PlatformerFactory implements EntityFactory {

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
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

    @Spawns("goal")
    public Entity newGoal(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.GOAL)
                .bbox(new HitBox(new Point2D(9, 20), BoundingShape.box(46, 44)))
                .with(new GoalComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("fruit")
    public Entity newFruit(SpawnData data) {
        Circle view = new Circle(data.<Integer>get("width") / 2, Color.GOLD);
        view.setCenterX(8);
        view.setCenterY(-8);
        return entityBuilder(data)
                .type(EntityType.COIN)
                .bbox(new HitBox(new Point2D(0, -16), BoundingShape.circle(data.<Integer>get("width") / 2)))
                .view(view)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("powerupbox")
    public Entity newPowerupbox(SpawnData data) {
        //HitBox bottomHitbox = new HitBox(new Point2D(4, 22), BoundingShape.box(20, 2));
        return entityBuilder(data)
                .type(EntityType.POWERUPBOX)
                .bbox(new HitBox(new Point2D(4, 2), BoundingShape.box(20, 20)))
                .with(new PowerupboxComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
        Point2D direction;
        if (player.getScaleX() > 0) {
            direction = new Point2D(100000000, data.getY());
        } else  {
            direction = new Point2D(-100000000, data.getY());
        }

        return entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox(new Rectangle(10, 2, Color.BLACK))
                .with(new ProjectileComponent(direction, 1000))
                .with(new OffscreenCleanComponent())
                .with(new CollidableComponent(true))
                .build();
    }
}
