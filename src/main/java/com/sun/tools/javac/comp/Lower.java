/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.ClassWriter;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Lower
/*      */   extends TreeTranslator
/*      */ {
/*   62 */   protected static final Context.Key<Lower> lowerKey = new Context.Key(); private Names names; private Log log; private Symtab syms; private Resolve rs; private Check chk; private Attr attr; private TreeMaker make; private JCDiagnostic.DiagnosticPosition make_pos; private ClassWriter writer; private ClassReader reader; private ConstFold cfolder; private Target target; private Source source; private final TypeEnvs typeEnvs; private boolean allowEnums; private final Name dollarAssertionsDisabled; private final Name classDollar; private Types types; private boolean debugLower; private Option.PkgInfo pkginfoOpt; Symbol.ClassSymbol currentClass; ListBuffer<JCTree> translated; Env<AttrContext> attrEnv; EndPosTable endPosTable; Map<Symbol.ClassSymbol, JCTree.JCClassDecl> classdefs; public Map<Symbol.ClassSymbol, List<JCTree>> prunedTree; Map<Symbol, Symbol> actualSymbols; JCTree.JCMethodDecl currentMethodDef; Symbol.MethodSymbol currentMethodSym; JCTree.JCClassDecl outermostClassDef; JCTree outermostMemberDef; Map<Symbol, Symbol> lambdaTranslationMap; ClassMap classMap; Map<Symbol.ClassSymbol, List<Symbol.VarSymbol>> freevarCache; Map<Symbol.TypeSymbol, EnumMapping> enumSwitchMap; JCTree.Visitor conflictsChecker; private static final int DEREFcode = 0; private static final int ASSIGNcode = 2; private static final int PREINCcode = 4; private static final int PREDECcode = 6; private static final int POSTINCcode = 8; private static final int POSTDECcode = 10;
/*      */   private static final int FIRSTASGOPcode = 12;
/*      */
/*      */   public static Lower instance(Context paramContext) {
/*   66 */     Lower lower = (Lower)paramContext.get(lowerKey);
/*   67 */     if (lower == null)
/*   68 */       lower = new Lower(paramContext);
/*   69 */     return lower;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Lower(Context paramContext) {
/*  146 */     this.prunedTree = new WeakHashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  173 */     this.lambdaTranslationMap = null;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  188 */     this.classMap = new ClassMap();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  401 */     this.enumSwitchMap = new LinkedHashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  715 */     this.conflictsChecker = (JCTree.Visitor)new TreeScanner()
/*      */       {
/*      */         Symbol.TypeSymbol currentClass;
/*      */
/*      */
/*      */         public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/*  721 */           Lower.this.chk.checkConflicts(param1JCMethodDecl.pos(), (Symbol)param1JCMethodDecl.sym, this.currentClass);
/*  722 */           super.visitMethodDef(param1JCMethodDecl);
/*      */         }
/*      */
/*      */
/*      */         public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/*  727 */           if (param1JCVariableDecl.sym.owner.kind == 2) {
/*  728 */             Lower.this.chk.checkConflicts(param1JCVariableDecl.pos(), (Symbol)param1JCVariableDecl.sym, this.currentClass);
/*      */           }
/*  730 */           super.visitVarDef(param1JCVariableDecl);
/*      */         }
/*      */
/*      */         public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl)
/*      */         {
/*  735 */           Symbol.TypeSymbol typeSymbol = this.currentClass;
/*  736 */           this.currentClass = (Symbol.TypeSymbol)param1JCClassDecl.sym;
/*      */
/*  738 */           try { super.visitClassDef(param1JCClassDecl); }
/*      */           finally
/*      */
/*  741 */           { this.currentClass = typeSymbol; }  }
/*      */       }; paramContext.put(lowerKey, this); this.names = Names.instance(paramContext); this.log = Log.instance(paramContext); this.syms = Symtab.instance(paramContext); this.rs = Resolve.instance(paramContext); this.chk = Check.instance(paramContext); this.attr = Attr.instance(paramContext); this.make = TreeMaker.instance(paramContext); this.writer = ClassWriter.instance(paramContext); this.reader = ClassReader.instance(paramContext); this.cfolder = ConstFold.instance(paramContext); this.target = Target.instance(paramContext); this.source = Source.instance(paramContext); this.typeEnvs = TypeEnvs.instance(paramContext); this.allowEnums = this.source.allowEnums(); this.dollarAssertionsDisabled = this.names.fromString(this.target.syntheticNameChar() + "assertionsDisabled"); this.classDollar = this.names.fromString("class" + this.target.syntheticNameChar()); this.types = Types.instance(paramContext); Options options = Options.instance(paramContext); this.debugLower = options.isSet("debuglower"); this.pkginfoOpt = Option.PkgInfo.get(options);
/*      */   }
/*      */   class ClassMap extends TreeScanner {
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) { Lower.this.classdefs.put(param1JCClassDecl.sym, param1JCClassDecl); super.visitClassDef(param1JCClassDecl); } }
/*      */   JCTree.JCClassDecl classDef(Symbol.ClassSymbol paramClassSymbol) { JCTree.JCClassDecl jCClassDecl = this.classdefs.get(paramClassSymbol); if (jCClassDecl == null && this.outermostMemberDef != null) { this.classMap.scan(this.outermostMemberDef); jCClassDecl = this.classdefs.get(paramClassSymbol); }  if (jCClassDecl == null) { this.classMap.scan((JCTree)this.outermostClassDef); jCClassDecl = this.classdefs.get(paramClassSymbol); }  return jCClassDecl; }
/*      */   abstract class BasicFreeVarCollector extends TreeScanner {
/*      */     abstract void addFreeVars(Symbol.ClassSymbol param1ClassSymbol);
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) { visitSymbol(param1JCIdent.sym); }
/*      */     abstract void visitSymbol(Symbol param1Symbol); public void visitNewClass(JCTree.JCNewClass param1JCNewClass) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1JCNewClass.constructor.owner; addFreeVars(classSymbol); super.visitNewClass(param1JCNewClass); } public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) { if (TreeInfo.name((JCTree)param1JCMethodInvocation.meth) == Lower.this.names._super) addFreeVars((Symbol.ClassSymbol)(TreeInfo.symbol((JCTree)param1JCMethodInvocation.meth)).owner);  super.visitApply(param1JCMethodInvocation); } } class FreeVarCollector extends BasicFreeVarCollector {
/*  751 */     Symbol owner; Symbol.ClassSymbol clazz; List<Symbol.VarSymbol> fvs; FreeVarCollector(Symbol.ClassSymbol param1ClassSymbol) { this.clazz = param1ClassSymbol; this.owner = param1ClassSymbol.owner; this.fvs = List.nil(); } private void addFreeVar(Symbol.VarSymbol param1VarSymbol) { for (List<Symbol.VarSymbol> list = this.fvs; list.nonEmpty(); list = list.tail) { if (list.head == param1VarSymbol) return;  }  this.fvs = this.fvs.prepend(param1VarSymbol); } void addFreeVars(Symbol.ClassSymbol param1ClassSymbol) { List list = Lower.this.freevarCache.get(param1ClassSymbol); if (list != null) for (List list1 = list; list1.nonEmpty(); list1 = list1.tail) addFreeVar((Symbol.VarSymbol)list1.head);   } void visitSymbol(Symbol param1Symbol) { Symbol symbol = param1Symbol; if (symbol.kind == 4 || symbol.kind == 16) { while (symbol != null && symbol.owner != this.owner) symbol = (Lower.this.proxies.lookup(Lower.this.proxyName(symbol.name))).sym;  if (symbol != null && symbol.owner == this.owner) { Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)symbol; if (varSymbol.getConstValue() == null) addFreeVar(varSymbol);  } else if (Lower.this.outerThisStack.head != null && Lower.this.outerThisStack.head != param1Symbol) { visitSymbol((Symbol)Lower.this.outerThisStack.head); }  }  } public void visitNewClass(JCTree.JCNewClass param1JCNewClass) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1JCNewClass.constructor.owner; if (param1JCNewClass.encl == null && classSymbol.hasOuterInstance() && Lower.this.outerThisStack.head != null) visitSymbol((Symbol)Lower.this.outerThisStack.head);  super.visitNewClass(param1JCNewClass); } public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) { if ((param1JCFieldAccess.name == Lower.this.names._this || param1JCFieldAccess.name == Lower.this.names._super) && param1JCFieldAccess.selected.type.tsym != this.clazz && Lower.this.outerThisStack.head != null) visitSymbol((Symbol)Lower.this.outerThisStack.head);  super.visitSelect(param1JCFieldAccess); } public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) { if (TreeInfo.name((JCTree)param1JCMethodInvocation.meth) == Lower.this.names._super) { Symbol symbol = TreeInfo.symbol((JCTree)param1JCMethodInvocation.meth); Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)symbol.owner; if (classSymbol.hasOuterInstance() && !param1JCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT) && Lower.this.outerThisStack.head != null) visitSymbol((Symbol)Lower.this.outerThisStack.head);  }  super.visitApply(param1JCMethodInvocation); } } Symbol.ClassSymbol ownerToCopyFreeVarsFrom(Symbol.ClassSymbol paramClassSymbol) { if (!paramClassSymbol.isLocal()) return null;  Symbol symbol = paramClassSymbol.owner; while ((symbol.owner.kind & 0x2) != 0 && symbol.isLocal()) symbol = symbol.owner;  if ((symbol.owner.kind & 0x14) != 0 && paramClassSymbol.isSubClass(symbol, this.types)) return (Symbol.ClassSymbol)symbol;  return null; } private Symbol lookupSynthetic(Name paramName, Scope paramScope) { Symbol symbol = (paramScope.lookup(paramName)).sym;
/*  752 */     return (symbol == null || (symbol.flags() & 0x1000L) == 0L) ? null : symbol; }
/*      */   List<Symbol.VarSymbol> freevars(Symbol.ClassSymbol paramClassSymbol) { List<Symbol.VarSymbol> list = this.freevarCache.get(paramClassSymbol); if (list != null) return list;  if ((paramClassSymbol.owner.kind & 0x14) != 0) { FreeVarCollector freeVarCollector = new FreeVarCollector(paramClassSymbol); freeVarCollector.scan((JCTree)classDef(paramClassSymbol)); list = freeVarCollector.fvs; this.freevarCache.put(paramClassSymbol, list); return list; }  Symbol.ClassSymbol classSymbol = ownerToCopyFreeVarsFrom(paramClassSymbol); if (classSymbol != null) { list = this.freevarCache.get(classSymbol); this.freevarCache.put(paramClassSymbol, list); return list; }  return List.nil(); }
/*      */   EnumMapping mapForEnum(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) { EnumMapping enumMapping = this.enumSwitchMap.get(paramTypeSymbol); if (enumMapping == null) this.enumSwitchMap.put(paramTypeSymbol, enumMapping = new EnumMapping(paramDiagnosticPosition, paramTypeSymbol));  return enumMapping; }
/*      */   class EnumMapping {
/*      */     JCDiagnostic.DiagnosticPosition pos;
/*      */     int next;
/*  758 */     final Symbol.TypeSymbol forEnum; final Symbol.VarSymbol mapVar; final Map<Symbol.VarSymbol, Integer> values; EnumMapping(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol.TypeSymbol param1TypeSymbol) { this.pos = null; this.next = 1; this.forEnum = param1TypeSymbol; this.values = new LinkedHashMap<>(); this.pos = param1DiagnosticPosition; Name name = Lower.this.names.fromString(Lower.this.target.syntheticNameChar() + "SwitchMap" + Lower.this.target.syntheticNameChar() + Lower.this.writer.xClassName(param1TypeSymbol.type).toString().replace('/', '.').replace('.', Lower.this.target.syntheticNameChar())); Symbol.ClassSymbol classSymbol = Lower.this.outerCacheClass(); this.mapVar = new Symbol.VarSymbol(4120L, name, (Type)new Type.ArrayType((Type)Lower.this.syms.intType, (Symbol.TypeSymbol)Lower.this.syms.arrayClass), (Symbol)classSymbol); Lower.this.enterSynthetic(param1DiagnosticPosition, (Symbol)this.mapVar, classSymbol.members()); } JCTree.JCLiteral forConstant(Symbol.VarSymbol param1VarSymbol) { Integer integer = this.values.get(param1VarSymbol); if (integer == null) this.values.put(param1VarSymbol, integer = Integer.valueOf(this.next++));  return Lower.this.make.Literal(integer); } void translate() { Lower.this.make.at(this.pos.getStartPosition()); JCTree.JCClassDecl jCClassDecl = Lower.this.classDef((Symbol.ClassSymbol)this.mapVar.owner); Symbol.MethodSymbol methodSymbol1 = Lower.this.lookupMethod(this.pos, Lower.this.names.values, this.forEnum.type, List.nil()); JCTree.JCExpression jCExpression1 = Lower.this.make.Select((JCTree.JCExpression)Lower.this.make.App(Lower.this.make.QualIdent((Symbol)methodSymbol1)), (Symbol)Lower.this.syms.lengthVar); JCTree.JCExpression jCExpression2 = Lower.this.make.NewArray(Lower.this.make.Type((Type)Lower.this.syms.intType), List.of(jCExpression1), null).setType((Type)new Type.ArrayType((Type)Lower.this.syms.intType, (Symbol.TypeSymbol)Lower.this.syms.arrayClass)); ListBuffer listBuffer = new ListBuffer(); Symbol.MethodSymbol methodSymbol2 = Lower.this.lookupMethod(this.pos, Lower.this.names.ordinal, this.forEnum.type, List.nil()); List list = List.nil().prepend(Lower.this.make.Catch(Lower.this.make.VarDef(new Symbol.VarSymbol(8589934592L, Lower.this.names.ex, Lower.this.syms.noSuchFieldErrorType, (Symbol)Lower.this.syms.noSymbol), null), Lower.this.make.Block(0L, List.nil()))); for (Map.Entry<Symbol.VarSymbol, Integer> entry : this.values.entrySet()) { Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)entry.getKey(); Integer integer = (Integer)entry.getValue(); JCTree.JCExpression jCExpression = Lower.this.make.Assign((JCTree.JCExpression)Lower.this.make.Indexed((Symbol)this.mapVar, (JCTree.JCExpression)Lower.this.make.App(Lower.this.make.Select(Lower.this.make.QualIdent((Symbol)varSymbol), (Symbol)methodSymbol2))), (JCTree.JCExpression)Lower.this.make.Literal(integer)).setType((Type)Lower.this.syms.intType); JCTree.JCExpressionStatement jCExpressionStatement = Lower.this.make.Exec(jCExpression); JCTree.JCTry jCTry = Lower.this.make.Try(Lower.this.make.Block(0L, List.of(jCExpressionStatement)), list, null); listBuffer.append(jCTry); }  jCClassDecl.defs = jCClassDecl.defs.prepend(Lower.this.make.Block(8L, listBuffer.toList())).prepend(Lower.this.make.VarDef(this.mapVar, jCExpression2)); } } TreeMaker make_at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) { this.make_pos = paramDiagnosticPosition; return this.make.at(paramDiagnosticPosition); } JCTree.JCExpression makeLit(Type paramType, Object paramObject) { return (JCTree.JCExpression)this.make.Literal(paramType.getTag(), paramObject).setType(paramType.constType(paramObject)); } JCTree.JCExpression makeNull() { return makeLit(this.syms.botType, (Object)null); } JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList) { JCTree.JCNewClass jCNewClass = this.make.NewClass(null, null, this.make.QualIdent((Symbol)paramType.tsym), paramList, null); jCNewClass.constructor = this.rs.resolveConstructor(this.make_pos, this.attrEnv, paramType, TreeInfo.types(paramList), List.nil()); jCNewClass.type = paramType; return jCNewClass; } JCTree.JCUnary makeUnary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression) { JCTree.JCUnary jCUnary = this.make.Unary(paramTag, paramJCExpression); jCUnary.operator = this.rs.resolveUnaryOperator(this.make_pos, paramTag, this.attrEnv, paramJCExpression.type); jCUnary.type = jCUnary.operator.type.getReturnType(); return jCUnary; } JCTree.JCBinary makeBinary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) { JCTree.JCBinary jCBinary = this.make.Binary(paramTag, paramJCExpression1, paramJCExpression2); jCBinary.operator = this.rs.resolveBinaryOperator(this.make_pos, paramTag, this.attrEnv, paramJCExpression1.type, paramJCExpression2.type); jCBinary.type = jCBinary.operator.type.getReturnType(); return jCBinary; } JCTree.JCAssignOp makeAssignop(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2) { JCTree.JCAssignOp jCAssignOp = this.make.Assignop(paramTag, paramJCTree1, paramJCTree2); jCAssignOp.operator = this.rs.resolveBinaryOperator(this.make_pos, jCAssignOp.getTag().noAssignOp(), this.attrEnv, paramJCTree1.type, paramJCTree2.type); jCAssignOp.type = paramJCTree1.type; return jCAssignOp; } JCTree.JCExpression makeString(JCTree.JCExpression paramJCExpression) { if (!paramJCExpression.type.isPrimitiveOrVoid()) return paramJCExpression;  Symbol.MethodSymbol methodSymbol = lookupMethod(paramJCExpression.pos(), this.names.valueOf, this.syms.stringType, List.of(paramJCExpression.type)); return (JCTree.JCExpression)this.make.App(this.make.QualIdent((Symbol)methodSymbol), List.of(paramJCExpression)); } JCTree.JCClassDecl makeEmptyClass(long paramLong, Symbol.ClassSymbol paramClassSymbol) { return makeEmptyClass(paramLong, paramClassSymbol, (Name)null, true); } JCTree.JCClassDecl makeEmptyClass(long paramLong, Symbol.ClassSymbol paramClassSymbol, Name paramName, boolean paramBoolean) { Symbol.ClassSymbol classSymbol = this.reader.defineClass(this.names.empty, (Symbol)paramClassSymbol); if (paramName != null) { classSymbol.flatname = paramName; } else { classSymbol.flatname = this.chk.localClassName(classSymbol); }  classSymbol.sourcefile = paramClassSymbol.sourcefile; classSymbol.completer = null; classSymbol.members_field = new Scope((Symbol)classSymbol); classSymbol.flags_field = paramLong; Type.ClassType classType = (Type.ClassType)classSymbol.type; classType.supertype_field = this.syms.objectType; classType.interfaces_field = List.nil(); JCTree.JCClassDecl jCClassDecl1 = classDef(paramClassSymbol); enterSynthetic(jCClassDecl1.pos(), (Symbol)classSymbol, paramClassSymbol.members()); this.chk.compiled.put(classSymbol.flatname, classSymbol); JCTree.JCClassDecl jCClassDecl2 = this.make.ClassDef(this.make.Modifiers(paramLong), this.names.empty, List.nil(), null, List.nil(), List.nil()); jCClassDecl2.sym = classSymbol; jCClassDecl2.type = classSymbol.type; if (paramBoolean) jCClassDecl1.defs = jCClassDecl1.defs.prepend(jCClassDecl2);  return jCClassDecl2; } private void enterSynthetic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope) { paramScope.enter(paramSymbol); } private Name makeSyntheticName(Name paramName, Scope paramScope) { while (true) { paramName = paramName.append(this.target.syntheticNameChar(), this.names.empty); if (lookupSynthetic(paramName, paramScope) == null) return paramName;  }  } void checkConflicts(List<JCTree> paramList) { for (JCTree jCTree : paramList) jCTree.accept(this.conflictsChecker);  } private Symbol.MethodSymbol lookupMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Name paramName, Type paramType, List<Type> paramList) { return this.rs.resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName, paramList, List.nil()); }
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.MethodSymbol lookupConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type> paramList) {
/*  764 */     return this.rs.resolveInternalConstructor(paramDiagnosticPosition, this.attrEnv, paramType, paramList, null);
/*      */   }
/*      */
/*      */
/*      */
/*      */   private Symbol.VarSymbol lookupField(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName) {
/*  770 */     return this.rs.resolveInternalField(paramDiagnosticPosition, this.attrEnv, paramType, paramName);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void checkAccessConstructorTags() {
/*  781 */     for (List<Symbol.ClassSymbol> list = this.accessConstrTags; list.nonEmpty(); list = list.tail) {
/*  782 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)list.head;
/*  783 */       if (!isTranslatedClassAvailable(classSymbol)) {
/*      */
/*      */
/*  786 */         JCTree.JCClassDecl jCClassDecl = makeEmptyClass(4104L, classSymbol
/*  787 */             .outermostClass(), classSymbol.flatname, false);
/*  788 */         swapAccessConstructorTag(classSymbol, jCClassDecl.sym);
/*  789 */         this.translated.append(jCClassDecl);
/*      */       }
/*      */     }
/*      */   }
/*      */   private boolean isTranslatedClassAvailable(Symbol.ClassSymbol paramClassSymbol) {
/*  794 */     for (JCTree jCTree : this.translated) {
/*  795 */       if (jCTree.hasTag(JCTree.Tag.CLASSDEF) && ((JCTree.JCClassDecl)jCTree).sym == paramClassSymbol)
/*      */       {
/*  797 */         return true;
/*      */       }
/*      */     }
/*  800 */     return false;
/*      */   }
/*      */
/*      */   void swapAccessConstructorTag(Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2) {
/*  804 */     for (Symbol.MethodSymbol methodSymbol : this.accessConstrs.values()) {
/*  805 */       Assert.check(methodSymbol.type.hasTag(TypeTag.METHOD));
/*  806 */       Type.MethodType methodType = (Type.MethodType)methodSymbol.type;
/*      */
/*  808 */       if (((Type)methodType.argtypes.head).tsym == paramClassSymbol1) {
/*  809 */         methodSymbol
/*  810 */           .type = this.types.createMethodTypeWithParameters((Type)methodType,
/*  811 */             (methodType.getParameterTypes()).tail
/*  812 */             .prepend(paramClassSymbol2.erasure(this.types)));
/*      */       }
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
/*  845 */   private static final int NCODES = accessCode(275) + 2;
/*      */
/*      */
/*      */   private Map<Symbol, Integer> accessNums;
/*      */
/*      */   private Map<Symbol, Symbol.MethodSymbol[]> accessSyms;
/*      */
/*      */   private Map<Symbol, Symbol.MethodSymbol> accessConstrs;
/*      */
/*      */   private List<Symbol.ClassSymbol> accessConstrTags;
/*      */
/*      */   private ListBuffer<Symbol> accessed;
/*      */
/*      */   Scope proxies;
/*      */
/*      */   Scope twrVars;
/*      */
/*      */   List<Symbol.VarSymbol> outerThisStack;
/*      */
/*      */   private Symbol.ClassSymbol assertionsDisabledClassCache;
/*      */
/*      */   private JCTree.JCExpression enclOp;
/*      */
/*      */   private Symbol.MethodSymbol systemArraycopyMethod;
/*      */
/*      */
/*      */   private static int accessCode(int paramInt) {
/*  872 */     if (96 <= paramInt && paramInt <= 131)
/*  873 */       return (paramInt - 96) * 2 + 12;
/*  874 */     if (paramInt == 256)
/*  875 */       return 84;
/*  876 */     if (270 <= paramInt && paramInt <= 275) {
/*  877 */       return (paramInt - 270 + 131 + 2 - 96) * 2 + 12;
/*      */     }
/*  879 */     return -1;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static int accessCode(JCTree paramJCTree1, JCTree paramJCTree2) {
/*  888 */     if (paramJCTree2 == null)
/*  889 */       return 0;
/*  890 */     if (paramJCTree2.hasTag(JCTree.Tag.ASSIGN) && paramJCTree1 ==
/*  891 */       TreeInfo.skipParens(((JCTree.JCAssign)paramJCTree2).lhs))
/*  892 */       return 2;
/*  893 */     if (paramJCTree2.getTag().isIncOrDecUnaryOp() && paramJCTree1 ==
/*  894 */       TreeInfo.skipParens(((JCTree.JCUnary)paramJCTree2).arg))
/*  895 */       return mapTagToUnaryOpCode(paramJCTree2.getTag());
/*  896 */     if (paramJCTree2.getTag().isAssignop() && paramJCTree1 ==
/*  897 */       TreeInfo.skipParens(((JCTree.JCAssignOp)paramJCTree2).lhs)) {
/*  898 */       return accessCode(((Symbol.OperatorSymbol)((JCTree.JCAssignOp)paramJCTree2).operator).opcode);
/*      */     }
/*  900 */     return 0;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private Symbol.OperatorSymbol binaryAccessOperator(int paramInt) {
/*  906 */     Scope.Entry entry = (this.syms.predefClass.members()).elems;
/*  907 */     for (; entry != null;
/*  908 */       entry = entry.sibling) {
/*  909 */       if (entry.sym instanceof Symbol.OperatorSymbol) {
/*  910 */         Symbol.OperatorSymbol operatorSymbol = (Symbol.OperatorSymbol)entry.sym;
/*  911 */         if (accessCode(operatorSymbol.opcode) == paramInt) return operatorSymbol;
/*      */       }
/*      */     }
/*  914 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private static JCTree.Tag treeTag(Symbol.OperatorSymbol paramOperatorSymbol) {
/*  921 */     switch (paramOperatorSymbol.opcode) { case 128:
/*      */       case 129:
/*  923 */         return JCTree.Tag.BITOR_ASG;
/*      */       case 130: case 131:
/*  925 */         return JCTree.Tag.BITXOR_ASG;
/*      */       case 126: case 127:
/*  927 */         return JCTree.Tag.BITAND_ASG;
/*      */       case 120: case 121: case 270:
/*      */       case 271:
/*  930 */         return JCTree.Tag.SL_ASG;
/*      */       case 122: case 123: case 272:
/*      */       case 273:
/*  933 */         return JCTree.Tag.SR_ASG;
/*      */       case 124: case 125: case 274:
/*      */       case 275:
/*  936 */         return JCTree.Tag.USR_ASG;
/*      */       case 96: case 97: case 98:
/*      */       case 99:
/*      */       case 256:
/*  940 */         return JCTree.Tag.PLUS_ASG;
/*      */       case 100: case 101: case 102:
/*      */       case 103:
/*  943 */         return JCTree.Tag.MINUS_ASG;
/*      */       case 104: case 105: case 106:
/*      */       case 107:
/*  946 */         return JCTree.Tag.MUL_ASG;
/*      */       case 108: case 109: case 110:
/*      */       case 111:
/*  949 */         return JCTree.Tag.DIV_ASG;
/*      */       case 112: case 113: case 114:
/*      */       case 115:
/*  952 */         return JCTree.Tag.MOD_ASG; }
/*      */
/*  954 */     throw new AssertionError();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Name accessName(int paramInt1, int paramInt2) {
/*  961 */     return this.names.fromString("access" + this.target
/*  962 */         .syntheticNameChar() + paramInt1 + (paramInt2 / 10) + (paramInt2 % 10));
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
/*      */   Symbol.MethodSymbol accessSymbol(Symbol paramSymbol, JCTree paramJCTree1, JCTree paramJCTree2, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     int i;
/*      */     List list1;
/*      */     Type type;
/*      */     List list2;
/*  983 */     Symbol.ClassSymbol classSymbol = (paramBoolean2 && paramBoolean1) ? (Symbol.ClassSymbol)((JCTree.JCFieldAccess)paramJCTree1).selected.type.tsym : accessClass(paramSymbol, paramBoolean1, paramJCTree1);
/*      */
/*  985 */     Symbol symbol = paramSymbol;
/*  986 */     if (paramSymbol.owner != classSymbol) {
/*  987 */       symbol = paramSymbol.clone((Symbol)classSymbol);
/*  988 */       this.actualSymbols.put(symbol, paramSymbol);
/*      */     }
/*      */
/*      */
/*  992 */     Integer integer = this.accessNums.get(symbol);
/*  993 */     if (integer == null) {
/*  994 */       integer = Integer.valueOf(this.accessed.length());
/*  995 */       this.accessNums.put(symbol, integer);
/*  996 */       this.accessSyms.put(symbol, new Symbol.MethodSymbol[NCODES]);
/*  997 */       this.accessed.append(symbol);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1005 */     switch (symbol.kind) {
/*      */       case 4:
/* 1007 */         i = accessCode(paramJCTree1, paramJCTree2);
/* 1008 */         if (i >= 12) {
/* 1009 */           Symbol.OperatorSymbol operatorSymbol = binaryAccessOperator(i);
/* 1010 */           if (operatorSymbol.opcode == 256)
/* 1011 */           { List list = List.of(this.syms.objectType); }
/*      */           else
/* 1013 */           { List list = (operatorSymbol.type.getParameterTypes()).tail; }
/* 1014 */         } else if (i == 2) {
/* 1015 */           List list = List.of(symbol.erasure(this.types));
/*      */         } else {
/* 1017 */           List list = List.nil();
/* 1018 */         }  type = symbol.erasure(this.types);
/* 1019 */         list2 = List.nil();
/*      */         break;
/*      */       case 16:
/* 1022 */         i = 0;
/* 1023 */         list1 = symbol.erasure(this.types).getParameterTypes();
/* 1024 */         type = symbol.erasure(this.types).getReturnType();
/* 1025 */         list2 = symbol.type.getThrownTypes();
/*      */         break;
/*      */       default:
/* 1028 */         throw new AssertionError();
/*      */     }
/*      */
/*      */
/*      */
/* 1033 */     if (paramBoolean1 && paramBoolean2) i++;
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1039 */     if ((symbol.flags() & 0x8L) == 0L) {
/* 1040 */       list1 = list1.prepend(symbol.owner.erasure(this.types));
/*      */     }
/* 1042 */     Symbol.MethodSymbol[] arrayOfMethodSymbol = this.accessSyms.get(symbol);
/* 1043 */     Symbol.MethodSymbol methodSymbol = arrayOfMethodSymbol[i];
/* 1044 */     if (methodSymbol == null) {
/*      */
/*      */
/* 1047 */       methodSymbol = new Symbol.MethodSymbol(4104L, accessName(integer.intValue(), i), (Type)new Type.MethodType(list1, type, list2, (Symbol.TypeSymbol)this.syms.methodClass), (Symbol)classSymbol);
/*      */
/*      */
/* 1050 */       enterSynthetic(paramJCTree1.pos(), (Symbol)methodSymbol, classSymbol.members());
/* 1051 */       arrayOfMethodSymbol[i] = methodSymbol;
/*      */     }
/* 1053 */     return methodSymbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression accessBase(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 1062 */     return ((paramSymbol.flags() & 0x8L) != 0L) ?
/* 1063 */       access(this.make.at(paramDiagnosticPosition.getStartPosition()).QualIdent(paramSymbol.owner)) :
/* 1064 */       makeOwnerThis(paramDiagnosticPosition, paramSymbol, true);
/*      */   }
/*      */
/*      */
/*      */
/*      */   boolean needsPrivateAccess(Symbol paramSymbol) {
/* 1070 */     if ((paramSymbol.flags() & 0x2L) == 0L || paramSymbol.owner == this.currentClass)
/* 1071 */       return false;
/* 1072 */     if (paramSymbol.name == this.names.init && paramSymbol.owner.isLocal()) {
/*      */
/* 1074 */       paramSymbol.flags_field &= 0xFFFFFFFFFFFFFFFDL;
/* 1075 */       return false;
/*      */     }
/* 1077 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   boolean needsProtectedAccess(Symbol paramSymbol, JCTree paramJCTree) {
/* 1084 */     if ((paramSymbol.flags() & 0x4L) == 0L || paramSymbol.owner.owner == this.currentClass.owner || paramSymbol
/*      */
/* 1086 */       .packge() == this.currentClass.packge())
/* 1087 */       return false;
/* 1088 */     if (!this.currentClass.isSubClass(paramSymbol.owner, this.types))
/* 1089 */       return true;
/* 1090 */     if ((paramSymbol.flags() & 0x8L) != 0L ||
/* 1091 */       !paramJCTree.hasTag(JCTree.Tag.SELECT) ||
/* 1092 */       TreeInfo.name((JCTree)((JCTree.JCFieldAccess)paramJCTree).selected) == this.names._super)
/* 1093 */       return false;
/* 1094 */     return !((JCTree.JCFieldAccess)paramJCTree).selected.type.tsym.isSubClass((Symbol)this.currentClass, this.types);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Symbol.ClassSymbol accessClass(Symbol paramSymbol, boolean paramBoolean, JCTree paramJCTree) {
/* 1103 */     if (paramBoolean) {
/* 1104 */       Symbol.TypeSymbol typeSymbol = null;
/* 1105 */       Symbol.ClassSymbol classSymbol = this.currentClass;
/* 1106 */       if (paramJCTree.hasTag(JCTree.Tag.SELECT) && (paramSymbol.flags() & 0x8L) == 0L) {
/* 1107 */         typeSymbol = ((JCTree.JCFieldAccess)paramJCTree).selected.type.tsym;
/* 1108 */         while (!typeSymbol.isSubClass((Symbol)classSymbol, this.types)) {
/* 1109 */           classSymbol = classSymbol.owner.enclClass();
/*      */         }
/* 1111 */         return classSymbol;
/*      */       }
/* 1113 */       while (!classSymbol.isSubClass(paramSymbol.owner, this.types)) {
/* 1114 */         classSymbol = classSymbol.owner.enclClass();
/*      */       }
/*      */
/* 1117 */       return classSymbol;
/*      */     }
/*      */
/* 1120 */     return paramSymbol.owner.enclClass();
/*      */   }
/*      */
/*      */
/*      */   private void addPrunedInfo(JCTree paramJCTree) {
/* 1125 */     List<JCTree> list = this.prunedTree.get(this.currentClass);
/* 1126 */     list = (list == null) ? List.of(paramJCTree) : list.prepend(paramJCTree);
/* 1127 */     this.prunedTree.put(this.currentClass, list);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression access(Symbol paramSymbol, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, boolean paramBoolean) {
/*      */     JCTree.JCIdent jCIdent;
/* 1139 */     while (paramSymbol.kind == 4 && paramSymbol.owner.kind == 16 && paramSymbol.owner
/* 1140 */       .enclClass() != this.currentClass) {
/*      */
/* 1142 */       Object object = ((Symbol.VarSymbol)paramSymbol).getConstValue();
/* 1143 */       if (object != null) {
/* 1144 */         this.make.at(paramJCExpression1.pos);
/* 1145 */         return makeLit(paramSymbol.type, object);
/*      */       }
/*      */
/* 1148 */       paramSymbol = (this.proxies.lookup(proxyName(paramSymbol.name))).sym;
/* 1149 */       Assert.check((paramSymbol != null && (paramSymbol.flags_field & 0x10L) != 0L));
/* 1150 */       jCIdent = this.make.at(paramJCExpression1.pos).Ident(paramSymbol);
/*      */     }
/* 1152 */     JCTree.JCExpression jCExpression = jCIdent.hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)jCIdent).selected : null;
/* 1153 */     switch (paramSymbol.kind) {
/*      */       case 2:
/* 1155 */         if (paramSymbol.owner.kind != 1) {
/*      */
/*      */
/* 1158 */           Name name = Convert.shortName(paramSymbol.flatName());
/* 1159 */           while (jCExpression != null &&
/* 1160 */             TreeInfo.symbol((JCTree)jCExpression) != null &&
/* 1161 */             (TreeInfo.symbol((JCTree)jCExpression)).kind != 1) {
/* 1162 */             jCExpression = jCExpression.hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)jCExpression).selected : null;
/*      */           }
/*      */
/*      */
/* 1166 */           if (jCIdent.hasTag(JCTree.Tag.IDENT)) {
/* 1167 */             jCIdent.name = name; break;
/* 1168 */           }  if (jCExpression == null) {
/* 1169 */             jCIdent = this.make.at(((JCTree.JCExpression)jCIdent).pos).Ident(paramSymbol);
/* 1170 */             jCIdent.name = name; break;
/*      */           }
/* 1172 */           ((JCTree.JCFieldAccess)jCIdent).selected = jCExpression;
/* 1173 */           ((JCTree.JCFieldAccess)jCIdent).name = name;
/*      */         }
/*      */         break;
/*      */       case 4:
/*      */       case 16:
/* 1178 */         if (paramSymbol.owner.kind == 2) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1186 */           boolean bool1 = ((paramBoolean && !needsPrivateAccess(paramSymbol)) || needsProtectedAccess(paramSymbol, (JCTree)jCIdent)) ? true : false;
/* 1187 */           boolean bool2 = (bool1 || needsPrivateAccess(paramSymbol)) ? true : false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1194 */           boolean bool3 = (jCExpression == null && paramSymbol.owner != this.syms.predefClass && !paramSymbol.isMemberOf((Symbol.TypeSymbol)this.currentClass, this.types)) ? true : false;
/*      */
/* 1196 */           if (bool2 || bool3) {
/* 1197 */             this.make.at(((JCTree.JCExpression)jCIdent).pos);
/*      */
/*      */
/* 1200 */             if (paramSymbol.kind == 4) {
/* 1201 */               Object object = ((Symbol.VarSymbol)paramSymbol).getConstValue();
/* 1202 */               if (object != null) {
/* 1203 */                 addPrunedInfo((JCTree)jCIdent);
/* 1204 */                 return makeLit(paramSymbol.type, object);
/*      */               }
/*      */             }
/*      */
/*      */
/*      */
/* 1210 */             if (bool2) {
/* 1211 */               List list = List.nil();
/* 1212 */               if ((paramSymbol.flags() & 0x8L) == 0L) {
/*      */
/*      */
/* 1215 */                 if (jCExpression == null)
/* 1216 */                   jCExpression = makeOwnerThis(jCIdent.pos(), paramSymbol, true);
/* 1217 */                 list = list.prepend(jCExpression);
/* 1218 */                 jCExpression = null;
/*      */               }
/* 1220 */               Symbol.MethodSymbol methodSymbol = accessSymbol(paramSymbol, (JCTree)jCIdent, (JCTree)paramJCExpression2, bool1, paramBoolean);
/*      */
/*      */
/* 1223 */               JCTree.JCExpression jCExpression1 = this.make.Select((jCExpression != null) ? jCExpression : this.make
/* 1224 */                   .QualIdent(((Symbol)methodSymbol).owner), (Symbol)methodSymbol);
/*      */
/* 1226 */               return (JCTree.JCExpression)this.make.App(jCExpression1, list);
/*      */             }
/*      */
/*      */
/* 1230 */             if (bool3)
/* 1231 */               return this.make.at(((JCTree.JCExpression)jCIdent).pos).Select(
/* 1232 */                   accessBase(jCIdent.pos(), paramSymbol), paramSymbol).setType(((JCTree.JCExpression)jCIdent).type);
/*      */           }  break;
/*      */         }
/* 1235 */         if (paramSymbol.owner.kind == 16 && this.lambdaTranslationMap != null) {
/*      */
/*      */
/*      */
/* 1239 */           Symbol symbol = this.lambdaTranslationMap.get(paramSymbol);
/* 1240 */           if (symbol != null)
/* 1241 */             jCIdent = this.make.at(((JCTree.JCExpression)jCIdent).pos).Ident(symbol);
/*      */         }
/*      */         break;
/*      */     }
/* 1245 */     return (JCTree.JCExpression)jCIdent;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression access(JCTree.JCExpression paramJCExpression) {
/* 1252 */     Symbol symbol = TreeInfo.symbol((JCTree)paramJCExpression);
/* 1253 */     return (symbol == null) ? paramJCExpression : access(symbol, paramJCExpression, (JCTree.JCExpression)null, false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Symbol accessConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 1262 */     if (needsPrivateAccess(paramSymbol)) {
/* 1263 */       Symbol.ClassSymbol classSymbol = paramSymbol.owner.enclClass();
/* 1264 */       Symbol.MethodSymbol methodSymbol = this.accessConstrs.get(paramSymbol);
/* 1265 */       if (methodSymbol == null) {
/* 1266 */         List list = paramSymbol.type.getParameterTypes();
/* 1267 */         if ((classSymbol.flags_field & 0x4000L) != 0L)
/*      */         {
/*      */
/* 1270 */           list = list.prepend(this.syms.intType).prepend(this.syms.stringType);
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1278 */         methodSymbol = new Symbol.MethodSymbol(4096L, this.names.init, (Type)new Type.MethodType(list.append(accessConstructorTag().erasure(this.types)), paramSymbol.type.getReturnType(), paramSymbol.type.getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass), (Symbol)classSymbol);
/*      */
/*      */
/* 1281 */         enterSynthetic(paramDiagnosticPosition, (Symbol)methodSymbol, classSymbol.members());
/* 1282 */         this.accessConstrs.put(paramSymbol, methodSymbol);
/* 1283 */         this.accessed.append(paramSymbol);
/*      */       }
/* 1285 */       return (Symbol)methodSymbol;
/*      */     }
/* 1287 */     return paramSymbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Symbol.ClassSymbol accessConstructorTag() {
/* 1294 */     Symbol.ClassSymbol classSymbol1 = this.currentClass.outermostClass();
/* 1295 */     Name name = this.names.fromString("" + classSymbol1.getQualifiedName() + this.target
/* 1296 */         .syntheticNameChar() + "1");
/*      */
/* 1298 */     Symbol.ClassSymbol classSymbol2 = this.chk.compiled.get(name);
/* 1299 */     if (classSymbol2 == null) {
/* 1300 */       classSymbol2 = (makeEmptyClass(4104L, classSymbol1)).sym;
/*      */     }
/* 1302 */     this.accessConstrTags = this.accessConstrTags.prepend(classSymbol2);
/* 1303 */     return classSymbol2;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void makeAccessible(Symbol paramSymbol) {
/* 1310 */     JCTree.JCClassDecl jCClassDecl = classDef(paramSymbol.owner.enclClass());
/* 1311 */     if (jCClassDecl == null) Assert.error("class def not found: " + paramSymbol + " in " + paramSymbol.owner);
/* 1312 */     if (paramSymbol.name == this.names.init) {
/* 1313 */       jCClassDecl.defs = jCClassDecl.defs.prepend(
/* 1314 */           accessConstructorDef(jCClassDecl.pos, paramSymbol, this.accessConstrs.get(paramSymbol)));
/*      */     } else {
/* 1316 */       Symbol.MethodSymbol[] arrayOfMethodSymbol = this.accessSyms.get(paramSymbol);
/* 1317 */       for (byte b = 0; b < NCODES; b++) {
/* 1318 */         if (arrayOfMethodSymbol[b] != null) {
/* 1319 */           jCClassDecl.defs = jCClassDecl.defs.prepend(
/* 1320 */               accessDef(jCClassDecl.pos, paramSymbol, arrayOfMethodSymbol[b], b));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private static JCTree.Tag mapUnaryOpCodeToTag(int paramInt) {
/* 1329 */     switch (paramInt) {
/*      */       case 4:
/* 1331 */         return JCTree.Tag.PREINC;
/*      */       case 6:
/* 1333 */         return JCTree.Tag.PREDEC;
/*      */       case 8:
/* 1335 */         return JCTree.Tag.POSTINC;
/*      */       case 10:
/* 1337 */         return JCTree.Tag.POSTDEC;
/*      */     }
/* 1339 */     return JCTree.Tag.NO_TAG;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static int mapTagToUnaryOpCode(JCTree.Tag paramTag) {
/* 1347 */     switch (paramTag) {
/*      */       case ALWAYS:
/* 1349 */         return 4;
/*      */       case LEGACY:
/* 1351 */         return 6;
/*      */       case NONEMPTY:
/* 1353 */         return 8;
/*      */       case null:
/* 1355 */         return 10;
/*      */     }
/* 1357 */     return -1;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree accessDef(int paramInt1, Symbol paramSymbol, Symbol.MethodSymbol paramMethodSymbol, int paramInt2) {
/*      */     JCTree.JCExpression jCExpression;
/*      */     List list1;
/*      */     JCTree.JCStatement jCStatement;
/* 1369 */     this.currentClass = paramSymbol.owner.enclClass();
/* 1370 */     this.make.at(paramInt1);
/* 1371 */     JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(paramMethodSymbol, null);
/*      */
/*      */
/* 1374 */     Symbol symbol = this.actualSymbols.get(paramSymbol);
/* 1375 */     if (symbol == null) symbol = paramSymbol;
/*      */
/*      */
/*      */
/* 1379 */     if ((symbol.flags() & 0x8L) != 0L) {
/* 1380 */       JCTree.JCIdent jCIdent = this.make.Ident(symbol);
/* 1381 */       list1 = this.make.Idents(jCMethodDecl.params);
/*      */     } else {
/* 1383 */       JCTree.JCExpression jCExpression1 = this.make.Ident((JCTree.JCVariableDecl)jCMethodDecl.params.head);
/* 1384 */       if (paramInt2 % 2 != 0)
/*      */       {
/*      */
/*      */
/* 1388 */         jCExpression1.setType(this.types.erasure(this.types.supertype((paramSymbol.owner.enclClass()).type)));
/*      */       }
/* 1390 */       jCExpression = this.make.Select(jCExpression1, symbol);
/* 1391 */       list1 = this.make.Idents(jCMethodDecl.params.tail);
/*      */     }
/*      */
/* 1394 */     if (symbol.kind == 4) {
/*      */       JCTree.JCExpression jCExpression1; JCTree.JCAssign jCAssign; JCTree.JCUnary jCUnary; JCTree.JCAssignOp jCAssignOp;
/* 1396 */       int i = paramInt2 - (paramInt2 & 0x1);
/*      */
/*      */
/* 1399 */       switch (i) {
/*      */         case 0:
/* 1401 */           jCExpression1 = jCExpression;
/*      */           break;
/*      */         case 2:
/* 1404 */           jCAssign = this.make.Assign(jCExpression, (JCTree.JCExpression)list1.head); break;
/*      */         case 4: case 6: case 8:
/*      */         case 10:
/* 1407 */           jCUnary = makeUnary(mapUnaryOpCodeToTag(i), jCExpression);
/*      */           break;
/*      */         default:
/* 1410 */           jCAssignOp = this.make.Assignop(
/* 1411 */               treeTag(binaryAccessOperator(i)), (JCTree)jCExpression, (JCTree)list1.head);
/* 1412 */           jCAssignOp.operator = (Symbol)binaryAccessOperator(i); break;
/*      */       }
/* 1414 */       JCTree.JCReturn jCReturn = this.make.Return(jCAssignOp.setType(symbol.type));
/*      */     } else {
/* 1416 */       jCStatement = this.make.Call((JCTree.JCExpression)this.make.App(jCExpression, list1));
/*      */     }
/* 1418 */     jCMethodDecl.body = this.make.Block(0L, List.of(jCStatement));
/*      */
/*      */     List list2;
/*      */
/* 1422 */     for (list2 = jCMethodDecl.params; list2.nonEmpty(); list2 = list2.tail)
/* 1423 */       ((JCTree.JCVariableDecl)list2.head).vartype = access(((JCTree.JCVariableDecl)list2.head).vartype);
/* 1424 */     jCMethodDecl.restype = access(jCMethodDecl.restype);
/* 1425 */     for (list2 = jCMethodDecl.thrown; list2.nonEmpty(); list2 = list2.tail) {
/* 1426 */       list2.head = access((JCTree.JCExpression)list2.head);
/*      */     }
/* 1428 */     return (JCTree)jCMethodDecl;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree accessConstructorDef(int paramInt, Symbol paramSymbol, Symbol.MethodSymbol paramMethodSymbol) {
/* 1437 */     this.make.at(paramInt);
/* 1438 */     JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(paramMethodSymbol, paramMethodSymbol
/* 1439 */         .externalType(this.types), null);
/*      */
/* 1441 */     JCTree.JCIdent jCIdent = this.make.Ident(this.names._this);
/* 1442 */     jCIdent.sym = paramSymbol;
/* 1443 */     jCIdent.type = paramSymbol.type;
/* 1444 */     jCMethodDecl
/* 1445 */       .body = this.make.Block(0L, List.of(this.make
/* 1446 */           .Call((JCTree.JCExpression)this.make
/* 1447 */             .App((JCTree.JCExpression)jCIdent, this.make
/*      */
/* 1449 */               .Idents((jCMethodDecl.params.reverse()).tail.reverse())))));
/* 1450 */     return (JCTree)jCMethodDecl;
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
/*      */   Name proxyName(Name paramName) {
/* 1482 */     return this.names.fromString("val" + this.target.syntheticNameChar() + paramName);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   List<JCTree.JCVariableDecl> freevarDefs(int paramInt, List<Symbol.VarSymbol> paramList, Symbol paramSymbol) {
/* 1491 */     return freevarDefs(paramInt, paramList, paramSymbol, 0L);
/*      */   }
/*      */
/*      */
/*      */   List<JCTree.JCVariableDecl> freevarDefs(int paramInt, List<Symbol.VarSymbol> paramList, Symbol paramSymbol, long paramLong) {
/* 1496 */     long l = 0x1010L | paramLong;
/* 1497 */     if (paramSymbol.kind == 2 && this.target
/* 1498 */       .usePrivateSyntheticFields())
/* 1499 */       l |= 0x2L;
/* 1500 */     List<JCTree.JCVariableDecl> list = List.nil();
/* 1501 */     for (List<Symbol.VarSymbol> list1 = paramList; list1.nonEmpty(); list1 = list1.tail) {
/* 1502 */       Symbol.VarSymbol varSymbol1 = (Symbol.VarSymbol)list1.head;
/*      */
/* 1504 */       Symbol.VarSymbol varSymbol2 = new Symbol.VarSymbol(l, proxyName(varSymbol1.name), varSymbol1.erasure(this.types), paramSymbol);
/* 1505 */       this.proxies.enter((Symbol)varSymbol2);
/* 1506 */       JCTree.JCVariableDecl jCVariableDecl = this.make.at(paramInt).VarDef(varSymbol2, null);
/* 1507 */       jCVariableDecl.vartype = access(jCVariableDecl.vartype);
/* 1508 */       list = list.prepend(jCVariableDecl);
/*      */     }
/* 1510 */     return list;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Name outerThisName(Type paramType, Symbol paramSymbol) {
/* 1517 */     Type type = paramType.getEnclosingType();
/* 1518 */     byte b = 0;
/* 1519 */     while (type.hasTag(TypeTag.CLASS)) {
/* 1520 */       type = type.getEnclosingType();
/* 1521 */       b++;
/*      */     }
/* 1523 */     Name name = this.names.fromString("this" + this.target.syntheticNameChar() + b);
/* 1524 */     while (paramSymbol.kind == 2 && (((Symbol.ClassSymbol)paramSymbol).members().lookup(name)).scope != null)
/* 1525 */       name = this.names.fromString(name.toString() + this.target.syntheticNameChar());
/* 1526 */     return name;
/*      */   }
/*      */
/*      */   private Symbol.VarSymbol makeOuterThisVarSymbol(Symbol paramSymbol, long paramLong) {
/* 1530 */     if (paramSymbol.kind == 2 && this.target
/* 1531 */       .usePrivateSyntheticFields())
/* 1532 */       paramLong |= 0x2L;
/* 1533 */     Type type = this.types.erasure((paramSymbol.enclClass()).type.getEnclosingType());
/*      */
/* 1535 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(paramLong, outerThisName(type, paramSymbol), type, paramSymbol);
/* 1536 */     this.outerThisStack = this.outerThisStack.prepend(varSymbol);
/* 1537 */     return varSymbol;
/*      */   }
/*      */
/*      */   private JCTree.JCVariableDecl makeOuterThisVarDecl(int paramInt, Symbol.VarSymbol paramVarSymbol) {
/* 1541 */     JCTree.JCVariableDecl jCVariableDecl = this.make.at(paramInt).VarDef(paramVarSymbol, null);
/* 1542 */     jCVariableDecl.vartype = access(jCVariableDecl.vartype);
/* 1543 */     return jCVariableDecl;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCVariableDecl outerThisDef(int paramInt, Symbol.MethodSymbol paramMethodSymbol) {
/* 1551 */     Symbol.ClassSymbol classSymbol = paramMethodSymbol.enclClass();
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1557 */     boolean bool = ((paramMethodSymbol.isConstructor() && paramMethodSymbol.isAnonymous()) || (paramMethodSymbol.isConstructor() && classSymbol.isInner() && !classSymbol.isPrivate() && !classSymbol.isStatic())) ? true : false;
/* 1558 */     long l = (0x10 | (bool ? 32768 : 4096)) | 0x200000000L;
/*      */
/* 1560 */     Symbol.VarSymbol varSymbol = makeOuterThisVarSymbol((Symbol)paramMethodSymbol, l);
/* 1561 */     paramMethodSymbol.extraParams = paramMethodSymbol.extraParams.prepend(varSymbol);
/* 1562 */     return makeOuterThisVarDecl(paramInt, varSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCVariableDecl outerThisDef(int paramInt, Symbol.ClassSymbol paramClassSymbol) {
/* 1570 */     Symbol.VarSymbol varSymbol = makeOuterThisVarSymbol((Symbol)paramClassSymbol, 4112L);
/* 1571 */     return makeOuterThisVarDecl(paramInt, varSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   List<JCTree.JCExpression> loadFreevars(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, List<Symbol.VarSymbol> paramList) {
/* 1580 */     List<JCTree.JCExpression> list = List.nil();
/* 1581 */     for (List<Symbol.VarSymbol> list1 = paramList; list1.nonEmpty(); list1 = list1.tail)
/* 1582 */       list = list.prepend(loadFreevar(paramDiagnosticPosition, (Symbol.VarSymbol)list1.head));
/* 1583 */     return list;
/*      */   }
/*      */
/*      */   JCTree.JCExpression loadFreevar(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol) {
/* 1587 */     return access((Symbol)paramVarSymbol, (JCTree.JCExpression)this.make.at(paramDiagnosticPosition).Ident((Symbol)paramVarSymbol), (JCTree.JCExpression)null, false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression makeThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) {
/* 1595 */     if (this.currentClass == paramTypeSymbol)
/*      */     {
/* 1597 */       return this.make.at(paramDiagnosticPosition).This(paramTypeSymbol.erasure(this.types));
/*      */     }
/*      */
/* 1600 */     return makeOuterThis(paramDiagnosticPosition, paramTypeSymbol);
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
/*      */   JCTree makeTwrTry(JCTree.JCTry paramJCTry) {
/* 1641 */     make_at(paramJCTry.pos());
/* 1642 */     this.twrVars = this.twrVars.dup();
/* 1643 */     JCTree.JCBlock jCBlock = makeTwrBlock(paramJCTry.resources, paramJCTry.body, paramJCTry.finallyCanCompleteNormally, 0);
/*      */
/* 1645 */     if (paramJCTry.catchers.isEmpty() && paramJCTry.finalizer == null) {
/* 1646 */       this.result = translate(jCBlock);
/*      */     } else {
/* 1648 */       this.result = translate(this.make.Try(jCBlock, paramJCTry.catchers, paramJCTry.finalizer));
/* 1649 */     }  this.twrVars = this.twrVars.leave();
/* 1650 */     return this.result;
/*      */   }
/*      */
/*      */   private JCTree.JCBlock makeTwrBlock(List<JCTree> paramList, JCTree.JCBlock paramJCBlock, boolean paramBoolean, int paramInt) {
/*      */     JCTree.JCIdent jCIdent;
/* 1655 */     if (paramList.isEmpty()) {
/* 1656 */       return paramJCBlock;
/*      */     }
/*      */
/* 1659 */     ListBuffer listBuffer = new ListBuffer();
/* 1660 */     JCTree jCTree = (JCTree)paramList.head;
/* 1661 */     JCTree.JCExpression jCExpression = null;
/* 1662 */     if (jCTree instanceof JCTree.JCVariableDecl) {
/* 1663 */       JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)jCTree;
/* 1664 */       jCExpression = this.make.Ident((Symbol)jCVariableDecl.sym).setType(jCTree.type);
/* 1665 */       listBuffer.add(jCVariableDecl);
/*      */     } else {
/* 1667 */       Assert.check(jCTree instanceof JCTree.JCExpression);
/*      */
/*      */
/*      */
/*      */
/* 1672 */       Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4112L, makeSyntheticName(this.names.fromString("twrVar" + paramInt), this.twrVars), jCTree.type.hasTag(TypeTag.BOT) ? this.syms.autoCloseableType : jCTree.type, (Symbol)this.currentMethodSym);
/*      */
/*      */
/* 1675 */       this.twrVars.enter((Symbol)varSymbol);
/*      */
/* 1677 */       JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, (JCTree.JCExpression)jCTree);
/* 1678 */       jCIdent = this.make.Ident((Symbol)varSymbol);
/* 1679 */       listBuffer.add(jCVariableDecl);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1685 */     Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(4096L, makeSyntheticName(this.names.fromString("primaryException" + paramInt), this.twrVars), this.syms.throwableType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 1689 */     this.twrVars.enter((Symbol)varSymbol1);
/* 1690 */     JCTree.JCVariableDecl jCVariableDecl1 = this.make.VarDef(varSymbol1, makeNull());
/* 1691 */     listBuffer.add(jCVariableDecl1);
/*      */
/*      */
/*      */
/*      */
/* 1696 */     Symbol.VarSymbol varSymbol2 = new Symbol.VarSymbol(4112L, this.names.fromString("t" + this.target
/* 1697 */           .syntheticNameChar()), this.syms.throwableType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/* 1700 */     JCTree.JCVariableDecl jCVariableDecl2 = this.make.VarDef(varSymbol2, null);
/* 1701 */     JCTree.JCStatement jCStatement = this.make.Assignment((Symbol)varSymbol1, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol2));
/* 1702 */     JCTree.JCThrow jCThrow = this.make.Throw((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol2));
/* 1703 */     JCTree.JCBlock jCBlock1 = this.make.Block(0L, List.of(jCStatement, jCThrow));
/* 1704 */     JCTree.JCCatch jCCatch = this.make.Catch(jCVariableDecl2, jCBlock1);
/*      */
/* 1706 */     int i = this.make.pos;
/* 1707 */     this.make.at(TreeInfo.endPos((JCTree)paramJCBlock));
/* 1708 */     JCTree.JCBlock jCBlock2 = makeTwrFinallyClause((Symbol)varSymbol1, (JCTree.JCExpression)jCIdent);
/* 1709 */     this.make.at(i);
/* 1710 */     JCTree.JCTry jCTry = this.make.Try(makeTwrBlock(paramList.tail, paramJCBlock, paramBoolean, paramInt + 1),
/*      */
/* 1712 */         List.of(jCCatch), jCBlock2);
/*      */
/* 1714 */     jCTry.finallyCanCompleteNormally = paramBoolean;
/* 1715 */     listBuffer.add(jCTry);
/* 1716 */     return this.make.Block(0L, listBuffer.toList());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCBlock makeTwrFinallyClause(Symbol paramSymbol, JCTree.JCExpression paramJCExpression) {
/* 1723 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4096L, this.make.paramName(2), this.syms.throwableType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 1727 */     JCTree.JCExpressionStatement jCExpressionStatement = this.make.Exec((JCTree.JCExpression)makeCall((JCTree.JCExpression)this.make.Ident(paramSymbol), this.names.addSuppressed,
/*      */
/* 1729 */           List.of(this.make.Ident((Symbol)varSymbol))));
/*      */
/*      */
/*      */
/* 1733 */     JCTree.JCBlock jCBlock1 = this.make.Block(0L, List.of(makeResourceCloseInvocation(paramJCExpression)));
/* 1734 */     JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, null);
/* 1735 */     JCTree.JCBlock jCBlock2 = this.make.Block(0L, List.of(jCExpressionStatement));
/* 1736 */     List list = List.of(this.make.Catch(jCVariableDecl, jCBlock2));
/* 1737 */     JCTree.JCTry jCTry = this.make.Try(jCBlock1, list, null);
/* 1738 */     jCTry.finallyCanCompleteNormally = true;
/*      */
/*      */
/* 1741 */     JCTree.JCIf jCIf = this.make.If(makeNonNullCheck((JCTree.JCExpression)this.make.Ident(paramSymbol)), (JCTree.JCStatement)jCTry,
/*      */
/* 1743 */         makeResourceCloseInvocation(paramJCExpression));
/*      */
/*      */
/* 1746 */     return this.make.Block(0L,
/* 1747 */         List.of(this.make.If(makeNonNullCheck(paramJCExpression), (JCTree.JCStatement)jCIf, null)));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCStatement makeResourceCloseInvocation(JCTree.JCExpression paramJCExpression) {
/* 1754 */     if (this.types.asSuper(paramJCExpression.type, (Symbol)this.syms.autoCloseableType.tsym) == null) {
/* 1755 */       paramJCExpression = (JCTree.JCExpression)convert((JCTree)paramJCExpression, this.syms.autoCloseableType);
/*      */     }
/*      */
/*      */
/* 1759 */     JCTree.JCMethodInvocation jCMethodInvocation = makeCall(paramJCExpression, this.names.close,
/*      */
/* 1761 */         List.nil());
/* 1762 */     return (JCTree.JCStatement)this.make.Exec((JCTree.JCExpression)jCMethodInvocation);
/*      */   }
/*      */
/*      */   private JCTree.JCExpression makeNonNullCheck(JCTree.JCExpression paramJCExpression) {
/* 1766 */     return (JCTree.JCExpression)makeBinary(JCTree.Tag.NE, paramJCExpression, makeNull());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression makeOuterThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) {
/* 1775 */     List<Symbol.VarSymbol> list = this.outerThisStack;
/* 1776 */     if (list.isEmpty()) {
/* 1777 */       this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { paramTypeSymbol });
/* 1778 */       Assert.error();
/* 1779 */       return makeNull();
/*      */     }
/* 1781 */     Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)list.head;
/* 1782 */     JCTree.JCExpression jCExpression = access((JCTree.JCExpression)this.make.at(paramDiagnosticPosition).Ident((Symbol)varSymbol));
/* 1783 */     Symbol.TypeSymbol typeSymbol = varSymbol.type.tsym;
/* 1784 */     while (typeSymbol != paramTypeSymbol) {
/*      */       while (true) {
/* 1786 */         list = list.tail;
/* 1787 */         if (list.isEmpty()) {
/* 1788 */           this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { paramTypeSymbol });
/*      */
/*      */
/* 1791 */           Assert.error();
/* 1792 */           return jCExpression;
/*      */         }
/* 1794 */         varSymbol = (Symbol.VarSymbol)list.head;
/* 1795 */         if (varSymbol.owner == typeSymbol) {
/* 1796 */           if (typeSymbol.owner.kind != 1 && !typeSymbol.hasOuterInstance())
/* 1797 */           { this.chk.earlyRefError(paramDiagnosticPosition, (Symbol)paramTypeSymbol);
/* 1798 */             Assert.error();
/* 1799 */             return makeNull(); }  break;
/*      */         }
/* 1801 */       }  jCExpression = access(this.make.at(paramDiagnosticPosition).Select(jCExpression, (Symbol)varSymbol));
/* 1802 */       typeSymbol = varSymbol.type.tsym;
/*      */     }
/* 1804 */     return jCExpression;
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
/*      */   JCTree.JCExpression makeOwnerThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, boolean paramBoolean) {
/* 1817 */     Symbol symbol = paramSymbol.owner;
/* 1818 */     if (paramBoolean ? paramSymbol.isMemberOf((Symbol.TypeSymbol)this.currentClass, this.types) : this.currentClass
/* 1819 */       .isSubClass(paramSymbol.owner, this.types))
/*      */     {
/* 1821 */       return this.make.at(paramDiagnosticPosition).This(symbol.erasure(this.types));
/*      */     }
/*      */
/* 1824 */     return makeOwnerThisN(paramDiagnosticPosition, paramSymbol, paramBoolean);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCExpression makeOwnerThisN(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, boolean paramBoolean) {
/* 1832 */     Symbol symbol = paramSymbol.owner;
/* 1833 */     List<Symbol.VarSymbol> list = this.outerThisStack;
/* 1834 */     if (list.isEmpty()) {
/* 1835 */       this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { symbol });
/* 1836 */       Assert.error();
/* 1837 */       return makeNull();
/*      */     }
/* 1839 */     Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)list.head;
/* 1840 */     JCTree.JCExpression jCExpression = access((JCTree.JCExpression)this.make.at(paramDiagnosticPosition).Ident((Symbol)varSymbol));
/* 1841 */     Symbol.TypeSymbol typeSymbol = varSymbol.type.tsym;
/* 1842 */     while (paramBoolean ? paramSymbol.isMemberOf(typeSymbol, this.types) : typeSymbol.isSubClass(paramSymbol.owner, this.types)) {
/*      */       while (true) {
/* 1844 */         list = list.tail;
/* 1845 */         if (list.isEmpty()) {
/* 1846 */           this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { symbol });
/*      */
/*      */
/* 1849 */           Assert.error();
/* 1850 */           return jCExpression;
/*      */         }
/* 1852 */         varSymbol = (Symbol.VarSymbol)list.head;
/* 1853 */         if (varSymbol.owner == typeSymbol)
/* 1854 */         { jCExpression = access(this.make.at(paramDiagnosticPosition).Select(jCExpression, (Symbol)varSymbol));
/* 1855 */           typeSymbol = varSymbol.type.tsym; }
/*      */       }
/* 1857 */     }  return jCExpression;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCStatement initField(int paramInt, Name paramName) {
/* 1864 */     Scope.Entry entry = this.proxies.lookup(paramName);
/* 1865 */     Symbol symbol1 = entry.sym;
/* 1866 */     Assert.check((symbol1.owner.kind == 16));
/* 1867 */     Symbol symbol2 = (entry.next()).sym;
/* 1868 */     Assert.check((symbol1.owner.owner == symbol2.owner));
/* 1869 */     this.make.at(paramInt);
/* 1870 */     return (JCTree.JCStatement)this.make
/* 1871 */       .Exec(this.make
/* 1872 */         .Assign(this.make
/* 1873 */           .Select(this.make.This(symbol2.owner.erasure(this.types)), symbol2), (JCTree.JCExpression)this.make
/* 1874 */           .Ident(symbol1)).setType(symbol2.erasure(this.types)));
/*      */   }
/*      */
/*      */
/*      */
/*      */   JCTree.JCStatement initOuterThis(int paramInt) {
/* 1880 */     Symbol.VarSymbol varSymbol1 = (Symbol.VarSymbol)this.outerThisStack.head;
/* 1881 */     Assert.check((varSymbol1.owner.kind == 16));
/* 1882 */     Symbol.VarSymbol varSymbol2 = (Symbol.VarSymbol)this.outerThisStack.tail.head;
/* 1883 */     Assert.check((varSymbol1.owner.owner == varSymbol2.owner));
/* 1884 */     this.make.at(paramInt);
/* 1885 */     return (JCTree.JCStatement)this.make
/* 1886 */       .Exec(this.make
/* 1887 */         .Assign(this.make
/* 1888 */           .Select(this.make.This(varSymbol2.owner.erasure(this.types)), (Symbol)varSymbol2), (JCTree.JCExpression)this.make
/* 1889 */           .Ident((Symbol)varSymbol1)).setType(varSymbol2.erasure(this.types)));
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
/*      */   private Symbol.ClassSymbol outerCacheClass() {
/* 1904 */     Symbol.ClassSymbol classSymbol = this.outermostClassDef.sym;
/* 1905 */     if ((classSymbol.flags() & 0x200L) == 0L &&
/* 1906 */       !this.target.useInnerCacheClass()) return classSymbol;
/* 1907 */     Scope scope = classSymbol.members();
/* 1908 */     for (Scope.Entry entry = scope.elems; entry != null; entry = entry.sibling) {
/* 1909 */       if (entry.sym.kind == 2 && entry.sym.name == this.names.empty && (entry.sym
/*      */
/* 1911 */         .flags() & 0x200L) == 0L) return (Symbol.ClassSymbol)entry.sym;
/* 1912 */     }  return (makeEmptyClass(4104L, classSymbol)).sym;
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
/*      */   private Symbol.MethodSymbol classDollarSym(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 1927 */     Symbol.ClassSymbol classSymbol = outerCacheClass();
/*      */
/* 1929 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)lookupSynthetic(this.classDollar, classSymbol
/* 1930 */         .members());
/* 1931 */     if (methodSymbol == null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1938 */       methodSymbol = new Symbol.MethodSymbol(4104L, this.classDollar, (Type)new Type.MethodType(List.of(this.syms.stringType), this.types.erasure(this.syms.classType), List.nil(), (Symbol.TypeSymbol)this.syms.methodClass), (Symbol)classSymbol);
/*      */
/*      */
/* 1941 */       enterSynthetic(paramDiagnosticPosition, (Symbol)methodSymbol, classSymbol.members());
/*      */
/* 1943 */       JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(methodSymbol, null);
/*      */       try {
/* 1945 */         jCMethodDecl.body = classDollarSymBody(paramDiagnosticPosition, jCMethodDecl);
/* 1946 */       } catch (Symbol.CompletionFailure completionFailure) {
/* 1947 */         jCMethodDecl.body = this.make.Block(0L, List.nil());
/* 1948 */         this.chk.completionError(paramDiagnosticPosition, completionFailure);
/*      */       }
/* 1950 */       JCTree.JCClassDecl jCClassDecl = classDef(classSymbol);
/* 1951 */       jCClassDecl.defs = jCClassDecl.defs.prepend(jCMethodDecl);
/*      */     }
/* 1953 */     return methodSymbol;
/*      */   }
/*      */   JCTree.JCBlock classDollarSymBody(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.JCMethodDecl paramJCMethodDecl) {
/*      */     JCTree.JCBlock jCBlock1;
/*      */     JCTree.JCThrow jCThrow;
/* 1958 */     Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym;
/* 1959 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)methodSymbol.owner;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1966 */     if (this.target.classLiteralsNoInit()) {
/*      */
/*      */
/* 1969 */       Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(4104L, this.names.fromString("cl" + this.target.syntheticNameChar()), this.syms.classLoaderType, (Symbol)classSymbol);
/*      */
/*      */
/* 1972 */       enterSynthetic(paramDiagnosticPosition, (Symbol)varSymbol1, classSymbol.members());
/*      */
/*      */
/* 1975 */       JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol1, null);
/* 1976 */       JCTree.JCClassDecl jCClassDecl = classDef(classSymbol);
/* 1977 */       jCClassDecl.defs = jCClassDecl.defs.prepend(jCVariableDecl);
/*      */
/*      */
/*      */
/* 1981 */       JCTree.JCNewArray jCNewArray = this.make.NewArray(this.make.Type(classSymbol.type),
/* 1982 */           List.of(this.make.Literal(TypeTag.INT, Integer.valueOf(0)).setType((Type)this.syms.intType)), null);
/*      */
/* 1984 */       jCNewArray.type = (Type)new Type.ArrayType(this.types.erasure(classSymbol.type), (Symbol.TypeSymbol)this.syms.arrayClass);
/*      */
/*      */
/*      */
/*      */
/* 1989 */       Symbol.MethodSymbol methodSymbol1 = lookupMethod(this.make_pos, this.names.forName, this.types
/* 1990 */           .erasure(this.syms.classType),
/* 1991 */           List.of(this.syms.stringType, this.syms.booleanType, this.syms.classLoaderType));
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2009 */       JCTree.JCExpression jCExpression = this.make.Conditional((JCTree.JCExpression)makeBinary(JCTree.Tag.EQ, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), makeNull()), this.make.Assign((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (JCTree.JCExpression)makeCall((JCTree.JCExpression)makeCall((JCTree.JCExpression)makeCall((JCTree.JCExpression)jCNewArray, this.names.getClass, List.nil()), this.names.getComponentType, List.nil()), this.names.getClassLoader, List.nil())).setType(this.syms.classLoaderType), (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1)).setType(this.syms.classLoaderType);
/*      */
/*      */
/* 2012 */       List list = List.of(this.make.Ident((Symbol)((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).sym),
/* 2013 */           makeLit((Type)this.syms.booleanType, Integer.valueOf(0)), jCExpression);
/*      */
/*      */
/* 2016 */       jCBlock1 = this.make.Block(0L, List.of(this.make
/* 2017 */             .Call((JCTree.JCExpression)this.make
/* 2018 */               .App((JCTree.JCExpression)this.make
/* 2019 */                 .Ident((Symbol)methodSymbol1), list))));
/*      */     } else {
/*      */
/* 2022 */       Symbol.MethodSymbol methodSymbol1 = lookupMethod(this.make_pos, this.names.forName, this.types
/*      */
/* 2024 */           .erasure(this.syms.classType),
/* 2025 */           List.of(this.syms.stringType));
/*      */
/*      */
/* 2028 */       jCBlock1 = this.make.Block(0L, List.of(this.make
/* 2029 */             .Call((JCTree.JCExpression)this.make
/* 2030 */               .App(this.make
/* 2031 */                 .QualIdent((Symbol)methodSymbol1),
/* 2032 */                 List.of(this.make
/* 2033 */                   .Ident((Symbol)((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).sym))))));
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 2039 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4096L, this.make.paramName(1), this.syms.classNotFoundExceptionType, (Symbol)methodSymbol);
/*      */
/*      */
/*      */
/*      */
/* 2044 */     if (this.target.hasInitCause()) {
/*      */
/*      */
/* 2047 */       JCTree.JCMethodInvocation jCMethodInvocation = makeCall((JCTree.JCExpression)makeNewClass(this.syms.noClassDefFoundErrorType,
/* 2048 */             List.nil()), this.names.initCause,
/*      */
/* 2050 */           List.of(this.make.Ident((Symbol)varSymbol)));
/* 2051 */       jCThrow = this.make.Throw((JCTree.JCExpression)jCMethodInvocation);
/*      */     } else {
/*      */
/* 2054 */       Symbol.MethodSymbol methodSymbol1 = lookupMethod(this.make_pos, this.names.getMessage, this.syms.classNotFoundExceptionType,
/*      */
/*      */
/* 2057 */           List.nil());
/*      */
/*      */
/* 2060 */       jCThrow = this.make.Throw((JCTree.JCExpression)makeNewClass(this.syms.noClassDefFoundErrorType,
/* 2061 */             List.of(this.make.App(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol), (Symbol)methodSymbol1),
/*      */
/* 2063 */                 List.nil()))));
/*      */     }
/*      */
/*      */
/* 2067 */     JCTree.JCBlock jCBlock2 = this.make.Block(0L, List.of(jCThrow));
/*      */
/*      */
/* 2070 */     JCTree.JCCatch jCCatch = this.make.Catch(this.make.VarDef(varSymbol, null), jCBlock2);
/*      */
/*      */
/*      */
/* 2074 */     JCTree.JCTry jCTry = this.make.Try(jCBlock1,
/* 2075 */         List.of(jCCatch), null);
/*      */
/* 2077 */     return this.make.Block(0L, List.of(jCTry));
/*      */   }
/*      */
/*      */
/*      */   private JCTree.JCMethodInvocation makeCall(JCTree.JCExpression paramJCExpression, Name paramName, List<JCTree.JCExpression> paramList) {
/* 2082 */     Assert.checkNonNull(paramJCExpression.type);
/* 2083 */     Symbol.MethodSymbol methodSymbol = lookupMethod(this.make_pos, paramName, paramJCExpression.type,
/* 2084 */         TreeInfo.types(paramList));
/* 2085 */     return this.make.App(this.make.Select(paramJCExpression, (Symbol)methodSymbol), paramList);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private Name cacheName(String paramString) {
/* 2092 */     StringBuilder stringBuilder = new StringBuilder();
/* 2093 */     if (paramString.startsWith("[")) {
/* 2094 */       stringBuilder = stringBuilder.append("array");
/* 2095 */       while (paramString.startsWith("[")) {
/* 2096 */         stringBuilder = stringBuilder.append(this.target.syntheticNameChar());
/* 2097 */         paramString = paramString.substring(1);
/*      */       }
/* 2099 */       if (paramString.startsWith("L")) {
/* 2100 */         paramString = paramString.substring(0, paramString.length() - 1);
/*      */       }
/*      */     } else {
/* 2103 */       stringBuilder = stringBuilder.append("class" + this.target.syntheticNameChar());
/*      */     }
/* 2105 */     stringBuilder = stringBuilder.append(paramString.replace('.', this.target.syntheticNameChar()));
/* 2106 */     return this.names.fromString(stringBuilder.toString());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.VarSymbol cacheSym(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString) {
/* 2115 */     Symbol.ClassSymbol classSymbol = outerCacheClass();
/* 2116 */     Name name = cacheName(paramString);
/*      */
/* 2118 */     Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)lookupSynthetic(name, classSymbol.members());
/* 2119 */     if (varSymbol == null) {
/*      */
/* 2121 */       varSymbol = new Symbol.VarSymbol(4104L, name, this.types.erasure(this.syms.classType), (Symbol)classSymbol);
/* 2122 */       enterSynthetic(paramDiagnosticPosition, (Symbol)varSymbol, classSymbol.members());
/*      */
/* 2124 */       JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, null);
/* 2125 */       JCTree.JCClassDecl jCClassDecl = classDef(classSymbol);
/* 2126 */       jCClassDecl.defs = jCClassDecl.defs.prepend(jCVariableDecl);
/*      */     }
/* 2128 */     return varSymbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private JCTree.JCExpression classOf(JCTree paramJCTree)
/*      */   {
/* 2135 */     return classOfType(paramJCTree.type, paramJCTree.pos()); } private JCTree.JCExpression classOfType(Type paramType, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) { Symbol.ClassSymbol classSymbol;
/*      */     Symbol symbol;
/*      */     String str;
/*      */     Symbol.VarSymbol varSymbol;
/* 2139 */     switch (paramType.getTag()) { case ALWAYS: case LEGACY: case NONEMPTY: case null: case null: case null:
/*      */       case null:
/*      */       case null:
/*      */       case null:
/* 2143 */         classSymbol = this.types.boxedClass(paramType);
/*      */
/* 2145 */         symbol = this.rs.accessBase(this.rs
/* 2146 */             .findIdentInType(this.attrEnv, classSymbol.type, this.names.TYPE, 4), paramDiagnosticPosition, classSymbol.type, this.names.TYPE, true);
/*      */
/* 2148 */         if (symbol.kind == 4)
/* 2149 */           ((Symbol.VarSymbol)symbol).getConstValue();
/* 2150 */         return this.make.QualIdent(symbol);
/*      */       case null: case null:
/* 2152 */         if (this.target.hasClassLiterals()) {
/* 2153 */           Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(25L, this.names._class, this.syms.classType, (Symbol)paramType.tsym);
/*      */
/*      */
/* 2156 */           return make_at(paramDiagnosticPosition).Select(this.make.Type(paramType), (Symbol)varSymbol1);
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2163 */         str = this.writer.xClassName(paramType).toString().replace('/', '.');
/* 2164 */         varSymbol = cacheSym(paramDiagnosticPosition, str);
/* 2165 */         return make_at(paramDiagnosticPosition).Conditional((JCTree.JCExpression)
/* 2166 */             makeBinary(JCTree.Tag.EQ, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol), makeNull()), this.make
/* 2167 */             .Assign((JCTree.JCExpression)this.make
/* 2168 */               .Ident((Symbol)varSymbol), (JCTree.JCExpression)this.make
/* 2169 */               .App((JCTree.JCExpression)this.make
/* 2170 */                 .Ident((Symbol)classDollarSym(paramDiagnosticPosition)),
/* 2171 */                 List.of(this.make.Literal(TypeTag.CLASS, str)
/* 2172 */                   .setType(this.syms.stringType))))
/* 2173 */             .setType(this.types.erasure(this.syms.classType)), (JCTree.JCExpression)this.make
/* 2174 */             .Ident((Symbol)varSymbol)).setType(this.types.erasure(this.syms.classType)); }
/*      */
/* 2176 */     throw new AssertionError(); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.ClassSymbol assertionsDisabledClass() {
/* 2189 */     if (this.assertionsDisabledClassCache != null) return this.assertionsDisabledClassCache;
/*      */
/* 2191 */     this.assertionsDisabledClassCache = (makeEmptyClass(4104L, this.outermostClassDef.sym)).sym;
/*      */
/* 2193 */     return this.assertionsDisabledClassCache;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCExpression assertFlagTest(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2204 */     Symbol.ClassSymbol classSymbol1 = this.outermostClassDef.sym;
/*      */
/*      */
/*      */
/* 2208 */     Symbol.ClassSymbol classSymbol2 = !this.currentClass.isInterface() ? this.currentClass : assertionsDisabledClass();
/*      */
/*      */
/* 2211 */     Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)lookupSynthetic(this.dollarAssertionsDisabled, classSymbol2
/* 2212 */         .members());
/* 2213 */     if (varSymbol == null) {
/* 2214 */       varSymbol = new Symbol.VarSymbol(4120L, this.dollarAssertionsDisabled, (Type)this.syms.booleanType, (Symbol)classSymbol2);
/*      */
/*      */
/*      */
/*      */
/* 2219 */       enterSynthetic(paramDiagnosticPosition, (Symbol)varSymbol, classSymbol2.members());
/* 2220 */       Symbol.MethodSymbol methodSymbol = lookupMethod(paramDiagnosticPosition, this.names.desiredAssertionStatus, this.types
/*      */
/* 2222 */           .erasure(this.syms.classType),
/* 2223 */           List.nil());
/* 2224 */       JCTree.JCClassDecl jCClassDecl = classDef(classSymbol2);
/* 2225 */       make_at(jCClassDecl.pos());
/* 2226 */       JCTree.JCUnary jCUnary = makeUnary(JCTree.Tag.NOT, (JCTree.JCExpression)this.make.App(this.make.Select(
/* 2227 */               classOfType(this.types.erasure(classSymbol1.type), jCClassDecl
/* 2228 */                 .pos()), (Symbol)methodSymbol)));
/*      */
/* 2230 */       JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, (JCTree.JCExpression)jCUnary);
/*      */
/* 2232 */       jCClassDecl.defs = jCClassDecl.defs.prepend(jCVariableDecl);
/*      */
/* 2234 */       if (this.currentClass.isInterface()) {
/*      */
/*      */
/* 2237 */         JCTree.JCClassDecl jCClassDecl1 = classDef(this.currentClass);
/* 2238 */         make_at(jCClassDecl1.pos());
/* 2239 */         JCTree.JCIf jCIf = this.make.If(this.make.QualIdent((Symbol)varSymbol), (JCTree.JCStatement)this.make.Skip(), null);
/* 2240 */         JCTree.JCBlock jCBlock = this.make.Block(8L, List.of(jCIf));
/* 2241 */         jCClassDecl1.defs = jCClassDecl1.defs.prepend(jCBlock);
/*      */       }
/*      */     }
/* 2244 */     make_at(paramDiagnosticPosition);
/* 2245 */     return (JCTree.JCExpression)makeUnary(JCTree.Tag.NOT, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol));
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
/*      */   JCTree abstractRval(JCTree paramJCTree, Type paramType, TreeBuilder paramTreeBuilder) {
/*      */     JCTree.JCIdent jCIdent;
/* 2273 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2274 */     switch (paramJCTree.getTag()) {
/*      */       case null:
/* 2276 */         return paramTreeBuilder.build(paramJCTree);
/*      */       case null:
/* 2278 */         jCIdent = (JCTree.JCIdent)paramJCTree;
/* 2279 */         if ((jCIdent.sym.flags() & 0x10L) != 0L && jCIdent.sym.owner.kind == 16) {
/* 2280 */           return paramTreeBuilder.build(paramJCTree);
/*      */         }
/*      */         break;
/*      */     }
/* 2284 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4112L, this.names.fromString(this.target
/* 2285 */           .syntheticNameChar() + "" + paramJCTree
/* 2286 */           .hashCode()), paramType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/* 2289 */     paramJCTree = convert(paramJCTree, paramType);
/* 2290 */     JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, (JCTree.JCExpression)paramJCTree);
/* 2291 */     JCTree jCTree = paramTreeBuilder.build((JCTree)this.make.Ident((Symbol)varSymbol));
/* 2292 */     JCTree.LetExpr letExpr = this.make.LetExpr(jCVariableDecl, jCTree);
/* 2293 */     ((JCTree)letExpr).type = jCTree.type;
/* 2294 */     return (JCTree)letExpr;
/*      */   }
/*      */
/*      */
/*      */   JCTree abstractRval(JCTree paramJCTree, TreeBuilder paramTreeBuilder) {
/* 2299 */     return abstractRval(paramJCTree, paramJCTree.type, paramTreeBuilder);
/*      */   }
/*      */
/*      */
/*      */   JCTree abstractLval(JCTree paramJCTree, final TreeBuilder builder) {
/*      */     final JCTree.JCFieldAccess s;
/*      */     final JCTree.JCArrayAccess i;
/*      */     JCTree.JCExpression jCExpression;
/*      */     Symbol symbol;
/* 2308 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2309 */     switch (paramJCTree.getTag()) {
/*      */       case null:
/* 2311 */         return builder.build(paramJCTree);
/*      */       case null:
/* 2313 */         jCFieldAccess = (JCTree.JCFieldAccess)paramJCTree;
/* 2314 */         jCExpression = TreeInfo.skipParens(jCFieldAccess.selected);
/* 2315 */         symbol = TreeInfo.symbol((JCTree)jCFieldAccess.selected);
/* 2316 */         if (symbol != null && symbol.kind == 2) return builder.build(paramJCTree);
/* 2317 */         return abstractRval((JCTree)jCFieldAccess.selected, new TreeBuilder() {
/*      */               public JCTree build(JCTree param1JCTree) {
/* 2319 */                 return builder.build((JCTree)Lower.this.make.Select((JCTree.JCExpression)param1JCTree, s.sym));
/*      */               }
/*      */             });
/*      */
/*      */       case null:
/* 2324 */         jCArrayAccess = (JCTree.JCArrayAccess)paramJCTree;
/* 2325 */         return abstractRval((JCTree)jCArrayAccess.indexed, new TreeBuilder() {
/*      */               public JCTree build(final JCTree indexed) {
/* 2327 */                 return Lower.this.abstractRval((JCTree)i.index, (Type)Lower.this.syms.intType, new TreeBuilder() {
/*      */                       public JCTree build(JCTree param2JCTree) {
/* 2329 */                         JCTree.JCArrayAccess jCArrayAccess = Lower.this.make.Indexed((JCTree.JCExpression)indexed, (JCTree.JCExpression)param2JCTree);
/*      */
/* 2331 */                         jCArrayAccess.setType(i.type);
/* 2332 */                         return builder.build((JCTree)jCArrayAccess);
/*      */                       }
/*      */                     });
/*      */               }
/*      */             });
/*      */
/*      */       case null:
/* 2339 */         return abstractLval((JCTree)((JCTree.JCTypeCast)paramJCTree).expr, builder);
/*      */     }
/*      */
/* 2342 */     throw new AssertionError(paramJCTree);
/*      */   }
/*      */
/*      */
/*      */   JCTree makeComma(JCTree paramJCTree1, final JCTree expr2) {
/* 2347 */     return abstractRval(paramJCTree1, new TreeBuilder() {
/*      */           public JCTree build(JCTree param1JCTree) {
/* 2349 */             return expr2;
/*      */           }
/*      */         });
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
/*      */   public <T extends JCTree> T translate(T paramT) {
/* 2367 */     if (paramT == null) {
/* 2368 */       return null;
/*      */     }
/* 2370 */     make_at(paramT.pos());
/* 2371 */     JCTree jCTree = super.translate((JCTree)paramT);
/* 2372 */     if (this.endPosTable != null && jCTree != paramT) {
/* 2373 */       this.endPosTable.replaceTree((JCTree)paramT, jCTree);
/*      */     }
/* 2375 */     return (T)jCTree;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public <T extends JCTree> T translate(T paramT, Type paramType) {
/* 2382 */     return (paramT == null) ? null : boxIfNeeded(translate(paramT), paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public <T extends JCTree> T translate(T paramT, JCTree.JCExpression paramJCExpression) {
/* 2388 */     JCTree.JCExpression jCExpression = this.enclOp;
/* 2389 */     this.enclOp = paramJCExpression;
/* 2390 */     T t = (T)translate((Object)paramT);
/* 2391 */     this.enclOp = jCExpression;
/* 2392 */     return t;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, JCTree.JCExpression paramJCExpression) {
/* 2398 */     JCTree.JCExpression jCExpression = this.enclOp;
/* 2399 */     this.enclOp = paramJCExpression;
/* 2400 */     List<T> list = translate(paramList);
/* 2401 */     this.enclOp = jCExpression;
/* 2402 */     return list;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, Type paramType) {
/* 2408 */     if (paramList == null) return null;
/* 2409 */     for (List<T> list = paramList; list.nonEmpty(); list = list.tail)
/* 2410 */       list.head = translate((JCTree)list.head, paramType);
/* 2411 */     return paramList;
/*      */   }
/*      */
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 2415 */     if (needPackageInfoClass(paramJCCompilationUnit)) {
/* 2416 */       Name name = this.names.package_info;
/* 2417 */       long l = 1536L;
/* 2418 */       if (this.target.isPackageInfoSynthetic())
/*      */       {
/* 2420 */         l |= 0x1000L;
/*      */       }
/* 2422 */       JCTree.JCClassDecl jCClassDecl = this.make.ClassDef(this.make.Modifiers(l, paramJCCompilationUnit.packageAnnotations), name,
/*      */
/* 2424 */           List.nil(), null,
/* 2425 */           List.nil(), List.nil());
/* 2426 */       Symbol.ClassSymbol classSymbol = paramJCCompilationUnit.packge.package_info;
/* 2427 */       classSymbol.flags_field |= l;
/* 2428 */       classSymbol.setAttributes((Symbol)paramJCCompilationUnit.packge);
/* 2429 */       Type.ClassType classType = (Type.ClassType)classSymbol.type;
/* 2430 */       classType.supertype_field = this.syms.objectType;
/* 2431 */       classType.interfaces_field = List.nil();
/* 2432 */       jCClassDecl.sym = classSymbol;
/*      */
/* 2434 */       this.translated.append(jCClassDecl);
/*      */     }
/*      */   }
/*      */
/*      */   private boolean needPackageInfoClass(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 2439 */     switch (this.pkginfoOpt) {
/*      */       case ALWAYS:
/* 2441 */         return true;
/*      */       case LEGACY:
/* 2443 */         return paramJCCompilationUnit.packageAnnotations.nonEmpty();
/*      */
/*      */       case NONEMPTY:
/* 2446 */         for (Attribute.Compound compound : paramJCCompilationUnit.packge.getDeclarationAttributes()) {
/* 2447 */           Attribute.RetentionPolicy retentionPolicy = this.types.getRetention(compound);
/* 2448 */           if (retentionPolicy != Attribute.RetentionPolicy.SOURCE)
/* 2449 */             return true;
/*      */         }
/* 2451 */         return false;
/*      */     }
/* 2453 */     throw new AssertionError();
/*      */   }
/*      */
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 2457 */     Env<AttrContext> env = this.attrEnv;
/* 2458 */     Symbol.ClassSymbol classSymbol = this.currentClass;
/* 2459 */     Symbol.MethodSymbol methodSymbol = this.currentMethodSym;
/*      */
/* 2461 */     this.currentClass = paramJCClassDecl.sym;
/* 2462 */     this.currentMethodSym = null;
/* 2463 */     this.attrEnv = this.typeEnvs.remove((Symbol.TypeSymbol)this.currentClass);
/* 2464 */     if (this.attrEnv == null) {
/* 2465 */       this.attrEnv = env;
/*      */     }
/* 2467 */     this.classdefs.put(this.currentClass, paramJCClassDecl);
/*      */
/* 2469 */     this.proxies = this.proxies.dup((Symbol)this.currentClass);
/* 2470 */     List<Symbol.VarSymbol> list = this.outerThisStack;
/*      */
/*      */
/* 2473 */     if ((paramJCClassDecl.mods.flags & 0x4000L) != 0L && (
/* 2474 */       (this.types.supertype(this.currentClass.type)).tsym.flags() & 0x4000L) == 0L) {
/* 2475 */       visitEnumDef(paramJCClassDecl);
/*      */     }
/*      */
/*      */
/* 2479 */     JCTree.JCVariableDecl jCVariableDecl = null;
/* 2480 */     if (this.currentClass.hasOuterInstance()) {
/* 2481 */       jCVariableDecl = outerThisDef(paramJCClassDecl.pos, this.currentClass);
/*      */     }
/*      */
/* 2484 */     List<JCTree.JCVariableDecl> list1 = freevarDefs(paramJCClassDecl.pos,
/* 2485 */         freevars(this.currentClass), (Symbol)this.currentClass);
/*      */
/*      */
/* 2488 */     paramJCClassDecl.extending = translate(paramJCClassDecl.extending);
/* 2489 */     paramJCClassDecl.implementing = translate(paramJCClassDecl.implementing);
/*      */
/* 2491 */     if (this.currentClass.isLocal()) {
/* 2492 */       Symbol.ClassSymbol classSymbol1 = this.currentClass.owner.enclClass();
/* 2493 */       if (classSymbol1.trans_local == null) {
/* 2494 */         classSymbol1.trans_local = List.nil();
/*      */       }
/* 2496 */       classSymbol1.trans_local = classSymbol1.trans_local.prepend(this.currentClass);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 2502 */     List list2 = List.nil();
/* 2503 */     while (paramJCClassDecl.defs != list2) {
/* 2504 */       List list4 = paramJCClassDecl.defs;
/* 2505 */       for (List list5 = list4; list5.nonEmpty() && list5 != list2; list5 = list5.tail) {
/* 2506 */         JCTree jCTree = this.outermostMemberDef;
/* 2507 */         if (jCTree == null) this.outermostMemberDef = (JCTree)list5.head;
/* 2508 */         list5.head = translate((JCTree)list5.head);
/* 2509 */         this.outermostMemberDef = jCTree;
/*      */       }
/* 2511 */       list2 = list4;
/*      */     }
/*      */
/*      */
/* 2515 */     if ((paramJCClassDecl.mods.flags & 0x4L) != 0L) paramJCClassDecl.mods.flags |= 0x1L;
/* 2516 */     paramJCClassDecl.mods.flags &= 0x7E11L;
/*      */
/*      */
/* 2519 */     paramJCClassDecl.name = Convert.shortName(this.currentClass.flatName());
/*      */
/*      */
/*      */
/* 2523 */     for (List<JCTree.JCVariableDecl> list3 = list1; list3.nonEmpty(); list3 = list3.tail) {
/* 2524 */       paramJCClassDecl.defs = paramJCClassDecl.defs.prepend(list3.head);
/* 2525 */       enterSynthetic(paramJCClassDecl.pos(), (Symbol)((JCTree.JCVariableDecl)list3.head).sym, this.currentClass.members());
/*      */     }
/* 2527 */     if (this.currentClass.hasOuterInstance()) {
/* 2528 */       paramJCClassDecl.defs = paramJCClassDecl.defs.prepend(jCVariableDecl);
/* 2529 */       enterSynthetic(paramJCClassDecl.pos(), (Symbol)jCVariableDecl.sym, this.currentClass.members());
/*      */     }
/*      */
/* 2532 */     this.proxies = this.proxies.leave();
/* 2533 */     this.outerThisStack = list;
/*      */
/*      */
/* 2536 */     this.translated.append(paramJCClassDecl);
/*      */
/* 2538 */     this.attrEnv = env;
/* 2539 */     this.currentClass = classSymbol;
/* 2540 */     this.currentMethodSym = methodSymbol;
/*      */
/*      */
/* 2543 */     this.result = (JCTree)make_at(paramJCClassDecl.pos()).Block(4096L, List.nil());
/*      */   }
/*      */
/*      */   private void visitEnumDef(JCTree.JCClassDecl paramJCClassDecl) {
/*      */     List list2;
/* 2548 */     make_at(paramJCClassDecl.pos());
/*      */
/*      */
/* 2551 */     if (paramJCClassDecl.extending == null) {
/* 2552 */       paramJCClassDecl.extending = this.make.Type(this.types.supertype(paramJCClassDecl.type));
/*      */     }
/*      */
/*      */
/*      */
/* 2557 */     JCTree.JCExpression jCExpression = classOfType(paramJCClassDecl.sym.type, paramJCClassDecl.pos()).setType(this.types.erasure(this.syms.classType));
/*      */
/*      */
/* 2560 */     byte b = 0;
/* 2561 */     ListBuffer listBuffer1 = new ListBuffer();
/* 2562 */     ListBuffer listBuffer2 = new ListBuffer();
/* 2563 */     ListBuffer listBuffer3 = new ListBuffer();
/* 2564 */     List list1 = paramJCClassDecl.defs;
/* 2565 */     for (; list1.nonEmpty();
/* 2566 */       list1 = list1.tail) {
/* 2567 */       if (((JCTree)list1.head).hasTag(JCTree.Tag.VARDEF) && (((JCTree.JCVariableDecl)list1.head).mods.flags & 0x4000L) != 0L) {
/* 2568 */         JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list1.head;
/* 2569 */         visitEnumConstantDef(jCVariableDecl, b++);
/* 2570 */         listBuffer1.append(this.make.QualIdent((Symbol)jCVariableDecl.sym));
/* 2571 */         listBuffer2.append(jCVariableDecl);
/*      */       } else {
/* 2573 */         listBuffer3.append(list1.head);
/*      */       }
/*      */     }
/*      */
/*      */
/* 2578 */     Name name = this.names.fromString(this.target.syntheticNameChar() + "VALUES");
/* 2579 */     while ((paramJCClassDecl.sym.members().lookup(name)).scope != null)
/* 2580 */       name = this.names.fromString(name + "" + this.target.syntheticNameChar());
/* 2581 */     Type.ArrayType arrayType = new Type.ArrayType(this.types.erasure(paramJCClassDecl.type), (Symbol.TypeSymbol)this.syms.arrayClass);
/* 2582 */     Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(4122L, name, (Type)arrayType, (Symbol)paramJCClassDecl.type.tsym);
/*      */
/*      */
/*      */
/* 2586 */     JCTree.JCNewArray jCNewArray = this.make.NewArray(this.make.Type(this.types.erasure(paramJCClassDecl.type)),
/* 2587 */         List.nil(), listBuffer1
/* 2588 */         .toList());
/* 2589 */     jCNewArray.type = (Type)arrayType;
/* 2590 */     listBuffer2.append(this.make.VarDef(varSymbol1, (JCTree.JCExpression)jCNewArray));
/* 2591 */     paramJCClassDecl.sym.members().enter((Symbol)varSymbol1);
/*      */
/* 2593 */     Symbol.MethodSymbol methodSymbol1 = lookupMethod(paramJCClassDecl.pos(), this.names.values, paramJCClassDecl.type,
/* 2594 */         List.nil());
/*      */
/* 2596 */     if (useClone()) {
/*      */
/*      */
/* 2599 */       JCTree.JCTypeCast jCTypeCast = this.make.TypeCast(((Symbol)methodSymbol1).type.getReturnType(), (JCTree.JCExpression)this.make
/* 2600 */           .App(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (Symbol)this.syms.arrayCloneMethod)));
/*      */
/* 2602 */       list2 = List.of(this.make.Return((JCTree.JCExpression)jCTypeCast));
/*      */     } else {
/*      */
/* 2605 */       Name name1 = this.names.fromString(this.target.syntheticNameChar() + "result");
/* 2606 */       while ((paramJCClassDecl.sym.members().lookup(name1)).scope != null)
/* 2607 */         name1 = this.names.fromString(name1 + "" + this.target.syntheticNameChar());
/* 2608 */       Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4112L, name1, (Type)arrayType, (Symbol)methodSymbol1);
/*      */
/*      */
/*      */
/* 2612 */       JCTree.JCNewArray jCNewArray1 = this.make.NewArray(this.make.Type(this.types.erasure(paramJCClassDecl.type)),
/* 2613 */           List.of(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (Symbol)this.syms.lengthVar)), null);
/*      */
/* 2615 */       jCNewArray1.type = (Type)arrayType;
/* 2616 */       JCTree.JCVariableDecl jCVariableDecl = this.make.VarDef(varSymbol, (JCTree.JCExpression)jCNewArray1);
/*      */
/*      */
/* 2619 */       if (this.systemArraycopyMethod == null) {
/* 2620 */         this
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2629 */           .systemArraycopyMethod = new Symbol.MethodSymbol(9L, this.names.fromString("arraycopy"), (Type)new Type.MethodType(List.of(this.syms.objectType, this.syms.intType, this.syms.objectType, (Object[])new Type[] { (Type)this.syms.intType, (Type)this.syms.intType }), (Type)this.syms.voidType, List.nil(), (Symbol.TypeSymbol)this.syms.methodClass), (Symbol)this.syms.systemType.tsym);
/*      */       }
/*      */
/*      */
/*      */
/* 2634 */       JCTree.JCExpressionStatement jCExpressionStatement = this.make.Exec((JCTree.JCExpression)this.make.App(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)this.syms.systemType.tsym), (Symbol)this.systemArraycopyMethod),
/*      */
/* 2636 */             List.of(this.make.Ident((Symbol)varSymbol1), this.make.Literal(Integer.valueOf(0)), this.make
/* 2637 */               .Ident((Symbol)varSymbol), (Object[])new JCTree.JCExpression[] { (JCTree.JCExpression)this.make.Literal(Integer.valueOf(0)), this.make
/* 2638 */                 .Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (Symbol)this.syms.lengthVar) })));
/*      */
/*      */
/* 2641 */       JCTree.JCReturn jCReturn1 = this.make.Return((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol));
/* 2642 */       list2 = List.of(jCVariableDecl, jCExpressionStatement, jCReturn1);
/*      */     }
/*      */
/*      */
/* 2646 */     JCTree.JCMethodDecl jCMethodDecl1 = this.make.MethodDef(methodSymbol1, this.make.Block(0L, list2));
/*      */
/* 2648 */     listBuffer2.append(jCMethodDecl1);
/*      */
/* 2650 */     if (this.debugLower) {
/* 2651 */       System.err.println(paramJCClassDecl.sym + ".valuesDef = " + jCMethodDecl1);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2661 */     Symbol.MethodSymbol methodSymbol2 = lookupMethod(paramJCClassDecl.pos(), this.names.valueOf, paramJCClassDecl.sym.type,
/*      */
/*      */
/* 2664 */         List.of(this.syms.stringType));
/* 2665 */     Assert.check(((methodSymbol2.flags() & 0x8L) != 0L));
/* 2666 */     Symbol.VarSymbol varSymbol2 = (Symbol.VarSymbol)methodSymbol2.params.head;
/* 2667 */     JCTree.JCIdent jCIdent = this.make.Ident((Symbol)varSymbol2);
/*      */
/* 2669 */     JCTree.JCReturn jCReturn = this.make.Return((JCTree.JCExpression)this.make.TypeCast(paramJCClassDecl.sym.type, (JCTree.JCExpression)
/* 2670 */           makeCall((JCTree.JCExpression)this.make.Ident((Symbol)this.syms.enumSym), this.names.valueOf,
/*      */
/* 2672 */             List.of(jCExpression, jCIdent))));
/* 2673 */     JCTree.JCMethodDecl jCMethodDecl2 = this.make.MethodDef(methodSymbol2, this.make
/* 2674 */         .Block(0L, List.of(jCReturn)));
/* 2675 */     jCIdent.sym = (Symbol)((JCTree.JCVariableDecl)jCMethodDecl2.params.head).sym;
/* 2676 */     if (this.debugLower)
/* 2677 */       System.err.println(paramJCClassDecl.sym + ".valueOf = " + jCMethodDecl2);
/* 2678 */     listBuffer2.append(jCMethodDecl2);
/*      */
/* 2680 */     listBuffer2.appendList(listBuffer3.toList());
/* 2681 */     paramJCClassDecl.defs = listBuffer2.toList();
/*      */   }
/*      */
/*      */
/*      */   private boolean useClone() {
/*      */     try {
/* 2687 */       Scope.Entry entry = this.syms.objectType.tsym.members().lookup(this.names.clone);
/* 2688 */       return (entry.sym != null);
/*      */     }
/* 2690 */     catch (Symbol.CompletionFailure completionFailure) {
/* 2691 */       return false;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void visitEnumConstantDef(JCTree.JCVariableDecl paramJCVariableDecl, int paramInt) {
/* 2697 */     JCTree.JCNewClass jCNewClass = (JCTree.JCNewClass)paramJCVariableDecl.init;
/* 2698 */     jCNewClass
/*      */
/* 2700 */       .args = jCNewClass.args.prepend(makeLit((Type)this.syms.intType, Integer.valueOf(paramInt))).prepend(makeLit(this.syms.stringType, paramJCVariableDecl.name.toString()));
/*      */   }
/*      */
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 2704 */     if (paramJCMethodDecl.name == this.names.init && (this.currentClass.flags_field & 0x4000L) != 0L) {
/*      */
/*      */
/*      */
/* 2708 */       JCTree.JCVariableDecl jCVariableDecl1 = make_at(paramJCMethodDecl.pos()).Param(this.names.fromString(this.target.syntheticNameChar() + "enum" + this.target
/* 2709 */             .syntheticNameChar() + "name"), this.syms.stringType, (Symbol)paramJCMethodDecl.sym);
/*      */
/* 2711 */       jCVariableDecl1.mods.flags |= 0x1000L; jCVariableDecl1.sym.flags_field |= 0x1000L;
/*      */
/* 2713 */       JCTree.JCVariableDecl jCVariableDecl2 = this.make.Param(this.names.fromString(this.target.syntheticNameChar() + "enum" + this.target
/* 2714 */             .syntheticNameChar() + "ordinal"), (Type)this.syms.intType, (Symbol)paramJCMethodDecl.sym);
/*      */
/*      */
/* 2717 */       jCVariableDecl2.mods.flags |= 0x1000L; jCVariableDecl2.sym.flags_field |= 0x1000L;
/*      */
/* 2719 */       paramJCMethodDecl.params = paramJCMethodDecl.params.prepend(jCVariableDecl2).prepend(jCVariableDecl1);
/*      */
/* 2721 */       Symbol.MethodSymbol methodSymbol1 = paramJCMethodDecl.sym;
/* 2722 */       methodSymbol1.extraParams = methodSymbol1.extraParams.prepend(jCVariableDecl2.sym);
/* 2723 */       methodSymbol1.extraParams = methodSymbol1.extraParams.prepend(jCVariableDecl1.sym);
/* 2724 */       Type type = methodSymbol1.erasure(this.types);
/* 2725 */       methodSymbol1
/*      */
/*      */
/* 2728 */         .erasure_field = (Type)new Type.MethodType(type.getParameterTypes().prepend(this.syms.intType).prepend(this.syms.stringType), type.getReturnType(), type.getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass);
/*      */     }
/*      */
/*      */
/* 2732 */     JCTree.JCMethodDecl jCMethodDecl = this.currentMethodDef;
/* 2733 */     Symbol.MethodSymbol methodSymbol = this.currentMethodSym;
/*      */     try {
/* 2735 */       this.currentMethodDef = paramJCMethodDecl;
/* 2736 */       this.currentMethodSym = paramJCMethodDecl.sym;
/* 2737 */       visitMethodDefInternal(paramJCMethodDecl);
/*      */     } finally {
/* 2739 */       this.currentMethodDef = jCMethodDecl;
/* 2740 */       this.currentMethodSym = methodSymbol;
/*      */     }
/*      */   }
/*      */
/*      */   private void visitMethodDefInternal(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 2745 */     if (paramJCMethodDecl.name == this.names.init && (this.currentClass
/* 2746 */       .isInner() || this.currentClass.isLocal())) {
/*      */
/* 2748 */       Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym;
/*      */
/*      */
/*      */
/* 2752 */       this.proxies = this.proxies.dup((Symbol)methodSymbol);
/* 2753 */       List<Symbol.VarSymbol> list1 = this.outerThisStack;
/* 2754 */       List<Symbol.VarSymbol> list2 = freevars(this.currentClass);
/* 2755 */       JCTree.JCVariableDecl jCVariableDecl = null;
/* 2756 */       if (this.currentClass.hasOuterInstance())
/* 2757 */         jCVariableDecl = outerThisDef(paramJCMethodDecl.pos, methodSymbol);
/* 2758 */       List<JCTree.JCVariableDecl> list = freevarDefs(paramJCMethodDecl.pos, list2, (Symbol)methodSymbol, 8589934592L);
/*      */
/*      */
/* 2761 */       paramJCMethodDecl.restype = translate(paramJCMethodDecl.restype);
/* 2762 */       paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/* 2763 */       paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown);
/*      */
/*      */
/* 2766 */       if (paramJCMethodDecl.body == null) {
/* 2767 */         this.result = (JCTree)paramJCMethodDecl;
/*      */
/*      */
/*      */         return;
/*      */       }
/*      */
/* 2773 */       paramJCMethodDecl.params = paramJCMethodDecl.params.appendList(list);
/* 2774 */       if (this.currentClass.hasOuterInstance()) {
/* 2775 */         paramJCMethodDecl.params = paramJCMethodDecl.params.prepend(jCVariableDecl);
/*      */       }
/*      */
/*      */
/*      */
/* 2780 */       JCTree.JCStatement jCStatement = (JCTree.JCStatement)translate(paramJCMethodDecl.body.stats.head);
/*      */
/* 2782 */       List list3 = List.nil();
/* 2783 */       if (list2.nonEmpty()) {
/* 2784 */         List list5 = List.nil();
/* 2785 */         for (List<Symbol.VarSymbol> list6 = list2; list6.nonEmpty(); list6 = list6.tail) {
/* 2786 */           if (TreeInfo.isInitialConstructor((JCTree)paramJCMethodDecl)) {
/* 2787 */             Name name = proxyName(((Symbol.VarSymbol)list6.head).name);
/* 2788 */             methodSymbol
/* 2789 */               .capturedLocals = methodSymbol.capturedLocals.append(
/* 2790 */                 (this.proxies.lookup(name)).sym);
/* 2791 */             list3 = list3.prepend(
/* 2792 */                 initField(paramJCMethodDecl.body.pos, name));
/*      */           }
/* 2794 */           list5 = list5.prepend(((Symbol.VarSymbol)list6.head).erasure(this.types));
/*      */         }
/* 2796 */         Type type = methodSymbol.erasure(this.types);
/* 2797 */         methodSymbol
/*      */
/*      */
/* 2800 */           .erasure_field = (Type)new Type.MethodType(type.getParameterTypes().appendList(list5), type.getReturnType(), type.getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass);
/*      */       }
/*      */
/* 2803 */       if (this.currentClass.hasOuterInstance() &&
/* 2804 */         TreeInfo.isInitialConstructor((JCTree)paramJCMethodDecl))
/*      */       {
/* 2806 */         list3 = list3.prepend(initOuterThis(paramJCMethodDecl.body.pos));
/*      */       }
/*      */
/*      */
/* 2810 */       this.proxies = this.proxies.leave();
/*      */
/*      */
/*      */
/* 2814 */       List list4 = translate(paramJCMethodDecl.body.stats.tail);
/* 2815 */       if (this.target.initializeFieldsBeforeSuper()) {
/* 2816 */         paramJCMethodDecl.body.stats = list4.prepend(jCStatement).prependList(list3);
/*      */       } else {
/* 2818 */         paramJCMethodDecl.body.stats = list4.prependList(list3).prepend(jCStatement);
/*      */       }
/* 2820 */       this.outerThisStack = list1;
/*      */     } else {
/* 2822 */       Map<Symbol, Symbol> map = this.lambdaTranslationMap;
/*      */
/*      */       try {
/* 2825 */         this
/*      */
/* 2827 */           .lambdaTranslationMap = ((paramJCMethodDecl.sym.flags() & 0x1000L) != 0L && paramJCMethodDecl.sym.name.startsWith(this.names.lambda)) ? makeTranslationMap(paramJCMethodDecl) : null;
/* 2828 */         super.visitMethodDef(paramJCMethodDecl);
/*      */       } finally {
/* 2830 */         this.lambdaTranslationMap = map;
/*      */       }
/*      */     }
/* 2833 */     this.result = (JCTree)paramJCMethodDecl;
/*      */   }
/*      */
/*      */   private Map<Symbol, Symbol> makeTranslationMap(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 2837 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 2838 */     for (JCTree.JCVariableDecl jCVariableDecl : paramJCMethodDecl.params) {
/* 2839 */       Symbol.VarSymbol varSymbol = jCVariableDecl.sym;
/* 2840 */       if (varSymbol != varSymbol.baseSymbol()) {
/* 2841 */         hashMap.put(varSymbol.baseSymbol(), varSymbol);
/*      */       }
/*      */     }
/* 2844 */     return (Map)hashMap;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 2850 */     paramJCAnnotatedType.annotations = List.nil();
/* 2851 */     paramJCAnnotatedType.underlyingType = translate(paramJCAnnotatedType.underlyingType);
/*      */
/* 2853 */     if (paramJCAnnotatedType.type.isAnnotated()) {
/* 2854 */       paramJCAnnotatedType.type = paramJCAnnotatedType.underlyingType.type.unannotatedType().annotatedType(paramJCAnnotatedType.type.getAnnotationMirrors());
/* 2855 */     } else if (paramJCAnnotatedType.underlyingType.type.isAnnotated()) {
/* 2856 */       paramJCAnnotatedType.type = paramJCAnnotatedType.underlyingType.type;
/*      */     }
/* 2858 */     this.result = (JCTree)paramJCAnnotatedType;
/*      */   }
/*      */
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 2862 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz);
/* 2863 */     if (paramJCTypeCast.type.isPrimitive() != paramJCTypeCast.expr.type.isPrimitive()) {
/* 2864 */       paramJCTypeCast.expr = translate(paramJCTypeCast.expr, paramJCTypeCast.type);
/*      */     } else {
/* 2866 */       paramJCTypeCast.expr = translate(paramJCTypeCast.expr);
/* 2867 */     }  this.result = (JCTree)paramJCTypeCast;
/*      */   }
/*      */
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 2871 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramJCNewClass.constructor.owner;
/*      */
/*      */
/* 2874 */     boolean bool = ((paramJCNewClass.constructor.owner.flags() & 0x4000L) != 0L) ? true : false;
/* 2875 */     List<Type> list = paramJCNewClass.constructor.type.getParameterTypes();
/* 2876 */     if (bool) list = list.prepend(this.syms.intType).prepend(this.syms.stringType);
/* 2877 */     paramJCNewClass.args = boxArgs(list, paramJCNewClass.args, paramJCNewClass.varargsElement);
/* 2878 */     paramJCNewClass.varargsElement = null;
/*      */
/*      */
/*      */
/* 2882 */     if (classSymbol.isLocal()) {
/* 2883 */       paramJCNewClass.args = paramJCNewClass.args.appendList(loadFreevars(paramJCNewClass.pos(), freevars(classSymbol)));
/*      */     }
/*      */
/*      */
/* 2887 */     Symbol symbol = accessConstructor(paramJCNewClass.pos(), paramJCNewClass.constructor);
/* 2888 */     if (symbol != paramJCNewClass.constructor) {
/* 2889 */       paramJCNewClass.args = paramJCNewClass.args.append(makeNull());
/* 2890 */       paramJCNewClass.constructor = symbol;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 2896 */     if (classSymbol.hasOuterInstance()) {
/*      */       JCTree.JCExpression jCExpression;
/* 2898 */       if (paramJCNewClass.encl != null) {
/* 2899 */         jCExpression = this.attr.makeNullCheck(translate(paramJCNewClass.encl));
/* 2900 */         jCExpression.type = paramJCNewClass.encl.type;
/* 2901 */       } else if (classSymbol.isLocal()) {
/*      */
/* 2903 */         jCExpression = makeThis(paramJCNewClass.pos(), (classSymbol.type.getEnclosingType()).tsym);
/*      */       } else {
/*      */
/* 2906 */         jCExpression = makeOwnerThis(paramJCNewClass.pos(), (Symbol)classSymbol, false);
/*      */       }
/* 2908 */       paramJCNewClass.args = paramJCNewClass.args.prepend(jCExpression);
/*      */     }
/* 2910 */     paramJCNewClass.encl = null;
/*      */
/*      */
/*      */
/* 2914 */     if (paramJCNewClass.def != null) {
/* 2915 */       translate(paramJCNewClass.def);
/* 2916 */       paramJCNewClass.clazz = access((JCTree.JCExpression)make_at(paramJCNewClass.clazz.pos()).Ident((Symbol)paramJCNewClass.def.sym));
/* 2917 */       paramJCNewClass.def = null;
/*      */     } else {
/* 2919 */       paramJCNewClass.clazz = access((Symbol)classSymbol, paramJCNewClass.clazz, this.enclOp, false);
/*      */     }
/* 2921 */     this.result = (JCTree)paramJCNewClass;
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
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 2937 */     JCTree.JCExpression jCExpression = paramJCConditional.cond = translate(paramJCConditional.cond, (Type)this.syms.booleanType);
/* 2938 */     if (((JCTree)jCExpression).type.isTrue()) {
/* 2939 */       this.result = convert((JCTree)translate(paramJCConditional.truepart, paramJCConditional.type), paramJCConditional.type);
/* 2940 */       addPrunedInfo((JCTree)jCExpression);
/* 2941 */     } else if (((JCTree)jCExpression).type.isFalse()) {
/* 2942 */       this.result = convert((JCTree)translate(paramJCConditional.falsepart, paramJCConditional.type), paramJCConditional.type);
/* 2943 */       addPrunedInfo((JCTree)jCExpression);
/*      */     } else {
/*      */
/* 2946 */       paramJCConditional.truepart = translate(paramJCConditional.truepart, paramJCConditional.type);
/* 2947 */       paramJCConditional.falsepart = translate(paramJCConditional.falsepart, paramJCConditional.type);
/* 2948 */       this.result = (JCTree)paramJCConditional;
/*      */     }
/*      */   }
/*      */
/*      */   private JCTree convert(JCTree paramJCTree, Type paramType) {
/* 2953 */     if (paramJCTree.type == paramType || paramJCTree.type.hasTag(TypeTag.BOT))
/* 2954 */       return paramJCTree;
/* 2955 */     JCTree.JCTypeCast jCTypeCast = make_at(paramJCTree.pos()).TypeCast((JCTree)this.make.Type(paramType), (JCTree.JCExpression)paramJCTree);
/* 2956 */     ((JCTree)jCTypeCast).type = (paramJCTree.type.constValue() != null) ? this.cfolder.coerce(paramJCTree.type, paramType) : paramType;
/*      */
/* 2958 */     return (JCTree)jCTypeCast;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 2964 */     JCTree.JCExpression jCExpression = paramJCIf.cond = translate(paramJCIf.cond, (Type)this.syms.booleanType);
/* 2965 */     if (((JCTree)jCExpression).type.isTrue()) {
/* 2966 */       this.result = translate(paramJCIf.thenpart);
/* 2967 */       addPrunedInfo((JCTree)jCExpression);
/* 2968 */     } else if (((JCTree)jCExpression).type.isFalse()) {
/* 2969 */       if (paramJCIf.elsepart != null) {
/* 2970 */         this.result = translate(paramJCIf.elsepart);
/*      */       } else {
/* 2972 */         this.result = (JCTree)this.make.Skip();
/*      */       }
/* 2974 */       addPrunedInfo((JCTree)jCExpression);
/*      */     } else {
/*      */
/* 2977 */       paramJCIf.thenpart = translate(paramJCIf.thenpart);
/* 2978 */       paramJCIf.elsepart = translate(paramJCIf.elsepart);
/* 2979 */       this.result = (JCTree)paramJCIf;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 2986 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = (paramJCAssert.detail == null) ? paramJCAssert.pos() : paramJCAssert.detail.pos();
/* 2987 */     paramJCAssert.cond = translate(paramJCAssert.cond, (Type)this.syms.booleanType);
/* 2988 */     if (!paramJCAssert.cond.type.isTrue()) {
/* 2989 */       JCTree.JCBinary jCBinary; JCTree.JCExpression jCExpression = assertFlagTest(paramJCAssert.pos());
/*      */
/* 2991 */       List<JCTree.JCExpression> list = (paramJCAssert.detail == null) ? List.nil() : List.of(translate(paramJCAssert.detail));
/* 2992 */       if (!paramJCAssert.cond.type.isFalse())
/*      */       {
/* 2994 */         jCBinary = makeBinary(JCTree.Tag.AND, jCExpression, (JCTree.JCExpression)
/*      */
/* 2996 */             makeUnary(JCTree.Tag.NOT, paramJCAssert.cond));
/*      */       }
/* 2998 */       this
/* 2999 */         .result = (JCTree)this.make.If((JCTree.JCExpression)jCBinary, (JCTree.JCStatement)
/* 3000 */           make_at((JCDiagnostic.DiagnosticPosition)paramJCAssert)
/* 3001 */           .Throw((JCTree.JCExpression)makeNewClass(this.syms.assertionErrorType, list)), null);
/*      */     } else {
/*      */
/* 3004 */       this.result = (JCTree)this.make.Skip();
/*      */     }
/*      */   }
/*      */
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 3009 */     Symbol symbol = TreeInfo.symbol((JCTree)paramJCMethodInvocation.meth);
/* 3010 */     List<Type> list = symbol.type.getParameterTypes();
/* 3011 */     if (this.allowEnums && symbol.name == this.names.init && symbol.owner == this.syms.enumSym)
/*      */     {
/*      */
/* 3014 */       list = list.tail.tail; }
/* 3015 */     paramJCMethodInvocation.args = boxArgs(list, paramJCMethodInvocation.args, paramJCMethodInvocation.varargsElement);
/* 3016 */     paramJCMethodInvocation.varargsElement = null;
/* 3017 */     Name name = TreeInfo.name((JCTree)paramJCMethodInvocation.meth);
/* 3018 */     if (symbol.name == this.names.init) {
/*      */
/*      */
/* 3021 */       Symbol symbol1 = accessConstructor(paramJCMethodInvocation.pos(), symbol);
/* 3022 */       if (symbol1 != symbol) {
/* 3023 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.append(makeNull());
/* 3024 */         TreeInfo.setSymbol((JCTree)paramJCMethodInvocation.meth, symbol1);
/*      */       }
/*      */
/*      */
/*      */
/* 3029 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)symbol1.owner;
/* 3030 */       if (classSymbol.isLocal()) {
/* 3031 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.appendList(loadFreevars(paramJCMethodInvocation.pos(), freevars(classSymbol)));
/*      */       }
/*      */
/*      */
/*      */
/* 3036 */       if ((classSymbol.flags_field & 0x4000L) != 0L || classSymbol.getQualifiedName() == this.names.java_lang_Enum) {
/* 3037 */         List list1 = this.currentMethodDef.params;
/* 3038 */         if (this.currentMethodSym.owner.hasOuterInstance())
/* 3039 */           list1 = list1.tail;
/* 3040 */         paramJCMethodInvocation
/*      */
/* 3042 */           .args = paramJCMethodInvocation.args.prepend(make_at(paramJCMethodInvocation.pos()).Ident((Symbol)((JCTree.JCVariableDecl)list1.tail.head).sym)).prepend(this.make.Ident((Symbol)((JCTree.JCVariableDecl)list1.head).sym));
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3051 */       if (classSymbol.hasOuterInstance()) {
/*      */         JCTree.JCExpression jCExpression;
/* 3053 */         if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT)) {
/*      */
/* 3055 */           jCExpression = this.attr.makeNullCheck(translate(((JCTree.JCFieldAccess)paramJCMethodInvocation.meth).selected));
/* 3056 */           paramJCMethodInvocation.meth = (JCTree.JCExpression)this.make.Ident(symbol1);
/* 3057 */           ((JCTree.JCIdent)paramJCMethodInvocation.meth).name = name;
/* 3058 */         } else if (classSymbol.isLocal() || name == this.names._this) {
/*      */
/* 3060 */           jCExpression = makeThis(paramJCMethodInvocation.meth.pos(), (classSymbol.type.getEnclosingType()).tsym);
/*      */         } else {
/*      */
/* 3063 */           jCExpression = makeOwnerThisN(paramJCMethodInvocation.meth.pos(), (Symbol)classSymbol, false);
/*      */         }
/* 3065 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.prepend(jCExpression);
/*      */       }
/*      */     } else {
/*      */
/* 3069 */       paramJCMethodInvocation.meth = translate(paramJCMethodInvocation.meth);
/*      */
/*      */
/*      */
/*      */
/* 3074 */       if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.APPLY)) {
/* 3075 */         JCTree.JCMethodInvocation jCMethodInvocation = (JCTree.JCMethodInvocation)paramJCMethodInvocation.meth;
/* 3076 */         jCMethodInvocation.args = paramJCMethodInvocation.args.prependList(jCMethodInvocation.args);
/* 3077 */         this.result = (JCTree)jCMethodInvocation;
/*      */         return;
/*      */       }
/*      */     }
/* 3081 */     this.result = (JCTree)paramJCMethodInvocation;
/*      */   }
/*      */
/*      */   List<JCTree.JCExpression> boxArgs(List<Type> paramList, List<JCTree.JCExpression> paramList1, Type paramType) {
/* 3085 */     List<JCTree.JCExpression> list = paramList1;
/* 3086 */     if (paramList.isEmpty()) return list;
/* 3087 */     int i = 0;
/* 3088 */     ListBuffer listBuffer = new ListBuffer();
/* 3089 */     while (paramList.tail.nonEmpty()) {
/* 3090 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)translate(list.head, (Type)paramList.head);
/* 3091 */       i |= (jCExpression != list.head) ? 1 : 0;
/* 3092 */       listBuffer.append(jCExpression);
/* 3093 */       list = list.tail;
/* 3094 */       paramList = paramList.tail;
/*      */     }
/* 3096 */     Type type = (Type)paramList.head;
/* 3097 */     if (paramType != null) {
/* 3098 */       i = 1;
/* 3099 */       ListBuffer listBuffer1 = new ListBuffer();
/* 3100 */       while (list.nonEmpty()) {
/* 3101 */         JCTree.JCExpression jCExpression = (JCTree.JCExpression)translate(list.head, paramType);
/* 3102 */         listBuffer1.append(jCExpression);
/* 3103 */         list = list.tail;
/*      */       }
/* 3105 */       JCTree.JCNewArray jCNewArray = this.make.NewArray(this.make.Type(paramType),
/* 3106 */           List.nil(), listBuffer1
/* 3107 */           .toList());
/* 3108 */       jCNewArray.type = (Type)new Type.ArrayType(paramType, (Symbol.TypeSymbol)this.syms.arrayClass);
/* 3109 */       listBuffer.append(jCNewArray);
/*      */     } else {
/* 3111 */       if (list.length() != 1) throw new AssertionError(list);
/* 3112 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)translate(list.head, type);
/* 3113 */       i |= (jCExpression != list.head) ? 1 : 0;
/* 3114 */       listBuffer.append(jCExpression);
/* 3115 */       if (i == 0) return paramList1;
/*      */     }
/* 3117 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */   <T extends JCTree> T boxIfNeeded(T paramT, Type paramType) {
/*      */     JCTree.JCExpression jCExpression;
/* 3123 */     boolean bool = ((JCTree)paramT).type.isPrimitive();
/* 3124 */     if (bool == paramType.isPrimitive())
/* 3125 */       return paramT;
/* 3126 */     if (bool) {
/* 3127 */       Type type = this.types.unboxedType(paramType);
/* 3128 */       if (!type.hasTag(TypeTag.NONE)) {
/* 3129 */         if (!this.types.isSubtype(((JCTree)paramT).type, type))
/* 3130 */           ((JCTree)paramT).type = type.constType(((JCTree)paramT).type.constValue());
/* 3131 */         return (T)boxPrimitive((JCTree.JCExpression)paramT, paramType);
/*      */       }
/* 3133 */       jCExpression = boxPrimitive((JCTree.JCExpression)paramT);
/*      */     } else {
/*      */
/* 3136 */       jCExpression = unbox(jCExpression, paramType);
/*      */     }
/* 3138 */     return (T)jCExpression;
/*      */   }
/*      */
/*      */
/*      */   JCTree.JCExpression boxPrimitive(JCTree.JCExpression paramJCExpression) {
/* 3143 */     return boxPrimitive(paramJCExpression, (this.types.boxedClass(paramJCExpression.type)).type);
/*      */   }
/*      */
/*      */
/*      */   JCTree.JCExpression boxPrimitive(JCTree.JCExpression paramJCExpression, Type paramType) {
/* 3148 */     make_at(paramJCExpression.pos());
/* 3149 */     if (this.target.boxWithConstructors()) {
/* 3150 */       Symbol.MethodSymbol methodSymbol1 = lookupConstructor(paramJCExpression.pos(), paramType,
/*      */
/* 3152 */           List.nil()
/* 3153 */           .prepend(paramJCExpression.type));
/* 3154 */       return this.make.Create((Symbol)methodSymbol1, List.of(paramJCExpression));
/*      */     }
/* 3156 */     Symbol.MethodSymbol methodSymbol = lookupMethod(paramJCExpression.pos(), this.names.valueOf, paramType,
/*      */
/*      */
/* 3159 */         List.nil()
/* 3160 */         .prepend(paramJCExpression.type));
/* 3161 */     return (JCTree.JCExpression)this.make.App(this.make.QualIdent((Symbol)methodSymbol), List.of(paramJCExpression));
/*      */   }
/*      */
/*      */
/*      */   JCTree.JCExpression unbox(JCTree.JCExpression paramJCExpression, Type paramType) {
/*      */     JCTree.JCTypeCast jCTypeCast;
/* 3167 */     Type type = this.types.unboxedType(paramJCExpression.type);
/* 3168 */     if (type.hasTag(TypeTag.NONE)) {
/* 3169 */       type = paramType;
/* 3170 */       if (!type.isPrimitive())
/* 3171 */         throw new AssertionError(type);
/* 3172 */       make_at(paramJCExpression.pos());
/* 3173 */       jCTypeCast = this.make.TypeCast((this.types.boxedClass(type)).type, paramJCExpression);
/*      */
/*      */     }
/* 3176 */     else if (!this.types.isSubtype(type, paramType)) {
/* 3177 */       throw new AssertionError(jCTypeCast);
/*      */     }
/* 3179 */     make_at(jCTypeCast.pos());
/* 3180 */     Symbol.MethodSymbol methodSymbol = lookupMethod(jCTypeCast.pos(), type.tsym.name
/* 3181 */         .append(this.names.Value), ((JCTree.JCExpression)jCTypeCast).type,
/*      */
/* 3183 */         List.nil());
/* 3184 */     return (JCTree.JCExpression)this.make.App(this.make.Select((JCTree.JCExpression)jCTypeCast, (Symbol)methodSymbol));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 3191 */     JCTree.JCExpression jCExpression = (JCTree.JCExpression)translate(paramJCParens.expr);
/* 3192 */     this.result = (jCExpression == paramJCParens.expr) ? (JCTree)paramJCParens : (JCTree)jCExpression;
/*      */   }
/*      */
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 3196 */     paramJCArrayAccess.indexed = translate(paramJCArrayAccess.indexed);
/* 3197 */     paramJCArrayAccess.index = translate(paramJCArrayAccess.index, (Type)this.syms.intType);
/* 3198 */     this.result = (JCTree)paramJCArrayAccess;
/*      */   }
/*      */
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 3202 */     paramJCAssign.lhs = translate(paramJCAssign.lhs, (JCTree.JCExpression)paramJCAssign);
/* 3203 */     paramJCAssign.rhs = translate(paramJCAssign.rhs, paramJCAssign.lhs.type);
/*      */
/*      */
/*      */
/*      */
/* 3208 */     if (paramJCAssign.lhs.hasTag(JCTree.Tag.APPLY)) {
/* 3209 */       JCTree.JCMethodInvocation jCMethodInvocation = (JCTree.JCMethodInvocation)paramJCAssign.lhs;
/* 3210 */       jCMethodInvocation.args = List.of(paramJCAssign.rhs).prependList(jCMethodInvocation.args);
/* 3211 */       this.result = (JCTree)jCMethodInvocation;
/*      */     } else {
/* 3213 */       this.result = (JCTree)paramJCAssign;
/*      */     }
/*      */   }
/*      */
/*      */   public void visitAssignop(final JCTree.JCAssignOp tree) {
/* 3218 */     JCTree.JCExpression jCExpression = access(TreeInfo.skipParens(tree.lhs));
/*      */
/* 3220 */     final boolean boxingReq = (!tree.lhs.type.isPrimitive() && tree.operator.type.getReturnType().isPrimitive()) ? true : false;
/*      */
/* 3222 */     if (bool || jCExpression.hasTag(JCTree.Tag.APPLY)) {
/*      */
/*      */
/*      */
/* 3226 */       JCTree jCTree = abstractLval((JCTree)tree.lhs, new TreeBuilder() { public JCTree build(JCTree param1JCTree) {
/*      */               JCTree.JCTypeCast jCTypeCast;
/* 3228 */               JCTree.Tag tag = tree.getTag().noAssignOp();
/*      */
/*      */
/*      */
/*      */
/* 3233 */               Symbol symbol = Lower.this.rs.resolveBinaryOperator(tree.pos(), tag, Lower.this.attrEnv, tree.type, tree.rhs.type);
/*      */
/*      */
/*      */
/*      */
/* 3238 */               JCTree.JCExpression jCExpression1 = (JCTree.JCExpression)param1JCTree;
/* 3239 */               if (jCExpression1.type != tree.type)
/* 3240 */                 jCTypeCast = Lower.this.make.TypeCast(tree.type, jCExpression1);
/* 3241 */               JCTree.JCBinary jCBinary = Lower.this.make.Binary(tag, (JCTree.JCExpression)jCTypeCast, tree.rhs);
/* 3242 */               jCBinary.operator = symbol;
/* 3243 */               jCBinary.type = symbol.type.getReturnType();
/*      */
/* 3245 */               JCTree.JCExpression jCExpression2 = (JCTree.JCExpression)(boxingReq ? Lower.this.make.TypeCast(Lower.this.types.unboxedType(tree.type), (JCTree.JCExpression)jCBinary) : jCBinary);
/*      */
/* 3247 */               return (JCTree)Lower.this.make.Assign((JCTree.JCExpression)param1JCTree, jCExpression2).setType(tree.type);
/*      */             } }
/*      */         );
/* 3250 */       this.result = translate(jCTree);
/*      */       return;
/*      */     }
/* 3253 */     tree.lhs = translate(tree.lhs, (JCTree.JCExpression)tree);
/* 3254 */     tree.rhs = translate(tree.rhs, (Type)(tree.operator.type.getParameterTypes()).tail.head);
/*      */
/*      */
/*      */
/*      */
/* 3259 */     if (tree.lhs.hasTag(JCTree.Tag.APPLY)) {
/* 3260 */       JCTree.JCMethodInvocation jCMethodInvocation = (JCTree.JCMethodInvocation)tree.lhs;
/*      */
/*      */
/*      */
/* 3264 */       JCTree.JCExpression jCExpression1 = (((Symbol.OperatorSymbol)tree.operator).opcode == 256) ? makeString(tree.rhs) : tree.rhs;
/*      */
/* 3266 */       jCMethodInvocation.args = List.of(jCExpression1).prependList(jCMethodInvocation.args);
/* 3267 */       this.result = (JCTree)jCMethodInvocation;
/*      */     } else {
/* 3269 */       this.result = (JCTree)tree;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree lowerBoxedPostop(final JCTree.JCUnary tree) {
/* 3279 */     final boolean cast = TreeInfo.skipParens(tree.arg).hasTag(JCTree.Tag.TYPECAST);
/* 3280 */     return abstractLval((JCTree)tree.arg, new TreeBuilder() {
/*      */           public JCTree build(final JCTree tmp1) {
/* 3282 */             return Lower.this.abstractRval(tmp1, tree.arg.type, new TreeBuilder() {
/*      */                   public JCTree build(JCTree param2JCTree) {
/* 3284 */                     JCTree.Tag tag = tree.hasTag(JCTree.Tag.POSTINC) ? JCTree.Tag.PLUS_ASG : JCTree.Tag.MINUS_ASG;
/*      */
/*      */
/* 3287 */                     JCTree jCTree = (JCTree)(cast ? Lower.this.make.TypeCast(tree.arg.type, (JCTree.JCExpression)tmp1) : tmp1);
/*      */
/* 3289 */                     JCTree.JCAssignOp jCAssignOp = Lower.this.makeAssignop(tag, jCTree,
/*      */
/* 3291 */                         (JCTree)Lower.this.make.Literal(Integer.valueOf(1)));
/* 3292 */                     return Lower.this.makeComma((JCTree)jCAssignOp, param2JCTree);
/*      */                   }
/*      */                 });
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 3300 */     boolean bool = paramJCUnary.getTag().isIncOrDecUnaryOp();
/* 3301 */     if (bool && !paramJCUnary.arg.type.isPrimitive()) {
/* 3302 */       JCTree.Tag tag; JCTree.JCAssignOp jCAssignOp; switch (paramJCUnary.getTag()) {
/*      */
/*      */
/*      */
/*      */         case ALWAYS:
/*      */         case LEGACY:
/* 3308 */           tag = paramJCUnary.hasTag(JCTree.Tag.PREINC) ? JCTree.Tag.PLUS_ASG : JCTree.Tag.MINUS_ASG;
/*      */
/* 3310 */           jCAssignOp = makeAssignop(tag, (JCTree)paramJCUnary.arg, (JCTree)this.make
/*      */
/* 3312 */               .Literal(Integer.valueOf(1)));
/* 3313 */           this.result = translate(jCAssignOp, paramJCUnary.type);
/*      */           return;
/*      */
/*      */
/*      */         case NONEMPTY:
/*      */         case null:
/* 3319 */           this.result = translate(lowerBoxedPostop(paramJCUnary), paramJCUnary.type);
/*      */           return;
/*      */       }
/*      */
/* 3323 */       throw new AssertionError(paramJCUnary);
/*      */     }
/*      */
/* 3326 */     paramJCUnary.arg = boxIfNeeded(translate(paramJCUnary.arg, (JCTree.JCExpression)paramJCUnary), paramJCUnary.type);
/*      */
/* 3328 */     if (paramJCUnary.hasTag(JCTree.Tag.NOT) && paramJCUnary.arg.type.constValue() != null) {
/* 3329 */       paramJCUnary.type = this.cfolder.fold1(257, paramJCUnary.arg.type);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 3335 */     if (bool && paramJCUnary.arg.hasTag(JCTree.Tag.APPLY)) {
/* 3336 */       this.result = (JCTree)paramJCUnary.arg;
/*      */     } else {
/* 3338 */       this.result = (JCTree)paramJCUnary;
/*      */     }
/*      */   }
/*      */
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 3343 */     List list = paramJCBinary.operator.type.getParameterTypes();
/* 3344 */     JCTree.JCExpression jCExpression = paramJCBinary.lhs = translate(paramJCBinary.lhs, (Type)list.head);
/* 3345 */     switch (paramJCBinary.getTag()) {
/*      */       case null:
/* 3347 */         if (((JCTree)jCExpression).type.isTrue()) {
/* 3348 */           this.result = (JCTree)jCExpression;
/*      */           return;
/*      */         }
/* 3351 */         if (((JCTree)jCExpression).type.isFalse()) {
/* 3352 */           this.result = translate(paramJCBinary.rhs, (Type)list.tail.head);
/*      */           return;
/*      */         }
/*      */         break;
/*      */       case null:
/* 3357 */         if (((JCTree)jCExpression).type.isFalse()) {
/* 3358 */           this.result = (JCTree)jCExpression;
/*      */           return;
/*      */         }
/* 3361 */         if (((JCTree)jCExpression).type.isTrue()) {
/* 3362 */           this.result = translate(paramJCBinary.rhs, (Type)list.tail.head);
/*      */           return;
/*      */         }
/*      */         break;
/*      */     }
/* 3367 */     paramJCBinary.rhs = translate(paramJCBinary.rhs, (Type)list.tail.head);
/* 3368 */     this.result = (JCTree)paramJCBinary;
/*      */   }
/*      */
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 3372 */     this.result = (JCTree)access(paramJCIdent.sym, (JCTree.JCExpression)paramJCIdent, this.enclOp, false);
/*      */   }
/*      */
/*      */
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 3377 */     if (this.types.elemtype(paramJCEnhancedForLoop.expr.type) == null) {
/* 3378 */       visitIterableForeachLoop(paramJCEnhancedForLoop);
/*      */     } else {
/* 3380 */       visitArrayForeachLoop(paramJCEnhancedForLoop);
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
/*      */   private void visitArrayForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 3405 */     make_at(paramJCEnhancedForLoop.expr.pos());
/*      */
/* 3407 */     Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(4096L, this.names.fromString("arr" + this.target.syntheticNameChar()), paramJCEnhancedForLoop.expr.type, (Symbol)this.currentMethodSym);
/*      */
/*      */
/* 3410 */     JCTree.JCVariableDecl jCVariableDecl1 = this.make.VarDef(varSymbol1, paramJCEnhancedForLoop.expr);
/*      */
/* 3412 */     Symbol.VarSymbol varSymbol2 = new Symbol.VarSymbol(4096L, this.names.fromString("len" + this.target.syntheticNameChar()), (Type)this.syms.intType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 3416 */     JCTree.JCVariableDecl jCVariableDecl2 = this.make.VarDef(varSymbol2, this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (Symbol)this.syms.lengthVar));
/*      */
/* 3418 */     Symbol.VarSymbol varSymbol3 = new Symbol.VarSymbol(4096L, this.names.fromString("i" + this.target.syntheticNameChar()), (Type)this.syms.intType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 3422 */     JCTree.JCVariableDecl jCVariableDecl3 = this.make.VarDef(varSymbol3, (JCTree.JCExpression)this.make.Literal(TypeTag.INT, Integer.valueOf(0)));
/* 3423 */     jCVariableDecl3.init.type = jCVariableDecl3.type = this.syms.intType.constType(Integer.valueOf(0));
/*      */
/* 3425 */     List list = List.of(jCVariableDecl1, jCVariableDecl2, jCVariableDecl3);
/* 3426 */     JCTree.JCBinary jCBinary = makeBinary(JCTree.Tag.LT, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol3), (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol2));
/*      */
/* 3428 */     JCTree.JCExpressionStatement jCExpressionStatement = this.make.Exec((JCTree.JCExpression)makeUnary(JCTree.Tag.PREINC, (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol3)));
/*      */
/* 3430 */     Type type = this.types.elemtype(paramJCEnhancedForLoop.expr.type);
/*      */
/* 3432 */     JCTree.JCExpression jCExpression = this.make.Indexed((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), (JCTree.JCExpression)this.make.Ident((Symbol)varSymbol3)).setType(type);
/*      */
/*      */
/*      */
/* 3436 */     JCTree.JCVariableDecl jCVariableDecl4 = (JCTree.JCVariableDecl)this.make.VarDef(paramJCEnhancedForLoop.var.mods, paramJCEnhancedForLoop.var.name, paramJCEnhancedForLoop.var.vartype, jCExpression).setType(paramJCEnhancedForLoop.var.type);
/* 3437 */     jCVariableDecl4.sym = paramJCEnhancedForLoop.var.sym;
/*      */
/* 3439 */     JCTree.JCBlock jCBlock = this.make.Block(0L, List.of(jCVariableDecl4, paramJCEnhancedForLoop.body));
/*      */
/* 3441 */     this.result = translate(this.make
/* 3442 */         .ForLoop(list, (JCTree.JCExpression)jCBinary,
/*      */
/* 3444 */           List.of(jCExpressionStatement), (JCTree.JCStatement)jCBlock));
/*      */
/* 3446 */     patchTargets((JCTree)jCBlock, (JCTree)paramJCEnhancedForLoop, this.result);
/*      */   }
/*      */
/*      */   private void patchTargets(JCTree paramJCTree1, final JCTree src, final JCTree dest) {
/*      */     class Patcher extends TreeScanner {
/*      */       public void visitBreak(JCTree.JCBreak param1JCBreak) {
/* 3452 */         if (param1JCBreak.target == src)
/* 3453 */           param1JCBreak.target = dest;
/*      */       }
/*      */       public void visitContinue(JCTree.JCContinue param1JCContinue) {
/* 3456 */         if (param1JCContinue.target == src)
/* 3457 */           param1JCContinue.target = dest;
/*      */       }
/*      */
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {} };
/* 3461 */     (new Patcher()).scan(paramJCTree1);
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
/*      */   private void visitIterableForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*      */     JCTree.JCTypeCast jCTypeCast;
/* 3482 */     make_at(paramJCEnhancedForLoop.expr.pos());
/* 3483 */     Type type1 = this.syms.objectType;
/* 3484 */     Type type2 = this.types.asSuper(this.types.cvarUpperBound(paramJCEnhancedForLoop.expr.type), (Symbol)this.syms.iterableType.tsym);
/*      */
/* 3486 */     if (type2.getTypeArguments().nonEmpty())
/* 3487 */       type1 = this.types.erasure((Type)(type2.getTypeArguments()).head);
/* 3488 */     Type type3 = paramJCEnhancedForLoop.expr.type;
/* 3489 */     while (type3.hasTag(TypeTag.TYPEVAR)) {
/* 3490 */       type3 = type3.getUpperBound();
/*      */     }
/* 3492 */     paramJCEnhancedForLoop.expr.type = this.types.erasure(type3);
/* 3493 */     if (type3.isCompound())
/* 3494 */       paramJCEnhancedForLoop.expr = (JCTree.JCExpression)this.make.TypeCast(this.types.erasure(type2), paramJCEnhancedForLoop.expr);
/* 3495 */     Symbol.MethodSymbol methodSymbol1 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.iterator, type3,
/*      */
/*      */
/* 3498 */         List.nil());
/*      */
/* 3500 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4096L, this.names.fromString("i" + this.target.syntheticNameChar()), this.types.erasure(this.types.asSuper(((Symbol)methodSymbol1).type.getReturnType(), (Symbol)this.syms.iteratorType.tsym)), (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 3504 */     JCTree.JCVariableDecl jCVariableDecl1 = this.make.VarDef(varSymbol, (JCTree.JCExpression)this.make.App(this.make.Select(paramJCEnhancedForLoop.expr, (Symbol)methodSymbol1)
/* 3505 */           .setType(this.types.erasure(((Symbol)methodSymbol1).type))));
/*      */
/* 3507 */     Symbol.MethodSymbol methodSymbol2 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.hasNext, varSymbol.type,
/*      */
/*      */
/* 3510 */         List.nil());
/* 3511 */     JCTree.JCMethodInvocation jCMethodInvocation1 = this.make.App(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol), (Symbol)methodSymbol2));
/* 3512 */     Symbol.MethodSymbol methodSymbol3 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.next, varSymbol.type,
/*      */
/*      */
/* 3515 */         List.nil());
/* 3516 */     JCTree.JCMethodInvocation jCMethodInvocation2 = this.make.App(this.make.Select((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol), (Symbol)methodSymbol3));
/* 3517 */     if (paramJCEnhancedForLoop.var.type.isPrimitive()) {
/* 3518 */       jCTypeCast = this.make.TypeCast(this.types.cvarUpperBound(type1), (JCTree.JCExpression)jCMethodInvocation2);
/*      */     } else {
/* 3520 */       jCTypeCast = this.make.TypeCast(paramJCEnhancedForLoop.var.type, (JCTree.JCExpression)jCTypeCast);
/*      */     }
/*      */
/*      */
/* 3524 */     JCTree.JCVariableDecl jCVariableDecl2 = (JCTree.JCVariableDecl)this.make.VarDef(paramJCEnhancedForLoop.var.mods, paramJCEnhancedForLoop.var.name, paramJCEnhancedForLoop.var.vartype, (JCTree.JCExpression)jCTypeCast).setType(paramJCEnhancedForLoop.var.type);
/* 3525 */     jCVariableDecl2.sym = paramJCEnhancedForLoop.var.sym;
/* 3526 */     JCTree.JCBlock jCBlock = this.make.Block(0L, List.of(jCVariableDecl2, paramJCEnhancedForLoop.body));
/* 3527 */     jCBlock.endpos = TreeInfo.endPos((JCTree)paramJCEnhancedForLoop.body);
/* 3528 */     this.result = translate(this.make
/* 3529 */         .ForLoop(List.of(jCVariableDecl1), (JCTree.JCExpression)jCMethodInvocation1,
/*      */
/* 3531 */           List.nil(), (JCTree.JCStatement)jCBlock));
/*      */
/* 3533 */     patchTargets((JCTree)jCBlock, (JCTree)paramJCEnhancedForLoop, this.result);
/*      */   }
/*      */
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 3537 */     Symbol.MethodSymbol methodSymbol = this.currentMethodSym;
/* 3538 */     paramJCVariableDecl.mods = translate(paramJCVariableDecl.mods);
/* 3539 */     paramJCVariableDecl.vartype = translate(paramJCVariableDecl.vartype);
/* 3540 */     if (this.currentMethodSym == null)
/*      */     {
/* 3542 */       this.currentMethodSym = new Symbol.MethodSymbol(paramJCVariableDecl.mods.flags & 0x8L | 0x100000L, this.names.empty, null, (Symbol)this.currentClass);
/*      */     }
/*      */
/*      */
/*      */
/* 3547 */     if (paramJCVariableDecl.init != null) paramJCVariableDecl.init = translate(paramJCVariableDecl.init, paramJCVariableDecl.type);
/* 3548 */     this.result = (JCTree)paramJCVariableDecl;
/* 3549 */     this.currentMethodSym = methodSymbol;
/*      */   }
/*      */
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 3553 */     Symbol.MethodSymbol methodSymbol = this.currentMethodSym;
/* 3554 */     if (this.currentMethodSym == null)
/*      */     {
/* 3556 */       this.currentMethodSym = new Symbol.MethodSymbol(paramJCBlock.flags | 0x100000L, this.names.empty, null, (Symbol)this.currentClass);
/*      */     }
/*      */
/*      */
/*      */
/* 3561 */     super.visitBlock(paramJCBlock);
/* 3562 */     this.currentMethodSym = methodSymbol;
/*      */   }
/*      */
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 3566 */     paramJCDoWhileLoop.body = translate(paramJCDoWhileLoop.body);
/* 3567 */     paramJCDoWhileLoop.cond = translate(paramJCDoWhileLoop.cond, (Type)this.syms.booleanType);
/* 3568 */     this.result = (JCTree)paramJCDoWhileLoop;
/*      */   }
/*      */
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 3572 */     paramJCWhileLoop.cond = translate(paramJCWhileLoop.cond, (Type)this.syms.booleanType);
/* 3573 */     paramJCWhileLoop.body = translate(paramJCWhileLoop.body);
/* 3574 */     this.result = (JCTree)paramJCWhileLoop;
/*      */   }
/*      */
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 3578 */     paramJCForLoop.init = translate(paramJCForLoop.init);
/* 3579 */     if (paramJCForLoop.cond != null)
/* 3580 */       paramJCForLoop.cond = translate(paramJCForLoop.cond, (Type)this.syms.booleanType);
/* 3581 */     paramJCForLoop.step = translate(paramJCForLoop.step);
/* 3582 */     paramJCForLoop.body = translate(paramJCForLoop.body);
/* 3583 */     this.result = (JCTree)paramJCForLoop;
/*      */   }
/*      */
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 3587 */     if (paramJCReturn.expr != null) {
/* 3588 */       paramJCReturn.expr = translate(paramJCReturn.expr, this.types
/* 3589 */           .erasure(this.currentMethodDef.restype.type));
/*      */     }
/* 3591 */     this.result = (JCTree)paramJCReturn;
/*      */   }
/*      */
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 3595 */     Type type1 = this.types.supertype(paramJCSwitch.selector.type);
/*      */
/* 3597 */     boolean bool1 = (type1 != null && (paramJCSwitch.selector.type.tsym.flags() & 0x4000L) != 0L) ? true : false;
/*      */
/* 3599 */     boolean bool2 = (type1 != null && this.types.isSameType(paramJCSwitch.selector.type, this.syms.stringType)) ? true : false;
/* 3600 */     Type type2 = (Type)(bool1 ? paramJCSwitch.selector.type : (bool2 ? this.syms.stringType : this.syms.intType));
/*      */
/* 3602 */     paramJCSwitch.selector = translate(paramJCSwitch.selector, type2);
/* 3603 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/* 3604 */     if (bool1) {
/* 3605 */       this.result = visitEnumSwitch(paramJCSwitch);
/* 3606 */     } else if (bool2) {
/* 3607 */       this.result = visitStringSwitch(paramJCSwitch);
/*      */     } else {
/* 3609 */       this.result = (JCTree)paramJCSwitch;
/*      */     }
/*      */   }
/*      */
/*      */   public JCTree visitEnumSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 3614 */     Symbol.TypeSymbol typeSymbol = paramJCSwitch.selector.type.tsym;
/* 3615 */     EnumMapping enumMapping = mapForEnum(paramJCSwitch.pos(), typeSymbol);
/* 3616 */     make_at(paramJCSwitch.pos());
/* 3617 */     Symbol.MethodSymbol methodSymbol = lookupMethod(paramJCSwitch.pos(), this.names.ordinal, paramJCSwitch.selector.type,
/*      */
/*      */
/* 3620 */         List.nil());
/* 3621 */     JCTree.JCArrayAccess jCArrayAccess = this.make.Indexed((Symbol)enumMapping.mapVar, (JCTree.JCExpression)this.make
/* 3622 */         .App(this.make.Select(paramJCSwitch.selector, (Symbol)methodSymbol)));
/*      */
/* 3624 */     ListBuffer listBuffer = new ListBuffer();
/* 3625 */     for (JCTree.JCCase jCCase : paramJCSwitch.cases) {
/* 3626 */       if (jCCase.pat != null) {
/* 3627 */         Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)TreeInfo.symbol((JCTree)jCCase.pat);
/* 3628 */         JCTree.JCLiteral jCLiteral = enumMapping.forConstant(varSymbol);
/* 3629 */         listBuffer.append(this.make.Case((JCTree.JCExpression)jCLiteral, jCCase.stats)); continue;
/*      */       }
/* 3631 */       listBuffer.append(jCCase);
/*      */     }
/*      */
/* 3634 */     JCTree.JCSwitch jCSwitch = this.make.Switch((JCTree.JCExpression)jCArrayAccess, listBuffer.toList());
/* 3635 */     patchTargets((JCTree)jCSwitch, (JCTree)paramJCSwitch, (JCTree)jCSwitch);
/* 3636 */     return (JCTree)jCSwitch;
/*      */   }
/*      */
/*      */   public JCTree visitStringSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 3640 */     List list = paramJCSwitch.getCases();
/* 3641 */     int i = list.size();
/*      */
/* 3643 */     if (i == 0) {
/* 3644 */       return (JCTree)this.make.at(paramJCSwitch.pos()).Exec(this.attr.makeNullCheck(paramJCSwitch.getExpression()));
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3687 */     ListBuffer listBuffer1 = new ListBuffer();
/*      */
/*      */
/*      */
/* 3691 */     LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<>(i + 1, 1.0F);
/*      */
/*      */
/*      */
/* 3695 */     LinkedHashMap<Object, Object> linkedHashMap2 = new LinkedHashMap<>(i + 1, 1.0F);
/*      */
/*      */
/* 3698 */     byte b = 0;
/* 3699 */     for (JCTree.JCCase jCCase : list) {
/* 3700 */       JCTree.JCExpression jCExpression = jCCase.getExpression();
/*      */
/* 3702 */       if (jCExpression != null) {
/* 3703 */         String str = (String)jCExpression.type.constValue();
/* 3704 */         Integer integer = (Integer)linkedHashMap1.put(str, Integer.valueOf(b));
/* 3705 */         Assert.checkNull(integer);
/* 3706 */         int j = str.hashCode();
/*      */
/* 3708 */         Set<String> set = (Set)linkedHashMap2.get(Integer.valueOf(j));
/* 3709 */         if (set == null) {
/* 3710 */           set = new LinkedHashSet(1, 1.0F);
/* 3711 */           set.add(str);
/* 3712 */           linkedHashMap2.put(Integer.valueOf(j), set);
/*      */         } else {
/* 3714 */           boolean bool = set.add(str);
/* 3715 */           Assert.check(bool);
/*      */         }
/*      */       }
/* 3718 */       b++;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3744 */     Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(4112L, this.names.fromString("s" + paramJCSwitch.pos + this.target.syntheticNameChar()), this.syms.stringType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/* 3747 */     listBuffer1.append(this.make.at(paramJCSwitch.pos()).VarDef(varSymbol1, paramJCSwitch.getExpression()).setType(varSymbol1.type));
/*      */
/*      */
/* 3750 */     Symbol.VarSymbol varSymbol2 = new Symbol.VarSymbol(4096L, this.names.fromString("tmp" + paramJCSwitch.pos + this.target.syntheticNameChar()), (Type)this.syms.intType, (Symbol)this.currentMethodSym);
/*      */
/*      */
/*      */
/* 3754 */     JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)this.make.VarDef(varSymbol2, (JCTree.JCExpression)this.make.Literal(TypeTag.INT, Integer.valueOf(-1))).setType(varSymbol2.type);
/* 3755 */     jCVariableDecl.init.type = varSymbol2.type = (Type)this.syms.intType;
/* 3756 */     listBuffer1.append(jCVariableDecl);
/* 3757 */     ListBuffer listBuffer2 = new ListBuffer();
/*      */
/*      */
/*      */
/* 3761 */     JCTree.JCMethodInvocation jCMethodInvocation = makeCall((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), this.names.hashCode, List.nil()).setType((Type)this.syms.intType);
/* 3762 */     JCTree.JCSwitch jCSwitch1 = this.make.Switch((JCTree.JCExpression)jCMethodInvocation, listBuffer2
/* 3763 */         .toList());
/* 3764 */     for (Map.Entry<Object, Object> entry : linkedHashMap2.entrySet()) {
/* 3765 */       int j = ((Integer)entry.getKey()).intValue();
/* 3766 */       Set set = (Set)entry.getValue();
/* 3767 */       Assert.check((set.size() >= 1));
/*      */
/* 3769 */       JCTree.JCIf jCIf = null;
/* 3770 */       for (String str : set) {
/* 3771 */         JCTree.JCMethodInvocation jCMethodInvocation1 = makeCall((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol1), this.names.equals,
/*      */
/* 3773 */             List.of(this.make.Literal(str)));
/* 3774 */         jCIf = this.make.If((JCTree.JCExpression)jCMethodInvocation1, (JCTree.JCStatement)this.make
/* 3775 */             .Exec(this.make.Assign((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol2), (JCTree.JCExpression)this.make
/* 3776 */                 .Literal(linkedHashMap1.get(str)))
/* 3777 */               .setType(varSymbol2.type)), (JCTree.JCStatement)jCIf);
/*      */       }
/*      */
/*      */
/* 3781 */       ListBuffer listBuffer = new ListBuffer();
/* 3782 */       JCTree.JCBreak jCBreak = this.make.Break(null);
/* 3783 */       jCBreak.target = (JCTree)jCSwitch1;
/* 3784 */       listBuffer.append(jCIf).append(jCBreak);
/*      */
/* 3786 */       listBuffer2.append(this.make.Case((JCTree.JCExpression)this.make.Literal(Integer.valueOf(j)), listBuffer.toList()));
/*      */     }
/*      */
/* 3789 */     jCSwitch1.cases = listBuffer2.toList();
/* 3790 */     listBuffer1.append(jCSwitch1);
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3796 */     ListBuffer listBuffer3 = new ListBuffer();
/* 3797 */     JCTree.JCSwitch jCSwitch2 = this.make.Switch((JCTree.JCExpression)this.make.Ident((Symbol)varSymbol2), listBuffer3.toList());
/* 3798 */     for (JCTree.JCCase jCCase : list) {
/*      */       JCTree.JCLiteral jCLiteral;
/*      */
/* 3801 */       patchTargets((JCTree)jCCase, (JCTree)paramJCSwitch, (JCTree)jCSwitch2);
/*      */
/* 3803 */       boolean bool = (jCCase.getExpression() == null) ? true : false;
/*      */
/* 3805 */       if (bool) {
/* 3806 */         jCLiteral = null;
/*      */       } else {
/* 3808 */         jCLiteral = this.make.Literal(linkedHashMap1.get((TreeInfo.skipParens(jCCase
/* 3809 */                 .getExpression())).type
/* 3810 */               .constValue()));
/*      */       }
/*      */
/* 3813 */       listBuffer3.append(this.make.Case((JCTree.JCExpression)jCLiteral, jCCase
/* 3814 */             .getStatements()));
/*      */     }
/*      */
/* 3817 */     jCSwitch2.cases = listBuffer3.toList();
/* 3818 */     listBuffer1.append(jCSwitch2);
/*      */
/* 3820 */     return (JCTree)this.make.Block(0L, listBuffer1.toList());
/*      */   }
/*      */
/*      */
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 3825 */     paramJCNewArray.elemtype = translate(paramJCNewArray.elemtype);
/* 3826 */     for (List list = paramJCNewArray.dims; list.tail != null; list = list.tail) {
/* 3827 */       if (list.head != null) list.head = translate((JCTree)list.head, (Type)this.syms.intType);
/* 3828 */     }  paramJCNewArray.elems = translate(paramJCNewArray.elems, this.types.elemtype(paramJCNewArray.type));
/* 3829 */     this.result = (JCTree)paramJCNewArray;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 3839 */     boolean bool = (paramJCFieldAccess.selected.hasTag(JCTree.Tag.SELECT) && TreeInfo.name((JCTree)paramJCFieldAccess.selected) == this.names._super && !this.types.isDirectSuperInterface(((JCTree.JCFieldAccess)paramJCFieldAccess.selected).selected.type.tsym, (Symbol.TypeSymbol)this.currentClass)) ? true : false;
/* 3840 */     paramJCFieldAccess.selected = translate(paramJCFieldAccess.selected);
/* 3841 */     if (paramJCFieldAccess.name == this.names._class) {
/* 3842 */       this.result = (JCTree)classOf((JCTree)paramJCFieldAccess.selected);
/*      */     }
/* 3844 */     else if (paramJCFieldAccess.name == this.names._super && this.types
/* 3845 */       .isDirectSuperInterface(paramJCFieldAccess.selected.type.tsym, (Symbol.TypeSymbol)this.currentClass)) {
/*      */
/* 3847 */       Symbol.TypeSymbol typeSymbol = paramJCFieldAccess.selected.type.tsym;
/* 3848 */       Assert.checkNonNull(this.types.asSuper(this.currentClass.type, (Symbol)typeSymbol));
/* 3849 */       this.result = (JCTree)paramJCFieldAccess;
/*      */     }
/* 3851 */     else if (paramJCFieldAccess.name == this.names._this || paramJCFieldAccess.name == this.names._super) {
/* 3852 */       this.result = (JCTree)makeThis(paramJCFieldAccess.pos(), paramJCFieldAccess.selected.type.tsym);
/*      */     } else {
/*      */
/* 3855 */       this.result = (JCTree)access(paramJCFieldAccess.sym, (JCTree.JCExpression)paramJCFieldAccess, this.enclOp, bool);
/*      */     }
/*      */   }
/*      */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/* 3859 */     paramLetExpr.defs = translateVarDefs(paramLetExpr.defs);
/* 3860 */     paramLetExpr.expr = translate(paramLetExpr.expr, paramLetExpr.type);
/* 3861 */     this.result = (JCTree)paramLetExpr;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 3867 */     this.result = (JCTree)paramJCAnnotation;
/*      */   }
/*      */
/*      */
/*      */   public void visitTry(JCTree.JCTry paramJCTry) {
/* 3872 */     if (paramJCTry.resources.nonEmpty()) {
/* 3873 */       this.result = makeTwrTry(paramJCTry);
/*      */
/*      */       return;
/*      */     }
/* 3877 */     boolean bool1 = paramJCTry.body.getStatements().nonEmpty();
/* 3878 */     boolean bool2 = paramJCTry.catchers.nonEmpty();
/*      */
/* 3880 */     boolean bool = (paramJCTry.finalizer != null && paramJCTry.finalizer.getStatements().nonEmpty()) ? true : false;
/*      */
/* 3882 */     if (!bool2 && !bool) {
/* 3883 */       this.result = translate(paramJCTry.body);
/*      */
/*      */       return;
/*      */     }
/* 3887 */     if (!bool1) {
/* 3888 */       if (bool) {
/* 3889 */         this.result = translate(paramJCTry.finalizer);
/*      */       } else {
/* 3891 */         this.result = translate(paramJCTry.body);
/*      */       }
/*      */
/*      */       return;
/*      */     }
/*      */
/* 3897 */     super.visitTry(paramJCTry);
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
/*      */   public List<JCTree> translateTopLevelClass(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/* 3911 */     ListBuffer<JCTree> listBuffer = null;
/*      */     try {
/* 3913 */       this.attrEnv = paramEnv;
/* 3914 */       this.make = paramTreeMaker;
/* 3915 */       this.endPosTable = paramEnv.toplevel.endPositions;
/* 3916 */       this.currentClass = null;
/* 3917 */       this.currentMethodDef = null;
/* 3918 */       this.outermostClassDef = paramJCTree.hasTag(JCTree.Tag.CLASSDEF) ? (JCTree.JCClassDecl)paramJCTree : null;
/* 3919 */       this.outermostMemberDef = null;
/* 3920 */       this.translated = new ListBuffer();
/* 3921 */       this.classdefs = new HashMap<>();
/* 3922 */       this.actualSymbols = new HashMap<>();
/* 3923 */       this.freevarCache = new HashMap<>();
/* 3924 */       this.proxies = new Scope((Symbol)this.syms.noSymbol);
/* 3925 */       this.twrVars = new Scope((Symbol)this.syms.noSymbol);
/* 3926 */       this.outerThisStack = List.nil();
/* 3927 */       this.accessNums = new HashMap<>();
/* 3928 */       this.accessSyms = (Map)new HashMap<>();
/* 3929 */       this.accessConstrs = new HashMap<>();
/* 3930 */       this.accessConstrTags = List.nil();
/* 3931 */       this.accessed = new ListBuffer();
/* 3932 */       translate(paramJCTree, (JCTree.JCExpression)null);
/* 3933 */       for (List list = this.accessed.toList(); list.nonEmpty(); list = list.tail)
/* 3934 */         makeAccessible((Symbol)list.head);
/* 3935 */       for (EnumMapping enumMapping : this.enumSwitchMap.values())
/* 3936 */         enumMapping.translate();
/* 3937 */       checkConflicts(this.translated.toList());
/* 3938 */       checkAccessConstructorTags();
/* 3939 */       listBuffer = this.translated;
/*      */     } finally {
/*      */
/* 3942 */       this.attrEnv = null;
/* 3943 */       this.make = null;
/* 3944 */       this.endPosTable = null;
/* 3945 */       this.currentClass = null;
/* 3946 */       this.currentMethodDef = null;
/* 3947 */       this.outermostClassDef = null;
/* 3948 */       this.outermostMemberDef = null;
/* 3949 */       this.translated = null;
/* 3950 */       this.classdefs = null;
/* 3951 */       this.actualSymbols = null;
/* 3952 */       this.freevarCache = null;
/* 3953 */       this.proxies = null;
/* 3954 */       this.outerThisStack = null;
/* 3955 */       this.accessNums = null;
/* 3956 */       this.accessSyms = null;
/* 3957 */       this.accessConstrs = null;
/* 3958 */       this.accessConstrTags = null;
/* 3959 */       this.accessed = null;
/* 3960 */       this.enumSwitchMap.clear();
/* 3961 */       this.assertionsDisabledClassCache = null;
/*      */     }
/* 3963 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   static interface TreeBuilder {
/*      */     JCTree build(JCTree param1JCTree);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Lower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
