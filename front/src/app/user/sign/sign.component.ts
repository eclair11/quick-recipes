import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Location } from '@angular/common';

import { UserService } from '../service/user.service';
import { MessageService } from 'src/app/message/service/message.service';

@Component({
  selector: 'app-sign',
  templateUrl: './sign.component.html',
  styleUrls: ['./sign.component.css']
})
export class SignComponent implements OnInit {

  constructor(private service: UserService,
    private msgService: MessageService,
    private builder: FormBuilder,
    private location: Location) { }

  signUp: FormGroup;
  signIn: FormGroup;

  ngOnInit() {
    this.signUp = this.builder.group({
      nickname: [],
      password1: [],
      password2: []
    });
    this.signIn = this.builder.group({
      nickname: [],
      password: []
    });
  }

  signingUp() {
    let key = JSON.parse(JSON.stringify(this.signUp.value));
    if (key['nickname'] != null && key['password1'] != null && key['password1'] == key['password2']) {
      this.service.signup(key['nickname'], key['password1']);
    }
    else {
      this.msgService.add("Veuillez saisir un nom d'utilisateur et un mot de passe");
    }
  }

  signingIn() {
    let key = JSON.parse(JSON.stringify(this.signIn.value));
    if (key['nickname'] != null && key['password'] != null) {
      this.service.signin(key['nickname'], key['password']);
    }
    else {
      this.msgService.add("Veuillez saisir un nom d'utilisateur et un mot de passe");
    }
  }

  back() {
    this.location.back();
  }

}