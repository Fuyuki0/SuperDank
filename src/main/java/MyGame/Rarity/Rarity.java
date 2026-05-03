package MyGame.Rarity;


import javafx.scene.paint.Color;

public enum Rarity {
    Common(Color.LIGHTGRAY, 1, 1),
    Uncommon(Color.LIMEGREEN, 1.2, 1.2),
    Rare(Color.DODGERBLUE, 1.5, 1.5),
    Epic(Color.PURPLE, 1.8, 1.8),
    Legendary(Color.GOLD, 2, 2.2),
    Abysmal(Color.DARKRED, 2.5, 2.6),
    Divine(Color.WHITE, 2.0, 3.0);

    private Color color;
    private double multiplyBonusWeapon;
    private double multiplyBonusCore;

    Rarity(Color color, double multiplyBonusWeapon, double multiplyBonusCore) {
        this.color = color;
        this.multiplyBonusWeapon = multiplyBonusWeapon;
        this.multiplyBonusCore = multiplyBonusCore;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getMultiplyBonusWeapon() {
        return multiplyBonusWeapon;
    }

    public double getMultiplyBonusCore() {
        return multiplyBonusCore;
    }

    public void setMultiplyBonusCore(double multiplyBonusCore) {
        this.multiplyBonusCore = multiplyBonusCore;
    }

    public void setMultiplyBonusWeapon(double multiplyBonusWeapon) {
        this.multiplyBonusWeapon = multiplyBonusWeapon;
    }
}
