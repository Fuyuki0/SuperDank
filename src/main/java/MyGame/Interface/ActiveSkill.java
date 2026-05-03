package MyGame.Interface;

import MyGame.Game.World;

public interface ActiveSkill extends Renderable {
    void updateSkill(double deltaTime, World world);
    boolean isFinished();
}
