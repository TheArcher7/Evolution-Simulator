import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class circleDrawer extends JFrame {

    public circleDrawer() {
        setTitle("Circle Drawer");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // Define the grid parameters
        int gridSize = 100;
        int gridCenterX = getWidth() / 2;
        int gridCenterY = getHeight() / 2;

        // Define the circle parameters
        int circleDiameter = 20;

        // Calculate the position of the circle center within the grid
        int circleCenterX = gridCenterX - (circleDiameter / 2);
        int circleCenterY = gridCenterY - (circleDiameter / 2);

        // Draw the grid
        for (int i = 0; i <= gridSize; i++) {
            int x = (i * getWidth()) / gridSize;
            int y = (i * getHeight()) / gridSize;
            g2d.drawLine(x, 0, x, getHeight());
            g2d.drawLine(0, y, getWidth(), y);
        }

        // Draw the circle
        Ellipse2D.Double circle = new Ellipse2D.Double(circleCenterX, circleCenterY, circleDiameter, circleDiameter);
        g2d.draw(circle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new circleDrawer());
    }
}

