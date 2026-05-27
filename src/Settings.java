import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings extends JPanel implements ActionListener, ChangeListener
{
	private GameData data;
	private PageManager pm;
	private JSlider volumeSlider;

	public Settings(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();
		setLayout(new BorderLayout());

		JPanel backBtnWrapper = new JPanel();
		JButton backBtn = pm.new Button1("<- Back", Color.GRAY);
		backBtn.setPreferredSize(new Dimension(100, 40));
		backBtn.addActionListener(this);
		add(backBtn, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 20));
		centerPanel.setOpaque(false);

		volumeSlider = new JSlider(0, 100, 75);
		volumeSlider.setOrientation(JSlider.HORIZONTAL);
		volumeSlider.setMajorTickSpacing(25);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.addChangeListener(this);
		volumeSlider.setValue((int)(pm.sound.currentVolume*100));
		centerPanel.add(volumeSlider);

		JButton exit = new JButton("Save & Exit");
		exit.addActionListener(this);
		centerPanel.add(exit);

		JButton quit = new JButton("Quit");
		quit.addActionListener(this);
		centerPanel.add(quit);

		add(centerPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command.equals("<- Back"))
		{
			//Use the same logic as ClosePanel - go to the previous panel without relying on lastCardName which might be incorrect when coming from ClosePanel
			if(pm.lastCardName != null && !pm.lastCardName.equals("Close")) {
				pm.changePanelCardNoHistory(pm.lastCardName);
			} else {
				//Goes back to Title page if lastCardName is not valid
				pm.changePanelCard("Title");
			}
		}
		else if(command.equals("Save & Exit"))
		{
		    // Save current battle progress if in battle, otherwise save saved progress
		    if (pm.lastCardName != null && pm.lastCardName.equals("Battle") && pm.battlePanel != null) {
		        int currentHealth = pm.battlePanel.getUserHealth();
		        int currentLevel = pm.battlePanel.getLevel();
		        data.saveUserProgress(currentHealth, currentLevel);
		    } else {
		        // Fallback to saving previously saved progress (current behavior)
		        data.saveUserProgress(data.getSavedHealth(), data.getSavedLevel());
		    }
		    if(pm.lastCardName != null && !pm.lastCardName.equals("Close")) 
		    {
		        pm.changePanelCardNoHistory(pm.lastCardName);
		    } 
		    else 
		    {
		        pm.changePanelCard("Title");
		    }
		}
		else
		{
			pm.frame.dispose();
			System.exit(0);
		}
	}

	public void stateChanged(ChangeEvent evt)
	{
		float targetVolume = (float)volumeSlider.getValue() / 100.0f;
		pm.sound.setVolume(targetVolume);
	}
}