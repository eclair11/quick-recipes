import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Recipe } from 'src/app/recipe/model/recipe';
import { AdminService } from '../service/admin.service';

@Component({
  selector: 'app-remove',
  templateUrl: './remove.component.html',
  styleUrls: ['./remove.component.css']
})
export class RemoveComponent implements OnInit {

  constructor(private service: AdminService, private route: ActivatedRoute, private location: Location) { }

  recipes: Recipe[] = [];
  removes: number[] = [];
  pages: number[] = [];
  actualpage: number = 1;

  ngOnInit() {
    this.service.getRecipes(this.actualpage, this.recipes, this.pages);
  }

  getListRecipes = (page: number): void => {
    this.recipes = [];
    this.removes = [];
    this.pages = [];
    this.actualpage = page;
    this.service.getRecipes(this.actualpage, this.recipes, this.pages);
  }

  getPages = (): number => {
    return this.pages.length;
  }

  setRemove = (id: number): void => {
    let index = this.removes.indexOf(id);
    let element = document.getElementById(id.toString());
    if (index == -1) {
      this.removes.push(id);
      element.style.background = "#6f42c1";
    }
    else {
      this.removes.splice(index, 1);
      element.style.background = "";
    }
  }

  remove = (): void => {
    this.service.removeRecipes(this.removes);
  }

  back() {
    this.location.back();
  }

}