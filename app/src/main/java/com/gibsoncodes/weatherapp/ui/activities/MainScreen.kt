package com.gibsoncodes.weatherapp.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gibsoncodes.weatherapp.*
import com.gibsoncodes.weatherapp.appcomponent.App
import com.gibsoncodes.weatherapp.databinding.ActivityMainScreenBinding
import com.gibsoncodes.weatherapp.repo.ForecastRepo
import com.gibsoncodes.weatherapp.ui.ForecastViewModel
import com.gibsoncodes.weatherapp.ui.Resource
import com.gibsoncodes.weatherapp.ui.Status
import com.gibsoncodes.weatherapp.ui.models.ForecastDataUi
import com.gibsoncodes.weatherapp.viewmodel.ViewModelFactory
import com.gibsoncodes.weatherapp.worker.NotifyWorker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class MainScreen : AppCompatActivity() {
private var binding:ActivityMainScreenBinding?=null
    private lateinit var viewModel: ForecastViewModel
    private lateinit var viewModelFactory: ViewModelFactory
   private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this@MainScreen,
            R.layout.activity_main_screen
        )
        auth= FirebaseAuth.getInstance()
        viewModelFactory=
            ViewModelFactory(ForecastRepo(App.injectAppApi()))
        viewModel= ViewModelProvider(this@MainScreen,viewModelFactory).get(ForecastViewModel::class.java)

        //TODO: find a better place/way to handle start the notification work request
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val notificationWork= OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .addTag("notification")
            .setConstraints(constraints)
            .setInitialDelay(60, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(this@MainScreen).enqueue(notificationWork)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.logout){
            auth.signOut()
            startActivity(Intent(this@MainScreen,
                MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        val searchManager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem= menu?.findItem(R.id.searchItem)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint= getString(R.string.enter_a_city_s_name)
        searchView.isSubmitButtonEnabled=true
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
               return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery(query,false)
                searchItem.collapseActionView()
                if (query!=null){
                    query.let {
                        if (it.isNotEmpty() && it.isNotBlank())
                            binding?.searchTxt?.visibility=View.GONE
                            binding?.mainProgress?.visibility=View.VISIBLE
                            ///Toast.makeText(this@MainScreen,query,Toast.LENGTH_SHORT).show()
                            viewModel.setCity(query)
                            viewModel.provideWeatherData()
                            viewModel.provideWeatherDataResponse().observe(this@MainScreen,Observer{data->
                            processResponse(data)
                        })
                    }

                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    private fun processResponse(resource: Resource<ForecastDataUi>){
        when(resource.status){
            Status.SUCCESS-> processData(resource.data)

            Status.LOADING->processLoading()
            Status.ERROR->processError(resource.message)
        }
    }
    private fun processData(dataUi: ForecastDataUi?){
        if (dataUi!=null){
            binding?.dataUi= dataUi
            Picasso.with(this)
                .load("http://openweathermap.org/img/wn/${dataUi.weather.icon}@2x.png")
                .resize(64,64)
                .into(binding?.iconFld)
            binding?.mainProgress?.visibility= View.GONE
            binding?.revLayout?.visibility=View.VISIBLE
        }else{
            binding?.mainProgress!!.visibility=View.GONE
            showError()
        }
    }
    private fun processError(throwable: Throwable?){
        binding?.mainProgress?.visibility=View.GONE
        binding?.revLayout!!.visibility=View.GONE
        when(throwable){
            is ConnectException, is UnknownHostException->{
                showError()
            }
            else->Snackbar.make(binding?.coordinator!!,"Well this is embarrassing:an unknown error occurred.Please try again",Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setAction("Retry"){
                    viewModel.provideWeatherData()
                }
                .setActionTextColor(Color.WHITE)
                .show()
        }
    }
    private fun processLoading(){
        binding?.mainProgress?.visibility=View.VISIBLE
        binding?.revLayout!!.visibility=View.GONE
    }
    private fun showError(){
        Snackbar.make(binding?.coordinator!!,"Well this is embarrassing:Please check your internet connection & try again.",Snackbar.LENGTH_LONG)
            .setBackgroundTint(getColor(R.color.colorPrimary))
            .setTextColor(Color.WHITE)
            .setAction("Retry"){
                viewModel.provideWeatherData()
            }
            .setActionTextColor(Color.WHITE)
            .show()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()}

}
