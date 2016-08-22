package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by scott_000 on 7/22/2016.
 */
public abstract class Enemy extends Sprite {
    protected World world;

    public Enemy(PlayScreen screen, float x, float y){
        world = screen.getWorld();
        defineEnemy(world, x, y);
    }

    protected abstract void defineEnemy(World world, float x, float y);
}
