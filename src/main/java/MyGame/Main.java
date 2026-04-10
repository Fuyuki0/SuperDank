package MyGame;

import MyGame.GameObject.Enemies.Boss;
import MyGame.GameObject.Enemies.Charger;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Items.Equipment.*;
import MyGame.GameObject.Items.PowerUp.EnergyDrink;
import MyGame.GameObject.Items.PowerUp.Steak;
import MyGame.GameObject.Player;
import MyGame.GameObject.Projectile.*;
import MyGame.GameObject.Skill.Skill_1.CrossSlash;
import MyGame.GameObject.Skill.Skill_1.JumpingSlash;
import MyGame.GameObject.Skill.Skill_1.Slash;
import MyGame.GameObject.SoundManager;
import MyGame.GameObject.Weapon.*;
import MyGame.GameObject.Weapon.Cores.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static MyGame.GameEngine.*;


public class Main extends Application {

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1200;

    public enum GameState {Menu, Play, Setting}
    public GameState currentState = GameState.Menu;

    // useable
    private VBox mainMenu;
    private VBox settingMenu;
    private BorderPane uiLayer;

    private Pane rouletteWindow;
    private VBox gambaPopup;
    private HBox itemBox;
    private StackPane holdDynamicRoulette;
    private VBox holdDynamicStat;

    private Button acceptRouletteButton;
    private Button refreshButton;
    private Button skipButton;
    private int freeRefresh = 3;
    private int refreshCost = 30;

    private Button continueGameButton;
    private Button returnMainMenuButton;

    private TranslateTransition spinningAnimation;
    private PauseTransition pauseTransition;

    private Weapon winningWeapon;
    private Weapon randomWeapon;


    private Button createCustomButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(500);
        button.setPrefHeight(100);
        button.setStyle(
                "-fx-background-color: #2b2b2b;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #E66420;" +
                "-fx-border-width: 3px;"
        );

        button.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (isFocused) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #2b2b2b;", "-fx-background-color: #8c3b3b;"));
            } else {
                button.setStyle(button.getStyle().replace("-fx-background-color: #8c3b3b;", "-fx-background-color: #2b2b2b;"));
            }
        });
        button.setOnMouseEntered(mouseEvent -> {
            if (!button.isFocused()) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #2b2b2b;", "-fx-background-color: #8c3b3b;"));
            }
        });
        button.setOnMouseExited(mouseEvent -> {
            if (!button.isFocused()) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #8c3b3b;", "-fx-background-color: #2b2b2b;"));
            }
        });
        button.setFont(BOLD_30);
        return button;
    }

    // roulette
    public void refreshButtonState(Player player) {
        if (freeRefresh > 0) {
            refreshButton.setText("Refresh (Free: " + freeRefresh + ")");
            refreshButton.setFont(FONT_20);
            refreshButton.setDisable(false);
        } else {
            refreshButton.setText("Refresh (" + refreshCost +" Coins)");
            refreshButton.setFont(FONT_20);
            if (player != null && player.getCoin() < refreshCost) {
                refreshButton.setDisable(true);
            } else {
                refreshButton.setDisable(false);
            }
        }
    }

    private Rarity gachaRarity(Player player) {
        double luck = player.getLuck();
        double roll = Math.random() * 100 + luck;
        if (roll <= 40) return Rarity.Common;
        if (roll <= 64) return Rarity.Uncommon;
        if (roll <= 82) return Rarity.Rare;
        if (roll <= 92) return Rarity.Epic;
        if (roll <= 97) return Rarity.Legendary;
        if (roll <= 99.5) return Rarity.Abysmal;
        return Rarity.Divine;
    }

    private VBox generateBonusStatDatabase(Weapon weapon) {
        VBox statBox = new VBox(3);
        statBox.setAlignment(Pos.CENTER);
        statBox.setVisible(false);
        statBox.setManaged(false);
        statBox.setId("Winning");

        // String for ui
        if (weapon.getLevel() > 1) {
            Label levelUpStatLabel = new Label("Stats Upgrade!");
            levelUpStatLabel.setTextFill(Color.GOLD);
            levelUpStatLabel.setFont(BOLD_14);
            statBox.getChildren().add(levelUpStatLabel);
        }

        int statSlot = 0;
        Rarity rarity = weapon.getRarity();
        if      (rarity == Rarity.Common)    statSlot = 1;
        else if (rarity == Rarity.Uncommon)  statSlot = 1;
        else if (rarity == Rarity.Rare)      statSlot = 2;
        else if (rarity == Rarity.Epic)      statSlot = 2;
        else if (rarity == Rarity.Legendary) statSlot = 3;
        else if (rarity == Rarity.Abysmal)   statSlot = 3;
        else if (rarity == Rarity.Divine)    statSlot = 5;

        String[] statNames = weapon.getWeaponStat();

        for (int i = 0; i < statSlot; i++) {
            if (weapon.isCore() && i > 0) {
                break;
            }
            String statName = statNames[(int) (Math.random() * statNames.length)];
            double bonusAmount = 0;
            switch (statName) {
                case "Damage" ->     {bonusAmount = 8 * rarity.getMultiplyBonusWeapon(); weapon.addBonusDamage(bonusAmount);}
                case "Atk Speed" ->  {bonusAmount = 2 * rarity.getMultiplyBonusWeapon(); weapon.addBonusAttackSpeed(bonusAmount);}
                case "Crit Rate" ->  {bonusAmount = 3 * rarity.getMultiplyBonusWeapon(); weapon.addBonusCritRate(bonusAmount);}
                case "Crit Dmg" ->   {bonusAmount = 15 * rarity.getMultiplyBonusWeapon(); weapon.addBonusCritDmg(bonusAmount);}
                case "Size" ->       {bonusAmount = 10 * rarity.getMultiplyBonusWeapon(); weapon.addBonusSize(bonusAmount);}
                case "Proj Count" -> {bonusAmount = 1 * rarity.getMultiplyBonusWeapon(); weapon.addBonusProjCount(bonusAmount);}
            }
            Label statlabel;
            if (!weapon.isCore()) {
                if (!statName.equals("Proj Count")) {
                    statlabel = new Label("+ " + String.format("%.1f", bonusAmount) + " % " + statName);
                } else {
                    statlabel = new Label("+ " + String.format("%.1f", bonusAmount) + " " + statName);
                }
            } else {
                bonusAmount = weapon.getAmount() * rarity.getMultiplyBonusCore() ;
                if (weapon.getAmountType() == Weapon.AmountType.Multiplier) {
                    statlabel = new Label("+ " + String.format("%.1f", bonusAmount * 100) + " % " + statName);
                } else {
                    statlabel = new Label("+ " + String.format("%.1f", bonusAmount) + " " + statName);
                }
            }
            statlabel.setTextFill(Color.GOLD);
            statlabel.setFont(FONT_12);
            statBox.getChildren().add(statlabel);
        }
        return statBox;
    }

    private void rouletteDatabase(World world) {
        itemBox.getChildren().clear();
        int winningIndex = 40;
        int totalItems = 50;
        int weaponCount = 0;
        for (Weapon weapon : world.getWeaponList()) {
            if (weapon.getMaxLevel() > 1) {
                weaponCount++;
            }
        }

        for (int i = 0; i < totalItems; i++) {
            List<Weapon> possibleWeapon = new ArrayList<>();
            possibleWeapon.add(new Sword());
            possibleWeapon.add(new BoomerangWeapon());
            possibleWeapon.add(new Bow());
            possibleWeapon.add(new OrbitRock());
            possibleWeapon.add(new Aura());
            possibleWeapon.add(new LightningWeapon());

            //possibleWeapon.add(new BiocatalystCore());
            //possibleWeapon.add(new ChaosCore());
            //possibleWeapon.add(new CryptoCore());
            //possibleWeapon.add(new DataCore());
            //possibleWeapon.add(new DynamoCore());
            //possibleWeapon.add(new EntropyCore());
            //possibleWeapon.add(new FractalCore());
            //possibleWeapon.add(new KineticCore());
            //possibleWeapon.add(new OverClockCore());
            //possibleWeapon.add(new OverHeatCore());
            //possibleWeapon.add(new PlasmaCore());
            //possibleWeapon.add(new ResonanceCore());
            //possibleWeapon.add(new SiphonCore());
            //possibleWeapon.add(new SomaticCore());
            //possibleWeapon.add(new SurgeCore());
            //possibleWeapon.add(new VectorCore());

            List<Weapon> weaponPool = new ArrayList<>();
            for (Weapon weapon : possibleWeapon) {
                int currentLevel = world.getWeaponLevel(weapon.getType());
                if (currentLevel > 0 && currentLevel < weapon.getMaxLevel()) {
                    weaponPool.add(weapon);
                } else if (currentLevel == 0 && weaponCount < 10) {
                    weaponPool.add(weapon);
                }
            }
            if (!weaponPool.isEmpty()) {
                randomWeapon = weaponPool.get((int) (Math.random() * weaponPool.size()));
            } else {
                randomWeapon = new Sword();
            }
            int currentLevel = world.getWeaponLevel(randomWeapon.getType());
            String displayText;
            Rarity randomRarity;
            if (currentLevel == 0) {
                randomRarity = Rarity.Common;
                displayText = "NEW! " + randomWeapon.getName();
                if (i == winningIndex) {
                    randomWeapon.setLevel(1);
                    this.winningWeapon = randomWeapon;
                }

            } else {
                randomRarity = gachaRarity(world.getPlayer());
                int nextLevel = currentLevel + 1;
                if (nextLevel == 20) displayText = "MAX " + randomWeapon.getName();
                else displayText = "Lvl " + nextLevel + " " + randomWeapon.getName();
                if (i == winningIndex) {
                    randomWeapon.setLevel(nextLevel);
                    this.winningWeapon = randomWeapon;
                }
            }
            randomWeapon.setRarity(randomRarity);

            StackPane item = new StackPane();
            item.setPrefSize(200, 200);
            item.setMaxSize(200, 200);
            String color = "#" + randomRarity.getColor().toString().substring(2, 8);
            item.setStyle("-fx-background-color: #2e2e2e; -fx-border-color: " + color + "; -fx-border-width: 4px;");
            Label weaponNameLabel = new Label(displayText);
            weaponNameLabel.setTextFill(Color.WHITE);
            weaponNameLabel.setFont(FONT_16);
            Label rarityLabel = new Label(randomRarity.toString());
            rarityLabel.setTextFill(randomRarity.getColor());
            rarityLabel.setFont(FONT_12);
            VBox itemLayout = new VBox(15, weaponNameLabel, rarityLabel);
            itemLayout.setAlignment(Pos.CENTER);
            if (i == winningIndex && currentLevel != 0) {
                VBox statBox = generateBonusStatDatabase(randomWeapon);
                itemLayout.getChildren().add(statBox);
            } else if (i == winningIndex && randomWeapon.isCore()) {
                VBox statBox = generateBonusStatDatabase(randomWeapon);
                itemLayout.getChildren().add(statBox);
            }
            item.getChildren().add(itemLayout);
            itemBox.getChildren().add(item);
        }
    }

    public void triggerPopupLevelUp(GameEngine gameEngine) {
        gameEngine.getWorld().setGameStop(true);
        gameEngine.setLevelUpCallBack(true);
        rouletteDatabase(gameEngine.getWorld());
        refreshButtonState(gameEngine.getWorld().getPlayer());
        gambaPopup.setVisible(true);
        acceptRouletteButton.setDisable(true);
        int winningShuffle = 40;
        double itemOnCenter = 10 + (winningShuffle * (200 + 15) ) + 100;
        double targetPosX = 700 - itemOnCenter;
        targetPosX += (Math.random() * 100) - 50;
        itemBox.setCache(true);
        itemBox.setCacheHint(CacheHint.SPEED);

        TranslateTransition spinningAnimation = getTranslateTransition(targetPosX, winningShuffle);
        spinningAnimation.play();

    }

    // Animation
    private TranslateTransition getTranslateTransition(double targetPosX, int winningShuffle) {
        spinningAnimation = new TranslateTransition();
        spinningAnimation.setNode(itemBox);
        spinningAnimation.setDelay(Duration.seconds(0.5));
        spinningAnimation.setDuration(Duration.seconds(3));
        spinningAnimation.setFromX(0);
        spinningAnimation.setToX(targetPosX);
        spinningAnimation.setInterpolator(Interpolator.EASE_BOTH);

        pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(actionEvent -> {
            itemBox.setCache(false);
            StackPane winningWeaponBox = (StackPane) itemBox.getChildren().get(winningShuffle);
            // stop cutting edge
            itemBox.getChildren().remove(winningWeaponBox);
            rouletteWindow.setVisible(false);
            rouletteWindow.setManaged(false);

            holdDynamicStat.setVisible(true);
            holdDynamicStat.setManaged(true);
            holdDynamicStat.getChildren().add(winningWeaponBox);

            VBox statLayout = (VBox) winningWeaponBox.getChildren().getFirst();
            for (Node node : statLayout.getChildren()) {
                if ("Winning".equals(node.getId())) {
                    node.setVisible(true);
                    node.setManaged(true);
                }
            }
            holdDynamicStat.applyCss();
            holdDynamicStat.layout();
            winningWeaponBox.setCache(true);
            winningWeaponBox.setCacheHint(CacheHint.SPEED);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), winningWeaponBox);
            scaleTransition.setToX(2);
            scaleTransition.setToY(2);
            scaleTransition.setOnFinished(event -> winningWeaponBox.setCache(false));
            scaleTransition.play();
            acceptRouletteButton.setDisable(false);
            acceptRouletteButton.setText("GET: " + winningWeapon.getName());
            acceptRouletteButton.setStyle(acceptRouletteButton.getStyle() + "-fx-border-color: #5af2d4;");
            acceptRouletteButton.requestFocus();
        });
        spinningAnimation.setOnFinished(actionEvent -> {
            pauseTransition.play();
        });

        acceptRouletteButton.setText("GET: ...");
        acceptRouletteButton.setStyle(acceptRouletteButton.getStyle() + "-fx-border-color: #E66420;");
        return spinningAnimation;
    }


    public void warmUpEngine(GraphicsContext gc, GameEngine engine) {
        Player player = new Player(0, 0);
        for (int i = 0; i < 100; i++) {

            new Enemy(0, 0, player);
            new Enemy(0, 0, player);
            new Enemy(0, 0, player);
            new Enemy(0, 0, player);
            new Enemy(0, 0, player);
            new Enemy(0, 0, player);
            new Charger(0, 0, player);
            new Charger(0, 0, player);
            new Charger(0, 0, player);
            new Charger(0, 0, player);
            new Charger(0, 0, player);
            new Boss(0, 0, player);

            new Arrow(0, 0, 0, 0);
            new Rock(0);
            new LightningEffect(new ArrayList<>());
            new Explosion(0, 0);
            new Slash(0, 0, 0, 0);
            new JumpingSlash(0, 0, 0, player);
            new CrossSlash(0, 0, 0);
            new Boomerang(0, 0, 0, 0, 0);

            new Sword();
            new BoomerangWeapon();
            new Bow();
            new OrbitRock();
            new Aura();
            new LightningWeapon();

            new BiocatalystCore();
            new ChaosCore();
            new CryptoCore();
            new DataCore();
            new DynamoCore();
            new EntropyCore();
            new FractalCore();
            new KineticCore();
            new MatrixCore();
            new OverClockCore();
            new OverHeatCore();
            new PlasmaCore();
            new ResonanceCore();
            new SiphonCore();
            new SomaticCore();
            new SurgeCore();
            new VectorCore();

            new DataFragment();
            new EngineOverload();
            new MagnetRing();
            new PhantomDrive();
            new ServoMotor();
            new AdrenalSyringe();
            new HexEditor();
            new MagneticRepulsor();
            new NeuroAmplifier();
            new TungstenPlating();
            new NecroticHeart();
            new ParasiticGas();
            new SoulHarvester();
            new TitaniumArmor();
            new DeepestVoid();
            new Doomsday();
            new MutatedBrain();
            new FinalWeapon();


            gc.save();
            gc.setGlobalAlpha(0.001);
            double offX = -10000;
            double offY = -10000;

            if (engine.getPlayerMech() != null) gc.drawImage(engine.getPlayerMech(), offX, offY);
            if (engine.getEnemyMech() != null) gc.drawImage(engine.getEnemyMech(), offX, offY);
            if (engine.getChargerMech() != null) gc.drawImage(engine.getChargerMech(), offX, offY);
            if (engine.getBossMech() != null) gc.drawImage(engine.getBossMech(), offX, offY);
            if (engine.getTile() != null) gc.drawImage(engine.getTile(), offX, offY);
            if (engine.getProps() != null) gc.drawImage(engine.getProps(), offX, offY);
            if (engine.getCrystal() != null) gc.drawImage(engine.getCrystal(), offX, offY);
            if (engine.getEnteringImage() != null) gc.drawImage(engine.getEnteringImage(), offX, offY);
            if (engine.getCrackedImage() != null) gc.drawImage(engine.getCrackedImage(), offX, offY);
            if (engine.getArrowImage() != null) gc.drawImage(engine.getArrowImage(), offX, offY);
            if (engine.getSlashImage() != null) gc.drawImage(engine.getSlashImage(), offX, offY);
            if (engine.getCrossSlashImage() != null) gc.drawImage(engine.getCrossSlashImage(), offX, offY);
            if (engine.getJumpingSlashImage() != null) gc.drawImage(engine.getJumpingSlashImage(), offX, offY);
            if (engine.getSwordImage() != null) gc.drawImage(engine.getSwordImage(), offX, offY);
            if (engine.getBoomerangImage() != null) gc.drawImage(engine.getBoomerangImage(), offX, offY);
            if (engine.getRockImage() != null) gc.drawImage(engine.getRockImage(), offX, offY);
            if (engine.getLightningImage() != null) gc.drawImage(engine.getLightningImage(), offX, offY);
            if (engine.getLightningImage_2() != null) gc.drawImage(engine.getLightningImage_2(), offX, offY);
            if (engine.getAuraImage() != null) gc.drawImage(engine.getAuraImage(), offX, offY);
            if (engine.getExplosionImage() != null) gc.drawImage(engine.getExplosionImage(), offX, offY);
            if (engine.getBlackholeImage() != null) gc.drawImage(engine.getBlackholeImage(), offX, offY);
            if (engine.getGlitchImage() != null) gc.drawImage(engine.getGlitchImage(), offX, offY);

            gc.restore();
            gc.setGlobalAlpha(1.0);

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {



        // world here
        World world = new World();


        StackPane root = new StackPane();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        uiLayer = new BorderPane();
        uiLayer.setPickOnBounds(false);
        uiLayer.setVisible(false);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // ========== Play ============

        // UI ------------------------


        Image healthFillImg = new Image(getClass().getResource("/HealthBarFill.png").toExternalForm());
        ImageView healthFillView = new ImageView(healthFillImg);
        healthFillView.setFitWidth(450);
        healthFillView.setFitHeight(16);

        Image overHealFillImg = new Image(getClass().getResource("/OverhealBarFill.png").toExternalForm());
        ImageView overHealFillView = new ImageView(overHealFillImg);
        overHealFillView.setFitWidth(450);
        overHealFillView.setFitHeight(16);

        Image healthBgImg = new Image(getClass().getResource("/HealthBar.png").toExternalForm());
        ImageView healthBgView = new ImageView(healthBgImg);
        healthBgView.setFitWidth(495);
        healthBgView.setFitHeight(26);
        healthBgView.setTranslateX(8);

        StackPane customHealthBar = new StackPane();
        customHealthBar.setAlignment(Pos.CENTER);
        customHealthBar.getChildren().addAll(healthBgView, healthFillView, overHealFillView);

        Label healthLabel = new Label("HP: 1000 / 1000");
        healthLabel.setTextFill(Color.WHITE);
        healthLabel.setFont(FONT_15);

        StackPane healthBottomCenterUI = new StackPane();
        healthBottomCenterUI.getChildren().addAll(customHealthBar, healthLabel);
        StackPane.setAlignment(healthLabel, Pos.CENTER);

        Image StaminaFillImg = new Image(getClass().getResource("/StaminaBarFill.png").toExternalForm());
        ImageView staminaFillView = new ImageView(StaminaFillImg);
        staminaFillView.setFitWidth(430);
        staminaFillView.setFitHeight(13);
        staminaFillView.setTranslateX(28);
        staminaFillView.setTranslateY(3);

        Image StaminaBgImg = new Image(getClass().getResource("/StaminaBar.png").toExternalForm());
        ImageView StaminaBgView = new ImageView(StaminaBgImg);
        StaminaBgView.setFitWidth(490);
        StaminaBgView.setFitHeight(26);
        StaminaBgView.setTranslateX(6);

        StackPane customStaminaBar = new StackPane();
        customStaminaBar.setAlignment(Pos.CENTER);
        customStaminaBar.getChildren().addAll(StaminaBgView, staminaFillView);

        Label staminaLabel = new Label("STA: 100 / 100");
        staminaLabel.setTextFill(Color.WHITE);
        staminaLabel.setFont(FONT_12);
        staminaLabel.setTranslateX(15);
        staminaLabel.setTranslateY(3);

        StackPane staminaBottomCenterUI = new StackPane();
        staminaBottomCenterUI.getChildren().addAll(customStaminaBar, staminaLabel);
        StackPane.setAlignment(staminaLabel, Pos.CENTER);
        StackPane.setMargin(staminaBottomCenterUI, new Insets(0, 0, 50, 0));

        StackPane BottomCenterUI = new StackPane();
        BottomCenterUI.getChildren().addAll(
                healthBottomCenterUI,
                staminaBottomCenterUI);
        uiLayer.setBottom(BottomCenterUI);


        Image expBgImg = new Image(getClass().getResource("/ExpBar.png").toExternalForm());
        ImageView expBgView = new ImageView(expBgImg);
        expBgView.setFitWidth(1980);
        expBgView.setFitHeight(40);
        Image expFillImg = new Image(getClass().getResource("/ExpBarFill.png").toExternalForm());
        ImageView expFillView = new ImageView(expFillImg);
        expFillView.setFitWidth(1920);
        expFillView.setFitHeight(30);

        StackPane customExpBar = new StackPane();
        customExpBar.setAlignment(Pos.CENTER_LEFT);
        customExpBar.getChildren().addAll(expBgView, expFillView);
        Label levelLabel = new Label("Level: 0");
        levelLabel.setTextFill(Color.WHITE);
        levelLabel.setFont(FONT_20);

        StackPane levelTopCenterUI = new StackPane();
        levelTopCenterUI.getChildren().addAll(
                customExpBar,
                levelLabel
        );
        StackPane.setAlignment(levelLabel, Pos.CENTER);
        uiLayer.setTop(levelTopCenterUI);

        VBox chestUIPopup = new VBox(20);
        chestUIPopup.setAlignment(Pos.CENTER);
        chestUIPopup.setStyle(
                "-fx-background-color: #2e2e2e; " +
                "-fx-padding: 50;" +
                "-fx-background-radius: 20;" +
                "-fx-border-width: 4px;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 200, 0, 0.4), 40, 0, 0, 0);"
        );
        chestUIPopup.setMaxSize((double) (SCREEN_WIDTH * 4) / 7, 900);
        chestUIPopup.setVisible(false);

        Label chestUITitle = new Label("Chest opened!");
        chestUITitle.setTextFill(Color.GOLD);
        chestUITitle.setFont(BOLD_80);
        chestUITitle.setStyle("-fx-effect: dropshadow(gaussian, black, 40, 0, 0, 0);");


        Label itemName = new Label("");
        itemName.setTextFill(Color.WHITE);
        itemName.setFont(FONT_60);

        Label itemRarityLabel = new Label("");
        itemRarityLabel.setFont(BOLD_35);
        itemRarityLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0, 0, 0);");

        Label itemDescribe = new Label("");
        itemDescribe.setTextFill(Color.WHITE);
        itemDescribe.setFont(FONT_40);
        itemDescribe.setWrapText(true);
        itemDescribe.setTextAlignment(TextAlignment.CENTER);

        Button chestSkipButton = createCustomButton("SKIP");
        chestSkipButton.setPrefWidth(250);
        chestSkipButton.setFont(FONT_30);
        Button chestContinueButton = createCustomButton("CONTINUE");
        chestContinueButton.setPrefWidth(250);
        chestContinueButton.setFont(FONT_30);
        HBox chestButtonBox = new HBox(30);
        chestButtonBox.getChildren().addAll(chestSkipButton, chestContinueButton);
        chestButtonBox.setAlignment(Pos.CENTER);

        chestUIPopup.getChildren().addAll(
                chestUITitle,
                itemName,
                itemRarityLabel,
                itemDescribe,
                new Region(),
                chestButtonBox
        );

        Label coinCountLabel = new Label("0");
        coinCountLabel.setTextFill(Color.WHITE);
        coinCountLabel.setFont(FONT_30);
        Image coinImg = new Image(getClass().getResource("/coin.png").toExternalForm());
        ImageView coinIcon = new ImageView(coinImg);
        coinIcon.setFitWidth(50);
        coinIcon.setFitHeight(50);
        coinCountLabel.setGraphic(coinIcon);
        coinCountLabel.setGraphicTextGap(10);

        Label killCountLabel = new Label("0");
        killCountLabel.setTextFill(Color.WHITE);
        killCountLabel.setFont(FONT_30);
        Image killImg = new Image(getClass().getResource("/skull.png").toExternalForm());
        ImageView killIcon = new ImageView(killImg);
        killIcon.setFitWidth(50);
        killIcon.setFitHeight(50);
        killCountLabel.setGraphic(killIcon);
        killCountLabel.setGraphicTextGap(10);

        Label timeSurvivedLabel = new Label("00:00");
        timeSurvivedLabel.setTextFill(Color.WHITE);
        timeSurvivedLabel.setFont(FONT_40);

        StackPane UtilUIPane = new StackPane(timeSurvivedLabel, killCountLabel, coinCountLabel);
        StackPane.setAlignment(timeSurvivedLabel, Pos.CENTER);
        StackPane.setAlignment(killCountLabel, Pos.CENTER);
        StackPane.setMargin(killCountLabel, new Insets(0, 0, 0,  SCREEN_WIDTH / 3.0 + 10));
        StackPane.setAlignment(coinCountLabel, Pos.CENTER);
        StackPane.setMargin(coinCountLabel, new Insets(0, SCREEN_WIDTH / 3.0, 0, 0));

        VBox TopUtilUIBox = new VBox(levelTopCenterUI, UtilUIPane);
        TopUtilUIBox.setSpacing(10);
        uiLayer.setTop(TopUtilUIBox);

        VBox topLeftWeaponHang = new VBox(5);
        topLeftWeaponHang.setPadding(new Insets(20, 0, 0, 10));
        uiLayer.setLeft(topLeftWeaponHang);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        Camera camera = new Camera(canvas.getWidth(), canvas.getHeight());
        Controller controller = new Controller();
        GameEngine engine = new GameEngine(
                scene, world, controller, camera, gc, canvas,

                healthFillView, overHealFillView, healthLabel,
                staminaFillView, staminaLabel, expFillView, levelLabel,

                chestUIPopup, itemName, itemRarityLabel, itemDescribe, chestContinueButton, chestSkipButton,
                killCountLabel, killIcon, coinCountLabel, coinIcon, timeSurvivedLabel, topLeftWeaponHang
        );

        //  ========= new skip handle (space to jump block) ==========


        // input bleed fix
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (gambaPopup.isVisible() && keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ENTER) {
                if (acceptRouletteButton.isDisable()) {
                    if (spinningAnimation != null) spinningAnimation.stop();
                    if (pauseTransition != null) pauseTransition.stop();
                    if (pauseTransition != null && pauseTransition.getOnFinished() != null)
                        pauseTransition.getOnFinished().handle(null);
                } else {
                    if (refreshButton.isFocused() && !refreshButton.isDisabled()) {
                        refreshButton.fire();
                    } else if (skipButton.isFocused() && !skipButton.isDisabled()) {
                        skipButton.fire();
                    } else {
                        acceptRouletteButton.fire();
                    }
                }
                keyEvent.consume();
            }
            if (chestUIPopup.isVisible() && (keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ENTER)) {
                if (!chestContinueButton.isDisabled()) {
                    if (chestSkipButton.isFocused()) {
                        chestSkipButton.fire();
                    } else {
                        chestContinueButton.fire();
                    }
                }
                keyEvent.consume();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (gambaPopup.isVisible() && keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ENTER) {
                keyEvent.consume();
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (gambaPopup.isVisible() && mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (acceptRouletteButton.isDisable()) {
                    if (spinningAnimation != null) spinningAnimation.stop();
                    if (pauseTransition != null) pauseTransition.stop();
                    if (pauseTransition != null && pauseTransition.getOnFinished() != null)
                        pauseTransition.getOnFinished().handle(null);
                    mouseEvent.consume();
                }
            }
        });


        // UI keyboard
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();
            // chest selector
            if (chestUIPopup.isVisible() && !chestContinueButton.isDisabled()) {
                if (code == KeyCode.H || code == KeyCode.LEFT || (code == KeyCode.TAB && keyEvent.isShiftDown())) {
                    chestSkipButton.requestFocus();
                    keyEvent.consume();
                } else if (code == KeyCode.L || code == KeyCode.RIGHT || (code == KeyCode.TAB && !keyEvent.isShiftDown())) {
                    chestContinueButton.requestFocus();
                    keyEvent.consume();
                }
            }
            // roulette selector
            if (gambaPopup.isVisible() && !acceptRouletteButton.isDisabled()) {
                if (acceptRouletteButton.isDisable()) {
                    acceptRouletteButton.requestFocus();
                }
                if (code == KeyCode.H || code == KeyCode.LEFT || (code == KeyCode.TAB && keyEvent.isShiftDown())) {
                    if (acceptRouletteButton.isFocused()) {
                        if (!refreshButton.isDisabled()) refreshButton.requestFocus();
                        else skipButton.requestFocus();
                    } else if (refreshButton.isFocused()) {
                        skipButton.requestFocus();
                    } else if (skipButton.isFocused()) {
                        acceptRouletteButton.requestFocus();
                    }
                    keyEvent.consume();
                } else if (code == KeyCode.L || code == KeyCode.RIGHT || (code == KeyCode.TAB && !keyEvent.isShiftDown())) {
                    if (skipButton.isFocused()) {
                        if (!refreshButton.isDisabled()) refreshButton.requestFocus();
                        else acceptRouletteButton.requestFocus();
                    } else if (refreshButton.isFocused()) {
                        acceptRouletteButton.requestFocus();
                    } else if (acceptRouletteButton.isFocused()) {
                        skipButton.requestFocus();
                    }
                    keyEvent.consume();
                }
            }
            if (!returnMainMenuButton.isDisable() && continueGameButton.isVisible()) {
                if (code == KeyCode.J || code == KeyCode.DOWN || (code == KeyCode.TAB && !keyEvent.isShiftDown())) {
                    if (returnMainMenuButton.isFocused()) {
                        continueGameButton.requestFocus();
                    } else
                        returnMainMenuButton.requestFocus();
                    keyEvent.consume();
                } else if (code == KeyCode.K || code == KeyCode.UP || (code == KeyCode.TAB && keyEvent.isShiftDown())){
                    if (continueGameButton.isFocused()) {
                        returnMainMenuButton.requestFocus();
                    } else
                        continueGameButton.requestFocus();
                    keyEvent.consume();
                }
            }
        });


        // fix controller
        chestContinueButton.setOnAction(actionEvent -> {
            controller.resetInput();
            world.getPlayer().setVelocityX(0);
            world.getPlayer().setVelocityY(0);
            canvas.requestFocus();
        });

        chestSkipButton.setOnAction(actionEvent -> {
            controller.resetInput();
            world.getPlayer().setVelocityX(0);
            world.getPlayer().setVelocityY(0);
            canvas.requestFocus();
        });


        // run animation before level up >)
        engine.setLevelUpCallBack(() -> {
            triggerPopupLevelUp(engine);
        });

        engine.start();



        // ----------------------------




        // roulette gamba popup
        gambaPopup = new VBox(20);
        gambaPopup.setAlignment(Pos.CENTER);
        gambaPopup.setStyle("-fx-background-color: rgba(10, 10, 15, 0.9); -fx-padding: 50;");
        gambaPopup.setVisible(false);
        Label gambaTitle = new Label("LEVEL UP!");
        gambaTitle.setFont(FONT_50);
        gambaTitle.setTextFill(Color.GOLD);

        rouletteWindow = new Pane();
        rouletteWindow.setPrefSize(1400, 250);
        rouletteWindow.setMaxSize(1400, 250);
        rouletteWindow.setStyle("-fx-border-color: white; -fx-border-width: 4px; -fx-background-color: #787878;");
        Rectangle clip = new Rectangle(1400, 250);
        rouletteWindow.setClip(clip);

        itemBox = new HBox(15);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setPadding(new Insets(10));
        itemBox.setPrefHeight(250);

        Line centerLine = new Line(700, 0, 700, 250);
        centerLine.setStroke(Color.RED);
        centerLine.setStrokeWidth(4);

        rouletteWindow.getChildren().addAll(
                itemBox,
                centerLine
        );


        // getButton
        acceptRouletteButton = createCustomButton("CONTINUE!");
        acceptRouletteButton.setFont(FONT_20);
        acceptRouletteButton.setPrefWidth(350);
        acceptRouletteButton.setDisable(true);
        acceptRouletteButton.setOnAction(actionEvent -> {
            acceptRouletteButton.setDisable(true);
            gambaPopup.setVisible(false);
            itemBox.setTranslateX(0);

            holdDynamicStat.setVisible(false);
            holdDynamicStat.setManaged(false);
            holdDynamicStat.getChildren().clear();

            // reset wheel
            rouletteWindow.setVisible(true);
            rouletteWindow.setManaged(true);

            if (winningWeapon.getLevel() == 1 && !engine.getWorld().isExistWeapon(winningWeapon)) {
                engine.getWorld().addNewWeapon(winningWeapon);

                Label newWeaponLabel = new Label("Lvl 1 - " + winningWeapon.getName());
                newWeaponLabel.setId(winningWeapon.getName());
                if (!winningWeapon.isCore()) 
                    newWeaponLabel.setTextFill(Color.WHITE);
                else
                    newWeaponLabel.setTextFill(Color.LIGHTBLUE);
                newWeaponLabel.setFont(FONT_20);
                newWeaponLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 5; -fx-background-radius: 5;");
                topLeftWeaponHang.getChildren().add(newWeaponLabel);
            } else {
                engine.getWorld().upgradeExistWeapon(winningWeapon, winningWeapon.getRarity());
                for (Node node : topLeftWeaponHang.getChildren()) {
                    if (node.getId() != null && node.getId().equals(winningWeapon.getName())) {
                        ((Label) node).setText("Lvl " + engine.getWorld().getWeaponLevel(winningWeapon.getType()) + " - " + winningWeapon.getName());
                    }
                }
            }

            engine.getWorld().resumeGame();
            engine.setLevelUpCallBack(false);
            refreshCost = 30;
        });

        refreshButton = createCustomButton("REFRESH");
        refreshButton.setPrefWidth(350);
        refreshButton.setOnAction(actionEvent -> {
            Player player = engine.getWorld().getPlayer();
            if (freeRefresh > 0) {
                freeRefresh--;
            } else if (player.getCoin() >= refreshCost) {
                player.setCoin(player.getCoin() - refreshCost);
                refreshCost = (int)(refreshCost * 1.5);
            } else return;

            if (spinningAnimation != null) spinningAnimation.stop();
            if (pauseTransition != null) pauseTransition.stop();
            itemBox.setTranslateX(0);
            holdDynamicStat.setVisible(false);
            holdDynamicStat.setManaged(false);
            holdDynamicStat.getChildren().clear();
            rouletteWindow.setVisible(true);
            rouletteWindow.setManaged(true);
            triggerPopupLevelUp(engine);
        });

        skipButton = createCustomButton("SKIP REWARD");
        skipButton.setFont(FONT_20);
        skipButton.setPrefWidth(350);
        skipButton.setOnAction(actionEvent -> {
            if (spinningAnimation != null) spinningAnimation.stop();
            if (pauseTransition != null) pauseTransition.stop();
            gambaPopup.setVisible(false);
            itemBox.setTranslateX(0);
            holdDynamicStat.setVisible(false);
            holdDynamicStat.setManaged(false);
            holdDynamicStat.getChildren().clear();
            rouletteWindow.setVisible(true);
            rouletteWindow.setManaged(true);

            engine.getWorld().resumeGame();
            engine.setLevelUpCallBack(false);
        });

        HBox rouletteButtonLayout = new HBox(20);
        rouletteButtonLayout.setAlignment(Pos.CENTER);
        rouletteButtonLayout.getChildren().addAll(
                skipButton,
                refreshButton,
                acceptRouletteButton
        );

        holdDynamicRoulette = new StackPane();
        holdDynamicRoulette.setPrefHeight(600);
        holdDynamicRoulette.setAlignment(Pos.CENTER);

        holdDynamicStat = new VBox();
        holdDynamicStat.setAlignment(Pos.CENTER);
        holdDynamicStat.setVisible(false);
        holdDynamicStat.setFillWidth(false);
        holdDynamicRoulette.getChildren().addAll(
                rouletteWindow,
                holdDynamicStat
        );
        gambaPopup.getChildren().clear();

        gambaPopup.getChildren().addAll(
                gambaTitle,
                holdDynamicRoulette,
                rouletteButtonLayout
        );

        // load weapon font css
        rouletteDatabase(world);
        gambaPopup.applyCss();
        gambaPopup.layout();
        itemBox.getChildren().clear();




        // ========= GAME OVER ==========


        VBox gameOverPopup = new VBox(30);
        gameOverPopup.setAlignment(Pos.CENTER);
        gameOverPopup.setStyle("-fx-background-color: rgba(40, 0, 0, 0.9); -fx-padding: 50;");
        gameOverPopup.setVisible(false);
        Label deadTitle = new Label("YOU DEAD");
        deadTitle.setFont(FONT_100);
        deadTitle.setTextFill(Color.RED);
        deadTitle.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 10, 0, 0, 0);");
        Label deadStatLabel = new Label();
        deadStatLabel.setFont(FONT_35);
        deadStatLabel.setTextFill(Color.WHITE);
        deadStatLabel.setAlignment(Pos.CENTER);
        deadStatLabel.setTextAlignment(TextAlignment.CENTER);
        Button returnToMenuButton = createCustomButton("RETURN TO MENU");
        returnToMenuButton.setOnAction(actionEvent -> {
            gameOverPopup.setVisible(false);
            uiLayer.setVisible(false);
            topLeftWeaponHang.getChildren().clear();
            chestUIPopup.setVisible(false);
            gambaPopup.setVisible(false);
            mainMenu.setVisible(true);
            currentState = GameState.Menu;
            engine.setCurrentGameState(currentState);
        });

        gameOverPopup.getChildren().addAll(
                deadTitle,
                deadStatLabel,
                returnToMenuButton
        );

        engine.setGameOverCallBack(() -> {
            int totalTimeSurvived = (int) engine.getWorld().getTimeSurvived();
            int getMinute = totalTimeSurvived / 60;
            int getSecond = totalTimeSurvived % 60;
            String timeString = String.format("%02d:%02d", getMinute, getSecond);

            deadStatLabel.setText(
                    "Time Survived: " + timeString + "\n\n" +
                    "Enimies Killed: " + engine.getWorld().getPlayer().getEnemiesKilled() + "\n\n" +
                    "Player Level: " + engine.getWorld().getPlayer().getPlayerLevel() + "\n\n" +
                    "Coin Gained: " + engine.getWorld().getPlayer().getCoin()
            );
            gameOverPopup.setVisible(true);
        });


        root.getChildren().addAll(
                canvas,
                uiLayer,
                chestUIPopup,
                gambaPopup
        );





        // =========== Menu ============

        engine.setCurrentGameState(currentState);
        if (currentState == GameState.Menu) {
            warmUpEngine(gc, engine);
        }
        mainMenu = new VBox(50);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setStyle("-fx-backgroud-color: rgba(5, 0, 0, 0.9);");
        mainMenu.setVisible(true);
        Label titleLabel = new Label();
        titleLabel.setText("SUPERDANK");
        titleLabel.setFont(BOLD_100);
        titleLabel.setStyle(" -fx-text-fill: #E66420; -fx-effect: dropshadow(gaussian, red, 10, 0, 0, 0);");
        Button playButton = createCustomButton("START THE DANK!");
        playButton.setTranslateY(140);
        playButton.setOnAction(actionEvent -> {
            playButton.setText("LOADING ASSETS...");
            playButton.setDisable(true);
            PauseTransition loading = new PauseTransition(Duration.seconds(0.1));
            loading.setOnFinished(pauseTransition -> {
                warmUpEngine(gc, engine);
                currentState = GameState.Play;
            });
            loading.play();
        });
        Button exitButton = createCustomButton("QUIT");
        exitButton.setOnAction(actionEvent -> Platform.exit());
        exitButton.setTranslateY(140);
        mainMenu.getChildren().addAll(titleLabel, playButton, exitButton);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (playButton.isVisible()) {
                if (code == KeyCode.J || code == KeyCode.DOWN || (code == KeyCode.TAB && !keyEvent.isShiftDown())) {
                    if (playButton.isFocused()) {
                        exitButton.requestFocus();
                    } else
                        playButton.requestFocus();
                    keyEvent.consume();
                } else if (code == KeyCode.K || code == KeyCode.UP || (code == KeyCode.TAB && keyEvent.isShiftDown())) {
                    if (exitButton.isFocused()) {
                        playButton.requestFocus();
                    } else
                        exitButton.requestFocus();
                    keyEvent.consume();
                }
            }
        });

        // ========= Setting ========= //

        settingMenu = new VBox(30);
        settingMenu.setAlignment(Pos.CENTER);
        settingMenu.setStyle("-fx-backgroud-color: rgba(0, 0, 0, 0.8);");
        settingMenu.setVisible(false);
        Label pauseLabel = new Label("PAUSE");
        pauseLabel.setStyle("-fx-text-fill: #ff3311;");
        pauseLabel.setFont(BOLD_100);
        continueGameButton = createCustomButton("CONTINUE");
        continueGameButton.setOnAction(actionEvent -> {
            currentState = GameState.Play;
            engine.setCurrentGameState(currentState);
            engine.getWorld().resumeGame();
            settingMenu.setVisible(false);
        });
        returnMainMenuButton = createCustomButton("MAINMENU");
        returnMainMenuButton.setOnAction(actionEvent -> {
            currentState = GameState.Menu;
            engine.setCurrentGameState(currentState);
            engine.getWorld().setGameStop(true);
            settingMenu.setVisible(false);
            mainMenu.setVisible(true);
            uiLayer.setVisible(false);
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE && !gambaPopup.isVisible() && !chestUIPopup.isVisible()) {
                currentState = GameState.Setting;
                engine.setCurrentGameState(currentState);
                engine.getWorld().pauseGame();
                settingMenu.setVisible(true);
                continueGameButton.requestFocus();
                keyEvent.consume();
            }
        });
        settingMenu.getChildren().addAll(pauseLabel, continueGameButton, returnMainMenuButton);

        // ========== refresh every state ===========
        playButton.setOnAction(actionEvent -> {
            currentState = GameState.Play;
            mainMenu.setVisible(false);
            uiLayer.setVisible(true);
            topLeftWeaponHang.getChildren().clear();

            freeRefresh = 3;
            refreshCost = 30;

            engine.setCurrentGameState(currentState);
            engine.setWorld(new World());
            engine.setControl(scene, engine.getWorld(), engine.getCamera());
            SoundManager.enteringPlayer.stop();
            SoundManager.ultimatePlayer.stop();
            engine.getWorld().getPlayer().setPosZ(7500);
            engine.getWorld().setEntering(true);
            engine.getWorld().setEnteringSoundCheck(0);
            engine.getWorld().setTimeSurvived(0);

            engine.setLevelUpCallBack(false);
            chestUIPopup.setVisible(false);
            gambaPopup.setVisible(false);
        });

        // ----------------------------

        root.getChildren().addAll(mainMenu, settingMenu);


        // gameOver add here
        root.getChildren().add(gameOverPopup);


        // window setup
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setTitle("SuperDank");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            if (engine != null) engine.stop();
            System.exit(0);
        });
    }
}