/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.dlight.nativeexecution;

import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author arwillis
 */
public class MakeProjectRemoteUtils {
// Taken from BrokenReferencesSupport.java
// in Netbeans 8.1 module org.netbeans.modules.cnd.makeproject
//    private static UnsetEnvVar getUndefinedEnvVars(final MakeProject project) {
//        ConfigurationDescriptorProvider cdp = project.getLookup().lookup(ConfigurationDescriptorProvider.class);
//        if (cdp.gotDescriptor()) {
//            MakeConfigurationDescriptor configurationDescriptor = cdp.getConfigurationDescriptor();
//            MakeConfiguration activeConfiguration = configurationDescriptor.getActiveConfiguration();
//            if (activeConfiguration != null) {
//                CodeAssistanceConfiguration codeAssistanceConfiguration = activeConfiguration.getCodeAssistanceConfiguration();
//                if (codeAssistanceConfiguration != null) {
//                    VectorConfiguration<String> environmentVariables = codeAssistanceConfiguration.getEnvironmentVariables();
//                    if (environmentVariables != null && !environmentVariables.getValue().isEmpty()) {
//                        ExecutionEnvironment ee = activeConfiguration.getDevelopmentHost().getExecutionEnvironment();
//                        if (ConnectionManager.getInstance().isConnectedTo(ee)) {
//                            try {
//                                List<String> res = new ArrayList<String>();
//                                HostInfo hostInfo = HostInfoUtils.getHostInfo(ee);
//                                Map<String, String> environment = hostInfo.getEnvironment();
//                                for(String var : environmentVariables.getValue()) {
//                                    String env = environment.get(var);
//                                    if (env == null) {
//                                        Map<String, String> temporaryEnv = getTemporaryEnv(ee);
//                                        if (temporaryEnv == null || !temporaryEnv.containsKey(var)) {
//                                            res.add(var);
//                                        }
//                                    }
//                                }
//                                return new UnsetEnvVar(res, ee, hostInfo);
//                            } catch (IOException ex) {
//                                Exceptions.printStackTrace(ex);
//                            } catch (ConnectionManager.CancellationException ex) {
//                                Exceptions.printStackTrace(ex);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
        
}
