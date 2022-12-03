package org.platformer.entities.components.enemycomponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import java.util.ArrayList;
import java.util.List;

public abstract class EnemyComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animRun, animHit;

    private int movespeed;
    private List<Entity> leftGroundList = new ArrayList<>();
    private List<Entity> rightGroundList = new ArrayList<>();

    private ReadOnlyBooleanWrapper hasGroundLeftProperty = new ReadOnlyBooleanWrapper(false);
    private ReadOnlyBooleanWrapper hasGroundRightProperty = new ReadOnlyBooleanWrapper(false);

    public abstract void stop();
    public abstract void moving();
    public abstract void hit();

    public List<Entity> getLeftGroundList() {
        return leftGroundList;
    }

    public List<Entity> getRightGroundList() {
        return rightGroundList;
    }

    public boolean hasGroundLeft() {return !leftGroundList.isEmpty();}

    public boolean hasGroundRight() {return !rightGroundList.isEmpty();}

    public void setHasGroundLeft(Boolean hasGroundLeft) {
        this.hasGroundLeftProperty.setValue(hasGroundLeft);
    }

    public void setHasGroundRight(Boolean hasGroundRight) {
        this.hasGroundRightProperty.setValue(hasGroundRight());
    }

    public ReadOnlyBooleanProperty hasGroundLeftProperty() {
        return hasGroundLeftProperty.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty hasGroundRightProperty() {
        return hasGroundRightProperty.getReadOnlyProperty();
    }
}
