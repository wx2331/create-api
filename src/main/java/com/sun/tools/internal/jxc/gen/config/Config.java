/*     */ package com.sun.tools.internal.jxc.gen.config;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.NGCCRuntimeEx;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class Config
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String bd;
/*     */   private Schema _schema;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private File baseDir;
/*     */   private Classes classes;
/*     */   private List schema;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  54 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   public Config(NGCCRuntimeEx runtime) { this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1); }
/*     */   private void action0() throws SAXException { this.schema.add(this._schema); }
/*  58 */   private void action1() throws SAXException { this.baseDir = this.$runtime.getBaseDir(this.bd); } public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException { int $ai; this.$uri = $__uri; this.$localName = $__local; this.$qname = $__qname; switch (this.$_ngcc_current_state) { case 0: revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs); return;case 1: if ($__uri.equals("") && $__local.equals("schema")) { NGCCHandler h = new Schema(this, this._source, this.$runtime, 19, this.baseDir); spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs); } else { unexpectedEnterElement($__qname); }  return;case 8: if ($__uri.equals("") && $__local.equals("config")) { this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs); this.$_ngcc_current_state = 7; } else { unexpectedEnterElement($__qname); }  return;case 4: if ($__uri.equals("") && $__local.equals("classes")) { NGCCHandler h = new Classes(this, this._source, this.$runtime, 22); spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs); } else { unexpectedEnterElement($__qname); }  return;case 2: if ($__uri.equals("") && $__local.equals("schema")) { NGCCHandler h = new Schema(this, this._source, this.$runtime, 20, this.baseDir); spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs); } else { this.$_ngcc_current_state = 1; this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs); }  return;case 7: if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) { this.$runtime.consumeAttribute($ai); this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs); } else { unexpectedEnterElement($__qname); }  return; }  unexpectedEnterElement($__qname); } public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException { int $ai; this.$uri = $__uri; this.$localName = $__local; this.$qname = $__qname; switch (this.$_ngcc_current_state) { case 0: revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname); return;case 1: if ($__uri.equals("") && $__local.equals("config")) { this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname); this.$_ngcc_current_state = 0; } else { unexpectedLeaveElement($__qname); }  return;case 2: this.$_ngcc_current_state = 1; this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname); return;case 7: if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) { this.$runtime.consumeAttribute($ai); this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname); } else { unexpectedLeaveElement($__qname); }  return; }  unexpectedLeaveElement($__qname); } public Config(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) { super(source, parent, cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.schema = new ArrayList(); this.$runtime = runtime; this.$_ngcc_current_state = 8; }
/* 332 */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException { this.$uri = $__uri; this.$localName = $__local; this.$qname = $__qname; switch (this.$_ngcc_current_state) { case 0: revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname); return;case 2: this.$_ngcc_current_state = 1; this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname); return;case 7: if ($__uri.equals("") && $__local.equals("baseDir")) { this.$_ngcc_current_state = 6; } else { unexpectedEnterAttribute($__qname); }  return; }  unexpectedEnterAttribute($__qname); } public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException { this.$uri = $__uri; this.$localName = $__local; this.$qname = $__qname; switch (this.$_ngcc_current_state) { case 0: revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname); return;case 5: if ($__uri.equals("") && $__local.equals("baseDir")) { this.$_ngcc_current_state = 4; } else { unexpectedLeaveAttribute($__qname); }  return;case 2: this.$_ngcc_current_state = 1; this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname); return; }  unexpectedLeaveAttribute($__qname); } public void text(String $value) throws SAXException { int $ai; switch (this.$_ngcc_current_state) { case 0: revertToParentFromText(this, this._cookie, $value); break;case 6: this.bd = $value; this.$_ngcc_current_state = 5; action1(); break;case 2: this.$_ngcc_current_state = 1; this.$runtime.sendText(this._cookie, $value); break;case 7: if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) { this.$runtime.consumeAttribute($ai); this.$runtime.sendText(this._cookie, $value); }  break; }  } public void onChildCompleted(Object result, int cookie, boolean needAttCheck) throws SAXException { switch (cookie) { case 19: this._schema = (Schema)result; action0(); this.$_ngcc_current_state = 1; break;case 22: this.classes = (Classes)result; this.$_ngcc_current_state = 2; break;case 20: this._schema = (Schema)result; action0(); this.$_ngcc_current_state = 1; break; }  } public boolean accepted() { return (this.$_ngcc_current_state == 0); } public Classes getClasses() { return this.classes; }
/* 333 */   public File getBaseDir() { return this.baseDir; } public List getSchema() {
/* 334 */     return this.schema;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */