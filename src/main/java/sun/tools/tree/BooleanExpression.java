/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
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
/*     */ public class BooleanExpression
/*     */   extends ConstantExpression
/*     */ {
/*     */   boolean value;
/*     */   
/*     */   public BooleanExpression(long paramLong, boolean paramBoolean) {
/*  47 */     super(61, paramLong, Type.tBoolean);
/*  48 */     this.value = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*  55 */     return new Integer(this.value ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(boolean paramBoolean) {
/*  62 */     return (this.value == paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsDefault() {
/*  70 */     return !this.value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars) {
/*  90 */     if (this.value) {
/*  91 */       paramConditionVars.vsFalse = Vset.DEAD_END;
/*  92 */       paramConditionVars.vsTrue = paramVset;
/*     */     } else {
/*  94 */       paramConditionVars.vsFalse = paramVset;
/*  95 */       paramConditionVars.vsTrue = Vset.DEAD_END;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/* 104 */     if (this.value == paramBoolean)
/* 105 */       paramAssembler.add(this.where, 167, paramLabel); 
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 109 */     paramAssembler.add(this.where, 18, new Integer(this.value ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 116 */     paramPrintStream.print(this.value ? "true" : "false");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BooleanExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */