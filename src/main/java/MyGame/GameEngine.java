package MyGame;

import MyGame.GameObject.Enemies.Boss;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.*;
import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.PowerUp.EnergyDrink;
import MyGame.GameObject.Items.PowerUp.Steak;
import MyGame.GameObject.Projectile.*;
import MyGame.GameObject.Skill.CrossSlash;
import MyGame.GameObject.Skill.JumpingSlash;
import MyGame.GameObject.Skill.Slash;
import MyGame.GameObject.Weapon.Aura;
import MyGame.GameObject.Weapon.OrbitRock;
import MyGame.GameObject.Weapon.Sword;
import MyGame.GameObject.Weapon.Weapon;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static MyGame.Main.SCREEN_HEIGHT;
import static MyGame.Main.SCREEN_WIDTH;

public class GameEngine {
    private Main.GameState currentGameState;
    private Scene scene;
    private World world;
    private final Controller controller;
    private final Camera camera;
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final ImageView healthFillView;
    private final ImageView overHealFillView;
    private final Label healthLabel;
    private final ImageView staminaFillView;
    private final Label staminaLabel;
    private final ImageView expFillView;
    private final Label levelLabel;
    private final VBox chestUIPopup;
    private final Label itemName;
    private final Label itemRarityLabel;
    private final Label itemDescribe;
    private final Button chestContinueButton;
    private final Button chestSkipButton;
    private final Label killcountlabel;
    private final ImageView killIcon;
    private final Label coinCountLabel;
    private final ImageView coinIcon;
    private final Label timeSurvivedLabel;
    private final VBox topLeftWeaponHang;

    // hp
    private boolean isNowOverHealed = false;

    // another thread
    private boolean chestDelayActive = false;
    private Runnable levelUpCallBack;
    private boolean isLevelUpCallBack = false;
    private Runnable gameOverCallBack;

    private static Font loadCustomFont(double size, boolean bold) {
        try {
            if (!bold) {
                Font customFont = Font.loadFont(GameEngine.class.getResourceAsStream("/fonts/font.ttf"), size);
                if (customFont != null) {
                    return customFont;
                }
            } else {
                Font customBoldFont = Font.loadFont(GameEngine.class.getResourceAsStream("/fonts/fontbold.ttf"), size);
                if (customBoldFont != null) {
                    return customBoldFont;
                }
            }
        } catch (Exception e) {
            System.out.println("Font error");
        }
        return Font.font("Arial", FontWeight.BOLD, size);
    }
    // font load
    public static final Font FONT_12 = loadCustomFont(12, false);
    public static final Font FONT_14 = loadCustomFont(14, false);
    public static final Font FONT_15 = loadCustomFont(15, false);
    public static final Font FONT_16 = loadCustomFont(16, false);
    public static final Font FONT_20 = loadCustomFont(20, false);
    public static final Font FONT_24 = loadCustomFont(24, false);
    public static final Font FONT_30 = loadCustomFont(30, false);
    public static final Font FONT_35 = loadCustomFont(35, false);
    public static final Font FONT_40 = loadCustomFont(40, false);
    public static final Font FONT_50 = loadCustomFont(50, false);
    public static final Font FONT_60 = loadCustomFont(60, false);
    public static final Font FONT_100 = loadCustomFont(100, false);

    public static final Font BOLD_12 = loadCustomFont(12, true);
    public static final Font BOLD_14 = loadCustomFont(14, true);
    public static final Font BOLD_15 = loadCustomFont(15, true);
    public static final Font BOLD_16 = loadCustomFont(16, true);
    public static final Font BOLD_20 = loadCustomFont(20, true);
    public static final Font BOLD_24 = loadCustomFont(24, true);
    public static final Font BOLD_30 = loadCustomFont(30, true);
    public static final Font BOLD_35 = loadCustomFont(35, true);
    public static final Font BOLD_80 = loadCustomFont(80, true);
    public static final Font BOLD_100 = loadCustomFont(100, true);

    // image
    private Image playerMech = new Image(getClass().getResource("/MourningCloak-sprite.png").toExternalForm());
    private List<PlayerTrail> playerTrails = new ArrayList<>();
    private double trailTimer = 0.0;
    private Image enemyMech = new Image(getClass().getResource("/Berserker-sprite.png").toExternalForm());
    private Image chargerMech = new Image(getClass().getResource("/Assassin-sprite.png").toExternalForm());
    private Image bossMech = new Image(getClass().getResource("/Blackbeard-sprite.png").toExternalForm());

    private Image tile = new Image(getClass().getResource("/neo_zero_tileset_02.png").toExternalForm());
    private WritableImage tile_normal;
    private WritableImage tile_craked;
    private ImagePattern pattern;

    private Image props = new Image(getClass().getResource("/neo_zero_props_02_free.png").toExternalForm());

    private Image crystal = new Image(getClass().getResource("/crystal_dark_32x32_24f_20d_normal.png").toExternalForm());
    private double currentCrystalX = 0;
    private double currentCrystalY = 0;
    private double currentCrystalNoZ = 0;

    private Image gem = new Image(getClass().getResource("/gem.png").toExternalForm());

    private Image arrowImage = new Image(getClass().getResource("/IcePick_64x64.png").toExternalForm());
    private Image slashImage = new Image(getClass().getResource("/sprite-sheet.png").toExternalForm());
    private Image crossSlashImage = new Image(getClass().getResource("/sprite-sheet-2.png").toExternalForm());
    private Image jumpingSlashImage = new Image(getClass().getResource("/sprite-sheet-3.png").toExternalForm());
    private Image swordImage = new Image(getClass().getResource("/sprite-sheet-4.png").toExternalForm());
    private Image boomerangImage = new Image(getClass().getResource("/Shuriken.png").toExternalForm());
    private Image rockImage = new Image(getClass().getResource("/FireBall_2_64x64.png").toExternalForm());
    private Image lightningImage = new Image(getClass().getResource("/lightning.png").toExternalForm());
    private Image lightningImage_2 = new Image(getClass().getResource("/lightning-2.png").toExternalForm());
    private Image auraImage = new Image(getClass().getResource("/aura.png").toExternalForm());

    private Image explosionImage = new Image(getClass().getResource("/explosion.png").toExternalForm());
    private Image blackholeImage = new Image(getClass().getResource("/blackhole.png").toExternalForm());
    private Image glitchImage = new Image(getClass().getResource("/MediumStar_64x64.png").toExternalForm());

    private Image enteringImage = new Image(getClass().getResource("/Entering.png").toExternalForm());
    private Image crackedImage = new Image(getClass().getResource("/cracked.png").toExternalForm());
    private double crackedPosX = 0;
    private double crackedPosY = 0;

    private final Image[] fireFrames = new Image[30];
    private Image fireBg;
    private int currentFireBg = 0;
    private double fireBgAnimationTimer = 0.0;

    // effect
    ColorAdjust black = new ColorAdjust();
    ColorAdjust hitFlash = new ColorAdjust();
    ColorAdjust hitPlayer = new ColorAdjust();
    ColorAdjust maxColor = new ColorAdjust();
    ColorAdjust rainbow = new ColorAdjust();

    // animation
    private int lastCoinCount = 0;
    private int lastKillCount = 0;
    private int lastsecond = 0;

    // bmg / sound effect
    private MediaPlayer bgmPlayer;
    private int currentSongIndex = 0;
    private final String[] playlist = {
            "/illusion-of-control.wav",
            "/chrysalis.wav",
            "/Sewerslvt-all-the-joy-In-life-was-gone-once-you-left.wav",
            "/why-as-soon-as-we-become-so-close-we-have-to-say-goodbye.wav"
    };

    public GameEngine(
            Scene scene,
            World world,
            Controller controller,
            Camera camera,
            GraphicsContext gc,
            Canvas canvas,
            ImageView healthFillView,
            ImageView overHealFillView,
            Label healthLabel,
            ImageView staminaFillView,
            Label staminaLabel,
            ImageView expFillView,
            Label levelLabel,
            VBox chestUIPopup,
            Label itemName,
            Label itemRarityLabel,
            Label itemDescribe,
            Button chestContinueButton,
            Button chestSkipButton,
            Label killcountlabel,
            ImageView killIcon,
            Label coinCountLabel,
            ImageView coinIcon,
            Label timeSurvivedLabel,
            VBox topLeftWeaponHang
    ) {
        this.scene = scene;
        this.world = world;
        this.controller = controller;
        this.camera = camera;
        this.gc = gc;
        this.canvas = canvas;
        this.healthFillView = healthFillView;
        this.overHealFillView = overHealFillView;
        this.healthLabel = healthLabel;
        this.staminaFillView = staminaFillView;
        this.staminaLabel = staminaLabel;
        this.expFillView = expFillView;
        this.levelLabel = levelLabel;
        this.chestUIPopup = chestUIPopup;
        this.itemName = itemName;
        this.itemRarityLabel = itemRarityLabel;
        this.itemDescribe = itemDescribe;
        this.chestContinueButton = chestContinueButton;
        this.chestSkipButton = chestSkipButton;
        this.killcountlabel = killcountlabel;
        this.killIcon = killIcon;
        this.coinCountLabel = coinCountLabel;
        this.coinIcon = coinIcon;
        this.timeSurvivedLabel = timeSurvivedLabel;
        this.topLeftWeaponHang = topLeftWeaponHang;

        // tile
        PixelReader reader = tile.getPixelReader();
        tile_normal = new WritableImage(reader, 0, 480, 48, 48);
        tile_craked = new WritableImage(reader, 96, 480, 48, 48);
        double drawTileX = tile_normal.getWidth() * 2 * 10;
        double drawTileY = tile_normal.getHeight() * 2 * 10;
        Canvas hideCanvas = new Canvas(drawTileX, drawTileY);
        GraphicsContext hgc = hideCanvas.getGraphicsContext2D();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Image randomTile = (Math.random() < 0.05) ? tile_craked : tile_normal;
                hgc.drawImage(randomTile, x * tile_normal.getWidth() * 2, y * tile_normal.getHeight() * 2, tile_normal.getWidth() * 2, tile_normal.getHeight() * 2);
            }
        }
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage superChunkImage = hideCanvas.snapshot(parameters, null);
        pattern = new ImagePattern(superChunkImage, 0, 0, drawTileX, drawTileY, false);

        setGameLoop();


        // init sound
        SoundManager.initSounds();


        //effect
        black.setBrightness(-1);
        hitFlash.setBrightness(1);
        gc.save();
        gc.save();
        hitPlayer.setBrightness(1);
        gc.restore();
        hitPlayer.setHue(0.1);
        hitPlayer.setSaturation(5);
        gc.restore();
        maxColor.setSaturation(1.5);
        double hue = (System.currentTimeMillis() % 2000) / 1000.0 - 1;
        rainbow.setHue(hue);
        rainbow.setSaturation(1.2);

        // animation
        for (int i = 0; i < 30; i++) {
            String input = String.format("/fireframes/frame_%03d.png", i);
            try {
                fireFrames[i] = new Image(getClass().getResource(input).toExternalForm());
            } catch (Exception e) {
                System.out.println("error fireframe");
            }
        }


    }

    public void start() {
        loop.start();
        playNextSong();
    }

    public void stop() {
        if (loop != null) loop.stop();
    }


    public void renderGame(double deltaTime) {
        double cameraPosX = camera.getPosX();
        double cameraPosY = camera.getPosY();
        double screenWidth = camera.getWidth();
        double screenHeight = camera.getHeight();
        double mapLimit = world.getPlayer().getMAP_LIMIT();
        double margin = 500;

        double frameWidth = 0;
        double frameHeight = 0;
        double sourceX = 0;
        double sourceY = 0;

        Player player = world.getPlayer();
        List<Enemy> enemies = world.getEnemies();


        if (world.getScreenShakeTimer() > 0) {
            cameraPosX += (Math.random() - 0.5) * world.getScreenShakeIntensity();
            cameraPosY += (Math.random() - 0.5) * world.getScreenShakeIntensity();
        }

        gc.save();
        gc.setFill(Color.rgb(30, 10, 10, 1.0));
        gc.fillRect(0, 0, screenWidth, screenHeight);
        gc.restore();

        gc.save();
        double pulseBG = (Math.sin(System.currentTimeMillis() / 600.0) + 1) / 12.0;
        gc.setFill(Color.rgb(80, 80, 200, Math.min(1,pulseBG)));
        gc.fillRect(0, 0, screenWidth, screenHeight);
        gc.restore();

        gc.save();
        gc.translate(-cameraPosX, -cameraPosY);
        gc.setFill(pattern);
        gc.fillRect(cameraPosX, cameraPosY, screenWidth, screenHeight);
        gc.restore();

        gc.setStroke(Color.PALEVIOLETRED);
        gc.setLineWidth(10);
        gc.strokeRect(-mapLimit - cameraPosX, -mapLimit - cameraPosY, mapLimit * 2, mapLimit * 2);

        gc.setFill(Color.rgb(50, 50, 50));
        gc.setStroke(Color.rgb(30, 30, 30));
        gc.setLineWidth(5);
        for (Obstacle obstacle : world.getObstacles()) {
            margin = 1000;
            if (obstacle.getPosX() - cameraPosX > -margin
                && obstacle.getPosX() - cameraPosX < screenWidth + margin
                && obstacle.getPosY() - cameraPosY > -margin
                && obstacle.getPosY() - cameraPosY < screenHeight + margin
            ) {
                double obstaclePosX = obstacle.getPosX() - cameraPosX;
                double obstaclePosY = obstacle.getPosY() - cameraPosY;
                if (obstaclePosX + obstacle.getWidth() > 0 && obstaclePosX < screenWidth && obstaclePosY + obstacle.getHeight() > 0 && obstaclePosY < screenHeight) {
                    gc.fillRect(obstaclePosX, obstaclePosY, obstacle.getWidth(), obstacle.getHeight());
                    gc.strokeRect(obstaclePosX, obstaclePosY, obstacle.getWidth(), obstacle.getHeight());
                }
            }
        }


        // craked for ultimate
        if (world.isUltimateActive()) {
            if (world.getUltimate().getUltimateTimer() >= 2) {
                frameWidth = 150;
                frameHeight = 150;
                double scaleEnter = 5;
                gc.save();
                gc.setGlobalAlpha(Math.max(0, 1.0 - ((world.getUltimate().getUltimateTimer() - 1.5) / 1.0)));
                gc.translate(player.getPosX() - cameraPosX + 10, player.getPosY() - cameraPosY + 50);
                gc.setEffect(black);
                gc.drawImage(
                        crackedImage,
                        0, 0, frameWidth, frameHeight,
                        -(frameWidth / 2) * scaleEnter, -(frameHeight / 2) * scaleEnter, frameWidth * scaleEnter, frameHeight * scaleEnter
                );
                gc.restore();
                gc.setEffect(null);
            }
        }

        // entering
        if (world.getTimeSurvived() > 1.3 && world.getTimeSurvived() < 3.6) {
            frameWidth = 150;
            frameHeight = 150;
            sourceX = 0;
            sourceY = 0;
            double scaleEnter = 3;
            gc.save();
            gc.setGlobalAlpha(Math.max(0, 1 - world.getTimeSurvived() / 3.4));
            if (world.getTimeSurvived() > 1.3 && world.getTimeSurvived() < 1.35) {
                crackedPosX = player.getPosX();
                crackedPosY = player.getPosY();
            }
            gc.translate(crackedPosX - cameraPosX + 10, crackedPosY - cameraPosY + 50);
            gc.setEffect(black);
            gc.drawImage(
                    crackedImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleEnter, -(frameHeight / 2) * scaleEnter, frameWidth * scaleEnter, frameHeight * scaleEnter
            );
            gc.restore();
        }
        gc.setEffect(null);
        gc.setGlobalAlpha(1);

        if (world.isEntering() && world.getTimeSurvived() > 1.15) {
            frameWidth = 128 * 5;
            frameHeight = 256 * 5;
            sourceX = world.getCurrentEnteringFrame() * frameWidth;
            sourceY = 0;
            double scaleEnter = 1;
            gc.save();
            gc.setGlobalAlpha(0.5);
            gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY - 500);
            gc.drawImage(
                    enteringImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleEnter, -(frameHeight / 2) * scaleEnter, frameWidth * scaleEnter, frameHeight * scaleEnter
            );
            gc.restore();
        }
        gc.setGlobalAlpha(1);

        // telegraph attack
        for (Enemy enemy : enemies) {
            if (enemy instanceof Boss boss) {
                gc.save();
                gc.translate(-cameraPosX, -cameraPosY);
                double maxTelegraphTime = boss.getMaxStateTimer();
                double progress = boss.getStateTimer() / maxTelegraphTime;
                if (progress > 1.0) progress = 1.0;
                if (boss.getCurrentState() == Boss.BossState.TELEGRAPH_CIRCLE) {
                    double maxRadius = 350;
                    double currentRadius = maxRadius * progress;
                    gc.setFill(Color.rgb(200, 0, 0, 0.2));
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    gc.fillOval(boss.getAttackTargetPosX() - maxRadius, boss.getAttackTargetPosY() - maxRadius, maxRadius * 2, maxRadius * 2);
                    gc.strokeOval(boss.getAttackTargetPosX() - maxRadius, boss.getAttackTargetPosY() - maxRadius, maxRadius * 2, maxRadius * 2);
                    gc.setFill(Color.rgb(255, 0, 0, 0.6));
                    gc.fillOval(boss.getAttackTargetPosX() - currentRadius, boss.getAttackTargetPosY() - currentRadius, currentRadius * 2, currentRadius * 2);
                } else if (boss.getCurrentState() == Boss.BossState.TELEGRAPH_ARC) {
                    double maxRadius = 550;
                    double currentRadius = maxRadius * progress;
                    double angle = 90;
                    double counterAngle = -boss.getAttackAngle();
                    double startAngle = counterAngle - (angle / 2);
                    gc.setFill(Color.rgb(200, 0, 0, 0.2));
                    gc.fillArc(boss.getPosX() - maxRadius, boss.getPosY() - maxRadius, maxRadius * 2, maxRadius * 2, startAngle, angle, ArcType.ROUND);
                    gc.setFill(Color.rgb(255, 0, 0, 0.6));
                    gc.fillArc(boss.getPosX() - currentRadius, boss.getPosY() - currentRadius, currentRadius * 2, currentRadius * 2, startAngle, angle, ArcType.ROUND);
                }
                gc.restore();
            }
        }

        // aura
        Aura aura = world.getAura();
        frameWidth = 128;
        frameHeight = 128;
        if (aura != null) {
            double scaleAura = 2.2 + (1 + aura.getBonusSize() / 100) * 2.2;
            if (aura.getCurrentFrame() == 5) {
                sourceX = 0;
                sourceY = 128;
            } else {
                sourceX = aura.getCurrentFrame() * frameWidth;
                sourceY = 0;
            }
            gc.save();
            gc.setGlobalAlpha(0.3);
            gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY);
            gc.drawImage(
                    auraImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleAura, -(frameHeight / 2) * scaleAura, frameWidth * scaleAura, frameHeight * scaleAura
            );
            gc.restore();
        }
        gc.setGlobalAlpha(1);

        // black hole
        frameWidth = 64;
        frameHeight = 64;
        double scaleBlackHole = 4;
        for (double[] blackHole : world.getBlackHoles()) {
            sourceX = (world.getCurrentBlackholeFrame() % 4) * frameWidth;
            sourceY = (world.getCurrentBlackholeFrame() / 4) * frameHeight;
            gc.save();
            gc.translate(blackHole[0] - cameraPosX, blackHole[1] - cameraPosY);
            gc.drawImage(
                    blackholeImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleBlackHole, -(frameHeight / 2) * scaleBlackHole, frameWidth * scaleBlackHole, frameHeight * scaleBlackHole
            );
            gc.restore();
        }


        List<Experience> experiences = world.getExperience();
        frameWidth = 32;
        frameHeight = 32;
        for (Experience experience : experiences) {
            if (experience.getPosX() - cameraPosX > -margin
                    && experience.getPosX() - cameraPosX < screenWidth + margin
                    && experience.getPosY() - cameraPosY > -margin
                    && experience.getPosY() - cameraPosY< screenHeight + margin
            ) {
                gc.setFill(Color.rgb(0, 0, 0, 0.4));
                double shadowWidth = 25;
                double shadowHeight = 10;
                gc.fillOval(experience.getPosX() - cameraPosX - (shadowWidth / 2.0), experience.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);

                gc.save();
                ColorAdjust maxColor = new ColorAdjust();
                maxColor.setSaturation(1.5);
                gc.setEffect(maxColor);
                sourceX = frameWidth * experience.getExpType();
                sourceY = 0;
                double hoverY = Math.sin(System.currentTimeMillis() / 250.0) * 5;
                gc.translate(experience.getPosX() - cameraPosX, experience.getPosY() - cameraPosY - hoverY);
                double hue = (System.currentTimeMillis() % 2000) / 1000.0 - 1;
                if (experience.getExpType() == 4) {
                    ColorAdjust rainbow = new ColorAdjust();
                    rainbow.setHue(hue);
                    rainbow.setSaturation(1.2);
                    gc.setEffect(rainbow);
                    gc.scale(1.5, 1.5);
                }
                gc.drawImage(
                        gem,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
                );
                gc.restore();
            }
        }

        List<Chest> chests = world.getChests();
        frameWidth = 56;
        frameHeight = 48;
        sourceX = 132;
        sourceY = 200;
        for (Chest chest : chests) {
            if (chest.getPosX() - cameraPosX > -margin
                    && chest.getPosX() - cameraPosX < screenWidth + margin
                    && chest.getPosY() - cameraPosY > -margin
                    && chest.getPosY() - cameraPosY < screenHeight + margin
            ) {
                gc.setFill(Color.rgb(0, 0, 0, 0.4));
                double shadowWidth = 65;
                double shadowHeight = 15;
                gc.fillOval(chest.getPosX() - cameraPosX - (shadowWidth / 2.0), chest.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);
                double hoverY = Math.sin(System.currentTimeMillis() / 600.0) * 5;
                gc.save();
                gc.translate(chest.getPosX() - cameraPosX, chest.getPosY() - cameraPosY - hoverY);
                gc.drawImage(
                        props,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
                );
                gc.restore();
            }
        }

        List<Steak> steaks = world.getSteaks();
        frameWidth = 44;
        frameHeight = 40;
        sourceX = 324;
        sourceY = 204;
        for (Steak steak : steaks) {
            if (steak.getPosX() - cameraPosX > -margin
                    && steak.getPosX() - cameraPosX < screenWidth + margin
                    && steak.getPosY() - cameraPosY > -margin
                    && steak.getPosY() - cameraPosY < screenHeight + margin
            ) {
                gc.setFill(Color.rgb(0, 0, 0, 0.4));
                double shadowWidth = 45;
                double shadowHeight = 15;
                gc.fillOval(steak.getPosX() - cameraPosX - (shadowWidth / 2.0), steak.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);

                double hoverY = Math.sin(System.currentTimeMillis() / 400.0) * 5;
                gc.save();
                gc.translate(steak.getPosX() - cameraPosX, steak.getPosY() - cameraPosY - hoverY);
                gc.drawImage(
                        props,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
                );
                gc.restore();
            }
        }

        List<EnergyDrink> energyDrinks = world.getEnergyDrinks();
        frameWidth = 24;
        frameHeight = 48;
        for (EnergyDrink drink : energyDrinks) {
            if (drink.getPosX() - cameraPosX > -margin && drink.getPosX() - cameraPosX < screenWidth + margin
                    && drink.getPosY() - cameraPosY > -margin && drink.getPosY() - cameraPosY < screenHeight + margin) {
                gc.setFill(Color.rgb(0, 0, 0, 0.4));
                double shadowWidth = 30;
                double shadowHeight = 15;
                gc.fillOval(drink.getPosX() - cameraPosX - (shadowWidth / 2.0), drink.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);
                double hoverY = Math.sin(System.currentTimeMillis() / 300.0) * 5;
                if (drink.getDrinkType() == 0) {
                    sourceX = 12 + frameWidth * 2;
                    sourceY = 252;
                }
                else if (drink.getDrinkType() == 1) {
                    sourceX = 12 + frameWidth;
                    sourceY = 252;
                }
                else if (drink.getDrinkType() == 2) {
                    sourceX = 12;
                    sourceY = 252;
                }
                gc.save();
                gc.translate(drink.getPosX() - cameraPosX, drink.getPosY() - cameraPosY - hoverY);
                gc.drawImage(
                        props,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
                );
                gc.restore();
            }
        }

        // explosion
        List<Explosion> explosions = world.getExplosions();
        frameWidth = 64;
        frameHeight = 64;
        double scaleExplosion = 4;
        for (Explosion explosion : explosions) {
            sourceX = world.getCurrentExplosionFrame() * frameWidth;
            sourceY = 0;
            gc.save();
            gc.setGlobalAlpha(explosion.opacity());
            gc.translate(explosion.getPosX() -cameraPosX, explosion.getPosY() - cameraPosY);
            gc.drawImage(
                    explosionImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleExplosion, -(frameHeight / 2) * scaleExplosion, frameWidth * scaleExplosion, frameHeight *scaleExplosion
            );
            gc.restore();
        }
        gc.setGlobalAlpha(1);

        // enemy
        for (Enemy enemy : enemies) {
            enemy.draw(gc, cameraPosX, cameraPosY, margin, screenWidth, screenHeight, hitFlash, enemyMech, chargerMech, bossMech);
        }

        // boss pointer
        for (Enemy enemy : enemies) {
            if (enemy instanceof Boss boss) {
                if (boss.getPosX() - cameraPosX < 0 ||
                    boss.getPosX() - cameraPosX > screenWidth ||
                    boss.getPosY() - cameraPosY < 0 ||
                    boss.getPosY() - cameraPosY > screenHeight
                ) {
                    gc.save();
                    double bossPulse = (Math.sin(System.currentTimeMillis() / 150.0) + 1);
                    gc.setFill(Color.rgb(250, 0, 0, 0.50 + (bossPulse * 0.2)));
                    double paddingX = 60;
                    double paddingY = 100;
                    double pointToX = Math.max(paddingX, Math.min(screenWidth - paddingX, boss.getPosX() - cameraPosX));
                    double pointToY = Math.max(paddingY, Math.min(screenHeight - paddingY, boss.getPosY() - cameraPosY));
                    gc.translate(pointToX, pointToY);
                    double bossAngle = Math.toDegrees(Math.atan2(boss.getPosY() - cameraPosY - pointToY, boss.getPosX() - cameraPosX - pointToX));
                    gc.rotate(bossAngle);
                    double[] XPoint = {-30, 30, -30, -15};
                    double[] YPoint = {30, 0, -30, 0};
                    gc.fillPolygon(XPoint, YPoint, 4);
                    gc.restore();
                }
            }
        }


        // player
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        double shadowWidth = 80;
        double shadowHeight = 20;
        gc.fillOval(player.getPosX() - cameraPosX - (shadowWidth / 2.0), player.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);


        // faceTo
        gc.setFill(Color.CYAN);
        gc.save();
        gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY - player.getPosZ());
        double playerAngle = Math.toDegrees(Math.atan2(player.getFaceToY(), player.getFaceToX()));
        gc.rotate(playerAngle);
        double[] playerXPoint = {40, 60, 40, 45};
        double[] playerYPoint = {10, 0, -10, 0};
        gc.setGlobalAlpha(0.5);
        gc.fillPolygon(playerXPoint, playerYPoint, 4);
        gc.restore();
        gc.setGlobalAlpha(1);

        // crystal
        frameWidth = 32;
        frameHeight = 32;
        sourceX = world.getCurrentCrystalFrame() * 32;
        sourceY = 0;
        double hoverY = Math.sin(System.currentTimeMillis() / 250.0) * 15;
        double crystalTargetX = player.getPosX() - 60;
        double crystalTargetY = player.getPosY() - player.getPosZ() - 80;
        double crystalTargetNoZ = player.getPosY() - 80;
        gc.save();
        currentCrystalX += (crystalTargetX - currentCrystalX) * 0.2;
        currentCrystalY += (crystalTargetY - currentCrystalY) * 0.2;
        currentCrystalNoZ += (crystalTargetNoZ - currentCrystalNoZ) * 0.2;
        gc.translate(currentCrystalX - cameraPosX, currentCrystalY - cameraPosY + hoverY);
        gc.drawImage(
                crystal,
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
        );
        gc.restore();

        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        shadowWidth = 20;
        shadowHeight = 10;
        gc.fillOval(currentCrystalX - cameraPosX - (shadowWidth / 2.0), currentCrystalNoZ - cameraPosY - (shadowHeight / 2.0) + 100, shadowWidth, shadowHeight);

        // frame player

        player.draw(gc, cameraPosX, cameraPosY, playerMech, hitPlayer);

        double offsetX = 10;
        double offsetY = 20;
        double scalePlayer = 1.5;

        for (int i = playerTrails.size() - 1; i >= 0; i--) {
            PlayerTrail trail = playerTrails.get(i);
            trail.opacity -= deltaTime;
            if (trail.opacity <= 0) {
                playerTrails.remove(i);
            } else {
                gc.save();
                gc.setGlobalAlpha(trail.opacity);
                gc.translate(trail.worldPosX - cameraPosX - offsetX, trail.worldPosY - cameraPosY - offsetY);
                if (trail.isFlipped) {
                    gc.scale(-1, 1);
                }
                gc.drawImage(
                        playerMech,
                        trail.sourcePosX, trail.sourcePosY, trail.frameWidth, trail.frameHeight,
                        -(trail.frameWidth * scalePlayer / 2.0), -(trail.frameHeight * scalePlayer / 2.0), trail.frameWidth * scalePlayer, trail.frameHeight * scalePlayer
                );
                gc.restore();
            }
        }
        boolean isFlipped = player.getFaceToX() <= -0.1;
        if (player.getCheckSetFrame() > 0) {
            trailTimer += deltaTime;
            if (player.getCurrentMovementState() == Player.MovementState.Run) {
                if (trailTimer >= 0.2) {
                    playerTrails.add(new PlayerTrail(
                            player.getPosX(), player.getPosY() - player.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped
                    ));
                    trailTimer = 0;
                }
            } else if (player.getCurrentMovementState() == Player.MovementState.Dash || player.getCurrentMovementState() == Player.MovementState.JumpUp || player.getCurrentMovementState() == Player.MovementState.JumpDown) {
                if (trailTimer >= 0.05) {
                    playerTrails.add(new PlayerTrail(
                            player.getPosX(), player.getPosY() - player.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped
                    ));
                    trailTimer = 0;
                }
            } else if (player.getCurrentMovementState() == Player.MovementState.Skill) {
                if (trailTimer >= 0.03) {
                    playerTrails.add(new PlayerTrail(
                            player.getPosX(), player.getPosY() - player.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped
                    ));
                    trailTimer = 0;
                }
            } else if (player.getCurrentMovementState() == Player.MovementState.SkillJump) {
                if (trailTimer >= 0.01) {
                    playerTrails.add(new PlayerTrail(
                            player.getPosX(), player.getPosY() - player.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped
                    ));
                    trailTimer = 0;
                }
            } else if (world.isUltimateActive() && world.isEntering()) {
                if (trailTimer >= 0.005) {
                    playerTrails.add(new PlayerTrail(
                            player.getPosX(), player.getPosY() - player.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped
                    ));
                    trailTimer = 0;
                }
            } else {
                trailTimer = 0;
            }
        }

        // hp bar
        //gc.setFill(Color.LIGHTGRAY);
        //gc.fillRect(player.getPosX() - cameraPosX - 40, player.getPosY() - cameraPosY + 52 - player.getPosZ(), 80, 8);
        //if (!player.hasCurrentHeal()) {
        //    gc.setFill(Color.DARKRED);
        //    gc.fillRect(player.getPosX() - cameraPosX - 40, player.getPosY() - cameraPosY + 52 - player.getPosZ(), 80 * player.healthRatio(), 8);
        //} else {
        //    gc.setFill(Color.DARKRED);
        //    gc.fillRect(player.getPosX() - cameraPosX - 40, player.getPosY() - cameraPosY + 52 - player.getPosZ(), 80 * player.healthOverHealRatio(), 8);
        //    gc.setFill(Color.YELLOW);
        //    gc.fillRect(player.getPosX() - cameraPosX - 40 + (80 * player.healthOverHealRatio()), player.getPosY() - cameraPosY + 52 - player.getPosZ(), 80 * player.overHealRatio(), 8);
        //}
        //gc.setFill(Color.LIGHTGRAY);
        //gc.fillRect(player.getPosX() - cameraPosX - 40, player.getPosY() - cameraPosY + 62 - player.getPosZ(), 80, 5);
        //gc.setFill(Color.DARKCYAN);
        //gc.fillRect(player.getPosX() - cameraPosX - 40, player.getPosY() - cameraPosY + 62 - player.getPosZ(), 80 * player.staminaRatio(), 5);


        // player powerup
        double pulse = (Math.sin(System.currentTimeMillis() / 100.0) + 1) / 2.0;
        if (world.getDrinkShieldTimer() > 0) { // shield
            gc.save();
            gc.setFill(Color.rgb(0, 255, 255, 0.3 + (pulse * 0.2)));
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(4);
            gc.fillOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.strokeOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.restore();
        }
        if (world.getDrinkSpeedTimer() > 0) { // speed
            gc.save();
            gc.setFill(Color.rgb(255, 255, 0, 0.2 + (pulse * 0.1)));
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(2);
            gc.fillOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.strokeOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.restore();
        }
        if (world.getDrinkMagnetTimer() > 0) { // magnet
            gc.save();
            gc.setFill(Color.rgb(255, 0, 255, 0.1 + (pulse * 0.1)));
            gc.setStroke(Color.MAGENTA);
            gc.setLineWidth(2);
            gc.fillOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.strokeOval(player.getPosX() - cameraPosX - 50, player.getPosY() - cameraPosY - 50 - player.getPosZ(), 100, 100);
            gc.restore();
        }

        // damage text
        List<DamageText> damageTexts = world.getDamageTexts();
        gc.setFont(FONT_30);
        for (DamageText damageText : damageTexts) {
            gc.setGlobalAlpha(damageText.getDamageTextOpacity());
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeText(damageText.getText(), damageText.getPosX() - cameraPosX, damageText.getPosY() - cameraPosY);
            if (!damageText.isCrit())
                gc.setFill(Color.WHITE);
            else
                gc.setFill(Color.ORANGE);
            gc.fillText(damageText.getText(), damageText.getPosX() - cameraPosX, damageText.getPosY() - cameraPosY);
        }
        gc.setGlobalAlpha(1);

        // projectiles
        for (Projectile projectile : world.getProjectiles()) {
            projectile.draw(gc, cameraPosX, cameraPosY, this);
        }
        // rocks
        OrbitRock orbitRock = world.getOrbitRock();
        if (orbitRock != null) {
            for (int i = 0; i < orbitRock.getRocks().size(); i++) {
                Rock rock = orbitRock.getRocks().get(i);
                rock.draw(gc, cameraPosX, cameraPosY, this);
            }
        }

        // sword
        Sword sword = world.getSword();
        if (sword != null) {
            frameWidth = 288.0;
            frameHeight = 288.0;
            if (sword.isAttacking() && (!world.getPlayer().isJumping())) {
                double scaleSword = 2.0 + (sword.getBonusSize() / 100.0) * 2.0;
                sourceX =  0 + frameWidth * sword.getCurrentFrame();
                sourceY =  0;
                gc.save();
                gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY - player.getPosZ() - 40);
                gc.rotate(Math.toDegrees(Math.atan2(player.getFaceToY(), player.getFaceToX())));
                gc.translate(100, 0);
                gc.drawImage(
                        swordImage,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth * scaleSword / 2.0), -(frameHeight * scaleSword / 2.0), frameWidth * scaleSword, frameHeight * scaleSword
                );
                gc.restore();
            }
        }

        List<Slash> slashes = world.getSlashes();
        frameWidth = 128 * 6;
        frameHeight = 128 * 6;
        double scaleSlash = 1.0;
        for (Slash slash : slashes) {
            if (slash.getCurrentFrame() == 1) {
                sourceX = frameWidth * 1;
                sourceY = frameHeight * 0;
            } else if (slash.getCurrentFrame() == 2) {
                sourceX = frameWidth * 0;
                sourceY = frameHeight * 1;
            } else if (slash.getCurrentFrame() == 3) {
                sourceX = frameWidth * 0;
                sourceY = frameHeight * 3;
            } else if (slash.getCurrentFrame() == 4) {
                sourceX = frameWidth * 1;
                sourceY = frameHeight * 3;
            } else if (slash.getCurrentFrame() == 5) {
                sourceX = frameWidth * 0;
                sourceY = frameHeight * 4;
            } else if (slash.getCurrentFrame() == 6) {
                sourceX = frameWidth * 1;
                sourceY = frameHeight * 4;
                continue;
            } else {
                sourceX = 0;
                sourceY = 0;
            }
            gc.save();
            gc.setGlobalAlpha(slash.opacity());
            gc.translate(slash.getPosX() - cameraPosX, slash.getPosY() - cameraPosY - slash.getPosZ());
            gc.save();
            gc.rotate(slash.getAngle());
            gc.translate(300, 0);
            gc.rotate(40 + 180);
            gc.drawImage(
                    slashImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth * scaleSlash / 2), -(frameHeight * scaleSlash / 2.0), frameWidth * scaleSlash, frameHeight * scaleSlash
            );
            gc.restore();
            //gc.save();
            //gc.rotate(slash.getAngle());
            //gc.translate(300, 0);
            //gc.rotate(-40 + 180);
            //gc.scale(1, -1);
            //gc.drawImage(
            //        slashImage,
            //        sourceX, sourceY, frameWidth, frameHeight,
            //        -(frameWidth * scaleSlash / 2), -(frameHeight * scaleSlash / 2.0), frameWidth * scaleSlash, frameHeight * scaleSlash
            //);
            //gc.restore();
            gc.restore();
        }
        gc.setGlobalAlpha(1);

        frameWidth = 128 * 6;
        frameHeight = 128 * 6;
        for (CrossSlash crossSlash : world.getCrossSlashes()) {
            sourceX = 0 + frameWidth * (crossSlash.getCurrentFrame() % 2);
            sourceY = 0 + frameHeight * (crossSlash.getCurrentFrame() / 2);
            gc.save();
            gc.setGlobalAlpha(crossSlash.opacity());
            gc.translate(crossSlash.getPosX() - cameraPosX, crossSlash.getPosY() - cameraPosY);
            gc.rotate(Math.toDegrees(crossSlash.getAngle()));

            gc.save();
            gc.rotate(-50);
            gc.drawImage(
                    crossSlashImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2.0), -(frameHeight / 2.0), frameWidth, frameHeight
            );
            gc.restore();

            gc.save();
            gc.scale(1, -1);
            gc.rotate(-50);
            gc.drawImage(
                    crossSlashImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2.0), -(frameHeight / 2.0), frameWidth, frameHeight
            );
            gc.restore();

            gc.restore();
        }
        gc.setGlobalAlpha(1);

        frameWidth = 288;
        frameHeight = 288;
        double scaleJumpingSlash = 1.3;
        List<JumpingSlash> jumpingSlashes = world.getJumpingSlashes();
        for (JumpingSlash jumpingSlash : jumpingSlashes) {
            sourceX = frameWidth * jumpingSlash.getCurrentFrame();
            sourceY = frameHeight;
            gc.save();
            gc.setGlobalAlpha(jumpingSlash.opacity());
            gc.translate(jumpingSlash.getPosX() - cameraPosX, jumpingSlash.getPosY() - cameraPosY - jumpingSlash.getPosZ() + 50);
            gc.drawImage(
                    jumpingSlashImage,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth * scaleJumpingSlash / 2.0), -(frameHeight * scaleJumpingSlash / 2.0), frameWidth * scaleJumpingSlash, frameHeight * scaleJumpingSlash
            );
            gc.restore();
        }
        gc.setGlobalAlpha(1);

        // Mutated brain rainbow effect @>
        double rainbowTimer = world.getRainbowTimer();
        if (rainbowTimer > 0) {
            gc.save();
            gc.setGlobalAlpha(Math.max(0, (rainbowTimer / 0.5) * 0.4));
            double hue = (System.currentTimeMillis() % 500) / 500.0 * 360 * Math.random();
            gc.setFill(Color.hsb(hue, 1, 1));
            gc.fillRect(0, 0, screenWidth, screenHeight);
            gc.restore();
            gc.setGlobalAlpha(1);
        }
        double repulsorTimer = world.getRepulsorVisualTimer();
        if (repulsorTimer > 0) {
            gc.save();
            double progress = 1.0 - (repulsorTimer / 0.4);
            double radius = progress * 400;
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(15 * (1.0 - progress));
            gc.strokeOval(player.getPosX() - cameraPosX - radius, player.getPosY() - cameraPosY - radius, radius * 2, radius * 2);
            gc.restore();
        }
        double gasTimer = world.getGasVisualTimer();
        if (gasTimer > 0) {
            gc.save();
            double opacity = gasTimer / 0.8;
            gc.setFill(Color.rgb(50, 200, 50, opacity * 0.3));
            gc.fillRect(0, 0, screenWidth, screenHeight);
            gc.restore();
        }
        if (player.healthRatio() <= 0.2 && player.getCurrentHealth() > 0) {
            gc.save();
            pulse = (Math.sin(System.currentTimeMillis() / 150.0) + 1) / 2.0;
            gc.setFill(Color.rgb(150, 0, 0, 0.05 + (pulse * 0.2)));
            gc.fillRect(0, 0, screenWidth, screenHeight);
            gc.restore();
        }

        // final weapon glitch
        if (player.isHasFinalWeapon()) {
            gc.save();
            gc.setGlobalAlpha(0.25);
            for(int i = 0; i < 8; i++) {
                gc.setFill(Color.hsb(Math.random() * 360, 1.0, 1.0));
                gc.setGlobalAlpha(0.05);
                gc.fillRect(0, Math.random() * screenHeight, screenWidth, 5 + Math.random() * 25);
            }
            List<double[]> glitches = world.getGlitchStrikes();
            frameWidth = 64;
            frameHeight = 64;
            for (double[] glitch : glitches) {
                double glitchX = glitch[0] - cameraPosX;
                double glitchY = glitch[1] - cameraPosY;
                double radius = glitch[2];
                double timer = glitch[3];
                double opacity = Math.max(0, timer / 0.3);
                gc.setGlobalAlpha(opacity);
                sourceX = world.getCurrentFinalWeaponFrame() * frameWidth;
                sourceY = 0;
                double scaleGlitch = radius / 60.0;
                gc.save();
                gc.translate(glitchX, glitchY);
                gc.drawImage(
                        glitchImage,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth * scaleGlitch / 2.0), -(frameHeight * scaleGlitch / 2.0), frameWidth * scaleGlitch, frameHeight * scaleGlitch
                );
                gc.restore();
            }
            gc.restore();
            gc.setGlobalAlpha(1.0);
        }


        // ultimate
        if (world.isUltimateActive()) {
            double ultimateTimer = world.getUltimate().getUltimateTimer();
            gc.save();
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillRect(0, 0, screenWidth, screenHeight);
            if (ultimateTimer < 2.0) {
                gc.setStroke(Color.rgb(150, 0, 90, 0.4));
                gc.setLineWidth(15);
                double radius = 1200 - (ultimateTimer / 1.5) * 1200;
                gc.strokeOval(player.getPosX() - cameraPosX - radius, player.getPosY() - cameraPosY - radius, radius * 2, radius * 2);
            }

            if (ultimateTimer >= 2.0) {
                if (ultimateTimer < 2.1) {
                    gc.setFill(Color.rgb(255, 255, 255, 0.9));
                    gc.fillRect(0, 0, screenWidth, screenHeight);
                }
            }
            gc.restore();


            if (ultimateTimer > 2.0 && ultimateTimer < 2.3) {
                frameWidth = 128 * 5;
                frameHeight = 256 * 5;
                sourceX = world.getCurrentEnteringFrame() * frameWidth;
                sourceY = 0;
                double scaleEnter = 1;
                gc.save();
                gc.setGlobalAlpha(0.3);
                gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY - 500);
                gc.drawImage(
                        enteringImage,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2) * scaleEnter, -(frameHeight / 2) * scaleEnter, frameWidth * scaleEnter, frameHeight * scaleEnter
                );
            }
            gc.restore();
            gc.setGlobalAlpha(1);
        }


        //========== Skill ============
        double skillSize = 120;
        double spacing = 40;

        double startPosX = SCREEN_WIDTH - (skillSize * 3) - spacing * 2 - 40;
        double startPosY = SCREEN_HEIGHT - skillSize - 20;
        double moveStartPosX = 120;

        for (int i = 0; i < 2; i++) {
            double skillPosX = startPosX + ((skillSize + spacing) * i);
            double skillPosY = startPosY - 150;
            gc.setFill(Color.rgb(40, 40, 40, 0.7));
            gc.fillOval(skillPosX, skillPosY, skillSize, skillSize);
            if (i == 0) {
                gc.setFill(Color.WHITE);
                gc.fillOval(skillPosX + 10, skillPosY + 10, skillSize - 20, skillSize - 20);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(10);
                double cooldownRatio = player.dashCooldownRatio();
                if (cooldownRatio > 0) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillArc(skillPosX, skillPosY, skillSize, skillSize, 90, cooldownRatio * -360, ArcType.ROUND);
                }
                if (player.getCurrentStamina() < 15) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, skillPosY, skillSize, skillSize);
                }
            } else if (i == 1) {
                gc.setFill(Color.WHITE);
                gc.fillOval(skillPosX + 10, skillPosY + 10, skillSize - 20, skillSize - 20);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(10);
                double cooldownRatio = player.jumpCooldownRatio();
                if (cooldownRatio > 0) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillArc(skillPosX, skillPosY, skillSize, skillSize, 90, cooldownRatio * -360, ArcType.ROUND);
                }
                if (player.isJumping() || player.getCurrentStamina() < 25) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, skillPosY, skillSize, skillSize);
                }
            }
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(skillPosX, skillPosY, skillSize, skillSize);
            gc.setFill(Color.WHITE);
            gc.setFont(FONT_15);
            if (i == 0) gc.fillText("[SHIFT / R-Click]", skillPosX - 15, skillPosY - 10);
            else if (i == 1) gc.fillText("[SPACE]", skillPosX + 25, skillPosY - 10);
        }


        for (int i = 0; i < 3; i++) {
            double skillPosX = startPosX + ((skillSize + spacing) * i);
            gc.setFill(Color.rgb(40, 40, 40, 0.7));
            gc.fillOval(skillPosX, startPosY, skillSize, skillSize);
            if (i == 0) {
                gc.setFill(Color.WHITE);
                gc.fillOval(skillPosX + 10, startPosY + 10, skillSize - 20, skillSize - 20);
                double cooldownRatio = player.slashCooldownRatio();
                if (cooldownRatio > 0) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillArc(skillPosX, startPosY, skillSize, skillSize, 90, cooldownRatio * -360, ArcType.ROUND);
                }
                if (player.getCurrentStamina() < 10) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, startPosY, skillSize, skillSize);
                }
            } else if (i == 1) {
                gc.setFill(Color.WHITE);
                gc.fillOval(skillPosX + 10, startPosY + 10, skillSize - 20, skillSize - 20);
                double cooldownRatio = player.crossSlashCooldownRatio();
                if (cooldownRatio > 0) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillArc(skillPosX, startPosY, skillSize, skillSize, 90, cooldownRatio * -360, ArcType.ROUND);
                }
                if (player.isJumping()) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, startPosY, skillSize, skillSize);
                } else if (player.getCurrentStamina() < 60) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, startPosY, skillSize, skillSize);
                }
            } else if (i == 2) {
                gc.setFill(Color.WHITE);
                gc.fillOval(skillPosX + 10, startPosY + 10, skillSize - 20, skillSize - 20);
                double cooldownRatio = player.ultimateCooldownRatio();
                if (cooldownRatio > 0) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillArc(skillPosX, startPosY, skillSize, skillSize, 90, cooldownRatio * -360, ArcType.ROUND);
                }
                if (player.getCurrentStamina() < 100) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.7));
                    gc.fillOval(skillPosX, startPosY, skillSize, skillSize);
                }
            }
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(skillPosX, startPosY, skillSize, skillSize);
            gc.setFill(Color.WHITE);
            gc.setFont(FONT_15);
            if (i == 0) gc.fillText("[L-Click / J]", skillPosX + 5, startPosY - 10);
            else if (i == 1) gc.fillText("[E / K]", skillPosX + 5, startPosY - 10);
            else gc.fillText("[R / L]", skillPosX + 5, startPosY - 10);
        }


        // boss hp
        for (Enemy enemy : enemies) {
            Image bossBarImg = new Image(getClass().getResource("/bossBar.png").toExternalForm());
            ImageView bossBarView = new ImageView(bossBarImg);
            bossBarView.setTranslateY(10);
            if (enemy instanceof Boss boss) {
                double barWidth = 550;
                double barHeight = 32;
                double barPosX = (screenWidth - barWidth) / 2;
                double barPosY = 150;
                double hpRatio = boss.getHealth() / boss.getMaxHealth();
                sourceX = 0;
                sourceY = 0;
                frameWidth = 683;
                frameHeight = 52;
                gc.setFill(Color.rgb(50, 0, 0, 0.6));
                gc.fillRect(barPosX, barPosY, barWidth, barHeight);
                gc.setFill(Color.rgb(100, 10, 10));
                gc.fillRect(barPosX, barPosY, barWidth * hpRatio, barHeight);
                gc.setFill(Color.rgb(150, 40, 10));
                gc.fillRect(barPosX, barPosY, barWidth * hpRatio, barHeight / 2 - 9 );
                gc.setFill(Color.rgb(50, 10, 10));
                gc.fillRect(barPosX, barPosY + 23, barWidth * hpRatio, barHeight - 23);
                gc.save();
                gc.translate(barPosX + 275, barPosY + 15);
                gc.drawImage(
                        bossBarImg,
                        sourceX, sourceY, frameWidth, frameHeight,
                        -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
                );
                gc.restore();
                gc.setFont(FONT_24);
                gc.setFill(Color.RED);
                gc.fillText("BOSS", barPosX + (barWidth / 2) - 35, barPosY - 15);

                gc.setFont(FONT_16);
                gc.setFill(Color.WHITE);
                gc.save();
                gc.fillText((int)boss.getHealth() + " / " + (int)boss.getMaxHealth(), barPosX + (barWidth / 2) - 60, barPosY + 20);
                gc.restore();
            }
        }




        //=============== Minimap ===============
        if (world.isShowMiniMap()) {

            topLeftWeaponHang.setVisible(true);

            double miniMapWidth = 200 + 20;
            double miniMapHeight = 200 + 20;
            double miniMapPosX = screenWidth - miniMapWidth - 40;
            double miniMapPosY = 60;
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(miniMapPosX, miniMapPosY, miniMapWidth, miniMapHeight);
            // clipping
            gc.save();
            gc.beginPath();
            gc.rect(miniMapPosX, miniMapPosY, miniMapWidth, miniMapHeight);
            gc.clip();

            // x4 zoom
            double viewDistanceX = screenWidth;
            double viewDistanceY = screenHeight;

            gc.setFill(Color.DARKGRAY);
            for (Obstacle obstacle : world.getObstacles()) {
                viewDistanceX = screenWidth;
                viewDistanceY = screenHeight;
                double distanceX = obstacle.getPosX() - player.getPosX();
                double distanceY = obstacle.getPosY() - player.getPosY();
                if ((distanceX <= viewDistanceX * 2 && distanceX + obstacle.getWidth() >= -viewDistanceX * 2) &&
                    (distanceY <= viewDistanceY * 2 && distanceY + obstacle.getHeight() >= -viewDistanceY * 2)) {
                    double scaleX = miniMapWidth / (viewDistanceX * 4);
                    double scaleY = miniMapHeight / (viewDistanceY * 4);
                    gc.fillRect(miniMapPosX + (miniMapWidth / 2) + (distanceX * scaleX),
                              miniMapPosY + (miniMapHeight / 2) + (distanceY * scaleY),
                            Math.max(2, obstacle.getWidth() * scaleX),
                            Math.max(2, obstacle.getHeight() * scaleY)
                    );
                }
            }

            gc.setFill(Color.ORANGE);
            for (Chest chest : world.getChests()) {
                double distanceX = chest.getPosX() - player.getPosX();
                double distanceY = chest.getPosY() - player.getPosY();
                if ((distanceX <= viewDistanceX * 2 && distanceX >= -viewDistanceX * 2) &&
                        (distanceY <= viewDistanceY * 2 && distanceY >= -viewDistanceY * 2)) {
                    gc.fillRect(miniMapPosX + (miniMapWidth / 2) + (distanceX * (miniMapWidth / (viewDistanceX * 4)) - 3),
                            miniMapPosY + (miniMapHeight / 2) + (distanceY * (miniMapHeight / (viewDistanceY * 4)) - 3),
                            6,
                            6
                    );
                }
            }
            gc.setFill(Color.SADDLEBROWN);
            for (Steak steak : world.getSteaks()) {
                double distanceX = steak.getPosX() - player.getPosX();
                double distanceY = steak.getPosY() - player.getPosY();
                if ((distanceX <= viewDistanceX * 2 && distanceX >= -viewDistanceX * 2) &&
                        (distanceY <= viewDistanceY * 2 && distanceY >= -viewDistanceY * 2)) {
                    gc.fillRect(miniMapPosX + (miniMapWidth / 2) + (distanceX * (miniMapWidth / (viewDistanceX * 4)) - 2),
                            miniMapPosY + (miniMapHeight / 2) + (distanceY * (miniMapHeight / (viewDistanceY * 4)) - 2),
                            4,
                            4
                    );
                }
            }
            gc.setFill(Color.RED);
            for (Enemy enemy : world.getEnemies()) {
                if (enemy instanceof Boss ) {
                    Boss boss = (Boss) enemy;
                    double distanceX = boss.getPosX() - player.getPosX();
                    double distanceY = boss.getPosY() - player.getPosY();
                    if ((distanceX <= viewDistanceX * 2 && distanceX >= -viewDistanceX * 2) &&
                            (distanceY <= viewDistanceY * 2 && distanceY >= -viewDistanceY * 2)) {
                        gc.fillRect(miniMapPosX + (miniMapWidth / 2) + (distanceX * (miniMapWidth / (viewDistanceX * 4)) - 2),
                                miniMapPosY + (miniMapHeight / 2) + (distanceY * (miniMapHeight / (viewDistanceY * 4)) - 2),
                                8,
                                8
                        );
                    }
                }
            }
            gc.setStroke(Color.PALEVIOLETRED);
            gc.setLineWidth(10);
            gc.strokeRect(-mapLimit - cameraPosX, -mapLimit - cameraPosY, mapLimit * 2, mapLimit * 2);

            // player
            gc.setFill(Color.RED);
            gc.fillRect(miniMapPosX + (miniMapWidth / 2) - 3, miniMapPosY + (miniMapHeight / 2) - 3, 6, 6);

            gc.restore();

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(miniMapPosX, miniMapPosY, miniMapWidth, miniMapHeight);
            gc.setFill(Color.WHITE);
            gc.setFont(FONT_20);
            gc.fillText("[TAB]", miniMapPosX + 85, miniMapPosY + 250);


        } else {

            topLeftWeaponHang.setVisible(false);

            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(0, 0, screenWidth, screenHeight);

            // stat
            double panelWidth = 600;
            double panelPosX = 100;
            double panelPosY = 150;
            double panelHeight = screenHeight - 300;
            gc.setFill(Color.rgb(20, 20, 20, 0.6));
            gc.fillRect(panelPosX, panelPosY, panelWidth, panelHeight);
            gc.setStroke(Color.ORANGE);
            gc.setLineWidth(4);
            gc.strokeRect(panelPosX, panelPosY, panelWidth, panelHeight);
            gc.setFill(Color.GOLD);
            gc.setFont(FONT_35);
            gc.fillText("Player Stats", panelPosX + 80, panelPosY + 60);
            gc.setFill(Color.WHITE);
            gc.setFont(FONT_20);
            double statPosY = panelPosY + 120;
            double statPosX = panelPosX + 40;
            double padding = 40;
            gc.fillText("Max Health: " + player.getMaxHealth(), statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Max Stamina: " + player.getMaxStamina(), statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Speed: " + String.format("%.2f", player.getMAX_SPEED() * player.getSpeedMultiplier()), statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Max Overheal: " + player.getMaxOverHeal(), statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Regenaration: " + player.getRegeneration(), statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Lifesteal: " + String.format("%.2f", player.getLifeSteal() * 100) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Exp Multi: " + String.format("%.2f", player.getExpMultiplier() * 100) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Luck: " + String.format("%.2f", player.getLuck() * 100) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Difficulty: " + String.format("%.2f", world.getDifficultyMultiplier() * 100) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Coin Multi: " + String.format("%.2f", player.getCoinMultiplier() * 100) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Cooldown: -" + String.format("%.2f", player.getSlashCooldownMultiplier()) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Damage: " + String.format("%.2f", world.getPlayer().getStatDamage()) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Atk Speed: " + String.format("%.2f", world.getPlayer().getStatAtkSpeed()) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Crit Dmg: " + String.format("%.2f", world.getPlayer().getStatCritDamage()) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Crit Rate: " + String.format("%.2f", world.getPlayer().getStatCritRate()) + "%", statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Proj Count: " + String.format("%.2f", world.getPlayer().getStatProjCount()) , statPosX, statPosY);
            statPosY += padding;
            gc.fillText("Size Multi: " + String.format("%.2f", world.getPlayer().getStatSize()) + "%", statPosX, statPosY);
            statPosY += padding;

            double mapSize = (screenHeight - 40) * 0.8;
            double mapPosX = (screenWidth - mapSize) / 2 + 400;
            double mapPosY = (screenHeight - mapSize) / 2;
            gc.setFill(Color.rgb(40, 40, 40, 0.6));
            gc.fillRect(mapPosX, mapPosY, mapSize, mapSize);
            // clipping
            gc.save();
            gc.beginPath();
            gc.rect(mapPosX, mapPosY, mapSize, mapSize);
            gc.clip();

            gc.setFill(Color.DARKGRAY);
            for (Obstacle obstacle : world.getObstacles()) {
                double obstacleMapPosX = mapPosX + (mapSize / 2) + (obstacle.getPosX() * (mapSize / mapLimit / 2));
                double obstacleMapPosY = mapPosY + (mapSize / 2) + (obstacle.getPosY() * (mapSize / mapLimit / 2));

                double obstacleMapWidth = obstacle.getWidth() * (mapSize / mapLimit / 2);
                double obstacleMapHeight = obstacle.getHeight() * (mapSize / mapLimit / 2);

                gc.fillRect(obstacleMapPosX, obstacleMapPosY, obstacleMapWidth, obstacleMapHeight);
            }

            double mapLimitWidth = mapLimit * 2;
            double playerMapPosX = mapPosX + (mapSize / 2) + (player.getPosX() * (mapSize / mapLimitWidth));
            double playerMapPosY = mapPosY + (mapSize / 2) + (player.getPosY() * (mapSize / mapLimitWidth));
            gc.setFill(Color.RED);
            gc.fillOval(playerMapPosX - 5, playerMapPosY - 5, 10, 10);

            gc.restore();

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeRect(mapPosX, mapPosY, mapSize, mapSize);
        }

    }

    // sort UI
    public void updateLeftPanelUI() {
        topLeftWeaponHang.getChildren().clear();

        for (Weapon w : world.getWeaponList()) {
            if (!w.isCore()) {
                Label label = new Label("Lvl " + w.getLevel() + " - " + w.getName());
                label.setTextFill(Color.WHITE);
                label.setFont(FONT_16);
                label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 5; -fx-background-radius: 5;");
                topLeftWeaponHang.getChildren().add(label);
            }
        }

        for (Weapon w : world.getWeaponList()) {
            if (w.isCore()) {
                Label label = new Label("Lvl " + w.getLevel() + " - " + w.getName());
                label.setTextFill(Color.LIGHTBLUE);
                label.setFont(FONT_16);
                label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 5; -fx-background-radius: 5;");
                topLeftWeaponHang.getChildren().add(label);
            }
        }

        int activeItemCount = Math.min(world.getItemList().size(), world.getPlayer().getMaxItemSlot());
        for (int i = 0; i < activeItemCount; i++) {
            Item it = world.getItemList().get(i);
            Label label = new Label("Equipped - " + it.getName());
            label.setTextFill(Color.ORANGE);
            label.setFont(FONT_16);
            label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 5; -fx-background-radius: 5;");
            topLeftWeaponHang.getChildren().add(label);
        }
        topLeftWeaponHang.setTranslateX(30);

    }





    // animation
    private void popAnimation(Node uiElement) {
        ScaleTransition pop = new ScaleTransition(Duration.seconds(0.1), uiElement);

        pop.setToX(1.3);
        pop.setToY(1.3);
        pop.setAutoReverse(true);
        pop.setCycleCount(2);
        pop.setOnFinished(event -> {
            uiElement.setScaleX(1.0);
            uiElement.setScaleY(1.0);
        });
        pop.play();
    }





    //=================== Timer =========================

    public AnimationTimer loop;

    public void setGameLoop() {

        loop = new AnimationTimer() {
            long time = System.nanoTime();



            @Override
            public void handle(long l) {
                double deltaTime = (l - time) / 1000000000.0;
                time = l;


                if (deltaTime > 0.1) deltaTime = 0.1;


                fireBgAnimationTimer += deltaTime;
                if (fireBgAnimationTimer > 0.01) {
                    currentFireBg++;
                    if (currentFireBg >= 30) {
                        currentFireBg = 0;
                    }
                    fireBgAnimationTimer = 0;
                }


                // engine start
                if (currentGameState == Main.GameState.Play) {


                    // reset UI
                    if (world.isUiNeedsUpdate()) {
                        updateLeftPanelUI();
                        world.setUiNeedsUpdate(false);
                    }


                    // gameover
                    if (world.getPlayer().getCurrentHealth() <= 0 && !world.isGameStop()) {
                        world.setGameStop(true);
                        if (gameOverCallBack != null) {
                            gameOverCallBack.run();
                        }
                        return;
                    }


                    // run all pending level
                    if (!world.isGameStop() && world.getPlayer().getPendingLevelup() > 0) {
                        world.getPlayer().claimLevelUp();
                        if (levelUpCallBack != null) {
                            levelUpCallBack.run();
                        }
                        isLevelUpCallBack = true;
                        return;
                    }


                    // chestget

                    if (world.isGameStop()) {
                        coinCountLabel.setText(String.format("%d", world.getPlayer().getCoin()));
                        if (world.getItem() != null && !chestUIPopup.isVisible() && !isLevelUpCallBack) {

                            if (!chestDelayActive) {
                                chestDelayActive = true;
                                Item item = world.getItem();
                                chestUIPopup.setVisible(true);
                                itemName.setText("Got: " + world.getItem().getName() + "!");
                                itemDescribe.setText(world.getItem().getDescription());
                                itemRarityLabel.setText(world.getItem().getItemRarity().toString() + " Tier");
                                itemRarityLabel.setTextFill(world.getItem().getItemRarity().getColor());
                                chestContinueButton.setDisable(true);
                                chestSkipButton.setDisable(true);
                                chestContinueButton.setText("WAIT...");
                                chestSkipButton.setText("WATI...");

                                PauseTransition chestDelay = new PauseTransition(Duration.seconds(0.5));
                                chestDelay.setOnFinished(actionEvent -> {
                                    chestContinueButton.setDisable(false);
                                    chestSkipButton.setDisable(false);
                                    chestContinueButton.setText("CONTINUE");
                                    chestSkipButton.setText("SKIP");
                                    chestContinueButton.requestFocus();
                                });
                                chestDelay.play();

                                chestContinueButton.setOnAction(event -> {
                                    chestContinueButton.setDisable(true);
                                    chestSkipButton.setDisable(true);
                                    if (item != null) {
                                        if (item.getName().equals("Coins")) {
                                            item.applyBuff(world);
                                        } else {
                                            world.addItem(item);
                                            if (world.getItemList().size() <= world.getPlayer().getMaxItemSlot()) {
                                                item.applyBuff(world);
                                                world.setUiNeedsUpdate(true);
                                            }
                                        }
                                    }
                                    world.setItem(null);
                                    world.resumeGame();
                                    chestUIPopup.setVisible(false);
                                    chestDelayActive = false;
                                });
                                chestSkipButton.setOnAction(event -> {
                                    chestContinueButton.setDisable(true);
                                    chestSkipButton.setDisable(true);
                                    world.setItem(null);
                                    world.resumeGame();
                                    chestUIPopup.setVisible(false);
                                    chestDelayActive = false;
                                });
                            }
                        }


                    // update world + UI

                    } else {
                        Player player = world.getPlayer();
                        controller.playerMovement(player, world);
                        world.update(deltaTime);
                        camera.update(player);

                        double healthRatio = player.healthRatio();
                        if (healthRatio > 0) {
                            healthFillView.setVisible(true);
                            double healthFullWidth = 450;
                            double healthHeight = healthFillView.getImage().getHeight();
                            healthFillView.setClip(new Rectangle(healthFullWidth * healthRatio, healthHeight));
                        } else {
                            healthFillView.setVisible(false);
                        }

                        if (player.hasCurrentHeal()) {
                            overHealFillView.setVisible(true);
                            double overHealRatio = player.overHealRatio();
                            double overHealFullWidth = 450;
                            double overHealHeight = overHealFillView.getImage().getHeight();
                            overHealFillView.setClip(new Rectangle(overHealFullWidth * overHealRatio, overHealHeight));
                        } else {
                            overHealFillView.setVisible(false);
                        }
                        double staminaRatio = player.staminaRatio();
                        if (staminaRatio > 0) {
                            staminaFillView.setVisible(true);
                            double staminaFullWidth = 430;
                            double staminaHeight = staminaFillView.getImage().getHeight();
                            staminaFillView.setClip(new Rectangle(staminaFullWidth * staminaRatio, staminaHeight));
                        } else {
                            staminaFillView.setVisible(false);
                        }

                        healthLabel.setText("HP: " + (int) (player.getCurrentHealth() + player.getCurrentOverHeal()) + " / " + (int) player.getMaxHealth());
                        staminaLabel.setText("STA: " + (int) player.getCurrentStamina() + " / " + (int) player.getMaxStamina());

                        double expRatio = player.getExpRatio();

                        if (expRatio > 0) {
                            expFillView.setVisible(true);
                            double expFullWidth = 1920;
                            double expHeight = expFillView.getImage().getHeight();
                            expFillView.setClip(new Rectangle(expFullWidth * expRatio, expHeight));
                        } else {
                            expFillView.setVisible(false);
                        }
                        levelLabel.setText("Level: " + player.getPlayerLevel());

                        if (world.getPlayer().getEnemiesKilled() > lastKillCount) {
                            killcountlabel.setText(String.format("%d", player.getEnemiesKilled()));
                            popAnimation(killcountlabel);
                            popAnimation(killIcon);
                            lastKillCount = world.getPlayer().getEnemiesKilled();
                        }
                        if (world.getPlayer().getCoin() > lastCoinCount) {
                            coinCountLabel.setText(String.format("%d", player.getCoin()));
                            popAnimation(coinCountLabel);
                            popAnimation(coinIcon);
                            lastCoinCount = world.getPlayer().getCoin();
                        }
                        int totalSecond = (int) world.getTimeSurvived();
                        int getMinutes = totalSecond / 60;
                        int getSeconds = totalSecond % 60;
                        timeSurvivedLabel.setText(String.format("%02d:%02d", getMinutes, getSeconds));
                        if (getSeconds > lastsecond) {
                            popAnimation(timeSurvivedLabel);
                            lastsecond = getSeconds;
                        }
                    }

                    renderGame(deltaTime);

                } else if (currentGameState == Main.GameState.Menu) {

                    fireBg = fireFrames[currentFireBg];
                    gc.save();
                    gc.drawImage(
                            fireBg,
                            0, 0, 320, 180,
                            0, 0, SCREEN_WIDTH, SCREEN_HEIGHT
                    );
                    gc.restore();
                } else if (currentGameState == Main.GameState.Setting) {
                    gc.setFill(Color.rgb(50, 0, 20, 0.02));
                    gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
            }
        };

    }

    // song
    private void playNextSong() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
        }
        try {
            Media bgmMedia = new Media(getClass().getResource(playlist[currentSongIndex]).toExternalForm());
            bgmPlayer = new MediaPlayer(bgmMedia);
            if (currentSongIndex == 1) {
                bgmPlayer.setVolume(0.10);
            } else if (currentSongIndex == 2) {
                bgmPlayer.setVolume(0.10);
            } else if (currentSongIndex == 3) {
                bgmPlayer.setVolume(0.15);
            } else {
                bgmPlayer.setVolume(0.05);
            }
            bgmPlayer.setOnEndOfMedia(() -> {
                currentSongIndex++;
                if (currentSongIndex >= playlist.length) {
                    currentSongIndex = 0;
                }
                playNextSong();
            });

            bgmPlayer.play();

        } catch (Exception e) {
            System.out.println("Error from " + playlist[currentSongIndex]);
        }
    }



    public void setWorld(World world) {
        this.world = world;
    }
    public World getWorld() {
        return this.world;
    }
    public Camera getCamera() {
        return this.camera;
    }
    public void setCurrentGameState(Main.GameState gameState) {
        this.currentGameState = gameState;
    }
    public void setControl(Scene scene, World world, Camera camera) {
        this.controller.control(scene, world, camera);
    }
    public Runnable getLevelUpCallBack() {
        return levelUpCallBack;
    }

    public void setLevelUpCallBack(Runnable levelUpCallBack) {
        this.levelUpCallBack = levelUpCallBack;
    }

    public boolean isLevelUpCallBack() {
        return isLevelUpCallBack;
    }

    public Runnable getGameOverCallBack() {
        return gameOverCallBack;
    }

    public void setGameOverCallBack(Runnable gameOverCallBack) {
        this.gameOverCallBack = gameOverCallBack;
    }

    public void setLevelUpCallBack(boolean levelUpCallBack) {
        isLevelUpCallBack = levelUpCallBack;
    }

    public double getCrackedPosY() {
        return crackedPosY;
    }

    public void setCrackedPosY(double crackedPosY) {
        this.crackedPosY = crackedPosY;
    }

    public double getCrackedPosX() {
        return crackedPosX;
    }

    public void setCrackedPosX(double crackedPosX) {
        this.crackedPosX = crackedPosX;
    }

    public Image getCrackedImage() {
        return crackedImage;
    }

    public void setCrackedImage(Image crackedImage) {
        this.crackedImage = crackedImage;
    }

    public Image getEnteringImage() {
        return enteringImage;
    }

    public void setEnteringImage(Image enteringImage) {
        this.enteringImage = enteringImage;
    }

    public Image getGlitchImage() {
        return glitchImage;
    }

    public void setGlitchImage(Image glitchImage) {
        this.glitchImage = glitchImage;
    }

    public Image getBlackholeImage() {
        return blackholeImage;
    }

    public void setBlackholeImage(Image blackholeImage) {
        this.blackholeImage = blackholeImage;
    }

    public Image getExplosionImage() {
        return explosionImage;
    }

    public void setExplosionImage(Image explosionImage) {
        this.explosionImage = explosionImage;
    }

    public Image getAuraImage() {
        return auraImage;
    }

    public void setAuraImage(Image auraImage) {
        this.auraImage = auraImage;
    }

    public Image getLightningImage_2() {
        return lightningImage_2;
    }

    public void setLightningImage_2(Image lightningImage_2) {
        this.lightningImage_2 = lightningImage_2;
    }

    public Image getLightningImage() {
        return lightningImage;
    }

    public void setLightningImage(Image lightningImage) {
        this.lightningImage = lightningImage;
    }

    public Image getRockImage() {
        return rockImage;
    }

    public void setRockImage(Image rockImage) {
        this.rockImage = rockImage;
    }

    public Image getBoomerangImage() {
        return boomerangImage;
    }

    public void setBoomerangImage(Image boomerangImage) {
        this.boomerangImage = boomerangImage;
    }

    public Image getSwordImage() {
        return swordImage;
    }

    public void setSwordImage(Image swordImage) {
        this.swordImage = swordImage;
    }

    public Image getJumpingSlashImage() {
        return jumpingSlashImage;
    }

    public void setJumpingSlashImage(Image jumpingSlashImage) {
        this.jumpingSlashImage = jumpingSlashImage;
    }

    public Image getCrossSlashImage() {
        return crossSlashImage;
    }

    public void setCrossSlashImage(Image crossSlashImage) {
        this.crossSlashImage = crossSlashImage;
    }

    public Image getSlashImage() {
        return slashImage;
    }

    public void setSlashImage(Image slashImage) {
        this.slashImage = slashImage;
    }

    public Image getArrowImage() {
        return arrowImage;
    }

    public void setArrowImage(Image arrowImage) {
        this.arrowImage = arrowImage;
    }

    public double getCurrentCrystalNoZ() {
        return currentCrystalNoZ;
    }

    public void setCurrentCrystalNoZ(double currentCrystalNoZ) {
        this.currentCrystalNoZ = currentCrystalNoZ;
    }

    public double getCurrentCrystalY() {
        return currentCrystalY;
    }

    public void setCurrentCrystalY(double currentCrystalY) {
        this.currentCrystalY = currentCrystalY;
    }

    public double getCurrentCrystalX() {
        return currentCrystalX;
    }

    public void setCurrentCrystalX(double currentCrystalX) {
        this.currentCrystalX = currentCrystalX;
    }

    public Image getCrystal() {
        return crystal;
    }

    public void setCrystal(Image crystal) {
        this.crystal = crystal;
    }

    public Image getProps() {
        return props;
    }

    public void setProps(Image props) {
        this.props = props;
    }

    public ImagePattern getPattern() {
        return pattern;
    }

    public void setPattern(ImagePattern pattern) {
        this.pattern = pattern;
    }

    public WritableImage getTile_craked() {
        return tile_craked;
    }

    public void setTile_craked(WritableImage tile_craked) {
        this.tile_craked = tile_craked;
    }

    public WritableImage getTile_normal() {
        return tile_normal;
    }

    public void setTile_normal(WritableImage tile_normal) {
        this.tile_normal = tile_normal;
    }

    public Image getTile() {
        return tile;
    }

    public void setTile(Image tile) {
        this.tile = tile;
    }

    public Image getBossMech() {
        return bossMech;
    }

    public void setBossMech(Image bossMech) {
        this.bossMech = bossMech;
    }

    public Image getChargerMech() {
        return chargerMech;
    }

    public void setChargerMech(Image chargerMech) {
        this.chargerMech = chargerMech;
    }

    public Image getEnemyMech() {
        return enemyMech;
    }

    public void setEnemyMech(Image enemyMech) {
        this.enemyMech = enemyMech;
    }

    public double getTrailTimer() {
        return trailTimer;
    }

    public void setTrailTimer(double trailTimer) {
        this.trailTimer = trailTimer;
    }

    public List<PlayerTrail> getPlayerTrails() {
        return playerTrails;
    }

    public void setPlayerTrails(List<PlayerTrail> playerTrails) {
        this.playerTrails = playerTrails;
    }

    public Image getPlayerMech() {
        return playerMech;
    }

    public void setPlayerMech(Image playerMech) {
        this.playerMech = playerMech;
    }
}
