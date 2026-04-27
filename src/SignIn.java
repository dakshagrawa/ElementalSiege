import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

public class SignIn extends JPanel
{
	private JTextField username;
	private JPasswordField password;
	private JButton login;
	private JButton cancel;
	private GameData data;
	private PageManager pm;
	private SouthPanel sp;

	public SignIn(PageManager pageMngr)
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
			add(signIn());
		}
		
		public JLabel signIn()
		{
			JLabel title = new JLabel("Sign In");
			title.setFont(pm.titleFont);
			return title;
		}
	}
	
	public class CenterPanel extends JPanel
	{
		public CenterPanel()
		{
			setLayout(new GridLayout(2,1,0,10));
			setPreferredSize(new Dimension(200,80));

			username = new JTextField();
			password = new JPasswordField();
			
			username.setEditable(true);
			password.setEditable(true);

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
				login.doClick();
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

			login = new JButton("Login");
			cancel = new JButton("Cancel");

			login.setFont(pm.normalFont);
			cancel.setFont(pm.normalFont);
			
			login.addActionListener(this);
			cancel.addActionListener(this);

			add(cancel);
			add(login);
			add(new JPanel());
			warningLabel = new JLabel("");
			warningLabel.setForeground(Color.RED);
			add(warningLabel);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Login"))
			{
				char[] pwd = password.getPassword();
				boolean loginExists = data.isAccountInFile(username.getText(),pwd);
				Arrays.fill(pwd, '*');
				if(loginExists)
				{}
				else
				{
					warningLabel.setText("Username or password is incorrect. Please try again.");
				}
			}
			else if (command.equals("Cancel"))
			{
				username.setText("");
				password.setText("");
				pm.changeCard("Title");
			}
		}
	}
}