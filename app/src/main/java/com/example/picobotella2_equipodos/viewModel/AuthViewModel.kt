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

    suspend fun login(email: String, password: String) {
        // Simula una autenticación
        if (email.isNotEmpty() && password.isNotEmpty()) {
            authRepository.login(email, password)
            _loginResult.value = true
        } else {
            _loginResult.value = false
        }
    }

    suspend fun register(email: String, password: String) {
        // Simula un registro exitoso (esto debería llamarse a un backend real)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            authRepository.register(email, password)
            _registerResult.value = true
        } else {
            _registerResult.value = false
        }
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


