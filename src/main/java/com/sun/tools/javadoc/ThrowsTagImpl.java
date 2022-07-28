/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ThrowsTagImpl
/*     */   extends TagImpl
/*     */   implements ThrowsTag
/*     */ {
/*     */   private final String exceptionName;
/*     */   private final String exceptionComment;
/*     */   private Tag[] inlineTags;
/*     */   
/*     */   ThrowsTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2) {
/*  57 */     super(paramDocImpl, paramString1, paramString2);
/*  58 */     String[] arrayOfString = divideAtWhite();
/*  59 */     this.exceptionName = arrayOfString[0];
/*  60 */     this.exceptionComment = arrayOfString[1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String exceptionName() {
/*  67 */     return this.exceptionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String exceptionComment() {
/*  74 */     return this.exceptionComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc exception() {
/*     */     ClassDocImpl classDocImpl;
/*  82 */     if (!(this.holder instanceof com.sun.javadoc.ExecutableMemberDoc)) {
/*  83 */       classDocImpl = null;
/*     */     } else {
/*  85 */       ExecutableMemberDocImpl executableMemberDocImpl = (ExecutableMemberDocImpl)this.holder;
/*  86 */       ClassDocImpl classDocImpl1 = (ClassDocImpl)executableMemberDocImpl.containingClass();
/*  87 */       classDocImpl = (ClassDocImpl)classDocImpl1.findClass(this.exceptionName);
/*     */     } 
/*  89 */     return classDocImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type exceptionType() {
/*  98 */     return (Type)exception();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String kind() {
/* 108 */     return "@throws";
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
/*     */   public Tag[] inlineTags() {
/* 121 */     if (this.inlineTags == null) {
/* 122 */       this.inlineTags = Comment.getInlineTags(this.holder, exceptionComment());
/*     */     }
/* 124 */     return this.inlineTags;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ThrowsTagImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */