import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
//Для получения текущей даты и для форматирования даты.

class TaskManager {
    private val zadachi = mutableListOf<Task>()
    private val fileHandler = FileHandler()
    private var sleduyushchiyId = 1
    //Класс управляет всеми задачами.

    init {
        zagruzitZadachi()
    }
    //Блок выполняется при создании объекта.

    private fun zagruzitZadachi() {
        zadachi.addAll(fileHandler.zagruzitZadachi())
        zadachi.forEach {
            if (it.id >= sleduyushchiyId) sleduyushchiyId = it.id + 1
        }
    }
    //Добавляю загруженные задачи в список.

    private fun sohranitZadachi() {
        fileHandler.sohranitZadachi(zadachi)
    }
    //Сохраняю список задач в файл.

    fun pokazatVseZadachi() {
        if (zadachi.isEmpty()) {
            println("\nНет задач для отображения.")
            return
        }
        //вывожу все задачи.

        println("\n=== СПИСОК ВСЕХ ЗАДАЧ ===")
        zadachi.forEach { it.vyvestiInfo() }
    }

    fun dobavitZadachu() {
        println("\n=== ДОБАВЛЕНИЕ НОВОЙ ЗАДАЧИ ===")

        print("Введите название: ")
        val nazvanie = readLine() ?: ""

        print("Введите описание: ")
        val opisanie = readLine() ?: ""

        print("Введите категорию: ")
        val kategoriya = readLine() ?: ""

        val prioritet = vvesPrioritet()

        print("Введите срок выполнения (YYYY-MM-DD): ")
        val srokVypolneniya = readLine() ?: ""

        val tekushchayaData = poluchitTekushchuyuDatu()

        val zadacha = Task(
            id = sleduyushchiyId++,
            title = nazvanie,
            description = opisanie,
            category = kategoriya,
            priority = prioritet,
            dueDate = srokVypolneniya,
            isCompleted = false,
            createdAt = tekushchayaData
        )

        zadachi.add(zadacha)
        sohranitZadachi()
        println("\nЗадача успешно добавлена! ID: ${zadacha.id}")
    }

    private fun vvesPrioritet(): Priority {
        print("Введите приоритет (HIGH/MEDIUM/LOW): ")
        val vvod = readLine()?.uppercase() ?: "MEDIUM"

        return try {
            Priority.valueOf(vvod)
        } catch (e: Exception) {
            println("Неверный приоритет. Установлен MEDIUM.")
            Priority.MEDIUM
        }
    }

    private fun poluchitTekushchuyuDatu(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
    //ерёт текущую дату и форматирует как "yyyy-MM-dd"

    fun naytiZadachi() {
        println("\n=== ПОИСК ЗАДАЧ ===")
        println("1. По названию")
        println("2. По категории")
        println("3. По приоритету")
        print("Выберите критерий поиска: ")

        val rezultat = when (readLine()) {
            "1" -> poiskPoNazvaniju()
            "2" -> poiskPoKategorii()
            "3" -> poiskPoPrioritetu()
            else -> {
                println("Неверный выбор.")
                return
            }
        }

        pokazatRezultaty(rezultat)
    }

    private fun poiskPoNazvaniju(): List<Task> {
        print("Введите название (или часть): ")
        val zapros = readLine()?.lowercase() ?: ""
        return zadachi.filter { it.title.lowercase().contains(zapros) }
    }

    private fun poiskPoKategorii(): List<Task> {
        print("Введите категорию: ")
        val kategoriya = readLine() ?: ""
        return zadachi.filter { it.category.equals(kategoriya, ignoreCase = true) }
    }

    private fun poiskPoPrioritetu(): List<Task> {
        print("Введите приоритет (HIGH/MEDIUM/LOW): ")
        val vvod = readLine()?.uppercase() ?: ""

        return try {
            val prioritet = Priority.valueOf(vvod)
            zadachi.filter { it.priority == prioritet }
        } catch (e: Exception) {
            println("Неверный приоритет.")
            emptyList()
        }
    }

    private fun pokazatRezultaty(naydennye: List<Task>) {
        if (naydennye.isEmpty()) {
            println("\nЗадачи не найдены.")
        } else {
            println("\nНайдено задач: ${naydennye.size}")
            naydennye.forEach { it.vyvestiInfo() }
        }
    }

    fun otmetitKakVypolnennuyu() {
        print("\nВведите ID задачи для отметки как выполненной: ")
        val id = readLine()?.toIntOrNull() ?: run {
            println("Неверный ID.")
            return
        }

        val indeks = zadachi.indexOfFirst { it.id == id }
        if (indeks == -1) {
            println("Задача с ID $id не найдена.")
            return
        }

        val zadacha = zadachi[indeks]
        if (zadacha.isCompleted) {
            println("Задача уже отмечена как выполненная.")
            return
        }

        zadachi[indeks] = zadacha.copy(isCompleted = true)
        sohranitZadachi()
        println("Задача ID $id отмечена как выполненная!")
    }

    fun udalitZadachu() {
        print("\nВведите ID задачи для удаления: ")
        val id = readLine()?.toIntOrNull() ?: run {
            println("Неверный ID.")
            return
        }

        val udaleno = zadachi.removeIf { it.id == id }
        if (udaleno) {
            sohranitZadachi()
            println("Задача ID $id успешно удалена!")
        } else {
            println("Задача с ID $id не найдена.")
        }
    }

    fun pokazatStatistiku() {
        println("\n=== СТАТИСТИКА ЗАДАЧ ===")

        val vsego = zadachi.size
        val vypolneno = zadachi.count { it.isCompleted }
        val vRabote = vsego - vypolneno

        println("Всего задач: $vsego")
        println("Выполнено: $vypolneno")
        println("В работе: $vRabote")

        vyvestiStatistikuPoPrioritetu()
        vyvestiStatistikuPoKategoriyam()

        pokazatDopolnitelnoeMenu()
    }

    private fun vyvestiStatistikuPoPrioritetu() {
        println("\nПо приоритетам:")
        Priority.values().forEach { prioritet ->
            val kolichestvo = zadachi.count { it.priority == prioritet }
            println("  $prioritet: $kolichestvo")
        }
    }

    private fun vyvestiStatistikuPoKategoriyam() {
        println("\nПо категориям:")
        zadachi.groupBy { it.category }
            .forEach { (kategoriya, spisok) ->
                println("  $kategoriya: ${spisok.size}")
            }
    }

    private fun pokazatDopolnitelnoeMenu() {
        println("\nДополнительные опции:")
        println("1. Показать только выполненные")
        println("2. Показать только невыполненные")
        println("3. Сортировать по дате создания")
        println("4. Сортировать по сроку выполнения")
        println("5. Сортировать по приоритету")
        println("0. Назад")
        print("Выберите опцию: ")

        when (readLine()) {
            "1" -> filtrPoStatusu(true)
            "2" -> filtrPoStatusu(false)
            "3" -> sortirovkaPoDate { it.createdAt }
            "4" -> sortirovkaPoDate { it.dueDate }
            "5" -> sortirovkaPoPrioritetu()
        }
    }

    private fun filtrPoStatusu(vypolneno: Boolean) {
        val otfiltrovannye = zadachi.filter { it.isCompleted == vypolneno }
        val status = if (vypolneno) "выполненные" else "невыполненные"
        println("\nПоказаны $status задачи (${otfiltrovannye.size}):")
        otfiltrovannye.forEach { it.vyvestiInfo() }
    }

    private fun sortirovkaPoDate(selector: (Task) -> String) {
        val sortirovannye = zadachi.sortedBy(selector)
        println("\nОтсортированные задачи:")
        sortirovannye.forEach { it.vyvestiInfo() }
    }

    private fun sortirovkaPoPrioritetu() {
        val poryadok = mapOf(Priority.HIGH to 1, Priority.MEDIUM to 2, Priority.LOW to 3)
        val sortirovannye = zadachi.sortedBy { poryadok[it.priority] }
        println("\nОтсортированные по приоритету:")
        sortirovannye.forEach { it.vyvestiInfo() }
    }
}
