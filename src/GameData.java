import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class GameData
{
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

	private String username;
	private int savedHealth;
	private int savedLevel;
	private String userDataFileName = "../storedData/UsersGameData.txt";

	private int currentQuestionNumber; //question number that's being displayed -- will later be passed into 
	//Explanation panel methods in case user gets question wrong
	
	public GameData(PageManager pageMngr)
	{
		pm = pageMngr;
		fileName = "../storedData/AccountInfo.txt";
		accountInfoFile = new File(fileName);
		getCharactersFromFiles();
		//getQuestions();
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
		chosenQuestions = new boolean[65];
		questionCount = correctCount = 0;
	}

	public void grabQuestionFromFile ( )
	{
		Scanner inFile = null;
		String fileName = "../storedData/Questions.txt";
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
		int questionNumber = (int)(Math.random() * 65);
		// if all questions have been used, reset the pool so game doesn't get stuck
		if(questionCount >= 65)
		{
			Arrays.fill(chosenQuestions, false);
			questionCount = 0;
		}
		while(chosenQuestions[questionNumber] == true)
		{
			questionNumber = (int)(Math.random() * 65);
		}
		chosenQuestions[questionNumber] = true;
		questionCount++;
		int counter = 0;

		while(inFile.hasNextLine() && counter < 6 * questionNumber)
		{
			String line = inFile.nextLine();
			counter++;
		}
		question = inFile.nextLine();

		counter = 0;
		while(inFile.hasNextLine() && counter < 4)
		{
			answerSet[counter] = inFile.nextLine();
			counter++;
		}
		correctAnswer = inFile.nextInt() -1;
		inFile.close();

		currentQuestionNumber = questionNumber + 1; // +1 because questions in file start at 1
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
			return "Congratulations, you answered " + lastGameCorrectCount +
				" out of 4 of the questions correctly.  Your name will be added to the list of high scores, shown to the right.  Good work!";
		}
		return "Good try, you answered " + lastGameCorrectCount +
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
	
	public void saveToHighScores ( )
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
				System.out.println(e);
				System.exit(1);
			}
			while(inFile.hasNext()) 
			{
				String line = inFile.nextLine();
				int slashIdx = line.indexOf("/");
				if (slashIdx > 0) {
					try {
						int lineScore = Integer.parseInt("" + line.charAt(slashIdx - 1));
						if(!hasBeenAdded && lineScore <= lastGameCorrectCount)
						{
							result += username + " " + lastGameCorrectCount + "/4\n";
							hasBeenAdded = true;
						}
					} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
						// skip malformed line
					}
				}
				result += line + "\n";
			}
			if(!hasBeenAdded)
			{
				result += username + " " + lastGameCorrectCount + "/4\n";
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

	//checks if the AccountInfo.txt file has the account in it
	public boolean isAccountInFile(String inName, char[] inPwd, boolean checkPassword)
	{
		Scanner input = null;
        try 
		{
			input = new Scanner(accountInfoFile);
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println(e);
			System.err.printf("\nERROR MESSAGE: Cannot read %s\n", fileName);
			System.err.println("Cannot open account.");
			return false;
		}

		String currentName = null;
		char[] currentPwd = new char[0];
		while(input.hasNextLine()) 
		{
			String line = input.nextLine();
			
			if(line.startsWith("U: ")) 
			{
				currentName = line.substring(3);
			} 
			else if (line.startsWith("P: ")) 
			{
				currentPwd = line.substring(3).toCharArray();
				
				if(currentName!=null && currentName.equalsIgnoreCase(inName))
				{
					if(checkPassword) 
					{
						if(Arrays.equals(currentPwd, inPwd))
						{
							System.out.println("\nLogin Successful! :)");
    						Arrays.fill(currentPwd, '*'); //up password
    						input.close();
							loadSavedData(inName);
							return true;

						}
					}
					else
					{
						System.out.println("\nSign up login does not already exist in file! :)");
						Arrays.fill(currentPwd, '*');
						input.close();
						return true;
					}
				}
				
				Arrays.fill(currentPwd, '*'); 
			}
			else
			{
				currentName = null;
				Arrays.fill(currentPwd, '*');
			}
		}
		input.close();
		return false;
	}

	public void loadSavedData(String inName)
	{
		username = inName;
		savedHealth = 100; //defaults if no saved data found
		savedLevel = 1;
		for (int i = 0; i < userCharacters.length; i++)
			userCharacters[i] = -1;

		Scanner input = null;
		try
		{
			input = new Scanner(new File(userDataFileName));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("No user data file found. Starting fresh.");
			return;
		}

		boolean foundUser = false;
		while (input.hasNextLine())
		{
			String line = input.nextLine().trim();
			if (line.equals("U: " + inName))
			{
				foundUser = true;
			}
			else if (foundUser && line.startsWith("H: ")) //checks to see if this is the correct statistic to enter in that line
			{
				savedHealth = Integer.parseInt(line.substring(3).trim());
			}
			else if (foundUser && line.startsWith("L: "))
			{
				savedLevel = Integer.parseInt(line.substring(3).trim());
			}
			else if (foundUser && line.startsWith("C: "))
			{
				String cardsPart = line.substring(3).trim();
				cardsPart = cardsPart.replace("[", "").replace("]", "").trim();
				if (!cardsPart.isEmpty())
				{
					String[] parts = cardsPart.split(",");
					for (int i = 0; i < parts.length && i < userCharacters.length; i++)
					{
						String part = parts[i].trim();
						if (!part.isEmpty())
							userCharacters[i] = Integer.parseInt(part);
					}
				}
				break; //done with this method
			}
		}
		input.close();

		if (!foundUser)
			System.out.println("No saved data found for " + inName + ". Starting fresh.");
	}

	public void saveUserProgress(int currentHealth, int currentLevel)
	{
		// don't save if username is null -- this can happen if game tries to save before user has logged in
		if (username == null || username.equals("null"))
		{
			System.err.println("saveUserProgress skipped: username is null");
			return;
		}
		//Will read entire file into a string
		StringBuilder allContent = new StringBuilder();
		Scanner input = null;
		try
		{
			input = new Scanner(new File(userDataFileName));
		}
		catch (FileNotFoundException e)
		{
			//File doesn't exist yet, will be created fresh
		}

		boolean foundUser = false;
		boolean inUserBlock = false;

		if (input != null)
		{
			while (input.hasNextLine())
			{
				String line = input.nextLine().trim();

				if (line.equals("U: " + username))
				{
					foundUser = true;
					inUserBlock = true;
					//Write an updated block instead of old one
					allContent.append(buildUserBlock(currentHealth, currentLevel));
				}
				else if (inUserBlock && (line.startsWith("H: ") || line.startsWith("L: ") || line.startsWith("C: ")))
				{
					//Skip old lines for current user — already replaced above
				}
				else
				{
					inUserBlock = false;
					allContent.append(line).append("\n");
				}
			}
			input.close();
		}

		//If user wasn't in file at all, add them
		if (!foundUser)
		{
			allContent.append(buildUserBlock(currentHealth, currentLevel));
		}

		//Write everything back
		PrintWriter output = null;
		try
		{
			output = new PrintWriter(new File(userDataFileName));
			output.print(allContent.toString());
			output.close();
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Could not save user progress: " + e);
		}
	}

	private String buildUserBlock(int currentHealth, int currentLevel)
	{
		StringBuilder block = new StringBuilder();
		block.append("U: ").append(username).append("\n");
		block.append("H: ").append(currentHealth).append("\n");
		block.append("L: ").append(currentLevel).append("\n");

		//Build a string array to store index of cards e.g. [0, 3, 7]
		block.append("C: [");
		boolean first = true;
		for (int i = 0; i < userCharacters.length; i++)
		{
			if (userCharacters[i] != -1)
			{
				if (!first) block.append(", ");
				block.append(userCharacters[i]);
				first = false;
			}
		}
		block.append("]\n");
		return block.toString();
	}

	public boolean putAccountInFile(String inName, char[] inPwd, boolean passwordCorrectlyRepeated)
	{
		if(passwordCorrectlyRepeated)
		{
			PrintWriter pw = null;
			try 
			{
				pw = new PrintWriter(new FileWriter(accountInfoFile, true));
			} 
			catch(IOException e) 
			{
				System.err.println(e);
				System.err.printf("ERROR MESSAGE: Cannot open %s\n", fileName);
				System.err.println("Cannot create new account.");
				return false;
			}

			if(!isAccountInFile(inName,inPwd,false))
			{
				pw.println("");
				pw.println("---");
				pw.println("U: " + inName);
				pw.println("P: " + new String(inPwd));
				//Passwords aren't encrypted in the txt file
				
				pw.close();
				return true;
			}

			pw.close();
		}
		return false;
	}

	public void getCharactersFromFiles()
	{
		String infoFileName = "../storedData/CharacterInfo.txt";
		Scanner inputCharacterInfo = null;
        try 
		{
			inputCharacterInfo = new Scanner(new File(infoFileName));
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println(e);
			System.err.printf("\nERROR MESSAGE: Cannot read %s\n", infoFileName);
			System.err.println("Characters cannot be retrieved from file. Game will not work. :(");
			System.exit(1);
		}


		characters = new CharacterImage[12];

		userCharacters = new int[characters.length];
		for (int i = 0; i < characters.length; i++) 
		{
			characters[i] = null;
			userCharacters[i] = -1;
		}
		int counter = 0;

		BufferedImage pic1 = PageManager.Functions.getBufferedImage("characters/characters1.png");
		for (int pic1_i = 0; pic1_i < 6; pic1_i++) 
		{
			for (int pic1_j = 0; pic1_j < 2; pic1_j++) 
			{
				characters[counter] = new CharacterImage(pic1,30+(224*pic1_i),25+(364*pic1_j),211,354, inputCharacterInfo);
				counter++;
			}
		}
	}

	public int getCurrentQuestionNumber()
	{
		return currentQuestionNumber;
	}

	public static class CharacterImage 
	{
		private Image fullImage;
		public BufferedImage image;
		public String name;
		public int x1, y1, x2, y2, width, height;  //used: https://pixspy.com/ to get coordinates
		public int problemSolving, wisdom, curiosity;
		public char rarityChar;
		public String rarity;
		public boolean hasError;

        public CharacterImage(Image img, int xVal, int yVal, int widthVal, int heightVal, Scanner inputCharInfo) 
		{
		    fullImage = img;
		    x1 = xVal;
		    y1 = yVal;
		    width = widthVal;
		    height = heightVal;
		    x2 = x1 + width;
		    y2 = y1 + height;
		
		    hasError = (img == null || x1 < 0 || y1 < 0 || width <= 0 || height <= 0 || !parsedCharacterInfo(inputCharInfo)); //Basic parsing
		    getCroppedImage();
		
		    if(rarityChar == 'C') 
            {
		        rarity = "Common";
		    } 
            else if(rarityChar == 'L') 
            {
		        rarity = "Legendary";
		    } 
            else if(rarityChar == 'R') 
            {
		        rarity = "Rare";
		    } 
            else if(rarityChar == 'E') 
            {
		        rarity = "Epic";
		    } 
            else 
            {
		        hasError = true;
		    }
		}

        public CharacterImage(Image img, int xMin, int yMin, int xMax, int yMax, Scanner inputCharInfo, boolean justXandY) 
		{
			this(img, xMin, yMin, xMax - xMin, yMax - yMin, inputCharInfo);
		}
        
		public void getCroppedImage()
		{
			try
			{
				image = ((BufferedImage)fullImage).getSubimage(x1, y1, width, height);
				image = PageManager.Functions.characterToBufferedImage(this);
				hasError = false;
			}
			catch(RasterFormatException | ArrayIndexOutOfBoundsException e)
			{
				System.err.println("\n\n"+name+" has error");
				e.printStackTrace();
				hasError = true;
			}
		}

        public boolean parsedCharacterInfo(Scanner input)
		{
			if(input != null && input.hasNextLine())
			{
				try
				{
					String line = input.nextLine().trim();
					if(line.contains(":"))
					{
						name = line.substring(0,line.indexOf(':')).trim();
						line = line.substring(line.indexOf(':')+1).trim();

						problemSolving = Integer.parseInt(line.substring(0,line.indexOf(",")).trim());
						line = line.substring(line.indexOf(',')+1).trim();

						wisdom = Integer.parseInt(line.substring(0,line.indexOf(",")).trim());
						line = line.substring(line.indexOf(',')+1).trim();

						curiosity = Integer.parseInt(line.substring(0,line.indexOf(",")).trim());
						line = line.substring(line.indexOf(',')+1).trim();

						rarityChar = line.toUpperCase().charAt(0);

						return true;
					}
				}
				catch(NumberFormatException | IndexOutOfBoundsException e)
				{
					System.err.println("Error parsing Character info: " + e);
				}
			}
			return false;
		}
	}

	public void setUserCharacters(int characterIdx)
	{
		for (int i = 0; i < userCharacters.length; i++) 
		{
			if(userCharacters[i]==-1)
			{
				userCharacters[i] = characterIdx;
				break;
			}
		}
	}

	public String getUsername()  //getter-and-setter methods to display this info when neeeded on screen
	{
		return username;
	} 
    public int getSavedHealth()  
	{
		return savedHealth;
	}
    public int getSavedLevel()   
	{
		return savedLevel;
	}

    public void resetProgress() //resets to default stats when user wants to start new game
    {
        savedHealth = 100;
        savedLevel  = 1;
        for (int i = 0; i < userCharacters.length; i++)
            userCharacters[i] = -1;
    }
}