package com.sun.tools.internal.xjc.generator.bean;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.tools.internal.xjc.model.CElementInfo;

public abstract class ObjectFactoryGenerator {
  abstract void populate(CElementInfo paramCElementInfo);
  
  abstract void populate(ClassOutlineImpl paramClassOutlineImpl);
  
  public abstract JDefinedClass getObjectFactory();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\ObjectFactoryGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */