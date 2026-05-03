package MyGame.Interface;

import javafx.scene.canvas.GraphicsContext;
import MyGame.Game.GameEngine;

public interface Renderable {
    void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine);
}
