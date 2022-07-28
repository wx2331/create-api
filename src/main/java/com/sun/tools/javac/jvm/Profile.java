/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.EnumSet;
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
/*     */ public enum Profile
/*     */ {
/*  42 */   COMPACT1("compact1", 1, Target.JDK1_8, new Target[0]),
/*  43 */   COMPACT2("compact2", 2, Target.JDK1_8, new Target[0]),
/*  44 */   COMPACT3("compact3", 3, Target.JDK1_8, new Target[0]),
/*     */   
/*  46 */   DEFAULT
/*     */   {
/*     */     public boolean isValid(Target param1Target) {
/*  49 */       return true;
/*     */     } };
/*     */   private static final Context.Key<Profile> profileKey; public final String name;
/*     */   static {
/*  53 */     profileKey = new Context.Key();
/*     */   }
/*     */   public final int value; final Set<Target> targets;
/*     */   public static Profile instance(Context paramContext) {
/*  57 */     Profile profile = (Profile)paramContext.get(profileKey);
/*  58 */     if (profile == null) {
/*  59 */       Options options = Options.instance(paramContext);
/*  60 */       String str = options.get(Option.PROFILE);
/*  61 */       if (str != null) profile = lookup(str); 
/*  62 */       if (profile == null) profile = DEFAULT; 
/*  63 */       paramContext.put(profileKey, profile);
/*     */     } 
/*  65 */     return profile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Profile() {
/*  73 */     this.name = null;
/*  74 */     this.value = Integer.MAX_VALUE;
/*  75 */     this.targets = null;
/*     */   }
/*     */   
/*     */   Profile(String paramString1, int paramInt1, Target paramTarget, Target... paramVarArgs) {
/*  79 */     this.name = paramString1;
/*  80 */     this.value = paramInt1;
/*  81 */     this.targets = EnumSet.of(paramTarget, paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Profile lookup(String paramString) {
/*  86 */     for (Profile profile : values()) {
/*  87 */       if (paramString.equals(profile.name))
/*  88 */         return profile; 
/*     */     } 
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Profile lookup(int paramInt) {
/*  95 */     for (Profile profile : values()) {
/*  96 */       if (paramInt == profile.value)
/*  97 */         return profile; 
/*     */     } 
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isValid(Target paramTarget) {
/* 103 */     return this.targets.contains(paramTarget);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Profile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */