package com.rafadevilha.carrinhodecompras.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafadevilha.carrinhodecompras.models.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel() {

    private val _shoppingList = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val shoppingList: StateFlow<List<ShoppingItem>> get() = _shoppingList

    fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            _shoppingList.value += item
        }
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            _shoppingList.value -= item
        }
    }

    fun valorTotal(): Double {
        val valorTotal = _shoppingList.value.sumOf { it.price }
        return valorTotal
    }

    fun limparLista(){
        viewModelScope.launch {
            _shoppingList.value = emptyList()
        }
    }
}