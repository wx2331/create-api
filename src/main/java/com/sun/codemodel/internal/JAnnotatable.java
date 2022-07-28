package com.sun.codemodel.internal;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface JAnnotatable {
  JAnnotationUse annotate(JClass paramJClass);
  
  JAnnotationUse annotate(Class<? extends Annotation> paramClass);
  
  <W extends JAnnotationWriter> W annotate2(Class<W> paramClass);
  
  Collection<JAnnotationUse> annotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnnotatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */