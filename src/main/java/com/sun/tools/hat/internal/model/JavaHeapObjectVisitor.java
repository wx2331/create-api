package com.sun.tools.hat.internal.model;

public interface JavaHeapObjectVisitor {
  void visit(JavaHeapObject paramJavaHeapObject);
  
  boolean exclude(JavaClass paramJavaClass, JavaField paramJavaField);
  
  boolean mightExclude();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaHeapObjectVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */