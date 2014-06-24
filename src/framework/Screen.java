package framework;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Screen extends JFrame {

	private static final long serialVersionUID = 1L;
	Core core = new Core(this);
	Clicker click = new Clicker(core);
	Typer type = new Typer(core);
	Drawer draw = new Drawer(core);
	
	public Screen(String string){
		super(string);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1350, 800);
		setVisible(true);
		
		addMouseListener(click);
		addMouseMotionListener(click);
		addMouseWheelListener(click);
		addKeyListener(type);
		draw.setVisible(true);
		add(draw);
		
		
	}
}
