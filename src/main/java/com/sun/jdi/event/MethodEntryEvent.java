package com.sun.jdi.event;

import com.sun.jdi.Method;
import jdk.Exported;

@Exported
public interface MethodEntryEvent extends LocatableEvent {
  Method method();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\MethodEntryEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */