import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

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
		JButton backBtn = pm.new Button1("<- Back", Color.LIGHT_GRAY);
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
			// Use the same logic as ClosePanel - go to the previous panel without relying on lastCardName
            // which might be incorrect when coming from ClosePanel
            if(pm.lastCardName != null && !pm.lastCardName.equals("Close")) {
                pm.changePanelCardNoHistory(pm.lastCardName);
            } else {
                // Fallback to Title page if lastCardName is not valid
                pm.changePanelCard("Title");
            }
		}
		else if(command.equals("Save & Exit"))
		{
			// Use the same logic as ClosePanel - go to the previous panel without relying on lastCardName
            // which might be incorrect when coming from ClosePanel
			data.saveUserProgress(data.getSavedHealth(), data.getSavedLevel());
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