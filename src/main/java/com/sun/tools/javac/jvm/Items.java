/*     */ package com.sun.tools.javac.jvm;
/*     */
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Items
/*     */ {
/*     */   Pool pool;
/*     */   Code code;
/*     */   Symtab syms;
/*     */   Types types;
/*     */   private final Item voidItem;
/*     */   private final Item thisItem;
/*     */   private final Item superItem;
/*  73 */   private final Item[] stackItem = new Item[9];
/*     */
/*     */   public Items(Pool paramPool, Code paramCode, Symtab paramSymtab, Types paramTypes) {
/*  76 */     this.code = paramCode;
/*  77 */     this.pool = paramPool;
/*  78 */     this.types = paramTypes;
/*  79 */     this.voidItem = new Item(8) {
/*  80 */         public String toString() { return "void"; }
/*     */       };
/*  82 */     this.thisItem = new SelfItem(false);
/*  83 */     this.superItem = new SelfItem(true);
/*  84 */     for (byte b = 0; b < 8; ) { this.stackItem[b] = new StackItem(b); b++; }
/*  85 */      this.stackItem[8] = this.voidItem;
/*  86 */     this.syms = paramSymtab;
/*     */   }
/*     */
/*     */
/*     */
/*     */   Item makeVoidItem() {
/*  92 */     return this.voidItem;
/*     */   }
/*     */
/*     */
/*     */   Item makeThisItem() {
/*  97 */     return this.thisItem;
/*     */   }
/*     */
/*     */
/*     */
/*     */   Item makeSuperItem() {
/* 103 */     return this.superItem;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeStackItem(Type paramType) {
/* 110 */     return this.stackItem[Code.typecode(paramType)];
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeDynamicItem(Symbol paramSymbol) {
/* 117 */     return new DynamicItem(paramSymbol);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeIndexedItem(Type paramType) {
/* 124 */     return new IndexedItem(paramType);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   LocalItem makeLocalItem(Symbol.VarSymbol paramVarSymbol) {
/* 131 */     return new LocalItem(paramVarSymbol.erasure(this.types), paramVarSymbol.adr);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private LocalItem makeLocalItem(Type paramType, int paramInt) {
/* 139 */     return new LocalItem(paramType, paramInt);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeStaticItem(Symbol paramSymbol) {
/* 146 */     return new StaticItem(paramSymbol);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeMemberItem(Symbol paramSymbol, boolean paramBoolean) {
/* 155 */     return new MemberItem(paramSymbol, paramBoolean);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeImmediateItem(Type paramType, Object paramObject) {
/* 163 */     return new ImmediateItem(paramType, paramObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Item makeAssignItem(Item paramItem) {
/* 170 */     return new AssignItem(paramItem);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   CondItem makeCondItem(int paramInt, Code.Chain paramChain1, Code.Chain paramChain2) {
/* 181 */     return new CondItem(paramInt, paramChain1, paramChain2);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   CondItem makeCondItem(int paramInt) {
/* 188 */     return makeCondItem(paramInt, null, null);
/*     */   }
/*     */
/*     */
/*     */
/*     */   abstract class Item
/*     */   {
/*     */     int typecode;
/*     */
/*     */
/*     */
/*     */     Item(int param1Int) {
/* 200 */       this.typecode = param1Int;
/*     */     }
/*     */
/*     */
/*     */
/*     */     Item load() {
/* 206 */       throw new AssertionError();
/*     */     }
/*     */
/*     */
/*     */
/*     */     void store() {
/* 212 */       throw new AssertionError("store unsupported: " + this);
/*     */     }
/*     */
/*     */
/*     */
/*     */     Item invoke() {
/* 218 */       throw new AssertionError(this);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     void duplicate() {}
/*     */
/*     */
/*     */
/*     */     void drop() {}
/*     */
/*     */
/*     */
/*     */     void stash(int param1Int) {
/* 233 */       Items.this.stackItem[param1Int].duplicate();
/*     */     }
/*     */
/*     */
/*     */
/*     */     CondItem mkCond() {
/* 239 */       load();
/* 240 */       return Items.this.makeCondItem(154);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     Item coerce(int param1Int) {
/* 247 */       if (this.typecode == param1Int) {
/* 248 */         return this;
/*     */       }
/* 250 */       load();
/* 251 */       int i = Code.truncate(this.typecode);
/* 252 */       int j = Code.truncate(param1Int);
/* 253 */       if (i != j) {
/* 254 */         int k = (j > i) ? (j - 1) : j;
/*     */
/* 256 */         Items.this.code.emitop0(133 + i * 3 + k);
/*     */       }
/* 258 */       if (param1Int != j) {
/* 259 */         Items.this.code.emitop0(145 + param1Int - 5);
/*     */       }
/* 261 */       return Items.this.stackItem[param1Int];
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Item coerce(Type param1Type) {
/* 269 */       return coerce(Code.typecode(param1Type));
/*     */     }
/*     */
/*     */
/*     */
/*     */     int width() {
/* 275 */       return 0;
/*     */     }
/*     */
/*     */
/*     */     public abstract String toString();
/*     */   }
/*     */
/*     */   class StackItem
/*     */     extends Item
/*     */   {
/*     */     StackItem(int param1Int) {
/* 286 */       super(param1Int);
/*     */     }
/*     */
/*     */     Item load() {
/* 290 */       return this;
/*     */     }
/*     */
/*     */     void duplicate() {
/* 294 */       Items.this.code.emitop0((width() == 2) ? 92 : 89);
/*     */     }
/*     */
/*     */     void drop() {
/* 298 */       Items.this.code.emitop0((width() == 2) ? 88 : 87);
/*     */     }
/*     */
/*     */     void stash(int param1Int) {
/* 302 */       Items.this.code.emitop0((
/* 303 */           (width() == 2) ? 91 : 90) + 3 * (Code.width(param1Int) - 1));
/*     */     }
/*     */
/*     */     int width() {
/* 307 */       return Code.width(this.typecode);
/*     */     }
/*     */
/*     */     public String toString() {
/* 311 */       return "stack(" + ByteCodes.typecodeNames[this.typecode] + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   class IndexedItem
/*     */     extends Item
/*     */   {
/*     */     IndexedItem(Type param1Type) {
/* 320 */       super(Code.typecode(param1Type));
/*     */     }
/*     */
/*     */     Item load() {
/* 324 */       Items.this.code.emitop0(46 + this.typecode);
/* 325 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void store() {
/* 329 */       Items.this.code.emitop0(79 + this.typecode);
/*     */     }
/*     */
/*     */     void duplicate() {
/* 333 */       Items.this.code.emitop0(92);
/*     */     }
/*     */
/*     */     void drop() {
/* 337 */       Items.this.code.emitop0(88);
/*     */     }
/*     */
/*     */     void stash(int param1Int) {
/* 341 */       Items.this.code.emitop0(91 + 3 * (Code.width(param1Int) - 1));
/*     */     }
/*     */
/*     */     int width() {
/* 345 */       return 2;
/*     */     }
/*     */
/*     */     public String toString() {
/* 349 */       return "indexed(" + ByteCodes.typecodeNames[this.typecode] + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class SelfItem
/*     */     extends Item
/*     */   {
/*     */     boolean isSuper;
/*     */
/*     */
/*     */     SelfItem(boolean param1Boolean) {
/* 362 */       super(4);
/* 363 */       this.isSuper = param1Boolean;
/*     */     }
/*     */
/*     */     Item load() {
/* 367 */       Items.this.code.emitop0(42);
/* 368 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     public String toString() {
/* 372 */       return this.isSuper ? "super" : "this";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class LocalItem
/*     */     extends Item
/*     */   {
/*     */     int reg;
/*     */
/*     */
/*     */     Type type;
/*     */
/*     */
/*     */
/*     */     LocalItem(Type param1Type, int param1Int) {
/* 389 */       super(Code.typecode(param1Type));
/* 390 */       Assert.check((param1Int >= 0));
/* 391 */       this.type = param1Type;
/* 392 */       this.reg = param1Int;
/*     */     }
/*     */
/*     */     Item load() {
/* 396 */       if (this.reg <= 3) {
/* 397 */         Items.this.code.emitop0(26 + Code.truncate(this.typecode) * 4 + this.reg);
/*     */       } else {
/* 399 */         Items.this.code.emitop1w(21 + Code.truncate(this.typecode), this.reg);
/* 400 */       }  return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void store() {
/* 404 */       if (this.reg <= 3) {
/* 405 */         Items.this.code.emitop0(59 + Code.truncate(this.typecode) * 4 + this.reg);
/*     */       } else {
/* 407 */         Items.this.code.emitop1w(54 + Code.truncate(this.typecode), this.reg);
/* 408 */       }  Items.this.code.setDefined(this.reg);
/*     */     }
/*     */
/*     */     void incr(int param1Int) {
/* 412 */       if (this.typecode == 0 && param1Int >= -32768 && param1Int <= 32767) {
/* 413 */         Items.this.code.emitop1w(132, this.reg, param1Int);
/*     */       } else {
/* 415 */         load();
/* 416 */         if (param1Int >= 0) {
/* 417 */           Items.this.makeImmediateItem((Type)Items.this.syms.intType, Integer.valueOf(param1Int)).load();
/* 418 */           Items.this.code.emitop0(96);
/*     */         } else {
/* 420 */           Items.this.makeImmediateItem((Type)Items.this.syms.intType, Integer.valueOf(-param1Int)).load();
/* 421 */           Items.this.code.emitop0(100);
/*     */         }
/* 423 */         Items.this.makeStackItem((Type)Items.this.syms.intType).coerce(this.typecode);
/* 424 */         store();
/*     */       }
/*     */     }
/*     */
/*     */     public String toString() {
/* 429 */       return "localItem(type=" + this.type + "; reg=" + this.reg + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class StaticItem
/*     */     extends Item
/*     */   {
/*     */     Symbol member;
/*     */
/*     */
/*     */     StaticItem(Symbol param1Symbol) {
/* 442 */       super(Code.typecode(param1Symbol.erasure(Items.this.types)));
/* 443 */       this.member = param1Symbol;
/*     */     }
/*     */
/*     */     Item load() {
/* 447 */       Items.this.code.emitop2(178, Items.this.pool.put(this.member));
/* 448 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void store() {
/* 452 */       Items.this.code.emitop2(179, Items.this.pool.put(this.member));
/*     */     }
/*     */
/*     */     Item invoke() {
/* 456 */       Type.MethodType methodType = (Type.MethodType)this.member.erasure(Items.this.types);
/* 457 */       int i = Code.typecode(methodType.restype);
/* 458 */       Items.this.code.emitInvokestatic(Items.this.pool.put(this.member), (Type)methodType);
/* 459 */       return Items.this.stackItem[i];
/*     */     }
/*     */
/*     */     public String toString() {
/* 463 */       return "static(" + this.member + ")";
/*     */     }
/*     */   }
/*     */
/*     */   class DynamicItem
/*     */     extends StaticItem
/*     */   {
/*     */     DynamicItem(Symbol param1Symbol) {
/* 471 */       super(param1Symbol);
/*     */     }
/*     */
/*     */     Item load() {
/*     */       assert false;
/* 476 */       return null;
/*     */     }
/*     */
/*     */
/*     */     void store() {
/*     */       assert false;
/*     */     }
/*     */
/*     */     Item invoke() {
/* 485 */       Type.MethodType methodType = (Type.MethodType)this.member.erasure(Items.this.types);
/* 486 */       int i = Code.typecode(methodType.restype);
/* 487 */       Items.this.code.emitInvokedynamic(Items.this.pool.put(this.member), (Type)methodType);
/* 488 */       return Items.this.stackItem[i];
/*     */     }
/*     */
/*     */     public String toString() {
/* 492 */       return "dynamic(" + this.member + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class MemberItem
/*     */     extends Item
/*     */   {
/*     */     Symbol member;
/*     */
/*     */
/*     */     boolean nonvirtual;
/*     */
/*     */
/*     */
/*     */     MemberItem(Symbol param1Symbol, boolean param1Boolean) {
/* 509 */       super(Code.typecode(param1Symbol.erasure(Items.this.types)));
/* 510 */       this.member = param1Symbol;
/* 511 */       this.nonvirtual = param1Boolean;
/*     */     }
/*     */
/*     */     Item load() {
/* 515 */       Items.this.code.emitop2(180, Items.this.pool.put(this.member));
/* 516 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void store() {
/* 520 */       Items.this.code.emitop2(181, Items.this.pool.put(this.member));
/*     */     }
/*     */
/*     */     Item invoke() {
/* 524 */       Type.MethodType methodType = (Type.MethodType)this.member.externalType(Items.this.types);
/* 525 */       int i = Code.typecode(methodType.restype);
/* 526 */       if ((this.member.owner.flags() & 0x200L) != 0L && !this.nonvirtual) {
/* 527 */         Items.this.code.emitInvokeinterface(Items.this.pool.put(this.member), (Type)methodType);
/* 528 */       } else if (this.nonvirtual) {
/* 529 */         Items.this.code.emitInvokespecial(Items.this.pool.put(this.member), (Type)methodType);
/*     */       } else {
/* 531 */         Items.this.code.emitInvokevirtual(Items.this.pool.put(this.member), (Type)methodType);
/*     */       }
/* 533 */       return Items.this.stackItem[i];
/*     */     }
/*     */
/*     */     void duplicate() {
/* 537 */       Items.this.stackItem[4].duplicate();
/*     */     }
/*     */
/*     */     void drop() {
/* 541 */       Items.this.stackItem[4].drop();
/*     */     }
/*     */
/*     */     void stash(int param1Int) {
/* 545 */       Items.this.stackItem[4].stash(param1Int);
/*     */     }
/*     */
/*     */     int width() {
/* 549 */       return 1;
/*     */     }
/*     */
/*     */     public String toString() {
/* 553 */       return "member(" + this.member + (this.nonvirtual ? " nonvirtual)" : ")");
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class ImmediateItem
/*     */     extends Item
/*     */   {
/*     */     Object value;
/*     */
/*     */
/*     */     ImmediateItem(Type param1Type, Object param1Object) {
/* 566 */       super(Code.typecode(param1Type));
/* 567 */       this.value = param1Object;
/*     */     }
/*     */
/*     */     private void ldc() {
/* 571 */       int i = Items.this.pool.put(this.value);
/* 572 */       if (this.typecode == 1 || this.typecode == 3) {
/* 573 */         Items.this.code.emitop2(20, i);
/*     */       } else {
/* 575 */         Items.this.code.emitLdc(i);
/*     */       }  } Item load() { int i;
/*     */       long l;
/*     */       float f;
/*     */       double d;
/* 580 */       switch (this.typecode) { case 0: case 5: case 6:
/*     */         case 7:
/* 582 */           i = ((Number)this.value).intValue();
/* 583 */           if (-1 <= i && i <= 5) {
/* 584 */             Items.this.code.emitop0(3 + i);
/* 585 */           } else if (-128 <= i && i <= 127) {
/* 586 */             Items.this.code.emitop1(16, i);
/* 587 */           } else if (-32768 <= i && i <= 32767) {
/* 588 */             Items.this.code.emitop2(17, i);
/*     */           } else {
/* 590 */             ldc();
/*     */           }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 620 */           return Items.this.stackItem[this.typecode];case 1: l = ((Number)this.value).longValue(); if (l == 0L || l == 1L) { Items.this.code.emitop0(9 + (int)l); } else { ldc(); }  return Items.this.stackItem[this.typecode];case 2: f = ((Number)this.value).floatValue(); if (isPosZero(f) || f == 1.0D || f == 2.0D) { Items.this.code.emitop0(11 + (int)f); } else { ldc(); }  return Items.this.stackItem[this.typecode];case 3: d = ((Number)this.value).doubleValue(); if (isPosZero(d) || d == 1.0D) { Items.this.code.emitop0(14 + (int)d); } else { ldc(); }  return Items.this.stackItem[this.typecode];case 4: ldc(); return Items.this.stackItem[this.typecode]; }  Assert.error(); return Items.this.stackItem[this.typecode]; }
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean isPosZero(float param1Float) {
/* 626 */       return (param1Float == 0.0F && 1.0F / param1Float > 0.0F);
/*     */     }
/*     */
/*     */
/*     */     private boolean isPosZero(double param1Double) {
/* 631 */       return (param1Double == 0.0D && 1.0D / param1Double > 0.0D);
/*     */     }
/*     */
/*     */     CondItem mkCond() {
/* 635 */       int i = ((Number)this.value).intValue();
/* 636 */       return Items.this.makeCondItem((i != 0) ? 167 : 168);
/*     */     }
/*     */
/*     */     Item coerce(int param1Int) {
/* 640 */       if (this.typecode == param1Int) {
/* 641 */         return this;
/*     */       }
/* 643 */       switch (param1Int) {
/*     */         case 0:
/* 645 */           if (Code.truncate(this.typecode) == 0) {
/* 646 */             return this;
/*     */           }
/* 648 */           return new ImmediateItem((Type)Items.this.syms.intType,
/*     */
/* 650 */               Integer.valueOf(((Number)this.value).intValue()));
/*     */         case 1:
/* 652 */           return new ImmediateItem((Type)Items.this.syms.longType,
/*     */
/* 654 */               Long.valueOf(((Number)this.value).longValue()));
/*     */         case 2:
/* 656 */           return new ImmediateItem((Type)Items.this.syms.floatType,
/*     */
/* 658 */               Float.valueOf(((Number)this.value).floatValue()));
/*     */         case 3:
/* 660 */           return new ImmediateItem((Type)Items.this.syms.doubleType,
/*     */
/* 662 */               Double.valueOf(((Number)this.value).doubleValue()));
/*     */         case 5:
/* 664 */           return new ImmediateItem((Type)Items.this.syms.byteType,
/*     */
/* 666 */               Integer.valueOf((byte)((Number)this.value).intValue()));
/*     */         case 6:
/* 668 */           return new ImmediateItem((Type)Items.this.syms.charType,
/*     */
/* 670 */               Integer.valueOf((char)((Number)this.value).intValue()));
/*     */         case 7:
/* 672 */           return new ImmediateItem((Type)Items.this.syms.shortType,
/*     */
/* 674 */               Integer.valueOf((short)((Number)this.value).intValue()));
/*     */       }
/* 676 */       return super.coerce(param1Int);
/*     */     }
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 682 */       return "immediate(" + this.value + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   class AssignItem
/*     */     extends Item
/*     */   {
/*     */     Item lhs;
/*     */
/*     */
/*     */     AssignItem(Item param1Item) {
/* 695 */       super(param1Item.typecode);
/* 696 */       this.lhs = param1Item;
/*     */     }
/*     */
/*     */     Item load() {
/* 700 */       this.lhs.stash(this.typecode);
/* 701 */       this.lhs.store();
/* 702 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void duplicate() {
/* 706 */       load().duplicate();
/*     */     }
/*     */
/*     */     void drop() {
/* 710 */       this.lhs.store();
/*     */     }
/*     */
/*     */     void stash(int param1Int) {
/* 714 */       Assert.error();
/*     */     }
/*     */
/*     */     int width() {
/* 718 */       return this.lhs.width() + Code.width(this.typecode);
/*     */     }
/*     */
/*     */     public String toString() {
/* 722 */       return "assign(lhs = " + this.lhs + ")";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   class CondItem
/*     */     extends Item
/*     */   {
/*     */     Code.Chain trueJumps;
/*     */
/*     */
/*     */
/*     */     Code.Chain falseJumps;
/*     */
/*     */
/*     */
/*     */     int opcode;
/*     */
/*     */
/*     */
/*     */     JCTree tree;
/*     */
/*     */
/*     */
/*     */
/*     */     CondItem(int param1Int, Code.Chain param1Chain1, Code.Chain param1Chain2) {
/* 751 */       super(5);
/* 752 */       this.opcode = param1Int;
/* 753 */       this.trueJumps = param1Chain1;
/* 754 */       this.falseJumps = param1Chain2;
/*     */     }
/*     */
/*     */     Item load() {
/* 758 */       Code.Chain chain1 = null;
/* 759 */       Code.Chain chain2 = jumpFalse();
/* 760 */       if (!isFalse()) {
/* 761 */         Items.this.code.resolve(this.trueJumps);
/* 762 */         Items.this.code.emitop0(4);
/* 763 */         chain1 = Items.this.code.branch(167);
/*     */       }
/* 765 */       if (chain2 != null) {
/* 766 */         Items.this.code.resolve(chain2);
/* 767 */         Items.this.code.emitop0(3);
/*     */       }
/* 769 */       Items.this.code.resolve(chain1);
/* 770 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */
/*     */     void duplicate() {
/* 774 */       load().duplicate();
/*     */     }
/*     */
/*     */     void drop() {
/* 778 */       load().drop();
/*     */     }
/*     */
/*     */     void stash(int param1Int) {
/* 782 */       Assert.error();
/*     */     }
/*     */
/*     */     CondItem mkCond() {
/* 786 */       return this;
/*     */     }
/*     */
/*     */     Code.Chain jumpTrue() {
/* 790 */       if (this.tree == null) return Code.mergeChains(this.trueJumps, Items.this.code.branch(this.opcode));
/*     */
/* 792 */       int i = Items.this.code.curCP();
/* 793 */       Code.Chain chain = Code.mergeChains(this.trueJumps, Items.this.code.branch(this.opcode));
/* 794 */       Items.this.code.crt.put(this.tree, 128, i, Items.this.code.curCP());
/* 795 */       return chain;
/*     */     }
/*     */
/*     */     Code.Chain jumpFalse() {
/* 799 */       if (this.tree == null) return Code.mergeChains(this.falseJumps, Items.this.code.branch(Code.negate(this.opcode)));
/*     */
/* 801 */       int i = Items.this.code.curCP();
/* 802 */       Code.Chain chain = Code.mergeChains(this.falseJumps, Items.this.code.branch(Code.negate(this.opcode)));
/* 803 */       Items.this.code.crt.put(this.tree, 256, i, Items.this.code.curCP());
/* 804 */       return chain;
/*     */     }
/*     */
/*     */     CondItem negate() {
/* 808 */       CondItem condItem = new CondItem(Code.negate(this.opcode), this.falseJumps, this.trueJumps);
/* 809 */       condItem.tree = this.tree;
/* 810 */       return condItem;
/*     */     }
/*     */
/*     */
/*     */     int width() {
/* 815 */       throw new AssertionError();
/*     */     }
/*     */
/*     */     boolean isTrue() {
/* 819 */       return (this.falseJumps == null && this.opcode == 167);
/*     */     }
/*     */
/*     */     boolean isFalse() {
/* 823 */       return (this.trueJumps == null && this.opcode == 168);
/*     */     }
/*     */
/*     */     public String toString() {
/* 827 */       return "cond(" + Code.mnem(this.opcode) + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Items.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
