import java.awt.*;
 import java.awt.event.*;
 import java.io.*;
 import javax.imageio.ImageIO;
 import javax.swing.*;
public class TitlePage extends JPanel implements ActionListener
{
	private Image picture;
	private GameData data;
	private PageManager pm;
	
	public TitlePage(PageManager pageMngr)
	{
		data = pageMngr.getGameData();
		pm = pageMngr;
		setLayout(null);
		picture = null;
		StartButtons();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		getMyImage(); //need to input image  
		g.drawImage(picture, 0, 0, getWidth(),getHeight(),this);
	}

	public void getMyImage()
	{
		String picName = "storedData/images/HomeScreenBackground.png";
		try
		{
			picture = ImageIO.read(new File(picName));
		}

		catch(IOException e)
		{
			System.err.println("\n\n" + picName + " can't be found. \n\n");
			e.printStackTrace();        
		}
	}

	public void StartButtons() ///Login and sign-in button on title page
	{
		JButton signIn;
		JButton signUp;

		signIn = new JButton("Continue");
		signUp = new JButton("New Game");

		signIn.setBackground(Color.WHITE);
		signUp.setBackground(Color.WHITE);
		signIn.setFont(pm.titleFont);
		signUp.setFont(pm.titleFont);
		signIn.setForeground(new Color(21,49,94));
		signUp.setForeground(new Color(23,55,23));
		signUp.setFocusPainted(false);
		signIn.setFocusPainted(false);
		signIn.setOpaque(false);
		signUp.setOpaque(false);
		signUp.setBorderPainted(false);
		signIn.setBorderPainted(false);
		signIn.setBounds(525, 685, 200, 50);
		signUp.setBounds(770, 685, 200, 50);
		signUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		signIn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		add(signUp);
		add(signIn);

		signIn.addMouseListener(pm.buttonHoverEffect(signIn, new Color(34, 139, 34), signIn.getForeground()));
		signUp.addMouseListener(pm.buttonHoverEffect(signUp, new Color(34, 139, 34), signUp.getForeground()));

		signIn.addActionListener(this);
		signUp.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		pm.changeCard(e.getActionCommand());
	}
}
