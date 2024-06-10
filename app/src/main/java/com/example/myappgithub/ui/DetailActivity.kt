package com.example.myappgithub.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.myappgithub.R
import com.example.myappgithub.database.FavoriteUser
import com.example.myappgithub.database.FavoriteUserDao
import com.example.myappgithub.database.UserDatabase
import com.example.myappgithub.database.UserRepository
import com.example.myappgithub.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: SectionPagerAdapter
    private lateinit var favoriteUserDao: FavoriteUserDao
    private lateinit var userRepository: UserRepository


    private var isFavorite: Boolean = false
    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDatabase = Room.databaseBuilder(applicationContext, UserDatabase::class.java, "user_database").build()
        userRepository = UserRepository(userDatabase.favoriteUserDao())
        favoriteUserDao = userDatabase.favoriteUserDao()

        val username = intent.getStringExtra("EXTRA_USERNAME")
        val avatarUrl = intent.getStringExtra("EXTRA_AVATARURL")

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        username?.let { detailViewModel.getUserDetail(it) }

        adapter = SectionPagerAdapter(this@DetailActivity)
        adapter.username = username ?: ""

        binding.viewPager.adapter = adapter

        binding.btFavorite.setOnClickListener {
            toggleFavoriteStatus()
        }

        observeFavoriteStatus(username ?: "")

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> ""
            }
        }.attach()

        detailViewModel.userDetail.observe(this) { userDetail ->
            binding.apply {
                tvUsername.text = userDetail.login
                tvName.text = userDetail.name
                tvFollowers.text = "${userDetail.followers} Followers"
                tvFollowing.text = "${userDetail.following} Following"
                Glide.with(this@DetailActivity)
                    .load(userDetail.avatarUrl)
                    .into(imageAva)
            }
        }
        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun observeFavoriteStatus(username: String) {
        if (::favoriteUserDao.isInitialized) {
            favoriteUserDao.getAllFavoriteUsers().observe(this) { favoriteUsers ->
                val favoriteUser = favoriteUsers.find { it.username == username }
                isFavorite = favoriteUser != null
                updateFavoriteButtonIcon()
            }
        }
    }

    private fun updateFavoriteButtonIcon() {
        val iconRes = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        binding.btFavorite.setImageResource(iconRes)
    }

    private fun toggleFavoriteStatus() {
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: return
        val avatarUrl = intent.getStringExtra("EXTRA_AVATARURL")

        if (isFavorite) {
            delete(username)
        } else {
            insert(username, avatarUrl)
        }
    }

    private fun insert(username: String, avatarUrl: String?) {
        val favoriteUser = FavoriteUser(username, avatarUrl)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insert(favoriteUser)
            }
            isFavorite = true
            updateFavoriteButtonIcon()
        }
    }

    private fun delete(username: String) {
        val favoriteUser = FavoriteUser(username)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.delete(favoriteUser)
            }
            isFavorite = false
            updateFavoriteButtonIcon()

            val resultIntent = Intent()
            resultIntent.putExtra("USERNAME_DELETED", username)
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

}