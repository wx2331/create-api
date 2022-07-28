/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
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
/*     */ class FieldUpdater
/*     */   implements Constants
/*     */ {
/*     */   private long where;
/*     */   private MemberDefinition field;
/*     */   private Expression base;
/*     */   private MemberDefinition getter;
/*     */   private MemberDefinition setter;
/*     */   private int depth;
/*     */   
/*     */   public FieldUpdater(long paramLong, MemberDefinition paramMemberDefinition1, Expression paramExpression, MemberDefinition paramMemberDefinition2, MemberDefinition paramMemberDefinition3) {
/*  80 */     this.where = paramLong;
/*  81 */     this.field = paramMemberDefinition1;
/*  82 */     this.base = paramExpression;
/*  83 */     this.getter = paramMemberDefinition2;
/*  84 */     this.setter = paramMemberDefinition3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater inline(Environment paramEnvironment, Context paramContext) {
/*  95 */     if (this.base != null) {
/*  96 */       if (this.field.isStatic()) {
/*  97 */         this.base = this.base.inline(paramEnvironment, paramContext);
/*     */       } else {
/*  99 */         this.base = this.base.inlineValue(paramEnvironment, paramContext);
/*     */       } 
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public FieldUpdater copyInline(Context paramContext) {
/* 106 */     return new FieldUpdater(this.where, this.field, this.base.copyInline(paramContext), this.getter, this.setter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext, boolean paramBoolean) {
/* 111 */     int i = paramBoolean ? 7 : 3;
/*     */     
/* 113 */     if (!this.field.isStatic() && this.base != null) {
/* 114 */       i += this.base.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */     
/* 117 */     return i;
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
/*     */   private void codeDup(Assembler paramAssembler, int paramInt1, int paramInt2) {
/* 129 */     switch (paramInt1) {
/*     */       case 0:
/*     */         return;
/*     */       case 1:
/* 133 */         switch (paramInt2) {
/*     */           case 0:
/* 135 */             paramAssembler.add(this.where, 89);
/*     */             return;
/*     */           case 1:
/* 138 */             paramAssembler.add(this.where, 90);
/*     */             return;
/*     */           case 2:
/* 141 */             paramAssembler.add(this.where, 91);
/*     */             return;
/*     */         } 
/*     */         
/*     */         break;
/*     */       case 2:
/* 147 */         switch (paramInt2) {
/*     */           case 0:
/* 149 */             paramAssembler.add(this.where, 92);
/*     */             return;
/*     */           case 1:
/* 152 */             paramAssembler.add(this.where, 93);
/*     */             return;
/*     */           case 2:
/* 155 */             paramAssembler.add(this.where, 94);
/*     */             return;
/*     */         } 
/*     */         
/*     */         break;
/*     */     } 
/* 161 */     throw new CompilerError("can't dup: " + paramInt1 + ", " + paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startUpdate(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 172 */     if (!this.getter.isStatic() || !this.setter.isStatic()) {
/* 173 */       throw new CompilerError("startUpdate isStatic");
/*     */     }
/* 175 */     if (!this.field.isStatic()) {
/*     */       
/* 177 */       this.base.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 178 */       this.depth = 1;
/*     */     }
/*     */     else {
/*     */       
/* 182 */       if (this.base != null) {
/* 183 */         this.base.code(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/* 185 */       this.depth = 0;
/*     */     } 
/* 187 */     codeDup(paramAssembler, this.depth, 0);
/* 188 */     paramAssembler.add(this.where, 184, this.getter);
/* 189 */     if (paramBoolean) {
/* 190 */       codeDup(paramAssembler, this.field.getType().stackSize(), this.depth);
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
/*     */   public void finishUpdate(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 204 */     if (paramBoolean) {
/* 205 */       codeDup(paramAssembler, this.field.getType().stackSize(), this.depth);
/*     */     }
/* 207 */     paramAssembler.add(this.where, 184, this.setter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startAssign(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 218 */     if (!this.setter.isStatic()) {
/* 219 */       throw new CompilerError("startAssign isStatic");
/*     */     }
/* 221 */     if (!this.field.isStatic()) {
/*     */       
/* 223 */       this.base.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 224 */       this.depth = 1;
/*     */     }
/*     */     else {
/*     */       
/* 228 */       if (this.base != null) {
/* 229 */         this.base.code(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/* 231 */       this.depth = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void finishAssign(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 236 */     if (paramBoolean) {
/* 237 */       codeDup(paramAssembler, this.field.getType().stackSize(), this.depth);
/*     */     }
/* 239 */     paramAssembler.add(this.where, 184, this.setter);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\FieldUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */