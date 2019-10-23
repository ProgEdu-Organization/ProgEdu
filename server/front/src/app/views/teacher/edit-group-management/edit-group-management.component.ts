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
  public users;
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
    this.getAllUser();

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
      }
    );
  }

  editLeaderSubmit() {
    const leader = this.groupForm.get('leader').value;
    const groupName = this.groupForm.get('name').value;
    this.editGroupManagementService.editGroupLeader(groupName, leader).subscribe(
      response => {
        this.getGroup(groupName);
      }
    );
  }

  setLeader(member: string) {
    this.groupForm.get('members').value.push(this.groupForm.get('leader').value);
    this.groupForm.get('members').setValue(this.groupForm.get('members').value.filter(item => item !== member));
    this.groupForm.get('leader').setValue(member);
  }

  removeGroupMemberByUsername(groupName: string, member: string) {
    this.editGroupManagementService.deleteGroupMember(groupName, member).subscribe(
      () => {
        this.getGroup(groupName);
        this.getAllUser();
      }
    );
  }

  getAllUser() {
    this.editGroupManagementService.getAllUser().subscribe(response => {
      this.users = response.Users;
      // reGet the all user data and remove exist in  group merber
      const selectedUsers = this.groupForm.get('members').value;
      for (const i of selectedUsers) {
        if (i) {
          this.users = this.users.filter(item => {
            return item.username !== i[0];
          });
        }
      }
    });
  }

  addGroupMemberByUsername(groupName: string, username: string) {
    this.editGroupManagementService.addGroupMemeber(groupName, username).subscribe(
      () => {
        this.getGroup(groupName);
        this.getAllUser();
      }
    );
  }

}
