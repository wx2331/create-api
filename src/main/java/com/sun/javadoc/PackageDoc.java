package com.sun.javadoc;

public interface PackageDoc extends Doc {
  ClassDoc[] allClasses(boolean paramBoolean);
  
  ClassDoc[] allClasses();
  
  ClassDoc[] ordinaryClasses();
  
  ClassDoc[] exceptions();
  
  ClassDoc[] errors();
  
  ClassDoc[] enums();
  
  ClassDoc[] interfaces();
  
  AnnotationTypeDoc[] annotationTypes();
  
  AnnotationDesc[] annotations();
  
  ClassDoc findClass(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\PackageDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */