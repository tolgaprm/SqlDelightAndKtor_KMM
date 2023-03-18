package com.prmcoding.sqldelightandktor.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prmcoding.sqldelightandktor.Greeting
import com.prmcoding.sqldelightandktor.RocketLaunch
import com.prmcoding.sqldelightandktor.SpaceXSDK
import com.prmcoding.sqldelightandktor.android.ui.colorAccent
import com.prmcoding.sqldelightandktor.android.ui.colorSuccessful
import com.prmcoding.sqldelightandktor.android.ui.colorUnsuccessful
import com.prmcoding.sqldelightandktor.cache.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sdk = SpaceXSDK(DatabaseDriverFactory(this))
        setContent {

            val scaffoldState = rememberScaffoldState()
            val launches = remember { mutableStateOf<List<RocketLaunch>>(emptyList()) }
            val isLoading = remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = true) {
                isLoading.value = true
                mainScope.launch {
                    kotlin.runCatching {
                        sdk.getLaunches(forceReload = false)
                    }.onSuccess {
                        isLoading.value = false
                        launches.value = it
                    }.onFailure {
                        isLoading.value = false
                        scaffoldState.snackbarHostState.showSnackbar(
                            it.localizedMessage ?: "Something went wrong"
                        )
                    }
                }
            }


            MyApplicationTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Space X Launches",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    scaffoldState = scaffoldState,
                    content = {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 12.dp)
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    vertical = it.calculateTopPadding(),
                                    horizontal = 4.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(launches.value) {
                                    Content(launch = it)
                                }
                            }

                            if (isLoading.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    launch: RocketLaunch,
) {

    val launchesSuccessText = launch.launchSuccess?.let {
        if (it) "Successful" else "Unsuccessful"
    } ?: ""

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .border(2.dp,Color.Gray),
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Text(text = "Launch name: ${launch.missionName}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = launchesSuccessText,
                color = launch.launchSuccess?.let {
                    if (it) colorSuccessful else colorUnsuccessful
                } ?: colorAccent
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Launch year: ${launch.launchYear}")
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Launch Details: ${launch.details}")
        }
    }
}