package com.example.testfieldapp.widgets

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.testfieldapp.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InstrumentSelectionWidget() {
    FlowRow(modifier = Modifier.padding(40.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 2,
        ) {

        val context = LocalContext.current

        InstrumentWidget("Guitar", R.drawable.guitar_icon, onClick = {})
        InstrumentWidget("More Soon", R.drawable.construction_icon, onClick = {
            Toast.makeText(context, "More instruments will be added later", Toast.LENGTH_SHORT).show()
        })
    }
}

@Composable
fun InstrumentWidget(
    name: String,
    iconPath: Int,
    iconSize: Dp = 100.dp,
    iconTint: Color = Color.Cyan,
    iconPadding: Dp = 10.dp,
    onClick: () -> Unit) {
    Box(
        modifier = Modifier.border(
            width = 2.dp,
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ).padding(10.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                modifier = Modifier.size(iconSize).padding(iconPadding),
                painter = painterResource(iconPath),
                tint = iconTint,
                contentDescription = name
            )
            Text(name, color = Color.White)
        }
    }
}