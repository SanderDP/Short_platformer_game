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
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.platformer.entities.*;
import org.platformer.entities.components.*;
import org.platformer.entities.components.player.PlayerBottomSensorCollisionHandler;
import org.platformer.entities.components.player.PlayerComponent;
import org.platformer.entities.components.enemies.EnemyPlatformSensorCollisionHandler;
import org.platformer.entities.components.enemies.MushroomComponent;
import org.platformer.entities.components.player.PlayerTopSensorCollisionHandler;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerFactory implements EntityFactory {

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        PlayerComponent playerComponent = new PlayerComponent();

        //adding sensors
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(5, 30), BoundingShape.box(23, 5)));
        physics.addSensor(new HitBox("TOP_SENSOR", new Point2D(7, 5), BoundingShape.box(19, 4)), new PlayerTopSensorCollisionHandler(playerComponent));
        physics.addSensor(new HitBox("BOTTOM_SENSOR", new Point2D(6,28), BoundingShape.box(20, 5)), new PlayerBottomSensorCollisionHandler(playerComponent));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox("HITBOX",new Point2D(5,7), BoundingShape.box(22, 23))) // general hitbox of player
                .collidable()
                .with(physics)
                .with(playerComponent)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("startpoint")
    public Entity newStartpoint(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.START)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new StartComponent())
                .build();
    }

    @Spawns("checkpoint")
    public Entity newCheckpoint(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.CHECKPOINT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CheckpointComponent())
                .collidable()
                .build();
    }

    @Spawns("goal")
    public Entity newGoal(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.GOAL)
                .bbox(new HitBox(new Point2D(9, 20), BoundingShape.box(46, 44)))
                .with(new GoalComponent())
                .collidable()
                .build();
    }

    @Spawns("fruit")
    public Entity newFruit(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.FRUIT)
                .bbox(new HitBox(new Point2D(data.<Integer>get("width") / 2, data.<Integer>get("width") / 2), BoundingShape.circle(data.<Integer>get("width") / 2)))
                .with(new FruitComponent())
                .collidable()
                .build();
    }

    @Spawns("powerupbox")
    public Entity newPowerupbox(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.POWERUPBOX)
                .bbox(new HitBox("HITBOX", new Point2D(4, 2), BoundingShape.box(20, 20)))
                .collidable()
                .with(new PowerupboxComponent())
                .with(new PhysicsComponent())
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
                .collidable()
                .build();
    }

    @Spawns("mushroom")
    public Entity newMushroom(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        MushroomComponent mushroomComponent = new MushroomComponent();

        physics.addGroundSensor(new HitBox("RIGHT_GROUND_SENSOR", new Point2D(29, 32), BoundingShape.box(3, 5)));
        physics.addSensor(new HitBox("RIGHT_PLATFORM_SENSOR", new Point2D(29, 12), BoundingShape.box(3, 5)), new EnemyPlatformSensorCollisionHandler(mushroomComponent));

        return entityBuilder(data)
                .type(EnemyType.MUSHROOM)
                .bbox(new HitBox("HITBOX", new Point2D(3,12), BoundingShape.box(26, 20))) // general hitbox mushroom
                .collidable()
                .with(physics)
                .with(mushroomComponent)
                .build();
    }
}
