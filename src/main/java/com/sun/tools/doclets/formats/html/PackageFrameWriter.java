/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageFrameWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   private PackageDoc packageDoc;
/*     */   private Set<ClassDoc> documentedClasses;
/*     */   
/*     */   public PackageFrameWriter(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc) throws IOException {
/*  76 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(DocPaths.PACKAGE_FRAME));
/*  77 */     this.packageDoc = paramPackageDoc;
/*  78 */     if ((paramConfigurationImpl.root.specifiedPackages()).length == 0) {
/*  79 */       this.documentedClasses = new HashSet<>(Arrays.asList(paramConfigurationImpl.root.classes()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc) {
/*     */     try {
/*  94 */       PackageFrameWriter packageFrameWriter = new PackageFrameWriter(paramConfigurationImpl, paramPackageDoc);
/*  95 */       String str = Util.getPackageName(paramPackageDoc);
/*  96 */       HtmlTree htmlTree1 = packageFrameWriter.getBody(false, packageFrameWriter.getWindowTitle(str));
/*  97 */       StringContent stringContent = new StringContent(str);
/*  98 */       HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, packageFrameWriter
/*  99 */           .getTargetPackageLink(paramPackageDoc, "classFrame", (Content)stringContent));
/* 100 */       htmlTree1.addContent((Content)htmlTree2);
/* 101 */       HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 102 */       htmlTree3.addStyle(HtmlStyle.indexContainer);
/* 103 */       packageFrameWriter.addClassListing((Content)htmlTree3);
/* 104 */       htmlTree1.addContent((Content)htmlTree3);
/* 105 */       packageFrameWriter.printHtmlDocument(paramConfigurationImpl.metakeywords
/* 106 */           .getMetaKeywords(paramPackageDoc), false, (Content)htmlTree1);
/* 107 */       packageFrameWriter.close();
/* 108 */     } catch (IOException iOException) {
/* 109 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 111 */             .toString(), DocPaths.PACKAGE_FRAME.getPath() });
/* 112 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassListing(Content paramContent) {
/* 124 */     ConfigurationImpl configurationImpl = this.configuration;
/* 125 */     if (this.packageDoc.isIncluded()) {
/* 126 */       addClassKindListing(this.packageDoc.interfaces(), 
/* 127 */           getResource("doclet.Interfaces"), paramContent);
/* 128 */       addClassKindListing(this.packageDoc.ordinaryClasses(), 
/* 129 */           getResource("doclet.Classes"), paramContent);
/* 130 */       addClassKindListing(this.packageDoc.enums(), 
/* 131 */           getResource("doclet.Enums"), paramContent);
/* 132 */       addClassKindListing(this.packageDoc.exceptions(), 
/* 133 */           getResource("doclet.Exceptions"), paramContent);
/* 134 */       addClassKindListing(this.packageDoc.errors(), 
/* 135 */           getResource("doclet.Errors"), paramContent);
/* 136 */       addClassKindListing((ClassDoc[])this.packageDoc.annotationTypes(), 
/* 137 */           getResource("doclet.AnnotationTypes"), paramContent);
/*     */     } else {
/* 139 */       String str = Util.getPackageName(this.packageDoc);
/* 140 */       addClassKindListing(configurationImpl.classDocCatalog.interfaces(str), 
/* 141 */           getResource("doclet.Interfaces"), paramContent);
/* 142 */       addClassKindListing(configurationImpl.classDocCatalog.ordinaryClasses(str), 
/* 143 */           getResource("doclet.Classes"), paramContent);
/* 144 */       addClassKindListing(configurationImpl.classDocCatalog.enums(str), 
/* 145 */           getResource("doclet.Enums"), paramContent);
/* 146 */       addClassKindListing(configurationImpl.classDocCatalog.exceptions(str), 
/* 147 */           getResource("doclet.Exceptions"), paramContent);
/* 148 */       addClassKindListing(configurationImpl.classDocCatalog.errors(str), 
/* 149 */           getResource("doclet.Errors"), paramContent);
/* 150 */       addClassKindListing(configurationImpl.classDocCatalog.annotationTypes(str), 
/* 151 */           getResource("doclet.AnnotationTypes"), paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassKindListing(ClassDoc[] paramArrayOfClassDoc, Content paramContent1, Content paramContent2) {
/* 164 */     paramArrayOfClassDoc = Util.filterOutPrivateClasses(paramArrayOfClassDoc, this.configuration.javafx);
/* 165 */     if (paramArrayOfClassDoc.length > 0) {
/* 166 */       Arrays.sort((Object[])paramArrayOfClassDoc);
/* 167 */       boolean bool = false;
/* 168 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 169 */       htmlTree.setTitle(paramContent1);
/* 170 */       for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 171 */         if (this.documentedClasses == null || this.documentedClasses
/* 172 */           .contains(paramArrayOfClassDoc[b]))
/*     */         {
/*     */           
/* 175 */           if (Util.isCoreClass(paramArrayOfClassDoc[b]) && this.configuration
/* 176 */             .isGeneratedDoc(paramArrayOfClassDoc[b])) {
/*     */             HtmlTree htmlTree1;
/*     */             
/* 179 */             if (!bool) {
/* 180 */               htmlTree1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, paramContent1);
/*     */               
/* 182 */               paramContent2.addContent((Content)htmlTree1);
/* 183 */               bool = true;
/*     */             } 
/* 185 */             StringContent stringContent = new StringContent(paramArrayOfClassDoc[b].name());
/* 186 */             if (paramArrayOfClassDoc[b].isInterface()) htmlTree1 = HtmlTree.SPAN(HtmlStyle.interfaceName, (Content)stringContent); 
/* 187 */             Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE_FRAME, paramArrayOfClassDoc[b]))
/* 188 */                 .label((Content)htmlTree1).target("classFrame"));
/* 189 */             HtmlTree htmlTree2 = HtmlTree.LI(content);
/* 190 */             htmlTree.addContent((Content)htmlTree2);
/*     */           }  } 
/* 192 */       }  paramContent2.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */