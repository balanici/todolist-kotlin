package io.bootify.todo_app.item_category

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class ItemCategoryDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 255)
    var title: String? = null

    @Size(max = 255)
    var description: String? = null

}
