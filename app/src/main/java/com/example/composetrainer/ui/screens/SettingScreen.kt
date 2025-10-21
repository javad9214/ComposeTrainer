package com.example.composetrainer.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composetrainer.R
import com.example.composetrainer.ui.screens.invoice.productselection.ProductSelectionBottomSheet
import com.example.composetrainer.ui.theme.Kamran
import com.example.composetrainer.ui.viewmodels.InvoiceListViewModel
import com.example.composetrainer.ui.viewmodels.ProductsViewModel
import com.example.composetrainer.ui.viewmodels.home.HomeViewModel
import com.example.composetrainer.ui.components.barcodescanner.BarcodeScannerView
import com.example.composetrainer.ui.navigation.Screen
import com.example.composetrainer.ui.viewmodels.InvoiceViewModel
import com.example.composetrainer.ui.viewmodels.SettingViewModel
import com.example.composetrainer.utils.dimen
import com.example.composetrainer.utils.str


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onButtonClick: () -> Unit,
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean = false,
    navController: NavController = rememberNavController(),
    settingViewModel: SettingViewModel = hiltViewModel(),
) {



}

const val SETTING_SCREEN_TAG = "SettingScreen"

