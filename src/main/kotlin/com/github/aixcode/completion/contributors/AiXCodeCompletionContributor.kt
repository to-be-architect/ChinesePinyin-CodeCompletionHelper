package com.github.aixcode.completion.contributors

import com.github.aixcode.completion.ChineseLookupElement
import com.github.aixcode.completion.CodePrefixMatcher
import com.github.aixcode.config.PluginSettingsState
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author tuchg
 * @date 2020-8-1
 */
// 自定义了的语言
val languages = arrayOf("Go", "Kotlin", "C#")
val code = """
open val renderElementHandle: (element: LookupElement, pinyin: String, priority: Double, rs: CompletionResultSet, r: CompletionResult) -> Unit =
        { element, pinyin, priority, rs, r ->
            val chineseLookupElement = ChineseLookupElement(element.lookupString, pinyin).copyFrom(element)
            val withPriority = PrioritizedLookupElement.withPriority(chineseLookupElement, priority)
            val wrap = CompletionResult.wrap(withPriority, r.prefixMatcher, r.sorter)
            rs.passResult(wrap!!)
        }
            """.trimIndent()

open class ChineseCompletionContributor() : CompletionContributor() {
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val pluginSettingsState = PluginSettingsState.instance

        // 手工过滤没必要执行的贡献器流程
        if (languages.contains(parameters.originalFile.fileType.name) && this.javaClass.simpleName == "ChineseCompletionContributor") {
            return
        }

        //feature:可暴力解决 bug:二次激活获取补全 但性能影响较大
        if (pluginSettingsState.enableForceCompletion) {
            parameters.withInvocationCount(2)
        }

        val prefix = result.prefixMatcher.prefix
        println("prefix=${prefix}")

        val resultSet = result.withPrefixMatcher(CodePrefixMatcher(result.prefixMatcher))
        resultSet.addLookupAdvertisement("Rocket RSD")

        if (prefix.equals("rsd", true)) {
            resultSet.addElement(PrioritizedLookupElement.withPriority(ChineseLookupElement(code), 1000.0))
        }

        // 先跳过当前 Contributors 获取包装后的 lookupElement而后进行修改装饰
        resultSet.runRemainingContributors(parameters) { r ->
            val element = r.lookupElement
            println(element.lookupString)
            resultSet.passResult(r)
            renderElementHandle(element, code, 500.0, resultSet, r)
        }

        // 修复 输入单个字符本贡献器无响应
        resultSet.restartCompletionWhenNothingMatches()
    }

    open val renderElementHandle: (element: LookupElement, codeCompletion: String, priority: Double, rs: CompletionResultSet, r: CompletionResult) -> Unit =
        { element, pinyin, priority, rs, r ->
            val withPriority = PrioritizedLookupElement.withPriority(element, priority)
            val wrap = CompletionResult.wrap(withPriority, r.prefixMatcher, r.sorter)
            rs.passResult(wrap!!)
        }
}
