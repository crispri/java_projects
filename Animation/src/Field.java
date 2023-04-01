import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Field extends JPanel implements Runnable {
    public static final int TIME_DELTA = 10;

    private ArrayList<Ball> balls;

    public Field() {
        super(true);

        balls = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Ball ball : balls)
            ball.paint(g);
    }

    @Override
    public void run() {
        while (true) {
            for (Ball ball : balls)
                ball.move();
            repaint();
            try {
                Thread.sleep(TIME_DELTA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Ball addBall(int x, int y) {
        Ball ball = new Ball(x, y, this);
        balls.add(ball);
        return ball;
    }
}