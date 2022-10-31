package com.paunocustudios.entities;

import java.awt.Graphics;  
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.paunocustudios.graficos.spriteSheet;
import com.paunocustudios.main.Game;
import com.paunocustudios.world.Camera;
import com.paunocustudios.world.World;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = down_dir;
	public double speed = 0.7;
	
	private int frames = 0, maxFrames = 5,index = 0, maxIndex = 3;
	public boolean moved = false,mouseShoot = false;
	private BufferedImage[] rightplayer;
	private BufferedImage[] leftplayer;
	private BufferedImage[] upplayer;
	private BufferedImage[] downplayer;
	
	private BufferedImage playerDamage;
	
	private boolean hasGloves = false;
	
	private int damageFrames = 0;
	
	public int Transmutation = 0;
	
	public boolean shoot = false;
	
	public boolean isDamaged = false;
	
	public double life = 100, maxLife = 100;
	
	public int mx, my;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	
	public int z = 0;
	
	public int jumpFrames = 50, jumpCur = 0;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightplayer = new BufferedImage[4];
		leftplayer = new BufferedImage[4];
		upplayer = new BufferedImage[4];
		downplayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(8*16, 2*16, 16, 16);
		for (int i = 0; i < 4; i++) {
			rightplayer[i] = Game.spritesheet.getSprite(96 + (i * 16), 0, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			leftplayer[i] = Game.spritesheet.getSprite(48 - (i * 16), 16, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			upplayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 16, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			downplayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
	}
	
	public void tick() {
		
		if (jump) {
			if(isJumping = false) {
			jump = false;
			isJumping = true;
			}
		}
		
		if (isJumping == true) {
			if (jumpCur < jumpFrames) {
				jumpCur++;
				z = jumpCur;
				if(jumpFrames == jumpCur) {
					isJumping = false;
				}
			}
		}
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		
		if(up && World.isFree(this.getX(), (int)(y - speed))) {
			moved = true;
			dir = up_dir;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int)(y + speed))) {
			moved = true;
			dir = down_dir;
			y += speed;
		}
		
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if (shoot) {
			shoot = false;
			if(hasGloves && Transmutation > 0) {
			Transmutation--;
			//Criar fogo e atirar
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 8;
			if(dir == right_dir) {
				px = 16;
				dx = 1;
			} else if (dir == left_dir) {
				px = 0;
				dx = -1;
			} if (dir == up_dir) {
				px = 5;
				dy = -1;
			} else if (dir == down_dir) {
				px = 5;
				dy = 1;
			}
			
			glovesShoot fire = new glovesShoot(this.getX() + px, this.getY() + py, 5, 5, null, dx, dy);
			Game.fires.add(fire);
			}
		}
		
		if (mouseShoot) {
			mouseShoot = false;
			if(hasGloves && Transmutation > 0) {
			Transmutation--;
			//Criar fogo e atirar
			double angle = 0;

			int px = 0;
			int py = 8;
			if(dir == right_dir) {
				px = 16;
			angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX() + px - Camera.x));
			} else if (dir == left_dir) {
				px = 0;
			angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX() + px - Camera.x));
			} if (dir == up_dir) {
				px = 5;
			angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX() + px - Camera.x));
			} else if (dir == down_dir) {
				px = 5;
			angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX() + px - Camera.x));
			}
			
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			
			glovesShoot fire = new glovesShoot(this.getX() + px, this.getY() + py, 5, 5, null, dx, dy);
			Game.fires.add(fire);
			}
		}
		
		checkCollisionLife();
		checkCollisionTransmutation();
		checkCollisionGloves();
		
		if(isDamaged) {
			this.damageFrames++;
			if(damageFrames == 2) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
			
		if (life <= 0) {
			//Game over
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void checkCollisionTransmutation( ) {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Transmutation) {
				if(Entity.isColidding(this, atual)) {
					Transmutation+=10;
					//System.out.println("Circulos de transmutação atuais:" + Transmutation);
					Game.entities.remove(atual);
				}
				
			}
		}
	}
	
	public void checkCollisionGloves( ) {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Gloves) {
				if(Entity.isColidding(this, atual)) {
					hasGloves = true;
					Game.entities.remove(atual);
				}
				
			}
		}
	}
	
	public void checkCollisionLife() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Life) {
				if(Entity.isColidding(this, atual)) {
					life+=10;
					if (life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
				
			}
		}
		
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
		if (dir == right_dir) {
			g.drawImage(rightplayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if (hasGloves) {
				//Desenhar Gloves para a direita
				g.drawImage(Entity.Gloves_all, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}
		} else if (dir == left_dir) {
			g.drawImage(leftplayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if (hasGloves) {
				//Desenhar Gloves para a esquerda
				g.drawImage(Entity.Gloves_all, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}
		}
		if (dir == up_dir) {
			g.drawImage(upplayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if (hasGloves) {
				//Desenhar Gloves para cima
				g.drawImage(Entity.Gloves_up, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}
		} else if (dir == down_dir) {
			g.drawImage(downplayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if(hasGloves) {
			//Desenhar Gloves para baixo
				g.drawImage(Entity.Gloves_all, this.getX() - Camera.x, this.getY() - Camera.y - z, null);			}
		}
		} else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
		}
	}

}
