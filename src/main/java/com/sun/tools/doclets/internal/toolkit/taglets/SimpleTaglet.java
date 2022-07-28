/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleTaglet
/*     */   extends BaseTaglet
/*     */   implements InheritableTaglet
/*     */ {
/*     */   public static final String EXCLUDED = "x";
/*     */   public static final String PACKAGE = "p";
/*     */   public static final String TYPE = "t";
/*     */   public static final String CONSTRUCTOR = "c";
/*     */   public static final String FIELD = "f";
/*     */   public static final String METHOD = "m";
/*     */   public static final String OVERVIEW = "o";
/*     */   public static final String ALL = "a";
/*     */   protected String tagName;
/*     */   protected String header;
/*     */   protected String locations;
/*     */   
/*     */   public SimpleTaglet(String paramString1, String paramString2, String paramString3) {
/* 112 */     this.tagName = paramString1;
/* 113 */     this.header = paramString2;
/* 114 */     paramString3 = StringUtils.toLowerCase(paramString3);
/* 115 */     if (paramString3.indexOf("a") != -1 && paramString3.indexOf("x") == -1) {
/* 116 */       this.locations = "ptfmco";
/*     */     } else {
/* 118 */       this.locations = paramString3;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 126 */     return this.tagName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inConstructor() {
/* 137 */     return (this.locations.indexOf("c") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inField() {
/* 148 */     return (this.locations.indexOf("f") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inMethod() {
/* 159 */     return (this.locations.indexOf("m") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inOverview() {
/* 170 */     return (this.locations.indexOf("o") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inPackage() {
/* 181 */     return (this.locations.indexOf("p") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inType() {
/* 192 */     return (this.locations.indexOf("t") != -1 && this.locations.indexOf("x") == -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInlineTag() {
/* 202 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput) {
/* 207 */     Tag[] arrayOfTag = paramInput.element.tags(this.tagName);
/* 208 */     if (arrayOfTag.length > 0) {
/* 209 */       paramOutput.holder = (Doc)paramInput.element;
/* 210 */       paramOutput.holderTag = arrayOfTag[0];
/* 211 */       paramOutput
/* 212 */         .inlineTags = paramInput.isFirstSentence ? arrayOfTag[0].firstSentenceTags() : arrayOfTag[0].inlineTags();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) {
/* 220 */     return (this.header == null || paramTag == null) ? null : paramTagletWriter.simpleTagOutput(paramTag, this.header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) {
/* 227 */     if (this.header == null || (paramDoc.tags(getName())).length == 0) {
/* 228 */       return null;
/*     */     }
/* 230 */     return paramTagletWriter.simpleTagOutput(paramDoc.tags(getName()), this.header);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\SimpleTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */