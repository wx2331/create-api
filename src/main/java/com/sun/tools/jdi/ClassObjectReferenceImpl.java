/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ClassObjectReference;
/*    */ import com.sun.jdi.ReferenceType;
/*    */ import com.sun.jdi.VirtualMachine;
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
/*    */ public class ClassObjectReferenceImpl
/*    */   extends ObjectReferenceImpl
/*    */   implements ClassObjectReference
/*    */ {
/*    */   private ReferenceType reflectedType;
/*    */   
/*    */   ClassObjectReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/* 36 */     super(paramVirtualMachine, paramLong);
/*    */   }
/*    */   
/*    */   public ReferenceType reflectedType() {
/* 40 */     if (this.reflectedType == null) {
/*    */       
/*    */       try {
/* 43 */         JDWP.ClassObjectReference.ReflectedType reflectedType = JDWP.ClassObjectReference.ReflectedType.process(this.vm, this);
/* 44 */         this.reflectedType = this.vm.referenceType(reflectedType.typeID, reflectedType.refTypeTag);
/*    */       
/*    */       }
/* 47 */       catch (JDWPException jDWPException) {
/* 48 */         throw jDWPException.toJDIException();
/*    */       } 
/*    */     }
/* 51 */     return this.reflectedType;
/*    */   }
/*    */   
/*    */   byte typeValueKey() {
/* 55 */     return 99;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 59 */     return "instance of " + referenceType().name() + "(reflected class=" + 
/* 60 */       reflectedType().name() + ", id=" + uniqueID() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ClassObjectReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */