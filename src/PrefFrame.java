
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import javax.swing.JTextField;

public class PrefFrame extends JFrame{
    //declares and sets default values of the settings
    private int linewidth=1; 
    private int dashlength=1;
    private int shape=0;
    private int colour1=0;
    private int colour2=0;
    private boolean filled=false;
    private boolean gradient=false;
    private boolean dashed=false;
    
    //the swing components that will be put in the preferences window
    private JPanel jPanels[]; 
    private JSlider slider;
    private JSlider slider2;
    private JSlider slider3;
    private JButton button; // array of buttons to hide portions
    private JTextField jTextFields [];
    private JLabel jLabels[];
    private JCheckBox checkBoxes[];
    private JComboBox comboBoxes[];
    private JTextField textField1; // text field with set size
    private String string="Enter fps here" ;
    private int fontsize=18;
    private String shapes[] = { "Pen","Line", "Rectangle", "Round Rect", "Oval", "Polygon" }; // the String array representing the shapes
    private String colours[] = { "Black", "Blue", "Cyan", "Gray", "Green", "Magenta",
        "Orange", "Pink", "Red", "White", "Yellow" }; // String array representing the selection of colours
    public PrefFrame () {
        super("Preferences: Customize Default Settings");
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); //makes it so that it is components are stacked
        //from top to bottom, if it was BoxLayout.X_AXIS, components would be stacked from left to right
        ButtonHandler bHandler=new ButtonHandler();
        //stacks 5 buttons vertically
        jPanels = new JPanel [ 10 ];
        jLabels = new JLabel [ 5 ];
        checkBoxes = new JCheckBox [ 3 ];
        comboBoxes = new JComboBox [ 3 ];
        
        // the shape settings, creates the swing compents and adds them  to a new jPanel object
        jPanels[ 0 ] = new JPanel();
        jPanels[ 0 ].setLayout(new BoxLayout(jPanels[ 0 ], BoxLayout.X_AXIS));
        jLabels[ 0 ] = new JLabel( "Shape Type: ");
        jLabels[ 0 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(jLabels[ 0 ]);
        comboBoxes [ 0 ] = new JComboBox (shapes);
        comboBoxes[ 0 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(comboBoxes[ 0 ]);
        jPanels[ 0 ].add(jLabels[ 0 ]);
        jPanels[ 0 ].add(Box.createHorizontalGlue());
        jPanels[ 0 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 0 ].add(comboBoxes [0] );
        jPanels[ 0 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        // the fill shape setting
        jPanels[ 1 ] = new JPanel();
        jPanels[ 1 ].setLayout(new BoxLayout(jPanels[ 1 ], BoxLayout.X_AXIS));
        checkBoxes [ 0 ] = new JCheckBox ("Filled");
        checkBoxes[ 0 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize2(checkBoxes[ 0 ]);
        jPanels[ 1 ].add(checkBoxes[ 0 ]);
        
        // the fill shape setting
        jPanels[ 2 ] = new JPanel();
        jPanels[ 2 ].setLayout(new BoxLayout(jPanels[ 2 ], BoxLayout.X_AXIS));
        checkBoxes [ 1 ] = new JCheckBox ("Gradient");
        checkBoxes[ 1 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize2(checkBoxes[ 1 ]);
        jPanels[ 2 ].add(checkBoxes[ 1 ]);
        
        //primary colour settings
        jPanels[ 3 ] = new JPanel();
        jPanels[ 3 ].setLayout(new BoxLayout(jPanels[ 3 ], BoxLayout.X_AXIS));
        jLabels[ 1 ] = new JLabel( "First Colour: ");
        jLabels[ 1 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize( jLabels[ 1 ]);
        comboBoxes [ 1 ] = new JComboBox (colours);
        comboBoxes[ 1 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(comboBoxes[ 1 ]);
        jPanels[ 3 ].add(jLabels[ 1 ]);
        jPanels[ 3 ].add(Box.createHorizontalGlue());
        jPanels[ 3 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 3 ].add(comboBoxes [1] );
        jPanels[ 3 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        //secondary colour settings
        jPanels[ 4 ] = new JPanel();
        jPanels[ 4 ].setLayout(new BoxLayout(jPanels[ 4 ], BoxLayout.X_AXIS));
        jLabels[ 2 ] = new JLabel( "Second Colour: ");
        jLabels[ 2 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize( jLabels[ 2 ]);
        comboBoxes [ 2 ] = new JComboBox (colours);
        comboBoxes[ 2 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(comboBoxes[ 2 ]);
        jPanels[ 4 ].add(jLabels[ 2 ]);
        jPanels[ 4 ].add(Box.createHorizontalGlue());
        jPanels[ 4 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 4 ].add(comboBoxes [2] );
        jPanels[ 4 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        //line width setting
        jPanels[ 5 ] = new JPanel();
        jPanels[ 5 ].setLayout(new BoxLayout(jPanels[ 5 ], BoxLayout.X_AXIS));
        jLabels[ 3 ] = new JLabel( "Line Width: ");
        jLabels[ 3 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize( jLabels[ 3 ]);
        slider = new JSlider(0, 15, 1); // slider
        slider.setMinorTickSpacing(1); //sets distance between short ticks
        slider.setPaintTicks(true); //sets whether ticks are visible
        slider.setSnapToTicks(true); //sets whether only ticks can selectable
        slider.setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(slider);
        jPanels[ 5 ].add(jLabels[ 3 ]);
        jPanels[ 5 ].add(Box.createHorizontalGlue());
        jPanels[ 5 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 5 ].add(slider );
        jPanels[ 5 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        //dash length setting
        jPanels[ 6 ] = new JPanel();
        jPanels[ 6 ].setLayout(new BoxLayout(jPanels[ 6 ], BoxLayout.X_AXIS));
        jLabels[ 4 ] = new JLabel( "Dash length: ");
        jLabels[ 4 ].setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize( jLabels[ 4 ]);
        slider2 = new JSlider(1, 20, 1); // slider
        slider2.setMinorTickSpacing(2); //sets distance between short ticks
        slider2.setPaintTicks(true); //sets whether ticks are visible
        slider2.setSnapToTicks(true); //sets whether only ticks can selectable
        slider2.setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(slider2);
        jPanels[ 6 ].add(jLabels[ 4 ]);
        jPanels[ 6 ].add(Box.createHorizontalGlue());
        jPanels[ 6 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 6 ].add(slider2 );
        jPanels[ 6 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        //dashed or not dashed setting
        jPanels[ 7 ] = new JPanel();
        jPanels[ 7 ].setLayout(new BoxLayout(jPanels[ 7 ], BoxLayout.X_AXIS));
        checkBoxes [ 2 ] = new JCheckBox ("Dashed Line");
        checkBoxes[ 2 ].setAlignmentX( Component.LEFT_ALIGNMENT );
        setSize2(checkBoxes[ 2 ]);
        jPanels[ 7 ].add(checkBoxes[ 2 ]);
        
        //the draw string settings
        jPanels[ 8 ] = new JPanel();
        jPanels[ 8 ].setLayout(new BoxLayout(jPanels[ 8 ], BoxLayout.X_AXIS));
        textField1 = new JTextField( "Enter fps here" ); 
        textField1.setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize( textField1);
        slider3= new JSlider(10, 120, 18); // slider
        slider3.setMinorTickSpacing(5); //sets distance between short ticks
        slider3.setPaintTicks(true); //sets whether ticks are visible
        slider3.setSnapToTicks(true); //sets whether only ticks can selectable
        slider3.setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(slider3);
        
        jPanels[ 8 ].add(textField1);
        jPanels[ 8 ].add(Box.createHorizontalGlue());
        jPanels[ 8 ].add(Box.createRigidArea(new Dimension(10,10)));
        jPanels[ 8 ].add(slider3 );
        jPanels[ 8 ].add(Box.createRigidArea(new Dimension(10,10)));
        
        
        //save preferences to a file button
        jPanels[ 9 ] = new JPanel();
        jPanels[ 9 ].setLayout(new BoxLayout(jPanels[ 9 ], BoxLayout.X_AXIS));
        button = new JButton ("Save Preferences");
        button.setAlignmentX( Component.CENTER_ALIGNMENT );
        setSize(button);
        jPanels[ 9 ].add(button);
        button.addActionListener( bHandler );
        
        //creates event handlers that handle all the events
        TextFieldHandler tfhandler=new TextFieldHandler (); //event handlers for textfield
        textField1.addActionListener( tfhandler );
        SliderHandler sHandler=new SliderHandler(); //event handler for jsliders
        slider.addChangeListener( sHandler ); //sets the event handlers
        slider2.addChangeListener( sHandler ); //sets the event handlers
        slider3.addChangeListener( sHandler ); //sets the event handlers
        CheckBoxHandler checkBoxHandler = new CheckBoxHandler();//handles check box events
        checkBoxes[0].addItemListener( checkBoxHandler );
        checkBoxes[1].addItemListener( checkBoxHandler );
        checkBoxes[2].addItemListener( checkBoxHandler );
        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();//handles combo box events
        comboBoxes[0].addItemListener( comboBoxHandler );
        comboBoxes[1].addItemListener( comboBoxHandler );
        comboBoxes[2].addItemListener( comboBoxHandler );
        
        /* - adds the buttons to BoxLayout 
         * - the createRigidArea creates an invisible border around the buttons 
         * - the vertical glue makes it so the components would be vertically as far as possible from each
         * other if the JFrame would be resized.
         * - the horizontal glue makes it so the components would be horizontally as far as possible from each
         * other if the JFrame would be resized.
         */ 
        for (int i=0; i < 10 ; i++){
            add( jPanels[ i ] ); 
            add(Box.createRigidArea(new Dimension(10,10)));
            add(Box.createVerticalGlue());
        }
        pack();
        
    }
    //makes components have a fixed size
    public void setSize(Component comp){
        comp.setPreferredSize(new Dimension (200,25));
        comp.setMaximumSize(new Dimension (200,25));
        comp.setMinimumSize(new Dimension (200,25));
        
        
    }
    //makes components have a fixed size
    public void setSize2(Component comp){
        comp.setPreferredSize(new Dimension (400,25));
        comp.setMaximumSize(new Dimension (400,25));
        comp.setMinimumSize(new Dimension (400,25));
    }
    
    //handles button events
    public class ButtonHandler implements ActionListener{
        public void actionPerformed( ActionEvent event )
        {
            JOptionPane.showMessageDialog(null, "Preferences saved, program will now shut down"  );
            try{
            printFile();
            }catch (FileNotFoundException e){
            }
            System.exit(0);
        } // end method actionPerformed
    }
    
    //handles jslider events
    private class SliderHandler implements ChangeListener 
    {
        // handle slider event
        public void stateChanged( ChangeEvent event )
        {
            JSlider curSlider = (JSlider)event.getSource(); //creates a reference to the current slider
            if (curSlider==slider)
                linewidth=curSlider.getValue();
            else if (curSlider==slider2)
                dashlength=curSlider.getValue();
            else 
                fontsize=curSlider.getValue();
        } // end method stateChanged
    } // end private inner class SliderHandler
    private class ComboBoxHandler implements ItemListener  // handles combo box events
    {
        public void itemStateChanged( ItemEvent event ){
            // determine which combo box is changed
            if ( event.getSource() == comboBoxes[0] ){//if user decides to change the colour
                if ( event.getStateChange() == ItemEvent.SELECTED ){
                    shape=comboBoxes[0].getSelectedIndex();//changes the current the current color
                }
            }
            else if ( event.getSource() == comboBoxes[1]){//if user decides to change the shape
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    colour1=comboBoxes[1].getSelectedIndex();//changes the current the current color
                }
            }
            else if ( event.getSource() == comboBoxes[2] ){//if user decides to change the shape
                if ( event.getStateChange() == ItemEvent.SELECTED ){  
                    colour2=comboBoxes[2].getSelectedIndex();//changes the current the current color
                }
            }
        } // end method itemStateChanged
    } // end private inner class ComboBoxHandler
    
    //handlers checkbox events
    private class CheckBoxHandler implements ItemListener // responds to checkbox events
    { 
        public void itemStateChanged( ItemEvent event ){
            JCheckBox curBox=(JCheckBox)event.getSource();
            if (curBox==checkBoxes[0])
                filled= checkBoxes[0].isSelected() ; // sets the filled status
            else if (curBox==checkBoxes[1])
                gradient= checkBoxes[1].isSelected(); // sets the filled status
            else if (curBox==checkBoxes[2])
                dashed= checkBoxes[2].isSelected(); // sets the filled status
        } // end method itemStateChanged
    } // end private inner class CheckBoxHandler
    //method to print settings to a file
    public void printFile ()throws FileNotFoundException{
        PrintWriter out = new PrintWriter("prefs.txt");
            out.println (string);
            out.println (shape);
            out.println (colour1);
            out.println (colour2);
            out.println (linewidth);
            out.println (dashlength);
            out.println (fontsize);
            out.println (filled);
            out.println (gradient);
            out.println (dashed);
            out.close();
    }
    //handles textfield events
    private class TextFieldHandler implements ActionListener
    {
        
        // process textfield events
        public void actionPerformed( ActionEvent event )
        {
            string = String.format( event.getActionCommand() );
            
        } // end method actionPerformed
    } // end private inner class TextFieldHandler
}