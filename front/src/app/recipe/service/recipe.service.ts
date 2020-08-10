import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';

import { Recipe } from '../model/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(private http: HttpClient, private sanitizer: DomSanitizer) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  getListRecipes = (type: string, key: string, page: number, recipes: Recipe[], pages: number[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/recipes/list/' + type + '/' + key + '/' + page).subscribe(
      (data) => {
        for (let i = 0; i < data['recipes'][0].length; i++) {
          const recipe = new Recipe();
          recipe.id = data['recipes'][0][i][0];
          recipe.name = data['recipes'][0][i][1];
          recipe.picture = this.sanitizer.bypassSecurityTrustUrl(data['recipes'][0][i][2]);
          recipes.push(recipe);
        }
        for (let i = 1; i <= data['pages'][0]; i++) {
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
        recipe.calorie = data['calorie'];
        recipe.picture = this.sanitizer.bypassSecurityTrustUrl(data['picture']);
        recipe.history = data['history'];
        recipe.categories = data['categories'];
        data['pictures'].forEach((pic: string) => {
          recipe.pictures.push(this.sanitizer.bypassSecurityTrustUrl(pic));
        });
        recipe.ingredients = data['ingredients'];
        recipe.preparations = data['preparations'];
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