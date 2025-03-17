package com.rafadevilha.carrinhodecompras.utilitarios

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafadevilha.carrinhodecompras.models.ShoppingItem
import com.rafadevilha.carrinhodecompras.viewModels.ShoppingListViewModel

fun showAlertDialog(
    item: ShoppingItem,
    viewModel: ShoppingListViewModel,
    context: Context,
    titulo: String,
    mensagem: String,
    removerItem: (item: ShoppingItem) -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(titulo)
        .setMessage(mensagem)
        .setPositiveButton("Sim") { _, _ ->
            //viewModel.removeItem(item)
            removerItem(item)
        }
        .setNegativeButton("Não") { _, _ ->

        }
        .create()
        .show()
}