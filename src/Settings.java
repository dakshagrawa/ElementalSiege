import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
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
		setBackground(new Color(30, 30, 50));

		// ── Title bar ──
		JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
		titleBar.setOpaque(false);

		JButton backBtn = pm.new Button1("<- Back", new Color(200, 200, 200), new Color(60, 60, 80));
		backBtn.setPreferredSize(new Dimension(110, 40));
		backBtn.addActionListener(this);
		titleBar.add(backBtn);

		JLabel titleLabel = new JLabel("Settings", SwingConstants.LEFT);
		titleLabel.setFont(pm.titleFont);
		titleLabel.setForeground(Color.WHITE);
		titleBar.add(titleLabel);

		add(titleBar, BorderLayout.NORTH);

		// ── Center card ──
		JPanel outerWrapper = new JPanel(new GridBagLayout());
		outerWrapper.setOpaque(false);

		JPanel card = new JPanel(new BorderLayout(0, 20));
		card.setBackground(new Color(22, 22, 40, 220));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
			BorderFactory.createEmptyBorder(30, 40, 30, 40)
		));
		card.setPreferredSize(new Dimension(420, 320));

		// Volume section
		JPanel volumePanel = new JPanel(new BorderLayout(0, 8));
		volumePanel.setOpaque(false);

		JLabel volumeLabel = new JLabel("Volume", SwingConstants.CENTER);
		volumeLabel.setFont(pm.normalBoldFont);
		volumeLabel.setForeground(new Color(180, 200, 255));
		volumePanel.add(volumeLabel, BorderLayout.NORTH);

		volumeSlider = new JSlider(0, 100, (int)(pm.sound.currentVolume * 100));
		volumeSlider.setOrientation(JSlider.HORIZONTAL);
		volumeSlider.setMajorTickSpacing(25);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.setOpaque(false);
		volumeSlider.setForeground(Color.WHITE);
		volumeSlider.addChangeListener(this);
		volumePanel.add(volumeSlider, BorderLayout.CENTER);

		card.add(volumePanel, BorderLayout.NORTH);

		// Buttons section
		JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 14));
		buttonsPanel.setOpaque(false);

		JButton saveBtn = pm.new Button1("Save & Exit", Color.WHITE, new Color(60, 140, 80));
		saveBtn.setFont(pm.normalBoldFont);
		saveBtn.setPreferredSize(new Dimension(200, 44));
		saveBtn.addActionListener(this);
		pm.addHoverEffect(saveBtn, Color.LIGHT_GRAY, new Color(60, 140, 80));
		buttonsPanel.add(saveBtn);

		JButton quitBtn = pm.new Button1("Quit", Color.WHITE, new Color(160, 60, 60));
		quitBtn.setFont(pm.normalBoldFont);
		quitBtn.setPreferredSize(new Dimension(200, 44));
		quitBtn.addActionListener(this);
		pm.addHoverEffect(quitBtn, Color.LIGHT_GRAY, new Color(160, 60, 60));
		buttonsPanel.add(quitBtn);

		card.add(buttonsPanel, BorderLayout.CENTER);

		outerWrapper.add(card);
		add(outerWrapper, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent evt)
	{
		String command = evt.getActionCommand();
		if(command.equals("<- Back"))
		{
			if(pm.lastCardName != null && !pm.lastCardName.equals("Close")) {
				pm.changePanelCardNoHistory(pm.lastCardName);
			} else {
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
				data.saveUserProgress(data.getSavedHealth(), data.getSavedLevel());
			}
			if(pm.lastCardName != null && !pm.lastCardName.equals("Close")) {
				pm.changePanelCardNoHistory(pm.lastCardName);
			} else {
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
