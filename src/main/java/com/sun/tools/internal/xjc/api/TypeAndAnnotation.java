package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JAnnotatable;
import com.sun.codemodel.internal.JType;

public interface TypeAndAnnotation {
  JType getTypeClass();
  
  void annotate(JAnnotatable paramJAnnotatable);
  
  boolean equals(Object paramObject);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\TypeAndAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */