package com.sun.tools.jdi;

import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;

public interface VirtualMachineManagerService extends VirtualMachineManager {
  void setDefaultConnector(LaunchingConnector paramLaunchingConnector);
  
  void addConnector(Connector paramConnector);
  
  void removeConnector(Connector paramConnector);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VirtualMachineManagerService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */