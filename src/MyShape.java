import java.awt.Point;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.io.File;

import javax.swing.Spring;
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
abstract class MyShape //class is abstract
{
    private int x1; // x coordinate of first endpoint
    private int y1; // y coordinate of first endpoint
    private int x2; // x coordinate of second endpoint
    private int y2; // y coordinate of second endpoint
    private int thickness;  // thickness of the line
    private boolean dashed; //if line is dashed
    private boolean gradient; //whether or not to draw with gradient
    private Color myColor; // color of this shape
    private Color myColor2; // color of this shape
    private int dlength; //dash length
    private ArrayList<Integer> xPts=new ArrayList<Integer>();
    private ArrayList<Integer> yPts=new ArrayList<Integer>();
    private boolean isFinal;
    private int startTime;
    private int endTime;
    private ArrayList<Integer> dxPts=new ArrayList<Integer>();
    private ArrayList<Integer> dyPts=new ArrayList<Integer>();
    private int lastMoved;

	//Default Constuctor
    public MyShape(){ 
    	startTime=-1;
    	endTime=3600;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        dxPts.add(0);
        dyPts.add(0);
        isFinal=false;
        xPts.add(x1);
        yPts.add(y1);
        xPts.add(x2);
        yPts.add(y2);
        thickness=1;
        gradient=false;
        dashed=false;
        myColor = Color.BLACK;
        myColor2 = Color.BLACK;
        dlength=1;
    }
	// constructor with input values
    public MyShape( int x1, int y1, int x2, int y2, int thickness, Color color, Color color2, boolean gradient, boolean dashed, int dlength )
    {
    	startTime=-1;
    	endTime=36000;
    	xPts.add(x1);
        yPts.add(y1);
        xPts.add(x2);
        yPts.add(y2);
        dxPts.add(0);
        dyPts.add(0);
        isFinal=false;
        this.x1 = x1; // set x coordinate of first endpoint
        this.y1 = y1; // set y coordinate of first endpoint
        this.x2 = x2; // set x coordinate of second endpoint
        this.y2 = y2; // set y coordinate of second endpoint
        this.thickness=thickness; //set thickness of line
        this.gradient=gradient; //set whether or not to paint with gradient
        myColor = color; // set the color
        myColor2=color2; //set 2nd color of gradient
        this.dashed=dashed; //set if line is dashed
        this.dlength=dlength; //set dash length
        
    } // end MyLine constructor
    
    //mutator methods for coordinates

	public void setLastMoved(int lastMoved) {
		this.lastMoved = lastMoved;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public void insertDX(int i, int val){
		for(int j=dxPts.size();j<i;j++){
			dxPts.add(0);
		}
		dxPts.add(i, val);
	}
	public void insertDY(int i, int val){
		for(int j=dyPts.size();j<i;j++){
			dyPts.add(0);
		}
		dyPts.add(i, val);
	}
	public void setDX(int i, int val){
		for(int j=dxPts.size()-1; j>=i+1;j--){
			dxPts.remove(j);
		}
		for(int j=dxPts.size();j<i;j++){
			dxPts.add(0);
		}
		if(i==dxPts.size()){
			dxPts.add(val);
		}else{
			dxPts.set(i, val);
		}
	}
	public void setDY(int i, int val){
		for(int j=dyPts.size()-1; j>=i+1;j--){
			dyPts.remove(j);
		}
		for(int j=dyPts.size();j<i;j++){
			dyPts.add(0);
		}
		if(i==dyPts.size()){
			dyPts.add(val);
		}else{
			dyPts.set(i, val);
		}
	}
    public void setX1( int x1 ){ //set the x coordinate for the first point
        this.x1 = x1;
    }
    public void setX2( int x2 ){ //set the x coordinate for the 2nd point
        this.x2 = x2;
    }
    public void setY1( int y1 ){ //set the y coordinate for the first point
        this.y1 = y1;
    }    
    public void setY2( int y2 ){ //set the y coordinate for the 2nd point
        this.y2 = y2;
    }    
    public void addXPoint(int x1){
    	xPts.add(x1);
    }
    public void addYPoint(int y1){
    	yPts.add(y1);
    }
    public void addPoint(int x1, int y1){
    	x2=x1;
    	y2=y1;
    	xPts.add(x1);
    	yPts.add(y1);
    }
    public void setThickness (int thickness){ //set thickness of line
        this.thickness=thickness;
    }
     public void setDLength (int dlength){ //set dash length
        this.dlength=dlength;
    }
    public void setDashed(boolean b){ //set whether or not line is dashed
        dashed=b;
    }
    public void setGradient(boolean gr){ //set whether or not to paint with a gradient
        gradient=gr;
    }                      
    //mutator method for color
    public void setColor( Color a ){ //set primary color
        myColor = a;
    }
    public void setColor2( Color a ){ //set second color of gradient
        myColor2 = a;
    }
    public void setFinal(){
    	isFinal=true;
    }
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
    public void trans(int dx, int dy){
    	x1+=dx;
    	y1+=dy;
    	x2+=dx;
    	y2+=dy;
    	for (int i=0; i<xPts.size();i++){
    		xPts.set(i, xPts.get(i)+dx);
    		yPts.set(i, yPts.get(i)+dy);
    	}
    }
    public void clearPts(){
    	xPts.clear();
    	yPts.clear();
    }
    public void setPts(ArrayList<Integer> newXPts, ArrayList<Integer> newYPts ){
    	xPts=newXPts;
    	yPts=newYPts;
    }
    public void setDisplacement(ArrayList<Integer> newdxPts, ArrayList<Integer> newdyPts ){
    	dxPts=newdxPts;
    	dyPts=newdyPts;
    }
    public ArrayList<Integer> getXPts(){
    	return xPts;
    }
    public ArrayList<Integer> getYPts(){
    	return yPts;
    }
    public Element saveData(String type, Document doc, boolean filled){
    	Element rootElement=null;
    	
		rootElement = doc.createElement(type);
		
		Element x1Node = doc.createElement("x1");
		x1Node.appendChild(doc.createTextNode(Integer.toString(x1)));
		rootElement.appendChild(x1Node);
		
		Element y1Node = doc.createElement("y1");
		y1Node.appendChild(doc.createTextNode(Integer.toString(y1)));
		rootElement.appendChild(y1Node);

		Element x2Node = doc.createElement("x2");
		x2Node.appendChild(doc.createTextNode(Integer.toString(x2)));
		rootElement.appendChild(x2Node);
		
		Element y2Node = doc.createElement("y2");
		y2Node.appendChild(doc.createTextNode(Integer.toString(y2)));
		rootElement.appendChild(y2Node);
		
		Element thicknessNode = doc.createElement("thickness");
		thicknessNode.appendChild(doc.createTextNode(Integer.toString(thickness)));
		rootElement.appendChild(thicknessNode);
		
		Element dashedNode = doc.createElement("dashed");
		dashedNode.appendChild(doc.createTextNode(Boolean.toString(dashed)));
		rootElement.appendChild(dashedNode);
		
		Element gradientNode = doc.createElement("gradient");
		gradientNode.appendChild(doc.createTextNode(Boolean.toString(gradient)));
		rootElement.appendChild(gradientNode);
		
		Element myColorNode = doc.createElement("myColor");
		myColorNode.appendChild(doc.createTextNode(Integer.toString(myColor.getRGB())));
		rootElement.appendChild(myColorNode);
		
		Element myColor2Node = doc.createElement("myColor2");
		myColor2Node.appendChild(doc.createTextNode(Integer.toString(myColor2.getRGB())));
		rootElement.appendChild(myColor2Node);
		
		Element dlengthNode = doc.createElement("dlength");
		dlengthNode.appendChild(doc.createTextNode(Integer.toString(dlength)));
		rootElement.appendChild(dlengthNode);
		
		Element xPtsNode = doc.createElement("xPts");
		for(int xVal:xPts){
			Element xNode = doc.createElement("x");
			xNode.appendChild(doc.createTextNode(Integer.toString(xVal)));
			xPtsNode.appendChild(xNode);
		}
		rootElement.appendChild(xPtsNode);
		
		Element yPtsNode = doc.createElement("yPts");
		for(int yVal:yPts){
			Element yNode = doc.createElement("y");
			yNode.appendChild(doc.createTextNode(Integer.toString(yVal)));
			yPtsNode.appendChild(yNode);
		}
		rootElement.appendChild(yPtsNode);
		
		Element isFinalNode = doc.createElement("isFinal");
		isFinalNode.appendChild(doc.createTextNode(Boolean.toString(isFinal)));
		rootElement.appendChild(isFinalNode);
		
		Element startTimeNode = doc.createElement("startTime");
		startTimeNode.appendChild(doc.createTextNode(Integer.toString(startTime)));
		rootElement.appendChild(startTimeNode);
		
		Element endTimeNode = doc.createElement("endTime");
		endTimeNode.appendChild(doc.createTextNode(Integer.toString(endTime)));
		rootElement.appendChild(endTimeNode);
		
		Element dxPtsNode = doc.createElement("dxPts");
		for(int xVal:dxPts){
			Element dxNode = doc.createElement("dx");
			dxNode.appendChild(doc.createTextNode(Integer.toString(xVal)));
			dxPtsNode.appendChild(dxNode);
		}
		rootElement.appendChild(dxPtsNode);
		
		Element dyPtsNode = doc.createElement("dyPts");
		for(int yVal:dyPts){
			Element dyNode = doc.createElement("dy");
			dyNode.appendChild(doc.createTextNode(Integer.toString(yVal)));
			dyPtsNode.appendChild(dyNode);
		}
		rootElement.appendChild(dyPtsNode);
		
		Element lastMovedNode = doc.createElement("lastMoved");
		lastMovedNode.appendChild(doc.createTextNode(Integer.toString(lastMoved)));
		rootElement.appendChild(lastMovedNode);
		
		Element filledNode = doc.createElement("filled");
		filledNode.appendChild(doc.createTextNode(Boolean.toString(filled)));
		rootElement.appendChild(filledNode);
		
    	return rootElement;
    }
    public Element saveData(String type, Document doc){
    	Element rootElement=null;
    	
		rootElement = doc.createElement(type);
		
		Element x1Node = doc.createElement("x1");
		x1Node.appendChild(doc.createTextNode(Integer.toString(x1)));
		rootElement.appendChild(x1Node);
		
		Element y1Node = doc.createElement("y1");
		y1Node.appendChild(doc.createTextNode(Integer.toString(y1)));
		rootElement.appendChild(y1Node);

		Element x2Node = doc.createElement("x2");
		x2Node.appendChild(doc.createTextNode(Integer.toString(x2)));
		rootElement.appendChild(x2Node);
		
		Element y2Node = doc.createElement("y2");
		y2Node.appendChild(doc.createTextNode(Integer.toString(y2)));
		rootElement.appendChild(y2Node);
		
		Element thicknessNode = doc.createElement("thickness");
		thicknessNode.appendChild(doc.createTextNode(Integer.toString(thickness)));
		rootElement.appendChild(thicknessNode);
		
		Element dashedNode = doc.createElement("dashed");
		dashedNode.appendChild(doc.createTextNode(Boolean.toString(dashed)));
		rootElement.appendChild(dashedNode);
		
		Element gradientNode = doc.createElement("gradient");
		gradientNode.appendChild(doc.createTextNode(Boolean.toString(gradient)));
		rootElement.appendChild(gradientNode);
		
		Element myColorNode = doc.createElement("myColor");
		myColorNode.appendChild(doc.createTextNode(Integer.toString(myColor.getRGB())));
		rootElement.appendChild(myColorNode);
		
		Element myColor2Node = doc.createElement("myColor2");
		myColor2Node.appendChild(doc.createTextNode(Integer.toString(myColor2.getRGB())));
		rootElement.appendChild(myColor2Node);
		
		Element dlengthNode = doc.createElement("dlength");
		dlengthNode.appendChild(doc.createTextNode(Integer.toString(dlength)));
		rootElement.appendChild(dlengthNode);
		
		Element xPtsNode = doc.createElement("xPts");
		for(int xVal:xPts){
			Element xNode = doc.createElement("x");
			xNode.appendChild(doc.createTextNode(Integer.toString(xVal)));
			xPtsNode.appendChild(xNode);
		}
		rootElement.appendChild(xPtsNode);
		
		Element yPtsNode = doc.createElement("yPts");
		for(int yVal:yPts){
			Element yNode = doc.createElement("y");
			yNode.appendChild(doc.createTextNode(Integer.toString(yVal)));
			yPtsNode.appendChild(yNode);
		}
		rootElement.appendChild(yPtsNode);
		
		Element isFinalNode = doc.createElement("isFinal");
		isFinalNode.appendChild(doc.createTextNode(Boolean.toString(isFinal)));
		rootElement.appendChild(isFinalNode);
		
		Element startTimeNode = doc.createElement("startTime");
		startTimeNode.appendChild(doc.createTextNode(Integer.toString(startTime)));
		rootElement.appendChild(startTimeNode);
		
		Element endTimeNode = doc.createElement("endTime");
		endTimeNode.appendChild(doc.createTextNode(Integer.toString(endTime)));
		rootElement.appendChild(endTimeNode);
		
		Element dxPtsNode = doc.createElement("dxPts");
		for(int xVal:dxPts){
			Element dxNode = doc.createElement("dx");
			dxNode.appendChild(doc.createTextNode(Integer.toString(xVal)));
			dxPtsNode.appendChild(dxNode);
		}
		rootElement.appendChild(dxPtsNode);
		
		Element dyPtsNode = doc.createElement("dyPts");
		for(int yVal:dyPts){
			Element dyNode = doc.createElement("dy");
			dyNode.appendChild(doc.createTextNode(Integer.toString(yVal)));
			dyPtsNode.appendChild(dyNode);
		}
		rootElement.appendChild(dyPtsNode);
		
		Element lastMovedNode = doc.createElement("lastMoved");
		lastMovedNode.appendChild(doc.createTextNode(Integer.toString(lastMoved)));
		rootElement.appendChild(lastMovedNode);

    	return rootElement;
    }
    public Element saveData(Document doc){
    	Element rootElement=null;
    	return rootElement;
    }
    public Element saveData(){
    	Element rootElement=null;
    	return rootElement;
    }
    public void saveModel(){
    	
    }
    public int [] getXPtsA(){
    	return convertIntegers(xPts);
    }
    public int [] getYPtsA(){
    	return convertIntegers(yPts);
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
    public Point [] getPts(){
    	Point []pts= new Point [xPts.size()];
    	for(int i=0;i<xPts.size();i++){
    		Point p=new Point();
    		p.x=xPts.get(i);
    		p.y=yPts.get(i);
    		pts[i]=p;
    	}
    	return pts;
    }
    public boolean isFinal(){
    	return isFinal;
    }
    //accessor methods for coordinates
	public int getLastMoved() {
		return lastMoved;
	}
    public int getStartTime() {
		return startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public int getDX(int i){
		if(i<dxPts.size()){
			return dxPts.get(i);
		}else{
			return 0;
		}
	}
	public int getDY(int i){
		if(i<dyPts.size()){
			return dyPts.get(i);
		}else{
			return 0;
		}
	}
    public int getX1(){ //accesses the x coordinate for the first point
        return x1;
    }
    public int getX2(){ //accesses the x coordinate for the 2nd point
        return x2;
    }    
    public int getY1(){ //accesses the y coordinate for the first point
        return y1;
    }        
    public int getY2(){ //accesses the y coordinate for the 2nd point
        return y2;
    } 
    public int getThickness (){//accesses thickness of line
        return thickness;
    }
    //accessor method for color
    public Color getColor(){//accesses primary color
        return myColor;
    }    
    public Color getColor2(){//accesses secondary color
        return myColor2;
    }    
    public boolean getDashed(){//accesses whether or not line is dashed
        return dashed;
    }
    public boolean getGradient(){//accesses whether or not to paint with gradient
        return gradient;
    }
    public boolean inEraseRegion(MyShape eraser){
    	return false;
    }
    public boolean inSelected(MyShape selector){
    	return false;
    }
    public boolean hasPoint(int x1, int y1){
    	return false;
    }
    public int getDLength(){//accesses dashlength
        return dlength;
    }
    public boolean pixelInPoly(Point[] verts, Point pos) { //checks if pixel is in polygon
	    int i, j;
	    boolean c=false;
	    int sides = verts.length;
	    for (i=0,j=sides-1;i<sides;j=i++) {
	      if (( ((verts[i].y <= pos.y) && (pos.y < verts[j].y)) || ((verts[j].y <= pos.y) && (pos.y < verts[i].y))) &&
	            (pos.x < (verts[j].x - verts[i].x) * (pos.y - verts[i].y) / (verts[j].y - verts[i].y) + verts[i].x)) {
	        c = !c;
	      }
	    }
	    return c;
	  }
    // Abstract method 
    abstract void draw( Graphics g );
} // end class MyShape