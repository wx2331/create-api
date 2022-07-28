package com.sun.jdi.event;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface MonitorWaitedEvent extends LocatableEvent {
  ThreadReference thread();
  
  ObjectReference monitor();
  
  boolean timedout();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\MonitorWaitedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */