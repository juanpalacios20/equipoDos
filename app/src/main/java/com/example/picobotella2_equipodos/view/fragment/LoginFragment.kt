package com.example.picobotella2_equipodos.auth

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
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.FragmentLoginBinding
import com.example.picobotella2_equipodos.repository.AuthRepository
import com.example.picobotella2_equipodos.view.fragment.HomeFragment
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
                navigateToHome()

            } else {
                Toast.makeText(context, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        })

        // Observador para el resultado del registro
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                navigateToHome()

            } else {
                Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToHome() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null) // Opcional: permite regresar a LoginFragment con el botón de retroceso
            .commit()
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val password = binding.etPassword.text.toString().trim()
            validatePassword(password)
            validateInputs()  // Llamar a validateInputs para verificar ambos campos
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validatePassword(password: String) {
        when {
            password.isEmpty() -> {
                // Si el campo está vacío, mantener el borde blanco y eliminar cualquier mensaje de error
                binding.tilPassword.isErrorEnabled = false // Oculta el error correctamente
                binding.tilPassword.boxStrokeColor = ContextCompat.getColor(requireContext(), android.R.color.white)
            }
            password.length < 6 -> {
                // Mostrar mensaje de error y cambiar el borde a rojo
                binding.tilPassword.error = "Mínimo 6 dígitos"
                binding.tilPassword.isErrorEnabled = true // Asegurarse de que el error se muestre
                binding.tilPassword.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.red)
            }
            else -> {
                // Borrar el mensaje de error y volver a color blanco
                binding.tilPassword.isErrorEnabled = false // Oculta el error completamente
                binding.tilPassword.error = null // Asegura que no hay texto de error
                binding.tilPassword.boxStrokeColor = ContextCompat.getColor(requireContext(), android.R.color.white)
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateInputs()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateInputs() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val isValid = email.isNotEmpty() && password.isNotEmpty()

        // Cambiar el estado del botón de login
        binding.btnLogin.isEnabled = isValid
        val textColor = if (isValid) {
            ContextCompat.getColor(requireContext(), android.R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.orange_dark) // Naranja oscuro
        }
        binding.btnLogin.setTextColor(textColor)

        // Habilitar o deshabilitar el TextView "Registrarse"
        binding.tvRegister.isEnabled = isValid
        if (isValid) {
            // Cambiar a color blanco y negrita
            binding.tvRegister.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            binding.tvRegister.setTypeface(binding.tvRegister.typeface, Typeface.BOLD)
        } else {
            // Volver a color gris cuando esté deshabilitado
            binding.tvRegister.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            binding.tvRegister.setTypeface(binding.tvRegister.typeface, Typeface.NORMAL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
