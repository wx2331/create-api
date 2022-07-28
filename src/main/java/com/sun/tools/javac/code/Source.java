/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.SourceVersion;
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
/*     */ public enum Source
/*     */ {
/*  52 */   JDK1_2("1.2"),
/*     */ 
/*     */   
/*  55 */   JDK1_3("1.3"),
/*     */ 
/*     */   
/*  58 */   JDK1_4("1.4"),
/*     */ 
/*     */ 
/*     */   
/*  62 */   JDK1_5("1.5"),
/*     */ 
/*     */   
/*  65 */   JDK1_6("1.6"),
/*     */ 
/*     */   
/*  68 */   JDK1_7("1.7"),
/*     */ 
/*     */   
/*  71 */   JDK1_8("1.8"); private static final Context.Key<Source> sourceKey; public final String name; private static final Map<String, Source> tab; public static final Source DEFAULT;
/*     */   
/*  73 */   static { sourceKey = new Context.Key();
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
/*  90 */     tab = new HashMap<>();
/*     */     
/*  92 */     for (Source source : values()) {
/*  93 */       tab.put(source.name, source);
/*     */     }
/*  95 */     tab.put("5", JDK1_5);
/*  96 */     tab.put("6", JDK1_6);
/*  97 */     tab.put("7", JDK1_7);
/*  98 */     tab.put("8", JDK1_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     DEFAULT = JDK1_8; }
/*     */   public static Source instance(Context paramContext) { Source source = (Source)paramContext.get(sourceKey); if (source == null) { Options options = Options.instance(paramContext); String str = options.get(Option.SOURCE); if (str != null)
/*     */         source = lookup(str);  if (source == null)
/* 108 */         source = DEFAULT;  paramContext.put(sourceKey, source); }  return source; } public static Source lookup(String paramString) { return tab.get(paramString); }
/*     */    Source(String paramString1) {
/*     */     this.name = paramString1;
/*     */   } public Target requiredTarget() {
/* 112 */     if (compareTo(JDK1_8) >= 0) return Target.JDK1_8; 
/* 113 */     if (compareTo(JDK1_7) >= 0) return Target.JDK1_7; 
/* 114 */     if (compareTo(JDK1_6) >= 0) return Target.JDK1_6; 
/* 115 */     if (compareTo(JDK1_5) >= 0) return Target.JDK1_5; 
/* 116 */     if (compareTo(JDK1_4) >= 0) return Target.JDK1_4; 
/* 117 */     return Target.JDK1_1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowEncodingErrors() {
/* 122 */     return (compareTo(JDK1_6) < 0);
/*     */   }
/*     */   public boolean allowAsserts() {
/* 125 */     return (compareTo(JDK1_4) >= 0);
/*     */   }
/*     */   public boolean allowCovariantReturns() {
/* 128 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowGenerics() {
/* 131 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowDiamond() {
/* 134 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowMulticatch() {
/* 137 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowImprovedRethrowAnalysis() {
/* 140 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowImprovedCatchAnalysis() {
/* 143 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowEnums() {
/* 146 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowForeach() {
/* 149 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowStaticImport() {
/* 152 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowBoxing() {
/* 155 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowVarargs() {
/* 158 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowAnnotations() {
/* 161 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   
/*     */   public boolean allowHexFloats() {
/* 165 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowAnonOuterThis() {
/* 168 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean addBridges() {
/* 171 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean enforceMandatoryWarnings() {
/* 174 */     return (compareTo(JDK1_5) >= 0);
/*     */   }
/*     */   public boolean allowTryWithResources() {
/* 177 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowBinaryLiterals() {
/* 180 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowUnderscoresInLiterals() {
/* 183 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowStringsInSwitch() {
/* 186 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowSimplifiedVarargs() {
/* 189 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowObjectToPrimitiveCast() {
/* 192 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean enforceThisDotInit() {
/* 195 */     return (compareTo(JDK1_7) >= 0);
/*     */   }
/*     */   public boolean allowPoly() {
/* 198 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowLambda() {
/* 201 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowMethodReferences() {
/* 204 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowDefaultMethods() {
/* 207 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowStaticInterfaceMethods() {
/* 210 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowStrictMethodClashCheck() {
/* 213 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowEffectivelyFinalInInnerClasses() {
/* 216 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowTypeAnnotations() {
/* 219 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowAnnotationsAfterTypeParams() {
/* 222 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowRepeatedAnnotations() {
/* 225 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowIntersectionTypesInCast() {
/* 228 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowGraphInference() {
/* 231 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowFunctionalInterfaceMostSpecific() {
/* 234 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public boolean allowPostApplicabilityVarargsAccessCheck() {
/* 237 */     return (compareTo(JDK1_8) >= 0);
/*     */   }
/*     */   public static SourceVersion toSourceVersion(Source paramSource) {
/* 240 */     switch (paramSource) {
/*     */       case JDK1_2:
/* 242 */         return SourceVersion.RELEASE_2;
/*     */       case JDK1_3:
/* 244 */         return SourceVersion.RELEASE_3;
/*     */       case JDK1_4:
/* 246 */         return SourceVersion.RELEASE_4;
/*     */       case JDK1_5:
/* 248 */         return SourceVersion.RELEASE_5;
/*     */       case JDK1_6:
/* 250 */         return SourceVersion.RELEASE_6;
/*     */       case JDK1_7:
/* 252 */         return SourceVersion.RELEASE_7;
/*     */       case JDK1_8:
/* 254 */         return SourceVersion.RELEASE_8;
/*     */     } 
/* 256 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Source.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */