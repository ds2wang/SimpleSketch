import java.awt.Stroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
abstract class MyBoundedShape extends MyShape
{
    private boolean filled; //whether or not the shape is filled
    
    //default constructor
    public MyBoundedShape(){
        super(); //calls super constructor
        filled = false; 
    }
    
    // constructor with input values
    public MyBoundedShape( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean filled, boolean dashed, int dlength )
    {
        super( x1, y1, x2, y2, thickness, color, color2, gradient, dashed, dlength ); //calls super constructor
        this.filled=filled;
        
    } // end MyBoundedShape constructor
    
    public int getUpperLeftX(  ){//gets the smaller of the 2 x coordinates
        return Math.min(  getX1(), getX2() );
    }
    public int getUpperLeftY( ){//gets the smaller of the 2 y coordinates
        return Math.min( getY1(), getY2() );
    }
    public int getWidth(  ){//find absolute value of the difference between x coordinates
        return Math.abs( getX1() - getX2() );
    }
    public int getHeight(  ){//find absolute value of the difference between y coordinates
        return Math.abs(getY1() - getY2() );
    }    
    
    //mutator method for whether or not to fill the shape
    public void setFilled( boolean tf ){
        filled=tf;
    }    
    //accessor method for whether or not to fill the shape
    public boolean getFilled(){
        return filled;
    }
    // abstract method which will get overidden
    abstract void draw( Graphics g );
} // end class MyBoundedShape