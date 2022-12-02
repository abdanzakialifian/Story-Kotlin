package com.abdanzakialifian.storyapp.presentation.splashscreen.viewmodel

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SplashScreenViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private val dummyLanguageCodeId = "id"
    private val dummyLanguageCodeEn = "en"

    @Before
    fun setup() {
        // open to mock if annotation run with not MockitoJUnitRunner
        MockitoAnnotations.openMocks(this)

        // initialization view model
        splashScreenViewModel = SplashScreenViewModel(storyUseCase)
    }

    @Test
    fun `When User Session is Login`() = runTest {
        val expectedUserSession = MutableStateFlow(false)
        expectedUserSession.value = true

        // manipulate method get user session in story use case
        `when`(storyUseCase.getUserSession()).thenReturn(expectedUserSession)

        // call method get user session in view model
        splashScreenViewModel.getUserSession()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            splashScreenViewModel.getUserSession.collect {
                // data not null
                assertNotNull(it)
                // result is true (login)
                assertTrue(it)
            }
        }

        // ensure the method get user session is called
        verify(storyUseCase).getUserSession()

        collectJob.cancel()
    }

    @Test
    fun `When User Session is Not Login`() = runTest {
        val expectedUserSession = MutableStateFlow(false)
        expectedUserSession.value = false

        // manipulate method get user session in story use case
        `when`(storyUseCase.getUserSession()).thenReturn(expectedUserSession)

        // call method get user session in view model
        splashScreenViewModel.getUserSession()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            splashScreenViewModel.getUserSession.collect {
                // data not null
                assertNotNull(it)
                // result is false (not login)
                assertFalse(it)
            }
        }

        // ensure the method get user session is called
        verify(storyUseCase).getUserSession()

        collectJob.cancel()
    }

    @Test
    fun `When Get Language Code is ID`() = runTest {
        val expectedLanguageCode = MutableStateFlow("")
        expectedLanguageCode.value = dummyLanguageCodeId

        // manipulate method get language code in story use case
        `when`(storyUseCase.getLanguageCode()).thenReturn(expectedLanguageCode)

        // call method get language code in view model
        splashScreenViewModel.getLanguageCode()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            splashScreenViewModel.getLanguageCode.collect {
                // data not null
                assertNotNull(it)
                // equals data
                assertEquals(dummyLanguageCodeId, it)
            }
        }

        // ensure the method get language code is called
        verify(storyUseCase).getLanguageCode()

        collectJob.cancel()
    }

    @Test
    fun `When Get Language Code id EN`() = runTest {
        val expectedLanguageCode = MutableStateFlow("")
        expectedLanguageCode.value = dummyLanguageCodeEn

        // manipulate method get language code in story use case
        `when`(storyUseCase.getLanguageCode()).thenReturn(expectedLanguageCode)

        // call method get language code in view model
        splashScreenViewModel.getLanguageCode()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            splashScreenViewModel.getLanguageCode.collect {
                // data not null
                assertNotNull(it)
                // equals data
                assertEquals(dummyLanguageCodeEn, it)
            }
        }

        // ensure the method get language code is called
        verify(storyUseCase).getLanguageCode()

        collectJob.cancel()
    }
}