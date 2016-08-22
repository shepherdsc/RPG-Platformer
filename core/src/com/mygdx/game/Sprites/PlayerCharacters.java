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

/**
 * Created by scott_000 on 6/3/2016.
 */
public class PlayerCharacters extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion rifleStand;
    private String facing = "right";

    public PlayerCharacters(World world){
        //super(screen.getAtlas().findRegion("RifleNoArms"));
        this.world = world;
        defineCharacter();
        rifleStand = new TextureRegion(new Texture("RifleNoArms.png"), 0, 0, 300, 600);
        setBounds(0,0, 60 / MainRPG.PPM, 120 / MainRPG.PPM);
        setRegion(rifleStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 20 / MainRPG.PPM);

        if(facing == "right" && isFlipX()){
            flip(true, false);
        }else if(facing == "left" && !isFlipX()){
            flip(true, false);
        }
    }

    public void defineCharacter(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / MainRPG.PPM, 280 / MainRPG.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(22 / MainRPG.PPM, 34 / MainRPG.PPM);

        fdef.shape = boxShape;
        fdef.filter.categoryBits = MainRPG.CHARACTER_BIT;
        fdef.filter.maskBits = MainRPG.OBJECT_BIT |
                MainRPG.CHARACTER_BIT |
                MainRPG.HEAD_BIT |
                MainRPG.ENEMY_BIT/*|
                MainRPG.ACID_BIT |
                MainRPG.SAW_BIT |*/;

        b2body.createFixture(fdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(22 / MainRPG.PPM);
        shape.setPosition(new Vector2(0, 58 / MainRPG.PPM));

        fdef.shape = shape;
        fdef.filter.categoryBits = MainRPG.HEAD_BIT;
        fdef.filter.maskBits = MainRPG.OBJECT_BIT |
                MainRPG.CHARACTER_BIT |
                MainRPG.ENEMY_BIT;

        b2body.createFixture(fdef);
    }


    public void setFacing(String facing) {
        this.facing = facing;
    }
}
