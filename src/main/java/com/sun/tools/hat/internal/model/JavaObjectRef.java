/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ import com.sun.tools.hat.internal.util.Misc;
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
/*    */ public class JavaObjectRef
/*    */   extends JavaThing
/*    */ {
/*    */   private long id;
/*    */   
/*    */   public JavaObjectRef(long paramLong) {
/* 48 */     this.id = paramLong;
/*    */   }
/*    */   
/*    */   public long getId() {
/* 52 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean isHeapAllocated() {
/* 56 */     return true;
/*    */   }
/*    */   
/*    */   public JavaThing dereference(Snapshot paramSnapshot, JavaField paramJavaField) {
/* 60 */     return dereference(paramSnapshot, paramJavaField, true);
/*    */   }
/*    */   public JavaThing dereference(Snapshot paramSnapshot, JavaField paramJavaField, boolean paramBoolean) {
/*    */     HackJavaValue hackJavaValue;
/* 64 */     if (paramJavaField != null && !paramJavaField.hasId())
/*    */     {
/*    */       
/* 67 */       return new JavaLong(this.id);
/*    */     }
/* 69 */     if (this.id == 0L) {
/* 70 */       return paramSnapshot.getNullThing();
/*    */     }
/* 72 */     JavaHeapObject javaHeapObject = paramSnapshot.findThing(this.id);
/* 73 */     if (javaHeapObject == null) {
/* 74 */       if (!paramSnapshot.getUnresolvedObjectsOK() && paramBoolean) {
/*    */         
/* 76 */         String str = "WARNING:  Failed to resolve object id " + Misc.toHex(this.id);
/* 77 */         if (paramJavaField != null)
/*    */         {
/* 79 */           str = str + " for field " + paramJavaField.getName() + " (signature " + paramJavaField.getSignature() + ")";
/*    */         }
/* 81 */         System.out.println(str);
/*    */       } 
/*    */ 
/*    */       
/* 85 */       hackJavaValue = new HackJavaValue("Unresolved object " + Misc.toHex(this.id), 0);
/*    */     } 
/* 87 */     return hackJavaValue;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 91 */     return 0;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 95 */     return "Unresolved object " + Misc.toHex(this.id);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaObjectRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */