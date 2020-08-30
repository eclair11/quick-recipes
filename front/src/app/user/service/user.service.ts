import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { User } from '../model/user';
import { Recipe } from 'src/app/recipe/model/recipe';
import { MessageService } from '../../message/service/message.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private service: MessageService, private http: HttpClient, private router: Router) { }

  home: string = 'http://localhost:8080';
  loading: boolean = false;

  signup = (nickname: string, password: string): void => {
    this.loading = true;
    this.http.post<User>(this.home + '/api/v1/users/signup', { nickname, password }).subscribe(
      (data) => {
        this.service.add(data['message'][0]);
        this.loading = false;
        this.router.navigate(['/home']);
      },
      (error) => {
        this.service.add(error['error']['message'][0]);
        this.loading = false;
      }
    );
  }

  signin = (nickname: string, password: string): void => {
    this.loading = true;
    this.http.post<User>(this.home + '/api/v1/users/signin', { nickname, password }).subscribe(
      (data) => {
        sessionStorage.setItem("nickname", nickname);
        sessionStorage.setItem("token", "Bearer " + data['token'][0]);
        this.router.navigate(['/home']);
        this.loading = false;
      },
      (error) => {
        this.service.add(error['error']['token'][0]);
        this.loading = false;
      }
    );
  }

  signout = (): void => {
    sessionStorage.removeItem("nickname");
    sessionStorage.removeItem("token");
    this.router.navigate(['/home']);
  }

  signcheck = (): boolean => {
    return !(sessionStorage.getItem("nickname") === null);
  }

  admincheck = (): boolean => {
    return sessionStorage.getItem("nickname") === "adminqr";
  }

  getListFavorites = (page: number, recipes: Recipe[], pages: number[]): void => {
    this.loading = true;
    this.http.get(this.home + '/api/v1/users/list/' + sessionStorage.getItem("nickname") + '/' + page).subscribe(
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

  getFavorites = (): number[] => {
    this.loading = true;
    let favs = [];
    this.http.get(this.home + '/api/v1/users/favorites/' + sessionStorage.getItem("nickname")).subscribe(
      (data) => {
        data['favs'][0].forEach((e: number) => {
          favs.push(e);
        });
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
    return favs;
  }

  addFavorite = (id: number): void => {
    this.loading = true;
    const formData = new FormData();
    formData.append("recipe", id + "");
    formData.append("user", sessionStorage.getItem("nickname"));
    this.http.put(this.home + '/api/v1/users/favorite', formData).subscribe(
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

  removeFavorite = (id: number): void => {
    this.loading = true;
    const formData = new FormData();
    formData.append("recipe", id + "");
    formData.append("user", sessionStorage.getItem("nickname"));
    this.http.put(this.home + '/api/v1/users/unfavorite', formData).subscribe(
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