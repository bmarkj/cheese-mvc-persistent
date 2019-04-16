package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired  //creates a class that implements the CategoryDao interface, and then puts that object into the categoryDao variable field below
    private CategoryDao categoryDao; //used to interact with objects stored in db

    @RequestMapping(value = "") //this is the default request handler
    public String index(Model model) {

        model.addAttribute("categories", categoryDao.findAll() );
        model.addAttribute("title", "Categories");

        return "category/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute(new Category()); //create new Category object and pass it to the view
        model.addAttribute("title", "Add Category");

        return "category/add"; //remember, these are html templates, but don't add the html extension
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Category category, Errors errors) {

        if (errors.hasErrors()) {
            return "category/add";
        }

        categoryDao.save(category);
        return "redirect:";
    }
}
