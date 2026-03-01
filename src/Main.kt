fun main() {
    val menedzher = TaskManager()
    //Создаю объект TaskManager. Через него будет происходить вся работа с задачами.

    while (true) {
        vyvestiMenu()
        //Вызываю меню которое будет показываться постоянно, пока пользователь не выйдет.

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
//Считываю ввод пользователя. when проверяет, что именно он ввёл.

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
//Функция выводит меню.
