package com.example.contactosenlahabitacion_amarp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactosenlahabitacion_amarp.MainActivity.Companion.basedatos
import com.example.contactosenlahabitacion_amarp.dal.ContactoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.FloatingActionButton as FloatingActionButton1

@Composable
fun Agenda(navController: NavController, coroutineScope: CoroutineScope) {
    var contactos by remember { mutableStateOf<List<ContactoEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Aquí estás cargando los contactos desde la base de datos
        contactos = basedatos.contactoDao().getAll()
    }

    // Usando Scaffold para envolver el contenido y agregar el FAB
    Scaffold(
        floatingActionButton = {
            FloatingActionButton1(
                onClick = {
                    // Acción cuando se presiona el FAB
                    // Aquí puedes agregar la navegación o cualquier acción que desees
                    navController.navigate("addContacto")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Contacto")
            }
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        ContactList(contactos, modifier = Modifier.padding(paddingValues))
    }
}


@Composable
fun ContactList(contacts: List<ContactoEntity>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(contacts.size) { index ->
            VistaContacto(contact = contacts[index])
        }
    }
}

@Composable
fun VistaContacto(contact: ContactoEntity) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen circular a la izquierda
        Image(
            painter = painterResource(contact.foto), // Usar la URL o el recurso de imagen
            contentDescription = "Imagen de contacto",
            modifier = Modifier
                .size(50.dp)  // Tamaño de la imagen
                .clip(CircleShape)  // Forma circular
                .padding(4.dp)
        )

        // Información del contacto en el centro
        Spacer(modifier = Modifier.width(16.dp))  // Espacio entre la imagen y el texto
        Column(
            modifier = Modifier.weight(1f) // Para que ocupe todo el espacio disponible
        ) {
            Text(
                text = contact.nombre,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contact.telefono,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}


@Composable
fun AddContacto(navController: NavController, coroutineScope: CoroutineScope) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var imagenSeleccionada by remember { mutableStateOf<String>("") }

    // Lista de imágenes
    val imagenesDisponibles = listOf(
        R.drawable.man,
        R.drawable.women,
    )

    Scaffold{ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Campo de texto para el nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para los apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para el teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seleccionar una imagen
            Text("Selecciona una imagen:")


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        // Comprobamos que todos los campos estén rellenos
                        if (telefono.isNotEmpty() && nombre.isNotEmpty() && apellidos.isNotEmpty() && imagenSeleccionada.isNotEmpty()) {
                            val user = ContactoEntity(telefono, nombre, apellidos, imagenSeleccionada.toInt())

                            // Comprobamos que no exista ya un usuario con ese teléfono
                            if (!basedatos.contactoDao().existsByTlfn(telefono)) {
                                // Guardamos al usuario
                                basedatos.contactoDao().insert(user)

                                navController.navigate("agenda") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    launchSingleTop = true
                                }                            } else {
                                Toast.makeText(context, "Ya existe un contacto con ese teléfono", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text(
                    text = "Guardar contacto"
                )
            }
        }
    }
}
