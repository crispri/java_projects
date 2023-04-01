import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame wnd = new JFrame("Balls");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen_size = toolkit.getScreenSize();
        Dimension window_size = new Dimension(
                screen_size.width / 2,
                screen_size.height / 2
        );
        Point window_location = new Point(
                (screen_size.width - window_size.width) / 2,
                (screen_size.height - window_size.height) / 2
        );
        wnd.setSize(window_size);
        wnd.setLocation(window_location);
        wnd.setLayout(new BorderLayout());
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Field field = new Field();
        field.addMouseListener(new FieldMouseListener(field));
        wnd.add(field, BorderLayout.CENTER);

        Thread fieldThread = new Thread(field);
        fieldThread.start();

        wnd.setVisible(true);
    }

}
