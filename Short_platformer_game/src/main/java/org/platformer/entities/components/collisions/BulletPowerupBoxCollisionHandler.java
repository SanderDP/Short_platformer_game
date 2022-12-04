package org.platformer.entities.components.collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import org.platformer.entities.EntityType;

public class BulletPowerupBoxCollisionHandler extends CollisionHandler {

    public BulletPowerupBoxCollisionHandler() {
        super(EntityType.BULLET, EntityType.POWERUPBOX);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity powerupbox) {
        bullet.removeFromWorld();
    }
}
