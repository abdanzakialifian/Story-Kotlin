package com.abdanzakialifian.storyapp.presentation.upload.viewmodel

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.utils.DataDummy
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.RequestBody
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
class UploadViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var uploadViewModel: UploadViewModel
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDWjhCMWVCSlhjalhJcjAiLCJpYXQiOjE2Njk1MjY1ODh9.-FQZeZC49FLcjICU0Mtqj3foOj2r4GBBubdX3yUD-ZU"
    private val dummyPhotoFile = DataDummy.dummyImageFile()
    private val dummyDescription = DataDummy.dummyDescription()
    private val dummyLatitude: RequestBody? = DataDummy.dummyLatitude()
    private val dummyLongitude: RequestBody? = DataDummy.dummyLongitude()
    private val dummyCreateNewStoryResponseSuccess = DataDummy.dummyCreateNewStoryResponseSuccess()
    private val dummyCreateNewStoryResponseFailed = DataDummy.dummyCreateNewStoryResponseFailed()

    @Before
    fun setup() {
        // open to mock if annotation run with not MockitoJUnitRunner
        MockitoAnnotations.openMocks(this)

        // initialization view model
        uploadViewModel = UploadViewModel(storyUseCase)
    }

    @Test
    fun `When Get Token`() = runTest {
        val expectedToken = MutableStateFlow("")
        expectedToken.value = dummyToken

        // manipulate method get token in story use case
        `when`(storyUseCase.getToken()).thenReturn(expectedToken)

        // call method get token in view model
        uploadViewModel.getToken()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            uploadViewModel.getToken.collect {
                // data not null
                assertNotNull(it)
                // data same
                assertEquals(dummyToken, it)
            }
        }

        // ensure the method get token is called
        verify(storyUseCase).getToken()

        collectJob.cancel()
    }

    @Test
    fun `When Created New Story Success`() = runTest {
        val expectedCreatedStory = MutableStateFlow<Result<NewStory>>(Result.loading())
        expectedCreatedStory.value = Result.success(dummyCreateNewStoryResponseSuccess)

        // manipulate method create new story in story use case
        `when`(
            storyUseCase.createNewStory(
                "Bearer $dummyToken",
                dummyPhotoFile,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedCreatedStory)

        // call method new story in view model
        uploadViewModel.newStory(
            dummyPhotoFile,
            dummyDescription,
            dummyLatitude,
            dummyLongitude,
            dummyToken
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            uploadViewModel.uiStateNewStory.collect {
                // data not null
                assertNotNull(it.data)
                // data same
                assertEquals(dummyCreateNewStoryResponseSuccess, it.data)
                // result status is success
                assertTrue(it.status == Status.SUCCESS)
                // response not error
                assertTrue(it.data?.error == false)
            }
        }

        // ensure the method create new story is called
        verify(storyUseCase).createNewStory(
            "Bearer $dummyToken",
            dummyPhotoFile,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        collectJob.cancel()
    }

    @Test
    fun `When Created New Story Failed`() = runTest {
        val expectedCreatedStory = MutableStateFlow<Result<NewStory>>(Result.loading())
        expectedCreatedStory.value =
            Result.error(dummyCreateNewStoryResponseFailed.message.toString())

        // manipulate method create new story in story use case
        `when`(
            storyUseCase.createNewStory(
                "Bearer $dummyToken",
                dummyPhotoFile,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedCreatedStory)

        // call method new story in view model
        uploadViewModel.newStory(
            dummyPhotoFile,
            dummyDescription,
            dummyLatitude,
            dummyLongitude,
            dummyToken
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            uploadViewModel.uiStateNewStory.collect {
                // result status is error
                assertTrue(it.status == Status.ERROR)
                // data same
                assertEquals(dummyCreateNewStoryResponseFailed.message, it.message)
            }
        }

        // ensure the method create new story is called
        verify(storyUseCase).createNewStory(
            "Bearer $dummyToken",
            dummyPhotoFile,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        collectJob.cancel()
    }
}