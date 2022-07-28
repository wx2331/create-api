package com.sun.tools.doclets.internal.toolkit.taglets;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.internal.toolkit.Content;

public interface Taglet {
  boolean inField();
  
  boolean inConstructor();
  
  boolean inMethod();
  
  boolean inOverview();
  
  boolean inPackage();
  
  boolean inType();
  
  boolean isInlineTag();
  
  String getName();
  
  Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) throws IllegalArgumentException;
  
  Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) throws IllegalArgumentException;
  
  String toString();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\Taglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */