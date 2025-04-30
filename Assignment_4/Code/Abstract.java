package hw4;

import api.Actor;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.Mode;

/**
 * Abstract class that implements Actor and acts a parent class for both Ghosts and Pacman.
 * @author Logan Roe
 */
public abstract class Abstract implements Actor
{
	/**
	 * Margin of error for comparing exact position to centerline
	 * of cell.
	 */
	public static final double ERR = .001;
	  
	/**
	 * Initial location on reset().
	 */
	private Location home;
	  
	/**
	 * Initial direction on reset().
	 */
	private Direction homeDirection;
	  
	/**
	 * Current direction of travel.
	 */
	private Direction currentDirection;
	  
	/**
	 * Basic speed increment, used to determine currentIncrement.
	 */
	private double baseIncrement;
	  
	/**
	 * Current speed increment, added in direction of travel each frame.
	 */
	private double currentIncrement;
	  
	/**
	 * Row (y) coordinate, in units of cells.  The row number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double rowExact;
	  
	/**
	 * Column (x) coordinate, in units of cells.  The column number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double colExact;
	
	/**
	 * This constructor initiates the necessary instance variables for this abstract class.
	 * Note: For all methods, they are not meant to be overrode by any subclasses unless I say otherwise in the javadoc.
	 * @param home
	 * 	initial location
	 * @param baseSpeed
	 * 	initial speed
	 * @param homeDirection
	 * 	initial direction
	 */
	public Abstract(Location home, double baseSpeed, Direction homeDirection)
	{
		this.home = home;
		this.baseIncrement = baseSpeed;
		this.currentIncrement = baseSpeed;
		this.homeDirection = homeDirection;
	}
	
	/**
	 * Returns the base increment.
	 */
	public double getBaseIncrement() 
	{
		return baseIncrement;
	}

	/**
	 * Returns the exact column value.
	 */
	public double getColExact() 
	{
		return colExact;
	}

	/**
	 * Returns the current increment.
	 */
	public double getCurrentIncrement() 
	{
		return currentIncrement;
	}

	/**
	 * Returns the current location.
	 */
	public Location getCurrentLocation() 
	{
		return new Location((int) rowExact, (int) colExact);
	}

	/**
	 * Returns the current direction.
	 */
	public Direction getCurrentDirection() 
	{
		return currentDirection;
	}

	/**
	 * Returns the home direction.
	 */
	public Direction getHomeDirection() 
	{
		return homeDirection;
	}

	/**
	 * Returns the home location.
	 */
	public Location getHomeLocation() 
	{
		return home;
	}
	
	/**
	 * Does nothing in this case but it is meant to be overrode by the Ghosts subclass.
	 * Takes care of the Pac-Mans getMode.
	 */
	public Mode getMode()
	{
		// does nothing
		return null;
	}
	
	/**
	 * Returns the exact row value.
	 */
	public double getRowExact() 
	{
		return rowExact;
	}
	
	/**
	 * Resets if any ghost(s) call this function but is overrode by Pac-Man.
	 */
	public void reset() 
	{
		Location homeLoc = getHomeLocation();
	    setRowExact(homeLoc.row() + 0.5);
	    setColExact(homeLoc.col() + 0.5);
	    setDirection(getHomeDirection());
	    currentIncrement = getBaseIncrement();
	}
	
	/**
	 * Sets the colExact variable to a specified value, c.
	 * @param c
	 * 	value to set colExact to
	 */
	public void setColExact(double c)
	{
	    colExact = c;
	}

	/**
	 * Sets the current direction.
	 * @param dir
	 * 	sets current direction to dir
	 */
	public void setDirection(Direction dir)
	{
		currentDirection = dir;
	}
	
	/**
	 * Does nothing in this case but is meant to be overrode by the Ghosts subclass.
	 * Takes care of Pac-Man's portion of setMode.
	 * @param mode
	 * 	To pass into other subclasses that call this.
	 * @param desc
	 * 	To pass into other subclasses that call this.
	 */
	public void setMode(Mode mode, Descriptor desc) 
	{
		// does nothing
	}
	
	/**
	 * Sets the rowExact variable to r.
	 * @param r
	 * 	value that rowExact will be set to
	 */
	public void setRowExact(double r)
	{
	  rowExact = r;
	}
	
	/**
	 * Does nothing in this case but is overrode by both Pacman and Ghosts.
	 * @param desc
	 * 	To pass into other subclasses that call this.
	 */
	public void update(Descriptor desc) 
	{
		// does nothing
	}
	
	/**
	 * Sets the current increment to a specified value, inc.
	 * @param inc
	 * 	what the current increment will be set to
	 */
	protected void setCurrentIncrement(double inc)
	{
		currentIncrement = inc;
	}
	
	/**
	 * Determines the difference between current position and center of 
	 * current cell, in the direction of travel.
	 */
	protected double distanceToCenter()
	{
	  double colPos = getColExact();
	  double rowPos = getRowExact();
	  switch (getCurrentDirection())
	  {
	    case LEFT:
	      return colPos - ((int) colPos) - 0.5;
	    case RIGHT:
	      return 0.5 - (colPos - ((int) colPos));
	    case UP:
	      return rowPos - ((int) rowPos) - 0.5;
	    case DOWN:
	      return 0.5 - (rowPos - ((int) rowPos));
	  }    
	  return 0;
	}
}