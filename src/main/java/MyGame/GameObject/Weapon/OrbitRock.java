package MyGame.GameObject.Weapon;

import MyGame.GameObject.Projectile.Rock;
import MyGame.World;

import java.util.ArrayList;
import java.util.List;

public class OrbitRock extends Weapon{
    private final double RADIUS = 350;
    private double rockAmount;
    private int rockCount = 0;
    private double angle;
    private double respawnTime = 1.5;
    private double timer;
    private double opacity;
    private List<Rock> rocks;

    public OrbitRock() {
        super(WeaponType.ROCKS);
        this.angle = 0;
        this.rockAmount = 3;
        this.rockCount = (int)rockAmount;
        this.timer = respawnTime;
        this.opacity = 1;
        this.rocks = new ArrayList<>();
    }

    public void update(double deltaTime, World world, boolean attackWhileJumping) {
        this.attackWhileJumping = false;
        this.angle = (angle + (360 * deltaTime / 2 * (1 + bonusAttackSpeed / 100))) % 360;
        for (Rock rock : rocks) {
            rock.update(deltaTime, world);
        }
        boolean rockBroken = rocks.removeIf(Rock::isBroken);
        boolean rockUseSlot = false;
        if (rocks.size() < rockAmount + (int)bonusProjCount) {
            timer -= deltaTime;
            if (timer <= 0 ) {
                double spawnAngle = rocks.isEmpty() ? 0 : rocks.size() * (360.0 / (rocks.size() + 1));
                rocks.add(new Rock(spawnAngle));
                timer = respawnTime;
                rockUseSlot = true;
            }
        } else {
            timer = respawnTime;
        }
        if (rockBroken || rockUseSlot || rocks.size() != rockCount) {
            if (!rocks.isEmpty()) {
                double spacing = 360.0 / rocks.size();
                for (int i = 0; i < rocks.size(); i++) {
                    rocks.get(i).setTargetRelativeAngle(i * spacing);
                }
            }
            rockCount = rocks.size();
        }

    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Size", "Proj Count"};
    }

    public void playerAttack(World world) {}
    public String getName() {
        return "Hot Rock";
    }

    public double getRADIUS() {
        return RADIUS;
    }

    public double getRockAmount() {
        return rockAmount;
    }

    public void setRockAmount(double rockAmount) {
        this.rockAmount = rockAmount;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public List<Rock> getRocks() {
        return rocks;
    }

    public void setRocks(List<Rock> rocks) {
        this.rocks = rocks;
    }

    public double getRockCount() {
        return rockCount;
    }

    public void setRockCount(double rockCount) {
        this.rockCount = (int) rockCount;
    }

    public double getRespawnTime() {
        return respawnTime;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setRockCount(int rockCount) {
        this.rockCount = rockCount;
    }

    public void setRespawnTime(double respawnTime) {
        this.respawnTime = respawnTime;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
}
