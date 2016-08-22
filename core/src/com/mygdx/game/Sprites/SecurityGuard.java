package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MainRPG;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by scott_000 on 7/22/2016.
 */
public class SecurityGuard extends Enemy {

    public Body b2body;
    private boolean isHit = false, isHeadHit = false, dead = false;
    int totHP = 400, HP = 400;
    float hpPercent = 1;

    public SecurityGuard(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        world = screen.getWorld();
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if(isHit){
            HP -= 30;
            hpPercent = HP / totHP;
            isHit = false;
        }else if(isHeadHit){
            HP -= 100;
            hpPercent = HP / totHP;
            isHeadHit = false;
        }

        if(HP < 0){
            world.destroyBody(b2body);
            dead = true;
        }
    }

    @Override
    protected void defineEnemy(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(20 / MainRPG.PPM, 34 / MainRPG.PPM);

        fdef.shape = boxShape;
        fdef.filter.categoryBits = MainRPG.ENEMY_BIT;
        fdef.filter.maskBits = MainRPG.OBJECT_BIT |
                MainRPG.CHARACTER_BIT |
                MainRPG.HEAD_BIT |
                MainRPG.BULLET_BIT/*/|
                MainRPG.ACID_BIT |
                MainRPG.SAW_BIT |/*/;

        b2body.createFixture(fdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(24 / MainRPG.PPM);
        shape.setPosition(new Vector2(0, 58 / MainRPG.PPM));

        fdef.shape = shape;
        fdef.filter.categoryBits = MainRPG.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = MainRPG.OBJECT_BIT |
                MainRPG.CHARACTER_BIT |
                MainRPG.BULLET_BIT;

        b2body.createFixture(fdef);

    }

    public void setHit(boolean hit){
        this.isHit = hit;
    }

    public void setHeadHit(boolean headHit) {
        isHeadHit = headHit;
    }

    public float getHpPercent(){
        return hpPercent;
    }

    public boolean isDead(){
        return dead;
    }
}
