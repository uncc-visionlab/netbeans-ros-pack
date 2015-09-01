/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.uncc.netbeans.ros.project.ws;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;

/* 1.4 example used by DialogDemo.java. */
class CreateROSPackageDialog extends JDialog
        implements ActionListener,
        PropertyChangeListener {

    private String packageName = null;
    private String dependenciesStr = null;
    private JTextField packageNameField;
    private JTextField dependenciesField;
//    private DialogDemo dd;

    private String magicWord;
    private JOptionPane optionPane;

    private String btnString1 = "Create";
    private String btnString2 = "Cancel";

    /**
     * Returns null if the typed string was invalid; otherwise, returns the
     * string as the user entered it.
     */
    public String getPackageName() {
        return packageName;
    }

    public String getDependencies() {
        return dependenciesStr;
    }

    /**
     * Creates the reusable dialog.
     */
    public CreateROSPackageDialog(Frame aFrame, String aWord) {
        super(aFrame, true);
//        dd = parent;

        magicWord = aWord.toUpperCase();
        setTitle("Create ROS Package");

        packageNameField = new JTextField(10);
        dependenciesField = new JTextField(10);

        //Create an array of the text and components to be displayed.
        String infoStr = "This action will create a new package having the provided\n"
                + " title inside the src folder and will initialize the ROS package.\n\n";
        String msgString1 = "Package name:";
        String msgString2 = "Package dependencies:";
        Object[] array = {infoStr, msgString1, packageNameField, msgString2, dependenciesField};

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                packageNameField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        packageNameField.addActionListener(this);
        setMinimumSize(new Dimension(500, 250));

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }

    /**
     * This method handles events for the text field.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /**
     * This method reacts to state changes in the option pane.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
                packageName = packageNameField.getText();
                dependenciesStr = dependenciesField.getText();
                clearAndHide();
//                    packageName = packageNameField.getText();
//                String ucText = packageName.toUpperCase();
//                if (magicWord.equals(ucText)) {
//                    //we're done; clear and dismiss the dialog
//                    clearAndHide();
//                } else {
//                    //text was invalid
//                    packageNameField.selectAll();
//                    JOptionPane.showMessageDialog(CreateROSPackageDialog.this,
//                                    "Sorry, \"" + packageName + "\" "
//                                    + "isn't a valid response.\n"
//                                    + "Please enter "
//                                    + magicWord + ".",
//                                    "Try again",
//                                    JOptionPane.ERROR_MESSAGE);
//                    packageName = null;
//                    packageNameField.requestFocusInWindow();
//                }
            } else { //user closed dialog or clicked cancel
////                dd.setLabel("It's OK.  "
////                         + "We won't force you to type "
////                         + magicWord + ".");
                packageName = null;
                dependenciesStr = null;
                clearAndHide();
            }
        }
    }

    /**
     * This method clears the dialog and hides it.
     */
    public void clearAndHide() {
        packageNameField.setText(null);
        setVisible(false);
    }

    public static void main(String args[]) {
        CreateROSPackageDialog d = new CreateROSPackageDialog(null, "test");
        d.setVisible(true);
    }
}
