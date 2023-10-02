package io.bootify.todo_app.item_category

import io.bootify.todo_app.todo_item.TodoItemRepository
import io.bootify.todo_app.util.NotFoundException
import io.bootify.todo_app.util.WebUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class ItemCategoryService(
    private val itemCategoryRepository: ItemCategoryRepository,
    private val todoItemRepository: TodoItemRepository
) {

    fun findAll(): List<ItemCategoryDTO> {
        val itemCategories = itemCategoryRepository.findAll(Sort.by("id"))
        return itemCategories.stream()
                .map { itemCategory -> mapToDTO(itemCategory, ItemCategoryDTO()) }
                .toList()
    }

    fun `get`(id: Long): ItemCategoryDTO = itemCategoryRepository.findById(id)
            .map { itemCategory -> mapToDTO(itemCategory, ItemCategoryDTO()) }
            .orElseThrow { NotFoundException() }

    fun create(itemCategoryDTO: ItemCategoryDTO): Long {
        val itemCategory = ItemCategory()
        mapToEntity(itemCategoryDTO, itemCategory)
        return itemCategoryRepository.save(itemCategory).id!!
    }

    fun update(id: Long, itemCategoryDTO: ItemCategoryDTO) {
        val itemCategory = itemCategoryRepository.findById(id)
                .orElseThrow { NotFoundException() }
        mapToEntity(itemCategoryDTO, itemCategory)
        itemCategoryRepository.save(itemCategory)
    }

    fun delete(id: Long) {
        itemCategoryRepository.deleteById(id)
    }

    private fun mapToDTO(itemCategory: ItemCategory, itemCategoryDTO: ItemCategoryDTO):
            ItemCategoryDTO {
        itemCategoryDTO.id = itemCategory.id
        itemCategoryDTO.title = itemCategory.title
        itemCategoryDTO.description = itemCategory.description
        return itemCategoryDTO
    }

    private fun mapToEntity(itemCategoryDTO: ItemCategoryDTO, itemCategory: ItemCategory):
            ItemCategory {
        itemCategory.title = itemCategoryDTO.title
        itemCategory.description = itemCategoryDTO.description
        return itemCategory
    }

    fun titleExists(title: String?): Boolean = itemCategoryRepository.existsByTitleIgnoreCase(title)

    fun getReferencedWarning(id: Long): String? {
        val itemCategory = itemCategoryRepository.findById(id)
                .orElseThrow { NotFoundException() }
        val itemCategoryTodoItem = todoItemRepository.findFirstByItemCategory(itemCategory)
        if (itemCategoryTodoItem != null) {
            return WebUtils.getMessage("itemCategory.todoItem.itemCategory.referenced",
                    itemCategoryTodoItem.id)
        }
        return null
    }

}
