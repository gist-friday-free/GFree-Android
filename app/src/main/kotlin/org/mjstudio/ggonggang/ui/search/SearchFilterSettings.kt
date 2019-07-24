package org.mjstudio.ggonggang.ui.search

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.ggonggang.BR

@Parcelize
class SearchFilterSettings: Parcelable, BaseObservable() {

    @get:Bindable
    var major: String = Constant.DEFAULT_MAJOR
        set(value) {
            field = value
            notifyPropertyChanged(BR.major)
        }

    @get:Bindable
    var possible: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.possible)
        }

    @get:Bindable
    var grade: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.grade)
        }

    @get:Bindable
    var gradeValue: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.gradeValue)
        }

}