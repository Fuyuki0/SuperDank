package MyGame.GameObject.Player;

public class PlayerTrail {
    public double worldPosX;
    public double worldPosY;
    public double sourcePosX;
    public double sourcePosY;
    public double frameWidth;
    public double frameHeight;
    public boolean isFlipped;
    public double opacity;
    public double rotation;

    public PlayerTrail(double PosX, double PosY, double sourceX, double sourceY, double frameWidth, double frameHeight, boolean isFlipped, double rotation) {
        this.worldPosX = PosX;
        this.worldPosY = PosY;
        this.sourcePosX = sourceX;
        this.sourcePosY = sourceY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.isFlipped = isFlipped;
        this.opacity = 0.5;
        this.rotation = rotation;
    }

}
