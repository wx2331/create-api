/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ class Util
/*    */ {
/*    */   private static XSType[] listDirectSubstitutables(XSType _this) {
/* 44 */     ArrayList<XSType> r = new ArrayList();
/*    */ 
/*    */     
/* 47 */     Iterator<XSType> itr = ((SchemaImpl)_this.getOwnerSchema()).parent.iterateTypes();
/* 48 */     while (itr.hasNext()) {
/* 49 */       XSType t = itr.next();
/* 50 */       if (t.getBaseType() == _this)
/* 51 */         r.add(t); 
/*    */     } 
/* 53 */     return r.<XSType>toArray(new XSType[r.size()]);
/*    */   }
/*    */   
/*    */   public static XSType[] listSubstitutables(XSType _this) {
/* 57 */     Set substitables = new HashSet();
/* 58 */     buildSubstitutables(_this, substitables);
/* 59 */     return (XSType[])substitables.toArray((Object[])new XSType[substitables.size()]);
/*    */   }
/*    */   
/*    */   public static void buildSubstitutables(XSType _this, Set substitutables) {
/* 63 */     if (_this.isLocal())
/* 64 */       return;  buildSubstitutables(_this, _this, substitutables);
/*    */   }
/*    */   
/*    */   private static void buildSubstitutables(XSType head, XSType _this, Set<XSType> substitutables) {
/* 68 */     if (!isSubstitutable(head, _this)) {
/*    */       return;
/*    */     }
/* 71 */     if (substitutables.add(_this)) {
/* 72 */       XSType[] child = listDirectSubstitutables(_this);
/* 73 */       for (int i = 0; i < child.length; i++) {
/* 74 */         buildSubstitutables(head, child[i], substitutables);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isSubstitutable(XSType _base, XSType derived) {
/* 86 */     if (_base.isComplexType()) {
/* 87 */       XSComplexType base = _base.asComplexType();
/*    */       
/* 89 */       for (; base != derived; derived = derived.getBaseType()) {
/* 90 */         if (base.isSubstitutionProhibited(derived.getDerivationMethod()))
/* 91 */           return false; 
/*    */       } 
/* 93 */       return true;
/*    */     } 
/*    */     
/* 96 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */