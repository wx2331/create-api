package com.sun.jdi.connect;

import com.sun.jdi.VirtualMachine;
import java.io.IOException;
import java.util.Map;
import jdk.Exported;

@Exported
public interface LaunchingConnector extends Connector {
  VirtualMachine launch(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException, VMStartException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\LaunchingConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
