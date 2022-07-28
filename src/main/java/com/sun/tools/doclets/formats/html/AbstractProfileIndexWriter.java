/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractProfileIndexWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   protected Profiles profiles;
/*     */   
/*     */   public AbstractProfileIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  62 */     super(paramConfigurationImpl, paramDocPath);
/*  63 */     this.profiles = paramConfigurationImpl.profiles;
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
/*     */   
/*     */   protected abstract void addOverviewHeader(Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildProfileIndexFile(String paramString, boolean paramBoolean) throws IOException {
/* 119 */     String str = this.configuration.getText(paramString);
/* 120 */     HtmlTree htmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 121 */     addNavigationBarHeader((Content)htmlTree);
/* 122 */     addOverviewHeader((Content)htmlTree);
/* 123 */     addIndex((Content)htmlTree);
/* 124 */     addOverview((Content)htmlTree);
/* 125 */     addNavigationBarFooter((Content)htmlTree);
/* 126 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString, this.configuration.doctitle), paramBoolean, (Content)htmlTree);
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
/*     */   
/*     */   protected void buildProfilePackagesIndexFile(String paramString1, boolean paramBoolean, String paramString2) throws IOException {
/* 141 */     String str = this.configuration.getText(paramString1);
/* 142 */     HtmlTree htmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 143 */     addNavigationBarHeader((Content)htmlTree);
/* 144 */     addOverviewHeader((Content)htmlTree);
/* 145 */     addProfilePackagesIndex((Content)htmlTree, paramString2);
/* 146 */     addOverview((Content)htmlTree);
/* 147 */     addNavigationBarFooter((Content)htmlTree);
/* 148 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString1, this.configuration.doctitle), paramBoolean, (Content)htmlTree);
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
/* 166 */     addIndexContents(this.profiles, "doclet.Profile_Summary", this.configuration
/* 167 */         .getText("doclet.Member_Table_Summary", this.configuration
/* 168 */           .getText("doclet.Profile_Summary"), this.configuration
/* 169 */           .getText("doclet.profiles")), paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProfilePackagesIndex(Content paramContent, String paramString) {
/* 179 */     addProfilePackagesIndexContents(this.profiles, "doclet.Profile_Summary", this.configuration
/* 180 */         .getText("doclet.Member_Table_Summary", this.configuration
/* 181 */           .getText("doclet.Profile_Summary"), this.configuration
/* 182 */           .getText("doclet.profiles")), paramContent, paramString);
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
/*     */   protected void addIndexContents(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent) {
/* 196 */     if (paramProfiles.getProfileCount() > 0) {
/* 197 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 198 */       htmlTree.addStyle(HtmlStyle.indexHeader);
/* 199 */       addAllClassesLink((Content)htmlTree);
/* 200 */       addAllPackagesLink((Content)htmlTree);
/* 201 */       paramContent.addContent((Content)htmlTree);
/* 202 */       addProfilesList(paramProfiles, paramString1, paramString2, paramContent);
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
/*     */ 
/*     */   
/*     */   protected void addProfilePackagesIndexContents(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3) {
/* 218 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 219 */     htmlTree.addStyle(HtmlStyle.indexHeader);
/* 220 */     addAllClassesLink((Content)htmlTree);
/* 221 */     addAllPackagesLink((Content)htmlTree);
/* 222 */     addAllProfilesLink((Content)htmlTree);
/* 223 */     paramContent.addContent((Content)htmlTree);
/* 224 */     addProfilePackagesList(paramProfiles, paramString1, paramString2, paramContent, paramString3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addConfigurationTitle(Content paramContent) {
/* 233 */     if (this.configuration.doctitle.length() > 0) {
/* 234 */       RawHtml rawHtml = new RawHtml(this.configuration.doctitle);
/* 235 */       HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.title, (Content)rawHtml);
/*     */       
/* 237 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 238 */       paramContent.addContent((Content)htmlTree2);
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
/* 249 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.overviewLabel);
/*     */   }
/*     */   
/*     */   protected void addAllClassesLink(Content paramContent) {}
/*     */   
/*     */   protected void addAllPackagesLink(Content paramContent) {}
/*     */   
/*     */   protected void addAllProfilesLink(Content paramContent) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractProfileIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */