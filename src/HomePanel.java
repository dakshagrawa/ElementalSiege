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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel implements ActionListener
{
	private PageManager pm;
	private BattlePanel battle;

	public HomePanel(PageManager pageMngr, BattlePanel bp)
	{
		pm = pageMngr;
		battle = bp;

		setLayout(new BorderLayout());
		setBackground(new Color(30, 30, 50));

		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);

		JPanel card = new JPanel(new GridLayout(2, 1, 0, 16));
		card.setOpaque(false);
		card.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

		JLabel statusLabel = new JLabel("Battle Ended!", SwingConstants.CENTER);
		statusLabel.setForeground(Color.WHITE);
		statusLabel.setFont(pm.titleFont);
		card.add(statusLabel);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);

		JButton nextRoundBtn = pm.new Button1("Next Round", Color.WHITE, new Color(60, 120, 60));
		nextRoundBtn.setPreferredSize(new Dimension(180, 44));
		pm.addHoverEffect(nextRoundBtn, Color.LIGHT_GRAY, nextRoundBtn.getBackground());
		nextRoundBtn.addActionListener(this);

		JButton settingsBtn = pm.new Button1("Settings", new Color(200, 200, 200), new Color(80, 80, 100));
		settingsBtn.setPreferredSize(new Dimension(140, 44));
		pm.addHoverEffect(settingsBtn, Color.LIGHT_GRAY, settingsBtn.getBackground());
		settingsBtn.addActionListener(this);

		buttonPanel.add(nextRoundBtn);
		buttonPanel.add(settingsBtn);
		card.add(buttonPanel);

		centerPanel.add(card);
		add(centerPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if(cmd.equals("Next Round"))
		{
			battle.level++;
			battle.userHealth = 100;
			battle.setEnemyHealth((int)(battle.level * 1.7) + 17); // Full heal enemy for new level
			pm.getGameData().saveUserProgress(100, battle.level);
			battle.drawNewCards();
			pm.changePanelCard("Battle");
		}
		else if(cmd.equals("Settings"))
		{
			pm.changePanelCard("Settings");
		}
	}
}
