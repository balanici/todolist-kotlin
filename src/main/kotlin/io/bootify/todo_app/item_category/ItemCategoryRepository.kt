package io.bootify.todo_app.item_category

import org.springframework.data.jpa.repository.JpaRepository


interface ItemCategoryRepository : JpaRepository<ItemCategory, Long> {

    fun existsByTitleIgnoreCase(title: String?): Boolean

}
