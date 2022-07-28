/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Entity
/*     */ {
/*  47 */   nbsp(160),
/*  48 */   iexcl(161),
/*  49 */   cent(162),
/*  50 */   pound(163),
/*  51 */   curren(164),
/*  52 */   yen(165),
/*  53 */   brvbar(166),
/*  54 */   sect(167),
/*  55 */   uml(168),
/*  56 */   copy(169),
/*  57 */   ordf(170),
/*  58 */   laquo(171),
/*  59 */   not(172),
/*  60 */   shy(173),
/*  61 */   reg(174),
/*  62 */   macr(175),
/*  63 */   deg(176),
/*  64 */   plusmn(177),
/*  65 */   sup2(178),
/*  66 */   sup3(179),
/*  67 */   acute(180),
/*  68 */   micro(181),
/*  69 */   para(182),
/*  70 */   middot(183),
/*  71 */   cedil(184),
/*  72 */   sup1(185),
/*  73 */   ordm(186),
/*  74 */   raquo(187),
/*  75 */   frac14(188),
/*  76 */   frac12(189),
/*  77 */   frac34(190),
/*  78 */   iquest(191),
/*  79 */   Agrave(192),
/*  80 */   Aacute(193),
/*  81 */   Acirc(194),
/*  82 */   Atilde(195),
/*  83 */   Auml(196),
/*  84 */   Aring(197),
/*  85 */   AElig(198),
/*  86 */   Ccedil(199),
/*  87 */   Egrave(200),
/*  88 */   Eacute(201),
/*  89 */   Ecirc(202),
/*  90 */   Euml(203),
/*  91 */   Igrave(204),
/*  92 */   Iacute(205),
/*  93 */   Icirc(206),
/*  94 */   Iuml(207),
/*  95 */   ETH(208),
/*  96 */   Ntilde(209),
/*  97 */   Ograve(210),
/*  98 */   Oacute(211),
/*  99 */   Ocirc(212),
/* 100 */   Otilde(213),
/* 101 */   Ouml(214),
/* 102 */   times(215),
/* 103 */   Oslash(216),
/* 104 */   Ugrave(217),
/* 105 */   Uacute(218),
/* 106 */   Ucirc(219),
/* 107 */   Uuml(220),
/* 108 */   Yacute(221),
/* 109 */   THORN(222),
/* 110 */   szlig(223),
/* 111 */   agrave(224),
/* 112 */   aacute(225),
/* 113 */   acirc(226),
/* 114 */   atilde(227),
/* 115 */   auml(228),
/* 116 */   aring(229),
/* 117 */   aelig(230),
/* 118 */   ccedil(231),
/* 119 */   egrave(232),
/* 120 */   eacute(233),
/* 121 */   ecirc(234),
/* 122 */   euml(235),
/* 123 */   igrave(236),
/* 124 */   iacute(237),
/* 125 */   icirc(238),
/* 126 */   iuml(239),
/* 127 */   eth(240),
/* 128 */   ntilde(241),
/* 129 */   ograve(242),
/* 130 */   oacute(243),
/* 131 */   ocirc(244),
/* 132 */   otilde(245),
/* 133 */   ouml(246),
/* 134 */   divide(247),
/* 135 */   oslash(248),
/* 136 */   ugrave(249),
/* 137 */   uacute(250),
/* 138 */   ucirc(251),
/* 139 */   uuml(252),
/* 140 */   yacute(253),
/* 141 */   thorn(254),
/* 142 */   yuml(255),
/* 143 */   fnof(402),
/* 144 */   Alpha(913),
/* 145 */   Beta(914),
/* 146 */   Gamma(915),
/* 147 */   Delta(916),
/* 148 */   Epsilon(917),
/* 149 */   Zeta(918),
/* 150 */   Eta(919),
/* 151 */   Theta(920),
/* 152 */   Iota(921),
/* 153 */   Kappa(922),
/* 154 */   Lambda(923),
/* 155 */   Mu(924),
/* 156 */   Nu(925),
/* 157 */   Xi(926),
/* 158 */   Omicron(927),
/* 159 */   Pi(928),
/* 160 */   Rho(929),
/* 161 */   Sigma(931),
/* 162 */   Tau(932),
/* 163 */   Upsilon(933),
/* 164 */   Phi(934),
/* 165 */   Chi(935),
/* 166 */   Psi(936),
/* 167 */   Omega(937),
/* 168 */   alpha(945),
/* 169 */   beta(946),
/* 170 */   gamma(947),
/* 171 */   delta(948),
/* 172 */   epsilon(949),
/* 173 */   zeta(950),
/* 174 */   eta(951),
/* 175 */   theta(952),
/* 176 */   iota(953),
/* 177 */   kappa(954),
/* 178 */   lambda(955),
/* 179 */   mu(956),
/* 180 */   nu(957),
/* 181 */   xi(958),
/* 182 */   omicron(959),
/* 183 */   pi(960),
/* 184 */   rho(961),
/* 185 */   sigmaf(962),
/* 186 */   sigma(963),
/* 187 */   tau(964),
/* 188 */   upsilon(965),
/* 189 */   phi(966),
/* 190 */   chi(967),
/* 191 */   psi(968),
/* 192 */   omega(969),
/* 193 */   thetasym(977),
/* 194 */   upsih(978),
/* 195 */   piv(982),
/* 196 */   bull(8226),
/* 197 */   hellip(8230),
/* 198 */   prime(8242),
/* 199 */   Prime(8243),
/* 200 */   oline(8254),
/* 201 */   frasl(8260),
/* 202 */   weierp(8472),
/* 203 */   image(8465),
/* 204 */   real(8476),
/* 205 */   trade(8482),
/* 206 */   alefsym(8501),
/* 207 */   larr(8592),
/* 208 */   uarr(8593),
/* 209 */   rarr(8594),
/* 210 */   darr(8595),
/* 211 */   harr(8596),
/* 212 */   crarr(8629),
/* 213 */   lArr(8656),
/* 214 */   uArr(8657),
/* 215 */   rArr(8658),
/* 216 */   dArr(8659),
/* 217 */   hArr(8660),
/* 218 */   forall(8704),
/* 219 */   part(8706),
/* 220 */   exist(8707),
/* 221 */   empty(8709),
/* 222 */   nabla(8711),
/* 223 */   isin(8712),
/* 224 */   notin(8713),
/* 225 */   ni(8715),
/* 226 */   prod(8719),
/* 227 */   sum(8721),
/* 228 */   minus(8722),
/* 229 */   lowast(8727),
/* 230 */   radic(8730),
/* 231 */   prop(8733),
/* 232 */   infin(8734),
/* 233 */   ang(8736),
/* 234 */   and(8743),
/* 235 */   or(8744),
/* 236 */   cap(8745),
/* 237 */   cup(8746),
/* 238 */   _int(8747),
/* 239 */   there4(8756),
/* 240 */   sim(8764),
/* 241 */   cong(8773),
/* 242 */   asymp(8776),
/* 243 */   ne(8800),
/* 244 */   equiv(8801),
/* 245 */   le(8804),
/* 246 */   ge(8805),
/* 247 */   sub(8834),
/* 248 */   sup(8835),
/* 249 */   nsub(8836),
/* 250 */   sube(8838),
/* 251 */   supe(8839),
/* 252 */   oplus(8853),
/* 253 */   otimes(8855),
/* 254 */   perp(8869),
/* 255 */   sdot(8901),
/* 256 */   lceil(8968),
/* 257 */   rceil(8969),
/* 258 */   lfloor(8970),
/* 259 */   rfloor(8971),
/* 260 */   lang(9001),
/* 261 */   rang(9002),
/* 262 */   loz(9674),
/* 263 */   spades(9824),
/* 264 */   clubs(9827),
/* 265 */   hearts(9829),
/* 266 */   diams(9830),
/* 267 */   quot(34),
/* 268 */   amp(38),
/* 269 */   lt(60),
/* 270 */   gt(62),
/* 271 */   OElig(338),
/* 272 */   oelig(339),
/* 273 */   Scaron(352),
/* 274 */   scaron(353),
/* 275 */   Yuml(376),
/* 276 */   circ(710),
/* 277 */   tilde(732),
/* 278 */   ensp(8194),
/* 279 */   emsp(8195),
/* 280 */   thinsp(8201),
/* 281 */   zwnj(8204),
/* 282 */   zwj(8205),
/* 283 */   lrm(8206),
/* 284 */   rlm(8207),
/* 285 */   ndash(8211),
/* 286 */   mdash(8212),
/* 287 */   lsquo(8216),
/* 288 */   rsquo(8217),
/* 289 */   sbquo(8218),
/* 290 */   ldquo(8220),
/* 291 */   rdquo(8221),
/* 292 */   bdquo(8222),
/* 293 */   dagger(8224),
/* 294 */   Dagger(8225),
/* 295 */   permil(8240),
/* 296 */   lsaquo(8249),
/* 297 */   rsaquo(8250),
/* 298 */   euro(8364);
/*     */   
/*     */   int code;
/*     */   
/*     */   Entity(int paramInt1) {
/* 303 */     this.code = paramInt1;
/*     */   }
/*     */   private static final Map<String, Entity> names; private static final Map<Integer, Entity> codes;
/*     */   static boolean isValid(String paramString) {
/* 307 */     return names.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isValid(int paramInt) {
/* 312 */     return (codes.containsKey(Integer.valueOf(paramInt)) || (32 <= paramInt && paramInt < 2127));
/*     */   }
/*     */   static {
/* 315 */     names = new HashMap<>();
/* 316 */     codes = new HashMap<>();
/*     */     
/* 318 */     for (Entity entity : values()) {
/* 319 */       String str = entity.name();
/* 320 */       int i = entity.code;
/* 321 */       if (str.startsWith("_")) str = str.substring(1); 
/* 322 */       names.put(str, entity);
/* 323 */       codes.put(Integer.valueOf(i), entity);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */