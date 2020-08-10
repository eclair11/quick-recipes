import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { AdminService } from '../service/admin.service';
import { MessageService } from 'src/app/message/service/message.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {

  constructor(private service: AdminService, private messageService: MessageService, private location: Location) { }

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
    if (this.files.length) {
      this.service.addRecipes(this.files);
      this.files = [];
    }
    else {
      this.messageService.add("Veuillez ajouter des fichiers");
    }
  }

  back() {
    this.location.back();
  }

}