package com.sun.jdi.request;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public interface StepRequest extends EventRequest {
  public static final int STEP_INTO = 1;
  
  public static final int STEP_OVER = 2;
  
  public static final int STEP_OUT = 3;
  
  public static final int STEP_MIN = -1;
  
  public static final int STEP_LINE = -2;
  
  ThreadReference thread();
  
  int size();
  
  int depth();
  
  void addClassFilter(ReferenceType paramReferenceType);
  
  void addClassFilter(String paramString);
  
  void addClassExclusionFilter(String paramString);
  
  void addInstanceFilter(ObjectReference paramObjectReference);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\StepRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */