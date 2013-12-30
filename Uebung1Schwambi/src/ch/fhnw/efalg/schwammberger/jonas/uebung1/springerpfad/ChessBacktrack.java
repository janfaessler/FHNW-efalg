package ch.fhnw.efalg.schwammberger.jonas.uebung1.springerpfad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class ChessBacktrack {
	private LinkedList<Board.Field> solution;
	private int max;
	private Board b;
	private int n;
	
	public ChessBacktrack(int n)
	{
		this.n = n;
		if(n > 3) {
			solution = new LinkedList<>();
			max = n*n-1;
			b = new Board(n);
		}
	}
	
	public LinkedList<Board.Field> findSolution(int startX, int startY) {
		if( n > 3) {
			chessBacktrack(b,0,b.getField(startX, startY));
			
			
		} else {
			//special cases
			if(n == 1)
				solution.add(b.getField(startX, startY));
			
			//print nothing, there is no solution
		}
		
		return solution;
	}
	
	
	public boolean chessBacktrack(Board b,int step,Board.Field f) {
		Iterator<Board.Field> it = b.getGreedy(f);
		
		if(step < max)
		{
			while(it.hasNext())
			{	
				Board.Field nextf = it.next();
				
				if(chessBacktrack(b,step+1,nextf))
				{
					//found solution
					solution.add(f);
					return true;
				}				
			}
			
			b.backTrack(f);
			return false;
		}
		
		solution.add(f);
		System.out.println();
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public Image DrawSolution() {
		int fSize = 15;	//pixel size of a single field
		int offset = fSize/2+1;
		BufferedImage im = new BufferedImage(n*fSize, n*fSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = im.createGraphics();
		
		int yPos = 0;
		for(int i = 0; i < n;i++)
		{
			int xPos = 0;
			for(int j = 0; j < n;j++)
			{
				if(i + (j % 2)% 2 == 0)
				{
					g.setColor(Color.black);
				} else {
					g.setColor(Color.white);
				}
				
				g.fillRect(xPos, yPos, fSize, fSize);
				xPos += fSize;
			}
			yPos += fSize;
		}
		
		Iterator<Board.Field> it = solution.descendingIterator();
		/*
		//draw begin circle
		Board.Field fBegin = null;
		if(it.hasNext()) {
			fBegin = it.next();
			g.setColor(Color.blue);
			g.fillOval(fBegin.x*fSize,fBegin.y*fSize, fSize, fSize);
		}
		
		//draw line
		while(it.hasNext())
		{
			
			Board.Field fEnd = it.next();
			g.setColor(Color.red);
			g.drawLine(fBegin.x*fSize+offset, fBegin.y*fSize+offset, fEnd.x*fSize+offset, fEnd.y*fSize+offset);
			fBegin = fEnd;
		}
		
		//draw end dot
		if(fBegin != null) 
			g.fillOval(fBegin.x*fSize,fBegin.y*fSize, fSize, fSize);
		*/
		return im;
	}
	
	
	public static void main(String[] args) {
		int n = 8;
		
		ChessBacktrack chess = new ChessBacktrack(n);
		long time = System.nanoTime();
		chess.findSolution(2, 2);
		System.out.println("Time: "+(System.nanoTime()-time)+"ns");
		
		Image im = chess.DrawSolution();
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(new JLabel(new ImageIcon(im)),BorderLayout.CENTER);
		frame.setVisible(true);
		
	}


}
