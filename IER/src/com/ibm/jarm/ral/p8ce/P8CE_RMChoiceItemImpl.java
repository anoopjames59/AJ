/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.admin.Choice;
/*     */ import com.filenet.api.admin.LocalizedString;
/*     */ import com.filenet.api.collection.ChoiceList;
/*     */ import com.filenet.api.collection.LocalizedStringList;
/*     */ import com.filenet.api.constants.ChoiceType;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.ChoiceItemType;
/*     */ import com.ibm.jarm.api.meta.RMChoiceItem;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ class P8CE_RMChoiceItemImpl
/*     */   implements RMChoiceItem
/*     */ {
/*  30 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   static final String[] MandatoryPropertyNames = { "Id", "ChoiceIntegerValue", "ChoiceType", "ChoiceStringValue", "DisplayName", "DisplayNames", "Name", "ChoiceValues" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Choice jaceChoice;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMChoiceItemImpl(Choice jaceChoice)
/*     */   {
/*  51 */     Tracer.traceMethodEntry(new Object[] { jaceChoice });
/*  52 */     this.jaceChoice = jaceChoice;
/*  53 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getChoiceIntegerValue()
/*     */   {
/*  61 */     return this.jaceChoice.get_ChoiceIntegerValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChoiceItemType getChoiceItemType()
/*     */   {
/*  69 */     Tracer.traceMethodEntry(new Object[0]);
/*  70 */     ChoiceItemType result = null;
/*  71 */     ChoiceType jaceChoiceType = this.jaceChoice.get_ChoiceType();
/*  72 */     if (jaceChoiceType != null)
/*     */     {
/*  74 */       int intValue = jaceChoiceType.getValue();
/*  75 */       result = ChoiceItemType.getInstanceFromInt(intValue);
/*     */     }
/*     */     
/*  78 */     Tracer.traceMethodExit(new Object[] { result });
/*  79 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getChoiceStringValue()
/*     */   {
/*  87 */     return this.jaceChoice.get_ChoiceStringValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/*  95 */     return this.jaceChoice.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getDisplayNames()
/*     */   {
/* 104 */     Tracer.traceMethodEntry(new Object[0]);
/* 105 */     Map<String, String> result = null;
/* 106 */     LocalizedStringList jaceDisplayNames = this.jaceChoice.get_DisplayNames();
/* 107 */     if (jaceDisplayNames != null)
/*     */     {
/* 109 */       result = new HashMap();
/* 110 */       LocalizedString localizedString = null;
/*     */       
/*     */ 
/* 113 */       Iterator<LocalizedString> it = jaceDisplayNames.iterator();
/* 114 */       while ((it != null) && (it.hasNext()))
/*     */       {
/* 116 */         localizedString = (LocalizedString)it.next();
/* 117 */         if (localizedString != null)
/*     */         {
/* 119 */           String localeName = localizedString.get_LocaleName();
/* 120 */           String localizedText = localizedString.get_LocalizedText();
/* 121 */           if (localeName != null) {
/* 122 */             result.put(localeName, localizedText);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 127 */     Tracer.traceMethodExit(new Object[] { result });
/* 128 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMChoiceItem> getGroupChoiceItems()
/*     */   {
/* 137 */     Tracer.traceMethodEntry(new Object[0]);
/* 138 */     List<RMChoiceItem> result = null;
/* 139 */     ChoiceList jaceChoiceList = this.jaceChoice.get_ChoiceValues();
/* 140 */     if (jaceChoiceList != null)
/*     */     {
/* 142 */       Choice jaceSubChoice = null;
/* 143 */       result = new ArrayList();
/* 144 */       Iterator<Choice> it = jaceChoiceList.iterator();
/* 145 */       while ((it != null) && (it.hasNext()))
/*     */       {
/* 147 */         jaceSubChoice = (Choice)it.next();
/* 148 */         if (jaceSubChoice != null) {
/* 149 */           result.add(new P8CE_RMChoiceItemImpl(jaceSubChoice));
/*     */         }
/*     */       }
/*     */     }
/* 153 */     Tracer.traceMethodExit(new Object[] { result });
/* 154 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 162 */     Tracer.traceMethodEntry(new Object[0]);
/* 163 */     String result = null;
/* 164 */     Id choiceId = this.jaceChoice.get_Id();
/* 165 */     if (choiceId != null) {
/* 166 */       result = choiceId.toString();
/*     */     }
/* 168 */     Tracer.traceMethodExit(new Object[] { result });
/* 169 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 178 */     return this.jaceChoice.get_Name();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMChoiceItemImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */