/*    */ package com.sun.tools.internal.xjc.api;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.impl.s2j.SchemaCompilerImpl;
/*    */ import com.sun.xml.internal.bind.api.impl.NameConverter;
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
/*    */ public final class XJC
/*    */ {
/*    */   public static SchemaCompiler createSchemaCompiler() {
/* 47 */     return (SchemaCompiler)new SchemaCompilerImpl();
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getDefaultPackageName(String namespaceUri) {
/* 63 */     if (namespaceUri == null) throw new IllegalArgumentException(); 
/* 64 */     return NameConverter.standard.toPackageName(namespaceUri);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\XJC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */