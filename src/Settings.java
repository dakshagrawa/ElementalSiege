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
		setLayout(new GridLayout(3, 1, 10, 20));
		
		volumeSlider = new JSlider(0, 100, 100);
		volumeSlider.setOrientation(JSlider.HORIZONTAL);
		volumeSlider.addChangeListener(this);
		add(volumeSlider);

		JButton exit = new JButton("Save & Exit");
		exit.addActionListener(this);
		add(exit);

		JButton quit = new JButton("Quit");
		quit.addActionListener(this);
		add(quit);

		//JButton timeLimit = new JButton("Set a time limit");
	}

	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command.equals("Save & Exit"))
		{
			data.saveData();
			pm.changePanelCard("Save & Exit");
		}
		else
		{
			pm.frame.dispose();
			System.exit(0);
		}
	}

	public void stateChanged(ChangeEvent evt)
	{
		// pm.sound.currentVolume = volumeSlider.getValue();
		// pm.sound.fc.setVaue(pm.sound.currentVolume);
	}
}