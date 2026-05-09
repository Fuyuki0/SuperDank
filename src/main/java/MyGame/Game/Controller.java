package MyGame.Game;

import MyGame.GameObject.Player.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static MyGame.Game.Main.SCREEN_HEIGHT;
import static MyGame.Game.Main.SCREEN_WIDTH;

/**
 * Handles user input from the keyboard or mouse to control the game.
 */
public class Controller {

    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;
    private int totalPressed = 0;

    public void control(Scene scene, World world, Camera camera) {

        scene.setOnKeyPressed(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (!world.isGameStop()) {
                if (code == KeyCode.W)
                    up = true;
                if (code == KeyCode.S)
                    down = true;
                if (code == KeyCode.D)
                    right = true;
                if (code == KeyCode.A)
                    left = true;
                if (code == KeyCode.SHIFT)
                    world.getPlayer().dash();
                if (code == KeyCode.SPACE)
                    world.getPlayer().jump();
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.W)
                up = false;
            if (code == KeyCode.S)
                down = false;
            if (code == KeyCode.D)
                right = false;
            if (code == KeyCode.A)
                left = false;
            if (!world.isGameStop()) {
                if (code == KeyCode.J)
                    world.triggerSlash();
                if (code == KeyCode.E)
                    world.triggerCrossSlash();
                if (code == KeyCode.K)
                    world.triggerCrossSlash();
                if (code == KeyCode.R) {
                    if (!world.isGameStop()) {
                        world.triggerUltimate();
                    }
                }
                if (code == KeyCode.L) {
                    if (!world.isGameStop()) {
                        world.triggerUltimate();
                    }
                }
                if (code == KeyCode.TAB) {
                    if (!world.isGameStop()) {
                        world.setShowMiniMap(!world.isShowMiniMap());
                    }
                }
            }

        });

        scene.setOnMouseClicked(mouseEvent -> {
            MouseButton code = mouseEvent.getButton();
            double mouseX = mouseEvent.getSceneX();
            double mouseY = mouseEvent.getSceneY();

            // map
            double miniMapWidth = 220;
            double miniMapHeight = 220;
            boolean onMiniMap = world.isShowMiniMap()
                    && mouseX >= SCREEN_WIDTH - miniMapWidth - 42
                    && mouseX <= SCREEN_WIDTH - 42
                    && mouseY >= 60 - 20
                    && mouseY <= miniMapHeight + 60 - 20;

            if (onMiniMap) {
                world.setShowMiniMap(false);
                return;
            }

            if (!(mouseX >= SCREEN_WIDTH)) {
                world.setShowMiniMap(true);
            }

            if (!world.isGameStop()) {
                if (code == MouseButton.SECONDARY) {
                    double controllerPosX = mouseX + camera.getPosX();
                    double controllerPosY = mouseY + camera.getPosY();
                    world.getPlayer().dash(controllerPosX, controllerPosY);
                }
                if (code == MouseButton.PRIMARY) {
                    double controllerPosX = mouseX + camera.getPosX();
                    double controllerPosY = mouseY + camera.getPosY();
                    world.triggerSlash(controllerPosX, controllerPosY);
                }
            }
        });

    }

    public void resetInput() {
        for (int i = 0; i < 2; i++) {
            this.up = false;
            this.down = false;
            this.right = false;
            this.left = false;
            this.totalPressed = 0;
        }
    }

    public void playerMovement(Player player, World world) {
        double x = 0;
        double y = 0;
        totalPressed = 0;

        if (!world.isUltimateActive()) {
            if (up) {
                y -= 1;
                totalPressed++;
            }
            if (down) {
                y += 1;
                totalPressed++;
            }
            if (right) {
                x += 1;
                totalPressed++;
            }
            if (left) {
                x -= 1;
                totalPressed++;
            }
            if (totalPressed == 0 && player.getSlashCooldown() < player.getSlashMaxCooldown() - 0.13
                    && player.getCrossSlashCooldown() < 8 - 0.13) {
                player.setCurrentMovementState(Player.MovementState.Idle);
            }
            if (totalPressed > 0 && player.getDashTimer() <= 0
                    && player.getSlashCooldown() < player.getSlashMaxCooldown() - 0.13
                    && player.getCrossSlashCooldown() < 8 - 0.13) {
                player.setCurrentMovementState(Player.MovementState.Run);
            }
            if (totalPressed > 1) {
                if (x != 0 && y != 0) {
                    double f = Math.sqrt(x * x + y * y);
                    x /= f;
                    y /= f;
                }
            }
            player.setVelocity(x, y);
        } else {
            totalPressed = 0;
        }
    }
}
