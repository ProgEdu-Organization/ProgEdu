import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { EditGroupManagementService } from './edit-group-management.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
@Component({
  selector: 'app-edit-group-management',
  templateUrl: './edit-group-management.component.html',
  styleUrls: ['./edit-group-management.component.scss']
})
export class EditGroupManagementComponent implements OnInit {
  @ViewChild('confirmModal', { static: true }) public confirmModal: ModalDirective;
  public groupName;
  public group;
  public users;
  public groupForm: FormGroup;
  public selectedUser;
  public confirmModalMsg;
  public mode;

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
        this.getAllUser();
      }
    );
  }

  editLeaderSubmit() {
    const leader = this.groupForm.get('leader').value;
    const groupName = this.groupForm.get('name').value;

    this.editGroupManagementService.editGroupLeader(groupName, leader).subscribe(
      response => {
        this.getGroup(groupName);
        this.getAllUser();
        this.confirmModal.hide();
      }
    );
  }

  setLeader() {
    this.groupForm.get('members').value.push(this.groupForm.get('leader').value);
    this.groupForm.get('members').setValue(this.groupForm.get('members').value.filter(item => item !== this.selectedUser));
    this.groupForm.get('leader').setValue(this.selectedUser);
    this.editLeaderSubmit();
  }

  removeGroupMemberByUsername() {

    this.editGroupManagementService.deleteGroupMember(this.groupName, this.selectedUser).subscribe(
      () => {
        this.getGroup(this.groupName);
        this.getAllUser();
        this.confirmModal.hide();
      }
    );
  }

  getAllUser() {
    this.editGroupManagementService.getAllUser().subscribe(response => {
      this.users = response.Users;
      console.log(this.users);
      // reGet the all user data and remove exist in  group merber
      const selectedUsers = this.groupForm.get('members').value;
      const selectedLeader = this.groupForm.get('leader').value;

      for (const i of selectedUsers) {
        if (i) {
          this.users = this.users.filter(item => {
            return item.username !== i[0] && item.username !== selectedLeader[0];
          });
        }
      }
    });
  }

  showConfirmModal(mode: string) {
    this.mode = mode;
    if (mode === 'add-member') {
      this.confirmModalMsg = `Do you add ${this.selectedUser.username} ${this.selectedUser.name} to group members`;
    } else if (mode === 'remove-member') {
      this.confirmModalMsg = `Do you remove ${this.selectedUser[0]} ${this.selectedUser[1]} from group members`;
    } else if (mode === 'set-leader') {
      this.confirmModalMsg = `Do you set ${this.selectedUser[0]} ${this.selectedUser[1]} to group leader`;
    }
    this.confirmModal.show();
  }
  // removeGroupMemberByUsername(groupForm.get('name').value,member);

  async addGroupMemberByUsername() {
    await this.editGroupManagementService.addGroupMemeber(this.groupName, this.selectedUser.username).subscribe(
      () => {
        this.getGroup(this.groupName);
        this.getAllUser();
        this.confirmModal.hide();
      }
    );
  }

}
