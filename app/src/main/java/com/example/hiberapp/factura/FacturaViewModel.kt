package com.example.hiberapp.factura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiberapp.Domain.Factura
import com.example.hiberapp.dataretrofit.repository.FacturaRepository
import kotlinx.coroutines.launch

class FacturaViewModel : ViewModel() {

    // Repositorio para acceder a los datos
    private val repository = FacturaRepository()

    // LiveData para las facturas
    private val _facturas = MutableLiveData<List<Factura>>()
    val facturas: LiveData<List<Factura>> = _facturas

    // LiveData para estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Cargar facturas desde el API
    fun loadFacturas() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Obtener facturas del repositorio
                val result = repository.getFacturas()
                _facturas.value = result
            } catch (e: Exception) {
                // En caso de error
                _error.value = "Error al cargar las facturas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}