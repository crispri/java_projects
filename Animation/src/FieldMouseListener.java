import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class FieldMouseListener implements MouseListener {
    private Field field;
    private Ball currentBall;

    public FieldMouseListener(Field field) {
        this.field = field;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            currentBall = field.addBall(e.getX(), e.getY());
            field.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            currentBall.setSpeed(e.getX(), e.getY());
            field.repaint();
            currentBall = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
