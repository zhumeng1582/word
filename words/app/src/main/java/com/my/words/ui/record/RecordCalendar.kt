package com.my.words.ui.record

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.my.words.ui.theme.WordsTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PunchCardCalendar(viewModel: RecordViewModel = RecordViewModel()) {
    val recordList = viewModel.recordList.observeAsState()
    val currentMonth = viewModel.currentMonth.observeAsState()

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Row(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = { viewModel.minusMonths() }) {
                        Text("Prev")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${currentMonth.value?.month} ${currentMonth.value?.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = { viewModel.plusMonths() }) {
                        Text("Next")
                    }
                }

                DaysOfWeekRow()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(recordList.value?.size ?: 0) { index ->
                        val data = recordList.value?.get(index)
                        if (data != null) {
                            CalendarDay(date = data, onDayClick = { date ->

                            })
                        } else {
                            Spacer(Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun DaysOfWeekRow() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDay(date: Record?, onDayClick: (Record) -> Unit) {
    if (date != null) {
        val isPunched = date.punched
        TextButton(
            onClick = { onDayClick(date) },
            content = {
                Text(
                    text = date.localDate.dayOfMonth.toString(),
                    color = if (isPunched) Color.Green else Color.Black
                )
            }
        )
    } else {
        Spacer(Modifier.size(40.dp))
    }
}
