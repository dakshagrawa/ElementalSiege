import java.net.URL;
import javax.sound.sampled.*;

public class Sound 
{
    private Clip clip;

    public void playBackground(String fileName) 
	{
        // Stop and close existing clip to free Linux audio resources
        disposeSound();

        try {
            // Ensure the file is a .wav and the path is correct
            URL url = Sound.class.getResource("/" + fileName + ".wav");
            if (url == null) {
                System.err.println("Could not find file: " + fileName + ".wav");
                return;
            }

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(inputStream);

			// // --- VOLUME CONTROL START ---
            // if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) { //TODO: THIS DOESNT WORK (if test: add to param: ", float volume")
            //     FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                
            //     // volume is a float between 0.0 (silent) and 1.0 (full)
            //     // We convert that to decibels
            //     float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            //     gainControl.setValue(dB);
            // }
            // // --- VOLUME CONTROL END ---
            
            // Loop continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        } 
		catch (Exception e) 
		{
            e.printStackTrace();
        }
    }

    public void disposeSound() 
	{
        if (clip != null) 
		{
            clip.stop();
            clip.close();
        }
    }
}
