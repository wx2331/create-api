/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
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
/*     */ import com.sun.tools.javac.jvm.Profile;
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
/*     */ 
/*     */ 
/*     */ public class ProfileIndexFrameWriter
/*     */   extends AbstractProfileIndexWriter
/*     */ {
/*     */   public ProfileIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  58 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/*  68 */     DocPath docPath = DocPaths.PROFILE_OVERVIEW_FRAME;
/*     */     try {
/*  70 */       ProfileIndexFrameWriter profileIndexFrameWriter = new ProfileIndexFrameWriter(paramConfigurationImpl, docPath);
/*  71 */       profileIndexFrameWriter.buildProfileIndexFile("doclet.Window_Overview", false);
/*  72 */       profileIndexFrameWriter.close();
/*  73 */     } catch (IOException iOException) {
/*  74 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  76 */             .toString(), docPath });
/*  77 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent) {
/*  86 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.PROFILE_HEADING, true, this.profilesLabel);
/*     */     
/*  88 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, (Content)htmlTree1);
/*  89 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/*  90 */     htmlTree3.setTitle(this.profilesLabel);
/*     */     
/*  92 */     for (byte b = 1; b < paramProfiles.getProfileCount(); b++) {
/*  93 */       String str = (Profile.lookup(b)).name;
/*     */ 
/*     */       
/*  96 */       if (this.configuration.shouldDocumentProfile(str))
/*  97 */         htmlTree3.addContent(getProfile(str)); 
/*     */     } 
/*  99 */     htmlTree2.addContent((Content)htmlTree3);
/* 100 */     paramContent.addContent((Content)htmlTree2);
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
/*     */   protected Content getProfile(String paramString) {
/* 112 */     StringContent stringContent = new StringContent(paramString);
/* 113 */     Content content = getHyperLink(DocPaths.profileFrame(paramString), (Content)stringContent, "", "packageListFrame");
/*     */     
/* 115 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarHeader(Content paramContent) {
/*     */     RawHtml rawHtml;
/* 124 */     if (this.configuration.packagesheader.length() > 0) {
/* 125 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     } else {
/* 127 */       rawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     } 
/* 129 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, (Content)rawHtml);
/*     */     
/* 131 */     paramContent.addContent((Content)htmlTree);
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
/* 147 */     Content content = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */     
/* 149 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 150 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllPackagesLink(Content paramContent) {
/* 160 */     Content content = getHyperLink(DocPaths.OVERVIEW_FRAME, this.allpackagesLabel, "", "packageListFrame");
/*     */     
/* 162 */     HtmlTree htmlTree = HtmlTree.SPAN(content);
/* 163 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarFooter(Content paramContent) {
/* 170 */     HtmlTree htmlTree = HtmlTree.P(getSpace());
/* 171 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */   
/*     */   protected void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ProfileIndexFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */