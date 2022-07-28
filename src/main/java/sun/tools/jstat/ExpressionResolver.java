/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.Variability;
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
/*     */ public class ExpressionResolver
/*     */   implements ExpressionEvaluator
/*     */ {
/*  40 */   private static boolean debug = Boolean.getBoolean("ExpressionResolver.debug");
/*     */   private MonitoredVm vm;
/*     */   
/*     */   ExpressionResolver(MonitoredVm paramMonitoredVm) {
/*  44 */     this.vm = paramMonitoredVm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate(Expression paramExpression) throws MonitorException {
/*  53 */     if (paramExpression == null) {
/*  54 */       return null;
/*     */     }
/*     */     
/*  57 */     if (debug) {
/*  58 */       System.out.println("Resolving Expression:" + paramExpression);
/*     */     }
/*     */     
/*  61 */     if (paramExpression instanceof Identifier) {
/*  62 */       Identifier identifier = (Identifier)paramExpression;
/*     */ 
/*     */       
/*  65 */       if (identifier.isResolved()) {
/*  66 */         return identifier;
/*     */       }
/*     */ 
/*     */       
/*  70 */       Monitor monitor = this.vm.findByName(identifier.getName());
/*  71 */       if (monitor == null) {
/*  72 */         System.err.println("Warning: Unresolved Symbol: " + identifier
/*  73 */             .getName() + " substituted NaN");
/*  74 */         return new Literal(new Double(Double.NaN));
/*     */       } 
/*  76 */       if (monitor.getVariability() == Variability.CONSTANT) {
/*  77 */         if (debug) {
/*  78 */           System.out.println("Converting constant " + identifier.getName() + " to literal with value " + monitor
/*     */               
/*  80 */               .getValue());
/*     */         }
/*  82 */         return new Literal(monitor.getValue());
/*     */       } 
/*  84 */       identifier.setValue(monitor);
/*  85 */       return identifier;
/*     */     } 
/*     */     
/*  88 */     if (paramExpression instanceof Literal) {
/*  89 */       return paramExpression;
/*     */     }
/*     */     
/*  92 */     Expression expression1 = null;
/*  93 */     Expression expression2 = null;
/*     */     
/*  95 */     if (paramExpression.getLeft() != null) {
/*  96 */       expression1 = (Expression)evaluate(paramExpression.getLeft());
/*     */     }
/*  98 */     if (paramExpression.getRight() != null) {
/*  99 */       expression2 = (Expression)evaluate(paramExpression.getRight());
/*     */     }
/*     */     
/* 102 */     if (expression1 != null && expression2 != null && 
/* 103 */       expression1 instanceof Literal && expression2 instanceof Literal) {
/* 104 */       Literal literal1 = (Literal)expression1;
/* 105 */       Literal literal2 = (Literal)expression2;
/* 106 */       boolean bool = false;
/*     */       
/* 108 */       Double double_ = new Double(Double.NaN);
/* 109 */       if (literal1.getValue() instanceof String) {
/* 110 */         bool = true; literal1.setValue(double_);
/*     */       } 
/* 112 */       if (literal2.getValue() instanceof String) {
/* 113 */         bool = true; literal2.setValue(double_);
/*     */       } 
/* 115 */       if (debug && bool) {
/* 116 */         System.out.println("Warning: String literal in numerical expression: substitutied NaN");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       Number number1 = (Number)literal1.getValue();
/* 123 */       Number number2 = (Number)literal2.getValue();
/* 124 */       double d = paramExpression.getOperator().eval(number1.doubleValue(), number2
/* 125 */           .doubleValue());
/* 126 */       if (debug) {
/* 127 */         System.out.println("Converting expression " + paramExpression + " (left = " + number1
/* 128 */             .doubleValue() + ") (right = " + number2
/* 129 */             .doubleValue() + ") to literal value " + d);
/*     */       }
/*     */       
/* 132 */       return new Literal(new Double(d));
/*     */     } 
/*     */ 
/*     */     
/* 136 */     if (expression1 != null && expression2 == null) {
/* 137 */       return expression1;
/*     */     }
/*     */     
/* 140 */     paramExpression.setLeft(expression1);
/* 141 */     paramExpression.setRight(expression2);
/*     */     
/* 143 */     return paramExpression;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\ExpressionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */