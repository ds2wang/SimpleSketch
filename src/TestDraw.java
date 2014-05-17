
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class TestDraw
{
    public static void main( String args[] )
    {
        //create Drawframe object, runs the program
        FrameView application = new FrameView();
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.setSize( 960, 544 );
        application.setVisible( true ); // set the frame to visible
       
    } // end main
} // end class TestDraw

