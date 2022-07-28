package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface PathSearchingVirtualMachine extends VirtualMachine {
  List<String> classPath();
  
  List<String> bootClassPath();
  
  String baseDirectory();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\PathSearchingVirtualMachine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */