/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NGCCHandler
/*     */   implements NGCCEventReceiver
/*     */ {
/*     */   protected final NGCCHandler _parent;
/*     */   protected final NGCCEventSource _source;
/*     */   protected final int _cookie;
/*     */   
/*     */   protected NGCCHandler(NGCCEventSource source, NGCCHandler parent, int parentCookie) {
/*  40 */     this._parent = parent;
/*  41 */     this._source = source;
/*  42 */     this._cookie = parentCookie;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract NGCCRuntime getRuntime();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onChildCompleted(Object paramObject, int paramInt, boolean paramBoolean) throws SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnChildFromEnterElement(NGCCEventReceiver child, String uri, String localname, String qname, Attributes atts) throws SAXException {
/*  98 */     int id = this._source.replace(this, child);
/*  99 */     this._source.sendEnterElement(id, uri, localname, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromEnterAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 104 */     int id = this._source.replace(this, child);
/* 105 */     this._source.sendEnterAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveElement(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 110 */     int id = this._source.replace(this, child);
/* 111 */     this._source.sendLeaveElement(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 116 */     int id = this._source.replace(this, child);
/* 117 */     this._source.sendLeaveAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromText(NGCCEventReceiver child, String value) throws SAXException {
/* 122 */     int id = this._source.replace(this, child);
/* 123 */     this._source.sendText(id, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revertToParentFromEnterElement(Object result, int cookie, String uri, String local, String qname, Attributes atts) throws SAXException {
/* 134 */     int id = this._source.replace(this, this._parent);
/* 135 */     this._parent.onChildCompleted(result, cookie, true);
/* 136 */     this._source.sendEnterElement(id, uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveElement(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 141 */     if (uri == "\000" && uri == local && uri == qname && this._parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 147 */     int id = this._source.replace(this, this._parent);
/* 148 */     this._parent.onChildCompleted(result, cookie, true);
/* 149 */     this._source.sendLeaveElement(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromEnterAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 154 */     int id = this._source.replace(this, this._parent);
/* 155 */     this._parent.onChildCompleted(result, cookie, true);
/* 156 */     this._source.sendEnterAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 161 */     int id = this._source.replace(this, this._parent);
/* 162 */     this._parent.onChildCompleted(result, cookie, true);
/* 163 */     this._source.sendLeaveAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromText(Object result, int cookie, String text) throws SAXException {
/* 168 */     int id = this._source.replace(this, this._parent);
/* 169 */     this._parent.onChildCompleted(result, cookie, true);
/* 170 */     this._source.sendText(id, text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unexpectedEnterElement(String qname) throws SAXException {
/* 180 */     getRuntime().unexpectedX('<' + qname + '>');
/*     */   }
/*     */   public void unexpectedLeaveElement(String qname) throws SAXException {
/* 183 */     getRuntime().unexpectedX("</" + qname + '>');
/*     */   }
/*     */   public void unexpectedEnterAttribute(String qname) throws SAXException {
/* 186 */     getRuntime().unexpectedX('@' + qname);
/*     */   }
/*     */   public void unexpectedLeaveAttribute(String qname) throws SAXException {
/* 189 */     getRuntime().unexpectedX("/@" + qname);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\NGCCHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */