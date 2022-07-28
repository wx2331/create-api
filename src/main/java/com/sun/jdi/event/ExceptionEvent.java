package com.sun.jdi.event;

import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import jdk.Exported;

@Exported
public interface ExceptionEvent extends LocatableEvent {
  ObjectReference exception();
  
  Location catchLocation();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\ExceptionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */