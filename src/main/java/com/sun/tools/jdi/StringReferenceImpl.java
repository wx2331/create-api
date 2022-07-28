/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.StringReference;
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
/*    */ 
/*    */ public class StringReferenceImpl
/*    */   extends ObjectReferenceImpl
/*    */   implements StringReference
/*    */ {
/*    */   private String value;
/*    */   
/*    */   StringReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/* 36 */     super(paramVirtualMachine, paramLong);
/*    */   }
/*    */   
/*    */   public String value() {
/* 40 */     if (this.value == null) {
/*    */       
/*    */       try {
/*    */         
/* 44 */         this
/* 45 */           .value = (JDWP.StringReference.Value.process(this.vm, this)).stringValue;
/* 46 */       } catch (JDWPException jDWPException) {
/* 47 */         throw jDWPException.toJDIException();
/*    */       } 
/*    */     }
/* 50 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     return "\"" + value() + "\"";
/*    */   }
/*    */   
/*    */   byte typeValueKey() {
/* 58 */     return 115;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\StringReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */