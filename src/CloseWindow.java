import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CloseWindow extends JPanel //when user tries to close the window, this panel will pop up to ask the user if they're sure
//about the closign the game -- if they say no, it will let them get back to whatever panel they're on and if they say yes, game will close
{
	private PageManager pm;
	private GameData data;

	public CloseWindow(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new GridBagLayout());
		setBackground(new Color(0, 0, 0, 160));

		JPanel card = new JPanel(new BorderLayout(0, 20));
		card.setBackground(new Color(22, 22, 40, 240));
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1), BorderFactory.createEmptyBorder(30, 40, 30, 40)));
		//border outline the pop up in black to make it clear to user
		card.setPreferredSize(new Dimension(520, 200));

		card.add(closeLabel(), BorderLayout.NORTH);
		card.add(new ButtonGrid(), BorderLayout.CENTER);

		add(card);
	}

	public JLabel closeLabel()
	{
		JLabel label = new JLabel("Exit without saving?", SwingConstants.CENTER);
		label.setFont(pm.titleFont);
		label.setForeground(new Color(255, 200, 100)); //pale yellow for background of pop up
		return label;
	}

	class ButtonGrid extends JPanel implements ActionListener
	{
		public ButtonGrid()
		{
			setLayout(new GridLayout(1, 2, 20, 0));
			setOpaque(false);

			JButton no = pm.new Button1("Cancel", Color.WHITE, new Color(80, 80, 100));
			no.setPreferredSize(new Dimension(200, 44));
			pm.addHoverEffect(no, Color.LIGHT_GRAY, no.getBackground()); //hoverEffect method from pageManager highlights buttton when user hovers
			no.addActionListener(this);

			JButton yes = pm.new Button1("Exit", Color.WHITE, new Color(160, 60, 60));
			yes.setPreferredSize(new Dimension(200, 44));
			pm.addHoverEffect(yes, Color.LIGHT_GRAY, yes.getBackground());
			yes.addActionListener(this);

			add(no);
			add(yes);
		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();

			if(command.equals("Exit"))
			{
				pm.frame.dispose();
				System.exit(0);
			}
			else if(command.equals("Cancel"))
			{
				pm.changePanelCardNoHistory(pm.lastCardName);
			}
		}
	}
}
