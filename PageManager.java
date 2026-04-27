import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PageManager extends JPanel
{
	private CardLayout card;
	private GameData data;
	public Font titleFont;
	public Font normalFont; 

	public PageManager()
	{
		titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 24);
		normalFont = new Font(Font.DIALOG, Font.PLAIN, 14);
		
		data = new GameData();

		card = new CardLayout();
		setLayout(card);

		add(new TitlePage(this), "Title");
		add(new SignIn(this),"Continue");
		add(new SignUp(this),"New Game");
		add(new Settings(this),"Settings");

		card.show(this,"Title");
	}

	public void changeCard(String cardName)
	{
		card.show(this,cardName);
	}

	public GameData getGameData()
	{
		return data;
	}

	public MouseListener buttonHoverEffect(JButton btn, Color BgCol, Color fgCol)
	{
		FormatOnHover coh = new FormatOnHover(btn, BgCol, fgCol);
		return coh;
	} 
	static class FormatOnHover implements MouseListener
	{
		private Color hoverBackgroundColor;
		private Color normalBackgroundColor;
		private Color hoverForegroundColor;
		private Color normalForegroundColor;
		private JButton button;

		public FormatOnHover(JButton btn, Color bgColor, Color fgColor)
		{
			button = btn;
			normalBackgroundColor = button.getBackground();
			normalForegroundColor = button.getForeground();
			hoverBackgroundColor = bgColor;
			hoverForegroundColor = fgColor;
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
			button.setOpaque(false);
		}
	}
}
