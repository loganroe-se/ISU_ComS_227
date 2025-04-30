package hw2;

import api.ThrowType;
import static api.ThrowType.*;

/**
 * This class models a standard game of darts, keeping track of the scores,
 * whose turn it is, and how many darts the current player has remaining.
 * The number of starting points and the number of darts used in 
 * a player's turn are configurable.
 * @author Logan Roe
 */

public class DartGame
{  
	/**
	 * Player 0's score.
	 */
	private int playerScore0;
	
	/**
	 * Player 1's score.
	 */
	private int playerScore1;
	
	/**
	 * Which player is the current player, 0 or 1.
	 */
	private int currentPlayer;
	
	/**
	 * Darts left to throw for this turn.
	 */
	private int dartsLeft;
	
	/**
	 * To store a value in the case of a bust.
	 */
	private int currentScore;
	
	/**
	 * Determines whether player 0 has doubled in yet.
	 */
	private boolean doubledIn0;
	
	/**
	 * Determines whether player 1 has doubled in yet.
	 */
	private boolean doubledIn1;
	
	/**
	 * Total darts per turn. This only needs to be set one time for each new game and should not be changed afterwards.
	 */
	private final int totalDarts;
	
	/**
	 * Constructs a dart game with the given starting player, initial points of 301, and three darts for each turn.
	 * @param StartingPlayer
	 * 	The starting player, 0 or 1.
	 */
	public DartGame(int StartingPlayer)
	{
		currentPlayer = StartingPlayer;
		playerScore0 = 301;
		playerScore1 = 301;
		totalDarts = 3;
		dartsLeft = 3;
		doubledIn0 = false;
		doubledIn1 = false;
	}
  
	/**
	 * Constructs a dart game with the given starting player, initial points, and number of darts for each turn.
	 * @param StartingPlayer
	 * 	The starting player, 0 or 1.
	 * @param StartingPoints
	 * 	The starting points for a game.
	 * @param NumDarts
	 * 	The number of darts for each turn.
   	*/
	public DartGame(int StartingPlayer, int StartingPoints, int NumDarts)
	{
		currentPlayer = StartingPlayer;
		playerScore0 = StartingPoints;
		playerScore1 = StartingPoints;
		totalDarts = NumDarts;
		dartsLeft = NumDarts;
		doubledIn0 = false;
		doubledIn1 = false;
	}
  
	/**
	 * Returns the player whose turn it is.  (When the game is over,
	 * this method always returns the winning player.)
	 * @return
	 *   current player (0 or 1)
	 */
	public int getCurrentPlayer()
	{
		if (whoWon() != -1)
		{
			currentPlayer = whoWon();
		}
		
		return currentPlayer;
	}
  
	/**
	 * Returns the score of the indicated player (0 or 1).  If
	 * the argument is any value other than 0 or 1, the method returns
	 * -1.
	 * @param which
	 *   indicator for which player (0 or 1)
	 * @return
	 *   score for the indicated player, or -1 if the argument is invalid
	 */
	public int getScore(int which)
	{
		if (which == 0)
		{
			return playerScore0;
		}
		else if (which == 1)
		{
			return playerScore1;
		}
		else
		{
			return -1;
		}
	}
  
	/**
	 * Returns the number of darts left in the current player's turn.
	 * @return
	 *   the number of darts left in the current player's turn
	 */
	public int getDartCount()
  	{
		return dartsLeft;
  	}
  
	/**
	 * Returns the number of points gained/loss for the given throw. The number parameter is ignored if type is MISS, OUTER_BULLSEYE, or INNER_BULLSEYE.
	 * @param type
	 * 	The type of throw.
	 * @param number
	 * 	The area of the dart board where the dart lands, ignored if there is a MISS, OUTER_BULLSEYE, or INNER_BULLSEYE.
	 * @return
	 *	The number of points for the given throw.
	 */
	public static int calcPoints(ThrowType type, int number)
	{
		int scoreToDeduct = 0;

		if (type == MISS)
		{
			scoreToDeduct = 0;
		}
		else if (type == OUTER_BULLSEYE)
		{
			scoreToDeduct = 25;
		}
		else if (type == INNER_BULLSEYE)
		{
			scoreToDeduct = 50;
		}
		else if (type == SINGLE)
		{
			scoreToDeduct = number;
		}
		else if (type == DOUBLE)
		{
			scoreToDeduct = number * 2;
		}
		else
		{
			scoreToDeduct = number * 3;
		}
		
		return scoreToDeduct;
	}
  
	/**
	 * Returns true if one of the players has a score of 0, else, false.
	 * @return
	 * 	Returns true if a player has a score of 0 and false otherwise.
	 */
	public boolean isOver()
	{
		if (playerScore0 == 0 || playerScore1 == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Switches the current player and resets the dart count. This is a helper method.
	 */
	private void switchPlayer()
	{
		currentPlayer = currentPlayer ^ 1;
		dartsLeft = totalDarts;
	}
	
	/**
	 * Reduces the score for the current player by the given amount. This is a helper method.
	 * @param amount
	 * 	The number of points to subtract.
	 */
	private void adjustScore(int amount)
	{
		if (currentPlayer == 0)
		{
			playerScore0 -= amount;
		}
		else
		{
			playerScore1 -= amount;
		}
	}
	
	/**
	 * A placeholder for the score at the start of the turn, in the case a bust occurs.
	 */
	private void storeCurrentScore()
	{	
		if (currentPlayer == 0)
		{
			currentScore = playerScore0;
		}
		else
		{
			currentScore = playerScore1;
		}
	}
	
	/**
	 * Determines if the current player has busted or not. 
	 * If they have busted, hasBusted is set to true, the player's score is reset to their score at the start of that turn, and players are switched.
	 */
	private boolean bustedOrNot(ThrowType type)
	{
		boolean hasBusted;
		
		if (currentPlayer == 0)
		{
			if (playerScore0 == 1 || playerScore0 < 0 || (playerScore0 == 0 && (type != DOUBLE && type != INNER_BULLSEYE)))
			{
				hasBusted = true;
				playerScore0 = currentScore;
				switchPlayer();
			}
			else
			{
				hasBusted = false;
			}
			return hasBusted;
		}
		else
		{
			if (playerScore1 == 1 || playerScore1 < 0 || (playerScore1 == 0 && (type != DOUBLE && type != INNER_BULLSEYE)))
			{
				hasBusted = true;
				playerScore1 = currentScore;
				switchPlayer();
			}
			else
			{
				hasBusted = false;
			}
		}
		
		return hasBusted;
	}
	
	/**
	 * Determines if the player has doubled in or not. If they haven't and they did not get a double or inner bullseye, nothing happens.
	 * If they haven't doubled in but get a type double or inner bullseye while also busting, they are now doubled in but lose their turn.
	 * If they haven't doubled in but get a type double or inner bullseye without busting, they are doubled in and get the points.
	 * If they have doubled in, they get the points as usual.
	 * @param type
	 * 	The type of throw.
	 * @param number
	 * 	The area of the dart board where the dart lands, ignored if there is a MISS, OUTER_BULLSEYE, or INNER_BULLSEYE.
	 */
	private void doubledIn(ThrowType type, int number)
	{
		if (currentPlayer == 0)
		{
			if (doubledIn0 == false && (type != DOUBLE && type != INNER_BULLSEYE))
			{
				// Do nothing, player 0 has not doubled in yet and did not get a type of double or inner bullseye.
			}
			else if (bustedOrNot(type) == true && doubledIn0 == false && (type == DOUBLE || type == INNER_BULLSEYE))
			{
				doubledIn0 = true;
				switchPlayer();
			}
			else if (doubledIn0 == false && (type == DOUBLE || type == INNER_BULLSEYE))
			{
				doubledIn0 = true;
				adjustScore(calcPoints(type, number));
			}
			else
			{
				adjustScore(calcPoints(type, number));
			}
			
		}
		else
		{
			if (doubledIn1 == false && (type != DOUBLE && type != INNER_BULLSEYE))
			{
				// Do nothing, player 0 has not doubled in yet and did not get a type of double or inner bullseye.
			}
			else if (bustedOrNot(type) == true && doubledIn1 == false && (type == DOUBLE || type == INNER_BULLSEYE))
			{
				doubledIn1 = true;
				switchPlayer();
			}
			else if (doubledIn1 == false && (type == DOUBLE || type == INNER_BULLSEYE))
			{
				doubledIn1 = true;
				adjustScore(calcPoints(type, number));
			}
			else
			{
				adjustScore(calcPoints(type, number));
			}
		}
	}
	
	/**
	 * Simulates the throwing of one dart and adjusting points as needed. If the current player has used all darts or has busted, the player switches, unless the player has won.
	 * @param type
	 * 	The type of throw.
	 * @param number
	 * 	The area of the dart board where the dart lands, ignored if there is a MISS, OUTER_BULLSEYE, or INNER_BULLSEYE.
	 */
	public void throwDart(ThrowType type, int number)
	{
		if (dartsLeft == totalDarts)
		{
			storeCurrentScore();
		}
		
		if (whoWon() != -1)
		{
			// Do nothing, a player has won already.
		}
		else if (dartsLeft == 0 || bustedOrNot(type) == true)
		{
			switchPlayer();
		}
		else
		{			
			dartsLeft -= 1;
			doubledIn(type, number);
			bustedOrNot(type);
			
			if (dartsLeft == 0 && isOver() == false)
			{
				switchPlayer();
			}
		}
	}
  
	/**
	 * Returns a string representation of the current game state.
	 */
	public String toString()
	{
		String result = "Player 0: " + getScore(0) +
						"  Player 1: " + getScore(1) +
						"  Current: Player " + getCurrentPlayer() +
						"  Darts: " + getDartCount();
		return result;
	}
  
	/**
	 * Determines who won the game, if anyone.
	 * @return
	 * 	Returns whether player 0 or 1 won or -1 if the game is not over.
	 */
	public int whoWon()
	{
		if (playerScore0 == 0)
		{
			return 0;
		}
		else if (playerScore1 == 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
  
}