package org.platformer.entities.components.enemycomponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.SensorCollisionHandler;

public class LeftGroundSensorCollisionHandler extends SensorCollisionHandler {

    private MushroomComponent mushroomComponent;

    public LeftGroundSensorCollisionHandler(MushroomComponent mushroomComponent) {
        this.mushroomComponent = mushroomComponent;
    }

    @Override
    protected void onCollisionBegin(Entity other) {
        mushroomComponent.getLeftGroundList().add(other);
        mushroomComponent.setHasGroundLeft(mushroomComponent.hasGroundLeft());
    }

    @Override
    protected void onCollisionEnd(Entity other) {
        mushroomComponent.getLeftGroundList().remove(other);
        mushroomComponent.setHasGroundLeft(mushroomComponent.hasGroundLeft());
    }
}
