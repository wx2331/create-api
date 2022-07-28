package com.sun.jdi.request;

import com.sun.jdi.ReferenceType;
import jdk.Exported;

@Exported
public interface ClassPrepareRequest extends EventRequest {
  void addClassFilter(ReferenceType paramReferenceType);
  
  void addClassFilter(String paramString);
  
  void addClassExclusionFilter(String paramString);
  
  void addSourceNameFilter(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\ClassPrepareRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */