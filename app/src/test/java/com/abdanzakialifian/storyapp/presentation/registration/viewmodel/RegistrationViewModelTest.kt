package com.abdanzakialifian.storyapp.presentation.registration.viewmodel

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.utils.DataDummy
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class RegistrationViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var registrationViewModel: RegistrationViewModel
    private val dummyRegistrationResponseSuccess = DataDummy.dummyRegistrationResponseSuccess()
    private val dummyRegistrationResponseFailed = DataDummy.dummyRegistrationResponseFailed()
    private val dummyRegistrationRequest = DataDummy.dummyRegistrationRequest()

    @Before
    fun setup() {
        // open to mock if annotation run with not MockitoJUnitRunner
        MockitoAnnotations.openMocks(this)

        // initialization view model
        registrationViewModel = RegistrationViewModel(storyUseCase)
    }

    @Test
    fun `When Registration Success`() = runTest {
        val expectedRegistration = MutableStateFlow<Result<Registration>>(Result.loading())
        expectedRegistration.value = Result.success(dummyRegistrationResponseSuccess)

        // manipulate method registration user in story use case
        `when`(storyUseCase.registrationUser(dummyRegistrationRequest)).thenReturn(
            expectedRegistration
        )

        // call method registration user in view model
        registrationViewModel.registrationUser(dummyRegistrationRequest)

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            registrationViewModel.uiStateRegistrationUser.collect {
                // result status is success
                assertTrue(it.status == Status.SUCCESS)
                // not error registration
                assertTrue(it.data?.error == false)
                // ensure return message created
                assertEquals(dummyRegistrationResponseSuccess.message, it.data?.message)
            }
        }

        // ensure the method registration user is called
        verify(storyUseCase).registrationUser(dummyRegistrationRequest)

        collectJob.cancel()
    }

    @Test
    fun `When Registration Failed`() = runTest {
        val expectedRegistration = MutableStateFlow<Result<Registration>>(Result.loading())
        expectedRegistration.value =
            Result.error(dummyRegistrationResponseFailed.message.toString())

        // manipulate method registration user in story use case
        `when`(storyUseCase.registrationUser(dummyRegistrationRequest)).thenReturn(
            expectedRegistration
        )

        // call method registration user in view model
        registrationViewModel.registrationUser(dummyRegistrationRequest)

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            registrationViewModel.uiStateRegistrationUser.collect {
                // result status is error
                assertTrue(it.status == Status.ERROR)

                // data message same
                assertEquals(dummyRegistrationResponseFailed.message, it.message)
            }
        }

        // ensure the method registration user is called
        verify(storyUseCase).registrationUser(dummyRegistrationRequest)

        collectJob.cancel()
    }
}