/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
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
/*     */ public class PackageIndexFrameWriter
/*     */   extends AbstractPackageIndexWriter
/*     */ {
/*     */   public PackageIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  56 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/*  65 */     DocPath docPath = DocPaths.OVERVIEW_FRAME;
/*     */     try {
/*  67 */       PackageIndexFrameWriter packageIndexFrameWriter = new PackageIndexFrameWriter(paramConfigurationImpl, docPath);
/*  68 */       packageIndexFrameWriter.buildPackageIndexFile("doclet.Window_Overview", false);
/*  69 */       packageIndexFrameWriter.close();
/*  70 */     } catch (IOException iOException) {
/*  71 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  73 */             .toString(), docPath });
/*  74 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent) {
/*  83 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, this.packagesLabel);
/*     */     
/*  85 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, (Content)htmlTree1);
/*  86 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/*  87 */     htmlTree3.setTitle(this.packagesLabel);
/*  88 */     for (byte b = 0; b < paramArrayOfPackageDoc.length; b++) {
/*     */ 
/*     */       
/*  91 */       if (paramArrayOfPackageDoc[b] != null && (!this.configuration.nodeprecated || 
/*  92 */         !Util.isDeprecated((Doc)paramArrayOfPackageDoc[b]))) {
/*  93 */         htmlTree3.addContent(getPackage(paramArrayOfPackageDoc[b]));
/*     */       }
/*     */     } 
/*  96 */     htmlTree2.addContent((Content)htmlTree3);
/*  97 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getPackage(PackageDoc paramPackageDoc) {
/*     */     Content content;
/* 109 */     if (paramPackageDoc.name().length() > 0) {
/* 110 */       Content content1 = getPackageLabel(paramPackageDoc.name());
/* 111 */       content = getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_FRAME), content1, "", "packageFrame");
/*     */     }
/*     */     else {
/*     */       
/* 115 */       StringContent stringContent = new StringContent("<unnamed package>");
/* 116 */       content = getHyperLink(DocPaths.PACKAGE_FRAME, (Content)stringContent, "", "packageFrame");
/*     */     } 
/*     */     
/* 119 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarHeader(Content paramContent) {
/*     */     RawHtml rawHtml;
/* 128 */     if (this.configuration.packagesheader.length() > 0) {
/* 129 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     } else {
/* 131 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     } 
/* 133 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, (Content)rawHtml);
/*     */     
/* 135 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addOverviewHeader(Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClassesLink(Content paramContent) {
/* 151 */     Content content = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */     
/* 153 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 154 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllProfilesLink(Content paramContent) {
/* 164 */     Content content = getHyperLink(DocPaths.PROFILE_OVERVIEW_FRAME, this.allprofilesLabel, "", "packageListFrame");
/*     */     
/* 166 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 167 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarFooter(Content paramContent) {
/* 174 */     HtmlTree htmlTree = HtmlTree.P(getSpace());
/* 175 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageIndexFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */