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
/*     */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
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
/*     */ public class PackageWriterImpl
/*     */   extends HtmlDocletWriter
/*     */   implements PackageSummaryWriter
/*     */ {
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */   protected PackageDoc packageDoc;
/*     */   
/*     */   public PackageWriterImpl(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3) throws IOException {
/*  83 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc1).resolve(DocPaths.PACKAGE_SUMMARY));
/*  84 */     this.prev = paramPackageDoc2;
/*  85 */     this.next = paramPackageDoc3;
/*  86 */     this.packageDoc = paramPackageDoc1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPackageHeader(String paramString) {
/*  93 */     String str = this.packageDoc.name();
/*  94 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str));
/*  95 */     addTop((Content)htmlTree1);
/*  96 */     addNavLinks(true, (Content)htmlTree1);
/*  97 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  98 */     htmlTree2.addStyle(HtmlStyle.header);
/*  99 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.P);
/* 100 */     addAnnotationInfo(this.packageDoc, (Content)htmlTree3);
/* 101 */     htmlTree2.addContent((Content)htmlTree3);
/* 102 */     HtmlTree htmlTree4 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.packageLabel);
/*     */     
/* 104 */     htmlTree4.addContent(getSpace());
/* 105 */     StringContent stringContent = new StringContent(paramString);
/* 106 */     htmlTree4.addContent((Content)stringContent);
/* 107 */     htmlTree2.addContent((Content)htmlTree4);
/* 108 */     addDeprecationInfo((Content)htmlTree2);
/* 109 */     if ((this.packageDoc.inlineTags()).length > 0 && !this.configuration.nocomment) {
/* 110 */       HtmlTree htmlTree5 = new HtmlTree(HtmlTag.DIV);
/* 111 */       htmlTree5.addStyle(HtmlStyle.docSummary);
/* 112 */       addSummaryComment((Doc)this.packageDoc, (Content)htmlTree5);
/* 113 */       htmlTree2.addContent((Content)htmlTree5);
/* 114 */       Content content1 = getSpace();
/* 115 */       Content content2 = getHyperLink(getDocLink(SectionName.PACKAGE_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/*     */       
/* 118 */       HtmlTree htmlTree6 = new HtmlTree(HtmlTag.P, new Content[] { this.seeLabel, content1, content2 });
/* 119 */       htmlTree2.addContent((Content)htmlTree6);
/*     */     } 
/* 121 */     htmlTree1.addContent((Content)htmlTree2);
/* 122 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentHeader() {
/* 129 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 130 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 131 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecationInfo(Content paramContent) {
/* 140 */     Tag[] arrayOfTag = this.packageDoc.tags("deprecated");
/* 141 */     if (Util.isDeprecated((Doc)this.packageDoc)) {
/* 142 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 143 */       htmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 144 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 145 */       htmlTree1.addContent((Content)htmlTree2);
/* 146 */       if (arrayOfTag.length > 0) {
/* 147 */         Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 148 */         if (arrayOfTag1.length > 0) {
/* 149 */           addInlineDeprecatedComment((Doc)this.packageDoc, arrayOfTag[0], (Content)htmlTree1);
/*     */         }
/*     */       } 
/* 152 */       paramContent.addContent((Content)htmlTree1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSummaryHeader() {
/* 160 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 161 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 162 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent) {
/* 170 */     if (paramArrayOfClassDoc.length > 0) {
/* 171 */       Arrays.sort((Object[])paramArrayOfClassDoc);
/* 172 */       Content content = getTableCaption((Content)new RawHtml(paramString1));
/* 173 */       HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.typeSummary, 0, 3, 0, paramString2, content);
/*     */       
/* 175 */       htmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/* 176 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 177 */       for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 178 */         if (Util.isCoreClass(paramArrayOfClassDoc[b]) && this.configuration
/* 179 */           .isGeneratedDoc(paramArrayOfClassDoc[b])) {
/*     */ 
/*     */           
/* 182 */           Content content1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE, paramArrayOfClassDoc[b]));
/*     */           
/* 184 */           HtmlTree htmlTree4 = HtmlTree.TD(HtmlStyle.colFirst, content1);
/* 185 */           HtmlTree htmlTree5 = HtmlTree.TR((Content)htmlTree4);
/* 186 */           if (b % 2 == 0) {
/* 187 */             htmlTree5.addStyle(HtmlStyle.altColor);
/*     */           } else {
/* 189 */             htmlTree5.addStyle(HtmlStyle.rowColor);
/* 190 */           }  HtmlTree htmlTree6 = new HtmlTree(HtmlTag.TD);
/* 191 */           htmlTree6.addStyle(HtmlStyle.colLast);
/* 192 */           if (Util.isDeprecated((Doc)paramArrayOfClassDoc[b])) {
/* 193 */             htmlTree6.addContent(this.deprecatedLabel);
/* 194 */             if ((paramArrayOfClassDoc[b].tags("deprecated")).length > 0) {
/* 195 */               addSummaryDeprecatedComment((Doc)paramArrayOfClassDoc[b], paramArrayOfClassDoc[b]
/* 196 */                   .tags("deprecated")[0], (Content)htmlTree6);
/*     */             }
/*     */           } else {
/*     */             
/* 200 */             addSummaryComment((Doc)paramArrayOfClassDoc[b], (Content)htmlTree6);
/* 201 */           }  htmlTree5.addContent((Content)htmlTree6);
/* 202 */           htmlTree2.addContent((Content)htmlTree5);
/*     */         } 
/* 204 */       }  htmlTree1.addContent((Content)htmlTree2);
/* 205 */       HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 206 */       paramContent.addContent((Content)htmlTree3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageDescription(Content paramContent) {
/* 214 */     if ((this.packageDoc.inlineTags()).length > 0) {
/* 215 */       paramContent.addContent(
/* 216 */           getMarkerAnchor(SectionName.PACKAGE_DESCRIPTION));
/*     */       
/* 218 */       StringContent stringContent = new StringContent(this.configuration.getText("doclet.Package_Description", this.packageDoc
/* 219 */             .name()));
/* 220 */       paramContent.addContent((Content)HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, (Content)stringContent));
/*     */       
/* 222 */       addInlineComment((Doc)this.packageDoc, paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageTags(Content paramContent) {
/* 230 */     addTagsInfo((Doc)this.packageDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageFooter(Content paramContent) {
/* 237 */     addNavLinks(false, paramContent);
/* 238 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 245 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.packageDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 255 */     Content content = getHyperLink(DocPaths.PACKAGE_USE, this.useLabel, "", "");
/*     */     
/* 257 */     return (Content)HtmlTree.LI(content);
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
/* 268 */     if (this.prev == null) {
/* 269 */       htmlTree = HtmlTree.LI(this.prevpackageLabel);
/*     */     } else {
/* 271 */       DocPath docPath = DocPath.relativePath(this.packageDoc, this.prev);
/* 272 */       htmlTree = HtmlTree.LI(getHyperLink(docPath.resolve(DocPaths.PACKAGE_SUMMARY), this.prevpackageLabel, "", ""));
/*     */     } 
/*     */     
/* 275 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/*     */     HtmlTree htmlTree;
/* 285 */     if (this.next == null) {
/* 286 */       htmlTree = HtmlTree.LI(this.nextpackageLabel);
/*     */     } else {
/* 288 */       DocPath docPath = DocPath.relativePath(this.packageDoc, this.next);
/* 289 */       htmlTree = HtmlTree.LI(getHyperLink(docPath.resolve(DocPaths.PACKAGE_SUMMARY), this.nextpackageLabel, "", ""));
/*     */     } 
/*     */     
/* 292 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 302 */     Content content = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */     
/* 304 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/* 314 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.packageLabel);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */