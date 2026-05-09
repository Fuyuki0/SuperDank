package MyGame.Interface;

import javafx.scene.canvas.GraphicsContext;
import MyGame.Game.GameEngine;

/**
 * An interface for any game object that can be drawn on the screen.
 */
public interface Renderable {
    void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine);
}
