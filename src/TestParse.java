import java.io.File;
import java.util.Scanner;

public class TestParse {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("storedData/CharacterInfo.txt"));
        // skip first two lines
        if (sc.hasNextLine()) sc.nextLine();
        if (sc.hasNextLine()) sc.nextLine();
        int i = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println("Line: '" + line + "'");
            // mimic parsedCharacterInfo
            if(line.contains(":")){
                String name = line.substring(0,line.indexOf(':')).trim();
                line = line.substring(line.indexOf(':')+1).trim();
                System.out.println("  name: '" + name + "'");
                try {
                    int problemSolving = Integer.parseInt(line.substring(0,line.indexOf(',')));
                    line = line.substring(line.indexOf(',')+1).trim();
                    int wisdom = Integer.parseInt(line.substring(0,line.indexOf(',')));
                    line = line.substring(line.indexOf(',')+1).trim();
                    int curiosity = Integer.parseInt(line.substring(0,line.indexOf(',')));
                    line = line.substring(line.indexOf(',')+1).trim();
                    char rarityChar = line.toUpperCase().charAt(0);
                    System.out.println("  parsed: PS=" + problemSolving + " Wis=" + wisdom + " Cur=" + curiosity + " Rarity=" + rarityChar);
                } catch (Exception e) {
                    System.out.println("  parse error: " + e);
                }
            }
            i++;
        }
        sc.close();
    }
}
