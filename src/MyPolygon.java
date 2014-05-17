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

import org.w3c.dom.Element;
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


public class MyPolygon extends MyBoundedShape{

	public MyPolygon( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean filled, boolean dashed, int dlength )
    {
		super( x1, y1, x2, y2, thickness, color, color2, gradient, filled, dashed, dlength ); //calls super constructor
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
	        if(!isFinal()){
		        for(int i=0;i<xPts.size()-1;i++){
		        	g2.drawLine(xPts.get(i), yPts.get(i), xPts.get(i+1), yPts.get(i+1));
		        }
	        }else{
	        	if (getFilled() ){ 
	        		g2.fillPolygon(convertIntegers(xPts), convertIntegers(yPts), xPts.size());
	        	}else{
	        		g2.drawPolygon(convertIntegers(xPts), convertIntegers(yPts), xPts.size());
	        	}
	        }
	        //g2.drawLine( getX1(), getY1(), getX2(), getY2() ); //draws the lines according to the coordinates
	    } // end method draw
	  public void setX2(int x2){
		  addXPoint(x2);
	  }
	  public void setY2(int y2){
		  addYPoint(y2);
	  }
	  public int[] convertIntegers(ArrayList<Integer> integers)
	  {
	      int[] ret = new int[integers.size()];
	      for (int i=0; i < ret.length; i++)
	      {
	          ret[i] = integers.get(i).intValue();
	      }
	      return ret;
	  }
	  public boolean inEraseRegion(MyShape eraser){
		  if(getFilled()){
			  return inRegionFilled(eraser);
		  }else{
			  return inRegion(eraser);
		  }
		
	  }
	  public boolean hasPoint(int x1, int y1){
		 // return hasPoint2(x1,y1);
		  Point p=new Point();
		  p.x=x1;
		  p.y=y1;
		  Point [] pts=getPts();
		  return pixelInPoly(pts, p);
	  }
	  public boolean hasPoint2(int x1, int y1){
		  Polygon p=new Polygon();
		  p.xpoints=getXPtsA();
		  p.ypoints=getYPtsA();
		  p.npoints=getXPts().size();
		  return p.contains(x1, y1);
	  }
	  public boolean inRegion(MyShape eraser){
		  int rx1, rx2, ry1, ry2;
		  int [] xPts=getXPtsA();
		  int [] yPts=getYPtsA();
		  rx1=eraser.getX1();
		  rx2=eraser.getX2();
		  ry1=eraser.getY1();
		  ry2=eraser.getY2();
		  Rectangle2D.Float rect=new Rectangle2D.Float(rx1, ry1, rx2-rx1, ry2-ry1);
		  for(int i=0;i<xPts.length;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[(i+1)%xPts.length], yPts[(i+1)%xPts.length] );
			  if(rect.intersectsLine(line)){
				  return true;
			  }
			  /*
			  if(pointInRect(xPts[i],yPts[i],rx1, rx2, ry1, ry2)){
				  return true;
			  }*/
		  }
		  
		  return false;
	  }
	  public boolean inRegionFilled(MyShape eraser){
		//  System.out.println("here");
		  int rx1, rx2, ry1, ry2;
		  rx1=eraser.getX1();
		  rx2=eraser.getX2();
		  ry1=eraser.getY1();
		  ry2=eraser.getY2();

		  int [] xpoints={rx1, rx2, rx2, rx1};
		  int [] ypoints={ry1, ry1, ry2, ry2};
		  Polygon p=new Polygon();
		  p.xpoints=xpoints;
		  p.ypoints=ypoints;
		  p.npoints=4;
		  Area a=new Area(p);
		  Polygon p2=new Polygon();
		  int [] xPts=getXPtsA();
		  int [] yPts=getYPtsA();
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
		  Polygon poly=new Polygon(sXPts, sYPts, sXPts.length);
		  Area a=new Area(poly);
		  for(int i=0;i<xPts.length;i++){
			  Line2D.Double line=new Line2D.Double(xPts[i], yPts[i], xPts[(i+1)%xPts.length], yPts[(i+1)%xPts.length] );
			 // Area b= new Area(line);
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
	    	return saveData("MyPolygon", doc, getFilled());
	    }
}
