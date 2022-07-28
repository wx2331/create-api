package com.sun.javadoc;

public interface RootDoc extends Doc, DocErrorReporter {
  String[][] options();
  
  PackageDoc[] specifiedPackages();
  
  ClassDoc[] specifiedClasses();
  
  ClassDoc[] classes();
  
  PackageDoc packageNamed(String paramString);
  
  ClassDoc classNamed(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\RootDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */