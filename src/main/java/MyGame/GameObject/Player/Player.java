package MyGame.GameObject.Player;

import MyGame.Game.GameEngine;
import MyGame.GameObject.GameObject;
import MyGame.Game.SoundManager;
import MyGame.Game.World;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    private final double MAP_LIMIT = 20000;

    private final double MAX_SPEED = 500;
    private final double ACCELERATION = 1500;
    private double FRICTION = 600;
    private final double GRAVITY = 8000;
    private final double JUMP_SPEED = 2000;

    private double timer;
    private double velocityX;
    private double velocityY;
    private double directionToX;
    private double directionToY;
    private double faceToX;
    private double faceToY;
    private double mouseFaceToX;
    private double mouseFaceToY;
    private double currentAngle;
    private double posZ;
    private double velocityZ;

    private boolean isJumping;
    private double jumpTimer;
    private final double JUMP_COOLDOWN = 0.2;

    private final double DASH_COOLDOWN = 0.6;
    private final double DASH_DURATION = 0.2;
    private final double DASH_SPEED = 800;
    private double smoothDash = 0.3;

    private double dashTimer = 0;
    private double dashCooldown = 0;
    private double maxStamina;
    private double currentStamina;
    private double staminaTimer;

    private double maxHealth;
    private double currentHealth;

    private int experience = 0;
    private int expToLevel = 50;
    private int pendingLevelup = 0;
    private int playerLevel = 0;
    private int coin = 0;
    private double magnetRadius = 150;
    private boolean shielded = false;

    private int enemiesKilled = 0;

    private double speedMultiplier = 1.0;
    private double expMultiplier = 1.0;
    private double coinMultiplier = 1.0;
    private double luck = 0;
    private double currentOverHeal;
    private double maxOverHeal;
    private double lifeSteal;
    private double regeneration;
    private double regenerationTimer = 0;

    // skill
    private double slashMaxCooldown = 2;
    private double slashCooldown = 0;
    private double slashCooldownMultiplier = 0;
    private boolean isJumpingSlashing = false;
    private boolean canJumpingSlashOnce = true;
    private double crossSlashCooldown = 0;
    private double ultimateCooldown = 0;

    // other stat
    private double statDamage = 0;
    private double statAtkSpeed = 0;
    private double statCritDamage = 0;
    private double statCritRate = 0;
    private double statProjCount = 0;
    private double statSize = 0;

    // item stat
    private double dodgeChance = 0;
    private boolean immuneToSlow = false;
    private double skillStaminaMultiplier = 1.0;
    private double dashDamageReduction = 0;
    private double instantKillChance = 0;
    private int maxItemSlot = 10;
    private boolean hasDeepestVoid = false;
    private List<double[]> pendingBlackHoles = new ArrayList<>();
    private boolean hasFinalWeapon = false;

    // animation
    private int currentFrame = 0;
    private double animationTimer = 0.0;
    private int checkSetFrame = 0;
    public enum MovementState {
        Idle(0, 0.15, 6),
        Run(1, 0.08, 5),
        Dash(2, 0.10, 2),
        JumpUp(3, 0.06, 1),
        JumpDown(3, 0.06, 1),
        Skill(4, 0.04, 3),
        SkillJump(4, 0.12, 1),
        Skill2(4, 0.04, 3),
        UltimateUp(5, 0.02, 1),
        UltimateDown(5, 0.02, 1);

        public final int checkSetFrame;
        public final double frameDuration;
        public final int maxFrames;

        MovementState(int checkSetFrame, double frameDuration, int maxFrames) {
            this.checkSetFrame = checkSetFrame;
            this.frameDuration = frameDuration;
            this.maxFrames = maxFrames;
        }
    }
    private MovementState currentMovementState;
    private double flashTimer = 0;

    // gamestart
    private double gameStartTimer = 2.0;

    public Player(double posX, double posY) {
        super(posX, posY, 25);
        this.velocityX = 0;
        this.velocityY = 0;
        this.currentAngle = 0;
        this.posZ = 7500;
        this.velocityZ = 0;

        this.isJumping = true;
        this.jumpTimer = 0;

        this.maxHealth = 3000;
        this.currentHealth = maxHealth;
        this.timer = 0;

        this.maxStamina = 100;
        this.currentStamina = 100;
        this.staminaTimer = 0;

        this.maxOverHeal = 0;
        this.lifeSteal = 0;
        this.currentOverHeal = 0;
        this.regeneration = 2;

        this.currentMovementState = MovementState.UltimateDown;
    }

    public void setFaceTo(double directionToX, double directionToY, double deltaTime) {
        if (this.directionToX != 0 || this.directionToY != 0) {

            double targetAngle = Math.toDegrees(Math.atan2(directionToY, directionToX));
            double angleDiff = (targetAngle - currentAngle + 360) % 360;
            if (angleDiff > 180) angleDiff -= 360;

            currentAngle += angleDiff * 20 * deltaTime;

            this.faceToX = Math.cos(Math.toRadians((currentAngle)));
            this.faceToY = Math.sin(Math.toRadians((currentAngle)));
        }
    }

    public void update(double deltaTime) {}

    public void update(double deltaTime, World world) {
        if (timer > 0) {
            timer -= deltaTime;
        }
        if (dashCooldown > 0) {
            dashCooldown -= deltaTime;
        }
        if (staminaTimer > 0) {
            staminaTimer -= deltaTime;
        }
        if (regenerationTimer > 0) {
            regenerationTimer -= deltaTime;
        }
        if (jumpTimer > 0) {
            jumpTimer -= deltaTime;
        }
        if (slashCooldown > 0) {
            if (slashCooldown > slashMaxCooldown - 0.13) {
                currentMovementState = MovementState.Skill;
            }
            slashCooldown -= deltaTime;
        }
        if (crossSlashCooldown > 0)  {
            if (crossSlashCooldown > 8 - 0.13) {
                currentMovementState = MovementState.Skill;
            }
            crossSlashCooldown -= deltaTime;
        }
        if (ultimateCooldown > 0) {
            ultimateCooldown -= deltaTime;
        }
        if (gameStartTimer > 0) {
            gameStartTimer -= deltaTime;
        }
        if (flashTimer > 0) {
            flashTimer -= deltaTime;
        }

        // set health
        setCurrentHealth(currentHealth);
        setCurrentOverHeal(currentOverHeal);

        if (dashTimer > 0) {
            dashTimer -= deltaTime;
            this.smoothDash += 20 * deltaTime;
            this.posX += faceToX * deltaTime * (DASH_SPEED * smoothDash);
            this.posY += faceToY * deltaTime * (DASH_SPEED * smoothDash);
            if (!isJumping) {
                this.velocityX += faceToX * (DASH_SPEED * 1.3);
                this.velocityY += faceToY * (DASH_SPEED * 1.3);
            } else {
                this.velocityX += faceToX * (DASH_SPEED * 0.5);
                this.velocityY += faceToY * (DASH_SPEED * 0.5);
            }
            if (currentMovementState != MovementState.Skill && currentMovementState != MovementState.SkillJump)
                currentMovementState = MovementState.Dash;
        } else {
            velocityX = updateVelocity(directionToX, velocityX, deltaTime);
            velocityY = updateVelocity(directionToY, velocityY, deltaTime);

            if (velocityX > MAX_SPEED) {
                velocityX -= (FRICTION * 250) * deltaTime;
                if (velocityX < MAX_SPEED) velocityX = MAX_SPEED;
            } else if (velocityX < -MAX_SPEED) {
                velocityX += (FRICTION * 250) * deltaTime;
                if (velocityX > -MAX_SPEED) velocityX = -MAX_SPEED;
            }
            if (velocityY > MAX_SPEED) {
                velocityY -= (FRICTION * 250) * deltaTime;
                if (velocityY < MAX_SPEED) velocityY = MAX_SPEED;
            } else if (velocityY < -MAX_SPEED) {
                velocityY += (FRICTION * 250) * deltaTime;
                if (velocityY > -MAX_SPEED) velocityY = -MAX_SPEED;
            }

            if (!world.isUltimateActive()) {
                this.posX += velocityX * deltaTime * (speedMultiplier);
                this.posY += velocityY * deltaTime * (speedMultiplier);
            }

            if (this.posX > MAP_LIMIT) this.posX = MAP_LIMIT - 1;
            if (this.posX < -MAP_LIMIT) this.posX = -MAP_LIMIT + 1;
            if (this.posY > MAP_LIMIT) this.posY = MAP_LIMIT - 1;
            if (this.posY < -MAP_LIMIT) this.posY = -MAP_LIMIT + 1;

            // no set when dash :)
            setFaceTo(directionToX, directionToY, deltaTime);

            this.smoothDash = 0.3;
        }

        // jump
        if (isJumping) {
            if (jumpTimer <= 0) {
                this.velocityZ -= GRAVITY * deltaTime;
            }
            if (dashTimer > 0) {
                this.posZ -= this.velocityZ * deltaTime * 0.8;
            }
            this.posZ += this.velocityZ * deltaTime;
            this.posX += velocityX * deltaTime * (0.5);
            this.posY += velocityY * deltaTime * (0.5);
            if (this.posZ <= 0) {
                this.posZ = 0;
                this.jumpTimer = JUMP_COOLDOWN;
                this.isJumping = false;
                this.velocityZ = 0;
                isJumpingSlashing = false;
            }
        }

        if (posZ > 0 && dashTimer <= 0) {
            if (!world.isUltimateActive() && !world.isEntering()) {
                if (!isJumpingSlashing) {
                    if (velocityZ > 0)
                        currentMovementState = MovementState.JumpUp;
                    else
                        currentMovementState = MovementState.JumpDown;
                } else {
                    currentMovementState = MovementState.SkillJump;
                }
            }
        }

        if (world.isUltimateActive()) {
            velocityX = 0;
            velocityY = 0;
        }

        regeneration();

        stamina();

        hasMaxOverHeal();

        updateAnimation(deltaTime);
    }

    public void updateAnimation(double deltaTime) {
        animationTimer += deltaTime;
        if (checkSetFrame != currentMovementState.checkSetFrame) {
            checkSetFrame = currentMovementState.checkSetFrame;
            currentFrame = 0;
        }
        if (animationTimer > currentMovementState.frameDuration) {
            currentFrame++;
            if (currentFrame >= currentMovementState.maxFrames) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        javafx.scene.image.Image playerMech = engine.getPlayerMech();
        javafx.scene.effect.Effect hitPlayer = engine.getHitPlayer();
        java.util.List<PlayerTrail> playerTrails = engine.getPlayerTrails();
        double deltaTime = engine.getDeltaTime();
        double trailTimer = engine.getTrailTimer();
        double offsetX = 10;
        double offsetY = 20;
        double scalePlayer = 1.5;
        double drawPlayerX = this.getPosX() - cameraPosX - offsetX;
        double drawPlayerY = this.getPosY() - cameraPosY - this.getPosZ() - offsetY;
        double frameWidth = 80;
        double frameHeight = 80;
        double sourceX;
        double sourceY;
        if (this.getCurrentMovementState() == Player.MovementState.Run) {
            double idleStartX = 0;
            double idleStartY = 80;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else if (this.getCurrentMovementState() == Player.MovementState.Dash) {
            double idleStartX = 0;
            double idleStartY = 160;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else if (this.getCurrentMovementState() == Player.MovementState.JumpUp) {
            double idleStartX = 0;
            double idleStartY = 240;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else if (this.getCurrentMovementState() == Player.MovementState.JumpDown) {
            double idleStartX = 80;
            double idleStartY = 240;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else if (this.getCurrentMovementState() == Player.MovementState.Skill) {
            frameWidth = 100;
            double idleStartX = 0;
            double idleStartY = 320;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
            frameWidth = 120;
            frameHeight = 120;
            double idleStartX = 160;
            double idleStartY = 200;
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
        } else {
            double walkStartX = 0;
            double walkStartY = 0;
            sourceX = walkStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = walkStartY;
        }

        boolean isFlipped = this.getFaceToX() < 0;

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
                if (trail.rotation != 0) {
                    gc.rotate(trail.rotation);
                }
                gc.drawImage(
                        playerMech,
                        trail.sourcePosX, trail.sourcePosY, trail.frameWidth, trail.frameHeight,
                        -(trail.frameWidth * scalePlayer / 2.0), -(trail.frameHeight * scalePlayer / 2.0), trail.frameWidth * scalePlayer, trail.frameHeight * scalePlayer
                );
                gc.restore();
            }
        }
        
        double currentXOffset = 0;
        if (isFlipped) {
            if (this.getCurrentMovementState() == Player.MovementState.Skill) {
                currentXOffset = -60;
            } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
                currentXOffset = 10;
            } else {
                currentXOffset = 22;
            }
        } else {
            if (this.getCurrentMovementState() == Player.MovementState.Skill) {
                currentXOffset = 60;
            } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
                currentXOffset = -10;
            } else {
                currentXOffset = 0;
            }
        }

        double currentRotation = 0;
        if (this.getSlashCooldown() > this.getSlashMaxCooldown() - 0.13 && this.getCurrentMovementState() == Player.MovementState.SkillJump) {
            currentRotation = 360 * (this.getSlashMaxCooldown() - this.getSlashCooldown()) / 0.13;
        }

        if (this.getCheckSetFrame() > 0) {
            trailTimer += deltaTime;
            engine.setTrailTimer(trailTimer);
            if (this.getCurrentMovementState() == Player.MovementState.Run) {
                if (trailTimer >= 0.2) {
                    playerTrails.add(new PlayerTrail(
                            this.getPosX() + currentXOffset, this.getPosY() - this.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped, currentRotation
                    ));
                    engine.setTrailTimer(0);
                }
            } else if (this.getCurrentMovementState() == Player.MovementState.Dash || this.getCurrentMovementState() == Player.MovementState.JumpUp || this.getCurrentMovementState() == Player.MovementState.JumpDown) {
                if (trailTimer >= 0.05) {
                    playerTrails.add(new PlayerTrail(
                            this.getPosX() + currentXOffset, this.getPosY() - this.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped, currentRotation
                    ));
                    engine.setTrailTimer(0);
                }
            } else if (this.getCurrentMovementState() == Player.MovementState.Skill) {
                if (trailTimer >= 0.03) {
                    playerTrails.add(new PlayerTrail(
                            this.getPosX() + currentXOffset, this.getPosY() - this.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped, currentRotation
                    ));
                    engine.setTrailTimer(0);
                }
            } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
                if (trailTimer >= 0.01) {
                    playerTrails.add(new PlayerTrail(
                            this.getPosX() + currentXOffset, this.getPosY() - this.getPosZ(),
                            sourceX, sourceY, frameWidth, frameHeight, isFlipped, currentRotation
                    ));
                    engine.setTrailTimer(0);
                }
            } else {
                engine.setTrailTimer(0);
            }
        }
        if (isFlipped) {
            gc.save();
            if (this.getCurrentMovementState() == Player.MovementState.Skill) {
                gc.translate(drawPlayerX - 60, drawPlayerY);
                gc.scale(-1, 1);
            } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
                gc.translate(drawPlayerX + 10, drawPlayerY);
                gc.scale(-1, 1);
            } else {
                gc.translate(drawPlayerX + 22, drawPlayerY);
                gc.scale(-1, 1);
            }
        } else {
            gc.save();
            if (this.getCurrentMovementState() == Player.MovementState.Skill) {
                gc.translate(drawPlayerX + 60, drawPlayerY);
            } else if (this.getCurrentMovementState() == Player.MovementState.SkillJump) {
                gc.translate(drawPlayerX - 10, drawPlayerY);
            } else {
                gc.translate(drawPlayerX, drawPlayerY);
            }
        }

        if (this.getSlashCooldown() > this.getSlashMaxCooldown() - 0.13 && this.getCurrentMovementState() == Player.MovementState.SkillJump) {
            gc.rotate(360 * (this.getSlashMaxCooldown() - this.getSlashCooldown()) / 0.13);
        }

        if (this.getFlashTimer() > 0) {
            gc.setEffect(hitPlayer);
        }

        gc.drawImage(
                playerMech,
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth * scalePlayer / 2.0), -(frameHeight * scalePlayer / 2.0), frameWidth * scalePlayer, frameHeight * scalePlayer
        );
        gc.restore();

    }

    @Override
    protected void pushObject(GameObject object, double pushX, double pushY) {
        object.hitboxContactPushOut(-pushX * 3, -pushY * 3);
    }

    public void setVelocity(double directionToX, double directionToY)  {
        this.directionToX = directionToX;
        this.directionToY = directionToY;
    }

    public double updateVelocity(double directionTo, double velocity, double deltaTime) {
        if (directionTo != 0) {
            velocity += directionTo * ACCELERATION * deltaTime;
        } else {
            if (velocity > 0) {
                velocity -= FRICTION * deltaTime;
                if (velocity < 0) velocity = 0;
            } else if (velocity < 0) {
                velocity += FRICTION * deltaTime;
                if (velocity > 0) velocity = 0;
            }
        }
        return velocity;
    }

    public void dash() {
        if (gameStartTimer > 0) return;
        if (dashCooldown <= 0 && currentStamina > 15 * skillStaminaMultiplier) {
            SoundManager.dashingSound.play();
            dashTimer = DASH_DURATION;
            dashCooldown = DASH_COOLDOWN;
            setCurrentStamina(currentStamina - (15 * skillStaminaMultiplier));
        }
    }

    public void dash(double controllerPosX, double controllerPosY) {
        if (gameStartTimer > 0) return;
        double distanceX = controllerPosX - this.posX;
        double distanceY = controllerPosY - this.posY;
        double angle = Math.atan2(distanceY, distanceX);
        mouseFaceToX = Math.cos(angle);
        mouseFaceToY = Math.sin(angle);
        faceToX = mouseFaceToX;
        faceToY = mouseFaceToY;
        if (dashCooldown <= 0 && currentStamina > 15 * skillStaminaMultiplier) {
            SoundManager.dashingSound.play();
            dashTimer = DASH_DURATION;
            dashCooldown = DASH_COOLDOWN;
            setCurrentStamina(currentStamina - (15 * skillStaminaMultiplier));
        }
    }

    public void stamina() {
        if (staminaTimer <= 0) {
            if (currentStamina < maxStamina) {
                setCurrentStamina(currentStamina + 1);
                staminaTimer = 0.1;
            }
            if (currentStamina > maxStamina) {
                setCurrentStamina(maxStamina);
            }
        }
    }

    public void regeneration() {
        if (regenerationTimer <= 0) {
            if (currentHealth < maxHealth) {
                setCurrentHealth(currentHealth + regeneration);
                regenerationTimer = 0.2;
            }
        }
    }

    public void jump() {
        if (gameStartTimer > 0) return;
        if (!isJumping && jumpTimer <= 0 && currentStamina > 25) {
            SoundManager.jumpingSound.play();
            isJumping = true;
            canJumpingSlashOnce = true;
            velocityZ = JUMP_SPEED;
            setCurrentStamina(currentStamina - 25);
        }
    }

    public double staminaRatio() {
        if (maxStamina <= 0) return 0;
        return currentStamina / maxStamina;
    }

    public double healthRatio() {
        if (maxHealth <= 0) return 0;
        return currentHealth / (maxHealth);
    }

    public double overHealRatio() {
        if (maxHealth + currentOverHeal <= 0) return 0;
        return currentOverHeal / (maxHealth + currentOverHeal);
    }

    public double healthOverHealRatio() {
        if (maxHealth + currentOverHeal <= 0) return 0;
        return currentHealth / (maxHealth + currentOverHeal);
    }

    public double slashCooldownRatio() {
        double ratio = (slashCooldown <= 0) ? 0 : slashCooldown / slashMaxCooldown;
        if (gameStartTimer > 0) return Math.max(ratio, gameStartTimer / 2.0);
        else return ratio;
    }

    // exp

    public void addExperience(int amount) {
        this.experience += (int)(amount * (expMultiplier));
        if (this.experience >= this.expToLevel) {
            this.experience -= this.expToLevel;
            this.playerLevel++;
            this.pendingLevelup++;
            this.expToLevel = (int) (this.expToLevel * 1.3);

            if (currentHealth <= maxHealth) {
                this.maxHealth += 10;
            }
            if (currentStamina <= maxStamina) {
                this.maxStamina += 1;
            }
        }
    }

    public void claimLevelUp() {
        pendingLevelup--;
    }

    public void coin(int coin) {
        setCoin(this.coin + coin);
    }

    public double getExpRatio() {
        return (double)this.experience / (double)this.expToLevel;
    }

    // kill count
    public void addKill() {
        this.enemiesKilled++;
    }





    // Items effect

    public void heal(int amount) {
        this.setCurrentHealth(currentHealth + amount);
    }

    public void addOverHealing(int amount) {
        this.maxOverHeal += amount;
    }

    public void addLifeSteal(double amount) {
        this.lifeSteal += amount;
    }

    public void addRegeneration(int amount) {
        this.regeneration += amount;
    }

    public boolean hasMaxOverHeal() {
        return maxOverHeal > 0;
    }

    public boolean hasCurrentHeal() {
        return currentOverHeal > 0;
    }

    public void addSpeedMultiplier(double amount) {
        this.speedMultiplier += amount;
    }

    public void addExpMultiplier(double amount) {
        this.expMultiplier += amount;
    }

    public void addCoinMultiplier(double amount) {
        this.coinMultiplier += amount;
    }


    // skill

    public double dashCooldownRatio() {
        double ratio = (dashCooldown <= 0) ? 0 : dashCooldown / DASH_COOLDOWN;
        if (gameStartTimer > 0) return Math.max(ratio, gameStartTimer / 2.0);
        else return ratio;
    }

    public double jumpCooldownRatio() {
        double ratio = (jumpTimer <= 0) ? 0 : jumpTimer / JUMP_COOLDOWN;
        if (gameStartTimer > 0) return Math.max(ratio, gameStartTimer / 2.0);
            else return ratio;
    }

    public boolean canSlash() {
        if (gameStartTimer > 0) return false;
        if (currentStamina > 10)
            return this.slashCooldown <= 0;
        else return false;
    }

    public boolean isJumpingSlashing() {
        return this.isJumpingSlashing;
    }

    public void resetSlashCooldown() {
        this.slashCooldown = slashMaxCooldown * (1 - slashCooldownMultiplier / 100);
    }

    public double crossSlashCooldownRatio() {
        double ratio = (crossSlashCooldown <= 0) ? 0 : crossSlashCooldown / 8.0;
        if (gameStartTimer > 0) return Math.max(ratio, gameStartTimer / 2.0);
        else return ratio;
    }

    public void triggerJumpingDashAttack() {
        this.isJumpingSlashing = true;
        this.velocityZ = 0;
    }

    public double ultimateCooldownRatio() {
        double ratio = Math.max(0, ultimateCooldown / 60.0);
        if (gameStartTimer > 0) return Math.max(ratio, gameStartTimer / 2.0);
        else return ratio;
    }





    // s, g

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getMouseFaceToX() {
        return mouseFaceToX;
    }

    public void setMouseFaceToX(double mouseFaceToX) {
        this.mouseFaceToX = mouseFaceToX;
    }

    public double getMouseFaceToY() {
        return mouseFaceToY;
    }

    public void setMouseFaceToY(double mouseFaceToY) {
        this.mouseFaceToY = mouseFaceToY;
    }

    public double getCoinMultiplier() {
        return coinMultiplier;
    }

    public void setCoinMultiplier(double coinMultiplier) {
        this.coinMultiplier = coinMultiplier;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        if (currentHealth < 0) this.currentHealth = 0;
        else if (currentHealth > this.maxHealth) this.currentHealth = this.maxHealth;
        else this.currentHealth = currentHealth;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getFaceToX() {
        return faceToX;
    }

    public double getFaceToY() {
        return faceToY;
    }

    public double getMAX_SPEED() {
        return MAX_SPEED;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getExpMultiplier() {
        return expMultiplier;
    }

    public double getCurrentOverHeal() {
        return currentOverHeal;
    }

    public void setCurrentOverHeal(double currentOverHeal) {
        if (currentOverHeal < 0) this.currentOverHeal = 0;
        else if (currentOverHeal > this.maxOverHeal) this.currentOverHeal = this.maxOverHeal;
        else this.currentOverHeal = currentOverHeal;
    }

    public double getMaxOverHeal() {
        return maxOverHeal;
    }

    public void setMaxOverHeal(double maxOverHeal) {
        this.maxOverHeal = maxOverHeal;
    }

    public double getLifeSteal() {
        return lifeSteal;
    }

    public double getRegeneration() {
        return regeneration;
    }

    public double getMAP_LIMIT() {
        return MAP_LIMIT;
    }

    public double getDashTimer() {
        return dashTimer;
    }

    public double getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(double maxStamina) {
        this.maxStamina = maxStamina;
    }

    public double getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(double currentStamina) {
        if (this.maxStamina < currentStamina)
            this.currentStamina = maxStamina;
        else this.currentStamina = Math.max(currentStamina, 0);
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public void setVelocityZ(double velocityZ) {
        this.velocityZ = velocityZ;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public double getSlashMaxCooldown() {
        return slashMaxCooldown;
    }

    public double getSlashCooldown() {
        return slashCooldown;
    }

    public void setSlashCooldown(double slashCooldown) {
        this.slashCooldown = slashCooldown;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = Math.max(coin, 0);
    }

    public boolean isCanJumpingSlashOnce() {
        return canJumpingSlashOnce;
    }

    public void setCanJumpingSlashOnce(boolean canJumpingSlashOnce) {
        this.canJumpingSlashOnce = canJumpingSlashOnce;
    }

    public int getPendingLevelup() {
        return pendingLevelup;
    }

    public double getMagnetRadius() {
        return magnetRadius;
    }

    public void setMagnetRadius(double magnetRadius) {
        this.magnetRadius = magnetRadius;
    }

    public double getLuck() {
        return luck;
    }

    public void setLuck(double luck) {
        this.luck = luck;
    }

    public void setSlashMaxCooldown(double slashMaxCooldown) {
        this.slashMaxCooldown = slashMaxCooldown;
    }

    public double getSlashCooldownMultiplier() {
        return slashCooldownMultiplier;
    }

    public void setSlashCooldownMultiplier(double slashCooldownMultiplier) {
        this.slashCooldownMultiplier = slashCooldownMultiplier;
    }

    public void setPendingLevelup(int pendingLevelup) {
        this.pendingLevelup = pendingLevelup;
    }

    public double getStatDamage() {
        return statDamage;
    }

    public void setStatDamage(double statDamage) {
        this.statDamage = statDamage;
    }

    public double getStatAtkSpeed() {
        return statAtkSpeed;
    }

    public void setStatAtkSpeed(double statAtkSpeed) {
        this.statAtkSpeed = statAtkSpeed;
    }

    public double getStatCritDamage() {
        return statCritDamage;
    }

    public void setStatCritDamage(double statCritDamage) {
        this.statCritDamage = statCritDamage;
    }

    public double getStatCritRate() {
        return statCritRate;
    }

    public void setStatCritRate(double statCritRate) {
        this.statCritRate = statCritRate;
    }

    public double getStatProjCount() {
        return statProjCount;
    }

    public void setStatProjCount(double statProjCount) {
        this.statProjCount = statProjCount;
    }

    public double getStatSize() {
        return statSize;
    }

    public void setFRICTION(double FRICTION) {
        this.FRICTION = FRICTION;
    }

    public void setStatSize(double statSize) {
        this.statSize = statSize;
    }

    public double getCrossSlashCooldown() {
        return crossSlashCooldown;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    public boolean isImmuneToSlow() {
        return immuneToSlow;
    }

    public void setImmuneToSlow(boolean immuneToSlow) {
        this.immuneToSlow = immuneToSlow;
    }

    public double getSkillStaminaMultiplier() {
        return skillStaminaMultiplier;
    }

    public void setSkillStaminaMultiplier(double skillStaminaMultiplier) {
        this.skillStaminaMultiplier = skillStaminaMultiplier;
    }

    public double getDashDamageReduction() {
        return dashDamageReduction;
    }

    public void setDashDamageReduction(double dashDamageReduction) {
        this.dashDamageReduction = dashDamageReduction;
    }

    public double getInstantKillChance() {
        return instantKillChance;
    }

    public void setInstantKillChance(double instantKillChance) {
        this.instantKillChance = instantKillChance;
    }

    public int getMaxItemSlot() {
        return maxItemSlot;
    }

    public void setMaxItemSlot(int maxItemSlot) {
        this.maxItemSlot = maxItemSlot;
    }

    public boolean isHasDeepestVoid() {
        return hasDeepestVoid;
    }

    public void setHasDeepestVoid(boolean hasDeepestVoid) {
        this.hasDeepestVoid = hasDeepestVoid;
    }

    public List<double[]> getPendingBlackHoles() {
        return pendingBlackHoles;
    }

    public boolean isHasFinalWeapon() {
        return hasFinalWeapon;
    }

    public void setHasFinalWeapon(boolean hasFinalWeapon) {
        this.hasFinalWeapon = hasFinalWeapon;
    }

    public void setCrossSlashCooldown(double crossSlashCooldown) {
        this.crossSlashCooldown = crossSlashCooldown;
    }

    public boolean isShielded() {
        return shielded;
    }

    public double getUltimateCooldown() {
        return ultimateCooldown;
    }

    public void setUltimateCooldown(double ultimateCooldown) {
        this.ultimateCooldown = ultimateCooldown;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getCheckSetFrame() {
        return checkSetFrame;
    }
    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }

    public MovementState getCurrentMovementState() {
        return currentMovementState;
    }

    public double getGameStartTimer() {
        return gameStartTimer;
    }

    public double getFlashTimer() {
        return flashTimer;
    }

    public void setFlashTimer(double flashTimer) {
        this.flashTimer = flashTimer;
    }

    public void setCurrentMovementState(MovementState currentMovementState) {
        this.currentMovementState = currentMovementState;
    }
}
