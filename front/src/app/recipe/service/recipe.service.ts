import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Recipe } from '../model/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  constructor(private http: HttpClient) { }

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