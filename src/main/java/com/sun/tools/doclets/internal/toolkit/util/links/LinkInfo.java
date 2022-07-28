/*     */ package com.sun.tools.doclets.internal.toolkit.util.links;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LinkInfo
/*     */ {
/*     */   public ClassDoc classDoc;
/*     */   public ExecutableMemberDoc executableMemberDoc;
/*     */   public Type type;
/*     */   public boolean isVarArg = false;
/*     */   public boolean isTypeBound = false;
/*     */   public boolean isJava5DeclarationLocation = true;
/*     */   public Content label;
/*     */   public boolean isStrong = false;
/*     */   public boolean includeTypeInClassLinkLabel = true;
/*     */   public boolean includeTypeAsSepLink = false;
/*     */   public boolean excludeTypeBounds = false;
/*     */   public boolean excludeTypeParameterLinks = false;
/*     */   public boolean excludeTypeBoundsLinks = false;
/*     */   public boolean linkToSelf = true;
/*     */   
/*     */   protected abstract Content newContent();
/*     */   
/*     */   public abstract boolean isLinkable();
/*     */   
/*     */   public Content getClassLinkLabel(Configuration paramConfiguration) {
/* 142 */     if (this.label != null && !this.label.isEmpty())
/* 143 */       return this.label; 
/* 144 */     if (isLinkable()) {
/* 145 */       Content content1 = newContent();
/* 146 */       content1.addContent(this.classDoc.name());
/* 147 */       return content1;
/*     */     } 
/* 149 */     Content content = newContent();
/* 150 */     content.addContent(paramConfiguration.getClassName(this.classDoc));
/* 151 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\links\LinkInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */