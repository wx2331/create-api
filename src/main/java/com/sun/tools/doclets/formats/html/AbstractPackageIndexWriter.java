/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
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
/*     */ public abstract class AbstractPackageIndexWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   protected PackageDoc[] packages;
/*     */   
/*     */   public AbstractPackageIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  64 */     super(paramConfigurationImpl, paramDocPath);
/*  65 */     this.packages = paramConfigurationImpl.packages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addNavigationBarHeader(Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addNavigationBarFooter(Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addOverviewHeader(Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildPackageIndexFile(String paramString, boolean paramBoolean) throws IOException {
/* 109 */     String str = this.configuration.getText(paramString);
/* 110 */     HtmlTree htmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 111 */     addNavigationBarHeader((Content)htmlTree);
/* 112 */     addOverviewHeader((Content)htmlTree);
/* 113 */     addIndex((Content)htmlTree);
/* 114 */     addOverview((Content)htmlTree);
/* 115 */     addNavigationBarFooter((Content)htmlTree);
/* 116 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString, this.configuration.doctitle), paramBoolean, (Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addOverview(Content paramContent) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addIndex(Content paramContent) {
/* 134 */     addIndexContents(this.packages, "doclet.Package_Summary", this.configuration
/* 135 */         .getText("doclet.Member_Table_Summary", this.configuration
/* 136 */           .getText("doclet.Package_Summary"), this.configuration
/* 137 */           .getText("doclet.packages")), paramContent);
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
/*     */   
/*     */   protected void addIndexContents(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent) {
/* 151 */     if (paramArrayOfPackageDoc.length > 0) {
/* 152 */       Arrays.sort((Object[])paramArrayOfPackageDoc);
/* 153 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 154 */       htmlTree.addStyle(HtmlStyle.indexHeader);
/* 155 */       addAllClassesLink((Content)htmlTree);
/* 156 */       if (this.configuration.showProfiles) {
/* 157 */         addAllProfilesLink((Content)htmlTree);
/*     */       }
/* 159 */       paramContent.addContent((Content)htmlTree);
/* 160 */       if (this.configuration.showProfiles && this.configuration.profilePackages.size() > 0) {
/* 161 */         Content content = this.configuration.getResource("doclet.Profiles");
/* 162 */         addProfilesList(content, paramContent);
/*     */       } 
/* 164 */       addPackagesList(paramArrayOfPackageDoc, paramString1, paramString2, paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addConfigurationTitle(Content paramContent) {
/* 174 */     if (this.configuration.doctitle.length() > 0) {
/* 175 */       RawHtml rawHtml = new RawHtml(this.configuration.doctitle);
/* 176 */       HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.title, (Content)rawHtml);
/*     */       
/* 178 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 179 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkContents() {
/* 190 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.overviewLabel);
/*     */   }
/*     */   
/*     */   protected void addAllClassesLink(Content paramContent) {}
/*     */   
/*     */   protected void addAllProfilesLink(Content paramContent) {}
/*     */   
/*     */   protected void addProfilesList(Content paramContent1, Content paramContent2) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractPackageIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */