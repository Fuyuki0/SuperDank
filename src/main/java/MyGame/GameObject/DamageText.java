package MyGame.GameObject;

import java.util.ArrayList;
import java.util.List;

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
