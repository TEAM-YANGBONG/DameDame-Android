package com.yangbong.main.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yangbong.core_ui.base.BindingFragment
import com.yangbong.core_ui.util.ResolutionMetrics
import com.yangbong.damedame.main.R
import com.yangbong.damedame.main.databinding.FragmentSearchBinding
import com.yangbong.domain.entity.SearchInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment(private val resolutionMetrics: ResolutionMetrics) :
    BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by activityViewModels()
    lateinit var myFSadapter: FriendSearchRecyclerViewAdapter
    lateinit var myRSadapter: SearchResultRecyclerViewAdapter

    lateinit var searchData:ArrayList<String>
    private val Number.dp get() = resolutionMetrics.toPixel(this.toInt())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initOnclick()
        binding.searchViewModel=searchViewModel
        super.onViewCreated(view, savedInstanceState)
    }
    fun toResultLayout(){
        binding.backBtn.visibility=View.VISIBLE
        binding.deleteAll.visibility=View.GONE
        binding.recentSearch.visibility=View.GONE
        binding.FSRecyclerView.visibility=View.GONE
        binding.RSRecyclerView.visibility=View.VISIBLE

    }
    fun toSearchLayout(){

        binding.backBtn.visibility=View.GONE
        binding.deleteAll.visibility=View.VISIBLE
        binding.recentSearch.visibility=View.VISIBLE
        binding.FSRecyclerView.visibility=View.VISIBLE
        binding.RSRecyclerView.visibility=View.GONE

    }
    fun initOnclick(){
        binding.deleteAll.setOnClickListener {
            searchViewModel.deleteAllDataView()

        }

        binding.SearchBtn.setOnClickListener {
            if(binding.SearchTxt.text.isEmpty()){
                binding.checkText.visibility=View.VISIBLE
            }
            else{
                binding.checkText.visibility=View.GONE
                searchViewModel.addSearchDataView(binding.SearchTxt.text.toString())
                toResultLayout()
                searchViewModel.getSearch(binding.SearchTxt.text.toString())
                initResult()


            }

        }

        binding.backBtn.setOnClickListener {
            toSearchLayout()
        }

    }
    fun initResult(){
        var resultData= arrayListOf<SearchInfo>()
        binding.RSRecyclerView.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        myRSadapter= SearchResultRecyclerViewAdapter(resultData)
        myRSadapter.SRitemClickListener=object :SearchResultRecyclerViewAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                searchViewModel.getUserProfileInfo(myRSadapter.items[position].userId)
                //searchViewModel.userProfileData.value

            }
        }
        binding.RSRecyclerView.adapter=myRSadapter
        searchViewModel.searchResultData.observe(viewLifecycleOwner,androidx.lifecycle.Observer{
            myRSadapter.items = searchViewModel.searchResultData.value!!
            (binding.RSRecyclerView.adapter)?.notifyDataSetChanged()
        })
    }

    fun init(){
        searchViewModel.getRecentSearchData()
        searchData= arrayListOf()
        binding.FSRecyclerView.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        myFSadapter= FriendSearchRecyclerViewAdapter(searchData)
        myFSadapter.FSitemClickListner=object :FriendSearchRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                searchViewModel.getSearch(myFSadapter.items[position])
                toResultLayout()
                initResult()
            }

            override fun onXClick(position: Int) {
                var data=myFSadapter.items[position]
                searchViewModel.deleteDataView(data)
            }
        }
        binding.FSRecyclerView.adapter=myFSadapter

        searchViewModel.searchData.observe(viewLifecycleOwner,androidx.lifecycle.Observer{
            myFSadapter.items= searchViewModel.searchData.value!!
            (binding.FSRecyclerView.adapter)?.notifyDataSetChanged()

        }

        )

        //----------RESULT LAYOUT--------------//


    }

}
