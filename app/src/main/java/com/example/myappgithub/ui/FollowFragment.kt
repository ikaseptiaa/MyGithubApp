package com.example.myappgithub.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappgithub.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var userAdapter: UserAdapter

    private var position: Int = 0
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSTION)
            username= it.getString(ARG_USERNAME) ?: ""
        }
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        userAdapter = UserAdapter()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        if (position == 1){
            viewModel.followers.observe(viewLifecycleOwner){followers ->
                userAdapter.submitList(followers)
            }
        }else{
            viewModel.following.observe(viewLifecycleOwner){following ->
                userAdapter.submitList(following)
            }
        }
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.adapter = userAdapter

        if (position == 1){
            viewModel.getFollowers(username)
        }else{
            viewModel.getFollowing(username)
        }
    }
    companion object{
        const val ARG_POSTION = "position"
        const val ARG_USERNAME = "username"
    }
}


