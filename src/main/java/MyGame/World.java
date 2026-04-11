package MyGame;



import MyGame.GameObject.Enemies.Boss;
import MyGame.GameObject.Enemies.Charger;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.*;
import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.PowerUp.EnergyDrink;
import MyGame.GameObject.Items.PowerUp.Steak;
import MyGame.GameObject.Projectile.*;
import MyGame.GameObject.Skill.CrossSlash;
import MyGame.GameObject.Skill.JumpingSlash;
import MyGame.GameObject.Skill.Slash;
import MyGame.GameObject.Skill.Ultimate;
import MyGame.GameObject.Weapon.*;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.List;

import static MyGame.GameObject.Items.ItemManager.openingChest;
import static MyGame.Main.SCREEN_HEIGHT;
import static MyGame.Main.SCREEN_WIDTH;

public class World {
    private boolean showMiniMap = true;

    private Player player;
    private List<Enemy> enemies;
    private double enemySpawnTimer;
    private double chargersSpawnTimer;
    private double bossSpawnTimer;
    private boolean bossIsAlive;
    private double chargerWaveTimer;

    // animation
    private int currentCrystalFrame = 0;
    private double crystalAnimationTimer = 0.0;
    private int currentExplosionFrame = 0;
    private double explosionAnimationTimer = 0.0;
    private int currentBlackholeFrame = 0;
    private double blackholeAnimationTimer = 0.0;
    private int currentFinalWeaponFrame = 0;
    private double finalWeaponAnimationTimer = 0.0;
    private int currentEnteringFrame = 0;
    private double enteringAnimationTimer = 0.0;
    private boolean entering = true;

    private List<Experience> experience;

    // skill
    private List<Slash> slashes;
    private List<JumpingSlash> jumpingSlashes;
    private List<CrossSlash> crossSlashes;
    private Ultimate ultimate;
    private boolean ultimateActive = false;

    private List<Weapon> weaponList;
    private Bow bow;
    private Sword sword;
    private Aura aura;
    private OrbitRock orbitRock;
    private BoomerangWeapon boomerangWeapon;
    private LightningWeapon lightningWeapon;
    private List<Rock> rocks;
    private List<Boomerang> boomerangs;
    private List<Arrow> arrows;
    private List<LightningEffect> lightningEffects;
    private List<Projectile> projectiles = new ArrayList<>();

    private List<Chest> chests;
    private List<Steak> steaks;
    private Item item = null;
    private List<Item> itemList;
    private boolean gameStop = false;

    // building
    private List<Obstacle> obstacles;

    // damage text
    private double timeSurvived = 0;
    private List<DamageText> damageTexts;

    // difficulty
    private double difficultyMultiplier = 1;

    // item
    private double adrenalCooldown = 0;
    private List<Explosion> explosions;
    private List<double[]> blackHoles = new ArrayList<>();
    private double rainbowTimer = 0;
    private double repulsorVisualTimer = 0;
    private double gasVisualTimer = 0;
    private List<double[]> glitchStrikes = new ArrayList<>();

    // powerup
    private List<EnergyDrink> energyDrinks = new ArrayList<>();
    private double energyDrinkTimer = 0;
    private boolean energyDrinkActive = false;
    private double drinkShieldTimer = 0;
    private double drinkSpeedTimer = 0;
    private boolean drinkSpeedActive = false;
    private double drinkMagnetTimer = 0;

    // camara
    private double screenShakeTimer = 0;
    private double screenShakeIntensity = 0;

    // ui
    private boolean uiNeedsUpdate = true;

    // sfx check
    private int enteringSoundCheck = 0;
    private int ultimateSoundCheck = 0;


    public World() {
        this.player = new Player(0, 0);
        this.enemies = new ArrayList<>();
        this.enemySpawnTimer = 0;
        this.chargersSpawnTimer = 0;
        this.bossSpawnTimer = 0;
        this.bossIsAlive = false;

        this.experience = new ArrayList<>();

        this.slashes = new ArrayList<>();
        this.jumpingSlashes = new ArrayList<>();
        this.crossSlashes = new ArrayList<>();
        this.ultimate = new Ultimate();

        this.weaponList = new ArrayList<>();

        this.sword = null;
        this.bow = null;
        this.aura = null;
        this.orbitRock = null;
        this.boomerangWeapon = null;
        this.lightningWeapon = null;

        this.boomerangs = new ArrayList<>();
        this.rocks = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.lightningEffects = new ArrayList<>();

        this.chests = new ArrayList<>();
        this.steaks = new ArrayList<>();
        this.itemList = new ArrayList<>();

        this.obstacles = new ArrayList<>();

        this.damageTexts = new ArrayList<>();

        // item
        this.explosions = new ArrayList<>();

        // get exp reuse
        for (int i = 0; i < 200; i++) {
            Experience exp = Experience.create(0, 0);
            Experience.release(exp);
        }


        // map
        mapGenerate();

    }

    public void mapGenerate() {
        double MAP_LIMIT = 20000;

        for (int i = 0; i < 250; i++) {
            double posX;
            double posY;
            double width;
            double height;
            if (Math.random() > 0.5) {
                width = 100 + (Math.random() * 300);
                height = 100 + (Math.random() * 300);
            } else {
                width = 200 + (Math.random() * 400);
                height = 200 + (Math.random() * 400);
            }
            do {
                posX = (Math.random() * MAP_LIMIT * 2) - MAP_LIMIT;
                posY = (Math.random() * MAP_LIMIT * 2) - MAP_LIMIT;
            } while (Math.abs(posX) < 1500 && Math.abs(posY) < 1500 || insideObstacle(posX, posY));

            obstacles.add(new Obstacle(posX, posY, width, height));
        }

        for (int i = 0; i < 100; i++) {
            double randomX = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
            double randomY = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;

            while (insideObstacle(randomX, randomY)) {
                randomX = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
                randomY = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
            }
            chests.add(new Chest(randomX, randomY));
        }

        for (int i = 0; i < 400; i++) {
            double randomX = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
            double randomY = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
            while (insideObstacle(randomX, randomY)) {
                randomX = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
                randomY = Math.random() * MAP_LIMIT * 2 - MAP_LIMIT;
            }
            steaks.add(new Steak(randomX, randomY));
        }
    }

    public void update(double deltaTime) {

        // debug


        // ============

        if (gameStop) {
            return;
        }

        else timeSurvived += deltaTime;
        if (screenShakeTimer > 0) {
            screenShakeTimer -= deltaTime;
        }

        // entering
        if (enteringSoundCheck < 1) {
            SoundManager.enteringPlayer.play();
            enteringSoundCheck++;
            triggerScreenShake(1.5, 30);
        }
        if (entering && timeSurvived > 0 && timeSurvived < 1.45) {
            player.setCurrentMovementState(Player.MovementState.UltimateDown);
        }
        if (entering && timeSurvived > 1.15) {
            enteringAnimationTimer += deltaTime;
            if (enteringAnimationTimer > 0.06) {
                currentEnteringFrame++;
                if (currentEnteringFrame >= 5) {
                    currentEnteringFrame = 0;
                }
                enteringAnimationTimer = 0;
            }
            if (timeSurvived > 1.5) {
                entering = false;
                player.setPosZ(0);
                player.setCurrentMovementState(Player.MovementState.Idle);
            }
        }

        // ultimate
        if (ultimateActive) {
            ultimate.update(deltaTime, this);
        }



        // player vs obstacle (no dash inside)
        player.update(deltaTime, this);
        double playerPosX = player.getPosX();
        double playerPosY = player.getPosY();
        double playerRadius = player.getHitbox();
        for (Obstacle obstacle : obstacles) {
            double left = obstacle.getPosX();
            double right = obstacle.getPosX() + obstacle.getWidth();
            double top = obstacle.getPosY();
            double bottom = obstacle.getPosY() + obstacle.getHeight();

            // prevent dash inside
            if (playerPosX > left && playerPosX < right && playerPosY > top && playerPosY < bottom) {
                double distanceLeft = playerPosX - left;
                double distanceRight = right - playerPosX;
                double distanceTop = playerPosY - top;
                double distanceBottom = bottom - playerPosY;
                double min = Math.min(Math.min(distanceLeft, distanceRight), Math.min(distanceTop, distanceBottom));
                if (min == distanceLeft) playerPosX = left - playerRadius;
                else if (min == distanceRight) playerPosX = right + playerRadius;
                else if (min == distanceTop) playerPosY = top - playerRadius;
                else if (min == distanceBottom) playerPosY = bottom + playerRadius;

            } else { // normal collision
                double closeToX = Math.max(obstacle.getPosX(), Math.min(playerPosX, obstacle.getPosX() + obstacle.getWidth()));
                double closeToY = Math.max(obstacle.getPosY(), Math.min(playerPosY, obstacle.getPosY() + obstacle.getHeight()));
                double distanceX = playerPosX - closeToX;
                double distanceY = playerPosY - closeToY;
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if (distance < playerRadius) {
                    if (distance > 0) {
                        double overlap = playerRadius - distance;
                        playerPosX += (distanceX / distance) * overlap;
                        playerPosY += (distanceY / distance) * overlap;
                    }
                }
            }
        }
        player.setPosX(playerPosX);
        player.setPosY(playerPosY);

        for (Weapon weapon : weaponList) {
            if (weapon != null) {
                weapon.update(deltaTime, this, false);
            }
        }

        double cameraPosX = player.getPosX() - (SCREEN_WIDTH / 2.0);
        double cameraPosY = player.getPosY() - (SCREEN_HEIGHT / 2.0);

        // crystal
        crystalAnimationTimer += deltaTime;
        if (crystalAnimationTimer > 0.1) {
            currentCrystalFrame++;
            if (currentCrystalFrame >= 24) {
                currentCrystalFrame = 0;
            }
            crystalAnimationTimer = 0;
        }


        // =========== Enemy ==============


        // difficulty
        difficultyMultiplier = 1 + (timeSurvived * 0.005);

        // >>>>>>>> Base enemy <<<<<<<<<
        enemySpawnTimer += deltaTime;
        if (timeSurvived > 300) {
            if (enemySpawnTimer >= 0.10) {
                spawnEnemy(0);
                enemySpawnTimer = 0;
            }
        } else if (timeSurvived > 60 && timeSurvived < 300) {
            if (enemySpawnTimer >= 0.20) {
                spawnEnemy(0);
                enemySpawnTimer = 0;
            }
        } else if (timeSurvived >= 30 && timeSurvived < 60) {
            if (enemySpawnTimer >= 0.40) {
                spawnEnemy(0);
                enemySpawnTimer = 0;
            }
        } else {
            if (enemySpawnTimer >= 0.80) {
                spawnEnemy(0);
                enemySpawnTimer = 0;
            }
        }

        if (timeSurvived > 40) {
            chargersSpawnTimer += deltaTime;
            if (chargersSpawnTimer >= 6.0) {
                spawnEnemy(1);
                chargersSpawnTimer = 0;
            }
        }
        if (timeSurvived > 60) {
            bossSpawnTimer += deltaTime;
            if (bossSpawnTimer >= 60 && !bossIsAlive) {
                spawnEnemy(2);
                bossSpawnTimer = 0;
                bossIsAlive = true;
            }
        }

        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {

                if (enemy instanceof  Boss) {
                    Boss boss = (Boss) enemy;
                    boss.update(deltaTime, player, difficultyMultiplier);
                } else
                    enemy.update(deltaTime);

                // player get attack
                if (enemy.checkCollision(player))
                    if (!player.isJumping())
                        // dodge check
                        if (Math.random() >= player.getDodgeChance()) {
                            double damage = 50 * difficultyMultiplier;
                            if (enemy.isCharger()) damage = 40 * difficultyMultiplier;
                            else if (enemy.isBoss()) damage = 100 * difficultyMultiplier;
                            if (player.getDashTimer() > 0) damage *= (1 - player.getDashDamageReduction());
                            enemy.doDamage(player, damage);

                        } else {
                            damageTexts.add(new DamageText(player.getPosX(), player.getPosY(), "Dodge!", false));
                        }

                // enemy vs obby
                if (!enemy.isCharger()) {
                    double enemyPosX = enemy.getPosX();
                    double enemyPosY = enemy.getPosY();
                    for (Obstacle obstacle : obstacles) {
                        double left = obstacle.getPosX();
                        double right = obstacle.getPosX() + obstacle.getWidth();
                        double top = obstacle.getPosY();
                        double bottom = obstacle.getPosY() + obstacle.getHeight();

                        if (enemyPosX + enemy.getHitbox() < left || enemyPosX - enemy.getHitbox() > right ||
                                enemyPosY + enemy.getHitbox() < top || enemyPosY - enemy.getHitbox() > bottom) {
                            continue;
                        }

                        if (enemyPosX > left && enemyPosX < right && enemyPosY > top && enemyPosY < bottom) {
                            double distanceLeft = enemyPosX - left;
                            double distanceRight = right - enemyPosX;
                            double distanceTop = enemyPosX - top;
                            double distanceBottom = bottom - enemyPosX;
                            double min = Math.min(Math.min(distanceLeft, distanceRight), Math.min(distanceTop, distanceBottom));
                            if (min == distanceLeft) enemyPosX = left - enemy.getHitbox();
                            else if (min == distanceRight) enemyPosX = right + enemy.getHitbox();
                            else if (min == distanceTop) enemyPosY = top - enemy.getHitbox();
                            else if (min == distanceBottom) enemyPosY = bottom + enemy.getHitbox();
                        } else {
                            double closeToX = Math.max(obstacle.getPosX(), Math.min(enemyPosX, obstacle.getPosX() + obstacle.getWidth()));
                            double closeToY = Math.max(obstacle.getPosY(), Math.min(enemyPosY, obstacle.getPosY() + obstacle.getHeight()));
                            double distanceX = enemyPosX - closeToX;
                            double distanceY = enemyPosY - closeToY;
                            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                            if (distance < enemy.getHitbox()) {
                                if (distance > 0) {
                                    double overlap = enemy.getHitbox() - distance;
                                    enemyPosX += (distanceX / distance) * overlap;
                                    enemyPosY += (distanceY / distance) * overlap;
                                }
                            }
                        }
                    }
                    enemy.setPosX(enemyPosX);
                    enemy.setPosY(enemyPosY);
                }
            }

            // chargerwave
            chargerWaveTimer += deltaTime;

            // enemy collision
            for (int i = 0; i < enemies.size(); i++) {
                for (int j = i + 1; j < enemies.size(); j++) {
                    if (i == j) continue;
                    Enemy enemy_1 = enemies.get(i);
                    Enemy enemy_2 = enemies.get(j);
                    if ((enemy_1.isCharger() || enemy_2.isCharger())) {
                        continue;
                    }
                    double distanceX = enemy_1.getPosX() - enemy_2.getPosX();
                    double distanceY = enemy_1.getPosY() - enemy_2.getPosY();
                    double distance = distanceX * distanceX + distanceY * distanceY;
                    if (distance < 200) {
                        if (enemy_1.checkCollision(enemy_2)) {
                            enemy_1.push(enemy_2);
                        }
                    }

                }
            }
            for (Enemy enemy : enemies) {
                if (player.checkCollision(enemy)) {
                    if (!player.isJumping())
                        player.push(enemy);
                }
            }
        }

        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            // check bound (swarm type)
            double boundDistanceX = player.getPosX() - enemy.getPosX();
            double boundDistanceY = player.getPosY() - enemy.getPosY();
            double killOnBound = boundDistanceX * boundDistanceX + boundDistanceY * boundDistanceY;
            if (enemy.isCharger() && killOnBound > 4000 * 4000) {
                enemies.remove(i);
            } else if (enemy.isBoss()) {
                continue;
            } else {
                if (killOnBound > 3000 * 3000) {
                    enemies.remove(i);
                }
            }
        }
        // enemies dead -> gem drop + get + kill count + coin
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.isDead()) {
                player.addKill();
                if (enemy.isBoss()) {
                    Experience exp = Experience.create(enemy.getPosX(), enemy.getPosY());
                    exp.setExpType(4);
                    exp.setExpValue((int)(3000 * difficultyMultiplier));
                    experience.add(exp);

                    player.setCoin(player.getCoin() + 200);

                    bossIsAlive = false;
                    bossSpawnTimer = 40;
                } else {
                    Experience exp = Experience.create(enemy.getPosX(), enemy.getPosY());
                    if (timeSurvived > 480) {
                        exp.setExpValue((int) (exp.getExpValue() * 10 * difficultyMultiplier * difficultyMultiplier / 1.5));
                    } else if (timeSurvived > 240 && timeSurvived < 480) {
                        exp.setExpValue((int) (exp.getExpValue() * 3 * difficultyMultiplier * difficultyMultiplier / 1.5));
                    } else {
                        exp.setExpValue((int) (exp.getExpValue() * difficultyMultiplier * difficultyMultiplier / 1.5));
                    }
                    experience.add(exp);
                    int coin = (int) (Math.random() * 15);
                    if (coin > 0) {
                        player.setCoin((int) (player.getCoin() + coin * player.getCoinMultiplier()));
                    }

                    if (!enemy.isBoss() && !enemy.isCharger()) {
                        if (Math.random() < 0.001) {
                            chests.add(new Chest(enemy.getPosX() - (Math.random() * 30 - 15), enemy.getPosY() - (Math.random() * 30 - 15)));
                        }
                        if (Math.random() < 0.01) {
                            steaks.add(new Steak(enemy.getPosX() - (Math.random() * 30 - 15), enemy.getPosY() - (Math.random() * 30 - 15)));
                        }
                        if (Math.random() < 0.01) {
                            energyDrinks.add(new EnergyDrink(enemy.getPosX(), enemy.getPosY()));
                        }

                    }
                }

                // necrotic heart
                if (hasItem("Necrotic Heart") && Math.random() < 0.05) {
                    explosions.add(new Explosion(enemy.getPosX(), enemy.getPosY()));
                    for (Enemy enemy1 : enemies) {
                        if (enemy1 != enemy) {
                            double distanceX = enemy1.getPosX() - enemy.getPosX();
                            double distanceY = enemy1.getPosY() - enemy.getPosY();
                            double distance = distanceX * distanceX + distanceY * distanceY;
                            if (distance < 250 * 250) {
                                enemy1.takeDamageAndEffectPlayer(player, 30 * (1 + player.getStatDamage() / 100), damageTexts, false);
                            }
                        }
                    }
                }

                enemies.remove(i);
            }
        }

        // exp
        for (int i = experience.size() - 1; i >= 0; i--) {
            Experience exp = experience.get(i);
            double expPosX = exp.getPosX();
            double expPosY = exp.getPosY();
            for (Obstacle obstacle : obstacles) {
                double left = obstacle.getPosX();
                double right = obstacle.getPosX() + obstacle.getWidth();
                double top = obstacle.getPosY();
                double bottom = obstacle.getPosY() + obstacle.getHeight();

                if (expPosX > left && expPosX < right && expPosY > top && expPosY < bottom) {
                    double distanceLeft = expPosX - left;
                    double distanceRight = right - expPosX;
                    double distanceTop = expPosY - top;
                    double distanceBottom = bottom - expPosY;
                    double min = Math.min(Math.min(distanceLeft, distanceRight), Math.min(distanceTop, distanceBottom));
                    if (min == distanceLeft) expPosX = left - exp.getHitbox();
                    else if (min == distanceRight) expPosX = right + exp.getHitbox();
                    else if (min == distanceTop) expPosY = top - exp.getHitbox();
                    else if (min == distanceBottom) expPosY = bottom + exp.getHitbox();
                }
                exp.setPosX(expPosX);
                exp.setPosY(expPosY);
            }

            double distancePosX = player.getPosX() - exp.getPosX();
            double distancePosY = player.getPosY() - exp.getPosY();
            double distance = Math.sqrt(distancePosX * distancePosX + distancePosY * distancePosY);
            if ((distance < player.getMagnetRadius() || drinkMagnetTimer > 0) && !player.isJumping()) {
                double pullingSpeed = 600 * deltaTime;
                exp.setPosX(exp.getPosX() + (distancePosX / distance) * pullingSpeed);
                exp.setPosY(exp.getPosY() + (distancePosY / distance) * pullingSpeed);
            }
            if (player.checkCollision(exp) && !player.isJumping()) {
                player.addExperience(exp.getExpValue());
                Experience.release(exp);
                experience.remove(i);
            }
        }

        // projectile
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.updateProj(deltaTime, this);

            if (projectile.isDone()) {
                projectiles.remove(i);
            }
        }

        // aura sigma
        if (aura != null) {
            if (aura.inArea(this)) {
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    double distanceX = enemy.getPosX() - player.getPosX();
                    double distanceY = enemy.getPosY() - player.getPosY();
                    double distance = (distanceX * distanceX + distanceY * distanceY);
                    double auraRadius = aura.getRadius() * (1 + aura.getBonusSize() / 100);
                    if (distance <= auraRadius * auraRadius) {
                        double auraDamage = 15 * (1 + aura.getBonusDamage() / 100);
                        enemy.takeDamageAndEffectPlayer(player, auraDamage, damageTexts, false);
                        enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 50, -enemy.getFaceToY() * 50, 0.1);
                    }
                }
            }
        }

        // chest
        for (int i = chests.size() - 1; i >= 0; i--) {
            Chest chest = chests.get(i);
            if (player.checkCollision(chest) && !player.isJumping()) {
                openingChest(this);
                chests.remove(i);
            }
        }

        // steak
        for (int i = steaks.size() - 1; i >= 0; i--) {
            Steak steak = steaks.get(i);
            if (player.checkCollision(steak) && !player.isJumping()) {
                if (player.getMaxHealth() > player.getCurrentHealth()) {
                    player.setCurrentHealth(player.getCurrentHealth() + steak.getHealAmount());
                }
                steaks.remove(i);
            }
        }

        // slash
        for (int i = slashes.size() - 1; i >= 0; i--) {
            Slash slash = slashes.get(i);
            slash.update(deltaTime);
            if (!slash.isSlashHit()) {
                for (Enemy enemy : enemies) {
                    double distancePosX = enemy.getPosX() - slash.getPosX();
                    double distancePosY = enemy.getPosY() - slash.getPosY();
                    double distance = Math.sqrt(distancePosX * distancePosX + distancePosY * distancePosY);
                    if (distance < 700) {
                        double slashCheckFront = (distancePosX / distance) * Math.cos(Math.toRadians(slash.getAngle())) + (distancePosY / distance) * Math.sin(Math.toRadians(slash.getAngle()));
                        if (slashCheckFront > 0.90) {
                            double slashDamage = 60 * (1 + player.getStatDamage() / 100);
                            enemy.takeDamageAndEffectPlayer(player, slashDamage, damageTexts, false);
                            enemy.smoothHitboxContactPushOut(Math.cos(Math.toRadians(slash.getAngle())) * 200,
                                    Math.sin(Math.toRadians(slash.getAngle())) * 200,
                                    0.08
                            );
                            slash.setSlashHit(true);
                        }
                    }
                }
                if (slash.isSlashHit())
                    player.setCurrentStamina(player.getCurrentStamina() - 5);
            }
            if (slash.fade()) slashes.remove(i);

        }

        for (int i = jumpingSlashes.size() - 1; i >= 0; i--) {
            JumpingSlash jumpingSlash = jumpingSlashes.get(i);
            jumpingSlash.update(deltaTime);
            for (Enemy enemy : enemies) {
                if (!jumpingSlash.getEnemyList().contains(enemy)) {
                    double distanceX = enemy.getPosX() - player.getPosX();
                    double distanceY = enemy.getPosY() - player.getPosY();
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                    if (distance < 300) {
                        enemy.takeDamageAndEffectPlayer(player, 80* (1 + player.getStatDamage() / 100), damageTexts, false);
                        enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 300, -enemy.getFaceToY() * 300, 0.2);
                        jumpingSlash.getEnemyList().add(enemy);
                        player.setSlashCooldown(player.getSlashCooldown() - 0.01);
                    }
                }
            }
            player.setCanJumpingSlashOnce(false);
            if (jumpingSlash.fade()) jumpingSlashes.remove(i);
        }

        for (int i = damageTexts.size() - 1; i >= 0; i--) {
            DamageText damageText = damageTexts.get(i);
            damageText.update(deltaTime);
            if (damageText.isFade()) {
                damageTexts.remove(i);
            }
        }

        // Cross slash X
        for (int i = crossSlashes.size() - 1; i >= 0; i--) {
            CrossSlash crossSlash = crossSlashes.get(i);
            crossSlash.update(deltaTime);
            if (crossSlash.isFade()) {
                crossSlashes.remove(i);
            } else {
                for (Enemy enemy : enemies) {
                    if (!crossSlash.getHitEnemies().contains(enemy)) {
                        double distanceX = enemy.getPosX() - crossSlash.getPosX();
                        double distanceY = enemy.getPosY() - crossSlash.getPosY();
                        if (distanceX * distanceX + distanceY * distanceY < 300 * 300) {
                            crossSlash.getHitEnemies().add(enemy);
                            double damage = 150 * (1 + player.getStatDamage() / 100);
                            enemy.takeDamageAndEffectPlayer(player, damage, damageTexts, false);
                            enemy.smoothHitboxContactPushOut(-player.getFaceToX() * 100, -player.getFaceToY() * 100, 0.2);
                        }
                    }
                }
            }
        }



        // =================== ITEM ====================


        if (drinkMagnetTimer > 0) drinkMagnetTimer -= deltaTime;
        if (rainbowTimer > 0) rainbowTimer -= deltaTime;
        if (repulsorVisualTimer > 0) repulsorVisualTimer -= deltaTime;
        if (gasVisualTimer > 0) gasVisualTimer -= deltaTime;
        if (adrenalCooldown > 0) adrenalCooldown -= deltaTime;


        // energy drink powerup
        for (int i = energyDrinks.size() - 1; i >= 0; i--) {
            EnergyDrink drink = energyDrinks.get(i);
            if (player.checkCollision(drink) && !player.isJumping()) {
                int type = drink.getDrinkType();
                if (type == 0) {
                    drinkShieldTimer = 5.0;
                    player.setShielded(true);
                }
                else if (type == 1) {
                    drinkSpeedTimer = 5.0;
                    if (!drinkSpeedActive) {
                        drinkSpeedActive = true;
                        player.addSpeedMultiplier(1.0);
                        player.setStatAtkSpeed(player.getStatAtkSpeed() + 20);
                        for (Weapon weapon : weaponList) weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() + 20);
                    }
                }
                else if (type == 2) {
                    drinkMagnetTimer = 5.0;
                }
                energyDrinks.remove(i);
            }
        }
        if (drinkShieldTimer > 0) {
            drinkShieldTimer -= deltaTime;
            if (drinkShieldTimer <= 0) player.setShielded(false);
        }
        if (drinkSpeedTimer > 0) {
            drinkSpeedTimer -= deltaTime;
            if (drinkSpeedTimer <= 0 && drinkSpeedActive) {
                drinkSpeedActive = false;
                player.addSpeedMultiplier(-1.0);
                player.setStatAtkSpeed(player.getStatAtkSpeed() - 20);
                for (Weapon weapon : weaponList) weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() - 20);
            }
        }


        explosionAnimationTimer += deltaTime;
        if (explosionAnimationTimer > 0.01) {
            currentExplosionFrame++;
            if (currentExplosionFrame >= 40)
                currentExplosionFrame = 0;
            explosionAnimationTimer = 0;
        }
        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion ex = explosions.get(i);
            ex.update(deltaTime);
            if (ex.isFade()) explosions.remove(i);
        }

        // black hole
        blackholeAnimationTimer += deltaTime;
        if (blackholeAnimationTimer > 0.05) {
            currentBlackholeFrame++;
            if (currentBlackholeFrame >= 7)
                currentBlackholeFrame = 0;
            blackholeAnimationTimer = 0;
        }
        blackHoles.addAll(player.getPendingBlackHoles());
        player.getPendingBlackHoles().clear();
        for (int i = blackHoles.size() - 1; i >= 0; i--) {
            double[] blackHole = blackHoles.get(i);
            blackHole[2] -= deltaTime;
            boolean dealRapidDamage = false;
            if (blackHole.length > 3) {
                blackHole[3] -= deltaTime;
                if (blackHole[3] <= 0) {
                    dealRapidDamage = true;
                    blackHole[3] = 0.1;
                }
            }
            for (Enemy enemy : enemies) {
                double distanceX = blackHole[0] - enemy.getPosX();
                double distanceY = blackHole[1] - enemy.getPosY();
                if (distanceX * distanceX + distanceY * distanceY < 800 * 800) {
                    enemy.setPosX(enemy.getPosX() + distanceX * 2 * deltaTime);
                    enemy.setPosY(enemy.getPosY() + distanceY * 2 * deltaTime);
                    if (dealRapidDamage) {
                        enemy.takeDamageAndEffectPlayer(player, 5, damageTexts, false);
                    }
                }
            }
            if (blackHole[2] <= 0) blackHoles.remove(i);
        }

        // final weapon
        finalWeaponAnimationTimer += deltaTime;
        if (finalWeaponAnimationTimer > 0.02) {
            currentFinalWeaponFrame++;
            if (currentFinalWeaponFrame >= 60)
                currentFinalWeaponFrame = 0;
            finalWeaponAnimationTimer = 0;
        }


        // item update
        for (Item item : itemList) {
            item.updateEffect(deltaTime, this, player);
        }

    }



    //=========================== Method =================================



    public boolean insideObstacle(double posX, double posY) {
        if (obstacles == null) return false;
        for (Obstacle obstacle : obstacles) {
            if (posX >= obstacle.getPosX() && posX <= obstacle.getPosX() + obstacle.getWidth() &&
                posY >= obstacle.getPosY() && posY <= obstacle.getPosY() + obstacle.getHeight()) {
                return true;
            }
        } return false;
    }


    // Enemies
    public void spawnEnemy(int type) {
        double angle = 2 * Math.PI * Math.random();
        double spawnRadius = Math.max(SCREEN_WIDTH, SCREEN_HEIGHT) * 1.5;
        double spawnX = player.getPosX() + spawnRadius * Math.cos(angle);
        double spawnY = player.getPosY() + spawnRadius * Math.sin(angle);
        double directionX = -Math.cos(angle);
        double directionY = -Math.sin(angle);

        while (insideObstacle(spawnX, spawnY)) {
            angle = 2 * Math.PI * Math.random();
            spawnX = player.getPosX() + spawnRadius * Math.cos(angle);
            spawnY = player.getPosY() + spawnRadius * Math.sin(angle);
            directionX = -Math.cos(angle);
            directionY = -Math.sin(angle);
        }

        switch (type) {
            case 0 -> {
            Enemy enemy = new Enemy(spawnX, spawnY, player);
            enemy.addMaxHealth(difficultyMultiplier);
            if (timeSurvived > 300) {
                enemy.setSpeed(100 * difficultyMultiplier);
                enemy.setMaxHealth(200 * difficultyMultiplier);
            } else if (timeSurvived > 120 && timeSurvived < 300) {
                enemy.setMaxHealth(200);
                enemy.setSpeed(350);
            } else {
                enemy.setMaxHealth(120);
                enemy.setSpeed(250);
            }
            enemies.add(enemy);

            }
            case 1 -> {
                int swarmWidth = 30;
                int waveCount = 3;
                double rotateAngle = angle + (Math.PI / 2);
                    for (int w = 0; w < waveCount; w++) {
                        double depthOffset = w * 280.0;
                        for (int i = 0; i < swarmWidth; i++) {
                            double widthOffset = (i - (swarmWidth / 2.0)) * 60;
                            double randomJitterX = (Math.random() - 0.5) * 150;
                            double randomJitterY = (Math.random() - 0.5) * 150;
                            double cSpawnX = spawnX + Math.cos(rotateAngle) * widthOffset;
                            double cSpawnY = spawnY + Math.sin(rotateAngle) * widthOffset;
                            cSpawnX += (Math.cos(angle) * depthOffset) + randomJitterX;
                            cSpawnY += (Math.sin(angle) * depthOffset) + randomJitterY;
                            Charger charger = new Charger(cSpawnX, cSpawnY, player);
                            charger.setDirectionX(directionX);
                            charger.setDirectionY(directionY);
                            charger.addMaxHealth(difficultyMultiplier);

                            if (timeSurvived > 180) {
                                charger.setSpeed(400 * difficultyMultiplier);
                                charger.setMaxHealth(120 * difficultyMultiplier);
                            }
                            enemies.add(charger);
                    }
                    chargerWaveTimer = 0;
                }
            }
            case 2 -> {
                Boss boss = new Boss(spawnX, spawnY, player);
                if (timeSurvived > 180) {
                    boss.setSpeed(200 * difficultyMultiplier);
                    boss.setMaxHealth(boss.getMaxHealth() + 1000 *  difficultyMultiplier);
                    boss.setHealth(boss.getMaxHealth());
                    boss.setMaxCooldown(boss.getMaxCooldown() / difficultyMultiplier);
                }
                enemies.add(boss);
            }
        }
    }


        public void triggerSlash(double controllerPosX, double controllerPosY) {
        if (!player.canSlash()) return;
        player.resetSlashCooldown();
        if (player.isJumping() && player.isCanJumpingSlashOnce() && player.getCurrentStamina() >= 10 * player.getSkillStaminaMultiplier()) {
            SoundManager.jumpingSlashSound.play();
            player.triggerJumpingDashAttack();
            player.setCurrentStamina(player.getCurrentStamina() - 10 * player.getSkillStaminaMultiplier());
            jumpingSlashes.add(new JumpingSlash(player.getPosX(), player.getPosY(), player.getPosZ(), player));
            return;
        } else {
            SoundManager.skill1Sound.play();
        }
        double distanceX = controllerPosX - player.getPosX();
        double distanceY = controllerPosY - player.getPosY();
        double angle = Math.atan2(distanceY, distanceX);
        double slashPosX = player.getPosX() + Math.cos(angle) * 40;
        double slashPosY = player.getPosY() + Math.sin(angle) * 40;
        slashes.add(new Slash(slashPosX, slashPosY, player.getPosZ(), Math.toDegrees(angle)));
    }

    public void triggerSlash() {
        if (!player.canSlash()) return;
        player.resetSlashCooldown();
        if (player.isJumping() && player.isCanJumpingSlashOnce() && player.getCurrentStamina() >= 10 * player.getSkillStaminaMultiplier()) {
            SoundManager.jumpingSlashSound.play();
            player.triggerJumpingDashAttack();
            player.setCurrentStamina(player.getCurrentStamina() - 10 * player.getSkillStaminaMultiplier());
            jumpingSlashes.add(new JumpingSlash(player.getPosX(), player.getPosY(), player.getPosZ(), player));
            return;
        } else {
            SoundManager.skill1Sound.play();
        }
        double distanceX = player.getFaceToX();
        double distanceY = player.getFaceToY();
        double angle = Math.atan2(distanceY, distanceX);
        double slashPosX = player.getPosX() + Math.cos(angle) * 40;
        double slashPosY = player.getPosY() + Math.sin(angle) * 40;
        slashes.add(new Slash(slashPosX, slashPosY, player.getPosZ(), Math.toDegrees(angle)));
    }


    public void triggerCrossSlash() {
        if (player.getGameStartTimer() > 0) return;
        if (player.getCrossSlashCooldown() <= 0 && !player.isJumping() && player.getCurrentStamina() >= 60 * player.getSkillStaminaMultiplier()) {
            SoundManager.skill2Sound.play();
            player.setCurrentStamina(player.getCurrentStamina() - (60 * player.getSkillStaminaMultiplier()));
            player.setCrossSlashCooldown(8.0);
            double angle = Math.atan2(player.getFaceToY(), player.getFaceToX());
            double spawnX = player.getPosX() + (player.getFaceToX() * 400);
            double spawnY = player.getPosY() + (player.getFaceToY() * 400);
            crossSlashes.add(new CrossSlash(spawnX, spawnY, angle));
        }
    }


    // ultimate
    public void triggerUltimate() {
        if (player.getGameStartTimer() > 0) return;
        if (player.getUltimateCooldown() <= 0 && player.getCurrentStamina() >= 100) {
            player.setCurrentStamina(player.getCurrentStamina() - 100);
            player.setUltimateCooldown(60.0);
            player.setShielded(true);
            ultimateActive = true;
            ultimate.setUltimateTimer(0);
            ultimateSoundCheck = 0;
            SoundManager.ultimatePlayer.stop();
        }
    }



    // set weapon

    public int getWeaponLevel(WeaponType weaponType) {
        if (weaponList.isEmpty()) {
            return 0;
        }
        for (Weapon weapon : weaponList) {
            if (weapon == null) {
                return 0;
            }
            if (weapon.getType().equals(weaponType)) return weapon.getLevel();
        }
        return 0;
    }

    public void upgradeExistWeapon(Weapon weapon, Rarity rarity) {
        this.uiNeedsUpdate = true;
        for (Weapon weapon_l : weaponList) {
            if (weapon.getType() == (weapon_l.getType())) {
                weapon_l.setLevel(weapon_l.getLevel() + 1);

                weapon_l.addBonusDamage(weapon.getBonusDamage());
                weapon_l.addBonusAttackSpeed(weapon.getBonusAttackSpeed());
                weapon_l.addBonusCritRate(weapon.getBonusCritRate());
                weapon_l.addBonusCritDmg(weapon.getBonusCritDmg());
                weapon_l.addBonusSize(weapon.getBonusSize());
                weapon_l.addBonusProjCount(weapon.getBonusProjCount());

                weapon.coreUpgrade(this, rarity.getMultiplyBonusCore());
            }
        }
    }

    public boolean isExistWeapon(Weapon weapon) {
        if (weaponList.isEmpty()) {
            return false;
        }
        for (Weapon weapon_l : weaponList) {
            if (weapon_l == null) {
                return false;
            }
            if (weapon.getType().equals(weapon_l.getType())) {
                return true;
            }
        }
        return false;
    }

    public void addNewWeapon(Weapon weapon) {
        this.uiNeedsUpdate = true;
        weaponList.add(weapon);
        if (weapon.isCore()) {
            weapon.coreUpgrade(this, 1);
        } else {
            for (Weapon core : weaponList) {
                if (core.isCore())
                    core.applyCoreEffect(weapon, core.getAllBonus());
            }
        }
        switch (weapon.getType()) {
            case SWORD -> this.sword = (Sword) weapon;
            case BOW -> this.bow = (Bow) weapon;
            case BOOMERANG -> this.boomerangWeapon = (BoomerangWeapon) weapon;
            case ROCKS -> this.orbitRock = (OrbitRock) weapon;
            case AURA -> this.aura = (Aura) weapon;
            case LIGHTNING -> this.lightningWeapon = (LightningWeapon) weapon;
        }
    }

    public void addItem(Item item) {
        this.uiNeedsUpdate = true;
        itemList.add(item);
    }

    public boolean hasItem(String itemName) {
        for (Item item1 : itemList) {
            if (item1.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }





    // GAME
    public void triggerScreenShake(double duration, double intensity) {
        this.screenShakeTimer = duration;
        this.screenShakeIntensity = intensity;
    }

    public void pauseGame() {
        this.gameStop = true;
        for (AudioClip audioClip : SoundManager.getSoundManagersLists()) {
            audioClip.stop();
        }
        if (SoundManager.ultimatePlayer != null)
            SoundManager.ultimatePlayer.pause();
        if (SoundManager.enteringPlayer != null)
            SoundManager.enteringPlayer.pause();
    }

    public void resumeGame() {
        this.gameStop = false;
        if (SoundManager.enteringPlayer != null)
            SoundManager.enteringPlayer.play();

        this.item = null;
    }



    //==================================================





    // s, g



    public boolean isShowMiniMap() {
        return showMiniMap;
    }

    public void setShowMiniMap(boolean showMiniMap) {
        this.showMiniMap = showMiniMap;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public double getEnemySpawnTimer() {
        return enemySpawnTimer;
    }

    public void setEnemySpawnTimer(double enemySpawnTimer) {
        this.enemySpawnTimer = enemySpawnTimer;
    }

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }

    public List<Slash> getSlashes() {
        return slashes;
    }

    public void setSlashes(List<Slash> slashes) {
        this.slashes = slashes;
    }

    public List<JumpingSlash> getJumpingSlashes() {
        return jumpingSlashes;
    }

    public void setJumpingSlashes(List<JumpingSlash> jumpingSlashes) {
        this.jumpingSlashes = jumpingSlashes;
    }

    public List<Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(List<Arrow> arrows) {
        this.arrows = arrows;
    }

    public Bow getBow() {
        return bow;
    }

    public void setBow(Bow bow) {
        this.bow = bow;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public Aura getAura() {
        return aura;
    }

    public void setAura(Aura aura) {
        this.aura = aura;
    }

    public OrbitRock getOrbitRock() {
        return orbitRock;
    }

    public void setOrbitRock(OrbitRock orbitRock) {
        this.orbitRock = orbitRock;
    }

    public List<Chest> getChests() {
        return chests;
    }

    public void setChests(List<Chest> chests) {
        this.chests = chests;
    }

    public List<Steak> getSteaks() {
        return steaks;
    }

    public void setSteaks(List<Steak> steaks) {
        this.steaks = steaks;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isGameStop() {
        return gameStop;
    }

    public void setGameStop(boolean gameStop) {
        this.gameStop = gameStop;
    }

    public double getTimeSurvived() {
        return timeSurvived;
    }

    public void setTimeSurvived(double timeSurvived) {
        this.timeSurvived = timeSurvived;
    }

    public double getScreenShakeTimer() {
        return screenShakeTimer;
    }

    public double getScreenShakeIntensity() {
        return screenShakeIntensity;
    }

    public List<DamageText> getDamageTexts() {
        return damageTexts;
    }

    public void setDamageTexts(List<DamageText> damageTexts) {
        this.damageTexts = damageTexts;
    }

    public List<Rock> getRocks() {
        return rocks;
    }

    public void setRocks(List<Rock> rocks) {
        this.rocks = rocks;
    }


    public double getChargersSpawnTimer() {
        return chargersSpawnTimer;
    }

    public void setChargersSpawnTimer(double chargersSpawnTimer) {
        this.chargersSpawnTimer = chargersSpawnTimer;
    }

    public List<Boomerang> getBoomerangs() {
        return boomerangs;
    }

    public void setBoomerangs(List<Boomerang> boomerangs) {
        this.boomerangs = boomerangs;
    }

    public BoomerangWeapon getBoomerangWeapon() {
        return boomerangWeapon;
    }

    public void setBoomerangWeapon(BoomerangWeapon boomerangWeapon) {
        this.boomerangWeapon = boomerangWeapon;
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }

    public double getBossSpawnTimer() {
        return bossSpawnTimer;
    }

    public void setBossSpawnTimer(double bossSpawnTimer) {
        this.bossSpawnTimer = bossSpawnTimer;
    }

    public boolean isBossIsAlive() {
        return bossIsAlive;
    }

    public void setBossIsAlive(boolean bossIsAlive) {
        this.bossIsAlive = bossIsAlive;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public void setDifficultyMultiplier(double difficultyMultiplier) {
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public LightningWeapon getLightningWeapon() {
        return lightningWeapon;
    }

    public void setLightningWeapon(LightningWeapon lightningWeapon) {
        this.lightningWeapon = lightningWeapon;
    }

    public List<LightningEffect> getLightningEffects() {
        return lightningEffects;
    }

    public void setLightningEffects(List<LightningEffect> lightningEffects) {
        this.lightningEffects = lightningEffects;
    }

    public List<CrossSlash> getCrossSlashes() {
        return crossSlashes;
    }

    public void setCrossSlashes(List<CrossSlash> crossSlashes) {
        this.crossSlashes = crossSlashes;
    }

    public List<double[]> getBlackHoles() {
        return blackHoles;
    }

    public void setBlackHoles(List<double[]> blackHoles) {
        this.blackHoles = blackHoles;
    }

    public double getRainbowTimer() {
        return rainbowTimer;
    }

    public void setRainbowTimer(double rainbowTimer) {
        this.rainbowTimer = rainbowTimer;
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public void setExplosions(List<Explosion> explosions) {
        this.explosions = explosions;
    }

    public double getRepulsorVisualTimer() {
        return repulsorVisualTimer;
    }

    public void setRepulsorVisualTimer(double repulsorVisualTimer) {
        this.repulsorVisualTimer = repulsorVisualTimer;
    }

    public double getGasVisualTimer() {
        return gasVisualTimer;
    }

    public void setGasVisualTimer(double gasVisualTimer) {
        this.gasVisualTimer = gasVisualTimer;
    }

    public int getCurrentFinalWeaponFrame() {
        return currentFinalWeaponFrame;
    }

    public void setCurrentFinalWeaponFrame(int currentFinalWeaponFrame) {
        this.currentFinalWeaponFrame = currentFinalWeaponFrame;
    }

    public double getFinalWeaponAnimationTimer() {
        return finalWeaponAnimationTimer;
    }

    public void setFinalWeaponAnimationTimer(double finalWeaponAnimationTimer) {
        this.finalWeaponAnimationTimer = finalWeaponAnimationTimer;
    }


    public void setGlitchStrikes(List<double[]> glitchStrikes) {
        this.glitchStrikes = glitchStrikes;
    }

    public List<EnergyDrink> getEnergyDrinks() {
        return energyDrinks;
    }

    public void setEnergyDrinks(List<EnergyDrink> energyDrinks) {
        this.energyDrinks = energyDrinks;
    }

    public double getEnergyDrinkTimer() {
        return energyDrinkTimer;
    }

    public void setEnergyDrinkTimer(double energyDrinkTimer) {
        this.energyDrinkTimer = energyDrinkTimer;
    }

    public boolean isEnergyDrinkActive() {
        return energyDrinkActive;
    }

    public void setEnergyDrinkActive(boolean energyDrinkActive) {
        this.energyDrinkActive = energyDrinkActive;
    }

    public double getDrinkShieldTimer() {
        return drinkShieldTimer;
    }

    public void setDrinkShieldTimer(double drinkShieldTimer) {
        this.drinkShieldTimer = drinkShieldTimer;
    }

    public double getDrinkSpeedTimer() {
        return drinkSpeedTimer;
    }

    public void setDrinkSpeedTimer(double drinkSpeedTimer) {
        this.drinkSpeedTimer = drinkSpeedTimer;
    }

    public boolean isDrinkSpeedActive() {
        return drinkSpeedActive;
    }

    public void setDrinkSpeedActive(boolean drinkSpeedActive) {
        this.drinkSpeedActive = drinkSpeedActive;
    }

    public double getDrinkMagnetTimer() {
        return drinkMagnetTimer;
    }

    public void setDrinkMagnetTimer(double drinkMagnetTimer) {
        this.drinkMagnetTimer = drinkMagnetTimer;
    }

    public boolean isUltimateActive() {
        return ultimateActive;
    }

    public void setUltimateActive(boolean ultimateActive) {
        this.ultimateActive = ultimateActive;
    }

    public void setScreenShakeTimer(double screenShakeTimer) {
        this.screenShakeTimer = screenShakeTimer;
    }

    public double getChargerWaveTimer() {
        return chargerWaveTimer;
    }

    public void setChargerWaveTimer(double chargerWaveTimer) {
        this.chargerWaveTimer = chargerWaveTimer;
    }

    public double getAdrenalCooldown() {
        return adrenalCooldown;
    }

    public void setAdrenalCooldown(double adrenalCooldown) {
        this.adrenalCooldown = adrenalCooldown;
    }

    public void setScreenShakeIntensity(double screenShakeIntensity) {
        this.screenShakeIntensity = screenShakeIntensity;
    }

    public boolean isUiNeedsUpdate() {
        return uiNeedsUpdate;
    }

    public void setUiNeedsUpdate(boolean uiNeedsUpdate) {
        this.uiNeedsUpdate = uiNeedsUpdate;
    }

    public List<double[]> getGlitchStrikes() {
        return glitchStrikes;
    }

    public int getCurrentCrystalFrame() {
        return currentCrystalFrame;
    }

    public void setCurrentCrystalFrame(int currentCrystalFrame) {
        this.currentCrystalFrame = currentCrystalFrame;
    }

    public double getCrystalAnimationTimer() {
        return crystalAnimationTimer;
    }

    public void setCrystalAnimationTimer(double crystalAnimationTimer) {
        this.crystalAnimationTimer = crystalAnimationTimer;
    }

    public int getCurrentExplosionFrame() {
        return currentExplosionFrame;
    }

    public void setCurrentExplosionFrame(int currentExplosionFrame) {
        this.currentExplosionFrame = currentExplosionFrame;
    }

    public double getExplosionAnimationTimer() {
        return explosionAnimationTimer;
    }

    public void setExplosionAnimationTimer(double explosionAnimationTimer) {
        this.explosionAnimationTimer = explosionAnimationTimer;
    }

    public int getCurrentBlackholeFrame() {
        return currentBlackholeFrame;
    }

    public void setCurrentBlackholeFrame(int currentBlackholeFrame) {
        this.currentBlackholeFrame = currentBlackholeFrame;
    }

    public double getBlackholeAnimationTimer() {
        return blackholeAnimationTimer;
    }

    public void setBlackholeAnimationTimer(double blackholeAnimationTimer) {
        this.blackholeAnimationTimer = blackholeAnimationTimer;
    }

    public int getCurrentEnteringFrame() {
        return currentEnteringFrame;
    }

    public void setCurrentEnteringFrame(int currentEnteringFrame) {
        this.currentEnteringFrame = currentEnteringFrame;
    }

    public double getEnteringAnimationTimer() {
        return enteringAnimationTimer;
    }

    public void setEnteringAnimationTimer(double enteringAnimationTimer) {
        this.enteringAnimationTimer = enteringAnimationTimer;
    }

    public boolean isEntering() {
        return entering;
    }

    public void setEntering(boolean entering) {
        this.entering = entering;
    }

    public void setWeaponList(List<Weapon> weaponList) {
        this.weaponList = weaponList;
    }

    public int getEnteringSoundCheck() {
        return enteringSoundCheck;
    }

    public void setEnteringSoundCheck(int enteringSoundCheck) {
        this.enteringSoundCheck = enteringSoundCheck;
    }

    public int getUltimateSoundCheck() {
        return ultimateSoundCheck;
    }


    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public Ultimate getUltimate() {
        return ultimate;
    }

    public void setUltimate(Ultimate ultimate) {
        this.ultimate = ultimate;
    }

    public void setUltimateSoundCheck(int ultimateSoundCheck) {
        this.ultimateSoundCheck = ultimateSoundCheck;
    }
}
