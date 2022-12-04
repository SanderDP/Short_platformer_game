package org.platformer.entities.components.collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.platformer.entities.EntityType;

public class BulletPlatformCollisionHandler extends CollisionHandler {

    public BulletPlatformCollisionHandler() {
        super(EntityType.BULLET, EntityType.PLATFORM);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity platform) {
        bullet.removeFromWorld();
    }
}
