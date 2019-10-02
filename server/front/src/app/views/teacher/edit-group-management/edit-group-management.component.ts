import { Component, OnInit } from '@angular/core';
import { EditGroupManagementService } from './edit-group-management.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { HttpResponseBase } from '@angular/common/http';

@Component({
  selector: 'app-edit-group-management',
  templateUrl: './edit-group-management.component.html',
  styleUrls: ['./edit-group-management.component.scss']
})
export class EditGroupManagementComponent implements OnInit {
  public groupName;
  public group;
  public groupForm: FormGroup;

  constructor(private editGroupManagementService: EditGroupManagementService, private activeRoute: ActivatedRoute,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.groupName = this.activeRoute.snapshot.queryParamMap.get('groupName');
    this.getGroup(this.groupName);

    this.groupForm = this.fb.group({
      name: [this.groupName, [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      leader: [new Array(), [Validators.required, Validators.maxLength(10)]],
      members: [new Array(), Validators.minLength(3)],
    });

  }

  getGroup(groupName: string) {
    this.editGroupManagementService.getGroup(groupName).subscribe(
      response => {
        this.group = response;
        const members = new Array();
        for (const member of this.group.members) {
          if (member.id === this.group.leader) {
            this.groupForm.get('leader').setValue([member.username, member.name]);
          } else {
            members.push([member.username, member.name]);
          }
        }
        this.groupForm.get('members').setValue(members);
        console.log(response);
      }
    );
  }

  editGroupSubmit() {
    const leader = this.groupForm.get('leader').value;
    const groupName = this.groupForm.get('name').value;
    console.log('leader: ' + leader + ' groupName: ' + groupName);
    this.editGroupManagementService.editGroupLeader(groupName, leader).subscribe(
      response => {
        console.log('uppdate Leader');
      }
    );
  }

  setLeader(member: string) {
    this.groupForm.get('members').value.push(this.groupForm.get('leader').value);
    this.groupForm.get('members').setValue(this.groupForm.get('members').value.filter(item => item !== member));
    this.groupForm.get('leader').setValue(member);
  }

  removeGroupMemberByUsername(username) {
    console.log(username);
  }

}
