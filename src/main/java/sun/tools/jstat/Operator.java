/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
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
/*     */ public abstract class Operator
/*     */ {
/*  39 */   private static int nextOrdinal = 0;
/*  40 */   private static HashMap<String, Operator> map = new HashMap<>();
/*     */   
/*     */   private final String name;
/*  43 */   private final int ordinal = nextOrdinal++;
/*     */   
/*     */   private Operator(String paramString) {
/*  46 */     this.name = paramString;
/*  47 */     map.put(paramString, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Operator PLUS = new Operator("+") {
/*     */       protected double eval(double param1Double1, double param1Double2) {
/*  55 */         return param1Double1 + param1Double2;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  60 */   public static final Operator MINUS = new Operator("-") {
/*     */       protected double eval(double param1Double1, double param1Double2) {
/*  62 */         return param1Double1 - param1Double2;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  67 */   public static final Operator DIVIDE = new Operator("/") {
/*     */       protected double eval(double param1Double1, double param1Double2) {
/*  69 */         if (param1Double2 == 0.0D) {
/*  70 */           return Double.NaN;
/*     */         }
/*  72 */         return param1Double1 / param1Double2;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  77 */   public static final Operator MULTIPLY = new Operator("*") {
/*     */       protected double eval(double param1Double1, double param1Double2) {
/*  79 */         return param1Double1 * param1Double2;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  89 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Operator toOperator(String paramString) {
/*  99 */     return map.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Set keySet() {
/* 109 */     return map.keySet();
/*     */   }
/*     */   
/*     */   protected abstract double eval(double paramDouble1, double paramDouble2);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Operator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */