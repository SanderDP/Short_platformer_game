package org.platformer.entities.components.enemies;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import org.platformer.entities.EnemyType;

public class MushMushCollisionHandler extends CollisionHandler {

    public MushMushCollisionHandler() {
        super(EnemyType.MUSHROOM, EnemyType.MUSHROOM);
    }

    @Override
    protected void onCollisionBegin(Entity mush1, Entity mush2) {
        //problem: when two mushes collide they both activate this event so this solution only randomizes stuff
        //todo: make this actually work normally -> when colliding both should turn around
        mush1.getComponent(MushroomComponent.class).pause();
        mush2.getComponent(MushroomComponent.class).setPlatformInWayFlag(true);

        mush2.getComponent(MushroomComponent.class).pause();
        mush2.getComponent(MushroomComponent.class).setPlatformInWayFlag(true);

        FXGL.getGameTimer().runOnceAfter(() -> {
            mush1.getComponent(MushroomComponent.class).resume();
        }, Duration.seconds(0.25));

        FXGL.getGameTimer().runOnceAfter(() -> {
            mush2.getComponent(MushroomComponent.class).resume();
        }, Duration.seconds(0.5));
    }
}
