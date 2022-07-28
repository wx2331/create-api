package com.sun.javadoc;

public interface DocErrorReporter {
  void printError(String paramString);
  
  void printError(SourcePosition paramSourcePosition, String paramString);
  
  void printWarning(String paramString);
  
  void printWarning(SourcePosition paramSourcePosition, String paramString);
  
  void printNotice(String paramString);
  
  void printNotice(SourcePosition paramSourcePosition, String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\DocErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */