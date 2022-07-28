package com.sun.jdi;

import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.ListeningConnector;
import com.sun.jdi.connect.spi.Connection;
import java.io.IOException;
import java.util.List;
import jdk.Exported;

@Exported
public interface VirtualMachineManager {
  LaunchingConnector defaultConnector();
  
  List<LaunchingConnector> launchingConnectors();
  
  List<AttachingConnector> attachingConnectors();
  
  List<ListeningConnector> listeningConnectors();
  
  List<Connector> allConnectors();
  
  List<VirtualMachine> connectedVirtualMachines();
  
  int majorInterfaceVersion();
  
  int minorInterfaceVersion();
  
  VirtualMachine createVirtualMachine(Connection paramConnection, Process paramProcess) throws IOException;
  
  VirtualMachine createVirtualMachine(Connection paramConnection) throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VirtualMachineManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */