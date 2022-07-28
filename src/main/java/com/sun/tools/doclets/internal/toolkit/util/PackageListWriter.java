/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.javadoc.PackageDoc;
/*    */ import com.sun.javadoc.RootDoc;
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackageListWriter
/*    */   extends PrintWriter
/*    */ {
/*    */   private Configuration configuration;
/*    */   
/*    */   public PackageListWriter(Configuration paramConfiguration) throws IOException {
/* 55 */     super(DocFile.createFileForOutput(paramConfiguration, DocPaths.PACKAGE_LIST).openWriter());
/* 56 */     this.configuration = paramConfiguration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void generate(Configuration paramConfiguration) {
/*    */     try {
/* 68 */       PackageListWriter packageListWriter = new PackageListWriter(paramConfiguration);
/* 69 */       packageListWriter.generatePackageListFile(paramConfiguration.root);
/* 70 */       packageListWriter.close();
/* 71 */     } catch (IOException iOException) {
/* 72 */       paramConfiguration.message.error("doclet.exception_encountered", new Object[] { iOException
/* 73 */             .toString(), DocPaths.PACKAGE_LIST });
/* 74 */       throw new DocletAbortException(iOException);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void generatePackageListFile(RootDoc paramRootDoc) {
/* 79 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 80 */     ArrayList<String> arrayList = new ArrayList(); byte b;
/* 81 */     for (b = 0; b < arrayOfPackageDoc.length; b++) {
/*    */ 
/*    */       
/* 84 */       if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b]))
/* 85 */         arrayList.add(arrayOfPackageDoc[b].name()); 
/*    */     } 
/* 87 */     Collections.sort(arrayList);
/* 88 */     for (b = 0; b < arrayList.size(); b++)
/* 89 */       println(arrayList.get(b)); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\PackageListWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */