/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.Taglet;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyTaglet
/*     */   implements Taglet
/*     */ {
/*     */   private Taglet legacyTaglet;
/*     */   
/*     */   public LegacyTaglet(Taglet paramTaglet) {
/*  55 */     this.legacyTaglet = paramTaglet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inField() {
/*  62 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inField());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inConstructor() {
/*  69 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inConstructor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inMethod() {
/*  76 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inMethod());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inOverview() {
/*  83 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inOverview());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inPackage() {
/*  90 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inPackage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inType() {
/*  97 */     return (this.legacyTaglet.isInlineTag() || this.legacyTaglet.inType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInlineTag() {
/* 107 */     return this.legacyTaglet.isInlineTag();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 114 */     return this.legacyTaglet.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) throws IllegalArgumentException {
/* 122 */     Content content = paramTagletWriter.getOutputInstance();
/* 123 */     content.addContent((Content)new RawHtml(this.legacyTaglet.toString(paramTag)));
/* 124 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) throws IllegalArgumentException {
/* 132 */     Content content = paramTagletWriter.getOutputInstance();
/* 133 */     Tag[] arrayOfTag = paramDoc.tags(getName());
/* 134 */     if (arrayOfTag.length > 0) {
/* 135 */       String str = this.legacyTaglet.toString(arrayOfTag);
/* 136 */       if (str != null) {
/* 137 */         content.addContent((Content)new RawHtml(str));
/*     */       }
/*     */     } 
/* 140 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\LegacyTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */