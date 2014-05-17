import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class MyPen extends MyShape{

	public MyPen( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean dashed, int dlength )
    {
        super( x1, y1, x2, y2, thickness, color, color2, gradient, dashed, dlength );
    } // end MyLine constructor
	  public void draw( Graphics g )
	    {
	        Graphics2D g2 = (Graphics2D) g; //converts to Graphics2D
	        if (getGradient()){ //determines whether or not to draw with gradient
	            Color s1 = getColor();
	            Color s2 = getColor2();
	            GradientPaint gradient = new GradientPaint(10,10,s1,20,20,s2,true);
	            g2.setPaint(gradient);
	        }else{
	            g2.setColor( getColor() ); 
	        }
	        if (getDashed()) //check if to make the lines dashed
	            g2.setStroke (new BasicStroke(getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,  3f, new float[] {getDLength()}, 3f));
	        else
	            g2.setStroke(new BasicStroke(getThickness()));
	        ArrayList<Integer> xPts=getXPts();
	        ArrayList<Integer> yPts=getYPts();
	        for(int i=0;i<xPts.size()-1;i++){
	        	g2.drawLine(xPts.get(i), yPts.get(i), xPts.get(i+1), yPts.get(i+1));
	        }
	        //g2.drawLine( getX1(), getY1(), getX2(), getY2() ); //draws the lines according to the coordinates
	    } // end method draw
	  public void setX2(int x2){
		  addXPoint(x2);
	  }
	  public void setY2(int y2){
		  addYPoint(y2);
	  }
	  public boolean inEraseRegion(MyShape eraser){
		  int rx1, rx2, ry1, ry2;
		  int [] xPts=getXPtsA();
		  int [] yPts=getYPtsA();
		  rx1=eraser.getX1();
		  rx2=eraser.getX2();
		  ry1=eraser.getY1();
		  ry2=eraser.getY2();
		  Rectangle2D.Float rect=new Rectangle2D.Float(rx1, ry1, rx2-rx1, ry2-ry1);
		  for(int i=0;i<xPts.length-1;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[i+1], yPts[i+1] );
			  if(rect.intersectsLine(line)){
				  return true;
			  }
		  }
		  return false;
	  }
	  public boolean pointInRect(int px, int py, int rx1, int rx2, int ry1, int ry2){
		  return (px>=rx1 && px <=rx2)&& (py>=ry1 && py<=ry2);
	  }
	  public boolean inSelected( MyShape selector ){
		  int rx1, rx2, ry1, ry2;
		  int [] xPts=getXPtsA();
		  int [] yPts=getYPtsA();
		  int [] sXPts=selector.getXPtsA();
		  int [] sYPts=selector.getYPtsA();
		  Point [] pts=selector.getPts();
		  Point p=new Point();
		  p.x=xPts[0];
		  p.y=yPts[0];
		  Polygon poly=new Polygon(sXPts, sYPts, sXPts.length);
		  Area a=new Area(poly);
		  if(!pixelInPoly(pts, p)){
			  return false;
		  }
		  for(int i=0;i<xPts.length-1;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[i+1], yPts[i+1] );
			  //Area b= new Area(line);
			  p.x=xPts[i+1];
			  p.y=yPts[i+1];
			  if(!pixelInPoly(pts, p)){
				  return false;
			  }
			  if((yPts[i]!=yPts[i+1])|| xPts[i]!=xPts[i+1]){
				  for(int j=0; j< sXPts.length;j++){
					  if(line.intersectsLine(sXPts[j], sYPts[j], sXPts[(j+1)%sXPts.length], sYPts[(j+1)%sXPts.length])){
						  System.out.println(xPts[i]+" "+ yPts[i]+" "+ xPts[i+1]+ " "+ yPts[i+1]+ " and "+sXPts[j]+" "+ sYPts[j]+ " "+ sXPts[(j+1)%sXPts.length]+ " "+ sYPts[(j+1)%sXPts.length]);
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
	    	return saveData("MyPen", doc);
	    }
}
