import javax.swing.JFrame;

public class Main {
	
	public Main() {
		JFrame frame = new JFrame();
		Snakepanel gamepanel = new Snakepanel();
		
		frame.add(gamepanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Snake By WillG");
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Main();
	}

}
