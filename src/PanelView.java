import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PanelView extends JPanel {
	int x, y;
	private JSlider timeSlider;
	private JPanel animationPanel;
	private JButton nextFrame;
	private JButton prevFrame;
	private JButton skipToEnd;
	private JButton skipToBegin;
	private JButton play;
    private JFileChooser fc;
	private int fps = 60;
	private double radians_per_second = 1.0;
	private double cur_angle = 0.0;
	private double radius = 0.3 * 800;
	private Timer t;
	private boolean undoed = false;
	private DynamicStack<MyShape> shapeObjects; // the shapes stack
	private DynamicStack<MyShape> shapeObjects2; // the shapes stack for undoed
													// items
	private int currentShapeType; // the current shape type, 0=line,
									// 1=rectangle, 2=oval
	private MyShape curSelectedObject;
	private MyShape currentShapeObject;// the current shape object
	private Color bgColor=Color.WHITE;
	private Color currentShapeColor = Color.BLACK;// the colour
	private Color currentShapeColor2 = Color.BLACK;// the colour
	private Color color[] = { Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY,
			Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED,
			Color.WHITE, Color.YELLOW };
	private boolean currentShapeFilled;// determines if the current shape is
										// filled
	private boolean currentShapeDashed;// determines if the current shape is
										// filled
	private boolean currentShapeGradient;
	private JLabel statusLabel;// label to display the current mouse position
	private int x1, y1, x2, y2; // coordinates of the shape
	private int thickness = 1;// set default thickness
	private int dlength = 1;// set default dash length
	private int ints[];// array for storing settings from the prefs.txt file
	private boolean booleans[];// array for storing boolean settings from the
								// prefs.txt file
	private String font; // the string character that will be drawn
	private int size; // font size
	private int mode = 0;
	private int fontsize = 18; // sets default font size
	private String string = "Enter fps here";
	private MyShape eraser;
	private MyShape selector;
	private boolean selected = false;
	private boolean drawingSelect = false;
	private boolean timerStarted = false;
	private int PrevX = 0, PrevY = 0;
	private int prevX = 0, prevY = 0;
	private int prevTimeVal = 0;
	private ArrayList<Integer> selectedIndexes = new ArrayList<Integer>();
	private ArrayList<TimerObject> timerObjects = new ArrayList<TimerObject>();
	private boolean freeze = false;

	// constructor that creates a panel
	public PanelView() {
		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		XmlFilter xmlfilter=new XmlFilter();
		fc.setFileFilter(xmlfilter);
		timerObjects.add(new TimerObject(0, 0));
		setBackground(Color.WHITE); // sets the background color to white
		shapeObjects = new DynamicStack<MyShape>();// creates new stack
		shapeObjects2 = new DynamicStack<MyShape>();// creates new stack
		BoxLayout box = new BoxLayout(animationPanel, BoxLayout.X_AXIS);
		GridBagLayout grid = new GridBagLayout();
		animationPanel = new JPanel(grid);
		// animationPanel = new JPanel( new BoxLayout(animationPanel,
		// BoxLayout.X_AXIS));
		animationPanel.setBackground(Color.WHITE); // sets the background colour
													// to white
		Dimension d = new Dimension(500, 30);
		Dimension d2 = new Dimension(300, 30);
		Dimension d3 = new Dimension(800, 30);
		timeSlider = new JSlider(0, 1200, 0); // slider
		timeSlider.setMinorTickSpacing(1); // sets distance between short ticks
		// timeSlider.setPreferredSize(d);
		timeSlider.setPreferredSize(d);
		timeSlider.setMinimumSize(d2);
		timeSlider.setMaximumSize(d);
		nextFrame = new JButton("  >  ");
		prevFrame = new JButton("  <  ");
		skipToEnd = new JButton(" >>  ");
		skipToBegin = new JButton(" <<  ");
		play = new JButton(" [ >  ");

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		animationPanel.add(play);
		animationPanel.add(skipToBegin);
		animationPanel.add(prevFrame);
		animationPanel.add(timeSlider, c);
		animationPanel.add(nextFrame);
		animationPanel.add(skipToEnd);

		setLayout(new BorderLayout());
		add(animationPanel, BorderLayout.SOUTH);

		ButtonHandler buttonHandler = new ButtonHandler();// handles button
															// events
		prevFrame.addActionListener(buttonHandler);
		nextFrame.addActionListener(buttonHandler);
		skipToBegin.addActionListener(buttonHandler);
		skipToEnd.addActionListener(buttonHandler);
		play.addActionListener(buttonHandler);

		SliderHandler handler = new SliderHandler(); // creates a SliderHandler
		timeSlider.addChangeListener(handler);
		// adds event handler

		this.setFocusable(true);
		this.requestFocusInWindow();
		KeyboardHandler keyHandler = new KeyboardHandler();
		MouseHandler pointer = new MouseHandler();
		this.addKeyListener(keyHandler);
		this.addMouseListener(pointer);
		this.addMouseMotionListener(pointer);
		try {
			readFile();
		} catch (FileNotFoundException e) {
		}

		TimerHandler repainter = new TimerHandler();
		t = new Timer(1000 / fps, repainter);

	} // end constructor DrawPanel

	// overloaded constructor to change the current status label to that of the
	// argument
	public PanelView(JLabel label) {
		this();
		statusLabel = label;
	}// end constructor DrawPanel

	public void readFile() throws FileNotFoundException { // reads from file,
															// sets default
															// settings
															// according to file
		FileReader adhd = new FileReader("prefs.txt");
		Scanner in = new Scanner(adhd);

		// reads the elements in the file
		ints = new int[6];
		font = in.nextLine();
		for (int i = 0; i < 6; i++) {
			ints[i] = in.nextInt();
		}
		booleans = new boolean[3];
		for (int i = 0; i < 3; i++) {
			booleans[i] = in.nextBoolean();
		}

		// makes changes to default values of the program accordingly
		currentShapeType = ints[0];
		currentShapeColor = color[ints[1]];
		currentShapeColor = color[ints[2]];
		thickness = ints[3];
		dlength = ints[4];
		fontsize = ints[5];
		currentShapeFilled = booleans[0];
		currentShapeGradient = booleans[1];
		currentShapeDashed = booleans[2];
		string = font;

	}

	public void setFPS(int newFPS) {
		fps = newFPS;
		TimerHandler repainter = new TimerHandler();
		t = new Timer(1000 / fps, repainter);
	}

	// mutator method to set/change the type of the current shape
	public void setCurrentShapeType(int type) {
		currentShapeType = type;
	}// end method setCurrentShapeType

	// mutator method to set/change the current colour
	public void setCurrentShapeColor(Color color) {
		currentShapeColor = color;
	}// end method setCurrentShapeColor

	public void setCurrentShapeColor2(Color color2) {
		currentShapeColor2 = color2;
	}

	public void setMode(int m) {
		mode = m;
		int xx1, yy1, xx2, yy2;
		xx1 = x2 - 8;
		yy1 = y2 - 8;
		xx2 = x2 + 8;
		yy2 = y2 + 8;
		if (mode == 2) {
			selector = null;
			eraser = new MyRectangle(xx1, yy1, xx2, yy2, 1, Color.BLACK,
					Color.BLACK, false, false, false, 1, 1);
		} else if (mode == 1) {
			eraser = null;
			repaint();
		} else if (mode == 0) {
			selector = null;
			eraser = null;
			repaint();
		} else {
			selector = null;
			eraser = null;
			repaint();
		}
	}
	public void setBGColor(Color c){
		bgColor=c;
	}
	public void clearPanel(){
		shapeObjects.makeEmpty();
		shapeObjects2.makeEmpty();
		timerObjects.clear();
		//timerObjects.add(new TimerObject(0,0));
		timeSlider.setValue(0);
	}
	public void loadFile(){
		int returnVal = fc.showOpenDialog(PanelView.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	clearPanel();
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + "." );
        	try {

                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse (file);

                // normalize text representation
                doc.getDocumentElement ().normalize ();
                System.out.println ("Root element of the doc is " + 
                     doc.getDocumentElement().getNodeName());
                Element shapeObjectsEl= (Element)doc.getElementsByTagName("shapeObjects").item(0);
                Element shapeObjects2El= (Element)doc.getElementsByTagName("shapeObjects2").item(0);
                Element timerEls= (Element)doc.getElementsByTagName("timerObjects").item(0);
                NodeList listOfMyShapes = shapeObjectsEl.getElementsByTagName("MyShape");
                NodeList listOfMyShapes2 = shapeObjects2El.getElementsByTagName("MyShape");;
                NodeList timerNodes = timerEls.getElementsByTagName("t");
                System.out.println("no of timerObs :"+ timerNodes.getLength());
                for(int s=0; s<timerNodes.getLength() ; s++){
                	Node tNode = timerNodes.item(s);
                	timerObjects.add(new TimerObject(0,0));
                }
                int totalPersons = listOfMyShapes.getLength();
                System.out.println("Total no of Shapes : " + totalPersons);
                for(int s=0; s<listOfMyShapes.getLength() ; s++){


                    Node shapeNode = listOfMyShapes.item(s);
                    if(shapeNode.getNodeType() == Node.ELEMENT_NODE){
                    	Element shapeElement = (Element)shapeNode.getFirstChild();
                    	String type=shapeElement.getTagName();
                    	System.out.println(type);
                        int Sx1; // x coordinate of first endpoint
                        int Sy1; // y coordinate of first endpoint
                        int Sx2; // x coordinate of second endpoint
                        int Sy2; // y coordinate of second endpoint
                        int Sthickness;  // thickness of the line
                        boolean Sdashed; //if line is dashed
                        boolean Sgradient; //whether or not to draw with gradient
                        Color SmyColor; // color of this shape
                        Color SmyColor2; // color of this shape
                        int Sdlength; //dash length
                        ArrayList<Integer> SxPts=new ArrayList<Integer>();
                        ArrayList<Integer> SyPts=new ArrayList<Integer>();
                        boolean SisFinal;
                        int SstartTime;
                        int SendTime;
                        ArrayList<Integer> SdxPts=new ArrayList<Integer>();
                        ArrayList<Integer> SdyPts=new ArrayList<Integer>();
                        int SlastMoved;
                        NodeList tempN=shapeElement.getElementsByTagName("x1");
                        System.out.println(tempN.getLength());
                        Element c=(Element)tempN.item(0);
                        System.out.println(c.getFirstChild().getNodeValue());
                        
                		Sx1=Integer.parseInt(((Element)shapeElement.getElementsByTagName("x1").item(0)).getFirstChild().getNodeValue());
                		Sx2=Integer.parseInt(((Element)shapeElement.getElementsByTagName("x2").item(0)).getFirstChild().getNodeValue());
                		Sy1=Integer.parseInt(((Element)shapeElement.getElementsByTagName("y1").item(0)).getFirstChild().getNodeValue());
                		Sy2=Integer.parseInt(((Element)shapeElement.getElementsByTagName("y2").item(0)).getFirstChild().getNodeValue());
                		Sthickness=Integer.parseInt(((Element)shapeElement.getElementsByTagName("thickness").item(0)).getFirstChild().getNodeValue());
                		Sdashed=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("dashed").item(0)).getFirstChild().getNodeValue());
                		Sgradient=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("gradient").item(0)).getFirstChild().getNodeValue());
                		SmyColor=new Color(Integer.parseInt(((Element)shapeElement.getElementsByTagName("myColor").item(0)).getFirstChild().getNodeValue()));
                		SmyColor2=new Color(Integer.parseInt(((Element)shapeElement.getElementsByTagName("myColor2").item(0)).getFirstChild().getNodeValue()));
                		Sdlength=Integer.parseInt(((Element)shapeElement.getElementsByTagName("dlength").item(0)).getFirstChild().getNodeValue());
                		SisFinal=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("isFinal").item(0)).getFirstChild().getNodeValue());
                		SstartTime=Integer.parseInt(((Element)shapeElement.getElementsByTagName("startTime").item(0)).getFirstChild().getNodeValue());
                		SendTime=Integer.parseInt(((Element)shapeElement.getElementsByTagName("endTime").item(0)).getFirstChild().getNodeValue());
                		SlastMoved=Integer.parseInt(((Element)shapeElement.getElementsByTagName("lastMoved").item(0)).getFirstChild().getNodeValue());
                		NodeList xPtsNodes=shapeElement.getElementsByTagName("x");
                		NodeList yPtsNodes=shapeElement.getElementsByTagName("y");
                		NodeList dxPtsNodes=shapeElement.getElementsByTagName("dx");
                		NodeList dyPtsNodes=shapeElement.getElementsByTagName("dy");
                		for(int i=0;i<xPtsNodes.getLength();i++){
                			SxPts.add(Integer.parseInt(((Element)xPtsNodes.item(i)).getFirstChild().getNodeValue()));
                			SyPts.add(Integer.parseInt(((Element)yPtsNodes.item(i)).getFirstChild().getNodeValue()));
                		}
                		for(int i=0;i<dxPtsNodes.getLength();i++){
                			SdxPts.add(Integer.parseInt(((Element)dxPtsNodes.item(i)).getFirstChild().getNodeValue()));
                			SdyPts.add(Integer.parseInt(((Element)dyPtsNodes.item(i)).getFirstChild().getNodeValue()));
                		}
                        MyShape curLoadShape;
                    	if(type.equals("MyPen")){
                    		curLoadShape= new MyPen(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyLine")){
                    		curLoadShape= new MyLine(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyOval")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());
                    		curLoadShape = new MyOval(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}else if(type.equals("MyRectangle")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		
                    		curLoadShape = new MyRectangle(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyRoundRect")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		
                    		curLoadShape = new MyRoundRect(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}else{ 
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		
                    		curLoadShape = new MyPolygon(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}
                    	if(SisFinal)
                    		curLoadShape.setFinal();
                    	curLoadShape.setStartTime(SstartTime);
                    	curLoadShape.setEndTime(SendTime);
                    	curLoadShape.setPts(SxPts, SyPts);
                		curLoadShape.setDisplacement(SdxPts, SdyPts);
                		curLoadShape.setLastMoved(SlastMoved);
                		shapeObjects.push(curLoadShape);
                    	System.out.println(shapeElement.getTagName());
                    }
                    


                }//end of for loop with s var
                for(int s=0; s<listOfMyShapes2.getLength() ; s++){


                    Node shapeNode = listOfMyShapes2.item(s);
                    if(shapeNode.getNodeType() == Node.ELEMENT_NODE){
                    	Element shapeElement = (Element)shapeNode.getFirstChild();
                    	String type=shapeElement.getTagName();
                    	System.out.println(type);
                        int Sx1; // x coordinate of first endpoint
                        int Sy1; // y coordinate of first endpoint
                        int Sx2; // x coordinate of second endpoint
                        int Sy2; // y coordinate of second endpoint
                        int Sthickness;  // thickness of the line
                        boolean Sdashed; //if line is dashed
                        boolean Sgradient; //whether or not to draw with gradient
                        Color SmyColor; // color of this shape
                        Color SmyColor2; // color of this shape
                        int Sdlength; //dash length
                        ArrayList<Integer> SxPts=new ArrayList<Integer>();
                        ArrayList<Integer> SyPts=new ArrayList<Integer>();
                        boolean SisFinal;
                        int SstartTime;
                        int SendTime;
                        ArrayList<Integer> SdxPts=new ArrayList<Integer>();
                        ArrayList<Integer> SdyPts=new ArrayList<Integer>();
                        int SlastMoved;
                        NodeList tempN=shapeElement.getElementsByTagName("x1");
                        System.out.println(tempN.getLength());
                        Element c=(Element)tempN.item(0);
                        System.out.println(c.getFirstChild().getNodeValue());
                        
                		Sx1=Integer.parseInt(((Element)shapeElement.getElementsByTagName("x1").item(0)).getFirstChild().getNodeValue());
                		Sx2=Integer.parseInt(((Element)shapeElement.getElementsByTagName("x2").item(0)).getFirstChild().getNodeValue());
                		Sy1=Integer.parseInt(((Element)shapeElement.getElementsByTagName("y1").item(0)).getFirstChild().getNodeValue());
                		Sy2=Integer.parseInt(((Element)shapeElement.getElementsByTagName("y2").item(0)).getFirstChild().getNodeValue());
                		Sthickness=Integer.parseInt(((Element)shapeElement.getElementsByTagName("thickness").item(0)).getFirstChild().getNodeValue());
                		Sdashed=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("dashed").item(0)).getFirstChild().getNodeValue());
                		Sgradient=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("gradient").item(0)).getFirstChild().getNodeValue());
                		SmyColor=new Color(Integer.parseInt(((Element)shapeElement.getElementsByTagName("myColor").item(0)).getFirstChild().getNodeValue()));
                		SmyColor2=new Color(Integer.parseInt(((Element)shapeElement.getElementsByTagName("myColor2").item(0)).getFirstChild().getNodeValue()));
                		Sdlength=Integer.parseInt(((Element)shapeElement.getElementsByTagName("dlength").item(0)).getFirstChild().getNodeValue());
                		SisFinal=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("isFinal").item(0)).getFirstChild().getNodeValue());
                		SstartTime=Integer.parseInt(((Element)shapeElement.getElementsByTagName("startTime").item(0)).getFirstChild().getNodeValue());
                		SendTime=Integer.parseInt(((Element)shapeElement.getElementsByTagName("endTime").item(0)).getFirstChild().getNodeValue());
                		SlastMoved=Integer.parseInt(((Element)shapeElement.getElementsByTagName("lastMoved").item(0)).getFirstChild().getNodeValue());
                		NodeList xPtsNodes=shapeElement.getElementsByTagName("x");
                		NodeList yPtsNodes=shapeElement.getElementsByTagName("y");
                		NodeList dxPtsNodes=shapeElement.getElementsByTagName("dx");
                		NodeList dyPtsNodes=shapeElement.getElementsByTagName("dy");
                		for(int i=0;i<xPtsNodes.getLength();i++){
                			SxPts.add(Integer.parseInt(((Element)xPtsNodes.item(i)).getFirstChild().getNodeValue()));
                			SyPts.add(Integer.parseInt(((Element)yPtsNodes.item(i)).getFirstChild().getNodeValue()));
                		}
                		for(int i=0;i<dxPtsNodes.getLength();i++){
                			SdxPts.add(Integer.parseInt(((Element)dxPtsNodes.item(i)).getFirstChild().getNodeValue()));
                			SdyPts.add(Integer.parseInt(((Element)dyPtsNodes.item(i)).getFirstChild().getNodeValue()));
                		}
                        MyShape curLoadShape;
                    	if(type.equals("MyPen")){
                    		curLoadShape= new MyPen(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyLine")){
                    		curLoadShape= new MyLine(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyOval")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());
                    		curLoadShape = new MyOval(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}else if(type.equals("MyRectangle")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		curLoadShape = new MyRectangle(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    		
                    	}else if(type.equals("MyRoundRect")){
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		curLoadShape = new MyRoundRect(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}else{ 
                    		boolean Sfilled=Boolean.parseBoolean(((Element)shapeElement.getElementsByTagName("filled").item(0)).getFirstChild().getNodeValue());                    		curLoadShape = new MyPolygon(Sx1, Sy1, Sx2, Sy2,
									Sthickness, SmyColor,
									SmyColor2, Sgradient,
									Sfilled, 
									Sdashed, Sdlength);
                    	}
                    	if(SisFinal)
                    		curLoadShape.setFinal();
                    	curLoadShape.setStartTime(SstartTime);
                    	curLoadShape.setEndTime(SendTime);
                    	curLoadShape.setPts(SxPts, SyPts);
                		curLoadShape.setDisplacement(SdxPts, SdyPts);
                		curLoadShape.setLastMoved(SlastMoved);
                		shapeObjects.push(curLoadShape);
                    	System.out.println(shapeElement.getTagName());
                    }
                }


            }catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line " 
                 + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());

            }catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();

            }catch (Throwable t) {
            t.printStackTrace ();
            }

        } else {
        	System.out.println("Open command cancelled by user." );
        }
		System.out.println("load clicked");
	}
	public void save() {
        int returnVal = fc.showSaveDialog(PanelView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would save the file.
            int tempTime=timeSlider.getValue();
            timeSlider.setValue(0);
           
            try {
    			DocumentBuilderFactory docFactory = DocumentBuilderFactory
    					.newInstance();
    			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    			TransformerFactory transformerFactory = TransformerFactory
    					.newInstance();
    			Transformer transformer = transformerFactory.newTransformer();
    			Document doc = docBuilder.newDocument();
    			Element rootNode = doc.createElement("Root");
    			doc.appendChild(rootNode);
    			Element timerObjectsNode = doc.createElement("timerObjects");
    			for(TimerObject t:timerObjects){
    				Element tNode = doc.createElement("t");
    				tNode.appendChild(doc.createTextNode(Integer.toString(t.dx)));
    				timerObjectsNode.appendChild(tNode);
    			}
    			rootNode.appendChild(timerObjectsNode);
    			Element shapeObjectsNode = doc.createElement("shapeObjects");
    			for (int i = shapeObjects.getSize()-1; i >=0; i--) {
    				Element MyShapeNode = doc.createElement("MyShape");
    				MyShape s = shapeObjects.get(i);
    				Element shapeEl = s.saveData(doc);
    				MyShapeNode.appendChild(shapeEl);
    				shapeObjectsNode.appendChild(MyShapeNode);
    			}

    			Element shapeObjects2Node = doc.createElement("shapeObjects2");
    			for (int i = shapeObjects2.getSize()-1; i >=0; i--) {
    				Element MyShapeNode = doc.createElement("MyShape");
    				MyShape s = shapeObjects2.get(i);
    				Element shapeEl = s.saveData(doc);
    				MyShapeNode.appendChild(shapeEl);
    				shapeObjectsNode.appendChild(MyShapeNode);
    			}
    			rootNode.appendChild(shapeObjectsNode);
    			rootNode.appendChild(shapeObjects2Node);

    			DOMSource source = new DOMSource(doc);
    			StreamResult result = new StreamResult(new File("E:\\349\\file.xml"));
    		    String extension = Utils.getExtension(file);
    		    if (extension != null) {
    		        if (extension.equals(Utils.xml) ||
    		            extension.equals(Utils.XML)) {
    		        	result = new StreamResult(new File(file.getAbsoluteFile()+""));
    		        } else {
    		        	result = new StreamResult(new File(file.getAbsoluteFile()+".xml"));
    		        }
    		    }else{
    		    	result = new StreamResult(new File(file.getAbsoluteFile()+".xml"));
    		    }
    			System.out.println(file.getAbsoluteFile()+"");
    			//StreamResult result = new StreamResult(new File(file.getName()));
        		transformer.transform(source, result);
        		System.out.println("File saved!");
        		timeSlider.setValue(tempTime);
    		} catch (ParserConfigurationException pce) {
    			pce.printStackTrace();
    		} catch (TransformerException tfe) {
    			tfe.printStackTrace();
    		}
        } else {
        	System.out.println("Save command cancelled by user." );
        }
		

	}

	public void setThickness(int thickness1) {
		thickness = thickness1;
	}// end method setCurrentShapeColor

	public void setCurShapeDLength(int dlength1) {
		dlength = dlength1;
	}// end method setCurrentShapeColor
		// mutator method to set/change the filled status

	public void setCurrentShapeFilled(boolean filled) {
		currentShapeFilled = filled;
	}// end method setCurrentShapeFilled

	public void setCurrentShapeDashed(boolean dashed) {
		currentShapeDashed = dashed;
	}// end method setCurrentShapeFilled
		// pops the previous shape drawn from the stack, adds into the undoed
		// stack

	public void clearLastShape() {
		if (shapeObjects.getSize() > 0) {
			shapeObjects2.push(shapeObjects.pop());
			undoed = true;
			repaint();
		}
	}// end method clearLastShape

	public void clearShape(int i) {
		shapeObjects2.push(shapeObjects.remove(i));
		undoed = true;
		repaint();
	}

	public void setCurrentShapeGradient(boolean gr) {
		currentShapeGradient = gr;
	}

	// clears the the drawing panel
	public void clearDrawing() {
		shapeObjects.makeEmpty();
		selectedIndexes.clear();
		selector = null;
		selected = false;
		repaint();
	}// end method clearDrawing

	public void clearDrawing2() {
		shapeObjects2.makeEmpty();
		repaint();
	}// end method clearDrawing

	public boolean isEmpty() {
		return shapeObjects.isEmpty();
	}

	public void setFreeze(boolean fr) {
		freeze = fr;
	}

	public void setString(String string1) {
		string = string1;
	}// end method setCurrentShapeColor

	public void setFontSize(int size) {
		fontsize = size;
	}// end method setCurrentShapeColor

	public void redoprev() {
		if (shapeObjects2.getSize() != 0) {
			// redraws the undoed item
			MyShape drawShape1 = shapeObjects2.dequeue();
			shapeObjects.push(drawShape1);
			repaint();
		}
	}

	public void transObjects(int dx, int dy, int curTime) {
		for (int i = 0; i < shapeObjects.getSize(); i++) {

		}
	}

	public void insertFrame() {
		MyShape cur;
		for (int i = 0; i < shapeObjects.getSize(); i++) {
			cur = shapeObjects.get(i);
			cur.insertDX(timeSlider.getValue(), 0);
			cur.insertDY(timeSlider.getValue(), 0);
		}
		if (!(timerObjects.size() - 1 > timeSlider.getValue()))
			timerObjects.add(new TimerObject(0, 0));
		timeSlider.setValue(timeSlider.getValue() + 1);
	}

	public void storeFrame() {

		int dx = selector.getX2() - prevX;
		int dy = selector.getY2() - prevY;

		MyShape cur;
		for (int i = 0; i < selectedIndexes.size(); i++) {
			cur = shapeObjects.get(selectedIndexes.get(i));
			cur.setDX(timeSlider.getValue(), dx);
			cur.setDY(timeSlider.getValue(), dy);
		}
		prevX = selector.getX2();
		prevY = selector.getY2();
		boolean unique = false;
		for (int i = 0; i < shapeObjects.getSize(); i++) {
			if (!selectedIndexes.contains(i)) {
				cur = shapeObjects.get(i);
				int dxx = cur.getDX(timeSlider.getValue());
				int dyy = cur.getDY(timeSlider.getValue());
				if (dxx != 0 || dyy != 0) {
					cur.trans(dxx, dyy);
					System.out.println("dx: " + dxx + "  dy: " + dyy
							+ "  timer:" + timeSlider.getValue());
				}
				for (int j = timeSlider.getValue(); j < timerObjects.size(); j++) {
					dxx = cur.getDX(j);
					dyy = cur.getDY(j);
					if (dxx != 0 || dyy != 0) {
						unique = true;
					}
				}
			}
		}
		if (timeSlider.getValue() + 1 > timerObjects.size()) {
			timeSlider.setValue(timerObjects.size() - 1);
		}
		if ((timerObjects.size() > timeSlider.getValue() + 1) && !unique) {
			for (int i = timerObjects.size() - 1; i >= timeSlider.getValue() + 1; i--) {
				timerObjects.remove(i);
			}
		}
		if (!(timerObjects.size() - 1 > timeSlider.getValue()))
			timerObjects.add(new TimerObject(dx, dy));
		if (timeSlider.getValue() == timeSlider.getMaximum()) {
			timeSlider.setMaximum(timeSlider.getMaximum() + 1200);
		}
		timeSlider.setValue(timeSlider.getValue() + 1);
	}

	private class XmlFilter extends FileFilter{
		public boolean accept(File f) {
		    if (f.isDirectory()) {
		        return true;
		    }

		    String extension = Utils.getExtension(f);
		    if (extension != null) {
		        if (extension.equals(Utils.xml) ||
		            extension.equals(Utils.XML)) {
		                return true;
		        } else {
		            return false;
		        }
		    }

		    return false;
		}
		public String getDescription() {
		    return "XML files only";
		}
		public String getTypeDescription(File f) {
		    String extension = Utils.getExtension(f);
		    String type = null;

		    if (extension != null) {
		        if (extension.equals(Utils.jpeg) ||
		            extension.equals(Utils.jpg)) {
		            type = "JPEG Image";
		        } else if (extension.equals(Utils.gif)){
		            type = "GIF Image";
		        } else if (extension.equals(Utils.tiff) ||
		                   extension.equals(Utils.tif)) {
		            type = "TIFF Image";
		        } else if (extension.equals(Utils.png)){
		            type = "PNG Image";
		        }
		    }
		    return type;
		}
	}
	private class KeyboardHandler implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == 'F' || e.getKeyChar() == 'f') {
				/*
				 * boolean op=!freezed.isSelected(); freezed.setSelected(op);
				 */
				setFreeze(!freeze);
				System.out.println("typed");
			}
		}

		public void keyTyped(KeyEvent e) {
			System.out.println("typed");
		}

		public void keyReleased(KeyEvent e) {
			System.out.println("releases");
		}
	}

	private class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (selected) {
				storeFrame();
				// System.out.println("dx: "+dx+"  dy: "+ dy);
				cur_angle += (radians_per_second / ((double) fps));
				x = (int) (radius * Math.cos(cur_angle));
				y = (int) (radius * Math.sin(cur_angle));
			} else {
				MyShape cur;
				for (int i = 0; i < shapeObjects.getSize(); i++) {
					cur = shapeObjects.get(i);
					int dx = cur.getDX(timeSlider.getValue());
					int dy = cur.getDY(timeSlider.getValue());
					if (dx != 0 || dy != 0) {
						cur.trans(dx, dy);
					}
				}
				if (timeSlider.getValue() < timerObjects.size()) {
					timeSlider.setValue(timeSlider.getValue() + 1);
				} else {
					play.setText(" [ >  ");
					timerStarted = false;
					t.stop();
				}

				repaint();
			}
		}
	}

	public class TimerObject {
		int dx;
		int dy;

		public TimerObject(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

	}

	private class ButtonHandler implements ActionListener // handles button
															// events
	{
		public void actionPerformed(ActionEvent event) {
			// determines which button is pressed
			if (event.getSource() == prevFrame)// if user clicked undo button
			{
				timeSlider.setValue(timeSlider.getValue() - 1);
				// clearLastShape(); //clears the previous shape drawn
			} else if (event.getSource() == nextFrame)// if user clicked the
														// clear button
			{
				if (timeSlider.getValue() < timerObjects.size())
					timeSlider.setValue(timeSlider.getValue() + 1);
				// redoprev(); //clears everything
			} else if (event.getSource() == play)// if user clicked the clear
													// button
			{
				if (!timerStarted) {
					System.out.println("playing");
					play.setText("  ||  ");
					timerStarted = true;
					t.start();
				} else {
					play.setText(" [ >  ");
					timerStarted = false;
					t.stop();
				}

				// clearDrawing();
			} else if (event.getSource() == skipToBegin) {
				timeSlider.setValue(0);
			} else if (event.getSource() == skipToEnd) {
				System.out.println(timerObjects.size());
				timeSlider.setValue(timerObjects.size());
			}
		} // end method actionPerformed
	} // end private inner class ButtonHandler

	private class SliderHandler implements ChangeListener // handles slider
															// events
	{
		// handle slider event
		public void stateChanged(ChangeEvent event) {
			JSlider curSlider = (JSlider) event.getSource(); // creates a
																// reference to
																// the current
																// slider
			if (curSlider == timeSlider) {
				if (timeSlider.getValue() > timerObjects.size()) {
					timeSlider.setValue(timerObjects.size());
				}
				if (!timerStarted && !selected) {
					int dTime = timeSlider.getValue() - prevTimeVal;
					if (dTime > 0) {
						// System.out.println("goin forward");
						for (int i = prevTimeVal; i < timeSlider.getValue(); i++) {
							MyShape cur;
							for (int j = 0; j < shapeObjects.getSize(); j++) {
								cur = shapeObjects.get(j);
								int dx = cur.getDX(i);
								int dy = cur.getDY(i);
								if (dx != 0 || dy != 0) {
									cur.trans(dx, dy);
								}
							}
						}
						repaint();
					} else {
						// System.out.println("goin back");
						for (int i = prevTimeVal - 1; i >= timeSlider
								.getValue(); i--) {
							MyShape cur;
							for (int j = 0; j < shapeObjects.getSize(); j++) {
								cur = shapeObjects.get(j);
								int dx = cur.getDX(i);
								int dy = cur.getDY(i);
								if (dx != 0 || dy != 0) {
									// System.out.println("dx: "+dx+"  dy: "+ dy
									// +"  timer:"+i);
									cur.trans(-dx, -dy);
								}
							}
						}
						repaint();
					}
					if (selected == false)
						selector = null;
					// selector=null;
				} else {
					if (!selected) { // play

					}
				}
				prevTimeVal = timeSlider.getValue();
			}
			// setThickness( curSlider.getValue() ); //sets line thickness
		} // end method stateChanged
	} // end private inner class SliderHandler
	// inner class, event handler for mouse

	private class MouseHandler extends MouseAdapter implements
			MouseMotionListener {
		// handles event when the user moves the mouse
		public void mouseMoved(MouseEvent event) {
			x2 = event.getX();
			y2 = event.getY();
			if (eraser != null) {
				int xx1, yy1, xx2, yy2;
				xx1 = x2 - 8;
				yy1 = y2 - 8;
				xx2 = x2 + 8;
				yy2 = y2 + 8;
				eraser.setX1(xx1);
				eraser.setX2(xx2);
				eraser.setY1(yy1);
				eraser.setY2(yy2);
				repaint();
			}
			statusLabel.setText(String.format("(%d, %d)", event.getX(),
					event.getY())); // displays mouse position
		} // end method mouseMoved

		public void mousePressed(MouseEvent event)// draws the shape when mouse
													// is released after
													// dragging
		{
			if (!play.getText().equals("  ||  ")) {

				x1 = event.getX();
				y1 = event.getY();
				x2 = event.getX();
				y2 = event.getY();
				if (eraser != null) {
					int xx1, yy1, xx2, yy2;
					xx1 = x2 - 8;
					yy1 = y2 - 8;
					xx2 = x2 + 8;
					yy2 = y2 + 8;
					eraser.setX1(xx1);
					eraser.setX2(xx2);
					eraser.setY1(yy1);
					eraser.setY2(yy2);
					repaint();
				}
				try { // prevents NullPointerException from being displayed in
						// the interactions panel
						// set the x/y values to current coordinates of the
						// mouse

					if (undoed == true) {// if previous action was undo, disable
											// the redo
						clearDrawing2();
					}

					undoed = false;
					if (mode == 0) {
						// determines what the current shape object is, then
						// creates a new object of that type
						// index of 0 correseponds to a line, 1 to a rectangle,
						// 2 to a rounded rectangle, 3 to an oval and 4 to a
						// String
						if (currentShapeType == 0) {
							currentShapeObject = new MyPen(x1, y1, x2, y2,
									thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeDashed, dlength);
						} else if (currentShapeType == 1) {
							currentShapeObject = new MyLine(x1, y1, x2, y2,
									thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeDashed, dlength);
						} else if (currentShapeType == 2) {
							currentShapeObject = new MyRectangle(x1, y1, x2,
									y2, thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeFilled, currentShapeDashed,
									dlength);
						} else if (currentShapeType == 3) {
							currentShapeObject = new MyRoundRect(x1, y1, x2,
									y2, thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeFilled, currentShapeDashed,
									dlength);
						} else if (currentShapeType == 4) {
							currentShapeObject = new MyOval(x1, y1, x2, y2,
									thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeFilled, currentShapeDashed,
									dlength);
						} else if (currentShapeType == 5) {
							currentShapeObject = new MyPolygon(x1, y1, x2, y2,
									thickness, currentShapeColor,
									currentShapeColor2, currentShapeGradient,
									currentShapeFilled, currentShapeDashed,
									dlength);
						}
					} else if (mode == 2) {
						for (int i = 0; i < shapeObjects.getSize(); i++) {
							MyShape temp = shapeObjects.get(i);
							if (temp.inEraseRegion(eraser)) {
								temp.setEndTime(timeSlider.getValue());
							}
						}
					} else if (mode == 1) {
						if (selector == null || !selector.hasPoint(x1, y1)) {
							selector = new MyPolygon(x1, y1, x2, y2, 3,
									Color.PINK, Color.BLACK, false, false,
									true, 5);
							drawingSelect = true;
							selectedIndexes.clear();
						} else {
							PrevX = x1;
							PrevY = y1;
							selected = true;
						}
					} else {
						insertFrame();
					}
					repaint();
				} catch (NullPointerException e) {
				}
			}
		} // end method mousePressed

		public void mouseDragged(MouseEvent event)// continuously updates the
													// coordinates as the user
													// drags and redraws the
													// shape
		{
			if (!play.getText().equals("  ||  ")) {
				x2 = event.getX();
				y2 = event.getY();
				if (eraser != null) {
					int xx1, yy1, xx2, yy2;
					xx1 = x2 - 8;
					yy1 = y2 - 8;
					xx2 = x2 + 8;
					yy2 = y2 + 8;
					eraser.setX1(xx1);
					eraser.setX2(xx2);
					eraser.setY1(yy1);
					eraser.setY2(yy2);
					for (int i = 0; i < shapeObjects.getSize(); i++) {
						MyShape temp = shapeObjects.get(i);
						if (temp.inEraseRegion(eraser)) {
							temp.setEndTime(timeSlider.getValue());
						}
					}
					repaint();
				}
				if (selector != null) {
					if (selector.hasPoint(x2, y2) && selected) {
						int dx = x2 - PrevX;
						int dy = y2 - PrevY;
						PrevX = x2;
						PrevY = y2;
						if (!timerStarted && !freeze) {
							System.out.println("timer started");
							t.start();
							timerStarted = true;
							prevX = selector.getX2();
							prevY = selector.getY2();
							selector.trans(dx, dy);
							MyShape cur;
							for (int i = 0; i < selectedIndexes.size(); i++) {
								cur = shapeObjects.get(selectedIndexes.get(i));
								cur.setEndTime(36000);
								cur.trans(dx, dy);
							}
							storeFrame();
						} else {
							MyShape cur;
							for (int i = 0; i < selectedIndexes.size(); i++) {
								cur = shapeObjects.get(selectedIndexes.get(i));
								cur.trans(dx, dy);
							}
							selector.trans(dx, dy);
						}

					} else {
						if (drawingSelect) {
							selector.setX2(x2);
							selector.setY2(y2);
						} else {
							timerStarted = false;
							t.stop();
							System.out.println("timer stopped");
							int dx = x2 - PrevX;
							int dy = y2 - PrevY;
							// prevX=selector.getX2();
							// prevY=selector.getY2();
							selector.trans(x2 - PrevX, y2 - PrevY);
							MyShape cur;
							for (int i = 0; i < selectedIndexes.size(); i++) {
								cur = shapeObjects.get(selectedIndexes.get(i));
								cur.trans(dx, dy);
							}
							storeFrame();
							PrevX = x2;
							PrevY = y2;
							selected = false;
						}
					}
					repaint();
				}
				if (mode == 3) {
					insertFrame();
				}
				try { // prevents NullPointerException from being displayed in
						// the interactions panel
						// gets current coordinates of the mouse
						// updates the coordinates

					currentShapeObject.setX2(x2);
					currentShapeObject.setY2(y2);

					statusLabel.setText(String.format("(%d, %d)", event.getX(),
							event.getY())); // displays mouse position
					repaint();

				} catch (NullPointerException e) {
				}
			}
		} // end method mouseDragged

		public void mouseReleased(MouseEvent event) {
			if (!play.getText().equals("  ||  ")) {
				x2 = event.getX();
				y2 = event.getY();
				if (eraser != null) {
					int xx1, yy1, xx2, yy2;
					xx1 = x2 - 8;
					yy1 = y2 - 8;
					xx2 = x2 + 8;
					yy2 = y2 + 8;
					eraser.setX1(xx1);
					eraser.setX2(xx2);
					eraser.setY1(yy1);
					eraser.setY2(yy2);
					repaint();
				}
				if (selector != null) {
					if (selector.hasPoint(x2, y2) && selected) {
						if (timerStarted) {
							timerStarted = false;
							t.stop();
							int dx = x2 - PrevX;
							int dy = y2 - PrevY;
							// prevX=selector.getX2();
							// prevY=selector.getY2();
							selector.trans(x2 - PrevX, y2 - PrevY);
							MyShape cur;
							for (int i = 0; i < selectedIndexes.size(); i++) {
								cur = shapeObjects.get(selectedIndexes.get(i));
								cur.trans(dx, dy);
							}
							storeFrame();
							PrevX = x2;
							PrevY = y2;
							selected = false;
							System.out.println(timeSlider.getValue());
						} else {
							int dx = x2 - PrevX;
							int dy = y2 - PrevY;
							// prevX=selector.getX2();
							// prevY=selector.getY2();
							selector.trans(x2 - PrevX, y2 - PrevY);
							PrevX = x2;
							PrevY = y2;
							selected = false;
							System.out.println(timeSlider.getValue());
						}
						// selectedIndexes.clear();
					} else {
						if (drawingSelect) {
							selector.setX2(x2);
							selector.setY2(y2);
							selector.setFinal();

							MyShape cur;
							for (int i = 0; i < shapeObjects.getSize(); i++) {
								cur = shapeObjects.get(i);
								if (cur.inSelected(selector)) {
									selectedIndexes.add(i);
								}
							}
							drawingSelect = false;
						}
					}
					repaint();
				}
				try { // prevents NullPointerException from being displayed in
						// the interactions panel
						// gets current coordinates of the mouse
						// updates the coordinates

					currentShapeObject.setX2(x2);
					currentShapeObject.setY2(y2);
					currentShapeObject.setFinal();
					currentShapeObject.setStartTime(timeSlider.getValue());
					currentShapeObject.setEndTime(36000);
					shapeObjects.push(currentShapeObject); // adds the shape to
															// the stack
					repaint();
					currentShapeObject = null; // the currentShapeObject is made
												// to null, resetting it
				} catch (NullPointerException e) {
				}
			}
		} // end method mouseReleased

		// handler for when mouse is outside panel
		public void mouseExited(MouseEvent event) {
			selected = false;
			statusLabel.setText("Mouse outside JPanel");
			System.out.println("Mouse exited");
		} // end method mouseExited
	} // end inner class MouseHandler

	// draws the individual shapes for each item in the stack
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(bgColor);
		MyShape drawShape; // MyShape variable for temporary storage of the
							// MyShape items in the stack

		// calling individual draw methods of each MyShape subclass to draw the
		// individual shapes from the stack
		if (shapeObjects.getSize() != 0) {
			// goes through the stack in reverse order, drawing the shape each
			// time
			for (int i = shapeObjects.getSize() - 1; i >= 0; i--) {
				drawShape = shapeObjects.get(i);
				if (drawShape.getStartTime() <= timeSlider.getValue()
						&& drawShape.getEndTime() > timeSlider.getValue()) {
					drawShape.draw(g);
				}
			}
		}

		// calls the draw method to draw the currentShapeObject object
		if (currentShapeObject != null) {
			currentShapeObject.draw(g);
		}
		if (eraser != null) {
			eraser.draw(g);
		}
		if (selector != null) {
			selector.draw(g);
		}
		Graphics2D g2 = (Graphics2D) g; // converts to Graphics2D
		g2.setStroke(new BasicStroke(1));
	} // end method paintComponent
} // end class DrawPanel