package app;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * This example shows how to add nodes to the scene, how to do event handling,
 * and how to work with animations.
 * 
 * Like every JavaFX application, we start by extending the Application class.
 */
public class Guybrush extends Application {

    /*
     * The sprites we will use to animate the character.
     */
    private static final Image STAND = new Image(Guybrush.class.getResourceAsStream("/resources/stand.png"));
    private static final Image WALK1 = new Image(Guybrush.class.getResourceAsStream("/resources/walk1.png"));
    private static final Image WALK2 = new Image(Guybrush.class.getResourceAsStream("/resources/walk2.png"));
    private static final Image WALK3 = new Image(Guybrush.class.getResourceAsStream("/resources/walk3.png"));
    private static final Image WALK4 = new Image(Guybrush.class.getResourceAsStream("/resources/walk4.png"));
    private static final Image WALK5 = new Image(Guybrush.class.getResourceAsStream("/resources/walk5.png"));
    private static final Image WALK6 = new Image(Guybrush.class.getResourceAsStream("/resources/walk6.png"));
    
    /*
     * The speed (in pixels/second) at which the character moves.
     */
    private static final double SPEED = 120;
    
    private ImageView guybrush;
    private TranslateTransition translation;
    private Timeline spriteAnimation;
    
    /*
     * The start method is the starting point for every JavaFX application.
     * The stage (window) is already created for us. We now have to populate a
     * scene and add it to the stage.
     */
    @Override
    public void start(Stage stage) throws Exception {
        
        // Create a simple group node to use as the root node for the scene.
        // Note that a group doesn't have its own size.
        // It's simply as large as the sum of its children.
        Group root = new Group();
        
        // Create the info text node.
        Text info = new Text("Click a point in the scene to walk there");
        info.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        info.setTextOrigin(VPos.TOP);
        info.setTranslateX(10);
        info.setTranslateY(10);
        
        // Add the text to the root group.
        root.getChildren().add(info);
        
        // Create an image view that shows the current sprite.
        guybrush = new ImageView(STAND);
        
        // Position it at (100,100) within its parent (the root group).
        guybrush.setTranslateX(100);
        guybrush.setTranslateY(100);
        
        // Add it to the root group.
        root.getChildren().add(guybrush);
        
        // Create the sprite animation.
        // This animation will run indefinitely, until it is stopped.
        // It has a keyframe every 8th of a second that swaps out the sprites.
        spriteAnimation = new Timeline();
        spriteAnimation.setCycleCount(Animation.INDEFINITE);
        spriteAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(125), event -> {
            if (guybrush.getImage() == WALK1) {
                guybrush.setImage(WALK2);
            } else if (guybrush.getImage() == WALK2) {
                guybrush.setImage(WALK3);
            } else if (guybrush.getImage() == WALK3) {
                guybrush.setImage(WALK4);
            } else if (guybrush.getImage() == WALK4) {
                guybrush.setImage(WALK5);
            } else if (guybrush.getImage() == WALK5) {
                guybrush.setImage(WALK6);
            } else {
                guybrush.setImage(WALK1);
            }
        }));
        
        // Create a scene with the root group as its root node, and give it a size of 600 by 400.
        Scene scene = new Scene(root, 600, 400);
        
        // Attach an event handler to the scene that makes the character move when the mouse is clicked.
        scene.setOnMouseClicked(event -> {

            // If the character was already moving, stop it.
            if (translation != null) {
                translation.stop();
            }

            // Calculate where the top left corner of the sprite needs to go
            // so that the character's feet end up where the mouse was clicked.
            double targetX = event.getX() - 54;
            double targetY = event.getY() - 150;

            // If the target point is to the left of the character,
            // we need to flip the sprites, if that isn't already the case.
            if (targetX < guybrush.getTranslateX() && guybrush.getTransforms().isEmpty()) {
                guybrush.getTransforms().add(new Scale(-1, 1, guybrush.getBoundsInLocal().getWidth() / 2, 0));
            }

            // If the target point is to the right of the character,
            // we need to unflip the sprites if they were flipped.
            if (targetX > guybrush.getTranslateX() && !guybrush.getTransforms().isEmpty()) {
                guybrush.getTransforms().clear();
            }

            // Find out the distance to the target.
            double distanceX = targetX - guybrush.getTranslateX();
            double distanceY = targetY - guybrush.getTranslateY();
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            // Create the translation animation.
            // This animation will translate the node from its current location to the target location.
            // The distance to the target and the speed of the character are used to calculate the length of the animation.
            // When the animation finishes, the sprite animation is stopped.
            translation = new TranslateTransition();
            translation.setNode(guybrush);
            translation.setFromX(guybrush.getTranslateX());
            translation.setFromY(guybrush.getTranslateY());
            translation.setToX(targetX);
            translation.setToY(targetY);
            translation.setDuration(Duration.seconds(distance / SPEED));
            translation.setOnFinished(actionEvent -> {
                spriteAnimation.stop();
                guybrush.setImage(STAND);
            });

            // Start the sprite animation if it wasn't already running.
            if (spriteAnimation.getStatus() != Animation.Status.RUNNING) {
                spriteAnimation.play();
            }

            // Start the translation animation.
            translation.play();
        });
        
        // Add the scene to the stage, set its title and show it.
        stage.setScene(scene);
        stage.setTitle("Guybrush");
        stage.show();
    }
}
