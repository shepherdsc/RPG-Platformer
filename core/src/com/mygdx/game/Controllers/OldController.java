package com.mygdx.game.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainRPG;

/**
 * Created by scott_000 on 7/19/2016.
 */
public class OldController {
    Viewport viewport;
    Stage stage;
    public boolean isMoving, isJumping, drawShotLine;
    public float currAimX, currAimY, initialAimX, initialAimY;
    public float velMove;
    OrthographicCamera cam;

    public OldController(){

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
                isMoving = true;
                velMove = (x - 65) / 60;
                if(velMove > 1.2){
                    velMove = 1.2f;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                if(y > 60){
                    isJumping = true;

                }else{
                    velMove = 0;
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
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

        });

        imgRight.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = true;
                velMove = 1;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                if(y > 60){
                    isJumping = true;

                }else{
                    velMove = 0;
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
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

        });

        imgCenter.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = true;
                velMove = 0;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMoving = false;
                if(y > 60){
                    isJumping = true;

                }else{
                    velMove = 0;
                }
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

        Image imgAimCenter = new Image(new Texture("midcircle.png"));
        imgAimCenter.setSize(80, 80);
        imgAimCenter.setPosition(380, 20);
        //set initial aim position
        initialAimX = imgAimCenter.getX() + imgAimCenter.getWidth() / 2;
        initialAimY = imgAimCenter.getY() + imgAimCenter.getHeight() / 2;

        stage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(x > stage.getWidth() / 2){
                    currAimX = x;
                    currAimY = y;
                    drawShotLine = true;
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currAimX = x;
                currAimY = y;
                drawShotLine = false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //make sure drag is different from start
                if(x > initialAimX + 1 || x < initialAimX - 1 || y > initialAimY + 1 || y < initialAimY - 1 || drawShotLine) {
                    currAimX = x;
                    currAimY = y;
                    drawShotLine = true;
                }
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
        stage.addActor(imgAimCenter);
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

    public boolean isJumpFling(){
        return isJumping;
    }

    public boolean isDrawShotLine(){
        return drawShotLine;
    }
}

