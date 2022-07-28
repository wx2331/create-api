/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForkingFilter
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private ContentHandler side;
/*     */   private int depth;
/*  65 */   private final ArrayList<String> namespaces = new ArrayList<>();
/*     */   
/*     */   private Locator loc;
/*     */ 
/*     */   
/*     */   public ForkingFilter() {}
/*     */   
/*     */   public ForkingFilter(ContentHandler next) {
/*  73 */     setContentHandler(next);
/*     */   }
/*     */   
/*     */   public ContentHandler getSideHandler() {
/*  77 */     return this.side;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  82 */     super.setDocumentLocator(locator);
/*  83 */     this.loc = locator;
/*     */   }
/*     */   
/*     */   public Locator getDocumentLocator() {
/*  87 */     return this.loc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  92 */     reset();
/*  93 */     super.startDocument();
/*     */   }
/*     */   
/*     */   private void reset() {
/*  97 */     this.namespaces.clear();
/*  98 */     this.side = null;
/*  99 */     this.depth = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 104 */     this.loc = null;
/* 105 */     reset();
/* 106 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 111 */     if ("http://www.w3.org/XML/1998/namespace".equals(uri))
/* 112 */       return;  if (this.side != null)
/* 113 */       this.side.startPrefixMapping(prefix, uri); 
/* 114 */     this.namespaces.add(prefix);
/* 115 */     this.namespaces.add(uri);
/* 116 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 121 */     if ("xml".equals(prefix))
/* 122 */       return;  if (this.side != null)
/* 123 */       this.side.endPrefixMapping(prefix); 
/* 124 */     super.endPrefixMapping(prefix);
/* 125 */     this.namespaces.remove(this.namespaces.size() - 1);
/* 126 */     this.namespaces.remove(this.namespaces.size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 131 */     if (this.side != null) {
/* 132 */       this.side.startElement(uri, localName, qName, atts);
/* 133 */       this.depth++;
/*     */     } 
/* 135 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startForking(String uri, String localName, String qName, Attributes atts, ContentHandler side) throws SAXException {
/* 142 */     if (this.side != null) throw new IllegalStateException();
/*     */     
/* 144 */     this.side = side;
/* 145 */     this.depth = 1;
/* 146 */     side.setDocumentLocator(this.loc);
/* 147 */     side.startDocument();
/* 148 */     for (int i = 0; i < this.namespaces.size(); i += 2)
/* 149 */       side.startPrefixMapping(this.namespaces.get(i), this.namespaces.get(i + 1)); 
/* 150 */     side.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 155 */     if (this.side != null) {
/* 156 */       this.side.endElement(uri, localName, qName);
/* 157 */       this.depth--;
/* 158 */       if (this.depth == 0) {
/* 159 */         for (int i = this.namespaces.size() - 2; i >= 0; i -= 2)
/* 160 */           this.side.endPrefixMapping(this.namespaces.get(i)); 
/* 161 */         this.side.endDocument();
/* 162 */         this.side = null;
/*     */       } 
/*     */     } 
/* 165 */     super.endElement(uri, localName, qName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 170 */     if (this.side != null)
/* 171 */       this.side.characters(ch, start, length); 
/* 172 */     super.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 177 */     if (this.side != null)
/* 178 */       this.side.ignorableWhitespace(ch, start, length); 
/* 179 */     super.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\ForkingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */