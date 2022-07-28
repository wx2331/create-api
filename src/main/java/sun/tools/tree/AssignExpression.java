/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
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
/*     */ public class AssignExpression
/*     */   extends BinaryAssignExpression
/*     */ {
/*  41 */   private FieldUpdater updater = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AssignExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  47 */     super(1, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  54 */     if (this.left instanceof IdentifierExpression) {
/*     */ 
/*     */       
/*  57 */       paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  58 */       paramVset = this.left.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     } else {
/*     */       
/*  61 */       paramVset = this.left.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  62 */       paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     } 
/*  64 */     this.type = this.left.type;
/*  65 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/*     */ 
/*     */     
/*  68 */     this.updater = this.left.getAssigner(paramEnvironment, paramContext);
/*     */     
/*  70 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  77 */     if (this.implementation != null) {
/*  78 */       return this.implementation.inlineValue(paramEnvironment, paramContext);
/*     */     }
/*     */     
/*  81 */     this.left = this.left.inlineLHS(paramEnvironment, paramContext);
/*  82 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*  83 */     if (this.updater != null) {
/*  84 */       this.updater = this.updater.inline(paramEnvironment, paramContext);
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/*  93 */     if (this.implementation != null)
/*  94 */       return this.implementation.copyInline(paramContext); 
/*  95 */     AssignExpression assignExpression = (AssignExpression)clone();
/*  96 */     assignExpression.left = this.left.copyInline(paramContext);
/*  97 */     assignExpression.right = this.right.copyInline(paramContext);
/*  98 */     if (this.updater != null) {
/*  99 */       assignExpression.updater = this.updater.copyInline(paramContext);
/*     */     }
/* 101 */     return assignExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 111 */     return (this.updater != null) ? (this.right
/*     */ 
/*     */       
/* 114 */       .costInline(paramInt, paramEnvironment, paramContext) + this.updater
/* 115 */       .costInline(paramInt, paramEnvironment, paramContext, false)) : (this.right
/*     */ 
/*     */       
/* 118 */       .costInline(paramInt, paramEnvironment, paramContext) + this.left
/* 119 */       .costInline(paramInt, paramEnvironment, paramContext) + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 126 */     if (this.updater == null) {
/*     */       
/* 128 */       int i = this.left.codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 129 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 130 */       codeDup(paramEnvironment, paramContext, paramAssembler, this.right.type.stackSize(), i);
/* 131 */       this.left.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 136 */       this.updater.startAssign(paramEnvironment, paramContext, paramAssembler);
/* 137 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 138 */       this.updater.finishAssign(paramEnvironment, paramContext, paramAssembler, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 143 */     if (this.updater == null) {
/*     */       
/* 145 */       this.left.codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 146 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 147 */       this.left.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 152 */       this.updater.startAssign(paramEnvironment, paramContext, paramAssembler);
/* 153 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 154 */       this.updater.finishAssign(paramEnvironment, paramContext, paramAssembler, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\AssignExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */