package com.sun.codemodel.internal;

import java.util.Iterator;

public interface JClassContainer {
  boolean isClass();
  
  boolean isPackage();
  
  JDefinedClass _class(int paramInt, String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _class(String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _interface(int paramInt, String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _interface(String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _class(int paramInt, String paramString, boolean paramBoolean) throws JClassAlreadyExistsException;
  
  JDefinedClass _class(int paramInt, String paramString, ClassType paramClassType) throws JClassAlreadyExistsException;
  
  Iterator<JDefinedClass> classes();
  
  JClassContainer parentContainer();
  
  JPackage getPackage();
  
  JCodeModel owner();
  
  JDefinedClass _annotationTypeDeclaration(String paramString) throws JClassAlreadyExistsException;
  
  JDefinedClass _enum(String paramString) throws JClassAlreadyExistsException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JClassContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */