package com.sun.javadoc;

public interface TypeVariable extends Type {
  Type[] bounds();
  
  ProgramElementDoc owner();
  
  AnnotationDesc[] annotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\TypeVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */