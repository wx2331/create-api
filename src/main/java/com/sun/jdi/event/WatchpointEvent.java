package com.sun.jdi.event;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import jdk.Exported;

@Exported
public interface WatchpointEvent extends LocatableEvent {
  Field field();
  
  ObjectReference object();
  
  Value valueCurrent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\WatchpointEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */