import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  addRecipes = (files: File[]): void => {
    this.loading = true;
    this.http.post(this.home + '/api/v1/admin/add', { files }).subscribe(
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

}