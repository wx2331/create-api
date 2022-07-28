/*     */ package com.sun.xml.internal.rngom.ast.util;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.binary.SchemaBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.binary.SchemaPatternBuilder;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.host.ParsedPatternHost;
/*     */ import com.sun.xml.internal.rngom.parse.host.SchemaBuilderHost;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ public class CheckingSchemaBuilder
/*     */   extends SchemaBuilderHost
/*     */ {
/*     */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh) {
/*  90 */     super((SchemaBuilder)new SchemaBuilderImpl(eh), sb);
/*     */   }
/*     */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh, DatatypeLibraryFactory dlf) {
/*  93 */     super((SchemaBuilder)new SchemaBuilderImpl(eh, dlf, new SchemaPatternBuilder()), sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern expandPattern(ParsedPattern p) throws BuildException, IllegalSchemaException {
/* 100 */     ParsedPatternHost r = (ParsedPatternHost)super.expandPattern(p);
/* 101 */     return r.rhs;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\as\\util\CheckingSchemaBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */