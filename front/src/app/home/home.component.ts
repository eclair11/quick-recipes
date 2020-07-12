import { Component, OnInit, Renderer2, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { HomeService } from '../home/service/home.service';
import { MessageService } from '../message/service/message.service';
import { UserService } from '../user/service/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private service: HomeService,
    private msgService: MessageService,
    private userService: UserService,
    private builder: FormBuilder,
    private router: Router,
    private renderer2: Renderer2,
    @Inject(DOCUMENT) private document: any
  ) { }

  searchNormal: FormGroup;
  searchSpecial: FormGroup;
  categories: string[] = [];
  regions: string[] = [];

  ngOnInit() {
    const s1 = this.renderer2.createElement('script');
    s1.type = 'text/javascript';
    s1.src = 'https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js';
    s1.text = '';
    this.renderer2.appendChild(this.document.body, s1);
    const s2 = this.renderer2.createElement('script');
    s2.type = 'text/javascript';
    s2.src = '../../assets/js/scripts.js';
    s2.text = '';
    this.renderer2.appendChild(this.document.body, s2);
    this.searchNormal = this.builder.group({
      search: []
    });
    this.searchSpecial = this.builder.group({
      search: []
    });
    this.service.getListCategories(this.categories);
    this.service.getListRegions(this.regions);
  }

  searchingNormal() {
    let key = JSON.parse(JSON.stringify(this.searchNormal.value));
    if (key['search'] != null) {
      this.router.navigate(['/recipe/list/normal/' + key['search'].trim()]);
    }
    else {
      this.msgService.add("Veuillez saisir un nom de recette à chercher");
    }
  }

  searchingSpecial() {
    let key = JSON.parse(JSON.stringify(this.searchSpecial.value));
    if (key['search'] != null) {
      this.router.navigate(['/recipe/list/special/' + key['search'].trim()]);
    }
    else {
      this.msgService.add("Veuillez saisir des ingrédients de recette à chercher");
    }
  }

  searchingCategory(key: string) {
    this.router.navigate(['/recipe/list/category/' + key]);
  }

  searchingRegion(key: string) {
    this.router.navigate(['/recipe/list/region/' + key]);
  }

  signingCheck(): boolean {
    if (this.userService.signcheck()) {
      return true;
    }
    else {
      return false;
    }
  }

}