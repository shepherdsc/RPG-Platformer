package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controllers.PlayerController;
import com.mygdx.game.MainRPG;
import com.mygdx.game.Sprites.Bullet;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.PlayerArm;
import com.mygdx.game.Sprites.PlayerCharacters;
import com.mygdx.game.Sprites.SecurityGuard;
import com.mygdx.game.Tools.B2dWorldCreator;
import com.mygdx.game.Tools.BulletPool;
import com.mygdx.game.Tools.WorldContactListener;

import sun.rmi.runtime.Log;

/**
 * Created by scott_000 on 6/2/2016.
 */
public class PlayScreen implements Screen {

    //game views and such
    private MainRPG game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    public PlayerCharacters player;
    public PlayerArm arm;

    public BulletPool pool;
    public Array<Bullet> bulletArray = new Array<Bullet>();

    private int direction = 1;
    private int countShots = 0;

    private boolean canShoot = true;

    //controller
    public PlayerController controller;

    //shape renderer(for shot aim line)
    ShapeRenderer sr;

    //maps
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables[
    private World world;
    private Box2DDebugRenderer b2dr;
    private WorldContactListener listener;

    public SecurityGuard guard;

    public PlayScreen(MainRPG game){
        //set game views and such
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MainRPG.V_WIDTH / MainRPG.PPM, MainRPG.V_HEIGHT / MainRPG.PPM,
                gamecam);

        //set map
        maploader = new TmxMapLoader();
        map = maploader.load("SciFiMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MainRPG.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //set world and debug lines (remove for final)
        world = new World(new Vector2(0,-20), true);
        b2dr = new Box2DDebugRenderer();

        listener = new WorldContactListener();
        listener.setScreen(this);

        world.setContactListener(listener);


        //rifle guy texture
        atlas = new TextureAtlas("Rifle.txt");

        //create user player
        player = new PlayerCharacters(world);
        arm = new PlayerArm(world);
        arm.origBody = player.b2body;

        guard = new SecurityGuard(this, 2.92f, 2.80f);

        pool = new BulletPool(world);

        //create user controller
        controller = new PlayerController();

        //create shape renderer
        sr = new ShapeRenderer();

        //create world with objects and such
        new B2dWorldCreator(world, map);

    }

    @Override
    public void show() {

    }

    public void handleInput() {

        //change player direction based on movement
        if(controller.velMove > 0){
            direction = 1;
            player.setFacing("right");
            arm.setFacing("right");

        }else if(controller.velMove < 0){
            direction = -1;
            player.setFacing("left");
            arm.setFacing("left");
        }

        //handle movement input
        //jump
        if(controller.isJumping() && player.b2body.getLinearVelocity().y == 0) {
            //do a jump
            player.b2body.applyLinearImpulse(new Vector2(0, 8f), player.b2body.getWorldCenter(), true);
            //stop further jumping
            controller.isJumping = false;
            controller.setCanJump(false);
        }
        //move
        if(controller.isMove()) {
            //move
            player.b2body.setLinearVelocity(new Vector2(controller.velMove * 4, player.b2body.getLinearVelocity().y));

        }else if(!controller.isMove() && player.b2body.getLinearVelocity().y == 0){
            //stop
            player.b2body.setLinearVelocity(new Vector2(0, 0));
        }

        if(player.b2body.getLinearVelocity().y == 0){
            //reset jump possibility upon landing
            controller.setCanJump(true);
        }

        //handle shooting input
        if(controller.isShooting()){
            if(canShoot){
                //set up and obtain bullet from pool
                Bullet bullet = pool.obtain();
                if(bullet != null){
                    //create and set b2body in motion
                    bullet.defineBullet(player.b2body.getPosition().x + (direction * 40 / MainRPG.PPM),
                            player.b2body.getPosition().y, controller.aimX, controller.aimY);
                    //add bullet to array
                    bulletArray.add(bullet);
                }
                //stop shooting
                canShoot = false;
            }else{
                /*
                //automatic shooting algorithm
                countShots += 1;
                if(countShots == 10){
                    canShoot = true;
                    countShots = 0;
                }*/
            }
        }else{
            canShoot = true;
        }

    }

    public void DrawAimLines(Vector2 start, Vector2 end){
        //set up renderer
        Gdx.gl.glLineWidth(10);
        sr.setProjectionMatrix(gamecam.combined);
        sr.setColor(1, 0, 0, 1);
        //render line
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(start, end);
        sr.end();
        Gdx.gl.glLineWidth(1);
    }

    public void DrawHPGauge(SecurityGuard securityGuard){
        float barY = securityGuard.b2body.getPosition().y + 90 / MainRPG.PPM;

        Vector2 start = new Vector2(securityGuard.b2body.getPosition().x - 25 / MainRPG.PPM,
                barY);
        Vector2 end = new Vector2(securityGuard.b2body.getPosition().x + 25 / MainRPG.PPM,
                barY);
        Vector2 end2 = new Vector2(securityGuard.b2body.getPosition().x - 25 / MainRPG.PPM + (50 * securityGuard.getHpPercent()) / MainRPG.PPM,
                barY);

        //set up renderer
        Gdx.gl.glLineWidth(15);
        sr.setProjectionMatrix(gamecam.combined);
        sr.setColor(112, 138, 144, 1);
        //render line
        sr.begin(ShapeRenderer.ShapeType.Line);
        //background line
        sr.line(start, end);
        sr.end();

        //change color based on hp percent
        if(securityGuard.getHpPercent() > .5){
            sr.setColor(0, 1, 0, 1);
        }else if(securityGuard.getHpPercent() > .2){
            sr.setColor(255, 255, 0, 1);
        }else{
            sr.setColor(1, 0, 0, 1);
        }

        sr.begin(ShapeRenderer.ShapeType.Line);
        //draw hp line
        sr.line(start, end2);
        sr.end();

        //reset line width
        Gdx.gl.glLineWidth(1);
    }

    public void update(float dt){
        handleInput();

        world.step(1/60f, 6, 2);

        if(player.b2body.getPosition().x < gamePort.getWorldWidth() / 2) {
            //set cam to left side
            gamecam.position.x = gamePort.getWorldWidth() / 2;
        }else if(player.b2body.getPosition().x > gamePort.getWorldWidth() / 2){
            //set cam x to player
            gamecam.position.x = player.b2body.getPosition().x;
        }

        if(player.b2body.getPosition().y < gamePort.getWorldHeight() / 2) {
            //set cam to bottom
            gamecam.position.y = gamePort.getWorldHeight() / 2;
        }else if(player.b2body.getPosition().y > gamePort.getWorldHeight() / 2){
            //set y cam to player
            gamecam.position.y = player.b2body.getPosition().y;
        }

        //update gamescreen
        gamecam.update();
        renderer.setView(gamecam);

        //update sprite positions
        player.update(dt);
        arm.update(dt);
        if(!guard.isDead()){
            guard.update(dt);
            DrawHPGauge(guard);
        }

        for(Bullet b : bulletArray){
            if(b != null){
                b.update(dt);
                if(b.isDestroyed()){
                    bulletArray.removeValue(b, true);
                    pool.free(b);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        //set background to black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render play screen
        renderer.render();
        update(delta);
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        //player
        player.draw(game.batch);
        //player arm
        arm.draw(game.batch);
        for(Bullet b : bulletArray){
            if(b != null){
                b.draw(game.batch);
            }
        }
        game.batch.end();

        controller.draw();
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        sr.dispose();
    }

}

