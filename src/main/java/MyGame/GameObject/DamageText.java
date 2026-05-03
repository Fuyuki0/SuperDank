package MyGame.GameObject;

import MyGame.Game.GameEngine;

import static MyGame.Game.GameAssets.FONT_30;

public class DamageText extends GameObject{

    private double posX;
    private double posY;
    private String text;
    private double timer;
    private double smoothMultiply;
    private final double MAX_TIMER = 0.5;
    private boolean isCrit = false;

    public DamageText(double posX, double posY, double amount, boolean isCrit) {
        super(posX, posY);
        this.posX = posX + (Math.random() * 40 - 20);
        this.posY = posY + (Math.random() * 40 - 20);
        this.text = String.format("%d", (int) amount);
        this.timer = MAX_TIMER;
        this.smoothMultiply = 1;
        this.isCrit = isCrit;
    }

    public DamageText(double posX, double posY, String string, boolean isCrit) {
        super(posX, posY);
        this.posX = posX + (Math.random() * 40 - 20);
        this.posY = posY + (Math.random() * 40 - 20);
        this.text = string;
        this.timer = MAX_TIMER;
        this.smoothMultiply = 1;
        this.isCrit = isCrit;
    }

    public double getDamageTextOpacity() {
        return Math.max(0, timer / MAX_TIMER * smoothMultiply);
    }

    public boolean isFade() {
        return timer <= 0;
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        gc.setFont(FONT_30);
        gc.setGlobalAlpha(this.getDamageTextOpacity());
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeText(this.getText(), this.getPosX() - cameraPosX, this.getPosY() - cameraPosY);
        if (!this.isCrit())
            gc.setFill(javafx.scene.paint.Color.WHITE);
        else
            gc.setFill(javafx.scene.paint.Color.ORANGE);
        gc.fillText(this.getText(), this.getPosX() - cameraPosX, this.getPosY() - cameraPosY);
        gc.setGlobalAlpha(1);
    }

    @Override
    public void update(double deltaTime) {
        this.timer -= deltaTime;
        this.posY -= 10 * deltaTime;
        this.smoothMultiply *= 1.005;
    }

    @Override
    public double getPosX() {
        return posX;
    }

    @Override
    public void setPosX(double posX) {
        this.posX = posX;
    }

    @Override
    public double getPosY() {
        return posY;
    }

    @Override
    public void setPosY(double posY) {
        this.posY = posY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public boolean isCrit() {
        return isCrit;
    }

    public void setCrit(boolean crit) {
        isCrit = crit;
    }

    public double getSmoothMultiply() {
        return smoothMultiply;
    }

    public void setSmoothMultiply(double smoothMultiply) {
        this.smoothMultiply = smoothMultiply;
    }

    public double getMAX_TIMER() {
        return MAX_TIMER;
    }
}
