// MyString.java
// Declaration of class MyString
//allows user to draw String images on the canvas of 
//various colours and sizes and characters
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Font;

class MyString extends MyShape
{
    private String string;
    private int fontsize; 
    //default constructor
    public MyString(){ 
        super(); //calls super constructor
    }
    
    // constructor with input values
    public MyString( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean dashed, int dlength, String string, int fontsize )
    {
        super( x1, y1, x2, y2, thickness, color, color2, gradient, dashed, dlength );
        this.string=string;
        this.fontsize=fontsize;
    } // end MyString constructor
    
    // draws the String
    public void draw( Graphics g )
    {
        Graphics2D g2 = (Graphics2D) g; //converts to Graphics2D
        g2.setFont( new Font( "Serif", Font.PLAIN, fontsize ) );//sets the font 
        if (getGradient()){ //determines whether or not to draw with gradient
            Color s1 = getColor();
            Color s2 = getColor2();
            GradientPaint gradient = new GradientPaint(10,10,s1,20,20,s2,true);
            g2.setPaint(gradient);
        }else{ //paint without gradient
            g2.setColor( getColor() ); 
        }
        if (getDashed()) //checks whether or not to make the stroke dashed
            g2.setStroke (new BasicStroke(getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,  3f, new float[] {getDLength()}, 3f));
        else
            g2.setStroke(new BasicStroke(getThickness()));
        g2.drawString( string, getX2(), getY2() ); //draws the Strings according to the coordinates
    } // end method draw
} // end class MyString