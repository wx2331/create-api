package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;

public interface ProfilePackageSummaryWriter {
  Content getPackageHeader(String paramString);
  
  Content getContentHeader();
  
  Content getSummaryHeader();
  
  void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent);
  
  void addPackageDescription(Content paramContent);
  
  void addPackageTags(Content paramContent);
  
  void addPackageFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\ProfilePackageSummaryWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */