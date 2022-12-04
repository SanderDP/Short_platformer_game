package org.platformer.entities.components.player;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import org.platformer.entities.EntityType;
import org.platformer.entities.components.PowerupboxComponent;

public class PlayerTopSensorCollisionHandler extends SensorCollisionHandler {

    private PlayerComponent playerComponent;

    public PlayerTopSensorCollisionHandler(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        if (other.getType().equals(EntityType.POWERUPBOX))
            other.getComponent(PowerupboxComponent.class).hit(playerComponent.getEntity());
    }
}
