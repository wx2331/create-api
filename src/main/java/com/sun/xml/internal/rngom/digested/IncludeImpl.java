/*     */ package com.sun.xml.internal.rngom.digested;
/*     */
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ final class IncludeImpl
/*     */   extends GrammarBuilderImpl
/*     */   implements Include
/*     */ {
/*  66 */   private Set overridenPatterns = new HashSet();
/*     */   private boolean startOverriden = false;
/*     */
/*     */   public IncludeImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/*  70 */     super(p, parent, sb);
/*     */   }
/*     */
/*     */
/*     */   public void define(String name, Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/*  75 */     super.define(name, combine, pattern, loc, anno);
/*  76 */     if (name == "\000#start\000") {
/*  77 */       this.startOverriden = true;
/*     */     } else {
/*  79 */       this.overridenPatterns.add(name);
/*     */     }
/*     */   }
/*     */   public void endInclude(Parseable current, String uri, String ns, Location loc, Annotations anno) throws BuildException, IllegalSchemaException {
/*  83 */     current.parseInclude(uri, this.sb, new IncludedGrammarImpl(this.grammar, this.parent, this.sb), ns);
/*     */   }
/*     */
/*     */   private class IncludedGrammarImpl extends GrammarBuilderImpl implements IncludedGrammar {
/*     */     public IncludedGrammarImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/*  88 */       super(p, parent, sb);
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void define(String name, Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/*  94 */       if (name == "\000#start\000") {
/*  95 */         if (IncludeImpl.this.startOverriden) {
/*     */           return;
/*     */         }
/*  98 */       } else if (IncludeImpl.this.overridenPatterns.contains(name)) {
/*     */         return;
/*     */       }
/*     */
/* 102 */       super.define(name, combine, pattern, loc, anno);
/*     */     }
/*     */
/*     */     public ParsedPattern endIncludedGrammar(Location loc, Annotations anno) throws BuildException {
/* 106 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\IncludeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
