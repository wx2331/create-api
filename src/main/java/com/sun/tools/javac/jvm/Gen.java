/*      */ package com.sun.tools.javac.jvm;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Check;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.comp.Flow;
/*      */ import com.sun.tools.javac.comp.Lower;
/*      */ import com.sun.tools.javac.comp.Resolve;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.model.FilteredMemberList;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import javax.lang.model.element.ElementKind;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Gen
/*      */   extends JCTree.Visitor
/*      */ {
/*   63 */   protected static final Context.Key<Gen> genKey = new Context.Key(); private final Log log; private final Symtab syms; private final Check chk; private final Resolve rs; private final TreeMaker make; private final Names names; private final Target target;
/*      */   private final Type stringBufferType;
/*      */   private final Map<Type, Symbol> stringBufferAppend;
/*      */   private Name accessDollar;
/*      */   private final Types types;
/*      */   private final Lower lower;
/*      */   private final Flow flow;
/*      */   private final boolean allowGenerics;
/*      */   private final boolean generateIproxies;
/*      */   private final Code.StackMapFormat stackMap;
/*      */   private final Type methodType;
/*      */   private Pool pool;
/*      */   private final boolean typeAnnoAsserts;
/*      */   private final boolean lineDebugInfo;
/*      */   private final boolean varDebugInfo;
/*      */   private final boolean genCrt;
/*      */   private final boolean debugCode;
/*      */   private final boolean allowInvokedynamic;
/*      */   private final int jsrlimit;
/*      */   private boolean useJsrLocally;
/*      */   private Code code;
/*      */   private Items items;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private JCTree.JCCompilationUnit toplevel;
/*      */   private int nerrs;
/*      */   EndPosTable endPosTable;
/*      */   Env<GenContext> env;
/*      */   Type pt;
/*      */   Items.Item result;
/*      */   private ClassReferenceVisitor classReferenceVisitor;
/*      */
/*      */   public static Gen instance(Context paramContext) {
/*   95 */     Gen gen = (Gen)paramContext.get(genKey);
/*   96 */     if (gen == null)
/*   97 */       gen = new Gen(paramContext);
/*   98 */     return gen;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void loadIntConst(int paramInt) {
/*      */     this.items.makeImmediateItem((Type)this.syms.intType, Integer.valueOf(paramInt)).load();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static int zero(int paramInt) {
/*      */     switch (paramInt) {
/*      */       case 0:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */         return 3;
/*      */       case 1:
/*      */         return 9;
/*      */       case 2:
/*      */         return 11;
/*      */       case 3:
/*      */         return 14;
/*      */     }
/*      */     throw new AssertionError("zero");
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static int one(int paramInt) {
/*      */     return zero(paramInt) + 1;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void emitMinusOne(int paramInt) {
/*      */     if (paramInt == 1) {
/*      */       this.items.makeImmediateItem((Type)this.syms.longType, new Long(-1L)).load();
/*      */     } else {
/*      */       this.code.emitop0(2);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   Symbol binaryQualifier(Symbol paramSymbol, Type paramType) {
/*      */     if (paramType.hasTag(TypeTag.ARRAY)) {
/*      */       if (paramSymbol == this.syms.lengthVar || paramSymbol.owner != this.syms.arrayClass) {
/*      */         return paramSymbol;
/*      */       }
/*      */       Symbol symbol = (Symbol)(this.target.arrayBinaryCompatibility() ? new Symbol.ClassSymbol(1L, paramType.tsym.name, paramType, (Symbol)this.syms.noSymbol) : this.syms.objectType.tsym);
/*      */       return paramSymbol.clone(symbol);
/*      */     }
/*      */     if (paramSymbol.owner == paramType.tsym || (paramSymbol.flags() & 0x1008L) == 4104L) {
/*      */       return paramSymbol;
/*      */     }
/*      */     if (!this.target.obeyBinaryCompatibility()) {
/*      */       return this.rs.isAccessible(this.attrEnv, (Symbol.TypeSymbol)paramSymbol.owner) ? paramSymbol : paramSymbol.clone((Symbol)paramType.tsym);
/*      */     }
/*      */     if (!this.target.interfaceFieldsBinaryCompatibility() && (paramSymbol.owner.flags() & 0x200L) != 0L && paramSymbol.kind == 4) {
/*      */       return paramSymbol;
/*      */     }
/*      */     if (paramSymbol.owner == this.syms.objectType.tsym) {
/*      */       return paramSymbol;
/*      */     }
/*      */     if (!this.target.interfaceObjectOverridesBinaryCompatibility() && (paramSymbol.owner.flags() & 0x200L) != 0L && (this.syms.objectType.tsym.members().lookup(paramSymbol.name)).scope != null) {
/*      */       return paramSymbol;
/*      */     }
/*      */     return paramSymbol.clone((Symbol)paramType.tsym);
/*      */   }
/*      */
/*      */
/*      */
/*      */   int makeRef(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/*      */     checkDimension(paramDiagnosticPosition, paramType);
/*      */     if (paramType.isAnnotated()) {
/*      */       return this.pool.put(paramType);
/*      */     }
/*      */     return this.pool.put(paramType.hasTag(TypeTag.CLASS) ? paramType.tsym : paramType);
/*      */   }
/*      */
/*      */
/*      */   private void checkDimension(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/*      */     List list;
/*      */     switch (paramType.getTag()) {
/*      */       case BLOCK:
/*      */         checkDimension(paramDiagnosticPosition, paramType.getReturnType());
/*      */         for (list = paramType.getParameterTypes(); list.nonEmpty(); list = list.tail) {
/*      */           checkDimension(paramDiagnosticPosition, (Type)list.head);
/*      */         }
/*      */         break;
/*      */       case METHODDEF:
/*      */         if (this.types.dimensions(paramType) > 255) {
/*      */           this.log.error(paramDiagnosticPosition, "limit.dimensions", new Object[0]);
/*      */           this.nerrs++;
/*      */         }
/*      */         break;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   Items.LocalItem makeTemp(Type paramType) {
/*      */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(4096L, this.names.empty, paramType, (Symbol)this.env.enclMethod.sym);
/*      */     this.code.newLocal(varSymbol);
/*      */     return this.items.makeLocalItem(varSymbol);
/*      */   }
/*      */
/*      */
/*      */   protected Gen(Context paramContext) {
/*  208 */     this.nerrs = 0;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  930 */     this.classReferenceVisitor = new ClassReferenceVisitor(); paramContext.put(genKey, this); this.names = Names.instance(paramContext); this.log = Log.instance(paramContext); this.syms = Symtab.instance(paramContext); this.chk = Check.instance(paramContext); this.rs = Resolve.instance(paramContext); this.make = TreeMaker.instance(paramContext); this.target = Target.instance(paramContext); this.types = Types.instance(paramContext); this.methodType = (Type)new Type.MethodType(null, null, null, (Symbol.TypeSymbol)this.syms.methodClass); this.allowGenerics = Source.instance(paramContext).allowGenerics(); this.stringBufferType = this.target.useStringBuilder() ? this.syms.stringBuilderType : this.syms.stringBufferType; this.stringBufferAppend = new HashMap<>(); this.accessDollar = this.names.fromString("access" + this.target.syntheticNameChar()); this.flow = Flow.instance(paramContext); this.lower = Lower.instance(paramContext); Options options = Options.instance(paramContext); this.lineDebugInfo = (options.isUnset(Option.G_CUSTOM) || options.isSet(Option.G_CUSTOM, "lines")); this.varDebugInfo = options.isUnset(Option.G_CUSTOM) ? options.isSet(Option.G) : options.isSet(Option.G_CUSTOM, "vars"); this.genCrt = options.isSet(Option.XJCOV); this.debugCode = options.isSet("debugcode"); this.allowInvokedynamic = (this.target.hasInvokedynamic() || options.isSet("invokedynamic")); this.pool = new Pool(this.types); this.typeAnnoAsserts = options.isSet("TypeAnnotationAsserts"); this.generateIproxies = (this.target.requiresIproxy() || options.isSet("miranda")); if (this.target.generateStackMapTable()) { this.stackMap = Code.StackMapFormat.JSR202; } else if (this.target.generateCLDCStackmap()) { this.stackMap = Code.StackMapFormat.CLDC; } else { this.stackMap = Code.StackMapFormat.NONE; }  int i = 50; String str = options.get("jsrlimit"); if (str != null) try { i = Integer.parseInt(str); } catch (NumberFormatException numberFormatException) {}  this.jsrlimit = i; this.useJsrLocally = false;
/*      */   }
/*      */   void callMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName, List<Type> paramList, boolean paramBoolean) { Symbol.MethodSymbol methodSymbol = this.rs.resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName, paramList, null); if (paramBoolean) { this.items.makeStaticItem((Symbol)methodSymbol).invoke(); } else { this.items.makeMemberItem((Symbol)methodSymbol, (paramName == this.names.init)).invoke(); }  }
/*      */   private boolean isAccessSuper(JCTree.JCMethodDecl paramJCMethodDecl) { return ((paramJCMethodDecl.mods.flags & 0x1000L) != 0L && isOddAccessName(paramJCMethodDecl.name)); }
/*      */   private boolean isOddAccessName(Name paramName) { return (paramName.startsWith(this.accessDollar) && (paramName.getByteAt(paramName.getByteLength() - 1) & 0x1) == 1); }
/*      */   void genFinalizer(Env<GenContext> paramEnv) { if (this.code.isAlive() && ((GenContext)paramEnv.info).finalize != null) ((GenContext)paramEnv.info).finalize.gen();  }
/*      */   Env<GenContext> unwind(JCTree paramJCTree, Env<GenContext> paramEnv) { Env<GenContext> env = paramEnv; while (true) { genFinalizer(env); if (env.tree == paramJCTree) break;  env = env.next; }  return env; }
/*      */   void endFinalizerGap(Env<GenContext> paramEnv) { if (((GenContext)paramEnv.info).gaps != null && ((GenContext)paramEnv.info).gaps.length() % 2 == 1) ((GenContext)paramEnv.info).gaps.append(Integer.valueOf(this.code.curCP()));  }
/*  938 */   void endFinalizerGaps(Env<GenContext> paramEnv1, Env<GenContext> paramEnv2) { Env<GenContext> env = null; while (env != paramEnv2) { endFinalizerGap(paramEnv1); env = paramEnv1; paramEnv1 = paramEnv1.next; }  } boolean hasFinally(JCTree paramJCTree, Env<GenContext> paramEnv) { while (paramEnv.tree != paramJCTree) { if (paramEnv.tree.hasTag(JCTree.Tag.TRY) && ((GenContext)paramEnv.info).finalize.hasFinalizer()) return true;  paramEnv = paramEnv.next; }  return false; } List<JCTree> normalizeDefs(List<JCTree> paramList, Symbol.ClassSymbol paramClassSymbol) { ListBuffer listBuffer1 = new ListBuffer(); ListBuffer listBuffer2 = new ListBuffer(); ListBuffer listBuffer3 = new ListBuffer(); ListBuffer listBuffer4 = new ListBuffer(); ListBuffer listBuffer5 = new ListBuffer(); List<JCTree> list; for (list = paramList; list.nonEmpty(); list = list.tail) { JCTree.JCBlock jCBlock; JCTree.JCVariableDecl jCVariableDecl; Symbol.VarSymbol varSymbol; JCTree jCTree = (JCTree)list.head; switch (jCTree.getTag()) { case BLOCK: jCBlock = (JCTree.JCBlock)jCTree; if ((jCBlock.flags & 0x8L) != 0L) { listBuffer3.append(jCBlock); break; }  if ((jCBlock.flags & 0x1000L) == 0L) listBuffer1.append(jCBlock);  break;case METHODDEF: listBuffer5.append(jCTree); break;case VARDEF: jCVariableDecl = (JCTree.JCVariableDecl)jCTree; varSymbol = jCVariableDecl.sym; checkDimension(jCVariableDecl.pos(), varSymbol.type); if (jCVariableDecl.init != null) { if ((varSymbol.flags() & 0x8L) == 0L) { JCTree.JCStatement jCStatement = this.make.at(jCVariableDecl.pos()).Assignment((Symbol)varSymbol, jCVariableDecl.init); listBuffer1.append(jCStatement); this.endPosTable.replaceTree((JCTree)jCVariableDecl, (JCTree)jCStatement); listBuffer2.addAll((Collection)getAndRemoveNonFieldTAs(varSymbol)); break; }  if (varSymbol.getConstValue() == null) { JCTree.JCStatement jCStatement = this.make.at(jCVariableDecl.pos).Assignment((Symbol)varSymbol, jCVariableDecl.init); listBuffer3.append(jCStatement); this.endPosTable.replaceTree((JCTree)jCVariableDecl, (JCTree)jCStatement); listBuffer4.addAll((Collection)getAndRemoveNonFieldTAs(varSymbol)); break; }  checkStringConstant(jCVariableDecl.init.pos(), varSymbol.getConstValue()); jCVariableDecl.init.accept(this.classReferenceVisitor); }  break;default: Assert.error(); break; }  }  if (listBuffer1.length() != 0) { list = listBuffer1.toList(); listBuffer2.addAll((Collection)paramClassSymbol.getInitTypeAttributes()); List<Attribute.TypeCompound> list1 = listBuffer2.toList(); for (JCTree jCTree : listBuffer5) normalizeMethod((JCTree.JCMethodDecl)jCTree, (List)list, list1);  }  if (listBuffer3.length() != 0) { Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(0x8L | paramClassSymbol.flags() & 0x800L, this.names.clinit, (Type)new Type.MethodType(List.nil(), (Type)this.syms.voidType, List.nil(), (Symbol.TypeSymbol)this.syms.methodClass), (Symbol)paramClassSymbol); paramClassSymbol.members().enter((Symbol)methodSymbol); List list1 = listBuffer3.toList(); JCTree.JCBlock jCBlock = this.make.at(((JCTree.JCStatement)list1.head).pos()).Block(0L, list1); jCBlock.endpos = TreeInfo.endPos((JCTree)list1.last()); listBuffer5.append(this.make.MethodDef(methodSymbol, jCBlock)); if (!listBuffer4.isEmpty()) methodSymbol.appendUniqueTypeAttributes(listBuffer4.toList());  if (!paramClassSymbol.getClassInitTypeAttributes().isEmpty()) methodSymbol.appendUniqueTypeAttributes(paramClassSymbol.getClassInitTypeAttributes());  }  return listBuffer5.toList(); } private List<Attribute.TypeCompound> getAndRemoveNonFieldTAs(Symbol.VarSymbol paramVarSymbol) { List list = paramVarSymbol.getRawTypeAttributes(); ListBuffer listBuffer1 = new ListBuffer(); ListBuffer listBuffer2 = new ListBuffer(); for (Attribute.TypeCompound typeCompound : list) { if ((typeCompound.getPosition()).type == TargetType.FIELD) { listBuffer1.add(typeCompound); continue; }  if (this.typeAnnoAsserts) Assert.error("Type annotation does not have a valid positior");  listBuffer2.add(typeCompound); }  paramVarSymbol.setTypeAttributes(listBuffer1.toList()); return listBuffer2.toList(); } private void checkStringConstant(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Object paramObject) { if (this.nerrs != 0 || paramObject == null || !(paramObject instanceof String) || ((String)paramObject).length() < 65535) return;  this.log.error(paramDiagnosticPosition, "limit.string", new Object[0]); this.nerrs++; } void normalizeMethod(JCTree.JCMethodDecl paramJCMethodDecl, List<JCTree.JCStatement> paramList, List<Attribute.TypeCompound> paramList1) { if (paramJCMethodDecl.name == this.names.init && TreeInfo.isInitialConstructor((JCTree)paramJCMethodDecl)) { List list = paramJCMethodDecl.body.stats; ListBuffer listBuffer = new ListBuffer(); if (list.nonEmpty()) { while (TreeInfo.isSyntheticInit((JCTree)list.head)) { listBuffer.append(list.head); list = list.tail; }  listBuffer.append(list.head); list = list.tail; while (list.nonEmpty() && TreeInfo.isSyntheticInit((JCTree)list.head)) { listBuffer.append(list.head); list = list.tail; }  listBuffer.appendList(paramList); while (list.nonEmpty()) { listBuffer.append(list.head); list = list.tail; }  }  paramJCMethodDecl.body.stats = listBuffer.toList(); if (paramJCMethodDecl.body.endpos == -1) paramJCMethodDecl.body.endpos = TreeInfo.endPos((JCTree)paramJCMethodDecl.body.stats.last());  paramJCMethodDecl.sym.appendUniqueTypeAttributes(paramList1); }  } public Items.Item genExpr(JCTree paramJCTree, Type paramType) { Type type = this.pt;
/*      */     try {
/*  940 */       if (paramJCTree.type.constValue() != null) {
/*      */
/*  942 */         paramJCTree.accept(this.classReferenceVisitor);
/*  943 */         checkStringConstant(paramJCTree.pos(), paramJCTree.type.constValue());
/*  944 */         this.result = this.items.makeImmediateItem(paramJCTree.type, paramJCTree.type.constValue());
/*      */       } else {
/*  946 */         this.pt = paramType;
/*  947 */         paramJCTree.accept(this);
/*      */       }
/*  949 */       return this.result.coerce(paramType);
/*  950 */     } catch (Symbol.CompletionFailure completionFailure) {
/*  951 */       this.chk.completionError(paramJCTree.pos(), completionFailure);
/*  952 */       this.code.state.stacksize = 1;
/*  953 */       return this.items.makeStackItem(paramType);
/*      */     } finally {
/*  955 */       this.pt = type;
/*      */     }  }
/*      */   void implementInterfaceMethods(Symbol.ClassSymbol paramClassSymbol) { implementInterfaceMethods(paramClassSymbol, paramClassSymbol); }
/*      */   void implementInterfaceMethods(Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2) { for (List list = this.types.interfaces(paramClassSymbol1.type); list.nonEmpty(); list = list.tail) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)((Type)list.head).tsym; Scope.Entry entry = (classSymbol.members()).elems; for (; entry != null; entry = entry.sibling) { if (entry.sym.kind == 16 && (entry.sym.flags() & 0x8L) == 0L) { Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)entry.sym; Symbol.MethodSymbol methodSymbol2 = methodSymbol1.binaryImplementation(paramClassSymbol2, this.types); if (methodSymbol2 == null) { addAbstractMethod(paramClassSymbol2, methodSymbol1); }
/*      */           else if ((methodSymbol2.flags() & 0x200000L) != 0L) { adjustAbstractMethod(paramClassSymbol2, methodSymbol2, methodSymbol1); }
/*      */            }
/*      */          }
/*      */        implementInterfaceMethods(classSymbol, paramClassSymbol2); }
/*      */      }
/*      */   private void addAbstractMethod(Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol) { Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(paramMethodSymbol.flags() | 0x200000L | 0x1000L, paramMethodSymbol.name, paramMethodSymbol.type, (Symbol)paramClassSymbol); paramClassSymbol.members().enter((Symbol)methodSymbol); }
/*  965 */   private void adjustAbstractMethod(Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2) { Type.MethodType methodType = (Type.MethodType)paramMethodSymbol1.type; Type type = this.types.memberType(paramClassSymbol.type, (Symbol)paramMethodSymbol2); methodType.thrown = this.chk.intersect(methodType.getThrownTypes(), type.getThrownTypes()); } public void genArgs(List<JCTree.JCExpression> paramList, List<Type> paramList1) { for (List<JCTree.JCExpression> list = paramList; list.nonEmpty(); list = list.tail) {
/*  966 */       genExpr((JCTree)list.head, (Type)paramList1.head).load();
/*  967 */       paramList1 = paramList1.tail;
/*      */     }
/*      */
/*  970 */     Assert.check(paramList1.isEmpty()); } public void genDef(JCTree paramJCTree, Env<GenContext> paramEnv) { Env<GenContext> env = this.env; try { this.env = paramEnv; paramJCTree.accept(this); } catch (Symbol.CompletionFailure completionFailure) { this.chk.completionError(paramJCTree.pos(), completionFailure); } finally { this.env = env; }  } public void genStat(JCTree paramJCTree, Env<GenContext> paramEnv, int paramInt) { if (!this.genCrt) { genStat(paramJCTree, paramEnv); return; }  int i = this.code.curCP(); genStat(paramJCTree, paramEnv); if (paramJCTree.hasTag(JCTree.Tag.BLOCK)) paramInt |= 0x2;  this.code.crt.put(paramJCTree, paramInt, i, this.code.curCP()); } public void genStat(JCTree paramJCTree, Env<GenContext> paramEnv) { if (this.code.isAlive()) { this.code.statBegin(paramJCTree.pos); genDef(paramJCTree, paramEnv); } else if (((GenContext)paramEnv.info).isSwitch && paramJCTree.hasTag(JCTree.Tag.VARDEF)) { this.code.newLocal(((JCTree.JCVariableDecl)paramJCTree).sym); }  }
/*      */   public void genStats(List<JCTree.JCStatement> paramList, Env<GenContext> paramEnv, int paramInt) { if (!this.genCrt) { genStats((List)paramList, paramEnv); return; }  if (paramList.length() == 1) { genStat((JCTree)paramList.head, paramEnv, paramInt | 0x1); } else { int i = this.code.curCP(); genStats((List)paramList, paramEnv); this.code.crt.put(paramList, paramInt, i, this.code.curCP()); }  }
/*      */   public void genStats(List<? extends JCTree> paramList, Env<GenContext> paramEnv) { for (List<? extends JCTree> list = paramList; list.nonEmpty(); list = list.tail) genStat((JCTree)list.head, paramEnv, 1);  }
/*      */   public Items.CondItem genCond(JCTree paramJCTree, int paramInt) { if (!this.genCrt) return genCond(paramJCTree, false);  int i = this.code.curCP(); Items.CondItem condItem = genCond(paramJCTree, ((paramInt & 0x8) != 0)); this.code.crt.put(paramJCTree, paramInt, i, this.code.curCP()); return condItem; }
/*      */   public Items.CondItem genCond(JCTree paramJCTree, boolean paramBoolean) { JCTree jCTree = TreeInfo.skipParens(paramJCTree); if (jCTree.hasTag(JCTree.Tag.CONDEXPR)) { JCTree.JCConditional jCConditional = (JCTree.JCConditional)jCTree; Items.CondItem condItem1 = genCond((JCTree)jCConditional.cond, 8); if (condItem1.isTrue()) { this.code.resolve(condItem1.trueJumps); Items.CondItem condItem5 = genCond((JCTree)jCConditional.truepart, 16); if (paramBoolean) condItem5.tree = (JCTree)jCConditional.truepart;  return condItem5; }  if (condItem1.isFalse()) { this.code.resolve(condItem1.falseJumps); Items.CondItem condItem5 = genCond((JCTree)jCConditional.falsepart, 16); if (paramBoolean) condItem5.tree = (JCTree)jCConditional.falsepart;  return condItem5; }  Code.Chain chain1 = condItem1.jumpFalse(); this.code.resolve(condItem1.trueJumps); Items.CondItem condItem2 = genCond((JCTree)jCConditional.truepart, 16); if (paramBoolean) condItem2.tree = (JCTree)jCConditional.truepart;  Code.Chain chain2 = condItem2.jumpFalse(); this.code.resolve(condItem2.trueJumps); Code.Chain chain3 = this.code.branch(167); this.code.resolve(chain1); Items.CondItem condItem3 = genCond((JCTree)jCConditional.falsepart, 16); Items.CondItem condItem4 = this.items.makeCondItem(condItem3.opcode, Code.mergeChains(chain3, condItem3.trueJumps), Code.mergeChains(chain2, condItem3.falseJumps)); if (paramBoolean) condItem4.tree = (JCTree)jCConditional.falsepart;  return condItem4; }  Items.CondItem condItem = genExpr(paramJCTree, (Type)this.syms.booleanType).mkCond(); if (paramBoolean) condItem.tree = paramJCTree;  return condItem; }
/*      */   class ClassReferenceVisitor extends JCTree.Visitor {
/*      */     public void visitTree(JCTree param1JCTree) {}
/*      */     public void visitBinary(JCTree.JCBinary param1JCBinary) { param1JCBinary.lhs.accept(this); param1JCBinary.rhs.accept(this); }
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) { if (param1JCFieldAccess.selected.type.hasTag(TypeTag.CLASS)) Gen.this.makeRef(param1JCFieldAccess.selected.pos(), param1JCFieldAccess.selected.type);  }
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) { if (param1JCIdent.sym.owner instanceof Symbol.ClassSymbol)
/*      */         Gen.this.pool.put(param1JCIdent.sym.owner);  }
/*      */     public void visitConditional(JCTree.JCConditional param1JCConditional) { param1JCConditional.cond.accept(this); param1JCConditional.truepart.accept(this); param1JCConditional.falsepart.accept(this); }
/*      */     public void visitUnary(JCTree.JCUnary param1JCUnary) { param1JCUnary.arg.accept(this); }
/*      */     public void visitParens(JCTree.JCParens param1JCParens) { param1JCParens.expr.accept(this); }
/*      */     public void visitTypeCast(JCTree.JCTypeCast param1JCTypeCast) { param1JCTypeCast.expr.accept(this); } }
/*      */   public static class CodeSizeOverflow extends RuntimeException {
/*      */     private static final long serialVersionUID = 0L; }
/*  987 */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) { Env<GenContext> env = this.env.dup((JCTree)paramJCMethodDecl);
/*  988 */     env.enclMethod = paramJCMethodDecl;
/*      */
/*      */
/*  991 */     this.pt = paramJCMethodDecl.sym.erasure(this.types).getReturnType();
/*      */
/*  993 */     checkDimension(paramJCMethodDecl.pos(), paramJCMethodDecl.sym.erasure(this.types));
/*  994 */     genMethod(paramJCMethodDecl, env, false); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void genMethod(JCTree.JCMethodDecl paramJCMethodDecl, Env<GenContext> paramEnv, boolean paramBoolean) {
/* 1007 */     Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym;
/* 1008 */     byte b = 0;
/*      */
/* 1010 */     if (methodSymbol.isConstructor()) {
/* 1011 */       b++;
/* 1012 */       if (methodSymbol.enclClass().isInner() &&
/* 1013 */         !methodSymbol.enclClass().isStatic()) {
/* 1014 */         b++;
/*      */       }
/* 1016 */     } else if ((paramJCMethodDecl.mods.flags & 0x8L) == 0L) {
/* 1017 */       b++;
/*      */     }
/*      */
/* 1020 */     if (Code.width(this.types.erasure(paramEnv.enclMethod.sym.type).getParameterTypes()) + b > 255) {
/*      */
/* 1022 */       this.log.error(paramJCMethodDecl.pos(), "limit.parameters", new Object[0]);
/* 1023 */       this.nerrs++;
/*      */
/*      */     }
/* 1026 */     else if (paramJCMethodDecl.body != null) {
/*      */
/* 1028 */       int i = initCode(paramJCMethodDecl, paramEnv, paramBoolean);
/*      */
/*      */       try {
/* 1031 */         genStat((JCTree)paramJCMethodDecl.body, paramEnv);
/* 1032 */       } catch (CodeSizeOverflow codeSizeOverflow) {
/*      */
/* 1034 */         i = initCode(paramJCMethodDecl, paramEnv, paramBoolean);
/* 1035 */         genStat((JCTree)paramJCMethodDecl.body, paramEnv);
/*      */       }
/*      */
/* 1038 */       if (this.code.state.stacksize != 0) {
/* 1039 */         this.log.error(paramJCMethodDecl.body.pos(), "stack.sim.error", new Object[] { paramJCMethodDecl });
/* 1040 */         throw new AssertionError();
/*      */       }
/*      */
/*      */
/*      */
/* 1045 */       if (this.code.isAlive()) {
/* 1046 */         this.code.statBegin(TreeInfo.endPos((JCTree)paramJCMethodDecl.body));
/* 1047 */         if (paramEnv.enclMethod == null || paramEnv.enclMethod.sym.type
/* 1048 */           .getReturnType().hasTag(TypeTag.VOID)) {
/* 1049 */           this.code.emitop0(177);
/*      */         }
/*      */         else {
/*      */
/* 1053 */           int j = this.code.entryPoint();
/* 1054 */           Items.CondItem condItem = this.items.makeCondItem(167);
/* 1055 */           this.code.resolve(condItem.jumpTrue(), j);
/*      */         }
/*      */       }
/* 1058 */       if (this.genCrt) {
/* 1059 */         this.code.crt.put(paramJCMethodDecl.body, 2, i, this.code
/*      */
/*      */
/* 1062 */             .curCP());
/*      */       }
/* 1064 */       this.code.endScopes(0);
/*      */
/*      */
/* 1067 */       if (this.code.checkLimits(paramJCMethodDecl.pos(), this.log)) {
/* 1068 */         this.nerrs++;
/*      */
/*      */
/*      */         return;
/*      */       }
/*      */
/* 1074 */       if (!paramBoolean && this.code.fatcode) genMethod(paramJCMethodDecl, paramEnv, true);
/*      */
/*      */
/* 1077 */       if (this.stackMap == Code.StackMapFormat.JSR202) {
/* 1078 */         this.code.lastFrame = null;
/* 1079 */         this.code.frameBeforeLast = null;
/*      */       }
/*      */
/*      */
/* 1083 */       this.code.compressCatchTable();
/*      */
/*      */
/* 1086 */       this.code.fillExceptionParameterPositions();
/*      */     }
/*      */   }
/*      */
/*      */   private int initCode(JCTree.JCMethodDecl paramJCMethodDecl, Env<GenContext> paramEnv, boolean paramBoolean) {
/* 1091 */     Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym;
/*      */
/*      */
/* 1094 */     methodSymbol.code = this.code = new Code(methodSymbol, paramBoolean, this.lineDebugInfo ? this.toplevel.lineMap : null, this.varDebugInfo, this.stackMap, this.debugCode, this.genCrt ? new CRTable(paramJCMethodDecl, paramEnv.toplevel.endPositions) : null, this.syms, this.types, this.pool);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1105 */     this.items = new Items(this.pool, this.code, this.syms, this.types);
/* 1106 */     if (this.code.debugCode) {
/* 1107 */       System.err.println(methodSymbol + " for body " + paramJCMethodDecl);
/*      */     }
/*      */
/*      */
/*      */
/* 1112 */     if ((paramJCMethodDecl.mods.flags & 0x8L) == 0L) {
/* 1113 */       UninitializedType uninitializedType; Type type = methodSymbol.owner.type;
/* 1114 */       if (methodSymbol.isConstructor() && type != this.syms.objectType)
/* 1115 */         uninitializedType = UninitializedType.uninitializedThis(type);
/* 1116 */       this.code.setDefined(this.code
/* 1117 */           .newLocal(new Symbol.VarSymbol(16L, this.names._this, (Type)uninitializedType, methodSymbol.owner)));
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1123 */     for (List list = paramJCMethodDecl.params; list.nonEmpty(); list = list.tail) {
/* 1124 */       checkDimension(((JCTree.JCVariableDecl)list.head).pos(), ((JCTree.JCVariableDecl)list.head).sym.type);
/* 1125 */       this.code.setDefined(this.code.newLocal(((JCTree.JCVariableDecl)list.head).sym));
/*      */     }
/*      */
/*      */
/* 1129 */     boolean bool = this.genCrt ? this.code.curCP() : false;
/* 1130 */     this.code.entryPoint();
/*      */
/*      */
/* 1133 */     this.code.pendingStackMap = false;
/*      */
/* 1135 */     return bool;
/*      */   }
/*      */
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 1139 */     Symbol.VarSymbol varSymbol = paramJCVariableDecl.sym;
/* 1140 */     this.code.newLocal(varSymbol);
/* 1141 */     if (paramJCVariableDecl.init != null) {
/* 1142 */       checkStringConstant(paramJCVariableDecl.init.pos(), varSymbol.getConstValue());
/* 1143 */       if (varSymbol.getConstValue() == null || this.varDebugInfo) {
/* 1144 */         genExpr((JCTree)paramJCVariableDecl.init, varSymbol.erasure(this.types)).load();
/* 1145 */         this.items.makeLocalItem(varSymbol).store();
/*      */       }
/*      */     }
/* 1148 */     checkDimension(paramJCVariableDecl.pos(), varSymbol.type);
/*      */   }
/*      */
/*      */
/*      */   public void visitSkip(JCTree.JCSkip paramJCSkip) {}
/*      */
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 1155 */     int i = this.code.nextreg;
/* 1156 */     Env<GenContext> env = this.env.dup((JCTree)paramJCBlock, new GenContext());
/* 1157 */     genStats(paramJCBlock.stats, env);
/*      */
/* 1159 */     if (!this.env.tree.hasTag(JCTree.Tag.METHODDEF)) {
/* 1160 */       this.code.statBegin(paramJCBlock.endpos);
/* 1161 */       this.code.endScopes(i);
/* 1162 */       this.code.pendingStatPos = -1;
/*      */     }
/*      */   }
/*      */
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 1167 */     genLoop((JCTree.JCStatement)paramJCDoWhileLoop, paramJCDoWhileLoop.body, paramJCDoWhileLoop.cond, List.nil(), false);
/*      */   }
/*      */
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 1171 */     genLoop((JCTree.JCStatement)paramJCWhileLoop, paramJCWhileLoop.body, paramJCWhileLoop.cond, List.nil(), true);
/*      */   }
/*      */
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 1175 */     int i = this.code.nextreg;
/* 1176 */     genStats(paramJCForLoop.init, this.env);
/* 1177 */     genLoop((JCTree.JCStatement)paramJCForLoop, paramJCForLoop.body, paramJCForLoop.cond, paramJCForLoop.step, true);
/* 1178 */     this.code.endScopes(i);
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
/*      */   private void genLoop(JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpressionStatement> paramList, boolean paramBoolean) {
/* 1194 */     Env<GenContext> env = this.env.dup((JCTree)paramJCStatement1, new GenContext());
/* 1195 */     int i = this.code.entryPoint();
/* 1196 */     if (paramBoolean) {
/*      */       Items.CondItem condItem;
/* 1198 */       if (paramJCExpression != null) {
/* 1199 */         this.code.statBegin(paramJCExpression.pos);
/* 1200 */         condItem = genCond((JCTree)TreeInfo.skipParens(paramJCExpression), 8);
/*      */       } else {
/* 1202 */         condItem = this.items.makeCondItem(167);
/*      */       }
/* 1204 */       Code.Chain chain = condItem.jumpFalse();
/* 1205 */       this.code.resolve(condItem.trueJumps);
/* 1206 */       genStat((JCTree)paramJCStatement2, env, 17);
/* 1207 */       this.code.resolve(((GenContext)env.info).cont);
/* 1208 */       genStats((List)paramList, env);
/* 1209 */       this.code.resolve(this.code.branch(167), i);
/* 1210 */       this.code.resolve(chain);
/*      */     } else {
/* 1212 */       Items.CondItem condItem; genStat((JCTree)paramJCStatement2, env, 17);
/* 1213 */       this.code.resolve(((GenContext)env.info).cont);
/* 1214 */       genStats((List)paramList, env);
/*      */
/* 1216 */       if (paramJCExpression != null) {
/* 1217 */         this.code.statBegin(paramJCExpression.pos);
/* 1218 */         condItem = genCond((JCTree)TreeInfo.skipParens(paramJCExpression), 8);
/*      */       } else {
/* 1220 */         condItem = this.items.makeCondItem(167);
/*      */       }
/* 1222 */       this.code.resolve(condItem.jumpTrue(), i);
/* 1223 */       this.code.resolve(condItem.falseJumps);
/*      */     }
/* 1225 */     this.code.resolve(((GenContext)env.info).exit);
/* 1226 */     if (((GenContext)env.info).exit != null) {
/* 1227 */       ((GenContext)env.info).exit.state.defined.excludeFrom(this.code.nextreg);
/*      */     }
/*      */   }
/*      */
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 1232 */     throw new AssertionError();
/*      */   }
/*      */
/*      */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 1236 */     Env<GenContext> env = this.env.dup((JCTree)paramJCLabeledStatement, new GenContext());
/* 1237 */     genStat((JCTree)paramJCLabeledStatement.body, env, 1);
/* 1238 */     this.code.resolve(((GenContext)env.info).exit);
/*      */   }
/*      */
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 1242 */     int i = this.code.nextreg;
/* 1243 */     Assert.check(!paramJCSwitch.selector.type.hasTag(TypeTag.CLASS));
/* 1244 */     boolean bool = this.genCrt ? this.code.curCP() : false;
/* 1245 */     Items.Item item = genExpr((JCTree)paramJCSwitch.selector, (Type)this.syms.intType);
/* 1246 */     List list = paramJCSwitch.cases;
/* 1247 */     if (list.isEmpty()) {
/*      */
/* 1249 */       item.load().drop();
/* 1250 */       if (this.genCrt) {
/* 1251 */         this.code.crt.put(TreeInfo.skipParens(paramJCSwitch.selector), 8, bool, this.code
/* 1252 */             .curCP());
/*      */       }
/*      */     } else {
/* 1255 */       item.load();
/* 1256 */       if (this.genCrt)
/* 1257 */         this.code.crt.put(TreeInfo.skipParens(paramJCSwitch.selector), 8, bool, this.code
/* 1258 */             .curCP());
/* 1259 */       Env<GenContext> env = this.env.dup((JCTree)paramJCSwitch, new GenContext());
/* 1260 */       ((GenContext)env.info).isSwitch = true;
/*      */
/*      */
/*      */
/* 1264 */       int j = Integer.MAX_VALUE;
/* 1265 */       int k = Integer.MIN_VALUE;
/* 1266 */       byte b1 = 0;
/*      */
/* 1268 */       int[] arrayOfInt1 = new int[list.length()];
/* 1269 */       byte b = -1;
/*      */
/* 1271 */       List list1 = list;
/* 1272 */       for (byte b2 = 0; b2 < arrayOfInt1.length; b2++) {
/* 1273 */         if (((JCTree.JCCase)list1.head).pat != null) {
/* 1274 */           int i2 = ((Number)((JCTree.JCCase)list1.head).pat.type.constValue()).intValue();
/* 1275 */           arrayOfInt1[b2] = i2;
/* 1276 */           if (i2 < j) j = i2;
/* 1277 */           if (k < i2) k = i2;
/* 1278 */           b1++;
/*      */         } else {
/* 1280 */           Assert.check((b == -1));
/* 1281 */           b = b2;
/*      */         }
/* 1283 */         list1 = list1.tail;
/*      */       }
/*      */
/*      */
/*      */
/* 1288 */       long l1 = 4L + k - j + 1L;
/* 1289 */       long l2 = 3L;
/* 1290 */       long l3 = 3L + 2L * b1;
/* 1291 */       long l4 = b1;
/* 1292 */       char c = (b1 > 0 && l1 + 3L * l2 <= l3 + 3L * l4) ? 'ª' : '«';
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1299 */       int m = this.code.curCP();
/* 1300 */       this.code.emitop0(c);
/* 1301 */       this.code.align(4);
/* 1302 */       int n = this.code.curCP();
/* 1303 */       int[] arrayOfInt2 = null;
/* 1304 */       this.code.emit4(-1);
/* 1305 */       if (c == 'ª') {
/* 1306 */         this.code.emit4(j);
/* 1307 */         this.code.emit4(k); long l;
/* 1308 */         for (l = j; l <= k; l++) {
/* 1309 */           this.code.emit4(-1);
/*      */         }
/*      */       } else {
/* 1312 */         this.code.emit4(b1);
/* 1313 */         for (byte b3 = 0; b3 < b1; b3++) {
/* 1314 */           this.code.emit4(-1); this.code.emit4(-1);
/*      */         }
/* 1316 */         arrayOfInt2 = new int[arrayOfInt1.length];
/*      */       }
/* 1318 */       Code.State state = this.code.state.dup();
/* 1319 */       this.code.markDead();
/*      */
/*      */
/* 1322 */       list1 = list; int i1;
/* 1323 */       for (i1 = 0; i1 < arrayOfInt1.length; i1++) {
/* 1324 */         JCTree.JCCase jCCase = (JCTree.JCCase)list1.head;
/* 1325 */         list1 = list1.tail;
/*      */
/* 1327 */         int i2 = this.code.entryPoint(state);
/*      */
/*      */
/* 1330 */         if (i1 != b) {
/* 1331 */           if (c == 'ª') {
/* 1332 */             this.code.put4(n + 4 * (arrayOfInt1[i1] - j + 3), i2 - m);
/*      */           }
/*      */           else {
/*      */
/* 1336 */             arrayOfInt2[i1] = i2 - m;
/*      */           }
/*      */         } else {
/* 1339 */           this.code.put4(n, i2 - m);
/*      */         }
/*      */
/*      */
/* 1343 */         genStats(jCCase.stats, env, 16);
/*      */       }
/*      */
/*      */
/* 1347 */       this.code.resolve(((GenContext)env.info).exit);
/*      */
/*      */
/* 1350 */       if (this.code.get4(n) == -1) {
/* 1351 */         this.code.put4(n, this.code.entryPoint(state) - m);
/*      */       }
/*      */
/* 1354 */       if (c == 'ª') {
/*      */
/* 1356 */         i1 = this.code.get4(n); long l;
/* 1357 */         for (l = j; l <= k; l++) {
/* 1358 */           int i2 = (int)(n + 4L * (l - j + 3L));
/* 1359 */           if (this.code.get4(i2) == -1) {
/* 1360 */             this.code.put4(i2, i1);
/*      */           }
/*      */         }
/*      */       } else {
/* 1364 */         if (b >= 0)
/* 1365 */           for (i1 = b; i1 < arrayOfInt1.length - 1; i1++) {
/* 1366 */             arrayOfInt1[i1] = arrayOfInt1[i1 + 1];
/* 1367 */             arrayOfInt2[i1] = arrayOfInt2[i1 + 1];
/*      */           }
/* 1369 */         if (b1 > 0)
/* 1370 */           qsort2(arrayOfInt1, arrayOfInt2, 0, b1 - 1);
/* 1371 */         for (i1 = 0; i1 < b1; i1++) {
/* 1372 */           int i2 = n + 8 * (i1 + 1);
/* 1373 */           this.code.put4(i2, arrayOfInt1[i1]);
/* 1374 */           this.code.put4(i2 + 4, arrayOfInt2[i1]);
/*      */         }
/*      */       }
/*      */     }
/* 1378 */     this.code.endScopes(i);
/*      */   }
/*      */
/*      */
/*      */   static void qsort2(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2)
/*      */   {
/* 1384 */     int i = paramInt1;
/* 1385 */     int j = paramInt2;
/* 1386 */     int k = paramArrayOfint1[(i + j) / 2];
/*      */     while (true) {
/* 1388 */       for (; paramArrayOfint1[i] < k; i++);
/* 1389 */       for (; k < paramArrayOfint1[j]; j--);
/* 1390 */       if (i <= j) {
/* 1391 */         int m = paramArrayOfint1[i];
/* 1392 */         paramArrayOfint1[i] = paramArrayOfint1[j];
/* 1393 */         paramArrayOfint1[j] = m;
/* 1394 */         int n = paramArrayOfint2[i];
/* 1395 */         paramArrayOfint2[i] = paramArrayOfint2[j];
/* 1396 */         paramArrayOfint2[j] = n;
/* 1397 */         i++;
/* 1398 */         j--;
/*      */       }
/* 1400 */       if (i > j) {
/* 1401 */         if (paramInt1 < j) qsort2(paramArrayOfint1, paramArrayOfint2, paramInt1, j);
/* 1402 */         if (i < paramInt2) qsort2(paramArrayOfint1, paramArrayOfint2, i, paramInt2);
/*      */         return;
/*      */       }
/*      */     }  } public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 1406 */     int i = this.code.nextreg;
/*      */
/* 1408 */     final Items.LocalItem lockVar = makeTemp(this.syms.objectType);
/* 1409 */     genExpr((JCTree)paramJCSynchronized.lock, paramJCSynchronized.lock.type).load().duplicate();
/* 1410 */     localItem.store();
/*      */
/*      */
/* 1413 */     this.code.emitop0(194);
/* 1414 */     this.code.state.lock(localItem.reg);
/*      */
/*      */
/*      */
/* 1418 */     final Env<GenContext> syncEnv = this.env.dup((JCTree)paramJCSynchronized, new GenContext());
/* 1419 */     ((GenContext)env.info).finalize = new GenFinalizer() {
/*      */         void gen() {
/* 1421 */           genLast();
/* 1422 */           Assert.check((((GenContext)syncEnv.info).gaps.length() % 2 == 0));
/* 1423 */           ((GenContext)syncEnv.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/*      */         }
/*      */         void genLast() {
/* 1426 */           if (Gen.this.code.isAlive()) {
/* 1427 */             lockVar.load();
/* 1428 */             Gen.this.code.emitop0(195);
/* 1429 */             Gen.this.code.state.unlock(lockVar.reg);
/*      */           }
/*      */         }
/*      */       };
/* 1433 */     ((GenContext)env.info).gaps = new ListBuffer();
/* 1434 */     genTry((JCTree)paramJCSynchronized.body, List.nil(), env);
/* 1435 */     this.code.endScopes(i);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitTry(final JCTree.JCTry tree) {
/* 1441 */     final Env<GenContext> tryEnv = this.env.dup((JCTree)tree, new GenContext());
/* 1442 */     final Env<GenContext> oldEnv = this.env;
/* 1443 */     if (!this.useJsrLocally) {
/* 1444 */       this
/*      */
/*      */
/*      */
/* 1448 */         .useJsrLocally = (this.stackMap == Code.StackMapFormat.NONE && (this.jsrlimit <= 0 || (this.jsrlimit < 100 && estimateCodeComplexity((JCTree)tree.finalizer) > this.jsrlimit)));
/*      */     }
/* 1450 */     ((GenContext)env1.info).finalize = new GenFinalizer() {
/*      */         void gen() {
/* 1452 */           if (Gen.this.useJsrLocally) {
/* 1453 */             if (tree.finalizer != null) {
/* 1454 */               Code.State state = Gen.this.code.state.dup();
/* 1455 */               state.push(Code.jsrReturnValue);
/* 1456 */               ((GenContext)tryEnv.info)
/* 1457 */                 .cont = new Code.Chain(Gen.this.code.emitJump(168), ((GenContext)tryEnv.info).cont, state);
/*      */             }
/*      */
/*      */
/* 1461 */             Assert.check((((GenContext)tryEnv.info).gaps.length() % 2 == 0));
/* 1462 */             ((GenContext)tryEnv.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/*      */           } else {
/* 1464 */             Assert.check((((GenContext)tryEnv.info).gaps.length() % 2 == 0));
/* 1465 */             ((GenContext)tryEnv.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/* 1466 */             genLast();
/*      */           }
/*      */         }
/*      */         void genLast() {
/* 1470 */           if (tree.finalizer != null)
/* 1471 */             Gen.this.genStat((JCTree)tree.finalizer, oldEnv, 2);
/*      */         }
/*      */         boolean hasFinalizer() {
/* 1474 */           return (tree.finalizer != null);
/*      */         }
/*      */       };
/* 1477 */     ((GenContext)env1.info).gaps = new ListBuffer();
/* 1478 */     genTry((JCTree)tree.body, tree.catchers, env1);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void genTry(JCTree paramJCTree, List<JCTree.JCCatch> paramList, Env<GenContext> paramEnv) {
/* 1487 */     int i = this.code.nextreg;
/* 1488 */     int j = this.code.curCP();
/* 1489 */     Code.State state = this.code.state.dup();
/* 1490 */     genStat(paramJCTree, paramEnv, 2);
/* 1491 */     int k = this.code.curCP();
/*      */
/*      */
/* 1494 */     boolean bool = (((GenContext)paramEnv.info).finalize != null && ((GenContext)paramEnv.info).finalize.hasFinalizer()) ? true : false;
/* 1495 */     List<Integer> list = ((GenContext)paramEnv.info).gaps.toList();
/* 1496 */     this.code.statBegin(TreeInfo.endPos(paramJCTree));
/* 1497 */     genFinalizer(paramEnv);
/* 1498 */     this.code.statBegin(TreeInfo.endPos(paramEnv.tree));
/* 1499 */     Code.Chain chain = this.code.branch(167);
/* 1500 */     endFinalizerGap(paramEnv);
/* 1501 */     if (j != k) for (List<JCTree.JCCatch> list1 = paramList; list1.nonEmpty(); list1 = list1.tail) {
/*      */
/* 1503 */         this.code.entryPoint(state, ((JCTree.JCCatch)list1.head).param.sym.type);
/* 1504 */         genCatch((JCTree.JCCatch)list1.head, paramEnv, j, k, list);
/* 1505 */         genFinalizer(paramEnv);
/* 1506 */         if (bool || list1.tail.nonEmpty()) {
/* 1507 */           this.code.statBegin(TreeInfo.endPos(paramEnv.tree));
/* 1508 */           chain = Code.mergeChains(chain, this.code
/* 1509 */               .branch(167));
/*      */         }
/* 1511 */         endFinalizerGap(paramEnv);
/*      */       }
/* 1513 */     if (bool) {
/*      */
/*      */
/* 1516 */       this.code.newRegSegment();
/*      */
/*      */
/*      */
/*      */
/* 1521 */       int m = this.code.entryPoint(state, this.syms.throwableType);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1528 */       int n = j;
/* 1529 */       while (((GenContext)paramEnv.info).gaps.nonEmpty()) {
/* 1530 */         int i1 = ((Integer)((GenContext)paramEnv.info).gaps.next()).intValue();
/* 1531 */         registerCatch(paramJCTree.pos(), n, i1, m, 0);
/*      */
/* 1533 */         n = ((Integer)((GenContext)paramEnv.info).gaps.next()).intValue();
/*      */       }
/* 1535 */       this.code.statBegin(TreeInfo.finalizerPos(paramEnv.tree, TreeInfo.PosKind.FIRST_STAT_POS));
/* 1536 */       this.code.markStatBegin();
/*      */
/* 1538 */       Items.LocalItem localItem = makeTemp(this.syms.throwableType);
/* 1539 */       localItem.store();
/* 1540 */       genFinalizer(paramEnv);
/* 1541 */       this.code.resolvePending();
/* 1542 */       this.code.statBegin(TreeInfo.finalizerPos(paramEnv.tree, TreeInfo.PosKind.END_POS));
/* 1543 */       this.code.markStatBegin();
/*      */
/* 1545 */       localItem.load();
/* 1546 */       registerCatch(paramJCTree.pos(), n, ((Integer)((GenContext)paramEnv.info).gaps
/* 1547 */           .next()).intValue(), m, 0);
/*      */
/* 1549 */       this.code.emitop0(191);
/* 1550 */       this.code.markDead();
/*      */
/*      */
/* 1553 */       if (((GenContext)paramEnv.info).cont != null) {
/*      */
/* 1555 */         this.code.resolve(((GenContext)paramEnv.info).cont);
/*      */
/*      */
/* 1558 */         this.code.statBegin(TreeInfo.finalizerPos(paramEnv.tree, TreeInfo.PosKind.FIRST_STAT_POS));
/* 1559 */         this.code.markStatBegin();
/*      */
/*      */
/* 1562 */         Items.LocalItem localItem1 = makeTemp(this.syms.throwableType);
/* 1563 */         localItem1.store();
/*      */
/*      */
/* 1566 */         ((GenContext)paramEnv.info).finalize.genLast();
/*      */
/*      */
/* 1569 */         this.code.emitop1w(169, localItem1.reg);
/* 1570 */         this.code.markDead();
/*      */       }
/*      */     }
/*      */
/* 1574 */     this.code.resolve(chain);
/*      */
/* 1576 */     this.code.endScopes(i);
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
/*      */   void genCatch(JCTree.JCCatch paramJCCatch, Env<GenContext> paramEnv, int paramInt1, int paramInt2, List<Integer> paramList) {
/* 1589 */     if (paramInt1 != paramInt2) {
/*      */
/*      */
/* 1592 */       List list = TreeInfo.isMultiCatch(paramJCCatch) ? ((JCTree.JCTypeUnion)paramJCCatch.param.vartype).alternatives : List.of(paramJCCatch.param.vartype);
/* 1593 */       while (paramList.nonEmpty()) {
/* 1594 */         for (JCTree.JCExpression jCExpression : list) {
/* 1595 */           int k = makeRef(paramJCCatch.pos(), jCExpression.type);
/* 1596 */           int m = ((Integer)paramList.head).intValue();
/* 1597 */           registerCatch(paramJCCatch.pos(), paramInt1, m, this.code
/* 1598 */               .curCP(), k);
/*      */
/* 1600 */           if (jCExpression.type.isAnnotated())
/*      */           {
/* 1602 */             for (Attribute.TypeCompound typeCompound : jCExpression.type.getAnnotationMirrors()) {
/* 1603 */               typeCompound.position.type_index = k;
/*      */             }
/*      */           }
/*      */         }
/* 1607 */         paramList = paramList.tail;
/* 1608 */         paramInt1 = ((Integer)paramList.head).intValue();
/* 1609 */         paramList = paramList.tail;
/*      */       }
/* 1611 */       if (paramInt1 < paramInt2) {
/* 1612 */         for (JCTree.JCExpression jCExpression : list) {
/* 1613 */           int k = makeRef(paramJCCatch.pos(), jCExpression.type);
/* 1614 */           registerCatch(paramJCCatch.pos(), paramInt1, paramInt2, this.code
/* 1615 */               .curCP(), k);
/*      */
/* 1617 */           if (jCExpression.type.isAnnotated())
/*      */           {
/* 1619 */             for (Attribute.TypeCompound typeCompound : jCExpression.type.getAnnotationMirrors()) {
/* 1620 */               typeCompound.position.type_index = k;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1625 */       Symbol.VarSymbol varSymbol = paramJCCatch.param.sym;
/* 1626 */       this.code.statBegin(paramJCCatch.pos);
/* 1627 */       this.code.markStatBegin();
/* 1628 */       int i = this.code.nextreg;
/* 1629 */       int j = this.code.newLocal(varSymbol);
/* 1630 */       this.items.makeLocalItem(varSymbol).store();
/* 1631 */       this.code.statBegin(TreeInfo.firstStatPos((JCTree)paramJCCatch.body));
/* 1632 */       genStat((JCTree)paramJCCatch.body, paramEnv, 2);
/* 1633 */       this.code.endScopes(i);
/* 1634 */       this.code.statBegin(TreeInfo.endPos((JCTree)paramJCCatch.body));
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void registerCatch(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 1643 */     char c1 = (char)paramInt1;
/* 1644 */     char c2 = (char)paramInt2;
/* 1645 */     char c3 = (char)paramInt3;
/* 1646 */     if (c1 == paramInt1 && c2 == paramInt2 && c3 == paramInt3) {
/*      */
/*      */
/* 1649 */       this.code.addCatch(c1, c2, c3, (char)paramInt4);
/*      */     } else {
/*      */
/* 1652 */       if (!this.useJsrLocally && !this.target.generateStackMapTable()) {
/* 1653 */         this.useJsrLocally = true;
/* 1654 */         throw new CodeSizeOverflow();
/*      */       }
/* 1656 */       this.log.error(paramDiagnosticPosition, "limit.code.too.large.for.try.stmt", new Object[0]);
/* 1657 */       this.nerrs++;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   int estimateCodeComplexity(JCTree paramJCTree) {
/* 1666 */     if (paramJCTree == null) return 0;
/*      */     class ComplexityScanner extends TreeScanner { int complexity; ComplexityScanner() {
/* 1668 */         this.complexity = 0;
/*      */       } public void scan(JCTree param1JCTree) {
/* 1670 */         if (this.complexity > Gen.this.jsrlimit)
/* 1671 */           return;  super.scan(param1JCTree);
/*      */       }
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {}
/*      */       public void visitDoLoop(JCTree.JCDoWhileLoop param1JCDoWhileLoop) {
/* 1675 */         super.visitDoLoop(param1JCDoWhileLoop); this.complexity++;
/*      */       } public void visitWhileLoop(JCTree.JCWhileLoop param1JCWhileLoop) {
/* 1677 */         super.visitWhileLoop(param1JCWhileLoop); this.complexity++;
/*      */       } public void visitForLoop(JCTree.JCForLoop param1JCForLoop) {
/* 1679 */         super.visitForLoop(param1JCForLoop); this.complexity++;
/*      */       } public void visitSwitch(JCTree.JCSwitch param1JCSwitch) {
/* 1681 */         super.visitSwitch(param1JCSwitch); this.complexity += 5;
/*      */       } public void visitCase(JCTree.JCCase param1JCCase) {
/* 1683 */         super.visitCase(param1JCCase); this.complexity++;
/*      */       } public void visitSynchronized(JCTree.JCSynchronized param1JCSynchronized) {
/* 1685 */         super.visitSynchronized(param1JCSynchronized); this.complexity += 6;
/*      */       } public void visitTry(JCTree.JCTry param1JCTry) {
/* 1687 */         super.visitTry(param1JCTry);
/* 1688 */         if (param1JCTry.finalizer != null) this.complexity += 6;
/*      */       } public void visitCatch(JCTree.JCCatch param1JCCatch) {
/* 1690 */         super.visitCatch(param1JCCatch); this.complexity += 2;
/*      */       } public void visitConditional(JCTree.JCConditional param1JCConditional) {
/* 1692 */         super.visitConditional(param1JCConditional); this.complexity += 2;
/*      */       } public void visitIf(JCTree.JCIf param1JCIf) {
/* 1694 */         super.visitIf(param1JCIf); this.complexity += 2;
/*      */       }
/*      */       public void visitBreak(JCTree.JCBreak param1JCBreak) {
/* 1697 */         super.visitBreak(param1JCBreak); this.complexity++;
/*      */       } public void visitContinue(JCTree.JCContinue param1JCContinue) {
/* 1699 */         super.visitContinue(param1JCContinue); this.complexity++;
/*      */       } public void visitReturn(JCTree.JCReturn param1JCReturn) {
/* 1701 */         super.visitReturn(param1JCReturn); this.complexity++;
/*      */       } public void visitThrow(JCTree.JCThrow param1JCThrow) {
/* 1703 */         super.visitThrow(param1JCThrow); this.complexity++;
/*      */       } public void visitAssert(JCTree.JCAssert param1JCAssert) {
/* 1705 */         super.visitAssert(param1JCAssert); this.complexity += 5;
/*      */       } public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/* 1707 */         super.visitApply(param1JCMethodInvocation); this.complexity += 2;
/*      */       } public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1709 */         scan((JCTree)param1JCNewClass.encl); scan(param1JCNewClass.args); this.complexity += 2;
/*      */       } public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/* 1711 */         super.visitNewArray(param1JCNewArray); this.complexity += 5;
/*      */       } public void visitAssign(JCTree.JCAssign param1JCAssign) {
/* 1713 */         super.visitAssign(param1JCAssign); this.complexity++;
/*      */       } public void visitAssignop(JCTree.JCAssignOp param1JCAssignOp) {
/* 1715 */         super.visitAssignop(param1JCAssignOp); this.complexity += 2;
/*      */       } public void visitUnary(JCTree.JCUnary param1JCUnary) {
/* 1717 */         this.complexity++;
/* 1718 */         if (param1JCUnary.type.constValue() == null) super.visitUnary(param1JCUnary);
/*      */       } public void visitBinary(JCTree.JCBinary param1JCBinary) {
/* 1720 */         this.complexity++;
/* 1721 */         if (param1JCBinary.type.constValue() == null) super.visitBinary(param1JCBinary);
/*      */       } public void visitTypeTest(JCTree.JCInstanceOf param1JCInstanceOf) {
/* 1723 */         super.visitTypeTest(param1JCInstanceOf); this.complexity++;
/*      */       } public void visitIndexed(JCTree.JCArrayAccess param1JCArrayAccess) {
/* 1725 */         super.visitIndexed(param1JCArrayAccess); this.complexity++;
/*      */       } public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 1727 */         super.visitSelect(param1JCFieldAccess);
/* 1728 */         if (param1JCFieldAccess.sym.kind == 4) this.complexity++;
/*      */       } public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 1730 */         if (param1JCIdent.sym.kind == 4) {
/* 1731 */           this.complexity++;
/* 1732 */           if (param1JCIdent.type.constValue() == null && param1JCIdent.sym.owner.kind == 2)
/*      */           {
/* 1734 */             this.complexity++; }
/*      */         }
/*      */       }
/*      */       public void visitLiteral(JCTree.JCLiteral param1JCLiteral) {
/* 1738 */         this.complexity++;
/*      */       } public void visitTree(JCTree param1JCTree) {}
/*      */       public void visitWildcard(JCTree.JCWildcard param1JCWildcard) {
/* 1741 */         throw new AssertionError(getClass().getName());
/*      */       } }
/*      */     ;
/* 1744 */     ComplexityScanner complexityScanner = new ComplexityScanner();
/* 1745 */     paramJCTree.accept((JCTree.Visitor)complexityScanner);
/* 1746 */     return complexityScanner.complexity;
/*      */   }
/*      */
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 1750 */     int i = this.code.nextreg;
/* 1751 */     Code.Chain chain1 = null;
/* 1752 */     Items.CondItem condItem = genCond((JCTree)TreeInfo.skipParens(paramJCIf.cond), 8);
/*      */
/* 1754 */     Code.Chain chain2 = condItem.jumpFalse();
/* 1755 */     if (!condItem.isFalse()) {
/* 1756 */       this.code.resolve(condItem.trueJumps);
/* 1757 */       genStat((JCTree)paramJCIf.thenpart, this.env, 17);
/* 1758 */       chain1 = this.code.branch(167);
/*      */     }
/* 1760 */     if (chain2 != null) {
/* 1761 */       this.code.resolve(chain2);
/* 1762 */       if (paramJCIf.elsepart != null) {
/* 1763 */         genStat((JCTree)paramJCIf.elsepart, this.env, 17);
/*      */       }
/*      */     }
/* 1766 */     this.code.resolve(chain1);
/* 1767 */     this.code.endScopes(i);
/*      */   }
/*      */
/*      */
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/* 1772 */     JCTree.JCExpression jCExpression = paramJCExpressionStatement.expr;
/* 1773 */     switch (jCExpression.getTag()) {
/*      */       case POSTINC:
/* 1775 */         ((JCTree.JCUnary)jCExpression).setTag(JCTree.Tag.PREINC);
/*      */         break;
/*      */       case POSTDEC:
/* 1778 */         ((JCTree.JCUnary)jCExpression).setTag(JCTree.Tag.PREDEC);
/*      */         break;
/*      */     }
/* 1781 */     genExpr((JCTree)paramJCExpressionStatement.expr, paramJCExpressionStatement.expr.type).drop();
/*      */   }
/*      */
/*      */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 1785 */     Env<GenContext> env = unwind(paramJCBreak.target, this.env);
/* 1786 */     Assert.check((this.code.state.stacksize == 0));
/* 1787 */     ((GenContext)env.info).addExit(this.code.branch(167));
/* 1788 */     endFinalizerGaps(this.env, env);
/*      */   }
/*      */
/*      */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 1792 */     Env<GenContext> env = unwind(paramJCContinue.target, this.env);
/* 1793 */     Assert.check((this.code.state.stacksize == 0));
/* 1794 */     ((GenContext)env.info).addCont(this.code.branch(167));
/* 1795 */     endFinalizerGaps(this.env, env);
/*      */   }
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*      */     Env<GenContext> env;
/* 1799 */     int i = this.code.nextreg;
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1805 */     int j = this.code.pendingStatPos;
/* 1806 */     if (paramJCReturn.expr != null) {
/* 1807 */       Items.Item item = genExpr((JCTree)paramJCReturn.expr, this.pt).load();
/* 1808 */       if (hasFinally((JCTree)this.env.enclMethod, this.env)) {
/* 1809 */         item = makeTemp(this.pt);
/* 1810 */         item.store();
/*      */       }
/* 1812 */       env = unwind((JCTree)this.env.enclMethod, this.env);
/* 1813 */       this.code.pendingStatPos = j;
/* 1814 */       item.load();
/* 1815 */       this.code.emitop0(172 + Code.truncate(Code.typecode(this.pt)));
/*      */     } else {
/* 1817 */       env = unwind((JCTree)this.env.enclMethod, this.env);
/* 1818 */       this.code.pendingStatPos = j;
/* 1819 */       this.code.emitop0(177);
/*      */     }
/* 1821 */     endFinalizerGaps(this.env, env);
/* 1822 */     this.code.endScopes(i);
/*      */   }
/*      */
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 1826 */     genExpr((JCTree)paramJCThrow.expr, paramJCThrow.expr.type).load();
/* 1827 */     this.code.emitop0(191);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 1835 */     setTypeAnnotationPositions(paramJCMethodInvocation.pos);
/*      */
/* 1837 */     Items.Item item = genExpr((JCTree)paramJCMethodInvocation.meth, this.methodType);
/*      */
/*      */
/*      */
/* 1841 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)TreeInfo.symbol((JCTree)paramJCMethodInvocation.meth);
/* 1842 */     genArgs(paramJCMethodInvocation.args, methodSymbol
/* 1843 */         .externalType(this.types).getParameterTypes());
/* 1844 */     if (!methodSymbol.isDynamic()) {
/* 1845 */       this.code.statBegin(paramJCMethodInvocation.pos);
/*      */     }
/* 1847 */     this.result = item.invoke();
/*      */   }
/*      */
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 1851 */     Code.Chain chain1 = null;
/* 1852 */     Items.CondItem condItem = genCond((JCTree)paramJCConditional.cond, 8);
/* 1853 */     Code.Chain chain2 = condItem.jumpFalse();
/* 1854 */     if (!condItem.isFalse()) {
/* 1855 */       this.code.resolve(condItem.trueJumps);
/* 1856 */       boolean bool = this.genCrt ? this.code.curCP() : false;
/* 1857 */       genExpr((JCTree)paramJCConditional.truepart, this.pt).load();
/* 1858 */       this.code.state.forceStackTop(paramJCConditional.type);
/* 1859 */       if (this.genCrt) this.code.crt.put(paramJCConditional.truepart, 16, bool, this.code
/* 1860 */             .curCP());
/* 1861 */       chain1 = this.code.branch(167);
/*      */     }
/* 1863 */     if (chain2 != null) {
/* 1864 */       this.code.resolve(chain2);
/* 1865 */       boolean bool = this.genCrt ? this.code.curCP() : false;
/* 1866 */       genExpr((JCTree)paramJCConditional.falsepart, this.pt).load();
/* 1867 */       this.code.state.forceStackTop(paramJCConditional.type);
/* 1868 */       if (this.genCrt) this.code.crt.put(paramJCConditional.falsepart, 16, bool, this.code
/* 1869 */             .curCP());
/*      */     }
/* 1871 */     this.code.resolve(chain1);
/* 1872 */     this.result = this.items.makeStackItem(this.pt);
/*      */   }
/*      */
/*      */   private void setTypeAnnotationPositions(int paramInt) {
/* 1876 */     Symbol.MethodSymbol methodSymbol = this.code.meth;
/*      */
/* 1878 */     boolean bool = (this.code.meth.getKind() == ElementKind.CONSTRUCTOR || this.code.meth.getKind() == ElementKind.STATIC_INIT) ? true : false;
/*      */
/* 1880 */     for (Attribute.TypeCompound typeCompound : methodSymbol.getRawTypeAttributes()) {
/* 1881 */       if (typeCompound.hasUnknownPosition()) {
/* 1882 */         typeCompound.tryFixPosition();
/*      */       }
/* 1884 */       if (typeCompound.position.matchesPos(paramInt)) {
/* 1885 */         typeCompound.position.updatePosOffset(this.code.cp);
/*      */       }
/*      */     }
/* 1888 */     if (!bool) {
/*      */       return;
/*      */     }
/* 1891 */     for (Attribute.TypeCompound typeCompound : methodSymbol.owner.getRawTypeAttributes()) {
/* 1892 */       if (typeCompound.hasUnknownPosition()) {
/* 1893 */         typeCompound.tryFixPosition();
/*      */       }
/* 1895 */       if (typeCompound.position.matchesPos(paramInt)) {
/* 1896 */         typeCompound.position.updatePosOffset(this.code.cp);
/*      */       }
/*      */     }
/* 1899 */     Symbol.ClassSymbol classSymbol = methodSymbol.enclClass();
/* 1900 */     for (Symbol symbol : new FilteredMemberList(classSymbol.members())) {
/* 1901 */       if (!symbol.getKind().isField()) {
/*      */         continue;
/*      */       }
/* 1904 */       for (Attribute.TypeCompound typeCompound : symbol.getRawTypeAttributes()) {
/* 1905 */         if (typeCompound.hasUnknownPosition()) {
/* 1906 */           typeCompound.tryFixPosition();
/*      */         }
/* 1908 */         if (typeCompound.position.matchesPos(paramInt)) {
/* 1909 */           typeCompound.position.updatePosOffset(this.code.cp);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 1917 */     Assert.check((paramJCNewClass.encl == null && paramJCNewClass.def == null));
/* 1918 */     setTypeAnnotationPositions(paramJCNewClass.pos);
/*      */
/* 1920 */     this.code.emitop2(187, makeRef(paramJCNewClass.pos(), paramJCNewClass.type));
/* 1921 */     this.code.emitop0(89);
/*      */
/*      */
/*      */
/*      */
/* 1926 */     genArgs(paramJCNewClass.args, paramJCNewClass.constructor.externalType(this.types).getParameterTypes());
/*      */
/* 1928 */     this.items.makeMemberItem(paramJCNewClass.constructor, true).invoke();
/* 1929 */     this.result = this.items.makeStackItem(paramJCNewClass.type);
/*      */   }
/*      */
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 1933 */     setTypeAnnotationPositions(paramJCNewArray.pos);
/*      */
/* 1935 */     if (paramJCNewArray.elems != null) {
/* 1936 */       Type type = this.types.elemtype(paramJCNewArray.type);
/* 1937 */       loadIntConst(paramJCNewArray.elems.length());
/* 1938 */       Items.Item item = makeNewArray(paramJCNewArray.pos(), paramJCNewArray.type, 1);
/* 1939 */       byte b = 0;
/* 1940 */       for (List list = paramJCNewArray.elems; list.nonEmpty(); list = list.tail) {
/* 1941 */         item.duplicate();
/* 1942 */         loadIntConst(b);
/* 1943 */         b++;
/* 1944 */         genExpr((JCTree)list.head, type).load();
/* 1945 */         this.items.makeIndexedItem(type).store();
/*      */       }
/* 1947 */       this.result = item;
/*      */     } else {
/* 1949 */       for (List list = paramJCNewArray.dims; list.nonEmpty(); list = list.tail) {
/* 1950 */         genExpr((JCTree)list.head, (Type)this.syms.intType).load();
/*      */       }
/* 1952 */       this.result = makeNewArray(paramJCNewArray.pos(), paramJCNewArray.type, paramJCNewArray.dims.length());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Items.Item makeNewArray(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, int paramInt) {
/* 1960 */     Type type = this.types.elemtype(paramType);
/* 1961 */     if (this.types.dimensions(paramType) > 255) {
/* 1962 */       this.log.error(paramDiagnosticPosition, "limit.dimensions", new Object[0]);
/* 1963 */       this.nerrs++;
/*      */     }
/* 1965 */     int i = Code.arraycode(type);
/* 1966 */     if (i == 0 || (i == 1 && paramInt == 1)) {
/* 1967 */       this.code.emitAnewarray(makeRef(paramDiagnosticPosition, type), paramType);
/* 1968 */     } else if (i == 1) {
/* 1969 */       this.code.emitMultianewarray(paramInt, makeRef(paramDiagnosticPosition, paramType), paramType);
/*      */     } else {
/* 1971 */       this.code.emitNewarray(i, paramType);
/*      */     }
/* 1973 */     return this.items.makeStackItem(paramType);
/*      */   }
/*      */
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 1977 */     this.result = genExpr((JCTree)paramJCParens.expr, paramJCParens.expr.type);
/*      */   }
/*      */
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 1981 */     Items.Item item = genExpr((JCTree)paramJCAssign.lhs, paramJCAssign.lhs.type);
/* 1982 */     genExpr((JCTree)paramJCAssign.rhs, paramJCAssign.lhs.type).load();
/* 1983 */     this.result = this.items.makeAssignItem(item);
/*      */   }
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/*      */     Items.Item item;
/* 1987 */     Symbol.OperatorSymbol operatorSymbol = (Symbol.OperatorSymbol)paramJCAssignOp.operator;
/*      */
/* 1989 */     if (operatorSymbol.opcode == 256) {
/*      */
/* 1991 */       makeStringBuffer(paramJCAssignOp.pos());
/*      */
/*      */
/*      */
/* 1995 */       item = genExpr((JCTree)paramJCAssignOp.lhs, paramJCAssignOp.lhs.type);
/* 1996 */       if (item.width() > 0) {
/* 1997 */         this.code.emitop0(90 + 3 * (item.width() - 1));
/*      */       }
/*      */
/*      */
/* 2001 */       item.load();
/* 2002 */       appendString((JCTree)paramJCAssignOp.lhs);
/*      */
/*      */
/* 2005 */       appendStrings((JCTree)paramJCAssignOp.rhs);
/*      */
/*      */
/* 2008 */       bufferToString(paramJCAssignOp.pos());
/*      */     } else {
/*      */
/* 2011 */       item = genExpr((JCTree)paramJCAssignOp.lhs, paramJCAssignOp.lhs.type);
/*      */
/*      */
/*      */
/*      */
/* 2016 */       if ((paramJCAssignOp.hasTag(JCTree.Tag.PLUS_ASG) || paramJCAssignOp.hasTag(JCTree.Tag.MINUS_ASG)) && item instanceof Items.LocalItem && paramJCAssignOp.lhs.type
/*      */
/* 2018 */         .getTag().isSubRangeOf(TypeTag.INT) && paramJCAssignOp.rhs.type
/* 2019 */         .getTag().isSubRangeOf(TypeTag.INT) && paramJCAssignOp.rhs.type
/* 2020 */         .constValue() != null) {
/* 2021 */         int i = ((Number)paramJCAssignOp.rhs.type.constValue()).intValue();
/* 2022 */         if (paramJCAssignOp.hasTag(JCTree.Tag.MINUS_ASG)) i = -i;
/* 2023 */         ((Items.LocalItem)item).incr(i);
/* 2024 */         this.result = item;
/*      */
/*      */         return;
/*      */       }
/*      */
/* 2029 */       item.duplicate();
/* 2030 */       item.coerce((Type)(operatorSymbol.type.getParameterTypes()).head).load();
/* 2031 */       completeBinop((JCTree)paramJCAssignOp.lhs, (JCTree)paramJCAssignOp.rhs, operatorSymbol).coerce(paramJCAssignOp.lhs.type);
/*      */     }
/* 2033 */     this.result = this.items.makeAssignItem(item);
/*      */   }
/*      */
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 2037 */     Symbol.OperatorSymbol operatorSymbol = (Symbol.OperatorSymbol)paramJCUnary.operator;
/* 2038 */     if (paramJCUnary.hasTag(JCTree.Tag.NOT)) {
/* 2039 */       Items.CondItem condItem = genCond((JCTree)paramJCUnary.arg, false);
/* 2040 */       this.result = condItem.negate();
/*      */     } else {
/* 2042 */       Items.Item item = genExpr((JCTree)paramJCUnary.arg, (Type)(operatorSymbol.type.getParameterTypes()).head);
/* 2043 */       switch (paramJCUnary.getTag()) {
/*      */         case POS:
/* 2045 */           this.result = item.load();
/*      */           return;
/*      */         case NEG:
/* 2048 */           this.result = item.load();
/* 2049 */           this.code.emitop0(operatorSymbol.opcode);
/*      */           return;
/*      */         case COMPL:
/* 2052 */           this.result = item.load();
/* 2053 */           emitMinusOne(item.typecode);
/* 2054 */           this.code.emitop0(operatorSymbol.opcode); return;
/*      */         case PREINC:
/*      */         case PREDEC:
/* 2057 */           item.duplicate();
/* 2058 */           if (item instanceof Items.LocalItem && (operatorSymbol.opcode == 96 || operatorSymbol.opcode == 100)) {
/*      */
/* 2060 */             ((Items.LocalItem)item).incr(paramJCUnary.hasTag(JCTree.Tag.PREINC) ? 1 : -1);
/* 2061 */             this.result = item;
/*      */           } else {
/* 2063 */             item.load();
/* 2064 */             this.code.emitop0(one(item.typecode));
/* 2065 */             this.code.emitop0(operatorSymbol.opcode);
/*      */
/*      */
/* 2068 */             if (item.typecode != 0 &&
/* 2069 */               Code.truncate(item.typecode) == 0)
/* 2070 */               this.code.emitop0(145 + item.typecode - 5);
/* 2071 */             this.result = this.items.makeAssignItem(item);
/*      */           }  return;
/*      */         case POSTINC:
/*      */         case POSTDEC:
/* 2075 */           item.duplicate();
/* 2076 */           if (item instanceof Items.LocalItem && (operatorSymbol.opcode == 96 || operatorSymbol.opcode == 100)) {
/*      */
/* 2078 */             Items.Item item1 = item.load();
/* 2079 */             ((Items.LocalItem)item).incr(paramJCUnary.hasTag(JCTree.Tag.POSTINC) ? 1 : -1);
/* 2080 */             this.result = item1;
/*      */           } else {
/* 2082 */             Items.Item item1 = item.load();
/* 2083 */             item.stash(item.typecode);
/* 2084 */             this.code.emitop0(one(item.typecode));
/* 2085 */             this.code.emitop0(operatorSymbol.opcode);
/*      */
/*      */
/* 2088 */             if (item.typecode != 0 &&
/* 2089 */               Code.truncate(item.typecode) == 0)
/* 2090 */               this.code.emitop0(145 + item.typecode - 5);
/* 2091 */             item.store();
/* 2092 */             this.result = item1;
/*      */           }
/*      */           return;
/*      */         case NULLCHK:
/* 2096 */           this.result = item.load();
/* 2097 */           this.code.emitop0(89);
/* 2098 */           genNullCheck(paramJCUnary.pos());
/*      */           return;
/*      */       }
/* 2101 */       Assert.error();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void genNullCheck(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2108 */     callMethod(paramDiagnosticPosition, this.syms.objectType, this.names.getClass,
/* 2109 */         List.nil(), false);
/* 2110 */     this.code.emitop0(87);
/*      */   }
/*      */
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 2114 */     Symbol.OperatorSymbol operatorSymbol = (Symbol.OperatorSymbol)paramJCBinary.operator;
/* 2115 */     if (operatorSymbol.opcode == 256) {
/*      */
/* 2117 */       makeStringBuffer(paramJCBinary.pos());
/*      */
/* 2119 */       appendStrings((JCTree)paramJCBinary);
/*      */
/* 2121 */       bufferToString(paramJCBinary.pos());
/* 2122 */       this.result = this.items.makeStackItem(this.syms.stringType);
/* 2123 */     } else if (paramJCBinary.hasTag(JCTree.Tag.AND)) {
/* 2124 */       Items.CondItem condItem = genCond((JCTree)paramJCBinary.lhs, 8);
/* 2125 */       if (!condItem.isFalse()) {
/* 2126 */         Code.Chain chain = condItem.jumpFalse();
/* 2127 */         this.code.resolve(condItem.trueJumps);
/* 2128 */         Items.CondItem condItem1 = genCond((JCTree)paramJCBinary.rhs, 16);
/* 2129 */         this
/* 2130 */           .result = this.items.makeCondItem(condItem1.opcode, condItem1.trueJumps,
/*      */
/* 2132 */             Code.mergeChains(chain, condItem1.falseJumps));
/*      */       } else {
/*      */
/* 2135 */         this.result = condItem;
/*      */       }
/* 2137 */     } else if (paramJCBinary.hasTag(JCTree.Tag.OR)) {
/* 2138 */       Items.CondItem condItem = genCond((JCTree)paramJCBinary.lhs, 8);
/* 2139 */       if (!condItem.isTrue()) {
/* 2140 */         Code.Chain chain = condItem.jumpTrue();
/* 2141 */         this.code.resolve(condItem.falseJumps);
/* 2142 */         Items.CondItem condItem1 = genCond((JCTree)paramJCBinary.rhs, 16);
/* 2143 */         this
/* 2144 */           .result = this.items.makeCondItem(condItem1.opcode,
/* 2145 */             Code.mergeChains(chain, condItem1.trueJumps), condItem1.falseJumps);
/*      */       } else {
/*      */
/* 2148 */         this.result = condItem;
/*      */       }
/*      */     } else {
/* 2151 */       Items.Item item = genExpr((JCTree)paramJCBinary.lhs, (Type)(operatorSymbol.type.getParameterTypes()).head);
/* 2152 */       item.load();
/* 2153 */       this.result = completeBinop((JCTree)paramJCBinary.lhs, (JCTree)paramJCBinary.rhs, operatorSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void makeStringBuffer(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2160 */     this.code.emitop2(187, makeRef(paramDiagnosticPosition, this.stringBufferType));
/* 2161 */     this.code.emitop0(89);
/* 2162 */     callMethod(paramDiagnosticPosition, this.stringBufferType, this.names.init,
/* 2163 */         List.nil(), false);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void appendString(JCTree paramJCTree) {
/* 2169 */     Type type = paramJCTree.type.baseType();
/* 2170 */     if (!type.isPrimitive() && type.tsym != this.syms.stringType.tsym) {
/* 2171 */       type = this.syms.objectType;
/*      */     }
/* 2173 */     this.items.makeMemberItem(getStringBufferAppend(paramJCTree, type), false).invoke();
/*      */   } Symbol getStringBufferAppend(JCTree paramJCTree, Type paramType) {
/*      */     Symbol.MethodSymbol methodSymbol;
/* 2176 */     Assert.checkNull(paramType.constValue());
/* 2177 */     Symbol symbol = this.stringBufferAppend.get(paramType);
/* 2178 */     if (symbol == null) {
/* 2179 */       methodSymbol = this.rs.resolveInternalMethod(paramJCTree.pos(), this.attrEnv, this.stringBufferType, this.names.append,
/*      */
/*      */
/*      */
/* 2183 */           List.of(paramType), null);
/*      */
/* 2185 */       this.stringBufferAppend.put(paramType, methodSymbol);
/*      */     }
/* 2187 */     return (Symbol)methodSymbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void appendStrings(JCTree paramJCTree) {
/* 2193 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2194 */     if (paramJCTree.hasTag(JCTree.Tag.PLUS) && paramJCTree.type.constValue() == null) {
/* 2195 */       JCTree.JCBinary jCBinary = (JCTree.JCBinary)paramJCTree;
/* 2196 */       if (jCBinary.operator.kind == 16 && ((Symbol.OperatorSymbol)jCBinary.operator).opcode == 256) {
/*      */
/* 2198 */         appendStrings((JCTree)jCBinary.lhs);
/* 2199 */         appendStrings((JCTree)jCBinary.rhs);
/*      */         return;
/*      */       }
/*      */     }
/* 2203 */     genExpr(paramJCTree, paramJCTree.type).load();
/* 2204 */     appendString(paramJCTree);
/*      */   }
/*      */
/*      */
/*      */
/*      */   void bufferToString(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2210 */     callMethod(paramDiagnosticPosition, this.stringBufferType, this.names.toString,
/*      */
/*      */
/*      */
/* 2214 */         List.nil(), false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Items.Item completeBinop(JCTree paramJCTree1, JCTree paramJCTree2, Symbol.OperatorSymbol paramOperatorSymbol) {
/* 2225 */     Type.MethodType methodType = (Type.MethodType)paramOperatorSymbol.type;
/* 2226 */     int i = paramOperatorSymbol.opcode;
/* 2227 */     if (i >= 159 && i <= 164 && paramJCTree2.type
/* 2228 */       .constValue() instanceof Number && ((Number)paramJCTree2.type
/* 2229 */       .constValue()).intValue() == 0) {
/* 2230 */       i += -6;
/* 2231 */     } else if (i >= 165 && i <= 166 &&
/* 2232 */       TreeInfo.isNull(paramJCTree2)) {
/* 2233 */       i += 33;
/*      */     } else {
/*      */       Type.JCPrimitiveType jCPrimitiveType;
/*      */
/*      */
/*      */
/* 2239 */       Type type = (Type)(paramOperatorSymbol.erasure(this.types).getParameterTypes()).tail.head;
/* 2240 */       if (i >= 270 && i <= 275) {
/* 2241 */         i += -150;
/* 2242 */         jCPrimitiveType = this.syms.intType;
/*      */       }
/*      */
/* 2245 */       genExpr(paramJCTree2, (Type)jCPrimitiveType).load();
/*      */
/*      */
/* 2248 */       if (i >= 512) {
/* 2249 */         this.code.emitop0(i >> 9);
/* 2250 */         i &= 0xFF;
/*      */       }
/*      */     }
/* 2253 */     if ((i >= 153 && i <= 166) || i == 198 || i == 199)
/*      */     {
/* 2255 */       return this.items.makeCondItem(i);
/*      */     }
/* 2257 */     this.code.emitop0(i);
/* 2258 */     return this.items.makeStackItem(methodType.restype);
/*      */   }
/*      */
/*      */
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 2263 */     setTypeAnnotationPositions(paramJCTypeCast.pos);
/* 2264 */     this.result = genExpr((JCTree)paramJCTypeCast.expr, paramJCTypeCast.clazz.type).load();
/*      */
/*      */
/*      */
/*      */
/* 2269 */     if (!paramJCTypeCast.clazz.type.isPrimitive() && this.types
/* 2270 */       .asSuper(paramJCTypeCast.expr.type, (Symbol)paramJCTypeCast.clazz.type.tsym) == null) {
/* 2271 */       this.code.emitop2(192, makeRef(paramJCTypeCast.pos(), paramJCTypeCast.clazz.type));
/*      */     }
/*      */   }
/*      */
/*      */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/* 2276 */     throw new AssertionError(getClass().getName());
/*      */   }
/*      */
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 2280 */     setTypeAnnotationPositions(paramJCInstanceOf.pos);
/* 2281 */     genExpr((JCTree)paramJCInstanceOf.expr, paramJCInstanceOf.expr.type).load();
/* 2282 */     this.code.emitop2(193, makeRef(paramJCInstanceOf.pos(), paramJCInstanceOf.clazz.type));
/* 2283 */     this.result = this.items.makeStackItem((Type)this.syms.booleanType);
/*      */   }
/*      */
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 2287 */     genExpr((JCTree)paramJCArrayAccess.indexed, paramJCArrayAccess.indexed.type).load();
/* 2288 */     genExpr((JCTree)paramJCArrayAccess.index, (Type)this.syms.intType).load();
/* 2289 */     this.result = this.items.makeIndexedItem(paramJCArrayAccess.type);
/*      */   }
/*      */
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 2293 */     Symbol symbol = paramJCIdent.sym;
/* 2294 */     if (paramJCIdent.name == this.names._this || paramJCIdent.name == this.names._super) {
/*      */
/*      */
/* 2297 */       Items.Item item = (paramJCIdent.name == this.names._this) ? this.items.makeThisItem() : this.items.makeSuperItem();
/* 2298 */       if (symbol.kind == 16) {
/*      */
/* 2300 */         item.load();
/* 2301 */         item = this.items.makeMemberItem(symbol, true);
/*      */       }
/* 2303 */       this.result = item;
/* 2304 */     } else if (symbol.kind == 4 && symbol.owner.kind == 16) {
/* 2305 */       this.result = this.items.makeLocalItem((Symbol.VarSymbol)symbol);
/* 2306 */     } else if (isInvokeDynamic(symbol)) {
/* 2307 */       this.result = this.items.makeDynamicItem(symbol);
/* 2308 */     } else if ((symbol.flags() & 0x8L) != 0L) {
/* 2309 */       if (!isAccessSuper(this.env.enclMethod))
/* 2310 */         symbol = binaryQualifier(symbol, this.env.enclClass.type);
/* 2311 */       this.result = this.items.makeStaticItem(symbol);
/*      */     } else {
/* 2313 */       this.items.makeThisItem().load();
/* 2314 */       symbol = binaryQualifier(symbol, this.env.enclClass.type);
/* 2315 */       this.result = this.items.makeMemberItem(symbol, ((symbol.flags() & 0x2L) != 0L));
/*      */     }
/*      */   }
/*      */
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 2320 */     Symbol symbol1 = paramJCFieldAccess.sym;
/*      */
/* 2322 */     if (paramJCFieldAccess.name == this.names._class) {
/* 2323 */       Assert.check(this.target.hasClassLiterals());
/* 2324 */       this.code.emitLdc(makeRef(paramJCFieldAccess.pos(), paramJCFieldAccess.selected.type));
/* 2325 */       this.result = this.items.makeStackItem(this.pt);
/*      */
/*      */       return;
/*      */     }
/* 2329 */     Symbol symbol2 = TreeInfo.symbol((JCTree)paramJCFieldAccess.selected);
/*      */
/*      */
/* 2332 */     boolean bool = (symbol2 != null && (symbol2.kind == 2 || symbol2.name == this.names._super)) ? true : false;
/*      */
/*      */
/*      */
/*      */
/* 2337 */     boolean bool1 = isAccessSuper(this.env.enclMethod);
/*      */
/*      */
/*      */
/* 2341 */     Items.Item item = bool ? this.items.makeSuperItem() : genExpr((JCTree)paramJCFieldAccess.selected, paramJCFieldAccess.selected.type);
/*      */
/* 2343 */     if (symbol1.kind == 4 && ((Symbol.VarSymbol)symbol1).getConstValue() != null) {
/*      */
/*      */
/* 2346 */       if ((symbol1.flags() & 0x8L) != 0L) {
/* 2347 */         if (!bool && (symbol2 == null || symbol2.kind != 2))
/* 2348 */           item = item.load();
/* 2349 */         item.drop();
/*      */       } else {
/* 2351 */         item.load();
/* 2352 */         genNullCheck(paramJCFieldAccess.selected.pos());
/*      */       }
/* 2354 */       this
/* 2355 */         .result = this.items.makeImmediateItem(symbol1.type, ((Symbol.VarSymbol)symbol1).getConstValue());
/*      */     } else {
/* 2357 */       if (isInvokeDynamic(symbol1)) {
/* 2358 */         this.result = this.items.makeDynamicItem(symbol1);
/*      */         return;
/*      */       }
/* 2361 */       symbol1 = binaryQualifier(symbol1, paramJCFieldAccess.selected.type);
/*      */
/* 2363 */       if ((symbol1.flags() & 0x8L) != 0L) {
/* 2364 */         if (!bool && (symbol2 == null || symbol2.kind != 2))
/* 2365 */           item = item.load();
/* 2366 */         item.drop();
/* 2367 */         this.result = this.items.makeStaticItem(symbol1);
/*      */       } else {
/* 2369 */         item.load();
/* 2370 */         if (symbol1 == this.syms.lengthVar) {
/* 2371 */           this.code.emitop0(190);
/* 2372 */           this.result = this.items.makeStackItem((Type)this.syms.intType);
/*      */         } else {
/* 2374 */           this
/* 2375 */             .result = this.items.makeMemberItem(symbol1, ((symbol1
/* 2376 */               .flags() & 0x2L) != 0L || bool || bool1));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public boolean isInvokeDynamic(Symbol paramSymbol) {
/* 2384 */     return (paramSymbol.kind == 16 && ((Symbol.MethodSymbol)paramSymbol).isDynamic());
/*      */   }
/*      */
/*      */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/* 2388 */     if (paramJCLiteral.type.hasTag(TypeTag.BOT)) {
/* 2389 */       this.code.emitop0(1);
/* 2390 */       if (this.types.dimensions(this.pt) > 1) {
/* 2391 */         this.code.emitop2(192, makeRef(paramJCLiteral.pos(), this.pt));
/* 2392 */         this.result = this.items.makeStackItem(this.pt);
/*      */       } else {
/* 2394 */         this.result = this.items.makeStackItem(paramJCLiteral.type);
/*      */       }
/*      */     } else {
/*      */
/* 2398 */       this.result = this.items.makeImmediateItem(paramJCLiteral.type, paramJCLiteral.value);
/*      */     }
/*      */   }
/*      */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/* 2402 */     int i = this.code.nextreg;
/* 2403 */     genStats(paramLetExpr.defs, this.env);
/* 2404 */     this.result = genExpr(paramLetExpr.expr, paramLetExpr.expr.type).load();
/* 2405 */     this.code.endScopes(i);
/*      */   }
/*      */
/*      */   private void generateReferencesToPrunedTree(Symbol.ClassSymbol paramClassSymbol, Pool paramPool) {
/* 2409 */     List list = (List)this.lower.prunedTree.get(paramClassSymbol);
/* 2410 */     if (list != null) {
/* 2411 */       for (JCTree jCTree : list) {
/* 2412 */         jCTree.accept(this.classReferenceVisitor);
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
/*      */   public boolean genClass(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl) {
/*      */     try {
/* 2430 */       this.attrEnv = paramEnv;
/* 2431 */       Symbol.ClassSymbol classSymbol = paramJCClassDecl.sym;
/* 2432 */       this.toplevel = paramEnv.toplevel;
/* 2433 */       this.endPosTable = this.toplevel.endPositions;
/*      */
/*      */
/* 2436 */       if (this.generateIproxies && (classSymbol
/* 2437 */         .flags() & 0x600L) == 1024L && !this.allowGenerics)
/*      */       {
/*      */
/* 2440 */         implementInterfaceMethods(classSymbol); }
/* 2441 */       classSymbol.pool = this.pool;
/* 2442 */       this.pool.reset();
/*      */
/*      */
/*      */
/* 2446 */       paramJCClassDecl.defs = normalizeDefs(paramJCClassDecl.defs, classSymbol);
/* 2447 */       generateReferencesToPrunedTree(classSymbol, this.pool);
/* 2448 */       Env<GenContext> env = new Env((JCTree)paramJCClassDecl, new GenContext());
/*      */
/* 2450 */       env.toplevel = paramEnv.toplevel;
/* 2451 */       env.enclClass = paramJCClassDecl;
/*      */       List list;
/* 2453 */       for (list = paramJCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 2454 */         genDef((JCTree)list.head, env);
/*      */       }
/* 2456 */       if (this.pool.numEntries() > 65535) {
/* 2457 */         this.log.error(paramJCClassDecl.pos(), "limit.pool", new Object[0]);
/* 2458 */         this.nerrs++;
/*      */       }
/* 2460 */       if (this.nerrs != 0)
/*      */       {
/* 2462 */         for (list = paramJCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 2463 */           if (((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF))
/* 2464 */             ((JCTree.JCMethodDecl)list.head).sym.code = null;
/*      */         }
/*      */       }
/* 2467 */       paramJCClassDecl.defs = List.nil();
/* 2468 */       return (this.nerrs == 0);
/*      */     } finally {
/*      */
/* 2471 */       this.attrEnv = null;
/* 2472 */       this.env = null;
/* 2473 */       this.toplevel = null;
/* 2474 */       this.endPosTable = null;
/* 2475 */       this.nerrs = 0;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   abstract class GenFinalizer
/*      */   {
/*      */     abstract void gen();
/*      */
/*      */
/*      */
/*      */     abstract void genLast();
/*      */
/*      */
/*      */
/*      */     boolean hasFinalizer() {
/* 2493 */       return true;
/*      */     } }
/*      */
/*      */   static class GenContext { Code.Chain exit;
/*      */     Code.Chain cont;
/*      */     GenFinalizer finalize;
/*      */     boolean isSwitch;
/*      */     ListBuffer<Integer> gaps;
/*      */
/*      */     GenContext() {
/* 2503 */       this.exit = null;
/*      */
/*      */
/*      */
/*      */
/* 2508 */       this.cont = null;
/*      */
/*      */
/*      */
/*      */
/* 2513 */       this.finalize = null;
/*      */
/*      */
/*      */
/*      */
/* 2518 */       this.isSwitch = false;
/*      */
/*      */
/*      */
/*      */
/* 2523 */       this.gaps = null;
/*      */     }
/*      */
/*      */
/*      */     void addExit(Code.Chain param1Chain) {
/* 2528 */       this.exit = Code.mergeChains(param1Chain, this.exit);
/*      */     }
/*      */
/*      */
/*      */
/*      */     void addCont(Code.Chain param1Chain) {
/* 2534 */       this.cont = Code.mergeChains(param1Chain, this.cont);
/*      */     } }
/*      */
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\Gen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
