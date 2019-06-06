package demoexam.indravadan.com.flygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Cat {

    int xPosition;
    int yPosition;
    int direction;
    Bitmap image;

    private Rect hitBox;

    boolean catMovingLeft = true;

    public Cat(Context context, int x, int y){
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat64);
        this.xPosition = x;
        this.yPosition = y;

        this.hitBox = new Rect(this.xPosition, this.yPosition , this.xPosition + this.image.getWidth(), this.yPosition + this.image.getHeight());

    }

    public void updateCat() {

        if(catMovingLeft == true){
            this.xPosition = this.xPosition - 30;
        }
        else {
            this.xPosition = this.xPosition + 30;
        }

        if (this.xPosition <= 0) {

            //this.xPosition = this.xPosition + 15;

            catMovingLeft = false;
        }

        if(this.xPosition >=1600){
            catMovingLeft = true;
        }


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
