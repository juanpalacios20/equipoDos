import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picobotella2_equipodos.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    suspend fun login(email: String, password: String): Boolean {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val user = authRepository.login(email, password)
            if (user != null) {
                _loginResult.value = true
            } else {
                _loginResult.value = false
            }
        } else {
            _loginResult.value = false
        }
        return _loginResult.value ?: false
    }

    suspend fun register(email: String, password: String): Boolean {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val user = authRepository.register(email, password)
            if (user != null) {
                _registerResult.value = true
            } else {
                _registerResult.value = false
            }
        } else {
            _registerResult.value = false
        }
        return _registerResult.value ?: false
    }


    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            register(email, password)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            login(email, password)
        }
    }
}


