package com.example.picobotella2_equipodos.view.fragment

import AuthViewModel
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.FragmentLoginBinding
import com.example.picobotella2_equipodos.repository.AuthRepository
import com.example.picobotella2_equipodos.viewModel.AuthenticationViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Crear el repositorio y el ViewModel usando el Factory
        val authRepository = AuthRepository()
        val factory = AuthenticationViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // Observa cambios en los campos de texto para habilitar/deshabilitar el botón
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(passwordWatcher)

        // Botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.loginUser(email, password)
        }

        // Texto para registrarse
        binding.tvRegister.setOnClickListener {
            Toast.makeText(context, "Función de registro no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        // Observador para el resultado del login
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                // Navegar a HomeFragment
                findNavController().navigate(R.id.action_login_to_homeMain)
            } else {
                Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validatePassword(binding.etPassword.text.toString().trim())
            validateInputs()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateInputs()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validatePassword(password: String) {
        if (password.length < 6) {
            binding.tilPassword.error = "Mínimo 6 dígitos"
        } else {
            binding.tilPassword.error = null
        }
    }

    private fun validateInputs() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        binding.btnLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
