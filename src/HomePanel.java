import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HomePanel extends JPanel implements ActionListener
{
	private PageManager pm;
	private BattlePanel battle;

	public HomePanel(PageManager pageMngr, BattlePanel bp)
	{
		pm = pageMngr;
		battle = bp;

		setLayout(new BorderLayout());
		setBackground(new Color(40, 40, 50));

		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new GridBagLayout());

		JLabel statusLabel = new JLabel("Battle Ended!", SwingConstants.CENTER);
		statusLabel.setForeground(Color.WHITE);
		statusLabel.setFont(pm.titleFont);
		centerPanel.add(statusLabel);
		add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		JButton nextRoundBtn = pm.new Button1("Next Round", Color.GREEN);
		nextRoundBtn.addActionListener(this);

		JButton settingsBtn = pm.new Button1("Settings", Color.LIGHT_GRAY);
		settingsBtn.addActionListener(this);

		buttonPanel.add(nextRoundBtn);
		buttonPanel.add(settingsBtn);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if(cmd.equals("Next Round"))
		{
			battle.level++;
			battle.userHealth = 100; //Reset user health
			//Enemy health is recalculated in drawNewCards or via a method we can add
			//Let's add a reset method to BattlePanel or just call drawNewCards which sets enemyHealth
			battle.drawNewCards();
			pm.changePanelCard("Battle");
		}
		else if(cmd.equals("Settings"))
		{
			pm.changePanelCard("Settings");
		}
	}
}