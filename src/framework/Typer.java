package framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Typer implements KeyListener {

Core c;
	
	public Typer(Core core) {
		c = core;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int typed = e.getKeyChar();
		
		//w
		if(typed == 119){
			c.movingUp = true;
		}
		
		//a
		if(typed == 97){
			c.movingLeft = true;	
		}
		
		//s
		if(typed == 115){
			c.movingDown = true;	
		}
		
		//d
		if(typed == 100){
			c.movingRight = true;			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int typed = e.getKeyChar();
		
		if(typed == 119){
			c.movingUp = false;
		}
		
		//a
		if(typed == 97){
			c.movingLeft = false;	
		}
		
		//s
		if(typed == 115){
			c.movingDown = false;	
		}
		
		//d
		if(typed == 100){
			c.movingRight = false;			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}

}
