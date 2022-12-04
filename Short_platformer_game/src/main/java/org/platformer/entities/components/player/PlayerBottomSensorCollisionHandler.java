package org.platformer.entities.components.player;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import org.platformer.entities.EnemyType;
import org.platformer.entities.components.enemies.EnemyComponent;
import org.platformer.entities.components.enemies.MushroomComponent;

public class PlayerBottomSensorCollisionHandler extends SensorCollisionHandler {

    private PlayerComponent playerComponent;

    public PlayerBottomSensorCollisionHandler(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        if (other.getType() == EnemyType.MUSHROOM) {
            playerComponent.setInvincibleFor(1);
            playerComponent.jump();
            other.getComponent(MushroomComponent.class).hit();
        }
    }
}
