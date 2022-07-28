/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SubWriterHolderWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   public SubWriterHolderWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  60 */     super(paramConfigurationImpl, paramDocPath);
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
/*     */   public void addSummaryHeader(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, Content paramContent) {
/*  72 */     paramAbstractMemberWriter.addSummaryAnchor(paramClassDoc, paramContent);
/*  73 */     paramAbstractMemberWriter.addSummaryLabel(paramContent);
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
/*     */   public Content getSummaryTableTree(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, List<Content> paramList, boolean paramBoolean) {
/*     */     Content content;
/*  88 */     if (paramBoolean) {
/*  89 */       content = getTableCaption(paramAbstractMemberWriter.methodTypes);
/*  90 */       generateMethodTypesScript(paramAbstractMemberWriter.typeMap, paramAbstractMemberWriter.methodTypes);
/*     */     } else {
/*     */       
/*  93 */       content = getTableCaption(paramAbstractMemberWriter.getCaption());
/*     */     } 
/*  95 */     HtmlTree htmlTree = HtmlTree.TABLE(HtmlStyle.memberSummary, 0, 3, 0, paramAbstractMemberWriter
/*  96 */         .getTableSummary(), content);
/*  97 */     htmlTree.addContent(getSummaryTableHeader(paramAbstractMemberWriter.getSummaryTableHeader((ProgramElementDoc)paramClassDoc), "col"));
/*  98 */     for (byte b = 0; b < paramList.size(); b++) {
/*  99 */       htmlTree.addContent(paramList.get(b));
/*     */     }
/* 101 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTableCaption(Set<MethodTypes> paramSet) {
/* 111 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.CAPTION);
/* 112 */     for (MethodTypes methodTypes : paramSet) {
/*     */       HtmlTree htmlTree1;
/*     */       
/* 115 */       if (methodTypes.isDefaultTab()) {
/* 116 */         HtmlTree htmlTree3 = HtmlTree.SPAN(this.configuration.getResource(methodTypes.resourceKey()));
/* 117 */         htmlTree1 = HtmlTree.SPAN(methodTypes.tabId(), HtmlStyle.activeTableTab, (Content)htmlTree3);
/*     */       } else {
/*     */         
/* 120 */         HtmlTree htmlTree3 = HtmlTree.SPAN(getMethodTypeLinks(methodTypes));
/* 121 */         htmlTree1 = HtmlTree.SPAN(methodTypes.tabId(), HtmlStyle.tableTab, (Content)htmlTree3);
/*     */       } 
/*     */       
/* 124 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.tabEnd, getSpace());
/* 125 */       htmlTree1.addContent((Content)htmlTree2);
/* 126 */       htmlTree.addContent((Content)htmlTree1);
/*     */     } 
/* 128 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodTypeLinks(MethodTypes paramMethodTypes) {
/* 138 */     String str = "javascript:show(" + paramMethodTypes.value() + ");";
/* 139 */     return (Content)HtmlTree.A(str, this.configuration.getResource(paramMethodTypes.resourceKey()));
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
/*     */   public void addInheritedSummaryHeader(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, Content paramContent) {
/* 152 */     paramAbstractMemberWriter.addInheritedSummaryAnchor(paramClassDoc, paramContent);
/* 153 */     paramAbstractMemberWriter.addInheritedSummaryLabel(paramClassDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addIndexComment(Doc paramDoc, Content paramContent) {
/* 163 */     addIndexComment(paramDoc, paramDoc.firstSentenceTags(), paramContent);
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
/*     */   protected void addIndexComment(Doc paramDoc, Tag[] paramArrayOfTag, Content paramContent) {
/* 175 */     Tag[] arrayOfTag = paramDoc.tags("deprecated");
/*     */     
/* 177 */     if (Util.isDeprecated(paramDoc)) {
/* 178 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 179 */       HtmlTree htmlTree1 = HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2);
/* 180 */       htmlTree1.addContent(getSpace());
/* 181 */       if (arrayOfTag.length > 0) {
/* 182 */         addInlineDeprecatedComment(paramDoc, arrayOfTag[0], (Content)htmlTree1);
/*     */       }
/* 184 */       paramContent.addContent((Content)htmlTree1);
/*     */       return;
/*     */     } 
/* 187 */     ClassDoc classDoc = ((ProgramElementDoc)paramDoc).containingClass();
/* 188 */     if (classDoc != null && Util.isDeprecated((Doc)classDoc)) {
/* 189 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 190 */       HtmlTree htmlTree1 = HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2);
/* 191 */       htmlTree1.addContent(getSpace());
/* 192 */       paramContent.addContent((Content)htmlTree1);
/*     */     } 
/*     */     
/* 195 */     addSummaryComment(paramDoc, paramArrayOfTag, paramContent);
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
/*     */   public void addSummaryType(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 207 */     paramAbstractMemberWriter.addSummaryType(paramProgramElementDoc, paramContent);
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
/*     */   public void addSummaryLinkComment(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 219 */     addSummaryLinkComment(paramAbstractMemberWriter, paramProgramElementDoc, paramProgramElementDoc.firstSentenceTags(), paramContent);
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
/*     */   public void addSummaryLinkComment(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, Content paramContent) {
/* 232 */     addIndexComment((Doc)paramProgramElementDoc, paramArrayOfTag, paramContent);
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
/*     */   public void addInheritedMemberSummary(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean, Content paramContent) {
/* 246 */     if (!paramBoolean) {
/* 247 */       paramContent.addContent(", ");
/*     */     }
/* 249 */     paramAbstractMemberWriter.addInheritedSummaryLink(paramClassDoc, paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentHeader() {
/* 258 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 259 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 260 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTreeHeader() {
/* 269 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 270 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 271 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTree(Content paramContent) {
/* 281 */     return (Content)HtmlTree.UL(HtmlStyle.blockList, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryTree(Content paramContent) {
/* 292 */     return getMemberTree(HtmlStyle.summary, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberDetailsTree(Content paramContent) {
/* 302 */     return getMemberTree(HtmlStyle.details, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTree(HtmlStyle paramHtmlStyle, Content paramContent) {
/* 312 */     return (Content)HtmlTree.DIV(paramHtmlStyle, getMemberTree(paramContent));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SubWriterHolderWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */