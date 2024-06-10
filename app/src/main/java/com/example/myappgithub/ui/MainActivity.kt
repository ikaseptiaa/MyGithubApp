package com.example.myappgithub.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappgithub.R
import com.example.myappgithub.data.response.GithubResponse
import com.example.myappgithub.data.response.ItemsItem
import com.example.myappgithub.data.retrofit.ApiConfig
import com.example.myappgithub.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity(), UserAdapter.RecyclerViewClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataViewModel: DetailViewModel
    private val adapter = UserAdapter()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = SettingPreferences.getInstance(dataStore)

        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    val value = searchView.text.toString()
                    findItem(value)
                    false
                }
        }

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.items.observe(this) { items ->
            setUserData(listOf(items))
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager

        mainViewModel.itemsListUser.observe(this){itemsItem ->
            setUserData(itemsItem)
        }

        MainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        getData("Arif")
    }

    private fun getData(query: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getItemsItem(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure:${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure:${t.message}")
            }
        })
    }

    private fun findItem(string: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getItemsItem(string)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure:${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure:${t.message}")
            }
        })
    }

    private fun setUserData(items: List<ItemsItem?>?) {
        adapter.submitList(items)
        binding.rvGithub.adapter = adapter
        adapter.listener = this
    }

    private fun showLoading(isLoading:Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                startActivity(Intent(this, FavoriteUserActivity::class.java))
                true
            }
            R.id.menu_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onFavoriteIconClicked(item: MenuItem) {
        Log.d(TAG, "Ikon favorit diklik!")
        startActivity(Intent(this, FavoriteUserActivity::class.java))
    }

    fun onSettingIconClicked(item: MenuItem) {
        Log.d(TAG, "Ikon setting diklik!")
        startActivity(Intent(this, SettingActivity::class.java))
    }

    override fun onItemClick(view: View, item: ItemsItem) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("EXTRA_ID", item.id)
        intent.putExtra("EXTRA_USERNAME", item.login)
        intent.putExtra("EXTRA_AVATARURL", item.avatarUrl)
        intent.putExtra("EXTRA_FOLLOWERS", item.followersUrl)
        intent.putExtra("EXTRA_FOLLOWING", item.followingUrl)
        Log.d(TAG, "onItemClick "+item.login)
        startActivity(intent).apply {
        }
    }
}