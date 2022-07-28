package com.sun.jdi.request;

import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface ThreadStartRequest extends EventRequest {
  void addThreadFilter(ThreadReference paramThreadReference);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\ThreadStartRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */