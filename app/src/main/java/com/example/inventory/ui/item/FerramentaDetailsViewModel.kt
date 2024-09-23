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

package com.example.inventory.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.FerramentasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class FerramentaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val ferramentasRepository: FerramentasRepository,
) : ViewModel() {

    private val ferramentaId: Int = checkNotNull(savedStateHandle[FerramentaDetailsDestination.ferramentaIdArg])

    val uiState: StateFlow<FerramentaDetailsUiState> =
        ferramentasRepository.getFerramentaStream(ferramentaId)
            .filterNotNull()
            .map {
                FerramentaDetailsUiState(quantidade = it.quantidade <= 0, ferramentaDetails = it.toFerramentaDetails())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FerramentaDetailsUiState()
            )

    suspend fun deleteFerramenta() {
        ferramentasRepository.deleteFerramenta(uiState.value.ferramentaDetails.toFerramenta())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class FerramentaDetailsUiState(
    val quantidade: Boolean = true,
    val ferramentaDetails: FerramentasDetails = FerramentasDetails()
)
