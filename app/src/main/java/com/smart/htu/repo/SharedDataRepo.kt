package com.smart.htu.repo

import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.SingleTerm
import com.smart.htu.api.module.TermCalendarEntity.TermCalendarData
import com.smart.htu.api.network.JWCAppService
import com.smart.htu.utils.TermUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface SharedDataRepository {

    /** 学生年级 */
    val userGrade: StateFlow<Int>

    /** 学期列表 */
    val termList: StateFlow<List<SingleTerm>>

    /** 当前学期 */
    val currentTermCode: StateFlow<String>

    /** 学期校历 */
    val termCalendar: StateFlow<TermCalendarData?>

    /** 学期起止 */
    val startDate: StateFlow<LocalDate>
    val endDate: StateFlow<LocalDate>
    val totalWeekCount: StateFlow<Int>

    /** 当前为第几周 */
    val weekIndex: StateFlow<Int>
        get() = startDate.map { startDate ->
            startDate.let {
                val now = LocalDate.now()
                val weeksBetween =
                    java.time.temporal.ChronoUnit.WEEKS.between(startDate, now).toInt()
                if (weeksBetween < 0) 1 else weeksBetween + 1
            }
        }
            .stateIn(
                scope = (this as SharedDataRepoImpl).scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 1
            )

    /** 每一周周一的日期 **/
    fun getDateOfWeekMonday(weekIndex: Int): LocalDate {
        val start = startDate.value
        return start.plusWeeks(weekIndex.toLong())
    }

    suspend fun refreshTermCalendar(termCode: String = "")
}

@Singleton
class SharedDataRepoImpl @Inject constructor(
    private val jwcAppService: JWCAppService,
    private val dataStoreRepo: DataStoreRepo
) : SharedDataRepository {

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _userGrade = MutableStateFlow(0)
    override val userGrade = _userGrade.asStateFlow()
    val studentIdStateFlow = dataStoreRepo.observeStudentId()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _termList = MutableStateFlow<List<SingleTerm>>(emptyList())
    override val termList = _termList.asStateFlow()

    private val _currentTermCode = MutableStateFlow(TermUtil.getCurrentTerm())
    override val currentTermCode = _currentTermCode.asStateFlow()

    private val _termCalendar = MutableStateFlow<TermCalendarData?>(null)
    override val termCalendar = _termCalendar.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate>(LocalDate.now())
    override val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate>(LocalDate.now())
    override val endDate = _endDate.asStateFlow()

    private val _totalWeekCount = MutableStateFlow(0)
    override val totalWeekCount = _totalWeekCount.asStateFlow()

    init {
        observeGrade()
        observeTermIndex()
        scope.launch {
            refreshTermCalendar("")
        }
    }

    override suspend fun refreshTermCalendar(termCode: String) {
        val res =
            jwcAppService.getTermCalendar(if (termCode == "") Object() else GlobalTerm(termCode))
        if (res.code != 200) return
        val startDay = res.calendar.months.firstOrNull()
            ?.weeks?.firstOrNull()
            ?.days?.firstOrNull()

        val endDay = res.calendar.months.lastOrNull()
            ?.weeks?.lastOrNull()
            ?.days?.lastOrNull()

        _startDate.value = LocalDate.parse(startDay?.dateString)
        _endDate.value = LocalDate.parse(endDay?.dateString)

        _totalWeekCount.value = res.calendar.months.last().weeks.last().weekIndex

        _termCalendar.value = res.calendar
    }


    private fun observeGrade() {
        studentIdStateFlow.map { it.take(2).toIntOrNull() ?: 0 }
            .distinctUntilChanged()
            .onEach { grade ->
                _userGrade.value = grade
            }
            .launchIn(scope)
    }

    private fun observeTermIndex() {
        userGrade
            .filter { it > 0 }
            .onEach { grade ->
                _termList.value = TermUtil.generateTermList(grade)
            }
            .launchIn(scope)
    }


}