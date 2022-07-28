/*     */ package com.sun.source.doctree;
/*     */ 
/*     */ import jdk.Exported;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public interface DocTree
/*     */ {
/*     */   Kind getKind();
/*     */   
/*     */   <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD);
/*     */   
/*     */   @Exported
/*     */   public enum Kind
/*     */   {
/*  41 */     ATTRIBUTE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     AUTHOR("author"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     CODE("code"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     COMMENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     DEPRECATED("deprecated"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     DOC_COMMENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     DOC_ROOT("docRoot"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     END_ELEMENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     ENTITY,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     ERRONEOUS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     EXCEPTION("exception"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     IDENTIFIER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     INHERIT_DOC("inheritDoc"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     LINK("link"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     LINK_PLAIN("linkplain"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     LITERAL("literal"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     PARAM("param"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     REFERENCE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     RETURN("return"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     SEE("see"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     SERIAL("serial"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     SERIAL_DATA("serialData"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     SERIAL_FIELD("serialField"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     SINCE("since"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     START_ELEMENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     TEXT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     THROWS("throws"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     UNKNOWN_BLOCK_TAG,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     UNKNOWN_INLINE_TAG,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     VALUE("value"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     VERSION("version"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     OTHER;
/*     */     
/*     */     public final String tagName;
/*     */     
/*     */     Kind() {
/* 233 */       this.tagName = null;
/*     */     }
/*     */     
/*     */     Kind(String param1String1) {
/* 237 */       this.tagName = param1String1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\DocTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */