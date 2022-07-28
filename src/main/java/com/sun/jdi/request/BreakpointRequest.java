package com.sun.jdi.request;

import com.sun.jdi.Locatable;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface BreakpointRequest extends EventRequest, Locatable {
  Location location();
  
  void addThreadFilter(ThreadReference paramThreadReference);
  
  void addInstanceFilter(ObjectReference paramObjectReference);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\BreakpointRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */