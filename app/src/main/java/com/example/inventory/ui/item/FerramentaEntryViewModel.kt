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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Ferramenta
import com.example.inventory.data.FerramentasRepository


class FerramentaEntryViewModel(private val ferramentasRepository: FerramentasRepository) : ViewModel() {


    var ferramentaUiState by mutableStateOf(FerramentaUiState())
        private set


    fun updateUiState(ferramentasDetails: FerramentasDetails) {
        ferramentaUiState =
            FerramentaUiState(ferramentasDetails = ferramentasDetails, isEntryValid = validateInput(ferramentasDetails))
    }


    suspend fun saveFerramenta() {
        if (validateInput()) {
            ferramentasRepository.insertFerramenta(ferramentaUiState.ferramentasDetails.toFerramenta())
        }
    }

    private fun validateInput(uiState: FerramentasDetails = ferramentaUiState.ferramentasDetails): Boolean {
        return with(uiState) {
            nome.isNotBlank() && fabricante.isNotBlank() && modelo.isNotBlank()
        }
    }
}

data class FerramentaUiState(
    val ferramentasDetails: FerramentasDetails = FerramentasDetails(),
    val isEntryValid: Boolean = false
)

data class FerramentasDetails(
    val id: Int = 0,
    val nome: String = "",
    val fabricante: String = "",
    val modelo:String = "",
    val quantidade: Int = 0,
    val data_emprestimo: String = ""
)


fun FerramentasDetails.toFerramenta(): Ferramenta = Ferramenta(
    id = id,
    nome = nome,
    quantidade = quantidade,
    modelo = modelo,
    data_emprestimo = data_emprestimo,
    fabricante = fabricante

)

fun Ferramenta.toFerramentaUiState(isEntryValid: Boolean = false): FerramentaUiState = FerramentaUiState(
    ferramentasDetails = this.toFerramentaDetails(),
    isEntryValid = isEntryValid
)


fun Ferramenta.toFerramentaDetails(): FerramentasDetails = FerramentasDetails(
    id = id,
    nome = nome,
    modelo = modelo,
    quantidade = quantidade,
    data_emprestimo = data_emprestimo,
    fabricante = fabricante,

)
