/*      */ package com.sun.tools.javac.code;
/*      */
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.jvm.Code;
/*      */ import com.sun.tools.javac.jvm.Pool;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Constants;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import java.lang.annotation.Inherited;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.lang.model.element.AnnotationValue;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ import javax.lang.model.element.ElementVisitor;
/*      */ import javax.lang.model.element.ExecutableElement;
/*      */ import javax.lang.model.element.Modifier;
/*      */ import javax.lang.model.element.Name;
/*      */ import javax.lang.model.element.NestingKind;
/*      */ import javax.lang.model.element.PackageElement;
/*      */ import javax.lang.model.element.TypeElement;
/*      */ import javax.lang.model.element.TypeParameterElement;
/*      */ import javax.lang.model.element.VariableElement;
/*      */ import javax.lang.model.type.TypeMirror;
/*      */ import javax.tools.JavaFileObject;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public abstract class Symbol
/*      */   extends AnnoConstruct
/*      */   implements Element
/*      */ {
/*      */   public int kind;
/*      */   public long flags_field;
/*      */   public Name name;
/*      */   public Type type;
/*      */   public Symbol owner;
/*      */   public Completer completer;
/*      */   public Type erasure_field;
/*      */   protected SymbolMetadata metadata;
/*      */
/*      */   public long flags() {
/*   76 */     return this.flags_field;
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
/*      */   public List<Attribute.Compound> getRawAttributes() {
/*  111 */     return (this.metadata == null) ?
/*  112 */       List.nil() : this.metadata
/*  113 */       .getDeclarationAttributes();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public List<Attribute.TypeCompound> getRawTypeAttributes() {
/*  121 */     return (this.metadata == null) ?
/*  122 */       List.nil() : this.metadata
/*  123 */       .getTypeAttributes();
/*      */   }
/*      */
/*      */
/*      */   public Attribute.Compound attribute(Symbol paramSymbol) {
/*  128 */     for (Attribute.Compound compound : getRawAttributes()) {
/*  129 */       if (compound.type.tsym == paramSymbol) return compound;
/*      */     }
/*  131 */     return null;
/*      */   }
/*      */
/*      */   public boolean annotationsPendingCompletion() {
/*  135 */     return (this.metadata == null) ? false : this.metadata.pendingCompletion();
/*      */   }
/*      */
/*      */   public void appendAttributes(List<Attribute.Compound> paramList) {
/*  139 */     if (paramList.nonEmpty()) {
/*  140 */       initedMetadata().append(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public void appendClassInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/*  145 */     if (paramList.nonEmpty()) {
/*  146 */       initedMetadata().appendClassInitTypeAttributes(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public void appendInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/*  151 */     if (paramList.nonEmpty()) {
/*  152 */       initedMetadata().appendInitTypeAttributes(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public void appendTypeAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.TypeCompound> paramAnnotateRepeatedContext) {
/*  157 */     initedMetadata().appendTypeAttributesWithCompletion(paramAnnotateRepeatedContext);
/*      */   }
/*      */
/*      */   public void appendUniqueTypeAttributes(List<Attribute.TypeCompound> paramList) {
/*  161 */     if (paramList.nonEmpty()) {
/*  162 */       initedMetadata().appendUniqueTypes(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public List<Attribute.TypeCompound> getClassInitTypeAttributes() {
/*  167 */     return (this.metadata == null) ?
/*  168 */       List.nil() : this.metadata
/*  169 */       .getClassInitTypeAttributes();
/*      */   }
/*      */
/*      */   public List<Attribute.TypeCompound> getInitTypeAttributes() {
/*  173 */     return (this.metadata == null) ?
/*  174 */       List.nil() : this.metadata
/*  175 */       .getInitTypeAttributes();
/*      */   }
/*      */
/*      */   public List<Attribute.Compound> getDeclarationAttributes() {
/*  179 */     return (this.metadata == null) ?
/*  180 */       List.nil() : this.metadata
/*  181 */       .getDeclarationAttributes();
/*      */   }
/*      */
/*      */   public boolean hasAnnotations() {
/*  185 */     return (this.metadata != null && !this.metadata.isEmpty());
/*      */   }
/*      */
/*      */   public boolean hasTypeAnnotations() {
/*  189 */     return (this.metadata != null && !this.metadata.isTypesEmpty());
/*      */   }
/*      */
/*      */   public void prependAttributes(List<Attribute.Compound> paramList) {
/*  193 */     if (paramList.nonEmpty()) {
/*  194 */       initedMetadata().prepend(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public void resetAnnotations() {
/*  199 */     initedMetadata().reset();
/*      */   }
/*      */
/*      */   public void setAttributes(Symbol paramSymbol) {
/*  203 */     if (this.metadata != null || paramSymbol.metadata != null) {
/*  204 */       initedMetadata().setAttributes(paramSymbol.metadata);
/*      */     }
/*      */   }
/*      */
/*      */   public void setDeclarationAttributes(List<Attribute.Compound> paramList) {
/*  209 */     if (this.metadata != null || paramList.nonEmpty()) {
/*  210 */       initedMetadata().setDeclarationAttributes(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   public void setDeclarationAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.Compound> paramAnnotateRepeatedContext) {
/*  215 */     initedMetadata().setDeclarationAttributesWithCompletion(paramAnnotateRepeatedContext);
/*      */   }
/*      */
/*      */   public void setTypeAttributes(List<Attribute.TypeCompound> paramList) {
/*  219 */     if (this.metadata != null || paramList.nonEmpty()) {
/*  220 */       if (this.metadata == null)
/*  221 */         this.metadata = new SymbolMetadata(this);
/*  222 */       this.metadata.setTypeAttributes(paramList);
/*      */     }
/*      */   }
/*      */
/*      */   private SymbolMetadata initedMetadata() {
/*  227 */     if (this.metadata == null)
/*  228 */       this.metadata = new SymbolMetadata(this);
/*  229 */     return this.metadata;
/*      */   }
/*      */
/*      */
/*      */   public SymbolMetadata getMetadata() {
/*  234 */     return this.metadata;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Symbol(int paramInt, long paramLong, Name paramName, Type paramType, Symbol paramSymbol) {
/*  242 */     this.kind = paramInt;
/*  243 */     this.flags_field = paramLong;
/*  244 */     this.type = paramType;
/*  245 */     this.owner = paramSymbol;
/*  246 */     this.completer = null;
/*  247 */     this.erasure_field = null;
/*  248 */     this.name = paramName;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Symbol clone(Symbol paramSymbol) {
/*  255 */     throw new AssertionError();
/*      */   }
/*      */
/*      */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/*  259 */     return paramVisitor.visitSymbol(this, paramP);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public String toString() {
/*  266 */     return this.name.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Symbol location() {
/*  276 */     if (this.owner.name == null || (this.owner.name.isEmpty() && (this.owner
/*  277 */       .flags() & 0x100000L) == 0L && this.owner.kind != 1 && this.owner.kind != 2)) {
/*  278 */       return null;
/*      */     }
/*  280 */     return this.owner;
/*      */   }
/*      */
/*      */   public Symbol location(Type paramType, Types paramTypes) {
/*  284 */     if (this.owner.name == null || this.owner.name.isEmpty()) {
/*  285 */       return location();
/*      */     }
/*  287 */     if (this.owner.type.hasTag(TypeTag.CLASS)) {
/*  288 */       Type type = paramTypes.asOuterSuper(paramType, this.owner);
/*  289 */       if (type != null) return type.tsym;
/*      */     }
/*  291 */     return this.owner;
/*      */   }
/*      */
/*      */   public Symbol baseSymbol() {
/*  295 */     return this;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public Type erasure(Types paramTypes) {
/*  301 */     if (this.erasure_field == null)
/*  302 */       this.erasure_field = paramTypes.erasure(this.type);
/*  303 */     return this.erasure_field;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type externalType(Types paramTypes) {
/*  311 */     Type type = erasure(paramTypes);
/*  312 */     if (this.name == this.name.table.names.init && this.owner.hasOuterInstance()) {
/*  313 */       Type type1 = paramTypes.erasure(this.owner.type.getEnclosingType());
/*  314 */       return new Type.MethodType(type.getParameterTypes().prepend(type1), type
/*  315 */           .getReturnType(), type
/*  316 */           .getThrownTypes(), type.tsym);
/*      */     }
/*      */
/*  319 */     return type;
/*      */   }
/*      */
/*      */
/*      */   public boolean isDeprecated() {
/*  324 */     return ((this.flags_field & 0x20000L) != 0L);
/*      */   }
/*      */
/*      */   public boolean isStatic() {
/*  328 */     return ((
/*  329 */       flags() & 0x8L) != 0L || ((this.owner
/*  330 */       .flags() & 0x200L) != 0L && this.kind != 16 && this.name != this.name.table.names._this));
/*      */   }
/*      */
/*      */
/*      */   public boolean isInterface() {
/*  335 */     return ((flags() & 0x200L) != 0L);
/*      */   }
/*      */
/*      */   public boolean isPrivate() {
/*  339 */     return ((this.flags_field & 0x7L) == 2L);
/*      */   }
/*      */
/*      */   public boolean isEnum() {
/*  343 */     return ((flags() & 0x4000L) != 0L);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isLocal() {
/*  352 */     return ((this.owner.kind & 0x14) != 0 || (this.owner.kind == 2 && this.owner
/*      */
/*  354 */       .isLocal()));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isAnonymous() {
/*  361 */     return this.name.isEmpty();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isConstructor() {
/*  367 */     return (this.name == this.name.table.names.init);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Name getQualifiedName() {
/*  375 */     return this.name;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Name flatName() {
/*  383 */     return getQualifiedName();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public Scope members() {
/*  389 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isInner() {
/*  395 */     return (this.kind == 2 && this.type.getEnclosingType().hasTag(TypeTag.CLASS));
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
/*      */   public boolean hasOuterInstance() {
/*  407 */     return (this.type
/*  408 */       .getEnclosingType().hasTag(TypeTag.CLASS) && (flags() & 0x400200L) == 0L);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public ClassSymbol enclClass() {
/*  414 */     Symbol symbol = this;
/*  415 */     while (symbol != null && ((symbol.kind & 0x2) == 0 ||
/*  416 */       !symbol.type.hasTag(TypeTag.CLASS))) {
/*  417 */       symbol = symbol.owner;
/*      */     }
/*  419 */     return (ClassSymbol)symbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public ClassSymbol outermostClass() {
/*  425 */     Symbol symbol1 = this;
/*  426 */     Symbol symbol2 = null;
/*  427 */     while (symbol1.kind != 1) {
/*  428 */       symbol2 = symbol1;
/*  429 */       symbol1 = symbol1.owner;
/*      */     }
/*  431 */     return (ClassSymbol)symbol2;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public PackageSymbol packge() {
/*  437 */     Symbol symbol = this;
/*  438 */     while (symbol.kind != 1) {
/*  439 */       symbol = symbol.owner;
/*      */     }
/*  441 */     return (PackageSymbol)symbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isSubClass(Symbol paramSymbol, Types paramTypes) {
/*  447 */     throw new AssertionError("isSubClass " + this);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isMemberOf(TypeSymbol paramTypeSymbol, Types paramTypes) {
/*  454 */     return (this.owner == paramTypeSymbol || (paramTypeSymbol
/*      */
/*  456 */       .isSubClass(this.owner, paramTypes) &&
/*  457 */       isInheritedIn(paramTypeSymbol, paramTypes) &&
/*  458 */       !hiddenIn((ClassSymbol)paramTypeSymbol, paramTypes)));
/*      */   }
/*      */
/*      */
/*      */   public boolean isEnclosedBy(ClassSymbol paramClassSymbol) {
/*  463 */     for (Symbol symbol = this; symbol.kind != 1; symbol = symbol.owner) {
/*  464 */       if (symbol == paramClassSymbol) return true;
/*  465 */     }  return false;
/*      */   }
/*      */
/*      */   private boolean hiddenIn(ClassSymbol paramClassSymbol, Types paramTypes) {
/*  469 */     Symbol symbol = hiddenInInternal(paramClassSymbol, paramTypes);
/*  470 */     Assert.check((symbol != null), "the result of hiddenInInternal() can't be null");
/*      */
/*      */
/*  473 */     return (symbol != this);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol hiddenInInternal(ClassSymbol paramClassSymbol, Types paramTypes) {
/*  484 */     if (paramClassSymbol == this.owner) {
/*  485 */       return this;
/*      */     }
/*  487 */     Scope.Entry entry = paramClassSymbol.members().lookup(this.name);
/*  488 */     while (entry.scope != null) {
/*  489 */       if (entry.sym.kind == this.kind && (this.kind != 16 || ((entry.sym
/*      */
/*  491 */         .flags() & 0x8L) != 0L && paramTypes
/*  492 */         .isSubSignature(entry.sym.type, this.type)))) {
/*  493 */         return entry.sym;
/*      */       }
/*  495 */       entry = entry.next();
/*      */     }
/*  497 */     Symbol symbol = null;
/*  498 */     for (Type type : paramTypes.interfaces(paramClassSymbol.type)
/*  499 */       .prepend(paramTypes.supertype(paramClassSymbol.type))) {
/*  500 */       if (type != null && type.hasTag(TypeTag.CLASS)) {
/*  501 */         Symbol symbol1 = hiddenInInternal((ClassSymbol)type.tsym, paramTypes);
/*  502 */         if (symbol1 == this)
/*  503 */           return this;
/*  504 */         if (symbol1 != null) {
/*  505 */           symbol = symbol1;
/*      */         }
/*      */       }
/*      */     }
/*  509 */     return symbol;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isInheritedIn(Symbol paramSymbol, Types paramTypes) {
/*  520 */     switch ((int)(this.flags_field & 0x7L))
/*      */
/*      */     { default:
/*  523 */         return true;
/*      */       case 2:
/*  525 */         return (this.owner == paramSymbol);
/*      */
/*      */       case 4:
/*  528 */         return ((paramSymbol.flags() & 0x200L) == 0L);
/*      */       case 0:
/*  530 */         break; }  PackageSymbol packageSymbol = packge();
/*  531 */     Symbol symbol = paramSymbol;
/*  532 */     for (; symbol != null && symbol != this.owner;
/*  533 */       symbol = (paramTypes.supertype(symbol.type)).tsym) {
/*  534 */       while (symbol.type.hasTag(TypeTag.TYPEVAR))
/*  535 */         symbol = (symbol.type.getUpperBound()).tsym;
/*  536 */       if (symbol.type.isErroneous())
/*  537 */         return true;
/*  538 */       if ((symbol.flags() & 0x1000000L) == 0L)
/*      */       {
/*  540 */         if (symbol.packge() != packageSymbol)
/*  541 */           return false;  }
/*      */     }
/*  543 */     return ((paramSymbol.flags() & 0x200L) == 0L);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Symbol asMemberOf(Type paramType, Types paramTypes) {
/*  552 */     throw new AssertionError();
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
/*      */   public boolean overrides(Symbol paramSymbol, TypeSymbol paramTypeSymbol, Types paramTypes, boolean paramBoolean) {
/*  565 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void complete() throws CompletionFailure {
/*  571 */     if (this.completer != null) {
/*  572 */       Completer completer = this.completer;
/*  573 */       this.completer = null;
/*  574 */       completer.complete(this);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean exists() {
/*  581 */     return true;
/*      */   }
/*      */
/*      */   public Type asType() {
/*  585 */     return this.type;
/*      */   }
/*      */
/*      */   public Symbol getEnclosingElement() {
/*  589 */     return this.owner;
/*      */   }
/*      */
/*      */   public ElementKind getKind() {
/*  593 */     return ElementKind.OTHER;
/*      */   }
/*      */
/*      */   public Set<Modifier> getModifiers() {
/*  597 */     return Flags.asModifierSet(flags());
/*      */   }
/*      */
/*      */   public Name getSimpleName() {
/*  601 */     return this.name;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public List<Attribute.Compound> getAnnotationMirrors() {
/*  610 */     return getRawAttributes();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public List<Symbol> getEnclosedElements() {
/*  616 */     return (List<Symbol>)List.nil();
/*      */   }
/*      */
/*      */   public List<TypeVariableSymbol> getTypeParameters() {
/*  620 */     ListBuffer listBuffer = new ListBuffer();
/*  621 */     for (Type type : this.type.getTypeArguments()) {
/*  622 */       Assert.check((type.tsym.getKind() == ElementKind.TYPE_PARAMETER));
/*  623 */       listBuffer.append(type.tsym);
/*      */     }
/*  625 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   public static class DelegatedSymbol<T extends Symbol> extends Symbol { protected T other;
/*      */
/*      */     public DelegatedSymbol(T param1T) {
/*  631 */       super(((Symbol)param1T).kind, ((Symbol)param1T).flags_field, ((Symbol)param1T).name, ((Symbol)param1T).type, ((Symbol)param1T).owner);
/*  632 */       this.other = param1T;
/*      */     }
/*  634 */     public String toString() { return this.other.toString(); }
/*  635 */     public Symbol location() { return this.other.location(); }
/*  636 */     public Symbol location(Type param1Type, Types param1Types) { return this.other.location(param1Type, param1Types); }
/*  637 */     public Symbol baseSymbol() { return (Symbol)this.other; }
/*  638 */     public Type erasure(Types param1Types) { return this.other.erasure(param1Types); }
/*  639 */     public Type externalType(Types param1Types) { return this.other.externalType(param1Types); }
/*  640 */     public boolean isLocal() { return this.other.isLocal(); }
/*  641 */     public boolean isConstructor() { return this.other.isConstructor(); }
/*  642 */     public Name getQualifiedName() { return this.other.getQualifiedName(); }
/*  643 */     public Name flatName() { return this.other.flatName(); }
/*  644 */     public Scope members() { return this.other.members(); }
/*  645 */     public boolean isInner() { return this.other.isInner(); }
/*  646 */     public boolean hasOuterInstance() { return this.other.hasOuterInstance(); }
/*  647 */     public ClassSymbol enclClass() { return this.other.enclClass(); }
/*  648 */     public ClassSymbol outermostClass() { return this.other.outermostClass(); }
/*  649 */     public PackageSymbol packge() { return this.other.packge(); }
/*  650 */     public boolean isSubClass(Symbol param1Symbol, Types param1Types) { return this.other.isSubClass(param1Symbol, param1Types); }
/*  651 */     public boolean isMemberOf(TypeSymbol param1TypeSymbol, Types param1Types) { return this.other.isMemberOf(param1TypeSymbol, param1Types); }
/*  652 */     public boolean isEnclosedBy(ClassSymbol param1ClassSymbol) { return this.other.isEnclosedBy(param1ClassSymbol); }
/*  653 */     public boolean isInheritedIn(Symbol param1Symbol, Types param1Types) { return this.other.isInheritedIn(param1Symbol, param1Types); }
/*  654 */     public Symbol asMemberOf(Type param1Type, Types param1Types) { return this.other.asMemberOf(param1Type, param1Types); } public void complete() throws CompletionFailure {
/*  655 */       this.other.complete();
/*      */     }
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/*  658 */       return this.other.accept(param1ElementVisitor, param1P);
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/*  662 */       return param1Visitor.visitSymbol((Symbol)this.other, param1P);
/*      */     }
/*      */
/*      */     public T getUnderlyingSymbol() {
/*  666 */       return this.other;
/*      */     } }
/*      */
/*      */
/*      */   public static abstract class TypeSymbol
/*      */     extends Symbol
/*      */   {
/*      */     public TypeSymbol(int param1Int, long param1Long, Name param1Name, Type param1Type, Symbol param1Symbol) {
/*  674 */       super(param1Int, param1Long, param1Name, param1Type, param1Symbol);
/*      */     }
/*      */
/*      */
/*      */     public static Name formFullName(Name param1Name, Symbol param1Symbol) {
/*  679 */       if (param1Symbol == null) return param1Name;
/*  680 */       if (param1Symbol.kind != 63 && ((param1Symbol.kind & 0x14) != 0 || (param1Symbol.kind == 2 && param1Symbol.type
/*      */
/*  682 */         .hasTag(TypeTag.TYPEVAR))))
/*  683 */         return param1Name;
/*  684 */       Name name = param1Symbol.getQualifiedName();
/*  685 */       if (name == null || name == name.table.names.empty)
/*  686 */         return param1Name;
/*  687 */       return name.append('.', param1Name);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public static Name formFlatName(Name param1Name, Symbol param1Symbol) {
/*  694 */       if (param1Symbol == null || (param1Symbol.kind & 0x14) != 0 || (param1Symbol.kind == 2 && param1Symbol.type
/*      */
/*  696 */         .hasTag(TypeTag.TYPEVAR)))
/*  697 */         return param1Name;
/*  698 */       byte b = (param1Symbol.kind == 2) ? 36 : 46;
/*  699 */       Name name = param1Symbol.flatName();
/*  700 */       if (name == null || name == name.table.names.empty)
/*  701 */         return param1Name;
/*  702 */       return name.append(b, param1Name);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public final boolean precedes(TypeSymbol param1TypeSymbol, Types param1Types) {
/*  712 */       if (this == param1TypeSymbol)
/*  713 */         return false;
/*  714 */       if (this.type.hasTag(param1TypeSymbol.type.getTag())) {
/*  715 */         if (this.type.hasTag(TypeTag.CLASS))
/*  716 */           return (param1Types
/*  717 */             .rank(param1TypeSymbol.type) < param1Types.rank(this.type) || (param1Types
/*  718 */             .rank(param1TypeSymbol.type) == param1Types.rank(this.type) && param1TypeSymbol
/*  719 */             .getQualifiedName().compareTo(getQualifiedName()) < 0));
/*  720 */         if (this.type.hasTag(TypeTag.TYPEVAR)) {
/*  721 */           return param1Types.isSubtype(this.type, param1TypeSymbol.type);
/*      */         }
/*      */       }
/*  724 */       return this.type.hasTag(TypeTag.TYPEVAR);
/*      */     }
/*      */
/*      */
/*      */     public List<Symbol> getEnclosedElements() {
/*  729 */       List list = List.nil();
/*  730 */       if (this.kind == 2 && this.type.hasTag(TypeTag.TYPEVAR)) {
/*  731 */         return (List<Symbol>)list;
/*      */       }
/*  733 */       for (Scope.Entry entry = (members()).elems; entry != null; entry = entry.sibling) {
/*  734 */         if (entry.sym != null && (entry.sym.flags() & 0x1000L) == 0L && entry.sym.owner == this)
/*  735 */           list = list.prepend(entry.sym);
/*      */       }
/*  737 */       return (List<Symbol>)list;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/*  742 */       return param1Visitor.visitTypeSymbol(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class TypeVariableSymbol
/*      */     extends TypeSymbol
/*      */     implements TypeParameterElement
/*      */   {
/*      */     public TypeVariableSymbol(long param1Long, Name param1Name, Type param1Type, Symbol param1Symbol) {
/*  753 */       super(2, param1Long, param1Name, param1Type, param1Symbol);
/*      */     }
/*      */
/*      */     public ElementKind getKind() {
/*  757 */       return ElementKind.TYPE_PARAMETER;
/*      */     }
/*      */
/*      */
/*      */     public Symbol getGenericElement() {
/*  762 */       return this.owner;
/*      */     }
/*      */
/*      */     public List<Type> getBounds() {
/*  766 */       Type.TypeVar typeVar = (Type.TypeVar)this.type;
/*  767 */       Type type = typeVar.getUpperBound();
/*  768 */       if (!type.isCompound())
/*  769 */         return List.of(type);
/*  770 */       Type.ClassType classType = (Type.ClassType)type;
/*  771 */       if (!classType.tsym.erasure_field.isInterface()) {
/*  772 */         return classType.interfaces_field.prepend(classType.supertype_field);
/*      */       }
/*      */
/*      */
/*  776 */       return classType.interfaces_field;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public List<Attribute.Compound> getAnnotationMirrors() {
/*  784 */       List<Attribute.TypeCompound> list = this.owner.getRawTypeAttributes();
/*  785 */       int i = this.owner.getTypeParameters().indexOf(this);
/*  786 */       List list1 = List.nil();
/*  787 */       for (Attribute.TypeCompound typeCompound : list) {
/*  788 */         if (isCurrentSymbolsAnnotation(typeCompound, i)) {
/*  789 */           list1 = list1.prepend(typeCompound);
/*      */         }
/*      */       }
/*  792 */       return list1.reverse();
/*      */     }
/*      */
/*      */
/*      */
/*      */     public <A extends java.lang.annotation.Annotation> Attribute.Compound getAttribute(Class<A> param1Class) {
/*  798 */       String str = param1Class.getName();
/*      */
/*      */
/*      */
/*  802 */       List<Attribute.TypeCompound> list = this.owner.getRawTypeAttributes();
/*  803 */       int i = this.owner.getTypeParameters().indexOf(this);
/*  804 */       for (Attribute.TypeCompound typeCompound : list) {
/*  805 */         if (isCurrentSymbolsAnnotation(typeCompound, i) && str
/*  806 */           .contentEquals((CharSequence)typeCompound.type.tsym.flatName()))
/*  807 */           return typeCompound;
/*      */       }
/*  809 */       return null;
/*      */     }
/*      */
/*      */     boolean isCurrentSymbolsAnnotation(Attribute.TypeCompound param1TypeCompound, int param1Int) {
/*  813 */       return ((param1TypeCompound.position.type == TargetType.CLASS_TYPE_PARAMETER || param1TypeCompound.position.type == TargetType.METHOD_TYPE_PARAMETER) && param1TypeCompound.position.parameter_index == param1Int);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/*  821 */       return param1ElementVisitor.visitTypeParameter(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class PackageSymbol
/*      */     extends TypeSymbol
/*      */     implements PackageElement
/*      */   {
/*      */     public Scope members_field;
/*      */     public Name fullname;
/*      */     public ClassSymbol package_info;
/*      */
/*      */     public PackageSymbol(Name param1Name, Type param1Type, Symbol param1Symbol) {
/*  835 */       super(1, 0L, param1Name, param1Type, param1Symbol);
/*  836 */       this.members_field = null;
/*  837 */       this.fullname = formFullName(param1Name, param1Symbol);
/*      */     }
/*      */
/*      */     public PackageSymbol(Name param1Name, Symbol param1Symbol) {
/*  841 */       this(param1Name, (Type)null, param1Symbol);
/*  842 */       this.type = new Type.PackageType(this);
/*      */     }
/*      */
/*      */     public String toString() {
/*  846 */       return this.fullname.toString();
/*      */     }
/*      */
/*      */     public Name getQualifiedName() {
/*  850 */       return this.fullname;
/*      */     }
/*      */
/*      */     public boolean isUnnamed() {
/*  854 */       return (this.name.isEmpty() && this.owner != null);
/*      */     }
/*      */
/*      */     public Scope members() {
/*  858 */       if (this.completer != null) complete();
/*  859 */       return this.members_field;
/*      */     }
/*      */
/*      */     public long flags() {
/*  863 */       if (this.completer != null) complete();
/*  864 */       return this.flags_field;
/*      */     }
/*      */
/*      */
/*      */     public List<Attribute.Compound> getRawAttributes() {
/*  869 */       if (this.completer != null) complete();
/*  870 */       if (this.package_info != null && this.package_info.completer != null) {
/*  871 */         this.package_info.complete();
/*  872 */         mergeAttributes();
/*      */       }
/*  874 */       return super.getRawAttributes();
/*      */     }
/*      */
/*      */     private void mergeAttributes() {
/*  878 */       if (this.metadata == null && this.package_info.metadata != null) {
/*      */
/*  880 */         this.metadata = new SymbolMetadata(this);
/*  881 */         this.metadata.setAttributes(this.package_info.metadata);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public boolean exists() {
/*  889 */       return ((this.flags_field & 0x800000L) != 0L);
/*      */     }
/*      */
/*      */     public ElementKind getKind() {
/*  893 */       return ElementKind.PACKAGE;
/*      */     }
/*      */
/*      */     public Symbol getEnclosingElement() {
/*  897 */       return null;
/*      */     }
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/*  901 */       return param1ElementVisitor.visitPackage(this, param1P);
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/*  905 */       return param1Visitor.visitPackageSymbol(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class ClassSymbol
/*      */     extends TypeSymbol
/*      */     implements TypeElement
/*      */   {
/*      */     public Scope members_field;
/*      */
/*      */
/*      */
/*      */
/*      */     public Name fullname;
/*      */
/*      */
/*      */
/*      */     public Name flatname;
/*      */
/*      */
/*      */
/*      */     public JavaFileObject sourcefile;
/*      */
/*      */
/*      */
/*      */     public JavaFileObject classfile;
/*      */
/*      */
/*      */
/*      */     public List<ClassSymbol> trans_local;
/*      */
/*      */
/*      */
/*      */     public Pool pool;
/*      */
/*      */
/*      */
/*      */
/*      */     public ClassSymbol(long param1Long, Name param1Name, Type param1Type, Symbol param1Symbol) {
/*  948 */       super(2, param1Long, param1Name, param1Type, param1Symbol);
/*  949 */       this.members_field = null;
/*  950 */       this.fullname = formFullName(param1Name, param1Symbol);
/*  951 */       this.flatname = formFlatName(param1Name, param1Symbol);
/*  952 */       this.sourcefile = null;
/*  953 */       this.classfile = null;
/*  954 */       this.pool = null;
/*      */     }
/*      */
/*      */     public ClassSymbol(long param1Long, Name param1Name, Symbol param1Symbol) {
/*  958 */       this(param1Long, param1Name, new Type.ClassType(Type.noType, null, null), param1Symbol);
/*      */
/*      */
/*      */
/*      */
/*  963 */       this.type.tsym = this;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public String toString() {
/*  969 */       return className();
/*      */     }
/*      */
/*      */     public long flags() {
/*  973 */       if (this.completer != null) complete();
/*  974 */       return this.flags_field;
/*      */     }
/*      */
/*      */     public Scope members() {
/*  978 */       if (this.completer != null) complete();
/*  979 */       return this.members_field;
/*      */     }
/*      */
/*      */
/*      */     public List<Attribute.Compound> getRawAttributes() {
/*  984 */       if (this.completer != null) complete();
/*  985 */       return super.getRawAttributes();
/*      */     }
/*      */
/*      */
/*      */     public List<Attribute.TypeCompound> getRawTypeAttributes() {
/*  990 */       if (this.completer != null) complete();
/*  991 */       return super.getRawTypeAttributes();
/*      */     }
/*      */
/*      */     public Type erasure(Types param1Types) {
/*  995 */       if (this.erasure_field == null)
/*  996 */         this
/*  997 */           .erasure_field = new Type.ClassType(param1Types.erasure(this.type.getEnclosingType()), List.nil(), this);
/*  998 */       return this.erasure_field;
/*      */     }
/*      */
/*      */     public String className() {
/* 1002 */       if (this.name.isEmpty()) {
/* 1003 */         return
/* 1004 */           Log.getLocalizedString("anonymous.class", new Object[] { this.flatname });
/*      */       }
/* 1006 */       return this.fullname.toString();
/*      */     }
/*      */
/*      */     public Name getQualifiedName() {
/* 1010 */       return this.fullname;
/*      */     }
/*      */
/*      */     public Name flatName() {
/* 1014 */       return this.flatname;
/*      */     }
/*      */
/*      */     public boolean isSubClass(Symbol param1Symbol, Types param1Types) {
/* 1018 */       if (this == param1Symbol)
/* 1019 */         return true;
/* 1020 */       if ((param1Symbol.flags() & 0x200L) != 0L)
/* 1021 */       { for (Type type = this.type; type.hasTag(TypeTag.CLASS); type = param1Types.supertype(type)) {
/* 1022 */           List<Type> list = param1Types.interfaces(type);
/* 1023 */           for (; list.nonEmpty();
/* 1024 */             list = list.tail)
/* 1025 */           { if (((Type)list.head).tsym.isSubClass(param1Symbol, param1Types)) return true;  }
/*      */         }  }
/* 1027 */       else { for (Type type = this.type; type.hasTag(TypeTag.CLASS); type = param1Types.supertype(type)) {
/* 1028 */           if (type.tsym == param1Symbol) return true;
/*      */         }  }
/* 1030 */        return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void complete() throws CompletionFailure {
/*      */       try {
/* 1037 */         super.complete();
/* 1038 */       } catch (CompletionFailure completionFailure) {
/*      */
/* 1040 */         this.flags_field |= 0x9L;
/* 1041 */         this.type = new Type.ErrorType(this, Type.noType);
/* 1042 */         throw completionFailure;
/*      */       }
/*      */     }
/*      */
/*      */     public List<Type> getInterfaces() {
/* 1047 */       complete();
/* 1048 */       if (this.type instanceof Type.ClassType) {
/* 1049 */         Type.ClassType classType = (Type.ClassType)this.type;
/* 1050 */         if (classType.interfaces_field == null)
/* 1051 */           classType.interfaces_field = List.nil();
/* 1052 */         if (classType.all_interfaces_field != null)
/* 1053 */           return Type.getModelTypes(classType.all_interfaces_field);
/* 1054 */         return classType.interfaces_field;
/*      */       }
/* 1056 */       return List.nil();
/*      */     }
/*      */
/*      */
/*      */     public Type getSuperclass() {
/* 1061 */       complete();
/* 1062 */       if (this.type instanceof Type.ClassType) {
/* 1063 */         Type.ClassType classType = (Type.ClassType)this.type;
/* 1064 */         if (classType.supertype_field == null) {
/* 1065 */           classType.supertype_field = Type.noType;
/*      */         }
/* 1067 */         return classType.isInterface() ? Type.noType : classType.supertype_field
/*      */
/* 1069 */           .getModelType();
/*      */       }
/* 1071 */       return Type.noType;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private ClassSymbol getSuperClassToSearchForAnnotations() {
/* 1081 */       Type type = getSuperclass();
/*      */
/* 1083 */       if (!type.hasTag(TypeTag.CLASS) || type.isErroneous()) {
/* 1084 */         return null;
/*      */       }
/* 1086 */       return (ClassSymbol)type.tsym;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     protected <A extends java.lang.annotation.Annotation> A[] getInheritedAnnotations(Class<A> param1Class) {
/* 1093 */       ClassSymbol classSymbol = getSuperClassToSearchForAnnotations();
/*      */
/* 1095 */       return (classSymbol == null) ? super.<A>getInheritedAnnotations(param1Class) : classSymbol
/* 1096 */         .<A>getAnnotationsByType(param1Class);
/*      */     }
/*      */
/*      */
/*      */     public ElementKind getKind() {
/* 1101 */       long l = flags();
/* 1102 */       if ((l & 0x2000L) != 0L)
/* 1103 */         return ElementKind.ANNOTATION_TYPE;
/* 1104 */       if ((l & 0x200L) != 0L)
/* 1105 */         return ElementKind.INTERFACE;
/* 1106 */       if ((l & 0x4000L) != 0L) {
/* 1107 */         return ElementKind.ENUM;
/*      */       }
/* 1109 */       return ElementKind.CLASS;
/*      */     }
/*      */
/*      */
/*      */     public Set<Modifier> getModifiers() {
/* 1114 */       long l = flags();
/* 1115 */       return Flags.asModifierSet(l & 0xFFFFF7FFFFFFFFFFL);
/*      */     }
/*      */
/*      */     public NestingKind getNestingKind() {
/* 1119 */       complete();
/* 1120 */       if (this.owner.kind == 1)
/* 1121 */         return NestingKind.TOP_LEVEL;
/* 1122 */       if (this.name.isEmpty())
/* 1123 */         return NestingKind.ANONYMOUS;
/* 1124 */       if (this.owner.kind == 16) {
/* 1125 */         return NestingKind.LOCAL;
/*      */       }
/* 1127 */       return NestingKind.MEMBER;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     protected <A extends java.lang.annotation.Annotation> Attribute.Compound getAttribute(Class<A> param1Class) {
/* 1134 */       Attribute.Compound compound = super.getAttribute(param1Class);
/*      */
/* 1136 */       boolean bool = param1Class.isAnnotationPresent((Class)Inherited.class);
/* 1137 */       if (compound != null || !bool) {
/* 1138 */         return compound;
/*      */       }
/*      */
/* 1141 */       ClassSymbol classSymbol = getSuperClassToSearchForAnnotations();
/* 1142 */       return (classSymbol == null) ? null : classSymbol
/* 1143 */         .<A>getAttribute(param1Class);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/* 1150 */       return param1ElementVisitor.visitType(this, param1P);
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 1154 */       return param1Visitor.visitClassSymbol(this, param1P);
/*      */     }
/*      */
/*      */     public void markAbstractIfNeeded(Types param1Types) {
/* 1158 */       if (param1Types.enter.getEnv(this) != null && (
/* 1159 */         flags() & 0x4000L) != 0L && (param1Types.supertype(this.type)).tsym == param1Types.syms.enumSym && (
/* 1160 */         flags() & 0x410L) == 0L &&
/* 1161 */         param1Types.firstUnimplementedAbstract(this) != null)
/*      */       {
/* 1163 */         this.flags_field |= 0x400L;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static class VarSymbol
/*      */     extends Symbol
/*      */     implements VariableElement
/*      */   {
/* 1175 */     public int pos = -1;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1186 */     public int adr = -1;
/*      */
/*      */     private Object data;
/*      */
/*      */     public VarSymbol(long param1Long, Name param1Name, Type param1Type, Symbol param1Symbol) {
/* 1191 */       super(4, param1Long, param1Name, param1Type, param1Symbol);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public VarSymbol clone(Symbol param1Symbol) {
/* 1197 */       VarSymbol varSymbol = new VarSymbol(this.flags_field, this.name, this.type, param1Symbol)
/*      */         {
/*      */           public Symbol baseSymbol() {
/* 1200 */             return VarSymbol.this;
/*      */           }
/*      */         };
/* 1203 */       varSymbol.pos = this.pos;
/* 1204 */       varSymbol.adr = this.adr;
/* 1205 */       varSymbol.data = this.data;
/*      */
/* 1207 */       return varSymbol;
/*      */     }
/*      */
/*      */     public String toString() {
/* 1211 */       return this.name.toString();
/*      */     }
/*      */
/*      */     public Symbol asMemberOf(Type param1Type, Types param1Types) {
/* 1215 */       return new VarSymbol(this.flags_field, this.name, param1Types.memberType(param1Type, this), this.owner);
/*      */     }
/*      */
/*      */     public ElementKind getKind() {
/* 1219 */       long l = flags();
/* 1220 */       if ((l & 0x200000000L) != 0L) {
/* 1221 */         if (isExceptionParameter()) {
/* 1222 */           return ElementKind.EXCEPTION_PARAMETER;
/*      */         }
/* 1224 */         return ElementKind.PARAMETER;
/* 1225 */       }  if ((l & 0x4000L) != 0L)
/* 1226 */         return ElementKind.ENUM_CONSTANT;
/* 1227 */       if (this.owner.kind == 2 || this.owner.kind == 63)
/* 1228 */         return ElementKind.FIELD;
/* 1229 */       if (isResourceVariable()) {
/* 1230 */         return ElementKind.RESOURCE_VARIABLE;
/*      */       }
/* 1232 */       return ElementKind.LOCAL_VARIABLE;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/* 1237 */       return param1ElementVisitor.visitVariable(this, param1P);
/*      */     }
/*      */
/*      */     public Object getConstantValue() {
/* 1241 */       return Constants.decode(getConstValue(), this.type);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void setLazyConstValue(final Env<AttrContext> env, final Attr attr, final JCTree.JCVariableDecl variable) {
/* 1248 */       setData(new Callable() {
/*      */             public Object call() {
/* 1250 */               return attr.attribLazyConstantValue(env, variable, VarSymbol.this.type);
/*      */             }
/*      */           });
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
/*      */     public boolean isExceptionParameter() {
/* 1264 */       return (this.data == ElementKind.EXCEPTION_PARAMETER);
/*      */     }
/*      */
/*      */     public boolean isResourceVariable() {
/* 1268 */       return (this.data == ElementKind.RESOURCE_VARIABLE);
/*      */     }
/*      */
/*      */
/*      */     public Object getConstValue() {
/* 1273 */       if (this.data == ElementKind.EXCEPTION_PARAMETER || this.data == ElementKind.RESOURCE_VARIABLE)
/*      */       {
/* 1275 */         return null; }
/* 1276 */       if (this.data instanceof Callable) {
/*      */
/*      */
/* 1279 */         Callable callable = (Callable)this.data;
/* 1280 */         this.data = null;
/*      */         try {
/* 1282 */           this.data = callable.call();
/* 1283 */         } catch (Exception exception) {
/* 1284 */           throw new AssertionError(exception);
/*      */         }
/*      */       }
/* 1287 */       return this.data;
/*      */     }
/*      */
/*      */     public void setData(Object param1Object) {
/* 1291 */       Assert.check(!(param1Object instanceof Env), this);
/* 1292 */       this.data = param1Object;
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 1296 */       return param1Visitor.visitVarSymbol(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class MethodSymbol
/*      */     extends Symbol
/*      */     implements ExecutableElement
/*      */   {
/* 1305 */     public Code code = null;
/*      */
/*      */
/* 1308 */     public List<VarSymbol> extraParams = List.nil();
/*      */
/*      */
/* 1311 */     public List<VarSymbol> capturedLocals = List.nil();
/*      */
/*      */
/* 1314 */     public List<VarSymbol> params = null;
/*      */
/*      */
/*      */
/*      */
/*      */     public List<Name> savedParameterNames;
/*      */
/*      */
/*      */
/* 1323 */     public Attribute defaultValue = null;
/*      */
/*      */
/*      */
/*      */     public MethodSymbol(long param1Long, Name param1Name, Type param1Type, Symbol param1Symbol) {
/* 1328 */       super(16, param1Long, param1Name, param1Type, param1Symbol);
/* 1329 */       if (param1Symbol.type.hasTag(TypeTag.TYPEVAR)) Assert.error(param1Symbol + "." + param1Name);
/*      */
/*      */     }
/*      */
/*      */
/*      */     public MethodSymbol clone(Symbol param1Symbol) {
/* 1335 */       MethodSymbol methodSymbol = new MethodSymbol(this.flags_field, this.name, this.type, param1Symbol)
/*      */         {
/*      */           public Symbol baseSymbol() {
/* 1338 */             return MethodSymbol.this;
/*      */           }
/*      */         };
/* 1341 */       methodSymbol.code = this.code;
/* 1342 */       return methodSymbol;
/*      */     }
/*      */
/*      */
/*      */     public Set<Modifier> getModifiers() {
/* 1347 */       long l = flags();
/* 1348 */       return Flags.asModifierSet(((l & 0x80000000000L) != 0L) ? (l & 0xFFFFFFFFFFFFFBFFL) : l);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public String toString() {
/* 1354 */       if ((flags() & 0x100000L) != 0L) {
/* 1355 */         return this.owner.name.toString();
/*      */       }
/*      */
/*      */
/* 1359 */       String str = (this.name == this.name.table.names.init) ? this.owner.name.toString() : this.name.toString();
/* 1360 */       if (this.type != null) {
/* 1361 */         if (this.type.hasTag(TypeTag.FORALL))
/* 1362 */           str = "<" + ((Type.ForAll)this.type).getTypeArguments() + ">" + str;
/* 1363 */         str = str + "(" + this.type.argtypes(((flags() & 0x400000000L) != 0L)) + ")";
/*      */       }
/* 1365 */       return str;
/*      */     }
/*      */
/*      */
/*      */     public boolean isDynamic() {
/* 1370 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public Symbol implemented(TypeSymbol param1TypeSymbol, Types param1Types) {
/* 1378 */       Symbol symbol = null;
/* 1379 */       List<Type> list = param1Types.interfaces(param1TypeSymbol.type);
/* 1380 */       for (; symbol == null && list.nonEmpty();
/* 1381 */         list = list.tail) {
/* 1382 */         TypeSymbol typeSymbol = ((Type)list.head).tsym;
/* 1383 */         symbol = implementedIn(typeSymbol, param1Types);
/* 1384 */         if (symbol == null)
/* 1385 */           symbol = implemented(typeSymbol, param1Types);
/*      */       }
/* 1387 */       return symbol;
/*      */     }
/*      */
/*      */     public Symbol implementedIn(TypeSymbol param1TypeSymbol, Types param1Types) {
/* 1391 */       Symbol symbol = null;
/* 1392 */       Scope.Entry entry = param1TypeSymbol.members().lookup(this.name);
/* 1393 */       for (; symbol == null && entry.scope != null;
/* 1394 */         entry = entry.next()) {
/* 1395 */         if (overrides(entry.sym, (TypeSymbol)this.owner, param1Types, true) && param1Types
/*      */
/*      */
/* 1398 */           .isSameType(this.type.getReturnType(), param1Types
/* 1399 */             .memberType(this.owner.type, entry.sym).getReturnType())) {
/* 1400 */           symbol = entry.sym;
/*      */         }
/*      */       }
/* 1403 */       return symbol;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public boolean binaryOverrides(Symbol param1Symbol, TypeSymbol param1TypeSymbol, Types param1Types) {
/* 1410 */       if (isConstructor() || param1Symbol.kind != 16) return false;
/*      */
/* 1412 */       if (this == param1Symbol) return true;
/* 1413 */       MethodSymbol methodSymbol = (MethodSymbol)param1Symbol;
/*      */
/*      */
/* 1416 */       if (methodSymbol.isOverridableIn((TypeSymbol)this.owner) && param1Types
/* 1417 */         .asSuper(this.owner.type, methodSymbol.owner) != null && param1Types
/* 1418 */         .isSameType(erasure(param1Types), methodSymbol.erasure(param1Types))) {
/* 1419 */         return true;
/*      */       }
/*      */
/* 1422 */       return ((
/* 1423 */         flags() & 0x400L) == 0L && methodSymbol
/* 1424 */         .isOverridableIn(param1TypeSymbol) &&
/* 1425 */         isMemberOf(param1TypeSymbol, param1Types) && param1Types
/* 1426 */         .isSameType(erasure(param1Types), methodSymbol.erasure(param1Types)));
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public MethodSymbol binaryImplementation(ClassSymbol param1ClassSymbol, Types param1Types) {
/* 1435 */       for (ClassSymbol classSymbol = param1ClassSymbol; classSymbol != null; typeSymbol = (param1Types.supertype(classSymbol.type)).tsym) {
/* 1436 */         TypeSymbol typeSymbol; Scope.Entry entry = classSymbol.members().lookup(this.name);
/* 1437 */         for (; entry.scope != null;
/* 1438 */           entry = entry.next()) {
/* 1439 */           if (entry.sym.kind == 16 && ((MethodSymbol)entry.sym)
/* 1440 */             .binaryOverrides(this, param1ClassSymbol, param1Types))
/* 1441 */             return (MethodSymbol)entry.sym;
/*      */         }
/*      */       }
/* 1444 */       return null;
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
/*      */     public boolean overrides(Symbol param1Symbol, TypeSymbol param1TypeSymbol, Types param1Types, boolean param1Boolean) {
/* 1457 */       if (isConstructor() || param1Symbol.kind != 16) return false;
/*      */
/* 1459 */       if (this == param1Symbol) return true;
/* 1460 */       MethodSymbol methodSymbol = (MethodSymbol)param1Symbol;
/*      */
/*      */
/* 1463 */       if (methodSymbol.isOverridableIn((TypeSymbol)this.owner) && param1Types
/* 1464 */         .asSuper(this.owner.type, methodSymbol.owner) != null) {
/* 1465 */         Type type3 = param1Types.memberType(this.owner.type, this);
/* 1466 */         Type type4 = param1Types.memberType(this.owner.type, methodSymbol);
/* 1467 */         if (param1Types.isSubSignature(type3, type4)) {
/* 1468 */           if (!param1Boolean)
/* 1469 */             return true;
/* 1470 */           if (param1Types.returnTypeSubstitutable(type3, type4)) {
/* 1471 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */
/* 1476 */       if ((flags() & 0x400L) != 0L || ((methodSymbol
/* 1477 */         .flags() & 0x400L) == 0L && (methodSymbol.flags() & 0x80000000000L) == 0L) ||
/* 1478 */         !methodSymbol.isOverridableIn(param1TypeSymbol) ||
/* 1479 */         !isMemberOf(param1TypeSymbol, param1Types)) {
/* 1480 */         return false;
/*      */       }
/*      */
/* 1483 */       Type type1 = param1Types.memberType(param1TypeSymbol.type, this);
/* 1484 */       Type type2 = param1Types.memberType(param1TypeSymbol.type, methodSymbol);
/* 1485 */       return (param1Types
/* 1486 */         .isSubSignature(type1, type2) && (!param1Boolean || param1Types
/* 1487 */         .resultSubtype(type1, type2, param1Types.noWarnings)));
/*      */     }
/*      */
/*      */
/*      */     private boolean isOverridableIn(TypeSymbol param1TypeSymbol) {
/* 1492 */       switch ((int)(this.flags_field & 0x7L)) {
/*      */         case 2:
/* 1494 */           return false;
/*      */         case 1:
/* 1496 */           return (!this.owner.isInterface() || (this.flags_field & 0x8L) == 0L);
/*      */
/*      */         case 4:
/* 1499 */           return ((param1TypeSymbol.flags() & 0x200L) == 0L);
/*      */
/*      */
/*      */         case 0:
/* 1503 */           return (
/* 1504 */             packge() == param1TypeSymbol.packge() && (param1TypeSymbol
/* 1505 */             .flags() & 0x200L) == 0L);
/*      */       }
/* 1507 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public boolean isInheritedIn(Symbol param1Symbol, Types param1Types) {
/* 1513 */       switch ((int)(this.flags_field & 0x7L)) {
/*      */         case 1:
/* 1515 */           return (!this.owner.isInterface() || param1Symbol == this.owner || (this.flags_field & 0x8L) == 0L);
/*      */       }
/*      */
/*      */
/* 1519 */       return super.isInheritedIn(param1Symbol, param1Types);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public MethodSymbol implementation(TypeSymbol param1TypeSymbol, Types param1Types, boolean param1Boolean) {
/* 1528 */       return implementation(param1TypeSymbol, param1Types, param1Boolean, implementation_filter);
/*      */     }
/*      */
/* 1531 */     public static final Filter<Symbol> implementation_filter = new Filter<Symbol>() {
/*      */         public boolean accepts(Symbol param2Symbol) {
/* 1533 */           return (param2Symbol.kind == 16 && (param2Symbol
/* 1534 */             .flags() & 0x1000L) == 0L);
/*      */         }
/*      */       };
/*      */
/*      */     public MethodSymbol implementation(TypeSymbol param1TypeSymbol, Types param1Types, boolean param1Boolean, Filter<Symbol> param1Filter) {
/* 1539 */       MethodSymbol methodSymbol = param1Types.implementation(this, param1TypeSymbol, param1Boolean, param1Filter);
/* 1540 */       if (methodSymbol != null) {
/* 1541 */         return methodSymbol;
/*      */       }
/*      */
/*      */
/* 1545 */       if (param1Types.isDerivedRaw(param1TypeSymbol.type) && !param1TypeSymbol.isInterface()) {
/* 1546 */         return implementation((param1Types.supertype(param1TypeSymbol.type)).tsym, param1Types, param1Boolean);
/*      */       }
/* 1548 */       return null;
/*      */     }
/*      */
/*      */     public List<VarSymbol> params() {
/* 1552 */       this.owner.complete();
/* 1553 */       if (this.params == null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1561 */         List<Name> list1 = this.savedParameterNames;
/* 1562 */         this.savedParameterNames = null;
/*      */
/* 1564 */         if (list1 == null || list1.size() != this.type.getParameterTypes().size()) {
/* 1565 */           list1 = List.nil();
/*      */         }
/* 1567 */         ListBuffer listBuffer = new ListBuffer();
/* 1568 */         List<Name> list2 = list1;
/*      */
/*      */
/* 1571 */         byte b = 0;
/* 1572 */         for (Type type : this.type.getParameterTypes()) {
/*      */           Name name;
/* 1574 */           if (list2.isEmpty()) {
/*      */
/* 1576 */             name = createArgName(b, list1);
/*      */           } else {
/* 1578 */             name = (Name)list2.head;
/* 1579 */             list2 = list2.tail;
/* 1580 */             if (name.isEmpty())
/*      */             {
/* 1582 */               name = createArgName(b, list1);
/*      */             }
/*      */           }
/* 1585 */           listBuffer.append(new VarSymbol(8589934592L, name, type, this));
/* 1586 */           b++;
/*      */         }
/* 1588 */         this.params = listBuffer.toList();
/*      */       }
/* 1590 */       return this.params;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private Name createArgName(int param1Int, List<Name> param1List) {
/* 1598 */       String str = "arg";
/*      */       while (true) {
/* 1600 */         Name name = this.name.table.fromString(str + param1Int);
/* 1601 */         if (!param1List.contains(name))
/* 1602 */           return name;
/* 1603 */         str = str + "$";
/*      */       }
/*      */     }
/*      */
/*      */     public Symbol asMemberOf(Type param1Type, Types param1Types) {
/* 1608 */       return new MethodSymbol(this.flags_field, this.name, param1Types.memberType(param1Type, this), this.owner);
/*      */     }
/*      */
/*      */     public ElementKind getKind() {
/* 1612 */       if (this.name == this.name.table.names.init)
/* 1613 */         return ElementKind.CONSTRUCTOR;
/* 1614 */       if (this.name == this.name.table.names.clinit)
/* 1615 */         return ElementKind.STATIC_INIT;
/* 1616 */       if ((flags() & 0x100000L) != 0L) {
/* 1617 */         return isStatic() ? ElementKind.STATIC_INIT : ElementKind.INSTANCE_INIT;
/*      */       }
/* 1619 */       return ElementKind.METHOD;
/*      */     }
/*      */
/*      */     public boolean isStaticOrInstanceInit() {
/* 1623 */       return (getKind() == ElementKind.STATIC_INIT ||
/* 1624 */         getKind() == ElementKind.INSTANCE_INIT);
/*      */     }
/*      */
/*      */     public Attribute getDefaultValue() {
/* 1628 */       return this.defaultValue;
/*      */     }
/*      */
/*      */     public List<VarSymbol> getParameters() {
/* 1632 */       return params();
/*      */     }
/*      */
/*      */     public boolean isVarArgs() {
/* 1636 */       return ((flags() & 0x400000000L) != 0L);
/*      */     }
/*      */
/*      */     public boolean isDefault() {
/* 1640 */       return ((flags() & 0x80000000000L) != 0L);
/*      */     }
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/* 1644 */       return param1ElementVisitor.visitExecutable(this, param1P);
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 1648 */       return param1Visitor.visitMethodSymbol(this, param1P);
/*      */     }
/*      */
/*      */     public Type getReceiverType() {
/* 1652 */       return asType().getReceiverType();
/*      */     }
/*      */
/*      */     public Type getReturnType() {
/* 1656 */       return asType().getReturnType();
/*      */     }
/*      */
/*      */     public List<Type> getThrownTypes() {
/* 1660 */       return asType().getThrownTypes();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class DynamicMethodSymbol
/*      */     extends MethodSymbol
/*      */   {
/*      */     public Object[] staticArgs;
/*      */     public Symbol bsm;
/*      */     public int bsmKind;
/*      */
/*      */     public DynamicMethodSymbol(Name param1Name, Symbol param1Symbol, int param1Int, MethodSymbol param1MethodSymbol, Type param1Type, Object[] param1ArrayOfObject) {
/* 1673 */       super(0L, param1Name, param1Type, param1Symbol);
/* 1674 */       this.bsm = param1MethodSymbol;
/* 1675 */       this.bsmKind = param1Int;
/* 1676 */       this.staticArgs = param1ArrayOfObject;
/*      */     }
/*      */
/*      */
/*      */     public boolean isDynamic() {
/* 1681 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class OperatorSymbol
/*      */     extends MethodSymbol
/*      */   {
/*      */     public int opcode;
/*      */
/*      */     public OperatorSymbol(Name param1Name, Type param1Type, int param1Int, Symbol param1Symbol) {
/* 1692 */       super(9L, param1Name, param1Type, param1Symbol);
/* 1693 */       this.opcode = param1Int;
/*      */     }
/*      */
/*      */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 1697 */       return param1Visitor.visitOperatorSymbol(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static class CompletionFailure
/*      */     extends RuntimeException
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */
/*      */
/*      */     public Symbol sym;
/*      */
/*      */
/*      */     public JCDiagnostic diag;
/*      */
/*      */
/*      */     @Deprecated
/*      */     public String errmsg;
/*      */
/*      */
/*      */
/*      */     public CompletionFailure(Symbol param1Symbol, String param1String) {
/* 1722 */       this.sym = param1Symbol;
/* 1723 */       this.errmsg = param1String;
/*      */     }
/*      */
/*      */
/*      */     public CompletionFailure(Symbol param1Symbol, JCDiagnostic param1JCDiagnostic) {
/* 1728 */       this.sym = param1Symbol;
/* 1729 */       this.diag = param1JCDiagnostic;
/*      */     }
/*      */
/*      */
/*      */     public JCDiagnostic getDiagnostic() {
/* 1734 */       return this.diag;
/*      */     }
/*      */
/*      */
/*      */     public String getMessage() {
/* 1739 */       if (this.diag != null) {
/* 1740 */         return this.diag.getMessage(null);
/*      */       }
/* 1742 */       return this.errmsg;
/*      */     }
/*      */
/*      */     public Object getDetailValue() {
/* 1746 */       return (this.diag != null) ? this.diag : this.errmsg;
/*      */     }
/*      */
/*      */
/*      */     public CompletionFailure initCause(Throwable param1Throwable) {
/* 1751 */       super.initCause(param1Throwable);
/* 1752 */       return this;
/*      */     }
/*      */   }
/*      */
/*      */   public static interface Completer {
/*      */     void complete(Symbol param1Symbol) throws CompletionFailure;
/*      */   }
/*      */
/*      */   public static interface Visitor<R, P> {
/*      */     R visitClassSymbol(ClassSymbol param1ClassSymbol, P param1P);
/*      */
/*      */     R visitMethodSymbol(MethodSymbol param1MethodSymbol, P param1P);
/*      */
/*      */     R visitPackageSymbol(PackageSymbol param1PackageSymbol, P param1P);
/*      */
/*      */     R visitOperatorSymbol(OperatorSymbol param1OperatorSymbol, P param1P);
/*      */
/*      */     R visitVarSymbol(VarSymbol param1VarSymbol, P param1P);
/*      */
/*      */     R visitTypeSymbol(TypeSymbol param1TypeSymbol, P param1P);
/*      */
/*      */     R visitSymbol(Symbol param1Symbol, P param1P);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Symbol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
