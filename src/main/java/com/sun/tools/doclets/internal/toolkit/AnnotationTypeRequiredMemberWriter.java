package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MemberDoc;
import java.io.IOException;

public interface AnnotationTypeRequiredMemberWriter {
  Content getMemberTreeHeader();
  
  void addAnnotationDetailsMarker(Content paramContent);
  
  void addAnnotationDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getAnnotationDocTreeHeader(MemberDoc paramMemberDoc, Content paramContent);
  
  Content getAnnotationDetails(Content paramContent);
  
  Content getAnnotationDoc(Content paramContent, boolean paramBoolean);
  
  Content getSignature(MemberDoc paramMemberDoc);
  
  void addDeprecated(MemberDoc paramMemberDoc, Content paramContent);
  
  void addComments(MemberDoc paramMemberDoc, Content paramContent);
  
  void addTags(MemberDoc paramMemberDoc, Content paramContent);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\AnnotationTypeRequiredMemberWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */