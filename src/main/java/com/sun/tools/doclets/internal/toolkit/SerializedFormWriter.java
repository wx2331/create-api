package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.SerialFieldTag;
import java.io.IOException;

public interface SerializedFormWriter {
  Content getHeader(String paramString);
  
  Content getSerializedSummariesHeader();
  
  Content getPackageSerializedHeader();
  
  Content getPackageHeader(String paramString);
  
  Content getClassSerializedHeader();
  
  Content getClassHeader(ClassDoc paramClassDoc);
  
  Content getSerialUIDInfoHeader();
  
  void addSerialUIDInfo(String paramString1, String paramString2, Content paramContent);
  
  Content getClassContentHeader();
  
  SerialFieldWriter getSerialFieldWriter(ClassDoc paramClassDoc);
  
  SerialMethodWriter getSerialMethodWriter(ClassDoc paramClassDoc);
  
  void close() throws IOException;
  
  Content getSerializedContent(Content paramContent);
  
  void addFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
  
  public static interface SerialFieldWriter {
    Content getSerializableFieldsHeader();
    
    Content getFieldsContentHeader(boolean param1Boolean);
    
    Content getSerializableFields(String param1String, Content param1Content);
    
    void addMemberDeprecatedInfo(FieldDoc param1FieldDoc, Content param1Content);
    
    void addMemberDescription(FieldDoc param1FieldDoc, Content param1Content);
    
    void addMemberDescription(SerialFieldTag param1SerialFieldTag, Content param1Content);
    
    void addMemberTags(FieldDoc param1FieldDoc, Content param1Content);
    
    void addMemberHeader(ClassDoc param1ClassDoc, String param1String1, String param1String2, String param1String3, Content param1Content);
    
    boolean shouldPrintOverview(FieldDoc param1FieldDoc);
  }
  
  public static interface SerialMethodWriter {
    Content getSerializableMethodsHeader();
    
    Content getMethodsContentHeader(boolean param1Boolean);
    
    Content getSerializableMethods(String param1String, Content param1Content);
    
    Content getNoCustomizationMsg(String param1String);
    
    void addMemberHeader(MethodDoc param1MethodDoc, Content param1Content);
    
    void addDeprecatedMemberInfo(MethodDoc param1MethodDoc, Content param1Content);
    
    void addMemberDescription(MethodDoc param1MethodDoc, Content param1Content);
    
    void addMemberTags(MethodDoc param1MethodDoc, Content param1Content);
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\SerializedFormWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */