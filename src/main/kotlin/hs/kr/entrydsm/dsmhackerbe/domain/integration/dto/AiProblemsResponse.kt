package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto

data class AiProblemsResponse(
    val problems: List<AiProblem>
)

data class AiProblem(
    val type: String,
    val text: String,
    val choices: List<String>? = null,
    val correct_choice: String? = null,
    val correct_answer: String? = null
)
