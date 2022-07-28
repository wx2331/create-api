/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TagImpl
/*     */   implements Tag
/*     */ {
/*     */   protected final String text;
/*     */   protected final String name;
/*     */   protected final DocImpl holder;
/*     */   private Tag[] firstSentence;
/*     */   private Tag[] inlineTags;
/*     */   
/*     */   TagImpl(DocImpl paramDocImpl, String paramString1, String paramString2) {
/*  71 */     this.holder = paramDocImpl;
/*  72 */     this.name = paramString1;
/*  73 */     this.text = paramString2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  80 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Doc holder() {
/*  87 */     return this.holder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String kind() {
/*  94 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String text() {
/* 101 */     return this.text;
/*     */   }
/*     */   
/*     */   DocEnv docenv() {
/* 105 */     return this.holder.env;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String[] divideAtWhite() {
/* 112 */     String[] arrayOfString = new String[2];
/* 113 */     int i = this.text.length();
/*     */     
/* 115 */     arrayOfString[0] = this.text;
/* 116 */     arrayOfString[1] = "";
/* 117 */     for (byte b = 0; b < i; b++) {
/* 118 */       char c = this.text.charAt(b);
/* 119 */       if (Character.isWhitespace(c)) {
/* 120 */         arrayOfString[0] = this.text.substring(0, b);
/* 121 */         for (; b < i; b++) {
/* 122 */           c = this.text.charAt(b);
/* 123 */           if (!Character.isWhitespace(c)) {
/* 124 */             arrayOfString[1] = this.text.substring(b, i);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/* 131 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return this.name + ":" + this.text;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] inlineTags() {
/* 158 */     if (this.inlineTags == null) {
/* 159 */       this.inlineTags = Comment.getInlineTags(this.holder, this.text);
/*     */     }
/* 161 */     return this.inlineTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] firstSentenceTags() {
/* 168 */     if (this.firstSentence == null) {
/*     */       
/* 170 */       inlineTags();
/*     */       try {
/* 172 */         docenv().setSilent(true);
/* 173 */         this.firstSentence = Comment.firstSentenceTags(this.holder, this.text);
/*     */       } finally {
/* 175 */         docenv().setSilent(false);
/*     */       } 
/*     */     } 
/* 178 */     return this.firstSentence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/* 186 */     return this.holder.position();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\TagImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */