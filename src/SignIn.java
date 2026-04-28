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
		positioner.setOpaque(false);
		add(positioner); 

		setBackground(new Color(211, 211, 211, 220));
	}

	public class signInLabel extends JPanel
	{
		public signInLabel()
		{
			setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
			setPreferredSize(new Dimension(800,200));
			add(signIn());
			setOpaque(false);
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
			setOpaque(false);

			username = new JTextField();
			password = new JPasswordField();
			
			username.setEditable(true);
			password.setEditable(true);

			username.addActionListener(new nameListener());
			password.addActionListener(new pwdListener());

			JPanel usernamePanel = new JPanel(new GridLayout(1,2));
			JLabel usernameLabel = new JLabel("Username:");
			usernameLabel.setFont(pm.normalBoldFont);
			usernamePanel.add(usernameLabel);
			usernamePanel.add(username);
			usernamePanel.setOpaque(false);
			add(usernamePanel);

			JPanel passwordPanel = new JPanel(new GridLayout(1,2));
			JLabel passwordLabel = new JLabel("Password:");
			passwordLabel.setFont(pm.normalBoldFont);
			passwordPanel.add(passwordLabel);
			passwordPanel.add(password);
			passwordPanel.setOpaque(false);
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

	public class SouthPanel extends JPanel implements ActionListener
	{
		private JLabel warningLabel;

		public SouthPanel()
		{
			setLayout(new GridLayout(2,2,100,100));

			login = pm.new Button1("Login", getBackground(),new Color(70, 130, 180));
			cancel = pm.new Button1("Cancel", getBackground(),new Color(70, 130, 180));

			login.setFont(pm.normalBoldFont);
			cancel.setFont(pm.normalBoldFont);

			pm.addHoverEffect(login, new Color(20, 35, 20), getBackground());
			pm.addHoverEffect(cancel, new Color(20, 35, 20), getBackground());
			
			login.addActionListener(this);
			cancel.addActionListener(this);

			add(cancel);
			add(login);
			JPanel placeholder = new JPanel();
			placeholder.setOpaque(false);
			add(placeholder);
			warningLabel = new JLabel("");
			warningLabel.setForeground(Color.RED);
			add(warningLabel);
			setOpaque(false);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Login"))
			{
				char[] pwd = password.getPassword();
				boolean loginExists = data.isAccountInFile(username.getText(),pwd,true);
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