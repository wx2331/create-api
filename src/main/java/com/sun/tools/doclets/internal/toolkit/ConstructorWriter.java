package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import java.io.IOException;

public interface ConstructorWriter {
  Content getConstructorDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getConstructorDocTreeHeader(ConstructorDoc paramConstructorDoc, Content paramContent);
  
  Content getSignature(ConstructorDoc paramConstructorDoc);
  
  void addDeprecated(ConstructorDoc paramConstructorDoc, Content paramContent);
  
  void addComments(ConstructorDoc paramConstructorDoc, Content paramContent);
  
  void addTags(ConstructorDoc paramConstructorDoc, Content paramContent);
  
  Content getConstructorDetails(Content paramContent);
  
  Content getConstructorDoc(Content paramContent, boolean paramBoolean);
  
  void setFoundNonPubConstructor(boolean paramBoolean);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\ConstructorWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */