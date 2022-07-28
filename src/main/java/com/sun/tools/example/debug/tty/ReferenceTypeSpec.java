package com.sun.tools.example.debug.tty;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;

interface ReferenceTypeSpec {
  boolean matches(ReferenceType paramReferenceType);
  
  ClassPrepareRequest createPrepareRequest();
  
  int hashCode();
  
  boolean equals(Object paramObject);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ReferenceTypeSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */