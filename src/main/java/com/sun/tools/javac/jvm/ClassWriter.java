/*      */ package com.sun.tools.javac.jvm;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.file.BaseFileObject;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.ByteBuffer;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.tools.FileObject;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.StandardLocation;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class ClassWriter
/*      */   extends ClassFile
/*      */ {
/*   66 */   protected static final Context.Key<ClassWriter> classWriterKey = new Context.Key();
/*      */
/*      */
/*      */
/*      */   private final Options options;
/*      */
/*      */
/*      */
/*      */   private boolean verbose;
/*      */
/*      */
/*      */
/*      */   private boolean scramble;
/*      */
/*      */
/*      */
/*      */   private boolean scrambleAll;
/*      */
/*      */
/*      */
/*      */   private boolean retrofit;
/*      */
/*      */
/*      */
/*      */   private boolean emitSourceFile;
/*      */
/*      */
/*      */
/*      */   private boolean genCrt;
/*      */
/*      */
/*      */
/*      */   boolean debugstackmap;
/*      */
/*      */
/*      */
/*      */   private Target target;
/*      */
/*      */
/*      */
/*      */   private Source source;
/*      */
/*      */
/*      */
/*      */   private Types types;
/*      */
/*      */
/*      */
/*      */   static final int DATA_BUF_SIZE = 65520;
/*      */
/*      */
/*      */   static final int POOL_BUF_SIZE = 131056;
/*      */
/*      */
/*  120 */   ByteBuffer databuf = new ByteBuffer(65520);
/*      */
/*      */
/*      */
/*  124 */   ByteBuffer poolbuf = new ByteBuffer(131056);
/*      */
/*      */
/*      */   Pool pool;
/*      */
/*      */
/*      */   Set<Symbol.ClassSymbol> innerClasses;
/*      */
/*      */
/*      */   ListBuffer<Symbol.ClassSymbol> innerClassesQueue;
/*      */
/*      */
/*      */   Map<Pool.DynamicMethod, Pool.MethodHandle> bootstrapMethods;
/*      */
/*      */   private final Log log;
/*      */
/*      */   private final Names names;
/*      */
/*      */   private final JavaFileManager fileManager;
/*      */
/*      */   private final CWSignatureGenerator signatureGen;
/*      */
/*      */   static final int SAME_FRAME_SIZE = 64;
/*      */
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_EXTENDED = 247;
/*      */
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */
/*      */   static final int FULL_FRAME = 255;
/*      */
/*      */   static final int MAX_LOCAL_LENGTH_DIFF = 4;
/*      */
/*      */   private final boolean dumpClassModifiers;
/*      */
/*      */   private final boolean dumpFieldModifiers;
/*      */
/*      */   private final boolean dumpInnerClassModifiers;
/*      */
/*      */   private final boolean dumpMethodModifiers;
/*      */
/*      */
/*      */   public static ClassWriter instance(Context paramContext) {
/*  166 */     ClassWriter classWriter = (ClassWriter)paramContext.get(classWriterKey);
/*  167 */     if (classWriter == null)
/*  168 */       classWriter = new ClassWriter(paramContext);
/*  169 */     return classWriter;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static String flagNames(long paramLong) {
/*  229 */     StringBuilder stringBuilder = new StringBuilder();
/*  230 */     byte b = 0;
/*  231 */     long l = paramLong & 0xFFFL;
/*  232 */     while (l != 0L) {
/*  233 */       if ((l & 0x1L) != 0L) {
/*  234 */         stringBuilder.append(" ");
/*  235 */         stringBuilder.append(flagName[b]);
/*      */       }
/*  237 */       l >>= 1L;
/*  238 */       b++;
/*      */     }
/*  240 */     return stringBuilder.toString();
/*      */   }
/*      */
/*  243 */   private static final String[] flagName = new String[] { "PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "SUPER", "VOLATILE", "TRANSIENT", "NATIVE", "INTERFACE", "ABSTRACT", "STRICTFP" };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   AttributeWriter awriter;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void putChar(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
/*  256 */     paramByteBuffer.elems[paramInt1] = (byte)(paramInt2 >> 8 & 0xFF);
/*  257 */     paramByteBuffer.elems[paramInt1 + 1] = (byte)(paramInt2 & 0xFF);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void putInt(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
/*  264 */     paramByteBuffer.elems[paramInt1] = (byte)(paramInt2 >> 24 & 0xFF);
/*  265 */     paramByteBuffer.elems[paramInt1 + 1] = (byte)(paramInt2 >> 16 & 0xFF);
/*  266 */     paramByteBuffer.elems[paramInt1 + 2] = (byte)(paramInt2 >> 8 & 0xFF);
/*  267 */     paramByteBuffer.elems[paramInt1 + 3] = (byte)(paramInt2 & 0xFF);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private class CWSignatureGenerator
/*      */     extends Types.SignatureGenerator
/*      */   {
/*  278 */     ByteBuffer sigbuf = new ByteBuffer();
/*      */
/*      */     CWSignatureGenerator(Types param1Types) {
/*  281 */       super(param1Types);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void assembleSig(Type param1Type) {
/*  290 */       param1Type = param1Type.unannotatedType();
/*  291 */       switch (param1Type.getTag()) {
/*      */
/*      */
/*      */         case CLDC:
/*      */         case JSR202:
/*  296 */           assembleSig(ClassWriter.this.types.erasure(((UninitializedType)param1Type).qtype));
/*      */           return;
/*      */       }
/*  299 */       super.assembleSig(param1Type);
/*      */     }
/*      */
/*      */
/*      */
/*      */     protected void append(char param1Char) {
/*  305 */       this.sigbuf.appendByte(param1Char);
/*      */     }
/*      */
/*      */
/*      */     protected void append(byte[] param1ArrayOfbyte) {
/*  310 */       this.sigbuf.appendBytes(param1ArrayOfbyte);
/*      */     }
/*      */
/*      */
/*      */     protected void append(Name param1Name) {
/*  315 */       this.sigbuf.appendName(param1Name);
/*      */     }
/*      */
/*      */
/*      */     protected void classReference(Symbol.ClassSymbol param1ClassSymbol) {
/*  320 */       ClassWriter.this.enterInner(param1ClassSymbol);
/*      */     }
/*      */
/*      */     private void reset() {
/*  324 */       this.sigbuf.reset();
/*      */     }
/*      */
/*      */     private Name toName() {
/*  328 */       return this.sigbuf.toName(ClassWriter.this.names);
/*      */     }
/*      */
/*      */     private boolean isEmpty() {
/*  332 */       return (this.sigbuf.length == 0);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Name typeSig(Type paramType) {
/*  340 */     Assert.check(this.signatureGen.isEmpty());
/*      */
/*  342 */     this.signatureGen.assembleSig(paramType);
/*  343 */     Name name = this.signatureGen.toName();
/*  344 */     this.signatureGen.reset();
/*      */
/*  346 */     return name;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Name xClassName(Type paramType) {
/*  353 */     if (paramType.hasTag(TypeTag.CLASS))
/*  354 */       return this.names.fromUtf(externalize(paramType.tsym.flatName()));
/*  355 */     if (paramType.hasTag(TypeTag.ARRAY)) {
/*  356 */       return typeSig(this.types.erasure(paramType));
/*      */     }
/*  358 */     throw new AssertionError("xClassName");
/*      */   }
/*      */
/*      */
/*      */   public static class PoolOverflow
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */   }
/*      */
/*      */
/*      */   public static class StringOverflow
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     public final String value;
/*      */
/*      */     public StringOverflow(String param1String) {
/*  376 */       this.value = param1String;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void writePool(Pool paramPool) throws PoolOverflow, StringOverflow {
/*  385 */     int i = this.poolbuf.length;
/*  386 */     this.poolbuf.appendChar(0);
/*  387 */     byte b = 1;
/*  388 */     while (b < paramPool.pp) {
/*  389 */       Object object = paramPool.pool[b];
/*  390 */       Assert.checkNonNull(object);
/*  391 */       if (object instanceof Pool.Method || object instanceof Pool.Variable) {
/*  392 */         object = ((Symbol.DelegatedSymbol)object).getUnderlyingSymbol();
/*      */       }
/*  394 */       if (object instanceof Symbol.MethodSymbol) {
/*  395 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)object;
/*  396 */         if (!methodSymbol.isDynamic()) {
/*  397 */           this.poolbuf.appendByte(((methodSymbol.owner.flags() & 0x200L) != 0L) ? 11 : 10);
/*      */
/*      */
/*  400 */           this.poolbuf.appendChar(paramPool.put(methodSymbol.owner));
/*  401 */           this.poolbuf.appendChar(paramPool.put(nameType((Symbol)methodSymbol)));
/*      */         } else {
/*      */
/*  404 */           Symbol.DynamicMethodSymbol dynamicMethodSymbol = (Symbol.DynamicMethodSymbol)methodSymbol;
/*  405 */           Pool.MethodHandle methodHandle = new Pool.MethodHandle(dynamicMethodSymbol.bsmKind, dynamicMethodSymbol.bsm, this.types);
/*  406 */           Pool.DynamicMethod dynamicMethod = new Pool.DynamicMethod(dynamicMethodSymbol, this.types);
/*  407 */           this.bootstrapMethods.put(dynamicMethod, methodHandle);
/*      */
/*  409 */           paramPool.put(this.names.BootstrapMethods);
/*  410 */           paramPool.put(methodHandle);
/*  411 */           for (Object object1 : dynamicMethodSymbol.staticArgs) {
/*  412 */             paramPool.put(object1);
/*      */           }
/*  414 */           this.poolbuf.appendByte(18);
/*  415 */           this.poolbuf.appendChar(this.bootstrapMethods.size() - 1);
/*  416 */           this.poolbuf.appendChar(paramPool.put(nameType((Symbol)dynamicMethodSymbol)));
/*      */         }
/*  418 */       } else if (object instanceof Symbol.VarSymbol) {
/*  419 */         Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)object;
/*  420 */         this.poolbuf.appendByte(9);
/*  421 */         this.poolbuf.appendChar(paramPool.put(varSymbol.owner));
/*  422 */         this.poolbuf.appendChar(paramPool.put(nameType((Symbol)varSymbol)));
/*  423 */       } else if (object instanceof Name) {
/*  424 */         this.poolbuf.appendByte(1);
/*  425 */         byte[] arrayOfByte = ((Name)object).toUtf();
/*  426 */         this.poolbuf.appendChar(arrayOfByte.length);
/*  427 */         this.poolbuf.appendBytes(arrayOfByte, 0, arrayOfByte.length);
/*  428 */         if (arrayOfByte.length > 65535)
/*  429 */           throw new StringOverflow(object.toString());
/*  430 */       } else if (object instanceof Symbol.ClassSymbol) {
/*  431 */         Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)object;
/*  432 */         if (classSymbol.owner.kind == 2) paramPool.put(classSymbol.owner);
/*  433 */         this.poolbuf.appendByte(7);
/*  434 */         if (classSymbol.type.hasTag(TypeTag.ARRAY)) {
/*  435 */           this.poolbuf.appendChar(paramPool.put(typeSig(classSymbol.type)));
/*      */         } else {
/*  437 */           this.poolbuf.appendChar(paramPool.put(this.names.fromUtf(externalize(classSymbol.flatname))));
/*  438 */           enterInner(classSymbol);
/*      */         }
/*  440 */       } else if (object instanceof NameAndType) {
/*  441 */         NameAndType nameAndType = (NameAndType)object;
/*  442 */         this.poolbuf.appendByte(12);
/*  443 */         this.poolbuf.appendChar(paramPool.put(nameAndType.name));
/*  444 */         this.poolbuf.appendChar(paramPool.put(typeSig(nameAndType.uniqueType.type)));
/*  445 */       } else if (object instanceof Integer) {
/*  446 */         this.poolbuf.appendByte(3);
/*  447 */         this.poolbuf.appendInt(((Integer)object).intValue());
/*  448 */       } else if (object instanceof Long) {
/*  449 */         this.poolbuf.appendByte(5);
/*  450 */         this.poolbuf.appendLong(((Long)object).longValue());
/*  451 */         b++;
/*  452 */       } else if (object instanceof Float) {
/*  453 */         this.poolbuf.appendByte(4);
/*  454 */         this.poolbuf.appendFloat(((Float)object).floatValue());
/*  455 */       } else if (object instanceof Double) {
/*  456 */         this.poolbuf.appendByte(6);
/*  457 */         this.poolbuf.appendDouble(((Double)object).doubleValue());
/*  458 */         b++;
/*  459 */       } else if (object instanceof String) {
/*  460 */         this.poolbuf.appendByte(8);
/*  461 */         this.poolbuf.appendChar(paramPool.put(this.names.fromString((String)object)));
/*  462 */       } else if (object instanceof Types.UniqueType) {
/*  463 */         Type type = ((Types.UniqueType)object).type;
/*  464 */         if (type instanceof Type.MethodType) {
/*  465 */           this.poolbuf.appendByte(16);
/*  466 */           this.poolbuf.appendChar(paramPool.put(typeSig(type)));
/*      */         } else {
/*  468 */           if (type.hasTag(TypeTag.CLASS)) enterInner((Symbol.ClassSymbol)type.tsym);
/*  469 */           this.poolbuf.appendByte(7);
/*  470 */           this.poolbuf.appendChar(paramPool.put(xClassName(type)));
/*      */         }
/*  472 */       } else if (object instanceof Pool.MethodHandle) {
/*  473 */         Pool.MethodHandle methodHandle = (Pool.MethodHandle)object;
/*  474 */         this.poolbuf.appendByte(15);
/*  475 */         this.poolbuf.appendByte(methodHandle.refKind);
/*  476 */         this.poolbuf.appendChar(paramPool.put(methodHandle.refSym));
/*      */       } else {
/*  478 */         Assert.error("writePool " + object);
/*      */       }
/*  480 */       b++;
/*      */     }
/*  482 */     if (paramPool.pp > 65535)
/*  483 */       throw new PoolOverflow();
/*  484 */     putChar(this.poolbuf, i, paramPool.pp);
/*      */   }
/*      */
/*      */
/*      */
/*      */   Name fieldName(Symbol paramSymbol) {
/*  490 */     if ((this.scramble && (paramSymbol.flags() & 0x2L) != 0L) || (this.scrambleAll && (paramSymbol
/*  491 */       .flags() & 0x5L) == 0L)) {
/*  492 */       return this.names.fromString("_$" + paramSymbol.name.getIndex());
/*      */     }
/*  494 */     return paramSymbol.name;
/*      */   }
/*      */
/*      */
/*      */
/*      */   NameAndType nameType(Symbol paramSymbol) {
/*  500 */     return new NameAndType(fieldName(paramSymbol), this.retrofit ? paramSymbol
/*      */
/*  502 */         .erasure(this.types) : paramSymbol
/*  503 */         .externalType(this.types), this.types);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   int writeAttr(Name paramName) {
/*  518 */     this.databuf.appendChar(this.pool.put(paramName));
/*  519 */     this.databuf.appendInt(0);
/*  520 */     return this.databuf.length;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void endAttr(int paramInt) {
/*  526 */     putInt(this.databuf, paramInt - 4, this.databuf.length - paramInt);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   int beginAttrs() {
/*  533 */     this.databuf.appendChar(0);
/*  534 */     return this.databuf.length;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void endAttrs(int paramInt1, int paramInt2) {
/*  540 */     putChar(this.databuf, paramInt1 - 2, paramInt2);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   int writeEnclosingMethodAttribute(Symbol.ClassSymbol paramClassSymbol) {
/*  547 */     if (!this.target.hasEnclosingMethodAttribute())
/*  548 */       return 0;
/*  549 */     return writeEnclosingMethodAttribute(this.names.EnclosingMethod, paramClassSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   protected int writeEnclosingMethodAttribute(Name paramName, Symbol.ClassSymbol paramClassSymbol) {
/*  556 */     if (paramClassSymbol.owner.kind != 16 && paramClassSymbol.name != this.names.empty)
/*      */     {
/*  558 */       return 0;
/*      */     }
/*  560 */     int i = writeAttr(paramName);
/*  561 */     Symbol.ClassSymbol classSymbol = paramClassSymbol.owner.enclClass();
/*  562 */     Symbol.MethodSymbol methodSymbol = (paramClassSymbol.owner.type == null || paramClassSymbol.owner.kind != 16) ? null : (Symbol.MethodSymbol)paramClassSymbol.owner;
/*      */
/*      */
/*      */
/*      */
/*  567 */     this.databuf.appendChar(this.pool.put(classSymbol));
/*  568 */     this.databuf.appendChar((methodSymbol == null) ? 0 : this.pool.put(nameType(paramClassSymbol.owner)));
/*  569 */     endAttr(i);
/*  570 */     return 1;
/*      */   }
/*      */
/*      */
/*      */
/*      */   int writeFlagAttrs(long paramLong) {
/*  576 */     byte b = 0;
/*  577 */     if ((paramLong & 0x20000L) != 0L) {
/*  578 */       int i = writeAttr(this.names.Deprecated);
/*  579 */       endAttr(i);
/*  580 */       b++;
/*      */     }
/*  582 */     if ((paramLong & 0x4000L) != 0L && !this.target.useEnumFlag()) {
/*  583 */       int i = writeAttr(this.names.Enum);
/*  584 */       endAttr(i);
/*  585 */       b++;
/*      */     }
/*  587 */     if ((paramLong & 0x1000L) != 0L && !this.target.useSyntheticFlag()) {
/*  588 */       int i = writeAttr(this.names.Synthetic);
/*  589 */       endAttr(i);
/*  590 */       b++;
/*      */     }
/*  592 */     if ((paramLong & 0x80000000L) != 0L && !this.target.useBridgeFlag()) {
/*  593 */       int i = writeAttr(this.names.Bridge);
/*  594 */       endAttr(i);
/*  595 */       b++;
/*      */     }
/*  597 */     if ((paramLong & 0x400000000L) != 0L && !this.target.useVarargsFlag()) {
/*  598 */       int i = writeAttr(this.names.Varargs);
/*  599 */       endAttr(i);
/*  600 */       b++;
/*      */     }
/*  602 */     if ((paramLong & 0x2000L) != 0L && !this.target.useAnnotationFlag()) {
/*  603 */       int i = writeAttr(this.names.Annotation);
/*  604 */       endAttr(i);
/*  605 */       b++;
/*      */     }
/*  607 */     return b;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   int writeMemberAttrs(Symbol paramSymbol) {
/*  614 */     int i = writeFlagAttrs(paramSymbol.flags());
/*  615 */     long l = paramSymbol.flags();
/*  616 */     if (this.source.allowGenerics() && (l & 0x80001000L) != 4096L && (l & 0x20000000L) == 0L && (
/*      */
/*      */
/*  619 */       !this.types.isSameType(paramSymbol.type, paramSymbol.erasure(this.types)) || this.signatureGen
/*  620 */       .hasTypeVar(paramSymbol.type.getThrownTypes()))) {
/*      */
/*      */
/*  623 */       int j = writeAttr(this.names.Signature);
/*  624 */       this.databuf.appendChar(this.pool.put(typeSig(paramSymbol.type)));
/*  625 */       endAttr(j);
/*  626 */       i++;
/*      */     }
/*  628 */     i += writeJavaAnnotations(paramSymbol.getRawAttributes());
/*  629 */     i += writeTypeAnnotations(paramSymbol.getRawTypeAttributes(), false);
/*  630 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   int writeMethodParametersAttr(Symbol.MethodSymbol paramMethodSymbol) {
/*  637 */     Type.MethodType methodType = paramMethodSymbol.externalType(this.types).asMethodType();
/*  638 */     int i = methodType.argtypes.size();
/*  639 */     if (paramMethodSymbol.params != null && i != 0) {
/*  640 */       int j = writeAttr(this.names.MethodParameters);
/*  641 */       this.databuf.appendByte(i);
/*      */
/*  643 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.extraParams) {
/*      */
/*      */
/*  646 */         int k = (int)varSymbol.flags() & 0x9010 | (int)paramMethodSymbol.flags() & 0x1000;
/*  647 */         this.databuf.appendChar(this.pool.put(varSymbol.name));
/*  648 */         this.databuf.appendChar(k);
/*      */       }
/*      */
/*  651 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.params) {
/*      */
/*      */
/*  654 */         int k = (int)varSymbol.flags() & 0x9010 | (int)paramMethodSymbol.flags() & 0x1000;
/*  655 */         this.databuf.appendChar(this.pool.put(varSymbol.name));
/*  656 */         this.databuf.appendChar(k);
/*      */       }
/*      */
/*  659 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.capturedLocals) {
/*      */
/*      */
/*  662 */         int k = (int)varSymbol.flags() & 0x9010 | (int)paramMethodSymbol.flags() & 0x1000;
/*  663 */         this.databuf.appendChar(this.pool.put(varSymbol.name));
/*  664 */         this.databuf.appendChar(k);
/*      */       }
/*  666 */       endAttr(j);
/*  667 */       return 1;
/*      */     }
/*  669 */     return 0;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   int writeParameterAttrs(Symbol.MethodSymbol paramMethodSymbol) {
/*  677 */     boolean bool1 = false;
/*  678 */     boolean bool2 = false;
/*  679 */     if (paramMethodSymbol.params != null) {
/*  680 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.params) {
/*  681 */         for (Attribute.Compound compound : varSymbol.getRawAttributes()) {
/*  682 */           switch (this.types.getRetention(compound)) {
/*      */             case JSR202:
/*  684 */               bool2 = true;
/*  685 */             case null: bool1 = true;
/*      */           }
/*      */
/*      */
/*      */         }
/*      */       }
/*      */     }
/*  692 */     byte b = 0;
/*  693 */     if (bool1) {
/*  694 */       int i = writeAttr(this.names.RuntimeVisibleParameterAnnotations);
/*  695 */       this.databuf.appendByte(paramMethodSymbol.params.length());
/*  696 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.params) {
/*  697 */         ListBuffer listBuffer = new ListBuffer();
/*  698 */         for (Attribute.Compound compound : varSymbol.getRawAttributes()) {
/*  699 */           if (this.types.getRetention(compound) == Attribute.RetentionPolicy.RUNTIME)
/*  700 */             listBuffer.append(compound);
/*  701 */         }  this.databuf.appendChar(listBuffer.length());
/*  702 */         for (Attribute.Compound compound : listBuffer)
/*  703 */           writeCompoundAttribute(compound);
/*      */       }
/*  705 */       endAttr(i);
/*  706 */       b++;
/*      */     }
/*  708 */     if (bool2) {
/*  709 */       int i = writeAttr(this.names.RuntimeInvisibleParameterAnnotations);
/*  710 */       this.databuf.appendByte(paramMethodSymbol.params.length());
/*  711 */       for (Symbol.VarSymbol varSymbol : paramMethodSymbol.params) {
/*  712 */         ListBuffer listBuffer = new ListBuffer();
/*  713 */         for (Attribute.Compound compound : varSymbol.getRawAttributes()) {
/*  714 */           if (this.types.getRetention(compound) == Attribute.RetentionPolicy.CLASS)
/*  715 */             listBuffer.append(compound);
/*  716 */         }  this.databuf.appendChar(listBuffer.length());
/*  717 */         for (Attribute.Compound compound : listBuffer)
/*  718 */           writeCompoundAttribute(compound);
/*      */       }
/*  720 */       endAttr(i);
/*  721 */       b++;
/*      */     }
/*  723 */     return b;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   int writeJavaAnnotations(List<Attribute.Compound> paramList) {
/*  734 */     if (paramList.isEmpty()) return 0;
/*  735 */     ListBuffer listBuffer1 = new ListBuffer();
/*  736 */     ListBuffer listBuffer2 = new ListBuffer();
/*  737 */     for (Attribute.Compound compound : paramList) {
/*  738 */       switch (this.types.getRetention(compound)) {
/*      */         case JSR202:
/*  740 */           listBuffer2.append(compound);
/*  741 */         case null: listBuffer1.append(compound);
/*      */       }
/*      */
/*      */
/*      */     }
/*  746 */     byte b = 0;
/*  747 */     if (listBuffer1.length() != 0) {
/*  748 */       int i = writeAttr(this.names.RuntimeVisibleAnnotations);
/*  749 */       this.databuf.appendChar(listBuffer1.length());
/*  750 */       for (Attribute.Compound compound : listBuffer1)
/*  751 */         writeCompoundAttribute(compound);
/*  752 */       endAttr(i);
/*  753 */       b++;
/*      */     }
/*  755 */     if (listBuffer2.length() != 0) {
/*  756 */       int i = writeAttr(this.names.RuntimeInvisibleAnnotations);
/*  757 */       this.databuf.appendChar(listBuffer2.length());
/*  758 */       for (Attribute.Compound compound : listBuffer2)
/*  759 */         writeCompoundAttribute(compound);
/*  760 */       endAttr(i);
/*  761 */       b++;
/*      */     }
/*  763 */     return b;
/*      */   }
/*      */
/*      */   int writeTypeAnnotations(List<Attribute.TypeCompound> paramList, boolean paramBoolean) {
/*  767 */     if (paramList.isEmpty()) return 0;
/*      */
/*  769 */     ListBuffer listBuffer1 = new ListBuffer();
/*  770 */     ListBuffer listBuffer2 = new ListBuffer();
/*      */
/*  772 */     for (Attribute.TypeCompound typeCompound : paramList) {
/*  773 */       if (typeCompound.hasUnknownPosition()) {
/*  774 */         boolean bool = typeCompound.tryFixPosition();
/*      */
/*      */
/*  777 */         if (!bool) {
/*      */
/*      */
/*      */
/*      */
/*  782 */           PrintWriter printWriter = this.log.getWriter(Log.WriterKind.ERROR);
/*  783 */           printWriter.println("ClassWriter: Position UNKNOWN in type annotation: " + typeCompound);
/*      */
/*      */           continue;
/*      */         }
/*      */       }
/*  788 */       if (typeCompound.position.type.isLocal() != paramBoolean)
/*      */         continue;
/*  790 */       if (!typeCompound.position.emitToClassfile())
/*      */         continue;
/*  792 */       switch (this.types.getRetention((Attribute.Compound)typeCompound)) {
/*      */         case JSR202:
/*  794 */           listBuffer2.append(typeCompound);
/*  795 */         case null: listBuffer1.append(typeCompound);
/*      */       }
/*      */
/*      */
/*      */     }
/*  800 */     byte b = 0;
/*  801 */     if (listBuffer1.length() != 0) {
/*  802 */       int i = writeAttr(this.names.RuntimeVisibleTypeAnnotations);
/*  803 */       this.databuf.appendChar(listBuffer1.length());
/*  804 */       for (Attribute.TypeCompound typeCompound : listBuffer1)
/*  805 */         writeTypeAnnotation(typeCompound);
/*  806 */       endAttr(i);
/*  807 */       b++;
/*      */     }
/*      */
/*  810 */     if (listBuffer2.length() != 0) {
/*  811 */       int i = writeAttr(this.names.RuntimeInvisibleTypeAnnotations);
/*  812 */       this.databuf.appendChar(listBuffer2.length());
/*  813 */       for (Attribute.TypeCompound typeCompound : listBuffer2)
/*  814 */         writeTypeAnnotation(typeCompound);
/*  815 */       endAttr(i);
/*  816 */       b++;
/*      */     }
/*      */
/*  819 */     return b;
/*      */   }
/*      */
/*      */
/*      */   class AttributeWriter
/*      */     implements Attribute.Visitor
/*      */   {
/*      */     public void visitConstant(Attribute.Constant param1Constant) {
/*  827 */       Object object = param1Constant.value;
/*  828 */       switch (param1Constant.type.getTag()) {
/*      */         case null:
/*  830 */           ClassWriter.this.databuf.appendByte(66);
/*      */           break;
/*      */         case null:
/*  833 */           ClassWriter.this.databuf.appendByte(67);
/*      */           break;
/*      */         case null:
/*  836 */           ClassWriter.this.databuf.appendByte(83);
/*      */           break;
/*      */         case null:
/*  839 */           ClassWriter.this.databuf.appendByte(73);
/*      */           break;
/*      */         case null:
/*  842 */           ClassWriter.this.databuf.appendByte(74);
/*      */           break;
/*      */         case null:
/*  845 */           ClassWriter.this.databuf.appendByte(70);
/*      */           break;
/*      */         case null:
/*  848 */           ClassWriter.this.databuf.appendByte(68);
/*      */           break;
/*      */         case null:
/*  851 */           ClassWriter.this.databuf.appendByte(90);
/*      */           break;
/*      */         case null:
/*  854 */           Assert.check(object instanceof String);
/*  855 */           ClassWriter.this.databuf.appendByte(115);
/*  856 */           object = ClassWriter.this.names.fromString(object.toString());
/*      */           break;
/*      */         default:
/*  859 */           throw new AssertionError(param1Constant.type);
/*      */       }
/*  861 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(object));
/*      */     }
/*      */     public void visitEnum(Attribute.Enum param1Enum) {
/*  864 */       ClassWriter.this.databuf.appendByte(101);
/*  865 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(ClassWriter.this.typeSig(param1Enum.value.type)));
/*  866 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(param1Enum.value.name));
/*      */     }
/*      */     public void visitClass(Attribute.Class param1Class) {
/*  869 */       ClassWriter.this.databuf.appendByte(99);
/*  870 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(ClassWriter.this.typeSig(param1Class.classType)));
/*      */     }
/*      */     public void visitCompound(Attribute.Compound param1Compound) {
/*  873 */       ClassWriter.this.databuf.appendByte(64);
/*  874 */       ClassWriter.this.writeCompoundAttribute(param1Compound);
/*      */     }
/*      */     public void visitError(Attribute.Error param1Error) {
/*  877 */       throw new AssertionError(param1Error);
/*      */     }
/*      */     public void visitArray(Attribute.Array param1Array) {
/*  880 */       ClassWriter.this.databuf.appendByte(91);
/*  881 */       ClassWriter.this.databuf.appendChar(param1Array.values.length);
/*  882 */       for (Attribute attribute : param1Array.values)
/*  883 */         attribute.accept(this);
/*      */     }
/*      */   }
/*      */
/*  887 */   protected ClassWriter(Context paramContext) { this.awriter = new AttributeWriter(); paramContext.put(classWriterKey, this); this.log = Log.instance(paramContext); this.names = Names.instance(paramContext); this.options = Options.instance(paramContext); this.target = Target.instance(paramContext); this.source = Source.instance(paramContext); this.types = Types.instance(paramContext); this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class); this.signatureGen = new CWSignatureGenerator(this.types); this.verbose = this.options.isSet(Option.VERBOSE); this.scramble = this.options.isSet("-scramble"); this.scrambleAll = this.options.isSet("-scrambleAll"); this.retrofit = this.options.isSet("-retrofit"); this.genCrt = this.options.isSet(Option.XJCOV); this.debugstackmap = this.options.isSet("debugstackmap"); this.emitSourceFile = (this.options.isUnset(Option.G_CUSTOM) || this.options.isSet(Option.G_CUSTOM, "source")); String str = this.options.get("dumpmodifiers");
/*      */     this.dumpClassModifiers = (str != null && str.indexOf('c') != -1);
/*      */     this.dumpFieldModifiers = (str != null && str.indexOf('f') != -1);
/*      */     this.dumpInnerClassModifiers = (str != null && str.indexOf('i') != -1);
/*  891 */     this.dumpMethodModifiers = (str != null && str.indexOf('m') != -1); } void writeCompoundAttribute(Attribute.Compound paramCompound) { this.databuf.appendChar(this.pool.put(typeSig(paramCompound.type)));
/*  892 */     this.databuf.appendChar(paramCompound.values.length());
/*  893 */     for (Pair pair : paramCompound.values) {
/*  894 */       this.databuf.appendChar(this.pool.put(((Symbol.MethodSymbol)pair.fst).name));
/*  895 */       ((Attribute)pair.snd).accept(this.awriter);
/*      */     }  }
/*      */
/*      */
/*      */   void writeTypeAnnotation(Attribute.TypeCompound paramTypeCompound) {
/*  900 */     writePosition(paramTypeCompound.position);
/*  901 */     writeCompoundAttribute((Attribute.Compound)paramTypeCompound);
/*      */   }
/*      */   void writePosition(TypeAnnotationPosition paramTypeAnnotationPosition) {
/*      */     byte b;
/*  905 */     this.databuf.appendByte(paramTypeAnnotationPosition.type.targetTypeValue());
/*  906 */     switch (paramTypeAnnotationPosition.type) {
/*      */
/*      */
/*      */
/*      */       case CLDC:
/*      */       case JSR202:
/*      */       case null:
/*      */       case null:
/*  914 */         this.databuf.appendChar(paramTypeAnnotationPosition.offset);
/*      */         break;
/*      */
/*      */
/*      */       case null:
/*      */       case null:
/*  920 */         this.databuf.appendChar(paramTypeAnnotationPosition.lvarOffset.length);
/*  921 */         for (b = 0; b < paramTypeAnnotationPosition.lvarOffset.length; b++) {
/*  922 */           this.databuf.appendChar(paramTypeAnnotationPosition.lvarOffset[b]);
/*  923 */           this.databuf.appendChar(paramTypeAnnotationPosition.lvarLength[b]);
/*  924 */           this.databuf.appendChar(paramTypeAnnotationPosition.lvarIndex[b]);
/*      */         }
/*      */         break;
/*      */
/*      */       case null:
/*  929 */         this.databuf.appendChar(paramTypeAnnotationPosition.exception_index);
/*      */         break;
/*      */
/*      */
/*      */       case null:
/*      */         break;
/*      */
/*      */       case null:
/*      */       case null:
/*  938 */         this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*      */         break;
/*      */
/*      */       case null:
/*      */       case null:
/*  943 */         this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*  944 */         this.databuf.appendByte(paramTypeAnnotationPosition.bound_index);
/*      */         break;
/*      */
/*      */       case null:
/*  948 */         this.databuf.appendChar(paramTypeAnnotationPosition.type_index);
/*      */         break;
/*      */
/*      */       case null:
/*  952 */         this.databuf.appendChar(paramTypeAnnotationPosition.type_index);
/*      */         break;
/*      */
/*      */       case null:
/*  956 */         this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*      */         break;
/*      */
/*      */
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*  965 */         this.databuf.appendChar(paramTypeAnnotationPosition.offset);
/*  966 */         this.databuf.appendByte(paramTypeAnnotationPosition.type_index);
/*      */         break;
/*      */
/*      */       case null:
/*      */       case null:
/*      */         break;
/*      */       case null:
/*  973 */         throw new AssertionError("jvm.ClassWriter: UNKNOWN target type should never occur!");
/*      */       default:
/*  975 */         throw new AssertionError("jvm.ClassWriter: Unknown target type for position: " + paramTypeAnnotationPosition);
/*      */     }
/*      */
/*      */
/*  979 */     this.databuf.appendByte(paramTypeAnnotationPosition.location.size());
/*  980 */     List list = TypeAnnotationPosition.getBinaryFromTypePath((List)paramTypeAnnotationPosition.location);
/*  981 */     for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  982 */       this.databuf.appendByte((byte)i); }
/*      */
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void enterInner(Symbol.ClassSymbol paramClassSymbol) {
/*  993 */     if (paramClassSymbol.type.isCompound()) {
/*  994 */       throw new AssertionError("Unexpected intersection type: " + paramClassSymbol.type);
/*      */     }
/*      */     try {
/*  997 */       paramClassSymbol.complete();
/*  998 */     } catch (Symbol.CompletionFailure completionFailure) {
/*  999 */       System.err.println("error: " + paramClassSymbol + ": " + completionFailure.getMessage());
/* 1000 */       throw completionFailure;
/*      */     }
/* 1002 */     if (!paramClassSymbol.type.hasTag(TypeTag.CLASS))
/* 1003 */       return;  if (this.pool != null && paramClassSymbol.owner
/* 1004 */       .enclClass() != null && (this.innerClasses == null ||
/* 1005 */       !this.innerClasses.contains(paramClassSymbol))) {
/*      */
/* 1007 */       enterInner(paramClassSymbol.owner.enclClass());
/* 1008 */       this.pool.put(paramClassSymbol);
/* 1009 */       if (paramClassSymbol.name != this.names.empty)
/* 1010 */         this.pool.put(paramClassSymbol.name);
/* 1011 */       if (this.innerClasses == null) {
/* 1012 */         this.innerClasses = new HashSet<>();
/* 1013 */         this.innerClassesQueue = new ListBuffer();
/* 1014 */         this.pool.put(this.names.InnerClasses);
/*      */       }
/* 1016 */       this.innerClasses.add(paramClassSymbol);
/* 1017 */       this.innerClassesQueue.append(paramClassSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeInnerClasses() {
/* 1024 */     int i = writeAttr(this.names.InnerClasses);
/* 1025 */     this.databuf.appendChar(this.innerClassesQueue.length());
/* 1026 */     List list = this.innerClassesQueue.toList();
/* 1027 */     for (; list.nonEmpty();
/* 1028 */       list = list.tail) {
/* 1029 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)list.head;
/* 1030 */       classSymbol.markAbstractIfNeeded(this.types);
/* 1031 */       char c = (char)adjustFlags(classSymbol.flags_field);
/* 1032 */       if ((c & 0x200) != 0) c = (char)(c | 0x400);
/* 1033 */       if (classSymbol.name.isEmpty()) c = (char)(c & 0xFFFFFFEF);
/* 1034 */       c = (char)(c & 0xFFFFF7FF);
/* 1035 */       if (this.dumpInnerClassModifiers) {
/* 1036 */         PrintWriter printWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1037 */         printWriter.println("INNERCLASS  " + classSymbol.name);
/* 1038 */         printWriter.println("---" + flagNames(c));
/*      */       }
/* 1040 */       this.databuf.appendChar(this.pool.get(classSymbol));
/* 1041 */       this.databuf.appendChar((classSymbol.owner.kind == 2 &&
/* 1042 */           !classSymbol.name.isEmpty()) ? this.pool.get(classSymbol.owner) : 0);
/* 1043 */       this.databuf.appendChar(
/* 1044 */           !classSymbol.name.isEmpty() ? this.pool.get(classSymbol.name) : 0);
/* 1045 */       this.databuf.appendChar(c);
/*      */     }
/* 1047 */     endAttr(i);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeBootstrapMethods() {
/* 1053 */     int i = writeAttr(this.names.BootstrapMethods);
/* 1054 */     this.databuf.appendChar(this.bootstrapMethods.size());
/* 1055 */     for (Map.Entry<Pool.DynamicMethod, Pool.MethodHandle> entry : this.bootstrapMethods.entrySet()) {
/* 1056 */       Pool.DynamicMethod dynamicMethod = (Pool.DynamicMethod)entry.getKey();
/* 1057 */       Symbol.DynamicMethodSymbol dynamicMethodSymbol = (Symbol.DynamicMethodSymbol)dynamicMethod.baseSymbol();
/*      */
/* 1059 */       this.databuf.appendChar(this.pool.get(entry.getValue()));
/*      */
/* 1061 */       this.databuf.appendChar(dynamicMethodSymbol.staticArgs.length);
/*      */
/* 1063 */       Object[] arrayOfObject = dynamicMethod.uniqueStaticArgs;
/* 1064 */       for (Object object : arrayOfObject) {
/* 1065 */         this.databuf.appendChar(this.pool.get(object));
/*      */       }
/*      */     }
/* 1068 */     endAttr(i);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeField(Symbol.VarSymbol paramVarSymbol) {
/* 1074 */     int i = adjustFlags(paramVarSymbol.flags());
/* 1075 */     this.databuf.appendChar(i);
/* 1076 */     if (this.dumpFieldModifiers) {
/* 1077 */       PrintWriter printWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1078 */       printWriter.println("FIELD  " + fieldName((Symbol)paramVarSymbol));
/* 1079 */       printWriter.println("---" + flagNames(paramVarSymbol.flags()));
/*      */     }
/* 1081 */     this.databuf.appendChar(this.pool.put(fieldName((Symbol)paramVarSymbol)));
/* 1082 */     this.databuf.appendChar(this.pool.put(typeSig(paramVarSymbol.erasure(this.types))));
/* 1083 */     int j = beginAttrs();
/* 1084 */     int k = 0;
/* 1085 */     if (paramVarSymbol.getConstValue() != null) {
/* 1086 */       int m = writeAttr(this.names.ConstantValue);
/* 1087 */       this.databuf.appendChar(this.pool.put(paramVarSymbol.getConstValue()));
/* 1088 */       endAttr(m);
/* 1089 */       k++;
/*      */     }
/* 1091 */     k += writeMemberAttrs((Symbol)paramVarSymbol);
/* 1092 */     endAttrs(j, k);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeMethod(Symbol.MethodSymbol paramMethodSymbol) {
/* 1098 */     int i = adjustFlags(paramMethodSymbol.flags());
/* 1099 */     this.databuf.appendChar(i);
/* 1100 */     if (this.dumpMethodModifiers) {
/* 1101 */       PrintWriter printWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1102 */       printWriter.println("METHOD  " + fieldName((Symbol)paramMethodSymbol));
/* 1103 */       printWriter.println("---" + flagNames(paramMethodSymbol.flags()));
/*      */     }
/* 1105 */     this.databuf.appendChar(this.pool.put(fieldName((Symbol)paramMethodSymbol)));
/* 1106 */     this.databuf.appendChar(this.pool.put(typeSig(paramMethodSymbol.externalType(this.types))));
/* 1107 */     int j = beginAttrs();
/* 1108 */     int k = 0;
/* 1109 */     if (paramMethodSymbol.code != null) {
/* 1110 */       int m = writeAttr(this.names.Code);
/* 1111 */       writeCode(paramMethodSymbol.code);
/* 1112 */       paramMethodSymbol.code = null;
/* 1113 */       endAttr(m);
/* 1114 */       k++;
/*      */     }
/* 1116 */     List list = paramMethodSymbol.erasure(this.types).getThrownTypes();
/* 1117 */     if (list.nonEmpty()) {
/* 1118 */       int m = writeAttr(this.names.Exceptions);
/* 1119 */       this.databuf.appendChar(list.length());
/* 1120 */       for (List list1 = list; list1.nonEmpty(); list1 = list1.tail)
/* 1121 */         this.databuf.appendChar(this.pool.put(((Type)list1.head).tsym));
/* 1122 */       endAttr(m);
/* 1123 */       k++;
/*      */     }
/* 1125 */     if (paramMethodSymbol.defaultValue != null) {
/* 1126 */       int m = writeAttr(this.names.AnnotationDefault);
/* 1127 */       paramMethodSymbol.defaultValue.accept(this.awriter);
/* 1128 */       endAttr(m);
/* 1129 */       k++;
/*      */     }
/* 1131 */     if (this.options.isSet(Option.PARAMETERS))
/* 1132 */       k += writeMethodParametersAttr(paramMethodSymbol);
/* 1133 */     k += writeMemberAttrs((Symbol)paramMethodSymbol);
/* 1134 */     k += writeParameterAttrs(paramMethodSymbol);
/* 1135 */     endAttrs(j, k);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeCode(Code paramCode) {
/* 1141 */     this.databuf.appendChar(paramCode.max_stack);
/* 1142 */     this.databuf.appendChar(paramCode.max_locals);
/* 1143 */     this.databuf.appendInt(paramCode.cp);
/* 1144 */     this.databuf.appendBytes(paramCode.code, 0, paramCode.cp);
/* 1145 */     this.databuf.appendChar(paramCode.catchInfo.length());
/* 1146 */     List list = paramCode.catchInfo.toList();
/* 1147 */     for (; list.nonEmpty();
/* 1148 */       list = list.tail) {
/* 1149 */       for (byte b = 0; b < ((char[])list.head).length; b++)
/* 1150 */         this.databuf.appendChar(((char[])list.head)[b]);
/*      */     }
/* 1152 */     int i = beginAttrs();
/* 1153 */     int j = 0;
/*      */
/* 1155 */     if (paramCode.lineInfo.nonEmpty()) {
/* 1156 */       int k = writeAttr(this.names.LineNumberTable);
/* 1157 */       this.databuf.appendChar(paramCode.lineInfo.length());
/* 1158 */       List list1 = paramCode.lineInfo.reverse();
/* 1159 */       for (; list1.nonEmpty();
/* 1160 */         list1 = list1.tail) {
/* 1161 */         for (byte b = 0; b < ((char[])list1.head).length; b++)
/* 1162 */           this.databuf.appendChar(((char[])list1.head)[b]);
/* 1163 */       }  endAttr(k);
/* 1164 */       j++;
/*      */     }
/*      */
/* 1167 */     if (this.genCrt && paramCode.crt != null) {
/* 1168 */       CRTable cRTable = paramCode.crt;
/* 1169 */       int k = writeAttr(this.names.CharacterRangeTable);
/* 1170 */       int m = beginAttrs();
/* 1171 */       int n = cRTable.writeCRT(this.databuf, paramCode.lineMap, this.log);
/* 1172 */       endAttrs(m, n);
/* 1173 */       endAttr(k);
/* 1174 */       j++;
/*      */     }
/*      */
/*      */
/* 1178 */     if (paramCode.varDebugInfo && paramCode.varBufferSize > 0) {
/* 1179 */       byte b1 = 0;
/* 1180 */       int k = writeAttr(this.names.LocalVariableTable);
/* 1181 */       this.databuf.appendChar(paramCode.getLVTSize()); byte b2;
/* 1182 */       for (b2 = 0; b2 < paramCode.varBufferSize; b2++) {
/* 1183 */         Code.LocalVar localVar = paramCode.varBuffer[b2];
/*      */
/* 1185 */         for (Code.LocalVar.Range range : localVar.aliveRanges) {
/*      */
/* 1187 */           Assert.check((range.start_pc >= '\000' && range.start_pc <= paramCode.cp));
/*      */
/* 1189 */           this.databuf.appendChar(range.start_pc);
/* 1190 */           Assert.check((range.length > '\000' && range.start_pc + range.length <= paramCode.cp));
/*      */
/* 1192 */           this.databuf.appendChar(range.length);
/* 1193 */           Symbol.VarSymbol varSymbol = localVar.sym;
/* 1194 */           this.databuf.appendChar(this.pool.put(varSymbol.name));
/* 1195 */           Type type = varSymbol.erasure(this.types);
/* 1196 */           this.databuf.appendChar(this.pool.put(typeSig(type)));
/* 1197 */           this.databuf.appendChar(localVar.reg);
/* 1198 */           if (needsLocalVariableTypeEntry(localVar.sym.type)) {
/* 1199 */             b1++;
/*      */           }
/*      */         }
/*      */       }
/* 1203 */       endAttr(k);
/* 1204 */       j++;
/*      */
/* 1206 */       if (b1 > 0) {
/* 1207 */         k = writeAttr(this.names.LocalVariableTypeTable);
/* 1208 */         this.databuf.appendChar(b1);
/* 1209 */         b2 = 0;
/*      */
/* 1211 */         for (byte b = 0; b < paramCode.varBufferSize; b++) {
/* 1212 */           Code.LocalVar localVar = paramCode.varBuffer[b];
/* 1213 */           Symbol.VarSymbol varSymbol = localVar.sym;
/* 1214 */           if (needsLocalVariableTypeEntry(varSymbol.type))
/*      */           {
/* 1216 */             for (Code.LocalVar.Range range : localVar.aliveRanges) {
/*      */
/* 1218 */               this.databuf.appendChar(range.start_pc);
/* 1219 */               this.databuf.appendChar(range.length);
/* 1220 */               this.databuf.appendChar(this.pool.put(varSymbol.name));
/* 1221 */               this.databuf.appendChar(this.pool.put(typeSig(varSymbol.type)));
/* 1222 */               this.databuf.appendChar(localVar.reg);
/* 1223 */               b2++;
/*      */             }  }
/*      */         }
/* 1226 */         Assert.check((b2 == b1));
/* 1227 */         endAttr(k);
/* 1228 */         j++;
/*      */       }
/*      */     }
/*      */
/* 1232 */     if (paramCode.stackMapBufferSize > 0) {
/* 1233 */       if (this.debugstackmap) System.out.println("Stack map for " + paramCode.meth);
/* 1234 */       int k = writeAttr(paramCode.stackMap.getAttributeName(this.names));
/* 1235 */       writeStackMap(paramCode);
/* 1236 */       endAttr(k);
/* 1237 */       j++;
/*      */     }
/*      */
/* 1240 */     j += writeTypeAnnotations(paramCode.meth.getRawTypeAttributes(), true);
/*      */
/* 1242 */     endAttrs(i, j);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean needsLocalVariableTypeEntry(Type paramType) {
/* 1249 */     return (!this.types.isSameType(paramType, this.types.erasure(paramType)) &&
/* 1250 */       !paramType.isCompound());
/*      */   }
/*      */   void writeStackMap(Code paramCode) {
/*      */     byte b;
/* 1254 */     int i = paramCode.stackMapBufferSize;
/* 1255 */     if (this.debugstackmap) System.out.println(" nframes = " + i);
/* 1256 */     this.databuf.appendChar(i);
/*      */
/* 1258 */     switch (paramCode.stackMap) {
/*      */       case CLDC:
/* 1260 */         for (b = 0; b < i; b++) {
/* 1261 */           if (this.debugstackmap) System.out.print("  " + b + ":");
/* 1262 */           Code.StackMapFrame stackMapFrame = paramCode.stackMapBuffer[b];
/*      */
/*      */
/* 1265 */           if (this.debugstackmap) System.out.print(" pc=" + stackMapFrame.pc);
/* 1266 */           this.databuf.appendChar(stackMapFrame.pc);
/*      */
/*      */
/* 1269 */           byte b1 = 0; int j;
/* 1270 */           for (j = 0; j < stackMapFrame.locals.length;
/* 1271 */             j += this.target.generateEmptyAfterBig() ? 1 : Code.width(stackMapFrame.locals[j])) {
/* 1272 */             b1++;
/*      */           }
/* 1274 */           if (this.debugstackmap) System.out.print(" nlocals=" + b1);
/*      */
/* 1276 */           this.databuf.appendChar(b1);
/* 1277 */           for (j = 0; j < stackMapFrame.locals.length;
/* 1278 */             j += this.target.generateEmptyAfterBig() ? 1 : Code.width(stackMapFrame.locals[j])) {
/* 1279 */             if (this.debugstackmap) System.out.print(" local[" + j + "]=");
/* 1280 */             writeStackMapType(stackMapFrame.locals[j]);
/*      */           }
/*      */
/*      */
/* 1284 */           j = 0; int k;
/* 1285 */           for (k = 0; k < stackMapFrame.stack.length;
/* 1286 */             k += this.target.generateEmptyAfterBig() ? 1 : Code.width(stackMapFrame.stack[k])) {
/* 1287 */             j++;
/*      */           }
/* 1289 */           if (this.debugstackmap) System.out.print(" nstack=" + j);
/*      */
/* 1291 */           this.databuf.appendChar(j);
/* 1292 */           for (k = 0; k < stackMapFrame.stack.length;
/* 1293 */             k += this.target.generateEmptyAfterBig() ? 1 : Code.width(stackMapFrame.stack[k])) {
/* 1294 */             if (this.debugstackmap) System.out.print(" stack[" + k + "]=");
/* 1295 */             writeStackMapType(stackMapFrame.stack[k]);
/*      */           }
/* 1297 */           if (this.debugstackmap) System.out.println();
/*      */         }
/*      */         return;
/*      */       case JSR202:
/* 1301 */         Assert.checkNull(paramCode.stackMapBuffer);
/* 1302 */         for (b = 0; b < i; b++) {
/* 1303 */           if (this.debugstackmap) System.out.print("  " + b + ":");
/* 1304 */           StackMapTableFrame stackMapTableFrame = paramCode.stackMapTableBuffer[b];
/* 1305 */           stackMapTableFrame.write(this);
/* 1306 */           if (this.debugstackmap) System.out.println();
/*      */
/*      */         }
/*      */         return;
/*      */     }
/* 1311 */     throw new AssertionError("Unexpected stackmap format value");
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeStackMapType(Type paramType) {
/* 1317 */     if (paramType == null) {
/* 1318 */       if (this.debugstackmap) System.out.print("empty");
/* 1319 */       this.databuf.appendByte(0);
/*      */     } else {
/* 1321 */       UninitializedType uninitializedType; switch (paramType.getTag()) {
/*      */         case null:
/*      */         case null:
/*      */         case null:
/*      */         case null:
/*      */         case null:
/* 1327 */           if (this.debugstackmap) System.out.print("int");
/* 1328 */           this.databuf.appendByte(1);
/*      */           return;
/*      */         case null:
/* 1331 */           if (this.debugstackmap) System.out.print("float");
/* 1332 */           this.databuf.appendByte(2);
/*      */           return;
/*      */         case null:
/* 1335 */           if (this.debugstackmap) System.out.print("double");
/* 1336 */           this.databuf.appendByte(3);
/*      */           return;
/*      */         case null:
/* 1339 */           if (this.debugstackmap) System.out.print("long");
/* 1340 */           this.databuf.appendByte(4);
/*      */           return;
/*      */         case null:
/* 1343 */           if (this.debugstackmap) System.out.print("null");
/* 1344 */           this.databuf.appendByte(5);
/*      */           return;
/*      */         case null:
/*      */         case null:
/* 1348 */           if (this.debugstackmap) System.out.print("object(" + paramType + ")");
/* 1349 */           this.databuf.appendByte(7);
/* 1350 */           this.databuf.appendChar(this.pool.put(paramType));
/*      */           return;
/*      */         case null:
/* 1353 */           if (this.debugstackmap) System.out.print("object(" + (this.types.erasure(paramType)).tsym + ")");
/* 1354 */           this.databuf.appendByte(7);
/* 1355 */           this.databuf.appendChar(this.pool.put((this.types.erasure(paramType)).tsym));
/*      */           return;
/*      */         case CLDC:
/* 1358 */           if (this.debugstackmap) System.out.print("uninit_this");
/* 1359 */           this.databuf.appendByte(6);
/*      */           return;
/*      */         case JSR202:
/* 1362 */           uninitializedType = (UninitializedType)paramType;
/* 1363 */           this.databuf.appendByte(8);
/* 1364 */           if (this.debugstackmap) System.out.print("uninit_object@" + uninitializedType.offset);
/* 1365 */           this.databuf.appendChar(uninitializedType.offset);
/*      */           return;
/*      */       }
/*      */
/* 1369 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */
/*      */   static abstract class StackMapTableFrame
/*      */   {
/*      */     abstract int getFrameType();
/*      */
/*      */     void write(ClassWriter param1ClassWriter) {
/* 1378 */       int i = getFrameType();
/* 1379 */       param1ClassWriter.databuf.appendByte(i);
/* 1380 */       if (param1ClassWriter.debugstackmap) System.out.print(" frame_type=" + i);
/*      */     }
/*      */
/*      */     static class SameFrame extends StackMapTableFrame { final int offsetDelta;
/*      */
/*      */       SameFrame(int param2Int) {
/* 1386 */         this.offsetDelta = param2Int;
/*      */       }
/*      */       int getFrameType() {
/* 1389 */         return (this.offsetDelta < 64) ? this.offsetDelta : 251;
/*      */       }
/*      */
/*      */       void write(ClassWriter param2ClassWriter) {
/* 1393 */         super.write(param2ClassWriter);
/* 1394 */         if (getFrameType() == 251) {
/* 1395 */           param2ClassWriter.databuf.appendChar(this.offsetDelta);
/* 1396 */           if (param2ClassWriter.debugstackmap)
/* 1397 */             System.out.print(" offset_delta=" + this.offsetDelta);
/*      */         }
/*      */       } }
/*      */
/*      */
/*      */     static class SameLocals1StackItemFrame extends StackMapTableFrame {
/*      */       final int offsetDelta;
/*      */       final Type stack;
/*      */
/*      */       SameLocals1StackItemFrame(int param2Int, Type param2Type) {
/* 1407 */         this.offsetDelta = param2Int;
/* 1408 */         this.stack = param2Type;
/*      */       }
/*      */       int getFrameType() {
/* 1411 */         return (this.offsetDelta < 64) ? (64 + this.offsetDelta) : 247;
/*      */       }
/*      */
/*      */
/*      */
/*      */       void write(ClassWriter param2ClassWriter) {
/* 1417 */         super.write(param2ClassWriter);
/* 1418 */         if (getFrameType() == 247) {
/* 1419 */           param2ClassWriter.databuf.appendChar(this.offsetDelta);
/* 1420 */           if (param2ClassWriter.debugstackmap) {
/* 1421 */             System.out.print(" offset_delta=" + this.offsetDelta);
/*      */           }
/*      */         }
/* 1424 */         if (param2ClassWriter.debugstackmap) {
/* 1425 */           System.out.print(" stack[0]=");
/*      */         }
/* 1427 */         param2ClassWriter.writeStackMapType(this.stack);
/*      */       } }
/*      */
/*      */     static class ChopFrame extends StackMapTableFrame {
/*      */       final int frameType;
/*      */       final int offsetDelta;
/*      */
/*      */       ChopFrame(int param2Int1, int param2Int2) {
/* 1435 */         this.frameType = param2Int1;
/* 1436 */         this.offsetDelta = param2Int2;
/*      */       } int getFrameType() {
/* 1438 */         return this.frameType;
/*      */       }
/*      */       void write(ClassWriter param2ClassWriter) {
/* 1441 */         super.write(param2ClassWriter);
/* 1442 */         param2ClassWriter.databuf.appendChar(this.offsetDelta);
/* 1443 */         if (param2ClassWriter.debugstackmap)
/* 1444 */           System.out.print(" offset_delta=" + this.offsetDelta);
/*      */       }
/*      */     }
/*      */
/*      */     static class AppendFrame extends StackMapTableFrame {
/*      */       final int frameType;
/*      */       final int offsetDelta;
/*      */       final Type[] locals;
/*      */
/*      */       AppendFrame(int param2Int1, int param2Int2, Type[] param2ArrayOfType) {
/* 1454 */         this.frameType = param2Int1;
/* 1455 */         this.offsetDelta = param2Int2;
/* 1456 */         this.locals = param2ArrayOfType;
/*      */       } int getFrameType() {
/* 1458 */         return this.frameType;
/*      */       }
/*      */       void write(ClassWriter param2ClassWriter) {
/* 1461 */         super.write(param2ClassWriter);
/* 1462 */         param2ClassWriter.databuf.appendChar(this.offsetDelta);
/* 1463 */         if (param2ClassWriter.debugstackmap) {
/* 1464 */           System.out.print(" offset_delta=" + this.offsetDelta);
/*      */         }
/* 1466 */         for (byte b = 0; b < this.locals.length; b++) {
/* 1467 */           if (param2ClassWriter.debugstackmap) System.out.print(" locals[" + b + "]=");
/* 1468 */           param2ClassWriter.writeStackMapType(this.locals[b]);
/*      */         }
/*      */       } }
/*      */
/*      */     static class FullFrame extends StackMapTableFrame {
/*      */       final int offsetDelta;
/*      */       final Type[] locals;
/*      */       final Type[] stack;
/*      */
/*      */       FullFrame(int param2Int, Type[] param2ArrayOfType1, Type[] param2ArrayOfType2) {
/* 1478 */         this.offsetDelta = param2Int;
/* 1479 */         this.locals = param2ArrayOfType1;
/* 1480 */         this.stack = param2ArrayOfType2;
/*      */       } int getFrameType() {
/* 1482 */         return 255;
/*      */       }
/*      */       void write(ClassWriter param2ClassWriter) {
/* 1485 */         super.write(param2ClassWriter);
/* 1486 */         param2ClassWriter.databuf.appendChar(this.offsetDelta);
/* 1487 */         param2ClassWriter.databuf.appendChar(this.locals.length);
/* 1488 */         if (param2ClassWriter.debugstackmap) {
/* 1489 */           System.out.print(" offset_delta=" + this.offsetDelta);
/* 1490 */           System.out.print(" nlocals=" + this.locals.length);
/*      */         }  byte b;
/* 1492 */         for (b = 0; b < this.locals.length; b++) {
/* 1493 */           if (param2ClassWriter.debugstackmap) System.out.print(" locals[" + b + "]=");
/* 1494 */           param2ClassWriter.writeStackMapType(this.locals[b]);
/*      */         }
/*      */
/* 1497 */         param2ClassWriter.databuf.appendChar(this.stack.length);
/* 1498 */         if (param2ClassWriter.debugstackmap) System.out.print(" nstack=" + this.stack.length);
/* 1499 */         for (b = 0; b < this.stack.length; b++) {
/* 1500 */           if (param2ClassWriter.debugstackmap) System.out.print(" stack[" + b + "]=");
/* 1501 */           param2ClassWriter.writeStackMapType(this.stack[b]);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static StackMapTableFrame getInstance(Code.StackMapFrame param1StackMapFrame, int param1Int, Type[] param1ArrayOfType, Types param1Types) {
/* 1512 */       Type[] arrayOfType1 = param1StackMapFrame.locals;
/* 1513 */       Type[] arrayOfType2 = param1StackMapFrame.stack;
/* 1514 */       int i = param1StackMapFrame.pc - param1Int - 1;
/* 1515 */       if (arrayOfType2.length == 1) {
/* 1516 */         if (arrayOfType1.length == param1ArrayOfType.length &&
/* 1517 */           compare(param1ArrayOfType, arrayOfType1, param1Types) == 0) {
/* 1518 */           return new SameLocals1StackItemFrame(i, arrayOfType2[0]);
/*      */         }
/* 1520 */       } else if (arrayOfType2.length == 0) {
/* 1521 */         int j = compare(param1ArrayOfType, arrayOfType1, param1Types);
/* 1522 */         if (j == 0)
/* 1523 */           return new SameFrame(i);
/* 1524 */         if (-4 < j && j < 0) {
/*      */
/* 1526 */           Type[] arrayOfType = new Type[-j]; int k; byte b;
/* 1527 */           for (k = param1ArrayOfType.length, b = 0; k < arrayOfType1.length; k++, b++) {
/* 1528 */             arrayOfType[b] = arrayOfType1[k];
/*      */           }
/* 1530 */           return new AppendFrame(251 - j, i, arrayOfType);
/*      */         }
/*      */
/* 1533 */         if (0 < j && j < 4)
/*      */         {
/* 1535 */           return new ChopFrame(251 - j, i);
/*      */         }
/*      */       }
/*      */
/*      */
/* 1540 */       return new FullFrame(i, arrayOfType1, arrayOfType2);
/*      */     }
/*      */
/*      */     static boolean isInt(Type param1Type) {
/* 1544 */       return (param1Type.getTag().isStrictSubRangeOf(TypeTag.INT) || param1Type.hasTag(TypeTag.BOOLEAN));
/*      */     }
/*      */
/*      */     static boolean isSameType(Type param1Type1, Type param1Type2, Types param1Types) {
/* 1548 */       if (param1Type1 == null) return (param1Type2 == null);
/* 1549 */       if (param1Type2 == null) return false;
/*      */
/* 1551 */       if (isInt(param1Type1) && isInt(param1Type2)) return true;
/*      */
/* 1553 */       if (param1Type1.hasTag(TypeTag.UNINITIALIZED_THIS))
/* 1554 */         return param1Type2.hasTag(TypeTag.UNINITIALIZED_THIS);
/* 1555 */       if (param1Type1.hasTag(TypeTag.UNINITIALIZED_OBJECT)) {
/* 1556 */         if (param1Type2.hasTag(TypeTag.UNINITIALIZED_OBJECT)) {
/* 1557 */           return (((UninitializedType)param1Type1).offset == ((UninitializedType)param1Type2).offset);
/*      */         }
/* 1559 */         return false;
/*      */       }
/* 1561 */       if (param1Type2.hasTag(TypeTag.UNINITIALIZED_THIS) || param1Type2.hasTag(TypeTag.UNINITIALIZED_OBJECT)) {
/* 1562 */         return false;
/*      */       }
/*      */
/* 1565 */       return param1Types.isSameType(param1Type1, param1Type2);
/*      */     }
/*      */
/*      */     static int compare(Type[] param1ArrayOfType1, Type[] param1ArrayOfType2, Types param1Types) {
/* 1569 */       int i = param1ArrayOfType1.length - param1ArrayOfType2.length;
/* 1570 */       if (i > 4 || i < -4) {
/* 1571 */         return Integer.MAX_VALUE;
/*      */       }
/* 1573 */       int j = (i > 0) ? param1ArrayOfType2.length : param1ArrayOfType1.length;
/* 1574 */       for (byte b = 0; b < j; b++) {
/* 1575 */         if (!isSameType(param1ArrayOfType1[b], param1ArrayOfType2[b], param1Types)) {
/* 1576 */           return Integer.MAX_VALUE;
/*      */         }
/*      */       }
/* 1579 */       return i;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void writeFields(Scope.Entry paramEntry) {
/* 1586 */     List list = List.nil();
/* 1587 */     for (Scope.Entry entry = paramEntry; entry != null; entry = entry.sibling) {
/* 1588 */       if (entry.sym.kind == 4) list = list.prepend(entry.sym);
/*      */     }
/* 1590 */     while (list.nonEmpty()) {
/* 1591 */       writeField((Symbol.VarSymbol)list.head);
/* 1592 */       list = list.tail;
/*      */     }
/*      */   }
/*      */
/*      */   void writeMethods(Scope.Entry paramEntry) {
/* 1597 */     List list = List.nil();
/* 1598 */     for (Scope.Entry entry = paramEntry; entry != null; entry = entry.sibling) {
/* 1599 */       if (entry.sym.kind == 16 && (entry.sym.flags() & 0x2000000000L) == 0L)
/* 1600 */         list = list.prepend(entry.sym);
/*      */     }
/* 1602 */     while (list.nonEmpty()) {
/* 1603 */       writeMethod((Symbol.MethodSymbol)list.head);
/* 1604 */       list = list.tail;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public JavaFileObject writeClass(Symbol.ClassSymbol paramClassSymbol) throws IOException, PoolOverflow, StringOverflow {
/* 1615 */     JavaFileObject javaFileObject = this.fileManager.getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramClassSymbol.flatname
/* 1616 */         .toString(), JavaFileObject.Kind.CLASS, paramClassSymbol.sourcefile);
/*      */
/*      */
/* 1619 */     OutputStream outputStream = javaFileObject.openOutputStream();
/*      */     try {
/* 1621 */       writeClassFile(outputStream, paramClassSymbol);
/* 1622 */       if (this.verbose)
/* 1623 */         this.log.printVerbose("wrote.file", new Object[] { javaFileObject });
/* 1624 */       outputStream.close();
/* 1625 */       outputStream = null;
/*      */     } finally {
/* 1627 */       if (outputStream != null) {
/*      */
/* 1629 */         outputStream.close();
/* 1630 */         javaFileObject.delete();
/* 1631 */         javaFileObject = null;
/*      */       }
/*      */     }
/* 1634 */     return javaFileObject;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void writeClassFile(OutputStream paramOutputStream, Symbol.ClassSymbol paramClassSymbol) throws IOException, PoolOverflow, StringOverflow {
/* 1641 */     Assert.check(((paramClassSymbol.flags() & 0x1000000L) == 0L));
/* 1642 */     this.databuf.reset();
/* 1643 */     this.poolbuf.reset();
/* 1644 */     this.signatureGen.reset();
/* 1645 */     this.pool = paramClassSymbol.pool;
/* 1646 */     this.innerClasses = null;
/* 1647 */     this.innerClassesQueue = null;
/* 1648 */     this.bootstrapMethods = new LinkedHashMap<>();
/*      */
/* 1650 */     Type type = this.types.supertype(paramClassSymbol.type);
/* 1651 */     List list1 = this.types.interfaces(paramClassSymbol.type);
/* 1652 */     List list2 = paramClassSymbol.type.getTypeArguments();
/*      */
/* 1654 */     int i = adjustFlags(paramClassSymbol.flags() & 0xFFFFF7FFFFFFFFFFL);
/* 1655 */     if ((i & 0x4) != 0) i |= 0x1;
/* 1656 */     i = i & 0x7E11 & 0xFFFFF7FF;
/* 1657 */     if ((i & 0x200) == 0) i |= 0x20;
/* 1658 */     if (paramClassSymbol.isInner() && paramClassSymbol.name.isEmpty()) i &= 0xFFFFFFEF;
/* 1659 */     if (this.dumpClassModifiers) {
/* 1660 */       PrintWriter printWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1661 */       printWriter.println();
/* 1662 */       printWriter.println("CLASSFILE  " + paramClassSymbol.getQualifiedName());
/* 1663 */       printWriter.println("---" + flagNames(i));
/*      */     }
/* 1665 */     this.databuf.appendChar(i);
/*      */
/* 1667 */     this.databuf.appendChar(this.pool.put(paramClassSymbol));
/* 1668 */     this.databuf.appendChar(type.hasTag(TypeTag.CLASS) ? this.pool.put(type.tsym) : 0);
/* 1669 */     this.databuf.appendChar(list1.length());
/* 1670 */     for (List list3 = list1; list3.nonEmpty(); list3 = list3.tail)
/* 1671 */       this.databuf.appendChar(this.pool.put(((Type)list3.head).tsym));
/* 1672 */     byte b1 = 0;
/* 1673 */     byte b2 = 0;
/* 1674 */     for (Scope.Entry entry = (paramClassSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 1675 */       switch (entry.sym.kind) { case 4:
/* 1676 */           b1++; break;
/* 1677 */         case 16: if ((entry.sym.flags() & 0x2000000000L) == 0L) b2++;  break;
/*      */         case 2:
/* 1679 */           enterInner((Symbol.ClassSymbol)entry.sym); break;
/* 1680 */         default: Assert.error();
/*      */           break; }
/*      */
/*      */     }
/* 1684 */     if (paramClassSymbol.trans_local != null) {
/* 1685 */       for (Symbol.ClassSymbol classSymbol : paramClassSymbol.trans_local) {
/* 1686 */         enterInner(classSymbol);
/*      */       }
/*      */     }
/*      */
/* 1690 */     this.databuf.appendChar(b1);
/* 1691 */     writeFields((paramClassSymbol.members()).elems);
/* 1692 */     this.databuf.appendChar(b2);
/* 1693 */     writeMethods((paramClassSymbol.members()).elems);
/*      */
/* 1695 */     int j = beginAttrs();
/* 1696 */     int k = 0;
/*      */
/*      */
/* 1699 */     boolean bool = (list2.length() != 0 || type.allparams().length() != 0) ? true : false;
/* 1700 */     for (List list4 = list1; !bool && list4.nonEmpty(); list4 = list4.tail)
/* 1701 */       bool = (((Type)list4.head).allparams().length() != 0) ? true : false;
/* 1702 */     if (bool) {
/* 1703 */       Assert.check(this.source.allowGenerics());
/* 1704 */       int m = writeAttr(this.names.Signature);
/* 1705 */       if (list2.length() != 0) this.signatureGen.assembleParamsSig(list2);
/* 1706 */       this.signatureGen.assembleSig(type);
/* 1707 */       for (List list = list1; list.nonEmpty(); list = list.tail)
/* 1708 */         this.signatureGen.assembleSig((Type)list.head);
/* 1709 */       this.databuf.appendChar(this.pool.put(this.signatureGen.toName()));
/* 1710 */       this.signatureGen.reset();
/* 1711 */       endAttr(m);
/* 1712 */       k++;
/*      */     }
/*      */
/* 1715 */     if (paramClassSymbol.sourcefile != null && this.emitSourceFile) {
/* 1716 */       int m = writeAttr(this.names.SourceFile);
/*      */
/*      */
/*      */
/*      */
/* 1721 */       String str = BaseFileObject.getSimpleName(paramClassSymbol.sourcefile);
/* 1722 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString(str)));
/* 1723 */       endAttr(m);
/* 1724 */       k++;
/*      */     }
/*      */
/* 1727 */     if (this.genCrt) {
/*      */
/* 1729 */       int m = writeAttr(this.names.SourceID);
/* 1730 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString(Long.toString(getLastModified(paramClassSymbol.sourcefile)))));
/* 1731 */       endAttr(m);
/* 1732 */       k++;
/*      */
/* 1734 */       m = writeAttr(this.names.CompilationID);
/* 1735 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString(Long.toString(System.currentTimeMillis()))));
/* 1736 */       endAttr(m);
/* 1737 */       k++;
/*      */     }
/*      */
/* 1740 */     k += writeFlagAttrs(paramClassSymbol.flags());
/* 1741 */     k += writeJavaAnnotations(paramClassSymbol.getRawAttributes());
/* 1742 */     k += writeTypeAnnotations(paramClassSymbol.getRawTypeAttributes(), false);
/* 1743 */     k += writeEnclosingMethodAttribute(paramClassSymbol);
/* 1744 */     k += writeExtraClassAttributes(paramClassSymbol);
/*      */
/* 1746 */     this.poolbuf.appendInt(-889275714);
/* 1747 */     this.poolbuf.appendChar(this.target.minorVersion);
/* 1748 */     this.poolbuf.appendChar(this.target.majorVersion);
/*      */
/* 1750 */     writePool(paramClassSymbol.pool);
/*      */
/* 1752 */     if (this.innerClasses != null) {
/* 1753 */       writeInnerClasses();
/* 1754 */       k++;
/*      */     }
/*      */
/* 1757 */     if (!this.bootstrapMethods.isEmpty()) {
/* 1758 */       writeBootstrapMethods();
/* 1759 */       k++;
/*      */     }
/*      */
/* 1762 */     endAttrs(j, k);
/*      */
/* 1764 */     this.poolbuf.appendBytes(this.databuf.elems, 0, this.databuf.length);
/* 1765 */     paramOutputStream.write(this.poolbuf.elems, 0, this.poolbuf.length);
/*      */
/* 1767 */     this.pool = paramClassSymbol.pool = null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected int writeExtraClassAttributes(Symbol.ClassSymbol paramClassSymbol) {
/* 1775 */     return 0;
/*      */   }
/*      */
/*      */   int adjustFlags(long paramLong) {
/* 1779 */     int i = (int)paramLong;
/* 1780 */     if ((paramLong & 0x1000L) != 0L && !this.target.useSyntheticFlag())
/* 1781 */       i &= 0xFFFFEFFF;
/* 1782 */     if ((paramLong & 0x4000L) != 0L && !this.target.useEnumFlag())
/* 1783 */       i &= 0xFFFFBFFF;
/* 1784 */     if ((paramLong & 0x2000L) != 0L && !this.target.useAnnotationFlag()) {
/* 1785 */       i &= 0xFFFFDFFF;
/*      */     }
/* 1787 */     if ((paramLong & 0x80000000L) != 0L && this.target.useBridgeFlag())
/* 1788 */       i |= 0x40;
/* 1789 */     if ((paramLong & 0x400000000L) != 0L && this.target.useVarargsFlag())
/* 1790 */       i |= 0x80;
/* 1791 */     if ((paramLong & 0x80000000000L) != 0L)
/* 1792 */       i &= 0xFFFFFBFF;
/* 1793 */     return i;
/*      */   }
/*      */
/*      */   long getLastModified(FileObject paramFileObject) {
/* 1797 */     long l = 0L;
/*      */     try {
/* 1799 */       l = paramFileObject.getLastModified();
/* 1800 */     } catch (SecurityException securityException) {
/* 1801 */       throw new AssertionError("CRT: couldn't get source file modification date: " + securityException.getMessage());
/*      */     }
/* 1803 */     return l;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\ClassWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
