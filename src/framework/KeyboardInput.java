package framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyboardInput implements KeyListener {

World world;
	
	public KeyboardInput(World core) {
		world = core;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int typed = e.getKeyChar();
		
		//w
		if(typed == 119){
			world.movingUp = true;
		}
		
		//a
		if(typed == 97){
			world.movingLeft = true;	
		}
		
		//s
		if(typed == 115){
			world.movingDown = true;	
		}
		
		//d
		if(typed == 100){
			world.movingRight = true;			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int typed = (int)e.getKeyChar();
		
		//System.out.println(typed);
		
		// uncomment this line to find out the integer that corresponds to a key
		//System.out.println((int)e.getKeyChar());
		
		if (typed == 119){
			world.movingUp = false;
		}
		
		//a
		if (typed == 97){
			world.movingLeft = false;	
		}
		
		//s
		if (typed == 115){
			world.movingDown = false;	
		}
		
		//d
		if (typed == 100){
			world.movingRight = false;			
		}
		
		// n
		if (typed == 110) {
			world.populate();
		}
		
		// space
		if (typed == 32) {
			if (world.doIterate == false) {
				world.doIterate = true;
				world.frame.draw.cycleButton.setText("Stop Cycle (space)");
			}
			else
			{
				world.doIterate = false;
				world.frame.notifyIterationsEnd();
				world.frame.draw.cycleButton.setText("Start Cycle (space)");
			}
		}
		
		// c (clear)
		if (typed == 99 ) {
			world.clear();			
		}
		
		// j
		if (typed == 106) {
			Settings set = Settings.getInstance();
			set.matingEnergyCost = 0.16f;
			//set.newSettings('j');
		}
		
		
		// +
		if (typed == 61) {
			world.setIterations(world.getIterations() + 1);
		}
		
		// -
		if (typed == 45) {
			world.setIterations(world.getIterations() - 1);
		}
		
		// x
		if (typed == 120) {
			StatisticManager.getInstance().printStatisticsToText();
		}
		
		//System.out.println(typed);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}

}
