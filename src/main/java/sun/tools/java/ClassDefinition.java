/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import sun.tools.javac.SourceMember;
/*      */ import sun.tools.tree.Context;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.LocalMember;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassDefinition
/*      */   implements Constants
/*      */ {
/*      */   protected Object source;
/*      */   protected long where;
/*      */   protected int modifiers;
/*      */   protected Identifier localName;
/*      */   protected ClassDeclaration declaration;
/*      */   protected IdentifierToken superClassId;
/*      */   protected IdentifierToken[] interfaceIds;
/*      */   protected ClassDeclaration superClass;
/*      */   protected ClassDeclaration[] interfaces;
/*      */   protected ClassDefinition outerClass;
/*      */   protected MemberDefinition outerMember;
/*      */   protected MemberDefinition innerClassMember;
/*      */   protected MemberDefinition firstMember;
/*      */   protected MemberDefinition lastMember;
/*      */   protected boolean resolved;
/*      */   protected String documentation;
/*      */   protected boolean error;
/*      */   protected boolean nestError;
/*      */   protected UplevelReference references;
/*      */   protected boolean referencesFrozen;
/*   67 */   private Hashtable fieldHash = new Hashtable<>(31);
/*      */ 
/*      */ 
/*      */   
/*      */   private int abstr;
/*      */ 
/*      */   
/*   74 */   private Hashtable localClasses = null;
/*   75 */   private final int LOCAL_CLASSES_SIZE = 31;
/*      */   
/*      */   protected Context classContext;
/*      */   
/*      */   protected boolean supersCheckStarted;
/*      */   
/*      */   MethodSet allMethods;
/*      */   
/*      */   private List permanentlyAbstractMethods;
/*      */ 
/*      */   
/*      */   public Context getClassContext() {
/*   87 */     return this.classContext;
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
/*      */   public final Object getSource() {
/*  108 */     return this.source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getError() {
/*  115 */     return this.error;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setError() {
/*  122 */     this.error = true;
/*  123 */     setNestError();
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
/*      */   public final boolean getNestError() {
/*  137 */     return (this.nestError || (this.outerClass != null && this.outerClass.getNestError()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNestError() {
/*  145 */     this.nestError = true;
/*  146 */     if (this.outerClass != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  153 */       this.outerClass.setNestError();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getWhere() {
/*  161 */     return this.where;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ClassDeclaration getClassDeclaration() {
/*  168 */     return this.declaration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getModifiers() {
/*  175 */     return this.modifiers;
/*      */   }
/*      */   public final void subModifiers(int paramInt) {
/*  178 */     this.modifiers &= paramInt ^ 0xFFFFFFFF;
/*      */   }
/*      */   
/*  181 */   public final void addModifiers(int paramInt) { this.modifiers |= paramInt; }
/*      */   public final ClassDeclaration getSuperClass() { if (!this.supersCheckStarted) throw new CompilerError("unresolved super");  return this.superClass; }
/*      */   public ClassDeclaration getSuperClass(Environment paramEnvironment) { return getSuperClass(); }
/*      */   public final ClassDeclaration[] getInterfaces() { if (this.interfaces == null) throw new CompilerError("getInterfaces");  return this.interfaces; }
/*  185 */   public final ClassDefinition getOuterClass() { return this.outerClass; } protected final void setOuterClass(ClassDefinition paramClassDefinition) { if (this.outerClass != null) throw new CompilerError("setOuterClass");  this.outerClass = paramClassDefinition; } protected final void setOuterMember(MemberDefinition paramMemberDefinition) { if (isStatic() || !isInnerClass()) throw new CompilerError("setOuterField");  if (this.outerMember != null) throw new CompilerError("setOuterField");  this.outerMember = paramMemberDefinition; } public final boolean isInnerClass() { return (this.outerClass != null); } public final boolean isMember() { return (this.outerClass != null && !isLocal()); } public final boolean isTopLevel() { return (this.outerClass == null || isStatic() || isInterface()); } public final boolean isInsideLocal() { return (isLocal() || (this.outerClass != null && this.outerClass.isInsideLocal())); } public final boolean isInsideLocalOrAnonymous() { return (isLocal() || isAnonymous() || (this.outerClass != null && this.outerClass.isInsideLocalOrAnonymous())); } public Identifier getLocalName() { if (this.localName != null) return this.localName;  return getName().getFlatName().getName(); } public void setLocalName(Identifier paramIdentifier) { if (isLocal()) this.localName = paramIdentifier;  } public final MemberDefinition getInnerClassMember() { if (this.outerClass == null) return null;  if (this.innerClassMember == null) { Identifier identifier = getName().getFlatName().getName(); MemberDefinition memberDefinition = this.outerClass.getFirstMatch(identifier); for (; memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) { if (memberDefinition.isInnerClass()) { this.innerClassMember = memberDefinition; break; }  }  if (this.innerClassMember == null) throw new CompilerError("getInnerClassField");  }  return this.innerClassMember; } public final MemberDefinition findOuterMember() { return this.outerMember; } public final boolean isStatic() { return ((this.modifiers & 0x8) != 0); } public final ClassDefinition getTopClass() { ClassDefinition classDefinition1; ClassDefinition classDefinition2; for (classDefinition1 = this; (classDefinition2 = classDefinition1.outerClass) != null; classDefinition1 = classDefinition2); return classDefinition1; } protected ClassDefinition(Object paramObject, long paramLong, ClassDeclaration paramClassDeclaration, int paramInt, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken) { this.supersCheckStarted = !(this instanceof sun.tools.javac.SourceClass);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1179 */     this.allMethods = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1189 */     this.permanentlyAbstractMethods = new ArrayList(); this.source = paramObject; this.where = paramLong; this.declaration = paramClassDeclaration; this.modifiers = paramInt; this.superClassId = paramIdentifierToken; this.interfaceIds = paramArrayOfIdentifierToken; }
/*      */   public final MemberDefinition getFirstMember() { return this.firstMember; }
/*      */   public final MemberDefinition getFirstMatch(Identifier paramIdentifier) { return (MemberDefinition)this.fieldHash.get(paramIdentifier); }
/*      */   public final Identifier getName() { return this.declaration.getName(); }
/*      */   public final Type getType() { return this.declaration.getType(); }
/*      */   public String getDocumentation() { return this.documentation; }
/*      */   public static boolean containsDeprecated(String paramString) { if (paramString == null) return false;  int i = 0; label29: while ((i = paramString.indexOf("@deprecated", i)) >= 0) { int j; for (j = i - 1; j >= 0; j--) { char c = paramString.charAt(j); if (c == '\n' || c == '\r') break;  if (!Character.isSpace(c)) continue label29;  }  j = i + "@deprecated".length(); if (j < paramString.length()) { char c = paramString.charAt(j); if (c != '\n' && c != '\r' && !Character.isSpace(c)) { i += "@deprecated".length(); continue; }  }  return true; }  return false; }
/*      */   public final boolean inSamePackage(ClassDeclaration paramClassDeclaration) { return inSamePackage(paramClassDeclaration.getName().getQualifier()); }
/* 1197 */   public final boolean inSamePackage(ClassDefinition paramClassDefinition) { return inSamePackage(paramClassDefinition.getName().getQualifier()); } public final boolean inSamePackage(Identifier paramIdentifier) { return getName().getQualifier().equals(paramIdentifier); } public final boolean isInterface() { return ((getModifiers() & 0x200) != 0); } public final boolean isClass() { return ((getModifiers() & 0x200) == 0); } public final boolean isPublic() { return ((getModifiers() & 0x1) != 0); } public final boolean isPrivate() { return ((getModifiers() & 0x2) != 0); } public final boolean isProtected() { return ((getModifiers() & 0x4) != 0); } public final boolean isPackagePrivate() { return ((this.modifiers & 0x7) == 0); } public final boolean isFinal() { return ((getModifiers() & 0x10) != 0); } protected Iterator getPermanentlyAbstractMethods() { if (this.allMethods == null) {
/* 1198 */       throw new CompilerError("isPermanentlyAbstract() called early");
/*      */     }
/*      */     
/* 1201 */     return this.permanentlyAbstractMethods.iterator(); } public final boolean isAbstract() { return ((getModifiers() & 0x400) != 0); }
/*      */   public final boolean isSynthetic() { return ((getModifiers() & 0x80000) != 0); }
/*      */   public final boolean isDeprecated() { return ((getModifiers() & 0x40000) != 0); }
/*      */   public final boolean isAnonymous() { return ((getModifiers() & 0x10000) != 0); }
/*      */   public final boolean isLocal() { return ((getModifiers() & 0x20000) != 0); }
/*      */   public final boolean hasConstructor() { return (getFirstMatch(idInit) != null); }
/*      */   public final boolean mustBeAbstract(Environment paramEnvironment) { if (isAbstract()) return true;  collectInheritedMethods(paramEnvironment); Iterator<MemberDefinition> iterator = getMethods(); while (iterator.hasNext()) { MemberDefinition memberDefinition = iterator.next(); if (memberDefinition.isAbstract()) return true;  }  return getPermanentlyAbstractMethods().hasNext(); }
/*      */   public boolean superClassOf(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { while (paramClassDeclaration != null) { if (getClassDeclaration().equals(paramClassDeclaration)) return true;  paramClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getSuperClass(); }  return false; }
/*      */   public boolean enclosingClassOf(ClassDefinition paramClassDefinition) { while ((paramClassDefinition = paramClassDefinition.getOuterClass()) != null) { if (this == paramClassDefinition) return true;  }  return false; }
/*      */   public boolean subClassOf(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { ClassDeclaration classDeclaration = getClassDeclaration(); while (classDeclaration != null) { if (classDeclaration.equals(paramClassDeclaration)) return true;  classDeclaration = classDeclaration.getClassDefinition(paramEnvironment).getSuperClass(); }  return false; }
/*      */   public boolean implementedBy(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { for (; paramClassDeclaration != null; paramClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getSuperClass()) { if (getClassDeclaration().equals(paramClassDeclaration)) return true;  ClassDeclaration[] arrayOfClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getInterfaces(); for (byte b = 0; b < arrayOfClassDeclaration.length; b++) { if (implementedBy(paramEnvironment, arrayOfClassDeclaration[b])) return true;  }  }  return false; }
/*      */   public boolean couldImplement(ClassDefinition paramClassDefinition) { if (!doInheritanceChecks) throw new CompilerError("couldImplement: no checks");  if (!isInterface() || !paramClassDefinition.isInterface()) throw new CompilerError("couldImplement: not interface");  if (this.allMethods == null) throw new CompilerError("couldImplement: called early");  Iterator<MemberDefinition> iterator = paramClassDefinition.getMethods(); while (iterator.hasNext()) { MemberDefinition memberDefinition1 = iterator.next(); Identifier identifier = memberDefinition1.getName(); Type type = memberDefinition1.getType(); MemberDefinition memberDefinition2 = this.allMethods.lookupSig(identifier, type); if (memberDefinition2 != null) if (!memberDefinition2.sameReturnType(memberDefinition1)) return false;   }  return true; }
/*      */   public boolean extendsCanAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { if (this.outerClass != null) return this.outerClass.canAccess(paramEnvironment, paramClassDeclaration);  ClassDefinition classDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment); if (classDefinition.isLocal()) throw new CompilerError("top local");  if (classDefinition.isInnerClass()) { MemberDefinition memberDefinition = classDefinition.getInnerClassMember(); if (memberDefinition.isPublic()) return true;  if (memberDefinition.isPrivate()) return getClassDeclaration().equals(memberDefinition.getTopClass().getClassDeclaration());  return getName().getQualifier().equals(memberDefinition.getClassDeclaration().getName().getQualifier()); }  if (classDefinition.isPublic()) return true;  return getName().getQualifier().equals(paramClassDeclaration.getName().getQualifier()); }
/*      */   public boolean canAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { ClassDefinition classDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment); if (classDefinition.isLocal()) return true;  if (classDefinition.isInnerClass()) return canAccess(paramEnvironment, classDefinition.getInnerClassMember());  if (classDefinition.isPublic()) return true;  return getName().getQualifier().equals(paramClassDeclaration.getName().getQualifier()); }
/*      */   public boolean canAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition) throws ClassNotFound { if (paramMemberDefinition.isPublic()) return true;  if (paramMemberDefinition.isProtected() && subClassOf(paramEnvironment, paramMemberDefinition.getClassDeclaration())) return true;  if (paramMemberDefinition.isPrivate()) return getTopClass().getClassDeclaration().equals(paramMemberDefinition.getTopClass().getClassDeclaration());  return getName().getQualifier().equals(paramMemberDefinition.getClassDeclaration().getName().getQualifier()); }
/*      */   public boolean permitInlinedAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration) throws ClassNotFound { return ((paramEnvironment.opt() && paramClassDeclaration.equals(this.declaration)) || (paramEnvironment.opt_interclass() && canAccess(paramEnvironment, paramClassDeclaration))); }
/*      */   public boolean permitInlinedAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition) throws ClassNotFound { return ((paramEnvironment.opt() && paramMemberDefinition.clazz.getClassDeclaration().equals(this.declaration)) || (paramEnvironment.opt_interclass() && canAccess(paramEnvironment, paramMemberDefinition))); }
/* 1218 */   public static void turnOffInheritanceChecks() { doInheritanceChecks = false; } public boolean protectedAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition, Type paramType) throws ClassNotFound { return (paramMemberDefinition.isStatic() || (paramType.isType(9) && paramMemberDefinition.getName() == idClone && (paramMemberDefinition.getType().getArgumentTypes()).length == 0) || (paramType.isType(10) && paramEnvironment.getClassDefinition(paramType.getClassName()).subClassOf(paramEnvironment, getClassDeclaration())) || getName().getQualifier().equals(paramMemberDefinition.getClassDeclaration().getName().getQualifier())); } public MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean) { throw new CompilerError("binary getAccessMember"); } public MemberDefinition getUpdateMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean) { throw new CompilerError("binary getUpdateMember"); }
/*      */   public MemberDefinition getVariable(Environment paramEnvironment, Identifier paramIdentifier, ClassDefinition paramClassDefinition) throws AmbiguousMember, ClassNotFound { return getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, true, true); }
/*      */   private MemberDefinition getVariable0(Environment paramEnvironment, Identifier paramIdentifier, ClassDefinition paramClassDefinition, boolean paramBoolean1, boolean paramBoolean2) throws AmbiguousMember, ClassNotFound { MemberDefinition memberDefinition1 = getFirstMatch(paramIdentifier); for (; memberDefinition1 != null; memberDefinition1 = memberDefinition1.getNextMatch()) { if (memberDefinition1.isVariable()) { if ((paramBoolean1 || !memberDefinition1.isPrivate()) && (paramBoolean2 || !memberDefinition1.isPackagePrivate())) return memberDefinition1;  return null; }  }  ClassDeclaration classDeclaration = getSuperClass(); MemberDefinition memberDefinition2 = null; if (classDeclaration != null) memberDefinition2 = classDeclaration.getClassDefinition(paramEnvironment).getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, false, (paramBoolean2 && inSamePackage(classDeclaration)));  for (byte b = 0; b < this.interfaces.length; b++) { MemberDefinition memberDefinition = this.interfaces[b].getClassDefinition(paramEnvironment).getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, true, true); if (memberDefinition != null) { if (memberDefinition2 != null && paramClassDefinition.canAccess(paramEnvironment, memberDefinition2) && memberDefinition != memberDefinition2) throw new AmbiguousMember(memberDefinition, memberDefinition2);  memberDefinition2 = memberDefinition; }  }  return memberDefinition2; }
/*      */   public boolean reportDeprecated(Environment paramEnvironment) { return (isDeprecated() || (this.outerClass != null && this.outerClass.reportDeprecated(paramEnvironment))); }
/*      */   public void noteUsedBy(ClassDefinition paramClassDefinition, long paramLong, Environment paramEnvironment) { if (reportDeprecated(paramEnvironment)) paramEnvironment.error(paramLong, "warn.class.is.deprecated", this);  }
/*      */   public MemberDefinition getInnerClass(Environment paramEnvironment, Identifier paramIdentifier) throws ClassNotFound { MemberDefinition memberDefinition = getFirstMatch(paramIdentifier); while (memberDefinition != null) { if (!memberDefinition.isInnerClass() || memberDefinition.getInnerClass().isLocal()) { memberDefinition = memberDefinition.getNextMatch(); continue; }  return memberDefinition; }  ClassDeclaration classDeclaration = getSuperClass(paramEnvironment); if (classDeclaration != null) return classDeclaration.getClassDefinition(paramEnvironment).getInnerClass(paramEnvironment, paramIdentifier);  return null; }
/*      */   private MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier1, Type[] paramArrayOfType, boolean paramBoolean, Identifier paramIdentifier2) throws AmbiguousMember, ClassNotFound { if (this.allMethods == null || !this.allMethods.isFrozen()) throw new CompilerError("matchMethod called early");  MemberDefinition memberDefinition = null; ArrayList<MemberDefinition> arrayList = null; Iterator<MemberDefinition> iterator = this.allMethods.lookupName(paramIdentifier1); while (iterator.hasNext()) { MemberDefinition memberDefinition1 = iterator.next(); if (!paramEnvironment.isApplicable(memberDefinition1, paramArrayOfType))
/*      */         continue;  if ((paramClassDefinition != null) ? !paramClassDefinition.canAccess(paramEnvironment, memberDefinition1) : (paramBoolean && (memberDefinition1.isPrivate() || (memberDefinition1.isPackagePrivate() && paramIdentifier2 != null && !inSamePackage(paramIdentifier2)))))
/*      */         continue;  if (memberDefinition == null) { memberDefinition = memberDefinition1; continue; }  if (paramEnvironment.isMoreSpecific(memberDefinition1, memberDefinition)) { memberDefinition = memberDefinition1; continue; }  if (!paramEnvironment.isMoreSpecific(memberDefinition, memberDefinition1)) { if (arrayList == null)
/*      */           arrayList = new ArrayList();  arrayList.add(memberDefinition1); }  }  if (memberDefinition != null && arrayList != null) { Iterator<MemberDefinition> iterator1 = arrayList.iterator(); while (iterator1.hasNext()) { MemberDefinition memberDefinition1 = iterator1.next(); if (!paramEnvironment.isMoreSpecific(memberDefinition, memberDefinition1))
/*      */           throw new AmbiguousMember(memberDefinition, memberDefinition1);  }  }  return memberDefinition; }
/*      */   public MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier, Type[] paramArrayOfType) throws AmbiguousMember, ClassNotFound { return matchMethod(paramEnvironment, paramClassDefinition, paramIdentifier, paramArrayOfType, false, (Identifier)null); }
/*      */   public MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier) throws AmbiguousMember, ClassNotFound { return matchMethod(paramEnvironment, paramClassDefinition, paramIdentifier, Type.noArgs, false, (Identifier)null); }
/*      */   public MemberDefinition matchAnonConstructor(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType) throws AmbiguousMember, ClassNotFound { return matchMethod(paramEnvironment, (ClassDefinition)null, idInit, paramArrayOfType, true, paramIdentifier); }
/*      */   public MemberDefinition findMethod(Environment paramEnvironment, Identifier paramIdentifier, Type paramType) throws ClassNotFound { for (MemberDefinition memberDefinition = getFirstMatch(paramIdentifier); memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) { if (memberDefinition.getType().equalArguments(paramType))
/*      */         return memberDefinition;  }  if (paramIdentifier.equals(idInit))
/*      */       return null;  ClassDeclaration classDeclaration = getSuperClass(); if (classDeclaration == null)
/*      */       return null;  return classDeclaration.getClassDefinition(paramEnvironment).findMethod(paramEnvironment, paramIdentifier, paramType); }
/*      */   protected void basicCheck(Environment paramEnvironment) throws ClassNotFound { if (this.outerClass != null)
/*      */       this.outerClass.basicCheck(paramEnvironment);  }
/*      */   public void check(Environment paramEnvironment) throws ClassNotFound {}
/*      */   public Vset checkLocalClass(Environment paramEnvironment, Context paramContext, Vset paramVset, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType) throws ClassNotFound { throw new CompilerError("checkLocalClass"); }
/*      */   protected static boolean doInheritanceChecks = true;
/* 1241 */   private void collectOneClass(Environment paramEnvironment, ClassDeclaration paramClassDeclaration, MethodSet paramMethodSet1, MethodSet paramMethodSet2, MethodSet paramMethodSet3) { try { ClassDefinition classDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment);
/* 1242 */       Iterator<MemberDefinition> iterator = classDefinition.getMethods(paramEnvironment);
/* 1243 */       while (iterator.hasNext()) {
/*      */         SourceMember sourceMember;
/* 1245 */         MemberDefinition memberDefinition1 = iterator.next();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1255 */         if (memberDefinition1.isPrivate() || memberDefinition1
/* 1256 */           .isConstructor() || (classDefinition
/* 1257 */           .isInterface() && !memberDefinition1.isAbstract())) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1263 */         Identifier identifier = memberDefinition1.getName();
/* 1264 */         Type type = memberDefinition1.getType();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1269 */         MemberDefinition memberDefinition2 = paramMethodSet1.lookupSig(identifier, type);
/*      */ 
/*      */ 
/*      */         
/* 1273 */         if (memberDefinition1.isPackagePrivate() && 
/* 1274 */           !inSamePackage(memberDefinition1.getClassDeclaration())) {
/*      */           
/* 1276 */           if (memberDefinition2 != null && this instanceof sun.tools.javac.SourceClass)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1290 */             paramEnvironment.error(memberDefinition1.getWhere(), "warn.no.override.access", memberDefinition2, memberDefinition2
/*      */ 
/*      */                 
/* 1293 */                 .getClassDeclaration(), memberDefinition1
/* 1294 */                 .getClassDeclaration());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1303 */           if (memberDefinition1.isAbstract()) {
/* 1304 */             this.permanentlyAbstractMethods.add(memberDefinition1);
/*      */           }
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 1311 */         if (memberDefinition2 != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1319 */           memberDefinition2.checkOverride(paramEnvironment, memberDefinition1);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1329 */         MemberDefinition memberDefinition3 = paramMethodSet2.lookupSig(identifier, type);
/*      */ 
/*      */ 
/*      */         
/* 1333 */         if (memberDefinition3 == null) {
/*      */ 
/*      */ 
/*      */           
/* 1337 */           if (paramMethodSet3 != null && classDefinition
/* 1338 */             .isInterface() && !isInterface()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1346 */             sourceMember = new SourceMember(memberDefinition1, this, paramEnvironment);
/*      */ 
/*      */             
/* 1349 */             paramMethodSet3.add((MemberDefinition)sourceMember);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1357 */           paramMethodSet2.add((MemberDefinition)sourceMember); continue;
/* 1358 */         }  if (isInterface() && 
/* 1359 */           !memberDefinition3.isAbstract() && sourceMember
/* 1360 */           .isAbstract()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1367 */           paramMethodSet2.replace((MemberDefinition)sourceMember);
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1376 */         if (!memberDefinition3.checkMeet(paramEnvironment, (MemberDefinition)sourceMember, 
/*      */             
/* 1378 */             getClassDeclaration())) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1384 */         if (memberDefinition3.couldOverride(paramEnvironment, (MemberDefinition)sourceMember)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1393 */         if (sourceMember.couldOverride(paramEnvironment, memberDefinition3)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1398 */           if (paramMethodSet3 != null && classDefinition
/* 1399 */             .isInterface() && !isInterface()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1407 */             sourceMember = new SourceMember((MemberDefinition)sourceMember, this, paramEnvironment);
/*      */ 
/*      */ 
/*      */             
/* 1411 */             paramMethodSet3.replace((MemberDefinition)sourceMember);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1417 */           paramMethodSet2.replace((MemberDefinition)sourceMember);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1428 */         paramEnvironment.error(this.where, "nontrivial.meet", sourceMember, memberDefinition3
/*      */             
/* 1430 */             .getClassDefinition(), sourceMember
/* 1431 */             .getClassDeclaration());
/*      */       
/*      */       }
/*      */        }
/*      */     
/* 1436 */     catch (ClassNotFound classNotFound)
/* 1437 */     { paramEnvironment.error(getWhere(), "class.not.found", classNotFound.name, this); }
/*      */      }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void collectInheritedMethods(Environment paramEnvironment) {
/*      */     MethodSet methodSet2;
/* 1459 */     if (this.allMethods != null) {
/* 1460 */       if (this.allMethods.isFrozen()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1467 */       throw new CompilerError("collectInheritedMethods()");
/*      */     } 
/*      */ 
/*      */     
/* 1471 */     MethodSet methodSet1 = new MethodSet();
/* 1472 */     this.allMethods = new MethodSet();
/*      */ 
/*      */     
/* 1475 */     if (paramEnvironment.version12()) {
/* 1476 */       methodSet2 = null;
/*      */     } else {
/* 1478 */       methodSet2 = new MethodSet();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1484 */     MemberDefinition memberDefinition = getFirstMember();
/* 1485 */     for (; memberDefinition != null; 
/* 1486 */       memberDefinition = memberDefinition.nextMember) {
/*      */ 
/*      */       
/* 1489 */       if (memberDefinition.isMethod() && 
/* 1490 */         !memberDefinition.isInitializer()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1506 */         methodSetAdd(paramEnvironment, methodSet1, memberDefinition);
/* 1507 */         methodSetAdd(paramEnvironment, this.allMethods, memberDefinition);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1517 */     ClassDeclaration classDeclaration = getSuperClass(paramEnvironment);
/* 1518 */     if (classDeclaration != null) {
/* 1519 */       collectOneClass(paramEnvironment, classDeclaration, methodSet1, this.allMethods, methodSet2);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1524 */       ClassDefinition classDefinition = classDeclaration.getClassDefinition();
/* 1525 */       Iterator iterator = classDefinition.getPermanentlyAbstractMethods();
/* 1526 */       while (iterator.hasNext()) {
/* 1527 */         this.permanentlyAbstractMethods.add(iterator.next());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1535 */     for (byte b = 0; b < this.interfaces.length; b++) {
/* 1536 */       collectOneClass(paramEnvironment, this.interfaces[b], methodSet1, this.allMethods, methodSet2);
/*      */     }
/*      */     
/* 1539 */     this.allMethods.freeze();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1547 */     if (methodSet2 != null && methodSet2.size() > 0) {
/* 1548 */       addMirandaMethods(paramEnvironment, methodSet2.iterator());
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
/*      */   private static void methodSetAdd(Environment paramEnvironment, MethodSet paramMethodSet, MemberDefinition paramMemberDefinition) {
/* 1569 */     MemberDefinition memberDefinition = paramMethodSet.lookupSig(paramMemberDefinition.getName(), paramMemberDefinition
/* 1570 */         .getType());
/* 1571 */     if (memberDefinition != null) {
/* 1572 */       Type type1 = memberDefinition.getType().getReturnType();
/* 1573 */       Type type2 = paramMemberDefinition.getType().getReturnType();
/*      */       try {
/* 1575 */         if (paramEnvironment.isMoreSpecific(type2, type1)) {
/* 1576 */           paramMethodSet.replace(paramMemberDefinition);
/*      */         }
/* 1578 */       } catch (ClassNotFound classNotFound) {}
/*      */     } else {
/*      */       
/* 1581 */       paramMethodSet.add(paramMemberDefinition);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator getMethods(Environment paramEnvironment) {
/* 1591 */     if (this.allMethods == null) {
/* 1592 */       collectInheritedMethods(paramEnvironment);
/*      */     }
/* 1594 */     return getMethods();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator getMethods() {
/* 1603 */     if (this.allMethods == null) {
/* 1604 */       throw new CompilerError("getMethods: too early");
/*      */     }
/* 1606 */     return this.allMethods.iterator();
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
/*      */   protected void addMirandaMethods(Environment paramEnvironment, Iterator paramIterator) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void inlineLocalClass(Environment paramEnvironment) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resolveTypeStructure(Environment paramEnvironment) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Identifier resolveName(Environment paramEnvironment, Identifier paramIdentifier) {
/* 1690 */     paramEnvironment.dtEvent("ClassDefinition.resolveName: " + paramIdentifier);
/*      */ 
/*      */     
/* 1693 */     if (paramIdentifier.isQualified()) {
/*      */ 
/*      */ 
/*      */       
/* 1697 */       Identifier identifier = resolveName(paramEnvironment, paramIdentifier.getHead());
/*      */       
/* 1699 */       if (identifier.hasAmbigPrefix())
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1704 */         return identifier;
/*      */       }
/*      */       
/* 1707 */       if (!paramEnvironment.classExists(identifier)) {
/* 1708 */         return paramEnvironment.resolvePackageQualifiedName(paramIdentifier);
/*      */       }
/*      */       try {
/* 1711 */         return paramEnvironment.getClassDefinition(identifier)
/* 1712 */           .resolveInnerClass(paramEnvironment, paramIdentifier.getTail());
/* 1713 */       } catch (ClassNotFound classNotFound) {
/*      */         
/* 1715 */         return Identifier.lookupInner(identifier, paramIdentifier.getTail());
/*      */       } 
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
/* 1727 */     int i = -2;
/* 1728 */     LocalMember localMember = null;
/* 1729 */     if (this.classContext != null) {
/* 1730 */       localMember = this.classContext.getLocalClass(paramIdentifier);
/* 1731 */       if (localMember != null) {
/* 1732 */         i = localMember.getScopeNumber();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1737 */     for (ClassDefinition classDefinition = this; classDefinition != null; classDefinition = classDefinition.outerClass) {
/*      */       try {
/* 1739 */         MemberDefinition memberDefinition = classDefinition.getInnerClass(paramEnvironment, paramIdentifier);
/* 1740 */         if (memberDefinition != null && (localMember == null || this.classContext
/* 1741 */           .getScopeNumber(classDefinition) > i))
/*      */         {
/*      */           
/* 1744 */           return memberDefinition.getInnerClass().getName();
/*      */         }
/* 1746 */       } catch (ClassNotFound classNotFound) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1752 */     if (localMember != null) {
/* 1753 */       return localMember.getInnerClass().getName();
/*      */     }
/*      */ 
/*      */     
/* 1757 */     return paramEnvironment.resolveName(paramIdentifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Identifier resolveInnerClass(Environment paramEnvironment, Identifier paramIdentifier) {
/* 1768 */     if (paramIdentifier.isInner()) throw new CompilerError("inner"); 
/* 1769 */     if (paramIdentifier.isQualified()) {
/* 1770 */       Identifier identifier = resolveInnerClass(paramEnvironment, paramIdentifier.getHead());
/*      */       try {
/* 1772 */         return paramEnvironment.getClassDefinition(identifier)
/* 1773 */           .resolveInnerClass(paramEnvironment, paramIdentifier.getTail());
/* 1774 */       } catch (ClassNotFound classNotFound) {
/*      */         
/* 1776 */         return Identifier.lookupInner(identifier, paramIdentifier.getTail());
/*      */       } 
/*      */     } 
/*      */     try {
/* 1780 */       MemberDefinition memberDefinition = getInnerClass(paramEnvironment, paramIdentifier);
/* 1781 */       if (memberDefinition != null) {
/* 1782 */         return memberDefinition.getInnerClass().getName();
/*      */       }
/* 1784 */     } catch (ClassNotFound classNotFound) {}
/*      */ 
/*      */ 
/*      */     
/* 1788 */     return Identifier.lookupInner(getName(), paramIdentifier);
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
/*      */   public boolean innerClassExists(Identifier paramIdentifier) {
/* 1804 */     for (MemberDefinition memberDefinition = getFirstMatch(paramIdentifier.getHead()); memberDefinition != null; ) {
/* 1805 */       if (!memberDefinition.isInnerClass() || 
/* 1806 */         memberDefinition.getInnerClass().isLocal()) {
/*      */         memberDefinition = memberDefinition.getNextMatch(); continue;
/*      */       } 
/* 1809 */       return (!paramIdentifier.isQualified() || memberDefinition
/* 1810 */         .getInnerClass().innerClassExists(paramIdentifier.getTail()));
/*      */     } 
/*      */     
/* 1813 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberDefinition findAnyMethod(Environment paramEnvironment, Identifier paramIdentifier) throws ClassNotFound {
/* 1821 */     for (MemberDefinition memberDefinition = getFirstMatch(paramIdentifier); memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) {
/* 1822 */       if (memberDefinition.isMethod()) {
/* 1823 */         return memberDefinition;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1828 */     ClassDeclaration classDeclaration = getSuperClass();
/* 1829 */     if (classDeclaration == null)
/* 1830 */       return null; 
/* 1831 */     return classDeclaration.getClassDefinition(paramEnvironment).findAnyMethod(paramEnvironment, paramIdentifier);
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
/*      */   public int diagnoseMismatch(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType1, int paramInt, Type[] paramArrayOfType2) throws ClassNotFound {
/* 1852 */     int[] arrayOfInt = new int[paramArrayOfType1.length];
/* 1853 */     Type[] arrayOfType = new Type[paramArrayOfType1.length];
/* 1854 */     if (!diagnoseMismatch(paramEnvironment, paramIdentifier, paramArrayOfType1, paramInt, arrayOfInt, arrayOfType))
/* 1855 */       return -2; 
/* 1856 */     for (int i = paramInt; i < paramArrayOfType1.length; i++) {
/* 1857 */       if (arrayOfInt[i] < 4) {
/* 1858 */         paramArrayOfType2[0] = arrayOfType[i];
/* 1859 */         return i << 2 | arrayOfInt[i];
/*      */       } 
/*      */     } 
/* 1862 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean diagnoseMismatch(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType1, int paramInt, int[] paramArrayOfint, Type[] paramArrayOfType2) throws ClassNotFound {
/* 1868 */     boolean bool = false;
/*      */     
/* 1870 */     for (MemberDefinition memberDefinition = getFirstMatch(paramIdentifier); memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) {
/* 1871 */       if (memberDefinition.isMethod()) {
/*      */ 
/*      */         
/* 1874 */         Type[] arrayOfType = memberDefinition.getType().getArgumentTypes();
/* 1875 */         if (arrayOfType.length == paramArrayOfType1.length) {
/* 1876 */           bool = true;
/* 1877 */           for (int i = paramInt; i < paramArrayOfType1.length; i++) {
/* 1878 */             Type type1 = paramArrayOfType1[i];
/* 1879 */             Type type2 = arrayOfType[i];
/* 1880 */             if (paramEnvironment.implicitCast(type1, type2)) {
/* 1881 */               paramArrayOfint[i] = 4; continue;
/*      */             } 
/* 1883 */             if (paramArrayOfint[i] <= 2 && paramEnvironment.explicitCast(type1, type2)) {
/* 1884 */               if (paramArrayOfint[i] < 2) paramArrayOfType2[i] = null; 
/* 1885 */               paramArrayOfint[i] = 2;
/* 1886 */             } else if (paramArrayOfint[i] > 0) {
/*      */               continue;
/*      */             } 
/* 1889 */             if (paramArrayOfType2[i] == null) {
/* 1890 */               paramArrayOfType2[i] = type2;
/* 1891 */             } else if (paramArrayOfType2[i] != type2) {
/* 1892 */               paramArrayOfint[i] = paramArrayOfint[i] | 0x1;
/*      */             }  continue;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1898 */     if (paramIdentifier.equals(idInit)) {
/* 1899 */       return bool;
/*      */     }
/*      */ 
/*      */     
/* 1903 */     ClassDeclaration classDeclaration = getSuperClass();
/* 1904 */     if (classDeclaration != null && 
/* 1905 */       classDeclaration.getClassDefinition(paramEnvironment).diagnoseMismatch(paramEnvironment, paramIdentifier, paramArrayOfType1, paramInt, paramArrayOfint, paramArrayOfType2))
/*      */     {
/* 1907 */       bool = true;
/*      */     }
/* 1909 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMember(MemberDefinition paramMemberDefinition) {
/* 1917 */     if (this.firstMember == null) {
/* 1918 */       this.firstMember = this.lastMember = paramMemberDefinition;
/* 1919 */     } else if (paramMemberDefinition.isSynthetic() && paramMemberDefinition.isFinal() && paramMemberDefinition
/* 1920 */       .isVariable()) {
/*      */       
/* 1922 */       paramMemberDefinition.nextMember = this.firstMember;
/* 1923 */       this.firstMember = paramMemberDefinition;
/* 1924 */       paramMemberDefinition.nextMatch = (MemberDefinition)this.fieldHash.get(paramMemberDefinition.name);
/*      */     } else {
/* 1926 */       this.lastMember.nextMember = paramMemberDefinition;
/* 1927 */       this.lastMember = paramMemberDefinition;
/* 1928 */       paramMemberDefinition.nextMatch = (MemberDefinition)this.fieldHash.get(paramMemberDefinition.name);
/*      */     } 
/* 1930 */     this.fieldHash.put(paramMemberDefinition.name, paramMemberDefinition);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMember(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/* 1937 */     addMember(paramMemberDefinition);
/* 1938 */     if (this.resolved)
/*      */     {
/* 1940 */       paramMemberDefinition.resolveTypeStructure(paramEnvironment);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UplevelReference getReference(LocalMember paramLocalMember) {
/* 1948 */     for (UplevelReference uplevelReference = this.references; uplevelReference != null; uplevelReference = uplevelReference.getNext()) {
/* 1949 */       if (uplevelReference.getTarget() == paramLocalMember) {
/* 1950 */         return uplevelReference;
/*      */       }
/*      */     } 
/* 1953 */     return addReference(paramLocalMember);
/*      */   }
/*      */   
/*      */   protected UplevelReference addReference(LocalMember paramLocalMember) {
/* 1957 */     if (paramLocalMember.getClassDefinition() == this) {
/* 1958 */       throw new CompilerError("addReference " + paramLocalMember);
/*      */     }
/* 1960 */     referencesMustNotBeFrozen();
/* 1961 */     UplevelReference uplevelReference = new UplevelReference(this, paramLocalMember);
/* 1962 */     this.references = uplevelReference.insertInto(this.references);
/* 1963 */     return uplevelReference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UplevelReference getReferences() {
/* 1970 */     return this.references;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UplevelReference getReferencesFrozen() {
/* 1979 */     this.referencesFrozen = true;
/* 1980 */     return this.references;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void referencesMustNotBeFrozen() {
/* 1987 */     if (this.referencesFrozen) {
/* 1988 */       throw new CompilerError("referencesMustNotBeFrozen " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberDefinition getClassLiteralLookup(long paramLong) {
/* 1996 */     throw new CompilerError("binary class");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDependency(ClassDeclaration paramClassDeclaration) {
/* 2003 */     throw new CompilerError("addDependency");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDefinition getLocalClass(String paramString) {
/* 2013 */     if (this.localClasses == null) {
/* 2014 */       return null;
/*      */     }
/* 2016 */     return (ClassDefinition)this.localClasses.get(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addLocalClass(ClassDefinition paramClassDefinition, String paramString) {
/* 2021 */     if (this.localClasses == null) {
/* 2022 */       this.localClasses = new Hashtable<>(31);
/*      */     }
/* 2024 */     this.localClasses.put(paramString, paramClassDefinition);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print(PrintStream paramPrintStream) {
/* 2032 */     if (isPublic()) {
/* 2033 */       paramPrintStream.print("public ");
/*      */     }
/* 2035 */     if (isInterface()) {
/* 2036 */       paramPrintStream.print("interface ");
/*      */     } else {
/* 2038 */       paramPrintStream.print("class ");
/*      */     } 
/* 2040 */     paramPrintStream.print(getName() + " ");
/* 2041 */     if (getSuperClass() != null) {
/* 2042 */       paramPrintStream.print("extends " + getSuperClass().getName() + " ");
/*      */     }
/* 2044 */     if (this.interfaces.length > 0) {
/* 2045 */       paramPrintStream.print("implements ");
/* 2046 */       for (byte b = 0; b < this.interfaces.length; b++) {
/* 2047 */         if (b > 0) {
/* 2048 */           paramPrintStream.print(", ");
/*      */         }
/* 2050 */         paramPrintStream.print(this.interfaces[b].getName());
/* 2051 */         paramPrintStream.print(" ");
/*      */       } 
/*      */     } 
/* 2054 */     paramPrintStream.println("{");
/*      */     
/* 2056 */     for (MemberDefinition memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 2057 */       paramPrintStream.print("    ");
/* 2058 */       memberDefinition.print(paramPrintStream);
/*      */     } 
/*      */     
/* 2061 */     paramPrintStream.println("}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2068 */     return getClassDeclaration().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanup(Environment paramEnvironment) {
/* 2076 */     if (paramEnvironment.dump()) {
/* 2077 */       paramEnvironment.output("[cleanup " + getName() + "]");
/*      */     }
/* 2079 */     for (MemberDefinition memberDefinition = getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 2080 */       memberDefinition.cleanup(paramEnvironment);
/*      */     }
/*      */     
/* 2083 */     this.documentation = null;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ClassDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */