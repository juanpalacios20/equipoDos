package com.example.picobotella2_equipodos.auth

import AuthViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.picobotella2_equipodos.databinding.FragmentLoginBinding
import com.example.picobotella2_equipodos.repository.AuthRepository
import com.example.picobotella2_equipodos.viewModel.AuthenticationViewModelFactory


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Aquí creamos el repository, puede ser una instancia que tengas previamente creada
        val authRepository = AuthRepository()  // Asumiendo que tienes un AuthRepository

        // Crear el ViewModel usando el factory
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
        // Botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(context, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Texto para registrarse
        binding.tvRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.registerUser(email, password)
            } else {
                Toast.makeText(context, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        // Observador para el resultado del login
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                // Aquí puedes redirigir a otra pantalla, como la principal
            } else {
                Toast.makeText(context, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        })

        // Observador para el resultado del registro
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Aquí puedes redirigir a otra pantalla, como la principal
            } else {
                Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
