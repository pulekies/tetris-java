package Tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener
{
	//The inputmanager singleton
	private static InputManager singleton = new InputManager();
	
	//Number of keys
	public static final int NUM_KEYS = 256;
	//Buffer of keys
	public static final int BUFFER_SIZE = 1024;
	
	//These keep track of keys whether keys are currently pushed or not
	private boolean[] key_state_up = new boolean[NUM_KEYS];
	private boolean[] key_state_down = new boolean[NUM_KEYS];
	
	//Used to check if any key was pushed/released
	private boolean keyPressed = false;
	private boolean keyReleased = false;
	
	//buffer
	private String keyBuffer = "";
	
	protected InputManager() {
	}
	
	public static InputManager getInstance() 
	{
	     return singleton;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("InputManager: A key has been pressed code=" + e.getKeyCode());
		int keyCode = e.getKeyCode();
		if(keyCode >=0 && keyCode <= NUM_KEYS)
		{
			key_state_down[e.getKeyCode()] = true;			 
			key_state_up[e.getKeyCode()] = false;
			keyPressed = true;
			keyReleased = false;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		//System.out.println("InputManager: A key has been released code=" + e.getKeyCode());
		int keyCode = e.getKeyCode();
		if(keyCode >=0 && keyCode <= NUM_KEYS)
		{
			key_state_down[e.getKeyCode()] = false;			 
			key_state_up[e.getKeyCode()] = true;
			keyPressed = false;
			keyReleased = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyBuffer += e.getKeyChar();
	}
	
	//Is specific key down?
	public boolean isKeyDown( int key ) {
		return key_state_down[key];
	}
	
	//Is a specific key released?
	public boolean isKeyUp( int key )
	{
		return key_state_up[key];
	}
	
	//Is any key currently pushed?
	public boolean isAnyKeyDown() 
	{
		return keyPressed;
	}
	
	//Any key released this frame?
	public boolean isAnyKeyUp() {
		return keyReleased;
	}
	
	public void update()
	{
		key_state_up = new boolean[NUM_KEYS];
		keyReleased = false;
		if( keyBuffer.length() > BUFFER_SIZE ) 
			keyBuffer = "";
	}
}
