package com.sun.tools.doclets;

import com.sun.javadoc.Tag;

public interface Taglet {
  boolean inField();
  
  boolean inConstructor();
  
  boolean inMethod();
  
  boolean inOverview();
  
  boolean inPackage();
  
  boolean inType();
  
  boolean isInlineTag();
  
  String getName();
  
  String toString(Tag paramTag);
  
  String toString(Tag[] paramArrayOfTag);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\Taglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */