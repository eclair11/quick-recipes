import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Recipe } from '../model/recipe';
import { RecipeService } from '../service/recipe.service';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit {

  constructor(private service: RecipeService, private route: ActivatedRoute) { }

  id: number = this.route.snapshot.params['id'];
  recipe: Recipe = new Recipe();

  ngOnInit() {
    this.recipe = this.service.getRecipe(this.id);
  }

}