/*     */ package com.ibm.ier.report.util;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryParser
/*     */ {
/*  15 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*     */   private String queryStmnt;
/*     */   
/*     */   public static class SelectItem
/*     */   {
/*  20 */     public String propName = null;
/*  21 */     public String alias = null;
/*  22 */     public String tableName = null;
/*  23 */     public int propType = 0;
/*  24 */     public int propSize = 0;
/*  25 */     public int cardinality = 0;
/*  26 */     public int startsAt = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class FromClass
/*     */   {
/*  32 */     public String className = null;
/*  33 */     public String alias = null;
/*  34 */     public int startsAt = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class WhereCondition
/*     */   {
/*  40 */     public String condition = null;
/*  41 */     public String operator = null;
/*  42 */     public int startsAt = 0;
/*     */   }
/*     */   
/*     */ 
/*  46 */   private int m_ptr = 0;
/*  47 */   private boolean m_selectparsed = false;
/*  48 */   private boolean m_fromparsed = false;
/*  49 */   private boolean m_whereparsed = false;
/*     */   
/*     */ 
/*  52 */   private Vector vSelectList = null;
/*  53 */   private Vector vFromClasses = null;
/*  54 */   private Vector vWhereConditions = null;
/*  55 */   private boolean bhastop = false;
/*     */   
/*  57 */   private boolean btoppercent = false;
/*     */   
/*     */ 
/*  60 */   private int topval = 0;
/*  61 */   private boolean bdistinct = false;
/*     */   
/*     */ 
/*     */   public QueryParser(String queryStatement)
/*     */   {
/*  66 */     Tracer.traceMethodEntry(new Object[] { queryStatement });
/*  67 */     this.queryStmnt = queryStatement;
/*  68 */     this.vSelectList = new Vector();
/*  69 */     this.vFromClasses = new Vector();
/*  70 */     this.vWhereConditions = new Vector();
/*  71 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public SelectItem[] getSelectList()
/*     */   {
/*  76 */     Tracer.traceMethodEntry(new Object[0]);
/*  77 */     if (!this.m_selectparsed) { parseSelectStmnt();
/*     */     }
/*  79 */     SelectItem[] retArray = new SelectItem[this.vSelectList.size()];
/*  80 */     Object[] obj = this.vSelectList.toArray();
/*     */     
/*  82 */     for (int i = 0; i < obj.length; i++) {
/*  83 */       retArray[i] = ((SelectItem)obj[i]);
/*     */     }
/*  85 */     Tracer.traceMethodExit(retArray);
/*  86 */     return retArray;
/*     */   }
/*     */   
/*     */   public FromClass[] getFromClasses()
/*     */   {
/*  91 */     Tracer.traceMethodEntry(new Object[0]);
/*  92 */     if (!this.m_fromparsed) { parseFromClause();
/*     */     }
/*  94 */     FromClass[] retArray = new FromClass[this.vFromClasses.size()];
/*  95 */     Object[] obj = this.vFromClasses.toArray();
/*     */     
/*  97 */     for (int i = 0; i < obj.length; i++) {
/*  98 */       retArray[i] = ((FromClass)obj[i]);
/*     */     }
/* 100 */     Tracer.traceMethodExit(retArray);
/* 101 */     return retArray;
/*     */   }
/*     */   
/*     */   public WhereCondition[] getWhereConditions()
/*     */   {
/* 106 */     Tracer.traceMethodEntry(new Object[0]);
/* 107 */     if (!this.m_whereparsed) { parseWhereClause();
/*     */     }
/* 109 */     WhereCondition[] retArray = new WhereCondition[this.vWhereConditions.size()];
/* 110 */     Object[] obj = this.vWhereConditions.toArray();
/*     */     
/* 112 */     for (int i = 0; i < obj.length; i++) {
/* 113 */       retArray[i] = ((WhereCondition)obj[i]);
/*     */     }
/* 115 */     Tracer.traceMethodExit(retArray);
/* 116 */     return retArray;
/*     */   }
/*     */   
/*     */   public boolean hasDistinct()
/*     */   {
/* 121 */     Tracer.traceMethodEntry(new Object[0]);
/* 122 */     if (!this.m_selectparsed) parseSelectStmnt();
/* 123 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(this.bdistinct) });
/* 124 */     return this.bdistinct;
/*     */   }
/*     */   
/*     */   public boolean hasTop() {
/* 128 */     Tracer.traceMethodEntry(new Object[0]);
/* 129 */     if (!this.m_selectparsed) parseSelectStmnt();
/* 130 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(this.bhastop) });
/* 131 */     return this.bhastop;
/*     */   }
/*     */   
/*     */   public int getTopValue()
/*     */   {
/* 136 */     Tracer.traceMethodEntry(new Object[0]);
/* 137 */     if (!this.m_selectparsed) { parseSelectStmnt();
/*     */     }
/* 139 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(this.topval) });
/* 140 */     return this.topval;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSelectFromStatement()
/*     */   {
/* 146 */     Tracer.traceMethodEntry(new Object[0]);
/* 147 */     String results = null;
/* 148 */     this.m_ptr = 0;
/* 149 */     while (this.m_ptr < this.queryStmnt.length())
/*     */     {
/* 151 */       String nextword = nextWord();
/* 152 */       if ((nextword != null) && (nextword.compareToIgnoreCase("WHERE") == 0))
/*     */         break;
/*     */     }
/* 155 */     if (this.m_ptr >= this.queryStmnt.length())
/*     */     {
/* 157 */       results = this.queryStmnt;
/*     */     }
/*     */     else
/*     */     {
/* 161 */       results = this.queryStmnt.substring(0, this.m_ptr - 6);
/*     */     }
/* 163 */     Tracer.traceMethodExit(new Object[] { results });
/* 164 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String nextWord()
/*     */   {
/* 171 */     Tracer.traceMethodEntry(new Object[0]);
/* 172 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 174 */     char c = this.queryStmnt.charAt(this.m_ptr);
/* 175 */     while (Character.isWhitespace(c))
/*     */     {
/* 177 */       this.m_ptr += 1;
/* 178 */       if (this.m_ptr >= this.queryStmnt.length()) break;
/* 179 */       c = this.queryStmnt.charAt(this.m_ptr);
/*     */     }
/*     */     
/* 182 */     if (this.m_ptr < this.queryStmnt.length())
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */ 
/* 187 */         c = this.queryStmnt.charAt(this.m_ptr++);
/* 188 */         if (!Character.isWhitespace(c))
/*     */         {
/* 190 */           buff.append(c);
/* 191 */           if (c != ',') if (this.m_ptr >= this.queryStmnt.length())
/*     */               break;
/*     */         }
/*     */       } }
/* 195 */     Tracer.traceMethodExit(new Object[] { buff });
/* 196 */     return buff.toString();
/*     */   }
/*     */   
/*     */   private void parseSelectStmnt()
/*     */   {
/* 201 */     Tracer.traceMethodEntry(new Object[0]);
/* 202 */     this.m_ptr = 0;
/* 203 */     while (this.m_ptr < this.queryStmnt.length())
/*     */     {
/* 205 */       String nextword = nextWord();
/* 206 */       if ((nextword != null) && (nextword.compareToIgnoreCase("SELECT") == 0))
/*     */         break;
/*     */     }
/* 209 */     if (this.m_ptr >= this.queryStmnt.length()) {
/* 210 */       return;
/*     */     }
/* 212 */     String lastword = null;
/* 213 */     SelectItem entry = new SelectItem();
/* 214 */     boolean bdone = false;
/* 215 */     boolean bdonecolumn = false;
/* 216 */     while ((this.m_ptr < this.queryStmnt.length()) && (!bdone))
/*     */     {
/* 218 */       String nextword = nextWord();
/* 219 */       if ((nextword != null) && (nextword.length() > 0))
/*     */       {
/*     */ 
/* 222 */         if (nextword.equalsIgnoreCase("TOP")) {
/* 223 */           this.bhastop = true;
/* 224 */         } else if (nextword.equalsIgnoreCase("ALL")) {
/* 225 */           this.bdistinct = false;
/* 226 */         } else if (nextword.equalsIgnoreCase("DISTINCT")) {
/* 227 */           this.bdistinct = true;
/* 228 */         } else if (nextword.equalsIgnoreCase("PERCENT")) {
/* 229 */           this.btoppercent = true;
/* 230 */         } else if (nextword.equalsIgnoreCase("FROM")) {
/* 231 */           bdone = true;
/* 232 */         } else if (nextword.equalsIgnoreCase(",")) {
/* 233 */           bdonecolumn = true;
/* 234 */         } else if ((lastword != null) && (lastword.equalsIgnoreCase("TOP")))
/*     */         {
/*     */           try
/*     */           {
/* 238 */             int strval = Integer.parseInt(nextword);
/* 239 */             this.topval = strval;
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (NumberFormatException e) {}
/*     */ 
/*     */         }
/* 246 */         else if (!nextword.equalsIgnoreCase("AS"))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 257 */           int colpos = 0;
/* 258 */           StringBuffer colbuff = new StringBuffer();
/* 259 */           boolean inbracket = false;
/* 260 */           entry.startsAt = (this.m_ptr - nextword.length());
/*     */           
/* 262 */           if (nextword.charAt(nextword.length() - 1) != ',')
/*     */           {
/* 264 */             entry.startsAt -= 1;
/*     */           }
/* 266 */           while (colpos < nextword.length())
/*     */           {
/* 268 */             char c = nextword.charAt(colpos++);
/* 269 */             switch (c)
/*     */             {
/*     */             case '.': 
/* 272 */               if (!inbracket)
/*     */               {
/* 274 */                 entry.tableName = colbuff.toString();
/* 275 */                 colbuff = new StringBuffer();
/*     */               }
/*     */               else {
/* 278 */                 colbuff.append(c); }
/* 279 */               break;
/*     */             case ',': 
/* 281 */               bdonecolumn = true;
/* 282 */               break;
/*     */             case '[': 
/* 284 */               if (colbuff.length() > 0)
/*     */               {
/* 286 */                 entry.propName = colbuff.toString();
/* 287 */                 colbuff = new StringBuffer();
/*     */               }
/* 289 */               inbracket = true;
/* 290 */               break;
/*     */             
/*     */             case ']': 
/* 293 */               if (entry.propName == null) {
/* 294 */                 entry.propName = colbuff.toString();
/*     */               } else
/* 296 */                 entry.alias = colbuff.toString();
/* 297 */               colbuff = new StringBuffer();
/* 298 */               inbracket = false;
/* 299 */               break;
/*     */             default: 
/* 301 */               colbuff.append(c);
/*     */             }
/*     */             
/*     */           }
/* 305 */           if (colbuff.length() > 0)
/*     */           {
/* 307 */             if (entry.propName == null)
/*     */             {
/* 309 */               entry.propName = colbuff.toString();
/*     */             }
/*     */             else
/*     */             {
/* 313 */               entry.alias = colbuff.toString();
/*     */             }
/*     */           }
/*     */         }
/* 317 */         lastword = nextword;
/*     */       }
/*     */       
/* 320 */       if (((bdonecolumn) || (bdone)) && (entry.propName != null))
/*     */       {
/*     */ 
/* 323 */         this.vSelectList.add(entry);
/* 324 */         entry = new SelectItem();
/* 325 */         bdonecolumn = false;
/*     */       }
/*     */     }
/* 328 */     this.m_selectparsed = true;
/* 329 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   private void parseFromClause()
/*     */   {
/* 334 */     Tracer.traceMethodEntry(new Object[0]);
/* 335 */     this.m_ptr = 0;
/* 336 */     while (this.m_ptr < this.queryStmnt.length())
/*     */     {
/* 338 */       String nextword = nextWord();
/* 339 */       if ((nextword != null) && (nextword.compareToIgnoreCase("FROM") == 0))
/*     */         break;
/*     */     }
/* 342 */     if (this.m_ptr >= this.queryStmnt.length()) {
/* 343 */       return;
/*     */     }
/* 345 */     String lastword = null;
/* 346 */     FromClass entry = new FromClass();
/* 347 */     boolean bdone = false;
/* 348 */     while ((this.m_ptr < this.queryStmnt.length()) && (!bdone))
/*     */     {
/* 350 */       String nextword = nextWord();
/* 351 */       if ((nextword != null) && (nextword.length() > 0))
/*     */       {
/*     */ 
/* 354 */         if ((!nextword.equalsIgnoreCase("AS")) && (!nextword.equalsIgnoreCase("WITH")) && (!nextword.equalsIgnoreCase("ON")) && (!nextword.equalsIgnoreCase("CROSS")) && (!nextword.equalsIgnoreCase("JOIN")) && (!nextword.equalsIgnoreCase("INNER")) && (!nextword.equalsIgnoreCase("OUTER")) && (!nextword.equalsIgnoreCase("LEFT")) && (!nextword.equalsIgnoreCase("RIGHT")) && (!nextword.equalsIgnoreCase("FULL")) && (!nextword.equalsIgnoreCase("WHERE")) && (!nextword.equalsIgnoreCase("=")))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 370 */           int colpos = 0;
/* 371 */           StringBuffer tblbuff = new StringBuffer();
/* 372 */           while (colpos < nextword.length())
/*     */           {
/* 374 */             char c = nextword.charAt(colpos++);
/* 375 */             switch (c)
/*     */             {
/*     */             case '(': 
/*     */             case ')': 
/*     */               break;
/*     */             default: 
/* 381 */               tblbuff.append(c);
/*     */             }
/*     */             
/*     */           }
/* 385 */           if (tblbuff.length() > 0)
/*     */           {
/* 387 */             if (lastword == null)
/*     */             {
/* 389 */               entry.className = tblbuff.toString();
/* 390 */             } else if (lastword.equalsIgnoreCase("JOIN"))
/*     */             {
/* 392 */               entry.className = tblbuff.toString();
/* 393 */             } else if (entry.className != null)
/*     */             {
/* 395 */               entry.alias = tblbuff.toString();
/* 396 */               this.vFromClasses.add(entry);
/* 397 */               entry = new FromClass();
/*     */             }
/*     */           }
/*     */         }
/* 401 */         lastword = nextword;
/*     */       }
/*     */     }
/* 404 */     if (entry.className != null)
/*     */     {
/* 406 */       this.vFromClasses.add(entry);
/*     */     }
/*     */     
/* 409 */     this.m_fromparsed = true;
/* 410 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   private void parseWhereClause()
/*     */   {
/* 415 */     Tracer.traceMethodEntry(new Object[0]);
/* 416 */     this.m_ptr = 0;
/* 417 */     while (this.m_ptr < this.queryStmnt.length())
/*     */     {
/* 419 */       String nextword = nextWord();
/* 420 */       if ((nextword != null) && (nextword.compareToIgnoreCase("WHERE") == 0))
/*     */         break;
/*     */     }
/* 423 */     if (this.m_ptr >= this.queryStmnt.length()) {
/* 424 */       return;
/*     */     }
/* 426 */     WhereCondition entry = new WhereCondition();
/* 427 */     boolean bdone = false;
/* 428 */     int conditionPos = this.m_ptr;
/*     */     
/* 430 */     StringBuffer conditionBuff = new StringBuffer();
/* 431 */     while ((this.m_ptr < this.queryStmnt.length()) && (!bdone))
/*     */     {
/* 433 */       String nextword = nextWord();
/* 434 */       if ((nextword != null) && (nextword.length() > 0))
/*     */       {
/*     */ 
/* 437 */         if ((nextword.equalsIgnoreCase("AND")) || (nextword.equalsIgnoreCase("OR")))
/*     */         {
/*     */ 
/* 440 */           this.vWhereConditions.add(entry);
/* 441 */           entry = new WhereCondition();
/* 442 */           entry.operator = nextword;
/* 443 */           entry.startsAt = this.m_ptr;
/* 444 */           conditionPos = this.m_ptr;
/* 445 */           conditionBuff = new StringBuffer();
/*     */         }
/*     */       }
/*     */       
/* 449 */       while (conditionPos < this.m_ptr)
/*     */       {
/* 451 */         char c = this.queryStmnt.charAt(conditionPos++);
/* 452 */         conditionBuff.append(c);
/*     */       }
/*     */       
/* 455 */       entry.condition = conditionBuff.toString();
/*     */     }
/*     */     
/* 458 */     this.vWhereConditions.add(entry);
/* 459 */     this.m_whereparsed = true;
/* 460 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\util\QueryParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */