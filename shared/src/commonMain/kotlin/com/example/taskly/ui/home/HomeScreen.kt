package com.example.taskly.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import taskly.shared.generated.resources.*

// Тимчасові дані. У наступній лабі їх замінять дані з dummyjson
private data class TaskItem(val title: String, val tag: String, val time: String, val done: Boolean)

private val mockTasks = listOf(
    TaskItem("Finish Kotlin Multiplatform architecture diagram", "College", "Today, 4:00 PM", false),
    TaskItem("Review Mobile UI state management with Compose", "Study", "Today, 6:30 PM", false),
    TaskItem("Submit UX research survey results", "Project", "Completed at 11:20 AM", true),
    TaskItem("Team sync on Taskly sprint backlog", "Meeting", "Completed at 9:00 AM", true),
)

@Composable
fun HomeScreen(onResetOnboarding: () -> Unit) {
    val active = mockTasks.filter { !it.done }
    val done = mockTasks.filter { it.done }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) { Icon(Icons.Default.Add, contentDescription = null) }
        },
        bottomBar = { HomeBottomBar() },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { HomeHeader(onResetOnboarding) }
            item { FocusCard(done = done.size, total = mockTasks.size) }
            item { FilterRow(all = mockTasks.size, pending = active.size, completed = done.size) }
            item { SectionLabel("${stringResource(Res.string.active_tasks)} (${active.size})") }
            items(active) { TaskCard(it) }
            item { SectionLabel("${stringResource(Res.string.completed_today)} (${done.size})") }
            items(done) { TaskCard(it) }
        }
    }
}

@Composable
private fun HomeHeader(onResetOnboarding: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(12.dp))
        Text(
            stringResource(Res.string.greeting, "Alex"),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onResetOnboarding) {
            Icon(Icons.Default.Refresh, contentDescription = stringResource(Res.string.show_onboarding))
        }
    }
}

@Composable
private fun FocusCard(done: Int, total: Int) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(Res.string.daily_focus), style = MaterialTheme.typography.labelSmall)
                Text(
                    stringResource(Res.string.completed_of, done, total),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            LinearProgressIndicator(
                progress = { done.toFloat() / total },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        }
    }
}

@Composable
private fun FilterRow(all: Int, pending: Int, completed: Int) {
    var selected by remember { mutableStateOf(0) }
    val labels = listOf(
        "${stringResource(Res.string.filter_all)} $all",
        "${stringResource(Res.string.filter_pending)} $pending",
        "${stringResource(Res.string.filter_completed)} $completed",
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        labels.forEachIndexed { i, label ->
            FilterChip(selected = selected == i, onClick = { selected = i }, label = { Text(label) })
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun TaskCard(task: TaskItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.done) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Checkbox(checked = task.done, onCheckedChange = null)
            Spacer(Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.done) TextDecoration.LineThrough else null,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(
                            task.tag,
                            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    Text(
                        task.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeBottomBar() {
    var selected by remember { mutableStateOf(0) }
    val items: List<Pair<String, ImageVector>> = listOf(
        stringResource(Res.string.nav_tasks) to Icons.Default.CheckCircle,
        stringResource(Res.string.nav_calendar) to Icons.Default.DateRange,
        stringResource(Res.string.nav_statistics) to Icons.Default.Star,
        stringResource(Res.string.nav_settings) to Icons.Default.Settings,
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEachIndexed { i, (label, icon) ->
            NavigationBarItem(
                selected = selected == i,
                onClick = { selected = i },
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label) },
            )
        }
    }
}