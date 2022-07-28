package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import java.io.IOException;

public interface MethodWriter {
  Content getMethodDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getMethodDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent);
  
  Content getSignature(MethodDoc paramMethodDoc);
  
  void addDeprecated(MethodDoc paramMethodDoc, Content paramContent);
  
  void addComments(Type paramType, MethodDoc paramMethodDoc, Content paramContent);
  
  void addTags(MethodDoc paramMethodDoc, Content paramContent);
  
  Content getMethodDetails(Content paramContent);
  
  Content getMethodDoc(Content paramContent, boolean paramBoolean);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\MethodWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */