package org.platformer.entities.components.enemycomponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;

public class RightGroundSensorCollisionHandler extends SensorCollisionHandler {

    private MushroomComponent mushroomComponent;

    public RightGroundSensorCollisionHandler(MushroomComponent mushroomComponent) {
        this.mushroomComponent = mushroomComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        mushroomComponent.getRightGroundList().add(other);
        mushroomComponent.setHasGroundRight(mushroomComponent.hasGroundRight());
    }

    @Override
    protected void onCollisionEnd(Entity other) {
        mushroomComponent.getRightGroundList().remove(other);
        mushroomComponent.setHasGroundRight(mushroomComponent.hasGroundRight());
    }
}
