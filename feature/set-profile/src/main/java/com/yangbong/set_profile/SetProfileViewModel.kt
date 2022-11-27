package com.yangbong.set_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yangbong.core_ui.constant.SetProfileNicknameConstant
import com.yangbong.core_ui.constant.SetProfileNicknameConstant.*
import com.yangbong.core_ui.util.Event
import com.yangbong.domain.entity.request.DomainSignUpRequest
import com.yangbong.domain.repository.SetProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val setProfileRepository: SetProfileRepository
) : ViewModel() {

    private val disposables by lazy {
        CompositeDisposable()
    }

    var profileNickname = MutableLiveData("")

    private val _profileNicknameState = MutableLiveData<SetProfileNicknameConstant>()
    val profileIdState: LiveData<SetProfileNicknameConstant> = _profileNicknameState

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> = _profileImageUrl

    private val _navigateToSetCharacter = MutableLiveData<Event<Boolean>>()
    val navigateToSetCharacter: LiveData<Event<Boolean>> = _navigateToSetCharacter

    fun updateProfileIdState(state: SetProfileNicknameConstant) {
        _profileNicknameState.value = state
    }

    fun getProfileImage() {
        _profileImageUrl.postValue(setProfileRepository.getUserProfileImageUrl())
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun checkDuplicateNickName() {
        viewModelScope.launch {
            setProfileRepository.checkDuplicateProfileNickname(
                profileNickname.value ?: throw IllegalStateException()
            )
                .onSuccess {
                    if (it) {
                        _profileNicknameState.postValue(ALLOWED_NICKNAME)
                    } else {
                        _profileNicknameState.postValue(DUPLICATE_NICKNAME)
                    }
                }
                .onFailure {
                    Timber.d(it)
                }
        }
    }

    fun postSignUp(platform: String, socialToken: String, fcmToken: String) {
        viewModelScope.launch {
            setProfileRepository.postSignUp(
                DomainSignUpRequest(
                    platform = platform,
                    socialToken = socialToken,
                    fcmToken = fcmToken,
                    nickname = profileNickname.value ?: "",
                    profileImageUrl = profileImageUrl.value ?: ""
                )
            ).onSuccess {
                setProfileRepository.saveUserId(it.userId)
                _navigateToSetCharacter.postValue(Event(true))
            }.onFailure {
                Timber.d(it)
            }
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}