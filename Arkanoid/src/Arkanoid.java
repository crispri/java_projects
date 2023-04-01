import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
public class Arkanoid extends JFrame {
    private static final int D_W = 1000;
    private static final int D_H = 700;
    DrawPanel drawPanel = new DrawPanel();
    Block[] block = new Block[2000];
    int n = 0;
    int dx = 6, dy = 5;
    int dR = 300;
    int xBall = 300, yBall = 300;
    boolean gameOver = false;
    int i=0;
    public Arkanoid() {
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        for (int i = 1; i <= 20; i++) {
            for (int j = 2; j <= 10; j++) {
                block[n] = new Block();
                block[n].x = i * 43;
                block[n].y = j * 20;
                n++;
            }
        }
        ActionListener listener = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    if (yBall > 660) {
                        gameOver = true;
                    }
                    if (yBall < 0) {
                        dy = 7;
                    }
                    xBall += dx;
                    if (xBall > 900)
                        dx = -6;
                    if (xBall < 40)
                        dx = 6;
                    yBall += dy;
                    for (int i = 0; i < n; i++) {
                        if (isBlockCollide(block[i])) {
                            block[i].x = -100;
                            dy = +7;
                        }
                    }
                    if (isPaddleCollide()) {
                        dy = -7;
                    }
                }
                drawPanel.repaint();
            }
        };
        Timer timer = new Timer(50, listener);
        timer.start();
    }
    boolean isBlockCollide(Block block) {
        if (yBall >= block.y && yBall < block.y + 20 && xBall > block.x && xBall < block.x + 43) {
            i++;
            return true;
        }
        return false;
    }
    boolean isPaddleCollide() {
        if (xBall+20 > dR && xBall < dR + 100 && yBall >= 620 && yBall < 720 ) {
            return true;
        }
        return false;
    }
    private class DrawPanel extends JPanel {
        public DrawPanel() {
            InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = getActionMap();
            String VK_LEFT = "VK_LEFT";
            KeyStroke W = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
            inputMap.put(W, VK_LEFT);
            actionMap.put(VK_LEFT, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dR = dR - 15;
                }
            });
            String VK_RIGHT = "VK_RIGHT";
            KeyStroke WVK_RIGHT = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
            inputMap.put(WVK_RIGHT, VK_RIGHT);
            actionMap.put(VK_RIGHT, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dR = dR + 15;
                }
            });
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics gBall = g;
            gBall.setColor(Color.black);
            gBall.fillOval(xBall, yBall, 40, 40);
            if (gameOver) {
                g.setFont(new Font("MyFont", 20, 20));
                g.drawString("Game Over", 300, 300);
            }
            Graphics gPaddle = g;
            gPaddle.setColor(Color.blue);
            int xR = dR;
            if (xR > 940)
                xR = 900;
            if (xR < 0)
                xR = 0;
            gPaddle.fillRect(xR, 650, 100, 20);
            Graphics gBlock = g;
            for (int i = 0; i < n; i++) {
                gBlock.setColor(Color.orange);
                gBall.fillRect(block[i].x, block[i].y, 43, 20);
                gBlock.setColor(Color.red);
                gBall.drawRect(block[i].x, block[i].y, 43, 20);
            }
        }
        public Dimension getPreferredSize() {
            return new Dimension(D_W, D_H);
        }
    }
    private class Block {
        int x;
        int y;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Arkanoid();
            }
        });
    }
}