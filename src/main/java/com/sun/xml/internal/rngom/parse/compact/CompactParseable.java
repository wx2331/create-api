/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import com.sun.xml.internal.rngom.xml.util.EncodingMap;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class CompactParseable
/*     */   implements Parseable
/*     */ {
/*     */   private final InputSource in;
/*     */   private final ErrorHandler eh;
/*     */   
/*     */   public CompactParseable(InputSource in, ErrorHandler eh) {
/*  74 */     this.in = in;
/*  75 */     this.eh = eh;
/*     */   }
/*     */   
/*     */   public ParsedPattern parse(SchemaBuilder sb) throws BuildException, IllegalSchemaException {
/*  79 */     ParsedPattern p = (new CompactSyntax(this, makeReader(this.in), this.in.getSystemId(), sb, this.eh, "")).parse(null);
/*  80 */     return sb.expandPattern(p);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern parseInclude(String uri, SchemaBuilder sb, IncludedGrammar g, String inheritedNs) throws BuildException, IllegalSchemaException {
/*  85 */     InputSource tem = new InputSource(uri);
/*  86 */     tem.setEncoding(this.in.getEncoding());
/*  87 */     return (new CompactSyntax(this, makeReader(tem), uri, sb, this.eh, inheritedNs)).parseInclude(g);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern parseExternal(String uri, SchemaBuilder sb, Scope scope, String inheritedNs) throws BuildException, IllegalSchemaException {
/*  92 */     InputSource tem = new InputSource(uri);
/*  93 */     tem.setEncoding(this.in.getEncoding());
/*  94 */     return (new CompactSyntax(this, makeReader(tem), uri, sb, this.eh, inheritedNs)).parse(scope);
/*     */   }
/*     */   
/*  97 */   private static final String UTF8 = EncodingMap.getJavaName("UTF-8");
/*  98 */   private static final String UTF16 = EncodingMap.getJavaName("UTF-16");
/*     */   
/*     */   private static Reader makeReader(InputSource is) throws BuildException {
/*     */     try {
/* 102 */       Reader r = is.getCharacterStream();
/* 103 */       if (r == null) {
/* 104 */         InputStream in = is.getByteStream();
/* 105 */         if (in == null) {
/* 106 */           String systemId = is.getSystemId();
/* 107 */           in = (new URL(systemId)).openStream();
/*     */         } 
/* 109 */         String encoding = is.getEncoding();
/* 110 */         if (encoding == null) {
/* 111 */           PushbackInputStream pb = new PushbackInputStream(in, 2);
/* 112 */           encoding = detectEncoding(pb);
/* 113 */           in = pb;
/*     */         } 
/* 115 */         r = new InputStreamReader(in, encoding);
/*     */       } 
/* 117 */       return r;
/*     */     }
/* 119 */     catch (IOException e) {
/* 120 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String detectEncoding(PushbackInputStream in) throws IOException {
/* 125 */     String encoding = UTF8;
/* 126 */     int b1 = in.read();
/* 127 */     if (b1 != -1) {
/* 128 */       int b2 = in.read();
/* 129 */       if (b2 != -1) {
/* 130 */         in.unread(b2);
/* 131 */         if ((b1 == 255 && b2 == 254) || (b1 == 254 && b2 == 255))
/* 132 */           encoding = UTF16; 
/*     */       } 
/* 134 */       in.unread(b1);
/*     */     } 
/* 136 */     return encoding;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\CompactParseable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */