package com.mygdx.game.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Sprites.Bullet;

/**
 * Created by scott_000 on 7/14/2016.
 */
public class BulletPool extends Pool<Bullet> {

    private World world;

    public BulletPool(World world) {
        super();
        this.world = world;
    }

    @Override
    protected Bullet newObject() {
        return new Bullet(world);
    }
}
