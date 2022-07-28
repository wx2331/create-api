/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Printer
/*     */   implements Type.Visitor<String, Locale>, Symbol.Visitor<String, Locale>
/*     */ {
/*  54 */   List<Type> seenCaptured = List.nil();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int PRIME = 997;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Printer createStandardPrinter(final Messages messages) {
/*  86 */     return new Printer()
/*     */       {
/*     */         protected String localize(Locale param1Locale, String param1String, Object... param1VarArgs) {
/*  89 */           return messages.getLocalizedString(param1Locale, param1String, param1VarArgs);
/*     */         }
/*     */ 
/*     */         
/*     */         protected String capturedVarId(Type.CapturedType param1CapturedType, Locale param1Locale) {
/*  94 */           return ((param1CapturedType.hashCode() & 0xFFFFFFFFL) % 997L) + "";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitTypes(List<Type> paramList, Locale paramLocale) {
/* 106 */     ListBuffer listBuffer = new ListBuffer();
/* 107 */     for (Type type : paramList) {
/* 108 */       listBuffer.append(visit(type, paramLocale));
/*     */     }
/* 110 */     return listBuffer.toList().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitSymbols(List<Symbol> paramList, Locale paramLocale) {
/* 121 */     ListBuffer listBuffer = new ListBuffer();
/* 122 */     for (Symbol symbol : paramList) {
/* 123 */       listBuffer.append(visit(symbol, paramLocale));
/*     */     }
/* 125 */     return listBuffer.toList().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String visit(Type paramType, Locale paramLocale) {
/* 136 */     return paramType.<String, Locale>accept(this, paramLocale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String visit(Symbol paramSymbol, Locale paramLocale) {
/* 147 */     return paramSymbol.<String, Locale>accept(this, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitCapturedType(Type.CapturedType paramCapturedType, Locale paramLocale) {
/* 152 */     if (this.seenCaptured.contains(paramCapturedType))
/* 153 */       return localize(paramLocale, "compiler.misc.type.captureof.1", new Object[] {
/* 154 */             capturedVarId(paramCapturedType, paramLocale)
/*     */           }); 
/*     */     try {
/* 157 */       this.seenCaptured = this.seenCaptured.prepend(paramCapturedType);
/* 158 */       return localize(paramLocale, "compiler.misc.type.captureof", new Object[] {
/* 159 */             capturedVarId(paramCapturedType, paramLocale), 
/* 160 */             visit(paramCapturedType.wildcard, paramLocale)
/*     */           });
/*     */     } finally {
/* 163 */       this.seenCaptured = this.seenCaptured.tail;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitForAll(Type.ForAll paramForAll, Locale paramLocale) {
/* 170 */     return "<" + visitTypes(paramForAll.tvars, paramLocale) + ">" + visit(paramForAll.qtype, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitUndetVar(Type.UndetVar paramUndetVar, Locale paramLocale) {
/* 175 */     if (paramUndetVar.inst != null) {
/* 176 */       return visit(paramUndetVar.inst, paramLocale);
/*     */     }
/* 178 */     return visit(paramUndetVar.qtype, paramLocale) + "?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitArrayType(Type.ArrayType paramArrayType, Locale paramLocale) {
/* 184 */     StringBuilder stringBuilder = new StringBuilder();
/* 185 */     printBaseElementType(paramArrayType, stringBuilder, paramLocale);
/* 186 */     printBrackets(paramArrayType, stringBuilder, paramLocale);
/* 187 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   void printBaseElementType(Type paramType, StringBuilder paramStringBuilder, Locale paramLocale) {
/* 191 */     Type type = paramType;
/* 192 */     while (type.hasTag(TypeTag.ARRAY)) {
/* 193 */       type = type.unannotatedType();
/* 194 */       type = ((Type.ArrayType)type).elemtype;
/*     */     } 
/* 196 */     paramStringBuilder.append(visit(type, paramLocale));
/*     */   }
/*     */   
/*     */   void printBrackets(Type paramType, StringBuilder paramStringBuilder, Locale paramLocale) {
/* 200 */     Type type = paramType;
/* 201 */     while (type.hasTag(TypeTag.ARRAY)) {
/* 202 */       if (type.isAnnotated()) {
/* 203 */         paramStringBuilder.append(' ');
/* 204 */         paramStringBuilder.append(type.getAnnotationMirrors());
/* 205 */         paramStringBuilder.append(' ');
/*     */       } 
/* 207 */       paramStringBuilder.append("[]");
/* 208 */       type = type.unannotatedType();
/* 209 */       type = ((Type.ArrayType)type).elemtype;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitClassType(Type.ClassType paramClassType, Locale paramLocale) {
/* 215 */     StringBuilder stringBuilder = new StringBuilder();
/* 216 */     if (paramClassType.getEnclosingType().hasTag(TypeTag.CLASS) && paramClassType.tsym.owner.kind == 2) {
/* 217 */       stringBuilder.append(visit(paramClassType.getEnclosingType(), paramLocale));
/* 218 */       stringBuilder.append('.');
/* 219 */       stringBuilder.append(className(paramClassType, false, paramLocale));
/*     */     } else {
/* 221 */       stringBuilder.append(className(paramClassType, true, paramLocale));
/*     */     } 
/* 223 */     if (paramClassType.getTypeArguments().nonEmpty()) {
/* 224 */       stringBuilder.append('<');
/* 225 */       stringBuilder.append(visitTypes(paramClassType.getTypeArguments(), paramLocale));
/* 226 */       stringBuilder.append('>');
/*     */     } 
/* 228 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitMethodType(Type.MethodType paramMethodType, Locale paramLocale) {
/* 233 */     return "(" + printMethodArgs(paramMethodType.argtypes, false, paramLocale) + ")" + visit(paramMethodType.restype, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitPackageType(Type.PackageType paramPackageType, Locale paramLocale) {
/* 238 */     return paramPackageType.tsym.getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitWildcardType(Type.WildcardType paramWildcardType, Locale paramLocale) {
/* 243 */     StringBuilder stringBuilder = new StringBuilder();
/* 244 */     stringBuilder.append(paramWildcardType.kind);
/* 245 */     if (paramWildcardType.kind != BoundKind.UNBOUND) {
/* 246 */       stringBuilder.append(visit(paramWildcardType.type, paramLocale));
/*     */     }
/* 248 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitErrorType(Type.ErrorType paramErrorType, Locale paramLocale) {
/* 253 */     return visitType(paramErrorType, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitTypeVar(Type.TypeVar paramTypeVar, Locale paramLocale) {
/* 258 */     return visitType(paramTypeVar, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitAnnotatedType(Type.AnnotatedType paramAnnotatedType, Locale paramLocale) {
/* 263 */     if (paramAnnotatedType.getAnnotationMirrors().nonEmpty()) {
/* 264 */       if (paramAnnotatedType.unannotatedType().hasTag(TypeTag.ARRAY)) {
/* 265 */         StringBuilder stringBuilder = new StringBuilder();
/* 266 */         printBaseElementType(paramAnnotatedType, stringBuilder, paramLocale);
/* 267 */         printBrackets(paramAnnotatedType, stringBuilder, paramLocale);
/* 268 */         return stringBuilder.toString();
/* 269 */       }  if (paramAnnotatedType.unannotatedType().hasTag(TypeTag.CLASS) && paramAnnotatedType
/* 270 */         .unannotatedType().getEnclosingType() != Type.noType) {
/* 271 */         return visit(paramAnnotatedType.unannotatedType().getEnclosingType(), paramLocale) + ". " + paramAnnotatedType
/*     */           
/* 273 */           .getAnnotationMirrors() + " " + 
/* 274 */           className((Type.ClassType)paramAnnotatedType.unannotatedType(), false, paramLocale);
/*     */       }
/* 276 */       return paramAnnotatedType.getAnnotationMirrors() + " " + visit(paramAnnotatedType.unannotatedType(), paramLocale);
/*     */     } 
/*     */     
/* 279 */     return visit(paramAnnotatedType.unannotatedType(), paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitType(Type paramType, Locale paramLocale) {
/* 284 */     return (paramType.tsym == null || paramType.tsym.name == null) ? 
/* 285 */       localize(paramLocale, "compiler.misc.type.none", new Object[0]) : paramType.tsym.name
/* 286 */       .toString();
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
/*     */   protected String className(Type.ClassType paramClassType, boolean paramBoolean, Locale paramLocale) {
/* 301 */     Symbol.TypeSymbol typeSymbol = paramClassType.tsym;
/* 302 */     if (typeSymbol.name.length() == 0 && (typeSymbol.flags() & 0x1000000L) != 0L) {
/* 303 */       StringBuilder stringBuilder = new StringBuilder(visit(paramClassType.supertype_field, paramLocale));
/* 304 */       for (List<Type> list = paramClassType.interfaces_field; list.nonEmpty(); list = list.tail) {
/* 305 */         stringBuilder.append('&');
/* 306 */         stringBuilder.append(visit((Type)list.head, paramLocale));
/*     */       } 
/* 308 */       return stringBuilder.toString();
/* 309 */     }  if (typeSymbol.name.length() == 0) {
/*     */       String str;
/* 311 */       Type.ClassType classType = (Type.ClassType)paramClassType.tsym.type;
/* 312 */       if (classType == null) {
/* 313 */         str = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { null });
/* 314 */       } else if (classType.interfaces_field != null && classType.interfaces_field.nonEmpty()) {
/* 315 */         str = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] {
/* 316 */               visit((Type)classType.interfaces_field.head, paramLocale) });
/*     */       } else {
/* 318 */         str = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] {
/* 319 */               visit(classType.supertype_field, paramLocale) });
/*     */       } 
/* 321 */       return str;
/* 322 */     }  if (paramBoolean) {
/* 323 */       return typeSymbol.getQualifiedName().toString();
/*     */     }
/* 325 */     return typeSymbol.name.toString();
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
/*     */   protected String printMethodArgs(List<Type> paramList, boolean paramBoolean, Locale paramLocale) {
/* 339 */     if (!paramBoolean) {
/* 340 */       return visitTypes(paramList, paramLocale);
/*     */     }
/* 342 */     StringBuilder stringBuilder = new StringBuilder();
/* 343 */     while (paramList.tail.nonEmpty()) {
/* 344 */       stringBuilder.append(visit((Type)paramList.head, paramLocale));
/* 345 */       paramList = paramList.tail;
/* 346 */       stringBuilder.append(',');
/*     */     } 
/* 348 */     if (((Type)paramList.head).unannotatedType().hasTag(TypeTag.ARRAY)) {
/* 349 */       stringBuilder.append(visit(((Type.ArrayType)((Type)paramList.head).unannotatedType()).elemtype, paramLocale));
/* 350 */       if (((Type)paramList.head).getAnnotationMirrors().nonEmpty()) {
/* 351 */         stringBuilder.append(' ');
/* 352 */         stringBuilder.append(((Type)paramList.head).getAnnotationMirrors());
/* 353 */         stringBuilder.append(' ');
/*     */       } 
/* 355 */       stringBuilder.append("...");
/*     */     } else {
/* 357 */       stringBuilder.append(visit((Type)paramList.head, paramLocale));
/*     */     } 
/* 359 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitClassSymbol(Symbol.ClassSymbol paramClassSymbol, Locale paramLocale) {
/* 365 */     return paramClassSymbol.name.isEmpty() ? 
/* 366 */       localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { paramClassSymbol.flatname }) : paramClassSymbol.fullname
/* 367 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitMethodSymbol(Symbol.MethodSymbol paramMethodSymbol, Locale paramLocale) {
/* 372 */     if (paramMethodSymbol.isStaticOrInstanceInit()) {
/* 373 */       return paramMethodSymbol.owner.name.toString();
/*     */     }
/*     */ 
/*     */     
/* 377 */     String str = (paramMethodSymbol.name == paramMethodSymbol.name.table.names.init) ? paramMethodSymbol.owner.name.toString() : paramMethodSymbol.name.toString();
/* 378 */     if (paramMethodSymbol.type != null) {
/* 379 */       if (paramMethodSymbol.type.hasTag(TypeTag.FORALL)) {
/* 380 */         str = "<" + visitTypes(paramMethodSymbol.type.getTypeArguments(), paramLocale) + ">" + str;
/*     */       }
/* 382 */       str = str + "(" + printMethodArgs(paramMethodSymbol.type
/* 383 */           .getParameterTypes(), 
/* 384 */           ((paramMethodSymbol.flags() & 0x400000000L) != 0L), paramLocale) + ")";
/*     */     } 
/*     */     
/* 387 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String visitOperatorSymbol(Symbol.OperatorSymbol paramOperatorSymbol, Locale paramLocale) {
/* 393 */     return visitMethodSymbol(paramOperatorSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitPackageSymbol(Symbol.PackageSymbol paramPackageSymbol, Locale paramLocale) {
/* 398 */     return paramPackageSymbol.isUnnamed() ? 
/* 399 */       localize(paramLocale, "compiler.misc.unnamed.package", new Object[0]) : paramPackageSymbol.fullname
/* 400 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitTypeSymbol(Symbol.TypeSymbol paramTypeSymbol, Locale paramLocale) {
/* 405 */     return visitSymbol(paramTypeSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitVarSymbol(Symbol.VarSymbol paramVarSymbol, Locale paramLocale) {
/* 410 */     return visitSymbol(paramVarSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   
/*     */   public String visitSymbol(Symbol paramSymbol, Locale paramLocale) {
/* 415 */     return paramSymbol.name.toString();
/*     */   }
/*     */   
/*     */   protected abstract String localize(Locale paramLocale, String paramString, Object... paramVarArgs);
/*     */   
/*     */   protected abstract String capturedVarId(Type.CapturedType paramCapturedType, Locale paramLocale);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Printer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */