package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import java.io.IOException;

public interface EnumConstantWriter {
  Content getEnumConstantsDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getEnumConstantsTreeHeader(FieldDoc paramFieldDoc, Content paramContent);
  
  Content getSignature(FieldDoc paramFieldDoc);
  
  void addDeprecated(FieldDoc paramFieldDoc, Content paramContent);
  
  void addComments(FieldDoc paramFieldDoc, Content paramContent);
  
  void addTags(FieldDoc paramFieldDoc, Content paramContent);
  
  Content getEnumConstantsDetails(Content paramContent);
  
  Content getEnumConstants(Content paramContent, boolean paramBoolean);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\EnumConstantWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */