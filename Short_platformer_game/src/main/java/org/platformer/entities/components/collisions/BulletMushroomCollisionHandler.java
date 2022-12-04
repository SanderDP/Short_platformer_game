package org.platformer.entities.components.collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.platformer.entities.EnemyType;
import org.platformer.entities.EntityType;
import org.platformer.entities.components.enemies.MushroomComponent;

public class BulletMushroomCollisionHandler extends CollisionHandler {

    public BulletMushroomCollisionHandler() {
        super(EntityType.BULLET, EnemyType.MUSHROOM);
    }

    @Override
    protected void onCollision(Entity bullet, Entity mushroom) {
        mushroom.getComponent(MushroomComponent.class).hit();
        bullet.removeFromWorld();
    }
}
