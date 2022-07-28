/*     */ package com.sun.tools.internal.ws.wsdl.document.schema;
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
/*     */ public interface SchemaConstants
/*     */ {
/*     */   public static final String NS_XMLNS = "http://www.w3.org/2000/xmlns/";
/*     */   public static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
/*     */   public static final String NS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
/*  42 */   public static final QName QNAME_ALL = new QName("http://www.w3.org/2001/XMLSchema", "all");
/*  43 */   public static final QName QNAME_ANNOTATION = new QName("http://www.w3.org/2001/XMLSchema", "annotation");
/*  44 */   public static final QName QNAME_ANY = new QName("http://www.w3.org/2001/XMLSchema", "any");
/*  45 */   public static final QName QNAME_ANY_ATTRIBUTE = new QName("http://www.w3.org/2001/XMLSchema", "anyAttribute");
/*  46 */   public static final QName QNAME_ATTRIBUTE = new QName("http://www.w3.org/2001/XMLSchema", "attribute");
/*  47 */   public static final QName QNAME_ATTRIBUTE_GROUP = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
/*  48 */   public static final QName QNAME_CHOICE = new QName("http://www.w3.org/2001/XMLSchema", "choice");
/*  49 */   public static final QName QNAME_COMPLEX_CONTENT = new QName("http://www.w3.org/2001/XMLSchema", "complexContent");
/*  50 */   public static final QName QNAME_COMPLEX_TYPE = new QName("http://www.w3.org/2001/XMLSchema", "complexType");
/*  51 */   public static final QName QNAME_ELEMENT = new QName("http://www.w3.org/2001/XMLSchema", "element");
/*  52 */   public static final QName QNAME_ENUMERATION = new QName("http://www.w3.org/2001/XMLSchema", "enumeration");
/*  53 */   public static final QName QNAME_EXTENSION = new QName("http://www.w3.org/2001/XMLSchema", "extension");
/*  54 */   public static final QName QNAME_FIELD = new QName("http://www.w3.org/2001/XMLSchema", "field");
/*  55 */   public static final QName QNAME_FRACTION_DIGITS = new QName("http://www.w3.org/2001/XMLSchema", "fractionDigits");
/*  56 */   public static final QName QNAME_GROUP = new QName("http://www.w3.org/2001/XMLSchema", "group");
/*  57 */   public static final QName QNAME_IMPORT = new QName("http://www.w3.org/2001/XMLSchema", "import");
/*  58 */   public static final QName QNAME_INCLUDE = new QName("http://www.w3.org/2001/XMLSchema", "include");
/*  59 */   public static final QName QNAME_KEY = new QName("http://www.w3.org/2001/XMLSchema", "key");
/*  60 */   public static final QName QNAME_KEYREF = new QName("http://www.w3.org/2001/XMLSchema", "keyref");
/*  61 */   public static final QName QNAME_LENGTH = new QName("http://www.w3.org/2001/XMLSchema", "length");
/*  62 */   public static final QName QNAME_LIST = new QName("http://www.w3.org/2001/XMLSchema", "list");
/*  63 */   public static final QName QNAME_MAX_EXCLUSIVE = new QName("http://www.w3.org/2001/XMLSchema", "maxExclusive");
/*  64 */   public static final QName QNAME_MAX_INCLUSIVE = new QName("http://www.w3.org/2001/XMLSchema", "maxInclusive");
/*  65 */   public static final QName QNAME_MAX_LENGTH = new QName("http://www.w3.org/2001/XMLSchema", "maxLength");
/*  66 */   public static final QName QNAME_MIN_EXCLUSIVE = new QName("http://www.w3.org/2001/XMLSchema", "minExclusive");
/*  67 */   public static final QName QNAME_MIN_INCLUSIVE = new QName("http://www.w3.org/2001/XMLSchema", "minInclusive");
/*  68 */   public static final QName QNAME_MIN_LENGTH = new QName("http://www.w3.org/2001/XMLSchema", "minLength");
/*  69 */   public static final QName QNAME_NOTATION = new QName("http://www.w3.org/2001/XMLSchema", "notation");
/*  70 */   public static final QName QNAME_RESTRICTION = new QName("http://www.w3.org/2001/XMLSchema", "restriction");
/*  71 */   public static final QName QNAME_PATTERN = new QName("http://www.w3.org/2001/XMLSchema", "pattern");
/*  72 */   public static final QName QNAME_PRECISION = new QName("http://www.w3.org/2001/XMLSchema", "precision");
/*  73 */   public static final QName QNAME_REDEFINE = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
/*  74 */   public static final QName QNAME_SCALE = new QName("http://www.w3.org/2001/XMLSchema", "scale");
/*  75 */   public static final QName QNAME_SCHEMA = new QName("http://www.w3.org/2001/XMLSchema", "schema");
/*  76 */   public static final QName QNAME_SELECTOR = new QName("http://www.w3.org/2001/XMLSchema", "selector");
/*  77 */   public static final QName QNAME_SEQUENCE = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
/*  78 */   public static final QName QNAME_SIMPLE_CONTENT = new QName("http://www.w3.org/2001/XMLSchema", "simpleContent");
/*     */   
/*  80 */   public static final QName QNAME_SIMPLE_TYPE = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
/*  81 */   public static final QName QNAME_TOTAL_DIGITS = new QName("http://www.w3.org/2001/XMLSchema", "totalDigits");
/*  82 */   public static final QName QNAME_UNIQUE = new QName("http://www.w3.org/2001/XMLSchema", "unique");
/*  83 */   public static final QName QNAME_UNION = new QName("http://www.w3.org/2001/XMLSchema", "union");
/*  84 */   public static final QName QNAME_WHITE_SPACE = new QName("http://www.w3.org/2001/XMLSchema", "whiteSpace");
/*     */ 
/*     */   
/*  87 */   public static final QName QNAME_TYPE_STRING = new QName("http://www.w3.org/2001/XMLSchema", "string");
/*  88 */   public static final QName QNAME_TYPE_NORMALIZED_STRING = new QName("http://www.w3.org/2001/XMLSchema", "normalizedString");
/*  89 */   public static final QName QNAME_TYPE_TOKEN = new QName("http://www.w3.org/2001/XMLSchema", "token");
/*  90 */   public static final QName QNAME_TYPE_BYTE = new QName("http://www.w3.org/2001/XMLSchema", "byte");
/*  91 */   public static final QName QNAME_TYPE_UNSIGNED_BYTE = new QName("http://www.w3.org/2001/XMLSchema", "unsignedByte");
/*  92 */   public static final QName QNAME_TYPE_BASE64_BINARY = new QName("http://www.w3.org/2001/XMLSchema", "base64Binary");
/*  93 */   public static final QName QNAME_TYPE_HEX_BINARY = new QName("http://www.w3.org/2001/XMLSchema", "hexBinary");
/*  94 */   public static final QName QNAME_TYPE_INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "integer");
/*  95 */   public static final QName QNAME_TYPE_POSITIVE_INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "positiveInteger");
/*  96 */   public static final QName QNAME_TYPE_NEGATIVE_INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "negativeInteger");
/*  97 */   public static final QName QNAME_TYPE_NON_NEGATIVE_INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger");
/*  98 */   public static final QName QNAME_TYPE_NON_POSITIVE_INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "nonPositiveInteger");
/*  99 */   public static final QName QNAME_TYPE_INT = new QName("http://www.w3.org/2001/XMLSchema", "int");
/* 100 */   public static final QName QNAME_TYPE_UNSIGNED_INT = new QName("http://www.w3.org/2001/XMLSchema", "unsignedInt");
/* 101 */   public static final QName QNAME_TYPE_LONG = new QName("http://www.w3.org/2001/XMLSchema", "long");
/* 102 */   public static final QName QNAME_TYPE_UNSIGNED_LONG = new QName("http://www.w3.org/2001/XMLSchema", "unsignedLong");
/* 103 */   public static final QName QNAME_TYPE_SHORT = new QName("http://www.w3.org/2001/XMLSchema", "short");
/* 104 */   public static final QName QNAME_TYPE_UNSIGNED_SHORT = new QName("http://www.w3.org/2001/XMLSchema", "unsignedShort");
/* 105 */   public static final QName QNAME_TYPE_DECIMAL = new QName("http://www.w3.org/2001/XMLSchema", "decimal");
/* 106 */   public static final QName QNAME_TYPE_FLOAT = new QName("http://www.w3.org/2001/XMLSchema", "float");
/* 107 */   public static final QName QNAME_TYPE_DOUBLE = new QName("http://www.w3.org/2001/XMLSchema", "double");
/* 108 */   public static final QName QNAME_TYPE_BOOLEAN = new QName("http://www.w3.org/2001/XMLSchema", "boolean");
/* 109 */   public static final QName QNAME_TYPE_TIME = new QName("http://www.w3.org/2001/XMLSchema", "time");
/* 110 */   public static final QName QNAME_TYPE_DATE_TIME = new QName("http://www.w3.org/2001/XMLSchema", "dateTime");
/* 111 */   public static final QName QNAME_TYPE_DURATION = new QName("http://www.w3.org/2001/XMLSchema", "duration");
/* 112 */   public static final QName QNAME_TYPE_DATE = new QName("http://www.w3.org/2001/XMLSchema", "date");
/* 113 */   public static final QName QNAME_TYPE_G_MONTH = new QName("http://www.w3.org/2001/XMLSchema", "gMonth");
/* 114 */   public static final QName QNAME_TYPE_G_YEAR = new QName("http://www.w3.org/2001/XMLSchema", "gYear");
/* 115 */   public static final QName QNAME_TYPE_G_YEAR_MONTH = new QName("http://www.w3.org/2001/XMLSchema", "gYearMonth");
/* 116 */   public static final QName QNAME_TYPE_G_DAY = new QName("http://www.w3.org/2001/XMLSchema", "gDay");
/* 117 */   public static final QName QNAME_TYPE_G_MONTH_DAY = new QName("http://www.w3.org/2001/XMLSchema", "gMonthDay");
/* 118 */   public static final QName QNAME_TYPE_NAME = new QName("http://www.w3.org/2001/XMLSchema", "Name");
/* 119 */   public static final QName QNAME_TYPE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "QName");
/* 120 */   public static final QName QNAME_TYPE_NCNAME = new QName("http://www.w3.org/2001/XMLSchema", "NCName");
/* 121 */   public static final QName QNAME_TYPE_ANY_URI = new QName("http://www.w3.org/2001/XMLSchema", "anyURI");
/* 122 */   public static final QName QNAME_TYPE_ID = new QName("http://www.w3.org/2001/XMLSchema", "ID");
/* 123 */   public static final QName QNAME_TYPE_IDREF = new QName("http://www.w3.org/2001/XMLSchema", "IDREF");
/* 124 */   public static final QName QNAME_TYPE_IDREFS = new QName("http://www.w3.org/2001/XMLSchema", "IDREFS");
/* 125 */   public static final QName QNAME_TYPE_ENTITY = new QName("http://www.w3.org/2001/XMLSchema", "ENTITY");
/* 126 */   public static final QName QNAME_TYPE_ENTITIES = new QName("http://www.w3.org/2001/XMLSchema", "ENTITIES");
/* 127 */   public static final QName QNAME_TYPE_NOTATION = new QName("http://www.w3.org/2001/XMLSchema", "NOTATION");
/* 128 */   public static final QName QNAME_TYPE_NMTOKEN = new QName("http://www.w3.org/2001/XMLSchema", "NMTOKEN");
/* 129 */   public static final QName QNAME_TYPE_NMTOKENS = new QName("http://www.w3.org/2001/XMLSchema", "NMTOKENS");
/*     */   
/* 131 */   public static final QName QNAME_TYPE_LANGUAGE = new QName("http://www.w3.org/2001/XMLSchema", "language");
/*     */ 
/*     */   
/* 134 */   public static final QName QNAME_TYPE_URTYPE = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
/* 135 */   public static final QName QNAME_TYPE_SIMPLE_URTYPE = new QName("http://www.w3.org/2001/XMLSchema", "anySimpleType");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\schema\SchemaConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */