package MyGame.Game;

import MyGame.GameObject.Player.Player;

public class Camera {
    private double posX;
    private double posY;
    private double Width;
    private double Height;

    public Camera(double Width, double Height) {
        this.Width = Width;
        this.Height = Height;
    }

    public void update(Player player) {
        this.posX = player.getPosX() - (Width / 2);
        this.posY = player.getPosY() - (Height / 2);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }
}
