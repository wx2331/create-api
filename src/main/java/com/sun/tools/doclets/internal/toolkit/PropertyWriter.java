package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.io.IOException;

public interface PropertyWriter {
  Content getPropertyDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getPropertyDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent);
  
  Content getSignature(MethodDoc paramMethodDoc);
  
  void addDeprecated(MethodDoc paramMethodDoc, Content paramContent);
  
  void addComments(MethodDoc paramMethodDoc, Content paramContent);
  
  void addTags(MethodDoc paramMethodDoc, Content paramContent);
  
  Content getPropertyDetails(Content paramContent);
  
  Content getPropertyDoc(Content paramContent, boolean paramBoolean);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\PropertyWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */