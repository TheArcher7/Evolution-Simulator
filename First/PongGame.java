import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongGame extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;

    private int paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballSpeedX = 3;
    private int ballSpeedY = 2;

    public PongGame() {
        setTitle("Pong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        setFocusable(true);
    }

    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (paddle2Y > 0) {
                    paddle2Y -= 10;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (paddle2Y < HEIGHT - PADDLE_HEIGHT) {
                    paddle2Y += 10;
                }
                break;
            case KeyEvent.VK_W:
                if (paddle1Y > 0) {
                    paddle1Y -= 10;
                }
                break;
            case KeyEvent.VK_S:
                if (paddle1Y < HEIGHT - PADDLE_HEIGHT) {
                    paddle1Y += 10;
                }
                break;
        }
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball collisions with walls
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballSpeedY = -ballSpeedY;
        }

        // Ball collisions with paddles
        if ((ballX <= PADDLE_WIDTH && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) ||
                (ballX >= WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT)) {
            ballSpeedX = -ballSpeedX;
        }

        // Ball out of bounds (scoring)
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballSpeedX = 3;
        ballSpeedY = 2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw paddles
        g2d.fillRect(PADDLE_WIDTH, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g2d.fillRect(WIDTH - 2 * PADDLE_WIDTH - BALL_SIZE, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g2d.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw center line
        for (int i = 0; i < HEIGHT; i += 20) {
            g2d.fillRect(WIDTH / 2 - 2, i, 4, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PongGame pongGame = new PongGame();
            pongGame.setVisible(true);

            // Game loop
            while (true) {
                pongGame.moveBall();
                pongGame.repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
