package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.AnnotationTypeDoc;
import java.io.IOException;

public interface AnnotationTypeWriter {
  Content getHeader(String paramString);
  
  Content getAnnotationContentHeader();
  
  Content getAnnotationInfoTreeHeader();
  
  Content getAnnotationInfo(Content paramContent);
  
  void addAnnotationTypeSignature(String paramString, Content paramContent);
  
  void addAnnotationTypeDescription(Content paramContent);
  
  void addAnnotationTypeTagInfo(Content paramContent);
  
  void addAnnotationTypeDeprecationInfo(Content paramContent);
  
  Content getMemberTreeHeader();
  
  Content getMemberTree(Content paramContent);
  
  Content getMemberSummaryTree(Content paramContent);
  
  Content getMemberDetailsTree(Content paramContent);
  
  void addFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
  
  void close() throws IOException;
  
  AnnotationTypeDoc getAnnotationTypeDoc();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\AnnotationTypeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */