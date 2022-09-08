package com.github.aixcode.completion.contributors

import com.github.aixcode.completion.ChineseLookupElement
import com.intellij.codeInsight.completion.CompletionResult
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement

/**
 * 针对GO的图标丢失等问题
 * @author: tuchg
 * @date: 2021/2/4 13:52
 */
class FixGoCompletionContributor : ChineseCompletionContributor() {
    override val renderElementHandle: (element: LookupElement, codeCompletion: String, priority: Double, rs: CompletionResultSet, r: CompletionResult) -> Unit =
        { element: LookupElement, codeCompletion: String, priority: Double, rs: CompletionResultSet, r: CompletionResult ->
            val chineseLookupElement = ChineseLookupElement(codeCompletion)
            rs.addElement(PrioritizedLookupElement.withPriority(chineseLookupElement, priority))
        }
}