/*     */ package com.sun.tools.javac.jvm;
/*     */
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.ArrayUtils;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.util.Arrays;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Pool
/*     */ {
/*     */   public static final int MAX_ENTRIES = 65535;
/*     */   public static final int MAX_STRING_LENGTH = 65535;
/*     */   int pp;
/*     */   Object[] pool;
/*     */   Map<Object, Integer> indices;
/*     */   Types types;
/*     */
/*     */   public Pool(int paramInt, Object[] paramArrayOfObject, Types paramTypes) {
/*  71 */     this.pp = paramInt;
/*  72 */     this.pool = paramArrayOfObject;
/*  73 */     this.types = paramTypes;
/*  74 */     this.indices = new HashMap<>(paramArrayOfObject.length);
/*  75 */     for (byte b = 1; b < paramInt; b++) {
/*  76 */       if (paramArrayOfObject[b] != null) this.indices.put(paramArrayOfObject[b], Integer.valueOf(b));
/*     */
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Pool(Types paramTypes) {
/*  83 */     this(1, new Object[64], paramTypes);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public int numEntries() {
/*  89 */     return this.pp;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void reset() {
/*  95 */     this.pp = 1;
/*  96 */     this.indices.clear();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public int put(Object paramObject) {
/* 104 */     paramObject = makePoolValue(paramObject);
/*     */
/* 106 */     Integer integer = this.indices.get(paramObject);
/* 107 */     if (integer == null) {
/*     */
/* 109 */       integer = Integer.valueOf(this.pp);
/* 110 */       this.indices.put(paramObject, integer);
/* 111 */       this.pool = ArrayUtils.ensureCapacity(this.pool, this.pp);
/* 112 */       this.pool[this.pp++] = paramObject;
/* 113 */       if (paramObject instanceof Long || paramObject instanceof Double) {
/* 114 */         this.pool = ArrayUtils.ensureCapacity(this.pool, this.pp);
/* 115 */         this.pool[this.pp++] = null;
/*     */       }
/*     */     }
/* 118 */     return integer.intValue();
/*     */   }
/*     */
/*     */   Object makePoolValue(Object paramObject) {
/* 122 */     if (paramObject instanceof Symbol.DynamicMethodSymbol)
/* 123 */       return new DynamicMethod((Symbol.DynamicMethodSymbol)paramObject, this.types);
/* 124 */     if (paramObject instanceof Symbol.MethodSymbol)
/* 125 */       return new Method((Symbol.MethodSymbol)paramObject, this.types);
/* 126 */     if (paramObject instanceof Symbol.VarSymbol)
/* 127 */       return new Variable((Symbol.VarSymbol)paramObject, this.types);
/* 128 */     if (paramObject instanceof Type) {
/* 129 */       return new Types.UniqueType((Type)paramObject, this.types);
/*     */     }
/* 131 */     return paramObject;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public int get(Object paramObject) {
/* 139 */     Integer integer = this.indices.get(paramObject);
/* 140 */     return (integer == null) ? -1 : integer.intValue();
/*     */   }
/*     */
/*     */   static class Method
/*     */     extends Symbol.DelegatedSymbol<Symbol.MethodSymbol> {
/*     */     Method(MethodSymbol param1MethodSymbol, Types param1Types) {
/* 146 */       super((Symbol)param1MethodSymbol);
/* 147 */       this.uniqueType = new Types.UniqueType(param1MethodSymbol.type, param1Types);
/*     */     } Types.UniqueType uniqueType;
/*     */     public boolean equals(Object param1Object) {
/* 150 */       if (!(param1Object instanceof Method)) return false;
/* 151 */       MethodSymbol methodSymbol1 = (MethodSymbol)((Method)param1Object).other;
/* 152 */       MethodSymbol methodSymbol2 = (MethodSymbol)this.other;
/* 153 */       return (methodSymbol1.name == methodSymbol2.name && methodSymbol1.owner == methodSymbol2.owner && ((Method)param1Object).uniqueType
/*     */
/*     */
/* 156 */         .equals(this.uniqueType));
/*     */     }
/*     */     public int hashCode() {
/* 159 */       MethodSymbol methodSymbol = (MethodSymbol)this.other;
/* 160 */       return methodSymbol.name
/* 161 */         .hashCode() * 33 + methodSymbol.owner
/* 162 */         .hashCode() * 9 + this.uniqueType
/* 163 */         .hashCode();
/*     */     }
/*     */   }
/*     */
/*     */   static class DynamicMethod extends Method {
/*     */     public Object[] uniqueStaticArgs;
/*     */
/*     */     DynamicMethod(DynamicMethodSymbol param1DynamicMethodSymbol, Types param1Types) {
/* 171 */       super((MethodSymbol)param1DynamicMethodSymbol, param1Types);
/* 172 */       this.uniqueStaticArgs = getUniqueTypeArray(param1DynamicMethodSymbol.staticArgs, param1Types);
/*     */     }
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 177 */       if (!super.equals(param1Object)) return false;
/* 178 */       if (!(param1Object instanceof DynamicMethod)) return false;
/* 179 */       DynamicMethodSymbol dynamicMethodSymbol1 = (DynamicMethodSymbol)this.other;
/* 180 */       DynamicMethodSymbol dynamicMethodSymbol2 = (DynamicMethodSymbol)((DynamicMethod)param1Object).other;
/* 181 */       return (dynamicMethodSymbol1.bsm == dynamicMethodSymbol2.bsm && dynamicMethodSymbol1.bsmKind == dynamicMethodSymbol2.bsmKind &&
/*     */
/* 183 */         Arrays.equals(this.uniqueStaticArgs, ((DynamicMethod)param1Object).uniqueStaticArgs));
/*     */     }
/*     */
/*     */
/*     */
/*     */     public int hashCode() {
/* 189 */       int i = super.hashCode();
/* 190 */       DynamicMethodSymbol dynamicMethodSymbol = (DynamicMethodSymbol)this.other;
/* 191 */       i += dynamicMethodSymbol.bsmKind * 7 + dynamicMethodSymbol.bsm
/* 192 */         .hashCode() * 11;
/* 193 */       for (byte b = 0; b < dynamicMethodSymbol.staticArgs.length; b++) {
/* 194 */         i += this.uniqueStaticArgs[b].hashCode() * 23;
/*     */       }
/* 196 */       return i;
/*     */     }
/*     */
/*     */     private Object[] getUniqueTypeArray(Object[] param1ArrayOfObject, Types param1Types) {
/* 200 */       Object[] arrayOfObject = new Object[param1ArrayOfObject.length];
/* 201 */       for (byte b = 0; b < param1ArrayOfObject.length; b++) {
/* 202 */         if (param1ArrayOfObject[b] instanceof Type) {
/* 203 */           arrayOfObject[b] = new Types.UniqueType((Type)param1ArrayOfObject[b], param1Types);
/*     */         } else {
/* 205 */           arrayOfObject[b] = param1ArrayOfObject[b];
/*     */         }
/*     */       }
/* 208 */       return arrayOfObject;
/*     */     } }
/*     */
/*     */   static class Variable extends Symbol.DelegatedSymbol<Symbol.VarSymbol> {
/*     */     Types.UniqueType uniqueType;
/*     */
/*     */     Variable(VarSymbol param1VarSymbol, Types param1Types) {
/* 215 */       super((Symbol)param1VarSymbol);
/* 216 */       this.uniqueType = new Types.UniqueType(param1VarSymbol.type, param1Types);
/*     */     }
/*     */     public boolean equals(Object param1Object) {
/* 219 */       if (!(param1Object instanceof Variable)) return false;
/* 220 */       VarSymbol varSymbol1 = (VarSymbol)((Variable)param1Object).other;
/* 221 */       VarSymbol varSymbol2 = (VarSymbol)this.other;
/* 222 */       return (varSymbol1.name == varSymbol2.name && varSymbol1.owner == varSymbol2.owner && ((Variable)param1Object).uniqueType
/*     */
/*     */
/* 225 */         .equals(this.uniqueType));
/*     */     }
/*     */     public int hashCode() {
/* 228 */       VarSymbol varSymbol = (VarSymbol)this.other;
/* 229 */       return varSymbol.name
/* 230 */         .hashCode() * 33 + varSymbol.owner
/* 231 */         .hashCode() * 9 + this.uniqueType
/* 232 */         .hashCode();
/*     */     }
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
/*     */   public static class MethodHandle
/*     */   {
/*     */     int refKind;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Symbol refSym;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Types.UniqueType uniqueType;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Filter<Name> nonInitFilter;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Filter<Name> initFilter;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public MethodHandle(int param1Int, Symbol param1Symbol, Types param1Types) {
/* 312 */       this.nonInitFilter = new Filter<Name>() {
/*     */           public boolean accepts(Name param2Name) {
/* 314 */             return (param2Name != param2Name.table.names.init && param2Name != param2Name.table.names.clinit);
/*     */           }
/*     */         };
/*     */
/* 318 */       this.initFilter = new Filter<Name>() {
/*     */           public boolean accepts(Name param2Name) {
/* 320 */             return (param2Name == param2Name.table.names.init);
/*     */           }
/*     */         };
/*     */       this.refKind = param1Int;
/*     */       this.refSym = param1Symbol;
/*     */       this.uniqueType = new Types.UniqueType(this.refSym.type, param1Types);
/*     */       checkConsistent();
/*     */     }
/*     */
/*     */     public boolean equals(Object param1Object) {
/*     */       if (!(param1Object instanceof MethodHandle))
/*     */         return false;
/*     */       MethodHandle methodHandle = (MethodHandle)param1Object;
/*     */       if (methodHandle.refKind != this.refKind)
/*     */         return false;
/*     */       Symbol symbol = methodHandle.refSym;
/*     */       return (symbol.name == this.refSym.name && symbol.owner == this.refSym.owner && ((MethodHandle)param1Object).uniqueType.equals(this.uniqueType));
/*     */     }
/*     */
/*     */     public int hashCode() {
/*     */       return this.refKind * 65 + this.refSym.name.hashCode() * 33 + this.refSym.owner.hashCode() * 9 + this.uniqueType.hashCode();
/*     */     }
/*     */
/*     */     private void checkConsistent() {
/*     */       boolean bool1 = false;
/*     */       byte b = -1;
/*     */       Filter<Name> filter = this.nonInitFilter;
/*     */       boolean bool2 = false;
/*     */       switch (this.refKind) {
/*     */         case 2:
/*     */         case 4:
/*     */           bool1 = true;
/*     */         case 1:
/*     */         case 3:
/*     */           b = 4;
/*     */           break;
/*     */         case 8:
/*     */           filter = this.initFilter;
/*     */           b = 16;
/*     */           break;
/*     */         case 9:
/*     */           bool2 = true;
/*     */           b = 16;
/*     */           break;
/*     */         case 6:
/*     */           bool2 = true;
/*     */           bool1 = true;
/*     */         case 5:
/*     */           b = 16;
/*     */           break;
/*     */         case 7:
/*     */           bool2 = true;
/*     */           b = 16;
/*     */           break;
/*     */       }
/*     */       Assert.check((!this.refSym.isStatic() || bool1));
/*     */       Assert.check((this.refSym.kind == b));
/*     */       Assert.check(filter.accepts(this.refSym.name));
/*     */       Assert.check((!this.refSym.owner.isInterface() || bool2));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Pool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
