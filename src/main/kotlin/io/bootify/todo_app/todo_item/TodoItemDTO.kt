package io.bootify.todo_app.todo_item

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class TodoItemDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 255)
    var title: String? = null

    @Size(max = 255)
    var description: String? = null

    @NotNull
    var status: ItemStatusEnum? = null

    @NotNull
    var itemCategory: Long? = null

}
