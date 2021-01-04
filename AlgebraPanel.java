import javax.swing.*;
import java.awt.*;

/**
 * Shows information about the process of the algorithm
 */
public class AlgebraPanel extends JPanel{
    public AlgebraPanel()
    {
        setBackground(new Color(250,210,250));
    }

    /**
     * Paints the info about the next clip on the right side of the screen
     * @param g Graphics object used by JPanel
     */
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        if (Main.redgreenblue!=null)
        {
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(newFont);
            g.drawString("The minimal guarding found:" + Math.min(Math.min(Main.redgreenblue[0],Main.redgreenblue[1]),Main.redgreenblue[2]),20,40);
            g.drawString("Red Count:" +Main.redgreenblue[0],20,70);
            g.drawString("Green Count:" +Main.redgreenblue[1],20,100);
            g.drawString("Blue Count:" +Main.redgreenblue[2],20,130);
            g.setFont(currentFont);
        }
    }
}
