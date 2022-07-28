/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Locator;
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
/*     */ final class PatternParseable
/*     */   implements Parseable
/*     */ {
/*     */   private final DPattern pattern;
/*     */   
/*     */   public PatternParseable(DPattern p) {
/*  70 */     this.pattern = p;
/*     */   }
/*     */   
/*     */   public ParsedPattern parse(SchemaBuilder sb) throws BuildException {
/*  74 */     return this.pattern.<ParsedPattern>accept(new Parser(sb));
/*     */   }
/*     */   
/*     */   public ParsedPattern parseInclude(String uri, SchemaBuilder f, IncludedGrammar g, String inheritedNs) throws BuildException {
/*  78 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ParsedPattern parseExternal(String uri, SchemaBuilder f, Scope s, String inheritedNs) throws BuildException {
/*  82 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private static class Parser
/*     */     implements DPatternVisitor<ParsedPattern> {
/*     */     private final SchemaBuilder sb;
/*     */     
/*     */     public Parser(SchemaBuilder sb) {
/*  90 */       this.sb = sb;
/*     */     }
/*     */ 
/*     */     
/*     */     private Annotations parseAnnotation(DPattern p) {
/*  95 */       return null;
/*     */     }
/*     */     
/*     */     private Location parseLocation(DPattern p) {
/*  99 */       Locator l = p.getLocation();
/* 100 */       return this.sb.makeLocation(l.getSystemId(), l.getLineNumber(), l.getColumnNumber());
/*     */     }
/*     */ 
/*     */     
/*     */     private ParsedNameClass parseNameClass(NameClass name) {
/* 105 */       return (ParsedNameClass)name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ParsedPattern onAttribute(DAttributePattern p) {
/* 111 */       return this.sb.makeAttribute(
/* 112 */           parseNameClass(p.getName()), p
/* 113 */           .getChild().<ParsedPattern>accept(this), 
/* 114 */           parseLocation(p), 
/* 115 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onChoice(DChoicePattern p) {
/* 119 */       List<ParsedPattern> kids = new ArrayList<>();
/* 120 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 121 */         kids.add(c.<ParsedPattern>accept(this)); 
/* 122 */       return this.sb.makeChoice(kids, parseLocation(p), null);
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern onData(DDataPattern p) {
/* 127 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onElement(DElementPattern p) {
/* 131 */       return this.sb.makeElement(
/* 132 */           parseNameClass(p.getName()), p
/* 133 */           .getChild().<ParsedPattern>accept(this), 
/* 134 */           parseLocation(p), 
/* 135 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onEmpty(DEmptyPattern p) {
/* 139 */       return this.sb.makeEmpty(
/* 140 */           parseLocation(p), 
/* 141 */           parseAnnotation(p));
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern onGrammar(DGrammarPattern p) {
/* 146 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onGroup(DGroupPattern p) {
/* 150 */       List<ParsedPattern> kids = new ArrayList<>();
/* 151 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 152 */         kids.add(c.<ParsedPattern>accept(this)); 
/* 153 */       return this.sb.makeGroup(kids, parseLocation(p), null);
/*     */     }
/*     */     
/*     */     public ParsedPattern onInterleave(DInterleavePattern p) {
/* 157 */       List<ParsedPattern> kids = new ArrayList<>();
/* 158 */       for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 159 */         kids.add(c.<ParsedPattern>accept(this)); 
/* 160 */       return this.sb.makeInterleave(kids, parseLocation(p), null);
/*     */     }
/*     */     
/*     */     public ParsedPattern onList(DListPattern p) {
/* 164 */       return this.sb.makeList(p
/* 165 */           .getChild().<ParsedPattern>accept(this), 
/* 166 */           parseLocation(p), 
/* 167 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onMixed(DMixedPattern p) {
/* 171 */       return this.sb.makeMixed(p
/* 172 */           .getChild().<ParsedPattern>accept(this), 
/* 173 */           parseLocation(p), 
/* 174 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onNotAllowed(DNotAllowedPattern p) {
/* 178 */       return this.sb.makeNotAllowed(
/* 179 */           parseLocation(p), 
/* 180 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onOneOrMore(DOneOrMorePattern p) {
/* 184 */       return this.sb.makeOneOrMore(p
/* 185 */           .getChild().<ParsedPattern>accept(this), 
/* 186 */           parseLocation(p), 
/* 187 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onOptional(DOptionalPattern p) {
/* 191 */       return this.sb.makeOptional(p
/* 192 */           .getChild().<ParsedPattern>accept(this), 
/* 193 */           parseLocation(p), 
/* 194 */           parseAnnotation(p));
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern onRef(DRefPattern p) {
/* 199 */       return null;
/*     */     }
/*     */     
/*     */     public ParsedPattern onText(DTextPattern p) {
/* 203 */       return this.sb.makeText(
/* 204 */           parseLocation(p), 
/* 205 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onValue(DValuePattern p) {
/* 209 */       return this.sb.makeValue(p
/* 210 */           .getDatatypeLibrary(), p
/* 211 */           .getType(), p
/* 212 */           .getValue(), p
/* 213 */           .getContext(), p
/* 214 */           .getNs(), 
/* 215 */           parseLocation(p), 
/* 216 */           parseAnnotation(p));
/*     */     }
/*     */     
/*     */     public ParsedPattern onZeroOrMore(DZeroOrMorePattern p) {
/* 220 */       return this.sb.makeZeroOrMore(p
/* 221 */           .getChild().<ParsedPattern>accept(this), 
/* 222 */           parseLocation(p), 
/* 223 */           parseAnnotation(p));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\PatternParseable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */