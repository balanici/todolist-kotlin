package io.bootify.todo_app.todo_item

import io.bootify.todo_app.item_category.ItemCategory
import io.bootify.todo_app.item_category.ItemCategoryRepository
import io.bootify.todo_app.util.CustomCollectors
import io.bootify.todo_app.util.WebUtils
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
@RequestMapping("/todoItems")
class TodoItemController(
    private val todoItemService: TodoItemService,
    private val itemCategoryRepository: ItemCategoryRepository
) {

    @ModelAttribute
    fun prepareContext(model: Model) {
        model.addAttribute("statusValues", ItemStatusEnum.values())
        model.addAttribute("itemCategoryValues", itemCategoryRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ItemCategory::id, ItemCategory::title)))
    }

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("todoItems", todoItemService.findAll())
        return "todoItem/list"
    }

    @GetMapping("/add")
    fun add(@ModelAttribute("todoItem") todoItemDTO: TodoItemDTO): String = "todoItem/add"

    @PostMapping("/add")
    fun add(
        @ModelAttribute("todoItem") @Valid todoItemDTO: TodoItemDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return "todoItem/add"
        }
        todoItemService.create(todoItemDTO)
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("todoItem.create.success"))
        return "redirect:/todoItems"
    }

    @GetMapping("/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        model.addAttribute("todoItem", todoItemService.get(id))
        return "todoItem/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute("todoItem") @Valid todoItemDTO: TodoItemDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return "todoItem/edit"
        }
        todoItemService.update(id, todoItemDTO)
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("todoItem.update.success"))
        return "redirect:/todoItems"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        todoItemService.delete(id)
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
                WebUtils.getMessage("todoItem.delete.success"))
        return "redirect:/todoItems"
    }

}
