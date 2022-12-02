package com.abdanzakialifian.storyapp.presentation.profile.viewmodel

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
class ProfileViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var profileViewModel: ProfileViewModel
    private val dummyName = "Abdan Zaki Alifian"
    private val dummyLanguageCodeId = "id"
    private val dummyLanguageCodeEn = "en"

    @Before
    fun setup() {
        // open to mock if annotation run with not MockitoJUnitRunner
        MockitoAnnotations.openMocks(this)

        // initialization view model
        profileViewModel = ProfileViewModel(storyUseCase)
    }

    @Test
    fun `When Delete Data Store is Called`() = runTest {
        profileViewModel.deleteDataStore()
        verify(storyUseCase).deleteDataStore()
    }

    @Test
    fun `Get Data Name From DataStore`() = runTest {
        val expectedName = MutableStateFlow("")
        expectedName.value = dummyName

        `when`(storyUseCase.getName()).thenReturn(expectedName)

        profileViewModel.getDataStoreName()

        val collectJob = launch(UnconfinedTestDispatcher()) {
            profileViewModel.userName.collect {
                // data not null
                assertNotNull(it)
                // data same
                assertEquals(dummyName, it)
            }
        }

        verify(storyUseCase).getName()

        collectJob.cancel()
    }

    @Test
    fun `When Save Language Code is Called`() = runTest {
        profileViewModel.saveLanguageCode(dummyLanguageCodeEn)
        verify(storyUseCase).saveLanguageCode(dummyLanguageCodeEn)
    }

    @Test
    fun `When Get Language Code is ID`() = runTest {
        val expectedLanguageCode = MutableStateFlow("")
        expectedLanguageCode.value = dummyLanguageCodeId

        // manipulate method get language code in story use case
        `when`(storyUseCase.getLanguageCode()).thenReturn(expectedLanguageCode)

        // call method get language code in view model
        profileViewModel.getLanguageCode()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            profileViewModel.languageCode.collect {
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
        profileViewModel.getLanguageCode()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            profileViewModel.languageCode.collect {
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