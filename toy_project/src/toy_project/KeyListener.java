package toy_project;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

public class KeyListener extends KeyAdapter {

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		super.keyPressed(arg0);

		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
			PlayThread.dir = 1;
		else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
			PlayThread.dir = 0;
		else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			PlayThread.dir = 2;
		else if (arg0.getKeyCode() == KeyEvent.VK_UP)
			PlayThread.dir = 3;
		else if(arg0.getKeyCode() == KeyEvent.VK_SPACE) 
			PlayThread.dir = 32;
		
		if(arg0.getKeyCode() == KeyEvent.VK_S) {
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		super.keyReleased(arg0);
	}

}