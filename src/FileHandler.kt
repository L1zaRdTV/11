import java.io.File
//Подключаю класс File, чтобы работать с файлами.

class FileHandler(private val imyaFayla: String = "tasks.txt") {
    //Создаю класс. В конструкторе задаю имя файла. Если имя не передали используется "tasks.txt".

    fun zagruzitZadachi(): MutableList<Task> {
        val zadachi = mutableListOf<Task>()
        val fayl = File(imyaFayla)
        //Создаю функцию загрузки задач.

        if (!fayl.exists()) return zadachi
        //Если файла нет возвращаю пустой список.

        fayl.forEachLine { stroka ->
            val zadacha = parseStroku(stroka)
            if (zadacha != null) zadachi.add(zadacha)
        }
        //Если строка "Task" тогда преобразовываем ее в задачу. 

        return zadachi
    }

    private fun parseStroku(stroka: String): Task? {
        if (stroka.isBlank()) return null
        //Создаю функцию преобразования строки в задачу. Может вернуть null, если формат неверный.

        val chasti = stroka.split("|")
        if (chasti.size != 8) return null
        //Если строка пустая и не содержит 8ч то пропускаю.

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
    //Если произошла ошибка возвращаю null.

    fun sohranitZadachi(zadachi: List<Task>) {
        //Создаю функцию сохранения задач в файл.
        val soderzhanie = zadachi.joinToString("\n") {
            formatZadachu(it)
        }
        File(imyaFayla).writeText(soderzhanie)
    }
    //Объединяю все задачи в одну строку через перенос строки.
    private fun formatZadachu(zadacha: Task): String {
        //Функция превращает объект Task в строку.
        return "${zadacha.id}|${zadacha.title}|${zadacha.description}|" +
                "${zadacha.category}|${zadacha.priority}|${zadacha.dueDate}|" +
                "${zadacha.isCompleted}|${zadacha.createdAt}"
    }
}
//Соединяю все поля задачи через |.
