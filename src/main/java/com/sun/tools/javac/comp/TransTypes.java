/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ public class TransTypes
/*      */   extends TreeTranslator
/*      */ {
/*   54 */   protected static final Context.Key<TransTypes> transTypesKey = new Context.Key(); private Names names; private Log log; private Symtab syms; private TreeMaker make; private Enter enter; private boolean allowEnums; private boolean allowInterfaceBridges; private Types types; private final Resolve resolve; private final boolean addBridges; private final CompileStates compileStates; Map<Symbol.MethodSymbol, Symbol.MethodSymbol> overridden; private Filter<Symbol> overrideBridgeFilter; private Type pt; JCTree currentMethod;
/*      */   private Env<AttrContext> env;
/*      */   private static final String statePreviousToFlowAssertMsg = "The current compile state [%s] of class %s is previous to FLOW";
/*      */   
/*      */   public static TransTypes instance(Context paramContext) {
/*   59 */     TransTypes transTypes = (TransTypes)paramContext.get(transTypesKey);
/*   60 */     if (transTypes == null)
/*   61 */       transTypes = new TransTypes(paramContext); 
/*   62 */     return transTypes;
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
/*      */   JCTree.JCExpression cast(JCTree.JCExpression paramJCExpression, Type paramType) {
/*      */     int i = this.make.pos;
/*      */     this.make.at(paramJCExpression.pos);
/*      */     if (!this.types.isSameType(paramJCExpression.type, paramType)) {
/*      */       if (!this.resolve.isAccessible(this.env, paramType.tsym)) {
/*      */         this.resolve.logAccessErrorInternal(this.env, (JCTree)paramJCExpression, paramType);
/*      */       }
/*      */       paramJCExpression = this.make.TypeCast((JCTree)this.make.Type(paramType), paramJCExpression).setType(paramType);
/*      */     } 
/*      */     this.make.pos = i;
/*      */     return paramJCExpression;
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
/*      */   public JCTree.JCExpression coerce(Env<AttrContext> paramEnv, JCTree.JCExpression paramJCExpression, Type paramType) {
/*      */     Env<AttrContext> env = this.env;
/*      */     try {
/*      */       this.env = paramEnv;
/*      */       return coerce(paramJCExpression, paramType);
/*      */     } finally {
/*      */       this.env = env;
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
/*      */   JCTree.JCExpression coerce(JCTree.JCExpression paramJCExpression, Type paramType) {
/*      */     Type type = paramType.baseType();
/*      */     if (paramJCExpression.type.isPrimitive() == paramType.isPrimitive()) {
/*      */       return this.types.isAssignable(paramJCExpression.type, type, this.types.noWarnings) ? paramJCExpression : cast(paramJCExpression, type);
/*      */     }
/*      */     return paramJCExpression;
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
/*      */   JCTree.JCExpression retype(JCTree.JCExpression paramJCExpression, Type paramType1, Type paramType2) {
/*      */     if (!paramType1.isPrimitive()) {
/*      */       if (paramType2 != null && paramType2.isPrimitive()) {
/*      */         paramType2 = erasure(paramJCExpression.type);
/*      */       }
/*      */       paramJCExpression.type = paramType1;
/*      */       if (paramType2 != null) {
/*      */         return coerce(paramJCExpression, paramType2);
/*      */       }
/*      */     } 
/*      */     return paramJCExpression;
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
/*      */   <T extends JCTree> List<T> translateArgs(List<T> paramList, List<Type> paramList1, Type paramType) {
/*      */     if (paramList1.isEmpty()) {
/*      */       return paramList;
/*      */     }
/*      */     List<T> list = paramList;
/*      */     while (paramList1.tail.nonEmpty()) {
/*      */       list.head = translate((JCTree)list.head, (Type)paramList1.head);
/*      */       list = list.tail;
/*      */       paramList1 = paramList1.tail;
/*      */     } 
/*      */     Type type = (Type)paramList1.head;
/*      */     Assert.check((paramType != null || list.length() == 1));
/*      */     if (paramType != null) {
/*      */       while (list.nonEmpty()) {
/*      */         list.head = translate((JCTree)list.head, paramType);
/*      */         list = list.tail;
/*      */       } 
/*      */     } else {
/*      */       list.head = translate((JCTree)list.head, type);
/*      */     } 
/*      */     return paramList;
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
/*      */   public <T extends JCTree> List<T> translateArgs(List<T> paramList, List<Type> paramList1, Type paramType, Env<AttrContext> paramEnv) {
/*      */     Env<AttrContext> env = this.env;
/*      */     try {
/*      */       this.env = paramEnv;
/*      */       return translateArgs(paramList, paramList1, paramType);
/*      */     } finally {
/*      */       this.env = env;
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
/*      */   protected TransTypes(Context paramContext) {
/*  393 */     this.overrideBridgeFilter = new Filter<Symbol>() {
/*      */         public boolean accepts(Symbol param1Symbol) {
/*  395 */           return ((param1Symbol.flags() & 0x10000001000L) != 4096L);
/*      */         }
/*      */       };
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
/*      */ 
/*      */ 
/*      */     
/*  515 */     this.currentMethod = null; paramContext.put(transTypesKey, this); this.compileStates = CompileStates.instance(paramContext); this.names = Names.instance(paramContext); this.log = Log.instance(paramContext); this.syms = Symtab.instance(paramContext); this.enter = Enter.instance(paramContext); this.overridden = new HashMap<>(); Source source = Source.instance(paramContext); this.allowEnums = source.allowEnums(); this.addBridges = source.addBridges(); this.allowInterfaceBridges = source.allowDefaultMethods(); this.types = Types.instance(paramContext); this.make = TreeMaker.instance(paramContext); this.resolve = Resolve.instance(paramContext);
/*      */   } void addBridge(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean, ListBuffer<JCTree> paramListBuffer) { this.make.at(paramDiagnosticPosition); Type type1 = this.types.memberType(paramClassSymbol.type, (Symbol)paramMethodSymbol1); Type type2 = erasure(type1); Type type3 = paramMethodSymbol1.erasure(this.types); long l = paramMethodSymbol2.flags() & 0x7L | 0x1000L | 0x80000000L | (paramClassSymbol.isInterface() ? 8796093022208L : 0L); if (paramBoolean) l |= 0x2000000000L;  Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(l, paramMethodSymbol1.name, type3, (Symbol)paramClassSymbol); methodSymbol.params = createBridgeParams(paramMethodSymbol2, methodSymbol, type3); methodSymbol.setAttributes((Symbol)paramMethodSymbol2); if (!paramBoolean) { JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(methodSymbol, null); JCTree.JCExpression jCExpression = (JCTree.JCExpression)((paramMethodSymbol2.owner == paramClassSymbol) ? this.make.This(paramClassSymbol.erasure(this.types)) : this.make.Super((this.types.supertype(paramClassSymbol.type)).tsym.erasure(this.types), (Symbol.TypeSymbol)paramClassSymbol)); Type type = erasure(paramMethodSymbol2.type.getReturnType()); JCTree.JCMethodInvocation jCMethodInvocation = this.make.Apply(null, this.make.Select(jCExpression, (Symbol)paramMethodSymbol2).setType(type), translateArgs(this.make.Idents(jCMethodDecl.params), type2.getParameterTypes(), (Type)null)).setType(type); Object object = type2.getReturnType().hasTag(TypeTag.VOID) ? this.make.Exec((JCTree.JCExpression)jCMethodInvocation) : this.make.Return(coerce((JCTree.JCExpression)jCMethodInvocation, type3.getReturnType())); jCMethodDecl.body = this.make.Block(0L, List.of(object)); paramListBuffer.append(jCMethodDecl); }  paramClassSymbol.members().enter((Symbol)methodSymbol); this.overridden.put(methodSymbol, paramMethodSymbol1); } private List<Symbol.VarSymbol> createBridgeParams(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Type paramType) { List<Symbol.VarSymbol> list = null; if (paramMethodSymbol1.params != null) { list = List.nil(); List list1 = paramMethodSymbol1.params; Type.MethodType methodType = (Type.MethodType)paramType; List list2 = methodType.argtypes; while (list1.nonEmpty() && list2.nonEmpty()) { Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(((Symbol.VarSymbol)list1.head).flags() | 0x1000L | 0x200000000L, ((Symbol.VarSymbol)list1.head).name, (Type)list2.head, (Symbol)paramMethodSymbol2); varSymbol.setAttributes((Symbol)list1.head); list = list.append(varSymbol); list1 = list1.tail; list2 = list2.tail; }  }  return list; }
/*  517 */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) { JCTree jCTree = this.currentMethod;
/*      */     try {
/*  519 */       this.currentMethod = (JCTree)paramJCMethodDecl;
/*  520 */       paramJCMethodDecl.restype = translate(paramJCMethodDecl.restype, (Type)null);
/*  521 */       paramJCMethodDecl.typarams = List.nil();
/*  522 */       paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/*  523 */       paramJCMethodDecl.recvparam = translate(paramJCMethodDecl.recvparam, (Type)null);
/*  524 */       paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown, (Type)null);
/*  525 */       paramJCMethodDecl.body = translate(paramJCMethodDecl.body, paramJCMethodDecl.sym.erasure(this.types).getReturnType());
/*  526 */       paramJCMethodDecl.type = erasure(paramJCMethodDecl.type);
/*  527 */       this.result = (JCTree)paramJCMethodDecl;
/*      */     } finally {
/*  529 */       this.currentMethod = jCTree;
/*      */     } 
/*      */ 
/*      */     
/*  533 */     Scope.Entry entry = paramJCMethodDecl.sym.owner.members().lookup(paramJCMethodDecl.name);
/*  534 */     for (; entry.sym != null; 
/*  535 */       entry = entry.next())
/*  536 */     { if (entry.sym != paramJCMethodDecl.sym && this.types
/*  537 */         .isSameType(erasure(entry.sym.type), paramJCMethodDecl.type))
/*  538 */       { this.log.error(paramJCMethodDecl.pos(), "name.clash.same.erasure", new Object[] { paramJCMethodDecl.sym, entry.sym }); return; }  }  }
/*      */   void addBridgeIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer) { if (paramSymbol.kind == 16 && paramSymbol.name != this.names.init && (paramSymbol.flags() & 0xAL) == 0L && (paramSymbol.flags() & 0x10000001000L) != 4096L && paramSymbol.isMemberOf((Symbol.TypeSymbol)paramClassSymbol, this.types)) { Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)paramSymbol; Symbol.MethodSymbol methodSymbol2 = methodSymbol1.binaryImplementation(paramClassSymbol, this.types); Symbol.MethodSymbol methodSymbol3 = methodSymbol1.implementation((Symbol.TypeSymbol)paramClassSymbol, this.types, true, this.overrideBridgeFilter); if (methodSymbol2 == null || methodSymbol2 == methodSymbol1 || (methodSymbol3 != null && !methodSymbol2.owner.isSubClass(methodSymbol3.owner, this.types))) { if (methodSymbol3 != null && isBridgeNeeded(methodSymbol1, methodSymbol3, paramClassSymbol.type)) { addBridge(paramDiagnosticPosition, methodSymbol1, methodSymbol3, paramClassSymbol, (methodSymbol2 == methodSymbol3), paramListBuffer); } else if (methodSymbol3 == methodSymbol1 && methodSymbol3.owner != paramClassSymbol && (methodSymbol3.flags() & 0x10L) == 0L && (methodSymbol1.flags() & 0x401L) == 1L && (paramClassSymbol.flags() & 0x1L) > (methodSymbol3.owner.flags() & 0x1L)) { addBridge(paramDiagnosticPosition, methodSymbol1, methodSymbol3, paramClassSymbol, false, paramListBuffer); }  } else if ((methodSymbol2.flags() & 0x10000001000L) == 4096L) { Symbol.MethodSymbol methodSymbol = this.overridden.get(methodSymbol2); if (methodSymbol != null && methodSymbol != methodSymbol1 && (methodSymbol3 == null || !methodSymbol3.overrides((Symbol)methodSymbol, (Symbol.TypeSymbol)paramClassSymbol, this.types, true))) this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { methodSymbol, methodSymbol.location(paramClassSymbol.type, this.types), methodSymbol1, methodSymbol1.location(paramClassSymbol.type, this.types) });  } else if (!methodSymbol2.overrides((Symbol)methodSymbol1, (Symbol.TypeSymbol)paramClassSymbol, this.types, true)) { if (methodSymbol2.owner == paramClassSymbol || this.types.asSuper(methodSymbol2.owner.type, methodSymbol1.owner) == null) this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { methodSymbol2, methodSymbol2.location(paramClassSymbol.type, this.types), methodSymbol1, methodSymbol1.location(paramClassSymbol.type, this.types) });  }  }  }
/*      */   private boolean isBridgeNeeded(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Type paramType) { if (paramMethodSymbol2 != paramMethodSymbol1) { Type type1 = paramMethodSymbol1.erasure(this.types); if (!isSameMemberWhenErased(paramType, paramMethodSymbol1, type1))
/*      */         return true;  Type type2 = paramMethodSymbol2.erasure(this.types); if (!isSameMemberWhenErased(paramType, paramMethodSymbol2, type2))
/*      */         return true;  return !this.types.isSameType(type2.getReturnType(), type1.getReturnType()); }  if ((paramMethodSymbol1.flags() & 0x400L) != 0L)
/*      */       return false;  return !isSameMemberWhenErased(paramType, paramMethodSymbol1, paramMethodSymbol1.erasure(this.types)); }
/*      */   private boolean isSameMemberWhenErased(Type paramType1, Symbol.MethodSymbol paramMethodSymbol, Type paramType2) { return this.types.isSameType(erasure(this.types.memberType(paramType1, (Symbol)paramMethodSymbol)), paramType2); }
/*      */   void addBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer) { for (Scope.Entry entry = (paramTypeSymbol.members()).elems; entry != null; entry = entry.sibling)
/*      */       addBridgeIfNeeded(paramDiagnosticPosition, entry.sym, paramClassSymbol, paramListBuffer);  for (List list = this.types.interfaces(paramTypeSymbol.type); list.nonEmpty(); list = list.tail)
/*  547 */       addBridges(paramDiagnosticPosition, ((Type)list.head).tsym, paramClassSymbol, paramListBuffer);  } public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) { paramJCVariableDecl.vartype = translate(paramJCVariableDecl.vartype, (Type)null);
/*  548 */     paramJCVariableDecl.init = translate(paramJCVariableDecl.init, paramJCVariableDecl.sym.erasure(this.types));
/*  549 */     paramJCVariableDecl.type = erasure(paramJCVariableDecl.type);
/*  550 */     this.result = (JCTree)paramJCVariableDecl; }
/*      */   void addBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer) { Type type = this.types.supertype(paramClassSymbol.type); while (type.hasTag(TypeTag.CLASS)) { addBridges(paramDiagnosticPosition, type.tsym, paramClassSymbol, paramListBuffer); type = this.types.supertype(type); }  for (List list = this.types.interfaces(paramClassSymbol.type); list.nonEmpty(); list = list.tail) addBridges(paramDiagnosticPosition, ((Type)list.head).tsym, paramClassSymbol, paramListBuffer);  }
/*      */   public <T extends JCTree> T translate(T paramT, Type paramType) { Type type = this.pt; try { this.pt = paramType; return (T)translate((JCTree)paramT); } finally { this.pt = type; }  }
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, Type paramType) { List<T> list; Type type = this.pt; try { this.pt = paramType; list = translate(paramList); } finally { this.pt = type; }  return list; }
/*  554 */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) { translateClass(paramJCClassDecl.sym); this.result = (JCTree)paramJCClassDecl; } public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) { paramJCDoWhileLoop.body = (JCTree.JCStatement)translate((JCTree)paramJCDoWhileLoop.body);
/*  555 */     paramJCDoWhileLoop.cond = translate(paramJCDoWhileLoop.cond, (Type)this.syms.booleanType);
/*  556 */     this.result = (JCTree)paramJCDoWhileLoop; }
/*      */ 
/*      */   
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/*  560 */     paramJCWhileLoop.cond = translate(paramJCWhileLoop.cond, (Type)this.syms.booleanType);
/*  561 */     paramJCWhileLoop.body = (JCTree.JCStatement)translate((JCTree)paramJCWhileLoop.body);
/*  562 */     this.result = (JCTree)paramJCWhileLoop;
/*      */   }
/*      */   
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/*  566 */     paramJCForLoop.init = translate(paramJCForLoop.init, (Type)null);
/*  567 */     if (paramJCForLoop.cond != null)
/*  568 */       paramJCForLoop.cond = translate(paramJCForLoop.cond, (Type)this.syms.booleanType); 
/*  569 */     paramJCForLoop.step = translate(paramJCForLoop.step, (Type)null);
/*  570 */     paramJCForLoop.body = (JCTree.JCStatement)translate((JCTree)paramJCForLoop.body);
/*  571 */     this.result = (JCTree)paramJCForLoop;
/*      */   }
/*      */   
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*  575 */     paramJCEnhancedForLoop.var = translate(paramJCEnhancedForLoop.var, (Type)null);
/*  576 */     Type type = paramJCEnhancedForLoop.expr.type;
/*  577 */     paramJCEnhancedForLoop.expr = translate(paramJCEnhancedForLoop.expr, erasure(paramJCEnhancedForLoop.expr.type));
/*  578 */     if (this.types.elemtype(paramJCEnhancedForLoop.expr.type) == null)
/*  579 */       paramJCEnhancedForLoop.expr.type = type; 
/*  580 */     paramJCEnhancedForLoop.body = (JCTree.JCStatement)translate((JCTree)paramJCEnhancedForLoop.body);
/*  581 */     this.result = (JCTree)paramJCEnhancedForLoop;
/*      */   }
/*      */   
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/*  585 */     JCTree jCTree = this.currentMethod;
/*      */     try {
/*  587 */       this.currentMethod = null;
/*  588 */       paramJCLambda.params = translate(paramJCLambda.params);
/*  589 */       paramJCLambda.body = translate(paramJCLambda.body, (paramJCLambda.body.type == null) ? null : erasure(paramJCLambda.body.type));
/*  590 */       paramJCLambda.type = erasure(paramJCLambda.type);
/*  591 */       this.result = (JCTree)paramJCLambda;
/*      */     } finally {
/*      */       
/*  594 */       this.currentMethod = jCTree;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/*  599 */     Type type1 = this.types.supertype(paramJCSwitch.selector.type);
/*  600 */     boolean bool = (type1 != null && type1.tsym == this.syms.enumSym) ? true : false;
/*      */     
/*  602 */     Type type2 = (Type)(bool ? erasure(paramJCSwitch.selector.type) : this.syms.intType);
/*  603 */     paramJCSwitch.selector = translate(paramJCSwitch.selector, type2);
/*  604 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/*  605 */     this.result = (JCTree)paramJCSwitch;
/*      */   }
/*      */   
/*      */   public void visitCase(JCTree.JCCase paramJCCase) {
/*  609 */     paramJCCase.pat = translate(paramJCCase.pat, (Type)null);
/*  610 */     paramJCCase.stats = translate(paramJCCase.stats);
/*  611 */     this.result = (JCTree)paramJCCase;
/*      */   }
/*      */   
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/*  615 */     paramJCSynchronized.lock = translate(paramJCSynchronized.lock, erasure(paramJCSynchronized.lock.type));
/*  616 */     paramJCSynchronized.body = (JCTree.JCBlock)translate((JCTree)paramJCSynchronized.body);
/*  617 */     this.result = (JCTree)paramJCSynchronized;
/*      */   }
/*      */   
/*      */   public void visitTry(JCTree.JCTry paramJCTry) {
/*  621 */     paramJCTry.resources = translate(paramJCTry.resources, this.syms.autoCloseableType);
/*  622 */     paramJCTry.body = (JCTree.JCBlock)translate((JCTree)paramJCTry.body);
/*  623 */     paramJCTry.catchers = translateCatchers(paramJCTry.catchers);
/*  624 */     paramJCTry.finalizer = (JCTree.JCBlock)translate((JCTree)paramJCTry.finalizer);
/*  625 */     this.result = (JCTree)paramJCTry;
/*      */   }
/*      */   
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/*  629 */     paramJCConditional.cond = translate(paramJCConditional.cond, (Type)this.syms.booleanType);
/*  630 */     paramJCConditional.truepart = translate(paramJCConditional.truepart, erasure(paramJCConditional.type));
/*  631 */     paramJCConditional.falsepart = translate(paramJCConditional.falsepart, erasure(paramJCConditional.type));
/*  632 */     paramJCConditional.type = erasure(paramJCConditional.type);
/*  633 */     this.result = (JCTree)retype((JCTree.JCExpression)paramJCConditional, paramJCConditional.type, this.pt);
/*      */   }
/*      */   
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/*  637 */     paramJCIf.cond = translate(paramJCIf.cond, (Type)this.syms.booleanType);
/*  638 */     paramJCIf.thenpart = (JCTree.JCStatement)translate((JCTree)paramJCIf.thenpart);
/*  639 */     paramJCIf.elsepart = (JCTree.JCStatement)translate((JCTree)paramJCIf.elsepart);
/*  640 */     this.result = (JCTree)paramJCIf;
/*      */   }
/*      */   
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/*  644 */     paramJCExpressionStatement.expr = translate(paramJCExpressionStatement.expr, (Type)null);
/*  645 */     this.result = (JCTree)paramJCExpressionStatement;
/*      */   }
/*      */   
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*  649 */     paramJCReturn.expr = translate(paramJCReturn.expr, (this.currentMethod != null) ? this.types.erasure(this.currentMethod.type).getReturnType() : null);
/*  650 */     this.result = (JCTree)paramJCReturn;
/*      */   }
/*      */   
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/*  654 */     paramJCThrow.expr = translate(paramJCThrow.expr, erasure(paramJCThrow.expr.type));
/*  655 */     this.result = (JCTree)paramJCThrow;
/*      */   }
/*      */   
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/*  659 */     paramJCAssert.cond = translate(paramJCAssert.cond, (Type)this.syms.booleanType);
/*  660 */     if (paramJCAssert.detail != null)
/*  661 */       paramJCAssert.detail = translate(paramJCAssert.detail, erasure(paramJCAssert.detail.type)); 
/*  662 */     this.result = (JCTree)paramJCAssert;
/*      */   }
/*      */   
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/*  666 */     paramJCMethodInvocation.meth = translate(paramJCMethodInvocation.meth, (Type)null);
/*  667 */     Symbol symbol = TreeInfo.symbol((JCTree)paramJCMethodInvocation.meth);
/*  668 */     Type type = symbol.erasure(this.types);
/*  669 */     List<Type> list = type.getParameterTypes();
/*  670 */     if (this.allowEnums && symbol.name == this.names.init && symbol.owner == this.syms.enumSym)
/*      */     {
/*      */       
/*  673 */       list = list.tail.tail; } 
/*  674 */     if (paramJCMethodInvocation.varargsElement != null) {
/*  675 */       paramJCMethodInvocation.varargsElement = this.types.erasure(paramJCMethodInvocation.varargsElement);
/*      */     }
/*  677 */     else if (paramJCMethodInvocation.args.length() != list.length()) {
/*  678 */       this.log.error(paramJCMethodInvocation.pos(), "method.invoked.with.incorrect.number.arguments", new Object[] {
/*      */             
/*  680 */             Integer.valueOf(paramJCMethodInvocation.args.length()), Integer.valueOf(list.length()) });
/*      */     } 
/*  682 */     paramJCMethodInvocation.args = translateArgs(paramJCMethodInvocation.args, list, paramJCMethodInvocation.varargsElement);
/*      */     
/*  684 */     paramJCMethodInvocation.type = this.types.erasure(paramJCMethodInvocation.type);
/*      */     
/*  686 */     this.result = (JCTree)retype((JCTree.JCExpression)paramJCMethodInvocation, type.getReturnType(), this.pt);
/*      */   }
/*      */   
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*  690 */     if (paramJCNewClass.encl != null)
/*  691 */       paramJCNewClass.encl = translate(paramJCNewClass.encl, erasure(paramJCNewClass.encl.type)); 
/*  692 */     paramJCNewClass.clazz = translate(paramJCNewClass.clazz, (Type)null);
/*  693 */     if (paramJCNewClass.varargsElement != null)
/*  694 */       paramJCNewClass.varargsElement = this.types.erasure(paramJCNewClass.varargsElement); 
/*  695 */     paramJCNewClass.args = translateArgs(paramJCNewClass.args, paramJCNewClass.constructor
/*  696 */         .erasure(this.types).getParameterTypes(), paramJCNewClass.varargsElement);
/*  697 */     paramJCNewClass.def = translate(paramJCNewClass.def, (Type)null);
/*  698 */     if (paramJCNewClass.constructorType != null)
/*  699 */       paramJCNewClass.constructorType = erasure(paramJCNewClass.constructorType); 
/*  700 */     paramJCNewClass.type = erasure(paramJCNewClass.type);
/*  701 */     this.result = (JCTree)paramJCNewClass;
/*      */   }
/*      */   
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/*  705 */     paramJCNewArray.elemtype = translate(paramJCNewArray.elemtype, (Type)null);
/*  706 */     translate(paramJCNewArray.dims, (Type)this.syms.intType);
/*  707 */     if (paramJCNewArray.type != null) {
/*  708 */       paramJCNewArray.elems = translate(paramJCNewArray.elems, erasure(this.types.elemtype(paramJCNewArray.type)));
/*  709 */       paramJCNewArray.type = erasure(paramJCNewArray.type);
/*      */     } else {
/*  711 */       paramJCNewArray.elems = translate(paramJCNewArray.elems, (Type)null);
/*      */     } 
/*      */     
/*  714 */     this.result = (JCTree)paramJCNewArray;
/*      */   }
/*      */   
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/*  718 */     paramJCParens.expr = translate(paramJCParens.expr, this.pt);
/*  719 */     paramJCParens.type = erasure(paramJCParens.type);
/*  720 */     this.result = (JCTree)paramJCParens;
/*      */   }
/*      */   
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/*  724 */     paramJCAssign.lhs = translate(paramJCAssign.lhs, (Type)null);
/*  725 */     paramJCAssign.rhs = translate(paramJCAssign.rhs, erasure(paramJCAssign.lhs.type));
/*  726 */     paramJCAssign.type = erasure(paramJCAssign.lhs.type);
/*  727 */     this.result = (JCTree)retype((JCTree.JCExpression)paramJCAssign, paramJCAssign.type, this.pt);
/*      */   }
/*      */   
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/*  731 */     paramJCAssignOp.lhs = translate(paramJCAssignOp.lhs, (Type)null);
/*  732 */     paramJCAssignOp.rhs = translate(paramJCAssignOp.rhs, (Type)(paramJCAssignOp.operator.type.getParameterTypes()).tail.head);
/*  733 */     paramJCAssignOp.type = erasure(paramJCAssignOp.type);
/*  734 */     this.result = (JCTree)paramJCAssignOp;
/*      */   }
/*      */   
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/*  738 */     paramJCUnary.arg = translate(paramJCUnary.arg, (Type)(paramJCUnary.operator.type.getParameterTypes()).head);
/*  739 */     this.result = (JCTree)paramJCUnary;
/*      */   }
/*      */   
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/*  743 */     paramJCBinary.lhs = translate(paramJCBinary.lhs, (Type)(paramJCBinary.operator.type.getParameterTypes()).head);
/*  744 */     paramJCBinary.rhs = translate(paramJCBinary.rhs, (Type)(paramJCBinary.operator.type.getParameterTypes()).tail.head);
/*  745 */     this.result = (JCTree)paramJCBinary;
/*      */   }
/*      */   
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/*  749 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz, (Type)null);
/*  750 */     Type type = paramJCTypeCast.type;
/*  751 */     paramJCTypeCast.type = erasure(paramJCTypeCast.type);
/*  752 */     paramJCTypeCast.expr = translate(paramJCTypeCast.expr, paramJCTypeCast.type);
/*  753 */     if (type.isIntersection()) {
/*  754 */       Type.IntersectionClassType intersectionClassType = (Type.IntersectionClassType)type;
/*  755 */       for (Type type1 : intersectionClassType.getExplicitComponents()) {
/*  756 */         Type type2 = erasure(type1);
/*  757 */         if (!this.types.isSameType(type2, paramJCTypeCast.type)) {
/*  758 */           paramJCTypeCast.expr = coerce(paramJCTypeCast.expr, type2);
/*      */         }
/*      */       } 
/*      */     } 
/*  762 */     this.result = (JCTree)paramJCTypeCast;
/*      */   }
/*      */   
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/*  766 */     paramJCInstanceOf.expr = translate(paramJCInstanceOf.expr, (Type)null);
/*  767 */     paramJCInstanceOf.clazz = translate(paramJCInstanceOf.clazz, (Type)null);
/*  768 */     this.result = (JCTree)paramJCInstanceOf;
/*      */   }
/*      */   
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/*  772 */     paramJCArrayAccess.indexed = translate(paramJCArrayAccess.indexed, erasure(paramJCArrayAccess.indexed.type));
/*  773 */     paramJCArrayAccess.index = translate(paramJCArrayAccess.index, (Type)this.syms.intType);
/*      */ 
/*      */     
/*  776 */     this.result = (JCTree)retype((JCTree.JCExpression)paramJCArrayAccess, this.types.elemtype(paramJCArrayAccess.indexed.type), this.pt);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/*  782 */     this.result = (JCTree)paramJCAnnotation;
/*      */   }
/*      */   
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/*  786 */     Type type = paramJCIdent.sym.erasure(this.types);
/*      */ 
/*      */     
/*  789 */     if (paramJCIdent.sym.kind == 2 && paramJCIdent.sym.type.hasTag(TypeTag.TYPEVAR)) {
/*  790 */       this.result = (JCTree)this.make.at(paramJCIdent.pos).Type(type);
/*      */     
/*      */     }
/*  793 */     else if (paramJCIdent.type.constValue() != null) {
/*  794 */       this.result = (JCTree)paramJCIdent;
/*      */     
/*      */     }
/*  797 */     else if (paramJCIdent.sym.kind == 4) {
/*  798 */       this.result = (JCTree)retype((JCTree.JCExpression)paramJCIdent, type, this.pt);
/*      */     } else {
/*      */       
/*  801 */       paramJCIdent.type = erasure(paramJCIdent.type);
/*  802 */       this.result = (JCTree)paramJCIdent;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/*  807 */     Type type = paramJCFieldAccess.selected.type;
/*  808 */     while (type.hasTag(TypeTag.TYPEVAR))
/*  809 */       type = type.getUpperBound(); 
/*  810 */     if (type.isCompound()) {
/*  811 */       if ((paramJCFieldAccess.sym.flags() & 0x200000L) != 0L) {
/*  812 */         paramJCFieldAccess
/*  813 */           .sym = ((Symbol.MethodSymbol)paramJCFieldAccess.sym).implemented((Symbol.TypeSymbol)paramJCFieldAccess.sym.owner, this.types);
/*      */       }
/*  815 */       paramJCFieldAccess.selected = coerce(
/*  816 */           translate(paramJCFieldAccess.selected, erasure(paramJCFieldAccess.selected.type)), 
/*  817 */           erasure(paramJCFieldAccess.sym.owner.type));
/*      */     } else {
/*  819 */       paramJCFieldAccess.selected = translate(paramJCFieldAccess.selected, erasure(type));
/*      */     } 
/*      */     
/*  822 */     if (paramJCFieldAccess.type.constValue() != null) {
/*  823 */       this.result = (JCTree)paramJCFieldAccess;
/*      */     
/*      */     }
/*  826 */     else if (paramJCFieldAccess.sym.kind == 4) {
/*  827 */       this.result = (JCTree)retype((JCTree.JCExpression)paramJCFieldAccess, paramJCFieldAccess.sym.erasure(this.types), this.pt);
/*      */     } else {
/*      */       
/*  830 */       paramJCFieldAccess.type = erasure(paramJCFieldAccess.type);
/*  831 */       this.result = (JCTree)paramJCFieldAccess;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/*  836 */     paramJCMemberReference.expr = translate(paramJCMemberReference.expr, erasure(paramJCMemberReference.expr.type));
/*  837 */     paramJCMemberReference.type = erasure(paramJCMemberReference.type);
/*  838 */     if (paramJCMemberReference.varargsElement != null)
/*  839 */       paramJCMemberReference.varargsElement = erasure(paramJCMemberReference.varargsElement); 
/*  840 */     this.result = (JCTree)paramJCMemberReference;
/*      */   }
/*      */   
/*      */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/*  844 */     paramJCArrayTypeTree.elemtype = translate(paramJCArrayTypeTree.elemtype, (Type)null);
/*  845 */     paramJCArrayTypeTree.type = erasure(paramJCArrayTypeTree.type);
/*  846 */     this.result = (JCTree)paramJCArrayTypeTree;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/*  852 */     JCTree jCTree = (JCTree)translate(paramJCTypeApply.clazz, (Type)null);
/*  853 */     this.result = jCTree;
/*      */   }
/*      */   
/*      */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/*  857 */     paramJCTypeIntersection.bounds = translate(paramJCTypeIntersection.bounds, (Type)null);
/*  858 */     paramJCTypeIntersection.type = erasure(paramJCTypeIntersection.type);
/*  859 */     this.result = (JCTree)paramJCTypeIntersection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type erasure(Type paramType) {
/*  867 */     return this.types.erasure(paramType);
/*      */   }
/*      */   
/*      */   private boolean boundsRestricted(Symbol.ClassSymbol paramClassSymbol) {
/*  871 */     Type type = this.types.supertype(paramClassSymbol.type);
/*  872 */     if (type.isParameterized()) {
/*  873 */       List list1 = type.allparams();
/*  874 */       List list2 = type.tsym.type.allparams();
/*  875 */       while (!list1.isEmpty() && !list2.isEmpty()) {
/*  876 */         Type type1 = (Type)list1.head;
/*  877 */         Type type2 = (Type)list2.head;
/*      */         
/*  879 */         if (!this.types.isSameType(this.types.erasure(type1), this.types
/*  880 */             .erasure(type2))) {
/*  881 */           return true;
/*      */         }
/*  883 */         list1 = list1.tail;
/*  884 */         list2 = list2.tail;
/*      */       } 
/*      */     } 
/*  887 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<JCTree> addOverrideBridgesIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) {
/*  892 */     ListBuffer<JCTree> listBuffer = new ListBuffer();
/*  893 */     if (paramClassSymbol.isInterface() || !boundsRestricted(paramClassSymbol))
/*  894 */       return listBuffer.toList(); 
/*  895 */     Type type = this.types.supertype(paramClassSymbol.type);
/*  896 */     Scope scope = type.tsym.members();
/*  897 */     if (scope.elems != null) {
/*  898 */       for (Symbol symbol : scope.getElements(new NeedsOverridBridgeFilter(paramClassSymbol))) {
/*      */         
/*  900 */         Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)symbol;
/*  901 */         Symbol.MethodSymbol methodSymbol2 = (Symbol.MethodSymbol)methodSymbol1.asMemberOf(paramClassSymbol.type, this.types);
/*  902 */         Symbol.MethodSymbol methodSymbol3 = methodSymbol1.implementation((Symbol.TypeSymbol)paramClassSymbol, this.types, false);
/*      */         
/*  904 */         if ((methodSymbol3 == null || methodSymbol3.owner != paramClassSymbol) && 
/*  905 */           !this.types.isSameType(methodSymbol2.erasure(this.types), methodSymbol1.erasure(this.types))) {
/*  906 */           addOverrideBridges(paramDiagnosticPosition, methodSymbol1, methodSymbol2, paramClassSymbol, listBuffer);
/*      */         }
/*      */       } 
/*      */     }
/*  910 */     return listBuffer.toList();
/*      */   }
/*      */   
/*      */   class NeedsOverridBridgeFilter
/*      */     implements Filter<Symbol> {
/*      */     Symbol.ClassSymbol c;
/*      */     
/*      */     NeedsOverridBridgeFilter(Symbol.ClassSymbol param1ClassSymbol) {
/*  918 */       this.c = param1ClassSymbol;
/*      */     }
/*      */     public boolean accepts(Symbol param1Symbol) {
/*  921 */       return (param1Symbol.kind == 16 && 
/*  922 */         !param1Symbol.isConstructor() && param1Symbol
/*  923 */         .isInheritedIn((Symbol)this.c, TransTypes.this.types) && (param1Symbol
/*  924 */         .flags() & 0x10L) == 0L && (param1Symbol
/*  925 */         .flags() & 0x10000001000L) != 4096L);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addOverrideBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer) {
/*  934 */     Type type1 = paramMethodSymbol1.erasure(this.types);
/*  935 */     long l = paramMethodSymbol1.flags() & 0x7L | 0x1000L | 0x80000000L | 0x10000000000L;
/*  936 */     paramMethodSymbol2 = new Symbol.MethodSymbol(l, paramMethodSymbol2.name, paramMethodSymbol2.type, (Symbol)paramClassSymbol);
/*  937 */     JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(paramMethodSymbol2, null);
/*  938 */     JCTree.JCIdent jCIdent = this.make.Super((this.types.supertype(paramClassSymbol.type)).tsym.erasure(this.types), (Symbol.TypeSymbol)paramClassSymbol);
/*  939 */     Type type2 = erasure(paramMethodSymbol1.type.getReturnType());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  945 */     JCTree.JCMethodInvocation jCMethodInvocation = this.make.Apply(null, this.make.Select((JCTree.JCExpression)jCIdent, (Symbol)paramMethodSymbol1).setType(type2), translateArgs(this.make.Idents(jCMethodDecl.params), type1.getParameterTypes(), (Type)null)).setType(type2);
/*      */ 
/*      */     
/*  948 */     Object object = paramMethodSymbol2.getReturnType().hasTag(TypeTag.VOID) ? this.make.Exec((JCTree.JCExpression)jCMethodInvocation) : this.make.Return(coerce((JCTree.JCExpression)jCMethodInvocation, paramMethodSymbol2.erasure(this.types).getReturnType()));
/*  949 */     jCMethodDecl.body = this.make.Block(0L, List.of(object));
/*  950 */     paramClassSymbol.members().enter((Symbol)paramMethodSymbol2);
/*  951 */     paramListBuffer.append(jCMethodDecl);
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
/*      */   void translateClass(Symbol.ClassSymbol paramClassSymbol) {
/*  964 */     Type type = this.types.supertype(paramClassSymbol.type);
/*      */     
/*  966 */     if (type.hasTag(TypeTag.CLASS)) {
/*  967 */       translateClass((Symbol.ClassSymbol)type.tsym);
/*      */     }
/*      */     
/*  970 */     Env<AttrContext> env1 = this.enter.getEnv((Symbol.TypeSymbol)paramClassSymbol);
/*  971 */     if (env1 == null || (paramClassSymbol.flags_field & 0x4000000000000L) != 0L) {
/*      */       return;
/*      */     }
/*  974 */     paramClassSymbol.flags_field |= 0x4000000000000L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  984 */     boolean bool = (this.compileStates.get(env1) != null) ? true : false;
/*  985 */     if (!bool && paramClassSymbol.outermostClass() == paramClassSymbol) {
/*  986 */       Assert.error("No info for outermost class: " + env1.enclClass.sym);
/*      */     }
/*      */     
/*  989 */     if (bool && CompileStates.CompileState.FLOW
/*  990 */       .isAfter(this.compileStates.get(env1))) {
/*  991 */       Assert.error(String.format("The current compile state [%s] of class %s is previous to FLOW", new Object[] { this.compileStates
/*  992 */               .get(env1), env1.enclClass.sym }));
/*      */     }
/*      */     
/*  995 */     Env<AttrContext> env2 = this.env;
/*      */     try {
/*  997 */       this.env = env1;
/*      */ 
/*      */       
/* 1000 */       TreeMaker treeMaker = this.make;
/* 1001 */       Type type1 = this.pt;
/* 1002 */       this.make = this.make.forToplevel(this.env.toplevel);
/* 1003 */       this.pt = null;
/*      */       try {
/* 1005 */         JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)this.env.tree;
/* 1006 */         jCClassDecl.typarams = List.nil();
/* 1007 */         super.visitClassDef(jCClassDecl);
/* 1008 */         this.make.at(jCClassDecl.pos);
/* 1009 */         if (this.addBridges) {
/* 1010 */           ListBuffer<JCTree> listBuffer = new ListBuffer();
/*      */ 
/*      */           
/* 1013 */           if (this.allowInterfaceBridges || (jCClassDecl.sym.flags() & 0x200L) == 0L) {
/* 1014 */             addBridges(jCClassDecl.pos(), paramClassSymbol, listBuffer);
/*      */           }
/* 1016 */           jCClassDecl.defs = listBuffer.toList().prependList(jCClassDecl.defs);
/*      */         } 
/* 1018 */         jCClassDecl.type = erasure(jCClassDecl.type);
/*      */       } finally {
/* 1020 */         this.make = treeMaker;
/* 1021 */         this.pt = type1;
/*      */       } 
/*      */     } finally {
/* 1024 */       this.env = env2;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JCTree translateTopLevelClass(JCTree paramJCTree, TreeMaker paramTreeMaker) {
/* 1033 */     this.make = paramTreeMaker;
/* 1034 */     this.pt = null;
/* 1035 */     return translate(paramJCTree, (Type)null);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\TransTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */