import java.io.File

class FileHandler(private val imyaFayla: String = "tasks.txt") {

    fun zagruzitZadachi(): MutableList<Task> {
        val zadachi = mutableListOf<Task>()
        val fayl = File(imyaFayla)

        if (!fayl.exists()) return zadachi

        fayl.forEachLine { stroka ->
            val zadacha = parseStroku(stroka)
            if (zadacha != null) zadachi.add(zadacha)
        }

        return zadachi
    }

    private fun parseStroku(stroka: String): Task? {
        if (stroka.isBlank()) return null

        val chasti = stroka.split("|")
        if (chasti.size != 8) return null

        return try {
            Task(
                id = chasti[0].toInt(),
                title = chasti[1],
                description = chasti[2],
                category = chasti[3],
                priority = Priority.valueOf(chasti[4]),
                dueDate = chasti[5],
                isCompleted = chasti[6].toBoolean(),
                createdAt = chasti[7]
            )
        } catch (e: Exception) {
            null
        }
    }

    fun sohranitZadachi(zadachi: List<Task>) {
        val soderzhanie = zadachi.joinToString("\n") {
            formatZadachu(it)
        }
        File(imyaFayla).writeText(soderzhanie)
    }

    private fun formatZadachu(zadacha: Task): String {
        return "${zadacha.id}|${zadacha.title}|${zadacha.description}|" +
                "${zadacha.category}|${zadacha.priority}|${zadacha.dueDate}|" +
                "${zadacha.isCompleted}|${zadacha.createdAt}"
    }
}