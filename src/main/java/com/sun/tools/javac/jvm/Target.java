/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public enum Target
/*     */ {
/*  44 */   JDK1_1("1.1", 45, 3),
/*  45 */   JDK1_2("1.2", 46, 0),
/*  46 */   JDK1_3("1.3", 47, 0),
/*     */ 
/*     */   
/*  49 */   JDK1_4("1.4", 48, 0),
/*     */ 
/*     */   
/*  52 */   JDK1_5("1.5", 49, 0),
/*     */ 
/*     */   
/*  55 */   JDK1_6("1.6", 50, 0),
/*     */ 
/*     */   
/*  58 */   JDK1_7("1.7", 51, 0),
/*     */ 
/*     */   
/*  61 */   JDK1_8("1.8", 52, 0); private static final Context.Key<Target> targetKey; private static final Target MIN; private static final Target MAX; private static final Map<String, Target> tab; public final String name; public final int majorVersion; public final int minorVersion; public static final Target DEFAULT;
/*     */   
/*  63 */   static { targetKey = new Context.Key();
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
/*  78 */     MIN = values()[0];
/*     */ 
/*     */     
/*  81 */     MAX = values()[(values()).length - 1];
/*     */ 
/*     */     
/*  84 */     tab = new HashMap<>();
/*     */     
/*  86 */     for (Target target : values()) {
/*  87 */       tab.put(target.name, target);
/*     */     }
/*  89 */     tab.put("5", JDK1_5);
/*  90 */     tab.put("6", JDK1_6);
/*  91 */     tab.put("7", JDK1_7);
/*  92 */     tab.put("8", JDK1_8);
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
/* 104 */     DEFAULT = JDK1_8; }
/*     */   public static Target instance(Context paramContext) { Target target = (Target)paramContext.get(targetKey); if (target == null) { Options options = Options.instance(paramContext); String str = options.get(Option.TARGET); if (str != null)
/*     */         target = lookup(str);  if (target == null)
/* 107 */         target = DEFAULT;  paramContext.put(targetKey, target); }  return target; } public static Target lookup(String paramString) { return tab.get(paramString); }
/*     */   public static Target MIN() { return MIN; } public static Target MAX() {
/*     */     return MAX;
/*     */   } Target(String paramString1, int paramInt1, int paramInt2) {
/*     */     this.name = paramString1;
/*     */     this.majorVersion = paramInt1;
/*     */     this.minorVersion = paramInt2;
/*     */   } public boolean requiresIproxy() {
/* 115 */     return (compareTo(JDK1_1) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean initializeFieldsBeforeSuper() {
/* 125 */     return (compareTo(JDK1_4) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean obeyBinaryCompatibility() {
/* 136 */     return (compareTo(JDK1_2) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean arrayBinaryCompatibility() {
/* 145 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean interfaceFieldsBinaryCompatibility() {
/* 154 */     return (compareTo(JDK1_2) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean interfaceObjectOverridesBinaryCompatibility() {
/* 164 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usePrivateSyntheticFields() {
/* 174 */     return (compareTo(JDK1_5) < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useInnerCacheClass() {
/* 183 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generateCLDCStackmap() {
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generateStackMapTable() {
/* 194 */     return (compareTo(JDK1_6) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPackageInfoSynthetic() {
/* 200 */     return (compareTo(JDK1_6) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generateEmptyAfterBig() {
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useStringBuilder() {
/* 214 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean useSyntheticFlag() {
/* 221 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean useEnumFlag() {
/* 224 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean useAnnotationFlag() {
/* 227 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean useVarargsFlag() {
/* 230 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean useBridgeFlag() {
/* 233 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char syntheticNameChar() {
/* 240 */     return '$';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasClassLiterals() {
/* 246 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInvokedynamic() {
/* 252 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMethodHandles() {
/* 260 */     return hasInvokedynamic();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean classLiteralsNoInit() {
/* 268 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInitCause() {
/* 275 */     return (compareTo(JDK1_4) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean boxWithConstructors() {
/* 282 */     return (compareTo(JDK1_5) < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasIterable() {
/* 289 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingMethodAttribute() {
/* 296 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */