package org.mjstudio.ggonggang.ui.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notice.*
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentNoticeBinding
import org.mjstudio.ggonggang.ui.MainFragmentDirections
import javax.inject.Inject

class NoticeFragment : Fragment(), NoticeContract.View {

    private val TAG = NoticeFragment::class.java.simpleName

    private var mBinding: FragmentNoticeBinding by AutoClearedValue()

    private var mAdapter: NoticeRecyclerAdapter? = null
    @Inject
    lateinit var mPresenter: NoticeContract.Presenter
    private var mDisposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentNoticeBinding.inflate(inflater, container, false)
        mBinding.setLifecycleOwner(viewLifecycleOwner)


        mPresenter.attachView(this)
        setRecyclerView()

        setItemClickListener()

        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mPresenter.detachView()
        mDisposable?.dispose()
    }

    override fun setNotice(items: List<NoticeDTO>) {
            mAdapter?.items = items
            mAdapter?.notifyDataSetChanged()
    }

    private fun setRecyclerView() {
        val rView = recyclerView_notice

        rView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        mAdapter = NoticeRecyclerAdapter()
        rView.adapter = mAdapter
        rView.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context!!, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL))
    }

    private fun setItemClickListener() {
        mDisposable = CompositeDisposable()

        val disposable = mAdapter?.clickEvent!!
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { noticeDTO ->

                    val action = MainFragmentDirections.actionMainFragmentToNoticeContentFragment(noticeDTO.toEntity())
                    findNavController().navigate(action)
        }

        mDisposable?.add(disposable)
    }
}
