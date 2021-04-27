import { Component, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-add-skills',
  templateUrl: './add-skills.component.html',
  styleUrls: ['./add-skills.component.css']
})
export class AddSkillsComponent {  
  skills: string = '';

  constructor(private dialogRef: MatDialogRef<AddSkillsComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any){
  }

  onSubmit(): void {    
    this.dialogRef.close({
      submit: true,
      skills: this.skills,

    });    
  }

  onCancel() {
    this.dialogRef.close({ 
      submit: false 
    });
  }
}
