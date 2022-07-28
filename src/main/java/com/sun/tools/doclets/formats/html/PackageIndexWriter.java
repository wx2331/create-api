/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
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
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageIndexWriter
/*     */   extends AbstractPackageIndexWriter
/*     */ {
/*     */   private RootDoc root;
/*     */   private Map<String, List<PackageDoc>> groupPackageMap;
/*     */   private List<String> groupList;
/*     */   
/*     */   public PackageIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  79 */     super(paramConfigurationImpl, paramDocPath);
/*  80 */     this.root = paramConfigurationImpl.root;
/*  81 */     this.groupPackageMap = paramConfigurationImpl.group.groupPackages(this.packages);
/*  82 */     this.groupList = paramConfigurationImpl.group.getGroupList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/*  92 */     DocPath docPath = DocPaths.OVERVIEW_SUMMARY;
/*     */     try {
/*  94 */       PackageIndexWriter packageIndexWriter = new PackageIndexWriter(paramConfigurationImpl, docPath);
/*  95 */       packageIndexWriter.buildPackageIndexFile("doclet.Window_Overview_Summary", true);
/*  96 */       packageIndexWriter.close();
/*  97 */     } catch (IOException iOException) {
/*  98 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 100 */             .toString(), docPath });
/* 101 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addIndex(Content paramContent) {
/* 112 */     for (byte b = 0; b < this.groupList.size(); b++) {
/* 113 */       String str = this.groupList.get(b);
/* 114 */       List list = this.groupPackageMap.get(str);
/* 115 */       if (list != null && list.size() > 0) {
/* 116 */         addIndexContents((PackageDoc[])list.toArray((Object[])new PackageDoc[list.size()]), str, this.configuration
/* 117 */             .getText("doclet.Member_Table_Summary", str, this.configuration
/* 118 */               .getText("doclet.packages")), paramContent);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProfilesList(Content paramContent1, Content paramContent2) {
/* 127 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlTag.H2, paramContent1);
/* 128 */     HtmlTree htmlTree2 = HtmlTree.DIV((Content)htmlTree1);
/* 129 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/*     */     
/* 131 */     for (byte b = 1; b < this.configuration.profiles.getProfileCount(); b++) {
/* 132 */       String str = (Profile.lookup(b)).name;
/*     */ 
/*     */       
/* 135 */       if (this.configuration.shouldDocumentProfile(str)) {
/* 136 */         Content content = getTargetProfileLink("classFrame", (Content)new StringContent(str), str);
/*     */         
/* 138 */         HtmlTree htmlTree = HtmlTree.LI(content);
/* 139 */         htmlTree3.addContent((Content)htmlTree);
/*     */       } 
/*     */     } 
/* 142 */     htmlTree2.addContent((Content)htmlTree3);
/* 143 */     HtmlTree htmlTree4 = HtmlTree.DIV(HtmlStyle.contentContainer, (Content)htmlTree2);
/* 144 */     paramContent2.addContent((Content)htmlTree4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent) {
/* 152 */     HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.overviewSummary, 0, 3, 0, paramString2, 
/* 153 */         getTableCaption((Content)new RawHtml(paramString1)));
/* 154 */     htmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 155 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 156 */     addPackagesList(paramArrayOfPackageDoc, (Content)htmlTree2);
/* 157 */     htmlTree1.addContent((Content)htmlTree2);
/* 158 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.contentContainer, (Content)htmlTree1);
/* 159 */     paramContent.addContent((Content)htmlTree3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, Content paramContent) {
/* 169 */     for (byte b = 0; b < paramArrayOfPackageDoc.length; b++) {
/* 170 */       if (paramArrayOfPackageDoc[b] != null && paramArrayOfPackageDoc[b].name().length() > 0 && (
/* 171 */         !this.configuration.nodeprecated || !Util.isDeprecated((Doc)paramArrayOfPackageDoc[b]))) {
/*     */         
/* 173 */         Content content = getPackageLink(paramArrayOfPackageDoc[b], 
/* 174 */             getPackageName(paramArrayOfPackageDoc[b]));
/* 175 */         HtmlTree htmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, content);
/* 176 */         HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TD);
/* 177 */         htmlTree2.addStyle(HtmlStyle.colLast);
/* 178 */         addSummaryComment((Doc)paramArrayOfPackageDoc[b], (Content)htmlTree2);
/* 179 */         HtmlTree htmlTree3 = HtmlTree.TR((Content)htmlTree1);
/* 180 */         htmlTree3.addContent((Content)htmlTree2);
/* 181 */         if (b % 2 == 0) {
/* 182 */           htmlTree3.addStyle(HtmlStyle.altColor);
/*     */         } else {
/* 184 */           htmlTree3.addStyle(HtmlStyle.rowColor);
/* 185 */         }  paramContent.addContent((Content)htmlTree3);
/*     */       } 
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
/*     */   protected void addOverviewHeader(Content paramContent) {
/* 198 */     if ((this.root.inlineTags()).length > 0) {
/* 199 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 200 */       htmlTree1.addStyle(HtmlStyle.subTitle);
/* 201 */       addSummaryComment((Doc)this.root, (Content)htmlTree1);
/* 202 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 203 */       Content content1 = this.seeLabel;
/* 204 */       content1.addContent(" ");
/* 205 */       HtmlTree htmlTree3 = HtmlTree.P(content1);
/* 206 */       Content content2 = getHyperLink(getDocLink(SectionName.OVERVIEW_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/*     */       
/* 209 */       htmlTree3.addContent(content2);
/* 210 */       htmlTree2.addContent((Content)htmlTree3);
/* 211 */       paramContent.addContent((Content)htmlTree2);
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
/*     */   protected void addOverviewComment(Content paramContent) {
/* 223 */     if ((this.root.inlineTags()).length > 0) {
/* 224 */       paramContent.addContent(
/* 225 */           getMarkerAnchor(SectionName.OVERVIEW_DESCRIPTION));
/* 226 */       addInlineComment((Doc)this.root, paramContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addOverview(Content paramContent) throws IOException {
/* 237 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 238 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 239 */     addOverviewComment((Content)htmlTree);
/* 240 */     addTagsInfo((Doc)this.root, (Content)htmlTree);
/* 241 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarHeader(Content paramContent) {
/* 252 */     addTop(paramContent);
/* 253 */     addNavLinks(true, paramContent);
/* 254 */     addConfigurationTitle(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavigationBarFooter(Content paramContent) {
/* 264 */     addNavLinks(false, paramContent);
/* 265 */     addBottom(paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */