/*      */ package com.sun.tools.javac.jvm;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.file.BaseFileObject;
/*      */ import com.sun.tools.javac.main.Option;
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
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.lang.model.type.TypeMirror;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.StandardJavaFileManager;
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
/*      */ public class ClassReader
/*      */ {
/*   78 */   protected static final Context.Key<ClassReader> classReaderKey = new Context.Key();
/*      */
/*      */
/*      */
/*      */
/*      */   public static final int INITIAL_BUFFER_SIZE = 65520;
/*      */
/*      */
/*      */
/*      */   Annotate annotate;
/*      */
/*      */
/*      */
/*      */   boolean verbose;
/*      */
/*      */
/*      */
/*      */   boolean checkClassFile;
/*      */
/*      */
/*      */
/*      */   public boolean readAllOfClassFile = false;
/*      */
/*      */
/*      */
/*      */   boolean allowGenerics;
/*      */
/*      */
/*      */
/*      */   boolean allowVarargs;
/*      */
/*      */
/*      */
/*      */   boolean allowAnnotations;
/*      */
/*      */
/*      */
/*      */   boolean allowSimplifiedVarargs;
/*      */
/*      */
/*      */
/*      */   boolean lintClassfile;
/*      */
/*      */
/*      */
/*      */   public boolean saveParameterNames;
/*      */
/*      */
/*      */
/*      */   private boolean cacheCompletionFailure;
/*      */
/*      */
/*      */
/*      */   public boolean preferSource;
/*      */
/*      */
/*      */
/*      */   public final Profile profile;
/*      */
/*      */
/*      */
/*      */   final Log log;
/*      */
/*      */
/*      */
/*      */   Symtab syms;
/*      */
/*      */
/*      */
/*      */   Types types;
/*      */
/*      */
/*      */
/*      */   final Names names;
/*      */
/*      */
/*      */
/*      */   final Name completionFailureName;
/*      */
/*      */
/*      */
/*      */   private final JavaFileManager fileManager;
/*      */
/*      */
/*      */
/*      */   JCDiagnostic.Factory diagFactory;
/*      */
/*      */
/*      */
/*  167 */   public SourceCompleter sourceCompleter = null;
/*      */
/*      */
/*      */
/*      */
/*      */   private Map<Name, Symbol.ClassSymbol> classes;
/*      */
/*      */
/*      */
/*      */   private Map<Name, Symbol.PackageSymbol> packages;
/*      */
/*      */
/*      */
/*      */   protected Scope typevars;
/*      */
/*      */
/*      */
/*  184 */   protected JavaFileObject currentClassFile = null;
/*      */
/*      */
/*      */
/*  188 */   protected Symbol currentOwner = null;
/*      */
/*      */
/*      */
/*  192 */   byte[] buf = new byte[65520];
/*      */
/*      */
/*      */
/*      */
/*      */   protected int bp;
/*      */
/*      */
/*      */
/*      */
/*      */   Object[] poolObj;
/*      */
/*      */
/*      */
/*      */
/*      */   int[] poolIdx;
/*      */
/*      */
/*      */
/*      */
/*      */   int majorVersion;
/*      */
/*      */
/*      */
/*      */   int minorVersion;
/*      */
/*      */
/*      */
/*      */   int[] parameterNameIndices;
/*      */
/*      */
/*      */
/*      */   boolean haveParameterNameIndices;
/*      */
/*      */
/*      */
/*      */   boolean sawMethodParameters;
/*      */
/*      */
/*      */
/*  232 */   Set<Name> warnedAttrs = new HashSet<>();
/*      */
/*      */
/*      */
/*      */
/*  237 */   private final Symbol.Completer thisCompleter = new Symbol.Completer()
/*      */     {
/*      */       public void complete(Symbol param1Symbol) throws Symbol.CompletionFailure {
/*  240 */         ClassReader.this.complete(param1Symbol);
/*      */       }
/*      */     };
/*      */   byte[] signature; int sigp; int siglimit; boolean sigEnterPhase; byte[] signatureBuffer; int sbp; protected Set<AttributeKind> CLASS_ATTRIBUTE; protected Set<AttributeKind> MEMBER_ATTRIBUTE; protected Set<AttributeKind> CLASS_OR_MEMBER_ATTRIBUTE; protected Map<Name, AttributeReader> attributeReaders; private boolean readingClassAttr; private List<Type> missingTypeVariables; private List<Type> foundTypeVariables; private boolean filling; private Symbol.CompletionFailure cachedCompletionFailure; protected JavaFileManager.Location currentLoc;
/*      */   private boolean verbosePath;
/*      */
/*      */   public static ClassReader instance(Context paramContext) {
/*  247 */     ClassReader classReader = (ClassReader)paramContext.get(classReaderKey);
/*  248 */     if (classReader == null)
/*  249 */       classReader = new ClassReader(paramContext, true);
/*  250 */     return classReader;
/*      */   }
/*      */
/*      */
/*      */   public void init(Symtab paramSymtab) {
/*  255 */     init(paramSymtab, true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void init(Symtab paramSymtab, boolean paramBoolean) {
/*  262 */     if (this.classes != null)
/*      */       return;
/*  264 */     if (paramBoolean) {
/*  265 */       Assert.check((this.packages == null || this.packages == paramSymtab.packages));
/*  266 */       this.packages = paramSymtab.packages;
/*  267 */       Assert.check((this.classes == null || this.classes == paramSymtab.classes));
/*  268 */       this.classes = paramSymtab.classes;
/*      */     } else {
/*  270 */       this.packages = new HashMap<>();
/*  271 */       this.classes = new HashMap<>();
/*      */     }
/*      */
/*  274 */     this.packages.put(this.names.empty, paramSymtab.rootPackage);
/*  275 */     paramSymtab.rootPackage.completer = this.thisCompleter;
/*  276 */     paramSymtab.unnamedPackage.completer = this.thisCompleter;
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
/*      */   private void enterMember(Symbol.ClassSymbol paramClassSymbol, Symbol paramSymbol) {
/*  330 */     if ((paramSymbol.flags_field & 0x80001000L) != 4096L || paramSymbol.name.startsWith(this.names.lambda)) {
/*  331 */       paramClassSymbol.members_field.enter(paramSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public class BadClassFile
/*      */     extends Symbol.CompletionFailure
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */
/*      */
/*      */     public BadClassFile(Symbol.TypeSymbol param1TypeSymbol, JavaFileObject param1JavaFileObject, JCDiagnostic param1JCDiagnostic) {
/*  343 */       super((Symbol)param1TypeSymbol, ClassReader.this.createBadClassFileDiagnostic(param1JavaFileObject, param1JCDiagnostic));
/*      */     }
/*      */   }
/*      */
/*      */   private JCDiagnostic createBadClassFileDiagnostic(JavaFileObject paramJavaFileObject, JCDiagnostic paramJCDiagnostic) {
/*  348 */     String str = (paramJavaFileObject.getKind() == JavaFileObject.Kind.SOURCE) ? "bad.source.file.header" : "bad.class.file.header";
/*      */
/*  350 */     return this.diagFactory.fragment(str, new Object[] { paramJavaFileObject, paramJCDiagnostic });
/*      */   }
/*      */
/*      */   public BadClassFile badClassFile(String paramString, Object... paramVarArgs) {
/*  354 */     return new BadClassFile((Symbol.TypeSymbol)this.currentOwner
/*  355 */         .enclClass(), this.currentClassFile, this.diagFactory
/*      */
/*  357 */         .fragment(paramString, paramVarArgs));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   char nextChar() {
/*  367 */     return (char)(((this.buf[this.bp++] & 0xFF) << 8) + (this.buf[this.bp++] & 0xFF));
/*      */   }
/*      */
/*      */
/*      */
/*      */   int nextByte() {
/*  373 */     return this.buf[this.bp++] & 0xFF;
/*      */   }
/*      */
/*      */
/*      */
/*      */   int nextInt() {
/*  379 */     return ((this.buf[this.bp++] & 0xFF) << 24) + ((this.buf[this.bp++] & 0xFF) << 16) + ((this.buf[this.bp++] & 0xFF) << 8) + (this.buf[this.bp++] & 0xFF);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   char getChar(int paramInt) {
/*  389 */     return (char)(((this.buf[paramInt] & 0xFF) << 8) + (this.buf[paramInt + 1] & 0xFF));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   int getInt(int paramInt) {
/*  396 */     return ((this.buf[paramInt] & 0xFF) << 24) + ((this.buf[paramInt + 1] & 0xFF) << 16) + ((this.buf[paramInt + 2] & 0xFF) << 8) + (this.buf[paramInt + 3] & 0xFF);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   long getLong(int paramInt) {
/*  407 */     DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 8));
/*      */
/*      */     try {
/*  410 */       return dataInputStream.readLong();
/*  411 */     } catch (IOException iOException) {
/*  412 */       throw new AssertionError(iOException);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   float getFloat(int paramInt) {
/*  419 */     DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 4));
/*      */
/*      */     try {
/*  422 */       return dataInputStream.readFloat();
/*  423 */     } catch (IOException iOException) {
/*  424 */       throw new AssertionError(iOException);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   double getDouble(int paramInt) {
/*  431 */     DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 8));
/*      */
/*      */     try {
/*  434 */       return dataInputStream.readDouble();
/*  435 */     } catch (IOException iOException) {
/*  436 */       throw new AssertionError(iOException);
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
/*      */   void indexPool() {
/*  448 */     this.poolIdx = new int[nextChar()];
/*  449 */     this.poolObj = new Object[this.poolIdx.length];
/*  450 */     byte b = 1;
/*  451 */     while (b < this.poolIdx.length) {
/*  452 */       char c; this.poolIdx[b++] = this.bp;
/*  453 */       byte b1 = this.buf[this.bp++];
/*  454 */       switch (b1) { case 1:
/*      */         case 2:
/*  456 */           c = nextChar();
/*  457 */           this.bp += c;
/*      */           continue;
/*      */
/*      */         case 7:
/*      */         case 8:
/*      */         case 16:
/*  463 */           this.bp += 2;
/*      */           continue;
/*      */         case 15:
/*  466 */           this.bp += 3;
/*      */           continue;
/*      */         case 3:
/*      */         case 4:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 18:
/*  475 */           this.bp += 4;
/*      */           continue;
/*      */         case 5:
/*      */         case 6:
/*  479 */           this.bp += 8;
/*  480 */           b++;
/*      */           continue; }
/*      */
/*  483 */       throw badClassFile("bad.const.pool.tag.at", new Object[] {
/*  484 */             Byte.toString(b1),
/*  485 */             Integer.toString(this.bp - 1)
/*      */           });
/*      */     }
/*      */   }
/*      */
/*      */   Object readPool(int paramInt) {
/*      */     Symbol.ClassSymbol classSymbol;
/*      */     ClassFile.NameAndType nameAndType;
/*  493 */     Object object = this.poolObj[paramInt];
/*  494 */     if (object != null) return object;
/*      */
/*  496 */     int i = this.poolIdx[paramInt];
/*  497 */     if (i == 0) return null;
/*      */
/*  499 */     byte b = this.buf[i];
/*  500 */     switch (b) {
/*      */       case 1:
/*  502 */         this.poolObj[paramInt] = this.names.fromUtf(this.buf, i + 3, getChar(i + 1));
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  555 */         return this.poolObj[paramInt];case 2: throw badClassFile("unicode.str.not.supported", new Object[0]);case 7: this.poolObj[paramInt] = readClassOrType(getChar(i + 1)); return this.poolObj[paramInt];case 8: this.poolObj[paramInt] = readName(getChar(i + 1)).toString(); return this.poolObj[paramInt];case 9: classSymbol = readClassSymbol(getChar(i + 1)); nameAndType = readNameAndType(getChar(i + 3)); this.poolObj[paramInt] = new Symbol.VarSymbol(0L, nameAndType.name, nameAndType.uniqueType.type, (Symbol)classSymbol); return this.poolObj[paramInt];case 10: case 11: classSymbol = readClassSymbol(getChar(i + 1)); nameAndType = readNameAndType(getChar(i + 3)); this.poolObj[paramInt] = new Symbol.MethodSymbol(0L, nameAndType.name, nameAndType.uniqueType.type, (Symbol)classSymbol); return this.poolObj[paramInt];case 12: this.poolObj[paramInt] = new ClassFile.NameAndType(readName(getChar(i + 1)), readType(getChar(i + 3)), this.types); return this.poolObj[paramInt];case 3: this.poolObj[paramInt] = Integer.valueOf(getInt(i + 1)); return this.poolObj[paramInt];case 4: this.poolObj[paramInt] = new Float(getFloat(i + 1)); return this.poolObj[paramInt];case 5: this.poolObj[paramInt] = new Long(getLong(i + 1)); return this.poolObj[paramInt];case 6: this.poolObj[paramInt] = new Double(getDouble(i + 1)); return this.poolObj[paramInt];case 15: skipBytes(4); return this.poolObj[paramInt];case 16: skipBytes(3); return this.poolObj[paramInt];case 18: skipBytes(5); return this.poolObj[paramInt];
/*      */     }
/*      */     throw badClassFile("bad.const.pool.tag", new Object[] { Byte.toString(b) });
/*      */   }
/*      */
/*      */   Type readType(int paramInt) {
/*  561 */     int i = this.poolIdx[paramInt];
/*  562 */     return sigToType(this.buf, i + 3, getChar(i + 1));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Object readClassOrType(int paramInt) {
/*  569 */     int i = this.poolIdx[paramInt];
/*  570 */     char c = getChar(i + 1);
/*  571 */     int j = i + 3;
/*  572 */     Assert.check((this.buf[j] == 91 || this.buf[j + c - 1] != 59));
/*      */
/*      */
/*  575 */     return (this.buf[j] == 91 || this.buf[j + c - 1] == 59) ?
/*  576 */       sigToType(this.buf, j, c) :
/*  577 */       enterClass(this.names.fromUtf(ClassFile.internalize(this.buf, j, c)));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   List<Type> readTypeParams(int paramInt) {
/*  584 */     int i = this.poolIdx[paramInt];
/*  585 */     return sigToTypeParams(this.buf, i + 3, getChar(i + 1));
/*      */   }
/*      */
/*      */
/*      */
/*      */   Symbol.ClassSymbol readClassSymbol(int paramInt) {
/*  591 */     Object object = readPool(paramInt);
/*  592 */     if (object != null && !(object instanceof Symbol.ClassSymbol))
/*  593 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  594 */             .toString(), "CONSTANT_Class_info",
/*  595 */             Integer.valueOf(paramInt) });
/*  596 */     return (Symbol.ClassSymbol)object;
/*      */   }
/*      */
/*      */
/*      */
/*      */   Name readName(int paramInt) {
/*  602 */     Object object = readPool(paramInt);
/*  603 */     if (object != null && !(object instanceof Name))
/*  604 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  605 */             .toString(), "CONSTANT_Utf8_info or CONSTANT_String_info",
/*  606 */             Integer.valueOf(paramInt) });
/*  607 */     return (Name)object;
/*      */   } Type sigToType(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) { this.signature = paramArrayOfbyte; this.sigp = paramInt1; this.siglimit = paramInt1 + paramInt2; return sigToType(); } Type sigToType() { int i; Type type1; List<Type> list; Type type2; List list1; List list2; Type.ForAll forAll; switch ((char)this.signature[this.sigp]) { case 'T': i = ++this.sigp; for (; this.signature[this.sigp] != 59; this.sigp++); this.sigp++; return this.sigEnterPhase ? (Type)Type.noType : findTypeVar(this.names.fromUtf(this.signature, i, this.sigp - 1 - i));case '+': this.sigp++; type1 = sigToType(); return (Type)new Type.WildcardType(type1, BoundKind.EXTENDS, (Symbol.TypeSymbol)this.syms.boundClass);case '*': this.sigp++; return (Type)new Type.WildcardType(this.syms.objectType, BoundKind.UNBOUND, (Symbol.TypeSymbol)this.syms.boundClass);case '-': this.sigp++; type1 = sigToType(); return (Type)new Type.WildcardType(type1, BoundKind.SUPER, (Symbol.TypeSymbol)this.syms.boundClass);case 'B': this.sigp++; return (Type)this.syms.byteType;case 'C': this.sigp++; return (Type)this.syms.charType;case 'D': this.sigp++; return (Type)this.syms.doubleType;case 'F': this.sigp++; return (Type)this.syms.floatType;case 'I': this.sigp++; return (Type)this.syms.intType;case 'J': this.sigp++; return (Type)this.syms.longType;case 'L': type1 = classSigToType(); if (this.sigp < this.siglimit && this.signature[this.sigp] == 46) throw badClassFile("deprecated inner class signature syntax (please recompile from source)", new Object[0]);  return type1;case 'S': this.sigp++; return (Type)this.syms.shortType;case 'V': this.sigp++; return (Type)this.syms.voidType;case 'Z': this.sigp++; return (Type)this.syms.booleanType;case '[': this.sigp++; return (Type)new Type.ArrayType(sigToType(), (Symbol.TypeSymbol)this.syms.arrayClass);case '(': this.sigp++; list = sigToTypes(')'); type2 = sigToType(); list1 = List.nil(); while (this.signature[this.sigp] == 94) { this.sigp++; list1 = list1.prepend(sigToType()); }  for (list2 = list1; list2.nonEmpty(); list2 = list2.tail) { if (((Type)list2.head).hasTag(TypeTag.TYPEVAR)) ((Type)list2.head).tsym.flags_field |= 0x800000000000L;  }  return (Type)new Type.MethodType(list, type2, list1.reverse(), (Symbol.TypeSymbol)this.syms.methodClass);case '<': this.typevars = this.typevars.dup(this.currentOwner); forAll = new Type.ForAll(sigToTypeParams(), sigToType()); this.typevars = this.typevars.leave(); return (Type)forAll; }  throw badClassFile("bad.signature", new Object[] { Convert.utf2string(this.signature, this.sigp, 10) }); } Type classSigToType() { if (this.signature[this.sigp] != 76) throw badClassFile("bad.class.signature", new Object[] { Convert.utf2string(this.signature, this.sigp, 10) });  this.sigp++; Type.JCNoType jCNoType = Type.noType; int i = this.sbp; while (true) { Type.ClassType classType; Symbol.ClassSymbol classSymbol; byte b = this.signature[this.sigp++]; switch (b) { case 59: classSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i)); try { return (Type)((jCNoType == Type.noType) ? classSymbol.erasure(this.types) : new Type.ClassType((Type)jCNoType, List.nil(), (Symbol.TypeSymbol)classSymbol)); } finally { this.sbp = i; } case 60: classSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i)); classType = new Type.ClassType((Type)jCNoType, sigToTypes('>'), (Symbol.TypeSymbol)classSymbol) { boolean completed = false; public Type getEnclosingType() { if (!this.completed) { this.completed = true; this.tsym.complete(); Type type = this.tsym.type.getEnclosingType(); if (type != Type.noType) { List list1 = super.getEnclosingType().allparams(); List list2 = type.allparams(); if (list2.length() != list1.length()) { super.setEnclosingType(ClassReader.this.types.erasure(type)); } else { super.setEnclosingType(ClassReader.this.types.subst(type, list2, list1)); }  } else { super.setEnclosingType((Type)Type.noType); }  }  return super.getEnclosingType(); } public void setEnclosingType(Type param1Type) { throw new UnsupportedOperationException(); } }
/*      */             ; switch (this.signature[this.sigp++]) { case 59: if (this.sigp < this.signature.length && this.signature[this.sigp] == 46) { this.sigp += this.sbp - i + 3; this.signatureBuffer[this.sbp++] = 36; continue; }  this.sbp = i; return (Type)classType;case 46: this.signatureBuffer[this.sbp++] = 36; continue; }  throw new AssertionError(this.signature[this.sigp - 1]);case 46: if (classType != Type.noType) { classSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i)); classType = new Type.ClassType((Type)classType, List.nil(), (Symbol.TypeSymbol)classSymbol); }  this.signatureBuffer[this.sbp++] = 36; continue;case 47: this.signatureBuffer[this.sbp++] = 46; continue; }  this.signatureBuffer[this.sbp++] = b; }  } List<Type> sigToTypes(char paramChar) { List list1 = List.of(null); List list2 = list1; while (this.signature[this.sigp] != paramChar) list2 = list2.setTail(List.of(sigToType()));  this.sigp++; return list1.tail; } List<Type> sigToTypeParams(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) { this.signature = paramArrayOfbyte; this.sigp = paramInt1; this.siglimit = paramInt1 + paramInt2; return sigToTypeParams(); } List<Type> sigToTypeParams() { List list = List.nil(); if (this.signature[this.sigp] == 60) { int i = ++this.sigp; this.sigEnterPhase = true; while (this.signature[this.sigp] != 62) list = list.prepend(sigToTypeParam());  this.sigEnterPhase = false; this.sigp = i; while (this.signature[this.sigp] != 62) sigToTypeParam();  this.sigp++; }  return list.reverse(); } Type sigToTypeParam() { Type.TypeVar typeVar; int i = this.sigp; for (; this.signature[this.sigp] != 58; this.sigp++); Name name = this.names.fromUtf(this.signature, i, this.sigp - i); if (this.sigEnterPhase) { typeVar = new Type.TypeVar(name, this.currentOwner, this.syms.botType); this.typevars.enter((Symbol)typeVar.tsym); } else { typeVar = (Type.TypeVar)findTypeVar(name); }  List list = List.nil(); boolean bool = false; if (this.signature[this.sigp] == 58 && this.signature[this.sigp + 1] == 58) { this.sigp++; bool = true; }  while (this.signature[this.sigp] == 58) { this.sigp++; list = list.prepend(sigToType()); }  if (!this.sigEnterPhase) this.types.setBounds(typeVar, list.reverse(), bool);  return (Type)typeVar; } Type findTypeVar(Name paramName) { Scope.Entry entry = this.typevars.lookup(paramName); if (entry.scope != null) return entry.sym.type;  if (this.readingClassAttr) { Type.TypeVar typeVar = new Type.TypeVar(paramName, this.currentOwner, this.syms.botType); this.missingTypeVariables = this.missingTypeVariables.prepend(typeVar); return (Type)typeVar; }  throw badClassFile("undecl.type.var", new Object[] { paramName }); } protected enum AttributeKind {
/*      */     CLASS, MEMBER; } protected abstract class AttributeReader {
/*      */     protected final Name name; protected final ClassFile.Version version; protected final Set<AttributeKind> kinds; protected AttributeReader(Name param1Name, ClassFile.Version param1Version, Set<AttributeKind> param1Set) { this.name = param1Name; this.version = param1Version; this.kinds = param1Set; } protected boolean accepts(AttributeKind param1AttributeKind) { if (this.kinds.contains(param1AttributeKind)) { if (ClassReader.this.majorVersion > this.version.major || (ClassReader.this.majorVersion == this.version.major && ClassReader.this.minorVersion >= this.version.minor)) return true;  if (ClassReader.this.lintClassfile && !ClassReader.this.warnedAttrs.contains(this.name)) { JavaFileObject javaFileObject = ClassReader.this.log.useSource(ClassReader.this.currentClassFile); try { ClassReader.this.log.warning(Lint.LintCategory.CLASSFILE, (JCDiagnostic.DiagnosticPosition)null, "future.attr", new Object[] { this.name, Integer.valueOf(this.version.major), Integer.valueOf(this.version.minor), Integer.valueOf(this.this$0.majorVersion), Integer.valueOf(this.this$0.minorVersion) }); } finally { ClassReader.this.log.useSource(javaFileObject); }  ClassReader.this.warnedAttrs.add(this.name); }  }  return false; } protected abstract void read(Symbol param1Symbol, int param1Int); } private void initAttributeReaders() { AttributeReader[] arrayOfAttributeReader = { new AttributeReader(this.names.Code, ClassFile.Version.V45_3, this.MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { if (ClassReader.this.readAllOfClassFile || ClassReader.this.saveParameterNames) { ((Symbol.MethodSymbol)param1Symbol).code = ClassReader.this.readCode(param1Symbol); } else { ClassReader.this.bp += param1Int; }  } }
/*      */         , new AttributeReader(this.names.ConstantValue, ClassFile.Version.V45_3, this.MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { Object object = ClassReader.this.readPool(ClassReader.this.nextChar()); if ((param1Symbol.flags() & 0x10L) != 0L) ((Symbol.VarSymbol)param1Symbol).setData(object);  } }
/*  613 */         , new AttributeReader(this.names.Deprecated, ClassFile.Version.V45_3, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { param1Symbol.flags_field |= 0x20000L; } }, new AttributeReader(this.names.Exceptions, ClassFile.Version.V45_3, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { char c = ClassReader.this.nextChar(); List list = List.nil(); for (byte b = 0; b < c; b++) list = list.prepend((ClassReader.this.readClassSymbol(ClassReader.this.nextChar())).type);  if (param1Symbol.type.getThrownTypes().isEmpty()) (param1Symbol.type.asMethodType()).thrown = list.reverse();  } }, new AttributeReader(this.names.InnerClasses, ClassFile.Version.V45_3, this.CLASS_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1Symbol; ClassReader.this.readInnerClasses(classSymbol); } }, new AttributeReader(this.names.LocalVariableTable, ClassFile.Version.V45_3, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { int i = ClassReader.this.bp + param1Int; if (ClassReader.this.saveParameterNames && !ClassReader.this.sawMethodParameters) { char c = ClassReader.this.nextChar(); for (byte b = 0; b < c; b++) { char c1 = ClassReader.this.nextChar(); char c2 = ClassReader.this.nextChar(); char c3 = ClassReader.this.nextChar(); char c4 = ClassReader.this.nextChar(); char c5 = ClassReader.this.nextChar(); if (c1 == '\000') { if (c5 >= ClassReader.this.parameterNameIndices.length) { int j = Math.max(c5, ClassReader.this.parameterNameIndices.length + 8); ClassReader.this.parameterNameIndices = Arrays.copyOf(ClassReader.this.parameterNameIndices, j); }  ClassReader.this.parameterNameIndices[c5] = c3; ClassReader.this.haveParameterNameIndices = true; }  }  }  ClassReader.this.bp = i; } }, new AttributeReader(this.names.MethodParameters, ClassFile.Version.V52, this.MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { int i = ClassReader.this.bp + param1Int; if (ClassReader.this.saveParameterNames) { ClassReader.this.sawMethodParameters = true; int j = ClassReader.this.nextByte(); ClassReader.this.parameterNameIndices = new int[j]; ClassReader.this.haveParameterNameIndices = true; for (byte b = 0; b < j; b++) { char c1 = ClassReader.this.nextChar(); char c2 = ClassReader.this.nextChar(); ClassReader.this.parameterNameIndices[b] = c1; }  }  ClassReader.this.bp = i; } }, new AttributeReader(this.names.SourceFile, ClassFile.Version.V45_3, this.CLASS_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1Symbol; Name name = ClassReader.this.readName(ClassReader.this.nextChar()); classSymbol.sourcefile = (JavaFileObject)new SourceFileObject(name, classSymbol.flatname); String str = name.toString(); if (classSymbol.owner.kind == 1 && str.endsWith(".java") && !str.equals(classSymbol.name.toString() + ".java")) classSymbol.flags_field |= 0x100000000000L;  } }, new AttributeReader(this.names.Synthetic, ClassFile.Version.V45_3, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { if (ClassReader.this.allowGenerics || (param1Symbol.flags_field & 0x80000000L) == 0L) param1Symbol.flags_field |= 0x1000L;  } }, new AttributeReader(this.names.EnclosingMethod, ClassFile.Version.V49, this.CLASS_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { int i = ClassReader.this.bp + param1Int; ClassReader.this.readEnclosingMethodAttr(param1Symbol); ClassReader.this.bp = i; } }, new AttributeReader(this.names.Signature, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected boolean accepts(AttributeKind param1AttributeKind) { return (super.accepts(param1AttributeKind) && ClassReader.this.allowGenerics); } protected void read(Symbol param1Symbol, int param1Int) { if (param1Symbol.kind == 2) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1Symbol; ClassReader.this.readingClassAttr = true; try { Type.ClassType classType = (Type.ClassType)classSymbol.type; Assert.check((classSymbol == ClassReader.this.currentOwner)); classType.typarams_field = ClassReader.this.readTypeParams(ClassReader.this.nextChar()); classType.supertype_field = ClassReader.this.sigToType(); ListBuffer listBuffer = new ListBuffer(); for (; ClassReader.this.sigp != ClassReader.this.siglimit; listBuffer.append(ClassReader.this.sigToType())); classType.interfaces_field = listBuffer.toList(); } finally { ClassReader.this.readingClassAttr = false; }  } else { List list = param1Symbol.type.getThrownTypes(); param1Symbol.type = ClassReader.this.readType(ClassReader.this.nextChar()); if (param1Symbol.kind == 16 && param1Symbol.type.getThrownTypes().isEmpty()) (param1Symbol.type.asMethodType()).thrown = list;  }  } }, new AttributeReader(this.names.AnnotationDefault, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachAnnotationDefault(param1Symbol); } }, new AttributeReader(this.names.RuntimeInvisibleAnnotations, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachAnnotations(param1Symbol); } }, new AttributeReader(this.names.RuntimeInvisibleParameterAnnotations, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachParameterAnnotations(param1Symbol); } }, new AttributeReader(this.names.RuntimeVisibleAnnotations, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachAnnotations(param1Symbol); } }, new AttributeReader(this.names.RuntimeVisibleParameterAnnotations, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachParameterAnnotations(param1Symbol); } }, new AttributeReader(this.names.Annotation, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { if (ClassReader.this.allowAnnotations) param1Symbol.flags_field |= 0x2000L;  } }, new AttributeReader(this.names.Bridge, ClassFile.Version.V49, this.MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { param1Symbol.flags_field |= 0x80000000L; if (!ClassReader.this.allowGenerics) param1Symbol.flags_field &= 0xFFFFFFFFFFFFEFFFL;  } }, new AttributeReader(this.names.Enum, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { param1Symbol.flags_field |= 0x4000L; } }, new AttributeReader(this.names.Varargs, ClassFile.Version.V49, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { if (ClassReader.this.allowVarargs) param1Symbol.flags_field |= 0x400000000L;  } }, new AttributeReader(this.names.RuntimeVisibleTypeAnnotations, ClassFile.Version.V52, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachTypeAnnotations(param1Symbol); } }, new AttributeReader(this.names.RuntimeInvisibleTypeAnnotations, ClassFile.Version.V52, this.CLASS_OR_MEMBER_ATTRIBUTE) { protected void read(Symbol param1Symbol, int param1Int) { ClassReader.this.attachTypeAnnotations(param1Symbol); } } }; for (AttributeReader attributeReader : arrayOfAttributeReader) this.attributeReaders.put(attributeReader.name, attributeReader);  } void unrecognized(Name paramName) { if (this.checkClassFile) printCCF("ccf.unrecognized.attribute", paramName);  } protected void readEnclosingMethodAttr(Symbol paramSymbol) { paramSymbol.owner.members().remove(paramSymbol); Symbol.ClassSymbol classSymbol1 = (Symbol.ClassSymbol)paramSymbol; Symbol.ClassSymbol classSymbol2 = readClassSymbol(nextChar()); ClassFile.NameAndType nameAndType = readNameAndType(nextChar()); if (classSymbol2.members_field == null) throw badClassFile("bad.enclosing.class", new Object[] { classSymbol1, classSymbol2 });  Symbol.MethodSymbol methodSymbol = findMethod(nameAndType, classSymbol2.members_field, classSymbol1.flags()); if (nameAndType != null && methodSymbol == null) throw badClassFile("bad.enclosing.method", new Object[] { classSymbol1 });  classSymbol1.name = simpleBinaryName(classSymbol1.flatname, classSymbol2.flatname); classSymbol1.owner = (methodSymbol != null) ? (Symbol)methodSymbol : (Symbol)classSymbol2; if (classSymbol1.name.isEmpty()) { classSymbol1.fullname = this.names.empty; } else { classSymbol1.fullname = Symbol.ClassSymbol.formFullName(classSymbol1.name, classSymbol1.owner); }  if (methodSymbol != null) { ((Type.ClassType)paramSymbol.type).setEnclosingType(methodSymbol.type); } else if ((classSymbol1.flags_field & 0x8L) == 0L) { ((Type.ClassType)paramSymbol.type).setEnclosingType(classSymbol2.type); } else { ((Type.ClassType)paramSymbol.type).setEnclosingType((Type)Type.noType); }  enterTypevars((Symbol)classSymbol1); if (!this.missingTypeVariables.isEmpty()) { ListBuffer listBuffer = new ListBuffer(); for (Type type : this.missingTypeVariables) listBuffer.append(findTypeVar(type.tsym.name));  this.foundTypeVariables = listBuffer.toList(); } else { this.foundTypeVariables = List.nil(); }  } private Name simpleBinaryName(Name paramName1, Name paramName2) { String str = paramName1.toString().substring(paramName2.toString().length()); if (str.length() < 1 || str.charAt(0) != '$') throw badClassFile("bad.enclosing.method", new Object[] { paramName1 });  byte b = 1; while (b < str.length() && isAsciiDigit(str.charAt(b))) b++;  return this.names.fromString(str.substring(b)); } private Symbol.MethodSymbol findMethod(ClassFile.NameAndType paramNameAndType, Scope paramScope, long paramLong) { if (paramNameAndType == null) return null;  Type.MethodType methodType = paramNameAndType.uniqueType.type.asMethodType(); for (Scope.Entry entry = paramScope.lookup(paramNameAndType.name); entry.scope != null; entry = entry.next()) { if (entry.sym.kind == 16 && isSameBinaryType(entry.sym.type.asMethodType(), methodType)) return (Symbol.MethodSymbol)entry.sym;  }  if (paramNameAndType.name != this.names.init) return null;  if ((paramLong & 0x200L) != 0L) return null;  if (paramNameAndType.uniqueType.type.getParameterTypes().isEmpty()) return null;  paramNameAndType.setType((Type)new Type.MethodType((paramNameAndType.uniqueType.type.getParameterTypes()).tail, paramNameAndType.uniqueType.type.getReturnType(), paramNameAndType.uniqueType.type.getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass)); return findMethod(paramNameAndType, paramScope, paramLong); } private boolean isSameBinaryType(Type.MethodType paramMethodType1, Type.MethodType paramMethodType2) { List list1 = this.types.erasure(paramMethodType1.getParameterTypes()).prepend(this.types.erasure(paramMethodType1.getReturnType())); List list2 = paramMethodType2.getParameterTypes().prepend(paramMethodType2.getReturnType()); while (!list1.isEmpty() && !list2.isEmpty()) { if (((Type)list1.head).tsym != ((Type)list2.head).tsym) return false;  list1 = list1.tail; list2 = list2.tail; }  return (list1.isEmpty() && list2.isEmpty()); } private static boolean isAsciiDigit(char paramChar) { return ('0' <= paramChar && paramChar <= '9'); } void readMemberAttrs(Symbol paramSymbol) { readAttrs(paramSymbol, AttributeKind.MEMBER); } ClassFile.NameAndType readNameAndType(int paramInt) { Object object = readPool(paramInt);
/*  614 */     if (object != null && !(object instanceof ClassFile.NameAndType))
/*  615 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  616 */             .toString(), "CONSTANT_NameAndType_info",
/*  617 */             Integer.valueOf(paramInt) });
/*  618 */     return (ClassFile.NameAndType)object; }
/*      */   void readAttrs(Symbol paramSymbol, AttributeKind paramAttributeKind) { char c = nextChar(); for (byte b = 0; b < c; b++) { Name name = readName(nextChar()); int i = nextInt(); AttributeReader attributeReader = this.attributeReaders.get(name); if (attributeReader != null && attributeReader.accepts(paramAttributeKind)) { attributeReader.read(paramSymbol, i); } else { unrecognized(name); this.bp += i; }  }  }
/*      */   void readClassAttrs(Symbol.ClassSymbol paramClassSymbol) { readAttrs((Symbol)paramClassSymbol, AttributeKind.CLASS); }
/*      */   Code readCode(Symbol paramSymbol) { nextChar(); nextChar(); int i = nextInt(); this.bp += i; char c = nextChar(); this.bp += c * 8; readMemberAttrs(paramSymbol); return null; }
/*      */   void attachAnnotations(Symbol paramSymbol) { char c = nextChar(); if (c != '\000') { ListBuffer listBuffer = new ListBuffer(); for (byte b = 0; b < c; b++) { CompoundAnnotationProxy compoundAnnotationProxy = readCompoundAnnotation(); if (compoundAnnotationProxy.type.tsym == this.syms.proprietaryType.tsym) { paramSymbol.flags_field |= 0x4000000000L; } else if (compoundAnnotationProxy.type.tsym == this.syms.profileType.tsym) { if (this.profile != Profile.DEFAULT) for (Pair<Name, Attribute> pair : compoundAnnotationProxy.values) { if (pair.fst == this.names.value && pair.snd instanceof Attribute.Constant) { Attribute.Constant constant = (Attribute.Constant)pair.snd; if (constant.type == this.syms.intType && ((Integer)constant.value).intValue() > this.profile.value) paramSymbol.flags_field |= 0x200000000000L;  }  }   } else { listBuffer.append(compoundAnnotationProxy); }  }  this.annotate.normal(new AnnotationCompleter(paramSymbol, listBuffer.toList())); }  }
/*      */   void attachParameterAnnotations(Symbol paramSymbol) { Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)paramSymbol; int i = this.buf[this.bp++] & 0xFF; List list = methodSymbol.params(); byte b = 0; while (list.tail != null) { attachAnnotations((Symbol)list.head); list = list.tail; b++; }  if (b != i)
/*      */       throw badClassFile("bad.runtime.invisible.param.annotations", new Object[] { methodSymbol });  }
/*      */   void attachTypeAnnotations(Symbol paramSymbol) { char c = nextChar(); if (c != '\000') { ListBuffer listBuffer = new ListBuffer(); for (byte b = 0; b < c; b++)
/*      */         listBuffer.append(readTypeAnnotation());  this.annotate.normal(new TypeAnnotationCompleter(paramSymbol, listBuffer.toList())); }  }
/*      */   void attachAnnotationDefault(Symbol paramSymbol) { Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)paramSymbol; Attribute attribute = readAttributeValue(); methodSymbol.defaultValue = attribute; this.annotate.normal(new AnnotationDefaultCompleter(methodSymbol, attribute)); }
/*      */   Type readTypeOrClassSymbol(int paramInt) { if (this.buf[this.poolIdx[paramInt]] == 7)
/*      */       return (readClassSymbol(paramInt)).type;  return readType(paramInt); }
/*      */   Type readEnumType(int paramInt) { int i = this.poolIdx[paramInt]; char c = getChar(i + 1); if (this.buf[i + c + 2] != 59)
/*  631 */       return (enterClass(readName(paramInt))).type;  return readType(paramInt); } protected ClassReader(Context paramContext, boolean paramBoolean) { this.sigEnterPhase = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  744 */     this.signatureBuffer = new byte[0];
/*  745 */     this.sbp = 0;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  990 */     this
/*  991 */       .CLASS_ATTRIBUTE = EnumSet.of(AttributeKind.CLASS);
/*  992 */     this
/*  993 */       .MEMBER_ATTRIBUTE = EnumSet.of(AttributeKind.MEMBER);
/*  994 */     this
/*  995 */       .CLASS_OR_MEMBER_ATTRIBUTE = EnumSet.of(AttributeKind.CLASS, AttributeKind.MEMBER);
/*      */
/*  997 */     this.attributeReaders = new HashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1389 */     this.readingClassAttr = false;
/* 1390 */     this.missingTypeVariables = List.nil();
/* 1391 */     this.foundTypeVariables = List.nil();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2484 */     this.filling = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2609 */     this.cachedCompletionFailure = new Symbol.CompletionFailure(null, (JCDiagnostic)null);
/*      */
/*      */
/* 2612 */     this.cachedCompletionFailure.setStackTrace(new StackTraceElement[0]);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2739 */     this.verbosePath = true; if (paramBoolean) paramContext.put(classReaderKey, this);  this.names = Names.instance(paramContext); this.syms = Symtab.instance(paramContext); this.types = Types.instance(paramContext); this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class); if (this.fileManager == null) throw new AssertionError("FileManager initialization error");  this.diagFactory = JCDiagnostic.Factory.instance(paramContext); init(this.syms, paramBoolean); this.log = Log.instance(paramContext); Options options = Options.instance(paramContext); this.annotate = Annotate.instance(paramContext); this.verbose = options.isSet(Option.VERBOSE); this.checkClassFile = options.isSet("-checkclassfile"); Source source = Source.instance(paramContext); this.allowGenerics = source.allowGenerics(); this.allowVarargs = source.allowVarargs(); this.allowAnnotations = source.allowAnnotations(); this.allowSimplifiedVarargs = source.allowSimplifiedVarargs(); this.saveParameterNames = options.isSet("save-parameter-names"); this.cacheCompletionFailure = options.isUnset("dev"); this.preferSource = "source".equals(options.get("-Xprefer")); this.profile = Profile.instance(paramContext); this.completionFailureName = options.isSet("failcomplete") ? this.names.fromString(options.get("failcomplete")) : null; this.typevars = new Scope((Symbol)this.syms.noSymbol); this.lintClassfile = Lint.instance(paramContext).isEnabled(Lint.LintCategory.CLASSFILE); initAttributeReaders(); } CompoundAnnotationProxy readCompoundAnnotation() { Type type = readTypeOrClassSymbol(nextChar()); char c = nextChar(); ListBuffer listBuffer = new ListBuffer(); for (byte b = 0; b < c; b++) { Name name = readName(nextChar()); Attribute attribute = readAttributeValue(); listBuffer.append(new Pair(name, attribute)); }  return new CompoundAnnotationProxy(type, listBuffer.toList()); } TypeAnnotationProxy readTypeAnnotation() { TypeAnnotationPosition typeAnnotationPosition = readPosition(); CompoundAnnotationProxy compoundAnnotationProxy = readCompoundAnnotation(); return new TypeAnnotationProxy(compoundAnnotationProxy, typeAnnotationPosition); } TypeAnnotationPosition readPosition() { char c; byte b1; int i = nextByte(); if (!TargetType.isValidTargetTypeValue(i)) throw badClassFile("bad.type.annotation.value", new Object[] { String.format("0x%02X", new Object[] { Integer.valueOf(i) }) });  TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition(); TargetType targetType = TargetType.fromTargetTypeValue(i); typeAnnotationPosition.type = targetType; switch (targetType) { case CLASS: case SOURCE: case null: case null: typeAnnotationPosition.offset = nextChar(); break;case null: case null: c = nextChar(); typeAnnotationPosition.lvarOffset = new int[c]; typeAnnotationPosition.lvarLength = new int[c]; typeAnnotationPosition.lvarIndex = new int[c]; for (b1 = 0; b1 < c; b1++) { typeAnnotationPosition.lvarOffset[b1] = nextChar(); typeAnnotationPosition.lvarLength[b1] = nextChar(); typeAnnotationPosition.lvarIndex[b1] = nextChar(); }  break;case null: typeAnnotationPosition.exception_index = nextChar(); break;case null: break;case null: case null: typeAnnotationPosition.parameter_index = nextByte(); break;case null: case null: typeAnnotationPosition.parameter_index = nextByte(); typeAnnotationPosition.bound_index = nextByte(); break;case null: typeAnnotationPosition.type_index = nextChar(); break;case null: typeAnnotationPosition.type_index = nextChar(); break;case null: typeAnnotationPosition.parameter_index = nextByte(); break;case null: case null: case null: case null: case null: typeAnnotationPosition.offset = nextChar(); typeAnnotationPosition.type_index = nextByte(); break;case null: case null: break;case null: throw new AssertionError("jvm.ClassReader: UNKNOWN target type should never occur!");default: throw new AssertionError("jvm.ClassReader: Unknown target type for position: " + typeAnnotationPosition); }  int j = nextByte(); ListBuffer listBuffer = new ListBuffer(); for (byte b2 = 0; b2 < j * 2; b2++) listBuffer = listBuffer.append(Integer.valueOf(nextByte()));  typeAnnotationPosition.location = TypeAnnotationPosition.getTypePathFromBinary((List)listBuffer.toList()); return typeAnnotationPosition; } Attribute readAttributeValue() { char c2; ListBuffer listBuffer; byte b; char c1 = (char)this.buf[this.bp++]; switch (c1) { case 'B': return (Attribute)new Attribute.Constant((Type)this.syms.byteType, readPool(nextChar()));case 'C': return (Attribute)new Attribute.Constant((Type)this.syms.charType, readPool(nextChar()));case 'D': return (Attribute)new Attribute.Constant((Type)this.syms.doubleType, readPool(nextChar()));case 'F': return (Attribute)new Attribute.Constant((Type)this.syms.floatType, readPool(nextChar()));case 'I': return (Attribute)new Attribute.Constant((Type)this.syms.intType, readPool(nextChar()));case 'J': return (Attribute)new Attribute.Constant((Type)this.syms.longType, readPool(nextChar()));case 'S': return (Attribute)new Attribute.Constant((Type)this.syms.shortType, readPool(nextChar()));case 'Z': return (Attribute)new Attribute.Constant((Type)this.syms.booleanType, readPool(nextChar()));case 's': return (Attribute)new Attribute.Constant(this.syms.stringType, readPool(nextChar()).toString());case 'e': return new EnumAttributeProxy(readEnumType(nextChar()), readName(nextChar()));case 'c': return (Attribute)new Attribute.Class(this.types, readTypeOrClassSymbol(nextChar()));case '[': c2 = nextChar(); listBuffer = new ListBuffer(); for (b = 0; b < c2; b++) listBuffer.append(readAttributeValue());  return new ArrayAttributeProxy(listBuffer.toList());case '@': return readCompoundAnnotation(); }  throw new AssertionError("unknown annotation tag '" + c1 + "'"); } static class EnumAttributeProxy extends Attribute {
/*      */     Type enumType; Name enumerator; public EnumAttributeProxy(Type param1Type, Name param1Name) { super(null); this.enumType = param1Type; this.enumerator = param1Name; } public void accept(Visitor param1Visitor) { ((ProxyVisitor)param1Visitor).visitEnumAttributeProxy(this); } public String toString() { return "/*proxy enum*/" + this.enumType + "." + this.enumerator; } } static class ArrayAttributeProxy extends Attribute {
/*      */     List<Attribute> values; ArrayAttributeProxy(List<Attribute> param1List) { super(null); this.values = param1List; } public void accept(Visitor param1Visitor) { ((ProxyVisitor)param1Visitor).visitArrayAttributeProxy(this); } public String toString() { return "{" + this.values + "}"; } } static class CompoundAnnotationProxy extends Attribute {
/*      */     final List<Pair<Name, Attribute>> values; public CompoundAnnotationProxy(Type param1Type, List<Pair<Name, Attribute>> param1List) { super(param1Type); this.values = param1List; } public void accept(Visitor param1Visitor) { ((ProxyVisitor)param1Visitor).visitCompoundAnnotationProxy(this); } public String toString() { StringBuilder stringBuilder = new StringBuilder(); stringBuilder.append("@"); stringBuilder.append((CharSequence)this.type.tsym.getQualifiedName()); stringBuilder.append("/*proxy*/{"); boolean bool = true; List<Pair<Name, Attribute>> list = this.values; for (; list.nonEmpty(); list = list.tail) { Pair pair = (Pair)list.head; if (!bool) stringBuilder.append(",");  bool = false; stringBuilder.append((CharSequence)pair.fst); stringBuilder.append("="); stringBuilder.append(pair.snd); }  stringBuilder.append("}"); return stringBuilder.toString(); } } static class TypeAnnotationProxy {
/*      */     final CompoundAnnotationProxy compound; final TypeAnnotationPosition position; public TypeAnnotationProxy(CompoundAnnotationProxy param1CompoundAnnotationProxy, TypeAnnotationPosition param1TypeAnnotationPosition) { this.compound = param1CompoundAnnotationProxy; this.position = param1TypeAnnotationPosition; } } class AnnotationDeproxy implements ProxyVisitor {
/* 2744 */     private Symbol.ClassSymbol requestingOwner = (ClassReader.this.currentOwner.kind == 16) ? ClassReader.this.currentOwner.enclClass() : (Symbol.ClassSymbol)ClassReader.this.currentOwner; Attribute result; Type type; List<Attribute.Compound> deproxyCompoundList(List<CompoundAnnotationProxy> param1List) { ListBuffer listBuffer = new ListBuffer(); for (List<CompoundAnnotationProxy> list = param1List; list.nonEmpty(); list = list.tail) listBuffer.append(deproxyCompound((CompoundAnnotationProxy)list.head));  return listBuffer.toList(); } Attribute.Compound deproxyCompound(CompoundAnnotationProxy param1CompoundAnnotationProxy) { ListBuffer listBuffer = new ListBuffer(); List<Pair<Name, Attribute>> list = param1CompoundAnnotationProxy.values; for (; list.nonEmpty(); list = list.tail) { Symbol.MethodSymbol methodSymbol = findAccessMethod(param1CompoundAnnotationProxy.type, (Name)((Pair)list.head).fst); listBuffer.append(new Pair(methodSymbol, deproxy(methodSymbol.type.getReturnType(), (Attribute)((Pair)list.head).snd))); }  return new Attribute.Compound(param1CompoundAnnotationProxy.type, listBuffer.toList()); } Symbol.MethodSymbol findAccessMethod(Type param1Type, Name param1Name) { Symbol.CompletionFailure completionFailure = null; try { Scope.Entry entry = param1Type.tsym.members().lookup(param1Name); for (; entry.scope != null; entry = entry.next()) { Symbol symbol = entry.sym; if (symbol.kind == 16 && symbol.type.getParameterTypes().length() == 0) return (Symbol.MethodSymbol)symbol;  }  } catch (Symbol.CompletionFailure completionFailure1) { completionFailure = completionFailure1; }  JavaFileObject javaFileObject = ClassReader.this.log.useSource(this.requestingOwner.classfile); try { if (ClassReader.this.lintClassfile) if (completionFailure == null) { ClassReader.this.log.warning("annotation.method.not.found", new Object[] { param1Type, param1Name }); } else { ClassReader.this.log.warning("annotation.method.not.found.reason", new Object[] { param1Type, param1Name, completionFailure.getDetailValue() }); }   } finally { ClassReader.this.log.useSource(javaFileObject); }  Type.MethodType methodType = new Type.MethodType(List.nil(), ClassReader.this.syms.botType, List.nil(), (Symbol.TypeSymbol)ClassReader.this.syms.methodClass); return new Symbol.MethodSymbol(1025L, param1Name, (Type)methodType, (Symbol)param1Type.tsym); } Attribute deproxy(Type param1Type, Attribute param1Attribute) { Type type = this.type; try { this.type = param1Type; param1Attribute.accept(this); return this.result; } finally { this.type = type; }  } public void visitConstant(Attribute.Constant param1Constant) { this.result = (Attribute)param1Constant; } public void visitClass(Attribute.Class param1Class) { this.result = (Attribute)param1Class; } public void visitEnum(Attribute.Enum param1Enum) { throw new AssertionError(); } public void visitCompound(Attribute.Compound param1Compound) { throw new AssertionError(); } public void visitArray(Attribute.Array param1Array) { throw new AssertionError(); } public void visitError(Attribute.Error param1Error) { throw new AssertionError(); } public void visitEnumAttributeProxy(EnumAttributeProxy param1EnumAttributeProxy) { Symbol.TypeSymbol typeSymbol = param1EnumAttributeProxy.enumType.tsym; Symbol.VarSymbol varSymbol = null; Symbol.CompletionFailure completionFailure = null; try { Scope.Entry entry = typeSymbol.members().lookup(param1EnumAttributeProxy.enumerator); for (; entry.scope != null; entry = entry.next()) { if (entry.sym.kind == 4) { varSymbol = (Symbol.VarSymbol)entry.sym; break; }  }  } catch (Symbol.CompletionFailure completionFailure1) { completionFailure = completionFailure1; }  if (varSymbol == null) { if (completionFailure != null) { ClassReader.this.log.warning("unknown.enum.constant.reason", new Object[] { this.this$0.currentClassFile, typeSymbol, param1EnumAttributeProxy.enumerator, completionFailure.getDiagnostic() }); } else { ClassReader.this.log.warning("unknown.enum.constant", new Object[] { this.this$0.currentClassFile, typeSymbol, param1EnumAttributeProxy.enumerator }); }  this.result = (Attribute)new Attribute.Enum(typeSymbol.type, new Symbol.VarSymbol(0L, param1EnumAttributeProxy.enumerator, ClassReader.this.syms.botType, (Symbol)typeSymbol)); } else { this.result = (Attribute)new Attribute.Enum(typeSymbol.type, varSymbol); }  } public void visitArrayAttributeProxy(ArrayAttributeProxy param1ArrayAttributeProxy) { int i = param1ArrayAttributeProxy.values.length(); Attribute[] arrayOfAttribute = new Attribute[i]; Type type = ClassReader.this.types.elemtype(this.type); byte b = 0; for (List<Attribute> list = param1ArrayAttributeProxy.values; list.nonEmpty(); list = list.tail) arrayOfAttribute[b++] = deproxy(type, (Attribute)list.head);  this.result = (Attribute)new Attribute.Array(this.type, arrayOfAttribute); } public void visitCompoundAnnotationProxy(CompoundAnnotationProxy param1CompoundAnnotationProxy) { this.result = (Attribute)deproxyCompound(param1CompoundAnnotationProxy); } } private void fillIn(Symbol.PackageSymbol paramPackageSymbol) throws IOException { if (paramPackageSymbol.members_field == null) paramPackageSymbol.members_field = new Scope((Symbol)paramPackageSymbol);
/* 2745 */     String str = paramPackageSymbol.fullname.toString();
/*      */
/* 2747 */     EnumSet<JavaFileObject.Kind> enumSet1 = getPackageFileKinds();
/*      */
/* 2749 */     fillIn(paramPackageSymbol, StandardLocation.PLATFORM_CLASS_PATH, this.fileManager
/* 2750 */         .list(StandardLocation.PLATFORM_CLASS_PATH, str,
/*      */
/* 2752 */           EnumSet.of(JavaFileObject.Kind.CLASS), false));
/*      */
/*      */
/* 2755 */     EnumSet<JavaFileObject.Kind> enumSet2 = EnumSet.copyOf(enumSet1);
/* 2756 */     enumSet2.remove(JavaFileObject.Kind.SOURCE);
/* 2757 */     boolean bool1 = !enumSet2.isEmpty() ? true : false;
/*      */
/* 2759 */     EnumSet<JavaFileObject.Kind> enumSet3 = EnumSet.copyOf(enumSet1);
/* 2760 */     enumSet3.remove(JavaFileObject.Kind.CLASS);
/* 2761 */     boolean bool2 = !enumSet3.isEmpty() ? true : false;
/*      */
/* 2763 */     boolean bool = this.fileManager.hasLocation(StandardLocation.SOURCE_PATH);
/*      */
/* 2765 */     if (this.verbose && this.verbosePath &&
/* 2766 */       this.fileManager instanceof StandardJavaFileManager) {
/* 2767 */       StandardJavaFileManager standardJavaFileManager = (StandardJavaFileManager)this.fileManager;
/* 2768 */       if (bool && bool2) {
/* 2769 */         List list = List.nil();
/* 2770 */         for (File file : standardJavaFileManager.getLocation(StandardLocation.SOURCE_PATH)) {
/* 2771 */           list = list.prepend(file);
/*      */         }
/* 2773 */         this.log.printVerbose("sourcepath", new Object[] { list.reverse().toString() });
/* 2774 */       } else if (bool2) {
/* 2775 */         List list = List.nil();
/* 2776 */         for (File file : standardJavaFileManager.getLocation(StandardLocation.CLASS_PATH)) {
/* 2777 */           list = list.prepend(file);
/*      */         }
/* 2779 */         this.log.printVerbose("sourcepath", new Object[] { list.reverse().toString() });
/*      */       }
/* 2781 */       if (bool1) {
/* 2782 */         List list = List.nil();
/* 2783 */         for (File file : standardJavaFileManager.getLocation(StandardLocation.PLATFORM_CLASS_PATH)) {
/* 2784 */           list = list.prepend(file);
/*      */         }
/* 2786 */         for (File file : standardJavaFileManager.getLocation(StandardLocation.CLASS_PATH)) {
/* 2787 */           list = list.prepend(file);
/*      */         }
/* 2789 */         this.log.printVerbose("classpath", new Object[] { list.reverse().toString() });
/*      */       }
/*      */     }
/*      */
/*      */
/* 2794 */     if (bool2 && !bool) {
/* 2795 */       fillIn(paramPackageSymbol, StandardLocation.CLASS_PATH, this.fileManager
/* 2796 */           .list(StandardLocation.CLASS_PATH, str, enumSet1, false));
/*      */
/*      */     }
/*      */     else {
/*      */
/* 2801 */       if (bool1) {
/* 2802 */         fillIn(paramPackageSymbol, StandardLocation.CLASS_PATH, this.fileManager
/* 2803 */             .list(StandardLocation.CLASS_PATH, str, enumSet2, false));
/*      */       }
/*      */
/*      */
/* 2807 */       if (bool2) {
/* 2808 */         fillIn(paramPackageSymbol, StandardLocation.SOURCE_PATH, this.fileManager
/* 2809 */             .list(StandardLocation.SOURCE_PATH, str, enumSet3, false));
/*      */       }
/*      */     }
/*      */
/*      */
/* 2814 */     this.verbosePath = false; } class AnnotationDefaultCompleter extends AnnotationDeproxy implements Annotate.Worker {
/*      */     final Symbol.MethodSymbol sym; final Attribute value; final JavaFileObject classFile = ClassReader.this.currentClassFile; public String toString() { return " ClassReader store default for " + this.sym.owner + "." + this.sym + " is " + this.value; } AnnotationDefaultCompleter(Symbol.MethodSymbol param1MethodSymbol, Attribute param1Attribute) { this.sym = param1MethodSymbol; this.value = param1Attribute; } public void run() { JavaFileObject javaFileObject = ClassReader.this.currentClassFile; try { this.sym.defaultValue = null; ClassReader.this.currentClassFile = this.classFile; this.sym.defaultValue = deproxy(this.sym.type.getReturnType(), this.value); } finally { ClassReader.this.currentClassFile = javaFileObject; }  } } class AnnotationCompleter extends AnnotationDeproxy implements Annotate.Worker {
/*      */     final Symbol sym; final List<CompoundAnnotationProxy> l; final JavaFileObject classFile; public String toString() { return " ClassReader annotate " + this.sym.owner + "." + this.sym + " with " + this.l; } AnnotationCompleter(Symbol param1Symbol, List<CompoundAnnotationProxy> param1List) { this.sym = param1Symbol; this.l = param1List; this.classFile = ClassReader.this.currentClassFile; } public void run() { JavaFileObject javaFileObject = ClassReader.this.currentClassFile; try { ClassReader.this.currentClassFile = this.classFile; List<Attribute.Compound> list = deproxyCompoundList(this.l); if (this.sym.annotationsPendingCompletion()) { this.sym.setDeclarationAttributes(list); } else { this.sym.appendAttributes(list); }  } finally { ClassReader.this.currentClassFile = javaFileObject; }  } } class TypeAnnotationCompleter extends AnnotationCompleter {
/*      */     List<TypeAnnotationProxy> proxies; TypeAnnotationCompleter(Symbol param1Symbol, List<TypeAnnotationProxy> param1List) { super(param1Symbol, List.nil()); this.proxies = param1List; } List<Attribute.TypeCompound> deproxyTypeCompoundList(List<TypeAnnotationProxy> param1List) { ListBuffer listBuffer = new ListBuffer(); for (TypeAnnotationProxy typeAnnotationProxy : param1List) { Attribute.Compound compound = deproxyCompound(typeAnnotationProxy.compound); Attribute.TypeCompound typeCompound = new Attribute.TypeCompound(compound, typeAnnotationProxy.position); listBuffer.add(typeCompound); }  return listBuffer.toList(); } public void run() { JavaFileObject javaFileObject = ClassReader.this.currentClassFile; try { ClassReader.this.currentClassFile = this.classFile; List<Attribute.TypeCompound> list = deproxyTypeCompoundList(this.proxies); this.sym.setTypeAttributes(list.prependList(this.sym.getRawTypeAttributes())); } finally { ClassReader.this.currentClassFile = javaFileObject; }  } } Symbol.VarSymbol readField() { long l = adjustFieldFlags(nextChar()); Name name = readName(nextChar()); Type type = readType(nextChar()); Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(l, name, type, this.currentOwner); readMemberAttrs((Symbol)varSymbol); return varSymbol; } Symbol.MethodSymbol readMethod() { Type.MethodType methodType; long l = adjustMethodFlags(nextChar()); Name name = readName(nextChar()); Type type = readType(nextChar()); if (this.currentOwner.isInterface() && (l & 0x400L) == 0L && !name.equals(this.names.clinit)) if (this.majorVersion > Target.JDK1_8.majorVersion || (this.majorVersion == Target.JDK1_8.majorVersion && this.minorVersion >= Target.JDK1_8.minorVersion)) { if ((l & 0x8L) == 0L) { this.currentOwner.flags_field |= 0x80000000000L; l |= 0x80000000400L; }  } else { throw badClassFile(((l & 0x8L) == 0L) ? "invalid.default.interface" : "invalid.static.interface", new Object[] { Integer.toString(this.majorVersion), Integer.toString(this.minorVersion) }); }   if (name == this.names.init && this.currentOwner.hasOuterInstance()) if (!this.currentOwner.name.isEmpty()) methodType = new Type.MethodType(adjustMethodParams(l, type.getParameterTypes()), type.getReturnType(), type.getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass);   Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(l, name, (Type)methodType, this.currentOwner); if (this.types.isSignaturePolymorphic(methodSymbol)) methodSymbol.flags_field |= 0x400000000000L;  if (this.saveParameterNames) initParameterNames(methodSymbol);  Symbol symbol = this.currentOwner; this.currentOwner = (Symbol)methodSymbol; try { readMemberAttrs((Symbol)methodSymbol); } finally { this.currentOwner = symbol; }  if (this.saveParameterNames) setParameterNames(methodSymbol, (Type)methodType);  return methodSymbol; } private List<Type> adjustMethodParams(long paramLong, List<Type> paramList) { boolean bool = ((paramLong & 0x400000000L) != 0L) ? true : false; if (bool) { Type type = (Type)paramList.last(); ListBuffer listBuffer = new ListBuffer(); for (Type type1 : paramList) listBuffer.append((type1 != type) ? type1 : ((Type.ArrayType)type1).makeVarargs());  paramList = listBuffer.toList(); }  return paramList.tail; } void initParameterNames(Symbol.MethodSymbol paramMethodSymbol) { int i = Code.width(paramMethodSymbol.type.getParameterTypes()) + 4; if (this.parameterNameIndices == null || this.parameterNameIndices.length < i) { this.parameterNameIndices = new int[i]; } else { Arrays.fill(this.parameterNameIndices, 0); }  this.haveParameterNameIndices = false; this.sawMethodParameters = false; } void setParameterNames(Symbol.MethodSymbol paramMethodSymbol, Type paramType) { if (!this.haveParameterNameIndices) return;  int i = 0; if (!this.sawMethodParameters) { i = ((paramMethodSymbol.flags() & 0x8L) == 0L) ? 1 : 0; if (paramMethodSymbol.name == this.names.init && this.currentOwner.hasOuterInstance()) if (!this.currentOwner.name.isEmpty()) i++;   if (paramMethodSymbol.type != paramType) { int k = Code.width(paramType.getParameterTypes()) - Code.width(paramMethodSymbol.type.getParameterTypes()); i += k; }  }  List list = List.nil(); int j = i; for (Type type : paramMethodSymbol.type.getParameterTypes()) { boolean bool = (j < this.parameterNameIndices.length) ? this.parameterNameIndices[j] : false; Name name = !bool ? this.names.empty : readName(bool); list = list.prepend(name); j += Code.width(type); }  paramMethodSymbol.savedParameterNames = list.reverse(); } void skipBytes(int paramInt) { this.bp += paramInt; } void skipMember() { this.bp += 6; char c = nextChar(); for (byte b = 0; b < c; b++) { this.bp += 2; int i = nextInt(); this.bp += i; }  } protected void enterTypevars(Type paramType) { if (paramType.getEnclosingType() != null && paramType.getEnclosingType().hasTag(TypeTag.CLASS)) enterTypevars(paramType.getEnclosingType());  for (List list = paramType.getTypeArguments(); list.nonEmpty(); list = list.tail) this.typevars.enter((Symbol)((Type)list.head).tsym);  } protected void enterTypevars(Symbol paramSymbol) { if (paramSymbol.owner.kind == 16) { enterTypevars(paramSymbol.owner); enterTypevars(paramSymbol.owner.owner); }  enterTypevars(paramSymbol.type); } void readClass(Symbol.ClassSymbol paramClassSymbol) { Type.ClassType classType = (Type.ClassType)paramClassSymbol.type; paramClassSymbol.members_field = new Scope((Symbol)paramClassSymbol); this.typevars = this.typevars.dup(this.currentOwner); if (classType.getEnclosingType().hasTag(TypeTag.CLASS)) enterTypevars(classType.getEnclosingType());  long l = adjustClassFlags(nextChar()); if (paramClassSymbol.owner.kind == 1) paramClassSymbol.flags_field = l;  Symbol.ClassSymbol classSymbol = readClassSymbol(nextChar()); if (paramClassSymbol != classSymbol) throw badClassFile("class.file.wrong.class", new Object[] { classSymbol.flatname });  int i = this.bp; nextChar(); char c1 = nextChar(); this.bp += c1 * 2; char c2 = nextChar(); char c3; for (c3 = Character.MIN_VALUE; c3 < c2; ) { skipMember(); c3++; }  c3 = nextChar(); char c4; for (c4 = Character.MIN_VALUE; c4 < c3; ) { skipMember(); c4++; }  readClassAttrs(paramClassSymbol); if (this.readAllOfClassFile) { for (c4 = '\001'; c4 < this.poolObj.length; ) { readPool(c4); c4++; }  paramClassSymbol.pool = new Pool(this.poolObj.length, this.poolObj, this.types); }  this.bp = i; c4 = nextChar(); if (classType.supertype_field == null) classType.supertype_field = (c4 == '\000') ? (Type)Type.noType : readClassSymbol(c4).erasure(this.types);  c4 = nextChar(); List list = List.nil(); byte b; for (b = 0; b < c4; b++) { Type type = readClassSymbol(nextChar()).erasure(this.types); list = list.prepend(type); }  if (classType.interfaces_field == null) classType.interfaces_field = list.reverse();  Assert.check((c2 == nextChar())); for (b = 0; b < c2; ) { enterMember(paramClassSymbol, (Symbol)readField()); b++; }  Assert.check((c3 == nextChar())); for (b = 0; b < c3; ) { enterMember(paramClassSymbol, (Symbol)readMethod()); b++; }  this.typevars = this.typevars.leave(); } void readInnerClasses(Symbol.ClassSymbol paramClassSymbol) { char c = nextChar(); for (byte b = 0; b < c; b++) { nextChar(); Symbol.ClassSymbol classSymbol = readClassSymbol(nextChar()); Name name = readName(nextChar()); if (name == null) name = this.names.empty;  long l = adjustClassFlags(nextChar()); if (classSymbol != null) { if (name == this.names.empty) name = this.names.one;  Symbol.ClassSymbol classSymbol1 = enterClass(name, (Symbol.TypeSymbol)classSymbol); if ((l & 0x8L) == 0L) { ((Type.ClassType)classSymbol1.type).setEnclosingType(classSymbol.type); if (classSymbol1.erasure_field != null) ((Type.ClassType)classSymbol1.erasure_field).setEnclosingType(this.types.erasure(classSymbol.type));  }  if (paramClassSymbol == classSymbol) { classSymbol1.flags_field = l; enterMember(paramClassSymbol, (Symbol)classSymbol1); }  }  }  } private void readClassFile(Symbol.ClassSymbol paramClassSymbol) throws IOException { int i = nextInt(); if (i != -889275714) throw badClassFile("illegal.start.of.class.file", new Object[0]);  this.minorVersion = nextChar(); this.majorVersion = nextChar(); int j = (Target.MAX()).majorVersion; int k = (Target.MAX()).minorVersion; if (this.majorVersion > j || this.majorVersion * 1000 + this.minorVersion < (Target.MIN()).majorVersion * 1000 + (Target.MIN()).minorVersion) { if (this.majorVersion == j + 1) { this.log.warning("big.major.version", new Object[] { this.currentClassFile, Integer.valueOf(this.majorVersion), Integer.valueOf(j) }); } else { throw badClassFile("wrong.version", new Object[] { Integer.toString(this.majorVersion), Integer.toString(this.minorVersion), Integer.toString(j), Integer.toString(k) }); }  } else if (this.checkClassFile && this.majorVersion == j && this.minorVersion > k) { printCCF("found.later.version", Integer.toString(this.minorVersion)); }  indexPool(); if (this.signatureBuffer.length < this.bp) { int m = Integer.highestOneBit(this.bp) << 1; this.signatureBuffer = new byte[m]; }  readClass(paramClassSymbol); }
/*      */   long adjustFieldFlags(long paramLong) { return paramLong; }
/*      */   long adjustMethodFlags(long paramLong) { if ((paramLong & 0x40L) != 0L) { paramLong &= 0xFFFFFFFFFFFFFFBFL; paramLong |= 0x80000000L; if (!this.allowGenerics) paramLong &= 0xFFFFFFFFFFFFEFFFL;  }  if ((paramLong & 0x80L) != 0L) { paramLong &= 0xFFFFFFFFFFFFFF7FL; paramLong |= 0x400000000L; }  return paramLong; }
/*      */   long adjustClassFlags(long paramLong) { return paramLong & 0xFFFFFFFFFFFFFFDFL; }
/* 2821 */   private void fillIn(Symbol.PackageSymbol paramPackageSymbol, JavaFileManager.Location paramLocation, Iterable<JavaFileObject> paramIterable) { this.currentLoc = paramLocation;
/* 2822 */     for (JavaFileObject javaFileObject : paramIterable)
/* 2823 */     { String str1, str2; switch (javaFileObject.getKind()) {
/*      */
/*      */         case CLASS:
/*      */         case SOURCE:
/* 2827 */           str1 = this.fileManager.inferBinaryName(this.currentLoc, javaFileObject);
/* 2828 */           str2 = str1.substring(str1.lastIndexOf(".") + 1);
/* 2829 */           if (SourceVersion.isIdentifier(str2) || str2
/* 2830 */             .equals("package-info")) {
/* 2831 */             includeClassFile(paramPackageSymbol, javaFileObject);
/*      */           }
/*      */           continue;
/*      */       }
/* 2835 */       extraFileActions(paramPackageSymbol, javaFileObject); }  } public Symbol.ClassSymbol defineClass(Name paramName, Symbol paramSymbol) { Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(0L, paramName, paramSymbol); if (paramSymbol.kind == 1) Assert.checkNull(this.classes.get(classSymbol.flatname), classSymbol);  classSymbol.completer = this.thisCompleter; return classSymbol; } public Symbol.ClassSymbol enterClass(Name paramName, Symbol.TypeSymbol paramTypeSymbol) { Name name = Symbol.TypeSymbol.formFlatName(paramName, (Symbol)paramTypeSymbol); Symbol.ClassSymbol classSymbol = this.classes.get(name); if (classSymbol == null) { classSymbol = defineClass(paramName, (Symbol)paramTypeSymbol); this.classes.put(name, classSymbol); } else if ((classSymbol.name != paramName || classSymbol.owner != paramTypeSymbol) && paramTypeSymbol.kind == 2 && classSymbol.owner.kind == 1) { classSymbol.owner.members().remove((Symbol)classSymbol); classSymbol.name = paramName; classSymbol.owner = (Symbol)paramTypeSymbol; classSymbol.fullname = Symbol.ClassSymbol.formFullName(paramName, (Symbol)paramTypeSymbol); }  return classSymbol; } public Symbol.ClassSymbol enterClass(Name paramName, JavaFileObject paramJavaFileObject) { Symbol.ClassSymbol classSymbol = this.classes.get(paramName); if (classSymbol != null) { String str = Log.format("%s: completer = %s; class file = %s; source file = %s", new Object[] { classSymbol.fullname, classSymbol.completer, classSymbol.classfile, classSymbol.sourcefile }); throw new AssertionError(str); }  Name name = Convert.packagePart(paramName); Symbol.PackageSymbol packageSymbol = name.isEmpty() ? this.syms.unnamedPackage : enterPackage(name); classSymbol = defineClass(Convert.shortName(paramName), (Symbol)packageSymbol); classSymbol.classfile = paramJavaFileObject; this.classes.put(paramName, classSymbol); return classSymbol; } public Symbol.ClassSymbol enterClass(Name paramName) { Symbol.ClassSymbol classSymbol = this.classes.get(paramName); if (classSymbol == null) return enterClass(paramName, (JavaFileObject)null);  return classSymbol; } private void complete(Symbol paramSymbol) throws Symbol.CompletionFailure { if (paramSymbol.kind == 2) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramSymbol; classSymbol.members_field = (Scope)new Scope.ErrorScope((Symbol)classSymbol); this.annotate.enterStart(); try { completeOwners(classSymbol.owner); completeEnclosing(classSymbol); } finally { this.annotate.enterDoneWithoutFlush(); }  fillIn(classSymbol); } else if (paramSymbol.kind == 1) { Symbol.PackageSymbol packageSymbol = (Symbol.PackageSymbol)paramSymbol; try { fillIn(packageSymbol); } catch (IOException iOException) { throw (new Symbol.CompletionFailure(paramSymbol, iOException.getLocalizedMessage())).initCause(iOException); }  }  if (!this.filling) this.annotate.flush();  } private void completeOwners(Symbol paramSymbol) { if (paramSymbol.kind != 1) completeOwners(paramSymbol.owner);  paramSymbol.complete(); } private void completeEnclosing(Symbol.ClassSymbol paramClassSymbol) { if (paramClassSymbol.owner.kind == 1) { Symbol symbol = paramClassSymbol.owner; for (Name name : Convert.enclosingCandidates(Convert.shortName(paramClassSymbol.name))) { Symbol symbol1 = (symbol.members().lookup(name)).sym; if (symbol1 == null) symbol1 = (Symbol)this.classes.get(Symbol.TypeSymbol.formFlatName(name, symbol));  if (symbol1 != null) symbol1.complete();  }  }  } private void fillIn(Symbol.ClassSymbol paramClassSymbol) { if (this.completionFailureName == paramClassSymbol.fullname) throw new Symbol.CompletionFailure(paramClassSymbol, "user-selected completion failure by class name");  this.currentOwner = (Symbol)paramClassSymbol; this.warnedAttrs.clear(); JavaFileObject javaFileObject = paramClassSymbol.classfile; if (javaFileObject != null) { JavaFileObject javaFileObject1 = this.currentClassFile; try { if (this.filling) Assert.error("Filling " + javaFileObject.toUri() + " during " + javaFileObject1);  this.currentClassFile = javaFileObject; if (this.verbose) this.log.printVerbose("loading", new Object[] { this.currentClassFile.toString() });  if (javaFileObject.getKind() == JavaFileObject.Kind.CLASS) { this.filling = true; try { this.bp = 0; this.buf = readInputStream(this.buf, javaFileObject.openInputStream()); readClassFile(paramClassSymbol); if (!this.missingTypeVariables.isEmpty() && !this.foundTypeVariables.isEmpty()) { List<Type> list1 = this.missingTypeVariables; List<Type> list2 = this.foundTypeVariables; this.missingTypeVariables = List.nil(); this.foundTypeVariables = List.nil(); this.filling = false; Type.ClassType classType = (Type.ClassType)this.currentOwner.type; classType.supertype_field = this.types.subst(classType.supertype_field, list1, list2); classType.interfaces_field = this.types.subst(classType.interfaces_field, list1, list2); } else if (this.missingTypeVariables.isEmpty() != this.foundTypeVariables.isEmpty()) { Name name = ((Type)this.missingTypeVariables.head).tsym.name; throw badClassFile("undecl.type.var", new Object[] { name }); }  } finally { this.missingTypeVariables = List.nil(); this.foundTypeVariables = List.nil(); this.filling = false; }  } else if (this.sourceCompleter != null) { this.sourceCompleter.complete(paramClassSymbol); } else { throw new IllegalStateException("Source completer required to read " + javaFileObject.toUri()); }  return; } catch (IOException iOException) { throw badClassFile("unable.to.access.file", new Object[] { iOException.getMessage() }); } finally { this.currentClassFile = javaFileObject1; }  }  JCDiagnostic jCDiagnostic = this.diagFactory.fragment("class.file.not.found", new Object[] { paramClassSymbol.flatname }); throw newCompletionFailure(paramClassSymbol, jCDiagnostic); } private static byte[] readInputStream(byte[] paramArrayOfbyte, InputStream paramInputStream) throws IOException { try { paramArrayOfbyte = ensureCapacity(paramArrayOfbyte, paramInputStream.available()); int i = paramInputStream.read(paramArrayOfbyte); int j = 0; while (i != -1) { j += i; paramArrayOfbyte = ensureCapacity(paramArrayOfbyte, j); i = paramInputStream.read(paramArrayOfbyte, j, paramArrayOfbyte.length - j); }  return paramArrayOfbyte; } finally { try { paramInputStream.close(); } catch (IOException iOException) {} }  } private static byte[] ensureCapacity(byte[] paramArrayOfbyte, int paramInt) { if (paramArrayOfbyte.length <= paramInt) { byte[] arrayOfByte = paramArrayOfbyte; paramArrayOfbyte = new byte[Integer.highestOneBit(paramInt) << 1]; System.arraycopy(arrayOfByte, 0, paramArrayOfbyte, 0, arrayOfByte.length); }  return paramArrayOfbyte; }
/*      */   private Symbol.CompletionFailure newCompletionFailure(Symbol.TypeSymbol paramTypeSymbol, JCDiagnostic paramJCDiagnostic) { if (!this.cacheCompletionFailure) return new Symbol.CompletionFailure((Symbol)paramTypeSymbol, paramJCDiagnostic);  Symbol.CompletionFailure completionFailure = this.cachedCompletionFailure; completionFailure.sym = (Symbol)paramTypeSymbol; completionFailure.diag = paramJCDiagnostic; return completionFailure; }
/*      */   public Symbol.ClassSymbol loadClass(Name paramName) throws Symbol.CompletionFailure { boolean bool = (this.classes.get(paramName) == null) ? true : false; Symbol.ClassSymbol classSymbol = enterClass(paramName); if (classSymbol.members_field == null && classSymbol.completer != null) try { classSymbol.complete(); } catch (Symbol.CompletionFailure completionFailure) { if (bool) this.classes.remove(paramName);  throw completionFailure; }   return classSymbol; }
/*      */   public boolean packageExists(Name paramName) { return enterPackage(paramName).exists(); }
/*      */   public Symbol.PackageSymbol enterPackage(Name paramName) { Symbol.PackageSymbol packageSymbol = this.packages.get(paramName); if (packageSymbol == null) { Assert.check(!paramName.isEmpty(), "rootPackage missing!"); packageSymbol = new Symbol.PackageSymbol(Convert.shortName(paramName), (Symbol)enterPackage(Convert.packagePart(paramName))); packageSymbol.completer = this.thisCompleter; this.packages.put(paramName, packageSymbol); }  return packageSymbol; }
/*      */   public Symbol.PackageSymbol enterPackage(Name paramName, Symbol.PackageSymbol paramPackageSymbol) { return enterPackage(Symbol.TypeSymbol.formFullName(paramName, (Symbol)paramPackageSymbol)); }
/*      */   protected void includeClassFile(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject) { int i; if ((paramPackageSymbol.flags_field & 0x800000L) == 0L) for (Symbol.PackageSymbol packageSymbol = paramPackageSymbol; packageSymbol != null && ((Symbol)packageSymbol).kind == 1; symbol = ((Symbol)packageSymbol).owner) { Symbol symbol; ((Symbol)packageSymbol).flags_field |= 0x800000L; }   JavaFileObject.Kind kind = paramJavaFileObject.getKind(); if (kind == JavaFileObject.Kind.CLASS) { i = 33554432; } else { i = 67108864; }  String str = this.fileManager.inferBinaryName(this.currentLoc, paramJavaFileObject); int j = str.lastIndexOf("."); Name name = this.names.fromString(str.substring(j + 1)); boolean bool = (name == this.names.package_info) ? true : false; Symbol.ClassSymbol classSymbol = bool ? paramPackageSymbol.package_info : (Symbol.ClassSymbol)(paramPackageSymbol.members_field.lookup(name)).sym; if (classSymbol == null) { classSymbol = enterClass(name, (Symbol.TypeSymbol)paramPackageSymbol); if (classSymbol.classfile == null) classSymbol.classfile = paramJavaFileObject;  if (bool) { paramPackageSymbol.package_info = classSymbol; } else if (classSymbol.owner == paramPackageSymbol) { paramPackageSymbol.members_field.enter((Symbol)classSymbol); }  } else if (classSymbol.classfile != null && (classSymbol.flags_field & i) == 0L) { if ((classSymbol.flags_field & 0x6000000L) != 0L) classSymbol.classfile = preferredFileObject(paramJavaFileObject, classSymbol.classfile);  }  classSymbol.flags_field |= i; }
/*      */   protected JavaFileObject preferredFileObject(JavaFileObject paramJavaFileObject1, JavaFileObject paramJavaFileObject2) { if (this.preferSource) return (paramJavaFileObject1.getKind() == JavaFileObject.Kind.SOURCE) ? paramJavaFileObject1 : paramJavaFileObject2;  long l1 = paramJavaFileObject1.getLastModified(); long l2 = paramJavaFileObject2.getLastModified(); return (l1 > l2) ? paramJavaFileObject1 : paramJavaFileObject2; }
/*      */   protected EnumSet<JavaFileObject.Kind> getPackageFileKinds() { return EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.SOURCE); }
/*      */   protected void extraFileActions(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject) {}
/* 2845 */   private void printCCF(String paramString, Object paramObject) { this.log.printLines(paramString, new Object[] { paramObject }); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static class SourceFileObject
/*      */     extends BaseFileObject
/*      */   {
/*      */     private Name name;
/*      */
/*      */
/*      */
/*      */
/*      */     private Name flatname;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public SourceFileObject(Name param1Name1, Name param1Name2) {
/* 2868 */       super(null);
/* 2869 */       this.name = param1Name1;
/* 2870 */       this.flatname = param1Name2;
/*      */     }
/*      */
/*      */
/*      */     public URI toUri() {
/*      */       try {
/* 2876 */         return new URI(null, this.name.toString(), null);
/* 2877 */       } catch (URISyntaxException uRISyntaxException) {
/* 2878 */         throw new CannotCreateUriError(this.name.toString(), uRISyntaxException);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public String getName() {
/* 2884 */       return this.name.toString();
/*      */     }
/*      */
/*      */
/*      */     public String getShortName() {
/* 2889 */       return getName();
/*      */     }
/*      */
/*      */
/*      */     public Kind getKind() {
/* 2894 */       return getKind(getName());
/*      */     }
/*      */
/*      */
/*      */     public InputStream openInputStream() {
/* 2899 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public OutputStream openOutputStream() {
/* 2904 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public CharBuffer getCharContent(boolean param1Boolean) {
/* 2909 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public Reader openReader(boolean param1Boolean) {
/* 2914 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public Writer openWriter() {
/* 2919 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public long getLastModified() {
/* 2924 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     public boolean delete() {
/* 2929 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */
/*      */     protected String inferBinaryName(Iterable<? extends File> param1Iterable) {
/* 2934 */       return this.flatname.toString();
/*      */     }
/*      */
/*      */
/*      */     public boolean isNameCompatible(String param1String, Kind param1Kind) {
/* 2939 */       return true;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public boolean equals(Object param1Object) {
/* 2950 */       if (this == param1Object) {
/* 2951 */         return true;
/*      */       }
/* 2953 */       if (!(param1Object instanceof SourceFileObject)) {
/* 2954 */         return false;
/*      */       }
/* 2956 */       SourceFileObject sourceFileObject = (SourceFileObject)param1Object;
/* 2957 */       return this.name.equals(sourceFileObject.name);
/*      */     }
/*      */
/*      */
/*      */     public int hashCode() {
/* 2962 */       return this.name.hashCode();
/*      */     }
/*      */   }
/*      */
/*      */   static interface ProxyVisitor extends Attribute.Visitor {
/*      */     void visitEnumAttributeProxy(EnumAttributeProxy param1EnumAttributeProxy);
/*      */
/*      */     void visitArrayAttributeProxy(ArrayAttributeProxy param1ArrayAttributeProxy);
/*      */
/*      */     void visitCompoundAnnotationProxy(CompoundAnnotationProxy param1CompoundAnnotationProxy);
/*      */   }
/*      */
/*      */   public static interface SourceCompleter {
/*      */     void complete(Symbol.ClassSymbol param1ClassSymbol) throws Symbol.CompletionFailure;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\ClassReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
