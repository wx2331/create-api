package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.model.CPropertyInfo;

public interface FieldOutline {
  ClassOutline parent();
  
  CPropertyInfo getPropertyInfo();
  
  JType getRawType();
  
  FieldAccessor create(JExpression paramJExpression);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\FieldOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */