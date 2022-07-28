/*     */ package com.sun.xml.internal.xsom;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.internal.xsom.impl.scd.ParseException;
/*     */ import com.sun.xml.internal.xsom.impl.scd.SCDImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.SCDParser;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Step;
/*     */ import com.sun.xml.internal.xsom.impl.scd.TokenMgrError;
/*     */ import com.sun.xml.internal.xsom.util.DeferedCollection;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
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
/*     */ public abstract class SCD
/*     */ {
/*     */   public static SCD create(String path, NamespaceContext nsContext) throws ParseException {
/*     */     try {
/*  75 */       SCDParser p = new SCDParser(path, nsContext);
/*  76 */       List<?> list = p.RelativeSchemaComponentPath();
/*  77 */       return (SCD)new SCDImpl(path, list.<Step>toArray(new Step[list.size()]));
/*  78 */     } catch (TokenMgrError e) {
/*  79 */       throw setCause(new ParseException(e.getMessage(), -1), e);
/*  80 */     } catch (ParseException e) {
/*  81 */       throw setCause(new ParseException(e.getMessage(), e.currentToken.beginColumn), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ParseException setCause(ParseException e, Throwable x) {
/*  86 */     e.initCause(x);
/*  87 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Collection<XSComponent> select(XSComponent contextNode) {
/*  98 */     return (Collection<XSComponent>)new DeferedCollection(select(Iterators.singleton(contextNode)));
/*     */   }
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
/*     */   public final Collection<XSComponent> select(XSSchemaSet contextNode) {
/* 113 */     return select((Collection)contextNode.getSchemas());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XSComponent selectSingle(XSComponent contextNode) {
/* 125 */     Iterator<XSComponent> r = select(Iterators.singleton(contextNode));
/* 126 */     if (r.hasNext()) return r.next(); 
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XSComponent selectSingle(XSSchemaSet contextNode) {
/* 139 */     Iterator<XSComponent> r = select((Iterator)contextNode.iterateSchema());
/* 140 */     if (r.hasNext()) return r.next(); 
/* 141 */     return null;
/*     */   }
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
/*     */   public abstract Iterator<XSComponent> select(Iterator<? extends XSComponent> paramIterator);
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
/*     */   public final Collection<XSComponent> select(Collection<? extends XSComponent> contextNodes) {
/* 169 */     return (Collection<XSComponent>)new DeferedCollection(select(contextNodes.iterator()));
/*     */   }
/*     */   
/*     */   public abstract String toString();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\SCD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */