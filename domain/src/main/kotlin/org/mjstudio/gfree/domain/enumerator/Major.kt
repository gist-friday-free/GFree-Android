package org.mjstudio.gfree.domain.enumerator

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import org.mjstudio.gfree.domain.R
import org.mjstudio.gfree.domain.constant.Constant
import kotlin.random.Random

enum class Major(val code: String, val isCommon: Boolean) {
    GS("GS", true),
    UC("UC", true),
    MM("MM", true),
    ET("ET", true),
    MD("MD", true),
    CT("CT", true),
    IR("IR", true),
    CC("CC", true),

    PS("PS", false),
    CH("CH", false),
    BS("BS", false),
    EC("EC", false),
    MC("MC", false),
    MA("MA", false),
    EV("EV", false),

    NONE(Constant.DEFAULT_MAJOR, false),

    ;

    companion object {
        private val TAG = Major::class.java.simpleName

        /**
         * 순서에 유의
         */
        fun getAllMajor(): List<Major> {
            return listOf(GS, UC, MM, ET, MD, CT, IR, CC, PS, CH, BS, EC, MC, MA, EV)
        }

        fun getAllRegisterableMajor(): List<Major> {
            return listOf(GS,PS,CH,BS,EC,MC,MA,EV)
        }

        fun getMajorsForSearchFilter() : List<Major> {
            return listOf(NONE) + getAllRegisterableMajor()
        }

        /**
         * For PROFILE SETTINGS
         */
        fun getLeftMajor(major : Major): Major {
            val majors = getAllRegisterableMajor()
            val index = majors.indexOf(major)
            return majors.getOrElse(index-1, {majors.last()})
        }
        fun getRightMajor(major : Major) : Major {
            val majors = getAllRegisterableMajor()
            val index = majors.indexOf(major)
            return majors.getOrElse(index+1, {majors.first()})
        }

        fun getAllCommonMajor(): List<Major> = getAllMajor().filter { it.isCommon }

        fun getMajorWithCode(code: String): Major {
            return getAllMajor().firstOrNull { it.code == code.take(2).toUpperCase() } ?: NONE
        }

        fun getColorFromMajorAndIndex(context : Context,major: Major, index: Int): Int {
            val r = context.resources


            val array =
                    if (major.isCommon) r.getIntArray(R.array.COMMONCOLOR)
            else when (major) {
                PS -> r.getIntArray(R.array.PSCOLOR)
                CH -> r.getIntArray(R.array.CHCOLOR)
                BS -> r.getIntArray(R.array.BSCOLOR)
                EC -> r.getIntArray(R.array.ECCOLOR)
                MC -> r.getIntArray(R.array.MCCOLOR)
                MA -> r.getIntArray(R.array.MACOLOR)
                EV -> r.getIntArray(R.array.EVCOLOR)
                else -> r.getIntArray(R.array.COMMONCOLOR)
            }

            return try {
                array[index]
            }catch(ex : Exception) {
                getRandomColor()
            }
        }
        fun getRandomColor(): Int {
            return Color.argb(255, Random.nextInt() % 160,
                    Random.nextInt() % 160,
                    Random.nextInt() % 160)
        }


    }

    class StringAdapter(private val res: Resources) {
        private val TAG = StringAdapter::class.java.simpleName

        fun toUi(code : String) : String {
            return toUi(Major.getMajorWithCode(code) ?: Major.NONE)
        }

        fun toUi(major: Major): String {
            return when (major) {
                GS -> res.getString(R.string.major_gs)
                UC -> res.getString(R.string.major_uc)
                MM -> res.getString(R.string.major_mm)
                ET -> res.getString(R.string.major_et)
                MD -> res.getString(R.string.major_md)
                CT -> res.getString(R.string.major_ct)
                IR -> res.getString(R.string.major_ir)
                CC -> res.getString(R.string.major_cc)

                PS -> res.getString(R.string.major_ps)
                CH -> res.getString(R.string.major_ch)
                BS -> res.getString(R.string.major_bs)
                EC -> res.getString(R.string.major_ec)
                MC -> res.getString(R.string.major_mc)
                MA -> res.getString(R.string.major_ma)
                EV -> res.getString(R.string.major_ev)

                else -> res.getString(R.string.major_unknown)
            }
        }

        fun fromUi(name: String): Major {
            val strArray = res.getStringArray(R.array.major_string)
            val majorArray=Major.getAllMajor()

            val idx = strArray.indexOf(name)

            if(idx == -1) return Major.NONE

            return majorArray[idx]
        }
    }


}
