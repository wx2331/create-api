/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.lang.model.element.Modifier;
/*     */ 
/*     */ public class Flags
/*     */ {
/*     */   public static final int PUBLIC = 1;
/*     */   public static final int PRIVATE = 2;
/*     */   public static final int PROTECTED = 4;
/*     */   public static final int STATIC = 8;
/*     */   public static final int FINAL = 16;
/*     */   public static final int SYNCHRONIZED = 32;
/*     */   public static final int VOLATILE = 64;
/*     */   public static final int TRANSIENT = 128;
/*     */   public static final int NATIVE = 256;
/*     */   public static final int INTERFACE = 512;
/*     */   public static final int ABSTRACT = 1024;
/*     */   public static final int STRICTFP = 2048;
/*     */   public static final int SYNTHETIC = 4096;
/*     */   public static final int ANNOTATION = 8192;
/*     */   public static final int ENUM = 16384;
/*     */   public static final int MANDATED = 32768;
/*     */   public static final int StandardFlags = 4095;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_BRIDGE = 64;
/*     */   public static final int ACC_VARARGS = 128;
/*     */   public static final int DEPRECATED = 131072;
/*     */   public static final int HASINIT = 262144;
/*     */   public static final int BLOCK = 1048576;
/*     */   public static final int IPROXY = 2097152;
/*     */   public static final int NOOUTERTHIS = 4194304;
/*     */   public static final int EXISTS = 8388608;
/*     */   public static final int COMPOUND = 16777216;
/*     */   public static final int CLASS_SEEN = 33554432;
/*     */   public static final int SOURCE_SEEN = 67108864;
/*     */   public static final int LOCKED = 134217728;
/*     */   public static final int UNATTRIBUTED = 268435456;
/*     */   public static final int ANONCONSTR = 536870912;
/*     */   public static final int ACYCLIC = 1073741824;
/*     */   public static final long BRIDGE = 2147483648L;
/*     */   
/*     */   public static String toString(long paramLong) {
/*  50 */     StringBuilder stringBuilder = new StringBuilder();
/*  51 */     String str = "";
/*  52 */     for (Flag flag : asFlagSet(paramLong)) {
/*  53 */       stringBuilder.append(str);
/*  54 */       stringBuilder.append(flag);
/*  55 */       str = " ";
/*     */     } 
/*  57 */     return stringBuilder.toString();
/*     */   }
/*     */   public static final long PARAMETER = 8589934592L; public static final long VARARGS = 17179869184L; public static final long ACYCLIC_ANN = 34359738368L; public static final long GENERATEDCONSTR = 68719476736L; public static final long HYPOTHETICAL = 137438953472L; public static final long PROPRIETARY = 274877906944L; public static final long UNION = 549755813888L; public static final long OVERRIDE_BRIDGE = 1099511627776L; public static final long EFFECTIVELY_FINAL = 2199023255552L; public static final long CLASH = 4398046511104L; public static final long DEFAULT = 8796093022208L; public static final long AUXILIARY = 17592186044416L; public static final long NOT_IN_PROFILE = 35184372088832L; public static final long BAD_OVERRIDE = 35184372088832L; public static final long SIGNATURE_POLYMORPHIC = 70368744177664L; public static final long THROWS = 140737488355328L; public static final long POTENTIALLY_AMBIGUOUS = 281474976710656L; public static final long LAMBDA_METHOD = 562949953421312L; public static final long TYPE_TRANSLATED = 1125899906842624L; public static final int AccessFlags = 7; public static final int LocalClassFlags = 23568; public static final int MemberClassFlags = 24087; public static final int ClassFlags = 32273; public static final int InterfaceVarFlags = 25; public static final int VarFlags = 16607; public static final int ConstructorFlags = 7; public static final int InterfaceMethodFlags = 1025; public static final int MethodFlags = 3391; public static final long ExtendedStandardFlags = 8796093026303L; public static final long ModifierFlags = 8796093025791L; public static final long InterfaceMethodMask = 8796093025289L; public static final long AnnotationTypeElementMask = 1025L; public static final long LocalVarFlags = 8589934608L; public static final long ReceiverParamFlags = 8589934592L;
/*     */   public static EnumSet<Flag> asFlagSet(long paramLong) {
/*  61 */     EnumSet<Flag> enumSet = EnumSet.noneOf(Flag.class);
/*  62 */     for (Flag flag : Flag.values()) {
/*  63 */       if ((paramLong & flag.value) != 0L) {
/*  64 */         enumSet.add(flag);
/*  65 */         paramLong &= flag.value ^ 0xFFFFFFFFFFFFFFFFL;
/*     */       } 
/*     */     } 
/*  68 */     Assert.check((paramLong == 0L), "Flags parameter contains unknown flags " + paramLong);
/*  69 */     return enumSet;
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
/*     */   public static Set<Modifier> asModifierSet(long paramLong) {
/* 308 */     Set<Modifier> set = modifierSets.get(Long.valueOf(paramLong));
/* 309 */     if (set == null) {
/* 310 */       set = EnumSet.noneOf(Modifier.class);
/* 311 */       if (0L != (paramLong & 0x1L)) set.add(Modifier.PUBLIC); 
/* 312 */       if (0L != (paramLong & 0x4L)) set.add(Modifier.PROTECTED); 
/* 313 */       if (0L != (paramLong & 0x2L)) set.add(Modifier.PRIVATE); 
/* 314 */       if (0L != (paramLong & 0x400L)) set.add(Modifier.ABSTRACT); 
/* 315 */       if (0L != (paramLong & 0x8L)) set.add(Modifier.STATIC); 
/* 316 */       if (0L != (paramLong & 0x10L)) set.add(Modifier.FINAL); 
/* 317 */       if (0L != (paramLong & 0x80L)) set.add(Modifier.TRANSIENT); 
/* 318 */       if (0L != (paramLong & 0x40L)) set.add(Modifier.VOLATILE); 
/* 319 */       if (0L != (paramLong & 0x20L))
/* 320 */         set.add(Modifier.SYNCHRONIZED); 
/* 321 */       if (0L != (paramLong & 0x100L)) set.add(Modifier.NATIVE); 
/* 322 */       if (0L != (paramLong & 0x800L)) set.add(Modifier.STRICTFP); 
/* 323 */       if (0L != (paramLong & 0x80000000000L)) set.add(Modifier.DEFAULT); 
/* 324 */       set = Collections.unmodifiableSet(set);
/* 325 */       modifierSets.put(Long.valueOf(paramLong), set);
/*     */     } 
/* 327 */     return set;
/*     */   }
/*     */ 
/*     */   
/* 331 */   private static final Map<Long, Set<Modifier>> modifierSets = new ConcurrentHashMap<>(64);
/*     */ 
/*     */   
/*     */   public static boolean isStatic(Symbol paramSymbol) {
/* 335 */     return ((paramSymbol.flags() & 0x8L) != 0L);
/*     */   }
/*     */   
/*     */   public static boolean isEnum(Symbol paramSymbol) {
/* 339 */     return ((paramSymbol.flags() & 0x4000L) != 0L);
/*     */   }
/*     */   
/*     */   public static boolean isConstant(Symbol.VarSymbol paramVarSymbol) {
/* 343 */     return (paramVarSymbol.getConstValue() != null);
/*     */   }
/*     */   
/*     */   public enum Flag
/*     */   {
/* 348 */     PUBLIC(1L),
/* 349 */     PRIVATE(2L),
/* 350 */     PROTECTED(4L),
/* 351 */     STATIC(8L),
/* 352 */     FINAL(16L),
/* 353 */     SYNCHRONIZED(32L),
/* 354 */     VOLATILE(64L),
/* 355 */     TRANSIENT(128L),
/* 356 */     NATIVE(256L),
/* 357 */     INTERFACE(512L),
/* 358 */     ABSTRACT(1024L),
/* 359 */     DEFAULT(8796093022208L),
/* 360 */     STRICTFP(2048L),
/* 361 */     BRIDGE(2147483648L),
/* 362 */     SYNTHETIC(4096L),
/* 363 */     ANNOTATION(8192L),
/* 364 */     DEPRECATED(131072L),
/* 365 */     HASINIT(262144L),
/* 366 */     BLOCK(1048576L),
/* 367 */     ENUM(16384L),
/* 368 */     MANDATED(32768L),
/* 369 */     IPROXY(2097152L),
/* 370 */     NOOUTERTHIS(4194304L),
/* 371 */     EXISTS(8388608L),
/* 372 */     COMPOUND(16777216L),
/* 373 */     CLASS_SEEN(33554432L),
/* 374 */     SOURCE_SEEN(67108864L),
/* 375 */     LOCKED(134217728L),
/* 376 */     UNATTRIBUTED(268435456L),
/* 377 */     ANONCONSTR(536870912L),
/* 378 */     ACYCLIC(1073741824L),
/* 379 */     PARAMETER(8589934592L),
/* 380 */     VARARGS(17179869184L),
/* 381 */     ACYCLIC_ANN(34359738368L),
/* 382 */     GENERATEDCONSTR(68719476736L),
/* 383 */     HYPOTHETICAL(137438953472L),
/* 384 */     PROPRIETARY(274877906944L),
/* 385 */     UNION(549755813888L),
/* 386 */     OVERRIDE_BRIDGE(1099511627776L),
/* 387 */     EFFECTIVELY_FINAL(2199023255552L),
/* 388 */     CLASH(4398046511104L),
/* 389 */     AUXILIARY(17592186044416L),
/* 390 */     NOT_IN_PROFILE(35184372088832L),
/* 391 */     BAD_OVERRIDE(35184372088832L),
/* 392 */     SIGNATURE_POLYMORPHIC(70368744177664L),
/* 393 */     THROWS(140737488355328L),
/* 394 */     LAMBDA_METHOD(562949953421312L),
/* 395 */     TYPE_TRANSLATED(1125899906842624L); final long value; final String lowercaseName;
/*     */     
/*     */     Flag(long param1Long) {
/* 398 */       this.value = param1Long;
/* 399 */       this.lowercaseName = StringUtils.toLowerCase(name());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 404 */       return this.lowercaseName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Flags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */