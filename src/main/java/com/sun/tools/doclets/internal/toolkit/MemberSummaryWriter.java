package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import java.io.IOException;
import java.util.List;

public interface MemberSummaryWriter {
  Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent);
  
  Content getSummaryTableTree(ClassDoc paramClassDoc, List<Content> paramList);
  
  void addMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, List<Content> paramList, int paramInt);
  
  Content getInheritedSummaryHeader(ClassDoc paramClassDoc);
  
  void addInheritedMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean1, boolean paramBoolean2, Content paramContent);
  
  Content getInheritedSummaryLinksTree();
  
  Content getMemberTree(Content paramContent);
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\MemberSummaryWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */