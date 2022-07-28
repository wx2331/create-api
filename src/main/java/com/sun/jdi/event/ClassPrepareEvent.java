package com.sun.jdi.event;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface ClassPrepareEvent extends Event {
  ThreadReference thread();
  
  ReferenceType referenceType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\ClassPrepareEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */