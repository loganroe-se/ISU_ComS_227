package hw4;

import java.util.Random;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

/**
 * Pinky, one of the four ghosts that extends the Ghosts abstract class.
 * @author Logan Roe
 */
public class Pinky extends Ghosts
{
	/**
	 * Constructor for Pinky that does a super() call to the Ghosts abstract class.
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
	public Pinky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	// Overrides the chaseMode in Ghosts and returns a target in the form of a Location.
	/**
	 * Overrides the chaseMode in Ghosts and returns a target in the form of a Location.
	 * Based off of Pac-Man's direction he is facing, the target will be set.
	 * The target is always four tiles in front of where Pac-Man is facing.
	 * @param desc
	 * 	Used to determine Pac-Man's location and direction.
	 */
	@Override
	protected Location chaseMode(Descriptor desc)
	{
		Location playerLoc = desc.getPlayerLocation();
		Direction playerDir = desc.getPlayerDirection();
		
		int targetRow = 0;
		int targetCol = 0;
		
		switch(playerDir)
		{
			case UP:
				targetRow = (playerLoc.row() - 4);
				targetCol = playerLoc.col();
				break;
			case LEFT:
				targetRow = playerLoc.row();
				targetCol = (playerLoc.col() - 4);
				break;
			case DOWN:
				targetRow = (playerLoc.row() + 4);
				targetCol = playerLoc.col();
				break;
			case RIGHT:
				targetRow = playerLoc.row();
				targetCol = (playerLoc.col() + 4);
				break;
		}
		
		return new Location(targetRow, targetCol);
	}
}
