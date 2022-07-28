/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionFormat
/*     */ {
/*     */   protected String name;
/*     */   protected List<OptionFormat> children;
/*     */   
/*     */   public OptionFormat(String paramString) {
/*  43 */     this.name = paramString;
/*  44 */     this.children = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  48 */     if (paramObject == this) {
/*  49 */       return true;
/*     */     }
/*  51 */     if (!(paramObject instanceof OptionFormat)) {
/*  52 */       return false;
/*     */     }
/*  54 */     OptionFormat optionFormat = (OptionFormat)paramObject;
/*  55 */     return (this.name.compareTo(optionFormat.name) == 0);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  59 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public void addSubFormat(OptionFormat paramOptionFormat) {
/*  63 */     this.children.add(paramOptionFormat);
/*     */   }
/*     */   
/*     */   public OptionFormat getSubFormat(int paramInt) {
/*  67 */     return this.children.get(paramInt);
/*     */   }
/*     */   
/*     */   public void insertSubFormat(int paramInt, OptionFormat paramOptionFormat) {
/*  71 */     this.children.add(paramInt, paramOptionFormat);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Closure paramClosure) throws MonitorException {
/*  80 */     for (null = this.children.iterator(); null.hasNext(); ) {
/*  81 */       OptionFormat optionFormat = null.next();
/*  82 */       paramClosure.visit(optionFormat, null.hasNext());
/*     */     } 
/*     */     
/*  85 */     for (OptionFormat optionFormat : this.children)
/*     */     {
/*  87 */       optionFormat.apply(paramClosure);
/*     */     }
/*     */   }
/*     */   
/*     */   public void printFormat() {
/*  92 */     printFormat(0);
/*     */   }
/*     */   
/*     */   public void printFormat(int paramInt) {
/*  96 */     String str = "  ";
/*  97 */     StringBuilder stringBuilder = new StringBuilder("");
/*     */     
/*  99 */     for (byte b = 0; b < paramInt; b++) {
/* 100 */       stringBuilder.append(str);
/*     */     }
/* 102 */     System.out.println(stringBuilder + this.name + " {");
/*     */ 
/*     */     
/* 105 */     for (OptionFormat optionFormat : this.children) {
/* 106 */       optionFormat.printFormat(paramInt + 1);
/*     */     }
/* 108 */     System.out.println(stringBuilder + "}");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\OptionFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */