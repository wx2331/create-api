/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AllClassesFrameWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   protected IndexBuilder indexbuilder;
/*  61 */   final HtmlTree BR = new HtmlTree(HtmlTag.BR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AllClassesFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder) throws IOException {
/*  75 */     super(paramConfigurationImpl, paramDocPath);
/*  76 */     this.indexbuilder = paramIndexBuilder;
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder) {
/*  90 */     DocPath docPath = DocPaths.ALLCLASSES_FRAME;
/*     */     try {
/*  92 */       AllClassesFrameWriter allClassesFrameWriter = new AllClassesFrameWriter(paramConfigurationImpl, docPath, paramIndexBuilder);
/*     */       
/*  94 */       allClassesFrameWriter.buildAllClassesFile(true);
/*  95 */       allClassesFrameWriter.close();
/*  96 */       docPath = DocPaths.ALLCLASSES_NOFRAME;
/*  97 */       allClassesFrameWriter = new AllClassesFrameWriter(paramConfigurationImpl, docPath, paramIndexBuilder);
/*     */       
/*  99 */       allClassesFrameWriter.buildAllClassesFile(false);
/* 100 */       allClassesFrameWriter.close();
/* 101 */     } catch (IOException iOException) {
/* 102 */       paramConfigurationImpl.standardmessage
/* 103 */         .error("doclet.exception_encountered", new Object[] {
/* 104 */             iOException.toString(), docPath });
/* 105 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildAllClassesFile(boolean paramBoolean) throws IOException {
/* 114 */     String str = this.configuration.getText("doclet.All_Classes");
/* 115 */     HtmlTree htmlTree1 = getBody(false, getWindowTitle(str));
/* 116 */     HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, this.allclassesLabel);
/*     */     
/* 118 */     htmlTree1.addContent((Content)htmlTree2);
/* 119 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/*     */     
/* 121 */     addAllClasses((Content)htmlTree3, paramBoolean);
/* 122 */     HtmlTree htmlTree4 = HtmlTree.DIV(HtmlStyle.indexContainer, (Content)htmlTree3);
/* 123 */     htmlTree1.addContent((Content)htmlTree4);
/* 124 */     printHtmlDocument((String[])null, false, (Content)htmlTree1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClasses(Content paramContent, boolean paramBoolean) {
/* 135 */     for (byte b = 0; b < (this.indexbuilder.elements()).length; b++) {
/* 136 */       Character character = (Character)this.indexbuilder.elements()[b];
/* 137 */       addContents(this.indexbuilder.getMemberList(character), paramBoolean, paramContent);
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
/*     */   
/*     */   protected void addContents(List<Doc> paramList, boolean paramBoolean, Content paramContent) {
/* 154 */     for (byte b = 0; b < paramList.size(); b++) {
/* 155 */       ClassDoc classDoc = (ClassDoc)paramList.get(b);
/* 156 */       if (Util.isCoreClass(classDoc)) {
/*     */ 
/*     */         
/* 159 */         Content content2, content1 = italicsClassName(classDoc, false);
/*     */         
/* 161 */         if (paramBoolean) {
/* 162 */           content2 = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ALL_CLASSES_FRAME, classDoc))
/* 163 */               .label(content1).target("classFrame"));
/*     */         } else {
/* 165 */           content2 = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, classDoc)).label(content1));
/*     */         } 
/* 167 */         HtmlTree htmlTree = HtmlTree.LI(content2);
/* 168 */         paramContent.addContent((Content)htmlTree);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AllClassesFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */