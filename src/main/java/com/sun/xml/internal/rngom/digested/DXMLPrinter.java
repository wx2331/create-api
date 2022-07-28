/*     */ package com.sun.xml.internal.rngom.digested;
/*     */
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.util.CheckingSchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.nc.NameClassVisitor;
/*     */ import com.sun.xml.internal.rngom.nc.SimpleNameClass;
/*     */ import com.sun.xml.internal.rngom.parse.compact.CompactParseable;
/*     */ import com.sun.xml.internal.rngom.parse.xml.SAXParseable;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class DXMLPrinter
/*     */ {
/*     */   protected XMLStreamWriter out;
/*  83 */   protected String indentStep = "\t";
/*  84 */   protected String newLine = System.getProperty("line.separator");
/*     */
/*     */   protected int indent;
/*     */
/*     */   protected boolean afterEnd = false;
/*     */
/*     */   protected DXMLPrinterVisitor visitor;
/*     */   protected NameClassXMLPrinterVisitor ncVisitor;
/*     */   protected DOMPrinter domPrinter;
/*     */
/*     */   public DXMLPrinter(XMLStreamWriter out) {
/*  95 */     this.out = out;
/*  96 */     this.visitor = new DXMLPrinterVisitor();
/*  97 */     this.ncVisitor = new NameClassXMLPrinterVisitor();
/*  98 */     this.domPrinter = new DOMPrinter(out);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printDocument(DGrammarPattern grammar) throws XMLStreamException {
/*     */     try {
/* 109 */       this.visitor.startDocument();
/* 110 */       this.visitor.on(grammar);
/* 111 */       this.visitor.endDocument();
/* 112 */     } catch (XMLWriterException e) {
/* 113 */       if (e.getCause() instanceof XMLStreamException) {
/* 114 */         throw (XMLStreamException)e.getCause();
/*     */       }
/* 116 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void print(DPattern pattern) throws XMLStreamException {
/*     */     try {
/* 128 */       pattern.accept(this.visitor);
/* 129 */     } catch (XMLWriterException e) {
/* 130 */       if (e.getCause() instanceof XMLStreamException) {
/* 131 */         throw (XMLStreamException)e.getCause();
/*     */       }
/* 133 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void print(NameClass nc) throws XMLStreamException {
/*     */     try {
/* 145 */       nc.accept(this.ncVisitor);
/* 146 */     } catch (XMLWriterException e) {
/* 147 */       if (e.getCause() instanceof XMLStreamException) {
/* 148 */         throw (XMLStreamException)e.getCause();
/*     */       }
/* 150 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public void print(Node node) throws XMLStreamException {
/* 156 */     this.domPrinter.print(node);
/*     */   }
/*     */
/*     */   protected class XMLWriterException extends RuntimeException {
/*     */     protected XMLWriterException(Throwable cause) {
/* 161 */       super(cause);
/*     */     }
/*     */   }
/*     */
/*     */   protected class XMLWriter {
/*     */     protected void newLine() {
/*     */       try {
/* 168 */         DXMLPrinter.this.out.writeCharacters(DXMLPrinter.this.newLine);
/* 169 */       } catch (XMLStreamException e) {
/* 170 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     protected void indent() {
/*     */       try {
/* 176 */         for (int i = 0; i < DXMLPrinter.this.indent; i++) {
/* 177 */           DXMLPrinter.this.out.writeCharacters(DXMLPrinter.this.indentStep);
/*     */         }
/* 179 */       } catch (XMLStreamException e) {
/* 180 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void startDocument() {
/*     */       try {
/* 186 */         DXMLPrinter.this.out.writeStartDocument();
/* 187 */       } catch (XMLStreamException e) {
/* 188 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void endDocument() {
/*     */       try {
/* 194 */         DXMLPrinter.this.out.writeEndDocument();
/* 195 */       } catch (XMLStreamException e) {
/* 196 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public final void start(String element) {
/*     */       try {
/* 202 */         newLine();
/* 203 */         indent();
/* 204 */         DXMLPrinter.this.out.writeStartElement(element);
/* 205 */         DXMLPrinter.this.indent++;
/* 206 */         DXMLPrinter.this.afterEnd = false;
/* 207 */       } catch (XMLStreamException e) {
/* 208 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void end() {
/*     */       try {
/* 214 */         DXMLPrinter.this.indent--;
/* 215 */         if (DXMLPrinter.this.afterEnd) {
/* 216 */           newLine();
/* 217 */           indent();
/*     */         }
/* 219 */         DXMLPrinter.this.out.writeEndElement();
/* 220 */         DXMLPrinter.this.afterEnd = true;
/* 221 */       } catch (XMLStreamException e) {
/* 222 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void attr(String prefix, String ns, String name, String value) {
/*     */       try {
/* 228 */         DXMLPrinter.this.out.writeAttribute(prefix, ns, name, value);
/* 229 */       } catch (XMLStreamException e) {
/* 230 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void attr(String name, String value) {
/*     */       try {
/* 236 */         DXMLPrinter.this.out.writeAttribute(name, value);
/* 237 */       } catch (XMLStreamException e) {
/* 238 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void ns(String prefix, String uri) {
/*     */       try {
/* 244 */         DXMLPrinter.this.out.writeNamespace(prefix, uri);
/* 245 */       } catch (XMLStreamException e) {
/* 246 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */
/*     */     public void body(String text) {
/*     */       try {
/* 252 */         DXMLPrinter.this.out.writeCharacters(text);
/* 253 */         DXMLPrinter.this.afterEnd = false;
/* 254 */       } catch (XMLStreamException e) {
/* 255 */         throw new XMLWriterException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   protected class DXMLPrinterVisitor extends XMLWriter implements DPatternVisitor<Void> {
/*     */     protected void on(DPattern p) {
/* 262 */       p.accept(this);
/*     */     }
/*     */
/*     */     protected void unwrapGroup(DPattern p) {
/* 266 */       if (p instanceof DGroupPattern && p.getAnnotation() == DAnnotation.EMPTY) {
/* 267 */         for (DPattern d : p) {
/* 268 */           on(d);
/*     */         }
/*     */       } else {
/* 271 */         on(p);
/*     */       }
/*     */     }
/*     */
/*     */     protected void unwrapChoice(DPattern p) {
/* 276 */       if (p instanceof DChoicePattern && p.getAnnotation() == DAnnotation.EMPTY) {
/* 277 */         for (DPattern d : p) {
/* 278 */           on(d);
/*     */         }
/*     */       } else {
/* 281 */         on(p);
/*     */       }
/*     */     }
/*     */
/*     */     protected void on(NameClass nc) {
/* 286 */       if (nc instanceof SimpleNameClass) {
/* 287 */         QName qname = ((SimpleNameClass)nc).name;
/* 288 */         String name = qname.getLocalPart();
/* 289 */         if (!qname.getPrefix().equals("")) name = qname.getPrefix() + ":";
/* 290 */         attr("name", name);
/*     */       } else {
/* 292 */         nc.accept(DXMLPrinter.this.ncVisitor);
/*     */       }
/*     */     }
/*     */
/*     */     protected void on(DAnnotation ann) {
/* 297 */       if (ann == DAnnotation.EMPTY)
/* 298 */         return;  for (DAnnotation.Attribute attr : ann.getAttributes().values()) {
/* 299 */         attr(attr.getPrefix(), attr.getNs(), attr.getLocalName(), attr.getValue());
/*     */       }
/* 301 */       for (Element elem : ann.getChildren()) {
/*     */         try {
/* 303 */           newLine();
/* 304 */           indent();
/* 305 */           DXMLPrinter.this.print(elem);
/*     */         }
/* 307 */         catch (XMLStreamException e) {
/* 308 */           throw new XMLWriterException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     public Void onAttribute(DAttributePattern p) {
/* 314 */       start("attribute");
/* 315 */       on(p.getName());
/* 316 */       on(p.getAnnotation());
/* 317 */       DPattern child = p.getChild();
/*     */
/* 319 */       if (!(child instanceof DTextPattern)) {
/* 320 */         on(p.getChild());
/*     */       }
/* 322 */       end();
/* 323 */       return null;
/*     */     }
/*     */
/*     */     public Void onChoice(DChoicePattern p) {
/* 327 */       start("choice");
/* 328 */       on(p.getAnnotation());
/* 329 */       for (DPattern d : p) {
/* 330 */         on(d);
/*     */       }
/* 332 */       end();
/* 333 */       return null;
/*     */     }
/*     */
/*     */     public Void onData(DDataPattern p) {
/* 337 */       List<DDataPattern.Param> params = p.getParams();
/* 338 */       DPattern except = p.getExcept();
/* 339 */       start("data");
/* 340 */       attr("datatypeLibrary", p.getDatatypeLibrary());
/* 341 */       attr("type", p.getType());
/* 342 */       on(p.getAnnotation());
/* 343 */       for (DDataPattern.Param param : params) {
/* 344 */         start("param");
/* 345 */         attr("ns", param.getNs());
/* 346 */         attr("name", param.getName());
/* 347 */         body(param.getValue());
/* 348 */         end();
/*     */       }
/* 350 */       if (except != null) {
/* 351 */         start("except");
/* 352 */         unwrapChoice(except);
/* 353 */         end();
/*     */       }
/* 355 */       end();
/* 356 */       return null;
/*     */     }
/*     */
/*     */     public Void onElement(DElementPattern p) {
/* 360 */       start("element");
/* 361 */       on(p.getName());
/* 362 */       on(p.getAnnotation());
/* 363 */       unwrapGroup(p.getChild());
/* 364 */       end();
/* 365 */       return null;
/*     */     }
/*     */
/*     */     public Void onEmpty(DEmptyPattern p) {
/* 369 */       start("empty");
/* 370 */       on(p.getAnnotation());
/* 371 */       end();
/* 372 */       return null;
/*     */     }
/*     */
/*     */     public Void onGrammar(DGrammarPattern p) {
/* 376 */       start("grammar");
/* 377 */       ns(null, "http://relaxng.org/ns/structure/1.0");
/* 378 */       on(p.getAnnotation());
/* 379 */       start("start");
/* 380 */       on(p.getStart());
/* 381 */       end();
/* 382 */       for (DDefine d : p) {
/* 383 */         start("define");
/* 384 */         attr("name", d.getName());
/* 385 */         on(d.getAnnotation());
/* 386 */         unwrapGroup(d.getPattern());
/* 387 */         end();
/*     */       }
/* 389 */       end();
/* 390 */       return null;
/*     */     }
/*     */
/*     */     public Void onGroup(DGroupPattern p) {
/* 394 */       start("group");
/* 395 */       on(p.getAnnotation());
/* 396 */       for (DPattern d : p) {
/* 397 */         on(d);
/*     */       }
/* 399 */       end();
/* 400 */       return null;
/*     */     }
/*     */
/*     */     public Void onInterleave(DInterleavePattern p) {
/* 404 */       start("interleave");
/* 405 */       on(p.getAnnotation());
/* 406 */       for (DPattern d : p) {
/* 407 */         on(d);
/*     */       }
/* 409 */       end();
/* 410 */       return null;
/*     */     }
/*     */
/*     */     public Void onList(DListPattern p) {
/* 414 */       start("list");
/* 415 */       on(p.getAnnotation());
/* 416 */       unwrapGroup(p.getChild());
/* 417 */       end();
/* 418 */       return null;
/*     */     }
/*     */
/*     */     public Void onMixed(DMixedPattern p) {
/* 422 */       start("mixed");
/* 423 */       on(p.getAnnotation());
/* 424 */       unwrapGroup(p.getChild());
/* 425 */       end();
/* 426 */       return null;
/*     */     }
/*     */
/*     */     public Void onNotAllowed(DNotAllowedPattern p) {
/* 430 */       start("notAllowed");
/* 431 */       on(p.getAnnotation());
/* 432 */       end();
/* 433 */       return null;
/*     */     }
/*     */
/*     */     public Void onOneOrMore(DOneOrMorePattern p) {
/* 437 */       start("oneOrMore");
/* 438 */       on(p.getAnnotation());
/* 439 */       unwrapGroup(p.getChild());
/* 440 */       end();
/* 441 */       return null;
/*     */     }
/*     */
/*     */     public Void onOptional(DOptionalPattern p) {
/* 445 */       start("optional");
/* 446 */       on(p.getAnnotation());
/* 447 */       unwrapGroup(p.getChild());
/* 448 */       end();
/* 449 */       return null;
/*     */     }
/*     */
/*     */     public Void onRef(DRefPattern p) {
/* 453 */       start("ref");
/* 454 */       attr("name", p.getName());
/* 455 */       on(p.getAnnotation());
/* 456 */       end();
/* 457 */       return null;
/*     */     }
/*     */
/*     */     public Void onText(DTextPattern p) {
/* 461 */       start("text");
/* 462 */       on(p.getAnnotation());
/* 463 */       end();
/* 464 */       return null;
/*     */     }
/*     */
/*     */     public Void onValue(DValuePattern p) {
/* 468 */       start("value");
/* 469 */       if (!p.getNs().equals("")) attr("ns", p.getNs());
/* 470 */       attr("datatypeLibrary", p.getDatatypeLibrary());
/* 471 */       attr("type", p.getType());
/* 472 */       on(p.getAnnotation());
/* 473 */       body(p.getValue());
/* 474 */       end();
/* 475 */       return null;
/*     */     }
/*     */
/*     */     public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 479 */       start("zeroOrMore");
/* 480 */       on(p.getAnnotation());
/* 481 */       unwrapGroup(p.getChild());
/* 482 */       end();
/* 483 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */   protected class NameClassXMLPrinterVisitor
/*     */     extends XMLWriter implements NameClassVisitor<Void> {
/*     */     public Void visitChoice(NameClass nc1, NameClass nc2) {
/* 490 */       start("choice");
/* 491 */       nc1.accept(this);
/* 492 */       nc2.accept(this);
/* 493 */       end();
/* 494 */       return null;
/*     */     }
/*     */
/*     */     public Void visitNsName(String ns) {
/* 498 */       start("nsName");
/* 499 */       attr("ns", ns);
/* 500 */       end();
/* 501 */       return null;
/*     */     }
/*     */
/*     */     public Void visitNsNameExcept(String ns, NameClass nc) {
/* 505 */       start("nsName");
/* 506 */       attr("ns", ns);
/* 507 */       start("except");
/* 508 */       nc.accept(this);
/* 509 */       end();
/* 510 */       end();
/* 511 */       return null;
/*     */     }
/*     */
/*     */     public Void visitAnyName() {
/* 515 */       start("anyName");
/* 516 */       end();
/* 517 */       return null;
/*     */     }
/*     */
/*     */     public Void visitAnyNameExcept(NameClass nc) {
/* 521 */       start("anyName");
/* 522 */       start("except");
/* 523 */       nc.accept(this);
/* 524 */       end();
/* 525 */       end();
/* 526 */       return null;
/*     */     }
/*     */
/*     */     public Void visitName(QName name) {
/* 530 */       start("name");
/* 531 */       if (!name.getPrefix().equals("")) {
/* 532 */         body(name.getPrefix() + ":");
/*     */       }
/* 534 */       body(name.getLocalPart());
/* 535 */       end();
/* 536 */       return null;
/*     */     }
/*     */
/*     */     public Void visitNull() {
/* 540 */       throw new UnsupportedOperationException("visitNull");
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static void main(String[] args) throws Exception {
/*     */     CompactParseable compactParseable;
/* 547 */     ErrorHandler eh = new DefaultHandler()
/*     */       {
/*     */         public void error(SAXParseException e) throws SAXException {
/* 550 */           throw e;
/*     */         }
/*     */       };
/*     */
/*     */
/* 555 */     String path = (new File(args[0])).toURL().toString();
/* 556 */     if (args[0].endsWith(".rng")) {
/* 557 */       SAXParseable sAXParseable = new SAXParseable(new InputSource(path), eh);
/*     */     } else {
/* 559 */       compactParseable = new CompactParseable(new InputSource(path), eh);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 566 */     CheckingSchemaBuilder checkingSchemaBuilder = new CheckingSchemaBuilder(new DSchemaBuilderImpl(), eh);
/*     */
/*     */     try {
/* 569 */       DGrammarPattern grammar = (DGrammarPattern)compactParseable.parse((SchemaBuilder)checkingSchemaBuilder);
/* 570 */       OutputStream out = new FileOutputStream(args[1]);
/* 571 */       XMLOutputFactory factory = XMLOutputFactory.newInstance();
/* 572 */       factory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
/* 573 */       XMLStreamWriter output = factory.createXMLStreamWriter(out);
/* 574 */       DXMLPrinter printer = new DXMLPrinter(output);
/* 575 */       printer.printDocument(grammar);
/* 576 */       output.close();
/* 577 */       out.close();
/* 578 */     } catch (BuildException e) {
/* 579 */       if (e.getCause() instanceof SAXParseException) {
/* 580 */         SAXParseException se = (SAXParseException)e.getCause();
/* 581 */         System.out.println("(" + se
/* 582 */             .getLineNumber() + "," + se
/*     */
/* 584 */             .getColumnNumber() + "): " + se
/*     */
/* 586 */             .getMessage());
/*     */
/*     */
/*     */         return;
/*     */       }
/*     */
/* 592 */       if (e.getCause() instanceof SAXException) {
/* 593 */         SAXException se = (SAXException)e.getCause();
/* 594 */         if (se.getException() != null)
/* 595 */           se.getException().printStackTrace();
/*     */       }
/* 597 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DXMLPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
