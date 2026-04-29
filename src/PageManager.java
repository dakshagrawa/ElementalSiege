import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PageManager extends JPanel
{
	private CardLayout card;
	private GameData data;
	public Font titleFont;
	public Font normalFont; 
	public Font normalBoldFont;

	public PageManager()
	{
		titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 26);
		normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		normalBoldFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		
		data = new GameData(this);

		card = new CardLayout();
		setLayout(card);

		add(new TitlePage(this), "Title");
		add(new SignIn(this),"Continue");
		add(new SignUp(this),"New Game");
		add(new Settings(this),"Settings");
		add(new PickAvatar(this), "Pick Avatar");
		//add(new characterSelect(this),"Character Selection");

		card.show(this,"Title");
	}

	public void changeCard(String cardName)
	{
		card.show(this,cardName);
		repaint();
	}

	public GameData getGameData()
	{
		return data;
	}

	public void addHoverEffect(JButton btn, Color onHoverFgColor, Color onHoverBgColor)
	{
		FormatButtonOnHover listener = new FormatButtonOnHover(btn, onHoverBgColor, onHoverFgColor);
		btn.addMouseListener(listener);
	}
	static class FormatButtonOnHover implements MouseListener
	{
		private Color hoverBackgroundColor;
		private Color normalBackgroundColor;
		private Color hoverForegroundColor;
		private Color normalForegroundColor;
		private boolean normalOpacity;
		private JButton button;

		public FormatButtonOnHover(JButton btn, Color hoverBgColor, Color hoverFgColor)
		{
			button = btn;
			normalBackgroundColor = button.getBackground();
			normalForegroundColor = button.getForeground();
			normalOpacity = button.isOpaque();
			hoverBackgroundColor = hoverBgColor;
			hoverForegroundColor = hoverFgColor;
		}

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) 
		{
			button.setBackground(hoverBackgroundColor);
			button.setForeground(hoverForegroundColor);
			button.setOpaque(true);
		}

		public void mouseExited(MouseEvent e) 
		{
			button.setBackground(normalBackgroundColor);
			button.setForeground(normalForegroundColor);
			button.setOpaque(normalOpacity);
			button.repaint();
		}
	}

	public class Button1 extends JButton
	{
		private Color fgColor;
		private Color bgColor;
		private boolean isOpaque;
		private boolean isBorderPainted;

		public Button1(String btnName, Color foregroundColor, boolean isBgOpaque)
		{
			super(btnName);
			fgColor = foregroundColor;
			isOpaque = isBgOpaque;
			bgColor = Color.WHITE;
			if(!isBgOpaque) 
				isBorderPainted=false;
			else
				isBorderPainted=true;
			makeButton();
		}

		public Button1(String btnName, Color foregroundColor, Color backgroundColor)
		{
			super(btnName);
			fgColor = foregroundColor;
			bgColor = backgroundColor;
			isOpaque = true;
			isBorderPainted=true;
			makeButton();
		}

		public Button1(String btnName, Color foregroundColor)
		{
			super(btnName);
			fgColor = foregroundColor;
			bgColor = Color.WHITE;
			isOpaque = false;
			isBorderPainted = false;
			makeButton();
		}

		public Button1(String btnName)
		{
			super(btnName);
			fgColor = Color.BLACK;
			bgColor = Color.WHITE;
			isOpaque = false;
			isBorderPainted = false;
			makeButton();
		}

		public void makeButton()
		{
			setBackground(Color.WHITE);
			setFont(titleFont);
			setForeground(fgColor);
			setBackground(bgColor);
			setFocusPainted(false);
			setOpaque(isOpaque);
			setBorderPainted(isBorderPainted);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} 
	}

	public static class Functions
	{
		public static Image getImage(String imgName)
		{
			Image picture = null;
			String pictName = "../storedData/images/"+imgName;

			try
			{
				picture = ImageIO.read(new File(pictName));
			}
			catch(IOException e)
			{
				System.err.println("\n" + pictName + " can't be found. \n");
				e.printStackTrace();        
			}

			return picture;
		}
	}
}
