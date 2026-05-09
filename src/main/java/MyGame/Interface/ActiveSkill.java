package MyGame.Interface;

import MyGame.Game.World;

/**
 * An interface or base class for skills that require player activation.
 */
public interface ActiveSkill extends Renderable {
    void updateSkill(double deltaTime, World world);
    boolean isFinished();
}
