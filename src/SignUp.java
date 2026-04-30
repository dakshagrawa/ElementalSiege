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
			add(signUp());
			setOpaque(false);
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
			setOpaque(false);

			username = new JTextField();
			password = new JPasswordField();
			passwordCheck = new JPasswordField();
			
			username.setEditable(true);
			password.setEditable(true);
			passwordCheck.setEditable(true);

			username.addActionListener(new nameListener());
			password.addActionListener(new pwdListener());
			passwordCheck.addActionListener(new pwdCheckListener());

			JPanel usernamePanel = new JPanel(new GridLayout(1,2));
			JLabel usernameLabel = new JLabel("Username:");
			usernameLabel.setFont(pm.normalFont);
			usernamePanel.add(usernameLabel);
			usernamePanel.add(username);
			usernamePanel.setOpaque(false);
			add(usernamePanel);

			JPanel passwordPanel = new JPanel(new GridLayout(1,2));
			JLabel passwordLabel = new JLabel("Password:");
			passwordLabel.setFont(pm.normalFont);
			passwordPanel.add(passwordLabel);
			passwordPanel.add(password);
			passwordPanel.setOpaque(false);
			add(passwordPanel);

			JPanel passwordCheckPanel = new JPanel(new GridLayout(1,2));
			JLabel passwordCheckLabel = new JLabel("Confirm Password: ");
			passwordCheckLabel.setFont(pm.normalFont);
			passwordCheckPanel.add(passwordCheckLabel);
			passwordCheckPanel.add(passwordCheck);
			passwordCheckPanel.setOpaque(false);
			add(passwordCheckPanel);

			username.requestFocusInWindow();
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

		public class pwdCheckListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				signUp.doClick();
			}
		}
	}

	public class SouthPanel extends JPanel implements ActionListener
	{
		private JLabel warningLabel;

		public SouthPanel()
		{
			setLayout(new GridLayout(2,2,100,100));

			signUp = pm.new Button1("Sign Up", getBackground(),new Color(70, 130, 180));
			cancel = pm.new Button1("Cancel", getBackground(),new Color(70, 130, 180));

			signUp.setFont(pm.normalBoldFont);
			cancel.setFont(pm.normalBoldFont);

			pm.addHoverEffect(signUp, new Color(20, 35, 20), getBackground());
			pm.addHoverEffect(cancel, new Color(20, 35, 20), getBackground());
			
			signUp.addActionListener(this);
			cancel.addActionListener(this);

			add(cancel);
			add(signUp);
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

			if(command.equals("Sign Up"))
			{
				char[] pwd = password.getPassword();
				char[] pwdCheck = passwordCheck.getPassword();
				boolean pwdCorrect=false;
				if(Arrays.equals(pwdCheck,pwd))
					pwdCorrect=true;
				boolean signUpSuccessful = data.putAccountInFile(username.getText(),pwd, pwdCorrect);
				Arrays.fill(pwd, '*');
				Arrays.fill(pwdCheck, '*');
				if(signUpSuccessful)
				{
					warningLabel.setForeground(Color.GREEN);
					warningLabel.setText("Success!");
					pm.changeCard("Pick Avatar");
					pm.repaint();
				}
				else if(!pwdCorrect)
				{
					warningLabel.setText("Password does not match. Please try again.");
					pm.repaint();
				}
				else
				{
					warningLabel.setText("Username already exists. Please try again.");
					pm.repaint();
				}
			}
			else if (command.equals("Cancel"))
			{
				username.setText("");
				password.setText("");
				passwordCheck.setText("");
				warningLabel.setText("");
				pm.changeCard("Title");
			}
		}
	}
}