package org.platformer.entities.components.enemies;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import org.platformer.entities.EnemyType;

public class MushMushCollisionHandler extends CollisionHandler {

    public MushMushCollisionHandler() {
        super(EnemyType.MUSHROOM, EnemyType.MUSHROOM);
    }

    @Override
    protected void onCollisionBegin(Entity mush1, Entity mush2) {
        mush1.getComponent(MushroomComponent.class).setPlatformInWayFlag(true);
        mush1.getComponent(CollidableComponent.class).setValue(false);

        mush2.getComponent(MushroomComponent.class).setPlatformInWayFlag(true);
        mush2.getComponent(CollidableComponent.class).setValue(false);
    }

    @Override
    protected void onCollisionEnd(Entity mush1, Entity mush2) {
        FXGL.getGameTimer().runOnceAfter(() -> {
            mush1.getComponent(CollidableComponent.class).setValue(true);
        }, Duration.seconds(.1));

        FXGL.getGameTimer().runOnceAfter(() -> {
            mush2.getComponent(CollidableComponent.class).setValue(true);
        }, Duration.seconds(.1));
    }
}
