package hw3;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Utility class with methods to help initializing a Lines game from 
 * a string descriptor, and for creating a collection of games from
 * a file containing descriptors.
 * @author Logan Roe
 */

public class Util
{

  
  /**
   * Given a 2d array of GridCell, constructs an array of Line
   * objects based on the information in the grid.  Specifically,
   * for each pair of endpoints with matching ids, a corresponding
   * Line object is constructed with that id and with the given endpoints.
   * The order of the endpoints (endpoint 0 vs endpoint 1) is unspecified.
   * If there are more than two endpoints with the same id, or if there
   * is only one endpoint with the given id, this
   * method returns null. No other error-checking is performed (e.g. there
   * may be middle cells with no matching endpoint, or the game
   * may be unsolvable for other reasons).
   * <p>
   * Note that in general the id for a Line will <em>not</em> be the
   * same as its index in the returned array.
   * @param grid
   *   a 2d array of GridCell
   * @return
   *   array of Line objects based on the grid information
   */
  public static ArrayList<Line> createLinesFromGrid(GridCell[][] grid)
  {
	int numEndpoints = 0;
	int repeatId = 0;
	
	int i1 = -1;
	int j1 = -1;
	int i2 = -1;
	int j2 = -1;
	
	ArrayList<Line> lines = new ArrayList<>();
	
	// There are ten different color id's so it makes sure it is within that range.
	while (repeatId <= 10)
	{
		for (int i = 0; i < grid.length; i++)
		{
		    for (int j = 0; j < grid[i].length; j++)
		    {
		    	// Checks if the cell is an end point and if it is in the same line id as the current id being checked (repeatId).
		    	if (grid[i][j].isEndpoint() && grid[i][j].getId() == repeatId)
		    	{
		    		numEndpoints++;
		    		
		    		// Stores the indices of the end points for the line.
		    		if (i1 == -1 && j1 == -1)
		    		{
		    			i1 = i;
		    			j1 = j;
		    		}
		    		else if (i2 == -1 && j2 == -1)
		    		{
		    			i2 = i;
		    			j2 = j;
		    		}
		    	}
		    	
		    	if (i == grid.length - 1 && j == grid.length - 1)
		    	{
		    		// Checks if there are too little or too many end points, in which case, null is returned.
		    		if (numEndpoints == 1 || numEndpoints > 2)
			    	{
			    		return null;
			    	}
		    		else if (numEndpoints == 2)
		    		{
		    			Location loc = new Location(i1, j1);
		    			Location loc2 = new Location(i2, j2);
		    			lines.add(new Line(repeatId, loc, loc2));
		    		}
		    	}
		    	
		    }
		}
		repeatId++;
		numEndpoints = 0;
		i1 = -1;
		j1 = -1;
		i2 = -1;
		j2 = -1;
	} 
    return lines;
  }
  
  /**
   * Reads the given file and constructs a list of LinesGame objects, one for
   * each descriptor in the file.  Descriptors in the file are separated by one or more
   * blank lines, where a "blank line" consists of some amount of whitespace and a 
   * newline character. The file may have extra whitespace at the beginning, 
   * and it must always end with one or more blank lines. Invalid descriptors
   * are ignored, so the method may return an empty list.  (A descriptor is "invalid"
   * if either createGridFromStringArray returns null, or createLinesFromGrid
   * returns null.)
   * @param filename
   *   name of the file to read
   * @return
   *   list of LinesGame objects created from the valid descriptors in the file
   * @throws FileNotFoundException
   *   if a file with the given name can't be opened
   */ 
  public static ArrayList<LinesGame> readFile(String filename) throws FileNotFoundException
  {
    ArrayList<LinesGame> linesGames = new ArrayList<>();
    
	File lgObjects = new File(filename);
    Scanner myObjects = new Scanner(lgObjects);
    
    while (myObjects.hasNextLine())
    {
    	ArrayList<String> stringGames = new ArrayList<String>();
    	
    	// Checks if the scanner has a next line and can be called back to in order to break this loop.
    	while_loop: while (myObjects.hasNextLine()) 
    	{
    		// Holds the next line in the scanner so that it does not have to be called repeatedly.
    		String tempS = myObjects.nextLine();
    		while (myObjects.hasNextLine() && tempS.length() > 0)
    		{
    			// Will continue the nearest while loop if the length of the line is 0.
    			if (tempS.length() == 0)
    			{
    				continue;
    			}
    			else if (tempS.length() > 0)
    			{
    				stringGames.add(tempS);
    			}
    			tempS = myObjects.nextLine();
    			// Breaks the second outer while loop, denoted by while_loop, if the length of the line is 0 after going to the next line.
    			if (tempS.length() == 0)
    			{
    				break while_loop;
    			}
    		}
    	}
    	// Creates a String array of the same size as the ArrayList.
    	String[] stringGames2 = new String[stringGames.size()];
    	int idx = 0;
    	// Use of a for loop to write the values of the ArrayList into the String array to be used later.
    	for (Object value : stringGames)
    	{
    		stringGames2[idx] = (String) value;
    		idx++;
    	}
      
    	// This if statement checks for the conditions of createGridFromStringArray or createLinesFromGrid being null, in which case null is returned.
    	if (StringUtil.createGridFromStringArray(stringGames2) == null)
    	{
    		myObjects.close();
    		return linesGames;
    	}
    	else if (createLinesFromGrid(StringUtil.createGridFromStringArray(stringGames2)) == null)
    	{
    		myObjects.close();
    		return linesGames;
    	}
    	
    	linesGames.add(new LinesGame(stringGames2));
    }
    
    myObjects.close();
    
    return linesGames;
  }
  

  
  /**
   * Determines whether a line between two diagonally adjacent locations
   * would cross any existing line in the given list.
   * The check is based on the following test:
   * <ul>
   *  <li>Let (rOld, cOld) denote the current cell location and let (rNew, cNew) denote
   * the new cell location.  
   *  <li>Let rDiff = rNew - rOld and cDiff = cNew - cOld.
   *  <li>If either rDiff or cDiff does not have absolute value 1, then
   *  the two positions are not diagonally adjacent and the method returns false
   *  <li>If the two positions are diagonally adjacent, then p0 = (rOld, cOld + cDiff) 
   *  and p1 = (rOld + rDiff, cOld) always form the opposite diagonal (i.e., the 
   *  line that could potentially be crossed).
   *  <li>The method returns true if p0 and p1 occur consecutively, in either order,
   *  in any existing line in the given array.
   * </ul>
   * 
   * @param lines
   *   list of Line objects
   * @param currentLoc
   *   any Location
   * @param newLoc
   *   any Location
   * @return
   *   true if the two locations are diagonally adjacent and some
   *   existing line crosses the opposite diagonal
   */
  public static boolean checkForPotentialCrossing(ArrayList<Line> lines, Location currentLoc, Location newLoc)
  {
    int rOld = currentLoc.row();
    int cOld = currentLoc.col();
    int rNew = newLoc.row();
    int cNew = newLoc.col();
    
    int rDiff = rNew - rOld;
    int cDiff = cNew - cOld;
    
    if (Math.abs(rDiff) != 1 || Math.abs(cDiff) != 1)
    {
    	return false;
    }
    else if (Math.abs(rDiff) == 1 && Math.abs(cDiff) == 1)
    {
    	Location p0 = new Location(rOld, cOld + cDiff);
    	Location p1 = new Location(rOld + rDiff, cOld);
    	
    	// Calls checkForLineSegment to simplify this method.
    	if (checkForLineSegment(lines, p0, p1))
    	{
    		return true;
    	}
    }
    
    return false;
  }
  
  /**
   * Determines whether any line in the given array already contains the segment between 
   * the given locations; that is, whether the two given locations occur consecutively,
   * in either order, in any of the given lines.
   * @param lines 
   *   any array of lines
   * @param currentLoc
   *   any position object
   * @param newLoc
   *   any position object
   * @return
   *   true if the two locations occur consecutively in some line
   */
  public static boolean checkForLineSegment(ArrayList<Line> lines, Location currentLoc, Location newLoc)
  {
	  for (int i = 0; i < lines.size(); i++)
		{
			ArrayList<Location> cells = new ArrayList<Location>(lines.get(i).getCells());
			for (int j = 0; j < cells.size(); j++)
			{
				// This if statement is used to see at what point in the for loop we are at. If near the end, it will go to the else if.
				// The reasoning for this is to avoid errors when checking before/after values in the nested if statements.
				if (j < cells.size() - 1)
				{
					// Checks to see if the cells before or after the current cell is equal to the current cell.
					if ((cells.get(j).equals(currentLoc) && cells.get(j + 1).equals(newLoc)) || (cells.get(j).equals(newLoc) && cells.get(j + 1).equals(currentLoc)))
					{
						return true;
					}
				}
				else if (j > 1)
				{
					// Checks to see if the cells before or after the current cell is equal to the current cell.
					if ((cells.get(j).equals(currentLoc) && cells.get(j - 1).equals(newLoc)) || (cells.get(j).equals(newLoc) && cells.get(j - 1).equals(currentLoc)))
					{
						return true;
					}
				}
			}
		}
    return false;
  }
}
