package com.sun.jdi.connect;

import com.sun.jdi.VirtualMachine;
import java.io.IOException;
import java.util.Map;
import jdk.Exported;

@Exported
public interface ListeningConnector extends Connector {
  boolean supportsMultipleConnections();

  String startListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException;

  void stopListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException;

  VirtualMachine accept(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\ListeningConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
