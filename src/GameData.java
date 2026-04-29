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

	private String userAvatar;
	
	public GameData(PageManager pageMngr)
	{
		pm = pageMngr;
		fileName = "../storedData/AccountInfo.txt";
		accountInfoFile = new File(fileName);
		first = "";
		userAvatar = "";
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
		String fileName = "computerQuestions.txt";
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
			System.out.println(e);
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
	}

	// checks if the AccountInfo.txt file has the account in it
	public boolean isAccountInFile(String inName, char[] inPwd, boolean checkPassword)
	{
		Scanner input = null;
        try 
		{
			input = new Scanner(accountInfoFile);
		} 
		catch (FileNotFoundException e) {
			System.err.printf("ERROR: Cannot read %s\n", fileName);
			System.err.println("Cannot open account.");
			System.out.println(e);
			e.printStackTrace();
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
				
				if(currentName!=null && currentName.equalsIgnoreCase(inName) && checkPassword && Arrays.equals(currentPwd, inPwd)) 
				{
					System.out.println("\nLogin Successful! :)");
					Arrays.fill(currentPwd, '*'); // Clean up local password
					input.close();
					return true;
				}
				else if(currentName!=null && currentName.equalsIgnoreCase(inName) && !checkPassword) 
				{
					System.out.println("\nSign up login does not already exist in file! :)");
					Arrays.fill(currentPwd, '*');
					input.close();
					return true;
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
				System.err.printf("ERROR: Cannot open %s\n", fileName);
				System.err.println("Cannot create new account.");
				System.out.println(e);
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
}