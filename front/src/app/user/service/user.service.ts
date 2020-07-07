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
        this.loading = false;
      },
      (error) => {
        this.loading = false;
      }
    );
  }

  signout = (): void => {

  }

}