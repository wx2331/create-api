package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import com.sun.jdi.request.EventRequest;
import jdk.Exported;

@Exported
public interface Event extends Mirror {
  EventRequest request();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */