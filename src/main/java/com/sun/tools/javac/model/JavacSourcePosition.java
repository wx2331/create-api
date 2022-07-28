/*    */ package com.sun.tools.javac.model;
/*    */ 
/*    */ import com.sun.tools.javac.util.Position;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JavacSourcePosition
/*    */ {
/*    */   final JavaFileObject sourcefile;
/*    */   final int pos;
/*    */   final Position.LineMap lineMap;
/*    */   
/*    */   JavacSourcePosition(JavaFileObject paramJavaFileObject, int paramInt, Position.LineMap paramLineMap) {
/* 48 */     this.sourcefile = paramJavaFileObject;
/* 49 */     this.pos = paramInt;
/* 50 */     this.lineMap = (paramInt != -1) ? paramLineMap : null;
/*    */   }
/*    */   
/*    */   public JavaFileObject getFile() {
/* 54 */     return this.sourcefile;
/*    */   }
/*    */   
/*    */   public int getOffset() {
/* 58 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int getLine() {
/* 62 */     return (this.lineMap != null) ? this.lineMap.getLineNumber(this.pos) : -1;
/*    */   }
/*    */   
/*    */   public int getColumn() {
/* 66 */     return (this.lineMap != null) ? this.lineMap.getColumnNumber(this.pos) : -1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 70 */     int i = getLine();
/* 71 */     return (i > 0) ? (this.sourcefile + ":" + i) : this.sourcefile
/*    */       
/* 73 */       .toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\model\JavacSourcePosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */