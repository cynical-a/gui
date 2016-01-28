
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the beginning of a simple game. You should expand it into a dodgeball
 * game, where the user controls an object with the mouse or keyboard, and tries
 * to avoid the balls flying around the screen. If they get hit, it's game over.
 * If they survive for 20 seconds (or some other fixed time), they go on to the
 * next level. <br>
 * <br>
 * Should be run at around 500x300 pixels.<br>
 * <br>
 * @version Nov. 2015
 * 
 * @author Christina Kemp adapted from Sam Scott
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, MouseListener {

	JLabel score = new JLabel("Score "+ scoreTotal);
	int width = 900;
	int height = 600;
	static int scoreTotal = 0;
	static int lives = 5;
	static boolean pause = false;
	static int counter = 0;
	/**
	 * The number of balls on the screen.
	 */
	static int numBalls = 10;
	/**
	 * The pause between repainting (should be set for about 30 frames per
	 * second).
	 */
	final int pauseDuration = 50;
	/**
	 * An array of balls.
	 */
	static ArrayList<FlashingBall> ball = new ArrayList<FlashingBall>();
//	static Spawner[] spawnedIn = new Spawner[5];
	static Keeper[] treasure = new Keeper[1];
	JLabel lifeCounter = new JLabel("\nLife "+ lives);
	static JLabel timeCounter = new JLabel("\nTime "+ counter);
    static Boolean endGame = false;
	
	static JPanel gui = new JPanel();


	/** main program (entry point) */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		// Set up main window (using Swing's Jframe)
		JFrame frame = new JFrame("Dodgeball");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 600));
		frame.setAutoRequestFocus(false);
				
		frame.setVisible(true);

	    Container c = frame.getContentPane();
		c.add(new GamePanel(), BorderLayout.CENTER);
		c.add(gui, BorderLayout.LINE_END);
		gui.setSize(new Dimension(200, 600));
		gui.setBackground(Color.GRAY);
		gui.setVisible(true);
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (pause == false){
					paused();
					}
				else{
					played();
					}
			}
		});
		JLabel fancyHeader = new JLabel("___________=========___________");
		JLabel score = new JLabel("\nScore "+ scoreTotal);

		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		gui.add(fancyHeader);
		gui.add(timeCounter);
		gui.add(pauseButton);
		frame.pack();
		
			
	}
	public static void paused(){
		pause = true;
		for (int i = 0; i < numBalls; i++){
			ball.get(i).stopThread();
		}
		treasure[0].stopThread();
	}
	
	public static void played(){
		if (endGame = false){
			pause = false;
			for (int i = 0; i < numBalls; i++){
					ball.get(i).startThread();
					
			}
			treasure[0].startThread();
		}
	}
	

	public GamePanel(){
		// Start the ball bouncing (in its own thread)
//		for (int i = 0; i < 120; i++){
//			if (i == 30){
//				g.drawString("3");
//			}
//		}
		addMouseListener (this);
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);

		for (int i = 0; i < numBalls; i++) {
			if (i < 20){
			ball.add(new FlashingBall(50, 50, 0, width, 0, height)) ;
			ball.get(i).setXSpeed(Math.random() * 16-8);
			ball.get(i).setYSpeed(Math.random() * 16-8);
			ball.get(i).setColor(new Color((int) (Math.random() * 256), (int) (Math
					.random() * 256), (int) (Math.random() * 256)));
			ball.get(i).stopped = false;
			}
			else{
				int tone = (int) (Math.random()*256);
				ball.add(new FlashingBall(50, 50, 0, width, 0, height));
				ball.get(i).setXSpeed(Math.random() * 16-8);
				ball.get(i).setYSpeed(Math.random() * 16-8);
				ball.get(i).setColor(new Color((int) (Math.random() * 256), (int) (Math
						.random() * 256), (int) (Math.random() * 256)));
				ball.get(i).stopped = false;
				ball.get(i).enemy = true;
				
			}
		

		}
//		for (int i = 0; i < 5; i++) {
//			spawnedIn[i] = new Spawner(200, 200, 0, width, 0, height);
//			spawnedIn[i].setXSpeed(2);
//			spawnedIn[i].setYSpeed(2);
//			spawnedIn[i].setColor(Color.RED);
//			spawnedIn[i].stopped = false;
//			}
		treasure[0] = new Keeper(300, 300, 0, width, 0, height);
		treasure[0].setXSpeed(1);
		treasure[0].setYSpeed(1);
		treasure[0].setColor(Color.YELLOW);
		treasure[0].stopped = false;
		
		

		Thread gameThread = new Thread(this);
		
		gameThread.start();

	

	}

	/**
	 * Repaints the frame periodically.
	 */
	static public boolean checkIntersect(Keeper treasure){
		for (int i = 0; i < ball.size(); i++){

			double dx = Math.abs(ball.get(i).getX()-treasure.getX()/2);
			double dy = Math.abs(ball.get(i).getY()- treasure.getY()/2);
			double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			if(distance <= ball.get(i).radius + treasure.getSize()/2){
				numBalls --;
				ball.remove(i);
				return true;
			}
			
//			if (dx > (treasure.getSize()/2 + ball.get(i).radius) ||
//					dy > (treasure.getSize()/2 + ball.get(i).radius)){
//				return false;
//				
//			}
//			
//			if (dx <= (treasure.getSize()/2) &&
//			dy <= (treasure.getSize()/2)){
//				numBalls --;
//				ball.remove(i);
//					return true;
//				}
			
//			if (dx <= (treasure.getSize()/2 + ball.get(i).radius) &&
//			dy <= (treasure.getSize()/2 + ball.get(i).radius)){
//				numBalls --;
//				ball.remove(i);
//					return true;
//				}
//			
//			double cornerDistance = Math.pow(dx - treasure.getSize()/2,2)+ Math.pow(dy - treasure.getSize()/2, 2);
//			if (cornerDistance <= Math.pow(ball.get(i).radius, 2)){
//				numBalls --;
//				ball.remove(i);
//				return true;
//			}
		}
		return false;
	}

	public void run() {
		while (endGame== false) {
			counter ++;
			timeCounter.repaint();

			//check collision with treasure square
			if(checkIntersect(treasure[0])){
				lives --;

				lifeCounter.setText("Life "+ lives);
				gui.add(lifeCounter);
				if (lives == 0){
					//System.exit(0);
					paused();
					endGame = true;
					this.setBackground(Color.RED);


				}
			}

			counter ++;

			int secondCounter = counter / 100;
			timeCounter.setText("Time "+ secondCounter);


			//spawning
			if ((counter % 300) == 0 && counter > 0){
				for (int i = 0; i < 5; i++){
					ball.add(new FlashingBall(50, 50, 0, width, 0, height)) ;
					ball.get(ball.size() -1).setXSpeed(Math.random() * 16-8);
					ball.get(ball.size() -1).setYSpeed(Math.random() * 16-8);
					ball.get(ball.size() -1).setColor(new Color((int) (Math.random() * 256), (int) (Math
							.random() * 256), (int) (Math.random() * 256)));
					ball.get(ball.size() -1).stopped = false;

				}
				numBalls = numBalls + 5;
			}
			repaint();
			try {
				Thread.sleep(pauseDuration);
			} catch (InterruptedException e) {
			}

		}

	}

	/**
	 * Clears the screen and paints the balls.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < numBalls; i++) {
			ball.get(i).draw(g);
		}
		if (endGame == true){
			g.drawString("You lost", 200, 300);
			int totalScore = scoreTotal + (counter / 2);
			g.drawString("Total Score" + totalScore, 200, 350);

		}
//		for (int i = 0; i < spawnedIn.length; i++){
//			spawnedIn[i].draw(g);
//		}
		treasure[0].draw(g);
	


	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (pause == false){
			for (int i = 0; i < numBalls; i++) {
				double d = Math.sqrt(((x - ball.get(i).getX())*(x - ball.get(i).getX())) +(((y - ball.get(i).getY())*(y - ball.get(i).getY()))));
				if (d < 100){
					if (ball.get(i).stopped == false){
						 ball.get(i).stopThread();
						  scoreTotal= scoreTotal +1;
						  score.setText("Score "+ scoreTotal);
							gui.add(score);
						  score.repaint();
				   		ball.get(i).stopped = true;
				   		ball.get(i).setXSpeed(0);
				   		ball.get(i).setYSpeed(0);
				   		ball.get(i).setColor(new Color((int) (0), (int) (0), (int) (0)));
				   		ball.get(i).radius = 0;
				   		}
					
					}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
