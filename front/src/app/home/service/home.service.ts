import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  getListCategories = (categories: string[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/recipes/categories').subscribe(
      (data) => {
        let objects = JSON.parse(JSON.stringify(data));
        for (let object of objects) {
          categories.push(object);
        }
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  getListRegions = (regions: string[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/recipes/regions').subscribe(
      (data) => {
        let objects = JSON.parse(JSON.stringify(data));
        for (let object of objects) {
          regions.push(object);
        }
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

}