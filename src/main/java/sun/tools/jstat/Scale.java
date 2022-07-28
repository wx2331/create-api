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
/*     */ public class Scale
/*     */ {
/*  37 */   private static int nextOrdinal = 0;
/*  38 */   private static HashMap<String, Scale> map = new HashMap<>();
/*     */   
/*     */   private final String name;
/*  41 */   private final int ordinal = nextOrdinal++;
/*     */   private final double factor;
/*     */   
/*     */   private Scale(String paramString, double paramDouble) {
/*  45 */     this.name = paramString;
/*  46 */     this.factor = paramDouble;
/*  47 */     assert !map.containsKey(paramString);
/*  48 */     map.put(paramString, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final Scale RAW = new Scale("raw", 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final Scale PERCENT = new Scale("percent", 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final Scale KILO = new Scale("K", 1024.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final Scale MEGA = new Scale("M", 1048576.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static final Scale GIGA = new Scale("G", 1.073741824E9D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Scale TERA = new Scale("T", 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static final Scale PETA = new Scale("P", 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final Scale PICO = new Scale("p", 1.0E-11D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final Scale NANO = new Scale("n", 1.0E-8D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final Scale MICRO = new Scale("u", 1.0E-5D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static final Scale MILLI = new Scale("m", 0.01D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final Scale PSEC = new Scale("ps", 1.0E-11D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static final Scale NSEC = new Scale("ns", 1.0E-8D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final Scale USEC = new Scale("us", 1.0E-5D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public static final Scale MSEC = new Scale("ms", 0.01D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static final Scale SEC = new Scale("s", 1.0D);
/* 130 */   public static final Scale SEC2 = new Scale("sec", 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static final Scale MINUTES = new Scale("min", 0.016666666666666666D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public static final Scale HOUR = new Scale("h", 2.777777777777778E-4D);
/* 141 */   public static final Scale HOUR2 = new Scale("hour", 2.777777777777778E-4D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFactor() {
/* 149 */     return this.factor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale toScale(String paramString) {
/* 169 */     return map.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Set keySet() {
/* 179 */     return map.keySet();
/*     */   }
/*     */   
/*     */   protected double scale(double paramDouble) {
/* 183 */     return paramDouble / this.factor;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Scale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */