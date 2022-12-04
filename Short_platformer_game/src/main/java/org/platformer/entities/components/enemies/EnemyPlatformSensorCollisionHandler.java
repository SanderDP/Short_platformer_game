package org.platformer.entities.components.enemies;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import org.platformer.entities.EntityType;

public class EnemyPlatformSensorCollisionHandler extends SensorCollisionHandler {

    EnemyComponent enemyComponent;

    public EnemyPlatformSensorCollisionHandler(EnemyComponent enemyComponent) {
        this.enemyComponent = enemyComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        if (other.getType().equals(EntityType.PLATFORM)) {
            enemyComponent.setPlatformInWayFlag(true);
        }
    }
}
