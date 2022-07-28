/*    */ package com.sun.codemodel.internal.writer;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PrologCodeWriter
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private final String prolog;
/*    */   
/*    */   public PrologCodeWriter(CodeWriter core, String prolog) {
/* 60 */     super(core);
/* 61 */     this.prolog = prolog;
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 66 */     Writer w = super.openSource(pkg, fileName);
/*    */     
/* 68 */     PrintWriter out = new PrintWriter(w);
/*    */ 
/*    */     
/* 71 */     if (this.prolog != null) {
/* 72 */       out.println("//");
/*    */       
/* 74 */       String s = this.prolog;
/*    */       int idx;
/* 76 */       while ((idx = s.indexOf('\n')) != -1) {
/* 77 */         out.println("// " + s.substring(0, idx));
/* 78 */         s = s.substring(idx + 1);
/*    */       } 
/* 80 */       out.println("//");
/* 81 */       out.println();
/*    */     } 
/* 83 */     out.flush();
/*    */     
/* 85 */     return w;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\writer\PrologCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */