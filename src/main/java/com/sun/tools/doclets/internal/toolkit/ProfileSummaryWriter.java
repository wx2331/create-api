package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import java.io.IOException;

public interface ProfileSummaryWriter {
  Content getProfileHeader(String paramString);
  
  Content getContentHeader();
  
  Content getSummaryHeader();
  
  Content getSummaryTree(Content paramContent);
  
  Content getPackageSummaryHeader(PackageDoc paramPackageDoc);
  
  Content getPackageSummaryTree(Content paramContent);
  
  void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent);
  
  void addProfileFooter(Content paramContent);
  
  void printDocument(Content paramContent) throws IOException;
  
  void close() throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\ProfileSummaryWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */