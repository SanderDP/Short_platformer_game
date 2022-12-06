package org.platformer.entities.components.enemies;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

public abstract class EnemyComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animRun, animHit;

    private int movespeed;

    private boolean platformInWayFlag;

    public void stop() {
        getPhysics().setVelocityX(0);
    };

    public abstract void moving();

    public void hit() {
        if (getTexture().getAnimationChannel() == getAnimHit())
            return;

        FXGL.play("enemy/enemyhit.wav");
        getTexture().playAnimationChannel(getAnimHit());

        //todo: make entity untouchable / set collision off
        entity.getComponent(CollidableComponent.class).setValue(false);
        //entity.getBoundingBoxComponent().clearHitBoxes(); also doesn't do it either

        getTexture().setOnCycleFinished(() -> {
            entity.removeFromWorld();
        });
    };

    public PhysicsComponent getPhysics() {
        return physics;
    }

    public AnimatedTexture getTexture() {
        return texture;
    }

    public void setTexture(AnimatedTexture texture) {
        this.texture = texture;
    }

    public AnimationChannel getAnimIdle() {
        return animIdle;
    }

    public void setAnimIdle(AnimationChannel animIdle) {
        this.animIdle = animIdle;
    }

    public AnimationChannel getAnimRun() {
        return animRun;
    }

    public void setAnimRun(AnimationChannel animRun) {
        this.animRun = animRun;
    }

    public AnimationChannel getAnimHit() {
        return animHit;
    }

    public void setAnimHit(AnimationChannel animHit) {
        this.animHit = animHit;
    }

    public int getMovespeed() {
        return movespeed;
    }

    public void setMovespeed(int movespeed) {
        this.movespeed = movespeed;
    }

    public void setPlatformInWayFlag(boolean platformInWayFlag) {
        this.platformInWayFlag = platformInWayFlag;
    }

    public boolean isPlatformInWayFlag() {
        return platformInWayFlag;
    }
}
