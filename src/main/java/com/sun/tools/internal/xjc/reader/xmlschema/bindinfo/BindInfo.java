/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.SchemaCache;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CPluginCustomization;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.annotation.XmlLocation;
/*     */ import com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlAnyElement;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Element;
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
/*     */ @XmlRootElement(namespace = "http://www.w3.org/2001/XMLSchema", name = "annotation")
/*     */ @XmlType(namespace = "http://www.w3.org/2001/XMLSchema", name = "foobar")
/*     */ public final class BindInfo
/*     */   implements Iterable<BIDeclaration>
/*     */ {
/*     */   private BGMBuilder builder;
/*     */   @XmlLocation
/*     */   private Locator location;
/*     */   @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema")
/*     */   private Documentation documentation;
/*     */   
/*     */   public boolean isPointless() {
/*  94 */     if (size() > 0) return false; 
/*  95 */     if (this.documentation != null && !this.documentation.contents.isEmpty()) {
/*  96 */       return false;
/*     */     }
/*  98 */     return true;
/*     */   }
/*     */   private static final class Documentation { @XmlAnyElement
/*     */     @XmlMixed
/* 102 */     List<Object> contents = new ArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     void addAll(Documentation rhs) {
/* 107 */       if (rhs == null)
/*     */         return; 
/* 109 */       if (this.contents == null)
/* 110 */         this.contents = new ArrayList(); 
/* 111 */       if (!this.contents.isEmpty())
/* 112 */         this.contents.add("\n\n"); 
/* 113 */       this.contents.addAll(rhs.contents);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 118 */   private final List<BIDeclaration> decls = new ArrayList<>();
/*     */   private XSComponent owner;
/*     */   
/*     */   private static final class AppInfo
/*     */   {
/*     */     @XmlAnyElement(lax = true, value = DomHandlerEx.class)
/* 124 */     List<Object> contents = new ArrayList();
/*     */ 
/*     */     
/*     */     public void addTo(BindInfo bi) {
/* 128 */       if (this.contents == null)
/*     */         return; 
/* 130 */       for (Object o : this.contents) {
/* 131 */         if (o instanceof BIDeclaration) {
/* 132 */           bi.addDecl((BIDeclaration)o);
/*     */         }
/* 134 */         if (o instanceof DomHandlerEx.DomAndLocation) {
/* 135 */           DomHandlerEx.DomAndLocation e = (DomHandlerEx.DomAndLocation)o;
/* 136 */           String nsUri = e.element.getNamespaceURI();
/* 137 */           if (nsUri == null || nsUri.equals("") || nsUri
/* 138 */             .equals("http://www.w3.org/2001/XMLSchema"))
/*     */             continue; 
/* 140 */           bi.addDecl(new BIXPluginCustomization(e.element, e.loc));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema")
/*     */   void setAppinfo(AppInfo aib) {
/* 150 */     aib.addTo(this);
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
/*     */   public Locator getSourceLocation() {
/* 164 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(BGMBuilder _builder, XSComponent _owner) {
/* 174 */     this.owner = _owner;
/* 175 */     this.builder = _builder;
/* 176 */     for (BIDeclaration d : this.decls)
/* 177 */       d.onSetOwner(); 
/*     */   } public XSComponent getOwner() {
/* 179 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BGMBuilder getBuilder() {
/* 185 */     return this.builder;
/*     */   }
/*     */   
/*     */   public void addDecl(BIDeclaration decl) {
/* 189 */     if (decl == null) throw new IllegalArgumentException(); 
/* 190 */     decl.setParent(this);
/* 191 */     this.decls.add(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BIDeclaration> T get(Class<T> kind) {
/* 200 */     for (BIDeclaration decl : this.decls) {
/* 201 */       if (kind.isInstance(decl))
/* 202 */         return kind.cast(decl); 
/*     */     } 
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIDeclaration[] getDecls() {
/* 211 */     return this.decls.<BIDeclaration>toArray(new BIDeclaration[this.decls.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocumentation() {
/* 221 */     if (this.documentation == null || this.documentation.contents == null) return null;
/*     */     
/* 223 */     StringBuilder buf = new StringBuilder();
/* 224 */     for (Object c : this.documentation.contents) {
/* 225 */       if (c instanceof String) {
/* 226 */         buf.append(c.toString());
/*     */       }
/* 228 */       if (c instanceof Element) {
/* 229 */         Transformer t = this.builder.getIdentityTransformer();
/* 230 */         StringWriter w = new StringWriter();
/*     */         try {
/* 232 */           Writer fw = new FilterWriter(w) {
/* 233 */               char[] buf = new char[1];
/*     */               
/*     */               public void write(int c) throws IOException {
/* 236 */                 this.buf[0] = (char)c;
/* 237 */                 write(this.buf, 0, 1);
/*     */               }
/*     */               
/*     */               public void write(char[] cbuf, int off, int len) throws IOException {
/* 241 */                 MinimumEscapeHandler.theInstance.escape(cbuf, off, len, false, this.out);
/*     */               }
/*     */               
/*     */               public void write(String str, int off, int len) throws IOException {
/* 245 */                 write(str.toCharArray(), off, len);
/*     */               }
/*     */             };
/* 248 */           t.transform(new DOMSource((Element)c), new StreamResult(fw));
/* 249 */         } catch (TransformerException e) {
/* 250 */           throw new Error(e);
/*     */         } 
/* 252 */         buf.append("\n<pre>\n");
/* 253 */         buf.append(w);
/* 254 */         buf.append("\n</pre>\n");
/*     */       } 
/*     */     } 
/* 257 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorb(BindInfo bi) {
/* 265 */     for (BIDeclaration d : bi)
/* 266 */       d.setParent(this); 
/* 267 */     this.decls.addAll(bi.decls);
/*     */     
/* 269 */     if (this.documentation == null) {
/* 270 */       this.documentation = bi.documentation;
/*     */     } else {
/* 272 */       this.documentation.addAll(bi.documentation);
/*     */     } 
/*     */   }
/*     */   public int size() {
/* 276 */     return this.decls.size();
/*     */   } public BIDeclaration get(int idx) {
/* 278 */     return this.decls.get(idx);
/*     */   }
/*     */   public Iterator<BIDeclaration> iterator() {
/* 281 */     return this.decls.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CCustomizations toCustomizationList() {
/* 292 */     CCustomizations r = null;
/* 293 */     for (BIDeclaration d : this) {
/* 294 */       if (d instanceof BIXPluginCustomization) {
/* 295 */         BIXPluginCustomization pc = (BIXPluginCustomization)d;
/* 296 */         pc.markAsAcknowledged();
/* 297 */         if (!((Model)Ring.get(Model.class)).options.pluginURIs.contains(pc.getName().getNamespaceURI()))
/*     */           continue; 
/* 299 */         if (r == null)
/* 300 */           r = new CCustomizations(); 
/* 301 */         r.add(new CPluginCustomization(pc.element, pc.getLocation()));
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     if (r == null) r = CCustomizations.EMPTY; 
/* 306 */     return new CCustomizations((Collection)r);
/*     */   }
/*     */   
/* 309 */   public static final BindInfo empty = new BindInfo();
/*     */ 
/*     */   
/*     */   private static volatile JAXBContext customizationContext;
/*     */ 
/*     */ 
/*     */   
/*     */   public static JAXBContext getCustomizationContext() {
/*     */     try {
/* 318 */       if (customizationContext == null) {
/* 319 */         synchronized (BindInfo.class) {
/* 320 */           if (customizationContext == null) {
/* 321 */             customizationContext = JAXBContext.newInstance(new Class[] { BindInfo.class, BIClass.class, BIConversion.User.class, BIConversion.UserAdapter.class, BIDom.class, BIFactoryMethod.class, BIInlineBinaryData.class, BIXDom.class, BIXSubstitutable.class, BIEnum.class, BIEnumMember.class, BIGlobalBinding.class, BIProperty.class, BISchemaBinding.class });
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 339 */       return customizationContext;
/* 340 */     } catch (JAXBException e) {
/* 341 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Unmarshaller getCustomizationUnmarshaller() {
/*     */     try {
/* 347 */       return getCustomizationContext().createUnmarshaller();
/* 348 */     } catch (JAXBException e) {
/* 349 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public static final SchemaCache bindingFileSchema = new SchemaCache(BindInfo.class.getResource("binding.xsd"));
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BindInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */