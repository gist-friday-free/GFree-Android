package org.mjstudio.ggonggang.util

import org.mjstudio.gfree.domain.entity.ClassData



typealias DuplicateCheckResult = Pair<Boolean,String?>

object ClassDataUtil {
    fun checkDuplicate(registeredClasses: List<ClassData>?, newClass: ClassData): DuplicateCheckResult {
        registeredClasses ?: return DuplicateCheckResult(false,null)

        var result = DuplicateCheckResult(false,null)

        outer@ for (registerClass in registeredClasses) {
            for (registerTime in registerClass.time) {
                for (newTime in newClass.time) {
                    if (newTime.isDuplicate(registerTime)) {
                        result = result.copy(true,registerClass.name)
                        break@outer
                    }
                }
            }
        }

        return result
    }

}