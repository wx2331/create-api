/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
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
/*     */ public class SingleIndexWriter
/*     */   extends AbstractIndexWriter
/*     */ {
/*     */   public SingleIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder) throws IOException {
/*  60 */     super(paramConfigurationImpl, paramDocPath, paramIndexBuilder);
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder) {
/*  72 */     DocPath docPath = DocPaths.INDEX_ALL;
/*     */     try {
/*  74 */       SingleIndexWriter singleIndexWriter = new SingleIndexWriter(paramConfigurationImpl, docPath, paramIndexBuilder);
/*     */       
/*  76 */       singleIndexWriter.generateIndexFile();
/*  77 */       singleIndexWriter.close();
/*  78 */     } catch (IOException iOException) {
/*  79 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  81 */             .toString(), docPath });
/*  82 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateIndexFile() throws IOException {
/*  91 */     String str = this.configuration.getText("doclet.Window_Single_Index");
/*  92 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str));
/*  93 */     addTop((Content)htmlTree1);
/*  94 */     addNavLinks(true, (Content)htmlTree1);
/*  95 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  96 */     htmlTree2.addStyle(HtmlStyle.contentContainer);
/*  97 */     addLinksForIndexes((Content)htmlTree2);
/*  98 */     for (byte b = 0; b < (this.indexbuilder.elements()).length; b++) {
/*  99 */       Character character = (Character)this.indexbuilder.elements()[b];
/* 100 */       addContents(character, this.indexbuilder.getMemberList(character), (Content)htmlTree2);
/*     */     } 
/* 102 */     addLinksForIndexes((Content)htmlTree2);
/* 103 */     htmlTree1.addContent((Content)htmlTree2);
/* 104 */     addNavLinks(false, (Content)htmlTree1);
/* 105 */     addBottom((Content)htmlTree1);
/* 106 */     printHtmlDocument((String[])null, true, (Content)htmlTree1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addLinksForIndexes(Content paramContent) {
/* 115 */     for (byte b = 0; b < (this.indexbuilder.elements()).length; b++) {
/* 116 */       String str = this.indexbuilder.elements()[b].toString();
/* 117 */       paramContent.addContent(
/* 118 */           getHyperLink(getNameForIndex(str), (Content)new StringContent(str)));
/*     */       
/* 120 */       paramContent.addContent(getSpace());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SingleIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */