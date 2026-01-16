/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lcz.wanandroid_compose

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.action.ViewActions.swipeLeft
import com.lcz.wanandroid_compose.module.demo.DemoPage
import com.lcz.wanandroid_compose.navigation.AppNavGraph
import com.lcz.wanandroid_compose.navigation.AppRoutePath
import com.lcz.wanandroid_compose.theme.WanAndroid_composeTheme
import org.junit.Rule
import org.junit.Test
//运行完测试后，app会自动卸载。运行时需要用户手动有个点击确认是否启动测试。
class AppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_launches() {
        // Check app launches at the correct destination
        composeTestRule
            .onNodeWithText("首页")//检查是否有首页文字的节点
            .assertIsDisplayed()//检查是否显示
        composeTestRule
            .onNodeWithText("我的")
            .assertIsDisplayed().performClick()
    }

    @Test
    fun app_canNavigateToAllScreens() {
        // Check app launches at HOME
        composeTestRule.onNodeWithText("HOME").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android's picks").assertIsDisplayed()

        // Navigate to Search
        composeTestRule.onNodeWithText("SEARCH").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()

        // Navigate to Cart
        composeTestRule.onNodeWithText("MY CART").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("Order (3 items)").assertIsDisplayed()

        // Navigate to Profile
        composeTestRule.onNodeWithText("PROFILE").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("This is currently work in progress").assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToDetailPage() {
        composeTestRule.onNodeWithText("Chips").performClick()
        composeTestRule.onNodeWithText("Lorem ipsum", substring = true).assertIsDisplayed()
    }
}
class AppTest2 {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun app_demo() {
        composeTestRule.setContent {
            WanAndroid_composeTheme(themeType = 0) {
                DemoPage(AppRoutePath.Demo())
            }
        }
        composeTestRule.onRoot().printToLog("TAG")//打印语义树。会打印到logcat中
        composeTestRule
//            .onNode(hasText("滑动条"))
            .onNodeWithText("滑动条")//检查是否有滑动条文字的节点
            .assertIsDisplayed()//检查是否显示
        composeTestRule.onNodeWithText("通知Demo").performClick().assertIsDisplayed()//点击
    }
    @Test
    fun app_all() {
        composeTestRule.setContent {
            WanAndroid_composeTheme(themeType = 0) {
                AppNavGraph()
            }
        }
        composeTestRule.onRoot().printToLog("TAG")//打印语义树。会打印到logcat中
        // Wait 2 seconds before navigating
        composeTestRule
            .onNodeWithText("我的")
            .assertIsDisplayed().performClick()
        composeTestRule
            .onNodeWithText("demo")
            .assertIsDisplayed().performClick()

        composeTestRule
            .onNodeWithText("滑动条")
            .assertIsDisplayed().performClick()
    }

}
