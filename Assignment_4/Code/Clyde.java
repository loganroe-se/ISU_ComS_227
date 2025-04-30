package hw4;

import java.util.Random;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

/**
 * Clyde, one of the four ghosts that extends the Ghosts abstract class.
 * @author Logan Roe
 */
public class Clyde extends Ghosts
{
	/**
	 * Constructor for Clyde that does a super() call to the Ghosts abstract class.
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
	public Clyde(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	/**
	 * Overrides the chaseMode method in Ghosts.
	 * Determines the distance from Pac-Man and then, if greater than eight, it will set the target to Pac-Man's location.
	 * However, if it is less than or equal to eight, the target will be Clyde's scatter target.
	 * @param desc
	 * 	Used to determine Pac-Man's location.
	 */
	@Override
	protected Location chaseMode(Descriptor desc)
	{
		Location playerLoc = desc.getPlayerLocation();
		Location clydeLoc = getCurrentLocation();
		
		int targetRow = 0;
		int targetCol = 0;
		
		double distFromPac = Math.sqrt(Math.pow(clydeLoc.row() - playerLoc.row(), 2) + Math.pow(clydeLoc.col() - playerLoc.col(), 2));
		
		if (distFromPac > 8)
		{
			targetRow = playerLoc.row();
			targetCol = playerLoc.col();
		}
		else
		{
			targetRow = getScatterTarget().row();
			targetCol = getScatterTarget().col();
		}

		return new Location(targetRow, targetCol);
	}
}
