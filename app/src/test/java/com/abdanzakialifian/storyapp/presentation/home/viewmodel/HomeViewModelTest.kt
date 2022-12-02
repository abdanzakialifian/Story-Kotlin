package com.abdanzakialifian.storyapp.presentation.home.viewmodel

import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.abdanzakialifian.storyapp.data.source.local.room.database.StoriesDatabase
import com.abdanzakialifian.storyapp.data.source.local.room.entity.StoriesEntities
import com.abdanzakialifian.storyapp.data.source.local.room.paging.StoriesRemoteMediator
import com.abdanzakialifian.storyapp.data.source.remote.model.*
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.presentation.home.adapter.HomePagingAdapter
import com.abdanzakialifian.storyapp.utils.DataDummy
import com.abdanzakialifian.storyapp.utils.MainDispatcherRule
import com.abdanzakialifian.storyapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
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
class HomeViewModelTest {
    @Mock
    private lateinit var storyUseCase: StoryUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDWjhCMWVCSlhjalhJcjAiLCJpYXQiOjE2Njk1MjY1ODh9.-FQZeZC49FLcjICU0Mtqj3foOj2r4GBBubdX3yUD-ZU"
    private val dummyStoriesResponse = DataDummy.dummyStoriesResponse()
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoriesDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoriesDatabase::class.java
    ).allowMainThreadQueries().build()

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}

        override fun onRemoved(position: Int, count: Int) {}

        override fun onMoved(fromPosition: Int, toPosition: Int) {}

        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        homeViewModel = HomeViewModel(storyUseCase)
    }

    @Test
    fun `When Get Token`() = runTest {
        val expectedToken = MutableStateFlow("")
        expectedToken.value = dummyToken

        // manipulate method get token in story use case
        `when`(storyUseCase.getToken()).thenReturn(expectedToken)

        // call method get token in view model
        homeViewModel.getToken()

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            homeViewModel.getToken.collect {
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
    fun `When Get List Stories Success`() = runTest {
        val data: PagingData<Stories> = StoriesPagingSource.snapshot(dummyStoriesResponse)
        val expectedStories = MutableStateFlow<Result<PagingData<Stories>>>(Result.loading())
        expectedStories.value = Result.success(data)

        // manipulate method get all stories in story use case
        `when`(storyUseCase.getAllStories("Bearer $dummyToken")).thenReturn(expectedStories)

        // call method get all stories in view model
        homeViewModel.getAllStories(dummyToken)

        // unit test stateflow
        val collectJob = launch(UnconfinedTestDispatcher()) {
            homeViewModel.uiStateGetAllStories.collect {
                val differ = AsyncPagingDataDiffer(
                    diffCallback = HomePagingAdapter.DIFF_CALLBACK,
                    updateCallback = listUpdateCallback,
                    workerDispatcher = Dispatchers.Main
                )

                it.data?.let { pagingData ->
                    differ.submitData(pagingData)
                }

                // data not null
                assertNotNull(differ.snapshot())
                // data same
                assertEquals(dummyStoriesResponse, differ.snapshot())
                // size data same
                assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
                // name same
                assertEquals(dummyStoriesResponse[0].name, differ.snapshot()[0]?.name)
            }
        }

        verify(storyUseCase).getAllStories("Bearer $dummyToken")

        collectJob.cancel()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoriesRemoteMediator(
            mockDb,
            mockApi,
            dummyToken
        )

        val pagingState = PagingState<Int, StoriesEntities>(listOf(), null, PagingConfig(10), 10)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        // result success
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        // end of pagination is false
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class StoriesPagingSource : PagingSource<Int, Flow<List<Stories>>>() {
    override fun getRefreshKey(state: PagingState<Int, Flow<List<Stories>>>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<Stories>>> =
        LoadResult.Page(
            emptyList(), 0, 1
        )

    companion object {
        fun snapshot(items: List<Stories>): PagingData<Stories> {
            return PagingData.from(items)
        }
    }
}

class FakeApiService : ApiService {
    override suspend fun registrationUser(requestBody: RequestBody): RegistrationResponse =
        RegistrationResponse()

    override suspend fun loginUser(requestBody: RequestBody): LoginResponse = LoginResponse()

    override suspend fun getAllStories(token: String, page: Int, size: Int): StoriesResponse {
        val stories = ArrayList<ListStoryResponse>()

        for (i in 0..50) {
            val model = ListStoryResponse(
                photoUrl = "https://images8.alphacoders.com/112/1128982.jpg",
                createdAt = "2022-12-02T02:20:44.537Z",
                name = "Abdan Zaki Alifian",
                description = "Unit testing story app",
                lon = 106.82720257559791,
                id = "1",
                lat = -6.174786544973977
            )
            stories.add(model)
        }

        stories.subList((page - 1) * size, (page - 1) * size + size)

        return StoriesResponse(stories, false, "success")
    }

    override suspend fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): NewStoryResponse = NewStoryResponse()

    override suspend fun getLocation(token: String, location: Int): StoriesResponse =
        StoriesResponse()
}