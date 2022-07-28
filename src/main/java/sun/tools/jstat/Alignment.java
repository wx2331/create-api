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
/*     */ public abstract class Alignment
/*     */ {
/*  38 */   private static int nextOrdinal = 0;
/*  39 */   private static HashMap<String, Alignment> map = new HashMap<>();
/*     */   private static final String blanks = "                                                                                                                                                               ";
/*     */   private final String name;
/*  42 */   private final int value = nextOrdinal++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final Alignment CENTER = new Alignment("center") {
/*     */       protected String align(String param1String, int param1Int) {
/*  51 */         int i = param1String.length();
/*  52 */         if (i >= param1Int) {
/*  53 */           return param1String;
/*     */         }
/*     */         
/*  56 */         int j = param1Int - i;
/*  57 */         int k = j / 2;
/*  58 */         int m = j % 2;
/*  59 */         if (k == 0)
/*     */         {
/*  61 */           return param1String + "                                                                                                                                                               ".substring(0, m);
/*     */         }
/*     */         
/*  64 */         return "                                                                                                                                                               ".substring(0, k) + param1String + "                                                                                                                                                               "
/*  65 */           .substring(0, k + m);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final Alignment LEFT = new Alignment("left") {
/*     */       protected String align(String param1String, int param1Int) {
/*  75 */         int i = param1String.length();
/*  76 */         if (i >= param1Int) {
/*  77 */           return param1String;
/*     */         }
/*  79 */         int j = param1Int - i;
/*  80 */         return param1String + "                                                                                                                                                               ".substring(0, j);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Alignment RIGHT = new Alignment("right") {
/*     */       protected String align(String param1String, int param1Int) {
/*  89 */         int i = param1String.length();
/*  90 */         if (i >= param1Int) {
/*  91 */           return param1String;
/*     */         }
/*  93 */         int j = param1Int - i;
/*  94 */         return "                                                                                                                                                               ".substring(0, j) + param1String;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Alignment toAlignment(String paramString) {
/* 105 */     return map.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set keySet() {
/* 114 */     return map.keySet();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 118 */     return this.name;
/*     */   }
/*     */   
/*     */   private Alignment(String paramString) {
/* 122 */     this.name = paramString;
/* 123 */     map.put(paramString, this);
/*     */   }
/*     */   
/*     */   protected abstract String align(String paramString, int paramInt);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Alignment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */