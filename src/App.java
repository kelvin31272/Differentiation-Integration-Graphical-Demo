import Visual.*;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class App {
    public static void main(String[] args) throws Exception {
        Window window = new Window();

        int delay = 6;
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                window.update();
            }
        };

        Timer timer = new Timer(delay, taskPerformer);
        timer.start();
    }
}
