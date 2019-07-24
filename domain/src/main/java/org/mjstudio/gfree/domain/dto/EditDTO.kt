package org.mjstudio.gfree.domain.dto

import androidx.annotation.StringRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.mjstudio.gfree.domain.entity.ClassData


data class EditDTO(
        @SerializedName("id")
        @Expose(serialize = false,deserialize = true)
        var id: Int = -1,
        @SerializedName("editClassData")
        @Expose(serialize = false,deserialize = true)
        var editClassData: ClassDataDTO,
        @SerializedName("editClass")
        @Expose
        var editClass : Int,
        @SerializedName("year")
        @Expose
        var year: Int,
        @SerializedName("semester")
        @Expose
        var semester: Int,
        @SerializedName("writer")
        @Expose
        var writer: String,
        @SerializedName("created")
        @Expose
        var created: Long,
        @SerializedName("type")
        @Expose
        var type : String,
        @SerializedName("value")
        @Expose
        var value : String,
        @SerializedName("star")
        @Expose(serialize = false,deserialize = true)
        var star : List<String>
) : DTO {

    /**
     * 어떤 종류의 Edit Request 인지 결정한다
     *
     * ADD : 새로운 과목을 생성
     *
     * REMOVE : 폐강
     *
     * CODE : 코드 변경
     *
     * NAME : 이름 변경
     *
     * GRADE : 학점 변경
     *
     * PROFESSOR : 교수 변경
     *
     * PLACE : 장소 변경
     *
     * SIZE : 정원 변경
     *
     * TIMEADD : 시간 추가
     *
     * TIMEREMOVE : 시간 삭제
     *
     * TIME : 기존 시간 변경
     */
    @Suppress("unused")
    enum class Type(@StringRes val strResId : Int) {
        ADD(org.mjstudio.gfree.domain.R.string.type_add),
        REMOVE(org.mjstudio.gfree.domain.R.string.type_remove),

        CODE(org.mjstudio.gfree.domain.R.string.type_code),
        NAME(org.mjstudio.gfree.domain.R.string.type_name),
        GRADE(org.mjstudio.gfree.domain.R.string.type_grade),
        PROFESSOR(org.mjstudio.gfree.domain.R.string.type_professor),
        PLACE(org.mjstudio.gfree.domain.R.string.type_place),
        SIZE(org.mjstudio.gfree.domain.R.string.type_size),

        TIMEADD(org.mjstudio.gfree.domain.R.string.type_timeadd),
        TIMEREMOVE(org.mjstudio.gfree.domain.R.string.type_timeremove),

        ;

        inner class ClassDataAdapter {
            fun toUi(classData : ClassData) : String {
                return when(this@Type) {
                    ADD->""
                    REMOVE->""
                    CODE->classData.code
                    NAME->classData.name
                    GRADE->classData.grade.toString()
                    PROFESSOR->classData.professor.toString()
                    PLACE->classData.place.toString()
                    SIZE->classData.size.toString()
                    TIMEADD->""
                    TIMEREMOVE->""
                }
            }
        }
    }

}

