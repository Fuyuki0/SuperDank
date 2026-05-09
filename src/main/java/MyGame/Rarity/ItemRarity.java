package MyGame.Rarity;

import javafx.scene.paint.Color;

/**
 * Defines the different levels of rarity for items in the game.
 */
public enum ItemRarity {
    Stock(Color.WHITE),
    Modified(Color.DODGERBLUE),
    Prototype(Color.PURPLE),
    Artifact(Color.GOLD),
    Ultimate(Color.RED);

    private final Color color;

    ItemRarity(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
