package org.mjstudio.gfree.domain.enumerator

import android.content.res.Resources
import androidx.annotation.StringRes
import org.mjstudio.gfree.domain.R
import org.mjstudio.gfree.domain.constant.Constant

enum class Sex(val value : Int,@StringRes val resId : Int) {
    UNKNOWN(Constant.DEFAULT_SEX, R.string.sex_unknown),
    MALE(0,R.string.sex_male),
    FEMALE(1,R.string.sex_female)

    ;

    companion object {
        fun getAllSex() : List<Sex> = listOf(UNKNOWN,MALE,FEMALE)

        fun getSexWithValue(value : Int) : Sex{
            return getAllSex().first { it.value == value }
        }

        fun getLeftSex(sex : Sex) : Sex {
            val allSex = getAllSex()
            val index = allSex.indexOf(sex)

            return allSex.getOrElse(index-1){allSex.last()}
        }
        fun getRightSex(sex : Sex) : Sex {
            val allSex = getAllSex()
            val index = allSex.indexOf(sex)

            return allSex.getOrElse(index+1){allSex.first()}
        }

    }

    class StringAdapter(private val res : Resources) {
        fun toUi(sex : Int): String {
            return when (sex) {
                Constant.DEFAULT_SEX -> res.getString(R.string.sex_unknown)
                0 -> res.getString(R.string.sex_male)
                1 -> res.getString(R.string.sex_female)
                else -> res.getString(R.string.sex_unknown)
            }
        }

        fun fromUi(str : String): Int {
            val strUnknown = res.getString(R.string.sex_unknown)
            val strMale = res.getString(R.string.sex_male)
            val strFemale = res.getString(R.string.sex_female)


            return if(str == strUnknown)
                Constant.DEFAULT_SEX
                else if(str == strMale)
                0
                else if(str == strFemale)
                1
                else
                Constant.DEFAULT_SEX
        }
    }
}