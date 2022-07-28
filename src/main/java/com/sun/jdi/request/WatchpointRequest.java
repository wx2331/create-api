package com.sun.jdi.request;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface WatchpointRequest extends EventRequest {
  Field field();
  
  void addThreadFilter(ThreadReference paramThreadReference);
  
  void addClassFilter(ReferenceType paramReferenceType);
  
  void addClassFilter(String paramString);
  
  void addClassExclusionFilter(String paramString);
  
  void addInstanceFilter(ObjectReference paramObjectReference);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\WatchpointRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */