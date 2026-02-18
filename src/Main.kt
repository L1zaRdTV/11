fun main() {
    val menedzher = TaskManager()

    while (true) {
        vyvestiMenu()

        when (readLine()) {
            "1" -> menedzher.pokazatVseZadachi()
            "2" -> menedzher.dobavitZadachu()
            "3" -> menedzher.naytiZadachi()
            "4" -> menedzher.otmetitKakVypolnennuyu()
            "5" -> menedzher.udalitZadachu()
            "6" -> menedzher.pokazatStatistiku()
            "0" -> {
                println("\nДо свидания!")
                return
            }
            else -> println("Неверный выбор. Попробуйте снова.")
        }
    }
}

fun vyvestiMenu() {
    println("\n" + "=".repeat(40))
    println("=== TO-DO LIST ===")
    println("=".repeat(40))
    println("1. Показать все задачи")
    println("2. Добавить задачу")
    println("3. Найти задачу")
    println("4. Отметить как выполненную")
    println("5. Удалить задачу")
    println("6. Статистика")
    println("0. Выход")
    println("=".repeat(40))
    print("Выберите действие: ")
}