package com.github.aixcode.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation

/**
 * @author: tuchg
 * @date: 2020/8/6 21:46
 * @description:
 */
class ChineseLookupElement(
    private val codeCompletion: String
) : LookupElement() {

    /**
     * 据此进行文本匹配
     */
    override fun getLookupString(): String {
        return codeCompletion
    }

    /**
     * 控制该项在补全列表最终显示效果
     */
    override fun renderElement(presentation: LookupElementPresentation) {
        presentation.itemText = lookupString
    }

}
