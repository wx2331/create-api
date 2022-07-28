/*     */ package com.sun.tools.internal.ws.processor.modeler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ModelerConstants
/*     */ {
/*  35 */   FALSE_STR("false"),
/*  36 */   ZERO_STR("0"),
/*  37 */   NULL_STR("null"),
/*  38 */   ARRAY_STR("Array"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   IOEXCEPTION_CLASSNAME("java.io.IOException"),
/*  47 */   BOOLEAN_CLASSNAME("boolean"),
/*  48 */   BOXED_BOOLEAN_CLASSNAME("java.lang.Boolean"),
/*  49 */   BYTE_CLASSNAME("byte"),
/*  50 */   BYTE_ARRAY_CLASSNAME("byte[]"),
/*  51 */   BOXED_BYTE_CLASSNAME("java.lang.Byte"),
/*  52 */   BOXED_BYTE_ARRAY_CLASSNAME("java.lang.Byte[]"),
/*  53 */   CLASS_CLASSNAME("java.lang.Class"),
/*  54 */   CHAR_CLASSNAME("char"),
/*  55 */   BOXED_CHAR_CLASSNAME("java.lang.Character"),
/*  56 */   DOUBLE_CLASSNAME("double"),
/*  57 */   BOXED_DOUBLE_CLASSNAME("java.lang.Double"),
/*  58 */   FLOAT_CLASSNAME("float"),
/*  59 */   BOXED_FLOAT_CLASSNAME("java.lang.Float"),
/*  60 */   INT_CLASSNAME("int"),
/*  61 */   BOXED_INTEGER_CLASSNAME("java.lang.Integer"),
/*  62 */   LONG_CLASSNAME("long"),
/*  63 */   BOXED_LONG_CLASSNAME("java.lang.Long"),
/*  64 */   SHORT_CLASSNAME("short"),
/*  65 */   BOXED_SHORT_CLASSNAME("java.lang.Short"),
/*  66 */   BIGDECIMAL_CLASSNAME("java.math.BigDecimal"),
/*  67 */   BIGINTEGER_CLASSNAME("java.math.BigInteger"),
/*  68 */   CALENDAR_CLASSNAME("java.util.Calendar"),
/*  69 */   DATE_CLASSNAME("java.util.Date"),
/*  70 */   STRING_CLASSNAME("java.lang.String"),
/*  71 */   STRING_ARRAY_CLASSNAME("java.lang.String[]"),
/*  72 */   QNAME_CLASSNAME("javax.xml.namespace.QName"),
/*  73 */   VOID_CLASSNAME("void"),
/*  74 */   OBJECT_CLASSNAME("java.lang.Object"),
/*  75 */   SOAPELEMENT_CLASSNAME("javax.xml.soap.SOAPElement"),
/*  76 */   IMAGE_CLASSNAME("java.awt.Image"),
/*  77 */   MIME_MULTIPART_CLASSNAME("javax.mail.internet.MimeMultipart"),
/*  78 */   SOURCE_CLASSNAME("javax.xml.transform.Source"),
/*  79 */   DATA_HANDLER_CLASSNAME("javax.activation.DataHandler"),
/*  80 */   URI_CLASSNAME("java.net.URI"),
/*     */ 
/*     */   
/*  83 */   COLLECTION_CLASSNAME("java.util.Collection"),
/*  84 */   LIST_CLASSNAME("java.util.List"),
/*  85 */   SET_CLASSNAME("java.util.Set"),
/*  86 */   VECTOR_CLASSNAME("java.util.Vector"),
/*  87 */   STACK_CLASSNAME("java.util.Stack"),
/*  88 */   LINKED_LIST_CLASSNAME("java.util.LinkedList"),
/*  89 */   ARRAY_LIST_CLASSNAME("java.util.ArrayList"),
/*  90 */   HASH_SET_CLASSNAME("java.util.HashSet"),
/*  91 */   TREE_SET_CLASSNAME("java.util.TreeSet"),
/*     */ 
/*     */   
/*  94 */   MAP_CLASSNAME("java.util.Map"),
/*  95 */   HASH_MAP_CLASSNAME("java.util.HashMap"),
/*  96 */   TREE_MAP_CLASSNAME("java.util.TreeMap"),
/*  97 */   HASHTABLE_CLASSNAME("java.util.Hashtable"),
/*  98 */   PROPERTIES_CLASSNAME("java.util.Properties"),
/*     */   
/* 100 */   JAX_WS_MAP_ENTRY_CLASSNAME("com.sun.xml.internal.ws.encoding.soap.JAXWSMapEntry");
/*     */   
/*     */   private String value;
/*     */   
/*     */   ModelerConstants(String value) {
/* 105 */     this.value = value;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 109 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\ModelerConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */