package org.mjstudio.ggonggang.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.item_class.view.*
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentSearchBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.ui.MainFragmentDirections
import org.mjstudio.ggonggang.ui.MainViewModel
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var mBinding: FragmentSearchBinding by AutoClearedValue()
    private lateinit var mMainViewModel : MainViewModel
    private lateinit var mViewModel: SearchViewModel

    private var isSearchViewOpen = false

    private val TAG = SearchFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mMainViewModel = ViewModelProvider(activity!!,viewModelFactory)[MainViewModel::class.java]
        mViewModel = ViewModelProvider(activity!!, viewModelFactory)[SearchViewModel::class.java]
        this.lifecycle.addObserver(mViewModel)

        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        mBinding.setLifecycleOwner(viewLifecycleOwner)
        mBinding.vm = mViewModel

        setSearchView()

        initView()
        listenViewModel()


        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerView.startLayoutAnimation()
    }



    private fun initView() {
        mBinding.floatingButtonAdd.isEnabled = false
        mBinding.floatingButtonInformation.isEnabled = false
        mBinding.floatingMenu.setOnMenuButtonClickListener {
            mViewModel.onClickMenuButton()
        }
        mBinding.floatingMenu.hideMenu(false)
    }

    private fun listenViewModel() {
        mViewModel.apply {

            filteredClassData.observe(viewLifecycleOwner, Observer {
                (mBinding.recyclerView.adapter as? SearchItemAdapter)?.submitList(it)
            })

            clickFilterButton.observeOnce(viewLifecycleOwner) {settings->
                val dir = MainFragmentDirections.actionMainFragmentToSearchFilterBottomSheet(settings)
                findNavController().navigate(dir)
            }

            clickSearchButton.observeOnce(viewLifecycleOwner) {
                mBinding.searchView.showSearch()
            }

            clickItem.observeOnce(viewLifecycleOwner) { classData ->

                val extras=FragmentNavigatorExtras(
//                        mBinding.appbar to "toolbar",
                        mBinding.appbar to "toolbar",
                        mBinding.floatingButtonAdd to "graph1",
                        mBinding.floatingButtonInformation to "graph2"
                )

                val direction = MainFragmentDirections.actionMainFragmentToInformationFragment(classData)
                findNavController().navigate(direction,extras)
            }

            currentSelectedClassData.observe(viewLifecycleOwner, Observer {classData : ClassData? ->
                mBinding.floatingMenu.showMenu(true)
                mBinding.floatingButtonInformation.isEnabled = classData != null
                mBinding.floatingButtonAdd.isEnabled = classData!=null

                if(classData == null) {

                }else {
                    mBinding.floatingMenu.menuButtonLabelText = classData.name

                }
            })

            isMenuExpanded.observe(viewLifecycleOwner, Observer {expanded->

                if(expanded) {
                    mBinding.floatingMenu.open(true)
                }else {
                    mBinding.floatingMenu.close(true)
                }
            })

            clickAddButton.observeOnce(viewLifecycleOwner) {classData->

            }
            clickInformationButton.observeOnce(viewLifecycleOwner) {classData->
                val extras=FragmentNavigatorExtras(
                        //                        mBinding.appbar to "toolbar",
                        mBinding.appbar to "toolbar",
                        mBinding.floatingButtonAdd to "graph1",
                        mBinding.floatingButtonInformation to "graph2"
                )

                val direction = MainFragmentDirections.actionMainFragmentToInformationFragment(classData)
                findNavController().navigate(direction,extras)
            }

            msg.observeOnce(viewLifecycleOwner) {
                this@SearchFragment.toast(it)
            }
            snackMsg.observeOnce(viewLifecycleOwner) {
                mBinding.root.showSnackbar(it.msg)
            }


            closeSearchView.observeOnce(viewLifecycleOwner) {
                mBinding.searchView.closeSearch()
            }
            openSearchView.observeOnce(viewLifecycleOwner) {
                mBinding.searchView.showSearch()
            }

            navTabChanged.observeOnce(viewLifecycleOwner) {
                if(mMainViewModel.currentNavigationTab.value != it)
                    mMainViewModel.currentNavigationTab.value = it
            }

            startLayoutAnim.observeOnce(viewLifecycleOwner) {
                mBinding.recyclerView.startLayoutAnimation()
            }
        }
    }

    fun onDestroyFilterBottomSheet(settings: SearchFilterSettings) {
//        mPresenter?.initAllClassesAndStartQuery(mQuery)
    }

    private fun showSearchFilter(settings: SearchFilterSettings) {

        val direction = MainFragmentDirections.actionMainFragmentToSearchFilterBottomSheet(settings)

        findNavController().navigate(direction)
    }

    fun setRecyclerView() {
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context!!)
        mBinding.recyclerView.adapter = SearchItemAdapter(userRepository, mViewModel, viewLifecycleOwner)
        mBinding.recyclerView.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context!!, androidx.recyclerview.widget.RecyclerView.VERTICAL))
    }

    fun setSearchView() {
        // Get the SearchView and set the searchable configuration

        mBinding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                isSearchViewOpen = false
//                mPresenter?.initAllClassesAndStartQuery(mQuery)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (isSearchViewOpen) {
//                mPresenter?.initAllClassesAndStartQuery(mQuery)
            }
                return true
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

        mBinding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShownAnimation() {
            }

            override fun onSearchViewClosed() {
                isSearchViewOpen = false
            }

            override fun onSearchViewClosedAnimation() {
            }

            override fun onSearchViewShown() {
                isSearchViewOpen = true
            }
        })
    }
}