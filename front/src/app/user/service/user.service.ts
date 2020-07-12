import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { User } from '../model/user';
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
        console.log(data);
        sessionStorage.setItem("nickname", nickname);
        sessionStorage.setItem("token", "Bearer " + data['token'][0]);
        this.router.navigate(['/home']);
        this.loading = false;
      },
      (error) => {
        console.log(error);
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
    return !(sessionStorage.getItem('nickname') === null);
  }

}