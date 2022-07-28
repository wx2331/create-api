/*     */ package com.sun.tools.doclets.formats.html;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
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
/*     */ public class SerializedFormWriterImpl
/*     */   extends SubWriterHolderWriter
/*     */   implements SerializedFormWriter
/*     */ {
/*     */   public SerializedFormWriterImpl(ConfigurationImpl paramConfigurationImpl) throws IOException {
/*  55 */     super(paramConfigurationImpl, DocPaths.SERIALIZED_FORM);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getHeader(String paramString) {
/*  65 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(paramString));
/*  66 */     addTop((Content)htmlTree1);
/*  67 */     addNavLinks(true, (Content)htmlTree1);
/*  68 */     StringContent stringContent = new StringContent(paramString);
/*  69 */     HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, (Content)stringContent);
/*     */
/*  71 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree2);
/*  72 */     htmlTree1.addContent((Content)htmlTree3);
/*  73 */     return (Content)htmlTree1;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getSerializedSummariesHeader() {
/*  82 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/*  83 */     htmlTree.addStyle(HtmlStyle.blockList);
/*  84 */     return (Content)htmlTree;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getPackageSerializedHeader() {
/*  93 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/*  94 */     htmlTree.addStyle(HtmlStyle.blockList);
/*  95 */     return (Content)htmlTree;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getPackageHeader(String paramString) {
/* 105 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, this.packageLabel);
/*     */
/* 107 */     htmlTree.addContent(getSpace());
/* 108 */     htmlTree.addContent(paramString);
/* 109 */     return (Content)htmlTree;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getClassSerializedHeader() {
/* 118 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 119 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 120 */     return (Content)htmlTree;
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
/*     */   public Content getClassHeader(ClassDoc paramClassDoc) {
/* 133 */     Object object = (paramClassDoc.isPublic() || paramClassDoc.isProtected()) ? getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, paramClassDoc)).label(this.configuration.getClassName(paramClassDoc))) : new StringContent(paramClassDoc.qualifiedName());
/* 134 */     HtmlTree htmlTree = HtmlTree.LI(HtmlStyle.blockList, getMarkerAnchor(paramClassDoc
/* 135 */           .qualifiedName()));
/*     */
/*     */
/* 138 */     Content content1 = (paramClassDoc.superclassType() != null) ? getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SERIALIZED_FORM, paramClassDoc
/*     */
/* 140 */           .superclassType())) : null;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 147 */     Content content2 = (content1 == null) ? this.configuration.getResource("doclet.Class_0_implements_serializable", object) : this.configuration.getResource("doclet.Class_0_extends_implements_serializable", object, content1);
/*     */
/*     */
/* 150 */     htmlTree.addContent((Content)HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, content2));
/*     */
/* 152 */     return (Content)htmlTree;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getSerialUIDInfoHeader() {
/* 161 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DL);
/* 162 */     htmlTree.addStyle(HtmlStyle.nameValue);
/* 163 */     return (Content)htmlTree;
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
/*     */   public void addSerialUIDInfo(String paramString1, String paramString2, Content paramContent) {
/* 176 */     StringContent stringContent1 = new StringContent(paramString1);
/* 177 */     paramContent.addContent((Content)HtmlTree.DT((Content)stringContent1));
/* 178 */     StringContent stringContent2 = new StringContent(paramString2);
/* 179 */     paramContent.addContent((Content)HtmlTree.DD((Content)stringContent2));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getClassContentHeader() {
/* 188 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 189 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 190 */     return (Content)htmlTree;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Content getSerializedContent(Content paramContent) {
/* 200 */     return (Content)HtmlTree.DIV(HtmlStyle.serializedFormContainer, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void addFooter(Content paramContent) {
/* 211 */     addNavLinks(false, paramContent);
/* 212 */     addBottom(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 219 */     printHtmlDocument((String[])null, true, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SerialFieldWriter getSerialFieldWriter(ClassDoc paramClassDoc) {
/* 228 */     return new HtmlSerialFieldWriter(this, paramClassDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SerialMethodWriter getSerialMethodWriter(ClassDoc paramClassDoc) {
/* 237 */     return new HtmlSerialMethodWriter(this, paramClassDoc);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SerializedFormWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
