import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CloseWindow extends JPanel
{
	private PageManager pm;
	private GameData data;

	public CloseWindow(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new FlowLayout(FlowLayout.CENTER));

		setBackground(new Color(211, 211, 211, 100));
		add(new WindowOnClosing());
	}

	class WindowOnClosing extends JPanel
	{
		public WindowOnClosing()
		{
			setBackground(new Color(215, 200, 169));

			Border etch = BorderFactory.createEtchedBorder(new Color(52, 53, 55), new Color(156, 157, 156));
			Border thickEtch = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 53, 55), 3), etch);
			setBorder(thickEtch);

			setLayout(new BorderLayout());

			add(closeLabel(),BorderLayout.NORTH);
			add(new ButtonGrid(),BorderLayout.SOUTH);
		}

		public JLabel closeLabel()
		{
			JLabel label = new JLabel("Are you sure you want to leave without saving your game data?");
			label.setFont(pm.titleFont);
			//label.setForeground(Color.RED);
			return label;
		}

		class ButtonGrid extends JPanel implements ActionListener
		{
			public ButtonGrid()
			{
				setLayout(new GridLayout(1,2));

				setOpaque(false);

				add(noButton());
				add(yesButton());
			}

			public JButton noButton()
			{
				JButton no = pm.new Button1("No", Color.RED);
				no.setPreferredSize(new Dimension(200,50));
				pm.addHoverEffect(no, Color.LIGHT_GRAY, no.getForeground());
				no.addActionListener(this);
				return no;
			}

			public JButton yesButton()
			{
				JButton yes = pm.new Button1("Yes", Color.GREEN);
				yes.setPreferredSize(new Dimension(200,50));
				pm.addHoverEffect(yes, Color.LIGHT_GRAY, yes.getForeground());
				yes.addActionListener(this);
				return yes;
			}

			
			public void actionPerformed(ActionEvent e)
			{
				String command = e.getActionCommand();

				if(command.equals("Yes"))
				{
					pm.frame.dispose();
					System.exit(0);
				}
				else if(command.equals("No"))
				{
					pm.changePanelCard(pm.lastCardName);
				}
				
			}
		}
	}
}
