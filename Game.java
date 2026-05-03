import java.util.Scanner;

public class Game 
{
    public static void main(String[] args) 
	{
        try 
		{
            String os = System.getProperty("os.name").toLowerCase();
            String shell;
            String shellFlag;
            String command;

            if (os.contains("win")) 
			{
                shell = "cmd.exe";
                shellFlag = "/c";
                // Added "start" for Windows to ensure the GUI launches smoothly
                command = "cls && cd src/ && javac *.java && java ElementalSiege && del *.class";
            } 
			else 
			{
                shell = "bash";
                shellFlag = "-c";
                command = "clear && cd src/ && javac *.java && java ElementalSiege && rm *.class";
            }

            ProcessBuilder pb = new ProcessBuilder(shell, shellFlag, command);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // We keep this to print any COMPILER errors if the game fails to start
            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\A");
            if (s.hasNext()) 
			{
                System.out.print(s.next());
            }

            p.waitFor(); // This keeps the runner alive until you close your GUI

        } catch (Exception e) 
		{
            e.printStackTrace();
        }
    }
}
