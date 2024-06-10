package com.example.myappgithub.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappgithub.R
import com.example.myappgithub.data.response.ItemsItem
import com.example.myappgithub.database.UserDatabase
import com.example.myappgithub.database.UserRepository
import com.example.myappgithub.databinding.ActivityFavoriteUserBinding

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var repository: UserRepository
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBar)

        showLoading()

        val userDatabase = UserDatabase.getInstance(applicationContext)
        val favoriteUserDao = userDatabase.favoriteUserDao()

        repository = UserRepository(favoriteUserDao)

        val viewModelFactory = FavoriteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        userAdapter = UserAdapter()

        val recyclerView: RecyclerView = binding.rvFavorite
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        viewModel.getAllFavoriteUsers().observe(this) { favoriteUsers ->
            val items = favoriteUsers.map {
                ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
            }
            userAdapter.submitList(items)
            hideLoading()
        }

        userAdapter.setRecyclerViewClickListener(object : UserAdapter.RecyclerViewClickListener {
            override fun onItemClick(view: View, item: ItemsItem) {
                onFavoriteItemClick(item)
            }
        })
    }

    private fun onFavoriteItemClick(item: ItemsItem) {
        val intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java).apply {
            putExtra("EXTRA_USERNAME", item.login)
            putExtra("EXTRA_AVATARURL", item.avatarUrl)
        }
        startActivityForResult(intent, DELETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DELETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val usernameDeleted = data?.getStringExtra("USERNAME_DELETED")
            usernameDeleted?.let { username ->
                val newList = userAdapter.currentList.filter { it.login != username }
                userAdapter.submitList(newList)
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    companion object {
        private const val DELETE_REQUEST_CODE = 1001
    }
}