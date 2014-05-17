//allows the user to draw ovals
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
class MyOval extends MyBoundedShape
{
    //default constructor
    public MyOval(){ 
        super(); //calls super constructor
    }    
    // constructor with input values
    public MyOval( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean filled, boolean dashed, int dlength )
    {
        super( x1, y1, x2, y2, thickness, color, color2, gradient, filled, dashed, dlength ); //calls super constructor
        
    } //end MyOval constructor
    
    //draws the ovals
    public void draw( Graphics g )
    {
        Graphics2D g2 = (Graphics2D) g; //converts to Graphics2D
        if (getGradient()){ //determines whether or not to draw with gradient
            Color s1 = getColor();
            Color s2 = getColor2();
            GradientPaint gradient = new GradientPaint(10,10,s1,20,20,s2,true);
            g2.setPaint(gradient);
        }else{
            g2.setColor( getColor() ); //checks whether or not to fill shape
        }
        if (getDashed()) //check if to make the lines dashed
            g2.setStroke (new BasicStroke(getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,  3f, new float[] {getDLength()}, 3f));
        else
            g2.setStroke(new BasicStroke(getThickness()));
        
        if ( getFilled() ){ //checks whether or not to fill shape
            g2.fillOval( getUpperLeftX(), getUpperLeftY(), getWidth(), getHeight() );//draws filled oval
        }else{
            g2.drawOval( getUpperLeftX(), getUpperLeftY(), getWidth(), getHeight() );//draws an oval with the specified color
        }
    } // end method draw
    public boolean inEraseRegion(MyShape eraser){
		if(!getFilled()){
			return inRegion(eraser);
		}else{
			return inRegionFilled(eraser);
		}
	  }
    public boolean inRegion(MyShape eraser){
    	  int rx1, rx2, ry1, ry2, xx1, xx2, yy1, yy2;
		  rx1=eraser.getX1();
		  rx2=eraser.getX2();
		  ry1=eraser.getY1();
		  ry2=eraser.getY2();
		  xx1=getX1();
		  xx2=getX2();
		  yy1=getY1();
		  yy2=getY2();
		  int [] xPts={xx1, xx2, xx2, xx1};
		  int [] yPts={yy1, yy1, yy2, yy2};
		  Rectangle2D.Float rect=new Rectangle2D.Float(rx1, ry1, rx2-rx1, ry2-ry1);
		  for(int i=0;i<xPts.length;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[(i+1)%xPts.length], yPts[(i+1)%xPts.length] );
			  if(rect.intersectsLine(line)){
				  return true;
			  }
		  }
		  
		  return false;
	  }
    public boolean inRegionFilled(MyShape eraser){
    	  System.out.println("here");
		  int rx1, rx2, ry1, ry2, xx1, xx2, yy1, yy2;
		  rx1=eraser.getX1();
		  rx2=eraser.getX2();
		  ry1=eraser.getY1();
		  ry2=eraser.getY2();
		  xx1=getX1();
		  xx2=getX2();
		  yy1=getY1();
		  yy2=getY2();
		  int [] xpoints={rx1, rx2, rx2, rx1};
		  int [] ypoints={ry1, ry1, ry2, ry2};
		  Polygon p=new Polygon();
		  p.xpoints=xpoints;
		  p.ypoints=ypoints;
		  p.npoints=4;
		  Area a=new Area(p);
		  Polygon p2=new Polygon();
		  int [] xPts={xx1, xx2, xx2, xx1};
		  int [] yPts={yy1, yy1, yy2, yy2};
		  p2.xpoints=xPts;
		  p2.ypoints=yPts;
		  p2.npoints=xPts.length;
		  Area b=new Area(p2);
		  a.intersect(b);
		  if (!a.isEmpty()){
			return true;
		  }
		  return false;
    }
    public boolean inSelected( MyShape selector ){
		  int  xx1, xx2, yy1, yy2;
		  xx1=getX1();
		  xx2=getX2();
		  yy1=getY1();
		  yy2=getY2();
		  int [] xPts={xx1, xx2, xx2, xx1};
		  int [] yPts={yy1, yy1, yy2, yy2};
		  int [] sXPts=selector.getXPtsA();
		  int [] sYPts=selector.getYPtsA();
		  Point [] pts=selector.getPts();
		  Point p=new Point();
		  Polygon poly=new Polygon(sXPts, sYPts, sXPts.length);
		  Area a=new Area(poly);
		  for(int i=0;i<xPts.length;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[(i+1)%xPts.length], yPts[(i+1)%xPts.length] );
			  Area b= new Area(line);
			  p.x=xPts[(i+1)%xPts.length];
			  p.y=yPts[(i+1)%xPts.length];
			  if(!pixelInPoly(pts, p)){
				  return false;
			  }
			  if((yPts[i]!=yPts[(i+1)%xPts.length])|| xPts[i]!=xPts[(i+1)%xPts.length]){
				  for(int j=0; j< sXPts.length;j++){
					  if(line.intersectsLine(sXPts[j], sYPts[j], sXPts[(j+1)%sXPts.length], sYPts[(j+1)%sXPts.length])){
						  return false;
					  }
				  }
			  }
			  /*
			  b.intersect(a);
			  if ( !b.isEmpty())
				  return false;*/
		  }
		  return true;
	  }
    public Element saveData(Document doc ){
    	return saveData("MyOval", doc, getFilled());
    }
} // end class MyOval