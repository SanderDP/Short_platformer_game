package org.platformer.entities.components.player;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import org.platformer.entities.EnemyType;

public class PlayerBottomSensorCollisionHandler extends SensorCollisionHandler {

    private PlayerComponent playerComponent;

    public PlayerBottomSensorCollisionHandler(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        if (other.getType() == EnemyType.MUSHROOM)
            System.out.println("hit mushroom on head");
    }
}
