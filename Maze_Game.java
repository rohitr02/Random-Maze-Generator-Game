import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Maze_Game extends JPanel implements KeyListener{

	//making the cell class
	private class Cell {
		//creating the cell properties
		private int x, y, w, h;
		private boolean[] walls = new boolean[4];

		//variables to check special cell properties
		private boolean hasBeenVisited;
		private boolean isCurrentCell;
		private boolean isPlayerCell;


		//Constructor which takes cell properties as parameters
		public Cell(int x, int y, int h, int w) {
			//each cell starts with all 4 walls -- North (0), East (1), South (2), West (3)
			walls = new boolean[] {true, true, true, true};

			//assigns cell properties
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;

			//each cell starts of without any special cell properties
			hasBeenVisited = false;
			isCurrentCell = false;
			isPlayerCell = false;
		}


		//setters and getters to access the private variables of this class
		public boolean getWall(int wall) {
			return walls[wall];
		}

		public void setWall(int wallPos, boolean bool) {
			walls[wallPos] = bool;
		}

		public boolean getHasBeenVisited() {
			return hasBeenVisited;
		}

		public void setHasBeenVisited(boolean hasBeenVisited) {
			this.hasBeenVisited = hasBeenVisited;
		}

		public boolean isCurrentCell() {
			return isCurrentCell;
		}

		public void setCurrentCell(boolean isCurrentCell) {
			this.isCurrentCell = isCurrentCell;
		}

		public boolean isPlayerCell() {
			return isPlayerCell;
		}

		public void setPlayerCell(boolean isPlayerCell) {
			this.isPlayerCell = isPlayerCell;
		}


		//Methods to draw cell walls
		public void drawNorthWall(Graphics g) {
			g.setColor(Color.WHITE);
			g.drawLine(x, y, x + w, y);
		}

		public void drawEastWall(Graphics g) {
			g.setColor(Color.WHITE);
			g.drawLine(x + w, y, x + w, y + h);
		}

		public void drawSouthWall(Graphics g) {
			g.setColor(Color.WHITE);
			g.drawLine(x + w, y + h, x, y + h);
		}

		public void drawWestWall(Graphics g) {
			g.setColor(Color.WHITE);
			g.drawLine(x, y + h, x, y);
		}


		//Method to draw special cells
		//type 0 is current cell; type 1 is player cell; type 2 is end cell
		public void drawSpecialCell(Graphics g, int type) {
			if (type == 0)
				g.setColor(Color.cyan);
			else if (type == 1)
				g.setColor(Color.red);
			else if (type == 2)
				g.setColor(Color.green);
			else
				g.setColor(Color.black);

			g.fillRect(x+1, y+1, w-1, h-1);
		}

	}

	//creating the maze properties
	private int x, y, h, w;
	private int dimensions;

	//creating the maze
	private Cell[][] maze;

	//creating the necessary data structures to make the maze
	private Stack<Cell> visited = new Stack<Cell>(); //holds all the visited cells
	private Stack<Integer> direction = new Stack<Integer>(); //holds the direction that was moved

	//making the jframe to display the maze and create UI
	private static JFrame frame = new JFrame();

	//default constructor
	public Maze_Game() {
		this(5, 500, 520);
	}

	//overloaded constructor
	public Maze_Game(int dimension, int height, int width) {
		this.dimensions = dimension;
		h = height / dimensions;
		w = width / dimensions;

		//maze is organized (y,x) NOT (x,y)
		maze = new Cell[dimensions][dimensions];

		drawGrid();
		getNextCell();
		setPlayerCell(maze[0][0], true);

		addKeyListener(this);
	}

	//Draws the initial grid with n*n Cell objects
	private void drawGrid() {
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				maze[i][j] = new Cell(x ,y ,h ,w); //makes a new Cell at the specific x,y and dimensions h,w
				x += w; //makes the next cell x position "w" far away
			}
			x = 0; //resets the x position back to the left
			y += h; //makes the next cell "h" distance downward -- essentially creates a new row
		}
		//resets the x and y variables back to the top.
		x = 0;
		y = 0;

		//sets the current cell (0,0)
		setCurrentCell(maze[y][x], true);
		visited.push(maze[0][0]);
		maze[0][0].setHasBeenVisited(true);

	}

	//sets the current cell to true/false
	private void setCurrentCell(Cell current, boolean bool) {
		current.setCurrentCell(bool);
	}

	//sets the player cell to true/false
	private void setPlayerCell(Cell player, boolean bool) {
		player.setPlayerCell(bool);
	}

	//gets the current cell x index
	private int getCurrentCellX() {
		int currentCellX = 0;
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].isCurrentCell())
					currentCellX = j;
			}
		}
		return currentCellX;
	}

	//gets the current cell y index
	private int getCurrentCellY() {
		int currentCellY = 0;
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].isCurrentCell())
					currentCellY = i;
			}
		}
		return currentCellY;
	}

	//gets the player cell x index
	private int getPlayerCellX() {
		int playerCellX = 0;
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].isPlayerCell())
					playerCellX = j;
			}
		}
		return playerCellX;
	}

	//gets the player cell y index
	private int getPlayerCellY() {
		int playerCellY = 0;
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].isPlayerCell())
					playerCellY = i;
			}
		}
		return playerCellY;
	}

	//checks if all the cells have been visited
	private boolean haveAllCellsBeenVisited() {
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].getHasBeenVisited() == false)
					return false;
			}
		}
		return true;
	}

	//removes walls -  old cell wall number, new cell wall number, direction moving
	private void removeWalls(int oldWallIndex, int newWallIndex, int directionToRemove) {
		//get old x,y vals and make new ones
		int oldCellX = getCurrentCellX();
		int oldCellY = getCurrentCellY();
		int newCellX = 0, newCellY = 0;
		
		//set new cell x and y
		if(directionToRemove == 0) { //right
			newCellX = getCurrentCellX() + 1;
			newCellY = getCurrentCellY();			
		}
		if(directionToRemove == 1) { //down
			newCellX = getCurrentCellX();
			newCellY = getCurrentCellY() + 1;
		}
		if(directionToRemove == 2) { //left
			newCellX = getCurrentCellX() - 1;
			newCellY = getCurrentCellY();
		}
		if(directionToRemove == 3) { //up
			newCellX = getCurrentCellX();
			newCellY = getCurrentCellY() - 1;
		}
		
		//remove the old/new walls
		maze[oldCellY][oldCellX].setWall(oldWallIndex, false);
		maze[newCellY][newCellX].setWall(newWallIndex, false);
	}

	//moves in the direction inputted. Error if it is invalid index. Calls on remove wall
	// 0 is right, 1 is down, 2 is left, 3 is up
	private void moveInDirection(int direction) { 
		//right
		if(direction == 0) {
			int newCellX = getCurrentCellX() + 1;
			int newCellY = getCurrentCellY();
			removeWalls(1,3,0);
			setCurrentCell(maze[getCurrentCellY()][getCurrentCellX()], false);
			setCurrentCell(maze[newCellY][newCellX], true); 
			maze[newCellY][newCellX].setHasBeenVisited(true); 
		}

		//down
		if(direction == 1) {
			int newCellX = getCurrentCellX();
			int newCellY = getCurrentCellY() + 1;
			removeWalls(2,0,1);
			setCurrentCell(maze[getCurrentCellY()][getCurrentCellX()], false);
			setCurrentCell(maze[newCellY][newCellX], true);
			maze[newCellY][newCellX].setHasBeenVisited(true);
		}

		//left
		if(direction == 2) {
			int newCellX = getCurrentCellX() - 1;
			int newCellY = getCurrentCellY();
			removeWalls(3,1,2);
			setCurrentCell(maze[getCurrentCellY()][getCurrentCellX()], false);
			setCurrentCell(maze[newCellY][newCellX], true);
			maze[newCellY][newCellX].setHasBeenVisited(true); 
		}

		//up
		if(direction == 3) {
			int newCellX = getCurrentCellX();
			int newCellY = getCurrentCellY() - 1;
			removeWalls(0,2,3);
			setCurrentCell(maze[getCurrentCellY()][getCurrentCellX()], false);
			setCurrentCell(maze[newCellY][newCellX], true);
			maze[newCellY][newCellX].setHasBeenVisited(true); 
		}
		repaint();
	}

	//checks if top neighbor is visited
	private boolean isTopNeighborVisited() {
		if(getCurrentCellY() - 1 >= 0) {
			if(maze[getCurrentCellY() - 1][getCurrentCellX()].getHasBeenVisited() == false) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	//checks if bottom neighbor is visited
	private boolean isBottomNeighborVisited() {
		if(getCurrentCellY() + 1 < dimensions) {
			if(maze[getCurrentCellY() + 1][getCurrentCellX()].getHasBeenVisited() == false) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	//checks if right neighbor is visited
	private boolean isRightNeighborVisited() {
		if(getCurrentCellX() + 1 < dimensions) {
			if(maze[getCurrentCellY()][getCurrentCellX() + 1].getHasBeenVisited() == false) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	//checks if left neighbor is visited
	private boolean isLeftNeighborVisited() {
		if(getCurrentCellX() - 1 >= 0) {
			if(maze[getCurrentCellY()][getCurrentCellX() - 1].getHasBeenVisited() == false) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	//return: 0 = right, 1 = bottom, 2 = left, 3 = top
	private int randomlyChooseNeighbor() { 
		int randChoose = (int) (Math.random()*4);

		boolean[] choices = new boolean[4];
		choices[0] = isRightNeighborVisited();
		choices[1] = isBottomNeighborVisited();
		choices[2] = isLeftNeighborVisited();
		choices[3] = isTopNeighborVisited();

		while(choices[randChoose] == true) {
			randChoose = (int) (Math.random()*4);
		}
		direction.push(randChoose);
		return randChoose;
	}

	//gets the next cell to remove
	private void getNextCell() {
		//check if all cells have been visited
		if(haveAllCellsBeenVisited() == false) { //they haven't all been visited
			//check if one of the neighbors has not been visited
			if(isTopNeighborVisited() == false || isBottomNeighborVisited() == false || 
					isRightNeighborVisited() == false || isLeftNeighborVisited() == false) { //one of the visitors has not been visited
				//push the current cell to the visited stack
				visited.push(maze[getCurrentCellY()][getCurrentCellX()]);
				moveInDirection(randomlyChooseNeighbor()); //move to a randomly chosen neighbor
			}else if(visited.isEmpty() == false) { //they have all been visited
				visited.pop().setHasBeenVisited(false); //get the last visited cell and make it unvisited
				//get the opposite of the direction chosen last time in order to travel back
				int oldDirection = direction.pop();
				int oppDirection = 0;
				if(oldDirection == 0) {
					oppDirection = 2;
				}else if(oldDirection==1) {
					oppDirection = 3;
				}else if(oldDirection==2) {
					oppDirection = 0;
				}else if(oldDirection == 3) {
					oppDirection = 1;
				}
				moveInDirection(oppDirection);
			}
		}
	}

	//paints the screen
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//paints background
		g.setColor(Color.black);
		g.fillRect(x, y, getWidth(), getHeight());

		//paints the walls;
		//Loop through every single Cell in the maze
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].getWall(0))
					maze[i][j].drawNorthWall(g);
				if(maze[i][j].getWall(1))
					maze[i][j].drawEastWall(g);
				if(maze[i][j].getWall(2))
					maze[i][j].drawSouthWall(g);
				if(maze[i][j].getWall(3))
					maze[i][j].drawWestWall(g);
			}
		}

		//checks if all cells have been visited. 
		//Positioned before the current cell painting so that it does not paint the last current cell
		if(haveAllCellsBeenVisited() == true ) {
			setCurrentCell(maze[getCurrentCellY()][getCurrentCellX()], false);
			//draws the end cell
			maze[dimensions-1][dimensions-1].drawSpecialCell(g, 2);
		}

		//draw player
		maze[getPlayerCellY()][getPlayerCellX()].drawSpecialCell(g, 1);

		//paints the current cell
		for(int i = 0; i < dimensions; i++) {
			for(int j = 0; j < dimensions; j++) {
				if(maze[i][j].isCurrentCell() == true) {
					maze[i][j].drawSpecialCell(g, 0);
				}
			}
		}

		//slows it down to see it paint
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.getLocalizedMessage();
				}

		//get the next cell
		getNextCell();
	}

	//creates maze and add it to the jframe
	static void createMaze(int n, int height, int wid) {
		Maze_Game maze = new Maze_Game(n, height, wid);
		frame.add(maze);
		frame.setTitle("Random Maze Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(wid+1, height+23);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addKeyListener(maze);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	//moves the player from the old index to the new specified index
	private void movePlayerTo(int newPlayerYIndex, int newPlayerXIndex) {
		setPlayerCell(maze[getPlayerCellY()][getPlayerCellX()], false);
		setPlayerCell(maze[newPlayerYIndex][newPlayerXIndex], true);
	}

	@Override
	public void keyPressed(KeyEvent e) {  
		//up arrow or w is up, right arrow or d is right, down arrow or s is down, left arrow or a is left

		//gets the coordinates and saves them once in variables
		int playerY = getPlayerCellY(), playerX = getPlayerCellX();

		//gets the key clicked and saves it once in this variable
		int keyClicked = e.getKeyCode();

		//if maze generation is over
		if(haveAllCellsBeenVisited() == true) {
			//if right or d key is pressed
			if(keyClicked == KeyEvent.VK_RIGHT || keyClicked == KeyEvent.VK_D) {
				//if it is possible to move right
				if(playerX+1 < dimensions) {
					//if there is no wall on the right of the player cell
					if (maze[playerY][playerX+1].getWall(3) == false) {
						//move right
						playerX+=1;
					}
				}
			}else if(keyClicked == KeyEvent.VK_DOWN || keyClicked == KeyEvent.VK_S) { //if down key is pressed 
				//if it is possible to move down
				if(playerY+1 < dimensions) {
					//if there is no wall on the bottom of the player cell
					if(maze[playerY+1][playerX].getWall(0) == false) {
						//move down
						playerY+=1;
					}
				}
			}else if(keyClicked == KeyEvent.VK_LEFT || keyClicked == KeyEvent.VK_A) {//if left key is pressed
				//if its not already at the left-most side
				if(playerX-1 >= 0) {
					//if there is no wall at the left 
					if(maze[playerY][playerX-1].getWall(1) == false) {
						//move left
						playerX-=1;
					}
				}
			}else if(keyClicked == KeyEvent.VK_UP || keyClicked == KeyEvent.VK_W) {//if up key is pressed
				//if its not already at the top
				if(playerY-1 >= 0) {
					// if there is no wall on above
					if(maze[playerY-1][playerX].getWall(2) == false) {
						//move up
						playerY-=1;
					}
				}
			}
			movePlayerTo(playerY, playerX);
			repaint();
		}

		//check if the player cell got to the end cell
		if((getPlayerCellY() == dimensions - 1) && (getPlayerCellX() == dimensions - 1)) {
			frame.dispose();
			frame = new JFrame();
			createMaze(dimensions+1, h*dimensions, w*dimensions);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		return;
	}

	public static void main(String[] args) {
		//starting dimensions
		int n = 5, wid = 800, height = 800;
		createMaze(n, height, wid);
	}
}
