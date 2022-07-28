/*      */ package com.sun.tools.javac.jvm;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.util.ArrayUtils;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Bits;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Position;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
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
/*      */ public class Code
/*      */ {
/*      */   public final boolean debugCode;
/*      */   public final boolean needStackMap;
/*      */   final Types types;
/*      */   final Symtab syms;
/*      */
/*      */   public enum StackMapFormat
/*      */   {
/*   56 */     NONE,
/*   57 */     CLDC {
/*      */       Name getAttributeName(Names param2Names) {
/*   59 */         return param2Names.StackMap;
/*      */       }
/*      */     },
/*   62 */     JSR202 {
/*      */       Name getAttributeName(Names param2Names) {
/*   64 */         return param2Names.StackMapTable;
/*      */       } };
/*      */
/*      */     Name getAttributeName(Names param1Names) {
/*   68 */       return param1Names.empty;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*   79 */   public int max_stack = 0;
/*      */
/*      */
/*      */
/*   83 */   public int max_locals = 0;
/*      */
/*      */
/*      */
/*   87 */   public byte[] code = new byte[64];
/*      */
/*      */
/*      */
/*   91 */   public int cp = 0;
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean checkLimits(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Log paramLog) {
/*   97 */     if (this.cp > 65535) {
/*   98 */       paramLog.error(paramDiagnosticPosition, "limit.code", new Object[0]);
/*   99 */       return true;
/*      */     }
/*  101 */     if (this.max_locals > 65535) {
/*  102 */       paramLog.error(paramDiagnosticPosition, "limit.locals", new Object[0]);
/*  103 */       return true;
/*      */     }
/*  105 */     if (this.max_stack > 65535) {
/*  106 */       paramLog.error(paramDiagnosticPosition, "limit.stack", new Object[0]);
/*  107 */       return true;
/*      */     }
/*  109 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*  115 */   ListBuffer<char[]> catchInfo = new ListBuffer();
/*      */
/*      */
/*      */
/*      */
/*  120 */   List<char[]> lineInfo = List.nil();
/*      */
/*      */
/*      */
/*      */
/*      */   public CRTable crt;
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean fatcode;
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean alive = true;
/*      */
/*      */
/*      */
/*      */   State state;
/*      */
/*      */
/*      */
/*      */   private boolean fixedPc = false;
/*      */
/*      */
/*      */
/*  147 */   public int nextreg = 0;
/*      */
/*      */
/*      */
/*      */
/*  152 */   Chain pendingJumps = null;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  159 */   int pendingStatPos = -1;
/*      */
/*      */
/*      */
/*      */
/*      */   boolean pendingStackMap = false;
/*      */
/*      */
/*      */
/*      */
/*      */   StackMapFormat stackMap;
/*      */
/*      */
/*      */
/*      */
/*      */   boolean varDebugInfo;
/*      */
/*      */
/*      */
/*      */
/*      */   boolean lineDebugInfo;
/*      */
/*      */
/*      */
/*      */
/*      */   Position.LineMap lineMap;
/*      */
/*      */
/*      */
/*      */
/*      */   final Pool pool;
/*      */
/*      */
/*      */
/*      */
/*      */   final Symbol.MethodSymbol meth;
/*      */
/*      */
/*      */
/*      */
/*      */   StackMapFrame[] stackMapBuffer;
/*      */
/*      */
/*      */
/*      */
/*      */   ClassWriter.StackMapTableFrame[] stackMapTableBuffer;
/*      */
/*      */
/*      */
/*      */
/*      */   int stackMapBufferSize;
/*      */
/*      */
/*      */
/*      */
/*      */   int lastStackMapPC;
/*      */
/*      */
/*      */
/*      */
/*      */   StackMapFrame lastFrame;
/*      */
/*      */
/*      */
/*      */
/*      */   StackMapFrame frameBeforeLast;
/*      */
/*      */
/*      */
/*      */
/*      */   public static int typecode(Type paramType) {
/*  230 */     switch (paramType.getTag()) { case BYTE:
/*  231 */         return 5;
/*  232 */       case SHORT: return 7;
/*  233 */       case CHAR: return 6;
/*  234 */       case INT: return 0;
/*  235 */       case LONG: return 1;
/*  236 */       case FLOAT: return 2;
/*  237 */       case DOUBLE: return 3;
/*  238 */       case BOOLEAN: return 5;
/*  239 */       case VOID: return 8;
/*      */       case CLASS:
/*      */       case ARRAY:
/*      */       case METHOD:
/*      */       case BOT:
/*      */       case TYPEVAR:
/*      */       case UNINITIALIZED_THIS:
/*      */       case UNINITIALIZED_OBJECT:
/*  247 */         return 4; }
/*  248 */      throw new AssertionError("typecode " + paramType.getTag());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static int truncate(int paramInt) {
/*  255 */     switch (paramInt) { case 5: case 6: case 7:
/*  256 */         return 0; }
/*  257 */      return paramInt;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static int width(int paramInt) {
/*  264 */     switch (paramInt) { case 1: case 3:
/*  265 */         return 2;
/*  266 */       case 8: return 0; }
/*  267 */      return 1;
/*      */   }
/*      */
/*      */
/*      */   public static int width(Type paramType) {
/*  272 */     return (paramType == null) ? 1 : width(typecode(paramType));
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static int width(List<Type> paramList) {
/*  278 */     int i = 0;
/*  279 */     for (List<Type> list = paramList; list.nonEmpty(); list = list.tail)
/*  280 */       i += width((Type)list.head);
/*  281 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static int arraycode(Type paramType) {
/*  287 */     switch (paramType.getTag()) { case BYTE:
/*  288 */         return 8;
/*  289 */       case BOOLEAN: return 4;
/*  290 */       case SHORT: return 9;
/*  291 */       case CHAR: return 5;
/*  292 */       case INT: return 10;
/*  293 */       case LONG: return 11;
/*  294 */       case FLOAT: return 6;
/*  295 */       case DOUBLE: return 7;
/*  296 */       case CLASS: return 0;
/*  297 */       case ARRAY: return 1; }
/*  298 */      throw new AssertionError("arraycode " + paramType);
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
/*      */   public int curCP() {
/*  316 */     if (this.pendingJumps != null) {
/*  317 */       resolvePending();
/*      */     }
/*  319 */     if (this.pendingStatPos != -1) {
/*  320 */       markStatBegin();
/*      */     }
/*  322 */     this.fixedPc = true;
/*  323 */     return this.cp;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void emit1(int paramInt) {
/*  329 */     if (!this.alive)
/*  330 */       return;  this.code = ArrayUtils.ensureCapacity(this.code, this.cp);
/*  331 */     this.code[this.cp++] = (byte)paramInt;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void emit2(int paramInt) {
/*  337 */     if (!this.alive)
/*  338 */       return;  if (this.cp + 2 > this.code.length) {
/*  339 */       emit1(paramInt >> 8);
/*  340 */       emit1(paramInt);
/*      */     } else {
/*  342 */       this.code[this.cp++] = (byte)(paramInt >> 8);
/*  343 */       this.code[this.cp++] = (byte)paramInt;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emit4(int paramInt) {
/*  350 */     if (!this.alive)
/*  351 */       return;  if (this.cp + 4 > this.code.length) {
/*  352 */       emit1(paramInt >> 24);
/*  353 */       emit1(paramInt >> 16);
/*  354 */       emit1(paramInt >> 8);
/*  355 */       emit1(paramInt);
/*      */     } else {
/*  357 */       this.code[this.cp++] = (byte)(paramInt >> 24);
/*  358 */       this.code[this.cp++] = (byte)(paramInt >> 16);
/*  359 */       this.code[this.cp++] = (byte)(paramInt >> 8);
/*  360 */       this.code[this.cp++] = (byte)paramInt;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void emitop(int paramInt) {
/*  367 */     if (this.pendingJumps != null) resolvePending();
/*  368 */     if (this.alive) {
/*  369 */       if (this.pendingStatPos != -1)
/*  370 */         markStatBegin();
/*  371 */       if (this.pendingStackMap) {
/*  372 */         this.pendingStackMap = false;
/*  373 */         emitStackMap();
/*      */       }
/*  375 */       if (this.debugCode)
/*  376 */         System.err.println("emit@" + this.cp + " stack=" + this.state.stacksize + ": " +
/*      */
/*  378 */             mnem(paramInt));
/*  379 */       emit1(paramInt);
/*      */     }
/*      */   }
/*      */
/*      */   void postop() {
/*  384 */     Assert.check((this.alive || this.state.stacksize == 0));
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitLdc(int paramInt) {
/*  390 */     if (paramInt <= 255) {
/*  391 */       emitop1(18, paramInt);
/*      */     } else {
/*      */
/*  394 */       emitop2(19, paramInt);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitMultianewarray(int paramInt1, int paramInt2, Type paramType) {
/*  401 */     emitop(197);
/*  402 */     if (!this.alive)
/*  403 */       return;  emit2(paramInt2);
/*  404 */     emit1(paramInt1);
/*  405 */     this.state.pop(paramInt1);
/*  406 */     this.state.push(paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitNewarray(int paramInt, Type paramType) {
/*  412 */     emitop(188);
/*  413 */     if (!this.alive)
/*  414 */       return;  emit1(paramInt);
/*  415 */     this.state.pop(1);
/*  416 */     this.state.push(paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitAnewarray(int paramInt, Type paramType) {
/*  422 */     emitop(189);
/*  423 */     if (!this.alive)
/*  424 */       return;  emit2(paramInt);
/*  425 */     this.state.pop(1);
/*  426 */     this.state.push(paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitInvokeinterface(int paramInt, Type paramType) {
/*  432 */     int i = width(paramType.getParameterTypes());
/*  433 */     emitop(185);
/*  434 */     if (!this.alive)
/*  435 */       return;  emit2(paramInt);
/*  436 */     emit1(i + 1);
/*  437 */     emit1(0);
/*  438 */     this.state.pop(i + 1);
/*  439 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitInvokespecial(int paramInt, Type paramType) {
/*  445 */     int i = width(paramType.getParameterTypes());
/*  446 */     emitop(183);
/*  447 */     if (!this.alive)
/*  448 */       return;  emit2(paramInt);
/*  449 */     Symbol symbol = (Symbol)this.pool.pool[paramInt];
/*  450 */     this.state.pop(i);
/*  451 */     if (symbol.isConstructor())
/*  452 */       this.state.markInitialized((UninitializedType)this.state.peek());
/*  453 */     this.state.pop(1);
/*  454 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitInvokestatic(int paramInt, Type paramType) {
/*  460 */     int i = width(paramType.getParameterTypes());
/*  461 */     emitop(184);
/*  462 */     if (!this.alive)
/*  463 */       return;  emit2(paramInt);
/*  464 */     this.state.pop(i);
/*  465 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitInvokevirtual(int paramInt, Type paramType) {
/*  471 */     int i = width(paramType.getParameterTypes());
/*  472 */     emitop(182);
/*  473 */     if (!this.alive)
/*  474 */       return;  emit2(paramInt);
/*  475 */     this.state.pop(i + 1);
/*  476 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitInvokedynamic(int paramInt, Type paramType) {
/*  482 */     int i = width(paramType.getParameterTypes());
/*  483 */     emitop(186);
/*  484 */     if (!this.alive)
/*  485 */       return;  emit2(paramInt);
/*  486 */     emit2(0);
/*  487 */     this.state.pop(i);
/*  488 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */
/*      */
/*      */   public void emitop0(int paramInt) {
/*      */     Type type1, type2;
/*  494 */     emitop(paramInt);
/*  495 */     if (!this.alive)
/*  496 */       return;  switch (paramInt) {
/*      */       case 50:
/*  498 */         this.state.pop(1);
/*  499 */         type1 = this.state.stack[this.state.stacksize - 1];
/*  500 */         this.state.pop(1);
/*      */
/*      */
/*      */
/*      */
/*  505 */         type2 = type1.hasTag(TypeTag.BOT) ? this.syms.objectType : this.types.erasure(this.types.elemtype(type1));
/*  506 */         this.state.push(type2);
/*      */         break;
/*      */       case 167:
/*  509 */         markDead();
/*      */         break;
/*      */       case 0:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */         break;
/*      */       case 1:
/*  518 */         this.state.push(this.syms.botType);
/*      */         break;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*  531 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 9:
/*      */       case 10:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*  539 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*  548 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 14:
/*      */       case 15:
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*  556 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 42:
/*  559 */         this.state.push((this.lvar[0]).sym.type);
/*      */         break;
/*      */       case 43:
/*  562 */         this.state.push((this.lvar[1]).sym.type);
/*      */         break;
/*      */       case 44:
/*  565 */         this.state.push((this.lvar[2]).sym.type);
/*      */         break;
/*      */       case 45:
/*  568 */         this.state.push((this.lvar[3]).sym.type);
/*      */         break;
/*      */       case 46:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*  574 */         this.state.pop(2);
/*  575 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 47:
/*  578 */         this.state.pop(2);
/*  579 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 48:
/*  582 */         this.state.pop(2);
/*  583 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 49:
/*  586 */         this.state.pop(2);
/*  587 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 87:
/*      */       case 121:
/*      */       case 123:
/*      */       case 125:
/*  605 */         this.state.pop(1);
/*      */         break;
/*      */       case 172:
/*      */       case 174:
/*      */       case 176:
/*  610 */         Assert.check((this.state.nlocks == 0));
/*  611 */         this.state.pop(1);
/*  612 */         markDead();
/*      */         break;
/*      */       case 191:
/*  615 */         this.state.pop(1);
/*  616 */         markDead();
/*      */         break;
/*      */       case 63:
/*      */       case 64:
/*      */       case 65:
/*      */       case 66:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 88:
/*  627 */         this.state.pop(2);
/*      */         break;
/*      */       case 173:
/*      */       case 175:
/*  631 */         Assert.check((this.state.nlocks == 0));
/*  632 */         this.state.pop(2);
/*  633 */         markDead();
/*      */         break;
/*      */       case 89:
/*  636 */         this.state.push(this.state.stack[this.state.stacksize - 1]);
/*      */         break;
/*      */       case 177:
/*  639 */         Assert.check((this.state.nlocks == 0));
/*  640 */         markDead();
/*      */         break;
/*      */       case 190:
/*  643 */         this.state.pop(1);
/*  644 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 96:
/*      */       case 100:
/*      */       case 104:
/*      */       case 108:
/*      */       case 112:
/*      */       case 120:
/*      */       case 122:
/*      */       case 124:
/*      */       case 126:
/*      */       case 128:
/*      */       case 130:
/*  657 */         this.state.pop(1);
/*      */         break;
/*      */
/*      */
/*      */       case 83:
/*  662 */         this.state.pop(3);
/*      */         break;
/*      */       case 97:
/*      */       case 101:
/*      */       case 105:
/*      */       case 109:
/*      */       case 113:
/*      */       case 127:
/*      */       case 129:
/*      */       case 131:
/*  672 */         this.state.pop(2);
/*      */         break;
/*      */       case 148:
/*  675 */         this.state.pop(4);
/*  676 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 136:
/*  679 */         this.state.pop(2);
/*  680 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 133:
/*  683 */         this.state.pop(1);
/*  684 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 134:
/*  687 */         this.state.pop(1);
/*  688 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 135:
/*  691 */         this.state.pop(1);
/*  692 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 137:
/*  695 */         this.state.pop(2);
/*  696 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 138:
/*  699 */         this.state.pop(2);
/*  700 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 139:
/*  703 */         this.state.pop(1);
/*  704 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 140:
/*  707 */         this.state.pop(1);
/*  708 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 141:
/*  711 */         this.state.pop(1);
/*  712 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 142:
/*  715 */         this.state.pop(2);
/*  716 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 143:
/*  719 */         this.state.pop(2);
/*  720 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 144:
/*  723 */         this.state.pop(2);
/*  724 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 170:
/*      */       case 171:
/*  728 */         this.state.pop(1);
/*      */         break;
/*      */
/*      */       case 90:
/*  732 */         type1 = this.state.pop1();
/*  733 */         type2 = this.state.pop1();
/*  734 */         this.state.push(type1);
/*  735 */         this.state.push(type2);
/*  736 */         this.state.push(type1);
/*      */         break;
/*      */
/*      */       case 84:
/*  740 */         this.state.pop(3);
/*      */         break;
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */         break;
/*      */       case 98:
/*      */       case 102:
/*      */       case 106:
/*      */       case 110:
/*      */       case 114:
/*  751 */         this.state.pop(1);
/*      */         break;
/*      */       case 79:
/*      */       case 81:
/*      */       case 85:
/*      */       case 86:
/*  757 */         this.state.pop(3);
/*      */         break;
/*      */       case 80:
/*      */       case 82:
/*  761 */         this.state.pop(4);
/*      */         break;
/*      */       case 92:
/*  764 */         if (this.state.stack[this.state.stacksize - 1] != null) {
/*  765 */           type1 = this.state.pop1();
/*  766 */           type2 = this.state.pop1();
/*  767 */           this.state.push(type2);
/*  768 */           this.state.push(type1);
/*  769 */           this.state.push(type2);
/*  770 */           this.state.push(type1); break;
/*      */         }
/*  772 */         type1 = this.state.pop2();
/*  773 */         this.state.push(type1);
/*  774 */         this.state.push(type1);
/*      */         break;
/*      */
/*      */       case 93:
/*  778 */         if (this.state.stack[this.state.stacksize - 1] != null) {
/*  779 */           type1 = this.state.pop1();
/*  780 */           type2 = this.state.pop1();
/*  781 */           Type type = this.state.pop1();
/*  782 */           this.state.push(type2);
/*  783 */           this.state.push(type1);
/*  784 */           this.state.push(type);
/*  785 */           this.state.push(type2);
/*  786 */           this.state.push(type1); break;
/*      */         }
/*  788 */         type1 = this.state.pop2();
/*  789 */         type2 = this.state.pop1();
/*  790 */         this.state.push(type1);
/*  791 */         this.state.push(type2);
/*  792 */         this.state.push(type1);
/*      */         break;
/*      */
/*      */       case 94:
/*  796 */         if (this.state.stack[this.state.stacksize - 1] != null) {
/*  797 */           type1 = this.state.pop1();
/*  798 */           type2 = this.state.pop1();
/*  799 */           if (this.state.stack[this.state.stacksize - 1] != null) {
/*      */
/*  801 */             Type type3 = this.state.pop1();
/*  802 */             Type type4 = this.state.pop1();
/*  803 */             this.state.push(type2);
/*  804 */             this.state.push(type1);
/*  805 */             this.state.push(type4);
/*  806 */             this.state.push(type3);
/*  807 */             this.state.push(type2);
/*  808 */             this.state.push(type1);
/*      */             break;
/*      */           }
/*  811 */           Type type = this.state.pop2();
/*  812 */           this.state.push(type2);
/*  813 */           this.state.push(type1);
/*  814 */           this.state.push(type);
/*  815 */           this.state.push(type2);
/*  816 */           this.state.push(type1);
/*      */           break;
/*      */         }
/*  819 */         type1 = this.state.pop2();
/*  820 */         if (this.state.stack[this.state.stacksize - 1] != null) {
/*      */
/*  822 */           type2 = this.state.pop1();
/*  823 */           Type type = this.state.pop1();
/*  824 */           this.state.push(type1);
/*  825 */           this.state.push(type);
/*  826 */           this.state.push(type2);
/*  827 */           this.state.push(type1);
/*      */           break;
/*      */         }
/*  830 */         type2 = this.state.pop2();
/*  831 */         this.state.push(type1);
/*  832 */         this.state.push(type2);
/*  833 */         this.state.push(type1);
/*      */         break;
/*      */
/*      */
/*      */       case 91:
/*  838 */         type1 = this.state.pop1();
/*  839 */         if (this.state.stack[this.state.stacksize - 1] != null) {
/*      */
/*  841 */           type2 = this.state.pop1();
/*  842 */           Type type = this.state.pop1();
/*  843 */           this.state.push(type1);
/*  844 */           this.state.push(type);
/*  845 */           this.state.push(type2);
/*  846 */           this.state.push(type1);
/*      */           break;
/*      */         }
/*  849 */         type2 = this.state.pop2();
/*  850 */         this.state.push(type1);
/*  851 */         this.state.push(type2);
/*  852 */         this.state.push(type1);
/*      */         break;
/*      */
/*      */
/*      */       case 149:
/*      */       case 150:
/*  858 */         this.state.pop(2);
/*  859 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 151:
/*      */       case 152:
/*  863 */         this.state.pop(4);
/*  864 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 95:
/*  867 */         type1 = this.state.pop1();
/*  868 */         type2 = this.state.pop1();
/*  869 */         this.state.push(type1);
/*  870 */         this.state.push(type2);
/*      */         break;
/*      */
/*      */       case 99:
/*      */       case 103:
/*      */       case 107:
/*      */       case 111:
/*      */       case 115:
/*  878 */         this.state.pop(2);
/*      */         break;
/*      */       case 169:
/*  881 */         markDead();
/*      */         break;
/*      */
/*      */       case 196:
/*      */         return;
/*      */       case 194:
/*      */       case 195:
/*  888 */         this.state.pop(1);
/*      */         break;
/*      */
/*      */       default:
/*  892 */         throw new AssertionError(mnem(paramInt));
/*      */     }
/*  894 */     postop();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void emitop1(int paramInt1, int paramInt2) {
/*  900 */     emitop(paramInt1);
/*  901 */     if (!this.alive)
/*  902 */       return;  emit1(paramInt2);
/*  903 */     switch (paramInt1) {
/*      */       case 16:
/*  905 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 18:
/*  908 */         this.state.push(typeForPool(this.pool.pool[paramInt2]));
/*      */         break;
/*      */       default:
/*  911 */         throw new AssertionError(mnem(paramInt1));
/*      */     }
/*  913 */     postop();
/*      */   }
/*      */
/*      */
/*      */   private Type typeForPool(Object paramObject) {
/*  918 */     if (paramObject instanceof Integer) return (Type)this.syms.intType;
/*  919 */     if (paramObject instanceof Float) return (Type)this.syms.floatType;
/*  920 */     if (paramObject instanceof String) return this.syms.stringType;
/*  921 */     if (paramObject instanceof Long) return (Type)this.syms.longType;
/*  922 */     if (paramObject instanceof Double) return (Type)this.syms.doubleType;
/*  923 */     if (paramObject instanceof Symbol.ClassSymbol) return this.syms.classType;
/*  924 */     if (paramObject instanceof Pool.MethodHandle) return this.syms.methodHandleType;
/*  925 */     if (paramObject instanceof Types.UniqueType) return typeForPool(((Types.UniqueType)paramObject).type);
/*  926 */     if (paramObject instanceof Type) {
/*  927 */       Type type = ((Type)paramObject).unannotatedType();
/*      */
/*  929 */       if (type instanceof Type.ArrayType) return this.syms.classType;
/*  930 */       if (type instanceof Type.MethodType) return this.syms.methodTypeType;
/*      */     }
/*  932 */     throw new AssertionError("Invalid type of constant pool entry: " + paramObject.getClass());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void emitop1w(int paramInt1, int paramInt2) {
/*  939 */     if (paramInt2 > 255) {
/*  940 */       emitop(196);
/*  941 */       emitop(paramInt1);
/*  942 */       emit2(paramInt2);
/*      */     } else {
/*  944 */       emitop(paramInt1);
/*  945 */       emit1(paramInt2);
/*      */     }
/*  947 */     if (!this.alive)
/*  948 */       return;  switch (paramInt1) {
/*      */       case 21:
/*  950 */         this.state.push((Type)this.syms.intType);
/*      */         break;
/*      */       case 22:
/*  953 */         this.state.push((Type)this.syms.longType);
/*      */         break;
/*      */       case 23:
/*  956 */         this.state.push((Type)this.syms.floatType);
/*      */         break;
/*      */       case 24:
/*  959 */         this.state.push((Type)this.syms.doubleType);
/*      */         break;
/*      */       case 25:
/*  962 */         this.state.push((this.lvar[paramInt2]).sym.type);
/*      */         break;
/*      */       case 55:
/*      */       case 57:
/*  966 */         this.state.pop(2);
/*      */         break;
/*      */       case 54:
/*      */       case 56:
/*      */       case 58:
/*  971 */         this.state.pop(1);
/*      */         break;
/*      */       case 169:
/*  974 */         markDead();
/*      */         break;
/*      */       default:
/*  977 */         throw new AssertionError(mnem(paramInt1));
/*      */     }
/*  979 */     postop();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void emitop1w(int paramInt1, int paramInt2, int paramInt3) {
/*  986 */     if (paramInt2 > 255 || paramInt3 < -128 || paramInt3 > 127) {
/*  987 */       emitop(196);
/*  988 */       emitop(paramInt1);
/*  989 */       emit2(paramInt2);
/*  990 */       emit2(paramInt3);
/*      */     } else {
/*  992 */       emitop(paramInt1);
/*  993 */       emit1(paramInt2);
/*  994 */       emit1(paramInt3);
/*      */     }
/*  996 */     if (!this.alive)
/*  997 */       return;  switch (paramInt1) {
/*      */       case 132:
/*      */         return;
/*      */     }
/* 1001 */     throw new AssertionError(mnem(paramInt1));
/*      */   }
/*      */
/*      */   public void emitop2(int paramInt1, int paramInt2) {
/*      */     Symbol symbol;
/*      */     Object object;
/*      */     Type type;
/* 1008 */     emitop(paramInt1);
/* 1009 */     if (!this.alive)
/* 1010 */       return;  emit2(paramInt2);
/* 1011 */     switch (paramInt1) {
/*      */       case 178:
/* 1013 */         this.state.push(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/*      */
/*      */       case 179:
/* 1016 */         this.state.pop(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/*      */
/*      */
/*      */       case 187:
/* 1020 */         if (this.pool.pool[paramInt2] instanceof Types.UniqueType) {
/*      */
/*      */
/*      */
/* 1024 */           Symbol.TypeSymbol typeSymbol = ((Types.UniqueType)this.pool.pool[paramInt2]).type.tsym;
/*      */         } else {
/* 1026 */           symbol = (Symbol)this.pool.pool[paramInt2];
/*      */         }
/* 1028 */         this.state.push((Type)UninitializedType.uninitializedObject(symbol.erasure(this.types), this.cp - 3));
/*      */
/*      */       case 17:
/* 1031 */         this.state.push((Type)this.syms.intType);
/*      */
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 198:
/*      */       case 199:
/* 1041 */         this.state.pop(1);
/*      */
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 163:
/*      */       case 164:
/*      */       case 165:
/*      */       case 166:
/* 1051 */         this.state.pop(2);
/*      */
/*      */       case 167:
/* 1054 */         markDead();
/*      */
/*      */       case 181:
/* 1057 */         this.state.pop(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/* 1058 */         this.state.pop(1);
/*      */
/*      */       case 180:
/* 1061 */         this.state.pop(1);
/* 1062 */         this.state.push(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/*      */
/*      */       case 192:
/* 1065 */         this.state.pop(1);
/* 1066 */         object = this.pool.pool[paramInt2];
/*      */
/*      */
/* 1069 */         type = (object instanceof Symbol) ? ((Symbol)object).erasure(this.types) : this.types.erasure(((Types.UniqueType)object).type);
/* 1070 */         this.state.push(type);
/*      */
/*      */       case 20:
/* 1073 */         this.state.push(typeForPool(this.pool.pool[paramInt2]));
/*      */
/*      */       case 193:
/* 1076 */         this.state.pop(1);
/* 1077 */         this.state.push((Type)this.syms.intType);
/*      */
/*      */       case 19:
/* 1080 */         this.state.push(typeForPool(this.pool.pool[paramInt2]));
/*      */
/*      */       case 168:
/*      */         return;
/*      */     }
/* 1085 */     throw new AssertionError(mnem(paramInt1));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void emitop4(int paramInt1, int paramInt2) {
/* 1093 */     emitop(paramInt1);
/* 1094 */     if (!this.alive)
/* 1095 */       return;  emit4(paramInt2);
/* 1096 */     switch (paramInt1) {
/*      */       case 200:
/* 1098 */         markDead();
/*      */
/*      */       case 201:
/*      */         return;
/*      */     }
/* 1103 */     throw new AssertionError(mnem(paramInt1));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void align(int paramInt) {
/* 1111 */     if (this.alive) {
/* 1112 */       for (; this.cp % paramInt != 0; emitop0(0));
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void put1(int paramInt1, int paramInt2) {
/* 1119 */     this.code[paramInt1] = (byte)paramInt2;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void put2(int paramInt1, int paramInt2) {
/* 1127 */     put1(paramInt1, paramInt2 >> 8);
/* 1128 */     put1(paramInt1 + 1, paramInt2);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void put4(int paramInt1, int paramInt2) {
/* 1136 */     put1(paramInt1, paramInt2 >> 24);
/* 1137 */     put1(paramInt1 + 1, paramInt2 >> 16);
/* 1138 */     put1(paramInt1 + 2, paramInt2 >> 8);
/* 1139 */     put1(paramInt1 + 3, paramInt2);
/*      */   }
/*      */
/*      */
/*      */
/*      */   private int get1(int paramInt) {
/* 1145 */     return this.code[paramInt] & 0xFF;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private int get2(int paramInt) {
/* 1151 */     return get1(paramInt) << 8 | get1(paramInt + 1);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public int get4(int paramInt) {
/* 1158 */     return
/* 1159 */       get1(paramInt) << 24 |
/* 1160 */       get1(paramInt + 1) << 16 |
/* 1161 */       get1(paramInt + 2) << 8 |
/* 1162 */       get1(paramInt + 3);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isAlive() {
/* 1168 */     return (this.alive || this.pendingJumps != null);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void markDead() {
/* 1174 */     this.alive = false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public int entryPoint() {
/* 1180 */     int i = curCP();
/* 1181 */     this.alive = true;
/* 1182 */     this.pendingStackMap = this.needStackMap;
/* 1183 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public int entryPoint(State paramState) {
/* 1190 */     int i = curCP();
/* 1191 */     this.alive = true;
/* 1192 */     State state = paramState.dup();
/* 1193 */     setDefined(state.defined);
/* 1194 */     this.state = state;
/* 1195 */     Assert.check((paramState.stacksize <= this.max_stack));
/* 1196 */     if (this.debugCode) System.err.println("entry point " + paramState);
/* 1197 */     this.pendingStackMap = this.needStackMap;
/* 1198 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public int entryPoint(State paramState, Type paramType) {
/* 1205 */     int i = curCP();
/* 1206 */     this.alive = true;
/* 1207 */     State state = paramState.dup();
/* 1208 */     setDefined(state.defined);
/* 1209 */     this.state = state;
/* 1210 */     Assert.check((paramState.stacksize <= this.max_stack));
/* 1211 */     this.state.push(paramType);
/* 1212 */     if (this.debugCode) System.err.println("entry point " + paramState);
/* 1213 */     this.pendingStackMap = this.needStackMap;
/* 1214 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */   static class StackMapFrame
/*      */   {
/*      */     int pc;
/*      */
/*      */     Type[] locals;
/*      */
/*      */     Type[] stack;
/*      */   }
/*      */
/*      */
/*      */   public Code(Symbol.MethodSymbol paramMethodSymbol, boolean paramBoolean1, Position.LineMap paramLineMap, boolean paramBoolean2, StackMapFormat paramStackMapFormat, boolean paramBoolean3, CRTable paramCRTable, Symtab paramSymtab, Types paramTypes, Pool paramPool) {
/* 1230 */     this.stackMapBuffer = null;
/*      */
/*      */
/* 1233 */     this.stackMapTableBuffer = null;
/* 1234 */     this.stackMapBufferSize = 0;
/*      */
/*      */
/* 1237 */     this.lastStackMapPC = -1;
/*      */
/*      */
/* 1240 */     this.lastFrame = null;
/*      */
/*      */
/* 1243 */     this.frameBeforeLast = null; this.meth = paramMethodSymbol; this.fatcode = paramBoolean1; this.lineMap = paramLineMap; this.lineDebugInfo = (paramLineMap != null); this.varDebugInfo = paramBoolean2; this.crt = paramCRTable; this.syms = paramSymtab; this.types = paramTypes; this.debugCode = paramBoolean3; this.stackMap = paramStackMapFormat; switch (paramStackMapFormat) { case BYTE:
/*      */       case SHORT:
/*      */         this.needStackMap = true; break;
/*      */       default:
/* 1247 */         this.needStackMap = false; break; }  this.state = new State(); this.lvar = new LocalVar[20]; this.pool = paramPool; } public void emitStackMap() { int i = curCP();
/* 1248 */     if (!this.needStackMap) {
/*      */       return;
/*      */     }
/*      */
/* 1252 */     switch (this.stackMap) {
/*      */       case BYTE:
/* 1254 */         emitCLDCStackMap(i, getLocalsSize());
/*      */         break;
/*      */       case SHORT:
/* 1257 */         emitStackMapFrame(i, getLocalsSize());
/*      */         break;
/*      */       default:
/* 1260 */         throw new AssertionError("Should have chosen a stackmap format");
/*      */     }
/*      */
/* 1263 */     if (this.debugCode) this.state.dump(i);  }
/*      */
/*      */
/*      */   private int getLocalsSize() {
/* 1267 */     int i = 0;
/* 1268 */     for (int j = this.max_locals - 1; j >= 0; j--) {
/* 1269 */       if (this.state.defined.isMember(j) && this.lvar[j] != null) {
/* 1270 */         i = j + width((this.lvar[j]).sym.erasure(this.types));
/*      */         break;
/*      */       }
/*      */     }
/* 1274 */     return i;
/*      */   }
/*      */
/*      */
/*      */   void emitCLDCStackMap(int paramInt1, int paramInt2) {
/* 1279 */     if (this.lastStackMapPC == paramInt1)
/*      */     {
/* 1281 */       this.stackMapBuffer[--this.stackMapBufferSize] = null;
/*      */     }
/* 1283 */     this.lastStackMapPC = paramInt1;
/*      */
/* 1285 */     if (this.stackMapBuffer == null) {
/* 1286 */       this.stackMapBuffer = new StackMapFrame[20];
/*      */     } else {
/* 1288 */       this.stackMapBuffer = (StackMapFrame[])ArrayUtils.ensureCapacity((Object[])this.stackMapBuffer, this.stackMapBufferSize);
/*      */     }
/* 1290 */     StackMapFrame stackMapFrame = this.stackMapBuffer[this.stackMapBufferSize++] = new StackMapFrame();
/*      */
/* 1292 */     stackMapFrame.pc = paramInt1;
/*      */
/* 1294 */     stackMapFrame.locals = new Type[paramInt2]; byte b;
/* 1295 */     for (b = 0; b < paramInt2; b++) {
/* 1296 */       if (this.state.defined.isMember(b) && this.lvar[b] != null) {
/* 1297 */         Type type = (this.lvar[b]).sym.type;
/* 1298 */         if (!(type instanceof UninitializedType))
/* 1299 */           type = this.types.erasure(type);
/* 1300 */         stackMapFrame.locals[b] = type;
/*      */       }
/*      */     }
/* 1303 */     stackMapFrame.stack = new Type[this.state.stacksize];
/* 1304 */     for (b = 0; b < this.state.stacksize; b++)
/* 1305 */       stackMapFrame.stack[b] = this.state.stack[b];
/*      */   }
/*      */
/*      */   void emitStackMapFrame(int paramInt1, int paramInt2) {
/* 1309 */     if (this.lastFrame == null) {
/*      */
/* 1311 */       this.lastFrame = getInitialFrame();
/* 1312 */     } else if (this.lastFrame.pc == paramInt1) {
/*      */
/* 1314 */       this.stackMapTableBuffer[--this.stackMapBufferSize] = null;
/* 1315 */       this.lastFrame = this.frameBeforeLast;
/* 1316 */       this.frameBeforeLast = null;
/*      */     }
/*      */
/* 1319 */     StackMapFrame stackMapFrame = new StackMapFrame();
/* 1320 */     stackMapFrame.pc = paramInt1;
/*      */
/* 1322 */     byte b1 = 0;
/* 1323 */     Type[] arrayOfType = new Type[paramInt2]; byte b2;
/* 1324 */     for (b2 = 0; b2 < paramInt2; b2++, b1++) {
/* 1325 */       if (this.state.defined.isMember(b2) && this.lvar[b2] != null) {
/* 1326 */         Type type = (this.lvar[b2]).sym.type;
/* 1327 */         if (!(type instanceof UninitializedType))
/* 1328 */           type = this.types.erasure(type);
/* 1329 */         arrayOfType[b2] = type;
/* 1330 */         if (width(type) > 1) b2++;
/*      */       }
/*      */     }
/* 1333 */     stackMapFrame.locals = new Type[b1]; byte b3;
/* 1334 */     for (b2 = 0, b3 = 0; b2 < paramInt2; b2++, b3++) {
/* 1335 */       Assert.check((b3 < b1));
/* 1336 */       stackMapFrame.locals[b3] = arrayOfType[b2];
/* 1337 */       if (width(arrayOfType[b2]) > 1) b2++;
/*      */
/*      */     }
/* 1340 */     b2 = 0;
/* 1341 */     for (b3 = 0; b3 < this.state.stacksize; b3++) {
/* 1342 */       if (this.state.stack[b3] != null) {
/* 1343 */         b2++;
/*      */       }
/*      */     }
/* 1346 */     stackMapFrame.stack = new Type[b2];
/* 1347 */     b2 = 0;
/* 1348 */     for (b3 = 0; b3 < this.state.stacksize; b3++) {
/* 1349 */       if (this.state.stack[b3] != null) {
/* 1350 */         stackMapFrame.stack[b2++] = this.types.erasure(this.state.stack[b3]);
/*      */       }
/*      */     }
/*      */
/* 1354 */     if (this.stackMapTableBuffer == null) {
/* 1355 */       this.stackMapTableBuffer = new ClassWriter.StackMapTableFrame[20];
/*      */     } else {
/* 1357 */       this.stackMapTableBuffer = (ClassWriter.StackMapTableFrame[])ArrayUtils.ensureCapacity((Object[])this.stackMapTableBuffer, this.stackMapBufferSize);
/*      */     }
/*      */
/*      */
/* 1361 */     this.stackMapTableBuffer[this.stackMapBufferSize++] =
/* 1362 */       ClassWriter.StackMapTableFrame.getInstance(stackMapFrame, this.lastFrame.pc, this.lastFrame.locals, this.types);
/*      */
/* 1364 */     this.frameBeforeLast = this.lastFrame;
/* 1365 */     this.lastFrame = stackMapFrame;
/*      */   }
/*      */
/*      */   StackMapFrame getInitialFrame() {
/* 1369 */     StackMapFrame stackMapFrame = new StackMapFrame();
/* 1370 */     List list = ((Type.MethodType)this.meth.externalType(this.types)).argtypes;
/* 1371 */     int i = list.length();
/* 1372 */     byte b = 0;
/* 1373 */     if (!this.meth.isStatic()) {
/* 1374 */       Type type = this.meth.owner.type;
/* 1375 */       stackMapFrame.locals = new Type[i + 1];
/* 1376 */       if (this.meth.isConstructor() && type != this.syms.objectType) {
/* 1377 */         stackMapFrame.locals[b++] = (Type)UninitializedType.uninitializedThis(type);
/*      */       } else {
/* 1379 */         stackMapFrame.locals[b++] = this.types.erasure(type);
/*      */       }
/*      */     } else {
/* 1382 */       stackMapFrame.locals = new Type[i];
/*      */     }
/* 1384 */     for (Type type : list) {
/* 1385 */       stackMapFrame.locals[b++] = this.types.erasure(type);
/*      */     }
/* 1387 */     stackMapFrame.pc = -1;
/* 1388 */     stackMapFrame.stack = null;
/* 1389 */     return stackMapFrame;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class Chain
/*      */   {
/*      */     public final int pc;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     State state;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public final Chain next;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public Chain(int param1Int, Chain param1Chain, State param1State) {
/* 1420 */       this.pc = param1Int;
/* 1421 */       this.next = param1Chain;
/* 1422 */       this.state = param1State;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static int negate(int paramInt) {
/* 1429 */     if (paramInt == 198) return 199;
/* 1430 */     if (paramInt == 199) return 198;
/* 1431 */     return (paramInt + 1 ^ 0x1) - 1;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public int emitJump(int paramInt) {
/* 1438 */     if (this.fatcode) {
/* 1439 */       if (paramInt == 167 || paramInt == 168) {
/* 1440 */         emitop4(paramInt + 200 - 167, 0);
/*      */       } else {
/* 1442 */         emitop2(negate(paramInt), 8);
/* 1443 */         emitop4(200, 0);
/* 1444 */         this.alive = true;
/* 1445 */         this.pendingStackMap = this.needStackMap;
/*      */       }
/* 1447 */       return this.cp - 5;
/*      */     }
/* 1449 */     emitop2(paramInt, 0);
/* 1450 */     return this.cp - 3;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Chain branch(int paramInt) {
/* 1458 */     Chain chain = null;
/* 1459 */     if (paramInt == 167) {
/* 1460 */       chain = this.pendingJumps;
/* 1461 */       this.pendingJumps = null;
/*      */     }
/* 1463 */     if (paramInt != 168 && isAlive()) {
/*      */
/*      */
/* 1466 */       chain = new Chain(emitJump(paramInt), chain, this.state.dup());
/* 1467 */       this.fixedPc = this.fatcode;
/* 1468 */       if (paramInt == 167) this.alive = false;
/*      */     }
/* 1470 */     return chain;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void resolve(Chain paramChain, int paramInt) {
/* 1476 */     boolean bool = false;
/* 1477 */     State state = this.state;
/* 1478 */     for (; paramChain != null; paramChain = paramChain.next) {
/* 1479 */       Assert.check((this.state != paramChain.state && (paramInt > paramChain.pc || this.state.stacksize == 0)));
/*      */
/* 1481 */       if (paramInt >= this.cp) {
/* 1482 */         paramInt = this.cp;
/* 1483 */       } else if (get1(paramInt) == 167) {
/* 1484 */         if (this.fatcode) { paramInt += get4(paramInt + 1); }
/* 1485 */         else { paramInt += get2(paramInt + 1); }
/*      */
/* 1487 */       }  if (get1(paramChain.pc) == 167 && paramChain.pc + 3 == paramInt && paramInt == this.cp && !this.fixedPc) {
/*      */
/*      */
/*      */
/* 1491 */         if (this.varDebugInfo) {
/* 1492 */           adjustAliveRanges(this.cp, -3);
/*      */         }
/* 1494 */         this.cp -= 3;
/* 1495 */         paramInt -= 3;
/* 1496 */         if (paramChain.next == null) {
/*      */
/*      */
/*      */
/* 1500 */           this.alive = true;
/*      */           break;
/*      */         }
/*      */       } else {
/* 1504 */         if (this.fatcode) {
/* 1505 */           put4(paramChain.pc + 1, paramInt - paramChain.pc);
/* 1506 */         } else if (paramInt - paramChain.pc < -32768 || paramInt - paramChain.pc > 32767) {
/*      */
/* 1508 */           this.fatcode = true;
/*      */         } else {
/* 1510 */           put2(paramChain.pc + 1, paramInt - paramChain.pc);
/* 1511 */         }  Assert.check((!this.alive || (paramChain.state.stacksize == state.stacksize && paramChain.state.nlocks == state.nlocks)));
/*      */       }
/*      */
/*      */
/* 1515 */       this.fixedPc = true;
/* 1516 */       if (this.cp == paramInt) {
/* 1517 */         bool = true;
/* 1518 */         if (this.debugCode)
/* 1519 */           System.err.println("resolving chain state=" + paramChain.state);
/* 1520 */         if (this.alive) {
/* 1521 */           state = paramChain.state.join(state);
/*      */         } else {
/* 1523 */           state = paramChain.state;
/* 1524 */           this.alive = true;
/*      */         }
/*      */       }
/*      */     }
/* 1528 */     Assert.check((!bool || this.state != state));
/* 1529 */     if (this.state != state) {
/* 1530 */       setDefined(state.defined);
/* 1531 */       this.state = state;
/* 1532 */       this.pendingStackMap = this.needStackMap;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void resolve(Chain paramChain) {
/* 1539 */     Assert.check((!this.alive || paramChain == null || (this.state.stacksize == paramChain.state.stacksize && this.state.nlocks == paramChain.state.nlocks)));
/*      */
/*      */
/*      */
/*      */
/* 1544 */     this.pendingJumps = mergeChains(paramChain, this.pendingJumps);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void resolvePending() {
/* 1550 */     Chain chain = this.pendingJumps;
/* 1551 */     this.pendingJumps = null;
/* 1552 */     resolve(chain, this.cp);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static Chain mergeChains(Chain paramChain1, Chain paramChain2) {
/* 1559 */     if (paramChain2 == null) return paramChain1;
/* 1560 */     if (paramChain1 == null) return paramChain2;
/* 1561 */     Assert.check((paramChain1.state.stacksize == paramChain2.state.stacksize && paramChain1.state.nlocks == paramChain2.state.nlocks));
/*      */
/*      */
/* 1564 */     if (paramChain1.pc < paramChain2.pc) {
/* 1565 */       return new Chain(paramChain2.pc,
/*      */
/* 1567 */           mergeChains(paramChain1, paramChain2.next), paramChain2.state);
/*      */     }
/* 1569 */     return new Chain(paramChain1.pc,
/*      */
/* 1571 */         mergeChains(paramChain1.next, paramChain2), paramChain1.state);
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
/*      */   public void addCatch(char paramChar1, char paramChar2, char paramChar3, char paramChar4) {
/* 1584 */     this.catchInfo.append(new char[] { paramChar1, paramChar2, paramChar3, paramChar4 });
/*      */   }
/*      */
/*      */
/*      */   public void compressCatchTable() {
/* 1589 */     ListBuffer<char[]> listBuffer = new ListBuffer();
/* 1590 */     List list = List.nil();
/* 1591 */     for (char[] arrayOfChar : this.catchInfo) {
/* 1592 */       list = list.prepend(Integer.valueOf(arrayOfChar[2]));
/*      */     }
/* 1594 */     for (char[] arrayOfChar : this.catchInfo) {
/* 1595 */       char c1 = arrayOfChar[0];
/* 1596 */       char c2 = arrayOfChar[1];
/* 1597 */       if (c1 == c2 || (c1 == c2 - 1 && list
/*      */
/* 1599 */         .contains(Integer.valueOf(c1)))) {
/*      */         continue;
/*      */       }
/* 1602 */       listBuffer.append(arrayOfChar);
/*      */     }
/*      */
/* 1605 */     this.catchInfo = listBuffer;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void addLineNumber(char paramChar1, char paramChar2) {
/* 1616 */     if (this.lineDebugInfo) {
/* 1617 */       if (this.lineInfo.nonEmpty() && ((char[])this.lineInfo.head)[0] == paramChar1)
/* 1618 */         this.lineInfo = this.lineInfo.tail;
/* 1619 */       if (this.lineInfo.isEmpty() || ((char[])this.lineInfo.head)[1] != paramChar2) {
/* 1620 */         this.lineInfo = this.lineInfo.prepend(new char[] { paramChar1, paramChar2 });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void statBegin(int paramInt) {
/* 1627 */     if (paramInt != -1) {
/* 1628 */       this.pendingStatPos = paramInt;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void markStatBegin() {
/* 1635 */     if (this.alive && this.lineDebugInfo) {
/* 1636 */       int i = this.lineMap.getLineNumber(this.pendingStatPos);
/* 1637 */       char c1 = (char)this.cp;
/* 1638 */       char c2 = (char)i;
/* 1639 */       if (c1 == this.cp && c2 == i)
/* 1640 */         addLineNumber(c1, c2);
/*      */     }
/* 1642 */     this.pendingStatPos = -1;
/*      */   }
/*      */
/*      */
/*      */
/*      */   class State
/*      */     implements Cloneable
/*      */   {
/*      */     Bits defined;
/*      */
/*      */
/*      */     Type[] stack;
/*      */
/*      */
/*      */     int stacksize;
/*      */
/*      */
/*      */     int[] locks;
/*      */
/*      */     int nlocks;
/*      */
/*      */
/*      */     State() {
/* 1665 */       this.defined = new Bits();
/* 1666 */       this.stack = new Type[16];
/*      */     }
/*      */
/*      */     State dup() {
/*      */       try {
/* 1671 */         State state = (State)clone();
/* 1672 */         state.defined = new Bits(this.defined);
/* 1673 */         state.stack = (Type[])this.stack.clone();
/* 1674 */         if (this.locks != null) state.locks = (int[])this.locks.clone();
/* 1675 */         if (Code.this.debugCode) {
/* 1676 */           System.err.println("duping state " + this);
/* 1677 */           dump();
/*      */         }
/* 1679 */         return state;
/* 1680 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 1681 */         throw new AssertionError(cloneNotSupportedException);
/*      */       }
/*      */     }
/*      */
/*      */     void lock(int param1Int) {
/* 1686 */       if (this.locks == null) {
/* 1687 */         this.locks = new int[20];
/*      */       } else {
/* 1689 */         this.locks = ArrayUtils.ensureCapacity(this.locks, this.nlocks);
/*      */       }
/* 1691 */       this.locks[this.nlocks] = param1Int;
/* 1692 */       this.nlocks++;
/*      */     }
/*      */
/*      */     void unlock(int param1Int) {
/* 1696 */       this.nlocks--;
/* 1697 */       Assert.check((this.locks[this.nlocks] == param1Int));
/* 1698 */       this.locks[this.nlocks] = -1;
/*      */     }
/*      */     void push(Type param1Type) {
/*      */       Type.JCPrimitiveType jCPrimitiveType;
/* 1702 */       if (Code.this.debugCode) System.err.println("   pushing " + param1Type);
/* 1703 */       switch (param1Type.getTag()) {
/*      */         case VOID:
/*      */           return;
/*      */         case BYTE:
/*      */         case SHORT:
/*      */         case CHAR:
/*      */         case BOOLEAN:
/* 1710 */           jCPrimitiveType = Code.this.syms.intType;
/*      */           break;
/*      */       }
/*      */
/*      */
/* 1715 */       this.stack = (Type[])ArrayUtils.ensureCapacity((Object[])this.stack, this.stacksize + 2);
/* 1716 */       this.stack[this.stacksize++] = (Type)jCPrimitiveType;
/* 1717 */       switch (Code.width((Type)jCPrimitiveType)) {
/*      */         case 1:
/*      */           break;
/*      */         case 2:
/* 1721 */           this.stack[this.stacksize++] = null;
/*      */           break;
/*      */         default:
/* 1724 */           throw new AssertionError(jCPrimitiveType);
/*      */       }
/* 1726 */       if (this.stacksize > Code.this.max_stack)
/* 1727 */         Code.this.max_stack = this.stacksize;
/*      */     }
/*      */
/*      */     Type pop1() {
/* 1731 */       if (Code.this.debugCode) System.err.println("   popping 1");
/* 1732 */       this.stacksize--;
/* 1733 */       Type type = this.stack[this.stacksize];
/* 1734 */       this.stack[this.stacksize] = null;
/* 1735 */       Assert.check((type != null && Code.width(type) == 1));
/* 1736 */       return type;
/*      */     }
/*      */
/*      */     Type peek() {
/* 1740 */       return this.stack[this.stacksize - 1];
/*      */     }
/*      */
/*      */     Type pop2() {
/* 1744 */       if (Code.this.debugCode) System.err.println("   popping 2");
/* 1745 */       this.stacksize -= 2;
/* 1746 */       Type type = this.stack[this.stacksize];
/* 1747 */       this.stack[this.stacksize] = null;
/* 1748 */       Assert.check((this.stack[this.stacksize + 1] == null && type != null &&
/* 1749 */           Code.width(type) == 2));
/* 1750 */       return type;
/*      */     }
/*      */
/*      */     void pop(int param1Int) {
/* 1754 */       if (Code.this.debugCode) System.err.println("   popping " + param1Int);
/* 1755 */       while (param1Int > 0) {
/* 1756 */         this.stack[--this.stacksize] = null;
/* 1757 */         param1Int--;
/*      */       }
/*      */     }
/*      */
/*      */     void pop(Type param1Type) {
/* 1762 */       pop(Code.width(param1Type));
/*      */     }
/*      */
/*      */     void forceStackTop(Type param1Type) {
/*      */       int i;
/*      */       Type type;
/* 1768 */       if (!Code.this.alive)
/* 1769 */         return;  switch (param1Type.getTag()) {
/*      */         case CLASS:
/*      */         case ARRAY:
/* 1772 */           i = Code.width(param1Type);
/* 1773 */           type = this.stack[this.stacksize - i];
/* 1774 */           Assert.check(Code.this.types.isSubtype(Code.this.types.erasure(type), Code.this.types
/* 1775 */                 .erasure(param1Type)));
/* 1776 */           this.stack[this.stacksize - i] = param1Type;
/*      */           break;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void markInitialized(UninitializedType param1UninitializedType) {
/* 1783 */       Type type = param1UninitializedType.initializedType(); byte b;
/* 1784 */       for (b = 0; b < this.stacksize; b++) {
/* 1785 */         if (this.stack[b] == param1UninitializedType) this.stack[b] = type;
/*      */       }
/* 1787 */       for (b = 0; b < Code.this.lvar.length; b++) {
/* 1788 */         LocalVar localVar = Code.this.lvar[b];
/* 1789 */         if (localVar != null && localVar.sym.type == param1UninitializedType) {
/* 1790 */           Symbol.VarSymbol varSymbol = localVar.sym;
/* 1791 */           varSymbol = varSymbol.clone(varSymbol.owner);
/* 1792 */           varSymbol.type = type;
/* 1793 */           LocalVar localVar1 = Code.this.lvar[b] = new LocalVar(varSymbol);
/* 1794 */           localVar1.aliveRanges = localVar.aliveRanges;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     State join(State param1State) {
/* 1800 */       this.defined.andSet(param1State.defined);
/* 1801 */       Assert.check((this.stacksize == param1State.stacksize && this.nlocks == param1State.nlocks));
/*      */
/* 1803 */       for (int i = 0; i < this.stacksize; ) {
/* 1804 */         Type type1 = this.stack[i];
/* 1805 */         Type type2 = param1State.stack[i];
/*      */
/*      */
/*      */
/*      */
/* 1810 */         Type type3 = (type1 == type2) ? type1 : (Code.this.types.isSubtype(type1, type2) ? type2 : (Code.this.types.isSubtype(type2, type1) ? type1 : error()));
/* 1811 */         int j = Code.width(type3);
/* 1812 */         this.stack[i] = type3;
/* 1813 */         if (j == 2) Assert.checkNull(this.stack[i + 1]);
/* 1814 */         i += j;
/*      */       }
/* 1816 */       return this;
/*      */     }
/*      */
/*      */     Type error() {
/* 1820 */       throw new AssertionError("inconsistent stack types at join point");
/*      */     }
/*      */
/*      */     void dump() {
/* 1824 */       dump(-1);
/*      */     }
/*      */
/*      */     void dump(int param1Int) {
/* 1828 */       System.err.print("stackMap for " + Code.this.meth.owner + "." + Code.this.meth);
/* 1829 */       if (param1Int == -1) {
/* 1830 */         System.out.println();
/*      */       } else {
/* 1832 */         System.out.println(" at " + param1Int);
/* 1833 */       }  System.err.println(" stack (from bottom):"); int i;
/* 1834 */       for (i = 0; i < this.stacksize; i++) {
/* 1835 */         System.err.println("  " + i + ": " + this.stack[i]);
/*      */       }
/* 1837 */       i = 0; int j;
/* 1838 */       for (j = Code.this.max_locals - 1; j >= 0; j--) {
/* 1839 */         if (this.defined.isMember(j)) {
/* 1840 */           i = j;
/*      */           break;
/*      */         }
/*      */       }
/* 1844 */       if (i >= 0)
/* 1845 */         System.err.println(" locals:");
/* 1846 */       for (j = 0; j <= i; j++) {
/* 1847 */         System.err.print("  " + j + ": ");
/* 1848 */         if (this.defined.isMember(j))
/* 1849 */         { LocalVar localVar = Code.this.lvar[j];
/* 1850 */           if (localVar == null) {
/* 1851 */             System.err.println("(none)");
/* 1852 */           } else if (localVar.sym == null) {
/* 1853 */             System.err.println("UNKNOWN!");
/*      */           } else {
/* 1855 */             System.err.println("" + localVar.sym + " of type " + localVar.sym
/* 1856 */                 .erasure(Code.this.types));
/*      */           }  }
/* 1858 */         else { System.err.println("undefined"); }
/*      */
/*      */       }
/* 1861 */       if (this.nlocks != 0) {
/* 1862 */         System.err.print(" locks:");
/* 1863 */         for (j = 0; j < this.nlocks; j++) {
/* 1864 */           System.err.print(" " + this.locks[j]);
/*      */         }
/* 1866 */         System.err.println();
/*      */       }
/*      */     }
/*      */   }
/*      */
/* 1871 */   static final Type jsrReturnValue = (Type)new Type.JCPrimitiveType(TypeTag.INT, null);
/*      */
/*      */   LocalVar[] lvar;
/*      */   LocalVar[] varBuffer;
/*      */   int varBufferSize;
/*      */
/*      */   static class LocalVar
/*      */   {
/*      */     final Symbol.VarSymbol sym;
/*      */     final char reg;
/*      */
/*      */     class Range
/*      */     {
/* 1884 */       char start_pc = Character.MAX_VALUE;
/* 1885 */       char length = Character.MAX_VALUE;
/*      */
/*      */       Range() {}
/*      */
/*      */       Range(char param2Char) {
/* 1890 */         this.start_pc = param2Char;
/*      */       }
/*      */
/*      */       Range(char param2Char1, char param2Char2) {
/* 1894 */         this.start_pc = param2Char1;
/* 1895 */         this.length = param2Char2;
/*      */       }
/*      */
/*      */       boolean closed() {
/* 1899 */         return (this.start_pc != Character.MAX_VALUE && this.length != Character.MAX_VALUE);
/*      */       }
/*      */
/*      */
/*      */       public String toString() {
/* 1904 */         char c1 = this.start_pc;
/* 1905 */         char c2 = this.length;
/* 1906 */         return "startpc = " + c1 + " length " + c2;
/*      */       }
/*      */     }
/*      */
/* 1910 */     List<Range> aliveRanges = new ArrayList<>();
/*      */
/*      */     LocalVar(Symbol.VarSymbol param1VarSymbol) {
/* 1913 */       this.sym = param1VarSymbol;
/* 1914 */       this.reg = (char)param1VarSymbol.adr;
/*      */     }
/*      */     public LocalVar dup() {
/* 1917 */       return new LocalVar(this.sym);
/*      */     }
/*      */
/*      */     Range firstRange() {
/* 1921 */       return this.aliveRanges.isEmpty() ? null : this.aliveRanges.get(0);
/*      */     }
/*      */
/*      */     Range lastRange() {
/* 1925 */       return this.aliveRanges.isEmpty() ? null : this.aliveRanges.get(this.aliveRanges.size() - 1);
/*      */     }
/*      */
/*      */     void removeLastRange() {
/* 1929 */       Range range = lastRange();
/* 1930 */       if (range != null) {
/* 1931 */         this.aliveRanges.remove(range);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public String toString() {
/* 1937 */       if (this.aliveRanges == null) {
/* 1938 */         return "empty local var";
/*      */       }
/*      */
/* 1941 */       StringBuilder stringBuilder = (new StringBuilder()).append(this.sym).append(" in register ").append(this.reg).append(" \n");
/* 1942 */       for (Range range : this.aliveRanges) {
/* 1943 */         stringBuilder.append(" starts at pc=").append(Integer.toString(range.start_pc))
/* 1944 */           .append(" length=").append(Integer.toString(range.length))
/* 1945 */           .append("\n");
/*      */       }
/* 1947 */       return stringBuilder.toString();
/*      */     }
/*      */
/*      */     public void openRange(char param1Char) {
/* 1951 */       if (!hasOpenRange()) {
/* 1952 */         this.aliveRanges.add(new Range(param1Char));
/*      */       }
/*      */     }
/*      */
/*      */     public void closeRange(char param1Char) {
/* 1957 */       if (isLastRangeInitialized() && param1Char > '\000') {
/* 1958 */         Range range = lastRange();
/* 1959 */         if (range != null &&
/* 1960 */           range.length == Character.MAX_VALUE) {
/* 1961 */           range.length = param1Char;
/*      */         }
/*      */       } else {
/*      */
/* 1965 */         removeLastRange();
/*      */       }
/*      */     }
/*      */
/*      */     public boolean hasOpenRange() {
/* 1970 */       if (this.aliveRanges.isEmpty()) {
/* 1971 */         return false;
/*      */       }
/* 1973 */       return ((lastRange()).length == Character.MAX_VALUE);
/*      */     }
/*      */
/*      */     public boolean isLastRangeInitialized() {
/* 1977 */       if (this.aliveRanges.isEmpty()) {
/* 1978 */         return false;
/*      */       }
/* 1980 */       return ((lastRange()).start_pc != Character.MAX_VALUE);
/*      */     }
/*      */
/*      */     public Range getWidestRange() {
/* 1984 */       if (this.aliveRanges.isEmpty()) {
/* 1985 */         return new Range();
/*      */       }
/* 1987 */       Range range1 = firstRange();
/* 1988 */       Range range2 = lastRange();
/* 1989 */       char c = (char)(range2.length + range2.start_pc - range1.start_pc);
/* 1990 */       return new Range(range1.start_pc, c);
/*      */     } } class Range {
/*      */     char start_pc = Character.MAX_VALUE; char length = Character.MAX_VALUE; Range() {} Range(char param1Char) { this.start_pc = param1Char; } Range(char param1Char1, char param1Char2) { this.start_pc = param1Char1;
/*      */       this.length = param1Char2; } boolean closed() {
/*      */       return (this.start_pc != Character.MAX_VALUE && this.length != Character.MAX_VALUE);
/*      */     } public String toString() {
/*      */       char c1 = this.start_pc;
/*      */       char c2 = this.length;
/*      */       return "startpc = " + c1 + " length " + c2;
/*      */     }
/*      */   } private void addLocalVar(Symbol.VarSymbol paramVarSymbol) {
/* 2001 */     int i = paramVarSymbol.adr;
/* 2002 */     this.lvar = (LocalVar[])ArrayUtils.ensureCapacity((Object[])this.lvar, i + 1);
/* 2003 */     Assert.checkNull(this.lvar[i]);
/* 2004 */     if (this.pendingJumps != null) {
/* 2005 */       resolvePending();
/*      */     }
/* 2007 */     this.lvar[i] = new LocalVar(paramVarSymbol);
/* 2008 */     this.state.defined.excl(i);
/*      */   }
/*      */
/*      */   void adjustAliveRanges(int paramInt1, int paramInt2) {
/* 2012 */     for (LocalVar localVar : this.lvar) {
/* 2013 */       if (localVar != null) {
/* 2014 */         for (LocalVar.Range range : localVar.aliveRanges) {
/* 2015 */           if (range.closed() && range.start_pc + range.length >= paramInt1) {
/* 2016 */             range.length = (char)(range.length + paramInt2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public int getLVTSize() {
/* 2027 */     int i = this.varBufferSize;
/* 2028 */     for (byte b = 0; b < this.varBufferSize; b++) {
/* 2029 */       LocalVar localVar = this.varBuffer[b];
/* 2030 */       i += localVar.aliveRanges.size() - 1;
/*      */     }
/* 2032 */     return i;
/*      */   }
/*      */
/*      */
/*      */   public void setDefined(Bits paramBits) {
/* 2037 */     if (this.alive && paramBits != this.state.defined) {
/* 2038 */       Bits bits = (new Bits(this.state.defined)).xorSet(paramBits);
/* 2039 */       int i = bits.nextBit(0);
/* 2040 */       for (; i >= 0;
/* 2041 */         i = bits.nextBit(i + 1)) {
/* 2042 */         if (i >= this.nextreg) {
/* 2043 */           this.state.defined.excl(i);
/* 2044 */         } else if (this.state.defined.isMember(i)) {
/* 2045 */           setUndefined(i);
/*      */         } else {
/* 2047 */           setDefined(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   public void setDefined(int paramInt) {
/* 2054 */     LocalVar localVar = this.lvar[paramInt];
/* 2055 */     if (localVar == null) {
/* 2056 */       this.state.defined.excl(paramInt);
/*      */     } else {
/* 2058 */       this.state.defined.incl(paramInt);
/* 2059 */       if (this.cp < 65535) {
/* 2060 */         localVar.openRange((char)this.cp);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void setUndefined(int paramInt) {
/* 2067 */     this.state.defined.excl(paramInt);
/* 2068 */     if (paramInt < this.lvar.length && this.lvar[paramInt] != null && this.lvar[paramInt]
/*      */
/* 2070 */       .isLastRangeInitialized()) {
/* 2071 */       LocalVar localVar = this.lvar[paramInt];
/* 2072 */       char c = (char)(curCP() - (localVar.lastRange()).start_pc);
/* 2073 */       if (c < Character.MAX_VALUE) {
/* 2074 */         this.lvar[paramInt] = localVar.dup();
/* 2075 */         localVar.closeRange(c);
/* 2076 */         putVar(localVar);
/*      */       } else {
/* 2078 */         localVar.removeLastRange();
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void endScope(int paramInt) {
/* 2085 */     LocalVar localVar = this.lvar[paramInt];
/* 2086 */     if (localVar != null) {
/* 2087 */       if (localVar.isLastRangeInitialized()) {
/* 2088 */         char c = (char)(curCP() - (localVar.lastRange()).start_pc);
/* 2089 */         if (c < Character.MAX_VALUE) {
/* 2090 */           localVar.closeRange(c);
/* 2091 */           putVar(localVar);
/* 2092 */           fillLocalVarPosition(localVar);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2100 */       this.lvar[paramInt] = null;
/*      */     }
/* 2102 */     this.state.defined.excl(paramInt);
/*      */   }
/*      */
/*      */   private void fillLocalVarPosition(LocalVar paramLocalVar) {
/* 2106 */     if (paramLocalVar == null || paramLocalVar.sym == null || !paramLocalVar.sym.hasTypeAnnotations())
/*      */       return;
/* 2108 */     for (Attribute.TypeCompound typeCompound : paramLocalVar.sym.getRawTypeAttributes()) {
/* 2109 */       TypeAnnotationPosition typeAnnotationPosition = typeCompound.position;
/* 2110 */       LocalVar.Range range = paramLocalVar.getWidestRange();
/* 2111 */       typeAnnotationPosition.lvarOffset = new int[] { range.start_pc };
/* 2112 */       typeAnnotationPosition.lvarLength = new int[] { range.length };
/* 2113 */       typeAnnotationPosition.lvarIndex = new int[] { paramLocalVar.reg };
/* 2114 */       typeAnnotationPosition.isValidOffset = true;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void fillExceptionParameterPositions() {
/* 2122 */     for (byte b = 0; b < this.varBufferSize; b++) {
/* 2123 */       LocalVar localVar = this.varBuffer[b];
/* 2124 */       if (localVar != null && localVar.sym != null && localVar.sym
/* 2125 */         .hasTypeAnnotations() && localVar.sym
/* 2126 */         .isExceptionParameter())
/*      */       {
/*      */
/* 2129 */         for (Attribute.TypeCompound typeCompound : localVar.sym.getRawTypeAttributes()) {
/* 2130 */           TypeAnnotationPosition typeAnnotationPosition = typeCompound.position;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2137 */           if (typeAnnotationPosition.type_index != -666) {
/* 2138 */             typeAnnotationPosition.exception_index = findExceptionIndex(typeAnnotationPosition.type_index);
/* 2139 */             typeAnnotationPosition.type_index = -666;
/*      */           }
/*      */         }  }
/*      */     }
/*      */   }
/*      */
/*      */   private int findExceptionIndex(int paramInt) {
/* 2146 */     if (paramInt == Integer.MIN_VALUE)
/*      */     {
/*      */
/*      */
/* 2150 */       return -1;
/*      */     }
/* 2152 */     List list = this.catchInfo.toList();
/* 2153 */     int i = this.catchInfo.length();
/* 2154 */     for (byte b = 0; b < i; b++) {
/* 2155 */       char[] arrayOfChar = (char[])list.head;
/* 2156 */       list = list.tail;
/* 2157 */       char c = arrayOfChar[3];
/* 2158 */       if (paramInt == c) {
/* 2159 */         return b;
/*      */       }
/*      */     }
/* 2162 */     return -1;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void putVar(LocalVar paramLocalVar) {
/* 2173 */     boolean bool1 = (this.varDebugInfo || (paramLocalVar.sym.isExceptionParameter() && paramLocalVar.sym.hasTypeAnnotations())) ? true : false;
/* 2174 */     if (!bool1) {
/*      */       return;
/*      */     }
/*      */
/* 2178 */     boolean bool2 = ((paramLocalVar.sym.flags() & 0x1000L) != 0L && ((paramLocalVar.sym.owner.flags() & 0x2000000000000L) == 0L || (paramLocalVar.sym.flags() & 0x200000000L) == 0L)) ? true : false;
/* 2179 */     if (bool2)
/* 2180 */       return;  if (this.varBuffer == null) {
/* 2181 */       this.varBuffer = new LocalVar[20];
/*      */     } else {
/* 2183 */       this.varBuffer = (LocalVar[])ArrayUtils.ensureCapacity((Object[])this.varBuffer, this.varBufferSize);
/* 2184 */     }  this.varBuffer[this.varBufferSize++] = paramLocalVar;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private int newLocal(int paramInt) {
/* 2194 */     int i = this.nextreg;
/* 2195 */     int j = width(paramInt);
/* 2196 */     this.nextreg = i + j;
/* 2197 */     if (this.nextreg > this.max_locals) this.max_locals = this.nextreg;
/* 2198 */     return i;
/*      */   }
/*      */
/*      */   private int newLocal(Type paramType) {
/* 2202 */     return newLocal(typecode(paramType));
/*      */   }
/*      */
/*      */   public int newLocal(Symbol.VarSymbol paramVarSymbol) {
/* 2206 */     int i = paramVarSymbol.adr = newLocal(paramVarSymbol.erasure(this.types));
/* 2207 */     addLocalVar(paramVarSymbol);
/* 2208 */     return i;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void newRegSegment() {
/* 2214 */     this.nextreg = this.max_locals;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void endScopes(int paramInt) {
/* 2220 */     int i = this.nextreg;
/* 2221 */     this.nextreg = paramInt;
/* 2222 */     for (int j = this.nextreg; j < i; ) { endScope(j); j++; }
/*      */
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static String mnem(int paramInt) {
/* 2230 */     return Mneumonics.mnem[paramInt];
/*      */   }
/*      */
/*      */   private static class Mneumonics {
/* 2234 */     private static final String[] mnem = new String[203];
/*      */     static {
/* 2236 */       mnem[0] = "nop";
/* 2237 */       mnem[1] = "aconst_null";
/* 2238 */       mnem[2] = "iconst_m1";
/* 2239 */       mnem[3] = "iconst_0";
/* 2240 */       mnem[4] = "iconst_1";
/* 2241 */       mnem[5] = "iconst_2";
/* 2242 */       mnem[6] = "iconst_3";
/* 2243 */       mnem[7] = "iconst_4";
/* 2244 */       mnem[8] = "iconst_5";
/* 2245 */       mnem[9] = "lconst_0";
/* 2246 */       mnem[10] = "lconst_1";
/* 2247 */       mnem[11] = "fconst_0";
/* 2248 */       mnem[12] = "fconst_1";
/* 2249 */       mnem[13] = "fconst_2";
/* 2250 */       mnem[14] = "dconst_0";
/* 2251 */       mnem[15] = "dconst_1";
/* 2252 */       mnem[16] = "bipush";
/* 2253 */       mnem[17] = "sipush";
/* 2254 */       mnem[18] = "ldc1";
/* 2255 */       mnem[19] = "ldc2";
/* 2256 */       mnem[20] = "ldc2w";
/* 2257 */       mnem[21] = "iload";
/* 2258 */       mnem[22] = "lload";
/* 2259 */       mnem[23] = "fload";
/* 2260 */       mnem[24] = "dload";
/* 2261 */       mnem[25] = "aload";
/* 2262 */       mnem[26] = "iload_0";
/* 2263 */       mnem[30] = "lload_0";
/* 2264 */       mnem[34] = "fload_0";
/* 2265 */       mnem[38] = "dload_0";
/* 2266 */       mnem[42] = "aload_0";
/* 2267 */       mnem[27] = "iload_1";
/* 2268 */       mnem[31] = "lload_1";
/* 2269 */       mnem[35] = "fload_1";
/* 2270 */       mnem[39] = "dload_1";
/* 2271 */       mnem[43] = "aload_1";
/* 2272 */       mnem[28] = "iload_2";
/* 2273 */       mnem[32] = "lload_2";
/* 2274 */       mnem[36] = "fload_2";
/* 2275 */       mnem[40] = "dload_2";
/* 2276 */       mnem[44] = "aload_2";
/* 2277 */       mnem[29] = "iload_3";
/* 2278 */       mnem[33] = "lload_3";
/* 2279 */       mnem[37] = "fload_3";
/* 2280 */       mnem[41] = "dload_3";
/* 2281 */       mnem[45] = "aload_3";
/* 2282 */       mnem[46] = "iaload";
/* 2283 */       mnem[47] = "laload";
/* 2284 */       mnem[48] = "faload";
/* 2285 */       mnem[49] = "daload";
/* 2286 */       mnem[50] = "aaload";
/* 2287 */       mnem[51] = "baload";
/* 2288 */       mnem[52] = "caload";
/* 2289 */       mnem[53] = "saload";
/* 2290 */       mnem[54] = "istore";
/* 2291 */       mnem[55] = "lstore";
/* 2292 */       mnem[56] = "fstore";
/* 2293 */       mnem[57] = "dstore";
/* 2294 */       mnem[58] = "astore";
/* 2295 */       mnem[59] = "istore_0";
/* 2296 */       mnem[63] = "lstore_0";
/* 2297 */       mnem[67] = "fstore_0";
/* 2298 */       mnem[71] = "dstore_0";
/* 2299 */       mnem[75] = "astore_0";
/* 2300 */       mnem[60] = "istore_1";
/* 2301 */       mnem[64] = "lstore_1";
/* 2302 */       mnem[68] = "fstore_1";
/* 2303 */       mnem[72] = "dstore_1";
/* 2304 */       mnem[76] = "astore_1";
/* 2305 */       mnem[61] = "istore_2";
/* 2306 */       mnem[65] = "lstore_2";
/* 2307 */       mnem[69] = "fstore_2";
/* 2308 */       mnem[73] = "dstore_2";
/* 2309 */       mnem[77] = "astore_2";
/* 2310 */       mnem[62] = "istore_3";
/* 2311 */       mnem[66] = "lstore_3";
/* 2312 */       mnem[70] = "fstore_3";
/* 2313 */       mnem[74] = "dstore_3";
/* 2314 */       mnem[78] = "astore_3";
/* 2315 */       mnem[79] = "iastore";
/* 2316 */       mnem[80] = "lastore";
/* 2317 */       mnem[81] = "fastore";
/* 2318 */       mnem[82] = "dastore";
/* 2319 */       mnem[83] = "aastore";
/* 2320 */       mnem[84] = "bastore";
/* 2321 */       mnem[85] = "castore";
/* 2322 */       mnem[86] = "sastore";
/* 2323 */       mnem[87] = "pop";
/* 2324 */       mnem[88] = "pop2";
/* 2325 */       mnem[89] = "dup";
/* 2326 */       mnem[90] = "dup_x1";
/* 2327 */       mnem[91] = "dup_x2";
/* 2328 */       mnem[92] = "dup2";
/* 2329 */       mnem[93] = "dup2_x1";
/* 2330 */       mnem[94] = "dup2_x2";
/* 2331 */       mnem[95] = "swap";
/* 2332 */       mnem[96] = "iadd";
/* 2333 */       mnem[97] = "ladd";
/* 2334 */       mnem[98] = "fadd";
/* 2335 */       mnem[99] = "dadd";
/* 2336 */       mnem[100] = "isub";
/* 2337 */       mnem[101] = "lsub";
/* 2338 */       mnem[102] = "fsub";
/* 2339 */       mnem[103] = "dsub";
/* 2340 */       mnem[104] = "imul";
/* 2341 */       mnem[105] = "lmul";
/* 2342 */       mnem[106] = "fmul";
/* 2343 */       mnem[107] = "dmul";
/* 2344 */       mnem[108] = "idiv";
/* 2345 */       mnem[109] = "ldiv";
/* 2346 */       mnem[110] = "fdiv";
/* 2347 */       mnem[111] = "ddiv";
/* 2348 */       mnem[112] = "imod";
/* 2349 */       mnem[113] = "lmod";
/* 2350 */       mnem[114] = "fmod";
/* 2351 */       mnem[115] = "dmod";
/* 2352 */       mnem[116] = "ineg";
/* 2353 */       mnem[117] = "lneg";
/* 2354 */       mnem[118] = "fneg";
/* 2355 */       mnem[119] = "dneg";
/* 2356 */       mnem[120] = "ishl";
/* 2357 */       mnem[121] = "lshl";
/* 2358 */       mnem[122] = "ishr";
/* 2359 */       mnem[123] = "lshr";
/* 2360 */       mnem[124] = "iushr";
/* 2361 */       mnem[125] = "lushr";
/* 2362 */       mnem[126] = "iand";
/* 2363 */       mnem[127] = "land";
/* 2364 */       mnem[128] = "ior";
/* 2365 */       mnem[129] = "lor";
/* 2366 */       mnem[130] = "ixor";
/* 2367 */       mnem[131] = "lxor";
/* 2368 */       mnem[132] = "iinc";
/* 2369 */       mnem[133] = "i2l";
/* 2370 */       mnem[134] = "i2f";
/* 2371 */       mnem[135] = "i2d";
/* 2372 */       mnem[136] = "l2i";
/* 2373 */       mnem[137] = "l2f";
/* 2374 */       mnem[138] = "l2d";
/* 2375 */       mnem[139] = "f2i";
/* 2376 */       mnem[140] = "f2l";
/* 2377 */       mnem[141] = "f2d";
/* 2378 */       mnem[142] = "d2i";
/* 2379 */       mnem[143] = "d2l";
/* 2380 */       mnem[144] = "d2f";
/* 2381 */       mnem[145] = "int2byte";
/* 2382 */       mnem[146] = "int2char";
/* 2383 */       mnem[147] = "int2short";
/* 2384 */       mnem[148] = "lcmp";
/* 2385 */       mnem[149] = "fcmpl";
/* 2386 */       mnem[150] = "fcmpg";
/* 2387 */       mnem[151] = "dcmpl";
/* 2388 */       mnem[152] = "dcmpg";
/* 2389 */       mnem[153] = "ifeq";
/* 2390 */       mnem[154] = "ifne";
/* 2391 */       mnem[155] = "iflt";
/* 2392 */       mnem[156] = "ifge";
/* 2393 */       mnem[157] = "ifgt";
/* 2394 */       mnem[158] = "ifle";
/* 2395 */       mnem[159] = "if_icmpeq";
/* 2396 */       mnem[160] = "if_icmpne";
/* 2397 */       mnem[161] = "if_icmplt";
/* 2398 */       mnem[162] = "if_icmpge";
/* 2399 */       mnem[163] = "if_icmpgt";
/* 2400 */       mnem[164] = "if_icmple";
/* 2401 */       mnem[165] = "if_acmpeq";
/* 2402 */       mnem[166] = "if_acmpne";
/* 2403 */       mnem[167] = "goto_";
/* 2404 */       mnem[168] = "jsr";
/* 2405 */       mnem[169] = "ret";
/* 2406 */       mnem[170] = "tableswitch";
/* 2407 */       mnem[171] = "lookupswitch";
/* 2408 */       mnem[172] = "ireturn";
/* 2409 */       mnem[173] = "lreturn";
/* 2410 */       mnem[174] = "freturn";
/* 2411 */       mnem[175] = "dreturn";
/* 2412 */       mnem[176] = "areturn";
/* 2413 */       mnem[177] = "return_";
/* 2414 */       mnem[178] = "getstatic";
/* 2415 */       mnem[179] = "putstatic";
/* 2416 */       mnem[180] = "getfield";
/* 2417 */       mnem[181] = "putfield";
/* 2418 */       mnem[182] = "invokevirtual";
/* 2419 */       mnem[183] = "invokespecial";
/* 2420 */       mnem[184] = "invokestatic";
/* 2421 */       mnem[185] = "invokeinterface";
/* 2422 */       mnem[186] = "invokedynamic";
/* 2423 */       mnem[187] = "new_";
/* 2424 */       mnem[188] = "newarray";
/* 2425 */       mnem[189] = "anewarray";
/* 2426 */       mnem[190] = "arraylength";
/* 2427 */       mnem[191] = "athrow";
/* 2428 */       mnem[192] = "checkcast";
/* 2429 */       mnem[193] = "instanceof_";
/* 2430 */       mnem[194] = "monitorenter";
/* 2431 */       mnem[195] = "monitorexit";
/* 2432 */       mnem[196] = "wide";
/* 2433 */       mnem[197] = "multianewarray";
/* 2434 */       mnem[198] = "if_acmp_null";
/* 2435 */       mnem[199] = "if_acmp_nonnull";
/* 2436 */       mnem[200] = "goto_w";
/* 2437 */       mnem[201] = "jsr_w";
/* 2438 */       mnem[202] = "breakpoint";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Code.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
