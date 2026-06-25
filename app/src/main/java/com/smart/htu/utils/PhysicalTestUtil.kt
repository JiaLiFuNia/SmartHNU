package com.smart.htu.utils

import com.smart.htu.screens.application.physicalTest.PhysicalTestScore
import java.time.LocalDate
import kotlin.math.roundToInt

object PhysicalTestUtil {

    enum class Gender { MALE, FEMALE }

    /** 大一大二 / 大三大四 */
    enum class YearLevel(val label: String) {
        FRESHMAN_SOPHOMORE("大一大二"),
        JUNIOR_SENIOR("大三大四")
    }

    data class ItemResult(val score: Int, val level: String)

    data class ScoreResult(
        val total: Float,
        val level: String,
        val bmi: ItemResult,
        val vitalCapacity: ItemResult,
        val fiftyMeterRun: ItemResult,
        val sitAndReach: ItemResult,
        val standingLongJump: ItemResult,
        val enduranceRun: ItemResult,
        val strengthExercise: ItemResult
    )

    fun getYearLevel(grade: Int): YearLevel {
        val currentYear = LocalDate.now().year
        return when (currentYear - grade) {
            0, 1 -> YearLevel.FRESHMAN_SOPHOMORE
            else -> YearLevel.JUNIOR_SENIOR
        }
    }

    fun calculate(
        score: PhysicalTestScore,
        gender: Gender = Gender.MALE,
        yearLevel: YearLevel = YearLevel.FRESHMAN_SOPHOMORE
    ): ScoreResult {
        val bmiResult = calcBmi(score.height, score.weight, gender)
        val vcResult = calcVitalCapacity(score.vitalCapacity, gender, yearLevel)
        val fiftyResult = calcFiftyMeterRun(score.fiftyMeterRun, gender, yearLevel)
        val sitResult = calcSitAndReach(score.sitAndReach, gender, yearLevel)
        val jumpResult = calcStandingLongJump(score.standingLongJump, gender, yearLevel)
        val enduranceResult = calcEnduranceRun(score.enduranceRun, gender, yearLevel)
        val strengthResult = calcStrengthExercise(score.strengthExercise, gender, yearLevel)

        val total = (bmiResult.score * 0.15f
                + vcResult.score * 0.15f
                + fiftyResult.score * 0.20f
                + sitResult.score * 0.10f
                + jumpResult.score * 0.10f
                + enduranceResult.score * 0.20f
                + strengthResult.score * 0.10f)

        val level = when {
            total >= 90f -> "优秀"
            total >= 80f -> "良好"
            total >= 60f -> "及格"
            else -> "不及格"
        }

        return ScoreResult(
            total = ((total * 10).roundToInt() / 10f),
            level = level,
            bmi = bmiResult,
            vitalCapacity = vcResult,
            fiftyMeterRun = fiftyResult,
            sitAndReach = sitResult,
            standingLongJump = jumpResult,
            enduranceRun = enduranceResult,
            strengthExercise = strengthResult
        )
    }

    private fun scoreLevel(score: Int): String = when {
        score >= 80 -> "良好"
        score >= 60 -> "及格"
        else -> "不及格"
    }

    // ==================== BMI ====================
    // 占总分15%，单项满分100
    // 男生: 正常 17.9~23.9 = 100; 低体重 <=17.8 = 80; 超重 24.0~27.9 = 80; 肥胖 >=28 = 60
    // 女生: 正常 17.2~23.9 = 100; 低体重 <=17.1 = 80; 超重 24.0~27.9 = 80; 肥胖 >=28 = 60
    private fun calcBmi(heightCm: Int, weightKg: Float, gender: Gender): ItemResult {
        val heightM = heightCm / 100f
        val bmi = weightKg / (heightM * heightM)
        val lowThreshold = if (gender == Gender.MALE) 17.9f else 17.2f
        val score = when {
            bmi in lowThreshold..23.9f -> 100
            bmi < lowThreshold -> 80
            bmi in 24.0f..27.9f -> 80
            else -> 60
        }
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 肺活量 (ML) ====================
    // 占总分15%，单项满分100
    private fun calcVitalCapacity(value: Int, gender: Gender, yearLevel: YearLevel): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                5040 to 100, 4920 to 95, 4800 to 90, 4550 to 85, 4300 to 80,
                4180 to 78, 4060 to 76, 3940 to 74, 3820 to 72, 3700 to 70,
                3580 to 68, 3460 to 66, 3340 to 64, 3220 to 62, 3100 to 60,
                2940 to 50, 2780 to 40, 2620 to 30, 2460 to 20, 2300 to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                5140 to 100, 5020 to 95, 4900 to 90, 4650 to 85, 4400 to 80,
                4280 to 78, 4160 to 76, 4040 to 74, 3920 to 72, 3800 to 70,
                3680 to 68, 3560 to 66, 3440 to 64, 3320 to 62, 3200 to 60,
                3030 to 50, 2860 to 40, 2690 to 30, 2520 to 20, 2350 to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                3400 to 100, 3350 to 95, 3300 to 90, 3150 to 85, 3000 to 80,
                2900 to 78, 2800 to 76, 2700 to 74, 2600 to 72, 2500 to 70,
                2400 to 68, 2300 to 66, 2200 to 64, 2100 to 62, 2000 to 60,
                1960 to 50, 1920 to 40, 1880 to 30, 1840 to 20, 1800 to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR
                3450 to 100, 3400 to 95, 3350 to 90, 3200 to 85, 3050 to 80,
                2950 to 78, 2850 to 76, 2750 to 74, 2650 to 72, 2550 to 70,
                2450 to 68, 2350 to 66, 2250 to 64, 2150 to 62, 2050 to 60,
                2010 to 50, 1970 to 40, 1930 to 30, 1890 to 20, 1850 to 10
            )
        }
        val score = lookupDescending(value, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 50米跑 (秒) ====================
    // 占总分20%，单项满分100，越快越好（值越小分越高）
    private fun calcFiftyMeterRun(
        seconds: Float,
        gender: Gender,
        yearLevel: YearLevel
    ): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                6.7f to 100, 6.8f to 95, 6.9f to 90, 7.0f to 85, 7.1f to 80,
                7.3f to 78, 7.5f to 76, 7.7f to 74, 7.9f to 72, 8.1f to 70,
                8.3f to 68, 8.5f to 66, 8.7f to 64, 8.9f to 62, 9.1f to 60,
                9.3f to 50, 9.5f to 40, 9.7f to 30, 9.9f to 20, 10.1f to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                6.6f to 100, 6.7f to 95, 6.8f to 90, 6.9f to 85, 7.0f to 80,
                7.2f to 78, 7.4f to 76, 7.6f to 74, 7.8f to 72, 8.0f to 70,
                8.2f to 68, 8.4f to 66, 8.6f to 64, 8.8f to 62, 9.0f to 60,
                9.2f to 50, 9.4f to 40, 9.6f to 30, 9.8f to 20, 10.0f to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                7.5f to 100, 7.6f to 95, 7.7f to 90, 8.0f to 85, 8.3f to 80,
                8.5f to 78, 8.7f to 76, 8.9f to 74, 9.1f to 72, 9.3f to 70,
                9.5f to 68, 9.7f to 66, 9.9f to 64, 10.1f to 62, 10.3f to 60,
                10.5f to 50, 10.7f to 40, 10.9f to 30, 11.1f to 20, 11.3f to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR
                7.4f to 100, 7.5f to 95, 7.6f to 90, 7.9f to 85, 8.2f to 80,
                8.4f to 78, 8.6f to 76, 8.8f to 74, 9.0f to 72, 9.2f to 70,
                9.4f to 68, 9.6f to 66, 9.8f to 64, 10.0f to 62, 10.2f to 60,
                10.4f to 50, 10.6f to 40, 10.8f to 30, 11.0f to 20, 11.2f to 10
            )
        }
        val score = lookupAscending(seconds, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 坐位体前屈 (CM) ====================
    // 占总分10%，单项满分100
    private fun calcSitAndReach(cm: Float, gender: Gender, yearLevel: YearLevel): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                24.9f to 100, 23.1f to 95, 21.3f to 90, 19.5f to 85, 17.7f to 80,
                16.3f to 78, 14.9f to 76, 13.5f to 74, 12.1f to 72, 10.7f to 70,
                9.7f to 68, 8.7f to 66, 7.7f to 64, 6.7f to 62, 5.7f to 60,
                4.7f to 50, 3.7f to 40, 2.7f to 30, 1.7f to 20, 0.7f to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                25.1f to 100, 23.3f to 95, 21.5f to 90, 19.9f to 85, 18.2f to 80,
                16.8f to 78, 15.4f to 76, 14.0f to 74, 12.6f to 72, 11.2f to 70,
                9.8f to 68, 8.4f to 66, 7.0f to 64, 5.6f to 62, 4.2f to 60,
                3.2f to 50, 2.2f to 40, 1.2f to 30, 0.2f to 20, -0.8f to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                25.8f to 100, 24.0f to 95, 22.2f to 90, 20.6f to 85, 19.0f to 80,
                17.7f to 78, 16.4f to 76, 15.1f to 74, 13.8f to 72, 12.5f to 70,
                11.5f to 68, 10.5f to 66, 9.5f to 64, 8.5f to 62, 7.5f to 60,
                6.5f to 50, 5.5f to 40, 4.5f to 30, 3.5f to 20, 2.5f to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR
                26.3f to 100, 24.4f to 95, 22.4f to 90, 21.0f to 85, 19.5f to 80,
                18.2f to 78, 16.9f to 76, 15.6f to 74, 14.3f to 72, 13.0f to 70,
                11.7f to 68, 10.4f to 66, 9.1f to 64, 7.8f to 62, 6.5f to 60,
                5.7f to 50, 4.9f to 40, 4.1f to 30, 3.3f to 20, 2.5f to 10
            )
        }
        val score = lookupDescending(cm, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 立定跳远 (米) ====================
    // 占总分10%，单项满分100
    private fun calcStandingLongJump(
        meters: Float,
        gender: Gender,
        yearLevel: YearLevel
    ): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                2.73f to 100, 2.68f to 95, 2.63f to 90, 2.56f to 85, 2.48f to 80,
                2.44f to 78, 2.40f to 76, 2.36f to 74, 2.32f to 72, 2.28f to 70,
                2.24f to 68, 2.20f to 66, 2.16f to 64, 2.12f to 62, 2.08f to 60,
                2.03f to 50, 1.98f to 40, 1.93f to 30, 1.88f to 20, 1.83f to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                2.75f to 100, 2.70f to 95, 2.65f to 90, 2.58f to 85, 2.50f to 80,
                2.46f to 78, 2.42f to 76, 2.38f to 74, 2.34f to 72, 2.30f to 70,
                2.26f to 68, 2.22f to 66, 2.18f to 64, 2.14f to 62, 2.10f to 60,
                2.05f to 50, 2.00f to 40, 1.95f to 30, 1.90f to 20, 1.85f to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                2.07f to 100, 2.01f to 95, 1.95f to 90, 1.88f to 85, 1.81f to 80,
                1.77f to 78, 1.73f to 76, 1.69f to 74, 1.65f to 72, 1.61f to 70,
                1.57f to 68, 1.53f to 66, 1.49f to 64, 1.45f to 62, 1.41f to 60,
                1.37f to 50, 1.33f to 40, 1.29f to 30, 1.25f to 20, 1.21f to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR
                2.08f to 100, 2.02f to 95, 1.96f to 90, 1.89f to 85, 1.82f to 80,
                1.78f to 78, 1.74f to 76, 1.70f to 74, 1.66f to 72, 1.62f to 70,
                1.58f to 68, 1.54f to 66, 1.50f to 64, 1.46f to 62, 1.42f to 60,
                1.38f to 50, 1.34f to 40, 1.30f to 30, 1.26f to 20, 1.22f to 10
            )
        }
        val score = lookupDescending(meters, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 耐力跑 ====================
    // 占总分20%，单项满分100
    // 男生1000米，女生800米，值越小分越高（格式：分.秒，如4.30 = 4分30秒）
    private fun calcEnduranceRun(value: Float, gender: Gender, yearLevel: YearLevel): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                3.17f to 100, 3.22f to 95, 3.27f to 90, 3.34f to 85, 3.42f to 80,
                3.47f to 78, 3.52f to 76, 3.57f to 74, 4.02f to 72, 4.07f to 70,
                4.12f to 68, 4.17f to 66, 4.22f to 64, 4.27f to 62, 4.32f to 60,
                4.52f to 50, 5.12f to 40, 5.32f to 30, 5.52f to 20, 6.12f to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                3.15f to 100, 3.20f to 95, 3.25f to 90, 3.32f to 85, 3.40f to 80,
                3.45f to 78, 3.50f to 76, 3.55f to 74, 4.00f to 72, 4.05f to 70,
                4.10f to 68, 4.15f to 66, 4.20f to 64, 4.25f to 62, 4.30f to 60,
                4.50f to 50, 5.10f to 40, 5.30f to 30, 5.50f to 20, 6.10f to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                3.18f to 100, 3.24f to 95, 3.30f to 90, 3.37f to 85, 3.44f to 80,
                3.49f to 78, 3.54f to 76, 3.59f to 74, 4.04f to 72, 4.09f to 70,
                4.14f to 68, 4.19f to 66, 4.24f to 64, 4.29f to 62, 4.34f to 60,
                4.44f to 50, 4.54f to 40, 5.04f to 30, 5.14f to 20, 5.24f to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR
                3.16f to 100, 3.22f to 95, 3.28f to 90, 3.35f to 85, 3.42f to 80,
                3.47f to 78, 3.52f to 76, 3.57f to 74, 4.02f to 72, 4.07f to 70,
                4.12f to 68, 4.17f to 66, 4.22f to 64, 4.27f to 62, 4.32f to 60,
                4.42f to 50, 4.52f to 40, 5.02f to 30, 5.12f to 20, 5.22f to 10
            )
        }
        val score = lookupAscending(value, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 力量练习 ====================
    // 占总分10%，单项满分100
    // 男生引体向上（个），女生1分钟仰卧起坐（个）
    private fun calcStrengthExercise(count: Int, gender: Gender, yearLevel: YearLevel): ItemResult {
        val table = when (gender) {
            Gender.MALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                19 to 100, 18 to 95, 17 to 90, 16 to 85, 15 to 80,
                14 to 76, 13 to 72, 12 to 68, 11 to 64, 10 to 60,
                9 to 50, 8 to 40, 7 to 30, 6 to 20, 5 to 10
            )
            Gender.MALE if yearLevel == YearLevel.JUNIOR_SENIOR -> listOf(
                20 to 100, 19 to 95, 18 to 90, 17 to 85, 16 to 80,
                15 to 76, 14 to 72, 13 to 68, 12 to 64, 11 to 60,
                10 to 50, 9 to 40, 8 to 30, 7 to 20, 6 to 10
            )
            Gender.FEMALE if yearLevel == YearLevel.FRESHMAN_SOPHOMORE -> listOf(
                56 to 100, 54 to 95, 52 to 90, 49 to 85, 46 to 80,
                44 to 76, 42 to 72, 40 to 68, 38 to 64, 36 to 60,
                34 to 50, 32 to 40, 30 to 30, 28 to 20, 26 to 10
            )
            else -> listOf( // FEMALE + JUNIOR_SENIOR (matches _extracted_tables.txt rows 2..16)
                57 to 100, 55 to 95, 53 to 90, 50 to 85, 47 to 80,
                45 to 76, 43 to 72, 41 to 68, 39 to 64, 37 to 60,
                35 to 50, 33 to 40, 31 to 30, 29 to 20, 27 to 10
            )
        }
        val score = lookupDescending(count, table, 0)
        return ItemResult(score, scoreLevel(score))
    }

    // ==================== 查找工具 ====================
    // 值越大分越高（肺活量、坐位体前屈、立定跳远、力量练习）
    private fun <T : Comparable<T>> lookupDescending(
        value: T,
        table: List<Pair<T, Int>>,
        default: Int
    ): Int {
        for ((threshold, score) in table) {
            if (value >= threshold) return score
        }
        return default
    }

    // 值越小分越高（50米跑、耐力跑）
    private fun <T : Comparable<T>> lookupAscending(
        value: T,
        table: List<Pair<T, Int>>,
        default: Int
    ): Int {
        for ((threshold, score) in table) {
            if (value <= threshold) return score
        }
        return default
    }
}
