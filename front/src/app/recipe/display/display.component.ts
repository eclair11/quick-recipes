import { Component, OnInit } from '@angular/core';

import { Recipe } from '../model/recipe';
import { RecipeService } from '../service/recipe.service';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit {

  recipe = new Recipe();

  constructor(private service: RecipeService) { }

  ngOnInit() {
    this.recipe = this.service.getRecipe(1);
  }

}
