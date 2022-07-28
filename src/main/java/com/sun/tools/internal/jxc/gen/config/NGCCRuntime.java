/*     */ package com.sun.tools.internal.jxc.gen.config;public class NGCCRuntime implements ContentHandler, NGCCEventSource { private Locator locator; private final Stack attStack; private AttributesImpl currentAtts; private StringBuffer text; private NGCCEventReceiver currentHandler; static final String IMPOSSIBLE = "\000"; private ContentHandler redirect;
/*     */   private int redirectionDepth;
/*     */   private final ArrayList namespaces;
/*     */   private int nsEffectivePtr;
/*     */   private final Stack nsEffectiveStack;
/*     */   private int indent;
/*     */   private boolean needIndent;
/*     */   
/*     */   public void setRootHandler(NGCCHandler rootHandler) {
/*     */     if (this.currentHandler != null)
/*     */       throw new IllegalStateException(); 
/*     */     this.currentHandler = rootHandler;
/*     */   }
/*     */   
/*     */   public void reset() {
/*     */     this.attStack.clear();
/*     */     this.currentAtts = null;
/*     */     this.currentHandler = null;
/*     */     this.indent = 0;
/*     */     this.locator = null;
/*     */     this.namespaces.clear();
/*     */     this.needIndent = true;
/*     */     this.redirect = null;
/*     */     this.redirectionDepth = 0;
/*     */     this.text = new StringBuffer();
/*     */     this.attStack.push(new AttributesImpl());
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator _loc) {
/*     */     this.locator = _loc;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/*     */     return this.locator;
/*     */   }
/*     */   
/*     */   public Attributes getCurrentAttributes() {
/*     */     return this.currentAtts;
/*     */   }
/*     */   
/*     */   public int replace(NGCCEventReceiver o, NGCCEventReceiver n) {
/*     */     if (o != this.currentHandler)
/*     */       throw new IllegalStateException(); 
/*     */     this.currentHandler = n;
/*     */     return 0;
/*     */   }
/*     */   
/*     */   private void processPendingText(boolean ignorable) throws SAXException {
/*     */     if (!ignorable || this.text.toString().trim().length() != 0)
/*     */       this.currentHandler.text(this.text.toString()); 
/*     */     if (this.text.length() > 1024) {
/*     */       this.text = new StringBuffer();
/*     */     } else {
/*     */       this.text.setLength(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void processList(String str) throws SAXException {
/*     */     StringTokenizer t = new StringTokenizer(str, " \t\r\n");
/*     */     while (t.hasMoreTokens())
/*     */       this.currentHandler.text(t.nextToken()); 
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localname, String qname, Attributes atts) throws SAXException {
/*     */     if (this.redirect != null) {
/*     */       this.redirect.startElement(uri, localname, qname, atts);
/*     */       this.redirectionDepth++;
/*     */     } else {
/*     */       processPendingText(true);
/*     */       this.currentHandler.enterElement(uri, localname, qname, atts);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts) throws SAXException {
/*     */     this.attStack.push(this.currentAtts = new AttributesImpl(atts));
/*     */     this.nsEffectiveStack.push(new Integer(this.nsEffectivePtr));
/*     */     this.nsEffectivePtr = this.namespaces.size();
/*     */   }
/*     */   
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException {
/*     */     this.attStack.pop();
/*     */     if (this.attStack.isEmpty()) {
/*     */       this.currentAtts = null;
/*     */     } else {
/*     */       this.currentAtts = this.attStack.peek();
/*     */     } 
/*     */     this.nsEffectivePtr = ((Integer)this.nsEffectiveStack.pop()).intValue();
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localname, String qname) throws SAXException {
/*     */     if (this.redirect != null) {
/*     */       this.redirect.endElement(uri, localname, qname);
/*     */       this.redirectionDepth--;
/*     */       if (this.redirectionDepth != 0)
/*     */         return; 
/*     */       for (int i = 0; i < this.namespaces.size(); i += 2)
/*     */         this.redirect.endPrefixMapping(this.namespaces.get(i)); 
/*     */       this.redirect.endDocument();
/*     */       this.redirect = null;
/*     */     } 
/*     */     processPendingText(false);
/*     */     this.currentHandler.leaveElement(uri, localname, qname);
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/*     */     if (this.redirect != null) {
/*     */       this.redirect.characters(ch, start, length);
/*     */     } else {
/*     */       this.text.append(ch, start, length);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/*     */     if (this.redirect != null) {
/*     */       this.redirect.ignorableWhitespace(ch, start, length);
/*     */     } else {
/*     */       this.text.append(ch, start, length);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttributeIndex(String uri, String localname) {
/*     */     return this.currentAtts.getIndex(uri, localname);
/*     */   }
/*     */   
/* 125 */   public NGCCRuntime() { this.attStack = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     this.text = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     this.redirect = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     this.redirectionDepth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     this.namespaces = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 500 */     this.nsEffectivePtr = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     this.nsEffectiveStack = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 539 */     this.indent = 0;
/* 540 */     this.needIndent = true; reset(); }
/*     */   public void consumeAttribute(int index) throws SAXException { String uri = this.currentAtts.getURI(index); String local = this.currentAtts.getLocalName(index); String qname = this.currentAtts.getQName(index); String value = this.currentAtts.getValue(index); this.currentAtts.removeAttribute(index); this.currentHandler.enterAttribute(uri, local, qname); this.currentHandler.text(value); this.currentHandler.leaveAttribute(uri, local, qname); }
/* 542 */   public void startPrefixMapping(String prefix, String uri) throws SAXException { if (this.redirect != null) { this.redirect.startPrefixMapping(prefix, uri); } else { this.namespaces.add(prefix); this.namespaces.add(uri); }  } public void endPrefixMapping(String prefix) throws SAXException { if (this.redirect != null) { this.redirect.endPrefixMapping(prefix); } else { this.namespaces.remove(this.namespaces.size() - 1); this.namespaces.remove(this.namespaces.size() - 1); }  } public void skippedEntity(String name) throws SAXException { if (this.redirect != null) this.redirect.skippedEntity(name);  } public void processingInstruction(String target, String data) throws SAXException { if (this.redirect != null) this.redirect.processingInstruction(target, data);  } public void endDocument() throws SAXException { this.currentHandler.leaveElement("\000", "\000", "\000"); reset(); } public void startDocument() {} private void printIndent() { for (int i = 0; i < this.indent; i++)
/* 543 */       System.out.print("  ");  }
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname) throws SAXException { this.currentHandler.enterAttribute(uri, local, qname); }
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts) throws SAXException { this.currentHandler.enterElement(uri, local, qname, atts); }
/* 546 */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname) throws SAXException { this.currentHandler.leaveAttribute(uri, local, qname); } public void sendLeaveElement(int threadId, String uri, String local, String qname) throws SAXException { this.currentHandler.leaveElement(uri, local, qname); } public void sendText(int threadId, String value) throws SAXException { this.currentHandler.text(value); } public void redirectSubtree(ContentHandler child, String uri, String local, String qname) throws SAXException { this.redirect = child; this.redirect.setDocumentLocator(this.locator); this.redirect.startDocument(); for (int i = 0; i < this.namespaces.size(); i += 2) this.redirect.startPrefixMapping(this.namespaces.get(i), this.namespaces.get(i + 1));  this.redirect.startElement(uri, local, qname, this.currentAtts); this.redirectionDepth = 1; } public String resolveNamespacePrefix(String prefix) { for (int i = this.nsEffectivePtr - 2; i >= 0; i -= 2) { if (this.namespaces.get(i).equals(prefix)) return this.namespaces.get(i + 1);  }  if (prefix.equals("")) return "";  if (prefix.equals("xml")) return "http://www.w3.org/XML/1998/namespace";  return null; } protected void unexpectedX(String token) throws SAXException { throw new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, new Integer(getLocator().getLineNumber()), new Integer(getLocator().getColumnNumber()) }), getLocator()); } public void trace(String s) { if (this.needIndent) {
/* 547 */       this.needIndent = false;
/* 548 */       printIndent();
/*     */     } 
/* 550 */     System.out.print(s); }
/*     */   
/*     */   public void traceln(String s) {
/* 553 */     trace(s);
/* 554 */     trace("\n");
/* 555 */     this.needIndent = true;
/*     */   } }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\NGCCRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */