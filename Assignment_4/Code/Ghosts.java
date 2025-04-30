package hw4;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;

import static api.Mode.*;
import static api.Direction.*;

/**
 * Is an abstract class that oversees all four of the Ghosts and also extends an overarching abstract class, Abstract.
 * @author Logan Roe
 */
public abstract class Ghosts extends Abstract
{
	/**
	 * Maze configuration.
	 */
	private MazeMap maze;
	
	/**
	 * The target that the Ghost(s) go to in Scatter mode.
	 */
	private Location scatterTarget;
	
	/**
	 * The randomly generated value to determine where a ghost goes in Frightened mode.
	 */
	private Random rand;
	
	/**
	 * The next location the Ghost(s) will go to.
	 */
	private Location nextLoc;
	
	/**
	 * The next direction the Ghost(s) will go to.
	 */
	private Direction nextDir;
	
	/**
	 * This is used to determine when to perfectly center a value if the direction is changing.
	 */
	private int numIterations;
	
	/**
	 * The mode the Ghost(s) is/are currently in.
	 */
	private Mode currentMode;
	  
	
	/**
	 * Constructor that will initiate any instance variables needed for this class whilst also calling super() to Abstract.
	 * @param maze
	 * 	Maze configuration
	 * @param home
	 * 	Initial location
	 * @param baseSpeed
	 * 	Initial speed
	 * @param homeDirection
	 * 	Initial direction
	 * @param scatterTarget
	 * 	The target the ghost goes for when in scatter mode
	 * @param rand
	 * 	The pseudorandomly generated value
	 */
	public Ghosts(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(home, baseSpeed, homeDirection);
		this.maze = maze;
		this.scatterTarget = scatterTarget;
		this.rand = rand;
		this.currentMode = INACTIVE;
	}

	/**
	 * Returns the current mode a ghost is in.
	 */
	@Override
	public Mode getMode() {
		return currentMode;
	}
	
	/**
	 * Sets the mode for a ghost and may change the speed depending on which mode.
	 * Frightened causes two-thirds the speed.
	 * Dead causes twice the speed.
	 * All other modes cause the speed to go back to normal.
	 * @param mode
	 * 	What the mode will be set to.
	 * @param desc
	 * 	Used to pass on to calculateNextCell()
	 */
	@Override
	public void setMode(Mode mode, Descriptor desc) 
	{
		currentMode = mode;
		double currentIncrement = getCurrentIncrement();
		double baseIncrement = getBaseIncrement();
		
		if (currentMode == FRIGHTENED)
		{
			setCurrentIncrement(((currentIncrement * 2) / 3));
		}
		else if (currentMode == DEAD)
		{
			setCurrentIncrement(currentIncrement *= 2);
		}
		else
		{
			setCurrentIncrement(currentIncrement = baseIncrement);
		}
		
		calculateNextCell(desc);		
	}
	
	/**
	 * This updates a ghost's direction, exact row/column values, if needed, and increment all based off of the current direction.
	 * It will also check if where the ghost wants to go to next is a tunnel or not, in which special cases occur.
	 * @param desc
	 * 	Used to pass on to calculateNextCell()
	 */
	public void update(Descriptor desc) 
	{
		if (currentMode == INACTIVE)
		{
			return;
		}
		
		// Obtaining values from the higher up class, Abstract.
		Direction currentDirection = getCurrentDirection();
		double currentIncrement = getCurrentIncrement();
		double rowExact = getRowExact();
		double colExact = getColExact();
		
		// This if/else if statement will determine the direction (so it knows whether to change the row or column), if its too far from the center, and if it is within a margin of error.
		// Once entering, it will change the row or column, based on direction, to whatever nice decimal value it needs to be (increments of 1 starting at 0.5).
		if (numIterations == 1 && ((currentDirection == UP || currentDirection == DOWN) && distanceToCenter() > (0 - ERR)) && distanceToCenter() < currentIncrement)
		{
			numIterations = 0;
			setDirection(nextDir);
			setRowExact(((int) rowExact) + 0.5);
			return;
		}
		else if (numIterations == 1 && ((currentDirection == LEFT || currentDirection == RIGHT) && distanceToCenter() > (0 - ERR)) && distanceToCenter() < currentIncrement)
		{
			numIterations = 0;
			setDirection(nextDir);
			setColExact(((int) colExact) + 0.5);
			return;
		}
		
		// This switch statement takes in the current direction then determines what to do.
		// The case for up and down are very similar as all they have to do is increment and call calculateNextCell()
		// However, left and right are more complicated as they must also account for potential tunnels, hence the if statement checking boundaries.
		switch(getCurrentDirection())
		{
			case UP:
				setRowExact(rowExact -= currentIncrement);
				calculateNextCell(desc);
				break;
			case LEFT:
				if (colExact - currentIncrement - 0.5 < 0)
				{
					setColExact(maze.getNumColumns() + (colExact - currentIncrement - 0.5));
					calculateNextCell(desc);
				}
				else
				{
					setColExact(colExact -= currentIncrement);
					calculateNextCell(desc);
				}
				break;
			case DOWN:
				setRowExact(rowExact += currentIncrement);
				calculateNextCell(desc);
				break;
			case RIGHT:
				if (colExact + currentIncrement + 0.5 >= maze.getNumColumns())
				{
					setColExact(colExact + currentIncrement + 0.5 - maze.getNumColumns());
					calculateNextCell(desc);
				}
				else
				{
					setColExact(colExact += currentIncrement);
					calculateNextCell(desc);
				}
				break;
		}
		
		if (currentDirection != nextDir)
		{
			numIterations = 1;
		}
	}
	
	/**
	 * Obtains the scatter target which will be used in Clyde, one of the ghosts.
	 */
	protected Location getScatterTarget()
	{
		return scatterTarget;
	}
	
	/**
	 * Does nothing in this case but is here so that the subclasses (individual ghosts) can implement their version of chase mode.
	 * @param desc
	 * 	Used to pass to the subclasses that will use this.
	 */
	protected Location chaseMode(Descriptor desc)
	{
		// Do nothing, for each ghost to implement their version of chase mode.
		return null;
	}
	
	/**
	 * This calculates the next cell for a ghost based on a multitude of factors.
	 * Some factors include current direction, current mode, distance from the player, other ghosts, etc.
	 * Priority of directions is given in this order, assuming equal distances: UP, LEFT, DOWN, RIGHT.
	 * @param desc
	 * 	Used to pass to chaseMode()
	 */
	public void calculateNextCell(Descriptor desc)
	{
		Location currentLoc = getCurrentLocation();
		double distUp = 0;
		double distLeft = 0;
		double distDown = 0;
		double distRight = 0;
		double shortestDistance = 999999;
		
		// Obtaining values from the abstract class, Abstract.
		Location home = getHomeLocation();
		Direction currentDirection = getCurrentDirection();
		
		// This massive if statement determines what mode the ghost is in and, depending on the mode, what to do next.
		// Inactive mode returns as there is no next cell. Frightened mode uses the random number brought in with the constructor to determine its next move.
		if (currentMode == INACTIVE)
		{
			return;
		}
		else if (currentMode == FRIGHTENED)
		{			
			double nextRand = rand.nextDouble();
			distUp = 1;
			distLeft = 1;
			distDown = 1;
			distRight = 1;
			
			if (nextRand <= 0.25)
			{
				distUp = 0;
			}
			else if (nextRand <= 0.5)
			{
				distLeft = 0;
			}
			else if (nextRand <= 0.75)
			{
				distDown = 0;
			}
			else
			{
				distRight = 0;
			}
		}
		// The Scatter method use targets that then allow for calculate of distances in each direction from the target.
		else if (currentMode == SCATTER)
		{
			distUp = Math.sqrt(Math.pow((scatterTarget.row() - (currentLoc.row() - 1)), 2) + Math.pow((scatterTarget.col() - currentLoc.col()), 2));
			distLeft = Math.sqrt(Math.pow((scatterTarget.row() - currentLoc.row()), 2) + Math.pow((scatterTarget.col() - (currentLoc.col() - 1)), 2));
			distDown = Math.sqrt(Math.pow((scatterTarget.row() - (currentLoc.row() + 1)), 2) + Math.pow((scatterTarget.col() - currentLoc.col()), 2));
			distRight = Math.sqrt(Math.pow((scatterTarget.row() - currentLoc.row()), 2) + Math.pow((scatterTarget.col() - (currentLoc.col() + 1)), 2));
		}
		// Chase is similar to Scatter and Dead but also calls chaseMode() as each ghost has a different chase mode.
		else if (currentMode == CHASE)
		{
			Location target = chaseMode(desc);
			
			distUp = Math.sqrt(Math.pow((target.row() - (currentLoc.row() - 1)), 2) + Math.pow((target.col() - currentLoc.col()), 2));
			distLeft = Math.sqrt(Math.pow((target.row() - currentLoc.row()), 2) + Math.pow((target.col() - (currentLoc.col() - 1)), 2));
			distDown = Math.sqrt(Math.pow((target.row() - (currentLoc.row() + 1)), 2) + Math.pow((target.col() - currentLoc.col()), 2));
			distRight = Math.sqrt(Math.pow((target.row() - currentLoc.row()), 2) + Math.pow((target.col() - (currentLoc.col() + 1)), 2));
		}
		// The Dead method use targets that then allow for calculate of distances in each direction from the target.
		else if (currentMode == DEAD)
		{
			distUp = Math.sqrt(Math.pow((home.row() - (currentLoc.row() - 1)), 2) + Math.pow((home.col() - currentLoc.col()), 2));
			distLeft = Math.sqrt(Math.pow((home.row() - currentLoc.row()), 2) + Math.pow((home.col() - (currentLoc.col() - 1)), 2));
			distDown = Math.sqrt(Math.pow((home.row() - (currentLoc.row() + 1)), 2) + Math.pow((home.col() - currentLoc.col()), 2));
			distRight = Math.sqrt(Math.pow((home.row() - currentLoc.row()), 2) + Math.pow((home.col() - (currentLoc.col() + 1)), 2));
		}
		
		// Checks if the distance to the center of the current cell is within a certain margin of error or not.
		if (distanceToCenter() > (0 - ERR))
		{
			// Determines if the distance to go up is shorter than the current shortest distance. Also determines if going up would cause the ghost to go into a wall.
			// The direction is also taken into consideration, alongside the mode, as ghosts cannot reverse direction unless in Frightened mode.
			if (distUp < shortestDistance && (currentDirection != DOWN || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row() - 1, currentLoc.col()) == false))
			{
				if (Math.abs(distUp - shortestDistance) > ERR)
				{
					shortestDistance = distUp;
					nextDir = UP;
					nextLoc = new Location(currentLoc.row() - 1, currentLoc.col());
				}
			}
			
			// Slightly more complicated than up and down, left also has to determine whether the ghost is in a tunnel or not and change equations accordingly.
			// Also determines if going left would cause the ghost to go into a wall.
			// The direction is also taken into consideration, alongside the mode, as ghosts cannot reverse direction unless in Frightened mode.
			if (currentLoc.col() - 1 < 0)
			{
				if (distLeft < shortestDistance && (currentDirection != RIGHT || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row(), maze.getNumColumns() - 1) == false))
				{
					if (Math.abs(distLeft - shortestDistance) > ERR)
					{
						shortestDistance = distLeft;
						nextDir = LEFT;
						nextLoc = new Location(currentLoc.row(), maze.getNumColumns() - 1);
					}
				}
			}
			else
			{
				if (distLeft < shortestDistance && (currentDirection != RIGHT || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row(), currentLoc.col() - 1) == false))
				{
					if (Math.abs(distLeft - shortestDistance) > ERR)
					{
						shortestDistance = distLeft;
						nextDir = LEFT;
						nextLoc = new Location(currentLoc.row(), currentLoc.col() - 1);
					}
				}
			}
			
			// Determines if the distance to go down is shorter than the current shortest distance. Also determines if going down would cause the ghost to go into a wall.
			// The direction is also taken into consideration, alongside the mode, as ghosts cannot reverse direction unless in Frightened mode.
			if (distDown < shortestDistance && (currentDirection != UP || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row() + 1, currentLoc.col()) == false))
			{
				if (Math.abs(distDown - shortestDistance) > ERR)
				{
					shortestDistance = distDown;
					nextDir = DOWN;
					nextLoc = new Location(currentLoc.row() + 1, currentLoc.col());
				}
			}
			
			// Slightly more complicated than up and down, left also has to determine whether the ghost is in a tunnel or not and change equations accordingly.
			// Also determines if going right would cause the ghost to go into a wall.
			// The direction is also taken into consideration, alongside the mode, as ghosts cannot reverse direction unless in Frightened mode.
			if (currentLoc.col() + 1 >= maze.getNumColumns())
			{
				if (distRight < shortestDistance && (currentDirection != LEFT || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row(), 0) == false))
				{
					if (Math.abs(distRight - shortestDistance) > ERR)
					{
						shortestDistance = distRight;
						nextDir = RIGHT;
						nextLoc = new Location(currentLoc.row(), 0);
					}
				}
			}
			else
			{
				if (distRight < shortestDistance && (currentDirection != LEFT || currentMode == FRIGHTENED) && (maze.isWall(currentLoc.row(), currentLoc.col() + 1) == false))
				{
					if (Math.abs(distRight - shortestDistance) > ERR)
					{
						shortestDistance = distRight;
						nextDir = RIGHT;
						nextLoc = new Location(currentLoc.row(), currentLoc.col() + 1);
					}
				}
			}
		}
		
		if (currentDirection != nextDir)
		{
			numIterations = 1;
		}
	}
	
	public Location getNextCell()
	{
		return nextLoc;
	}
}
