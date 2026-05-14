import java.io.File;
import javax.sound.sampled.*; //TODO: add citation

public class Sound 
{
    private Clip clip;
	public float currentVolume; // Value between 0.0f (silent) and 1.0f (full volume)

    public Sound() 
	{
        currentVolume = 0.75f; // Default to maximum volume
    }

	public void playNewBackground(String fileName)
	{
        // Stop and close existing clip to free Linux audio resources
        disposeSound();

		playBackground(fileName);
	}

    public void playBackground(String fileName) 
	{
        try 
		{
            // Use File instead of ResourceLoader to read from the external directory path
            File soundFile = new File(fileName);
            
            if (!soundFile.exists()) 
			{
                System.err.println("Could not find file: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(inputStream);

            // --- CROSS-PLATFORM VOLUME CONTROL ---
            setVolume(currentVolume);

            // Loop continuously and begin playback
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            
        } 
		catch (Exception e) 
		{
            e.printStackTrace();
        }
    }

    /**
     * Updates the volume dynamically while the clip is running or during initialization.
     * @param volume a float value between 0.0f (silent) and 1.0f (maximum)
     */
    public void setVolume(float volume) 
	{
        currentVolume = volume;
        
        if (clip == null || !clip.isOpen()) 
		{
            return;
        }

        // Make volume values strictly between 0.0f and 1.0f to avoid mathematical exceptions
        if (currentVolume < 0.0f) 
			currentVolume = 0.0f;
        if (currentVolume > 1.0f) 
			currentVolume = 1.0f;

        try 
		{
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) 
			{
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                
                // Prevent Log(0) mathematical failure when silent (volume is 0)
				float dB;
				if (currentVolume == 0.0f)
					dB = -80.0f;
				else
					dB = (float)((Math.log(currentVolume) / Math.log(10.0))*20.0);

				dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB)); // Clamp against hardware-specific limits
                gainControl.setValue(dB);
                
            } 
			else if (clip.isControlSupported(FloatControl.Type.VOLUME)) 
			{
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
                
                // Map linear scale directly within the specific mixer line's bounds
                float min = volumeControl.getMinimum();
                float max = volumeControl.getMaximum();
                float targetVolume = min + (max - min) * currentVolume;

				targetVolume = Math.max(min, Math.min(max, targetVolume)); // Clamp against hardware-specific limits
                volumeControl.setValue(targetVolume);
            }
        } 
		catch (Exception e) 
		{
            System.err.println("Failed to adjust volume hardware lines: " + e.getMessage());
        }
    }

    public void disposeSound() 
	{
        if (clip != null) 
		{
            if (clip.isRunning()) 
			{
                clip.stop();
            }
            clip.close();
            clip = null; // Dereference to allow garbage collection
        }
    }
}
