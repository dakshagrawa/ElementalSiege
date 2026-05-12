import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;
import java.util.*;

public class GameData
{
	private String first;
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
	
	public GameData(PageManager pageMngr)
	{
		pm = pageMngr;
		fileName = "../storedData/AccountInfo.txt";
		accountInfoFile = new File(fileName);
		first = "";
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
		String fileName = "computerQuestions.txt"; //TODO: Change FileName
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
		correctAnswer = inFile.nextInt();
		inFile.close();
	}
	
	public void setName(String f, String l)
	{
		first = f;
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
	
	/*public String toString ( )
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
				if(!hasBeenAdded && Integer.parseInt("" + line.charAt(line.indexOf("/") - 1)) <= lastGameCorrectCount);
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
	}*/

	// checks if the AccountInfo.txt file has the account in it
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
							Arrays.fill(currentPwd, '*'); // Clean up local password
							input.close();
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
				//Passwords are not encrypted in file for the sake of simplicity.
				
				pw.close();
				return true;
			}

			pw.close();
		}
		return false;
	}

	public void setAvatar(int avatar)
	{
		if (avatar==1)
			userAvatar = "FEMALE";
		else if(avatar==2)
			userAvatar = "MALE";
		else
			userAvatar = "NO AVATAR";
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
		inputCharacterInfo.nextLine(); // To ignore the instructions that are given on the top of the file

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

	public static class CharacterImage 
	{
		private Image fullImage;
		public BufferedImage image;
		public String name;
		public int x1, y1, x2, y2, width, height;  //used: https://pixspy.com/ to get coordinates // TODO: cite this
		public int problemSolving, wisdom, curiosity;
		public char rarity;
		public boolean hasError;

		public CharacterImage(Image img, int xVal, int yVal, int widthVal, int heightVal, Scanner inputCharInfo) 
		{
			try
			{
				fullImage = img;
				x1 = xVal;
				y1 = yVal;
				width = widthVal;
				height = heightVal;
				x2 = x1+width;
				y2 = y1+height;

				hasError = (img == null || x1 < 0 || y1 < 0 || width <= 0 || height <= 0 || !parsedCharacterInfo(inputCharInfo)); // Basic validation + parsing
				getCroppedImage();

				if(hasError)
					throw new CharacterInitializationException();
			}
			catch(CharacterInitializationException e)
			{
				System.err.println("Could not load " + name);
			}
		}

		public CharacterImage(Image img, int xMin, int yMin, int xMax, int yMax, Scanner inputCharInfo, boolean justXandY) 
		{
			this(img, xMin, yMin, xMax - xMin, yMax - yMin, inputCharInfo);
		}

		public boolean parsedCharacterInfo(Scanner input)
		{
			if(input.hasNextLine() && input!=null)
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

						rarity = line.toUpperCase().charAt(0);

						return true;
					}
				}
				catch(NumberFormatException | IndexOutOfBoundsException e) //TODO: citation for NumberFormatException
				{
					System.err.println("Error parsing Character info: " + e);
				}
			}
			return false;
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

		public class CharacterInitializationException extends RuntimeException //TODO: cite -> https://www.geeksforgeeks.org/java/user-defined-custom-exception-in-java/
		{
			public CharacterInitializationException()
			{
				super(name + " information is wrong");
			}
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
}