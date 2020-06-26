import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Recipe } from '../model/recipe';
import { RecipeService } from '../service/recipe.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  constructor(private service: RecipeService, private route: ActivatedRoute) { }

  key: string = this.route.snapshot.params['key'];
  type: string = this.route.snapshot.params['type'];
  recipes: Recipe[] = [];
  pages: number[] = [];
  actualpage: number = 1;

  ngOnInit() {
    this.service.getListRecipes(this.type, this.key, this.actualpage, this.recipes, this.pages);
  }

  getListRecipes = (page: number): void => {
    this.recipes = [];
    this.pages = [];
    this.actualpage = page;
    this.service.getListRecipes(this.type, this.key, this.actualpage, this.recipes, this.pages);
  }

  getPages = (): number => {
    return this.pages.length;
  }

}