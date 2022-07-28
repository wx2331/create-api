package com.sun.tools.internal.xjc.model.nav;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.outline.Aspect;
import com.sun.tools.internal.xjc.outline.Outline;

public interface NClass extends NType {
  JClass toType(Outline paramOutline, Aspect paramAspect);
  
  boolean isAbstract();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\NClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */