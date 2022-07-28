/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.java.ClassDeclaration;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
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
/*    */ public class SuperExpression
/*    */   extends ThisExpression
/*    */ {
/*    */   public SuperExpression(long paramLong) {
/* 45 */     super(83, paramLong);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SuperExpression(long paramLong, Expression paramExpression) {
/* 52 */     super(paramLong, paramExpression);
/* 53 */     this.op = 83;
/*    */   }
/*    */   
/*    */   public SuperExpression(long paramLong, Context paramContext) {
/* 57 */     super(paramLong, paramContext);
/* 58 */     this.op = 83;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 65 */     paramVset = checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 66 */     if (this.type != Type.tError)
/*    */     {
/* 68 */       paramEnvironment.error(this.where, "undef.var.super", idSuper);
/*    */     }
/* 70 */     return paramVset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/* 79 */     return checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable);
/*    */   }
/*    */ 
/*    */   
/*    */   private Vset checkCommon(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 84 */     ClassDeclaration classDeclaration = paramContext.field.getClassDefinition().getSuperClass();
/* 85 */     if (classDeclaration == null) {
/* 86 */       paramEnvironment.error(this.where, "undef.var", idSuper);
/* 87 */       this.type = Type.tError;
/* 88 */       return paramVset;
/*    */     } 
/* 90 */     paramVset = super.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 91 */     this.type = classDeclaration.getType();
/* 92 */     return paramVset;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\SuperExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */