package MyGame.GameObject.Enemies;

import MyGame.GameObject.Player;

public class Boss extends Enemy{
    public enum BossState {ACTIVE, DASHING, TELEGRAPH_CIRCLE, TELEGRAPH_ARC, TELEGRAPH_AIR, TELEGRAPH_LINE}
    private BossState currentState = BossState.ACTIVE;
    private double cooldown = 3;
    private double maxCooldown = 1;
    private double stateTimer = 0;
    private double maxStateTimer = 0.8;
    private double telegraphTimer = 0;
    private double attackTargetPosX;
    private double attackTargetPosY;
    private double attackAngle;
    private double dashTimer = 0;
    private double dashToX = 0;
    private double dashToY = 0;
    private double dashSpeed = 1200;
    private double gridRotationAngle = 0;
    private int remainingCircles = 0;

    public Boss(double posX, double posY, Player player) {
        super(posX, posY, player);
        this.maxHealth = 3000;
        this.health = maxHealth;
        this.speed = 180;
        this.hitbox = 35;
        this.isBoss = true;
    }

    public void update(double deltaTime, Player player, double difficultyMultiplier) {
        if (currentState == BossState.ACTIVE) {
            cooldown -= deltaTime;
            if (cooldown <= 0) {
                telegraphAttack(player);
                setSpeed(speed + (1 * difficultyMultiplier));
            }
            super.update(deltaTime);
        } else if (currentState == BossState.DASHING) {
            this.posX += dashToX * dashSpeed * deltaTime;
            this.posY += dashToY * dashSpeed * deltaTime;
            dashTimer -= deltaTime;
            if (dashTimer <= 0) {
                currentState = BossState.TELEGRAPH_ARC;
                telegraphTimer = 0.2;
                double distanceX = player.getPosX() - this.posX;
                double distanceY = player.getPosY() - this.posY;
                attackAngle = Math.toDegrees(Math.atan2(distanceY, distanceX));
            }
        } else {
            if (dashTimer > 0) {
                this.posX += dashToX * dashSpeed * deltaTime;
                this.posY += dashToY * dashSpeed * deltaTime;
            }
            telegraphTimer -= deltaTime;
            if (telegraphTimer <= 0) {
                attack(deltaTime, player, difficultyMultiplier);
            }
        }
        updateAnimation(deltaTime);

        if (flashTimer > 0) {
            flashTimer -= deltaTime;
        }
    }


    public void telegraphAttack(Player player) {
        double random = Math.random();
        if (random < 0.5) {
            currentState = BossState.TELEGRAPH_CIRCLE;
            telegraphTimer = 0.8;
            attackTargetPosX = player.getPosX() - (Math.random() - 0.5) * 100;
            attackTargetPosY = player.getPosY() - (Math.random() - 0.5) * 100;
            remainingCircles = (int)(Math.random() * 3) + 1;
            random = Math.random();
            if (random > 0.8) {
                dashTimer = 0.5;
                double randomDashBack = Math.random() * Math.PI * 2;
                this.dashToX = Math.cos(randomDashBack) / 2;
                this.dashToY = Math.sin(randomDashBack) / 2;
            }
        } else {
            currentState = BossState.DASHING;
            dashTimer = 0.5;
            double distanceX = player.getPosX() - this.posX;
            double distanceY = player.getPosY() - this.posY;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            if (distance > 0) {
                this.dashToX = distanceX / distance;
                this.dashToY = distanceY / distance;
            }
        }
    }

    public void attack(double deltaTime, Player player, double difficultyMultiplier) {
        switch (currentState) {
            case BossState.TELEGRAPH_CIRCLE: {
                stateTimer += deltaTime;
                if (stateTimer >= maxStateTimer) {
                    double distanceX = player.getPosX() - attackTargetPosX;
                    double distanceY = player.getPosY() - attackTargetPosY;
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                    if (distance <= 350) {
                        if (player.getCurrentHealth() >= 0 && !player.isJumping() && !player.isShielded())
                            doDamage(player, 500);
                    }
                    remainingCircles--;
                    if (remainingCircles > 0) {
                        stateTimer = 0;
                        attackTargetPosX = player.getPosX();
                        attackTargetPosY = player.getPosY();
                    } else {
                        stateTimer = 0;
                        currentState = BossState.ACTIVE;
                    }
                }
                break;
            }
            case BossState.TELEGRAPH_ARC: {
                stateTimer += deltaTime;
                if (stateTimer >= maxStateTimer) {
                    double distanceX = player.getPosX() - this.posX;
                    double distanceY = player.getPosY() - this.posY;
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                    if (distance <= 550) {
                        double attackAngleToPlayer = Math.toDegrees(Math.atan2(distanceY, distanceX));
                        double angleDiff = Math.abs(attackAngleToPlayer - attackAngle);
                        if (angleDiff > 180) angleDiff = 360 - angleDiff;
                        if (angleDiff <= 45 && !player.isJumping() && !player.isShielded())
                            doDamage(player, 400);
                    }
                    stateTimer = 0;
                    currentState = BossState.ACTIVE;
                }
                break;
            }
        }
        cooldown = maxCooldown - (difficultyMultiplier / 2);
    }


    @Override
    public boolean isBoss() {
        return true;
    }

    public BossState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BossState currentState) {
        this.currentState = currentState;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public double getTelegraphTimer() {
        return telegraphTimer;
    }

    public void setTelegraphTimer(double telegraphTimer) {
        this.telegraphTimer = telegraphTimer;
    }

    public double getAttackTargetPosX() {
        return attackTargetPosX;
    }

    public void setAttackTargetPosX(double attackTargetPosX) {
        this.attackTargetPosX = attackTargetPosX;
    }

    public double getAttackTargetPosY() {
        return attackTargetPosY;
    }

    public void setAttackTargetPosY(double attackTargetPosY) {
        this.attackTargetPosY = attackTargetPosY;
    }

    public double getAttackAngle() {
        return attackAngle;
    }

    public double getDashTimer() {
        return dashTimer;
    }

    public void setDashTimer(double dashTimer) {
        this.dashTimer = dashTimer;
    }

    public double getDashToX() {
        return dashToX;
    }

    public void setDashToX(double dashToX) {
        this.dashToX = dashToX;
    }

    public double getDashToY() {
        return dashToY;
    }

    public void setDashToY(double dashToY) {
        this.dashToY = dashToY;
    }

    public double getDashSpeed() {
        return dashSpeed;
    }

    public void setDashSpeed(double dashSpeed) {
        this.dashSpeed = dashSpeed;
    }

    public double getMaxCooldown() {
        return maxCooldown;
    }

    public void setMaxCooldown(double maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    public void setAttackAngle(double attackAngle) {
        this.attackAngle = attackAngle;
    }

    public double getStateTimer() {
        return stateTimer;
    }

    public double getMaxStateTimer() {
        return maxStateTimer;
    }

    public void setMaxStateTimer(double maxStateTimer) {
        this.maxStateTimer = maxStateTimer;
    }

    public double getGridRotationAngle() {
        return gridRotationAngle;
    }

    public void setGridRotationAngle(double gridRotationAngle) {
        this.gridRotationAngle = gridRotationAngle;
    }

    public void setStateTimer(double stateTimer) {
        this.stateTimer = stateTimer;
    }
}
