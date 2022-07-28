package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ConstantsSummaryWriter {
  void close() throws IOException;
  
  Content getHeader();
  
  Content getContentsHeader();
  
  void addLinkToPackageContent(PackageDoc paramPackageDoc, String paramString, Set<String> paramSet, Content paramContent);
  
  Content getContentsList(Content paramContent);
  
  Content getConstantSummaries();
  
  void addPackageName(PackageDoc paramPackageDoc, String paramString, Content paramContent);
  
  Content getClassConstantHeader();
  
  void addConstantMembers(ClassDoc paramClassDoc, List<FieldDoc> paramList, Content paramContent);
  
  void addFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\ConstantsSummaryWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */