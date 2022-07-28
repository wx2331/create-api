/*    */ package com.sun.tools.internal.xjc.reader;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
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
/*    */ public final class ModelChecker
/*    */ {
/* 48 */   private final Model model = Ring.<Model>get(Model.class);
/* 49 */   private final ErrorReceiver errorReceiver = Ring.<ErrorReceiver>get(ErrorReceiver.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void check() {
/* 55 */     for (CClassInfo ci : this.model.beans().values())
/* 56 */       check(ci); 
/*    */   }
/*    */   
/*    */   private void check(CClassInfo ci) {
/* 60 */     List<CPropertyInfo> props = ci.getProperties();
/* 61 */     Map<QName, CPropertyInfo> collisionTable = new HashMap<>();
/*    */     
/*    */     int i;
/* 64 */     label37: for (i = 0; i < props.size(); i++) {
/* 65 */       CPropertyInfo p1 = props.get(i);
/*    */       
/* 67 */       if (p1.getName(true).equals("Class")) {
/* 68 */         this.errorReceiver.error(p1.locator, Messages.PROPERTY_CLASS_IS_RESERVED.format(new Object[0]));
/*    */       }
/*    */       else {
/*    */         
/* 72 */         QName n = p1.collectElementNames(collisionTable);
/* 73 */         if (n != null) {
/* 74 */           CPropertyInfo p2 = collisionTable.get(n);
/*    */           
/* 76 */           if (p2.getName(true).equals(n.toString()) || p2.getName(false).equals(n.toString())) {
/* 77 */             this.errorReceiver.error(p1.locator, Messages.DUPLICATE_ELEMENT.format(new Object[] { n }));
/* 78 */             this.errorReceiver.error(p2.locator, Messages.ERR_RELEVANT_LOCATION.format(new Object[0]));
/*    */           } 
/*    */         } 
/*    */         
/* 82 */         for (int j = i + 1; j < props.size(); j++) {
/* 83 */           if (checkPropertyCollision(p1, props.get(j)))
/*    */             continue label37; 
/*    */         } 
/* 86 */         for (CClassInfo c = ci.getBaseClass(); c != null; c = c.getBaseClass()) {
/* 87 */           for (CPropertyInfo p2 : c.getProperties()) {
/* 88 */             if (checkPropertyCollision(p1, p2))
/*    */               continue label37; 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   } private boolean checkPropertyCollision(CPropertyInfo p1, CPropertyInfo p2) {
/* 95 */     if (!p1.getName(true).equals(p2.getName(true)))
/* 96 */       return false; 
/* 97 */     this.errorReceiver.error(p1.locator, Messages.DUPLICATE_PROPERTY.format(new Object[] { p1.getName(true) }));
/* 98 */     this.errorReceiver.error(p2.locator, Messages.ERR_RELEVANT_LOCATION.format(new Object[0]));
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\ModelChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */