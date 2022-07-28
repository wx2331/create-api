package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;

public interface ClassWriter {
  Content getHeader(String paramString);
  
  Content getClassContentHeader();
  
  void addClassTree(Content paramContent);
  
  Content getClassInfoTreeHeader();
  
  void addTypeParamInfo(Content paramContent);
  
  void addSuperInterfacesInfo(Content paramContent);
  
  void addImplementedInterfacesInfo(Content paramContent);
  
  void addSubClassInfo(Content paramContent);
  
  void addSubInterfacesInfo(Content paramContent);
  
  void addInterfaceUsageInfo(Content paramContent);
  
  void addFunctionalInterfaceInfo(Content paramContent);
  
  void addNestedClassInfo(Content paramContent);
  
  Content getClassInfo(Content paramContent);
  
  void addClassDeprecationInfo(Content paramContent);
  
  void addClassSignature(String paramString, Content paramContent);
  
  void addClassDescription(Content paramContent);
  
  void addClassTagInfo(Content paramContent);
  
  Content getMemberTreeHeader();
  
  void addFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
  
  void close() throws IOException;
  
  ClassDoc getClassDoc();
  
  Content getMemberSummaryTree(Content paramContent);
  
  Content getMemberDetailsTree(Content paramContent);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\ClassWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */