package com.sun.javadoc;

public interface ProgramElementDoc extends Doc {
  ClassDoc containingClass();
  
  PackageDoc containingPackage();
  
  String qualifiedName();
  
  int modifierSpecifier();
  
  String modifiers();
  
  AnnotationDesc[] annotations();
  
  boolean isPublic();
  
  boolean isProtected();
  
  boolean isPrivate();
  
  boolean isPackagePrivate();
  
  boolean isStatic();
  
  boolean isFinal();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ProgramElementDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */