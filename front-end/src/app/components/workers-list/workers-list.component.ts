import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {TokenHelper} from 'src/app/common/helper/token.helper';
import {UserHttpService} from 'src/app/common/services/user-http.service';
import {first} from "rxjs/operators";
import {ProjectWorker} from "../../common/model/project-worker";


@Component({
  selector: 'app-workers-list',
  templateUrl: './workers-list.component.html',
  styleUrls: ['./workers-list.component.scss']
})
export class WorkersListComponent implements OnInit {

  projectId: string;

  workers: ProjectWorker[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'isApprove', 'approve', 'delete'];

  constructor(
    private userService: UserHttpService,
    private router: ActivatedRoute,
    private router2: Router,
    private tokenHelper: TokenHelper,
    private toast: ToastrService
  ) {
    this.projectId = router.snapshot.params.projectId;

  }

  ngOnInit(): void {
    this.getWorkers();
  }

  private getWorkers(): void {
    this.userService.getProjectWorkers(this.projectId)
      .pipe(first())
      .subscribe(
        response => this.workers = response
      );
  }

  public showUserProfile(row: any): void {
    if (this.tokenHelper.getEmailFromToken() === row.email) {
      this.router2.navigateByUrl('profile');
    } else {
      this.router2.navigateByUrl(`profile/${row.id}`);
    }
  }

  public converToPlain(str: string): string {
    return str.replace('_', ' ').toLowerCase();
  }

  approve(worker: ProjectWorker) {
    this.userService.approveWorkerForProject(this.projectId, worker.userInfoProjectId)
      .pipe(first())
      .subscribe(() => this.getWorkers());
  }

  delete(userInfoProjectId: any) {
    this.userService.deleteWorkerForProject(this.projectId, userInfoProjectId)
      .pipe(first())
      .subscribe(() => this.getWorkers());
  }
}
