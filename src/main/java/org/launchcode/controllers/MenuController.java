package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.CheeseDao;
import org.launchcode.models.Menu;
import org.launchcode.models.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    // Request path: /menu
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu()); //pass new Menu object to the view; note that it uses the default constructor

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
        public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu); //passes in the new menu object from line 46
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId) {

        Menu menu = menuDao.findOne(menuId);
//        model.addAttribute("menu", menu);//passing in full menu object replaces individual items below
        model.addAttribute("title", menu.getName());
        model.addAttribute("cheeses", menu.getCheeses());
        model.addAttribute("menuId", menu.getId()); //passed in so view will know what menu to associate the cheese(s) (line above) with

        return"menu/view";
    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId) {

        Menu menu = menuDao.findOne(menuId); //find a specific menu based on an id number
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll()); //menu comes from line above
        model.addAttribute("form", form); //form comes from line above
        model.addAttribute("title", "Add item to menu: " + menu.getName());

        return"menu/add-item";
    }

    @RequestMapping(value="add-item", method=RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors, @RequestParam int menuId, @RequestParam int cheeseId) {

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            return"menu/add-item";
        }

        Cheese cheese = cheeseDao.findOne(cheeseId); //get teh selected cheese
        Menu menu = menuDao.findOne(menuId);
        menu.addItem((cheese)); //add the selected cheese to the current menu
        menuDao.save(menu); //save to the db
        return"redirect:/menu/view/" + menu.getId();
    }

}
