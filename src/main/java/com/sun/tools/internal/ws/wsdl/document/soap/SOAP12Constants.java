/*     */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SOAP12Constants
/*     */ {
/*     */   public static final String NS_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap12/";
/*     */   public static final String NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*     */   public static final String URI_SOAP_TRANSPORT_HTTP = "http://www.w3.org/2003/05/soap/bindings/HTTP/";
/*  45 */   public static final QName QNAME_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "address");
/*  46 */   public static final QName QNAME_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "binding");
/*  47 */   public static final QName QNAME_BODY = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "body");
/*  48 */   public static final QName QNAME_FAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "fault");
/*  49 */   public static final QName QNAME_HEADER = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "header");
/*  50 */   public static final QName QNAME_HEADERFAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "headerfault");
/*  51 */   public static final QName QNAME_OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "operation");
/*     */ 
/*     */   
/*  54 */   public static final QName QNAME_TYPE_ARRAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Array");
/*  55 */   public static final QName QNAME_ATTR_GROUP_COMMON_ATTRIBUTES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "commonAttributes");
/*  56 */   public static final QName QNAME_ATTR_ARRAY_TYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
/*  57 */   public static final QName QNAME_ATTR_ITEM_TYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "itemType");
/*  58 */   public static final QName QNAME_ATTR_ARRAY_SIZE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arraySize");
/*  59 */   public static final QName QNAME_ATTR_OFFSET = new QName("http://schemas.xmlsoap.org/soap/encoding/", "offset");
/*  60 */   public static final QName QNAME_ATTR_POSITION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "position");
/*     */   
/*  62 */   public static final QName QNAME_TYPE_BASE64 = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64");
/*     */   
/*  64 */   public static final QName QNAME_ELEMENT_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "string");
/*  65 */   public static final QName QNAME_ELEMENT_NORMALIZED_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "normalizedString");
/*  66 */   public static final QName QNAME_ELEMENT_TOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "token");
/*  67 */   public static final QName QNAME_ELEMENT_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "byte");
/*  68 */   public static final QName QNAME_ELEMENT_UNSIGNED_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedByte");
/*  69 */   public static final QName QNAME_ELEMENT_BASE64_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary");
/*  70 */   public static final QName QNAME_ELEMENT_HEX_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "hexBinary");
/*  71 */   public static final QName QNAME_ELEMENT_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "integer");
/*  72 */   public static final QName QNAME_ELEMENT_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "positiveInteger");
/*  73 */   public static final QName QNAME_ELEMENT_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "negativeInteger");
/*  74 */   public static final QName QNAME_ELEMENT_NON_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonNegativeInteger");
/*  75 */   public static final QName QNAME_ELEMENT_NON_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonPositiveInteger");
/*  76 */   public static final QName QNAME_ELEMENT_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "int");
/*  77 */   public static final QName QNAME_ELEMENT_UNSIGNED_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedInt");
/*  78 */   public static final QName QNAME_ELEMENT_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "long");
/*  79 */   public static final QName QNAME_ELEMENT_UNSIGNED_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedLong");
/*  80 */   public static final QName QNAME_ELEMENT_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "short");
/*  81 */   public static final QName QNAME_ELEMENT_UNSIGNED_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedShort");
/*  82 */   public static final QName QNAME_ELEMENT_DECIMAL = new QName("http://schemas.xmlsoap.org/soap/encoding/", "decimal");
/*  83 */   public static final QName QNAME_ELEMENT_FLOAT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "float");
/*  84 */   public static final QName QNAME_ELEMENT_DOUBLE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "double");
/*  85 */   public static final QName QNAME_ELEMENT_BOOLEAN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean");
/*  86 */   public static final QName QNAME_ELEMENT_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "time");
/*  87 */   public static final QName QNAME_ELEMENT_DATE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "dateTime");
/*  88 */   public static final QName QNAME_ELEMENT_DURATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "duration");
/*  89 */   public static final QName QNAME_ELEMENT_DATE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "date");
/*  90 */   public static final QName QNAME_ELEMENT_G_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonth");
/*  91 */   public static final QName QNAME_ELEMENT_G_YEAR = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYear");
/*  92 */   public static final QName QNAME_ELEMENT_G_YEAR_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYearMonth");
/*  93 */   public static final QName QNAME_ELEMENT_G_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gDay");
/*  94 */   public static final QName QNAME_ELEMENT_G_MONTH_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonthDay");
/*  95 */   public static final QName QNAME_ELEMENT_NAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Name");
/*  96 */   public static final QName QNAME_ELEMENT_QNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "QName");
/*  97 */   public static final QName QNAME_ELEMENT_NCNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NCName");
/*  98 */   public static final QName QNAME_ELEMENT_ANY_URI = new QName("http://schemas.xmlsoap.org/soap/encoding/", "anyURI");
/*  99 */   public static final QName QNAME_ELEMENT_ID = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ID");
/* 100 */   public static final QName QNAME_ELEMENT_IDREF = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREF");
/* 101 */   public static final QName QNAME_ELEMENT_IDREFS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREFS");
/* 102 */   public static final QName QNAME_ELEMENT_ENTITY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITY");
/* 103 */   public static final QName QNAME_ELEMENT_ENTITIES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITIES");
/* 104 */   public static final QName QNAME_ELEMENT_NOTATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NOTATION");
/* 105 */   public static final QName QNAME_ELEMENT_NMTOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKEN");
/* 106 */   public static final QName QNAME_ELEMENT_NMTOKENS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKENS");
/*     */   
/* 108 */   public static final QName QNAME_TYPE_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "string");
/* 109 */   public static final QName QNAME_TYPE_NORMALIZED_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "normalizedString");
/* 110 */   public static final QName QNAME_TYPE_TOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "token");
/* 111 */   public static final QName QNAME_TYPE_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "byte");
/* 112 */   public static final QName QNAME_TYPE_UNSIGNED_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedByte");
/* 113 */   public static final QName QNAME_TYPE_BASE64_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary");
/* 114 */   public static final QName QNAME_TYPE_HEX_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "hexBinary");
/* 115 */   public static final QName QNAME_TYPE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "integer");
/* 116 */   public static final QName QNAME_TYPE_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "positiveInteger");
/* 117 */   public static final QName QNAME_TYPE_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "negativeInteger");
/* 118 */   public static final QName QNAME_TYPE_NON_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonNegativeInteger");
/* 119 */   public static final QName QNAME_TYPE_NON_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonPositiveInteger");
/* 120 */   public static final QName QNAME_TYPE_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "int");
/* 121 */   public static final QName QNAME_TYPE_UNSIGNED_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedInt");
/* 122 */   public static final QName QNAME_TYPE_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "long");
/* 123 */   public static final QName QNAME_TYPE_UNSIGNED_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedLong");
/* 124 */   public static final QName QNAME_TYPE_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "short");
/* 125 */   public static final QName QNAME_TYPE_UNSIGNED_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedShort");
/* 126 */   public static final QName QNAME_TYPE_DECIMAL = new QName("http://schemas.xmlsoap.org/soap/encoding/", "decimal");
/* 127 */   public static final QName QNAME_TYPE_FLOAT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "float");
/* 128 */   public static final QName QNAME_TYPE_DOUBLE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "double");
/* 129 */   public static final QName QNAME_TYPE_BOOLEAN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean");
/* 130 */   public static final QName QNAME_TYPE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "time");
/* 131 */   public static final QName QNAME_TYPE_DATE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "dateTime");
/* 132 */   public static final QName QNAME_TYPE_DURATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "duration");
/* 133 */   public static final QName QNAME_TYPE_DATE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "date");
/* 134 */   public static final QName QNAME_TYPE_G_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonth");
/* 135 */   public static final QName QNAME_TYPE_G_YEAR = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYear");
/* 136 */   public static final QName QNAME_TYPE_G_YEAR_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYearMonth");
/* 137 */   public static final QName QNAME_TYPE_G_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gDay");
/* 138 */   public static final QName QNAME_TYPE_G_MONTH_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonthDay");
/* 139 */   public static final QName QNAME_TYPE_NAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Name");
/* 140 */   public static final QName QNAME_TYPE_QNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "QName");
/* 141 */   public static final QName QNAME_TYPE_NCNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NCName");
/* 142 */   public static final QName QNAME_TYPE_ANY_URI = new QName("http://schemas.xmlsoap.org/soap/encoding/", "anyURI");
/* 143 */   public static final QName QNAME_TYPE_ID = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ID");
/* 144 */   public static final QName QNAME_TYPE_IDREF = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREF");
/* 145 */   public static final QName QNAME_TYPE_IDREFS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREFS");
/* 146 */   public static final QName QNAME_TYPE_ENTITY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITY");
/* 147 */   public static final QName QNAME_TYPE_ENTITIES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITIES");
/* 148 */   public static final QName QNAME_TYPE_NOTATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NOTATION");
/* 149 */   public static final QName QNAME_TYPE_NMTOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKEN");
/* 150 */   public static final QName QNAME_TYPE_NMTOKENS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKENS");
/* 151 */   public static final QName QNAME_TYPE_LANGUAGE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "LANGUAGE");
/*     */ 
/*     */   
/* 154 */   public static final QName QNAME_ATTR_ID = new QName("", "id");
/* 155 */   public static final QName QNAME_ATTR_HREF = new QName("", "ref");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAP12Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */