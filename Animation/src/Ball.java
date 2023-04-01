import java.awt.*;
import java.util.Random;

public class Ball {
    public static final int RADIUS = 10;
    public static final int ACCELERATION = 10;

    private double x;
    private double y;
    private double speed_x;
    private double speed_y;
    private Color color;
    private Field field;

    public Ball(int x, int y, Field field) {
        this.x = x;
        this.y = y;
        this.speed_x = 0;
        this.speed_y = 0;
        Random random = new Random();
        color = new Color(
                random.nextInt(224),
                random.nextInt(224),
                random.nextInt(224)
        );
        this.field = field;
    }

    public void setSpeed(int next_x, int next_y) {
        this.speed_x = next_x - (int) x;
        this.speed_y = next_y - (int) y;
    }

    public void move() {
        x += speed_x * Field.TIME_DELTA / 1000.;
        y += speed_y * Field.TIME_DELTA / 1000.;
        speed_x += (speed_x >= 0 ? 1 : -1) * ACCELERATION * Field.TIME_DELTA / 1000.;
        speed_y += (speed_y >= 0 ? 1 : -1) * ACCELERATION * Field.TIME_DELTA / 1000.;
        if (x < RADIUS || x > field.getWidth() - RADIUS)
            speed_x *= -1;
        if (y < RADIUS || y > field.getHeight() - RADIUS)
            speed_y *= -1;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x - RADIUS, (int) y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
