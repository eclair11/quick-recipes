import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Recipe } from '../model/recipe';
import { RecipeService } from '../service/recipe.service';
import { UserService } from 'src/app/user/service/user.service';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit {

  constructor(private service: RecipeService,
    private userService: UserService,
    private route: ActivatedRoute,
    private location: Location) { }

  id: number = this.route.snapshot.params['id'];
  recipe: Recipe = new Recipe();
  favs: number[] = [];

  ngOnInit() {
    this.recipe = this.service.getRecipe(this.id);
    if (this.userService.signcheck()) {
      this.favs = this.userService.getFavorites();
    }
  }

  favorite() {
    this.userService.addFavorite(this.recipe.id);
  }

  unfavorite() {
    this.userService.removeFavorite(this.recipe.id);
  }

  showfav(): boolean {
    return this.userService.signcheck() && this.favs.indexOf(this.recipe.id) == -1;
  }

  showunfav(): boolean {
    return this.userService.signcheck() && this.favs.indexOf(this.recipe.id) != -1;
  }

  back() {
    this.location.back();
  }

}