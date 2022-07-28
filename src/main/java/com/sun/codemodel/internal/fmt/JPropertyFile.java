/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Properties;
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
/*    */ public class JPropertyFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private final Properties data;
/*    */   
/*    */   public JPropertyFile(String name) {
/* 40 */     super(name);
/*    */ 
/*    */     
/* 43 */     this.data = new Properties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(String key, String value) {
/* 51 */     this.data.put(key, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 59 */     this.data.store(out, (String)null);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\JPropertyFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */