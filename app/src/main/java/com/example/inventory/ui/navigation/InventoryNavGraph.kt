/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.FerramentaDetailsDestination
import com.example.inventory.ui.item.ProdutoDetailsScreen
import com.example.inventory.ui.item.FerramentaEditDestination
import com.example.inventory.ui.item.FerramentaEditScreen
import com.example.inventory.ui.item.FerramentaEntryDestination
import com.example.inventory.ui.item.FerramentaEntryScreen

@Composable
fun FerramentasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToFerramentaEntry = { navController.navigate(FerramentaEntryDestination.route) },
                navigateToFerramentaUpdate = {
                    navController.navigate("${FerramentaDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = FerramentaEntryDestination.route) {
            FerramentaEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = FerramentaDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(FerramentaDetailsDestination.ferramentaIdArg) {
                type = NavType.IntType
            })
        ) {
            ProdutoDetailsScreen(
                FerramentaToEditItem = { navController.navigate("${FerramentaEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = FerramentaEditDestination.routeWithArgs,
            arguments = listOf(navArgument(FerramentaEditDestination.ferramentaIdArg) {
                type = NavType.IntType
            })
        ) {
            FerramentaEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
