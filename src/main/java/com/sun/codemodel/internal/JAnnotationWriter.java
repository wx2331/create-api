package com.sun.codemodel.internal;

public interface JAnnotationWriter<A extends java.lang.annotation.Annotation> {
  JAnnotationUse getAnnotationUse();
  
  Class<A> getAnnotationType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnnotationWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */