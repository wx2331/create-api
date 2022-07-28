/*    */ package com.sun.xml.internal.xsom.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSType;
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
/*    */ public abstract class TypeSet
/*    */ {
/*    */   public abstract boolean contains(XSType paramXSType);
/*    */   
/*    */   public static TypeSet intersection(final TypeSet a, final TypeSet b) {
/* 57 */     return new TypeSet() {
/*    */         public boolean contains(XSType type) {
/* 59 */           return (a.contains(type) && b.contains(type));
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TypeSet union(final TypeSet a, final TypeSet b) {
/* 73 */     return new TypeSet() {
/*    */         public boolean contains(XSType type) {
/* 75 */           return (a.contains(type) || b.contains(type));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xso\\util\TypeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */