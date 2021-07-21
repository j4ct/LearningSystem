package cornelio;

import java.awt.*;
import java.awt.event.KeyEvent;

public class main
{
    // psvm
    public static void main(String[] args) throws AWTException {
        /*{ // Temporal solution until find a way to modify "GLFW" to force window always to front.
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_WINDOWS);
            r.keyPress(KeyEvent.VK_D);
            r.keyRelease(KeyEvent.VK_D);
            r.keyRelease(KeyEvent.VK_WINDOWS);
        }

         */
        GUI.displayGUI();
    }
}
