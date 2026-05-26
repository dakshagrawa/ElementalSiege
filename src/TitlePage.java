import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class TitlePage extends JPanel implements ActionListener
{
	private GameData data;
	private PageManager pm;
	
	public TitlePage(PageManager pageMngr)
	{
		data = pageMngr.getGameData();
		pm = pageMngr;
		setLayout(null);
		StartButtons();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Image picture = PageManager.Functions.getImage("HomeScreenBackground.png");
		super.paintComponent(g);
		if (picture != null) 
		{
			g.drawImage(picture, 0, 0, getWidth(),getHeight(),this);
		}
		else
		{
			setBackground(Color.LIGHT_GRAY);
		}
	}

	public void StartButtons() ///Login and sign-in button on title page
	{
		JButton signIn;
		JButton signUp;

		signIn = pm.new Button1("Continue", new Color(21,49,94));
		signUp = pm.new Button1("New Game", new Color(23,55,23));

		signIn.setBounds(525, 682, 200, 50);
		signUp.setBounds(770, 682, 200, 50);

		add(signUp);
		add(signIn);

		pm.addHoverEffect(signIn, Color.LIGHT_GRAY, signIn.getForeground());
		pm.addHoverEffect(signUp, Color.LIGHT_GRAY, signUp.getForeground());

		signIn.addActionListener(this);
		signUp.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		pm.changePanelCard(e.getActionCommand());
	}

}
