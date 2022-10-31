package com.paunocustudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.paunocustudios.main.Game;
import com.paunocustudios.main.Sound;
import com.paunocustudios.world.Camera;
import com.paunocustudios.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.4;
	
	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
	
	private int frames = 0, maxFrames = 40,index = 0, maxIndex = 2;
	
	private BufferedImage[] sprites;
	
	private int life = 3;
	
	private boolean isDamaged = false;
	
	private int damagedFrames = 5, damagedCurrent = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprite(16, 32, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(32, 32, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(48, 32, 16, 16);
		
	}
	
	public void tick() {
		if(this.isColiddingWithPlayer() == false) {
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY()) 
			&& !isColidding((int)(x + speed), this.getY())){
			x+=speed;
		} else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
				&& !isColidding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if ((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
				&& !isColidding(this.getX(), (int)(y+speed))) {
			y+=speed;
		} else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
				&& !isColidding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			if (Game.rand.nextInt(100) < 10) {
			Sound.hurtEffect.play();
			Game.player.life--;
			Game.player.isDamaged = true;
			}
		}
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
			
			collidingFire();
			
			if(life <= 0) {
				destroySelf();
				return;
			}
			
			if (isDamaged) {
				this.damagedCurrent++;
				if (this.damagedCurrent == this.damagedFrames) {
					this.damagedCurrent = 0;
					this.isDamaged = false;
					
				}
			}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingFire() {
		for (int i = 0; i < Game.fires.size(); i++) {
			Entity e = Game.fires.get(i);
			if (e instanceof glovesShoot) {
				
				if (Entity.isColidding(this, e)) {
					isDamaged = true;
					life--;
					Game.fires.remove(i);
					return;
				}
			}
		}

	}
	
	
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
			for (int i = 0; i < Game.enemies.size(); i++) {
				Enemy e = Game.enemies.get(i);
				if (e == this)
					continue;
				Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
				if (enemyCurrent.intersects(targetEnemy)) {
					return true;
				}
			}
		
		return false;
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	} else {
		g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	}

}
