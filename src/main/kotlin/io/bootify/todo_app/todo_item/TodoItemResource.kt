package io.bootify.todo_app.todo_item

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
    value = ["/api/todoItems"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class TodoItemResource(
    private val todoItemService: TodoItemService
) {

    @GetMapping
    fun getAllTodoItems(): ResponseEntity<List<TodoItemDTO>> =
            ResponseEntity.ok(todoItemService.findAll())

    @GetMapping("/{id}")
    fun getTodoItem(@PathVariable(name = "id") id: Long): ResponseEntity<TodoItemDTO> =
            ResponseEntity.ok(todoItemService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createTodoItem(@RequestBody @Valid todoItemDTO: TodoItemDTO): ResponseEntity<Long> {
        val createdId = todoItemService.create(todoItemDTO)
        return ResponseEntity(createdId, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateTodoItem(@PathVariable(name = "id") id: Long, @RequestBody @Valid
            todoItemDTO: TodoItemDTO): ResponseEntity<Long> {
        todoItemService.update(id, todoItemDTO)
        return ResponseEntity.ok(id)
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteTodoItem(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        todoItemService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
