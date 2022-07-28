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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SplitIndexWriter
/*     */   extends AbstractIndexWriter
/*     */ {
/*     */   protected int prev;
/*     */   protected int next;
/*     */   
/*     */   public SplitIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder, int paramInt1, int paramInt2) throws IOException {
/*  71 */     super(paramConfigurationImpl, paramDocPath, paramIndexBuilder);
/*  72 */     this.prev = paramInt1;
/*  73 */     this.next = paramInt2;
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder) {
/*  86 */     DocPath docPath1 = DocPath.empty;
/*  87 */     DocPath docPath2 = DocPaths.INDEX_FILES;
/*     */     try {
/*  89 */       for (byte b = 0; b < (paramIndexBuilder.elements()).length; b++) {
/*  90 */         int i = b + 1;
/*  91 */         boolean bool1 = (i == 1) ? true : b;
/*  92 */         boolean bool2 = (i == (paramIndexBuilder.elements()).length) ? true : (i + 1);
/*  93 */         docPath1 = DocPaths.indexN(i);
/*     */         
/*  95 */         SplitIndexWriter splitIndexWriter = new SplitIndexWriter(paramConfigurationImpl, docPath2.resolve(docPath1), paramIndexBuilder, bool1, bool2);
/*     */         
/*  97 */         splitIndexWriter.generateIndexFile(
/*  98 */             (Character)paramIndexBuilder.elements()[b]);
/*  99 */         splitIndexWriter.close();
/*     */       } 
/* 101 */     } catch (IOException iOException) {
/* 102 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 104 */             .toString(), docPath1.getPath() });
/* 105 */       throw new DocletAbortException(iOException);
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
/*     */   protected void generateIndexFile(Character paramCharacter) throws IOException {
/* 117 */     String str = this.configuration.getText("doclet.Window_Split_Index", paramCharacter
/* 118 */         .toString());
/* 119 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str));
/* 120 */     addTop((Content)htmlTree1);
/* 121 */     addNavLinks(true, (Content)htmlTree1);
/* 122 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 123 */     htmlTree2.addStyle(HtmlStyle.contentContainer);
/* 124 */     addLinksForIndexes((Content)htmlTree2);
/* 125 */     addContents(paramCharacter, this.indexbuilder.getMemberList(paramCharacter), (Content)htmlTree2);
/* 126 */     addLinksForIndexes((Content)htmlTree2);
/* 127 */     htmlTree1.addContent((Content)htmlTree2);
/* 128 */     addNavLinks(false, (Content)htmlTree1);
/* 129 */     addBottom((Content)htmlTree1);
/* 130 */     printHtmlDocument((String[])null, true, (Content)htmlTree1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addLinksForIndexes(Content paramContent) {
/* 139 */     Object[] arrayOfObject = this.indexbuilder.elements();
/* 140 */     for (byte b = 0; b < arrayOfObject.length; b++) {
/* 141 */       int i = b + 1;
/* 142 */       paramContent.addContent(getHyperLink(DocPaths.indexN(i), (Content)new StringContent(arrayOfObject[b]
/* 143 */               .toString())));
/* 144 */       paramContent.addContent(getSpace());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkPrevious() {
/* 154 */     Content content1 = getResource("doclet.Prev_Letter");
/* 155 */     if (this.prev == -1) {
/* 156 */       return (Content)HtmlTree.LI(content1);
/*     */     }
/*     */     
/* 159 */     Content content2 = getHyperLink(DocPaths.indexN(this.prev), content1);
/*     */     
/* 161 */     return (Content)HtmlTree.LI(content2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/* 171 */     Content content1 = getResource("doclet.Next_Letter");
/* 172 */     if (this.next == -1) {
/* 173 */       return (Content)HtmlTree.LI(content1);
/*     */     }
/*     */     
/* 176 */     Content content2 = getHyperLink(DocPaths.indexN(this.next), content1);
/*     */     
/* 178 */     return (Content)HtmlTree.LI(content2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SplitIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */