package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
    TextureRegion down, up, left, right;
    SpriteBatch batch;
    TextureRegion currentStance;
    float time;
    float x, y, xv, yv, a;
    Animation walk;
    boolean canRun = true;
    private String message = "Touch something already!";

    static final int WIDTH = 26 ; //two constants to represent the sprite in the game
    static final int HEIGHT = 26; //split texture by 18 and 26 chunks;
    static final int DRAW_WIDTH = WIDTH * 2;
    static final int DRAW_HEIGHT = HEIGHT * 2;
    static final float MAX_VELOCITY = 200; //movement

    @Override
    public void create() {
        batch = new SpriteBatch();
        Texture tiles = new Texture("tiles.png");
        TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
        down = grid[6][0];
        up = grid[6][1];
        right = grid[6][3];
        left = new TextureRegion(right);
        left.flip(true, false);
        walk = new Animation(0.2f, grid[0][4], grid[0][5], grid[0][6]);
    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime(); //time spent between render call // pass in anmination to get key frame
        move();

        // screen();
        Gdx.gl.glClearColor(0, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(currentStance, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    float decelerate(float velocity) {
        float deceleration = .001f; // the closer to 1, the slower the deceleration
        velocity *= deceleration;
        if (Math.abs(velocity) < .005) {
            velocity = 0;
        }
        return velocity;
    }

    void move() {
        a = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yv = MAX_VELOCITY;
            currentStance = up;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
            currentStance = down;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
            currentStance = right;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
            currentStance = left;
        } else {
            currentStance = down;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canRun) {
            // we are not already running and
            // we are hitting the space key
            a = 6;
            canRun = false;
        } else {
            // if we're not running already or
            // we're not hitting space already
            canRun = true;
        }

        if (x < -DRAW_WIDTH) {
            x = Gdx.graphics.getWidth();
        }
        if (x > Gdx.graphics.getWidth()) {
            x = -DRAW_WIDTH;
        }

        y += a * yv * Gdx.graphics.getDeltaTime();//calculates new positions x and y for velocities and time
        x += a * xv * Gdx.graphics.getDeltaTime();

        //if (Gdx.input.isKeyPressed())
        yv = decelerate(yv);
        xv = decelerate(xv);
    }
}

////
//    }