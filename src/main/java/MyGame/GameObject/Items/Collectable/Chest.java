package MyGame.GameObject.Items.Collectable;

import MyGame.GameObject.GameObject;
import MyGame.GameObject.Player.Player;
import MyGame.Interface.Collectable;
import MyGame.Interface.Renderable;
import MyGame.Game.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import MyGame.Game.GameEngine;

public class Chest extends GameObject implements Collectable, Renderable {
    public Chest(double PosX, double PosY) {
        super(PosX, PosY, 20);
    }

    public void update(double deltaTime) {}

    @Override
    public void onCollect(Player player, World world) {
        MyGame.GameObject.Items.ItemManager.openingChest(world);
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        if (posX - cameraPosX > -margin && posX - cameraPosX < screenWidth + margin && posY - cameraPosY > -margin && posY - cameraPosY < screenHeight + margin) {
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            double shadowWidth = 65;
            double shadowHeight = 15;
            gc.fillOval(posX - cameraPosX - (shadowWidth / 2.0), posY - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);
            double hoverY = Math.sin(System.currentTimeMillis() / 600.0) * 5;
            gc.save();
            gc.translate(posX - cameraPosX, posY - cameraPosY - hoverY);
            double frameWidth = 56;
            double frameHeight = 48;
            double sourceX = 132;
            double sourceY = 200;
            gc.drawImage(
                    engine.getPropsImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
            );
            gc.restore();
        }
    }
}
