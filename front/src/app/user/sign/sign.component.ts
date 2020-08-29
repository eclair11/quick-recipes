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
    let name = key["nickname"];
    let word1 = key["password1"];
    let word2 = key["password2"];
    if (name == null || name.length < 3 || name.length > 12) {
      this.msgService.add("Veuillez saisir un pseudo entre 3 et 12 caractères");
    }
    else if (name.search("[^A-Za-z0-9]") != -1) {
      this.msgService.add("Veuillez vérifier que le pseudo ne contient pas des caractères spéciaux");
    }
    else if (word1 == null || word1.length < 6 || word1.length > 12) {
      this.msgService.add("Veuillez saisir un mot de passe entre 6 et 12 caractères");
    }
    else if (word1.search("[^A-Za-z0-9]") != -1) {
      this.msgService.add("Veuillez vérifier que le mot de passe ne contient pas des caractères spéciaux");
    }
    else if (word1 != word2) {
      this.msgService.add("Veuillez vérifier que les deux mots de passe sont égaux");
    }
    else {
      this.service.signup(name, word1);
    }
  }

  signingIn() {
    let key = JSON.parse(JSON.stringify(this.signIn.value));
    let name = key["nickname"];
    let word = key["password"];
    if (name == null || name.length < 3 || name.length > 12) {
      this.msgService.add("Veuillez saisir un pseudo entre 3 et 12 caractères");
    }
    else if (name.search("[^A-Za-z0-9]") != -1) {
      this.msgService.add("Veuillez vérifier que le pseudo ne contient pas des caractères spéciaux");
    }
    else if (word == null || word.length < 6 || word.length > 12) {
      this.msgService.add("Veuillez saisir un mot de passe entre 6 et 12 caractères");
    }
    else if (word.search("[^A-Za-z0-9]") != -1) {
      this.msgService.add("Veuillez vérifier que le mot de passe ne contient pas des caractères spéciaux");
    }
    else {
      this.service.signin(name, word);
    }
  }

  back() {
    this.location.back();
  }

}