import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class PageManager extends JLayeredPane
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
	public ExplanationPanel explanationPanel;
	public ResumeOrNewGame resumeOrNewGame;
    private JButton settingsBtn;

    private JPanel mainPanel;

	public PageManager(ElementalSiege elemntlSiege_JFrame, Sound audio)
	{
		sound = audio;
		frame = elemntlSiege_JFrame;
		titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 26);
		normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		normalBoldFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        setLayout(null);

		data = new GameData(this);
		card = new CardLayout();
        mainPanel = new JPanel (card);
        add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        
		mainPanel.add(new CloseWindow(this),"Close"); //opens when user tries to close game
		mainPanel.add(new TitlePage(this), "Title"); //home screen/title page
		mainPanel.add(new SignIn(this),"Continue"); //opens when user clicks continue button a.k.a log in button
		mainPanel.add(new SignUp(this),"New Game"); //opens when user clicks new game button a.k.a sign up button
		mainPanel.add(new Settings(this),"Settings"); //opens the settings screen
		mainPanel.add(new PickAvatar(this), "Pick Avatar"); //panel that allows user to pick their avatar
		mainPanel.add(new GetNewCharacterCards(this, 1),"Get New Cards 1"); //panels that allow user to choose their 4 cards
		mainPanel.add(new GetNewCharacterCards(this, 2),"Get New Cards 2");
		mainPanel.add(new GetNewCharacterCards(this, 3),"Get New Cards 3");
		mainPanel.add(new GetNewCharacterCards(this, "Questions"),"Get New Cards 4"); //no page number entered, bcs it is the last one, nextPanelName entered
		BattlePanel bp = new BattlePanel(this, false);
		mainPanel.add(bp,"Battle"); //"battle" scene after each question
		mainPanel.add(new QuestionPanel(this, bp),"Questions"); //question panel
		mainPanel.add(new HomePanel(this, bp), "newLevel"); //panel between each round -- open when user defeats enemy

		mainPanel.add(new EndGamePanel(this, false), "Game Over");    // user health hit 0 -- they lost
		mainPanel.add(new EndGamePanel(this, true),  "You Win");      // user beats all 10 levels -- they win yay!

		resumeOrNewGame = new ResumeOrNewGame(this);
		mainPanel.add(resumeOrNewGame, "Resume Or New Game"); //if user logs in

		mainPanel.add(new TutorialPanel(this, "Get New Cards 1"), "Tutorial"); //cards for each level
		mainPanel.add(new LevelPanel(this, 1),  "Level 1");
		mainPanel.add(new LevelPanel(this, 2),  "Level 2");
		mainPanel.add(new LevelPanel(this, 3),  "Level 3");
		mainPanel.add(new LevelPanel(this, 4),  "Level 4");
		mainPanel.add(new LevelPanel(this, 5),  "Level 5");
		mainPanel.add(new LevelPanel(this, 6),  "Level 6");
		mainPanel.add(new LevelPanel(this, 7),  "Level 7");
		mainPanel.add(new LevelPanel(this, 8),  "Level 8");
		mainPanel.add(new LevelPanel(this, 9),  "Level 9");
		mainPanel.add(new LevelPanel(this, 10), "Level 10");

		explanationPanel = new ExplanationPanel(this, "Battle"); 
		mainPanel.add(explanationPanel, "Explanation"); //opened when user gets a question wrong

        addSettingsButton();

		lastCardName = "Title";
		currentCardName = "Title";
		changePanelCard(currentCardName);
	}

	public void changePanelCard(String cardName)
	{
		lastCardName = currentCardName;
		currentCardName = cardName;
		card.show(mainPanel,cardName);

        if (settingsBtn != null) {
            settingsBtn.setVisible(!cardName.equalsIgnoreCase("Settings") && !cardName.equalsIgnoreCase("Close"));
        }
        
		revalidate();
		repaint();
	}

	public void changePanelCardNoHistory(String cardName)
	{
		currentCardName = cardName;
		card.show(mainPanel,cardName);
        if (settingsBtn != null) {
            settingsBtn.setVisible(!cardName.equalsIgnoreCase("Settings") && !cardName.equalsIgnoreCase("Close"));
        }
		revalidate();
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

	/* This class has different functions that helps
		use some chunks of code that are used repeatedly in the game. */
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
			catch(NullPointerException | IOException e)
			{
				System.out.println(e.getMessage());
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

			catch(NullPointerException | IOException e)
			{
				System.out.println(e);
				System.err.println("\n" + pictName + " can't be found. \n");
			}

			return picture;
		}

		// Source - https://stackoverflow.com/a/13605411
		// Posted by Sri Harsha Chilakapati, modified by community. See post 'Timeline' for change history
		// Retrieved 2026-05-10, License - CC BY-SA 3.0
		public static BufferedImage characterToBufferedImage(GameData.CharacterImage imageClass)
		{
			if (imageClass.image instanceof BufferedImage)
			{
				return (BufferedImage) imageClass.image;
			}

			//Creates a buffered image with transparency
			BufferedImage buffered = new BufferedImage(imageClass.width, imageClass.height, BufferedImage.TYPE_INT_ARGB);

			//Draws the image on to the buffered image
			Graphics2D bGr = buffered.createGraphics();
			bGr.drawImage(imageClass.image, 0, 0, imageClass.width, imageClass.height, imageClass.x1, imageClass.y1, imageClass.x2, imageClass.y2, null);
			bGr.dispose();

			//Return the buffered image
			return buffered;
		}
	}

    public void addSettingsButton()
    {
        Image icon = PageManager.Functions.getImage("settings.png");
        if(icon==null) return;
        ImageIcon scaledSettingsIcon = new ImageIcon(icon.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        settingsBtn = new JButton(scaledSettingsIcon);
        settingsBtn.setBounds(frame.getWidth() - 60, 10, 50, 50); //top-left corner
        settingsBtn.setContentAreaFilled(false);
        settingsBtn.setBorderPainted(false);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        settingsBtn.setActionCommand("Settings");
        settingsBtn.addActionListener(new SettingsButtonListener());

        addHoverEffect(settingsBtn, Color.LIGHT_GRAY, null);
        add(settingsBtn, JLayeredPane.PALETTE_LAYER);
    }

    class SettingsButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            changePanelCard(e.getActionCommand());
        }
    }

        public void setBounds(int x, int y, int width, int height) 
        {
            super.setBounds(x, y, width, height);
            if (mainPanel != null) {
                mainPanel.setBounds(0, 0, width, height);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
    
            //Keeps the button pinned to the right margin dynamically
            if (settingsBtn != null) {
                //(width - 70) accounts for button size (50) and adds 20px gap from the edge
                settingsBtn.setBounds(width - 70, 10, 50, 50);
            }
        }


}
