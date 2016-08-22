package com.mygdx.game.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainRPG;

/**
 * Created by scott_000 on 6/5/2016.
 */
public class PlayerController{
    Viewport viewport;
    Stage stage;
    public boolean isMoving, isJumping, isShooting, canShoot = true;
    public float velMove, aimX, aimY;
    private boolean enabled = true, canJump;
    OrthographicCamera cam;

    public PlayerController(){

        cam = new OrthographicCamera();
        viewport = new FitViewport(500, 300, cam);
        stage = new Stage(viewport, MainRPG.batch);
        Gdx.input.setInputProcessor(stage);

        //initialize table in bottom left of screen
        Table table = new Table();
        table.bottom().left();

        //add controller images
        final Image imgLeft = new Image(new Texture("leftarrow.png"));
        imgLeft.setSize(50,50);

        final Image imgRight = new Image(new Texture("rightarrow.png"));
        imgRight.setSize(50,50);

        Image imgCenter = new Image(new Texture("midcircle.png"));
        imgCenter.setSize(50,50);

        //handle input for controller)
        imgLeft.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(enabled){
                    isMoving = true;
                    velMove = (x - 65) / 60;
                    if(velMove > 1.2){
                        velMove = 1.2f;
                    }
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                velMove = 0;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(enabled){
                    isMoving = true;
                    if(x > 85){
                        velMove = (x - 85) / 60;
                        if(velMove > 1.2){
                            velMove = 1.2f;
                        }
                    }else if(x < 65){
                        velMove = (x - 65) / 60;
                        if(velMove < -1.2){
                            velMove = -1.2f;
                        }
                    }else{
                        velMove = 0;
                    }
                }
            }

        });

        imgRight.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(enabled){
                    isMoving = true;
                    velMove = (x + 15) / 60;
                    if(velMove > 1.2){
                        velMove = 1.2f;
                    }
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                velMove = 0;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(enabled){
                    isMoving = true;

                    if(x > -15){
                        velMove = (x + 15) / 60;
                        if(velMove > 1.2){
                            velMove = 1.2f;
                        }
                    }else if(x < -35){
                        velMove = (x + 35) / 60;
                        if(velMove < -1.2){
                            velMove = -1.2f;
                        }
                    }else{
                        velMove = 0;
                    }
                }
            }

        });

        imgCenter.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(enabled){
                    isMoving = true;
                    velMove = 0;
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                velMove = 0;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                isMoving = true;
                if(x > 35){
                    velMove = (x - 35) / 60;
                    if(velMove > 1.2){
                        velMove = 1.2f;
                    }
                }else if(x < 15){
                    velMove = (x - 15) / 60;
                    if(velMove < -1.2){
                        velMove = -1.2f;
                    }
                }else{
                    velMove = 0;
                }
            }

        });

        Image imgShoot = new Image(new Texture("ShootCircle.png"));
        imgShoot.setSize(90, 90);
        imgShoot.setPosition(305, 15);

        imgShoot.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(enabled){
                    if(canShoot){
                        isShooting = true;
                        canShoot = false;

                        aimX = x - 40;
                        aimY = y - 40;
                    }
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isShooting = false;
                canShoot = true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                aimX = x - 40;
                aimY = y - 40;
            }
        });

        Image imgJump = new Image(new Texture("JumpCircle.png"));
        imgJump.setSize(70, 70);
        imgJump.setPosition(410, 15);

        imgJump.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(enabled && canJump){
                    isJumping = true;
                }
                return false;
            }
        });

        //add controller images to table
        table.add().size(30,50);
        table.add(imgLeft).size(imgLeft.getWidth(), imgLeft.getHeight());
        table.pad(0, 0, 0, 30);
        table.add(imgCenter).size(imgCenter.getWidth(), imgCenter.getHeight());
        table.pad(0, 0, 0, 30);
        table.add(imgRight).size(imgRight.getWidth(), imgRight.getHeight());
        table.padBottom(30);

        //add table to stage
        stage.addActor(table);
        stage.addActor(imgJump);
        stage.addActor(imgShoot);
    }

    public void draw(){
        stage.draw();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public boolean isMove() {
        return isMoving;
    }

    public boolean isJumping(){
        return isJumping;
    }

    public boolean isShooting(){
        return isShooting;
    }

    public void disable(){
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }

    public void setCanJump(boolean jump) {
        canJump = jump;
    }
}
