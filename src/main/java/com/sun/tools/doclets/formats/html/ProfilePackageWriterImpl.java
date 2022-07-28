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
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfilePackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
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
/*     */ public class ProfilePackageWriterImpl
/*     */   extends HtmlDocletWriter
/*     */   implements ProfilePackageSummaryWriter
/*     */ {
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */   protected PackageDoc packageDoc;
/*     */   protected String profileName;
/*     */   protected int profileValue;
/*     */   
/*     */   public ProfilePackageWriterImpl(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile) throws IOException {
/*  94 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc1).resolve(
/*  95 */           DocPaths.profilePackageSummary(paramProfile.name)));
/*  96 */     this.prev = paramPackageDoc2;
/*  97 */     this.next = paramPackageDoc3;
/*  98 */     this.packageDoc = paramPackageDoc1;
/*  99 */     this.profileName = paramProfile.name;
/* 100 */     this.profileValue = paramProfile.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPackageHeader(String paramString) {
/* 107 */     String str = this.packageDoc.name();
/* 108 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str));
/* 109 */     addTop((Content)htmlTree1);
/* 110 */     addNavLinks(true, (Content)htmlTree1);
/* 111 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 112 */     htmlTree2.addStyle(HtmlStyle.header);
/* 113 */     StringContent stringContent = new StringContent(this.profileName);
/* 114 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.subTitle, (Content)stringContent);
/* 115 */     htmlTree2.addContent((Content)htmlTree3);
/* 116 */     HtmlTree htmlTree4 = new HtmlTree(HtmlTag.P);
/* 117 */     addAnnotationInfo(this.packageDoc, (Content)htmlTree4);
/* 118 */     htmlTree2.addContent((Content)htmlTree4);
/* 119 */     HtmlTree htmlTree5 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.packageLabel);
/*     */     
/* 121 */     htmlTree5.addContent(getSpace());
/* 122 */     RawHtml rawHtml = new RawHtml(paramString);
/* 123 */     htmlTree5.addContent((Content)rawHtml);
/* 124 */     htmlTree2.addContent((Content)htmlTree5);
/* 125 */     addDeprecationInfo((Content)htmlTree2);
/* 126 */     if ((this.packageDoc.inlineTags()).length > 0 && !this.configuration.nocomment) {
/* 127 */       HtmlTree htmlTree6 = new HtmlTree(HtmlTag.DIV);
/* 128 */       htmlTree6.addStyle(HtmlStyle.docSummary);
/* 129 */       addSummaryComment((Doc)this.packageDoc, (Content)htmlTree6);
/* 130 */       htmlTree2.addContent((Content)htmlTree6);
/* 131 */       Content content1 = getSpace();
/* 132 */       Content content2 = getHyperLink(getDocLink(SectionName.PACKAGE_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/*     */       
/* 135 */       HtmlTree htmlTree7 = new HtmlTree(HtmlTag.P, new Content[] { this.seeLabel, content1, content2 });
/* 136 */       htmlTree2.addContent((Content)htmlTree7);
/*     */     } 
/* 138 */     htmlTree1.addContent((Content)htmlTree2);
/* 139 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentHeader() {
/* 146 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 147 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 148 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecationInfo(Content paramContent) {
/* 157 */     Tag[] arrayOfTag = this.packageDoc.tags("deprecated");
/* 158 */     if (Util.isDeprecated((Doc)this.packageDoc)) {
/* 159 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 160 */       htmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 161 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 162 */       htmlTree1.addContent((Content)htmlTree2);
/* 163 */       if (arrayOfTag.length > 0) {
/* 164 */         Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 165 */         if (arrayOfTag1.length > 0) {
/* 166 */           addInlineDeprecatedComment((Doc)this.packageDoc, arrayOfTag[0], (Content)htmlTree1);
/*     */         }
/*     */       } 
/* 169 */       paramContent.addContent((Content)htmlTree1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent) {
/* 178 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 179 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 180 */     addClassesSummary(paramArrayOfClassDoc, paramString1, paramString2, paramArrayOfString, (Content)htmlTree, this.profileValue);
/*     */     
/* 182 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSummaryHeader() {
/* 189 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 190 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 191 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageDescription(Content paramContent) {
/* 198 */     if ((this.packageDoc.inlineTags()).length > 0) {
/* 199 */       paramContent.addContent(
/* 200 */           getMarkerAnchor(SectionName.PACKAGE_DESCRIPTION));
/*     */       
/* 202 */       StringContent stringContent = new StringContent(this.configuration.getText("doclet.Package_Description", this.packageDoc
/* 203 */             .name()));
/* 204 */       paramContent.addContent((Content)HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, (Content)stringContent));
/*     */       
/* 206 */       addInlineComment((Doc)this.packageDoc, paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageTags(Content paramContent) {
/* 214 */     addTagsInfo((Doc)this.packageDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageFooter(Content paramContent) {
/* 221 */     addNavLinks(false, paramContent);
/* 222 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 229 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.packageDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 239 */     Content content = getHyperLink(DocPaths.PACKAGE_USE, this.useLabel, "", "");
/*     */     
/* 241 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkPrevious() {
/*     */     HtmlTree htmlTree;
/* 252 */     if (this.prev == null) {
/* 253 */       htmlTree = HtmlTree.LI(this.prevpackageLabel);
/*     */     } else {
/* 255 */       DocPath docPath = DocPath.relativePath(this.packageDoc, this.prev);
/* 256 */       htmlTree = HtmlTree.LI(getHyperLink(docPath.resolve(DocPaths.profilePackageSummary(this.profileName)), this.prevpackageLabel, "", ""));
/*     */     } 
/*     */     
/* 259 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/*     */     HtmlTree htmlTree;
/* 269 */     if (this.next == null) {
/* 270 */       htmlTree = HtmlTree.LI(this.nextpackageLabel);
/*     */     } else {
/* 272 */       DocPath docPath = DocPath.relativePath(this.packageDoc, this.next);
/* 273 */       htmlTree = HtmlTree.LI(getHyperLink(docPath.resolve(DocPaths.profilePackageSummary(this.profileName)), this.nextpackageLabel, "", ""));
/*     */     } 
/*     */     
/* 276 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 286 */     Content content = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */     
/* 288 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/* 298 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.packageLabel);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ProfilePackageWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */