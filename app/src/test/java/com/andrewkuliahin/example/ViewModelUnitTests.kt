package com.andrewkuliahin.example

import com.ivorysoftinc.kotlin.example.cache.CardEntity
import com.ivorysoftinc.kotlin.example.cache.CardMapper
import com.ivorysoftinc.kotlin.example.cache.CardsDao
import com.ivorysoftinc.kotlin.example.data.Attributes
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.data.FontAttribute
import com.ivorysoftinc.kotlin.example.data.Image
import com.ivorysoftinc.kotlin.example.data.Size
import com.ivorysoftinc.kotlin.example.network.CardsApi
import com.ivorysoftinc.kotlin.example.network.model.PageResponse
import com.ivorysoftinc.kotlin.example.network.model.RootCardsResponse
import com.ivorysoftinc.kotlin.example.repository.CardsRepository
import com.ivorysoftinc.kotlin.example.repository.NetworkRepository
import com.ivorysoftinc.kotlin.example.resources.strings.SOMETHING_WENT_WRONG
import com.ivorysoftinc.kotlin.example.ui.main.MainViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

/**
 * ViewModel Unit tests.
 */
class ViewModelUnitTests {

    private lateinit var viewModel: MainViewModel
    private lateinit var errorViewModel: MainViewModel
    private lateinit var loadingViewModel: MainViewModel
    private lateinit var cardsRepository: CardsRepository
    private lateinit var networkRepository: NetworkRepository

    private val expectedCards by lazy { provideCardsList() }

    @Before
    fun setup() {
        cardsRepository = CardsRepository(
            cardsApi = CardsApiImpl(),
            cardsDao = CardsDaoImpl(),
            cardMapper = CardMapper()
        )

        networkRepository = NetworkRepository()

        viewModel =
            MainViewModel(networkRepository = networkRepository, cardsRepository = cardsRepository)
        loadingViewModel = MainViewModel(networkRepository = networkRepository, cardsRepository = cardsRepository)
        errorViewModel = MainViewModel(
            networkRepository = networkRepository,
            cardsRepository = CardsRepository(
                cardsApi = ErrorCardsApiImpl(),
                cardsDao = CardsDaoImpl(),
                cardMapper = CardMapper()
            )
        )
    }

    /**
     * Test returning needed list by cardsFlow.
     */
    @Test
    fun `ViewModel cards flow test`() = runBlocking {
        viewModel.getCards()

        // Drop first to skip initial
        val secondCardsEmission = viewModel.cardsFlow.drop(1).first()

        assertEquals(expectedCards, secondCardsEmission)
    }

    /**
     * Test values of loadingFlow when calling getCards().
     */
    @Test
    fun `ViewModel loading flow test`() = runBlocking {
        assertEquals(true, loadingViewModel.loadingFlow.value)

        // Get cards
        loadingViewModel.getCards()

        // Drop first to skip initial
        val secondLoadingEmission = loadingViewModel.loadingFlow.drop(1).first()

        assertEquals(false, secondLoadingEmission)
    }

    /**
     * Test error emission by errorFlow.
     */
    @Test
    fun `ViewModel error flow test`() = runBlocking {
        // Get cards
        errorViewModel.getCards()

        // Drop first to skip initial
        val secondErrorEmission = errorViewModel.errorFlow.drop(1).first()

        assertEquals(SOMETHING_WENT_WRONG, secondErrorEmission)
    }

    private fun provideCardsList(): List<Card> {
        val baseText = Card.Text(
            value = "Hello, Welcome to App!",
            attributes = Attributes(textColor = "#262626", font = FontAttribute(size = 30))
        )
        val baseTitleDescription = Card.TitleDescription(
            title = baseText.copy(
                value = "Check out our App every week for exciting offers.",
                attributes = baseText.attributes?.copy(font = baseText.attributes?.font?.copy(size = 24))
            ),
            description = baseText.copy(
                value = "Offers available every week!",
                attributes = baseText.attributes?.copy(font = baseText.attributes?.font?.copy(size = 18))
            )
        )
        val baseImageTitleDescription = Card.ImageTitleDescription(
            title = baseText.copy(
                value = "Movie ticket to Dark Phoenix!",
                attributes = baseText.attributes?.copy(
                    textColor = "#FFFFFF",
                    font = baseText.attributes?.font?.copy(size = 18)
                )
            ),
            description = baseText.copy(
                value = "Tap to see offer dates and rescriptions.",
                attributes = baseText.attributes?.copy(
                    textColor = "#FFFFFF",
                    font = baseText.attributes?.font?.copy(size = 12)
                )
            ),
            image = Image(
                url = "https://qaevolution.blob.core.windows.net/assets/ios/3x/Featured@4.76x.png",
                size = Size(width = 1170, height = 1498)
            )
        )

        return listOf(
            baseText,
            baseTitleDescription,
            baseImageTitleDescription,
            baseText.copy(value = "Text 2"),
            baseTitleDescription.copy(
                title = baseImageTitleDescription.title?.copy(value = "This is a sample text title v1"),
                description = baseImageTitleDescription.description?.copy(value = "This is a sample text description v1")
            ),
            baseImageTitleDescription.copy()
        )
    }

    private fun provideCardEntities(): List<CardEntity> {
        return provideCardsList().mapNotNull(CardMapper()::cardToEntity)
    }

    /**
     * Class which represents mocked network API.
     */
    inner class CardsApiImpl : CardsApi {
        override suspend fun getCards(): RootCardsResponse {
            return RootCardsResponse(
                page = PageResponse(
                    cards = provideCardsList()
                )
            )
        }
    }

    /**
     * Class which represents mocked network API (with some error).
     */
    inner class ErrorCardsApiImpl : CardsApi {
        override suspend fun getCards(): RootCardsResponse {
            throw IOException(SOMETHING_WENT_WRONG)
        }
    }

    /**
     * Class which represents mocked cache Dao.
     */
    inner class CardsDaoImpl : CardsDao {
        override suspend fun insert(list: List<CardEntity>) = Unit // Do nothing

        override suspend fun nukeTable() = Unit // Do nothing

        override suspend fun getCards(): List<CardEntity> {
            return provideCardEntities()
        }
    }
}
