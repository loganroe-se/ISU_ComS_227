package hw3;

import java.util.ArrayList;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Game state for a Lines game.
 * @author Logan Roe
 */
public class LinesGame
{
	/**
	 * To store the givenGrid from the two argument constructor.
	 */
	private GridCell[][] grid;
	
	/**
	 * To store the givenLines from the two argument constructor.
	 */
	private ArrayList<Line> lines = new ArrayList<>();
	
	/**
	 * To store what the current line of the game is.
	 */
	private Line currLine;
	
	/**
	 * Stores the total moves made so far.
	 */
	private int totalMoves;
	
  /**
   * Constructs a LinesGame from the given grid and Line list.
   * This constructor does not do any error-checking to ensure
   * that the grid and the Line array are consistent. Initially
   * the current line is null.
   * @param givenGrid
   *   a 2d array of GridCell
   * @param givenLines
   *   list of Line objects
   */
  public LinesGame(GridCell[][] givenGrid, ArrayList<Line> givenLines)
  {	 
	grid = givenGrid;  

    lines = givenLines;
    
    currLine = null;
    
    totalMoves = 0;
  }
  
  /**
   * Constructs a LinesGame from the given descriptor. Initially the
   * current line is null.
   * @param descriptor
   *   array of strings representing initial state
   */
  public LinesGame(String[] descriptor)
  {
    grid = StringUtil.createGridFromStringArray(descriptor);
    
    lines = Util.createLinesFromGrid(grid);
    
    currLine = null;
    
    totalMoves = 0;
  }
  
  /**
   * Returns the number of columns for this game.
   * @return
   *  width for this game
   */ 
  public int getWidth()
  {
    return grid[0].length;
  }
  
  /**
   * Returns the number of rows for this game.
   * @return
   *   height for this game
   */ 
  public int getHeight()
  {
    return grid.length;
  }
  
  /**
   * Returns the current cell for this game, possibly null.
   * The current cell is just the last location, if any, 
   * in the current line, if there is one. Returns null
   * if the current line is null or if the current line
   * has an empty list of locations.
   * @return
   *   current cell for this game, or null
   *   
   */
  public Location getCurrentLocation()
  {
    return currLine.getLast();
  }
  
  /**
   * Returns the id for the current line, or -1
   * if the current line is null.
   * @return
   *   id for the current line
   */
  public int getCurrentId()
  {
    if (currLine == null)
    {
    	return -1;
    }
    else
    {
    	return currLine.getId();
    }
  }
  
  /**
   * Return this game's current line (which may be null).
   * @return
   *   current line for this game
   */
  public Line getCurrentLine()
  {
    return currLine;
  }
  
  /**
   * Returns a reference to this game's grid.  Clients should
   * not modify the array.
   * @return
   *   the game grid
   */
  public GridCell[][] getGrid()
  {
    return grid;
  }
  
  /**
   * Returns the grid cell at the given position.
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   grid cell at (row, col)
   */
  public GridCell getCell(int row, int col)
  {
    return grid[row][col];
  }
  
  /**
   * Returns all Lines for this game.  Clients should not modify
   * the returned list or the Line objects.
   * @return
   *   list of lines for this game
   */ 
  public ArrayList<Line> getAllLines()
  {
    return lines;
  }
  
  /**
   * Returns the total number of moves.  A "move" means that a 
   * new Location was successfully added to the current line
   * in addCell.
   * @return
   *   total number of moves so far in this game
   */
  public int getMoveCount()
  {
    return totalMoves;
  }
  
  /**
   * Returns true if all lines are connected and all
   * cells are at their maximum count.
   * @return
   *   true if all lines are complete and all cells are at max
   */ 
  public boolean isComplete()
  {
    boolean completeOrNot = false;
    int counter = 0;
    int counter2 = 0;
    int numCells = 0;
    int numLines = 0;
    
    for (int i = 0; i < lines.size(); i++)
    {
    	// Checks to see if the line is connected and, if so, it counts up to compare at the end.
    	if (lines.get(i).isConnected())
    	{
    		counter++;
    	}
    	
    	ArrayList<Location> cells = new ArrayList<Location>(lines.get(i).getCells());
    	
    	// This determines if the line has any cells within it and, if so, it counts the number of lines up.
    	if (cells.size() > 0)
    	{
    		numLines++;
    	}
    	
    	// Looks through all of the cells in the particular line and sees if they are maxed out.
    	for (int j = 0; j < cells.size(); j++)
    	{
    		int tempRow = cells.get(j).row();
    		int tempCol = cells.get(j).col();
    		numCells++;
    		
    		if (grid[tempRow][tempCol].maxedOut())
    		{
    			counter2++;
    		}
    	}
    }
    
    if (counter == numLines && counter2 == numCells)
    {
    	completeOrNot = true;
    }
    
    return completeOrNot;
  }
  
  /**
   * Attempts to set the current line based on the given
   * row and column.  When using a GUI, this method is typically 
   * invoked when the mouse is pressed. If the current line is 
   * already non-null, this method does nothing.
   * There are two possibilities:
   * <ul>
   *   <li>Any endpoint can be selected.  Selecting an 
   *   endpoint clears the line associated with that endpoint's id,
   *   and all cells that were previously included in the line are decremented.
   *   The line then becomes the current line, and the endpoint is incremented
   *   and placed on the line's list of locations as its only element.
   *   <li>A non-endpoint cell can be selected if it is not a crossing
   *   and if it is the last cell in some line.  That line then becomes
   *   the current line.
   * </ul>
   * If neither of the above conditions is met, or if the
   * current line is non-null, this method does nothing.
   * 
   * @param row
   *   given row
   * @param col
   *   given column
   */
  public void startLine(int row, int col)
  {
    if (currLine != null)
    {
    	return;
    }
    
    Location loc = new Location(row, col);
    
    // Multiple nested if statements and loops to check all of the conditions specified.
    if (grid[row][col].isEndpoint() == true)
    {    	
    	for (int i = 0; i < lines.size(); i++)
    	{
    		if (lines.get(i).getEndpoint(0).equals(loc))
    		{
    			ArrayList<Location> cells = new ArrayList<Location>(lines.get(i).getCells());
    			// Decrements all of the cells in a line if the above conditions are all met.
    			for (int j = 0; j < cells.size(); j++)
    			{
    				int tempRow = cells.get(j).row();
    				int tempCol = cells.get(j).col();
    				grid[tempRow][tempCol].decrement();
    			}
    			// Clears the line that the end point was a part of. Then sets the current line to it.
    			// Increments the cell and then adds the location to the line.
    			lines.get(i).clear();
    			currLine = lines.get(i);
    			grid[row][col].increment();
    			lines.get(i).add(loc);
    		}
    		// This is different than the previous if statement as it checks for the second end point of the line rather than the first.
    		else if (lines.get(i).getEndpoint(1).equals(loc))
    		{
    			ArrayList<Location> cells = new ArrayList<Location>(lines.get(i).getCells());
    			// Decrements all of the cells in a line if the above conditions are all met.
    			for (int j = 0; j < cells.size(); j++)
    			{
    				int tempRow = cells.get(j).row();
    				int tempCol = cells.get(j).col();
    				grid[tempRow][tempCol].decrement();
    			}
    			// Clears the line that the end point was a part of. Then sets the current line to it.
    			// Increments the cell and then adds the location to the line.
    			lines.get(i).clear();
    			currLine = lines.get(i);
    			grid[row][col].increment();
    			lines.get(i).add(loc);
    		}
    	}
    }
    else
    {
    	// Checks the condition of the cell being a crossing and if it is the last cell in the line.
    	for (int i = 0; i < lines.size(); i++)
    	{
    		if (!grid[row][col].isCrossing() && lines.get(i).getLast().equals(loc))
    		{
    			currLine = lines.get(i);
    		}
    	}
    }
  }
  
  /**
   * Sets the current line to null. When using a GUI, this method is 
   * typically invoked when the mouse is released.
   */
  public void endLine()
  {
	 currLine = null;
	 isComplete();
  }
  
  /**
   * Attempts to add a new cell to the current line.  
   * When using a GUI, this method is typically invoked when the mouse is 
   * dragged.  In order to add a cell, the following conditions must be satisfied.
   * Here the "current cell" is the last cell in the current line, and "new cell"
   * is the cell at the given row and column:
   * :
   * <ol>
   *   <li>The current line is non-null
   *   <li>The current line is not connected
   *   <li>The given row and column are adjacent to the location of the current cell
   *       (horizontally, vertically, or diagonally) and not the same as the current cell
   *   <li>The count for the new cell is less than its max count
   *   <li>If the new cell is a MIDDLE or ENDPOINT, then its id matches
   *   the id for the current line
   *   <li>Adding the new cell will not cause the line to re-trace any
   *   existing line (according to the result of Util.checkForLineSegment)
   *   <li>Adding the new cell to the line would not cross any existing line
   *   (according to the result of Util.checkForPotentialCrossing)
   * </ol>
   * If the above conditions are met, a new Location at (row, col) is added
   * to the current line and the cell count is incremented.  Otherwise, the 
   * method does nothing.  If a new location
   * is added to the current line, the move counter is increased by 1.
   * @param row
   *   given row for the new cell
   * @param col
   *   given column for the new cell
   */
  public void addCell(int row, int col)
  {
	int id = 0;
	 
	// Finds the initial id for the current line by iterating through the difference lines.
	for (int i = 0; i < lines.size(); i++)
	{
		if (currLine.getId() == lines.get(i).getId())
		{
			id = i;
		}
	}
	
	// A lot of nested if statements to check all of the conditionals required.
	if (currLine != null && !currLine.isConnected())
    {
    	Location loc = currLine.getLast();
    	int row2 = loc.row();
    	int col2 = loc.col();
    	Location loc2 = new Location(row, col);
    	
    	if (!Util.checkForLineSegment(lines, loc, loc2) && !Util.checkForPotentialCrossing(lines, loc, loc2))
    	{
    		// This if statement checks for adjacency by doing some absolute value math and making sure the two locations are not equal to each other.
    		if (Math.abs(row - row2) <= 1 && Math.abs(col - col2) <= 1 && !loc.equals(loc2))
        	{
        		if (grid[row][col].getCount() < grid[row][col].getMaxCount())
        		{
        			if (grid[row][col].isMiddle() || grid[row][col].isEndpoint())
        			{
        				// Checks if the id matches of the current line and the cell.
        				// If so, it will increment the cell, add the location to the line, and increase the total moves.
        				if (grid[row][col].idMatches(currLine.getId()))
        				{
        					grid[row][col].increment();
    						lines.get(id).add(loc2);
    						totalMoves++;
        				}
        			}
        			// Checks if the cell is an open or a crossing.
        			// If so, it will increment the cell, add the location to the line, and increase the total moves.
        			else if (grid[row][col].isOpen() || grid[row][col].isCrossing())
        			{
    					grid[row][col].increment();
    					lines.get(id).add(loc2);
    					totalMoves++;
        			}
        		}
        	}
    	}
    }
  }
  

  /**
   * Returns a string representation of this game.
   */
  public String toString()
  {
    String result = "";
    result += "-----\n";
    result += StringUtil.originalGridToString(getGrid());
    result += "-----\n";
    result += StringUtil.currentGridToString(getGrid(), getAllLines());
    result += "-----\n";
    result += StringUtil.allLinesToString(getAllLines());
    Line ln = getCurrentLine();
    if (ln != null)
    {
      result += "Current line: " + ln.getId() + "\n";
    }
    else
    {
      result += "Current line: null\n";
    }
    return result;
  }

}
