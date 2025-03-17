package com.rafadevilha.carrinhodecompras.screens

import android.app.AlertDialog
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafadevilha.carrinhodecompras.R
import com.rafadevilha.carrinhodecompras.models.ShoppingItem
import com.rafadevilha.carrinhodecompras.utilitarios.showAlertDialog
import com.rafadevilha.carrinhodecompras.viewModels.ShoppingListViewModel

@Composable
fun PrincipalScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    viewModel: ShoppingListViewModel = viewModel()
) {

    val shoppingList by viewModel.shoppingList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(PaddingValues(top = 12.dp + innerPadding.calculateTopPadding()))
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        CadastroItems(
            onAddItem = { name, price ->
                val newItem = ShoppingItem(
                    id = shoppingList.size + 1,
                    name = name,
                    price = price
                )
                viewModel.addItem(newItem)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Valor(viewModel)
        Spacer(modifier = Modifier.height(6.dp))
        ListaItems(viewModel = viewModel, shoppingList = shoppingList)
    }
}

@Composable
fun CadastroItems(
    viewModel: ShoppingListViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onAddItem: (String, Double) -> Unit
) {
    val context = LocalContext.current

    var nome by remember {
        mutableStateOf("")
    }
    var preco by remember {
        mutableStateOf("")
    }

    val shoppingList by viewModel.shoppingList.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = {
                    Text(
                        text = "Nome do Produto",
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = colorResource(R.color.details),
                    focusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            OutlinedTextField(
                placeholder = {
                    Text(
                        text = "Ex: 12.89 ( Não use Virgula )",
                        color = Color.LightGray
                    )
                },
                value = preco,
                onValueChange = { preco = it },
                label = {
                    Text(
                        text = "Valor do Produto",
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = colorResource(R.color.details),
                    focusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val qty = preco.toDoubleOrNull() ?: 0.0
                    if (nome.isNotBlank() && qty > 0) {
                        onAddItem(nome, qty)
                        nome = ""
                        preco = ""
                    } else {
                        Toast.makeText(
                            context,
                            "Preencha corretamente os campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.button)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                    contentDescription = "Adicionar",
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }
    }

}

@Composable
fun Valor(
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier
) {
    val valorTotal = viewModel.valorTotal()
    val valorTotalFormatado = String.format("%.2f", valorTotal)
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total: ",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black

        )
        Text(
            text = "$$valorTotalFormatado",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.details)
        )
        Spacer(modifier = Modifier.width(50.dp))
        IconButton(
            onClick = {
                showAlertDialog(
                    item = ShoppingItem(0, "", 0.0),
                    viewModel = viewModel,
                    context = context,
                    titulo = "Alerta",
                    mensagem = "Deseja realmente limpar a lista?",
                    removerItem = { viewModel.limparLista() }
                )
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_delete_sweep_24),
                contentDescription = "",
                tint = colorResource(R.color.delete)
            )
        }
    }
}

@Composable
fun ListaItems(
    viewModel: ShoppingListViewModel,
    shoppingList: List<ShoppingItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(shoppingList) {
            CardItemsLista(viewModel = viewModel, item = it)
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(horizontal = 26.dp)
            )

        }
    }
}

@Composable
fun CardItemsLista(
    viewModel: ShoppingListViewModel,
    item: ShoppingItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.details_1)
            )
            Text(
                text = "$${item.price}",
                fontSize = 16.sp,
                color = Color.White
            )
            Icon(
                painter = painterResource(R.drawable.baseline_delete_outline_24),
                contentDescription = "Deletar",
                tint = colorResource(R.color.button),
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        showAlertDialog(
                            item,
                            viewModel,
                            context,
                            "Alerta",
                            "Deseja realmente excluir esse item?",
                            removerItem = { viewModel.removeItem(item) }
                        )
                        //viewModel.removeItem(item)
                    }
            )

        }
    }
}

@Preview
@Composable
private fun PrincipalScreenPrev() {
    PrincipalScreen()
}