/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public class JSerializedObject
/*    */   extends JResourceFile
/*    */ {
/*    */   private final Object obj;
/*    */   
/*    */   public JSerializedObject(String name, Object obj) throws IOException {
/* 48 */     super(name);
/* 49 */     this.obj = obj;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void build(OutputStream os) throws IOException {
/* 57 */     ObjectOutputStream oos = new ObjectOutputStream(os);
/* 58 */     oos.writeObject(this.obj);
/* 59 */     oos.close();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\JSerializedObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */