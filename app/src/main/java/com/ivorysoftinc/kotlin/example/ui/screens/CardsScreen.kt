package com.ivorysoftinc.kotlin.example.ui.screens

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.data.EMPTY
import com.ivorysoftinc.kotlin.example.resources.strings.APP_NAME
import com.ivorysoftinc.kotlin.example.resources.strings.IMAGE_LOAD_FAILED
import com.ivorysoftinc.kotlin.example.resources.strings.NO_INTERNET_CONNECTION
import com.ivorysoftinc.kotlin.example.ui.main.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.compose.getStateViewModel
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * Composable screen for showing cards list.
 */

@Preview(showBackground = true)
@Composable
fun CardsScreen() {
    val viewModel = getStateViewModel<MainViewModel>()
    val internetConnectionState = viewModel.getInternetUpdatesFlow(context = LocalContext.current)
        .collectAsState(initial = true)
    val loadingState = viewModel.loadingFlow.collectAsState(initial = true)
    val error = viewModel.errorFlow.collectAsState(initial = EMPTY)
    val data = viewModel.cardsFlow.collectAsState(initial = listOf())

    Scaffold(topBar = {
        TopAppBar(contentPadding = PaddingValues(horizontal = 16.dp)) {
            Text(text = APP_NAME, fontSize = 20.sp, modifier = Modifier.weight(1F))

            IconButton(onClick = { viewModel.getCards() }) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = ComposeColor.White
                )
            }
        }
    }) {
        if (!internetConnectionState.value) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = ComposeColor.Red)
                    .zIndex(3F),
                text = NO_INTERNET_CONNECTION,
                color = ComposeColor.White,
                textAlign = TextAlign.Center
            )
        }

        if (loadingState.value && internetConnectionState.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .zIndex(3F)
                    .testTag("Progress")
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.4F)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }

        if (error.value.isNotEmpty()) {
            Toast.makeText(LocalContext.current, error.value, Toast.LENGTH_LONG).show()
        }

        LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
            items(
                items = data.value,
                itemContent = {
                    val modifier = if (data.value.indexOf(it) != data.value.lastIndex) {
                        Modifier.padding(bottom = 16.dp)
                    } else {
                        Modifier
                    }

                    when (it) {
                        is Card.Text -> CardTextItem(modifier = modifier, card = it, isBold = true)
                        is Card.TitleDescription -> CardTitleDescriptionItem(
                            modifier = modifier,
                            card = it
                        )
                        is Card.ImageTitleDescription -> CardImageTitleDescriptionItem(
                            modifier = modifier,
                            card = it
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CardTextItem(modifier: Modifier = Modifier, isBold: Boolean = false, card: Card.Text?) {
    val textColor = try {
        ComposeColor(Color.parseColor(card?.attributes?.textColor))
    } catch (e: Exception) {
        ComposeColor.Black
    }

    Text(
        text = card?.value ?: EMPTY,
        color = textColor,
        fontSize = (card?.attributes?.font?.size?.toFloat() ?: 16F).sp,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        modifier = modifier
    )
}

@Composable
fun CardTitleDescriptionItem(modifier: Modifier = Modifier, card: Card.TitleDescription?) {
    Column(modifier) {
        CardTextItem(card = card?.title, isBold = true)
        CardTextItem(card = card?.description)
    }
}

@Composable
fun CardImageTitleDescriptionItem(modifier: Modifier = Modifier, card: Card.ImageTitleDescription) {
    Card(modifier = modifier, elevation = 4.dp) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
        ) {
            CardTitleDescriptionItem(
                modifier = Modifier
                    .zIndex(2F)
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                card = Card.TitleDescription(title = card.title, description = card.description)
            )

            GlideImage(
                imageModel = card.image?.url ?: EMPTY,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        // GlideImage height = GlideImage width * ratio of card image width to height
                        ratio = (card.image?.size?.width ?: 1).toFloat() / (
                            card.image?.size?.height
                                ?: 1
                            )
                    ),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.1F)
                            .align(Alignment.Center),
                        color = MaterialTheme.colors.primaryVariant
                    )
                },
                failure = {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = IMAGE_LOAD_FAILED,
                        color = ComposeColor.Black
                    )
                }
            )
        }
    }
}
