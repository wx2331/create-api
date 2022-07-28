/*     */ package com.sun.tools.javac.code;
/*     */
/*     */ import com.sun.tools.javac.comp.Annotate;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.util.Map;
/*     */ import javax.tools.JavaFileObject;
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
/*     */
/*     */
/*     */
/*     */
/*     */ public class SymbolMetadata
/*     */ {
/*  73 */   private static final List<Attribute.Compound> DECL_NOT_STARTED = List.of(null);
/*  74 */   private static final List<Attribute.Compound> DECL_IN_PROGRESS = List.of(null);
/*     */
/*     */
/*     */
/*     */
/*  79 */   private List<Attribute.Compound> attributes = DECL_NOT_STARTED;
/*     */
/*     */
/*     */
/*     */
/*     */
/*  85 */   private List<Attribute.TypeCompound> type_attributes = List.nil();
/*     */
/*     */
/*     */
/*     */
/*     */
/*  91 */   private List<Attribute.TypeCompound> init_type_attributes = List.nil();
/*     */
/*     */
/*     */
/*     */
/*     */
/*  97 */   private List<Attribute.TypeCompound> clinit_type_attributes = List.nil();
/*     */
/*     */
/*     */   private final Symbol sym;
/*     */
/*     */
/*     */
/*     */   public SymbolMetadata(Symbol paramSymbol) {
/* 105 */     this.sym = paramSymbol;
/*     */   }
/*     */
/*     */   public List<Attribute.Compound> getDeclarationAttributes() {
/* 109 */     return filterDeclSentinels(this.attributes);
/*     */   }
/*     */
/*     */   public List<Attribute.TypeCompound> getTypeAttributes() {
/* 113 */     return this.type_attributes;
/*     */   }
/*     */
/*     */   public List<Attribute.TypeCompound> getInitTypeAttributes() {
/* 117 */     return this.init_type_attributes;
/*     */   }
/*     */
/*     */   public List<Attribute.TypeCompound> getClassInitTypeAttributes() {
/* 121 */     return this.clinit_type_attributes;
/*     */   }
/*     */
/*     */   public void setDeclarationAttributes(List<Attribute.Compound> paramList) {
/* 125 */     Assert.check((pendingCompletion() || !isStarted()));
/* 126 */     if (paramList == null) {
/* 127 */       throw new NullPointerException();
/*     */     }
/* 129 */     this.attributes = paramList;
/*     */   }
/*     */
/*     */   public void setTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 133 */     if (paramList == null) {
/* 134 */       throw new NullPointerException();
/*     */     }
/* 136 */     this.type_attributes = paramList;
/*     */   }
/*     */
/*     */   public void setInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 140 */     if (paramList == null) {
/* 141 */       throw new NullPointerException();
/*     */     }
/* 143 */     this.init_type_attributes = paramList;
/*     */   }
/*     */
/*     */   public void setClassInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 147 */     if (paramList == null) {
/* 148 */       throw new NullPointerException();
/*     */     }
/* 150 */     this.clinit_type_attributes = paramList;
/*     */   }
/*     */
/*     */   public void setAttributes(SymbolMetadata paramSymbolMetadata) {
/* 154 */     if (paramSymbolMetadata == null) {
/* 155 */       throw new NullPointerException();
/*     */     }
/* 157 */     setDeclarationAttributes(paramSymbolMetadata.getDeclarationAttributes());
/* 158 */     if ((this.sym.flags() & 0x80000000L) != 0L) {
/* 159 */       Assert.check((paramSymbolMetadata.sym.kind == 16));
/* 160 */       ListBuffer listBuffer = new ListBuffer();
/* 161 */       for (Attribute.TypeCompound typeCompound : paramSymbolMetadata.getTypeAttributes()) {
/*     */
/* 163 */         if (!typeCompound.position.type.isLocal())
/* 164 */           listBuffer.append(typeCompound);
/*     */       }
/* 166 */       setTypeAttributes(listBuffer.toList());
/*     */     } else {
/* 168 */       setTypeAttributes(paramSymbolMetadata.getTypeAttributes());
/*     */     }
/* 170 */     if (this.sym.kind == 2) {
/* 171 */       setInitTypeAttributes(paramSymbolMetadata.getInitTypeAttributes());
/* 172 */       setClassInitTypeAttributes(paramSymbolMetadata.getClassInitTypeAttributes());
/*     */     }
/*     */   }
/*     */
/*     */   public void setDeclarationAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.Compound> paramAnnotateRepeatedContext) {
/* 177 */     Assert.check((pendingCompletion() || (!isStarted() && this.sym.kind == 1)));
/* 178 */     setDeclarationAttributes(getAttributesForCompletion(paramAnnotateRepeatedContext));
/*     */   }
/*     */
/*     */   public void appendTypeAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.TypeCompound> paramAnnotateRepeatedContext) {
/* 182 */     appendUniqueTypes(getAttributesForCompletion(paramAnnotateRepeatedContext));
/*     */   }
/*     */
/*     */
/*     */
/*     */   private <T extends Attribute.Compound> List<T> getAttributesForCompletion(final Annotate.AnnotateRepeatedContext<T> ctx) {
/* 188 */     Map map = ctx.annotated;
/* 189 */     boolean bool = false;
/* 190 */     List list = List.nil();
/* 191 */     for (ListBuffer listBuffer : map.values()) {
/* 192 */       if (listBuffer.size() == 1) {
/* 193 */         list = list.prepend(listBuffer.first());
/*     */
/*     */
/*     */         continue;
/*     */       }
/*     */
/* 199 */       Placeholder<T> placeholder2 = new Placeholder<>(ctx, listBuffer.toList(), this.sym);
/* 200 */       Placeholder<T> placeholder1 = placeholder2;
/* 201 */       list = list.prepend(placeholder1);
/* 202 */       bool = true;
/*     */     }
/*     */
/*     */
/* 206 */     if (bool)
/*     */     {
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
/* 224 */       ctx.annotateRepeated(new Annotate.Worker()
/*     */           {
/*     */             public String toString() {
/* 227 */               return "repeated annotation pass of: " + SymbolMetadata.this.sym + " in: " + SymbolMetadata.this.sym.owner;
/*     */             }
/*     */
/*     */
/*     */             public void run() {
/* 232 */               SymbolMetadata.this.complete(ctx);
/*     */             }
/*     */           });
/*     */     }
/*     */
/* 237 */     return list.reverse();
/*     */   }
/*     */
/*     */   public SymbolMetadata reset() {
/* 241 */     this.attributes = DECL_IN_PROGRESS;
/* 242 */     return this;
/*     */   }
/*     */
/*     */   public boolean isEmpty() {
/* 246 */     return (!isStarted() ||
/* 247 */       pendingCompletion() || this.attributes
/* 248 */       .isEmpty());
/*     */   }
/*     */
/*     */   public boolean isTypesEmpty() {
/* 252 */     return this.type_attributes.isEmpty();
/*     */   }
/*     */
/*     */   public boolean pendingCompletion() {
/* 256 */     return (this.attributes == DECL_IN_PROGRESS);
/*     */   }
/*     */
/*     */   public SymbolMetadata append(List<Attribute.Compound> paramList) {
/* 260 */     this.attributes = filterDeclSentinels(this.attributes);
/*     */
/* 262 */     if (!paramList.isEmpty())
/*     */     {
/* 264 */       if (this.attributes.isEmpty()) {
/* 265 */         this.attributes = paramList;
/*     */       } else {
/* 267 */         this.attributes = this.attributes.appendList(paramList);
/*     */       }  }
/* 269 */     return this;
/*     */   }
/*     */
/*     */   public SymbolMetadata appendUniqueTypes(List<Attribute.TypeCompound> paramList) {
/* 273 */     if (!paramList.isEmpty())
/*     */     {
/* 275 */       if (this.type_attributes.isEmpty()) {
/* 276 */         this.type_attributes = paramList;
/*     */       }
/*     */       else {
/*     */
/* 280 */         for (Attribute.TypeCompound typeCompound : paramList) {
/* 281 */           if (!this.type_attributes.contains(typeCompound))
/* 282 */             this.type_attributes = this.type_attributes.append(typeCompound);
/*     */         }
/*     */       }  }
/* 285 */     return this;
/*     */   }
/*     */
/*     */   public SymbolMetadata appendInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 289 */     if (!paramList.isEmpty())
/*     */     {
/* 291 */       if (this.init_type_attributes.isEmpty()) {
/* 292 */         this.init_type_attributes = paramList;
/*     */       } else {
/* 294 */         this.init_type_attributes = this.init_type_attributes.appendList(paramList);
/*     */       }  }
/* 296 */     return this;
/*     */   }
/*     */
/*     */   public SymbolMetadata appendClassInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 300 */     if (!paramList.isEmpty())
/*     */     {
/* 302 */       if (this.clinit_type_attributes.isEmpty()) {
/* 303 */         this.clinit_type_attributes = paramList;
/*     */       } else {
/* 305 */         this.clinit_type_attributes = this.clinit_type_attributes.appendList(paramList);
/*     */       }  }
/* 307 */     return this;
/*     */   }
/*     */
/*     */   public SymbolMetadata prepend(List<Attribute.Compound> paramList) {
/* 311 */     this.attributes = filterDeclSentinels(this.attributes);
/*     */
/* 313 */     if (!paramList.isEmpty())
/*     */     {
/* 315 */       if (this.attributes.isEmpty()) {
/* 316 */         this.attributes = paramList;
/*     */       } else {
/* 318 */         this.attributes = this.attributes.prependList(paramList);
/*     */       }  }
/* 320 */     return this;
/*     */   }
/*     */
/*     */   private List<Attribute.Compound> filterDeclSentinels(List<Attribute.Compound> paramList) {
/* 324 */     return (paramList == DECL_IN_PROGRESS || paramList == DECL_NOT_STARTED) ?
/* 325 */       List.nil() : paramList;
/*     */   }
/*     */
/*     */
/*     */   private boolean isStarted() {
/* 330 */     return (this.attributes != DECL_NOT_STARTED);
/*     */   }
/*     */
/*     */   private List<Attribute.Compound> getPlaceholders() {
/* 334 */     List list = List.nil();
/* 335 */     for (Attribute.Compound compound : filterDeclSentinels(this.attributes)) {
/* 336 */       if (compound instanceof Placeholder) {
/* 337 */         list = list.prepend(compound);
/*     */       }
/*     */     }
/* 340 */     return list.reverse();
/*     */   }
/*     */
/*     */   private List<Attribute.TypeCompound> getTypePlaceholders() {
/* 344 */     List list = List.nil();
/* 345 */     for (Attribute.TypeCompound typeCompound : this.type_attributes) {
/* 346 */       if (typeCompound instanceof Placeholder) {
/* 347 */         list = list.prepend(typeCompound);
/*     */       }
/*     */     }
/* 350 */     return list.reverse();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private <T extends Attribute.Compound> void complete(Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext) {
/* 357 */     Log log = paramAnnotateRepeatedContext.log;
/* 358 */     Env env = paramAnnotateRepeatedContext.env;
/* 359 */     JavaFileObject javaFileObject = log.useSource(env.toplevel.sourcefile);
/*     */
/*     */     try {
/* 362 */       if (paramAnnotateRepeatedContext.isTypeCompound) {
/* 363 */         Assert.check(!isTypesEmpty());
/*     */
/* 365 */         if (isTypesEmpty()) {
/*     */           return;
/*     */         }
/*     */
/* 369 */         List list = List.nil();
/* 370 */         for (Attribute.TypeCompound typeCompound : getTypeAttributes()) {
/* 371 */           if (typeCompound instanceof Placeholder) {
/*     */
/* 373 */             Placeholder<Attribute.TypeCompound> placeholder = (Placeholder)typeCompound;
/* 374 */             Attribute.TypeCompound typeCompound1 = replaceOne(placeholder, placeholder.getRepeatedContext());
/*     */
/* 376 */             if (null != typeCompound1)
/* 377 */               list = list.prepend(typeCompound1);
/*     */             continue;
/*     */           }
/* 380 */           list = list.prepend(typeCompound);
/*     */         }
/*     */
/*     */
/* 384 */         this.type_attributes = list.reverse();
/*     */
/* 386 */         Assert.check(getTypePlaceholders().isEmpty());
/*     */       } else {
/* 388 */         Assert.check(!pendingCompletion());
/*     */
/* 390 */         if (isEmpty()) {
/*     */           return;
/*     */         }
/*     */
/* 394 */         List list = List.nil();
/* 395 */         for (Attribute.Compound compound : getDeclarationAttributes()) {
/* 396 */           if (compound instanceof Placeholder) {
/*     */
/* 398 */             Object object = replaceOne((Placeholder<Object>)compound, (Annotate.AnnotateRepeatedContext)paramAnnotateRepeatedContext);
/*     */
/* 400 */             if (null != object)
/* 401 */               list = list.prepend(object);
/*     */             continue;
/*     */           }
/* 404 */           list = list.prepend(compound);
/*     */         }
/*     */
/*     */
/* 408 */         this.attributes = list.reverse();
/*     */
/* 410 */         Assert.check(getPlaceholders().isEmpty());
/*     */       }
/*     */     } finally {
/* 413 */       log.useSource(javaFileObject);
/*     */     }
/*     */   }
/*     */
/*     */   private <T extends Attribute.Compound> T replaceOne(Placeholder<T> paramPlaceholder, Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext) {
/* 418 */     Log log = paramAnnotateRepeatedContext.log;
/*     */
/*     */
/* 421 */     Attribute.Compound compound = paramAnnotateRepeatedContext.processRepeatedAnnotations(paramPlaceholder.getPlaceholderFor(), this.sym);
/*     */
/* 423 */     if (compound != null) {
/*     */
/*     */
/*     */
/* 427 */       ListBuffer listBuffer = (ListBuffer)paramAnnotateRepeatedContext.annotated.get(compound.type.tsym);
/* 428 */       if (listBuffer != null) {
/* 429 */         log.error((JCDiagnostic.DiagnosticPosition)paramAnnotateRepeatedContext.pos.get(listBuffer.first()), "invalid.repeatable.annotation.repeated.and.container.present", new Object[] {
/* 430 */               ((Attribute.Compound)listBuffer.first()).type.tsym
/*     */             });
/*     */       }
/*     */     }
/*     */
/* 435 */     return (T)compound;
/*     */   }
/*     */
/*     */   private static class Placeholder<T extends Attribute.Compound>
/*     */     extends Attribute.TypeCompound {
/*     */     private final Annotate.AnnotateRepeatedContext<T> ctx;
/*     */     private final List<T> placeholderFor;
/*     */     private final Symbol on;
/*     */
/*     */     public Placeholder(Annotate.AnnotateRepeatedContext<T> param1AnnotateRepeatedContext, List<T> param1List, Symbol param1Symbol) {
/* 445 */       super(param1Symbol.type, List.nil(), param1AnnotateRepeatedContext.isTypeCompound ? ((TypeCompound)param1List.head).position : new TypeAnnotationPosition());
/*     */
/*     */
/*     */
/* 449 */       this.ctx = param1AnnotateRepeatedContext;
/* 450 */       this.placeholderFor = param1List;
/* 451 */       this.on = param1Symbol;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 456 */       return "<placeholder: " + this.placeholderFor + " on: " + this.on + ">";
/*     */     }
/*     */
/*     */     public List<T> getPlaceholderFor() {
/* 460 */       return this.placeholderFor;
/*     */     }
/*     */
/*     */     public Annotate.AnnotateRepeatedContext<T> getRepeatedContext() {
/* 464 */       return this.ctx;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\SymbolMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
