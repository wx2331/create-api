/*      */ package sun.tools.javac;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Vector;
/*      */ import sun.tools.asm.Assembler;
/*      */ import sun.tools.asm.ConstantPool;
/*      */ import sun.tools.java.AmbiguousClass;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassFile;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.IdentifierToken;
/*      */ import sun.tools.java.Imports;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
/*      */ import sun.tools.tree.AssignExpression;
/*      */ import sun.tools.tree.CatchStatement;
/*      */ import sun.tools.tree.CompoundStatement;
/*      */ import sun.tools.tree.Context;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.ExpressionStatement;
/*      */ import sun.tools.tree.FieldExpression;
/*      */ import sun.tools.tree.IdentifierExpression;
/*      */ import sun.tools.tree.LocalMember;
/*      */ import sun.tools.tree.MethodExpression;
/*      */ import sun.tools.tree.NewInstanceExpression;
/*      */ import sun.tools.tree.ReturnStatement;
/*      */ import sun.tools.tree.Statement;
/*      */ import sun.tools.tree.StringExpression;
/*      */ import sun.tools.tree.SuperExpression;
/*      */ import sun.tools.tree.ThisExpression;
/*      */ import sun.tools.tree.ThrowStatement;
/*      */ import sun.tools.tree.TryStatement;
/*      */ import sun.tools.tree.TypeExpression;
/*      */ import sun.tools.tree.UplevelReference;
/*      */ import sun.tools.tree.Vset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Deprecated
/*      */ public class SourceClass
/*      */   extends ClassDefinition
/*      */ {
/*      */   Environment toplevelEnv;
/*      */   SourceMember defConstructor;
/*   68 */   ConstantPool tab = new ConstantPool();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   73 */   Hashtable deps = new Hashtable<>(11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   LocalMember thisArg;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   long endPosition;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   private Type dummyArgumentType = null; private boolean sourceFileChecked; private boolean supersChecked; private boolean basicChecking; private boolean basicCheckDone; private boolean resolving; private boolean inlinedLocalClass; public long getEndPosition() { return this.endPosition; }
/*      */   public void setEndPosition(long paramLong) { this.endPosition = paramLong; }
/*      */   public String getAbsoluteName() { return ((ClassFile)getSource()).getAbsoluteName(); }
/*      */   public Imports getImports() { return this.toplevelEnv.getImports(); }
/*      */   public LocalMember getThisArgument() { if (this.thisArg == null) this.thisArg = new LocalMember(this.where, this, 0, getType(), idThis);  return this.thisArg; }
/*      */   public void addDependency(ClassDeclaration paramClassDeclaration) { if (this.tab != null) this.tab.put(paramClassDeclaration);  if (this.toplevelEnv.print_dependencies() && paramClassDeclaration != getClassDeclaration()) this.deps.put(paramClassDeclaration, paramClassDeclaration);  }
/*      */   public void addMember(Environment paramEnvironment, MemberDefinition paramMemberDefinition) { switch (paramMemberDefinition.getModifiers() & 0x7) { case 0: case 1: case 2: case 4: break;default: paramEnvironment.error(paramMemberDefinition.getWhere(), "inconsistent.modifier", paramMemberDefinition); if (paramMemberDefinition.isPublic()) { paramMemberDefinition.subModifiers(6); break; }  paramMemberDefinition.subModifiers(2); break; }  if (paramMemberDefinition.isStatic() && !isTopLevel() && !paramMemberDefinition.isSynthetic()) if (paramMemberDefinition.isMethod()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "static.inner.method", paramMemberDefinition, this); paramMemberDefinition.subModifiers(8); } else if (paramMemberDefinition.isVariable()) { if (!paramMemberDefinition.isFinal() || paramMemberDefinition.isBlankFinal()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "static.inner.field", paramMemberDefinition.getName(), this); paramMemberDefinition.subModifiers(8); }  } else { paramMemberDefinition.subModifiers(8); }   if (paramMemberDefinition.isMethod()) { if (paramMemberDefinition.isConstructor()) { if (paramMemberDefinition.getClassDefinition().isInterface()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.constructor"); return; }  if (paramMemberDefinition.isNative() || paramMemberDefinition.isAbstract() || paramMemberDefinition.isStatic() || paramMemberDefinition.isSynchronized() || paramMemberDefinition.isFinal()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "constr.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(1336); }  } else if (paramMemberDefinition.isInitializer() && paramMemberDefinition.getClassDefinition().isInterface()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.initializer"); return; }  if (paramMemberDefinition.getType().getReturnType().isVoidArray()) paramEnvironment.error(paramMemberDefinition.getWhere(), "void.array");  if (paramMemberDefinition.getClassDefinition().isInterface() && (paramMemberDefinition.isStatic() || paramMemberDefinition.isSynchronized() || paramMemberDefinition.isNative() || paramMemberDefinition.isFinal() || paramMemberDefinition.isPrivate() || paramMemberDefinition.isProtected())) { paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.method", paramMemberDefinition); paramMemberDefinition.subModifiers(314); }  if (paramMemberDefinition.isTransient()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "transient.meth", paramMemberDefinition); paramMemberDefinition.subModifiers(128); }  if (paramMemberDefinition.isVolatile()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "volatile.meth", paramMemberDefinition); paramMemberDefinition.subModifiers(64); }  if (paramMemberDefinition.isAbstract()) { if (paramMemberDefinition.isPrivate()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.private.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(2); }  if (paramMemberDefinition.isStatic()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.static.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(8); }  if (paramMemberDefinition.isFinal()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.final.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(16); }  if (paramMemberDefinition.isNative()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.native.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(256); }  if (paramMemberDefinition.isSynchronized()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.synchronized.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(32); }  }  if (paramMemberDefinition.isAbstract() || paramMemberDefinition.isNative()) { if (paramMemberDefinition.getValue() != null) { paramEnvironment.error(paramMemberDefinition.getWhere(), "invalid.meth.body", paramMemberDefinition); paramMemberDefinition.setValue(null); }  } else if (paramMemberDefinition.getValue() == null) { if (paramMemberDefinition.isConstructor()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "no.constructor.body", paramMemberDefinition); } else { paramEnvironment.error(paramMemberDefinition.getWhere(), "no.meth.body", paramMemberDefinition); }  paramMemberDefinition.addModifiers(1024); }  Vector<Object> vector = paramMemberDefinition.getArguments(); if (vector != null) { int i = vector.size(); Type[] arrayOfType = paramMemberDefinition.getType().getArgumentTypes(); for (byte b = 0; b < arrayOfType.length; b++) { Identifier identifier; MemberDefinition memberDefinition = (MemberDefinition)vector.elementAt(b); long l = paramMemberDefinition.getWhere(); if (memberDefinition instanceof MemberDefinition) { l = ((MemberDefinition)memberDefinition).getWhere(); identifier = memberDefinition.getName(); }  if (arrayOfType[b].isType(11) || arrayOfType[b].isVoidArray())
/*      */             paramEnvironment.error(l, "void.argument", identifier);  }  }  } else if (paramMemberDefinition.isInnerClass()) { if (paramMemberDefinition.isVolatile() || paramMemberDefinition.isTransient() || paramMemberDefinition.isNative() || paramMemberDefinition.isSynchronized()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "inner.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(480); }  if (paramMemberDefinition.getClassDefinition().isInterface() && (paramMemberDefinition.isPrivate() || paramMemberDefinition.isProtected())) { paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.field", paramMemberDefinition); paramMemberDefinition.subModifiers(6); paramMemberDefinition.addModifiers(1); ClassDefinition classDefinition = paramMemberDefinition.getInnerClass(); classDefinition.subModifiers(6); classDefinition.addModifiers(1); }  } else { if (paramMemberDefinition.getType().isType(11) || paramMemberDefinition.getType().isVoidArray()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "void.inst.var", paramMemberDefinition.getName()); return; }  if (paramMemberDefinition.isSynchronized() || paramMemberDefinition.isAbstract() || paramMemberDefinition.isNative()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "var.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(1312); }  if (paramMemberDefinition.isStrict()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "var.floatmodifier", paramMemberDefinition); paramMemberDefinition.subModifiers(2097152); }  if (paramMemberDefinition.isTransient() && isInterface()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "transient.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(128); }  if (paramMemberDefinition.isVolatile() && (isInterface() || paramMemberDefinition.isFinal())) { paramEnvironment.error(paramMemberDefinition.getWhere(), "volatile.modifier", paramMemberDefinition); paramMemberDefinition.subModifiers(64); }  if (paramMemberDefinition.isFinal() && paramMemberDefinition.getValue() == null && isInterface()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "initializer.needed", paramMemberDefinition); paramMemberDefinition.subModifiers(16); }  if (paramMemberDefinition.getClassDefinition().isInterface() && (paramMemberDefinition.isPrivate() || paramMemberDefinition.isProtected())) { paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.field", paramMemberDefinition); paramMemberDefinition.subModifiers(6); paramMemberDefinition.addModifiers(1); }  }  if (!paramMemberDefinition.isInitializer()) { MemberDefinition memberDefinition = getFirstMatch(paramMemberDefinition.getName()); for (; memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) { if (paramMemberDefinition.isVariable() && memberDefinition.isVariable()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "var.multidef", paramMemberDefinition, memberDefinition); return; }  if (paramMemberDefinition.isInnerClass() && memberDefinition.isInnerClass() && !paramMemberDefinition.getInnerClass().isLocal() && !memberDefinition.getInnerClass().isLocal()) { paramEnvironment.error(paramMemberDefinition.getWhere(), "inner.class.multidef", paramMemberDefinition); return; }  }  }  super.addMember(paramEnvironment, paramMemberDefinition); }
/*      */   public Environment setupEnv(Environment paramEnvironment) { return new Environment(this.toplevelEnv, this); }
/*      */   public boolean reportDeprecated(Environment paramEnvironment) { return false; }
/*  105 */   public SourceClass(Environment paramEnvironment, long paramLong, ClassDeclaration paramClassDeclaration, String paramString, int paramInt, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken, SourceClass paramSourceClass, Identifier paramIdentifier) { super(paramEnvironment.getSource(), paramLong, paramClassDeclaration, paramInt, paramIdentifierToken, paramArrayOfIdentifierToken);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  631 */     this.sourceFileChecked = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     this.supersChecked = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1073 */     this.basicChecking = false;
/* 1074 */     this.basicCheckDone = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1196 */     this.resolving = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1396 */     this.inlinedLocalClass = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1897 */     this.lookup = null; setOuterClass(paramSourceClass); this.toplevelEnv = paramEnvironment; this.documentation = paramString; if (ClassDefinition.containsDeprecated(paramString)) this.modifiers |= 0x40000;  if (isStatic() && paramSourceClass == null) { paramEnvironment.error(paramLong, "static.class", this); this.modifiers &= 0xFFFFFFF7; }  if (isLocal() || (paramSourceClass != null && !paramSourceClass.isTopLevel())) if (isInterface()) { paramEnvironment.error(paramLong, "inner.interface"); } else if (isStatic()) { paramEnvironment.error(paramLong, "static.inner.class", this); this.modifiers &= 0xFFFFFFF7; if (this.innerClassMember != null) this.innerClassMember.subModifiers(8);  }   if (isPrivate() && paramSourceClass == null) { paramEnvironment.error(paramLong, "private.class", this); this.modifiers &= 0xFFFFFFFD; }  if (isProtected() && paramSourceClass == null) { paramEnvironment.error(paramLong, "protected.class", this); this.modifiers &= 0xFFFFFFFB; }  if (!isTopLevel() && !isLocal()) { LocalMember localMember = paramSourceClass.getThisArgument(); UplevelReference uplevelReference = getReference(localMember); setOuterMember(uplevelReference.getLocalField(paramEnvironment)); }  if (paramIdentifier != null) setLocalName(paramIdentifier);  Identifier identifier = getLocalName(); if (identifier != idNull) for (SourceClass sourceClass = paramSourceClass; sourceClass != null; classDefinition = sourceClass.getOuterClass()) { ClassDefinition classDefinition; Identifier identifier1 = sourceClass.getLocalName(); if (identifier.equals(identifier1)) paramEnvironment.error(paramLong, "inner.redefined", identifier);  }   }
/*      */   public void noteUsedBy(ClassDefinition paramClassDefinition, long paramLong, Environment paramEnvironment) { super.noteUsedBy(paramClassDefinition, paramLong, paramEnvironment); ClassDefinition classDefinition = this; while (classDefinition.isInnerClass()) classDefinition = classDefinition.getOuterClass();  if (classDefinition.isPublic()) return;  while (paramClassDefinition.isInnerClass()) paramClassDefinition = paramClassDefinition.getOuterClass();  if (classDefinition.getSource().equals(paramClassDefinition.getSource())) return;  ((SourceClass)classDefinition).checkSourceFile(paramEnvironment, paramLong); }
/*      */   public void check(Environment paramEnvironment) throws ClassNotFound { paramEnvironment.dtEnter("SourceClass.check: " + getName()); if (isInsideLocal()) { paramEnvironment.dtEvent("SourceClass.check: INSIDE LOCAL " + getOuterClass().getName()); getOuterClass().check(paramEnvironment); } else { if (isInnerClass()) { paramEnvironment.dtEvent("SourceClass.check: INNER CLASS " + getOuterClass().getName()); ((SourceClass)getOuterClass()).maybeCheck(paramEnvironment); }  Vset vset = new Vset(); Context context = null; paramEnvironment.dtEvent("SourceClass.check: CHECK INTERNAL " + getName()); vset = checkInternal(setupEnv(paramEnvironment), context, vset); }  paramEnvironment.dtExit("SourceClass.check: " + getName()); }
/*      */   private void maybeCheck(Environment paramEnvironment) throws ClassNotFound { paramEnvironment.dtEvent("SourceClass.maybeCheck: " + getName()); ClassDeclaration classDeclaration = getClassDeclaration(); if (classDeclaration.getStatus() == 4) { classDeclaration.setDefinition(this, 5); check(paramEnvironment); }  }
/*      */   private Vset checkInternal(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound { Identifier identifier = getClassDeclaration().getName(); if (paramEnvironment.verbose())
/*      */       paramEnvironment.output("[checking class " + identifier + "]");  this.classContext = paramContext; basicCheck(Context.newEnvironment(paramEnvironment, paramContext)); ClassDeclaration classDeclaration = getSuperClass(); if (classDeclaration != null) { long l = getWhere(); l = IdentifierToken.getWhere(this.superClassId, l); paramEnvironment.resolveExtendsByName(l, this, classDeclaration.getName()); }  for (byte b = 0; b < this.interfaces.length; b++) { ClassDeclaration classDeclaration1 = this.interfaces[b]; long l = getWhere(); if (this.interfaceIds != null && this.interfaceIds.length == this.interfaces.length)
/*      */         l = IdentifierToken.getWhere(this.interfaceIds[b], l);  paramEnvironment.resolveExtendsByName(l, this, classDeclaration1.getName()); }  if (!isInnerClass() && !isInsideLocal()) { Identifier identifier1 = identifier.getName(); try { Imports imports = this.toplevelEnv.getImports(); Identifier identifier2 = imports.resolve(paramEnvironment, identifier1); if (identifier2 != getName())
/*      */           paramEnvironment.error(this.where, "class.multidef.import", identifier1, identifier2);  } catch (AmbiguousClass ambiguousClass) { Identifier identifier2 = (ambiguousClass.name1 != getName()) ? ambiguousClass.name1 : ambiguousClass.name2; paramEnvironment.error(this.where, "class.multidef.import", identifier1, identifier2); } catch (ClassNotFound classNotFound) {} if (isPublic())
/* 1905 */         checkSourceFile(paramEnvironment, getWhere());  }  paramVset = checkMembers(paramEnvironment, paramContext, paramVset); return paramVset; } public MemberDefinition getClassLiteralLookup(long paramLong) { if (this.lookup != null) {
/* 1906 */       return this.lookup;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1913 */     if (this.outerClass != null) {
/* 1914 */       this.lookup = this.outerClass.getClassLiteralLookup(paramLong);
/* 1915 */       return this.lookup;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1920 */     SourceClass sourceClass = this;
/* 1921 */     boolean bool = false;
/*      */     
/* 1923 */     if (isInterface()) {
/*      */ 
/*      */       
/* 1926 */       sourceClass = findLookupContext();
/* 1927 */       if (sourceClass == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1934 */         bool = true;
/* 1935 */         IdentifierToken identifierToken1 = new IdentifierToken(paramLong, idJavaLangObject);
/*      */         
/* 1937 */         IdentifierToken[] arrayOfIdentifierToken1 = new IdentifierToken[0];
/* 1938 */         IdentifierToken identifierToken2 = new IdentifierToken(paramLong, idNull);
/* 1939 */         int i = 589833;
/*      */         
/* 1941 */         sourceClass = (SourceClass)this.toplevelEnv.makeClassDefinition(this.toplevelEnv, paramLong, identifierToken2, null, i, identifierToken1, arrayOfIdentifierToken1, this);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1949 */     Identifier identifier1 = Identifier.lookup("class$");
/* 1950 */     Type[] arrayOfType = { Type.tString };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1979 */     long l = sourceClass.getWhere();
/* 1980 */     IdentifierToken identifierToken = new IdentifierToken(l, identifier1);
/* 1981 */     IdentifierExpression identifierExpression2 = new IdentifierExpression(identifierToken);
/* 1982 */     Expression[] arrayOfExpression1 = { (Expression)identifierExpression2 };
/* 1983 */     Identifier identifier2 = Identifier.lookup("forName");
/* 1984 */     MethodExpression methodExpression2 = new MethodExpression(l, (Expression)new TypeExpression(l, Type.tClassDesc), identifier2, arrayOfExpression1);
/*      */     
/* 1986 */     ReturnStatement returnStatement = new ReturnStatement(l, (Expression)methodExpression2);
/*      */ 
/*      */     
/* 1989 */     Identifier identifier3 = Identifier.lookup("java.lang.ClassNotFoundException");
/*      */     
/* 1991 */     Identifier identifier4 = Identifier.lookup("java.lang.NoClassDefFoundError");
/* 1992 */     Type type1 = Type.tClass(identifier3);
/* 1993 */     Type type2 = Type.tClass(identifier4);
/* 1994 */     Identifier identifier5 = Identifier.lookup("getMessage");
/* 1995 */     IdentifierExpression identifierExpression1 = new IdentifierExpression(l, identifier2);
/* 1996 */     MethodExpression methodExpression1 = new MethodExpression(l, (Expression)identifierExpression1, identifier5, new Expression[0]);
/* 1997 */     Expression[] arrayOfExpression2 = { (Expression)methodExpression1 };
/* 1998 */     NewInstanceExpression newInstanceExpression = new NewInstanceExpression(l, (Expression)new TypeExpression(l, type2), arrayOfExpression2);
/* 1999 */     CatchStatement catchStatement = new CatchStatement(l, (Expression)new TypeExpression(l, type1), new IdentifierToken(identifier2), (Statement)new ThrowStatement(l, (Expression)newInstanceExpression));
/*      */ 
/*      */     
/* 2002 */     Statement[] arrayOfStatement = { (Statement)catchStatement };
/* 2003 */     TryStatement tryStatement = new TryStatement(l, (Statement)returnStatement, arrayOfStatement);
/*      */     
/* 2005 */     Type type3 = Type.tMethod(Type.tClassDesc, arrayOfType);
/* 2006 */     IdentifierToken[] arrayOfIdentifierToken = { identifierToken };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2011 */     this.lookup = this.toplevelEnv.makeMemberDefinition(this.toplevelEnv, l, sourceClass, null, 524296, type3, identifier1, arrayOfIdentifierToken, null, tryStatement);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2019 */     if (bool) {
/* 2020 */       if (sourceClass.getClassDeclaration().getStatus() == 5) {
/* 2021 */         throw new CompilerError("duplicate check");
/*      */       }
/* 2023 */       sourceClass.getClassDeclaration().setDefinition(sourceClass, 4);
/* 2024 */       Expression[] arrayOfExpression = new Expression[0];
/* 2025 */       Type[] arrayOfType1 = new Type[0];
/*      */       
/*      */       try {
/* 2028 */         ClassDefinition classDefinition = this.toplevelEnv.getClassDefinition(idJavaLangObject);
/* 2029 */         sourceClass.checkLocalClass(this.toplevelEnv, null, new Vset(), classDefinition, arrayOfExpression, arrayOfType1);
/*      */       }
/* 2031 */       catch (ClassNotFound classNotFound) {}
/*      */     } 
/*      */     
/* 2034 */     return this.lookup; }
/*      */   public void checkSourceFile(Environment paramEnvironment, long paramLong) { if (this.sourceFileChecked) return;  this.sourceFileChecked = true; String str1 = getName().getName() + ".java"; String str2 = ((ClassFile)getSource()).getName(); if (!str2.equals(str1)) if (isPublic()) { paramEnvironment.error(paramLong, "public.class.file", this, str1); } else { paramEnvironment.error(paramLong, "warn.package.class.file", this, str2, str1); }   }
/*      */   public ClassDeclaration getSuperClass(Environment paramEnvironment) { paramEnvironment.dtEnter("SourceClass.getSuperClass: " + this); if (this.superClass == null && this.superClassId != null && !this.supersChecked) resolveTypeStructure(paramEnvironment);  paramEnvironment.dtExit("SourceClass.getSuperClass: " + this); return this.superClass; }
/*      */   private void checkSupers(Environment paramEnvironment) throws ClassNotFound { this.supersCheckStarted = true; paramEnvironment.dtEnter("SourceClass.checkSupers: " + this); if (isInterface()) { if (isFinal()) { Identifier identifier = getClassDeclaration().getName(); paramEnvironment.error(getWhere(), "final.intf", identifier); }  } else if (getSuperClass(paramEnvironment) != null) { long l = getWhere(); l = IdentifierToken.getWhere(this.superClassId, l); try { ClassDefinition classDefinition = getSuperClass().getClassDefinition(paramEnvironment); classDefinition.resolveTypeStructure(paramEnvironment); if (!extendsCanAccess(paramEnvironment, getSuperClass())) { paramEnvironment.error(l, "cant.access.class", getSuperClass()); this.superClass = null; } else if (classDefinition.isFinal()) { paramEnvironment.error(l, "super.is.final", getSuperClass()); this.superClass = null; } else if (classDefinition.isInterface()) { paramEnvironment.error(l, "super.is.intf", getSuperClass()); this.superClass = null; } else if (superClassOf(paramEnvironment, getSuperClass())) { paramEnvironment.error(l, "cyclic.super"); this.superClass = null; } else { classDefinition.noteUsedBy(this, l, paramEnvironment); }  if (this.superClass == null) { classDefinition = null; } else { ClassDefinition classDefinition1 = classDefinition; while (true) { if (enclosingClassOf(classDefinition1)) { paramEnvironment.error(l, "super.is.inner"); this.superClass = null; break; }  ClassDeclaration classDeclaration = classDefinition1.getSuperClass(paramEnvironment); if (classDeclaration == null) break;  classDefinition1 = classDeclaration.getClassDefinition(paramEnvironment); }  }  } catch (ClassNotFound classNotFound) { try { paramEnvironment.resolve(classNotFound.name); } catch (AmbiguousClass ambiguousClass) { paramEnvironment.error(l, "ambig.class", ambiguousClass.name1, ambiguousClass.name2); this.superClass = null; } catch (ClassNotFound classNotFound1) {} paramEnvironment.error(l, "super.not.found", classNotFound.name, this); this.superClass = null; }  } else { if (isAnonymous()) throw new CompilerError("anonymous super");  if (!getName().equals(idJavaLangObject)) throw new CompilerError("unresolved super");  }  this.supersChecked = true; byte b = 0; while (true) { if (b < this.interfaces.length) { ClassDeclaration classDeclaration = this.interfaces[b]; long l = getWhere(); if (this.interfaceIds != null && this.interfaceIds.length == this.interfaces.length) l = IdentifierToken.getWhere(this.interfaceIds[b], l);  try { ClassDefinition classDefinition = classDeclaration.getClassDefinition(paramEnvironment); classDefinition.resolveTypeStructure(paramEnvironment); if (!extendsCanAccess(paramEnvironment, classDeclaration)) { paramEnvironment.error(l, "cant.access.class", classDeclaration); } else if (!classDeclaration.getClassDefinition(paramEnvironment).isInterface()) { paramEnvironment.error(l, "not.intf", classDeclaration); } else if (isInterface() && implementedBy(paramEnvironment, classDeclaration)) { paramEnvironment.error(l, "cyclic.intf", classDeclaration); } else { classDefinition.noteUsedBy(this, l, paramEnvironment); b++; }  } catch (ClassNotFound classNotFound) { try { paramEnvironment.resolve(classNotFound.name); } catch (AmbiguousClass ambiguousClass) { paramEnvironment.error(l, "ambig.class", ambiguousClass.name1, ambiguousClass.name2); this.superClass = null; } catch (ClassNotFound classNotFound1) {} paramEnvironment.error(l, "intf.not.found", classNotFound.name, this); this.superClass = null; }  ClassDeclaration[] arrayOfClassDeclaration = new ClassDeclaration[this.interfaces.length - 1]; System.arraycopy(this.interfaces, 0, arrayOfClassDeclaration, 0, b); System.arraycopy(this.interfaces, b + 1, arrayOfClassDeclaration, b, arrayOfClassDeclaration.length - b); this.interfaces = arrayOfClassDeclaration; b--; } else { break; }  b++; }  paramEnvironment.dtExit("SourceClass.checkSupers: " + this); }
/*      */   private Vset checkMembers(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound { if (getError()) return paramVset;  MemberDefinition memberDefinition1 = getFirstMember(); for (; memberDefinition1 != null; memberDefinition1 = memberDefinition1.getNextMember()) { if (memberDefinition1.isInnerClass()) { SourceClass sourceClass = (SourceClass)memberDefinition1.getInnerClass(); if (sourceClass.isMember()) sourceClass.basicCheck(paramEnvironment);  }  }  if (isFinal() && isAbstract()) paramEnvironment.error(this.where, "final.abstract", getName().getName());  if (!isInterface() && !isAbstract() && mustBeAbstract(paramEnvironment)) { this.modifiers |= 0x400; Iterator<MemberDefinition> iterator = getPermanentlyAbstractMethods(); while (iterator.hasNext()) { MemberDefinition memberDefinition = iterator.next(); paramEnvironment.error(this.where, "abstract.class.cannot.override", getClassDeclaration(), memberDefinition, memberDefinition.getDefiningClassDeclaration()); }  iterator = getMethods(paramEnvironment); while (iterator.hasNext()) { MemberDefinition memberDefinition = iterator.next(); if (memberDefinition.isAbstract()) paramEnvironment.error(this.where, "abstract.class", getClassDeclaration(), memberDefinition, memberDefinition.getDefiningClassDeclaration());  }  }  Context context1 = new Context(paramContext); Vset vset1 = paramVset.copy(); Vset vset2 = paramVset.copy(); MemberDefinition memberDefinition2 = getFirstMember(); for (; memberDefinition2 != null; memberDefinition2 = memberDefinition2.getNextMember()) { if (memberDefinition2.isVariable() && memberDefinition2.isBlankFinal()) { int j = context1.declareFieldNumber(memberDefinition2); if (memberDefinition2.isStatic()) { vset2 = vset2.addVarUnassigned(j); vset1 = vset1.addVar(j); } else { vset1 = vset1.addVarUnassigned(j); vset2 = vset2.addVar(j); }  }  }  Context context2 = new Context(context1, this); LocalMember localMember = getThisArgument(); int i = context2.declare(paramEnvironment, localMember); vset1 = vset1.addVar(i); MemberDefinition memberDefinition3 = getFirstMember(); for (; memberDefinition3 != null; memberDefinition3 = memberDefinition3.getNextMember()) { try { if (memberDefinition3.isVariable() || memberDefinition3.isInitializer()) if (memberDefinition3.isStatic()) { vset2 = memberDefinition3.check(paramEnvironment, context1, vset2); } else { vset1 = memberDefinition3.check(paramEnvironment, context2, vset1); }   } catch (ClassNotFound classNotFound) { paramEnvironment.error(memberDefinition3.getWhere(), "class.not.found", classNotFound.name, this); }  }  checkBlankFinals(paramEnvironment, context1, vset2, true); memberDefinition3 = getFirstMember(); for (; memberDefinition3 != null; memberDefinition3 = memberDefinition3.getNextMember()) { try { if (memberDefinition3.isConstructor()) { Vset vset = memberDefinition3.check(paramEnvironment, context1, vset1.copy()); checkBlankFinals(paramEnvironment, context1, vset, false); } else { Vset vset = memberDefinition3.check(paramEnvironment, paramContext, paramVset.copy()); }  } catch (ClassNotFound classNotFound) { paramEnvironment.error(memberDefinition3.getWhere(), "class.not.found", classNotFound.name, this); }  }  getClassDeclaration().setDefinition(this, 5); memberDefinition3 = getFirstMember(); for (; memberDefinition3 != null; memberDefinition3 = memberDefinition3.getNextMember()) { if (memberDefinition3.isInnerClass()) { SourceClass sourceClass = (SourceClass)memberDefinition3.getInnerClass(); if (!sourceClass.isInsideLocal()) sourceClass.maybeCheck(paramEnvironment);  }  }  return paramVset; }
/*      */   private void checkBlankFinals(Environment paramEnvironment, Context paramContext, Vset paramVset, boolean paramBoolean) { for (byte b = 0; b < paramContext.getVarNumber(); b++) { if (!paramVset.testVar(b)) { MemberDefinition memberDefinition = paramContext.getElement(b); if (memberDefinition != null && memberDefinition.isBlankFinal() && memberDefinition.isStatic() == paramBoolean && memberDefinition.getClassDefinition() == this) paramEnvironment.error(memberDefinition.getWhere(), "final.var.not.initialized", memberDefinition.getName());  }  }  }
/*      */   protected void basicCheck(Environment paramEnvironment) throws ClassNotFound { paramEnvironment.dtEnter("SourceClass.basicCheck: " + getName()); super.basicCheck(paramEnvironment); if (this.basicChecking || this.basicCheckDone) { paramEnvironment.dtExit("SourceClass.basicCheck: OK " + getName()); return; }  paramEnvironment.dtEvent("SourceClass.basicCheck: CHECKING " + getName()); this.basicChecking = true; paramEnvironment = setupEnv(paramEnvironment); Imports imports = paramEnvironment.getImports(); if (imports != null) imports.resolve(paramEnvironment);  resolveTypeStructure(paramEnvironment); if (!isInterface()) if (!hasConstructor()) { CompoundStatement compoundStatement = new CompoundStatement(getWhere(), new Statement[0]); Type type = Type.tMethod(Type.tVoid); int i = getModifiers() & (isInnerClass() ? 5 : 1); paramEnvironment.makeMemberDefinition(paramEnvironment, getWhere(), this, null, i, type, idInit, null, null, compoundStatement); }   if (doInheritanceChecks) collectInheritedMethods(paramEnvironment);  this.basicChecking = false; this.basicCheckDone = true; paramEnvironment.dtExit("SourceClass.basicCheck: " + getName()); }
/*      */   protected void addMirandaMethods(Environment paramEnvironment, Iterator<MemberDefinition> paramIterator) { while (paramIterator.hasNext()) { MemberDefinition memberDefinition = paramIterator.next(); addMember(memberDefinition); }  }
/*      */   public void resolveTypeStructure(Environment paramEnvironment) { paramEnvironment.dtEnter("SourceClass.resolveTypeStructure: " + getName()); ClassDefinition classDefinition = getOuterClass(); if (classDefinition != null && classDefinition instanceof SourceClass && !((SourceClass)classDefinition).resolved) ((SourceClass)classDefinition).resolveTypeStructure(paramEnvironment);  if (this.resolved || this.resolving) { paramEnvironment.dtExit("SourceClass.resolveTypeStructure: OK " + getName()); return; }  this.resolving = true; paramEnvironment.dtEvent("SourceClass.resolveTypeStructure: RESOLVING " + getName()); paramEnvironment = setupEnv(paramEnvironment); resolveSupers(paramEnvironment); try { checkSupers(paramEnvironment); } catch (ClassNotFound classNotFound) { paramEnvironment.error(this.where, "class.not.found", classNotFound.name, this); }  MemberDefinition memberDefinition; for (memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (memberDefinition instanceof SourceMember) ((SourceMember)memberDefinition).resolveTypeStructure(paramEnvironment);  }  this.resolving = false; this.resolved = true; for (memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (!memberDefinition.isInitializer() && memberDefinition.isMethod()) for (MemberDefinition memberDefinition1 = memberDefinition; (memberDefinition1 = memberDefinition1.getNextMatch()) != null; ) { if (!memberDefinition1.isMethod()) continue;  if (memberDefinition.getType().equals(memberDefinition1.getType())) { paramEnvironment.error(memberDefinition.getWhere(), "meth.multidef", memberDefinition); continue; }  if (memberDefinition.getType().equalArguments(memberDefinition1.getType())) paramEnvironment.error(memberDefinition.getWhere(), "meth.redef.rettype", memberDefinition, memberDefinition1);  }   }  paramEnvironment.dtExit("SourceClass.resolveTypeStructure: " + getName()); }
/* 2043 */   protected void resolveSupers(Environment paramEnvironment) { paramEnvironment.dtEnter("SourceClass.resolveSupers: " + this); if (this.superClassId != null && this.superClass == null) { this.superClass = resolveSuper(paramEnvironment, this.superClassId); if (this.superClass == getClassDeclaration() && getName().equals(idJavaLangObject)) { this.superClass = null; this.superClassId = null; }  }  if (this.interfaceIds != null && this.interfaces == null) { this.interfaces = new ClassDeclaration[this.interfaceIds.length]; for (byte b = 0; b < this.interfaces.length; b++) { this.interfaces[b] = resolveSuper(paramEnvironment, this.interfaceIds[b]); for (byte b1 = 0; b1 < b; b1++) { if (this.interfaces[b] == this.interfaces[b1]) { Identifier identifier = this.interfaceIds[b].getName(); long l = this.interfaceIds[b1].getWhere(); paramEnvironment.error(l, "intf.repeated", identifier); }  }  }  }  paramEnvironment.dtExit("SourceClass.resolveSupers: " + this); } private ClassDeclaration resolveSuper(Environment paramEnvironment, IdentifierToken paramIdentifierToken) { Identifier identifier = paramIdentifierToken.getName(); paramEnvironment.dtEnter("SourceClass.resolveSuper: " + identifier); if (isInnerClass()) { identifier = this.outerClass.resolveName(paramEnvironment, identifier); } else { identifier = paramEnvironment.resolveName(identifier); }  ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(identifier); paramEnvironment.dtExit("SourceClass.resolveSuper: " + identifier); return classDeclaration; } public Vset checkLocalClass(Environment paramEnvironment, Context paramContext, Vset paramVset, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType) throws ClassNotFound { paramEnvironment = setupEnv(paramEnvironment); if (((paramClassDefinition != null)) != isAnonymous()) throw new CompilerError("resolveAnonymousStructure");  if (isAnonymous()) resolveAnonymousStructure(paramEnvironment, paramClassDefinition, paramArrayOfExpression, paramArrayOfType);  paramVset = checkInternal(paramEnvironment, paramContext, paramVset); return paramVset; } public void inlineLocalClass(Environment paramEnvironment) { MemberDefinition memberDefinition; for (memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if ((!memberDefinition.isVariable() && !memberDefinition.isInitializer()) || memberDefinition.isStatic()) try { ((SourceMember)memberDefinition).inline(paramEnvironment); } catch (ClassNotFound classNotFound) { paramEnvironment.error(memberDefinition.getWhere(), "class.not.found", classNotFound.name, this); }   }  if (getReferencesFrozen() != null && !this.inlinedLocalClass) { this.inlinedLocalClass = true; for (memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (memberDefinition.isConstructor()) ((SourceMember)memberDefinition).addUplevelArguments();  }  }  } public Vset checkInsideClass(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound { if (!isInsideLocal() || isLocal()) throw new CompilerError("checkInsideClass");  return checkInternal(paramEnvironment, paramContext, paramVset); } private static Vector active = new Vector(); private void resolveAnonymousStructure(Environment paramEnvironment, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType) throws ClassNotFound { SuperExpression superExpression; paramEnvironment.dtEvent("SourceClass.resolveAnonymousStructure: " + this + ", super " + paramClassDefinition); if (paramClassDefinition.isInterface()) { byte b = (this.interfaces == null) ? 0 : this.interfaces.length; ClassDeclaration[] arrayOfClassDeclaration = new ClassDeclaration[1 + b]; if (b > 0) { System.arraycopy(this.interfaces, 0, arrayOfClassDeclaration, 1, b); if (this.interfaceIds != null && this.interfaceIds.length == b) { IdentifierToken[] arrayOfIdentifierToken1 = new IdentifierToken[1 + b]; System.arraycopy(this.interfaceIds, 0, arrayOfIdentifierToken1, 1, b); arrayOfIdentifierToken1[0] = new IdentifierToken(paramClassDefinition.getName()); }  }  arrayOfClassDeclaration[0] = paramClassDefinition.getClassDeclaration(); this.interfaces = arrayOfClassDeclaration; paramClassDefinition = this.toplevelEnv.getClassDefinition(idJavaLangObject); }  this.superClass = paramClassDefinition.getClassDeclaration(); if (hasConstructor()) throw new CompilerError("anonymous constructor");  Type type = Type.tMethod(Type.tVoid, paramArrayOfType); IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[paramArrayOfType.length]; byte b1; for (b1 = 0; b1 < arrayOfIdentifierToken.length; b1++) arrayOfIdentifierToken[b1] = new IdentifierToken(paramArrayOfExpression[b1].getWhere(), Identifier.lookup("$" + b1));  b1 = (paramClassDefinition.isTopLevel() || paramClassDefinition.isLocal()) ? 0 : 1; Expression[] arrayOfExpression = new Expression[-b1 + paramArrayOfExpression.length]; for (byte b2 = b1; b2 < paramArrayOfExpression.length; b2++) arrayOfExpression[-b1 + b2] = (Expression)new IdentifierExpression(arrayOfIdentifierToken[b2]);  long l = getWhere(); if (b1 == 0) { superExpression = new SuperExpression(l); } else { superExpression = new SuperExpression(l, (Expression)new IdentifierExpression(arrayOfIdentifierToken[0])); }  MethodExpression methodExpression = new MethodExpression(l, (Expression)superExpression, idInit, arrayOfExpression); Statement[] arrayOfStatement = { (Statement)new ExpressionStatement(l, (Expression)methodExpression) }; CompoundStatement compoundStatement = new CompoundStatement(l, arrayOfStatement); int i = 524288; paramEnvironment.makeMemberDefinition(paramEnvironment, l, this, null, i, type, idInit, arrayOfIdentifierToken, null, compoundStatement); } private static int[] classModifierBits = new int[] { 1, 2, 4, 8, 16, 512, 1024, 32, 65536, 131072, 2097152, 2048 }; private static String[] classModifierNames = new String[] { "PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "INTERFACE", "ABSTRACT", "SUPER", "ANONYMOUS", "LOCAL", 
/*      */       "STRICTFP", "STRICT" }; private MemberDefinition lookup;
/*      */   static String classModifierString(int paramInt) { String str = ""; for (byte b = 0; b < classModifierBits.length; b++) { if ((paramInt & classModifierBits[b]) != 0) { str = str + " " + classModifierNames[b]; paramInt &= classModifierBits[b] ^ 0xFFFFFFFF; }  }  if (paramInt != 0) str = str + " ILLEGAL:" + Integer.toHexString(paramInt);  return str; }
/*      */   public MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean) { return getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, false, paramBoolean); }
/*      */   public MemberDefinition getUpdateMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean) { if (!paramMemberDefinition.isVariable()) throw new CompilerError("method");  return getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, true, paramBoolean); }
/*      */   private MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean1, boolean paramBoolean2) { Identifier identifier; Type[] arrayOfType; AssignExpression assignExpression; MethodExpression methodExpression; ReturnStatement returnStatement; boolean bool1 = paramMemberDefinition.isStatic(); boolean bool2 = paramMemberDefinition.isMethod(); MemberDefinition memberDefinition; for (memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (memberDefinition.getAccessMethodTarget() == paramMemberDefinition) { if (bool2 && memberDefinition.isSuperAccessMethod() == paramBoolean2) break;  int k = (memberDefinition.getType().getArgumentTypes()).length; if (k == (bool1 ? 0 : 1)) break;  }  }  if (memberDefinition != null) { if (!paramBoolean1) return memberDefinition;  MemberDefinition memberDefinition1 = memberDefinition.getAccessUpdateMember(); if (memberDefinition1 != null) return memberDefinition1;  } else if (paramBoolean1) { memberDefinition = getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, false, paramBoolean2); }  Type type1 = null; if (paramMemberDefinition.isConstructor()) { identifier = idInit; SourceClass sourceClass = (SourceClass)getTopClass(); type1 = sourceClass.dummyArgumentType; if (type1 == null) { IdentifierToken identifierToken1 = new IdentifierToken(0L, idJavaLangObject); IdentifierToken[] arrayOfIdentifierToken1 = new IdentifierToken[0]; IdentifierToken identifierToken2 = new IdentifierToken(0L, idNull); int k = 589832; if (sourceClass.isInterface()) k |= 0x1;  ClassDefinition classDefinition = this.toplevelEnv.makeClassDefinition(this.toplevelEnv, 0L, identifierToken2, null, k, identifierToken1, arrayOfIdentifierToken1, sourceClass); classDefinition.getClassDeclaration().setDefinition(classDefinition, 4); Expression[] arrayOfExpression1 = new Expression[0]; Type[] arrayOfType1 = new Type[0]; try { ClassDefinition classDefinition1 = this.toplevelEnv.getClassDefinition(idJavaLangObject); classDefinition.checkLocalClass(this.toplevelEnv, null, new Vset(), classDefinition1, arrayOfExpression1, arrayOfType1); } catch (ClassNotFound classNotFound) {} type1 = classDefinition.getType(); sourceClass.dummyArgumentType = type1; }  } else { for (byte b1 = 0;; b1++) { identifier = Identifier.lookup("access$" + b1); if (getFirstMatch(identifier) == null) break;  }  }  Type type2 = paramMemberDefinition.getType(); if (bool1) { if (!bool2) { if (!paramBoolean1) { Type[] arrayOfType1 = new Type[0]; arrayOfType = arrayOfType1; type2 = Type.tMethod(type2); } else { Type[] arrayOfType1 = { type2 }; arrayOfType = arrayOfType1; type2 = Type.tMethod(Type.tVoid, arrayOfType); }  } else { arrayOfType = type2.getArgumentTypes(); }  } else { Type type = getType(); if (!bool2) { if (!paramBoolean1) { Type[] arrayOfType1 = { type }; arrayOfType = arrayOfType1; type2 = Type.tMethod(type2, arrayOfType); } else { Type[] arrayOfType1 = { type, type2 }; arrayOfType = arrayOfType1; type2 = Type.tMethod(Type.tVoid, arrayOfType); }  } else { Type[] arrayOfType1 = type2.getArgumentTypes(); int k = arrayOfType1.length; if (paramMemberDefinition.isConstructor()) { LocalMember localMember = ((SourceMember)paramMemberDefinition).getOuterThisArg(); if (localMember != null) { if (arrayOfType1[0] != localMember.getType()) throw new CompilerError("misplaced outer this");  arrayOfType = new Type[k]; arrayOfType[0] = type1; for (byte b1 = 1; b1 < k; b1++) arrayOfType[b1] = arrayOfType1[b1];  } else { arrayOfType = new Type[k + 1]; arrayOfType[0] = type1; for (byte b1 = 0; b1 < k; b1++) arrayOfType[b1 + 1] = arrayOfType1[b1];  }  } else { arrayOfType = new Type[k + 1]; arrayOfType[0] = type; for (byte b1 = 0; b1 < k; b1++) arrayOfType[b1 + 1] = arrayOfType1[b1];  }  type2 = Type.tMethod(type2.getReturnType(), arrayOfType); }  }  int i = arrayOfType.length; long l = paramMemberDefinition.getWhere(); IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[i]; for (byte b = 0; b < i; b++) arrayOfIdentifierToken[b] = new IdentifierToken(l, Identifier.lookup("$" + b));  IdentifierExpression identifierExpression = null; ThisExpression thisExpression = null; Expression[] arrayOfExpression = null; if (bool1) { arrayOfExpression = new Expression[i]; for (byte b1 = 0; b1 < i; b1++) arrayOfExpression[b1] = (Expression)new IdentifierExpression(arrayOfIdentifierToken[b1]);  } else { IdentifierExpression identifierExpression1; if (paramMemberDefinition.isConstructor()) { thisExpression = new ThisExpression(l); arrayOfExpression = new Expression[i - 1]; for (byte b1 = 1; b1 < i; b1++) arrayOfExpression[b1 - 1] = (Expression)new IdentifierExpression(arrayOfIdentifierToken[b1]);  } else { identifierExpression1 = new IdentifierExpression(arrayOfIdentifierToken[0]); arrayOfExpression = new Expression[i - 1]; for (byte b1 = 1; b1 < i; b1++) arrayOfExpression[b1 - 1] = (Expression)new IdentifierExpression(arrayOfIdentifierToken[b1]);  }  identifierExpression = identifierExpression1; }  if (!bool2) { FieldExpression fieldExpression = new FieldExpression(l, (Expression)identifierExpression, paramMemberDefinition); if (paramBoolean1) assignExpression = new AssignExpression(l, (Expression)fieldExpression, arrayOfExpression[0]);  } else { methodExpression = new MethodExpression(l, (Expression)assignExpression, paramMemberDefinition, arrayOfExpression, paramBoolean2); }  if (type2.getReturnType().isType(11)) { ExpressionStatement expressionStatement = new ExpressionStatement(l, (Expression)methodExpression); } else { returnStatement = new ReturnStatement(l, (Expression)methodExpression); }  Statement[] arrayOfStatement = { (Statement)returnStatement }; CompoundStatement compoundStatement = new CompoundStatement(l, arrayOfStatement); int j = 524288; if (!paramMemberDefinition.isConstructor()) j |= 0x8;  SourceMember sourceMember = (SourceMember)paramEnvironment.makeMemberDefinition(paramEnvironment, l, this, null, j, type2, identifier, arrayOfIdentifierToken, paramMemberDefinition.getExceptionIds(), compoundStatement); sourceMember.setExceptions(paramMemberDefinition.getExceptions(paramEnvironment)); sourceMember.setAccessMethodTarget(paramMemberDefinition); if (paramBoolean1) memberDefinition.setAccessUpdateMember(sourceMember);  sourceMember.setIsSuperAccessMethod(paramBoolean2); Context context = sourceMember.getClassDefinition().getClassContext(); if (context != null) try { sourceMember.check(paramEnvironment, context, new Vset()); } catch (ClassNotFound classNotFound) { paramEnvironment.error(l, "class.not.found", classNotFound.name, this); }   return sourceMember; }
/*      */   SourceClass findLookupContext() { MemberDefinition memberDefinition = getFirstMember(); for (; memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (memberDefinition.isInnerClass()) { SourceClass sourceClass = (SourceClass)memberDefinition.getInnerClass(); if (!sourceClass.isInterface()) return sourceClass;  }  }  memberDefinition = getFirstMember(); for (; memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) { if (memberDefinition.isInnerClass()) { SourceClass sourceClass = ((SourceClass)memberDefinition.getInnerClass()).findLookupContext(); if (sourceClass != null) return sourceClass;  }  }  return null; }
/* 2050 */   public void compile(OutputStream paramOutputStream) throws InterruptedException, IOException { Environment environment = this.toplevelEnv;
/* 2051 */     synchronized (active) {
/* 2052 */       while (active.contains(getName())) {
/* 2053 */         active.wait();
/*      */       }
/* 2055 */       active.addElement(getName());
/*      */     } 
/*      */     
/*      */     try {
/* 2059 */       compileClass(environment, paramOutputStream);
/* 2060 */     } catch (ClassNotFound classNotFound) {
/* 2061 */       throw new CompilerError(classNotFound);
/*      */     } finally {
/* 2063 */       synchronized (active) {
/* 2064 */         active.removeElement(getName());
/* 2065 */         active.notifyAll();
/*      */       } 
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void assertModifiers(int paramInt1, int paramInt2) {
/* 2078 */     if ((paramInt1 & paramInt2) != paramInt2) {
/* 2079 */       throw new CompilerError("illegal class modifiers");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void compileClass(Environment paramEnvironment, OutputStream paramOutputStream) throws IOException, ClassNotFound {
/* 2085 */     Vector<CompilerMember> vector1 = new Vector();
/* 2086 */     Vector<CompilerMember> vector2 = new Vector();
/* 2087 */     Vector<SourceClass> vector = new Vector();
/* 2088 */     CompilerMember compilerMember = new CompilerMember(new MemberDefinition(getWhere(), this, 8, Type.tMethod(Type.tVoid), idClassInit, null, null), new Assembler());
/* 2089 */     Context context = new Context((Context)null, compilerMember.field);
/*      */     
/* 2091 */     for (SourceClass sourceClass = this; sourceClass.isInnerClass(); classDefinition = sourceClass.getOuterClass()) {
/* 2092 */       ClassDefinition classDefinition; vector.addElement(sourceClass);
/*      */     } 
/*      */     
/* 2095 */     int i = vector.size(); int j;
/* 2096 */     for (j = i; --j >= 0;)
/* 2097 */       vector.addElement(vector.elementAt(j)); 
/* 2098 */     for (j = i; --j >= 0;) {
/* 2099 */       vector.removeElementAt(j);
/*      */     }
/*      */ 
/*      */     
/* 2103 */     boolean bool1 = isDeprecated();
/* 2104 */     boolean bool2 = isSynthetic();
/* 2105 */     int k = 0;
/* 2106 */     int m = 0;
/*      */ 
/*      */     
/* 2109 */     SourceMember sourceMember = (SourceMember)getFirstMember();
/* 2110 */     for (; sourceMember != null; 
/* 2111 */       sourceMember = (SourceMember)sourceMember.getNextMember()) {
/*      */ 
/*      */ 
/*      */       
/* 2115 */       bool1 |= sourceMember.isDeprecated();
/* 2116 */       bool2 |= sourceMember.isSynthetic();
/*      */       
/*      */       try {
/* 2119 */         if (sourceMember.isMethod()) {
/* 2120 */           m |= 
/* 2121 */             ((sourceMember.getExceptions(paramEnvironment)).length > 0) ? 1 : 0;
/*      */           
/* 2123 */           if (sourceMember.isInitializer()) {
/* 2124 */             if (sourceMember.isStatic()) {
/* 2125 */               sourceMember.code(paramEnvironment, compilerMember.asm);
/*      */             }
/*      */           } else {
/* 2128 */             CompilerMember compilerMember1 = new CompilerMember(sourceMember, new Assembler());
/*      */             
/* 2130 */             sourceMember.code(paramEnvironment, compilerMember1.asm);
/* 2131 */             vector2.addElement(compilerMember1);
/*      */           } 
/* 2133 */         } else if (sourceMember.isInnerClass()) {
/* 2134 */           vector.addElement(sourceMember.getInnerClass());
/* 2135 */         } else if (sourceMember.isVariable()) {
/* 2136 */           sourceMember.inline(paramEnvironment);
/* 2137 */           CompilerMember compilerMember1 = new CompilerMember(sourceMember, null);
/* 2138 */           vector1.addElement(compilerMember1);
/* 2139 */           if (sourceMember.isStatic()) {
/* 2140 */             sourceMember.codeInit(paramEnvironment, context, compilerMember.asm);
/*      */           }
/*      */           
/* 2143 */           k |= 
/* 2144 */             (sourceMember.getInitialValue() != null) ? 1 : 0;
/*      */         } 
/* 2146 */       } catch (CompilerError compilerError) {
/* 2147 */         compilerError.printStackTrace();
/* 2148 */         paramEnvironment.error(sourceMember, 0L, "generic", sourceMember
/* 2149 */             .getClassDeclaration() + ":" + sourceMember + "@" + compilerError
/* 2150 */             .toString(), null, null);
/*      */       } 
/*      */     } 
/* 2153 */     if (!compilerMember.asm.empty()) {
/* 2154 */       compilerMember.asm.add(getWhere(), 177, true);
/* 2155 */       vector2.addElement(compilerMember);
/*      */     } 
/*      */ 
/*      */     
/* 2159 */     if (getNestError()) {
/*      */       return;
/*      */     }
/*      */     
/* 2163 */     byte b1 = 0;
/*      */ 
/*      */     
/* 2166 */     if (vector2.size() > 0) {
/* 2167 */       this.tab.put("Code");
/*      */     }
/* 2169 */     if (k != 0) {
/* 2170 */       this.tab.put("ConstantValue");
/*      */     }
/*      */     
/* 2173 */     String str1 = null;
/* 2174 */     if (paramEnvironment.debug_source()) {
/* 2175 */       str1 = ((ClassFile)getSource()).getName();
/* 2176 */       this.tab.put("SourceFile");
/* 2177 */       this.tab.put(str1);
/* 2178 */       b1++;
/*      */     } 
/*      */     
/* 2181 */     if (m != 0) {
/* 2182 */       this.tab.put("Exceptions");
/*      */     }
/*      */     
/* 2185 */     if (paramEnvironment.debug_lines()) {
/* 2186 */       this.tab.put("LineNumberTable");
/*      */     }
/* 2188 */     if (bool1) {
/* 2189 */       this.tab.put("Deprecated");
/* 2190 */       if (isDeprecated()) {
/* 2191 */         b1++;
/*      */       }
/*      */     } 
/* 2194 */     if (bool2) {
/* 2195 */       this.tab.put("Synthetic");
/* 2196 */       if (isSynthetic()) {
/* 2197 */         b1++;
/*      */       }
/*      */     } 
/*      */     
/* 2201 */     if (paramEnvironment.coverage()) {
/* 2202 */       b1 += 2;
/* 2203 */       this.tab.put("AbsoluteSourcePath");
/* 2204 */       this.tab.put("TimeStamp");
/* 2205 */       this.tab.put("CoverageTable");
/*      */     } 
/*      */     
/* 2208 */     if (paramEnvironment.debug_vars()) {
/* 2209 */       this.tab.put("LocalVariableTable");
/*      */     }
/* 2211 */     if (vector.size() > 0) {
/* 2212 */       this.tab.put("InnerClasses");
/* 2213 */       b1++;
/*      */     } 
/*      */ 
/*      */     
/* 2217 */     String str2 = "";
/* 2218 */     long l = 0L;
/*      */     
/* 2220 */     if (paramEnvironment.coverage()) {
/* 2221 */       str2 = getAbsoluteName();
/* 2222 */       l = System.currentTimeMillis();
/* 2223 */       this.tab.put(str2);
/*      */     } 
/*      */     
/* 2226 */     this.tab.put(getClassDeclaration());
/* 2227 */     if (getSuperClass() != null) {
/* 2228 */       this.tab.put(getSuperClass());
/*      */     }
/* 2230 */     for (byte b2 = 0; b2 < this.interfaces.length; b2++) {
/* 2231 */       this.tab.put(this.interfaces[b2]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2239 */     CompilerMember[] arrayOfCompilerMember = new CompilerMember[vector2.size()];
/* 2240 */     vector2.copyInto((Object[])arrayOfCompilerMember);
/* 2241 */     Arrays.sort((Object[])arrayOfCompilerMember);
/* 2242 */     for (byte b3 = 0; b3 < vector2.size(); b3++) {
/* 2243 */       vector2.setElementAt(arrayOfCompilerMember[b3], b3);
/*      */     }
/*      */     Enumeration<CompilerMember> enumeration1;
/* 2246 */     for (enumeration1 = vector2.elements(); enumeration1.hasMoreElements(); ) {
/* 2247 */       CompilerMember compilerMember1 = enumeration1.nextElement();
/*      */       try {
/* 2249 */         compilerMember1.asm.optimize(paramEnvironment);
/* 2250 */         compilerMember1.asm.collect(paramEnvironment, compilerMember1.field, this.tab);
/* 2251 */         this.tab.put(compilerMember1.name);
/* 2252 */         this.tab.put(compilerMember1.sig);
/* 2253 */         ClassDeclaration[] arrayOfClassDeclaration = compilerMember1.field.getExceptions(paramEnvironment);
/* 2254 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 2255 */           this.tab.put(arrayOfClassDeclaration[b]);
/*      */         }
/* 2257 */       } catch (Exception exception) {
/* 2258 */         exception.printStackTrace();
/* 2259 */         paramEnvironment.error(compilerMember1.field, -1L, "generic", compilerMember1.field.getName() + "@" + exception.toString(), null, null);
/* 2260 */         compilerMember1.asm.listing(System.out);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2265 */     for (enumeration1 = vector1.elements(); enumeration1.hasMoreElements(); ) {
/* 2266 */       CompilerMember compilerMember1 = enumeration1.nextElement();
/* 2267 */       this.tab.put(compilerMember1.name);
/* 2268 */       this.tab.put(compilerMember1.sig);
/*      */       
/* 2270 */       Object object = compilerMember1.field.getInitialValue();
/* 2271 */       if (object != null) {
/* 2272 */         this.tab.put((object instanceof String) ? new StringExpression(compilerMember1.field.getWhere(), (String)object) : object);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2277 */     enumeration1 = (Enumeration)vector.elements();
/* 2278 */     while (enumeration1.hasMoreElements()) {
/* 2279 */       ClassDefinition classDefinition = (ClassDefinition)enumeration1.nextElement();
/* 2280 */       this.tab.put(classDefinition.getClassDeclaration());
/*      */ 
/*      */ 
/*      */       
/* 2284 */       if (!classDefinition.isLocal()) {
/* 2285 */         ClassDefinition classDefinition1 = classDefinition.getOuterClass();
/* 2286 */         this.tab.put(classDefinition1.getClassDeclaration());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2291 */       Identifier identifier = classDefinition.getLocalName();
/* 2292 */       if (identifier != idNull) {
/* 2293 */         this.tab.put(identifier.toString());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2298 */     DataOutputStream dataOutputStream1 = new DataOutputStream(paramOutputStream);
/* 2299 */     dataOutputStream1.writeInt(-889275714);
/* 2300 */     dataOutputStream1.writeShort(this.toplevelEnv.getMinorVersion());
/* 2301 */     dataOutputStream1.writeShort(this.toplevelEnv.getMajorVersion());
/* 2302 */     this.tab.write(paramEnvironment, dataOutputStream1);
/*      */ 
/*      */     
/* 2305 */     int n = getModifiers() & 0x200611;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2319 */     if (isInterface()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2326 */       assertModifiers(n, 1024);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2331 */       n |= 0x20;
/*      */     } 
/*      */ 
/*      */     
/* 2335 */     if (this.outerClass != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2340 */       if (isProtected()) n |= 0x1;
/*      */       
/* 2342 */       if (this.outerClass.isInterface()) {
/* 2343 */         assertModifiers(n, 1);
/*      */       }
/*      */     } 
/*      */     
/* 2347 */     dataOutputStream1.writeShort(n);
/*      */     
/* 2349 */     if (paramEnvironment.dumpModifiers()) {
/* 2350 */       Identifier identifier1 = getName();
/*      */       
/* 2352 */       Identifier identifier2 = Identifier.lookup(identifier1.getQualifier(), identifier1.getFlatName());
/* 2353 */       System.out.println();
/* 2354 */       System.out.println("CLASSFILE  " + identifier2);
/* 2355 */       System.out.println("---" + classModifierString(n));
/*      */     } 
/*      */     
/* 2358 */     dataOutputStream1.writeShort(this.tab.index(getClassDeclaration()));
/* 2359 */     dataOutputStream1.writeShort((getSuperClass() != null) ? this.tab.index(getSuperClass()) : 0);
/* 2360 */     dataOutputStream1.writeShort(this.interfaces.length);
/* 2361 */     for (byte b4 = 0; b4 < this.interfaces.length; b4++) {
/* 2362 */       dataOutputStream1.writeShort(this.tab.index(this.interfaces[b4]));
/*      */     }
/*      */ 
/*      */     
/* 2366 */     ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream(256);
/* 2367 */     ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(256);
/* 2368 */     DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream1);
/*      */     
/* 2370 */     dataOutputStream1.writeShort(vector1.size()); Enumeration<CompilerMember> enumeration2;
/* 2371 */     for (enumeration2 = vector1.elements(); enumeration2.hasMoreElements(); ) {
/* 2372 */       CompilerMember compilerMember1 = enumeration2.nextElement();
/* 2373 */       Object object = compilerMember1.field.getInitialValue();
/*      */       
/* 2375 */       dataOutputStream1.writeShort(compilerMember1.field.getModifiers() & 0xDF);
/* 2376 */       dataOutputStream1.writeShort(this.tab.index(compilerMember1.name));
/* 2377 */       dataOutputStream1.writeShort(this.tab.index(compilerMember1.sig));
/*      */       
/* 2379 */       int i1 = (object != null) ? 1 : 0;
/* 2380 */       boolean bool3 = compilerMember1.field.isDeprecated();
/* 2381 */       boolean bool4 = compilerMember1.field.isSynthetic();
/* 2382 */       i1 += (bool3 ? 1 : 0) + (bool4 ? 1 : 0);
/*      */       
/* 2384 */       dataOutputStream1.writeShort(i1);
/* 2385 */       if (object != null) {
/* 2386 */         dataOutputStream1.writeShort(this.tab.index("ConstantValue"));
/* 2387 */         dataOutputStream1.writeInt(2);
/* 2388 */         dataOutputStream1.writeShort(this.tab.index((object instanceof String) ? new StringExpression(compilerMember1.field.getWhere(), (String)object) : object));
/*      */       } 
/* 2390 */       if (bool3) {
/* 2391 */         dataOutputStream1.writeShort(this.tab.index("Deprecated"));
/* 2392 */         dataOutputStream1.writeInt(0);
/*      */       } 
/* 2394 */       if (bool4) {
/* 2395 */         dataOutputStream1.writeShort(this.tab.index("Synthetic"));
/* 2396 */         dataOutputStream1.writeInt(0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2402 */     dataOutputStream1.writeShort(vector2.size());
/* 2403 */     for (enumeration2 = vector2.elements(); enumeration2.hasMoreElements(); ) {
/* 2404 */       CompilerMember compilerMember1 = enumeration2.nextElement();
/*      */       
/* 2406 */       int i1 = compilerMember1.field.getModifiers() & 0x20053F;
/*      */ 
/*      */ 
/*      */       
/* 2410 */       if ((i1 & 0x200000) != 0 || (n & 0x200000) != 0) {
/* 2411 */         i1 |= 0x800;
/*      */       
/*      */       }
/* 2414 */       else if (paramEnvironment.strictdefault()) {
/* 2415 */         i1 |= 0x800;
/*      */       } 
/*      */       
/* 2418 */       dataOutputStream1.writeShort(i1);
/*      */       
/* 2420 */       dataOutputStream1.writeShort(this.tab.index(compilerMember1.name));
/* 2421 */       dataOutputStream1.writeShort(this.tab.index(compilerMember1.sig));
/* 2422 */       ClassDeclaration[] arrayOfClassDeclaration = compilerMember1.field.getExceptions(paramEnvironment);
/* 2423 */       int i2 = (arrayOfClassDeclaration.length > 0) ? 1 : 0;
/* 2424 */       boolean bool3 = compilerMember1.field.isDeprecated();
/* 2425 */       boolean bool4 = compilerMember1.field.isSynthetic();
/* 2426 */       i2 += (bool3 ? 1 : 0) + (bool4 ? 1 : 0);
/*      */       
/* 2428 */       if (!compilerMember1.asm.empty()) {
/* 2429 */         dataOutputStream1.writeShort(i2 + 1);
/* 2430 */         compilerMember1.asm.write(paramEnvironment, dataOutputStream2, compilerMember1.field, this.tab);
/* 2431 */         byte b = 0;
/* 2432 */         if (paramEnvironment.debug_lines()) {
/* 2433 */           b++;
/*      */         }
/*      */         
/* 2436 */         if (paramEnvironment.coverage()) {
/* 2437 */           b++;
/*      */         }
/*      */         
/* 2440 */         if (paramEnvironment.debug_vars()) {
/* 2441 */           b++;
/*      */         }
/* 2443 */         dataOutputStream2.writeShort(b);
/*      */         
/* 2445 */         if (paramEnvironment.debug_lines()) {
/* 2446 */           compilerMember1.asm.writeLineNumberTable(paramEnvironment, new DataOutputStream(byteArrayOutputStream2), this.tab);
/* 2447 */           dataOutputStream2.writeShort(this.tab.index("LineNumberTable"));
/* 2448 */           dataOutputStream2.writeInt(byteArrayOutputStream2.size());
/* 2449 */           byteArrayOutputStream2.writeTo(byteArrayOutputStream1);
/* 2450 */           byteArrayOutputStream2.reset();
/*      */         } 
/*      */ 
/*      */         
/* 2454 */         if (paramEnvironment.coverage()) {
/* 2455 */           compilerMember1.asm.writeCoverageTable(paramEnvironment, this, new DataOutputStream(byteArrayOutputStream2), this.tab, compilerMember1.field.getWhere());
/* 2456 */           dataOutputStream2.writeShort(this.tab.index("CoverageTable"));
/* 2457 */           dataOutputStream2.writeInt(byteArrayOutputStream2.size());
/* 2458 */           byteArrayOutputStream2.writeTo(byteArrayOutputStream1);
/* 2459 */           byteArrayOutputStream2.reset();
/*      */         } 
/*      */         
/* 2462 */         if (paramEnvironment.debug_vars()) {
/* 2463 */           compilerMember1.asm.writeLocalVariableTable(paramEnvironment, compilerMember1.field, new DataOutputStream(byteArrayOutputStream2), this.tab);
/* 2464 */           dataOutputStream2.writeShort(this.tab.index("LocalVariableTable"));
/* 2465 */           dataOutputStream2.writeInt(byteArrayOutputStream2.size());
/* 2466 */           byteArrayOutputStream2.writeTo(byteArrayOutputStream1);
/* 2467 */           byteArrayOutputStream2.reset();
/*      */         } 
/*      */         
/* 2470 */         dataOutputStream1.writeShort(this.tab.index("Code"));
/* 2471 */         dataOutputStream1.writeInt(byteArrayOutputStream1.size());
/* 2472 */         byteArrayOutputStream1.writeTo(dataOutputStream1);
/* 2473 */         byteArrayOutputStream1.reset();
/*      */       } else {
/*      */         
/* 2476 */         if (paramEnvironment.coverage() && (compilerMember1.field.getModifiers() & 0x100) > 0) {
/* 2477 */           compilerMember1.asm.addNativeToJcovTab(paramEnvironment, this);
/*      */         }
/* 2479 */         dataOutputStream1.writeShort(i2);
/*      */       } 
/*      */       
/* 2482 */       if (arrayOfClassDeclaration.length > 0) {
/* 2483 */         dataOutputStream1.writeShort(this.tab.index("Exceptions"));
/* 2484 */         dataOutputStream1.writeInt(2 + arrayOfClassDeclaration.length * 2);
/* 2485 */         dataOutputStream1.writeShort(arrayOfClassDeclaration.length);
/* 2486 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 2487 */           dataOutputStream1.writeShort(this.tab.index(arrayOfClassDeclaration[b]));
/*      */         }
/*      */       } 
/* 2490 */       if (bool3) {
/* 2491 */         dataOutputStream1.writeShort(this.tab.index("Deprecated"));
/* 2492 */         dataOutputStream1.writeInt(0);
/*      */       } 
/* 2494 */       if (bool4) {
/* 2495 */         dataOutputStream1.writeShort(this.tab.index("Synthetic"));
/* 2496 */         dataOutputStream1.writeInt(0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2501 */     dataOutputStream1.writeShort(b1);
/*      */     
/* 2503 */     if (paramEnvironment.debug_source()) {
/* 2504 */       dataOutputStream1.writeShort(this.tab.index("SourceFile"));
/* 2505 */       dataOutputStream1.writeInt(2);
/* 2506 */       dataOutputStream1.writeShort(this.tab.index(str1));
/*      */     } 
/*      */     
/* 2509 */     if (isDeprecated()) {
/* 2510 */       dataOutputStream1.writeShort(this.tab.index("Deprecated"));
/* 2511 */       dataOutputStream1.writeInt(0);
/*      */     } 
/* 2513 */     if (isSynthetic()) {
/* 2514 */       dataOutputStream1.writeShort(this.tab.index("Synthetic"));
/* 2515 */       dataOutputStream1.writeInt(0);
/*      */     } 
/*      */ 
/*      */     
/* 2519 */     if (paramEnvironment.coverage()) {
/* 2520 */       dataOutputStream1.writeShort(this.tab.index("AbsoluteSourcePath"));
/* 2521 */       dataOutputStream1.writeInt(2);
/* 2522 */       dataOutputStream1.writeShort(this.tab.index(str2));
/* 2523 */       dataOutputStream1.writeShort(this.tab.index("TimeStamp"));
/* 2524 */       dataOutputStream1.writeInt(8);
/* 2525 */       dataOutputStream1.writeLong(l);
/*      */     } 
/*      */ 
/*      */     
/* 2529 */     if (vector.size() > 0) {
/* 2530 */       dataOutputStream1.writeShort(this.tab.index("InnerClasses"));
/* 2531 */       dataOutputStream1.writeInt(2 + 8 * vector.size());
/* 2532 */       dataOutputStream1.writeShort(vector.size());
/* 2533 */       enumeration2 = (Enumeration)vector.elements();
/* 2534 */       while (enumeration2.hasMoreElements()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2552 */         ClassDefinition classDefinition = (ClassDefinition)enumeration2.nextElement();
/* 2553 */         dataOutputStream1.writeShort(this.tab.index(classDefinition.getClassDeclaration()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2560 */         if (classDefinition.isLocal() || classDefinition.isAnonymous()) {
/* 2561 */           dataOutputStream1.writeShort(0);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2566 */           ClassDefinition classDefinition1 = classDefinition.getOuterClass();
/* 2567 */           dataOutputStream1.writeShort(this.tab.index(classDefinition1.getClassDeclaration()));
/*      */         } 
/*      */ 
/*      */         
/* 2571 */         Identifier identifier = classDefinition.getLocalName();
/* 2572 */         if (identifier == idNull) {
/* 2573 */           if (!classDefinition.isAnonymous()) {
/* 2574 */             throw new CompilerError("compileClass(), anonymous");
/*      */           }
/* 2576 */           dataOutputStream1.writeShort(0);
/*      */         } else {
/* 2578 */           dataOutputStream1.writeShort(this.tab.index(identifier.toString()));
/*      */         } 
/*      */ 
/*      */         
/* 2582 */         int i1 = classDefinition.getInnerClassMember().getModifiers() & 0xE1F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2590 */         if (classDefinition.isInterface())
/*      */         {
/* 2592 */           assertModifiers(i1, 1032);
/*      */         }
/* 2594 */         if (classDefinition.getOuterClass().isInterface()) {
/*      */           
/* 2596 */           i1 &= 0xFFFFFFF9;
/* 2597 */           assertModifiers(i1, 9);
/*      */         } 
/*      */         
/* 2600 */         dataOutputStream1.writeShort(i1);
/*      */         
/* 2602 */         if (paramEnvironment.dumpModifiers()) {
/* 2603 */           Identifier identifier1 = classDefinition.getInnerClassMember().getName();
/*      */           
/* 2605 */           Identifier identifier2 = Identifier.lookup(identifier1.getQualifier(), identifier1.getFlatName());
/* 2606 */           System.out.println("INNERCLASS " + identifier2);
/* 2607 */           System.out.println("---" + classModifierString(i1));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2614 */     dataOutputStream1.flush();
/* 2615 */     this.tab = null;
/*      */ 
/*      */ 
/*      */     
/* 2619 */     if (paramEnvironment.covdata()) {
/* 2620 */       Assembler assembler = new Assembler();
/* 2621 */       assembler.GenVecJCov(paramEnvironment, this, l);
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
/*      */   public void printClassDependencies(Environment paramEnvironment) {
/* 2633 */     if (this.toplevelEnv.print_dependencies()) {
/*      */ 
/*      */ 
/*      */       
/* 2637 */       String str1 = ((ClassFile)getSource()).getAbsoluteName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2644 */       String str2 = Type.mangleInnerType(getName()).toString();
/*      */ 
/*      */       
/* 2647 */       long l1 = getWhere() >> 32L;
/*      */ 
/*      */       
/* 2650 */       long l2 = getEndPosition() >> 32L;
/*      */ 
/*      */ 
/*      */       
/* 2654 */       System.out.println("CLASS:" + str1 + "," + l1 + "," + l2 + "," + str2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2665 */       for (Enumeration<ClassDeclaration> enumeration = this.deps.elements(); enumeration.hasMoreElements(); ) {
/* 2666 */         ClassDeclaration classDeclaration = enumeration.nextElement();
/*      */ 
/*      */         
/* 2669 */         String str = Type.mangleInnerType(classDeclaration.getName()).toString();
/* 2670 */         paramEnvironment.output("CLDEP:" + str2 + "," + str);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\SourceClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */