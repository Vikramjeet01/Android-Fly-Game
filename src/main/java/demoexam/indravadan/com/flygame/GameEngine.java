package demoexam.indravadan.com.flygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    final static String TAG="FLY-GAME";

    int screenHeight;
    int screenWidth;

    boolean gameIsRunning;

    Thread gameThread;

    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    Player player;
    Cat cat;
    Bird bird;

    Square bullet;
    Square cage;

    int SQUARE_WIDTH = 100;
    int CAGE_WIDTH = 200;

    List<Square> bullets = new ArrayList<Square>();

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        for(int i =0; i < 15; i++){
            Random r = new Random();
            int randomXPOS = r.nextInt(this.screenWidth) + 1;
            int randomYPOS = r.nextInt(this.screenHeight) + 1;
            Square b = new Square(getContext(), randomXPOS, randomYPOS, SQUARE_WIDTH );
        }

        this.bullet = new Square(context, 100, this.screenHeight - 400, SQUARE_WIDTH);
        this.cage = new Square(context, 1400, 50, CAGE_WIDTH);

        this.spawnPlayer();
        this.spawnCat();
        this.spawnBird();
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen

        player = new Player(this.getContext(), 50, this.screenHeight - 400 + 30);
    }

    private void spawnCat() {
        //Random random = new Random();

        //@TODO: Place the enemies in a random location
        cat = new Cat(this.getContext(), 1600, this.screenHeight - 400);

    }

    private  void spawnBird(){
        bird = new Bird(this.getContext(), this.screenWidth / 2, this.screenHeight / 4);
    }


    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    boolean cageMovingLeft = true;
    //boolean cageMovingDown = true;

    boolean gameOver = false;


    public void updatePositions() {


        // @TODO: Update position of enemy ships
        cat.updateCat();
        bird.updateBird();
        // @TODO: Update the position of the sprites
        if (cageMovingLeft == true) {
            this.cage.setxPosition(this.cage.getxPosition() - 30);
            //cageMovingDown = false;
        }
        else {
            this.cage.setxPosition(this.cage.getxPosition() + 30);
            //cageMovingDown = false;
        }

        this.cage.updateHitbox();

        if (this.cage.getxPosition() >= 1600) {
            cageMovingLeft = true;
            //cageMovingDown = false;
        }
        // R2. colliding with top of screen
        if (this.cage.getxPosition() <= 0 ) {
            cageMovingLeft = false;
            //cageMovingDown = false;
        }

        if (this.cage.getyPosition() >= this.screenHeight - CAGE_WIDTH){
            this.cage.setxPosition(1400);
            this.cage.setyPosition(50);
        }


        // @TODO: Collision detection between player and enemy

        if (bullet.getHitbox().intersect(cage.getHitbox())) {

            //cageMovingLeft = false;
            //cageMovingDown = true;

            this.cage.setyPosition(this.cage.getyPosition() + 50);

            // RESTART THE BULLET FROM INITIAL POSITION
            this.bullet.setxPosition(100);
            this.bullet.setyPosition(this.screenHeight - 400);

            // RESTART THE HITBOX
            this.bullet.updateHitbox();

            if (cage.getHitbox().intersect(cat.getHitbox())){
                gameOver = true;
                canvas.drawText("YOU WON", this.screenWidth / 2, this.screenHeight/ 2, paintbrush);
            }
        }



    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            this.canvas.drawColor(Color.argb(255,0,0,255));
            //paintbrush.setColor(Color.WHITE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            canvas.drawBitmap(this.player.getBitmap(), this.player.getXPosition(), this.player.getYPosition(), paintbrush);

            //@TODO: Draw the enemy
            canvas.drawBitmap(this.cat.getBitmap(), this.cat.getXPosition(), this.cat.getYPosition(), paintbrush);

            canvas.drawBitmap(this.bird.getBitmap(), this.bird.getXPosition(), this.bird.getYPosition(), paintbrush);

            paintbrush.setColor(Color.BLACK);
            canvas.drawRect(
                    this.bullet.getxPosition(),
                    this.bullet.getyPosition(),
                    this.bullet.getxPosition() + this.bullet.getWidth(),
                    this.bullet.getyPosition() + this.bullet.getWidth(),
                    paintbrush
            );
            canvas.drawRect(
                    this.bullet.getHitbox(),
                    paintbrush
            );

            for (int i = 0; i < bullets.size(); i++){
                Square b = bullets.get(i);
                canvas.drawRect(b.getxPosition(), b.getyPosition(), b.getxPosition() + SQUARE_WIDTH, b.getxPosition() + SQUARE_WIDTH, paintbrush);
            }

            paintbrush.setColor(Color.YELLOW);
            paintbrush.setStyle(Paint.Style.FILL);
            canvas.drawRect(
                    this.cage.getxPosition(),
                    this.cage.getyPosition(),
                    this.cage.getxPosition() + this.cage.getWidth(),
                    this.cage.getyPosition() + this.cage.getWidth(),
                    paintbrush
            );
            canvas.drawRect(
                    this.cage.getHitbox(),
                    paintbrush
            );


            // Show the hitboxes on player and enemy
            //paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);


            Rect enemyHitbox = cat.getHitbox();
            canvas.drawRect(enemyHitbox.left, enemyHitbox.top, enemyHitbox.right, enemyHitbox.bottom, paintbrush);

            Rect birdHitbox = bird.getHitbox();
            canvas.drawRect(birdHitbox.left, birdHitbox.top, birdHitbox.right, birdHitbox.bottom, paintbrush);


            // draw game stats
            paintbrush.setTextSize(60);
            //paintbrush.setColor(Color.BLACK);
            //canvas.drawText("Lives remaining: " + lives, 100, 800, paintbrush);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:

                for(int i = 0; i < bullets.size(); i++){
                    Square bul = bullets.get(i);

                    double e = (int) event.getX() - this.bullet.getxPosition();
                    double f = (int)event.getY() - this.bullet.getyPosition();

                    // d = sqrt(a^2 + b^2)

                    double d1 = Math.sqrt((e * e) + (f * f));

                    // 2. calculate xn and yn constants
                    // (amount of x to move, amount of y to move)
                    double xn1 = (e / d1);
                    double yn1 = (f / d1);

                    // 3. calculate new (x,y) coordinates
                    int newX = this.bullet.getxPosition() + (int) (xn1 * 100);
                    int newY = this.bullet.getyPosition() + (int) (yn1 * 100);
                    this.bullet.setxPosition(newX);
                    this.bullet.setyPosition(newY);

                    // 4. update the bullet hitbox position
                    this.bullet.updateHitbox();


                }



                break;

                case MotionEvent.ACTION_DOWN:

                    break;

        }

        return true;
    }
}
