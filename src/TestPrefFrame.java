import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
class TestPrefFrame{
    public static void main (String []args) {
        PrefFrame prefFrame = new PrefFrame(); 
        prefFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        prefFrame.setLocation(200,200);
        prefFrame.setVisible( true ); // display frame
    }
}