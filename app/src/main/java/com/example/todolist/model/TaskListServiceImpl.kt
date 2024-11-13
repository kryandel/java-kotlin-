package com.example.todolist.model

class TaskListServiceImpl : TaskListService {

    private val lists = mutableListOf<TaskList>()

    override fun createTaskList(task: TaskList) {
        lists.add(task)
    }

    override fun getTaskList(id: Int) : Result<TaskList> {
        val index = lists.indexOfFirst { it.id == id }
        if (index != -1) {
            return Result.success(lists[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found TaskList with index %d".format(id)
        ))
    }

    override fun changeTaskList(id: Int, new_value: TaskList) {
        val list = getTaskList(id)
        if (list.isFailure) {
            return
        }

        lists[id] = new_value
    }

    override fun deleteTaskList(id: Int) {
        val list = getTaskList(id)
        if (list.isFailure) {
            return
        }

        lists.removeAt(id)
    }
}