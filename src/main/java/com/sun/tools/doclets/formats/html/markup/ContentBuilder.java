/*    */ package com.sun.tools.doclets.formats.html.markup;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContentBuilder
/*    */   extends Content
/*    */ {
/* 39 */   protected List<Content> contents = Collections.emptyList();
/*    */ 
/*    */   
/*    */   public void addContent(Content paramContent) {
/* 43 */     nullCheck(paramContent);
/* 44 */     ensureMutableContents();
/* 45 */     if (paramContent instanceof ContentBuilder) {
/* 46 */       this.contents.addAll(((ContentBuilder)paramContent).contents);
/*    */     } else {
/* 48 */       this.contents.add(paramContent);
/*    */     } 
/*    */   }
/*    */   public void addContent(String paramString) {
/*    */     StringContent stringContent;
/* 53 */     if (paramString.isEmpty())
/*    */       return; 
/* 55 */     ensureMutableContents();
/* 56 */     Content content = this.contents.isEmpty() ? null : this.contents.get(this.contents.size() - 1);
/*    */     
/* 58 */     if (content != null && content instanceof StringContent) {
/* 59 */       stringContent = (StringContent)content;
/*    */     } else {
/* 61 */       this.contents.add(stringContent = new StringContent());
/*    */     } 
/* 63 */     stringContent.addContent(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException {
/* 68 */     for (Content content : this.contents) {
/* 69 */       paramBoolean = content.write(paramWriter, paramBoolean);
/*    */     }
/* 71 */     return paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 76 */     for (Content content : this.contents) {
/* 77 */       if (!content.isEmpty())
/* 78 */         return false; 
/*    */     } 
/* 80 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int charCount() {
/* 85 */     int i = 0;
/* 86 */     for (Content content : this.contents)
/* 87 */       i += content.charCount(); 
/* 88 */     return i;
/*    */   }
/*    */   
/*    */   private void ensureMutableContents() {
/* 92 */     if (this.contents.isEmpty())
/* 93 */       this.contents = new ArrayList<>(); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\ContentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */