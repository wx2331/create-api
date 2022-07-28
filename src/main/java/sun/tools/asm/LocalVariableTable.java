/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class LocalVariableTable
/*     */ {
/*  43 */   LocalVariable[] locals = new LocalVariable[8];
/*     */ 
/*     */   
/*     */   int len;
/*     */ 
/*     */   
/*     */   void define(MemberDefinition paramMemberDefinition, int paramInt1, int paramInt2, int paramInt3) {
/*  50 */     if (paramInt2 >= paramInt3) {
/*     */       return;
/*     */     }
/*  53 */     for (byte b = 0; b < this.len; b++) {
/*  54 */       if ((this.locals[b]).field == paramMemberDefinition && (this.locals[b]).slot == paramInt1 && paramInt2 <= (this.locals[b]).to && paramInt3 >= (this.locals[b]).from) {
/*     */         
/*  56 */         (this.locals[b]).from = Math.min((this.locals[b]).from, paramInt2);
/*  57 */         (this.locals[b]).to = Math.max((this.locals[b]).to, paramInt3);
/*     */         return;
/*     */       } 
/*     */     } 
/*  61 */     if (this.len == this.locals.length) {
/*  62 */       LocalVariable[] arrayOfLocalVariable = new LocalVariable[this.len * 2];
/*  63 */       System.arraycopy(this.locals, 0, arrayOfLocalVariable, 0, this.len);
/*  64 */       this.locals = arrayOfLocalVariable;
/*     */     } 
/*  66 */     this.locals[this.len++] = new LocalVariable(paramMemberDefinition, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim_ranges() {
/*  81 */     for (byte b = 0; b < this.len; b++) {
/*  82 */       for (int i = b + 1; i < this.len; i++) {
/*  83 */         if ((this.locals[b]).field.getName() == (this.locals[i]).field.getName() && (this.locals[b]).from <= (this.locals[i]).to && (this.locals[b]).to >= (this.locals[i]).from)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*  88 */           if ((this.locals[b]).slot < (this.locals[i]).slot) {
/*  89 */             if ((this.locals[b]).from < (this.locals[i]).from) {
/*  90 */               (this.locals[b]).to = Math.min((this.locals[b]).to, (this.locals[i]).from);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 100 */           else if ((this.locals[b]).slot > (this.locals[i]).slot && 
/* 101 */             (this.locals[b]).from > (this.locals[i]).from) {
/* 102 */             (this.locals[i]).to = Math.min((this.locals[i]).to, (this.locals[b]).from);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 121 */     trim_ranges();
/* 122 */     paramDataOutputStream.writeShort(this.len);
/* 123 */     for (byte b = 0; b < this.len; b++) {
/*     */       
/* 125 */       paramDataOutputStream.writeShort((this.locals[b]).from);
/* 126 */       paramDataOutputStream.writeShort((this.locals[b]).to - (this.locals[b]).from);
/* 127 */       paramDataOutputStream.writeShort(paramConstantPool.index((this.locals[b]).field.getName().toString()));
/* 128 */       paramDataOutputStream.writeShort(paramConstantPool.index((this.locals[b]).field.getType().getTypeSignature()));
/* 129 */       paramDataOutputStream.writeShort((this.locals[b]).slot);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\LocalVariableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */