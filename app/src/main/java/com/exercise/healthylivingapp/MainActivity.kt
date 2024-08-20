package com.exercise.healthylivingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import com.exercise.healthylivingapp.ui.theme.Gray
import com.exercise.healthylivingapp.ui.theme.Green14
import com.exercise.healthylivingapp.ui.theme.Green7D
import com.exercise.healthylivingapp.ui.theme.HealthyLivingAppTheme
import com.exercise.healthylivingapp.ui.theme.White
import com.exercise.healthylivingapp.ui.theme.lightGray


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthyLivingAppTheme {
                NewRecipyScreen()
            }
        }
    }
}

@Composable
fun NewRecipyScreen() {
    Surface(
        modifier = Modifier.fillMaxSize().background(lightGray),
        color = Color.Transparent
    ) {
        val context = LocalContext.current
        val tasks = remember { mutableStateListOf<Task>() }

        TaskListContent(
            tasks = tasks,
            onAddTask = { title, imageUrl ->
                tasks.add(Task(title, imageUrl))
            }
        )
    }
}

@Composable
fun TaskListContent(
    tasks: MutableList<Task>,
    onAddTask: (String, String?) -> Unit
) {
    val context = LocalContext.current
    var recipyTitle by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Green7D)
                .padding(20.dp)
        ) {
            Text(
                text = "Healthy Living App",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = recipyTitle,
            onValueChange = { recipyTitle = it },
            label = { Text("Nombre de la receta: ") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green7D,
                unfocusedBorderColor = Gray,
                cursorColor = Green14,
                focusedLabelColor = Green7D,
                unfocusedLabelColor = Gray
            )
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL: ") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green7D,
                unfocusedBorderColor = Gray,
                cursorColor = Green14,
                focusedLabelColor = Green7D,
                unfocusedLabelColor = Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (recipyTitle.isNotEmpty() && imageUrl.isNotEmpty()) {
                        // Verificar si el elemento ya existe en la lista
                        val exists = tasks.any { it.title == recipyTitle && it.imageUrl == imageUrl }
                        if (!exists) {
                            onAddTask(recipyTitle, imageUrl)
                            recipyTitle = ""
                            imageUrl = ""
                        } else {
                            Toast.makeText(context, "Error, el elemento ya existe en la lista", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error, debe completar todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green14,
                    contentColor = White,
                    disabledContainerColor = Gray,
                    disabledContentColor = White
                )
            ) {
                Text("Agregar a la lista")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TaskList(
            tasks = tasks,
            onDelete = { task ->
                tasks.remove(task)
                Toast.makeText(context, "Receta de: ${task.title} eliminada", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun TaskList(tasks: List<Task>, onDelete: (Task) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(task, onDelete)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: (Task)-> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDelete(task) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
            }
            task.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.size(68.dp),

                    )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthyLivingAppTheme {
        NewRecipyScreen()
    }
}

data class Task(val title: String, val imageUrl: String?)