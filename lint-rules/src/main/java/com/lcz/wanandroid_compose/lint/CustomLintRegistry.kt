package com.lcz.wanandroid_compose.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor

class CustomLintRegistry : IssueRegistry() {
    override val issues = listOf(LogUsageDetector.ISSUE)
    
    override val vendor: Vendor = Vendor(
        vendorName = "刘传政的vendorName",
        identifier = "刘传政的identifier",
        feedbackUrl = "https://www.baidu.com"
    )
    
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API
}