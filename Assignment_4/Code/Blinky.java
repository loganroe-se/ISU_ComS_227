package hw4;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

/**
 * Blinky, one of the four ghosts that extends the Ghosts abstract class.
 * @author Logan Roe
 */
public class Blinky extends Ghosts
{
	/**
	 * Constructor for Blinky that does a super() call to the Ghosts abstract class.
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
	public Blinky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}

	/**
	 * Overrides the chaseMode in Ghosts and sets the target equal to Pac-Man's location.
	 * @param desc
	 * 	Used to obtain Pac-Man's location.
	 */
	@Override
	protected Location chaseMode(Descriptor desc)
	{
		Location playerLoc = desc.getPlayerLocation();
		Location target = new Location(playerLoc.row(), playerLoc.col());
		
		return target;
	}	
}
