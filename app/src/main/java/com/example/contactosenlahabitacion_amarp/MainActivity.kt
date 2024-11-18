package com.example.contactosenlahabitacion_amarp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.contactosenlahabitacion_amarp.dal.ContactoDatabase
import com.example.contactosenlahabitacion_amarp.dal.ContactoEntity
import com.example.contactosenlahabitacion_amarp.ui.theme.ContactosEnLaHabitacion_AmarpTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var basedatos : ContactoDatabase
    }

    lateinit var contactos : List<ContactoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización BD
        basedatos = Room.databaseBuilder(
            this, ContactoDatabase::class.java,"users-db"
        ).fallbackToDestructiveMigration().build()
        runBlocking {
            launch {
                contactos = basedatos.contactoDao().getAll()
            }
        }

        enableEdgeToEdge()
        setContent {
            ContactosEnLaHabitacion_AmarpTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                NavHost(
                    navController = navController,
                    startDestination = "agenda",
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable("agenda") {
                        Agenda(navController, coroutineScope)
                    }
                    composable("addContacto") {
                        AddContacto(navController, coroutineScope)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactosEnLaHabitacion_AmarpTheme {
        Greeting("Android")
    }
}