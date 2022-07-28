/*    */ package com.sun.tools.internal.xjc.model;
/*    */
/*    */ import com.sun.codemodel.internal.JPackage;
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
/*    */ public interface CClassInfoParent
/*    */ {
/*    */   String fullName();
/*    */
/*    */   <T> T accept(Visitor<T> paramVisitor);
/*    */
/*    */   JPackage getOwnerPackage();
/*    */
/*    */   public static final class Package
/*    */     implements CClassInfoParent
/*    */   {
/*    */     public final JPackage pkg;
/*    */
/*    */     public Package(JPackage pkg) {
/* 68 */       this.pkg = pkg;
/*    */     }
/*    */
/*    */     public String fullName() {
/* 72 */       return this.pkg.name();
/*    */     }
/*    */
/*    */     public <T> T accept(Visitor<T> visitor) {
/* 76 */       return visitor.onPackage(this.pkg);
/*    */     }
/*    */
/*    */     public JPackage getOwnerPackage() {
/* 80 */       return this.pkg;
/*    */     }
/*    */   }
/*    */
/*    */   public static interface Visitor<T> {
/*    */     T onBean(CClassInfo param1CClassInfo);
/*    */
/*    */     T onPackage(JPackage param1JPackage);
/*    */
/*    */     T onElement(CElementInfo param1CElementInfo);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CClassInfoParent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
