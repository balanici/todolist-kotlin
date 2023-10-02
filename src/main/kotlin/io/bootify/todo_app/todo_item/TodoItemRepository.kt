package io.bootify.todo_app.todo_item

import io.bootify.todo_app.item_category.ItemCategory
import org.springframework.data.jpa.repository.JpaRepository


interface TodoItemRepository : JpaRepository<TodoItem, Long> {

    fun findFirstByItemCategory(itemCategory: ItemCategory): TodoItem?

}
