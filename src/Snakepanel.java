import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Snakepanel extends JPanel implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1000, HEIGHT = 1000;
	
	private Thread thread;
	private boolean running;
	
	private boolean right = true, left = false, up = false, down = false;
	
	private Body b;
	private ArrayList<Body> snk; 		// Body of the snake
	
	private Apple apple;
	private ArrayList<Apple> apples;	// Apples of the game
	private Random r;
	
	private int xCoor = 10, yCoor = 10, size = 5;	// Initial position and starting size
	private int ticks = 0;
	
	public Snakepanel() {
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		
		snk = new ArrayList<Body>();
		apples = new ArrayList<Apple>();
		
		r = new Random();
		
		start();
	}
	
	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if (snk.size() == 0) {	//	Creation of snake (beginning)
			b = new Body(xCoor, yCoor, 10);
			snk.add(b);
		}
		ticks++;
		if (ticks > 250000) {
			if (right) xCoor++;
			if (left) xCoor--;
			if (up) yCoor--;
			if (down) yCoor++;
			ticks = 0;
			
			b = new Body(xCoor, yCoor, 10);
			snk.add(b);
			
			if(snk.size() > size) {
				snk.remove(0);
			}
		}
		if (apples.size() == 0) {	// Placement of apples (beginning)
			int xCoor = r.nextInt(49);
			int yCoor = r.nextInt(49);
			apple = new Apple(xCoor, yCoor, 10);
			apples.add(apple);
		}
		
		for (int i = 0; i < apples.size(); i++) {	// Snake eats apple
			if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				size++;
				apples.remove(i);
				i++;
			}
		}
		
		for (int i = 0; i < snk.size(); i++) {		// Snake eats snake
			if (xCoor == snk.get(i).getxCoor() && yCoor == snk.get(i).getyCoor()) {
				if (i != snk.size()-1) {
					System.out.println("Game Over!");
					stop();
				}
			}
		}
		
		if (xCoor < 0 || xCoor > 99 || yCoor < 0 || yCoor > 99) {	// Out of Bounds
			System.out.println("Game Over!");
			stop();
		}
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.CYAN);		
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (int i=0; i < WIDTH/10; i++) {
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		for (int i=0; i < HEIGHT/10; i++) {
			g.drawLine(0, i * 10, WIDTH, i * 10);
		}
		for (int i = 0; i < snk.size(); i++) {
			snk.get(i).draw(g);
		}
		for (int i = 0; i < apples.size(); i++) {
			apples.get(i).draw(g);
		}
		
	}
	
	@Override
	public void run() {		// Engine
		while(running) {
			tick();
			repaint();
		}
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			up = false;
			down = false;
		} else if (key == KeyEvent.VK_LEFT && !right) {
			left = true;
			up = false;
			down = false;
		} else if (key == KeyEvent.VK_UP && !down) {
			up = true;
			left = false;
			right = false;
		} else if (key == KeyEvent.VK_DOWN && !up) {
			down = true;
			left = false;
			right = false;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
