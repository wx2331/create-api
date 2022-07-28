package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JBlock;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JVar;
import com.sun.tools.internal.xjc.model.CPropertyInfo;

public interface FieldAccessor {
  void toRawValue(JBlock paramJBlock, JVar paramJVar);
  
  void fromRawValue(JBlock paramJBlock, String paramString, JExpression paramJExpression);
  
  void unsetValues(JBlock paramJBlock);
  
  JExpression hasSetValue();
  
  FieldOutline owner();
  
  CPropertyInfo getPropertyInfo();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\FieldAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */