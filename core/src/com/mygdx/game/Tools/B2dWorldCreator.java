package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MainRPG;

/**
 * Created by scott_000 on 6/4/2016.
 */
public class B2dWorldCreator {
    public B2dWorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create objects in stone, platform, bound layers
        for(int i = 4; i < 7; i++){
            for(MapObject object : map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainRPG.PPM,
                        (rect.getY() + rect.getHeight() / 2) / MainRPG.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / MainRPG.PPM, (rect.getHeight() / 2) / MainRPG.PPM);
                fdef.shape = shape;
                fdef.friction = 0;
                fdef.filter.categoryBits = MainRPG.OBJECT_BIT;
                fdef.filter.maskBits = MainRPG.CHARACTER_BIT |
                        MainRPG.HEAD_BIT |
                        MainRPG.BULLET_BIT |
                        MainRPG.ENEMY_BIT;
                        
                body.createFixture(fdef);

            }
        }
    }
}
