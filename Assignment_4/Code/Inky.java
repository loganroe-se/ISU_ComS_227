package hw4;

import java.util.Random;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

/**
 * Inky, one of the four ghosts that extends the Ghosts abstract class.
 * @author Logan Roe
 */
public class Inky extends Ghosts
{
	/**
	 * Constructor for Inky that does a super() call to the Ghosts abstract class.
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
	public Inky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	/**
	 * Overrides the chaseMode in Ghosts and sets a target according to Inky's specific requirements.
	 * This will first find the row and column in front of Pac-Man by two tiles.
	 * Then, using these newly-found values, Blinky's current row and column will be subtracted from the already found row and column.
	 * Next, the target row and column will be doubled and then added to Blinky's current row and column.
	 * This is how the target location is found for Inky.
	 * @param desc
	 * 	Used to determine Pac-Man's and Blinky's location.
	 */
	@Override
	protected Location chaseMode(Descriptor desc)
	{
		Location playerLoc = desc.getPlayerLocation();
		Direction playerDir = desc.getPlayerDirection();
		Location blinkyLoc = desc.getBlinkyLocation();
		
		int targetRow = 0;
		int targetCol = 0;
		int inFrontRow = 0;
		int inFrontCol = 0;
		
		switch(playerDir)
		{
			case UP:
				inFrontRow = (playerLoc.row() - 2);
				inFrontCol = playerLoc.col();
				break;
			case LEFT:
				inFrontRow = playerLoc.row();
				inFrontCol = (playerLoc.col() - 2);
				break;
			case DOWN:
				inFrontRow = (playerLoc.row() + 2);
				inFrontCol = playerLoc.col();
				break;
			case RIGHT:
				inFrontRow = playerLoc.row();
				inFrontCol = (playerLoc.col() + 2);
				break;
		}
		
		targetRow = inFrontRow - blinkyLoc.row();
		targetCol = inFrontCol - blinkyLoc.col();
		
		targetRow *= 2;
		targetCol *= 2;
		
		targetRow += blinkyLoc.row();
		targetCol += blinkyLoc.col();
		
		return new Location(targetRow, targetCol);
	}
}
