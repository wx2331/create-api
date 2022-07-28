/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantsSummaryWriterImpl
/*     */   extends HtmlDocletWriter
/*     */   implements ConstantsSummaryWriter
/*     */ {
/*     */   ConfigurationImpl configuration;
/*     */   private ClassDoc currentClassDoc;
/*     */   private final String constantsTableSummary;
/*     */   private final String[] constantsTableHeader;
/*     */   
/*     */   public ConstantsSummaryWriterImpl(ConfigurationImpl paramConfigurationImpl) throws IOException {
/*  72 */     super(paramConfigurationImpl, DocPaths.CONSTANT_VALUES);
/*  73 */     this.configuration = paramConfigurationImpl;
/*  74 */     this.constantsTableSummary = paramConfigurationImpl.getText("doclet.Constants_Table_Summary", paramConfigurationImpl
/*  75 */         .getText("doclet.Constants_Summary"));
/*  76 */     this
/*     */ 
/*     */       
/*  79 */       .constantsTableHeader = new String[] { getModifierTypeHeader(), paramConfigurationImpl.getText("doclet.ConstantField"), paramConfigurationImpl.getText("doclet.Value") };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHeader() {
/*  87 */     String str = this.configuration.getText("doclet.Constants_Summary");
/*  88 */     HtmlTree htmlTree = getBody(true, getWindowTitle(str));
/*  89 */     addTop((Content)htmlTree);
/*  90 */     addNavLinks(true, (Content)htmlTree);
/*  91 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentsHeader() {
/*  98 */     return (Content)new HtmlTree(HtmlTag.UL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLinkToPackageContent(PackageDoc paramPackageDoc, String paramString, Set<String> paramSet, Content paramContent) {
/*     */     Content content;
/* 106 */     String str = paramPackageDoc.name();
/*     */ 
/*     */     
/* 109 */     if (str.length() == 0) {
/* 110 */       content = getHyperLink(getDocLink(SectionName.UNNAMED_PACKAGE_ANCHOR), this.defaultPackageLabel, "", "");
/*     */     }
/*     */     else {
/*     */       
/* 114 */       Content content1 = getPackageLabel(paramString);
/* 115 */       content1.addContent(".*");
/* 116 */       content = getHyperLink(DocLink.fragment(paramString), content1, "", "");
/*     */       
/* 118 */       paramSet.add(paramString);
/*     */     } 
/* 120 */     paramContent.addContent((Content)HtmlTree.LI(content));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentsList(Content paramContent) {
/* 127 */     Content content1 = getResource("doclet.Constants_Summary");
/*     */     
/* 129 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, content1);
/*     */     
/* 131 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 132 */     Content content2 = getResource("doclet.Contents");
/*     */     
/* 134 */     htmlTree2.addContent((Content)HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, content2));
/*     */     
/* 136 */     htmlTree2.addContent(paramContent);
/* 137 */     return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getConstantSummaries() {
/* 144 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 145 */     htmlTree.addStyle(HtmlStyle.constantValuesContainer);
/* 146 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPackageName(PackageDoc paramPackageDoc, String paramString, Content paramContent) {
/*     */     Content content;
/* 155 */     if (paramString.length() == 0) {
/* 156 */       paramContent.addContent(getMarkerAnchor(SectionName.UNNAMED_PACKAGE_ANCHOR));
/*     */       
/* 158 */       content = this.defaultPackageLabel;
/*     */     } else {
/* 160 */       paramContent.addContent(getMarkerAnchor(paramString));
/*     */       
/* 162 */       content = getPackageLabel(paramString);
/*     */     } 
/* 164 */     StringContent stringContent = new StringContent(".*");
/* 165 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, content);
/*     */     
/* 167 */     htmlTree.addContent((Content)stringContent);
/* 168 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getClassConstantHeader() {
/* 175 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 176 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 177 */     return (Content)htmlTree;
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
/*     */   public Content getConstantMembersHeader(ClassDoc paramClassDoc) {
/* 191 */     Content content = (Content)((paramClassDoc.isPublic() || paramClassDoc.isProtected()) ? getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CONSTANT_SUMMARY, paramClassDoc)) : new StringContent(paramClassDoc.qualifiedName()));
/* 192 */     String str = paramClassDoc.containingPackage().name();
/* 193 */     if (str.length() > 0) {
/* 194 */       ContentBuilder contentBuilder = new ContentBuilder();
/* 195 */       contentBuilder.addContent(str);
/* 196 */       contentBuilder.addContent(".");
/* 197 */       contentBuilder.addContent(content);
/* 198 */       return getClassName((Content)contentBuilder);
/*     */     } 
/* 200 */     return getClassName(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getClassName(Content paramContent) {
/* 211 */     HtmlTree htmlTree = HtmlTree.TABLE(HtmlStyle.constantsSummary, 0, 3, 0, this.constantsTableSummary, 
/* 212 */         getTableCaption(paramContent));
/* 213 */     htmlTree.addContent(getSummaryTableHeader(this.constantsTableHeader, "col"));
/* 214 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConstantMembers(ClassDoc paramClassDoc, List<FieldDoc> paramList, Content paramContent) {
/* 222 */     this.currentClassDoc = paramClassDoc;
/* 223 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.TBODY);
/* 224 */     for (byte b = 0; b < paramList.size(); b++) {
/* 225 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.TR);
/* 226 */       if (b % 2 == 0) {
/* 227 */         htmlTree.addStyle(HtmlStyle.altColor);
/*     */       } else {
/* 229 */         htmlTree.addStyle(HtmlStyle.rowColor);
/* 230 */       }  addConstantMember(paramList.get(b), htmlTree);
/* 231 */       htmlTree1.addContent((Content)htmlTree);
/*     */     } 
/* 233 */     Content content = getConstantMembersHeader(paramClassDoc);
/* 234 */     content.addContent((Content)htmlTree1);
/* 235 */     HtmlTree htmlTree2 = HtmlTree.LI(HtmlStyle.blockList, content);
/* 236 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addConstantMember(FieldDoc paramFieldDoc, HtmlTree paramHtmlTree) {
/* 246 */     paramHtmlTree.addContent(getTypeColumn(paramFieldDoc));
/* 247 */     paramHtmlTree.addContent(getNameColumn(paramFieldDoc));
/* 248 */     paramHtmlTree.addContent(getValue(paramFieldDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content getTypeColumn(FieldDoc paramFieldDoc) {
/* 258 */     Content content1 = getMarkerAnchor(this.currentClassDoc.qualifiedName() + "." + paramFieldDoc
/* 259 */         .name());
/* 260 */     HtmlTree htmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, content1);
/* 261 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.CODE);
/* 262 */     StringTokenizer stringTokenizer = new StringTokenizer(paramFieldDoc.modifiers());
/* 263 */     while (stringTokenizer.hasMoreTokens()) {
/* 264 */       StringContent stringContent = new StringContent(stringTokenizer.nextToken());
/* 265 */       htmlTree2.addContent((Content)stringContent);
/* 266 */       htmlTree2.addContent(getSpace());
/*     */     } 
/* 268 */     Content content2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CONSTANT_SUMMARY, paramFieldDoc
/* 269 */           .type()));
/* 270 */     htmlTree2.addContent(content2);
/* 271 */     htmlTree1.addContent((Content)htmlTree2);
/* 272 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content getNameColumn(FieldDoc paramFieldDoc) {
/* 282 */     Content content = getDocLink(LinkInfoImpl.Kind.CONSTANT_SUMMARY, (MemberDoc)paramFieldDoc, paramFieldDoc
/* 283 */         .name(), false);
/* 284 */     HtmlTree htmlTree = HtmlTree.CODE(content);
/* 285 */     return (Content)HtmlTree.TD((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content getValue(FieldDoc paramFieldDoc) {
/* 295 */     StringContent stringContent = new StringContent(paramFieldDoc.constantValueExpression());
/* 296 */     HtmlTree htmlTree = HtmlTree.CODE((Content)stringContent);
/* 297 */     return (Content)HtmlTree.TD(HtmlStyle.colLast, (Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFooter(Content paramContent) {
/* 304 */     addNavLinks(false, paramContent);
/* 305 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 312 */     printHtmlDocument((String[])null, true, paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ConstantsSummaryWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */