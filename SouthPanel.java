import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*; import java.util.*; import java.io.*;

public class SignInPanel extends JPanel
{
    private JTextField username;
    private JTextField password;
    private JButton login;
    private JButton cancel;
	private GameData data;

    public SignInPanel(GameData gd)
    {
		data = gd;

        setLayout(new BorderLayout());

        add(new CenterPanel(), BorderLayout.CENTER);
        add(new SouthPanel(), BorderLayout.SOUTH);
    }
    
    public class CenterPanel extends JPanel implements ActionListener
    {
        public CenterPanel()
        {
            setLayout(new GridLayout(2,1));

            username = new JTextField("Username", 20);
            password = new JTextField("Password", 20);

            add(username);
            add(password);
            
            username.setEditable(true);
            password.setEditable(true);
            
            username.addActionListener(this);
            password.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            
        }

        public class AccountInfo
        {
            public AccountInfo()
            {
                Scanner inFile = null;
                String fileName = "AccountInfo.txt";
                File inputFile = new File(fileName);

                try
                {
                    inFile = new Scanner(inputFile);
                }

                catch(FileNotFoundException e)
                {
                    System.err.printf("ERROR: Cannot open %s\n,", fileName);
                    System.out.println(e);
                    System.exit(1);
                }


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
	
	public void saveAccount()
	{

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
        }
    }

    

    public class SouthPanel extends JPanel implements ActionListener
    {
        public SouthPanel()
        {
            setLayout(new GridLayout(2,1));

            login = new JButton("Login");
            cancel = new JButton("Cancel");
            
            login.addActionListener(this);
            cancel.addActionListener(this);

            add(login);
            add(cancel);
        }

        public void actionPerformed(ActionEvent e)
        {

        }
    }
}