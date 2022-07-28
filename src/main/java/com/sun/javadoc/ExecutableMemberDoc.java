package com.sun.javadoc;

public interface ExecutableMemberDoc extends MemberDoc {
  ClassDoc[] thrownExceptions();
  
  Type[] thrownExceptionTypes();
  
  boolean isNative();
  
  boolean isSynchronized();
  
  boolean isVarArgs();
  
  Parameter[] parameters();
  
  Type receiverType();
  
  ThrowsTag[] throwsTags();
  
  ParamTag[] paramTags();
  
  ParamTag[] typeParamTags();
  
  String signature();
  
  String flatSignature();
  
  TypeVariable[] typeParameters();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ExecutableMemberDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */