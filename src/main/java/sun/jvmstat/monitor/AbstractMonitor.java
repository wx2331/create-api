/*     */ package sun.jvmstat.monitor;
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
/*     */ public abstract class AbstractMonitor
/*     */   implements Monitor
/*     */ {
/*     */   protected String name;
/*     */   protected Units units;
/*     */   protected Variability variability;
/*     */   protected int vectorLength;
/*     */   protected boolean supported;
/*     */   
/*     */   protected AbstractMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean, int paramInt) {
/*  55 */     this.name = paramString;
/*  56 */     this.units = paramUnits;
/*  57 */     this.variability = paramVariability;
/*  58 */     this.vectorLength = paramInt;
/*  59 */     this.supported = paramBoolean;
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
/*     */   protected AbstractMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean) {
/*  73 */     this(paramString, paramUnits, paramVariability, paramBoolean, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  80 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBaseName() {
/*  87 */     int i = this.name.lastIndexOf(".") + 1;
/*  88 */     return this.name.substring(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Units getUnits() {
/*  95 */     return this.units;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Variability getVariability() {
/* 102 */     return this.variability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVector() {
/* 109 */     return (this.vectorLength > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVectorLength() {
/* 116 */     return this.vectorLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupported() {
/* 123 */     return this.supported;
/*     */   }
/*     */   
/*     */   public abstract Object getValue();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\AbstractMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */