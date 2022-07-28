/*     */ package com.sun.tools.doclets.internal.toolkit.util;
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
/*     */ public class TextTag
/*     */   implements Tag
/*     */ {
/*     */   protected final String text;
/*  43 */   protected final String name = "Text";
/*     */ 
/*     */   
/*     */   protected final Doc holder;
/*     */ 
/*     */ 
/*     */   
/*     */   public TextTag(Doc paramDoc, String paramString) {
/*  51 */     this.holder = paramDoc;
/*  52 */     this.text = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  59 */     return "Text";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Doc holder() {
/*  66 */     return this.holder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String kind() {
/*  73 */     return "Text";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String text() {
/*  80 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  87 */     return "Text:" + this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] inlineTags() {
/*  94 */     return new Tag[] { this };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] firstSentenceTags() {
/* 101 */     return new Tag[] { this };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/* 108 */     return this.holder.position();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\TextTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */