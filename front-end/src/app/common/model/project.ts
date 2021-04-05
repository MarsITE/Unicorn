import { User } from "./user";
import { Skill } from './skill';

export class Project {
    id: string;
    name: string;
    description: string;
    projectStatus: string;
    creationDate: string;
    owner: User;
    skills: Skill[];
  }
