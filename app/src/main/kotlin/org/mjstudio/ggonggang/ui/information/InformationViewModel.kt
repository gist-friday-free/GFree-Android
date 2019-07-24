package org.mjstudio.ggonggang.ui.information

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.CustomMsg
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.NegativeMsg.CLASS_DUPLICATE
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.PositiveMsg
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.entity.UserInfo
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.enumerator.Sex
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.ReviewRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R.color
import org.mjstudio.ggonggang.databinding.ItemInfoTimeBinding
import org.mjstudio.ggonggang.util.ClassDataUtil
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class InformationViewModel @Inject constructor (
        private val classDataRepository: ClassDataRepository,
        private val reviewRepository: ReviewRepository,
        private val userRepository: UserRepository,
        private val authRepository: FirebaseAuthRepository,
        val classData : ClassData
) : ViewModel() {
    private val TAG = InformationViewModel::class.java.simpleName
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    //region DATA
    val majorMap : MutableLiveData<Map<String,Int>> = MutableLiveData(mapOf())
    val sexMap : MutableLiveData<Map<Int,Int>> = MutableLiveData(mapOf())
    val idMap : MutableLiveData<Map<Int,Int>> = MutableLiveData(mapOf())
    val ageMap : MutableLiveData<Map<Int,Int>> = MutableLiveData(mapOf())


    val usersInClass : MutableLiveData<List<UserInfo>> = MutableLiveData(listOf())
    val usersCountText = Transformations.map(usersInClass) {
        usersInClass.value?.let {users->
            return@map "${users.size}/${classData.size}"
        }

        return@map "..."
    }

    val registerDetailOpen : MutableLiveData<Boolean> = MutableLiveData(true)
    val reviewDetailOpen : MutableLiveData<Boolean> = MutableLiveData(false)

    val reviewsInClass = MutableLiveData<List<ReviewDTO>>()
    val reviewCountText = Transformations.map(reviewsInClass) {
        reviewsInClass.value?.let {reviews->
            return@map "${reviews.size}"
        }

        return@map "..."
    }

    val addButtonEnable:MutableLiveData<Boolean> = MutableLiveData(true)
    //endregion

    //region EVENT
    val msg = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()
    val clickBack = MutableLiveData<Once<Boolean>>()
    //endregion

    init {
        requestDatas()
        checkAddEnable()
    }

    private fun requestDatas() {
        compositeDisposable += classDataRepository.getClassData(classData.id)
                .addSchedulers()
                .subscribe({
                }, {
                    msg.value = Once(NegativeMsg.CLASS_DATA_GET_FAIL)
                })

        compositeDisposable +=  classDataRepository.getUsersInClass(classData.id)
                .addSchedulers()
                .subscribe({

                    val entities = it.toEntity().toList()

                    usersInClass.value = entities
                    processUsersInfo(entities)
                }, {
                    msg.value = Once(NegativeMsg.CLASS_USER_GET_FAIL)
                })

        //TODO 한줄평Hide
        /**
        compositeDisposable += classDataRepository.getReviewsInClass(classData.id)
                .addSchedulers()
                .subscribe({ reviews ->
                    debugE(TAG,reviews)
                    reviewsInClass.value = reviews
                }, {
                    debugE(TAG,it.message)
                    msg.value = Once(NegativeMsg.CLASS_REVIEW_GET_FAIL)
                })
        **/

    }
    private fun checkAddEnable() {
        val checkDuplicateResult = ClassDataUtil.checkDuplicate(userRepository.getRegisteredClassDataLiveData().value!!,classData)
        //만약 겹친다면
        if(checkDuplicateResult.first) {
            addButtonEnable.value = false
        }else {
            addButtonEnable.value = true
        }
    }


    private fun processUsersInfo(users: List<UserInfo>) {
        // User Count Set

        // Major Chart Set
        val majorGroup = users.groupBy { it.major }
        majorMap.value = majorGroup.map { kotlin.Pair(it.key, it.value.size) }.toMap()

        // ID Chart Set
        val idGroup = users.groupBy { it.id }
        idMap.value = idGroup.map { kotlin.Pair(it.key, it.value.size) }.toMap()

        // Age Chart Set
        val ageGroup = users.groupBy { it.age }
        ageMap.value = ageGroup.map { kotlin.Pair(it.key, it.value.size) }.toMap()

        // Sex Chart Set
        val sexGroup = users.groupBy { it.sex }
        sexMap.value = sexGroup.map { kotlin.Pair(it.key, it.value.size) }.toMap()

    }

    private fun saveReview(classData: ClassData, body: String) {
        val curUser = authRepository.getCurrentUser() ?: return
        val email = curUser.email ?: return

        val data: ReviewDTO = ReviewDTO(-1,classData.id, email, body, Date().time)

        val d = reviewRepository.createReview(data)
                .addSchedulers()
                .subscribe({
                    // REVIEW CREATE SUCCESS
                }, {
                    // REVIEW CREATE FAIL
                })
        compositeDisposable?.add(d)
    }

    fun onClickBackButton() {
        clickBack.value = Once(true)
    }
    fun onClickReviewDetailButton() {
        reviewDetailOpen.value = !(reviewDetailOpen.value ?: false)
    }
    fun onClickRegisterDetailButton() {
        registerDetailOpen.value = !(registerDetailOpen.value ?: false)
    }
    fun onClickAddButton() {
        val checkDuplicateResult = ClassDataUtil.checkDuplicate(userRepository.getRegisteredClassDataLiveData().value!!,classData)
        //만약 겹친다면
        if(checkDuplicateResult.first) {
            msg.value = Once(CustomMsg("${CLASS_DUPLICATE.msg}\nwith ${checkDuplicateResult.second ?: ""}"))
        }else {

            authRepository.getUid()?.let {uid->
                compositeDisposable+=userRepository.registerClass(uid,classData)
                        .addSchedulers()
                        .subscribe({
                            //추가에 성공한다면
                            userRepository.addRegisteredClassDataToCache(it)
                            snackMsg.value = Once(PositiveMsg.CLASS_REGISTER_SUCCESS)
                            addButtonEnable.value = false
                        }, {
                            snackMsg.value = Once(NegativeMsg.CLASS_REGISTER_FAIL)
                        })
            }

        }
    }
}

@BindingAdapter("app:childTimeViews")
fun LinearLayout.setChildTimeViews(classData : ClassData) {
    this.removeAllViews()
    val inflater = LayoutInflater.from(context)
    for(time in classData.time) {

        val binding = ItemInfoTimeBinding.inflate(inflater,this,false)
        binding.time = time
        this.addView(binding.root)
    }

}

@BindingAdapter("app:setPieChart")
fun PieChartView.setData(data : Map<out Any,Int>) {
    if(data.isEmpty())
        return

    val majorStrAdapter : Major.StringAdapter = Major.StringAdapter(this.resources)
    val sexStrAdapter : Sex.StringAdapter = Sex.StringAdapter(this.resources)

    val pieData: MutableList<SliceValue> = mutableListOf()

    for (d in data) {
        pieData.add(SliceValue(d.value.toFloat(),
                if (d.key in listOf( Constant.DEFAULT_SEX,  Constant.DEFAULT_MAJOR)) {
                    resources.getColor(color.UNKNOWN)
                }else if(this.tag == "sex") {
                    if(d.key == 0) {
                        Color.parseColor("#0000ff")
                    }else {
                        Color.parseColor("#ff0000")
                    }
                }
                else {
                    Color.rgb(Random.nextInt() % 180, Random.nextInt() % 180, Random.nextInt() % 180)
                }
        )
                .setLabel("${
                when(this.tag as? String) {
                    "major"->majorStrAdapter.toUi(d.key as String)
                    "studentid"->d.key
                    "age"->(Constant.CURRENT_YEAR - ((d.key as? Int) ?: 0) + 1)
                    "sex"->sexStrAdapter.toUi(d.key as Int)
                    else->d.key
                }

                }\n\r${d.value}"))
    }

    val pieChartData = PieChartData(pieData)
    pieChartData.setHasLabels(true)
    pieChartData.valueLabelBackgroundColor = android.R.color.transparent
    this.pieChartData = pieChartData
}