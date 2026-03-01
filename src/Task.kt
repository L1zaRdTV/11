data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val priority: Priority,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val createdAt: String
)
//Создаю data-класс задачи.
{
    fun vyvestiInfo() {
        val status = if (isCompleted) "Выполнено" else "В работе"
        println("\nID: $id | Статус: $status")
        println("Название: $title")
        println("Описание: $description")
        println("Категория: $category")
        println("Приоритет: $priority")
        println("Срок: $dueDate")
        println("Создано: $createdAt")
        println("-".repeat(50))
    }
}
//Метод вывода информации о задаче.
