package com.github.mrchuvyzgalov.urlinspection

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression

class UrlHashCodeInspection : AbstractBaseJavaLocalInspectionTool() {

    companion object {
        const val PROBLEM_DESCRIPTION = "In Java, calling java.net.Url#hashCode() is considered a bad API. This is because to compute the hash code it tries to resolve the host of the URL. This behaviour is unexpected and calling it may lead to performance problems if not handled correctly."
        const val METHOD_NAME = "hashCode"
        const val CLASS_NAME = "java.net.URL"
    }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {

        return object : JavaElementVisitor() {


            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                super.visitMethodCallExpression(expression)

                if (expression != null) {
                    val methodName = expression.resolveMethod()?.name
                    val className = expression.resolveMethod()?.containingClass?.qualifiedName

                    if (isDesiredMethod(methodName, className)) {
                        holder.registerProblem(expression, PROBLEM_DESCRIPTION)
                    }
                }
            }

            private fun isDesiredMethod(methodName: String?, className: String?): Boolean {
                return methodName == METHOD_NAME && className == CLASS_NAME
            }
        }
    }


}