package com.sun.javadoc;

public interface ParamTag extends Tag {
  String parameterName();
  
  String parameterComment();
  
  boolean isTypeParameter();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ParamTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */