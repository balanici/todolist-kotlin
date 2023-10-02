package io.bootify.todo_app.todo_item

import io.bootify.todo_app.item_category.ItemCategoryRepository
import io.bootify.todo_app.util.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class TodoItemService(
    private val todoItemRepository: TodoItemRepository,
    private val itemCategoryRepository: ItemCategoryRepository
) {

    fun findAll(): List<TodoItemDTO> {
        val todoItems = todoItemRepository.findAll(Sort.by("id"))
        return todoItems.stream()
                .map { todoItem -> mapToDTO(todoItem, TodoItemDTO()) }
                .toList()
    }

    fun `get`(id: Long): TodoItemDTO = todoItemRepository.findById(id)
            .map { todoItem -> mapToDTO(todoItem, TodoItemDTO()) }
            .orElseThrow { NotFoundException() }

    fun create(todoItemDTO: TodoItemDTO): Long {
        val todoItem = TodoItem()
        mapToEntity(todoItemDTO, todoItem)
        return todoItemRepository.save(todoItem).id!!
    }

    fun update(id: Long, todoItemDTO: TodoItemDTO) {
        val todoItem = todoItemRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(todoItemDTO, todoItem)
        todoItemRepository.save(todoItem)
    }

    fun delete(id: Long) {
        todoItemRepository.deleteById(id)
    }

    private fun mapToDTO(todoItem: TodoItem, todoItemDTO: TodoItemDTO): TodoItemDTO {
        todoItemDTO.id = todoItem.id
        todoItemDTO.title = todoItem.title
        todoItemDTO.description = todoItem.description
        todoItemDTO.status = todoItem.status
        todoItemDTO.itemCategory = todoItem.itemCategory?.id
        return todoItemDTO
    }

    private fun mapToEntity(todoItemDTO: TodoItemDTO, todoItem: TodoItem): TodoItem {
        todoItem.title = todoItemDTO.title
        todoItem.description = todoItemDTO.description
        todoItem.status = todoItemDTO.status
        val itemCategory = if (todoItemDTO.itemCategory == null) null else
                itemCategoryRepository.findById(todoItemDTO.itemCategory!!)
                .orElseThrow { NotFoundException("itemCategory not found") }
        todoItem.itemCategory = itemCategory
        return todoItem
    }

}
