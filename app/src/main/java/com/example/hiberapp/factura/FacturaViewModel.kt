package com.example.hiberapp.factura.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiberapp.Domain.Factura
import com.example.hiberapp.dataretrofit.repository.FacturaRepository
import kotlinx.coroutines.launch

class FacturaViewModel : ViewModel() {

    private val repository = FacturaRepository()

    private val _facturas = MutableLiveData<List<Factura>>()
    val facturas: LiveData<List<Factura>> = _facturas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadFacturas() {
        _isLoading.value = true

        viewModelScope.launch {
            try {

                val result = repository.getFacturas()
                _facturas.value = result
            } catch (e: Exception) {

                _error.value = "Error al cargar las facturas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
