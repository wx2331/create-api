package com.sun.jdi;

import jdk.Exported;

@Exported
public interface Location extends Mirror, Comparable<Location> {
  ReferenceType declaringType();
  
  Method method();
  
  long codeIndex();
  
  String sourceName() throws AbsentInformationException;
  
  String sourceName(String paramString) throws AbsentInformationException;
  
  String sourcePath() throws AbsentInformationException;
  
  String sourcePath(String paramString) throws AbsentInformationException;
  
  int lineNumber();
  
  int lineNumber(String paramString);
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\Location.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */