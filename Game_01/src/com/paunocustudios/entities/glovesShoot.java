package com.paunocustudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.paunocustudios.main.Game;
import com.paunocustudios.world.Camera;

public class glovesShoot extends Entity{
	
	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 30, curLife = 0;
	
	//private int frames = 0, maxFrames = 5,index = 0, maxIndex = 4;
	//private BufferedImage[] rightFire;
	//private BufferedImage[] leftFire;
	//private BufferedImage[] upFire;
	//private BufferedImage[] downFire;
	
	public glovesShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		
		this.dx = dx;
		this.dy = dy;

		}
		
		//rightFire = new BufferedImage[5];
		//leftFire = new BufferedImage[5];
		//upFire = new BufferedImage[5];
		//downFire = new BufferedImage[5];
		
		/*
		
		for (int i = 0; i < 5; i++) {
			rightFire[i] = Game.spritesheet.getSprite(16 + (i * 16), 3 * 16, 16, 16);
		}
		
		for (int i = 0; i < 5; i++) {
			leftFire[i] = Game.spritesheet.getSprite(6 * 16 - (i * 16), 3 * 16, 16, 16);
			if(i == 4)
				leftFire[i] = Game.spritesheet.getSprite(0, 4 * 16, 16, 16);
		}
		
		for (int i = 0; i < 5; i++) {
			upFire[i] = Game.spritesheet.getSprite(16 + (i * 16), 4 * 16, 16, 16);
		}
		
		for (int i = 0; i < 5; i++) {
			downFire[i] = Game.spritesheet.getSprite(6 * 16 + (i * 16), 4 * 16, 16, 16);
			if (i == 4)
				downFire[i] = Game.spritesheet.getSprite(0, 5 * 16, 16, 16);
		}
		
		*/
		
	
	public void tick() {
		x+=dx * spd;
		y+=dy * spd;
		curLife++;
		if(curLife == life) {
			Game.fires.remove(this);
			return;
		}
		/*
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			*/
	}
	
	public void render(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
		//g.drawImage(rightFire[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.drawImage(leftFire[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.drawImage(upFire[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.drawImage(downFire[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}

