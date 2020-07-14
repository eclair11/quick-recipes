import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { AdminService } from '../service/admin.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {

  constructor(private service: AdminService, private location: Location) { }

  files: File[] = [];

  ngOnInit() { }

  onSelect(event: any) {
    this.files.push(...event.addedFiles);
  }

  onRemove(event: any) {
    this.files.splice(this.files.indexOf(event), 1);
  }

  removeAll() {
    this.files = [];
  }

  sendAll() {
    this.service.addRecipes(this.files);
  }

  back() {
    this.location.back();
  }

}