/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
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
/*     */ public class ProfileWriterImpl
/*     */   extends HtmlDocletWriter
/*     */   implements ProfileSummaryWriter
/*     */ {
/*     */   protected Profile prevProfile;
/*     */   protected Profile nextProfile;
/*     */   protected Profile profile;
/*     */   
/*     */   public ProfileWriterImpl(ConfigurationImpl paramConfigurationImpl, Profile paramProfile1, Profile paramProfile2, Profile paramProfile3) throws IOException {
/*  80 */     super(paramConfigurationImpl, DocPaths.profileSummary(paramProfile1.name));
/*  81 */     this.prevProfile = paramProfile2;
/*  82 */     this.nextProfile = paramProfile3;
/*  83 */     this.profile = paramProfile1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getProfileHeader(String paramString) {
/*  90 */     String str = this.profile.name;
/*  91 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str));
/*  92 */     addTop((Content)htmlTree1);
/*  93 */     addNavLinks(true, (Content)htmlTree1);
/*  94 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  95 */     htmlTree2.addStyle(HtmlStyle.header);
/*  96 */     HtmlTree htmlTree3 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.profileLabel);
/*     */     
/*  98 */     htmlTree3.addContent(getSpace());
/*  99 */     RawHtml rawHtml = new RawHtml(paramString);
/* 100 */     htmlTree3.addContent((Content)rawHtml);
/* 101 */     htmlTree2.addContent((Content)htmlTree3);
/* 102 */     htmlTree1.addContent((Content)htmlTree2);
/* 103 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentHeader() {
/* 110 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 111 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 112 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSummaryHeader() {
/* 119 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 120 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 121 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSummaryTree(Content paramContent) {
/* 128 */     HtmlTree htmlTree = HtmlTree.UL(HtmlStyle.blockList, paramContent);
/* 129 */     return (Content)HtmlTree.DIV(HtmlStyle.summary, (Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPackageSummaryHeader(PackageDoc paramPackageDoc) {
/* 137 */     Content content = getTargetProfilePackageLink(paramPackageDoc, "classFrame", (Content)new StringContent(paramPackageDoc
/* 138 */           .name()), this.profile.name);
/* 139 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlTag.H3, content);
/* 140 */     HtmlTree htmlTree2 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 141 */     addPackageDeprecationInfo((Content)htmlTree2, paramPackageDoc);
/* 142 */     return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPackageSummaryTree(Content paramContent) {
/* 149 */     return (Content)HtmlTree.UL(HtmlStyle.blockList, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent) {
/* 158 */     addClassesSummary(paramArrayOfClassDoc, paramString1, paramString2, paramArrayOfString, paramContent, this.profile.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProfileFooter(Content paramContent) {
/* 166 */     addNavLinks(false, paramContent);
/* 167 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 174 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.profile), true, paramContent);
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
/*     */   public void addPackageDeprecationInfo(Content paramContent, PackageDoc paramPackageDoc) {
/* 186 */     if (Util.isDeprecated((Doc)paramPackageDoc)) {
/* 187 */       Tag[] arrayOfTag = paramPackageDoc.tags("deprecated");
/* 188 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 189 */       htmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 190 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 191 */       htmlTree1.addContent((Content)htmlTree2);
/* 192 */       if (arrayOfTag.length > 0) {
/* 193 */         Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 194 */         if (arrayOfTag1.length > 0) {
/* 195 */           addInlineDeprecatedComment((Doc)paramPackageDoc, arrayOfTag[0], (Content)htmlTree1);
/*     */         }
/*     */       } 
/* 198 */       paramContent.addContent((Content)htmlTree1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkPrevious() {
/*     */     HtmlTree htmlTree;
/* 209 */     if (this.prevProfile == null) {
/* 210 */       htmlTree = HtmlTree.LI(this.prevprofileLabel);
/*     */     } else {
/* 212 */       htmlTree = HtmlTree.LI(getHyperLink(this.pathToRoot.resolve(DocPaths.profileSummary(this.prevProfile.name)), this.prevprofileLabel, "", ""));
/*     */     } 
/*     */     
/* 215 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/*     */     HtmlTree htmlTree;
/* 225 */     if (this.nextProfile == null) {
/* 226 */       htmlTree = HtmlTree.LI(this.nextprofileLabel);
/*     */     } else {
/* 228 */       htmlTree = HtmlTree.LI(getHyperLink(this.pathToRoot.resolve(DocPaths.profileSummary(this.nextProfile.name)), this.nextprofileLabel, "", ""));
/*     */     } 
/*     */     
/* 231 */     return (Content)htmlTree;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ProfileWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */