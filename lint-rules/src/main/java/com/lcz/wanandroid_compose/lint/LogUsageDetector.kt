package com.lcz.wanandroid_compose.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class LogUsageDetector : Detector(), Detector.UastScanner {
    
    companion object {
        val ISSUE = Issue.create(
            id = "DirectLogUsage",//写@SuppressLint时，使用的名字
            briefDescription = "避免直接使用Android Log",
            explanation = "请使用项目中的LogUtil代替Android原生的Log类，以便统一日志管理",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                LogUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableMethodNames(): List<String> {
        return listOf("v", "d", "i", "w", "e", "wtf")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            val message = "请使用LogUtil代替Android Log"
            val location = context.getLocation(node)
            context.report(ISSUE, node, location, message)
        }
    }
}