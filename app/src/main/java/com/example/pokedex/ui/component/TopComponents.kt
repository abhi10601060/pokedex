package com.example.pokedex.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.LightBlue
import com.example.pokedex.ui.theme.PokedexTheme

@Composable
fun TitleBar(backgroundColor : Color , title : String){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = backgroundColor)
        .padding(10.dp),
        contentAlignment = Alignment.Center,
    ){
        Text(text = title, fontSize = 30.sp , fontWeight = FontWeight.ExtraBold , fontFamily = FontFamily.Serif)
    }
}

@Preview
@Composable
private fun Test() {
    PokedexTheme {
        TitleBar(backgroundColor = LightBlue, title = "Favourites")
    }
}