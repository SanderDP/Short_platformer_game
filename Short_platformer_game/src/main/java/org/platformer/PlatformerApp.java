package org.platformer;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(64 * 16);
        settings.setHeight(40 * 16);
        settings.setTitle("Short platformer game");
        settings.setVersion("0.1");

        //set mode to developer and make developer tools accessible when playing by pressing "1"
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
        settings.setDeveloperMenuEnabled(true);
    }

    private Entity player;

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Stomp") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).stomp();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).shoot();
            }
        }, KeyCode.Z);

        getInput().addAction(new UserAction("getCoordinates") {
            @Override
            protected void onActionBegin() {
                System.out.println(player.getCenter());;
            }
        }, KeyCode.C);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Fruit Collected", 0);
    }

    @Override
    protected void initUI() {
        Text textFruitAmount = new Text();
        textFruitAmount.setTranslateX(50); // x = 50
        textFruitAmount.setTranslateY(100); // y = 100

        textFruitAmount.textProperty().bind(getWorldProperties().intProperty("Fruit Collected").asString());

        getGameScene().addUINode(textFruitAmount); // add to the scene graph

        var fruitTexture = new AnimatedTexture(new AnimationChannel(image("Items/Fruits/Apple.png"), 17, 32, 32, Duration.seconds(10), 0, 16));
        fruitTexture.setTranslateX(50);
        fruitTexture.setTranslateY(100);

        getGameScene().addUINode(fruitTexture);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        setLevelFromMap("tmx/platformer.tmx");

        player = spawn("player", 87, 578);

        getGameScene().setBackgroundRepeat("Background/Blue.png");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, 0, getAppWidth(), getAppHeight() );
        viewport.setZoom(2);
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 700);

        //set collision rule for player and powerupbox
        onCollision(EntityType.PLAYER, EntityType.POWERUPBOX, (player, powerupbox) -> {

            //generate a random sequence with arrow keys
            ArrayList<KeyCode> sequence = new ArrayList<>();
            Random rand = new Random();
            List<KeyCode> inputList = Arrays.asList(KeyCode.UP, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.LEFT);

            int numberOfElements = 8;

            for (int i = 0; i < numberOfElements; i++) {
                int randomIndex = rand.nextInt(inputList.size());
                sequence.add(inputList.get(randomIndex));
            }

            //play triggersequence minigame with generated sequence
            getMiniGameService().startTriggerSequence(sequence, result -> {
                PowerupType pt;
                String message;
                if (result.isSuccess()) {
                    pt = PowerupType.SHOOT;
                    message = "Press \"z\" to shoot!";
                } else {
                    pt = PowerupType.STOMP;
                    message = "Press down to slam down with force!";
                }
                player.getComponent(PlayerComponent.class).addPowerup(pt);
                getNotificationService().pushNotification(message);
            });
            //delete touched powerupbox
            powerupbox.removeFromWorld();
        });

        //set collision rule player and goal
        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.GOAL, (player, goal) -> {
            //show complete level and exit the game
            getDialogService().showMessageBox("Game complete", getGameController()::exit);
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (player.getY() > getAppHeight()) {
            onPlayerDied();
        }
    }

    private void onPlayerDied() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(30, 550));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}