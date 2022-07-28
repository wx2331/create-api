package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import java.io.IOException;

public interface FieldWriter {
  Content getFieldDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getFieldDocTreeHeader(FieldDoc paramFieldDoc, Content paramContent);
  
  Content getSignature(FieldDoc paramFieldDoc);
  
  void addDeprecated(FieldDoc paramFieldDoc, Content paramContent);
  
  void addComments(FieldDoc paramFieldDoc, Content paramContent);
  
  void addTags(FieldDoc paramFieldDoc, Content paramContent);
  
  Content getFieldDetails(Content paramContent);
  
  Content getFieldDoc(Content paramContent, boolean paramBoolean);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\FieldWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */