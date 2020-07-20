import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Recipe } from 'src/app/recipe/model/recipe';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  addRecipes = (files: File[]): void => {
    this.loading = true;
    const formData = new FormData();
    for (let file of files) {
      formData.append("files", file);
    }
    this.http.post(this.home + '/api/v1/admin/add', formData).subscribe(
      (data) => {
        console.log(data);
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  getRecipes = (page: number, recipes: Recipe[], pages: number[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/admin/get/' + page).subscribe(
      (data) => {
        for (let i = 0; i < data['recipes'][0].length; i++) {
          const recipe = new Recipe();
          recipe.id = data['recipes'][0][i][0];
          recipe.name = data['recipes'][0][i][1];
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

  removeRecipes = (removes: number[]): void => {
    this.loading = true;
    this.http.put(this.home + '/api/v1/admin/delete/' + removes, {}).subscribe(
      () => {
        this.loading = false;
        window.location.reload();
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

}