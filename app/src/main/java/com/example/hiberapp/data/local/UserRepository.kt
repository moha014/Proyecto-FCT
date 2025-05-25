package com.example.hiberapp.data.local

import android.content.Context
import com.example.hiberapp.dataretrofit.api.ApiClient
import com.example.hiberapp.dataretrofit.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio para manejar los datos de usuarios tanto desde API como desde datos mock
class UserRepository(private val context: Context, private val userDao: UserDao) {

    // Metodo para obtener usuarios según el modo activo (mock o API real)
    suspend fun getUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        if (ApiClient.useMock) {
            // Si está activado el modo mock, obtenemos usuarios de la base de datos local
            userDao.getAllUsersOnce()
        } else {
            // Si no hay mock, obtenemos datos de la API real
            val apiService: ApiService = ApiClient.getService(context)
            val response = apiService.obtenerFacturas().execute()

            // Convertimos los datos de facturas a usuarios (mapeo temporal para pruebas)
            val users = response.body()?.facturas?.map {
                UserEntity(
                    name = it.fecha, // Usamos la fecha como nombre (solo para pruebas)
                    email = it.descEstado ?: "" // Usamos el estado como email
                )
            } ?: emptyList()

            // Limpiamos la tabla y guardamos los nuevos usuarios
            userDao.clearAll()
            users.forEach { userDao.insertUser(it) }
            users
        }
    }

    // Metodo para establecer datos mock de usuarios
    suspend fun setMockData(mockUsers: List<UserEntity>) = withContext(Dispatchers.IO) {
        // Limpiamos todos los usuarios existentes
        userDao.clearAll()
        // Insertamos cada usuario mock en la base de datos
        mockUsers.forEach { userDao.insertUser(it) }
    }
}