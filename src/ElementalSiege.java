//Daksh Agrawal & Dhikshitha Aiely
//Period 7

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame; 

public class ElementalSiege extends JFrame implements WindowListener
{
	private PageManager pm;

    public ElementalSiege()
	{
        super("Elemental Siege");
		Sound audio = new Sound();

		pm = new PageManager(this,audio);
		
        setSize(1500, 800);
        setLocation(10, 0);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setContentPane(pm);
        setVisible(true);
		addWindowListener(this);

		audio.playNewBackground("../storedData/Audio/bg.wav");
    }

    public static void main(String[] args) 
	{
        ElementalSiege game = new ElementalSiege();
    }

	public void windowClosing(WindowEvent e) 
	{
		if (!pm.currentCardName.equals("Close")) 
		{
			// Save mid-run progress before showing close panel
			if (pm.currentCardName.equals("Battle")) 
			{
				BattlePanel battlePanel = pm.battlePanel;
				if (battlePanel != null) 
				{
					int currentHealth = battlePanel.getUserHealth();
					int currentLevel = battlePanel.getLevel();
					pm.getGameData().saveUserProgress(currentHealth, currentLevel);
				}
			}
			pm.changePanelCard("Close");
		}
	}
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}
