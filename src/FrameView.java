
import java.util.Scanner;
import java.awt.GradientPaint;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JInternalFrame;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JTextField;

public class FrameView extends JFrame
{
    private JSlider slider; //jslider for line width
    private JSlider slider2; //jslider for dashlength
    private JSlider slider3; //jslider for font size
    private JSlider timeSlider;
    private JLabel statusBar; // the status of the mouse, displays the coordinates or if mouse is outside panel
    private JLabel label; //label to tell user that the jslider is for line width
    private JLabel modeLabel;
    private PanelView panel; // the interactions panel, where drawing occurs
    private JPanel optionPanel, animationPanel; // the panel with the options for drawing
    private JButton chooseColor1; // the undo button
    private JButton chooseColor2; // the undo button
    private JButton undo; // the undo button
    private JButton redo; // the undo button
    private JButton clear; // the clear button
    private JButton nextFrame;
    private JButton prevFrame;
    private JButton skipToEnd;
    private JButton skipToBegin;
    private JButton play;
    private JComboBox whichShape; // combo box for selecting which shape to draw
    private String shapes[] = { "Pen","Line", "Rectangle", "Round Rect", "Oval", "Polygon" }; // the String array representing the shapes
    private JComboBox whichColour; // combo box for selecting which colour to draw with
    private JComboBox whichColour2; // combo box for selecting which colour to draw with
    private JComboBox selectBGColor;
    private String colours[] = { "Black", "Blue", "Cyan", "Gray", "Green", "Magenta",
        "Orange", "Pink", "Red", "White", "Yellow", "White" }; // String array representing the selection of colours
    private JComboBox whichMode;
    private String modes[]={"Draw", "Select", "Erase", "Add Space"};
    private String options[]= {"About", "Prefs", "Load", "Save", "Exit"}; //file menu items
    private Color color[] = { Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN,
        Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW, Color.WHITE };
    // the Color array that matches the String array                                             
    private JCheckBox isFilled; // the checkbox for if the shape is filled
    private JCheckBox gradient; // the checkbox for if the user wants a gradient
    private JCheckBox dashed; // the checkbox for if the line to be dashed or not
    private JCheckBox freeze; //
    private JMenuBar menu; //menu item below
    private JMenu file; //menu bar
    //menu items
    private JMenuItem about; 
    private JMenuItem prefs;
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;
    private JFileChooser fc;
    
    private int  ints[]; // array of integers to store settings 
    private boolean booleans[]; // array of booleans to store settings
    private JTextField textField1; // text field with set size
    private String string; // default string of textfield
    private String font; // default string of MyString
    private int size; //size of font
    private boolean windowopen = false; //disallows multiple windows bigger than 2
    private Color color1=Color.BLACK;
    private Color color2=Color.BLACK;
    
    // constructor DrawFrame that creates a frame for the application
    public FrameView(){
    	
        // creates a new JPanel object with GridLayout of 1 row, 6 columns, 5 horizontal space and 12 vertical space
        optionPanel = new JPanel( new GridLayout( 2, 9, 2, 2 ) );
        optionPanel.setBackground( Color.WHITE ); // sets the background colour to white
        GridBagLayout grid=new GridBagLayout();
        animationPanel = new JPanel( grid);
        animationPanel.setBackground( Color.WHITE ); // sets the background colour to white
        fc = new JFileChooser();
        //adds elements to the swing components
        textField1 = new JTextField( "60" ); 
        menu = new JMenuBar();//new menu bar
        file = new JMenu("File");//creates a new JMenu
        chooseColor1=new JButton( "Color 1" ); //undo button
        chooseColor2=new JButton( "Color 2" ); //undo button
        undo = new JButton( "Undo Draw" ); //undo button
        redo = new JButton( "Redo Draw" ); //clear button
        clear = new JButton( "Clear All" ); //clear button
        nextFrame= new JButton("  >  ");
        prevFrame=new JButton("  <  ");
        skipToEnd= new JButton(" >>  ");
        skipToBegin= new JButton(" <<  ");
        play= new JButton(" [ >  ");
        whichColour = new JComboBox( colours ); //combo box for primary colour
        whichColour2 = new JComboBox( colours ); //combo box for 2ndary colour
        selectBGColor =new JComboBox(colours);
        whichShape = new JComboBox( shapes ); //combo box for shape
        whichMode= new JComboBox( modes ); //modes
        freeze= new JCheckBox(" Freeze ");
        isFilled = new JCheckBox( "Filled" ); //checkbox for if to fill shape
        gradient = new JCheckBox( "Gradient" ); //check box for if to draw using a gradient
        dashed = new JCheckBox( "Dashed Line" ); //check bo for if to make the line dashed
        about = new JMenuItem("About"); //about item on menu
        prefs = new JMenuItem("Prefs"); // prefs option on menu
        load= new JMenuItem("Load");
        save = new JMenuItem("Save"); // prefs option on menu
        exit = new JMenuItem("Exit"); //exit option on menu
        label=new JLabel("Line width : -->"); //tells user that the jslider is for line width
        modeLabel=new JLabel("Mode : ---------->");
        slider = new JSlider(0, 15, 3); // slider
        slider.setMinorTickSpacing(1); //sets distance between short ticks
        slider.setPaintTicks(true); //sets whether ticks are visible
        slider.setSnapToTicks(true); //sets whether only ticks can selectable
        slider2 = new JSlider(1, 20, 1); // slider2
        slider2.setMinorTickSpacing(2); //sets distance between short ticks
        slider2.setPaintTicks(true); //sets whether ticks are visible
        slider2.setSnapToTicks(true); //sets whether only ticks can selectable
        slider3 = new JSlider(10, 120, 18); // slider3
        slider3.setMinorTickSpacing(5); //sets distance between short ticks
        slider3.setPaintTicks(true); //sets whether ticks are visible
        slider3.setSnapToTicks(true); //sets whether only ticks can selectable
        
        Dimension d= new Dimension(500, 30);
        timeSlider = new JSlider(0, 1200, 0); // slider
        timeSlider.setMinorTickSpacing(1); //sets distance between short ticks
        //timeSlider.setPaintTicks(true); //sets whether ticks are visible
     //   timeSlider.setSnapToTicks(true); //sets whether only ticks can selectable
        timeSlider.setPreferredSize(d);
        whichShape.setSelectedItem( shapes[0] ); //sets default shape to line
        whichColour.setSelectedItem( colours[0] ); //sets default colour to black
        whichColour2.setSelectedItem( colours[0] ); //sets default colour to black
        // adds the swing components to the button panel
        
        //adds all the swing components to the JFrame
        optionPanel.add( menu );//add menu into the panel
        file.add(about);
        file.add(prefs);
        file.add(load);
        file.add(save);
        file.add(exit);;
        menu.add(file);
        optionPanel.add( undo );
        optionPanel.add( redo );
        optionPanel.add( clear );
        optionPanel.add( chooseColor1 );
        //optionPanel.add( whichColour );
        optionPanel.add( chooseColor2 );
        //optionPanel.add( whichColour2 );
        optionPanel.add( whichShape );
        optionPanel.add( isFilled );  
        optionPanel.add( gradient );  
        optionPanel.add( label ); 
        optionPanel.add( slider );
        optionPanel.add( dashed );  
        optionPanel.add( slider2 );
        optionPanel.add(freeze);
        optionPanel.add( modeLabel );
        //optionPanel.add( slider3 );
        optionPanel.add( whichMode );
       // optionPanel.add(textField1);
      //  optionPanel.add(selectBGColor);
        //optionPanel.add(fc);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx=0.5;
        /*
        animationPanel.add(play);
        animationPanel.add(skipToBegin);
        animationPanel.add(prevFrame);
        animationPanel.add(timeSlider,c);
        animationPanel.add(nextFrame);
        animationPanel.add(skipToEnd);*/
        // creates a status bar and passes it as an argument to a DrawPanel object
        statusBar = new JLabel();
        panel = new PanelView( statusBar );
        panel.setThickness(3);
        
        SliderHandler handler = new SliderHandler(); //creates a SliderHandler
        slider.addChangeListener( handler ); //sets the event handlers
        slider2.addChangeListener( handler ); //sets the event handlers
        slider3.addChangeListener( handler ); //sets the event handlers
        timeSlider.addChangeListener( handler );
        
        // adds event handlers to make them handle any events that occur
        ButtonHandler buttonHandler = new ButtonHandler();//handles button events
        undo.addActionListener( buttonHandler );
        redo.addActionListener( buttonHandler );
        clear.addActionListener( buttonHandler );
        chooseColor1.addActionListener( buttonHandler );
        chooseColor2.addActionListener( buttonHandler );
        prevFrame.addActionListener( buttonHandler );
        nextFrame.addActionListener( buttonHandler );
        skipToBegin.addActionListener( buttonHandler );
        skipToEnd.addActionListener( buttonHandler );
        play.addActionListener( buttonHandler );
        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();//handles combo box events
        whichColour.addItemListener( comboBoxHandler );
        whichColour2.addItemListener( comboBoxHandler );
        selectBGColor.addItemListener( comboBoxHandler );
        whichShape.addItemListener( comboBoxHandler );
        whichMode.addItemListener(comboBoxHandler);
        CheckBoxHandler checkBoxHandler = new CheckBoxHandler();//handles check box events
        isFilled.addItemListener( checkBoxHandler );
        dashed.addItemListener( checkBoxHandler );
        gradient.addItemListener( checkBoxHandler );
        freeze.addItemListener( checkBoxHandler );
        MenuHandler menuhandler = new MenuHandler();//handles menu events
        about.addActionListener( menuhandler );
        prefs.addActionListener( menuhandler );
        load.addActionListener( menuhandler );
        save.addActionListener( menuhandler );
        exit.addActionListener( menuhandler );
        TextFieldHandler tfhandler = new TextFieldHandler ();//handles textfield events
        textField1.addActionListener( tfhandler );
        // add the panels to the JFrame
        add( optionPanel, BorderLayout.NORTH );
        add( panel, BorderLayout.CENTER );
        add( statusBar, BorderLayout.SOUTH );

        KeyboardHandler keyHandler=new KeyboardHandler();
       // this.addKeyListener(keyHandler);
        try{
            readFile(); //reads file to readjust default values
        }catch (FileNotFoundException e){
        }
    } // end constructor
    
    public void readFile()throws FileNotFoundException { //reads the preferences file and adjust settings accordingly
        FileReader adhd = new FileReader ("prefs.txt"); 
        Scanner in = new Scanner(adhd); 
        ints = new int [6]; //creates array of integers
        font = in.nextLine(); //gets the default font
        for( int i = 0; i<6 ; i++ ){
            ints[i] = in.nextInt();
        }
        booleans = new boolean [3];
        for( int i = 0; i < 3 ; i++ ){
            booleans[i] = in.nextBoolean();
        }
        
        whichShape.setSelectedIndex( ints[0] );
        whichColour.setSelectedIndex( ints[1] );
        whichColour2.setSelectedIndex( ints[2] );
        slider.setValue(  ints[3] );
        slider2.setValue(  ints[4] );
        slider3.setValue( ints[5]) ;
        isFilled.setSelected( booleans[0] );
        gradient.setSelected( booleans[1] );
        dashed.setSelected( booleans[2] );
        textField1.setText( font );
        
    }
    private class KeyboardHandler implements KeyListener{
    	public void keyPressed(KeyEvent e) {
    		if(e.getKeyChar()=='F'||e.getKeyChar()=='f'){
    			boolean op=!freeze.isSelected();
    			freeze.setSelected(op);
    			panel.setFreeze(op);
    		}
    	}
    	public void keyTyped(KeyEvent e) {
    		System.out.println("typed");
    	}
    	public void keyReleased(KeyEvent e) {
    		System.out.println("releases");
    	}
    }
    private class ButtonHandler implements ActionListener // handles button events
    {
        public void actionPerformed( ActionEvent event ){
            // determines which button is pressed
            if ( event.getSource() == undo )//if user clicked undo button 
            {
                panel.clearLastShape(); //clears the previous shape drawn
            }
            else if ( event.getSource() == clear )//if user clicked the clear button
            {
                panel.clearDrawing(); //clears everything
            }
            else if ( event.getSource() == redo )//if user clicked the clear button
            {
                panel.redoprev(); //clears everything
            }else if ( event.getSource() == chooseColor1 )//if user clicked the clear button
            {
                panel.redoprev(); //clears everything
                color1 = JColorChooser.showDialog(null, "Dialog Title", Color.BLACK);
                chooseColor1.setBackground(color1);
                panel.setCurrentShapeColor(color1);
            }
            else if ( event.getSource() == chooseColor2 )//if user clicked the clear button
            {
                panel.redoprev(); //clears everything
                color2 = JColorChooser.showDialog(null, "Dialog Title", Color.BLACK);
                chooseColor2.setBackground(color2);
                panel.setCurrentShapeColor2(color2);
            }
        } // end method actionPerformed
    } // end private inner class ButtonHandler
    private class ComboBoxHandler implements ItemListener  // handles combo box events
    {
        public void itemStateChanged( ItemEvent event ){
            // determine which combo box is changed
            if ( event.getSource() == whichColour ){//if user decides to change the primary colour
                if ( event.getStateChange() == ItemEvent.SELECTED ){
                    panel.setCurrentShapeColor( color[whichColour.getSelectedIndex()] );//changes the current the current color
                }
            }
            else if ( event.getSource() == whichShape ){//if user decides to change the shape
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    panel.setCurrentShapeType( whichShape.getSelectedIndex() );//changes current shape to the one selected
                }
            }
            else if ( event.getSource() == whichColour2 ){//if user decides to change the secondary colour of gradient
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    panel.setCurrentShapeColor2( color[whichColour2.getSelectedIndex()] );//changes current shape to the one selected
                }
            }else if ( event.getSource() == whichMode){//if user decides to change the secondary colour of gradient
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    panel.setMode( whichMode.getSelectedIndex());//changes mode to the one selected
                }
            }else if ( event.getSource() == selectBGColor){//if user decides to change the secondary colour of gradient
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    panel.setBGColor( color[selectBGColor.getSelectedIndex()] );//changes mode to the one selected
                }
            }
        } // end method itemStateChanged
    } // end private inner class ComboBoxHandler
    
    private class CheckBoxHandler implements ItemListener // responds to checkbox events
    { 
        public void itemStateChanged( ItemEvent event ){
            JCheckBox curBox = (JCheckBox)event.getSource(); //creates a reference to the current checkbox
            if ( curBox == isFilled ){
                panel.setCurrentShapeFilled( isFilled.isSelected() ); // sets the filled status
            }else if ( curBox == dashed ){
                panel.setCurrentShapeDashed( dashed.isSelected() ); // sets the dashed status
            }else if ( curBox == gradient ){
                panel.setCurrentShapeGradient( gradient.isSelected() ); // sets the gradient status
            }else if (curBox==freeze){
            	System.out.println(freeze.isSelected());
            	panel.setFreeze(freeze.isSelected());
            }
        } // end method itemStateChanged
    } // end private inner class CheckBoxHandler
    private class SliderHandler implements ChangeListener //handles slider events
    {
        // handle slider event
        public void stateChanged( ChangeEvent event )
        {
            JSlider curSlider = (JSlider)event.getSource(); //creates a reference to the current slider
            if ( curSlider==slider )
                panel.setThickness( curSlider.getValue() ); //sets line thickness
            else if ( curSlider==slider2 )
                panel.setCurShapeDLength( curSlider.getValue() ); //sets current shape dash length
            else if ( curSlider==slider3 )
                panel.setFontSize( curSlider.getValue() ); //sets current font size
        } // end method stateChanged
    } // end private inner class SliderHandler
    private class MenuHandler implements ActionListener//handles events of the menu
    {
        public void actionPerformed( ActionEvent e )
        {
            // determine which button was clicked.
            Object o = e.getSource();
            int i = JOptionPane.INFORMATION_MESSAGE;
            //display message
            if ( o == about ){
                JOptionPane.showMessageDialog( null, "Programmed by David S. Wang", "About", i );
            //opens preference window for user to configure default settings
            }else if ( o == prefs ){
                if (!windowopen){
                PrefFrame prefFrame = new PrefFrame(); 
                prefFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                prefFrame.setLocation(200,200);
                prefFrame.setVisible( true ); // display frame
                windowopen=true;
                }
                
            }else if (o==exit){ //if user clicks exit
                System.exit(0);
            }else if (o==save){ //if user clicks exit
                panel.save();
            }else if (o==load){ //if user clicks exit
            	panel.loadFile();
               // panel.save();
            }
        }
    }
    // private inner class for event handling
    private class TextFieldHandler implements ActionListener //handles textfield events
    {
        
        // process textfield events
        public void actionPerformed( ActionEvent event )
        {
            string = String.format( event.getActionCommand() );
            int fps=60;
            try{
            	fps=Integer.parseInt(string);
            }catch(Exception pokemon){
            	System.out.println("Wild pikachu appeared");
            	//gotta catch em all
            }
            panel.setFPS(fps);
            
        } // end method actionPerformed
    } // end private inner class TextFieldHandler
}
