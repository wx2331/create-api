/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExprExpression
/*     */   extends UnaryExpression
/*     */ {
/*     */   public ExprExpression(long paramLong, Expression paramExpression) {
/*  45 */     super(56, paramLong, paramExpression.type, paramExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars) {
/*  53 */     this.right.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, paramConditionVars);
/*  54 */     this.type = this.right.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression) {
/*  64 */     paramVset = this.right.checkAssignOp(paramEnvironment, paramContext, paramVset, paramHashtable, paramExpression);
/*  65 */     this.type = this.right.type;
/*  66 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/*  74 */     return this.right.getUpdater(paramEnvironment, paramContext);
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
/*     */   public boolean isNull() {
/*  90 */     return this.right.isNull();
/*     */   }
/*     */   
/*     */   public boolean isNonNull() {
/*  94 */     return this.right.isNonNull();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*  99 */     return this.right.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringBuffer inlineValueSB(Environment paramEnvironment, Context paramContext, StringBuffer paramStringBuffer) {
/* 110 */     return this.right.inlineValueSB(paramEnvironment, paramContext, paramStringBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 117 */     this.type = this.right.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/* 124 */     return this.right;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ExprExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */