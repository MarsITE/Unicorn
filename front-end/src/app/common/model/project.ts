import {Skill} from './skill';
import {Worker} from './worker';

export class Project {
  id: string;
  name: string;
  description: string;
  projectStatus: string;
  creationDate: string;
  ownerId: string;
  ownerEmail: string;
  skills: Skill[] = [];
  workers: Worker[] = [];
}
