package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * Created by scott_000 on 6/23/2016.
 */
public class PlayerArm extends Sprite{

    public World world;
    public Body origBody;
    private Texture rifleArm;
    private String facing;

    public PlayerArm(World world){
        this.world = world;
        rifleArm = new Texture("RifeArmsGunCentered.png");
        setBounds(0,0, 116 / MainRPG.PPM, 100 / MainRPG.PPM);
        setRegion(rifleArm);
        setOrigin(42 / MainRPG.PPM, 54 / MainRPG.PPM);
    }

    public void update(float delta){
        setPosition(origBody.getPosition().x - getWidth() / 2,
                origBody.getPosition().y + 20 / MainRPG.PPM - getHeight() / 2);

        if(facing == "right" && isFlipX()){
            flip(true, false);
        }else if(facing == "left" && !isFlipX()){
            flip(true, false);
        }
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }
}
