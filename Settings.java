import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*;

public class Settings 
{
    GameData gd;

    public Settings()
    {
        gd = new GameData();
    }

    public class SettingsMenu extends JFrame
    {
        public class SettingsPanel extends JPanel
        {
            public SettingsPanel()
            {
                setLayout(new GridLayout(3, 1, 10, 20));
                
                JSlider volume = new JSlider(0, 100, 100);
                volume.setOrientation(JSlider.HORIZONTAL);
                SliderListener volumeListener = new SlideListener();
                volume.addChangeListener(volumeListener);
                add(volume);

                JButton exit = new JButton("Save & Exit");
                SettingsButtonHandler exitHandler = new SettingsButtonHandler();
                exit.addActionListener(exitHandler);
                add(exit);

                JButton quit = new JButton("Quit");
                SettingsButtonHandler quitHandler = new SettingsButtonHandler();
                quit.addActionListener(quitHandler);
                add(quit);
            }

            public void SettingsButtonHandler(ActionEvent evt)
            {
                String command = evt.getActionCommand()
                
            }
        }
    }
}
