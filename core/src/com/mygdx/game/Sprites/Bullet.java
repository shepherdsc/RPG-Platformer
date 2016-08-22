package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MainRPG;

import java.util.Random;

/**
 * Created by scott_000 on 7/4/2016.
 */
public class Bullet extends Sprite implements Pool.Poolable {

    private int speed;
    private Texture rifleBullet = new Texture("RifleBullet.png");

    private float totalTime, accuracy;

    private World world;
    private Body b2bullet;
    private FixtureDef fdef;

    private boolean setToDestroy, destroyed;

    public Bullet(World b2World){
        //set variables for bullet
        this.speed = 12;
        this.accuracy = 1.5f;
        world = b2World;
        setToDestroy = false;
        destroyed = false;

        //set up sprite texture
        setBounds(0, 0, 16 / MainRPG.PPM, 16 / MainRPG.PPM);
        setRegion(rifleBullet);

        //set up fixture for bullet
        fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(7f / MainRPG.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;

        fdef.filter.categoryBits = MainRPG.BULLET_BIT;
        fdef.filter.maskBits = MainRPG.OBJECT_BIT |
                MainRPG.ENEMY_BIT |
                MainRPG.ENEMY_HEAD_BIT;
    }

    public void update(float delta){
        totalTime += delta;

        setPosition(b2bullet.getPosition().x - getWidth() / 2, b2bullet.getPosition().y - getHeight() / 2);

        if(totalTime > .5){
            setToDestroy = true;
        }

        if(setToDestroy && !destroyed){
            world.destroyBody(b2bullet);
            destroyed = true;
        }
    }

    public void defineBullet(float startX, float startY, float aimX, float aimY){
        BodyDef bdef = new BodyDef();
        bdef.position.set(startX, startY + 22 / MainRPG.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2bullet = world.createBody(bdef);
        b2bullet.setGravityScale(0);

        b2bullet.createFixture(fdef).setUserData(this);

        //set b2d body in motion
        float slope = aimY / aimX;
        float velX = (float)(speed / Math.sqrt(Math.pow(slope, 2) + 1) * aimX / Math.abs(aimX));

        Random random = new Random();
        float velY = slope * velX + (random.nextFloat() * accuracy - (accuracy / 2));

        b2bullet.setLinearVelocity(velX, velY);

    }

    @Override
    public void reset() {
        totalTime = 0;
        setToDestroy = false;
        destroyed = false;
        setPosition(-10, -10);
    }

    public void setSetToDestroy(boolean destroy){
        setToDestroy = destroy;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public boolean isSetToDestroy(){
        return setToDestroy;
    }
}
