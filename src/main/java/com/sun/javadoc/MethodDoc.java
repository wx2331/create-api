package com.sun.javadoc;

public interface MethodDoc extends ExecutableMemberDoc {
  boolean isAbstract();
  
  boolean isDefault();
  
  Type returnType();
  
  ClassDoc overriddenClass();
  
  Type overriddenType();
  
  MethodDoc overriddenMethod();
  
  boolean overrides(MethodDoc paramMethodDoc);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\MethodDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */