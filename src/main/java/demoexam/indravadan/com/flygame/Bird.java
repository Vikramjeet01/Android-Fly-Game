package demoexam.indravadan.com.flygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Bird {

    int xPosition;
    int yPosition;
    int direction;
    Bitmap image;

    private Rect hitBox;

    boolean birdMovingLeft = true;

    public Bird(Context context, int x, int y){
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird64);
        this.xPosition = x;
        this.yPosition = y;

        this.hitBox = new Rect(this.xPosition, this.yPosition , this.xPosition + this.image.getWidth(), this.yPosition + this.image.getHeight());

    }

    public void updateBird() {

        Random r = new Random();
        int randomXPos = r.nextInt(1200) + 1;
        int randomYPos = r.nextInt(1200) + 1;
        setXPosition(randomXPos);
        setYPosition(randomYPos);

        // update the position of the hitbox
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.image.getWidth();
        this.updateHitbox();

    }

    public void updateHitbox() {
        // update the position of the hitbox
        this.hitBox.left = this.xPosition;
        this.hitBox.top = this.yPosition + 20;
        this.hitBox.right = this.xPosition + this.image.getWidth();
        this.hitBox.bottom = this.yPosition + 20 + this.image.getHeight() - 20;
    }

    public Rect getHitbox() {
        return this.hitBox;
    }

    public void setXPosition(int x) {
        this.xPosition = x;
        this.updateHitbox();
    }
    public void setYPosition(int y) {
        this.yPosition = y;
        this.updateHitbox();
    }
    public int getXPosition() {
        return this.xPosition;
    }
    public int getYPosition() {
        return this.yPosition;
    }

    public Bitmap getBitmap() {
        return this.image;
    }
}
