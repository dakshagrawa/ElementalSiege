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

		setLayout(new GridBagLayout());
		setBackground(new Color(30, 30, 50));

		JPanel card = new JPanel(new BorderLayout(0, 16));
		card.setBackground(new Color(22, 22, 40, 220));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
			BorderFactory.createEmptyBorder(36, 44, 36, 44)
		));
		card.setPreferredSize(new Dimension(480, 300));

		card.add(new SignInLabel(), BorderLayout.NORTH);
		card.add(new CenterPanel(), BorderLayout.CENTER);
		card.add(sp, BorderLayout.SOUTH);

		add(card);
	}

	class SignInLabel extends JPanel
	{
		public SignInLabel()
		{
			setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

			JLabel title = new JLabel("Sign In");
			title.setFont(pm.titleFont);
			title.setForeground(Color.WHITE);
			add(title);
		}
	}

	class CenterPanel extends JPanel
	{
		public CenterPanel()
		{
			setLayout(new GridLayout(2, 1, 0, 12));
			setOpaque(false);

			username = new JTextField();
			password = new JPasswordField();

			username.setFont(pm.normalFont);
			password.setFont(pm.normalFont);

			username.addActionListener(new NameListener());
			password.addActionListener(new PwdListener());

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
				login.doClick();
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

			login = pm.new Button1("Login", Color.WHITE, new Color(60, 120, 60));
			cancel = pm.new Button1("Cancel", Color.WHITE, new Color(120, 60, 60));

			login.setFont(pm.normalBoldFont);
			cancel.setFont(pm.normalBoldFont);

			pm.addHoverEffect(login, Color.LIGHT_GRAY, login.getBackground());
			pm.addHoverEffect(cancel, Color.LIGHT_GRAY, cancel.getBackground());

			login.addActionListener(this);
			cancel.addActionListener(this);

			btnRow.add(cancel);
			btnRow.add(login);
			add(btnRow);

			warningLabel = new JLabel("", SwingConstants.CENTER);
			warningLabel.setFont(pm.normalBoldFont);
			warningLabel.setForeground(new Color(255, 100, 100));
			add(warningLabel);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Login"))
			{
				char[] pwd = password.getPassword();
				boolean loginExists = data.isAccountInFile(username.getText(), pwd, true);
				Arrays.fill(pwd, '*');
				if(loginExists)
				{
					warningLabel.setForeground(new Color(100, 220, 100));
					warningLabel.setText("Success!");
					pm.resumeOrNewGame.refresh();
					pm.changePanelCard("Resume Or New Game");
				}
				else
				{
					warningLabel.setForeground(new Color(255, 100, 100));
					warningLabel.setText("Username or password is incorrect.");
					pm.repaint();
				}
			}
			else if (command.equals("Cancel"))
			{
				username.setText("");
				password.setText("");
				warningLabel.setText("");
				pm.changePanelCard("Title");
			}
		}
	}
}
