package Tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

public class Tetris extends JFrame 
{
	static final int COLUMNS = 10;
	static final int ROWS = 20;
	static final int TILE_WIDTH = 20;
	static final int WIDTH = COLUMNS * TILE_WIDTH;
	static final int HEIGHT = ROWS * TILE_WIDTH;

	// Map tiles
	int tiles[][] = new int[ROWS][COLUMNS];
	Color colors[][] = new Color[ROWS][COLUMNS];

	InputManager inputManager = InputManager.getInstance(); // Keyboard polling
	Canvas canvas; 											// Drawing component

	// Current Shape
	Shape currentShape;
	int imageX;
	int imageY;

	// Current Shape Control Control
	boolean leftFirst;
	boolean rightFirst;
	boolean rotateFirst;
	
	//Game State
	GameState gameState;
	int score = 0;
	int frameCount = 0;
	
	
	private enum GameState
	{
		PushToStart,
		Running,
		Paused,
		GameOver;
	}

	public Tetris() 
	{
		leftFirst = true;
		rightFirst = true;
		rotateFirst = true;		
		
		setIgnoreRepaint(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(WIDTH, HEIGHT);
		add(canvas);
		pack();

		setTitle("Tetris");
		setVisible(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int windowX = Math.max(0, (screenSize.width - WIDTH ) / 2);
		int windowY = Math.max(0, (screenSize.height - HEIGHT ) / 2);
		setLocation(windowX, windowY);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Hook up input manager
		addKeyListener(inputManager);
		canvas.addKeyListener(inputManager);

		// initialize the tiles to being empty
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				tiles[i][j] = -1;
			}
		}
		this.setResizable(false);
		
		run();

	}

	private void run() {
		canvas.createBufferStrategy(2);
		BufferStrategy buffer = canvas.getBufferStrategy();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage bi = gc.createCompatibleImage(WIDTH, HEIGHT);

		Graphics graphics = null;
		Graphics2D g2d = null;
		Color background = Color.BLACK;

		imageX = 0;
		imageY = 0;
		createNewShape();
		
		gameState = GameState.PushToStart;
		Boolean gamePausedFirst = true;

		while (true) 
		{
			inputManager.update();
			if (inputManager.isKeyDown(KeyEvent.VK_Q))
			{
				this.dispose();
				break;
			}
			if(gameState == GameState.Paused)
			{
				g2d.setColor(Color.CYAN);
				g2d.drawString("Push 'P' to Resume", WIDTH/2 - 65, HEIGHT/2 - 25);
				
				if (inputManager.isKeyDown(KeyEvent.VK_P)) 
				{
					if (gamePausedFirst) 
					{
						gameState = GameState.Running;
						gamePausedFirst = false;
						
					}
				} else
					gamePausedFirst = true;
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi, 0, 0, null);
				if (!buffer.contentsLost())
					buffer.show();
				
				try 
				{
					Thread.sleep(5);
				} catch (InterruptedException e) {}
			}
			
			g2d = bi.createGraphics();
			g2d.setColor(background);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			
			if(gameState == GameState.PushToStart)
			{
				
				g2d.setColor(Color.CYAN);
				
				g2d.drawString("Push 'Space' to Start", WIDTH/2 - 65 ,HEIGHT/2 - 25);
				g2d.drawString("Push 'P' to Pause", WIDTH/2 - 58 ,HEIGHT/2 - 5);
				
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi, 0, 0, null);
				if (!buffer.contentsLost())
					buffer.show();
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				
				if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) 
				{
					gameState = GameState.Running;
				} 
			}
			else if(gameState == GameState.Running)
			{
				if (inputManager.isKeyDown(KeyEvent.VK_P)) 
				{
					if (gamePausedFirst) 
					{
						gameState = GameState.Paused;
						gamePausedFirst = false;
						
					}
				} else
					gamePausedFirst = true;
			
				if(frameCount == 0)
				{
					
					updateControl();
					updateObjects();	
					
					
					int scoreMultiplier = 0;
					for (int i = ROWS-1; i >= 0; i--) 
					{
						boolean completeRow = true;
						for (int j = 0; j < COLUMNS; j++) 
						{
							if(tiles[i][j] == -1)
							{
								completeRow = false;
							}
						}
						if(completeRow)
						{
							scoreMultiplier++;
							for (int k = i; k > 0; k--) 
							{
								for (int l = 0; l < COLUMNS; l++) 
								{
									tiles[k][l] = tiles[k-1][l];
									colors[k][l] = colors[k-1][l];
								}
							}
							score += scoreMultiplier;
							i++;
						}	
					}
		
					// Draw existing tiles
					for (int i = 0; i < ROWS; i++) {
						for (int j = 0; j < COLUMNS; j++) {
							if (tiles[i][j] != -1) {
								g2d.setColor(colors[i][j]);
								g2d.fillRect(j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
								g2d.setColor(Color.DARK_GRAY);
								g2d.drawRect(j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
							} 
						}
					}
		
					// Draw shape
					for (int i = 0; i < Shape.MAX_SIZE; i++)
						for (int j = 0; j < Shape.MAX_SIZE; j++)
							if (currentShape.tilemap[i][j])
							{
								g2d.setColor(currentShape.color);
								g2d.fillRect(imageX + j * TILE_WIDTH, imageY + i* TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
								g2d.setColor(Color.DARK_GRAY);
								g2d.drawRect(imageX + j * TILE_WIDTH, imageY + i* TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
							}
		
					g2d.setColor(Color.CYAN);
					
					g2d.drawString("Score: " + score, 0, 10);
					
					graphics = buffer.getDrawGraphics();
					graphics.drawImage(bi, 0, 0, null);
					if (!buffer.contentsLost())
						buffer.show();
				}
				try 
				{
					Thread.sleep(4);
					frameCount++;
					frameCount %= 2;
				} catch (InterruptedException e) {}
			}
		}

	}

	private void updateControl() 
	{
		boolean incrementX = true;
		boolean decrementX = true;
		boolean rotate = true;
		
		if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) 
		{
			if (rightFirst) 
			{
				rightFirst = false;
				for (int i = 0; i < Shape.MAX_SIZE; i++) 
				{
					for (int j = 0; j < Shape.MAX_SIZE; j++) 
					{
						if (currentShape.tilemap[i][j])
						{
							if (imageX + (j + 1) * TILE_WIDTH == WIDTH) // Check
								incrementX = false;
							else if (tiles[imageY/TILE_WIDTH + i][imageX/TILE_WIDTH + j + 1] != -1) // Check overlapping
								incrementX = false;
						}
					}
				}
				if (incrementX) 
					imageX += TILE_WIDTH;
			}
		} 
		else
			rightFirst = true;
		if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) 
		{
			if (leftFirst) 
			{
				leftFirst = false;
				for (int i = 0; i < Shape.MAX_SIZE; i++) 
				{
					for (int j = 0; j < Shape.MAX_SIZE; j++) 
					{
						if (currentShape.tilemap[i][j])
						{
							if (imageX + j * TILE_WIDTH == 0) // Check
								decrementX = false;
							else if (tiles[imageY/TILE_WIDTH + i][imageX/TILE_WIDTH + j - 1] != -1) // Check overlapping
								decrementX = false;
						}
					}
				}
				if (decrementX) 
					imageX -= TILE_WIDTH;
			}
		}
		else
			leftFirst = true;
		
		if (inputManager.isKeyDown(KeyEvent.VK_UP)) 
		{
			Shape tempShape = currentShape.rotateRight();
			if (rotateFirst) 
			{
				rotateFirst = false;
				for (int i = 0; i < Shape.MAX_SIZE; i++) 
				{
					for (int j = 0; j < Shape.MAX_SIZE; j++) 
					{
						if (tempShape.tilemap[i][j])
						{
							if (imageX + j * TILE_WIDTH < 0) // Check
								rotate = false;
							else if (imageX + j * TILE_WIDTH >= WIDTH) // Check
								rotate = false;
							else if (imageY + i * TILE_WIDTH >= HEIGHT-TILE_WIDTH) // Check
								rotate = false;
							else if (imageY + i * TILE_WIDTH < 0) // Check
								rotate = false;
							else if (tiles[imageY/TILE_WIDTH + i][imageX/TILE_WIDTH + j] != -1) // Check overlapping
								rotate = false;
						}
					}
				}
				if (rotate) 
					currentShape = tempShape;
			}
		}
		else
			rotateFirst = true;
	}

	private void updateObjects() {
		// Check for shape hitting bottoom of map
		boolean incrementY = true;

		for (int i = 0; i < Shape.MAX_SIZE; i++) {
			for (int j = 0; j < Shape.MAX_SIZE; j++) 
			{
				if (currentShape.tilemap[i][j]) 
				{
					if (imageY + (i + 1) * TILE_WIDTH == HEIGHT) // Chec
					{
						incrementY = false;
					}
					else if(imageY / TILE_WIDTH + i + 1 < ROWS)
					{
						if (tiles[imageY / TILE_WIDTH + i + 1][imageX/ TILE_WIDTH + j] != -1) // Check stacking
							incrementY = false;
					}
				}
			}
		}
		if (incrementY)
			imageY++;
		else // Shape at bottom, new shape
		{
			for (int i = 0; i < Shape.MAX_SIZE; i++) {
				for (int j = 0; j < Shape.MAX_SIZE; j++) {
					if (currentShape.tilemap[i][j]) 
					{
						if(imageY / TILE_WIDTH + i < 2)
							gameState = GameState.GameOver;
						tiles[imageY / TILE_WIDTH + i][imageX / TILE_WIDTH + j] = 1;
						colors[imageY / TILE_WIDTH + i][imageX / TILE_WIDTH + j] = currentShape.color;
					}
				}
			}
			createNewShape();
		}
	}

	private void createNewShape() {
		imageY = -TILE_WIDTH;
		imageX = WIDTH/2-3*TILE_WIDTH;
		Random rand = new Random();
		int shape = Math.abs(rand.nextInt()) % 7;
		
		ShapeType shapeType = ShapeType.Square;
		
		switch (shape) 
		{
			case 0:
				shapeType = ShapeType.Long;
				break;
			case 1: 
				shapeType = ShapeType.Square;
				break;
			case 2:
				shapeType = ShapeType.L;
				break;
			case 3: 
				shapeType = ShapeType.ReverseL;
				break;
			case 4:
				shapeType = ShapeType.Z;
				break;
			case 5: 
				shapeType = ShapeType.ReverseZ;
				break;
			case 6: 
				shapeType = ShapeType.Tri;		
				break;
			default:
				break;
		}
		currentShape = new Shape(shapeType);
	}
	
	public static void main(String args[]) 
	{
		new Tetris();
	}

}
