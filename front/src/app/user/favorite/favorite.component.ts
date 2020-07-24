import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { Recipe } from 'src/app/recipe/model/recipe';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.css']
})
export class FavoriteComponent implements OnInit {

  constructor(private service: UserService, private location: Location) { }

  recipes: Recipe[] = [];
  pages: number[] = [];
  actualpage: number = 1;

  ngOnInit() {
    this.service.getListFavorites(this.actualpage, this.recipes, this.pages);
  }

  getListFavorites = (page: number): void => {
    this.recipes = [];
    this.pages = [];
    this.actualpage = page;
    this.service.getListFavorites(this.actualpage, this.recipes, this.pages);
  }

  getPages = (): number => {
    return this.pages.length;
  }

  back() {
    this.location.back();
  }

}