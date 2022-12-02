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
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
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

    private ArrayList<Node> UINodes = new ArrayList<>();

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

    int initialAmountLives = 5;
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Fruit Collected", 98);
        vars.put("Lives", initialAmountLives);
    }

    @Override
    protected void initUI() {

        for (int i = 0; i < getWorldProperties().intProperty("Lives").get(); i++) { // generate amount of full hearts in ai as the same amount of lives set in gameVars
            Texture heartTexture = getAssetLoader().loadTexture("Items/Other/Heart (64x64).png");
            if (i > 0) {
                heartTexture.setTranslateX(66 * i);
            } else {
                heartTexture.setTranslateX(0);
            }
            UINodes.add(heartTexture);
            getGameScene().addUINode(heartTexture); // add the generated full heart node to ui
        }

        var fruitTexture = getAssetLoader().loadTexture("Items/Fruits/Apple (64x64).png");

        fruitTexture.setY(64);
        getGameScene().addUINode(fruitTexture);
        UINodes.add(fruitTexture);

        Text textFruitAmount = new Text();
        textFruitAmount.setFont(new Font(50));
        textFruitAmount.setX(64);
        textFruitAmount.setY(119);

        textFruitAmount.textProperty().bind(getWorldProperties().intProperty("Fruit Collected").asString()); // text in ui shows amount of fruit collected

        getGameScene().addUINode(textFruitAmount);
        UINodes.add(textFruitAmount);
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

        // set collision rule for player and fruit
        onCollision(EntityType.PLAYER, EntityType.FRUIT, (player, fruit) -> {
            var fruitCollected = getWorldProperties().intProperty("Fruit Collected");
            inc("Fruit Collected", +1);

            if (fruitCollected.intValue() >= 100) { //if amount of fruits collected is 100 set it to 0 and add a life
                fruitCollected.set(99); //todo: set back to 0

                var lives = getWorldProperties().intProperty("Lives");

                if (lives.intValue() < initialAmountLives) { // check if replacing already existing UINode or adding one
                    inc("Lives", +1); // increment amount of lives by one
                    var heartTexture = getAssetLoader().loadTexture("Items/Other/Heart (64x64).png");

                    heartTexture.setTranslateX(UINodes.get(lives.get() - 1).getTranslateX()); // set location of new UINode as the location of node it is replacing

                    getGameScene().clearUINodes(); // clear all UINodes

                    UINodes.set(lives.get() - 1,heartTexture); // replace old node with new one on same index in global ArrayList UINodes

                    for (Node n : UINodes) //add all nodes from global Arraylist UINodes to gamescene
                        getGameScene().addUINode(n);
                } else {
                    //todo: add what to do if adding more hearts than initial amount
                }
            }
            fruit.removeFromWorld(); //delete touched fruit
        });

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
            powerupbox.removeFromWorld(); //delete touched powerupbox
        });

        //set collision rule player and goal
        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.GOAL, (player, goal) -> {
            getDialogService().showMessageBox("You win!", getGameController()::exit); //show complete level and exit the game
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (player.getY() > getAppHeight()) {
            onPlayerDied();
        }
    }

    private void onPlayerDied() {
        var lives = getWorldProperties().intProperty("Lives");

        var emptyHeartTexture = getAssetLoader().loadTexture("Items/Other/Heart Empty (64x64).png"); //set texture of new UINode

        emptyHeartTexture.setTranslateX(UINodes.get(lives.get() - 1).getTranslateX()); //set location of new UINode as the location of node it is replacing

        getGameScene().clearUINodes(); //clear all UINodes

        UINodes.set(lives.get() - 1,emptyHeartTexture); // replace old node with new one on same index in global ArrayList UINodes

        for (Node n : UINodes) { //add all nodes from global Arraylist UINodes to gamescene
            getGameScene().addUINode(n);
        }

        inc("Lives", -1); //decrease the amount of lives the player has by one

        if (player != null) {
            if (getWorldProperties().intProperty("Lives").isNotEqualTo(0).get()) //check whether player has lives left
                player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(30, 550)); //respawn player
            else
                getDialogService().showMessageBox("Game Over", getGameController()::exit);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}