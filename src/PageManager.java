import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PageManager extends JPanel
{
	private CardLayout card;
	private GameData data;
	public String lastCardName;
	public String currentCardName;
	public Font titleFont;
	public Font normalFont; 
	public Font normalBoldFont;
	public ElementalSiege frame;
	public Sound sound;

	public PageManager(ElementalSiege elemntlSiege_JFrame, Sound audio)
	{
		sound = audio;
		frame = elemntlSiege_JFrame;
		titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 26);
		normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		normalBoldFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		
		data = new GameData(this);

		card = new CardLayout();
		setLayout(card);

		add(new CloseWindow(this),"Close");
		add(new TitlePage(this), "Title");
		add(new SignIn(this),"Continue");
		add(new SignUp(this),"New Game");
		add(new Settings(this),"Settings");
		add(new PickAvatar(this), "Pick Avatar");
		add(new GetNewCharacterCards(this, 1),"Get New Cards 1");
		add(new GetNewCharacterCards(this, 2),"Get New Cards 2");
		add(new GetNewCharacterCards(this, 3),"Get New Cards 3");
		add(new GetNewCharacterCards(this, "Questions"),"Get New Cards 4"); //no page number entered, bcs it is the last one, nextPanelName entered
		add(new QuestionPanel(this),"Questions");
		add(new BattlePanel(this),"Battle");

		lastCardName = "Title";
		currentCardName = "Title";
		changePanelCard(currentCardName);
	}

	public void changePanelCard(String cardName)
	{
		if(!cardName.equals("Close"))
		{
			//card.show(this,lastCardName);
			lastCardName = cardName;
		}
		currentCardName = cardName;
		card.show(this,cardName);
		revalidate(); //TODO: add citation
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

	/* This class has different functions (tools) that helps 
		use some chunks of code that is used repeatedly. */
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
				System.out.println(e);      
				System.err.println("\n" + pictName + " can't be found. \n"); 
			}

			return picture;
		}

		public static BufferedImage getBufferedImage(String imgName)
		{
			BufferedImage picture = null;
			String pictName = "../storedData/images/"+imgName;

			try
			{
				picture = ImageIO.read(new File(pictName));
			}

			catch(IOException e)
			{
				System.out.println(e);      
				System.err.println("\n" + pictName + " can't be found. \n"); 
			}

			return picture;
		}

		// Source - https://stackoverflow.com/a/13605411
		// Posted by Sri Harsha Chilakapati, modified by community. See post 'Timeline' for change history
		// Retrieved 2026-05-10, License - CC BY-SA 3.0
		public static BufferedImage characterToBufferedImage(GameData.CharacterImage imageClass) //TODO: cite
		{
			if (imageClass.image instanceof BufferedImage)
			{
				return (BufferedImage) imageClass.image;
			}

			// Create a buffered image with transparency
			BufferedImage buffered = new BufferedImage(imageClass.width, imageClass.height, BufferedImage.TYPE_INT_ARGB);

			// Draw the image on to the buffered image
			Graphics2D bGr = buffered.createGraphics();
			bGr.drawImage(imageClass.image, 0, 0, imageClass.width, imageClass.height, imageClass.x1, imageClass.y1, imageClass.x2, imageClass.y2, null);
			bGr.dispose();

			// Return the buffered image
			return buffered;
		}

	}
}
