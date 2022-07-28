package com.sun.jdi.event;

import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface ThreadStartEvent extends Event {
  ThreadReference thread();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\ThreadStartEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */