package com.sun.tools.internal.xjc.model.nav;

import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.outline.Aspect;
import com.sun.tools.internal.xjc.outline.Outline;

public interface NType {
  JType toType(Outline paramOutline, Aspect paramAspect);
  
  boolean isBoxedType();
  
  String fullName();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\NType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */