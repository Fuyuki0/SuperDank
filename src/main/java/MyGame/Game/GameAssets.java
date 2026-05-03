package MyGame.Game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameAssets {

    public static final Font FONT_12  = loadFont(12,  false);
    public static final Font FONT_15  = loadFont(15,  false);
    public static final Font FONT_16  = loadFont(16,  false);
    public static final Font FONT_20  = loadFont(20,  false);
    public static final Font FONT_24  = loadFont(24,  false);
    public static final Font FONT_30  = loadFont(30,  false);
    public static final Font FONT_35  = loadFont(35,  false);
    public static final Font FONT_40  = loadFont(40,  false);
    public static final Font FONT_50  = loadFont(50,  false);
    public static final Font FONT_60  = loadFont(60,  false);
    public static final Font FONT_100 = loadFont(100, false);

    public static final Font BOLD_14  = loadFont(14,  true);
    public static final Font BOLD_30  = loadFont(30,  true);
    public static final Font BOLD_35  = loadFont(35,  true);
    public static final Font BOLD_80  = loadFont(80,  true);
    public static final Font BOLD_100 = loadFont(100, true);

    // sprite
    public final Image playerMech;
    public final Image enemyMech;
    public final Image chargerMech;
    public final Image bossMech;

    // tile
    public final Image tile;
    public final WritableImage tileNormal;
    public final WritableImage tileCracked;

    // item
    public final Image props;

    public final Image crystal;
    public final Image gem;

    public final Image arrowImage;
    public final Image boomerangImage;
    public final Image rockImage;
    public final Image lightningImage;
    public final Image lightningImage2;

    public final Image slashImage;
    public final Image crossSlashImage;
    public final Image jumpingSlashImage;
    public final Image swordImage;
    public final Image auraImage;

    public final Image explosionImage;
    public final Image blackholeImage;
    public final Image glitchImage;

    public final Image enteringImage;
    public final Image crackedImage;
    public final Image bossBarImage;

    public final Image[] fireFrames;

    public GameAssets() {

        playerMech  = img("/Sprite/MourningCloak-sprite.png");
        enemyMech   = img("/Sprite/Berserker-sprite.png");
        chargerMech = img("/Sprite/Assassin-sprite.png");
        bossMech    = img("/Sprite/Blackbeard-sprite.png");

        tile = img("/Sprite/neo_zero_tileset_02.png");
        PixelReader reader = tile.getPixelReader();
        tileNormal  = new WritableImage(reader, 0,  480, 48, 48);
        tileCracked = new WritableImage(reader, 96, 480, 48, 48);
        props = img("/Sprite/neo_zero_props_02_free.png");

        crystal = img("/Sprite/crystal_dark_32x32_24f_20d_normal.png");
        gem     = img("/Sprite/gem.png");

        arrowImage     = img("/Sprite/IcePick_64x64.png");
        boomerangImage = img("/Sprite/Shuriken.png");
        rockImage      = img("/Sprite/FireBall_2_64x64.png");
        lightningImage  = img("/Sprite/lightning.png");
        lightningImage2 = img("/Sprite/lightning-2.png");

        slashImage       = img("/Sprite/sprite-sheet.png");
        crossSlashImage  = img("/Sprite/sprite-sheet-2.png");
        jumpingSlashImage = img("/Sprite/sprite-sheet-3.png");
        swordImage       = img("/Sprite/sprite-sheet-4.png");
        auraImage        = img("/Sprite/aura.png");

        explosionImage = img("/Sprite/explosion.png");
        blackholeImage = img("/Sprite/blackhole.png");
        glitchImage    = img("/Sprite/MediumStar_64x64.png");

        enteringImage = img("/Sprite/Entering.png");
        crackedImage  = img("/Sprite/cracked.png");
        bossBarImage  = img("/UI/bossBar.png");

        fireFrames = new Image[30];
        for (int i = 0; i < 30; i++) {
            fireFrames[i] = img(String.format("/fireframes/frame_%03d.png", i));
        }
    }

    // helper
    private Image img(String path) {
        try {
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("fail on img");
            return new Image(getClass().getResource("/Sprite/gem.png").toExternalForm()); // fallback
        }
    }

    private static Font loadFont(double size, boolean bold) {
        try {
            String fontPath = bold ? "/fonts/fontbold.ttf" : "/fonts/font.ttf";
            Font font = Font.loadFont(GameAssets.class.getResourceAsStream(fontPath), size);
            if (font != null) return font;
        } catch (Exception e) {
            System.err.println("fail on font");
        }
        return Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, size);
    }
}
