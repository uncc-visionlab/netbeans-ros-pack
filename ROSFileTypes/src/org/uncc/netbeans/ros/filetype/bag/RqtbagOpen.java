/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.filetype.bag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.uncc.netbeans.ros.project.RunInNetbeansTerminal;
import org.uncc.netbeans.ros.project.RunROSCore;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.bag.RqtbagOpen"
)
@ActionRegistration(
        displayName = "#CTL_RqtbagOpen"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Loaders/application/x-ros-rqt-bag/Actions", position = 0)
})
@Messages("CTL_RqtbagOpen=Open with rqt_bag/roscore")
public final class RqtbagOpen implements ActionListener {
    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final BagDataObject context;

    public RqtbagOpen(BagDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String homeDir = context.getPrimaryFile().getParent().getPath();
        String actionName = "roscore";
//        commandList = new String[]{
//            "source /opt/ros/indigo/setup.bash\n",
//            "roscore\n",
//            "exit"
//        };
//        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        actionName = "rqt_bag";
        String bagfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source /opt/ros/indigo/setup.bash\n",
            "cd "+homeDir+"\n",
            "rqt_bag "+bagfilename+"\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
