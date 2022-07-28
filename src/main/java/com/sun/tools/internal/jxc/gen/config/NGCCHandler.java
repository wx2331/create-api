/*     */ package com.sun.tools.internal.jxc.gen.config;
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
/*  42 */     this._parent = parent;
/*  43 */     this._source = source;
/*  44 */     this._cookie = parentCookie;
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
/* 100 */     int id = this._source.replace(this, child);
/* 101 */     this._source.sendEnterElement(id, uri, localname, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromEnterAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 106 */     int id = this._source.replace(this, child);
/* 107 */     this._source.sendEnterAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveElement(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 112 */     int id = this._source.replace(this, child);
/* 113 */     this._source.sendLeaveElement(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromLeaveAttribute(NGCCEventReceiver child, String uri, String localname, String qname) throws SAXException {
/* 118 */     int id = this._source.replace(this, child);
/* 119 */     this._source.sendLeaveAttribute(id, uri, localname, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromText(NGCCEventReceiver child, String value) throws SAXException {
/* 124 */     int id = this._source.replace(this, child);
/* 125 */     this._source.sendText(id, value);
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
/* 136 */     int id = this._source.replace(this, this._parent);
/* 137 */     this._parent.onChildCompleted(result, cookie, true);
/* 138 */     this._source.sendEnterElement(id, uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveElement(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 143 */     if (uri == "\000" && uri == local && uri == qname && this._parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 149 */     int id = this._source.replace(this, this._parent);
/* 150 */     this._parent.onChildCompleted(result, cookie, true);
/* 151 */     this._source.sendLeaveElement(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromEnterAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 156 */     int id = this._source.replace(this, this._parent);
/* 157 */     this._parent.onChildCompleted(result, cookie, true);
/* 158 */     this._source.sendEnterAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromLeaveAttribute(Object result, int cookie, String uri, String local, String qname) throws SAXException {
/* 163 */     int id = this._source.replace(this, this._parent);
/* 164 */     this._parent.onChildCompleted(result, cookie, true);
/* 165 */     this._source.sendLeaveAttribute(id, uri, local, qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void revertToParentFromText(Object result, int cookie, String text) throws SAXException {
/* 170 */     int id = this._source.replace(this, this._parent);
/* 171 */     this._parent.onChildCompleted(result, cookie, true);
/* 172 */     this._source.sendText(id, text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unexpectedEnterElement(String qname) throws SAXException {
/* 182 */     getRuntime().unexpectedX('<' + qname + '>');
/*     */   }
/*     */   public void unexpectedLeaveElement(String qname) throws SAXException {
/* 185 */     getRuntime().unexpectedX("</" + qname + '>');
/*     */   }
/*     */   public void unexpectedEnterAttribute(String qname) throws SAXException {
/* 188 */     getRuntime().unexpectedX('@' + qname);
/*     */   }
/*     */   public void unexpectedLeaveAttribute(String qname) throws SAXException {
/* 191 */     getRuntime().unexpectedX("/@" + qname);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\NGCCHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */