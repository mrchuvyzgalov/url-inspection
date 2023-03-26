package com.github.mrchuvyzgalov.urlinspection

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase


@TestDataPath(value = "src/test/testData")
class UrlHashCodeInspectionTest : LightJavaCodeInsightFixtureTestCase() {
    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(UrlHashCodeInspection())
        myFixture.addClass("package java.net; public final class URL { @Override public int hashCode() { return 0; } public int getNum() { return 0; } }")
        myFixture.addClass("package java.lang; public final class Integer { @Override public int hashCode() { return 0; } } ")
    }

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    /**
     * URL.hashCode()
     */
    fun `test url hashCode`() {
        myFixture.configureByFile("UrlHashCode.java")
        val highlightInfos = myFixture.doHighlighting()

        assertTrue(highlightInfos.any { it.description == UrlHashCodeInspection.PROBLEM_DESCRIPTION })
    }

    /**
     * Int.hashCode()
     */
    fun `test not url hashCode`() {
        myFixture.configureByFile("IntHashCode.java")
        val highlightInfos = myFixture.doHighlighting()

        assertTrue(highlightInfos.all { it.description != UrlHashCodeInspection.PROBLEM_DESCRIPTION })
    }

    /**
     * URL.getNum()
     */
    fun `test url not hashCode`() {
        myFixture.configureByFile("UrlGetNum.java")
        val highlightInfos = myFixture.doHighlighting()

        assertTrue(highlightInfos.all { it.description != UrlHashCodeInspection.PROBLEM_DESCRIPTION })
    }
}