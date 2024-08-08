package com.example.submissionawal.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionawal.data.repository.StoryRepository
import com.example.submissionawal.data.repository.UserRepository
import com.example.submissionawal.di.Injection
import com.example.submissionawal.ui.viewmodel.AddStoryViewModel
import com.example.submissionawal.ui.viewmodel.LoginViewModel
import com.example.submissionawal.ui.viewmodel.MainViewModel
import com.example.submissionawal.ui.viewmodel.RegisterViewModel
import com.example.submissionawal.ui.viewmodel.maps.MapsViewModel

class ViewModelFactory(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            val storyRepository = Injection.provideStoryRepository(context)
            val userRepository = Injection.provideUserRepository(context)
            return ViewModelFactory(storyRepository, userRepository)
        }
    }
}
