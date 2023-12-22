package com.example.coursehub.navigationbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coursehub.R
import com.example.coursehub.navigation.Screens

@Composable
fun Bar(navController: NavController, modifier: Modifier = Modifier){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Iconos de navegación
        IconButton(onClick = { navController.navigate(Screens.HomeScreen.name) },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.home_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.home_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }

        IconButton(onClick = { navController.navigate(Screens.CoursesView.name)  },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = stringResource(id = R.string.course_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.course_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }

        IconButton(onClick = { /* TODO: Acción para Search */ },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.search_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }

        IconButton(onClick = { navController.navigate(Screens.UserProfile.name) },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.account_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.account_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}