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
/*     */ import com.sun.tools.javac.sym.Profiles;
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
/*     */ 
/*     */ public class ProfilePackageIndexFrameWriter
/*     */   extends AbstractProfileIndexWriter
/*     */ {
/*     */   public ProfilePackageIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  58 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, String paramString) {
/*  69 */     DocPath docPath = DocPaths.profileFrame(paramString);
/*     */     try {
/*  71 */       ProfilePackageIndexFrameWriter profilePackageIndexFrameWriter = new ProfilePackageIndexFrameWriter(paramConfigurationImpl, docPath);
/*  72 */       profilePackageIndexFrameWriter.buildProfilePackagesIndexFile("doclet.Window_Overview", false, paramString);
/*  73 */       profilePackageIndexFrameWriter.close();
/*  74 */     } catch (IOException iOException) {
/*  75 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  77 */             .toString(), docPath });
/*  78 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3) {
/*  87 */     StringContent stringContent = new StringContent(paramString3);
/*  88 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, 
/*  89 */         getTargetProfileLink("classFrame", (Content)stringContent, paramString3));
/*  90 */     htmlTree1.addContent(getSpace());
/*  91 */     htmlTree1.addContent(this.packagesLabel);
/*  92 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, (Content)htmlTree1);
/*  93 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/*  94 */     htmlTree3.setTitle(this.packagesLabel);
/*  95 */     PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(paramString3);
/*  96 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/*  97 */       if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b])) {
/*  98 */         htmlTree3.addContent(getPackage(arrayOfPackageDoc[b], paramString3));
/*     */       }
/*     */     } 
/* 101 */     htmlTree2.addContent((Content)htmlTree3);
/* 102 */     paramContent.addContent((Content)htmlTree2);
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
/*     */   protected Content getPackage(PackageDoc paramPackageDoc, String paramString) {
/*     */     Content content;
/* 115 */     if (paramPackageDoc.name().length() > 0) {
/* 116 */       Content content1 = getPackageLabel(paramPackageDoc.name());
/* 117 */       content = getHyperLink(pathString(paramPackageDoc, 
/* 118 */             DocPaths.profilePackageFrame(paramString)), content1, "", "packageFrame");
/*     */     } else {
/*     */       
/* 121 */       StringContent stringContent = new StringContent("<unnamed package>");
/* 122 */       content = getHyperLink(DocPaths.PACKAGE_FRAME, (Content)stringContent, "", "packageFrame");
/*     */     } 
/*     */     
/* 125 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarHeader(Content paramContent) {
/*     */     RawHtml rawHtml;
/* 134 */     if (this.configuration.packagesheader.length() > 0) {
/* 135 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     } else {
/* 137 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     } 
/* 139 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, (Content)rawHtml);
/*     */     
/* 141 */     paramContent.addContent((Content)htmlTree);
/*     */   }
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
/*     */   protected void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClassesLink(Content paramContent) {
/* 161 */     Content content = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */     
/* 163 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 164 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllPackagesLink(Content paramContent) {
/* 174 */     Content content = getHyperLink(DocPaths.OVERVIEW_FRAME, this.allpackagesLabel, "", "packageListFrame");
/*     */     
/* 176 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 177 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllProfilesLink(Content paramContent) {
/* 187 */     Content content = getHyperLink(DocPaths.PROFILE_OVERVIEW_FRAME, this.allprofilesLabel, "", "packageListFrame");
/*     */     
/* 189 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 190 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarFooter(Content paramContent) {
/* 197 */     HtmlTree htmlTree = HtmlTree.P(getSpace());
/* 198 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ProfilePackageIndexFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */