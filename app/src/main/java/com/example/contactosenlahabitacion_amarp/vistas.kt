package com.example.contactosenlahabitacion_amarp

import android.widget.ImageButton
import android.widget.Space
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactosenlahabitacion_amarp.MainActivity.Companion.basedatos
import com.example.contactosenlahabitacion_amarp.dal.ContactoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.FloatingActionButton as FloatingActionButton1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Agenda(navController: NavController, coroutineScope: CoroutineScope) {
    val contactos = remember { mutableStateListOf<ContactoEntity>() }

    // Cargamos los contactos al iniciar la pantalla
    LaunchedEffect(Unit) {
        contactos.clear()
        contactos.addAll(basedatos.contactoDao().getAll())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agenda",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton1(
                onClick = {
                    // Navegamos a la pantalla de añadir contacto
                    navController.navigate("addContacto")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Contacto")
            }
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        ContactList(contactos, modifier = Modifier.padding(paddingValues), coroutineScope, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        R.drawable.hombre,
        R.drawable.mujer
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Añadir Contacto",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Campo de texto para el nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para los apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para el teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seleccionar una imagen
            Text("Selecciona una imagen:")

            // Lista para las imágenes
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(imagenesDisponibles.size) { index ->
                    Image(
                        painter = painterResource(imagenesDisponibles[index]),
                        contentDescription = "Imagen a elegir",
                        modifier = Modifier
                            .size(84.dp)
                            .padding(8.dp)
                            .clickable {
                                imagenSeleccionada = imagenesDisponibles[index].toString()
                            }
                            .border(
                                width = 2.dp,
                                color = if (imagenesDisponibles[index].toString() == imagenSeleccionada) Color.Blue else Color.Gray
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        // Comprobamos que todos los campos estén rellenos
                        if (telefono.isNotEmpty() && nombre.isNotEmpty() && apellidos.isNotEmpty() && imagenSeleccionada.isNotEmpty()) {
                            // Comprobamos que el teléfono tenga 9 números
                            if (telefono.length == 9) {
                                val user = ContactoEntity(telefono, nombre, apellidos, imagenSeleccionada.toInt())

                                // Comprobamos que no exista ya un usuario con ese teléfono
                                if (!basedatos.contactoDao().existsByTlfn(telefono)) {
                                    // Guardamos al usuario
                                    basedatos.contactoDao().insert(user)

                                    Toast.makeText(context, "Contacto añadido", Toast.LENGTH_SHORT).show()
                                    navController.navigate("agenda") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    Toast.makeText(context, "Ya existe un contacto con ese teléfono", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "El teléfono debe tener 9 dígitos", Toast.LENGTH_SHORT).show()
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

@Composable
fun ContactList(contacts: SnapshotStateList<ContactoEntity>, modifier: Modifier = Modifier, coroutineScope: CoroutineScope, navController: NavController) {
    LazyColumn(modifier) {
        items(contacts.size) { index ->
            Column {
                VistaContacto(contact = contacts[index], coroutineScope, contacts, navController)
                HorizontalDivider(Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun VistaContacto(contact: ContactoEntity, coroutineScope: CoroutineScope, contacts: SnapshotStateList<ContactoEntity>, navController: NavController) {
    val context = LocalContext.current
    val shouldShowDialog = remember { mutableStateOf(false) } // 1

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen circular a la izquierda
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .padding(6.dp)
        ) {
            Image(
                painter = painterResource(contact.foto),
                contentDescription = "Imagen de contacto",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Información del contacto en el centro
        Spacer(modifier = Modifier.width(16.dp))

        Row(Modifier.weight(1f)) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "${contact.nombre} ${contact.apellido}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.telefono,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.editar),
                    modifier = Modifier.size(36.dp)
                        .clickable {
                            navController.navigate("editarContacto/${contact.telefono}")
                        },
                    contentDescription = "Editar"
                )
                Spacer(Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.trash),
                    modifier = Modifier.size(36.dp)
                        .clickable {
                            shouldShowDialog.value = true
                        },
                    contentDescription = "Borrar"
                )

                // Dialogo que se muestra al pulsar en eliminar contacto
                if (shouldShowDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            shouldShowDialog.value = false
                        },
                        title = { Text(text = "Atención!!") },
                        text = { Text(text = "¿Está seguro de que desea eliminar a este usuario?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        // Obtenemos el contacto por su teléfono
                                        val user = basedatos.contactoDao().getContacto(contact.telefono)
                                        basedatos.contactoDao().delete(user)
                                        Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show()

                                        // Actualizamos la lista después de eliminar el contacto
                                        contacts.clear()
                                        contacts.addAll(basedatos.contactoDao().getAll())
                                    }
                                    shouldShowDialog.value = false
                                }
                            ) {
                                Text(
                                    text = "Sí",
                                    color = Color.White
                                )
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    shouldShowDialog.value = false
                                }
                            ) {
                                Text(
                                    text = "No",
                                    color = Color.White
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarContacto(telefono: String, coroutineScope: CoroutineScope, navController: NavController) {
    val context = LocalContext.current

    var contact by remember { mutableStateOf<ContactoEntity?>(null) }

    LaunchedEffect(telefono) {
        // Obtenemos al contacto de la base de datos
        contact = basedatos.contactoDao().getContacto(telefono)
    }

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefonoState by remember { mutableStateOf("") }
    var imagenSeleccionada by remember { mutableStateOf("") }

    // Lista de imágenes
    val imagenesDisponibles = listOf(
        R.drawable.man,
        R.drawable.women,
        R.drawable.hombre,
        R.drawable.mujer
    )

    // Verificamos si el contacto se ha cargado
    if (contact != null) {
        nombre = contact!!.nombre
        apellidos = contact!!.apellido
        telefonoState = contact!!.telefono
        imagenSeleccionada = contact!!.foto.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Contacto",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            if (contact != null) {
                // Campo de texto para el nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de texto para los apellidos
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de texto para el teléfono
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefonoState = it },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seleccionar una imagen
                Text("Selecciona una imagen:")

                // Lista para las imágenes
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(imagenesDisponibles.size) { index ->
                        Image(
                            painter = painterResource(imagenesDisponibles[index]),
                            contentDescription = "Imagen a elegir",
                            modifier = Modifier
                                .size(84.dp)
                                .padding(8.dp)
                                .clickable {
                                    imagenSeleccionada = imagenesDisponibles[index].toString()
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (imagenesDisponibles[index].toString() == imagenSeleccionada) Color.Blue else Color.Gray
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            // Comprobamos que todos los campos estén rellenos
                            if (telefono.isNotEmpty() && nombre.isNotEmpty() && apellidos.isNotEmpty() && imagenSeleccionada.isNotEmpty()) {
                                // Comprobamos que el teléfono tenga 9 números
                                if (telefono.length == 9) {
                                    val user = ContactoEntity(telefono, nombre, apellidos, imagenSeleccionada.toInt())

                                    // Guardamos al usuario
                                    basedatos.contactoDao().update(user)

                                    Toast.makeText(context, "Contacto actualizado", Toast.LENGTH_SHORT).show()
                                    navController.navigate("agenda") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    }

                                } else {
                                    Toast.makeText(context, "El teléfono debe tener 9 dígitos", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Guardar cambios"
                    )
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
