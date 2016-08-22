package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MainRPG;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Bullet;

/**
 * Created by scott_000 on 6/4/2016.
 */
public class WorldContactListener implements ContactListener{

    private PlayScreen screen;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){

            case(MainRPG.BULLET_BIT | MainRPG.ENEMY_BIT) : {
                Gdx.app.log("TAG", "Enemy Hit");

                if (fixA.getFilterData().categoryBits == MainRPG.BULLET_BIT) {
                    screen.guard.setHit(true);
                    ((Bullet) fixA.getUserData()).setSetToDestroy(true);
                } else {
                    screen.guard.setHit(true);
                    ((Bullet) fixB.getUserData()).setSetToDestroy(true);
                }
            }

            case(MainRPG.BULLET_BIT | MainRPG.ENEMY_HEAD_BIT) : {
                Gdx.app.log("TAG", "Enemy Head Hit");

                if (fixA.getFilterData().categoryBits == MainRPG.BULLET_BIT) {
                    screen.guard.setHeadHit(true);
                    ((Bullet) fixA.getUserData()).setSetToDestroy(true);
                } else {
                    screen.guard.setHeadHit(true);
                    ((Bullet) fixB.getUserData()).setSetToDestroy(true);
                }
            }

            case(MainRPG.BULLET_BIT | MainRPG.OBJECT_BIT) : {
                Gdx.app.log("TAG", "Bullet Collision");

                if (fixA.getFilterData().categoryBits == MainRPG.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).setSetToDestroy(true);
                } else {
                    ((Bullet) fixB.getUserData()).setSetToDestroy(true);
                }
            }

            case(MainRPG.HEAD_BIT | MainRPG.OBJECT_BIT) : {
                Gdx.app.log("TAG", "Wall Collision");
                //screen.player.b2body.setLinearVelocity(0, 0);
            }

            case(MainRPG.CHARACTER_BIT | MainRPG.OBJECT_BIT) : {
                Gdx.app.log("TAG", "Player Collision");
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void setScreen(PlayScreen screen){
        this.screen = screen;
    }
}

