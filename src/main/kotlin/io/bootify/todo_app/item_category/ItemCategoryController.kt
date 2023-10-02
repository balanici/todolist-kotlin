package io.bootify.todo_app.item_category

import io.bootify.todo_app.util.WebUtils
import jakarta.validation.Valid
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
@RequestMapping("/itemCategories")
class ItemCategoryController(
    private val itemCategoryService: ItemCategoryService
) {

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("itemCategories", itemCategoryService.findAll())
        return "itemCategory/list"
    }

    @GetMapping("/add")
    fun add(@ModelAttribute("itemCategory") itemCategoryDTO: ItemCategoryDTO): String =
            "itemCategory/add"

    @PostMapping("/add")
    fun add(
        @ModelAttribute("itemCategory") @Valid itemCategoryDTO: ItemCategoryDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (!bindingResult.hasFieldErrors("title") &&
                itemCategoryService.titleExists(itemCategoryDTO.title)) {
            bindingResult.rejectValue("title", "Exists.itemCategory.title")
        }
        if (bindingResult.hasErrors()) {
            return "itemCategory/add"
        }
        itemCategoryService.create(itemCategoryDTO)
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("itemCategory.create.success"))
        return "redirect:/itemCategories"
    }

    @GetMapping("/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        model.addAttribute("itemCategory", itemCategoryService.get(id))
        return "itemCategory/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute("itemCategory") @Valid itemCategoryDTO: ItemCategoryDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        val currentItemCategoryDTO: ItemCategoryDTO = itemCategoryService.get(id)
        if (!bindingResult.hasFieldErrors("title") &&
                !itemCategoryDTO.title!!.equals(currentItemCategoryDTO.title, ignoreCase = true) &&
                itemCategoryService.titleExists(itemCategoryDTO.title)) {
            bindingResult.rejectValue("title", "Exists.itemCategory.title")
        }
        if (bindingResult.hasErrors()) {
            return "itemCategory/edit"
        }
        itemCategoryService.update(id, itemCategoryDTO)
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("itemCategory.update.success"))
        return "redirect:/itemCategories"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        val referencedWarning: String? = itemCategoryService.getReferencedWarning(id)
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning)
        } else {
            itemCategoryService.delete(id)
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
                    WebUtils.getMessage("itemCategory.delete.success"))
        }
        return "redirect:/itemCategories"
    }

}
