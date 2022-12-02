package com.abdanzakialifian.storyapp.presentation.login.viewmodel

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.utils.DataDummy
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
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
class LoginViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponseSuccess = DataDummy.dummyLoginResponseSuccess()
    private val dummyLoginResponseFailed = DataDummy.dummyLoginResponseFailed()
    private val dummyLoginRequest = DataDummy.dummyLoginRequest()
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDWjhCMWVCSlhjalhJcjAiLCJpYXQiOjE2Njk1MjY1ODh9.-FQZeZC49FLcjICU0Mtqj3foOj2r4GBBubdX3yUD-ZU"
    private val dummyName = "Abdan Zaki Alifian"

    @Before
    fun setup() {
        // open to mock if annotation run with not MockitoJUnitRunner
        MockitoAnnotations.openMocks(this)

        // initialization view model
        loginViewModel = LoginViewModel(storyUseCase)
    }

    // login success
    @Test
    fun `When Login Success`() = runTest {
        val expectedLogin = MutableStateFlow<Result<Login>>(Result.loading())
        expectedLogin.value = Result.success(dummyLoginResponseSuccess)

        // manipulate method login user in story use case
        `when`(storyUseCase.loginUser(dummyLoginRequest)).thenReturn(expectedLogin)

        // call method login user in view model
        loginViewModel.loginUser(dummyLoginRequest)

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            loginViewModel.uiStateLoginUser.collect {
                // data same
                assertEquals(Result.success(dummyLoginResponseSuccess), it)
                // data not null
                assertNotNull(it.data)
                // result status is success
                assertTrue(it.status == Status.SUCCESS)
                // response not error
                assertTrue(it.data?.error == false)
            }
        }

        // ensure the method login user is called
        verify(storyUseCase).loginUser(dummyLoginRequest)

        collectJob.cancel()
    }

    // login failed
    @Test
    fun `When Login Failed`() = runTest {
        val expectedLogin = MutableStateFlow<Result<Login>>(Result.loading())
        expectedLogin.value = Result.error(dummyLoginResponseFailed.message.toString())

        // manipulate method login user in story use case
        `when`(storyUseCase.loginUser(dummyLoginRequest)).thenReturn(expectedLogin)

        // call method login user in view model
        loginViewModel.loginUser(dummyLoginRequest)

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            loginViewModel.uiStateLoginUser.collect {
                // result status is error
                assertTrue(it.status == Status.ERROR)
                // message not valid email
                assertEquals(dummyLoginResponseFailed.message, it.message)
            }
        }

        // ensure the method is called
        verify(storyUseCase).loginUser(dummyLoginRequest)

        collectJob.cancel()
    }

    // ensure the method save user data is called
    @Test
    fun `When Save User Data is Called`() = runTest {
        loginViewModel.saveUserData(dummyToken, dummyName)
        verify(storyUseCase).saveUserData(dummyToken, dummyName)
    }
}