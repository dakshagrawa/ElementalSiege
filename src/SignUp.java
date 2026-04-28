import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

public class SignUp extends JPanel
{
	private JTextField username;
	private JPasswordField password;
	private JPasswordField passwordCheck;
	private JButton signUp;
	private JButton cancel;
	private GameData data;
	private PageManager pm;
	private SouthPanel sp;

	public SignUp(PageManager pageMngr)
	{
		data = pageMngr.getGameData();
		pm = pageMngr;
		sp = new SouthPanel();

		setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel positioner = new JPanel(new BorderLayout(50,50));
		
		positioner.add(new signInLabel(),BorderLayout.NORTH);
		positioner.add(new CenterPanel(),BorderLayout.CENTER);
		positioner.add(sp,BorderLayout.SOUTH);
		add(positioner); 
	}

	public class signInLabel extends JPanel
	{
		public signInLabel()
		{
			setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
			setPreferredSize(new Dimension(800,200));
			add(signUp());
		}
		
		public JLabel signUp()
		{
			JLabel title = new JLabel("Sign Up");
			title.setFont(pm.titleFont);
			return title;
		}
	}
	
	public class CenterPanel extends JPanel
	{
		public CenterPanel()
		{
			setLayout(new GridLayout(3,1,0,10));
			setPreferredSize(new Dimension(200,120));

			username = new JTextField();
			password = new JPasswordField();
			passwordCheck = new JPasswordField();
			
			username.setEditable(true);
			password.setEditable(true);
			passwordCheck.setEditable(true);

			username.addActionListener(new nameListener());
			password.addActionListener(new pwdListener());

			JPanel usernamePanel = new JPanel(new GridLayout(1,2));
			JLabel usernameLabel = new JLabel("Username:");
			usernameLabel.setFont(pm.normalFont);
			usernamePanel.add(usernameLabel);
			usernamePanel.add(username);
			add(usernamePanel);

			JPanel passwordPanel = new JPanel(new GridLayout(1,2));
			JLabel passwordLabel = new JLabel("Password:");
			passwordLabel.setFont(pm.normalFont);
			passwordPanel.add(passwordLabel);
			passwordPanel.add(password);
			add(passwordPanel);

			JPanel passwordCheckPanel = new JPanel(new GridLayout(1,2));
			JLabel passwordCheckLabel = new JLabel("Confirm Password: ");
			passwordCheckLabel.setFont(pm.normalFont);
			passwordCheckPanel.add(passwordCheckLabel);
			passwordCheckPanel.add(passwordCheck);
			add(passwordCheckPanel);
		}

		public class nameListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				password.requestFocusInWindow();
			}
		}

		public class pwdListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				passwordCheck.requestFocusInWindow();
			}
		}
	}

	// public String getHighScores()
	// {
	// 	String result = "";
	// 	String fileName = "highScores.txt";
	// 	Scanner inFile = null;
	// 	File inputFile = new File(fileName);
	// 	try
	// 	{
	// 		inFile = new Scanner(inputFile);
	// 	} 
	// 	catch(FileNotFoundException e) 
	// 	{
	// 		System.err.printf("ERROR: Cannot open %s\n", fileName);
	// 		System.out.println(e);
	// 		System.exit(1);
	// 	}
	// 	while(inFile.hasNext()) 
	// 	{
	// 		String line = inFile.nextLine();
	// 		result += line + "\n";
	// 	}
	// 	return result;
	// }
	
	// public void saveAccount()
	// {
	//     String fileName = "highScores.txt";
	//     Scanner inFile = null;
	//     File inputFile = new File(fileName);
	//     try 
	//     {
	//         inFile = new Scanner(inputFile);
	//     } 
	//     catch(FileNotFoundException e) 
	//     {
	//         System.err.printf("ERROR: Cannot open %s\n", fileName);
	//         System.out.println(e);
	//         System.exit(1);
	//     }
	//     while(inFile.hasNext()) 
	//     {
	//         String line = inFile.nextLine();
	//         if(!hasBeenAdded && Integer.parseInt("" + line.charAt(line.indexOf("/") - 1)) <= lastGameCorrectCount);
	//         {
	//             result += first + " " + lastGameCorrectCount + "/4\n";
	//             hasBeenAdded = true;
	//         }
	//         result += line + "\n";
	//     }
	//     if(!hasBeenAdded)
	//     {
	//         result += first + " " + lastGameCorrectCount + "/4\n";
	//     }
	//     inFile.close();

	//     File ioFile = new File("highScores.txt");
	//     PrintWriter outFile = null;
	//     try
	//     {
	//         outFile = new PrintWriter(ioFile);
	//     }
	//     catch(IOException e)
	//     {
	//         e.printStackTrace();
	//         System.exit(1);
	//     }
	//     outFile.print(result);
	//     outFile.close();
	// }

	public class SouthPanel extends JPanel implements ActionListener
	{
		private JLabel warningLabel;

		public SouthPanel()
		{
			setLayout(new GridLayout(2,2,100,100));

			signUp = new JButton("Sign Up");
			cancel = new JButton("Cancel");

			signUp.setFont(pm.normalFont);
			cancel.setFont(pm.normalFont);
			
			signUp.addActionListener(this);
			cancel.addActionListener(this);

			add(cancel);
			add(signUp);
			add(new JPanel());
			warningLabel = new JLabel("");
			warningLabel.setForeground(Color.RED);
			add(warningLabel);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Sign Up"))
			{
				char[] pwd = password.getPassword();
				boolean signUpSuccessful = data.putAccountInFile(username.getText(),pwd);
				Arrays.fill(pwd, '*');
				if(signUpSuccessful)
				{
					pm.changeCard("Character Selection");
				}
				else
				{
					warningLabel.setText("Username already exists. Please try again.");
				}
			}
			else if (command.equals("Cancel"))
			{
				username.setText("");
				password.setText("");
				passwordCheck.setText("");
				pm.changeCard("Title");
			}
		}
	}
}