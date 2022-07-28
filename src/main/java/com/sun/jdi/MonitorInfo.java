package com.sun.jdi;

import jdk.Exported;

@Exported
public interface MonitorInfo extends Mirror {
  ObjectReference monitor();
  
  int stackDepth();
  
  ThreadReference thread();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\MonitorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */