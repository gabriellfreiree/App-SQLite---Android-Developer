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

package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.FerramentasApplication
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.item.FerramentaDetailsViewModel
import com.example.inventory.ui.item.FerramentaEditViewModel
import com.example.inventory.ui.item.FerramentaEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for FerramentaEditViewModel
        initializer {
            FerramentaEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.ferramentasRepository
            )
        }
        // Initializer for FerramentaEntryViewModel
        initializer {
            FerramentaEntryViewModel(inventoryApplication().container.ferramentasRepository)
        }

        // Initializer for FerramentaDetailsViewModel
        initializer {
            FerramentaDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.ferramentasRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.ferramentasRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FerramentasApplication].
 */
fun CreationExtras.inventoryApplication(): FerramentasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FerramentasApplication)
