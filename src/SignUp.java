import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

		setLayout(new GridBagLayout());
		setBackground(new Color(30, 30, 50));

		JPanel card = new JPanel(new BorderLayout(0, 16));
		card.setBackground(new Color(22, 22, 40, 220));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
			BorderFactory.createEmptyBorder(36, 44, 36, 44)
		));
		card.setPreferredSize(new Dimension(500, 360));

		card.add(new SignUpLabel(), BorderLayout.NORTH);
		card.add(new CenterPanel(), BorderLayout.CENTER);
		card.add(sp, BorderLayout.SOUTH);

		add(card);
	}

	class SignUpLabel extends JPanel
	{
		public SignUpLabel()
		{
			setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

			JLabel title = new JLabel("Sign Up");
			title.setFont(pm.titleFont);
			title.setForeground(Color.WHITE);
			add(title);
		}
	}

	class CenterPanel extends JPanel
	{
		public CenterPanel()
		{
			setLayout(new GridLayout(3, 1, 0, 12));
			setOpaque(false);

			username = new JTextField();
			password = new JPasswordField();
			passwordCheck = new JPasswordField();

			username.setFont(pm.normalFont);
			password.setFont(pm.normalFont);
			passwordCheck.setFont(pm.normalFont);

			username.addActionListener(new NameListener());
			password.addActionListener(new PwdListener());
			passwordCheck.addActionListener(new PwdCheckListener());

			JPanel usernamePanel = new JPanel(new GridLayout(1, 2, 10, 0));
			JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
			usernameLabel.setFont(pm.normalBoldFont);
			usernameLabel.setForeground(new Color(180, 200, 255));
			usernamePanel.add(usernameLabel);
			usernamePanel.add(username);
			usernamePanel.setOpaque(false);
			add(usernamePanel);

			JPanel passwordPanel = new JPanel(new GridLayout(1, 2, 10, 0));
			JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
			passwordLabel.setFont(pm.normalBoldFont);
			passwordLabel.setForeground(new Color(180, 200, 255));
			passwordPanel.add(passwordLabel);
			passwordPanel.add(password);
			passwordPanel.setOpaque(false);
			add(passwordPanel);

			JPanel passwordCheckPanel = new JPanel(new GridLayout(1, 2, 10, 0));
			JLabel passwordCheckLabel = new JLabel("Confirm:", SwingConstants.RIGHT);
			passwordCheckLabel.setFont(pm.normalBoldFont);
			passwordCheckLabel.setForeground(new Color(180, 200, 255));
			passwordCheckPanel.add(passwordCheckLabel);
			passwordCheckPanel.add(passwordCheck);
			passwordCheckPanel.setOpaque(false);
			add(passwordCheckPanel);

			username.requestFocusInWindow();
		}

		class NameListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				password.requestFocusInWindow();
			}
		}

		class PwdListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				passwordCheck.requestFocusInWindow();
			}
		}

		class PwdCheckListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				signUp.doClick();
			}
		}
	}

	class SouthPanel extends JPanel implements ActionListener
	{
		private JLabel warningLabel;

		public SouthPanel()
		{
			setLayout(new GridLayout(2, 1, 0, 8));
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

			JPanel btnRow = new JPanel(new GridLayout(1, 2, 20, 0));
			btnRow.setOpaque(false);

			signUp = pm.new Button1("Sign Up", Color.WHITE, new Color(60, 120, 60));
			cancel = pm.new Button1("Cancel", Color.WHITE, new Color(120, 60, 60));

			signUp.setFont(pm.normalBoldFont);
			cancel.setFont(pm.normalBoldFont);

			pm.addHoverEffect(signUp, Color.LIGHT_GRAY, signUp.getBackground());
			pm.addHoverEffect(cancel, Color.LIGHT_GRAY, cancel.getBackground());

			signUp.addActionListener(this);
			cancel.addActionListener(this);

			btnRow.add(cancel);
			btnRow.add(signUp);
			add(btnRow);

			warningLabel = new JLabel("", SwingConstants.CENTER);
			warningLabel.setFont(pm.normalBoldFont);
			warningLabel.setForeground(new Color(255, 100, 100));
			add(warningLabel);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Sign Up"))
			{
				char[] pwd = password.getPassword();
				char[] pwdCheck = passwordCheck.getPassword();
				boolean pwdCorrect = false;
				if(Arrays.equals(pwdCheck, pwd))
					pwdCorrect = true;
				boolean isBlank = true;
				if(pwd.length > 0)
					isBlank = false;
				boolean signUpSuccessful = data.putAccountInFile(username.getText(), pwd, pwdCorrect);
				Arrays.fill(pwd, '*');
				Arrays.fill(pwdCheck, '*');
				if(signUpSuccessful && !isBlank)
				{
					warningLabel.setForeground(new Color(100, 220, 100));
					warningLabel.setText("Success!");
					pm.changePanelCard("Pick Avatar");
					pm.repaint();
				}
				else if(!pwdCorrect)
				{
					warningLabel.setForeground(new Color(255, 100, 100));
					warningLabel.setText("Passwords do not match.");
					pm.repaint();
				}
				else
				{
					warningLabel.setForeground(new Color(255, 100, 100));
					warningLabel.setText("Username already exists.");
					pm.repaint();
				}
			}
			else if (command.equals("Cancel"))
			{
				username.setText("");
				password.setText("");
				passwordCheck.setText("");
				warningLabel.setText("");
				pm.changePanelCard("Title");
			}
		}
	}
}
