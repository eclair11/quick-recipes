import { SafeUrl } from '@angular/platform-browser';

export class Recipe {

  id: number;
  name: string;
  region: string;
  discovery: string;
  author: string;
  calorie: string;
  picture: SafeUrl;
  history: string;
  categories: string[];
  pictures: SafeUrl[] = [];
  ingredients: string[];
  preparations: string[];

  constructor() { }

}