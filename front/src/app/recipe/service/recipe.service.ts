import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Recipe } from '../model/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(private http: HttpClient) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  getListRecipes = (page: number, recipes: Recipe[], pages: number[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/recipes/list/' + page).subscribe(
      (data) => {
        let objects = JSON.parse(JSON.stringify(data));
        for (let object of objects['list'][0]) {
          const recipe = new Recipe();
          recipe.id = object['id'];
          recipe.name = object['name'];
          recipe.pictures = object['pictures'];
          recipes.push(recipe);
        }
        for (let i = 1; i <= objects['total'][0]; i++) {
          pages.push(i);
        }
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  getRecipe = (id: number): Recipe => {
    this.loading = true;
    const recipe = new Recipe();
    this.http.get(this.home + '/api/v1/recipes/' + id).subscribe(
      (data) => {
        recipe.id = data['id'];
        recipe.name = data['name'];
        recipe.region = data['region'];
        recipe.discovery = data['discovery'];
        recipe.author = data['author'];
        recipe.history = data['history'];
        recipe.categories = data['categories'];
        recipe.pictures = data['pictures'];
        recipe.ingredients = data['ingredients'];
        recipe.preparation = data['preparation'];
        recipe.nutritionals = data['nutritionals'];
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
    return recipe;
  }

}