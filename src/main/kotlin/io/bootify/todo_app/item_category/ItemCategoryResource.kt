package io.bootify.todo_app.item_category

import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import java.lang.Void
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/api/itemCategories"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ItemCategoryResource(
    private val itemCategoryService: ItemCategoryService
) {

    @GetMapping
    fun getAllItemCategories(): ResponseEntity<List<ItemCategoryDTO>> =
            ResponseEntity.ok(itemCategoryService.findAll())

    @GetMapping("/{id}")
    fun getItemCategory(@PathVariable(name = "id") id: Long): ResponseEntity<ItemCategoryDTO> =
            ResponseEntity.ok(itemCategoryService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createItemCategory(@RequestBody @Valid itemCategoryDTO: ItemCategoryDTO):
            ResponseEntity<Long> {
        val createdId = itemCategoryService.create(itemCategoryDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateItemCategory(@PathVariable(name = "id") id: Long, @RequestBody @Valid
            itemCategoryDTO: ItemCategoryDTO): ResponseEntity<Long> {
        itemCategoryService.update(id, itemCategoryDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteItemCategory(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        itemCategoryService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
