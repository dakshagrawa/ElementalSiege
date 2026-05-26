import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;
import java.util.*;

public class GameData
{
	private String first, last;
	private String question;
	private String [] answerSet;
	private int correctAnswer;
	private boolean [] chosenQuestions;
	private int questionCount;
	private int correctCount, lastGameCorrectCount;
    private File accountInfoFile;
	private String fileName;
	private PageManager pm;

	public CharacterImage[] characters;
	public int[] userCharacters;

	private String userAvatar;

	private String username;
	private int savedHealth;
	private int savedLevel;

	// Explicit game state tracking for centralized management
	public int currentLevel; // Tracks current level during gameplay
	public int currentHealth; // Tracks current health during gameplay

	/**
	 * Call when player successfully completes a level
	 * Advances to next level and resets health if needed
	 */
	public void advanceLevel() {
		currentLevel++;
		// Reset health to max for new level if desired
		// currentHealth = getMaxHealthForLevel(currentLevel);
	}

	/**
	 * Trigger win state - player has won the game
	 * Notifies PageManager to transition to win screen
	 */
	public void triggerWinState() {
		// This would typically be handled by BattlePanel checking conditions
		// and calling pm.changePanelCard("You Win");
		// Keeping this method for future extension
	}

	/**
	 * Trigger loss state - player has lost the game
	 * Notifies PageManager to transition to loss screen
	 */
	public void triggerLossState() {
		// This would typically be handled by BattlePanel checking conditions
		// and calling pm.changePanelCard("Game Over");
		// Keeping this method for future extension
	}

	private String userDataFileName = "storedData/UsersGameData.txt";
	
	public GameData(PageManager pageMngr)
	{
		pm = pageMngr;
		fileName = "storedData/AccountInfo.txt";
		accountInfoFile = new File(fileName);
		first = "";
		last = "";
		userAvatar = "";
		getCharactersFromFiles();
		//getQuestions(); //TODO
		correctCount = 0;
		resetAll();
	}
	
	public void resetAll ( )
	{
		lastGameCorrectCount = correctCount;
		answerSet = new String[4];
		question = "";
		for(int i = 0; i < answerSet.length; i++)
		{
			answerSet[i] = "";
		}
		correctAnswer = -1;
		chosenQuestions = new boolean[30];
		questionCount = correctCount = 0;
	}

	public void grabQuestionFromFile ( )
	{
		Scanner inFile = null;
		String fileName = "storedData/Questions.txt"; //TODO: Change FileName
		File inputFile = new File(fileName);
		try
		{
			inFile = new Scanner(inputFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("ERROR MESSAGE: Cannot open %s\n", fileName);
			System.err.println(e);
			System.exit(1);
		}
		int questionNumber = (int)(Math.random() * 30);
		while(chosenQuestions[questionNumber] == true)
		{
			questionNumber = (int)(Math.random() * 30);
		}
		chosenQuestions[questionNumber] = true;
		questionCount++;
		int counter = 0;

		while(inFile.hasNext() && counter < 6 * questionNumber)
		{
			String line = inFile.nextLine();
			counter++;
		}
		question = inFile.nextLine();

		counter = 0;
		while(inFile.hasNext() && counter < 4)
		{
			answerSet[counter] = inFile.nextLine();
			counter++;
		}
		correctAnswer = inFile.nextInt() -1;
		inFile.close();
	}
	
	public void setName(String f, String l)
	{
		first = f;
		last = l;
	}
	
	public String getQuestion ( )
	{
		return "" + questionCount + ". " + question;
	}
	
	public String getAnswer(int index)
	{
		return answerSet[index];
	}
	
	public int getCorrectAnswer ( )
	{
		return correctAnswer;
	}
	
	public int getQuestionCount ( )
	{
		return questionCount;
	}
	
	public int getCorrectCount ( )
	{
		return lastGameCorrectCount;
	}
	
	public void addOneToCorrectCount ( )
	{
		correctCount++;
	}
	
	public String toString ( )
	{
		if(lastGameCorrectCount > 2)
		{
			return "Congratulations, " + first + ", you answered " + lastGameCorrectCount +
				" out of 4 of the questions correctly.  Your name will be added to the list of high scores, shown to the right.  Good work!";
		}
		return "Good try " + first + ", you answered " + lastGameCorrectCount +
			" out of 4 of the questions correctly.  Keep working at it, and maybe next time your name will be added to the list of high scores!";
	}
	
	public String getHighScores ( )
	{
		String result = "";
		String fileName = "highScores.txt";
		Scanner inFile = null;
		File inputFile = new File(fileName);
		try
		{
			inFile = new Scanner(inputFile);
		} 
		catch(FileNotFoundException e) 
		{
			System.err.printf("ERROR: Cannot open %s\n", fileName);
			System.err.println(e);
			System.exit(1);
		}
		while(inFile.hasNext()) 
		{
			String line = inFile.nextLine();
			result += line + "\n";
		}
		return result;
	}
	
	public void saveData ( )
	{
		if(lastGameCorrectCount >= 3)
		{
			String result = "";
			boolean hasBeenAdded = false;
			String fileName = "highScores.txt";
			Scanner inFile = null;
			File inputFile = new File(fileName);
			try 
			{
				inFile = new Scanner(inputFile);
			} 
			catch(FileNotFoundException e) 
			{
				System.err.printf("ERROR: Cannot open %s\n", fileName);
				System.err.println(e);
				System.exit(1);
			}
			while(inFile.hasNext()) 
			{
				String line = inFile.nextLine();
				if(!hasBeenAdded && Integer.parseInt("" + line.charAt(line.indexOf("/") - 1)) <= lastGameCorrectCount)
				{
					result += first + " " + lastGameCorrectCount + "/4\n";
					hasBeenAdded = true;
				}
				result += line + "\n";
			}
			if(!hasBeenAdded)
			{
				result += first + " " + lastGameCorrectCount + "/4\n";
			}
			inFile.close();

			File ioFile = new File("highScores.txt");
			PrintWriter outFile = null;
			try
			{
				outFile = new PrintWriter(ioFile);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
			outFile.print(result);
			outFile.close();
		}
	}
}
